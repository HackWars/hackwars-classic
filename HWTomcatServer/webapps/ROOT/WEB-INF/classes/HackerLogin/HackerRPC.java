package HackerLogin;
/**
HackerSearch.java

HackerRPC provides a system for performing remote function calls. This is good for attaching PHP to the server, 
It is also good for performing some central tasks like search engine indexing, etc.
*/
import util.zip;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.plink.dolphinnet.*;
import HackerSearch.Assignments.*;
import HackerSearch.util.*;
import HackerSearch.Server.*;
import java.util.ArrayList;
import java.util.HashMap;
import Assignments.*;
import util.*;
import Hackscript.Model.*;
import java.util.concurrent.Semaphore;
import Game.*;
import Assignments.*;
import Server.*;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import java.net.URL;

public class HackerRPC extends HttpServlet implements DataHandler{
	
	private boolean RESPONSE=false;//Has a response been received.
	private static final long TIME_OUT=5000;//How long should we wait for a connection.
	private static final long RESPONSE_TIME_OUT=15000;//How long should we wait for a response.
	private Time MyTime=Time.getInstance();
	private int RESULTS_PER_PAGE=10;
	
	public HackerRPC(){
	
	}
	
	/**
	An XML rpc call that peforms a search.
	*/
	public String doSearch(String query,String spage){
		String out="";
		String ip="localhost";
		SearchResultAssignment Result=null;
		
		if(query!=null&&!query.equals("")){
		
			//Get the query.
			int page=0;
			if(spage!=null&&!spage.equals(""))
				page=new Integer(spage);
			SearchServer MySearchServer=SearchServer.getInstance();
			int STAMP=(int)MyTime.getCurrentTime();
			SearchAssignment SA=new SearchAssignment(STAMP);
			SA.setIndex(page*RESULTS_PER_PAGE);
			SA.setVector(query);

			Result=MySearchServer.requestSearch(SA);
		}
	
	    //The search response, edit this for a new look.
		out+="<html>";
		out+="<head>";
		out+="<title>Hacker Search</title>";
		out+="</head>";
		out+="<body style=\"padding:5px;background-color:#313230;width:100%;height:100%\">";
		out+="<img width=\"200\" height=\"81\" src=\"http://www.hackwars.net/images/logo.gif\" /><br /><br />";
		//out+="<img width=\"120\" height=\"80\" src=\"http://www.hackwars.net/images/logo.jpg\" /><br /><br />";

		out+="<b style=\"color:white;size:15px;\">Use the hacker search engine to find the websites of players and NPCs. You can use these websites to purchase items, plan attacks, and to move one step closer to global domination.</b><br /><br />";

		out+="<form action=\"search.html\"><input type=\"text\" name=\"query\"><input type=\"submit\" value=\"Search\"></form><br />";
	 
		//Output results.
		if(Result!=null&&query!=null){
			ArrayList Results=Result.getResults();
			boolean divider=false;
			
			if(Result.getCurrent()>RESULTS_PER_PAGE-1){
				out+="<a style=\"color:white;\" href=\"?query="+query+"&page="+(Result.getCurrent()/RESULTS_PER_PAGE-1)+"\"> Previous </a>";
				divider=true;
			}
			
			if(Result.getCurrent()+RESULTS_PER_PAGE<Result.getSize()){
				if(divider)
					out+="<a style=\"color:white;\"> | </a>";
				out+="<a style=\"color:white;\" href=\"?query="+query+"&page="+(Result.getCurrent()/RESULTS_PER_PAGE+1)+"\"> Next </a>";
			}
			
			if(Results!=null&&Results.size()>0)
				out+="<a style=\"color:white\">("+(Result.getCurrent()+1)+" to "+Math.min(Result.getSize(),Result.getCurrent()+RESULTS_PER_PAGE)+" of "+Result.getSize()+".)</a>";
			else
				out+="<a style=\"color:white\">No results found.</a>";
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			try{
				config.setServerURL(new URL("http://www.hackwars.net/xmlrpc/domain.php"));
			}catch(Exception e){}
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);
			
			for(int i=0;i<Results.size();i++){
				SearchResult SR=(SearchResult)Results.get(i);
				Object[] send = {SR.getAddress()};
				String address=SR.getAddress();
				try{
				address = (String)client.execute("reverseLookup", send);
				}catch(Exception e){}
				out+="<div style=\"padding:5px;background-color:white;\">";
				
				out+="<b style=\"color:blue;size:18px;\"><a href=\""+address+"\">"+SR.getTitle()+"</a></b><br /><b style=\"color:black;size:15px;\">";
				out+=SR.getDescription()+"</b><br />";
				out+="<i style=\"color:green;font-size:13px;\">"+address+"</i>";
				out+="</div><br />";
			}
			
			if(Result.getCurrent()>RESULTS_PER_PAGE-1){
				out+="<a style=\"color:white;\" href=\"?query="+query+"&page="+(Result.getCurrent()/RESULTS_PER_PAGE-1)+"\"> Previous </a>";
				divider=true;
			}
				
			if(Result.getCurrent()+RESULTS_PER_PAGE<Result.getSize()){
				if(divider)
					out+="<a style=\"color:white;\"> | </a>";
				out+="<a style=\"color:white;\" href=\"?query="+query+"&page="+(Result.getCurrent()/RESULTS_PER_PAGE+1)+"\"> Next </a>";
			}
			
			if(Results!=null&&Results.size()>0)
				out+="<a style=\"color:white\">("+(Result.getCurrent()+1)+" to "+Math.min(Result.getSize(),Result.getCurrent()+RESULTS_PER_PAGE)+" of "+Result.getSize()+".)</a>";
		}
	
		
		out+="<br /><br /><b style=\"color:white;font-size:13px;\">&copy; Hack Wars 2007.</b>";
		out+="</body>";
		out+="</html>";//End of search response.
		Result=null;
		System.gc();
		return(out);
	}
	
	/**
	The main servlet entry-point that can perform a search.
	*/
	public void doGetMultiple(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
	{
		String password = null;
		try {
			BufferedReader BR = new BufferedReader(new FileReader("password.ini"));
			password = BR.readLine();
		} catch (Exception e) {
		//	e.printStackTrace();
		}
		
		PrintWriter out = response.getWriter();
		String mode=request.getParameter("mode");
		if(mode==null){//Serve an account.
			response.setContentType("application/xml");
			
			String ip=request.getParameter("ip");
			String serverID=request.getParameter("serverID");
			
		
			if (password == null || password.equals(request.getParameter("pass")))
			if(ip!=null&&serverID!=null){
				CheckOutHandler COH=CheckOutSingleton.getInstance();
				boolean activeBoolean=false;
				String active=request.getParameter("active");
				if(active!=null&&!active.equals(""))
					activeBoolean=true;
				out.print(COH.checkOutPlayer(ip,serverID,activeBoolean));
			}
		}else{//Serve a search engine.
		
			//The main search engine page, for when no results are currently being displayed (edit this for new look and feel).
			String ip="localhost";
			String query=request.getParameter("query");
			SearchResultAssignment Result=null;
			response.setContentType("text/html");
					
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Hacker Search</title>");
			out.println("</head>");
			out.println("<body style=\"padding:5px;background-color:#313230;width:100%;height:100%\">");
			out.println("<img width=\"200\" height=\"81\" src=\"http://www.hackwars.net/images/logo.gif\" /><br /><br />");
			out.println("<b style=\"color:white;size:15px;\">Use the hacker search engine to find the websites of players and NPCs. You can use these websites to purchase items, plan attacks, and to move one step closer to global domination.</b><br /><br />");

			out.println("<form action=\"http://"+ip+":8081/search.html\" method=\"get\"><input type=\"text\" name=\"query\"><input type=\"submit\" value=\"Search\"></form><br />");
		 
			//Output results.
			if(Result!=null&&query!=null){
				ArrayList Results=Result.getResults();
				boolean divider=false;
				
				if(Result.getCurrent()>RESULTS_PER_PAGE-1){
					out.println("<a style=\"color:white;\" href=\"?query="+query+"&page="+(Result.getCurrent()/RESULTS_PER_PAGE-1)+"\"> Previous </a>");
					divider=true;
				}
				
				if(Result.getCurrent()+RESULTS_PER_PAGE<Result.getSize()){
					if(divider)
						out.println("<a style=\"color:white;\"> | </a>");
					out.println("<a style=\"color:white;\" href=\"?query="+query+"&page="+(Result.getCurrent()/RESULTS_PER_PAGE+1)+"\"> Next </a>");
				}
				
				if(Results!=null&&Results.size()>0)
					out.println("<a style=\"color:white\">("+(Result.getCurrent()+1)+" to "+Math.min(Result.getSize(),Result.getCurrent()+RESULTS_PER_PAGE)+" of "+Result.getSize()+".)</a>");
				else
					out.println("<a style=\"color:white\">No results found.</a>");

				for(int i=0;i<Results.size();i++){
					out.println("<div style=\"padding:5px;background-color:white;\">");
					SearchResult SR=(SearchResult)Results.get(i);
					out.println("<b style=\"color:blue;size:18px;\"><a href=\""+SR.getAddress()+"\">"+SR.getTitle()+"</a></b><br /><b style=\"color:black;size:15px;\">");
					out.println(SR.getDescription()+"</b><br />");
					out.println("<i style=\"color:green;font-size:13px;\">"+SR.getAddress()+"</i>");
					out.println("</div><br />");
				}
				
				if(Result.getCurrent()>RESULTS_PER_PAGE-1){
					out.println("<a style=\"color:white;\" href=\"?query="+query+"&page="+(Result.getCurrent()/RESULTS_PER_PAGE-1)+"\"> Previous </a>");
					divider=true;
				}
					
				if(Result.getCurrent()+RESULTS_PER_PAGE<Result.getSize()){
					if(divider)
						out.println("<a style=\"color:white;\"> | </a>");
					out.println("<a style=\"color:white;\" href=\"?query="+query+"&page="+(Result.getCurrent()/RESULTS_PER_PAGE+1)+"\"> Next </a>");
				}
				
				if(Results!=null&&Results.size()>0)
					out.println("<a style=\"color:white\">("+(Result.getCurrent()+1)+" to "+Math.min(Result.getSize(),Result.getCurrent()+RESULTS_PER_PAGE)+" of "+Result.getSize()+".)</a>");
			}
		
			
			out.println("<br /><br /><b style=\"color:white;font-size:13px;\">&copy; Hack Wars 2007.</b>");
			out.println("</body>");
			out.println("</html>");
			Result=null;
		}
		out.close();
	}
	
	/**
	The main entry point of the servlet, keeping in mind this file also has XML-RPC specific stuff.
	*/
    public synchronized void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
		try{
			
			try{
				doGetMultiple(request,response);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			try{
				Thread.sleep(250);
			}catch(Exception e){
				e.printStackTrace();
			}
			System.gc();
		}catch(Exception e){
			e.printStackTrace();
		}
    }
	
	/**
	At one point this XML-RPC call was used to create a profile. now depricated.
	*/
	public String createProfile(String ip,String username){
		CheckOutHandler COH=CheckOutSingleton.getInstance();
		COH.createPlayer("awa878",ip,username);
		return("Success.");
	}
	
	/**
	An XML-RPC entry point that allows a facebook remote app to depost.
	This can be potentially used if we make other mobile apps.
	*/
	public String facebookDeposit(String ip,Double amount,Integer port){
		ServerConnectionSingleton Server=ServerConnectionSingleton.getInstance();
		Object O[]=new Object[]{ip,new Float(amount.floatValue()),port};
		RemoteFunctionCall RFC=new RemoteFunctionCall(0,null,O);
		RFC.setFunction("facebookdeposit");
		Server.returnAssignment(RFC);
		
		return("IP: "+ip+" Amount:"+amount+"Port: "+port);
	}
	
	/**
	An XML-RPC entry point that allows a facebook remote app to transfer.
	This can be potentially used if we make other mobile apps.
	*/
	public String facebookTransfer(String ip1,String ip2,Double amount,Integer port){
	
		ServerConnectionSingleton Server=ServerConnectionSingleton.getInstance();
		Object O[]=new Object[]{ip1,ip2,new Float(amount.floatValue()),port};
		RemoteFunctionCall RFC=new RemoteFunctionCall(0,null,O);
		RFC.setFunction("facebooktransfer");

		Server.returnAssignment(RFC);
		
		return("Success!");
	}
	
	/**
	An XML-RPC entry point that allows a facebook remote app to withdraw.
	This can be potentially used if we make other mobile apps.
	*/
	public String facebookWithdraw(String ip,Double amount,Integer port){
	
		ServerConnectionSingleton Server=ServerConnectionSingleton.getInstance();
		Object O[]=new Object[]{ip,new Float(amount.floatValue()),port};
		RemoteFunctionCall RFC=new RemoteFunctionCall(0,null,O);
		RFC.setFunction("facebookwithdraw");
		Server.returnAssignment(RFC);
		
		return("Success!");
	}
	
	/**
	An XML-RPC entry point that allows a facebook remote app to request an update.
	This can be potentially used if we make other mobile apps.
	*/
	public String facebookUpdate(String ip){
		ServerConnectionSingleton Server=ServerConnectionSingleton.getInstance();
		Object O[]=new Object[]{ip};
		RemoteFunctionCall RFC=new RemoteFunctionCall(0,null,O);
		RFC.setFunction("facebookupdate");
		Server.returnAssignment(RFC);
		
		return("Success!");
	}
	
	/**
	This XML-RPC call requests a website from the server, this will actually cause scripts to
	run, etc.
	*/
	private static int requestCounter=0;
	private static HashMap PageReturn=new HashMap();
	public String requestWebsite(String ip,Object Keys[],Object Values[]){
		System.out.println("Requested Website");
		HashMap Parameters=new HashMap();
		String function="";
		System.out.println(Keys.length);
		for(int i=0;i<Keys.length;i++){
			System.out.println(Keys[i]+">>>"+Values[i]);
			if(i!=0)
				Parameters.put((String)Keys[i],(String)Values[i]);
			else
				function=(String)Values[i];
		}
		System.out.println("Function: "+function);
		System.out.println("IP: "+ip);
		
		
		Object O[]=new Object[]{ip,"062.153.7.142",Parameters};
		RemoteFunctionCall RFC=new RemoteFunctionCall(0,null,O);
		RFC.setFunction(function);
		System.out.println("Starting Server Connection");
		ServerConnectionSingleton Server=ServerConnectionSingleton.getInstance();
		int requestCheck=requestCounter;
		RFC.setID(requestCheck);
		requestCounter++;
		Integer key=new Integer(requestCheck);
		PageReturn.put(key,null);
		System.out.println("Sending Request to server");
		Server.returnAssignment(RFC);//Dispatch the request to the server.

		int breakCount=0;
		String data="";
		while((data=(String)PageReturn.get(key))==null){
			try{
				Thread.sleep(1);
			}catch(Exception e){
			
			}
			if(breakCount>10000)
				break;
				
			breakCount++;
			
		}
		System.out.println("Received Response from server");
		if(data.equals(""))
			data="Maximum timeout reached.";
		//System.out.println(data);
		return(data);
	}
	
	/**
	The XML-RPC call for returning a page requested.
	*/
	public String returnWebsite(String title,String body,Integer ID){
		PageReturn.put(ID,zip.unzipString(body));
		return("sucess.");
	}
			
	/**
	Used to put the server into a locked down state.
	*/
	public String shutdownServer(String username,Integer id,String server){
		if(username.equals("bcoe")&&(int)id==850335){
				Reporter R=new Reporter(server,200000,10021,10020);

				R.setDataHandler(this);
				connect(R,"","","",false);
				
				long startTime=MyTime.getCurrentTime();
				
				while(!RESPONSE){
					if(MyTime.getCurrentTime()-startTime>RESPONSE_TIME_OUT){
						return("Failure.");
					}
				}
				
				R.addFinishedAssignment(new PingAssignment(850335,"bcoe"));
				return("Server shutdown message sent.");
		}else
			return("Failure");
	}
	
	/**
	Remote function call to save an account, this is used when the website needs to be re-indexed.
	*/
	public String saveProfile(String password,String ip,String content,Boolean pageUpdated,String pageTitle,String pageBody){
		if(!password.equals("asdbas0d98a0sd9fa8sasdlbo")){
			return("failure");
		}else{
			try{
			if(pageUpdated){
				SearchServer MySearchServer=SearchServer.getInstance();
				MySearchServer.returnAssignment(new IndexPageAssignment(0,pageTitle,ip,pageBody));
			}
			
			CheckOutHandler COH=CheckOutSingleton.getInstance();
			COH.returnPlayer(ip,content);
			
			}catch(Exception e){
				return(e.toString());
			}
		}
		return("success");
	}

	/**
	Entry point for Servlet.
	*/
    public synchronized void doPost(HttpServletRequest request,HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
	}
	
	/**
	Connect to the search server and tell the server to shutdown.
	*/
	public void connect(Reporter R,String title,String address,String content,boolean sendPacket){

		boolean success=true;
		long startTime=MyTime.getCurrentTime();
		
		while(R.getID()==-1){
			if(MyTime.getCurrentTime()-startTime>TIME_OUT){
				success=false;
				break;
			}
		}
		
		if(success){
			if(sendPacket)
				R.addFinishedAssignment(new IndexPageAssignment(0,title,address,content));
			RESPONSE=true;
		}else
			RESPONSE=false;
	}
	
	//Interface implementation.
	
	/**
	Called by Data handler to return assignments to front-end.
	*/
	public void addData(Object o){
		if(o instanceof PingAssignment){
			RESPONSE=true;
		}
	}
	
	//Un-used stuff from the abstract class we implement.
	public void addFinishedAssignment(Assignment A){};
	public Object getData(int i){return(null);}
	public void resetData(){};
	
	/**
	Compile the application.
	*/
	//This chart provides a list of APIS plus the associated level requirements, plus the associated costs.
	public static final String Bank[]={"lowerDeposit","mediumDeposit","higherDeposit","withdraw","lowerTransfer","mediumTransfer","higherTransfer","message","getAmount","getSourceIP","getTargetIP","checkPettyCash","getMaliciousIP","checkPettyCashTarget","greaterDeposit","greaterTransfer","playSound","logMessage"};
	public static final int BankLevel[]={1,25,75,1,1,25,75,1,1,1,1,25,15,25,95,95,10,1};
	public static final float BankCPUCost[]={1.0f,5.0f,10.0f,1.0f,1.0f,5.0f,10.0f,1.0f,1.0f,1.0f,1.0f,5.0f,2.0f,5.0f,20.0f,20.0f,1.0f,1.0f};
	public static final float BankPrice[]={10.0f,1500.0f,6000.0f,10.0f,10.0f,1500.0f,6000.0f,10.0f,10.0f,10.0f,10.0f,1500.0f,1500.0f,1500.0f,100000.0f,100000.0f,500.0f,10.0f};
	public static final String BankKeys[]=new String[]{"deposit","withdraw","transfer"};
	public static final float bankbase=10.0f;
	
	public static final String Attack[]={"message","switchAttack","getCPULoad","cancelAttack","underAttack","getSourcePort","getTargetPort","getTargetIP","getHP","installScript","emptyPettyCash","showChoices","checkPettyCash","getMaximumCPULoad","checkPettyCashTarget","destroyWatches","checkForWatch","getTargetCPUCost","getIterations","getTargetHP","getSourceIP","berserk","zombie","isZombie","deleteLogs","getMaliciousIP","playSound","freeze","editLogs","logMessage","changeDailyPay","stealFile"};
	public static final int AttackLevel[]={1,50,10,10,10,5,5,5,25,15,15,1,60,25,60,95,50,85,15,35,1,40,60,60,45,1,10,65,55,1,15,70};
	public static final float AttackCPUCost[]={1.0f,10.0f,2.0f,2.0f,2.0f,1.0f,1.0f,1.0f,5.0f,5.0f,5.0f,1.0f,8.0f,2.0f,8.0f,25.0f,5.0f,15.0f,2.0f,3.0f,1.0f,5.0f,0.0f,0.0f,4.0f,1.0f,1.0f,10.0f,4.0f,1.0f,5.0f,7.0f};
	public static final float AttackPrice[]={5.0f,1000.0f,10.0f,50.0f,50.0f,10.0f,10.0f,10.0f,500.0f,250.0f,250.0f,5.0f,750.0f,500.0f,750.0f,300000.0f,2000.0f,10000.0f,150.0f,1500.0f,5.0f,1500.0f,2000.0f,500.0f,1000.0f,50.0f,500.0f,2500.0f,1500.0f,5.0f,250.0f,3000.0f};
	private static String AttackKeys[]=new String[]{"continue","initialize","finalize"};
	public static final float attackbase=20.0f;
	
	public static final String Redirect[]={"redirectDuctTape","redirectGermanium","redirectSilicon","redirectYBCO","redirectPlutonium"};
	public static final int RedirectLevel[]={1,25,45,75,85};
	public static final float RedirectCPUCost[]={1.0f,5.0f,10.0f,15.0f,20.0f};
	public static final float RedirectPrice[]={20.0f,1500.0f,6000.0f,50000.0f,150000.0f};
	private static String RedirectKeys[]=new String[]{"continue","initialize","finalize"};
	public static final float redirectbase=20.0f;
	
	public static final String FTP[]={"put","get","message","getTargetIP","getMaliciousIP","getFileName","getFileType","getFilePrice","playSound","logMessage"};
	public static final int FTPLevel[]={1,1,1,1,1,1,1,1,1,};
	public static final float FTPCPUCost[]={2.0f,2.0f,2.0f,2.0f,2.0f,2.0f,2.0f,2.0f,1.0f,2.0f};
	private static final float FTPPrice[]={50.0f,50.0f,50.0f,50.0f,500.0f,800.0f,1600.0f,3000.0f,50.0f,50.0f};
	private static String FTPKeys[]=new String[]{"put","get"};
	public static final float ftpbase=10.0f;
	
	public static final String Watch[]={"checkForFireWall","counterattack","checkPettyCash","switchFireWall","switchAnyFireWall","depositPettyCash","checkFireWall","shutDownPorts","shutDownPort","heal","getTargetPort","message","getTargetIP","getSearchFireWall","getSourceIP","counterattackBank","counterattackAttack","getDefaultAttack","logMessage","turnOnPort","turnOnPorts","getDefaultBank","getDefaultFTP","getDefaultHTTP","getPort","cancelAttack","sendEmail","playSound","sendFacebookMessage","transferMoney","shutDownWatch","turnOnWatch","scan","getTriggerParameter","getTransactionAmount","getCPULoad","getMaximumCPULoad","isTriggered","attack","zombieAttack"};
	public static final int WatchLevel[]={15,50,25,15,10,95,5,80,75,75,1,1,1,20,1,60,60,50,1,75,80,60,60,60,1,50,35,10,1,40,65,65,45,35,5,10,20,35,50,55};
	public static final float WatchCPUCost[]={3.0f,10.0f,4.0f,3.0f,5.0f,15.0f,2.0f,10.0f,10.0f,15.0f,1.0f,1.0f,1.0f,2.0f,1.0f,12.0f,12.0f,2.0f,1.0f,10.0f,15.0f,2.0f,2.0f,2.0f,1.0f,4.0f,1.0f,1.0f,1.0f,5.0f,8.0f,8.0f,5.0f,2.0f,1.0f,2.0f,2.0f,2.0f,10.0f,10.0f};
	public static final float WatchPrice[]={250.0f,10000.0f,250.0f,250.0f,500.0f,100000.0f,100.0f,5000.0f,2000.0f,10000.0f,250.0f,50.0f,250.0f,500.0f,250.0f,15000.0f,15000.0f,10000.0f,50.0f,2000.0f,5000.0f,1000.0f,1000.0f,1000.0f,250.0f,2000.0f,1000.0f,500.0f,250.0f,2000.0f,2000.0f,2000.0f,2500.0f,1000.0f,250.0f,250.0f,1000.0f,1000.0f,10000.0f,15000.0f};
	private static String WatchKeys[]=new String[]{"fire"};
	public static final float watchbase=5.0f;
	
	public static final String HTTP[]={"getVisitorIP","getHostIP","message","logMessage","popUp","getParameter","playSound","triggerWatch","replaceContent","fetchGetVariable","hideStore","turnOnWatch"};
	public static final int HTTPLevel[]={1,1,1,1,5,20,10,30,30,35,40,30};
	public static final float HTTPCPUCost[]={1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,1.0f,3.0f,1.0f,1.0f,6.0f,3.0f};
	public static final float HTTPPrice[]={10.0f,10.0f,10.0f,50.0f,250.0f,100.0f,500.0f,1000.0f,250.0f,500.0f,1500.0f,1000.0f};
	private static String HTTPKeys[]=new String[]{"enter","exit","submit"};
	public static final float httpbase=10.0f;
	
	public static String HelperFunctions[]={
		"clearFile","readFile","readLine","countLines","writeLine","indexOf","intValue","floatValue","equal",
		"printf","rand","setGlobal","getGlobal","writeFile","fileExists","replaceAll","parseFloat","parseInt",
		"char","toUpper","toLower","isGlobalSet","isGetVariableSet","isParameterSet","isTriggerParameterSet",
		"split","join","length","giveTask","giveCommodity","takeCommodity","giveFile","takeFile","giveXP",
		"finishQuest","giveAccess","setTask","exchangeFile","exchangeCommodity","giveQuest","getDate","getTime",
		"giveTask","setTask","giveQuest","finishQuest","takeMoney","takeCommodity","exchangeCommodity","exchangeFile",
		"giveXP","giveMoney","giveCommodity","giveFile","giveFile2","takeFile","takeFile2","giveMoney","giveAccess","triggerWatchRemote",
		"changeNetwork","completeTask"
	};
	
	boolean checkHelper(String function){
		for(int i=0;i<HelperFunctions.length;i++){
			if(HelperFunctions[i].equals(function))
				return(true);
		}
		return(false);
	}
	
	/**
	Compiles a regular HackScript application.
	*/
	public HashMap compileApplication(Integer Type,HashMap Source,HashMap Stats){
	
		int type=Type;
		float cpubase=0.0f;
		
		//Create Hash Maps of the Various Function lists.
		HashMap BankFunctions=new HashMap();
		HashMap AttackFunctions=new HashMap();
		HashMap FTPFunctions=new HashMap();
		HashMap WatchFunctions=new HashMap();
		HashMap HTTPFunctions=new HashMap();
		HashMap HelperFunctions=new HashMap();
		HashMap RedirectFunctions=new HashMap();
		
		HashMap returnMe=new HashMap();
		String Keys[]=null;
		int level=1;
		Program GatherInformation=null;

		
		try{
		
			for(int i=0;i<Challenge.length;i++){
				HelperFunctions.put(Challenge[i],"");
			}
			
			for(int i=0;i<Bank.length;i++){
				BankFunctions.put(Bank[i],new Integer(i));
			}
			
			for(int i=0;i<Attack.length;i++){
				AttackFunctions.put(Attack[i],new Integer(i));
			}

			for(int i=0;i<Redirect.length;i++){
				RedirectFunctions.put(Redirect[i],new Integer(i));
			}
			
			for(int i=0;i<FTP.length;i++){
				FTPFunctions.put(FTP[i],new Integer(i));
			}

			for(int i=0;i<Watch.length;i++){
				WatchFunctions.put(Watch[i],new Integer(i));
			}
			
			for(int i=0;i<HTTP.length;i++){
				HTTPFunctions.put(HTTP[i],new Integer(i));
			}
			//*/
			
			if(type==HackerFile.BANKING_COMPILED){
				Keys=BankKeys;
				if(Stats.get("Merchanting")!=null)
					level=(Integer)Stats.get("Merchanting");
				GatherInformation=new Banking(null,null,null);
				cpubase=bankbase;
			}else
			
			if(type==HackerFile.ATTACKING_COMPILED){
				Keys=AttackKeys;
				if(Stats.get("Attack")!=null)
					level=(Integer)Stats.get("Attack");		
				GatherInformation=new AttackProgram(null,null,null,null,null);
				cpubase=attackbase;
			}else
			
			if(type==HackerFile.SHIPPING_COMPILED){
				Keys=RedirectKeys;
				if(Stats.get("Redirect")!=null)
					level=(Integer)Stats.get("Redirect");		
				GatherInformation=new ShippingProgram(null,null,null,null,null);
				cpubase=redirectbase;
			}else
			
			
			if(type==HackerFile.FTP_COMPILED){
				Keys=FTPKeys;
				GatherInformation=new FTPProgram(null,null,null,null);
				cpubase=ftpbase;
			}else
			
			if(type==HackerFile.WATCH_COMPILED){
				Keys=WatchKeys;
				if(Stats.get("Watch")!=null)
					level=(Integer)Stats.get("Watch");
				GatherInformation=new WatchProgram(null,null,null);
				cpubase=watchbase;
			}
			
			if(type==HackerFile.HTTP){
				Keys=HTTPKeys;
				if(Stats.get("HTTP")!=null)
					level=(Integer)Stats.get("HTTP");
				GatherInformation=new HTTPProgram(null,null);
				cpubase=httpbase;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String Scripts[]=new String[Keys.length];
		
		try{
			for(int i=0;i<Scripts.length;i++){
				Scripts[i]=(String)Source.get(Keys[i]);
			}
		}catch(Exception e){
		}
		
		float cpucost=0.0f;
		float price=5.0f;
		String error="";
		boolean errorFound=false;
		
		int currentFunction=0;
		HackerLinker HL=new HackerLinker(GatherInformation,null);
		
		try{
			//Exectue the given script.
			for(int i=0;i<Scripts.length;i++){
				currentFunction=i;
							
				ArrayList Functions=RunFactory.getCodeList(Scripts[i]);
				RunFactory.runAllCode(Scripts[i],HL);

				if(GatherInformation.getError().length()>0){
					error=GatherInformation.getError();
					errorFound=true;
					break;
				}
				
				for(int ii=0;ii<Functions.size();ii++){
					String name=(String)Functions.get(ii);
					
					if(HelperFunctions.get(name)!=null)
						continue;
					 					
					if(type==HackerFile.BANKING_COMPILED){
						if(!(BankFunctions.get(name)==null)){
							int key=(Integer)BankFunctions.get(name);
							if(BankLevel[key]>level){
								errorFound=true;
								error="You must be Merchanting level "+BankLevel[key]+" to compile "+name+"().";
								break;
							}
							cpucost+=BankCPUCost[key];
							price+=BankPrice[key];
						}else{
							if(!checkHelper(name)){
								errorFound=true;
								error="Function "+name+"() not found.";
								break;
							}
						}
					}else
					
					if(type==HackerFile.ATTACKING_COMPILED){
						if(!(AttackFunctions.get(name)==null)){
							int key=(Integer)AttackFunctions.get(name);
							if(AttackLevel[key]>level){
								errorFound=true;
								error="You must be Attack level "+AttackLevel[key]+" to compile "+name+"().";
								break;
							}
							cpucost+=AttackCPUCost[key];
							price+=AttackPrice[key];
						}else{
							if(!checkHelper(name)){
								errorFound=true;
								error="Function "+name+"() not found.";
								break;
							}
						}
					}else
					
					if(type==HackerFile.SHIPPING_COMPILED){
						if(!(RedirectFunctions.get(name)==null)){
							int key=(Integer)RedirectFunctions.get(name);
							if(RedirectLevel[key]>level){
								errorFound=true;
								error="You must be Redirect level "+RedirectLevel[key]+" to compile "+name+"().";
								break;
							}
							cpucost+=RedirectCPUCost[key];
							price+=RedirectPrice[key];
						}else{
							if(!checkHelper(name)){
								errorFound=true;
								error="Function "+name+"() not found.";
								break;
							}
						}
					}else
					
					if(type==HackerFile.FTP_COMPILED){
						if(!(FTPFunctions.get(name)==null)){
							int key=(Integer)FTPFunctions.get(name);
							if(FTPLevel[key]>level){
								errorFound=true;
								error="You must be FTP level "+FTPLevel[key]+" to compile "+name+"().";
								break;
							}
							cpucost+=FTPCPUCost[key];
							price+=FTPPrice[key];
						}else{
							if(!checkHelper(name)){
								errorFound=true;
								error="Function "+name+"() not found.";
								break;
							}
						}
					}else
					
					if(type==HackerFile.WATCH_COMPILED){
						if(!(WatchFunctions.get(name)==null)){
						
						
							int key=(Integer)WatchFunctions.get(name);


							if(WatchLevel[key]>level){
								errorFound=true;
								error="You must be Watch level "+WatchLevel[key]+" to compile "+name+"().";
								break;
							}
							cpucost+=WatchCPUCost[key];
							price+=WatchPrice[key];
						}else{
							if(!checkHelper(name)){
								errorFound=true;
								error="Function "+name+"() not found.";
								break;
							}
						}
					}else
					
					if(type==HackerFile.HTTP){
						if(!(HTTPFunctions.get(name)==null)){
						
							int key=(Integer)HTTPFunctions.get(name);
							
							if(HTTPLevel[key]>level){
								errorFound=true;
								error="You must be HTTP level "+HTTPLevel[key]+" to compile "+name+"().";
								break;
							}
							cpucost+=HTTPCPUCost[key];
							price+=HTTPPrice[key];
						}else{
							if(!checkHelper(name)){
								errorFound=true;
								error="Function "+name+"() not found.";
								break;
							}
						}
					}
				}
				if(errorFound)
					break;
			}
		}catch(Exception e){
			error="Syntax error parsing function "+Keys[currentFunction]+". compiler returned ["+e.getMessage()+"]";
		}

		returnMe.put("cpucost",new Double(Math.max(cpucost,cpubase)));
		returnMe.put("price",new Double(price));
		returnMe.put("error",error);
		
		System.gc();
		return(returnMe);
	}
	
	public static final String Challenge[]={"strlen","sqrt","abs","ln","atan","acos","asin","tan","cos","sin","getE","getPI","substr","getInputString","getInputStringCount","setOutputString","getInputFloat","getInputFloatCount","setOutputFloat","getInputInt","getInputIntCount","setOutputInt","equal","printf","rand","intValue","floatValue","indexOf","parseFloat","parseInt","replaceAll","split","length"};

	/**
	Runs a challenge script application.
	*/
	public HashMap runToyProblem(String Source,Integer Iterations,Object OInputFloat[],Object OInputString[],Object OInputInteger[],Object OTargetFloat[],Object OTargetString[],Object OTargetInteger[]){
		HashMap returnMe=new HashMap();
		String error="";
		
		Double InputFloat[]=null;
		try{
			InputFloat=new Double[OInputFloat.length];
			for(int i=0;i<OInputFloat.length;i++)
				InputFloat[i]=(Double)OInputFloat[i];
		}catch(Exception e){}
		
		String InputString[]=null;
		try{
		InputString=new String[OInputString.length];
		for(int i=0;i<OInputString.length;i++)
			InputString[i]=(String)OInputString[i];
		}catch(Exception e){}

		Integer InputInteger[]=null;
		try{
		InputInteger=new Integer[OInputInteger.length];
		for(int i=0;i<OInputInteger.length;i++)
			InputInteger[i]=(Integer)OInputInteger[i];
		}catch(Exception e){}

		Double TargetFloat[]=null;
		try{
		TargetFloat=new Double[OTargetFloat.length];
		for(int i=0;i<OTargetFloat.length;i++)
			TargetFloat[i]=(Double)OTargetFloat[i];
		}catch(Exception e){}

		String TargetString[]=null;
		try{
		TargetString=new String[OTargetString.length];
		for(int i=0;i<OTargetString.length;i++)
			TargetString[i]=(String)OTargetString[i];
		}catch(Exception e){}

		Integer TargetInteger[]=null;
		try{
		TargetInteger=new Integer[OTargetInteger.length];
		for(int i=0;i<OTargetInteger.length;i++)
			TargetInteger[i]=(Integer)OTargetInteger[i];
		}catch(Exception e){}

		
		ToyProgram TP=new ToyProgram(Source,64,(int)Iterations);
		ToyLinker HL=new ToyLinker(TP,null);
		
		//Add the inputs.
		if(InputFloat!=null)
			for(int i=0;i<InputFloat.length;i++)
				TP.addInFloat((float)((double)InputFloat[i]));
		if(InputString!=null)
			for(int i=0;i<InputString.length;i++){
				TP.addInString(InputString[i]);
			}
		if(InputInteger!=null)
			for(int i=0;i<InputInteger.length;i++){
				TP.addInInt((int)InputInteger[i]);
			}
				
		//Add the targets.
		if(TargetFloat!=null)
			for(int i=0;i<TargetFloat.length;i++)
				TP.addTargetFloat((float)((double)TargetFloat[i]));
		if(TargetString!=null)
			for(int i=0;i<TargetString.length;i++){
				TP.addTargetString(TargetString[i]);
			}
		if(TargetInteger!=null)
			for(int i=0;i<TargetInteger.length;i++)
				TP.addTargetInt((int)TargetInteger[i]);
				
		boolean found=false;
		try{
			ArrayList Functions=RunFactory.getCodeList(Source);
			for(int i=0;i<Functions.size();i++){
				String name=(String)Functions.get(i);
				found=false;
				for(int ii=0;ii<Challenge.length;ii++){
					if(Challenge[ii].equals(name))
						found=true;
				}
				if(found==false){
					TP.setError("Function "+name+" not found.");
					break;
				}
			}
		}catch(Exception e){
			TP.setError("Syntax error in challenge code compiler returned ["+e.getMessage()+"]");
		}
		if(found){
			TP.execute(null);
		}
					
		returnMe.put("outint",TP.getOutInt());
		returnMe.put("outstring",TP.getOutString());
		returnMe.put("outdouble",TP.getOutDouble());
		returnMe.put("success",new Boolean(TP.getSuccess()));
		returnMe.put("error",TP.getError());

		System.gc();
		return(returnMe);
	}
	
	/**
	Testing main.
	*/
	public static void main(String args[]){
		HackerRPC HR=new HackerRPC();
	//	HR.doSearch("dorothy scare","");
	}
}

