package View;
/**
(C) Ben Coe 2007 <br />
The main controller for Coezilla.
*/

import javax.swing.*;
import com.plink.dolphinnet.*;
import com.plink.dolphinnet.assignments.*;
import Assignments.*;
import java.util.*;
import util.*;
import GUI.*;
import java.util.concurrent.Semaphore;
import Game.*;
import Applet.*;
import chat.messages.*;
import java.awt.image.*;

public class View implements DataHandler,Runnable{
	////////////////////////
	// Data.
	public boolean reconnect=false;
	public static long lastPingSuccess=0;
	public static final int PINGTIMEOUT=40000;
	public static final int PINGTIME=15000;
	private Thread MyThread=null;
	private Reporter R=null;
	private Reporter chatR=null;
	private String user=null;
	private String pass=null;
	private long lastAccessed;
	private long lastPing;
	private Time MyTime=new Time();
	private long TIME_OUT=70000;
	private long CHAT_TIME_OUT=70000;
	private final Semaphore available = new Semaphore(1, true);
	private Hacker MyHacker=null;
	private ArrayList Tasks = new ArrayList();
	private String ip;
	private JFrame LoginFrame;
	private JTextField usernameField,ipField;
	private String username=null;
	private JLabel message;
	private ArrayList packets = new ArrayList();
	private boolean loaded=false;
	private Load MyLoad=null;
	private String encryptedIP;	
	private Tutorial tutorial=null;
	private String function="";
	private boolean open=false;
	private boolean run=false;
	public static int instrument=0;
	private long lastPacketSent=0;
	private boolean offline;
	//Constructor.
	public View(String ip,Load MyLoad){
		//try{
		this.ip=ip;
		this.MyLoad=MyLoad;
		//Encryption.getInstance().init();
		tutorial = new Tutorial();
		//Sound.startSound();
		
	}
	
	public Tutorial getTutorial(){
		return tutorial;
	}
	
	public Load getLoad(){
		return MyLoad;
	}
	
    /**
    * This is run from the main(), when the View is first loaded.  Also called from Load.java, which is the Applet.
    */
	public void loginToServer(String username,String password,String ip){
		this.username=username;
		this.pass=password;
		this.user=ip;
		//System.out.println("Trying to login "+username+" with password "+password);
		run=true;
//System.out.println("BEFORE RECONNECT()");
		reconnect();
//System.out.println("DONE RECONNECT()");
		MyThread=new Thread(this);
		MyThread.start();
		//addFinishedAssignment(new LoginAssignment(0,username,password));
	}
	
	public void startProgram(String username,String ip,boolean npc,String encryptedIP){
		//System.out.println("Starting Program");
		reconnect=false;
		/*this.username=username;
		loadUser(ip);
		
		reconnect();*/
		//user=ip;
		this.encryptedIP=encryptedIP;
		/*MyThread=new Thread(this);
		MyThread.start();*/
		if(MyHacker==null)
			MyHacker = new Hacker(this,username,ip,npc,encryptedIP,offline);
		else
			MyHacker.update(this,username,ip,npc,encryptedIP);
	}

	public String getIP(){
		return(ip);
	}

	/**
	Load in username and password information.
	*/
	public boolean loadUser(String ip){
		user=ip;
		//pass="Blah";
		//System.out.println("Loading User");
		return(true);
	}
	
	public void setFunction(String function){
		this.function=function;
	}

	/////////////////////////////
	//DataHandler implementation of abstract methods..
	public void addFinishedAssignment(Assignment A){
		
		if(A instanceof RemoteFunctionCall){
			RemoteFunctionCall RFC = (RemoteFunctionCall)A;
			
			Object[] o = (Object[])RFC.getParameters();
			boolean next = tutorial.checkStep(function,new Object[]{o[0]});
			//System.out.println("Checking Tutorial Step "+tutorial.getFunction()+"  "+tutorial.getCheck()+":"+next+": "+function+" "+o[0]);
			if(next)
				tutorial.nextStep();
			
			
		}
		R.addFinishedAssignment(new ZippedAssignment(0,A));
		try{
			MyThread.sleep(250);
		}catch(Exception e){}
	}
	
	public void addFinishedAssignment(MessageInPacket A){
		//System.out.println("Sending Chat Assignment");
		if(!offline){
			chatR.addFinishedAssignment(new ZippedAssignment(0,(MessageInPacket)A));
		}
	}

	public Object getData(int i){
		return(null);
	}

	public void resetData(){}

	/**
	Called by Data handler to return assignments to front-end.
	*/
	boolean connected=false;
	public synchronized void addData(Object o){
		if(MyHacker!=null&&!reconnect){
			if(o instanceof PingAssignment){
				lastPingSuccess=MyTime.getCurrentTime();
			}
		
			if(o instanceof PacketAssignment||o instanceof DamageAssignment){
				packets.add(o);
			}
			else if(o instanceof ArrayMessageOut){
				//System.out.println("Got Array Message Out");
				try{
					MyHacker.getChatController().processMessage((ArrayMessageOut)o);
				}catch(Exception e){e.printStackTrace();}
			}
			else if(o instanceof LoginSuccessAssignment){
				//System.out.println("Login Successful");
				//System.out.println("Got Unknown Packet"+o);
			}
			else if(o instanceof LoginFailedAssignment){
			//	MyLoad.setMessage("Didn't Work");
				//System.out.println("Login Failed");
			}
			lastAccessed=MyTime.getCurrentTime();//Prevent timeout.
		}
		else{
			if(o instanceof PacketAssignment){
				if(o!=null){
					packets.add((PacketAssignment)o);
					//System.out.println("Received a packet before finished creating");
				}
			}
			else if(o instanceof DamageAssignment){
				if(o!=null){
					packets.add((DamageAssignment)o);
				}
			}
			else if(o instanceof ArrayMessageOut){
				packets.add((ArrayMessageOut)o);
				//System.out.println("Got Array Message Out Before Loaded");
			}
			else if(o instanceof LoginSuccessAssignment){
				//System.out.println("Login Successful");
				LoginSuccessAssignment LSA = (LoginSuccessAssignment)o;
				Encryption.getInstance().finalize(LSA.getPublicKey());
				startProgram(username,LSA.getIP(),LSA.isNPC(),LSA.getEncryptedIP());
			}
			else if(o instanceof LoginFailedAssignment){
				LoginFailedAssignment LFA = (LoginFailedAssignment)o;
				//System.out.println("Login Failed "+LFA.getMessage());
				if(MyLoad!=null){
					MyLoad.setMessage("Login failed, check your username<br> and password.");
					//MyLoad.setMessage(LFA.getMessage());
				}
			}
		}
	}

	
	public String crypt (byte [] data,String key)
	{
		for (int ii = 0; ii < data.length;ii++) {
			data [ii] ^= (int)(key.getBytes())[ii%key.length()]; 
		}
		return(new String(data));
	}
	
	/**
	Retry connecting.
	*/
	public void reconnect(){
		//System.out.println("Connecting");
//System.out.println("ABOUT TO CREATE REPORTER");
		R=new Reporter(ip,200000,10021,10020);
//System.out.println("ABOUT TO CREATE CHAT REPORTER");
		//chatR=new Reporter("localhost",200000,10026,10025);

		R.setDataHandler(this);
		if(!offline){
			chatR=new Reporter(ip,200000,10026,10025);
			chatR.setDataHandler(this);
		}
//System.out.println("CREATED REPORTER & CHAT REPORTER");

		//Wait for handshake from server.
		boolean success=true;
		long startTime=MyTime.getCurrentTime();
		//System.out.println("Attempting to Connect to Server");
		if(!ip.equals("none")){
			while(R.getID()==-1){
				if(MyTime.getCurrentTime()-startTime>TIME_OUT){
//System.out.println("breaking, timed out1");
					break;
				}
			}
			startTime=MyTime.getCurrentTime();
			//System.out.println("Connection ID: "+R.getID());
			//System.out.println("Connecting to Chat");
			if(!offline){
				while(chatR.getID()==-1){
					if(MyTime.getCurrentTime()-startTime>CHAT_TIME_OUT){
						break;
					}
					try{
						Thread.sleep(10);
					}catch(Exception e){};
				}
			}
			//System.out.println("Connection ID: "+chatR.getID());
		}
//System.out.println("ZING");
		//System.out.println("Connected --- "+username+"   "+pass+"   "+user);

		Encryption.getInstance().init();
		String key = HashSingleton.getInstance().getHash();
		LoginAssignment MyLoginAssignment=new LoginAssignment(0,username,crypt(pass.getBytes(),key),user);
		MyLoginAssignment.setPublicKey(Encryption.getInstance().getEncodedKey());
		R.addFinishedAssignment(MyLoginAssignment);
		if(!offline){
                        LoginAssignment MyChatLoginAssignment= new LoginAssignment(0,username,crypt(pass.getBytes(),key),user);
			MyChatLoginAssignment.setPublicKey(Encryption.getInstance().getEncodedKey());
                        chatR.addFinishedAssignment(MyChatLoginAssignment);
		}
//System.out.println("YAR");
	}

	public void addFunctionCall(RemoteFunctionCall RFC){
		try{
			available.acquire();
			Tasks.add(RFC);
			available.release();
		}catch(Exception e){}
	}
	
	public void exitProgram(){
		if(MyLoad!=null)
			MyLoad.exitProgram();
	}
	
	public void finishedLoading(){
		open=true;
		if(MyLoad!=null){
			MyLoad.finishedLoading();
		}
		
	}
	
	public void clean(){
	//	System.out.println("---------------------CLEANING----------------------------");
		MyHacker=null;
			MyLoad=null;
			MyTime.clean();
			open=false;
			System.gc();
			run=false;
			lastPingSuccess=0;
		try{
			R.clean();
		}catch(Exception e){}
		try{
			if(!offline){
				chatR.clean();
			}
		}catch(Exception e){}
	}
	
	public void setOfflineMode(boolean offline){
		this.offline = offline;
	}
		

	public void run(){
		int i=0;
		while(lastPingSuccess==0)
			lastPingSuccess=MyTime.getCurrentTime();
		while(run){
			try{
				available.acquire();
				Iterator MyIterator = Tasks.iterator();

				while(MyIterator.hasNext()){
					Object o = MyIterator.next();
					if(o instanceof RemoteFunctionCall){
						addFinishedAssignment((Assignment)o);
					}
					MyIterator.remove();
				}

				available.release();
				MyThread.sleep(150);
			}catch(Exception e){
				e.printStackTrace();
			}
			if(MyHacker!=null){
				if(!MyHacker.getLoading()){
					for(int j=0;j<packets.size();j++){
						if(packets.get(j) instanceof PacketAssignment){
					
							PacketAssignment PA=(PacketAssignment)packets.get(j);
							packets.set(j,null);
						
							//System.out.println("Received a packet after finished loading");
							//System.out.println("Test");
							//System.out.println("Received Packet");
							MyHacker.setPettyCash(PA.getPettyCash());
							//System.out.println("PETTY CASH: "+PA.getPettyCash());
							MyHacker.setLoadFile(PA.getLoadFile());
							MyHacker.setLoadFile(PA.getLoadFile());
							MyHacker.setBankMoney(PA.getBankMoney());
							MyHacker.setDefaultBank(PA.getDefaultBank());
							MyHacker.setDefaultAttack(PA.getDefaultAttack());
							MyHacker.setDefaultFTP(PA.getDefaultFTP());
							MyHacker.setDefaultHTTP(PA.getDefaultHTTP());
							MyHacker.setDefaultRedirect(PA.getDefaultShipping());
							MyHacker.setCPUType(PA.getCPUType());
							MyHacker.setHDType(PA.getHDType());
							MyHacker.setHDMax(PA.getHDMaximum());
							MyHacker.setHDQuantity(PA.getHDQuantity());
							MyHacker.setMemoryType(PA.getMemoryType());
							MyHacker.setHackOMeter(PA.getHackCount());
							MyHacker.setVoteOMeter(PA.getVoteCount());
							MyHacker.setServerLoad(PA.getServerLoad());
							MyHacker.setCommodities(PA.getCommodities());
							MyHacker.getStatsPanel().getCPULoadIcon().setLoad((int)PA.getCPUCost());
							MyHacker.getStatsPanel().getCPULoadIcon().setTotalLoad((int)PA.getCPUMax());
							//System.out.println("Heal Discount: "+PA.getHealDiscount());
							MyHacker.setHealDiscount(PA.getHealDiscount());
							MyHacker.setVotesLeft(PA.getVotesLeft());
                            
                            if (PA.getPreferences() != null) {
                                MyHacker.setPreferences(PA.getPreferences());
                            }
                            
							if(PA.getCountDown()!=-1){
								//System.out.println("Start Countdown -- View");
								MyHacker.setCountDown(PA.getCountDown());
							}
							if(PA.getMessages().length!=0){
								Object messages[] = PA.getMessages();
								//System.out.println("Message Received");
								for(int ii=0;ii<messages.length;ii++)
									MyHacker.showMessage(messages[ii]);
							}
							if(PA.getPacketPorts()!=null){
								//System.out.println("GOT PORTS?");
								MyHacker.setPorts(PA.getPacketPorts());
							}
							
							if(PA.getDirectory()!=null){
								//System.out.println(MyHacker.getCurrentFolder());
								//System.out.println("received primary");
								Object dir[] = PA.getDirectory();
								MyHacker.receivedDirectory(dir);
							}
							if(PA.getSecondaryDirectory()!=null){
								//System.out.println("received secondary directory");
								MyHacker.receivedSecondaryDirectory(PA.getSecondaryDirectory(),PA.getAllowedDir());
							}
							if(PA.getFile()!=null){
								HackerFile HF = PA.getFile();
								//System.out.println("Received File from server: "+HF.getName());
								MyHacker.receivedFile(HF);
							}
							if(PA.getBody()!=null){
								//System.out.println("Title not null");
								MyHacker.receivedPage(PA.getTitle(),PA.getBody());
							}
							if(PA.getScannedPorts()!=null){
								//System.out.println("Received Scan");
								MyHacker.receivedScan(PA.getScannedPorts());
							}
							if(PA.getPacketWatches()!=null){
								MyHacker.receivedWatches(PA.getPacketWatches());
							}
							if(PA.requestPrimary()){
								int reqDir = MyHacker.getRequestedDirectory();
								Object objects[] ={MyHacker.getEncryptedIP(),MyHacker.getCurrentFolder()};
								if(reqDir != Hacker.BROWSER && reqDir != Hacker.EQUIPMENT){
									addFunctionCall(new RemoteFunctionCall(PA.getRequestPrimaryID(),"requestdirectory",objects));
								} else if (reqDir == Hacker.EQUIPMENT)  {
									addFunctionCall(new RemoteFunctionCall(Hacker.EQUIPMENT,"requestequipment",objects));									
								}
							}
							if(PA.getRequestHardware()){
								Object objects[]={MyHacker.getEncryptedIP()};
								MyHacker.setRequestedDirectory(Hacker.EQUIPMENT);
								addFunctionCall(new RemoteFunctionCall(Hacker.EQUIPMENT,"requestequipment",objects));
							}
							if(PA.requestSecondary()){
								//System.out.println("Request Secondary - "+MyHacker.getFTPIP());
								Object objects[]= {MyHacker.getFTPIP(),MyHacker.getSecondaryFolder(),MyHacker.getEncryptedIP(),MyHacker.getFTPPort()};
								addFunctionCall(new RemoteFunctionCall(Hacker.FTP,"requestsecondarydirectory",objects));
							}
							if(PA.getChoices().length!=0){
								MyHacker.showChoices(PA.getChoices());
							}
							if(PA.getPeakCode()!=null){
								//System.out.println("Peeking at Code");
								MyHacker.showCode(PA.getPeakCode());
							}
							if(PA.getLogUpdate()!=null){
								MyHacker.receivedLogUpdate(PA.getLogUpdate());
							}
							if(PA.getCAPTCHA()!=null){
								//System.out.println("Captcha!");
								BufferedImage BI = new BufferedImage(175,45,BufferedImage.TYPE_INT_ARGB);
								BI.setRGB(0,0,175,45,(int[])PA.getCAPTCHA(),0,175);
								MacroDialog MD = new MacroDialog(BI,MyHacker);
							}
							
							if(PA.getPacketNetwork()!=null){
								MyHacker.setNetwork(PA.getPacketNetwork());
							}
                            
                            
						}else if(packets.get(j) instanceof DamageAssignment){
							
							DamageAssignment DA = (DamageAssignment)packets.get(j);
							packets.set(j,null);
							
							MyHacker.setAttackXP(DA.getAttackXP());
							MyHacker.setMerchantingXP(DA.getMerchantXP());
							MyHacker.setWatchXP(DA.getWatchXP());
							MyHacker.setScanXP(DA.getScanningXP());
							MyHacker.setFirewallXP(DA.getFireWallXP());
							MyHacker.setHTTPXP(DA.getHTTPXP());
							MyHacker.setRedirectXP(DA.getRedirectXP());
							MyHacker.setRepairXP(DA.getRepairXP());
							MyHacker.getStatsPanel().getCPULoadIcon().setLoad((int)DA.getCPUCost());
							//System.out.println("DAMAGE CPU COST: "+DA.getCPUCost());
							MyHacker.setHealth(DA.getHealthUpdates());
							if(DA.getDamage()!=null){
								Object[] damage = DA.getDamage();
								Object d[];
								int windowHandle;
								float dam;
								boolean firewall = false;
								boolean mining = false;
								for(int ii=0;ii<damage.length;ii++){
									d = (Object[])damage[ii];	
									windowHandle = (Integer)d[0];
									if(d[1] instanceof Float){
										dam = (Float)d[1];
										if(d.length>4){
											String ip=(String)d[2];
											firewall = (Boolean)d[3];
											mining = (Boolean)d[4];
											MyHacker.showZombieMessage(dam,windowHandle,ip);
										}
										else{
											firewall = (Boolean)d[2];
											mining = (Boolean)d[3];
											MyHacker.showAttackMessage(dam,windowHandle,firewall,mining);
										}
									}
									else{
										boolean attacking = (Boolean)d[1];
										MyHacker.setAttacking(windowHandle,attacking);
										
									}
									//System.out.println("You damaged from port "+port+" for "+dam+" damage.");
								}
							}

						}
								
					}
					
					//Remove processed packets.
					Iterator PacketIterator=packets.iterator();
					while(PacketIterator.hasNext()){
						if(PacketIterator.next()==null)
							PacketIterator.remove();
					}
				}
				    
			}
			if(MyHacker!=null){
				try{
					ArrayMessageIn AMI=MyHacker.getChatController().popMessages();
					if(AMI!=null){
						//System.out.println("Sending Message");
						if(!offline){
							chatR.addFinishedAssignment(new MessageInPacket(AMI)); 
						}
				    }
				}catch(Exception e){e.printStackTrace();}
			
				if(MyTime.getCurrentTime()-lastPing>PINGTIME){
					lastPing=MyTime.getCurrentTime();
					//System.out.println("PINGING - "+user);
					if(user!=null){
						R.addFinishedAssignment(new PingAssignment(0,user));
						if(!offline){
							chatR.addFinishedAssignment(new PingAssignment(0,username.toLowerCase()));
						}
					}
					//System.out.println("PINGING");
				}

			}
			if(MyTime.getCurrentTime()-lastPingSuccess>PINGTIMEOUT){
				if(open){
					//System.out.println("Reconnecting");
					lastPingSuccess=MyTime.getCurrentTime();
					try{
						//System.out.println("Attempt reconnect.");
						if(R!=null){
						//	System.out.println("Calling R.clean()");
							R.clean();
						}
					}catch(Exception e){}
					try{
						if(chatR!=null)
							chatR.clean();
					}catch(Exception e){
					//	e.printStackTrace();
					}
				
					reconnect=true;
					reconnect();
				}
			}
		}
	}
	
	//Testing main.
	public static void main(String args[]){	
		try {
			
	    // Set System L&F
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    
		//System.out.println(UIManager.getCrossPlatformLookAndFeelClassName());
	    }
	    catch (UnsupportedLookAndFeelException e) {
	       System.out.println("Unsupported");
	    }
	    catch (ClassNotFoundException e) {
		    System.out.println("Class not found");
	    }
	    catch (InstantiationException e) {
		System.out.println("Instantiation Exception");
	    }
	    catch (IllegalAccessException e) {
		System.out.println("Illegal Access Exception");
	    }
    	View MyView=new View(args[0],null);
		if(args.length == 5){
			MyView.setOfflineMode(true);
		}
//System.out.println("HERE1");
        
		try {
			MyView.loginToServer(args[1],args[2],args[3]);			
//System.out.println("LOGIN TO SERVER FINISHED");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error: you must provide the hackwars URL, playername, " +
				"player's password and the players ip as arguments.");
		}
	}
}
