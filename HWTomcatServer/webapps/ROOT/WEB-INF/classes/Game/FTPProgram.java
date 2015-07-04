package Game;
/**
FTPProgram.java

A program that can be installed on a port of type FTP and which performs "get" and "put" operations.
*/
import Hackscript.Model.*;
import java.io.*;
import java.util.HashMap;
import Assignments.*;
public class FTPProgram extends Program{

	//Scripts.
	private String putScript="";
	private String getScript="";
	
	//Execute specific data.
	private String path="";//Path to place the file in.
	private HackerFile HF=null;//The cloned hacker file to transfer.
	private String targetIP="";

	//Data
	private FileSystem MyFileSystem=null;//File system.
	private Computer MyComputer=null;//Computer this program is associated with.
	private Port ParentPort=null;//Port this program is installed on.
	private NetworkSwitch  MyComputerHandler=null;//Central messaging system.
	
	//Constructor.
	public FTPProgram(Computer MyComputer,FileSystem MyFileSystem,Port ParentPort,NetworkSwitch MyComputerHandler){
		super.setComputerHandler(MyComputerHandler);
		super.setComputer(MyComputer);
		this.MyFileSystem=MyFileSystem;
		this.MyComputer=MyComputer;
		this.ParentPort=ParentPort;
		this.MyComputerHandler=MyComputerHandler;
		if(MyComputer!=null)
			this.targetIP=MyComputer.getIP();
	}
	
	/**
	Get the path of the file being transfered.
	*/
	public String getPath(){
		return(path);
	}
	
	/**
	Get the hacker file being transfered.
	*/
	public HackerFile getFile(){
		return(HF);
	}
	
	/**
	Get the IP that the file should be transfered to.
	*/
	public String getIP(){
		return(targetIP);
	}
	
	/**
	Get the malicious IP address that programs should be transfered to.
	*/
	public String getMaliciousIP(){
		return(ParentPort.getMaliciousTarget());
	}
	
	/**
	Get the fetch path of this file. Where it will be saved to.
	*/
	public String fetchPath="";
	public String getFetchPath(){
		return(fetchPath);
	}
	
	/**
	Execute the "put" and "get" commands.
	*/
	public void execute(ApplicationData MyApplicationData){
	
		String script="";
								
		//A DIRECTORY LISTING HAS BEEN REQUESTED FROM THE FILE SYSTEM.
		if(MyApplicationData.getFunction().equals("requestsecondarydirectory")){//Request a directory listing.
			Object[] parameters =(Object[])MyApplicationData.getParameters(); 
			String path=(String)parameters[1];
			String targetIP=(String)parameters[0];
			Object[] Directory = null;
			if(MyComputer.getType()!=Computer.NPC){
				Directory=MyFileSystem.getDirectory(path);
				Object Temp[]=new Object[Directory.length+1];
				Temp[0]=(Integer)((Object[])MyApplicationData.getParameters())[2];//Add an ID 
				for(int i=0;i<Directory.length;i++){
					Temp[i+1]=Directory[i];
				}
				Directory=Temp;
			}
			else{
				int id= (int)(Integer)((Object[])MyApplicationData.getParameters())[2];//Add an ID 
				Directory=new Object[2];
				Object listing[] = new Object[7];
				if(MyComputer.getDrop()==null){
					HF = MyComputer.getDropTable().generateDrop();
					MyComputer.setDrop(HF);
				}
				Directory[0] = new Integer(id);
				listing[0]=HF.getName();
				listing[1]=new Integer(HF.getType());
				listing[2]=new Integer(HF.getQuantity());
				listing[3]=new Float(HF.getPrice());
				listing[4]=HF.getMaker();
				listing[5]=HF.getCPUCost();
				listing[6]=HF.getDescription();
				Directory[1] = listing;
			}
			
			MyComputerHandler.addData(new ApplicationData("delivereddirectory",new Object[] {Directory,MyComputer.isNPC()},0,MyComputer.getIP()),targetIP);
			return;
		}else
		
		if(MyApplicationData.getFunction().equals("malget")){	
			
			HackerFile HF = null;
			Object[] parameters = (Object[])MyApplicationData.getParameters();
			this.targetIP=(String)parameters[0];
			String name=(String)parameters[1];
			int stolenPort = 0;
			if(parameters.length == 6){
				stolenPort = (Integer)parameters[5];
			}else if(parameters.length == 7){
				stolenPort = (Integer)parameters[6];
			}
			if(MyComputer.getType()!=Computer.NPC){
				if(name==null){//Steal the first file found.
					name="";
					Object O[]=MyComputer.getFileSystem().getWebDirectory("Public/");
					for(int i=0;i<O.length;i++){
						if(O[i] instanceof HackerFile){
							name=((HackerFile)O[i]).getName();
							break;
						}
					}
				}
				String fetch_path=(String)parameters[2];
				this.path=(String)parameters[3];
				String password=(String)parameters[4];
				
				
				
				if(fetch_path!="Store/"){

					HackerFile THF=MyFileSystem.getFile(fetch_path,name);	
								
					int quantity=1;
					if(THF!=null){
						if(THF.isStacking()){
							quantity=THF.getQuantity()-1;
						}
					}else
						return;
						
					THF.setQuantity(quantity);
					if(THF.getQuantity()<=0||!THF.isStacking()){
						MyFileSystem.deleteFile(fetch_path,name);
					}
					
					HF=THF.clone();
					HF.setQuantity(1);
					HF.setLocation(path);
				}
			}else{
				HF = MyComputer.getDrop();
				if(HF == null){
					HF = MyComputer.getDropTable().generateDrop();
				}
			}
											
			PacketAssignment PA=ParentPort.getCurrentPacket();
			PA.setRequestPrimary(true,8);
			PA.setRequestSecondary(true,8);
			MyComputer.setDrop(null);
			Object Parameter[]=new Object[]{"",HF,MyComputer.getIP(),ParentPort.getLastDamageWindowHandle()};				
			MyComputerHandler.addData(new ApplicationData("savefile",Parameter,0,MyComputer.getIP()),targetIP);
			
		
			//MyComputer.respawn(Port.FTP);			
			return;
		}else
	
		if(MyApplicationData.getFunction().equals("get")){	
		
			this.targetIP=(String)((Object[])MyApplicationData.getParameters())[0];
			String name=(String)((Object[])MyApplicationData.getParameters())[1];
			String fetch_path=(String)((Object[])MyApplicationData.getParameters())[2];
			this.path=(String)((Object[])MyApplicationData.getParameters())[3];
			String password=(String)((Object[])MyApplicationData.getParameters())[4];
			int getQuantity=(Integer)((Object[])MyApplicationData.getParameters())[5];
			
			//Check whether you have permission to peform this action.
			if(!targetIP.equals(MyComputer.getIP())){
				if(!password.equals(MyComputer.getPassword())){
					MyComputerHandler.addData(new ApplicationData("message",MessageHandler.FTP_FAIL_PASSWORD_INCORRECT,0,this.getIP()),targetIP);
					return;
				}
			}

			HackerFile THF=MyFileSystem.getFile(path,name);	
						
			int quantity=1;
			if(THF!=null){
				if(THF.isStacking()){
					if(THF.getQuantity()<getQuantity)
						return;
				
					quantity=THF.getQuantity()-getQuantity;
				}
			}else
				return;
				
			THF.setQuantity(quantity);
			if(THF.getQuantity()<=0||!THF.isStacking()){
				MyFileSystem.deleteFile(path,name);
			}
			
			HF=THF.clone();
			HF.setQuantity(getQuantity);
			HF.setLocation(fetch_path);
			this.fetchPath=fetch_path;
									
			script=getScript;	
			
			if(!targetIP.equals(MyComputer.getIP()))
				MyComputer.getComputerHandler().addData(new ApplicationData("requestftpupdate",null,0,MyComputer.getIP()),targetIP);
			MyComputer.getComputerHandler().addData(new ApplicationData("requestftpupdate",null,0,MyComputer.getIP()),MyComputer.getIP());
		}else
		
		//Prepare the put message.
		if(MyApplicationData.getFunction().equals("put")){		
		
			String targetIP=(String)((Object[])MyApplicationData.getParameters())[0];
			String name=(String)((Object[])MyApplicationData.getParameters())[1];
			String fetch_path=(String)((Object[])MyApplicationData.getParameters())[2];
			HackerFile HF=MyFileSystem.getFile(fetch_path,name);
			String tpath=(String)((Object[])MyApplicationData.getParameters())[3];
			int putQuantity=(Integer)((Object[])MyApplicationData.getParameters())[5];

			HackerFile SF=null;
			
			int quantity=1;
			if(HF!=null){
				if(HF.isStacking()){
					if(HF.getQuantity()<putQuantity)
						return;
								
					quantity=HF.getQuantity()-putQuantity;
				}
			}else
				return;
				
			HF.setQuantity(quantity);
			if(HF.getQuantity()<=0||!HF.isStacking()){
				MyFileSystem.deleteFile(fetch_path,name);
			}
			
			SF=HF.clone();
			SF.setQuantity(putQuantity);
			SF.setLocation(tpath);
		
			Object Parameters[]=new Object[]{MyComputer.getIP(),((Object[])MyApplicationData.getParameters())[1],((Object[])MyApplicationData.getParameters())[2],((Object[])MyApplicationData.getParameters())[3],((Object[])MyApplicationData.getParameters())[4],SF};
			MyComputerHandler.addData(new ApplicationData("finalizeput",Parameters,ParentPort.getNumber(),MyComputer.getIP()),targetIP);

			if(!targetIP.equals(MyComputer.getIP()))
				MyComputerHandler.addData(new ApplicationData("requestftpupdate",null,0,MyComputer.getIP()),targetIP);
			MyComputerHandler.addData(new ApplicationData("requestftpupdate",null,0,MyComputer.getIP()),MyComputer.getIP());
		}else if(MyApplicationData.getFunction().equals("finalizeput")){
		
			this.targetIP=MyComputer.getIP();
			String name=(String)((Object[])MyApplicationData.getParameters())[1];
			String fetch_path=(String)((Object[])MyApplicationData.getParameters())[2];
			this.path=(String)((Object[])MyApplicationData.getParameters())[3];
			String password=(String)((Object[])MyApplicationData.getParameters())[4];
			HF=(HackerFile)((Object[])MyApplicationData.getParameters())[5];
			
			//Check whether you have permission to peform this action.
			if(MyComputer.getFileSystem().getSpaceLeft()<=0){
					MyComputerHandler.addData(new ApplicationData("message",MessageHandler.FTP_PUT_FAIL_HD_FULL,0,this.getIP()),MyApplicationData.getSourceIP());
					HF.setLocation("");
					Object Parameters[]=new Object[]{fetch_path,HF};
					MyComputerHandler.addData(new ApplicationData("savefile",Parameters,0,this.getIP()),MyApplicationData.getSourceIP());
					return;
			}else if(!MyApplicationData.getSourceIP().equals(MyComputer.getIP())){
				if(!password.equals(MyComputer.getPassword())){
					MyComputerHandler.addData(new ApplicationData("message",MessageHandler.FTP_FAIL_PASSWORD_INCORRECT,0,this.getIP()),MyApplicationData.getSourceIP());
					HF.setLocation("");
					Object Parameters[]=new Object[]{fetch_path,HF};
					MyComputerHandler.addData(new ApplicationData("savefile",Parameters,0,this.getIP()),MyApplicationData.getSourceIP());
					return;
				}
			}
			
			script=putScript;

			if(!targetIP.equals(MyComputer.getIP()))
				MyComputerHandler.addData(new ApplicationData("requestftpupdate",null,0,MyComputer.getIP()),targetIP);
			MyComputerHandler.addData(new ApplicationData("requestftpupdate",null,0,MyComputer.getIP()),MyComputer.getIP());
		}else
		
		return;

		try{
			HackerLinker HL=new HackerLinker(this,MyComputerHandler);
			RunFactory.runCode(script,HL,4096);
		}catch(Exception e){
		}
	}
	
	/**
	installScript(HashMap Script);
	Installs a script on the various entrance points on this program.
	*/
	public void installScript(HashMap Script){	
		putScript=(String)Script.get("put");
		getScript=(String)Script.get("get");
	}
	
	/**
	Return a hash map representation of the program currently installed on this port.
	*/
	public HashMap getContent(){
		HashMap returnMe=new HashMap();
		returnMe.put("put",putScript);
		returnMe.put("get",getScript);
		return(returnMe);
	}

	/**
	Returns the keys associated with this program type.
	*/
	public String[] getTypeKeys(){
		String returnMe[]=new String[]{"get","put"};
		return(returnMe);
	}
	
	/**
	Output the class data in XML format.
	*/
	public String outputXML(){
		String returnMe="";
		
		if(putScript!=null)
			returnMe+="<put><![CDATA["+putScript.replaceAll("]]>","]]&gt;")+"]]></put>\n";
		else
			returnMe+="<put><![CDATA["+putScript+"]]></put>\n";

		if(getScript!=null)
			returnMe+="<get><![CDATA["+getScript.replaceAll("]]>","]]&gt;")+"]]></get>\n";
		else
			returnMe+="<get><![CDATA["+getScript+"]]></get>\n";

		return(returnMe);
	}
}
