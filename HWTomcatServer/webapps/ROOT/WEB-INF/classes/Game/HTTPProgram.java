package Game;
/**
FTPProgram.java

A program that can be installed on a port of type HTTP and which performs "enter" and "exit" and "submit" operations.
*/
import Hackscript.Model.*;
import java.io.*;
import java.util.*;
import Assignments.*;
public class HTTPProgram extends Program{

	//Scripts.
	private String enterScript="";
	private String exitScript="";
	private String submitScript="";
	private String content="";
	
	private String targetIP="";//IP of computer that caused this program to run.
	private HashMap Parameters=null;//The parameters submitted along with a submit.

	//Data
	private Computer MyComputer=null;//Computer this program is associated with.
	private NetworkSwitch MyComputerHandler=null;//Computer this program is associated with.

	//Constructor.
	public HTTPProgram(Computer MyComputer,NetworkSwitch MyComputerHandler){
		super.setComputerHandler(MyComputerHandler);
		super.setComputer(MyComputer);
		this.MyComputer=MyComputer;
		this.MyComputerHandler=MyComputerHandler;
		if(MyComputer!=null)
			content=MyComputer.getBody();
	}
	
	/**
	Trigger a watch.
	*/
	public void triggerWatch(int watchNumber,HashMap TriggerParam){
		MyComputer.getWatchHandler().triggerWatch(watchNumber,targetIP,TriggerParam);
	}
	
	/**
	Get the path of the file being transfered.
	*/
	public String getTargetIP(){
		return(targetIP);
	}
	
	/**
	Should the store be served?
	*/
	boolean hideStore=false;
	public void hideStore(){
		hideStore=true;
	}
	
	/**
	HTTP Get variable.
	*/
	HashMap GetString=null;
	public String fetchGetVariable(String key){
		if(GetString==null)
			return("");
		String Data=(String)GetString.get(key);
		if(Data==null)
			return("");
		else
			return(Data);
	}
	
	/**
	Replace a place-holder string in the HTML page.
	*/
	public void replaceContent(String key,String content){
		content=content.replaceAll("\\\\", "\\\\\\\\");
		content=content.replaceAll("\\$", "\\\\\\$");
		this.content=this.content.replaceAll("\\<\\?"+key+"\\?\\>",content);
	}
	
	/**
	Get a parameter from 
	*/
	public String getParameter(String Key){
	
		String returnMe="";
		if(Parameters==null)
			return("");
		else
			returnMe=(String)Parameters.get(Key);
		if(Key!=null)
			return(returnMe);
		else
			return("");
	}
	
	/**
	Execute the commands.
	*/
	public void execute(ApplicationData MyApplicationData){
	
		content=MyComputer.getBody();
		String script="";
		hideStore=false;
		Integer packetID=new Integer(0);
								
		if(MyApplicationData.getFunction().equals("requestwebpage")){	
			GetString=(HashMap)MyApplicationData.getParameters();
			packetID=(Integer)GetString.get("packetid");
			targetIP=MyApplicationData.getSourceIP();
			script=enterScript;	
		}else
		
		//Prepare the put message.
		if(MyApplicationData.getFunction().equals("exit")){		
			targetIP=MyApplicationData.getSourceIP();
			script=exitScript;
		}else
		
		//Prepare the put message.
		if(MyApplicationData.getFunction().equals("submit")){
			targetIP=MyApplicationData.getSourceIP();
			Parameters=(HashMap)MyApplicationData.getParameters();
			packetID=(Integer)Parameters.get("packetid");
			script=submitScript;
			
			try{
				HackerLinker HL=new HackerLinker(this,MyComputerHandler);
				RunFactory.runCode(script,HL,MyComputer.MAX_OPS);
			}catch(Exception e){
			}
			
			script=enterScript;
		}
		
		if(script!=null&&!script.equals("")){
		
			try{
				HackerLinker HL=new HackerLinker(this,MyComputerHandler);
				RunFactory.runCode(script,HL,MyComputer.MAX_OPS);
			}catch(Exception e){
			}
		}
		
		if(!MyApplicationData.getFunction().equals("exit"))//Serve the web-page.
			serveWebPage(MyApplicationData,packetID);
			
		Parameters=null;

	}
	
	/**
	installScript(HashMap Script);
	Installs a script on the various entrance points on this program.
	*/
	public void installScript(HashMap Script){
		enterScript=(String)Script.get("enter");
		exitScript=(String)Script.get("exit");
		submitScript=(String)Script.get("submit");
	}
	
	/**
	Return a hash map representation of the program currently installed on this port.
	*/
	public HashMap getContent(){
		HashMap returnMe=new HashMap();
		returnMe.put("enter",enterScript);
		returnMe.put("exit",exitScript);
		returnMe.put("submit",submitScript);
		return(returnMe);
	}

	/**
	Returns the keys associated with this program type.
	*/
	public String[] getTypeKeys(){
		String returnMe[]=new String[]{"enter","exit","submit"};
		return(returnMe);
	}
	
	/**
	Server a webpage to a player.
	*/
	public void serveWebPage(ApplicationData MyApplicationData,Integer packetID){
		String PageTitle=MyComputer.getTitle();
		String PageBody=content;
		//Receive Payment.
		
		Object Files[]=MyComputer.getFileSystem().getWebDirectory("Store/");
		for(int i=0;i<Files.length;i++){//Make sure we describe hardware.
			EquipmentSheet MyEquipmentSheet=new EquipmentSheet(MyComputer);
			if(Files[i]!=null&&Files[i] instanceof HackerFile&&(((HackerFile)Files[i]).getType()==HackerFile.PCI||((HackerFile)Files[i]).getType()==HackerFile.AGP)){
				MyEquipmentSheet.degradeEquipment((HackerFile)Files[i]);
				MyEquipmentSheet.describeCard((HackerFile)Files[i]);//Testing outputting a description of the bonus.
			}
		}
		
		Port TempPort=null;
		if(!MyComputer.checkHTTP()){
		//if(((TempPort=(Port)MyComputer.getPorts().get(new Integer(MyComputer.getDefaultHTTP())))==null)||TempPort.getType()!=Port.HTTP||!TempPort.getOn()||TempPort.getDummy()){
			PageTitle="Server Not Found";
			PageBody="<html><head><title>Hack Wars - Error report</title><style><!--H1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:22px;color:white} H2 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:16px;} H3 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:14px;} BODY {background-color:rgb(0,0,0);font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;color:white;} B {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;color:white;} P {color:white;font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;}A {color : black;}A.name {color : black;}HR {color : #525D76;}--></style> </head><body><h1 style=\"width:100%\">HTTP Status 408</h1><HR size=\"1\" noshade=\"noshade\"><p style=\"background-color:black;\"><b>type</b> HTTP Error</p><p style=\"background-color:black;\"><b>message</b> <u>Resource not found.</u></p><p style=\"background-color:black\"><b>description</b> <u>The HTTP server of the player you attempted to connect to does not seem to be on.</u></p><HR size=\"1\" noshade=\"noshade\"><h3>&copy; Hack Wars</h3></body></html>";
			Files=null;
		}
		
		//If no banking application is found don't return the store listing.
		if(!MyComputer.checkBank()){
			Files=null;
		}
		
		//If the default FTP application is null don't return the store listing.
		if(((TempPort=(Port)MyComputer.getPorts().get(new Integer(MyComputer.getDefaultFTP())))==null)||TempPort.getType()!=Port.FTP||!TempPort.getOn()){
			Files=null;
		}
		
		if(hideStore)
			Files=null;
		
		Object O[]=new Object[]{PageTitle,PageBody,Files,packetID};

		MyComputerHandler.addData(new ApplicationData("webpage",O,0,MyComputer.getIP()),MyApplicationData.getSourceIP());
	}

	
	/**
	Output the class data in XML format.
	*/
	public String outputXML(){
		String returnMe="";
		if(enterScript!=null)
			returnMe+="<enter><![CDATA["+enterScript.replaceAll("]]>","]]&gt;")+"]]></enter>\n";
		else
			returnMe+="<enter><![CDATA["+enterScript+"]]></enter>\n";

		if(exitScript!=null)
			returnMe+="<exit><![CDATA["+exitScript.replaceAll("]]>","]]&gt;")+"]]></exit>\n";
		else
			returnMe+="<exit><![CDATA["+exitScript+"]]></exit>\n";

		if(submitScript!=null)
			returnMe+="<submit><![CDATA["+submitScript.replaceAll("]]>","]]&gt;")+"]]></submit>\n";
		else
			returnMe+="<submit><![CDATA["+submitScript+"]]></submit>\n";

		return(returnMe);
	}
}
