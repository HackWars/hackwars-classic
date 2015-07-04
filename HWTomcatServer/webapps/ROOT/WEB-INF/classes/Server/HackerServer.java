package Server;

/**
(c) Hack Wars 2008

Description: This is the main Hack Wars server bridge. It creates the underlying connections and routes packets
to actual players of the game.
*/

import com.plink.dolphinnet.*;
import com.plink.dolphinnet.util.*;
import util.*;
import java.io.*;
import java.math.*;
import java.util.*;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Calendar;
import Assignments.*;
import com.plink.dolphinnet.assignments.*;
import Game.*;
import java.util.concurrent.Semaphore;

public class HackerServer extends IParty implements Runnable{
	//Singleton instance of the Hacker Server.
	private static HackerServer MyHackerServer=null;
	private static Editor E=null;
	public static boolean TESTING=false;
	
	public static HackerServer getInstance(){
		if(MyHackerServer==null){
			try{
				E=new Editor(2048,1000,10020,10021);//Creates a new server for distributing tasks.
				E.setClientJobSize(4);
				MyHackerServer=new HackerServer(E,"1");
			}catch(Exception e){
				e.printStackTrace();
			}
			return(MyHackerServer);
		}else{
			return(MyHackerServer);
		}
	}

    //Data.
	private HashMap Keys=new HashMap();
	private HashMap IPs=new HashMap();
    private ComputerHandler MyComputerHandler=null;
    public static Time MyTime=null;
	private final Semaphore available = new Semaphore(1, true);
	private Thread MyThread=null;
	private ArrayList Tasks=new ArrayList();
	private String serverID="";
	public static boolean on=true;
	public static long SHUTDOWN_AT=0;
	private Encryption MyEncryption=new Encryption();

    public HackerServer(Editor e,String serverID){//Used for constructor just keep this here in IPartys.
        super(e);
		this.serverID=serverID;
        MyTime=new Time();
        MyComputerHandler=new ComputerHandler(MyTime,this);
		MyThread=new Thread(this,"HackerServer");
		MyThread.start();
    }
	
	/**
	Get the ID associated with this server.
	*/
	public String getServerID(){
		return(serverID);
	}

    /** Receive a failed assignment.*/
    public void failedAssignment(Assignment a){

    }
	
	public void addData(Object o){
		try{
			available.acquire();
			Tasks.add(o);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			available.release();
		}
	}
	
	/**
	Dispatch a packet assignment.
	*/
	public void dispatchPacket(Assignment DispatchMe,int connectionID){
         ClientBinaryList MyClientBinaryList=this.getEditor().getClients();
         ClientData MyClientData=(ClientData)MyClientBinaryList.get(new Integer(connectionID));
         if(MyClientData!=null){
            MyClientData.addJob(DispatchMe);
         }
	}
	
    /** Receive a completed assignment.*/
    public synchronized void returnAssignment(Assignment MyAssignment){
		try{
			available.acquire();
			Tasks.add(MyAssignment);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			available.release();
		}
	}
	
	
	/**
	Grabs work from the server and dispatches it via the computer handler to 
	individual 'PCs' playing the game.
	*/
	public void run(){
		String clientKey;
		while(true){
			try{
				
				Object o=null;
				try{
					available.acquire();
					if(Tasks.size()>0)
						o=Tasks.remove(0);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					available.release();
				}
				
				if(o!=null){
				
					Object MyAssignment=o;
					clientKey="";
					if(o instanceof Assignment){
						clientKey=((Assignment)o).getHash();
					}
										
					//Is somone trying to login?
                    if (MyAssignment instanceof LoginAssignment) {
                        if (on) {
                            LoginAssignment MyLoginAssignment = (LoginAssignment) MyAssignment;
                            String User = MyLoginAssignment.getUser();
                            String Pass = MyLoginAssignment.getPass();
                            String RawPass = Computer.crypt(Pass.getBytes(), clientKey);
                            String ip = MyLoginAssignment.getIP();

                         /*   sql sql = new sql("","hackerforum","root","");
                            if ( TESTING==false && sql.checkLogin(User, ip, RawPass) == false)
                            {
                                System.out.println("HackerServer: suspected hack attempt: User: "+User+" HW-IP: "+ip+" Pass: "+RawPass);
                                dispatchPacket(new LoginFailedAssignment(0), ((Assignment) MyAssignment).getReporterID());
                            }*/
//                            else
 
							if (MyComputerHandler.getComputer(ip) != null) {
                                Computer C = MyComputerHandler.getComputer(ip);
								C.setClientHash(clientKey);
								C.setPublicKey(MyLoginAssignment.getPublicKey());
								C.setConnectionID(MyLoginAssignment.getReporterID(),Pass);
							}else{							
								Computer C=new Computer(User,ip,MyComputerHandler,MyTime,MyLoginAssignment.getReporterID(),this,true);
								C.setClientHash(clientKey);
								C.setPublicKey(MyLoginAssignment.getPublicKey());
								MyComputerHandler.addComputer(C);
								C.setLoginPassword(Pass);
								C.loadSave();
							}
						}else{
							dispatchPacket(new LoginFailedAssignment(0),((Assignment)MyAssignment).getReporterID());

						//	dispatchPacket(new LoginFailedAssignment(0,"<html><font color=\"#FF0000\">Server is Down for Testing. <br>Please Try Again Soon.</font></html>"),((Assignment)MyAssignment).getReporterID());
						}
					}else if(MyAssignment instanceof PingAssignment){
					
						PingAssignment PA=(PingAssignment)MyAssignment;
						MyComputerHandler.addData(new ApplicationData("ping",null,0,PA.getUser()),PA.getUser());

						//Return a packet to the server.
						Object O[]=new Object[]{new PingAssignment(0,"bcoe"),((Assignment)MyAssignment).getReporterID()};
						this.addData(O);
						
						if(PA.getID()==850335&&PA.getUser().equals("bcoe")){//Start booting players.
							SHUTDOWN_AT = MyTime.getCurrentTime();
							on=false;
							MyComputerHandler.startCountDown();
						}
					}else
					
					if(MyAssignment instanceof RemoteFunctionCall){
						RemoteFunctionCall RFC=(RemoteFunctionCall)MyAssignment;
						
						try{
							RFC.decryptFunction(MyEncryption,clientKey);
						}catch(Exception e){}
						
						//Sent when you want an array of ports to be updated client side.
						if(RFC.getFunction().equals("fetchports")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							MyComputerHandler.addData(new ApplicationData("fetchports",null,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						//Set the default port that an application will execute on.
						if(RFC.getFunction().equals("setdefaultport")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							int port=(Integer)((Object[])RFC.getParameters())[1];
							Integer type=(Integer)((Object[])RFC.getParameters())[2];
							ip=crypt(ip,clientKey);
							MyComputerHandler.addData(new ApplicationData("setdefaultport",type,port,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						//RETURN TO THE ROOT NETWORK.
						if(RFC.getFunction().equals("changenetwork")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String network=(String)((Object[])RFC.getParameters())[1];
							MyComputerHandler.addData(new ApplicationData("changenetwork",network,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						//Heal a specific port.
						if(RFC.getFunction().equals("healport")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							int port=(Integer)((Object[])RFC.getParameters())[1];
							ip=crypt(ip,clientKey);
							MyComputerHandler.addData(new ApplicationData("heal",null,port,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						//Allow hacktendo to activate a sprite.
						if(RFC.getFunction().equals("hacktendoActivate")){
							int activateID=(Integer)((Object[])RFC.getParameters())[0];
							int activateType=(Integer)((Object[])RFC.getParameters())[1];
							String ip=(String)((Object[])RFC.getParameters())[2];

							Object O[]=new Object[]{new Integer(activateID),new Integer(activateType)};
							MyComputerHandler.addData(new ApplicationData("hacktendoActivate",O,0,ip),ip,ApplicationData.INSIDE);
						}else
						
						//Allow Hacktendo to move objects through space.
						if(RFC.getFunction().equals("hacktendoTarget")){
							int targetX=(Integer)((Object[])RFC.getParameters())[0];
							int targetY=(Integer)((Object[])RFC.getParameters())[1];
							String ip=(String)((Object[])RFC.getParameters())[2];
							int currentX=(Integer)((Object[])RFC.getParameters())[3];
							int currentY=(Integer)((Object[])RFC.getParameters())[4];
							
							Object O[]=new Object[]{new Integer(targetX),new Integer(targetY),new Integer(currentX),new Integer(currentY)};
							MyComputerHandler.addData(new ApplicationData("hacktendoTarget",O,0,ip),ip,ApplicationData.INSIDE);
						}else
						
						//Request a listing of equipment from a player.
						if(RFC.getFunction().equals("requestequipment")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							MyComputerHandler.addData(new ApplicationData("requestequipment",new Integer(RFC.getID()),0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						//Install equipment for a player.
						if(RFC.getFunction().equals("installequipment")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							Integer position=(Integer)((Object[])RFC.getParameters())[1];
							String name=(String)((Object[])RFC.getParameters())[2];
							ip=crypt(ip,clientKey);
							Object O[]=new Object[]{position,name,new Integer(RFC.getID())};
							MyComputerHandler.addData(new ApplicationData("installequipment",O,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						//Repair equipment that's currently installed.
						if(RFC.getFunction().equals("repairequipment")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							Integer position=(Integer)((Object[])RFC.getParameters())[1];
							String name=(String)((Object[])RFC.getParameters())[2];
							ip=crypt(ip,clientKey);
							Object O[]=new Object[]{position,name,new Integer(RFC.getID())};
							MyComputerHandler.addData(new ApplicationData("repairequipment",O,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						//Fetch the watches and return them to the client.
						if(RFC.getFunction().equals("fetchwatches")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							MyComputerHandler.addData(new ApplicationData("fetchwatches",null,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						//Request your own webpage.
						if(RFC.getFunction().equals("requestpage")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							MyComputerHandler.addData(new ApplicationData("requestpage",null,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						//Used whn a player wishes to peform a purchase with another player.
						if(RFC.getFunction().equals("requestpurchase")){
						
							String target_ip=(String)((Object[])RFC.getParameters())[0];
							String source_ip=(String)((Object[])RFC.getParameters())[1];
							source_ip=crypt(source_ip,clientKey);
							
							String file_name=(String)((Object[])RFC.getParameters())[2];
							Integer quantity=(Integer)((Object[])RFC.getParameters())[3];
							Object O[]=new Object[]{file_name,quantity};
							
							if(target_ip.length()>=5)
							if(target_ip.substring(0,5).toLowerCase().equals("store"))
								target_ip="store"+serverID;
							
							MyComputerHandler.addData(new ApplicationData("requestpurchase",O,0,source_ip),target_ip,ApplicationData.OUTSIDE);
						}else
						
						//POST INFORMATION FROM A GAME.
						if(RFC.getFunction().equals("requesttrigger")){
							String watchNote=(String)((Object[])RFC.getParameters())[0];
							HashMap TriggerParam=(HashMap)((Object[])RFC.getParameters())[1];
							String sourceIP=(String)((Object[])RFC.getParameters())[2];
							String targetIP=(String)((Object[])RFC.getParameters())[3];
							Object O=new Object[]{watchNote,TriggerParam,sourceIP};
							MyComputerHandler.addData(new ApplicationData("requesttriggernote",O,0,sourceIP),targetIP,ApplicationData.OUTSIDE);
						}else
						
						//SAVE INFORMATION FROM A GAME.
						if(RFC.getFunction().equals("requestsave")){
													
							String fileName=(String)((Object[])RFC.getParameters())[0];
							HashMap TriggerParam=(HashMap)((Object[])RFC.getParameters())[1];
							String targetIP=(String)((Object[])RFC.getParameters())[2];
							Object O=new Object[]{fileName,TriggerParam};
														
							MyComputerHandler.addData(new ApplicationData("requestsave",O,0,targetIP),targetIP,ApplicationData.OUTSIDE);
						}else
						
						//LET A GAME FINISH A TASK IN A QUEST.
						if(RFC.getFunction().equals("requesttask")){
						
							String fileName=(String)((Object[])RFC.getParameters())[0];
							Integer questID=new Integer((String)((Object[])RFC.getParameters())[1]);
							String taskName=(String)((Object[])RFC.getParameters())[2];
							String targetIP=(String)((Object[])RFC.getParameters())[3];
							Object O=new Object[]{fileName,questID,taskName};
							MyComputerHandler.addData(new ApplicationData("requesttask",O,0,targetIP),targetIP,ApplicationData.OUTSIDE);
						}else
						
						//Request another player's webpage.
						if(RFC.getFunction().equals("requestwebpage")){
						
							String target_ip=(String)((Object[])RFC.getParameters())[0];
							String source_ip=(String)((Object[])RFC.getParameters())[1];
							
							if(!(source_ip.equals("062.153.7.142")))//This is the IP used to hook-in and make requests externally.
								source_ip=crypt(source_ip,clientKey);
								
							HashMap Parameters=(HashMap)((Object[])RFC.getParameters())[2];
							
							if(Parameters==null)
								Parameters=new HashMap();
							Parameters.put("packetid",new Integer(RFC.getID()));
														
							if(target_ip.length()>=5)
							if(target_ip.substring(0,5).toLowerCase().equals("store"))
								target_ip="store"+serverID;
							
							MyComputerHandler.addData(new ApplicationData("requestwebpage",Parameters,0,source_ip),target_ip,ApplicationData.OUTSIDE);
						}else
						
						//Send a form submission to another player.
						if(RFC.getFunction().equals("submit")){
							String target_ip=(String)((Object[])RFC.getParameters())[0];
							String source_ip=(String)((Object[])RFC.getParameters())[1];
							HashMap Parameters=(HashMap)((Object[])RFC.getParameters())[2];
							if(Parameters==null)
								Parameters=new HashMap();
							Parameters.put("packetid",new Integer(RFC.getID()));
						
							if(!(source_ip.equals("062.153.7.142")))//This is the IP used to hook-in and make requests externally.
								source_ip=crypt(source_ip,clientKey);
							
							MyComputerHandler.addData(new ApplicationData("submit",Parameters,0,source_ip),target_ip,ApplicationData.OUTSIDE);
						}else
						
						//Create a bounty.
						if(RFC.getFunction().equals("makebounty")){
							String source_ip=(String)((Object[])RFC.getParameters())[0];
							source_ip=crypt(source_ip,clientKey);
							Boolean anonymous=(Boolean)((Object[])RFC.getParameters())[1];
							String target=(String)((Object[])RFC.getParameters())[2];
							Integer type=(Integer)((Object[])RFC.getParameters())[3];
							String fname=(String)((Object[])RFC.getParameters())[4];
							String folder=(String)((Object[])RFC.getParameters())[5];
							Integer iterations=(Integer)((Object[])RFC.getParameters())[6];
							Float reward=(Float)((Object[])RFC.getParameters())[7];
							Object O[]=new Object[]{anonymous,target,type,fname,folder,iterations,reward};
							MyComputerHandler.addData(new ApplicationData("makebounty",O,0,source_ip),source_ip,ApplicationData.OUTSIDE);
						}else
						
						//Exit a player's webpage.
						if(RFC.getFunction().equals("exit")){
							String target_ip=(String)((Object[])RFC.getParameters())[0];
							String source_ip=(String)((Object[])RFC.getParameters())[1];

							source_ip=crypt(source_ip,clientKey);
							
							MyComputerHandler.addData(new ApplicationData("exit",null,0,source_ip),target_ip,ApplicationData.OUTSIDE);
						}else
						
						//Vote for a player's webpage.
						if(RFC.getFunction().equals("vote")){
							String target_ip=(String)((Object[])RFC.getParameters())[0];
							String source_ip=(String)((Object[])RFC.getParameters())[1];

							source_ip=crypt(source_ip,clientKey);
							
							MyComputerHandler.addData(new ApplicationData("vote",null,0,target_ip),source_ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("savepage")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String title=(String)((Object[])RFC.getParameters())[1];
							String body=(String)((Object[])RFC.getParameters())[2];
							Object O[]=new Object[]{title,body};
							MyComputerHandler.addData(new ApplicationData("savepage",O,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("withdraw")){
							float amount=(Float)((Object[])RFC.getParameters())[0];
							String ip=(String)((Object[])RFC.getParameters())[1];
							ip=crypt(ip,clientKey);
							int port=(Integer)((Object[])RFC.getParameters())[2];
							MyComputerHandler.addData(new ApplicationData("withdraw",amount,port,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("requestdirectory")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String path=(String)((Object[])RFC.getParameters())[1];
							Object O[]=new Object[]{path,new Integer(RFC.getID())};
							MyComputerHandler.addData(new ApplicationData("requestdirectory",O,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("unlock")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String code=(String)((Object[])RFC.getParameters())[1];
							MyComputerHandler.addData(new ApplicationData("unlock",code,0,""),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("setftppassword")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String password=(String)((Object[])RFC.getParameters())[1];
							MyComputerHandler.addData(new ApplicationData("setftppassword",password,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("requestsecondarydirectory")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							String path=(String)((Object[])RFC.getParameters())[1];
							String targetIP=(String)((Object[])RFC.getParameters())[2];
							
							targetIP=crypt(targetIP,clientKey);
														
							int port=(Integer)((Object[])RFC.getParameters())[3];
							Object Parameter[]=new Object[]{targetIP,path,new Integer(RFC.getID())};
							MyComputerHandler.addData(new ApplicationData("requestsecondarydirectory",Parameter,port,targetIP),ip,ApplicationData.OUTSIDE);
						}else

						if(RFC.getFunction().equals("requestcancelattack")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							int port=(Integer)((Object[])RFC.getParameters())[1];
							MyComputerHandler.addData(new ApplicationData("requestcancelattack",null,port,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("cluedata")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String data=(String)((Object[])RFC.getParameters())[1];
							MyComputerHandler.addData(new ApplicationData("cluedata",data,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("requestzombiecancelattack")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							int port=(Integer)((Object[])RFC.getParameters())[1];
							String targetIP=(String)((Object[])RFC.getParameters())[2];
							targetIP=crypt(targetIP,clientKey);
							MyComputerHandler.addData(new ApplicationData("requestcancelattack",null,port,targetIP),ip,ApplicationData.OUTSIDE);
						}else

						
						if(RFC.getFunction().equals("installapplication")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							int port=(Integer)((Object[])RFC.getParameters())[1];
							String path=(String)((Object[])RFC.getParameters())[2];
							String name=(String)((Object[])RFC.getParameters())[3];
							String Parameter[]=new String[]{path,name};
							MyComputerHandler.addData(new ApplicationData("installapplication",Parameter,port,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("installwatch")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String path=(String)((Object[])RFC.getParameters())[1];
							String name=(String)((Object[])RFC.getParameters())[2];
							int type=(Integer)((Object[])RFC.getParameters())[3];
							int port=(Integer)((Object[])RFC.getParameters())[4];
							Object Parameter[]=new Object[]{path,name,new Integer(type)};
							MyComputerHandler.addData(new ApplicationData("installwatch",Parameter,port,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("setwatchobservedports")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							Integer watchID=(Integer)((Object[])RFC.getParameters())[1];
							Integer ObservedPorts[]=(Integer[])((Object[])RFC.getParameters())[2];
							Object Parameter[]=new Object[]{watchID,ObservedPorts};
							MyComputerHandler.addData(new ApplicationData("setwatchobservedports",Parameter,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("installfirewall")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							int port=(Integer)((Object[])RFC.getParameters())[1];
							String path=(String)((Object[])RFC.getParameters())[2];
							String name=(String)((Object[])RFC.getParameters())[3];
							String Parameter[]=new String[]{path,name};
							MyComputerHandler.addData(new ApplicationData("installfirewall",Parameter,port,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("replaceapplication")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							int port=(Integer)((Object[])RFC.getParameters())[1];
							String path=(String)((Object[])RFC.getParameters())[2];
							String name=(String)((Object[])RFC.getParameters())[3];
							String Parameter[]=new String[]{path,name};
							MyComputerHandler.addData(new ApplicationData("replaceapplication",Parameter,port,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("uninstallport")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							int port=(Integer)((Object[])RFC.getParameters())[1];
							MyComputerHandler.addData(new ApplicationData("uninstallport",new Integer(port),port,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("portonoff")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							int port=(Integer)((Object[])RFC.getParameters())[1];
							Boolean on=(Boolean)((Object[])RFC.getParameters())[2];
							MyComputerHandler.addData(new ApplicationData("portonoff",on,port,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("peekcode")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String targetIP=(String)((Object[])RFC.getParameters())[1];
							int port=(Integer)((Object[])RFC.getParameters())[2]; 
							MyComputerHandler.addData(new ApplicationData("peekcode",null,port,ip),targetIP,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("peeklogs")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String targetIP=(String)((Object[])RFC.getParameters())[1];
							int port=(Integer)((Object[])RFC.getParameters())[2]; 
							MyComputerHandler.addData(new ApplicationData("peeklogs",null,port,ip),targetIP,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("saveportnote")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							int port=(Integer)((Object[])RFC.getParameters())[1];
							String note=(String)((Object[])RFC.getParameters())[2];
							MyComputerHandler.addData(new ApplicationData("saveportnote",note,port,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("setwatchquantity")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							Integer watchID=(Integer)((Object[])RFC.getParameters())[1];
							Float quantity=(Float)((Object[])RFC.getParameters())[2];
							Object O=new Object[]{watchID,quantity};
							MyComputerHandler.addData(new ApplicationData("setwatchquantity",O,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("setwatchonoff")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							Integer watchID=(Integer)((Object[])RFC.getParameters())[1];
							Boolean state=(Boolean)((Object[])RFC.getParameters())[2];
							Object O=new Object[]{watchID,state};
							MyComputerHandler.addData(new ApplicationData("setwatchonoff",O,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("setwatchnote")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							Integer watchID=(Integer)((Object[])RFC.getParameters())[1];
							String note=(String)((Object[])RFC.getParameters())[2];
							Object O=new Object[]{watchID,note};
							MyComputerHandler.addData(new ApplicationData("setwatchnote",O,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("setwatchsearchfirewall")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							Integer watchID=(Integer)((Object[])RFC.getParameters())[1];
							Integer searchFireWall=(Integer)((Object[])RFC.getParameters())[2];
							Object O=new Object[]{watchID,searchFireWall};
							MyComputerHandler.addData(new ApplicationData("setwatchsearchfirewall",O,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("deletewatch")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							Integer watchID=(Integer)((Object[])RFC.getParameters())[1];
							Object O=new Object[]{watchID};
							MyComputerHandler.addData(new ApplicationData("deletewatch",O,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("deletefirewall")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							Integer portID=(Integer)((Object[])RFC.getParameters())[1];
							MyComputerHandler.addData(new ApplicationData("deletefirewall",portID,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("changewatchport")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							Integer WatchID=(Integer)((Object[])RFC.getParameters())[1];
							Integer PortID=(Integer)((Object[])RFC.getParameters())[2];
							Integer I[]=new Integer[]{WatchID,PortID};
							MyComputerHandler.addData(new ApplicationData("changewatchport",I,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("changewatchtype")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							Integer WatchID=(Integer)((Object[])RFC.getParameters())[1];
							Integer PortID=(Integer)((Object[])RFC.getParameters())[2];
							Integer I[]=new Integer[]{WatchID,PortID};
							MyComputerHandler.addData(new ApplicationData("changewatchtype",I,0,ip),ip,ApplicationData.OUTSIDE);
						}

						if(RFC.getFunction().equals("deletefolder")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String directory=(String)((Object[])RFC.getParameters())[1];
							MyComputerHandler.addData(new ApplicationData("deletefolder",directory,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("setdummyport")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							int port=(Integer)((Object[])RFC.getParameters())[1];
							Boolean dummy=(Boolean)((Object[])RFC.getParameters())[2];
							MyComputerHandler.addData(new ApplicationData("setdummyport",dummy,port,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("changedailypay")){
							Object[] parameters = (Object[])RFC.getParameters();
							String ip=(String)parameters[0];
							int port=(Integer)parameters[1];
							String change=(String)parameters[2];
							String finalizeIP=(String)parameters[3];
							int attackPort = (Integer)parameters[4];
							finalizeIP=crypt(finalizeIP,clientKey);
							
							MyComputerHandler.addData(new ApplicationData("changedailypay",new Object[]{change,attackPort},port,finalizeIP),ip,ApplicationData.OUTSIDE);
						}else

						if(RFC.getFunction().equals("deletelogs")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							MyComputerHandler.addData(new ApplicationData("deletelogs",null,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("createfolder")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String directory=(String)((Object[])RFC.getParameters())[1];
							MyComputerHandler.addData(new ApplicationData("createfolder",directory,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("put")){						
							String ip=(String)((Object[])RFC.getParameters())[0];
							int port=(Integer)((Object[])RFC.getParameters())[1];
							String name=(String)((Object[])RFC.getParameters())[2];
							String fetch_path=(String)((Object[])RFC.getParameters())[3];
							String put_path=(String)((Object[])RFC.getParameters())[4];
							String targetIP=(String)((Object[])RFC.getParameters())[5];
							targetIP=crypt(targetIP,clientKey);
							String password=(String)((Object[])RFC.getParameters())[6];
							Integer quantity=(Integer)((Object[])RFC.getParameters())[7];
							Object Parameter[]=new Object[]{ip,name,fetch_path,put_path,password,quantity};
							MyComputerHandler.addData(new ApplicationData("put",Parameter,port,targetIP),targetIP,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("get")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							int port=(Integer)((Object[])RFC.getParameters())[1];
							String name=(String)((Object[])RFC.getParameters())[2];
							String fetch_path=(String)((Object[])RFC.getParameters())[3];
							String put_path=(String)((Object[])RFC.getParameters())[4];
							String targetIP=(String)((Object[])RFC.getParameters())[5];
							targetIP=crypt(targetIP,clientKey);
							String password=(String)((Object[])RFC.getParameters())[6];
							Integer quantity=(Integer)((Object[])RFC.getParameters())[7];
							Object Parameter[]=new Object[]{targetIP,name,fetch_path,put_path,password,quantity};
							MyComputerHandler.addData(new ApplicationData("get",Parameter,port,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("malget")){
							Object[] parameters = (Object[])RFC.getParameters();
							String ip=(String)parameters[0];
							int port=(Integer)parameters[1];
							String name=(String)parameters[2];
							String fetch_path=(String)parameters[3];
							String put_path=(String)parameters[4];
							String targetIP=(String)parameters[5];
							int attackPort = (Integer)parameters[6];
							targetIP=crypt(targetIP,clientKey);
							Object Parameter[]=new Object[]{targetIP,name,fetch_path,put_path,"",port,attackPort};
							MyComputerHandler.addData(new ApplicationData("malget",Parameter,port,targetIP),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("requestfile")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String path=(String)((Object[])RFC.getParameters())[1];
							String name=(String)((Object[])RFC.getParameters())[2];
							String Parameter[]=new String[]{path,name};
							MyComputerHandler.addData(new ApplicationData("requestfile",Parameter,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("requestgame")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String path=(String)((Object[])RFC.getParameters())[1];
							String name=(String)((Object[])RFC.getParameters())[2];
							String Parameter[]=new String[]{path,name};
							MyComputerHandler.addData(new ApplicationData("requestgame",Parameter,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("requestscan")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String targetIP=(String)((Object[])RFC.getParameters())[1];
							if(!ip.equals(targetIP))
								MyComputerHandler.addData(new ApplicationData("requestscan",ip,0,ip),targetIP,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("savefile")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String path=(String)((Object[])RFC.getParameters())[1];
							HackerFile name=(HackerFile)((Object[])RFC.getParameters())[2];
							Object Parameter[]=new Object[]{path,name};
							MyComputerHandler.addData(new ApplicationData("savefile",Parameter,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("compilefile")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String path=(String)((Object[])RFC.getParameters())[1];
							HackerFile name=(HackerFile)((Object[])RFC.getParameters())[2];
							Float price=(Float)((Object[])RFC.getParameters())[3];
							Object Parameter[]=new Object[]{path,name,price};
							MyComputerHandler.addData(new ApplicationData("compilefile",Parameter,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
                        if (RFC.getFunction().equals("deletemulti")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
                            
                            Object[] allFiles = (Object[])((Object[])RFC.getParameters())[1];
							Object parameters[]=new Object[]{allFiles};
							MyComputerHandler.addData(new ApplicationData("deletemulti",parameters,0,ip),ip,ApplicationData.OUTSIDE);
                        
                        }else
                        
						if(RFC.getFunction().equals("deletefile")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String path=(String)((Object[])RFC.getParameters())[1];
							String name=(String)((Object[])RFC.getParameters())[2];
							Object Parameter[]=new Object[]{path,name};
							MyComputerHandler.addData(new ApplicationData("deletefile",Parameter,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("setfiledescription")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String path=(String)((Object[])RFC.getParameters())[1];
							String name=(String)((Object[])RFC.getParameters())[2];
							String description=(String)((Object[])RFC.getParameters())[3];
							Object Parameter[]=new Object[]{path,name,description};
							MyComputerHandler.addData(new ApplicationData("setfiledescription",Parameter,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("setfileprice")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String path=(String)((Object[])RFC.getParameters())[1];
							String name=(String)((Object[])RFC.getParameters())[2];
							Float price=(Float)((Object[])RFC.getParameters())[3];
							Object Parameter[]=new Object[]{path,name,price};
							MyComputerHandler.addData(new ApplicationData("setfileprice",Parameter,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("emptypettycash")){
							Object[] parameters = (Object[])RFC.getParameters();
							String ip=(String)parameters[0];
							ip=crypt(ip,clientKey);
							String targetIP=(String)parameters[1];
							int targetPort=(Integer)parameters[2];
							int windowHandle = (Integer)parameters[3];
							MyComputerHandler.addData(new ApplicationData("emptyPettyCash",windowHandle,targetPort,ip),targetIP,ApplicationData.OUTSIDE);
						}
                        
                        else if (RFC.getFunction().equals("finalizecancelled")) {
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String targetIP=(String)((Object[])RFC.getParameters())[1];
							int targetPort=(Integer)((Object[])RFC.getParameters())[2];
							MyComputerHandler.addData(new ApplicationData("finalizecancelled",null,targetPort,ip),targetIP,ApplicationData.OUTSIDE);
                        } 
                        
                        else if (RFC.getFunction().equals("requestattack")) {
							Object[] parameters = (Object[])RFC.getParameters();
							String targetIP=(String)parameters[0];
							int targetPort=(Integer)parameters[1];
							String sourceIP=(String)parameters[2];
							sourceIP=crypt(sourceIP,clientKey);
							
							int sourcePort=(Integer)parameters[3];
							
							Integer secondaryPorts[]=(Integer[])parameters[4];
							String scripts[][]=(String[][])parameters[5];
							Object extraInfo[]=(Object[])parameters[6];
							Integer windowHandle = (Integer)parameters[7];

							Object Parameters[]=new Object[]{targetIP,new Integer(targetPort),secondaryPorts,scripts,extraInfo,windowHandle};
							ApplicationData AD=new ApplicationData("requestattack",Parameters,sourcePort,sourceIP);
							
							if(!targetIP.equals(sourceIP)&&targetIP.indexOf("store")==-1)
								MyComputerHandler.addData(AD,sourceIP,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("requestzombieattack")){
							String targetIP=(String)((Object[])RFC.getParameters())[0];
							int targetPort=(Integer)((Object[])RFC.getParameters())[1];
							String sourceIP=(String)((Object[])RFC.getParameters())[2];
							int sourcePort=(Integer)((Object[])RFC.getParameters())[3];
							
							Integer I[]=(Integer[])((Object[])RFC.getParameters())[4];
							String S[][]=(String[][])((Object[])RFC.getParameters())[5];
							Object O[]=(Object[])((Object[])RFC.getParameters())[6];
							String parentIP=(String)((Object[])RFC.getParameters())[7];
							parentIP=crypt(parentIP,clientKey);

							Object Parameters[]=new Object[]{targetIP,new Integer(targetPort),I,S,O,sourceIP};
							ApplicationData AD=new ApplicationData("requestzombieattack",Parameters,sourcePort,parentIP);
							
							if(!targetIP.equals(sourceIP)&&targetIP.indexOf("store")==-1)
								MyComputerHandler.addData(AD,parentIP,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("transfer")){
							float amount=(Float)((Object[])RFC.getParameters())[0];
							String ip=(String)((Object[])RFC.getParameters())[1];
							ip=crypt(ip,clientKey);
							String target_ip=(String)((Object[])RFC.getParameters())[2];
							int port=(Integer)((Object[])RFC.getParameters())[3];
							Object tO[]=new Object[]{target_ip,new Float(amount)};
							MyComputerHandler.addData(new ApplicationData("transfer",tO,port,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("deposit")){
							float amount=(Float)((Object[])RFC.getParameters())[0];
							String ip=(String)((Object[])RFC.getParameters())[1];
							ip=crypt(ip,clientKey);
							int port=(Integer)((Object[])RFC.getParameters())[2];
							MyComputerHandler.addData(new ApplicationData("deposit",amount,port,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("dochallenge")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String code=(String)((Object[])RFC.getParameters())[1];
							String challengeID=(String)((Object[])RFC.getParameters())[2];

							Object O[]=new Object[]{code,challengeID};
							MyComputerHandler.addData(new ApplicationData("dochallenge",O,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						//Allows a player to sell a file back to the game store.
						if(RFC.getFunction().equals("sellfile")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String location=(String)((Object[])RFC.getParameters())[1];
							String fileName=(String)((Object[])RFC.getParameters())[2];
							Float compileCost=(Float)((Object[])RFC.getParameters())[3];
							Integer quantity = (Integer)((Object[])RFC.getParameters())[4];
							Object O[]=new Object[]{location,fileName,compileCost,ip,quantity};
							MyComputerHandler.addData(new ApplicationData("requestsellfile",O,0,"store"+serverID),ip,ApplicationData.OUTSIDE);
						}else
                        
                        // Sell multiple files at once from the client
                        if (RFC.getFunction().equals("sellfilemulti")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
                            Object[] allFiles = (Object[])((Object[])RFC.getParameters())[1];
							Object O[]=new Object[]{allFiles,ip};
                            MyComputerHandler.addData(new ApplicationData("sellfilemulti"  ,O,0,"store"+serverID),ip,ApplicationData.OUTSIDE);
                        }else
						
						//Allows a player to sell a file back to the game store.
						if(RFC.getFunction().equals("decompilefile")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							ip=crypt(ip,clientKey);
							String location=(String)((Object[])RFC.getParameters())[1];
							String fileName=(String)((Object[])RFC.getParameters())[2];
							Float compileCost=(Float)((Object[])RFC.getParameters())[3];
							Object O[]=new Object[]{location,fileName,compileCost,ip};
							MyComputerHandler.addData(new ApplicationData("decompilefile",O,0,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						//A deposit requested from facebook.
						if(RFC.getFunction().equals("facebookdeposit")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							Float amount=(Float)((Object[])RFC.getParameters())[1];
							int defaultPort=(Integer)((Object[])RFC.getParameters())[2];

							MyComputerHandler.addData(new ApplicationData("deposit",amount,defaultPort,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						//A withdraw requested from facebook.
						if(RFC.getFunction().equals("facebookwithdraw")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							Float amount=(Float)((Object[])RFC.getParameters())[1];
							int defaultPort=(Integer)((Object[])RFC.getParameters())[2];

							MyComputerHandler.addData(new ApplicationData("withdraw",amount,defaultPort,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("facebooktransfer")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							String ip2=(String)((Object[])RFC.getParameters())[1];

							Float amount=(Float)((Object[])RFC.getParameters())[2];
							int defaultPort=(Integer)((Object[])RFC.getParameters())[3];

							Object tO[]=new Object[]{ip2,new Float(amount)};
							MyComputerHandler.addData(new ApplicationData("transfer",tO,defaultPort,ip),ip,ApplicationData.OUTSIDE);
						}else
						
						if(RFC.getFunction().equals("facebookupdate")){
							String ip=(String)((Object[])RFC.getParameters())[0];
							MyComputerHandler.addData(new ApplicationData("facebookupdate",null,0,ip),ip,ApplicationData.OUTSIDE);
						}
                        
                        else if (RFC.getFunction().equals("setpreferences")) {
                            String ip=(String)((Object[])RFC.getParameters())[0];
                            ip=crypt(ip,clientKey);
                            HashMap preferences = (HashMap)((Object[])RFC.getParameters())[1];
                            Object[] O = new Object[] {ip, preferences};
                            MyComputerHandler.addData(new ApplicationData("setpreferences",O,0,ip),ip,ApplicationData.OUTSIDE);
                        }

						
					}else
					
					if(MyAssignment instanceof Object[]){
						Assignment DispatchMe=(Assignment)((Object[])MyAssignment)[0];
						int connectionID=(Integer)((Object[])MyAssignment)[1];
						ClientBinaryList MyClientBinaryList=this.getEditor().getClients();
						ClientData MyClientData=(ClientData)MyClientBinaryList.get(new Integer(connectionID));
						if(MyClientData!=null){
							MyClientData.addJob(new ZippedAssignment(0,DispatchMe));
						}
					}
					
				}
				
				Thread.sleep(1);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
	}

	Random rnd = null;
	private long startTime = 0;
	
    /**
     * De/encrypts binary data in a given byte array. Calling the method again
     * reverses the encryption.
     */
    public String crypt (String ip,String clientHash)
    {
		return((String)Keys.get(ip+clientHash));
	}
	
	
	/**
	Remove a key from our encryption system based on an IP provided.
	*/
	public synchronized void removeRandomKey(String ip){
		String hash=(String)IPs.get(ip);
		if(ip!=null)
			Keys.remove(hash);
		IPs.remove(ip);
		MyEncryption.remove(ip);
	}
	

	/**
	Generates a random key for use with our encryption algorithm.
	*/
	public synchronized Object[] getRandomKey(String ip,String clientHash,byte publicKey[]){
			
		String key="";
		for(int i=0;i<10;i++){
			int ii=(int)(Math.random()*26);
			char c=(char)('a'+ii);
			key+=c;
		}
		
		Keys.put(key+clientHash,ip);
		IPs.put(ip,key+clientHash);
		byte myPublicKey[]=MyEncryption.init(publicKey,clientHash,ip);
		
		Object returnMe[]=new Object[]{key,myPublicKey};
		
		return(returnMe);
	}
	
	/**
	Create an instance of the server.
	*/
    public static void main(String args[]){
		/*try{
			File f = new File("error.log");
			PrintStream ps = new PrintStream(f);
			System.setErr(ps);
		}catch(Exception e){}*/
        try{
            Editor E=new Editor(2048,1000,10020,10021);//Creates a new server for distributing tasks.
            E.setClientJobSize(4);
			if(args.length==2&&args[1].equals("test"))
				TESTING=true;
            HackerServer HS=new HackerServer(E,args[0]);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
