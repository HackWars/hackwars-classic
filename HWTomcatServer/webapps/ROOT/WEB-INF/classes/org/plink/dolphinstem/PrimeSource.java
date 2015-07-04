package com.plink.dolphinstem;
import org.tartarus.snowball.*;
import java.lang.reflect.Method;
import java.util.*;
import java.io.*;
/**
<b>
DolphinNet (2006)<br />
Benjamin E. Coe<br /><br />
</b>
Retrieves a news source and preforms stop word removal and stemming. This class must be
inherited and extended to reflect the format of the news source we wish to access.
*/
abstract public class PrimeSource implements Serializable{
	//Data.
	private ArrayList Data=null;
	private ArrayList Items=null;
	private WordBinaryList StopWords=null;
	private boolean complete=true;
	private String StopWordFile="temp.txt";
	//Stemmer data.
	private String StemmerClass="org.tartarus.snowball.englishStemmer";

	////////////////////////
	// Constructor.
	public PrimeSource(String source){
		Items=new ArrayList();
		StopWords=new WordBinaryList();
		//Get the working data.
		Items=fetchItems(source);
	}
	
	public PrimeSource(String source,ArrayList Data){
		this.Data=Data;
		Items=new ArrayList();
		StopWords=new WordBinaryList();
		//Get the working data.
		Items=fetchItems(source);
	}

	////////////////////////
	// Getters.
	public ArrayList getData(){
		return(Data);
	}
	public ArrayList getItems(){
		return(Items);
	}
	public boolean getComplete(){
		return(complete);
	}
	////////////////////////
	// Setters.
	public void setComplete(boolean complete){
		this.complete=complete;
	}

	public void setStopWordFile(String StopWordFile){
		this.StopWordFile=StopWordFile;
	}
	////////////////////////
	// Methods.
	/**Preform stop word removal and stemming.*/
	public void prime(){
		loadStopWords(StopWordFile);

		if(Items==null){
			complete=false;
			return;
		}

		//Set up Stemmer.
		Class stemClass=null;
		SnowballProgram stemmer=null;
		Method stemMethod=null;
		try{
			stemClass = Class.forName(StemmerClass);
			stemmer = (SnowballProgram) stemClass.newInstance();
			stemMethod = stemClass.getMethod("stem", new Class[0]);
		}catch(Exception e){
			e.printStackTrace();
			setComplete(false);
			return;
		}

		ArrayList data=getItems();
		for(int i=0;i<data.size();i++){
			ItemData temp=(ItemData)data.get(i);
			String primeText=temp.getTitle()+" "+temp.getDescription()+" ";
			WordBinaryList VectorData=new WordBinaryList();
			//Run the algorithms.
			Object [] emptyArgs = new Object[0];
			String input="";
			for(int ii=0;ii<primeText.length();ii++){
			
				char ch = primeText.charAt(ii);
				ch=Character.toLowerCase(ch);
				
				String mod=null;
				if(ch==0xE0||ch==0xE1||ch==0xE2||ch==0xE3||ch==0xE4||ch==0xE5||ch==0xC0||ch==0xC1||ch==0xC2||ch==0xC3||ch==0xC4||ch==0xC5)
					ch='a';
					
				if(ch==0xDF){
					ch='s';
					mod="ss";
				}
					
				if(ch==0xC6||ch==0xE6){
					ch='a';
					mod="ae";
				}
				
				if(ch==0xC7||ch==0xE7)
					ch='c';
					
				if(ch==0xD0||ch==0xF0)
					ch='d';
					
				if(ch==0xD1||ch==0xF1)
					ch='n';
					
				if(ch==0xDD||ch==0xDE||ch==0xFD||ch==0xFE||ch==0xFF)
					ch='y';
					
				if(ch==0xC8||ch==0xC9||ch==0xCA||ch==0xCB||ch==0xE8||ch==0xE9||ch==0xEA||ch==0xEB)
					ch='e';
					
				if(ch==0xCC||ch==0xCD||ch==0xCE||ch==0xCF||ch==0xEC||ch==0xED||ch==0xEE||ch==0xEF)
					ch='i';
					
				if(ch==0xD2||ch==0xD3||ch==0xD4||ch==0xD5||ch==0xD6||ch==0xD8||ch==0xF2||ch==0xF3||ch==0xF4||ch==0xF5||ch==0xF6||ch==0xF8)
					ch='o';
					
				if(ch==0xD9||ch==0xDA||ch==0xDB||ch==0xDC||ch==0xF9||ch==0xFA||ch==0xFB||ch==0xFC)
					ch='u';
				
				if((!((ch>='a'&&ch<='z')||(ch>='0'&&ch<='9')))&&ch!='\''){
					ch=' ';
				}
					
				if (Character.isWhitespace((char) ch)) {
				if (input.length()>0&&input.length()<16){
					if(StopWords.get((Object)input)==null){//Check for stop words.
						try{
							
							stemmer.setCurrent(input);
							stemMethod.invoke(stemmer, emptyArgs);

							WordData word=null;
							String inputMod="";
							Object o=stemmer.getCurrent();
							if(o!=null)
								inputMod=(String)o;
								
							if((word=(WordData)VectorData.get(inputMod))!=null)
								word.setFrequency(word.getFrequency()+1.0f);
							else{
								if(inputMod.length()>0){
									VectorData.add(new WordData(inputMod,1.0f));
								}
							}


						}catch(Exception e){
							e.printStackTrace();
							temp.setComplete(false);
						}
					}
				}
				input="";
				} else {
					if(mod==null)
						input+=ch;
					else
						input+=mod;
				}
			}
			///////////
			temp.setVectorData(VectorData);
		}
		resetStopWords();
	}

	/** Load a new stop word list from the file provided.*/
	public void loadStopWords(String file){
		try{
			BufferedReader in = new BufferedReader(new FileReader(new File(file)));
			String word=null;
			while((word=in.readLine())!=null)
				StopWords.add(new WordData(word,1.0));
			in.close();
		}catch(Exception e){
			complete=false;
			e.printStackTrace();
		}
	}
	/** Reset the stop word list.*/
	public void resetStopWords(){
		StopWords=null;
	}

	/** Fetch the news items in the form of an ArrayList.*/
	abstract public ArrayList fetchItems(String source);
}