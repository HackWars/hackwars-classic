package HackerSearch.util;

/**
SearchHandler.java<br />
(c) Vulgate 2007<br />

Handles indexing Hacker pages in an inverse lookup
*/

import java.io.*;
import java.util.*;
import com.plink.dolphinnet.assignments.*;
import HackerSearch.Server.*;
import HackerSearch.Assignments.*;
import com.plink.dolphinstem.*;
import HackerSearch.Assignments.*;
import java.util.concurrent.Semaphore;
import org.w3c.dom.Node;
import util.*;

public class SearchHandler implements Runnable{
    //Data.
	public static final String STOP_WORD_LOCATION="stop_words.txt";
	
    private Time MyTime=null;//Central 
	
    private static final long sleepTime=50;//Time between interations of handling thread.
	private static final int ReturnCount=10;//How many results should be returned.
	
    private ArrayList ExecuteStack=new ArrayList();//Current tasks to execute in the handling thread.
	
    private Thread MyThread=null;//Central handling thread.
	private SearchServer MyServer=null;//Server for dispatching messages.
	private final Semaphore available = new Semaphore(1, true);//Semaphore for handling access to shared object.
	
	private InverseLookup MyInverseLookup=new InverseLookup();//An Inverse Lookup Table of Websites.
	private WordBinaryList DocumentFrequency=new WordBinaryList();//Used for IDF calculation.
	private SiteDataBinaryList SDBL=new SiteDataBinaryList();//Central
	
	private int currentlyIndexed=0;//Files currently indexed from the data directory into the search engine.
	private int fileCount=0;//Total files to index.
	private String fileList[];//List of file names to index.;
	private int count=0;
	
	private String Connection="127.0.0.1";
	private String DB="hackwars";
	private String Username="root";
	private String Password="";
        
	//A news instance of the search handler.
    public SearchHandler(SearchServer MyServer){
		this.MyTime=Time.getInstance();
		this.MyServer=MyServer;
		
		//Get file information.
		try{
			fileCount=0;
			sql C=new sql(Connection,DB,Username,Password);
			ArrayList result=null;
			String Q="select max(num) from user;";
			result=C.process(Q);	
			if(result!=null){
				fileCount=new Integer((String)result.get(0));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
        MyThread=new Thread(this);
        MyThread.start();
	}
	
	private static SearchHandler MySearchHandler=null;
	public static SearchHandler getInstance(SearchServer MyServer){
		if(MySearchHandler==null){
			MySearchHandler=new SearchHandler(MyServer);
		}
		
		return(MySearchHandler);
	}
	
	/**
	This threaded task when executed performs a single search.
	*/
	public class requestSearchTask implements Task{
		private SearchAssignment MySearchAssignment=null;
		private WordBinaryList EliminateDuplicate=new WordBinaryList();
		
		public requestSearchTask(SearchAssignment MySearchAssignment){
			this.MySearchAssignment=MySearchAssignment;
		}
		
		public void execute(){
		
		}
		
		public SearchResultAssignment executeBlocking(){
			SearchResultAssignment result=null;
			//Find results.
			WordBinaryList SearchTerms=MySearchAssignment.getVector();
			if(SearchTerms!=null){
				
				WordIndex Results[]=new WordIndex[SearchTerms.getData().size()];
				for(int i=0;i<SearchTerms.getData().size();i++){
					Results[i]=(WordIndex)MyInverseLookup.get(((WordData)SearchTerms.getData().get(i)).getData());
				}
				
				result=calculatePageRank(Results,SearchTerms);
				if(result==null)
					result=new SearchResultAssignment(0);
			}
			
			ArrayList Results=result.getResults();
			for(int i=0;i<Results.size();i++){
				SearchResult SR=(SearchResult)Results.get(i);
			}
			
			return(result);
		}
		
		private SearchResultAssignment calculatePageRank(WordIndex Results[],WordBinaryList SearchTerms){
		
			SearchResultAssignment returnMe=new SearchResultAssignment(MySearchAssignment.getID());
			for(int i=0;i<SearchTerms.getData().size();i++){
				returnMe.addSearchTerm(((WordData)SearchTerms.getData().get(i)).getData());
			}
			RankBinaryList SearchResults=new RankBinaryList();
						
			if(Results!=null){
			
				//String case.
				if(Results.length==0)
					return(returnMe);
					
				int Counters[]=new int[Results.length];
				SiteIndex Sites[]=new SiteIndex[Results.length];
				
				boolean stop=false;
				while(!stop){
					for(int i=0;i<Results.length;i++){
						if(Results[i]!=null){
							if(Counters[i]<Results[i].getSiteBinaryList().getData().size())
								Sites[i]=(SiteIndex)Results[i].getSiteBinaryList().getData().get(Counters[i]);
							else
								Sites[i]=null;
						}else
							Sites[i]=null;
					}
					
					SiteIndex Minimum=Sites[0];
					int minimumIndex=0;
					for(int i=1;i<Sites.length;i++){
						if(Minimum==null){
							Minimum=Sites[i];
							minimumIndex=i;
						}else if(Sites[i]!=null){
							if(Sites[i].getAddress().compareTo(Minimum.getAddress())<0){
								Minimum=Sites[i];
								minimumIndex=i;
							}
						}
					}
					
					if(Minimum==null)
						break;
						
					SiteIndex AddMe=Minimum.clone();
					int mcount=0;
					for(int i=0;i<Sites.length;i++){
						if(Sites[i]!=null){
							SiteIndex temp=Sites[i];
							if(temp.getAddress().equals(Minimum.getAddress())){
								if(i!=minimumIndex){
									AddMe.setRank(AddMe.getRank()+temp.getRank());
								}
								Counters[i]++;
								mcount++;
							}
						}
					}
					if(mcount==Sites.length){
						AddMe.setRank(AddMe.getRank()/3.0);
						SearchResults.add(AddMe);
					}
					
					stop=true;
					for(int i=0;i<Results.length;i++){
						if(Results[i]!=null)
							if(Counters[i]<Results[i].getSiteBinaryList().getData().size()){
								stop=false;
								break;
							}
					}
				}
			}
			
			ArrayList result=null;
			if(SearchResults.getData().size()-MySearchAssignment.getIndex()>0)
			for(int i=MySearchAssignment.getIndex();i<Math.min(ReturnCount,SearchResults.getData().size()-MySearchAssignment.getIndex())+MySearchAssignment.getIndex();i++){
				SiteIndex SI=((SiteIndex)SearchResults.getData().get(i));
				SiteData SD=(SiteData)SDBL.get(SI.getAddress());
				
				String title=SD.getTitle();
				String description=SD.getDescription();
				SearchResult SR=new SearchResult();
				SR.setAddress(SD.getAddress());
				SR.setTitle(title);
				SR.setDescription(description);
				returnMe.addResult(SR);
			}
			
			returnMe.setCurrent(MySearchAssignment.getIndex());
			returnMe.setSize(SearchResults.getData().size());
			
			return(returnMe);
		}
	}
	
	/**
	Request a search be peformed.
	*/
	public SearchResultAssignment  requestSearch(SearchAssignment MySearchAssignment){
		try{
			available.acquire();
			requestSearchTask RST=new requestSearchTask(MySearchAssignment);
			return(RST.executeBlocking());			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			available.release();
		}
		return(null);
	}
	
	/**
	Index a page in the search engine.
	*/
	public class indexPageTask implements Task{
		private String title=null;
		private String address=null;
		private String content=null;
		public indexPageTask(String title,String address,String content){
			this.title=title;
			this.address=address;
			this.content=content; 
		}
		
		public void execute(){
					
			//Strip the HTML.
			title=title.replaceAll("\\<.*?\\>","");
			title=title.replaceAll("\\&.*?;","");
			content=content.replaceAll("\\<.*?\\>","");
			content=content.replaceAll("\\&.*?;","");
		
			//Does an index of this page already exist?
			if(SDBL.get(address)!=null){
				SiteData SD=(SiteData)SDBL.get(address);
				WordBinaryList WBL=SD.getTerms();
				for(int i=0;i<WBL.getData().size();i++){
					WordData WD=(WordData)WBL.getData().get(i);
					WordIndex WI=(WordIndex)MyInverseLookup.get(WD.getData());
					SiteBinaryList SBL=WI.getSiteBinaryList();
					SiteIndex SI=(SiteIndex)SBL.get(SD.getAddress());
					WI.removeSite(SI);
					
					if(SBL.getData().size()==0)
						MyInverseLookup.remove(WD.getData());
					
					WordData DFWD=(WordData)DocumentFrequency.get(WD.getData());
					if(DFWD!=null){
						
						DFWD.setFrequency(DFWD.getFrequency()-1.0);
						if(DFWD.getFrequency()<=0.0){
							DocumentFrequency.remove(WD.getData());
						}
						
					}
						
				}
				SDBL.remove(address);
			}
			
			//Extract the terms.
			WordBinaryList WBL=null;
			ArrayList PrimeMe=new ArrayList();
			PrimeMe.add(content);
			TextSource Prime=new TextSource("source",PrimeMe);
			Prime.setStopWordFile(STOP_WORD_LOCATION);
			Prime.prime();
			ArrayList temp=Prime.getItems();
		
			if(temp!=null)
				if(temp.size()>0)
					WBL=((ItemData)temp.get(0)).getVectorData();
					
			//Add this list to the document vector.
			if(WBL!=null){
				DocumentFrequency.addList(WBL);

				
				//Calculate the ranks of words based on DF-IDF.
				float tCount=0.0f;
				for(int i=0;i<WBL.getData().size();i++){//Calculate frequency of each term.
					tCount+=((WordData)WBL.getData().get(i)).getFrequency();
				}
				
				WordBinaryListByRank WBLRanked=new WordBinaryListByRank();
				for(int i=0;i<WBL.getData().size();i++){//Re-Calculate rank for each term.
					WordData WD=(WordData)WBL.getData().get(i);
					double DF=WD.getFrequency()/tCount;
					WordData WDIDF=(WordData)DocumentFrequency.get(WD.getData());
					double IDF=Math.log((WDIDF.getFrequency()*currentlyIndexed));
					WD.setFrequency(DF*IDF);
					WBLRanked.add(WD);
				}
				
				//Use the 64 best key-words in the index.
				WBL=new WordBinaryList();
				for(int i=0;i<WBLRanked.getData().size();i++){
					WBL.add((WordData)WBLRanked.getData().get(i));
					if(i==512)
						break;
				}
												
				//Now insert this data into our inverse index.
				SiteData SD=new SiteData();
				String description=content;
				if(description.length()>=128)
					description=description.substring(0,128)+"...";
				SD.setDescription(description);
				SD.setTitle(title);
				SD.setAddress(address);
				SD.setTerms(WBL);
				SDBL.add(SD);
				
				for(int i=0;i<WBL.getData().size();i++){//Insert each word into the inverse lookup.
					WordData WD=(WordData)WBL.getData().get(i);
					SiteIndex SI=new SiteIndex();
					SI.setAddress(address);
					SI.setRank(WD.getFrequency());
					WordIndex WI=(WordIndex)MyInverseLookup.get(WD.getData());
					RankBinaryList RBL=null;
					SiteBinaryList SBL=null;
					
					if(WI!=null){					
					
						RBL=WI.getRankBinaryList();
					
						while(RBL.get(SI.getRank())!=null){
							SI.setRank(SI.getRank()+0.0000001);
						};
						
						SBL=WI.getSiteBinaryList();
						
						WI.addSite(SI);
					}else if(MyInverseLookup.getData().size()<100000){
						WordIndex NewWI=new WordIndex(WD.getData(),"");
						NewWI.addSite(SI);
						MyInverseLookup.add(NewWI);
					}
						
				}
			}

		}
	}
	
	/**
	Index a page in the search engine.
	*/
	public void indexPage(String title,String address,String content){
		try{
			available.acquire();
			ExecuteStack.add(new indexPageTask(title,address,content));
			currentlyIndexed++;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			available.release();
		}
	}
	
    /**
    Fetch execution tasks from the stack.
    */
    public void run(){
        while(true){
            long startTime=MyTime.getCurrentTime();
			try{
			
			available.acquire();
            Iterator MyIterator=ExecuteStack.iterator();
			
			//Check for users to load.
            while(MyIterator.hasNext()){
				Task MyTask=(Task)MyIterator.next();
				MyTask.execute();
				MyIterator.remove();
            }
			
			available.release();
			
			//Bootstrap our index of pages if the indexing server has been reset.
			if(currentlyIndexed<fileCount+1){
				try{
					LoadXML LX=new LoadXML();
					String Data="";
					String ip="";
					sql C=new sql(Connection,DB,Username,Password);
					ArrayList result=null;
					String Q="select stats,ip from user where num="+(currentlyIndexed+1)+";";
					result=C.process(Q);	
					if(result!=null){
						Data=(String)result.get(0);
						ip=(String)result.get(1);
					}
					Q = "SELECT TO_DAYS(NOW())-TO_DAYS(last_logged_in),npc FROM hackerforum.users WHERE ip='"+ip+"'";
					result = C.process(Q);
					if(((int)Integer.parseInt((String)result.get(0))<14)||((String)result.get(1)).equals('Y')){
						LX.loadByteArray(Data.getBytes());
						
						String address="";
						String title="";
						String content="";
						
						//Get the address of this file.
						Node N=LX.findNodeRecursive("ip",0);
						N=LX.findNodeRecursive(N,"#text",0);
						if(N!=null)
							address=N.getNodeValue();
							
						//Get the title.
						N=LX.findNodeRecursive("title",0);
						N=LX.findNodeRecursive(N,"#text",0);
						if(N!=null)
							title=N.getNodeValue();
							
						//Get the content.
						N=LX.findNodeRecursive("body",0);
						N=LX.findNodeRecursive(N,"#text",0);
						if(N!=null)
							content=N.getNodeValue();
																			
						content=content.replaceAll("\\<.*?\\>","");
						content=content.replaceAll("\\&.*?;","");
						content=content.replaceAll("\\n","");
						
						
						indexPage(title,address,content.toLowerCase());
											
						//Get the file list out of memory.
						if(currentlyIndexed==fileCount)
							fileList=null;
						if(count%100==0)
							System.out.println("Current Index: "+count);
						count++;
					}
					else{
						if(count%100==0)
							System.out.println("Current Index: "+count);
						currentlyIndexed++;
						count++;
					}
					C.close();
				}catch(Exception e){
					currentlyIndexed++;
					count++;
				}
			}
			
			}catch(Exception e){

			}finally{
				available.release();
			}
            
			
			
            try{
             long endTime=MyTime.getCurrentTime();
             if(sleepTime-(endTime-startTime)>0)
                MyThread.sleep(sleepTime-(endTime-startTime));
            }catch(Exception e){

            }
        }
    }
	
	/**
	Have we loaded all the files in from the saves directory.
	*/
	public boolean getLoaded(){
		if(currentlyIndexed>=fileCount&&ExecuteStack.size()==0)
			return(true);
		return(false);
	}
}

