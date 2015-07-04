package HackerLogin;
/**
CheckOutHandler.java

Controls the distribution and saving of profiles.
*/
import java.util.*;
import java.io.*;
import util.*;
import java.util.concurrent.Semaphore;

public class CheckOutHandler implements Runnable{
	private static final String DATA_PATH="/Users/benjamincoe/desktop/Programming/HackerSearch/data/";

	private String Connection="127.0.0.1";
	private String DB="hackwars";
	private String Username="root";
	private String Password="";
	
	private static final String ERROR_STRING="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
	private static HashMap CheckedOut=new HashMap();
	
	private static final long sleepTime = 500;//How long should it sleep after saving a profile.
	private static boolean running=false;//The variable that keeps track of whether it is running.
	private static final Semaphore available = new Semaphore(1, true);
	private static ArrayList Work=new ArrayList();
	
	public CheckOutHandler(){
		if(running==false){
			running=true;
			Thread MyThread=new Thread(this);
			Thread MyThread2=new Thread(new CheckOutHandler());
			Thread MyThread3=new Thread(new CheckOutHandler());
			Thread MyThread4=new Thread(new CheckOutHandler());
			Thread MyThread5=new Thread(new CheckOutHandler());
			MyThread.start();
			MyThread2.start();
			MyThread3.start();
			MyThread4.start();
			MyThread5.start();
		}
	}
	
	/**
	Checkout a profile.
	*/
	public String checkOutPlayer(String ip,String serverID,boolean active){
		String fetchedServerID=(String)CheckedOut.get(ip);
		if(fetchedServerID==null){
			CheckedOut.put(ip,serverID);
		}
		
		if(fetchedServerID==null||fetchedServerID.equals(serverID)){
			try{
				String data=fetchProfile(ip,active);
				if(data==null){
					File f=new File(DATA_PATH+ip.replaceAll("\\.","")+".xml");
					BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
					String read="";
					data="";
					while((read=in.readLine())!=null){
						data+=read+"\n";
					}
					in.close();
				}
				if(data.equals("inactive")){
					return(ERROR_STRING+"<error><type>1</type><message>The player you attempted to connect to is inactive.</message></error>");
				}
				return(data);
			}catch(Exception e){
				return(ERROR_STRING+"<error><type>1</type><message>The player you attempted to connect to could not be found.</message></error>");
			}
		}
		
		return(ERROR_STRING+"<error><type>0</type><message>Profile is currently being accessed on server "+fetchedServerID+".</message></error>");
	}
	
	/**
	Create the initial file for a player.
	*/
	public void createPlayer(String password,String ip,String username){
		if(password.equals("awa878")){
			try{
				String FileData="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>"+
								"<save>"+
								"<ip>"+ip+"</ip>"+
								"<name><![CDATA["+username+"]]></name>"+
								"<password>public</password>"+
								"<picture>2</picture>"+
								"<hits>0</hits>"+
								"<cputype>0</cputype>"+
								"<memorytype>0</memorytype>"+
								"<hdtype>0</hdtype>"+
								"<lastpaid>0</lastpaid>"+
								"<pettycash>0.0</pettycash>"+
								"<bank>3000.0</bank>"+
								"<defaultattack>0</defaultattack>"+
								"<defaultbank>0</defaultbank>"+
								"<defaultftp>0</defaultftp>"+
								"<defaulthttp>0</defaulthttp>"+
								"<stats>"+
								"<attackxp>0</attackxp>"+
								"<merchantingxp>0</merchantingxp>"+
								"<firewallxp>0</firewallxp>"+
								"<watchxp>0</watchxp>"+
								"<scanningxp>0</scanningxp>"+
								"</stats>"+
								"<ports>"+
								"</ports>"+
								"<watches>"+
								"</watches>"+
								"<files>"+
								"<directory><![CDATA[Store/]]></directory>"+
								"<directory><![CDATA[Public/]]></directory>"+
								"</files>"+
								"<website>"+
								"<adrevenue>"+ip+"</adrevenue>"+
								"<storerevenue>"+ip+"</storerevenue>"+
								"<title><![CDATA["+username+"'s Home Page]]></title>"+
								"<body><![CDATA[ ]]></body>"+
								"</website>"+
								"</save>";
				FileData.replaceAll("\"","\\\\\"");
				sql C=new sql(Connection,DB,Username,Password);
				ArrayList result=null;
				String Q="insert into user values(\""+ip+"\",\""+FileData+"\",NULL);";
				C.process(Q);	
				C.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	Return a profile.
	*/
	public void returnPlayer(String ip,String content){
		try{
			available.acquire();
			Work.add(new String[]{ip,content});
			available.release();
		}catch(Exception e){
			available.release();
		}
	}
	
	public String fetchProfile(String ip,boolean checkActive){
		sql C=new sql(Connection,DB,Username,Password);
		ArrayList result=null;
		try{

		if(checkActive){
			String query = "SELECT npc,TO_DAYS(NOW())-TO_DAYS(last_logged_in) FROM hackerforum.users WHERE ip=\""+ip+"\"";
			System.out.println(query);
			result = C.process(query);
			if(result==null)
				return null;
			String npc = (String)result.get(0);
			String active = (String)result.get(1);
			if(npc.equals("N")){
				if((int)Integer.parseInt(active)>14)
					return "inactive";
			}
		}
		String Q="select stats from user where ip=\""+ip+"\"";
		System.out.println(Q);

		result=C.process(Q);	
		C.close();
		}catch(Exception e){
			return "inactive";
		}
		
		if(result==null)
			return(null);
		else{
			return((String)result.get(0));
		}
	}
	
	public static void main(String args[]){
		String content="{text-decoration: none;color:green;}";
		content = content.replaceAll("\\\\","\\\\\\\\");
		content = content.replaceAll("\"","\\\\\"");
		content = content.replaceAll("\\{","\\\\{");
		System.out.println(content);
	}
	
	public synchronized void run(){
		while(running){
		try{
			try{
				available.acquire();
				if(Work.size()>0){
					String Data[]=(String[])Work.remove(0);
					String ip=Data[0];
					String content=Data[1];
					
					try{
						if(fetchProfile(ip,false)!=null){
							sql C=new sql(Connection,DB,Username,Password);
							ArrayList result=null;
							content = content.replaceAll("\\\\","\\\\\\\\");
							content = content.replaceAll("\"","\\\\\"");				
							
							String Q="update user set stats=\""+content+"\" where ip=\""+ip+"\";";
							C.process(Q);	
							C.close();
						}
						
						System.out.println("Saved Profile: "+ip);
						}catch(Exception e){
							System.out.println("Saving Failed IP: "+ip);
							e.printStackTrace();
						}
						
						CheckedOut.remove(ip);
				}
				available.release();
			}catch(Exception e){
				available.release();
			}
			
			Thread.sleep(sleepTime);
		}catch(Exception e){}
		}
	}
	
}

