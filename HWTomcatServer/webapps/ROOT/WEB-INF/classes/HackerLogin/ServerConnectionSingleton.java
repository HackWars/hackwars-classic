package HackerLogin;
/**
ServerConnectionSingleton.java

A Client for connecting to the main game server.
*/
import Game.*;
import Assignments.*;
import Server.*;
import com.plink.dolphinnet.*;
import util.*;


public class ServerConnectionSingleton implements Runnable,DataHandler{

	public static final int PINGTIME=30000;
	private long lastPing;
	private String server="localhost";
	private Reporter R=null;
	private static ServerConnectionSingleton MyServerConnectionSingleton=null;
	private boolean RESPONSE=false;//Has a response been received.
	private static final long TIME_OUT=5000;//How long should we wait for a connection.
	private static final long RESPONSE_TIME_OUT=15000;//How long should we wait for a response.
	private Time MyTime=Time.getInstance();
	private Thread MyThread=null;
	
	private ServerConnectionSingleton(){
		R=new Reporter(server,200000,10021,10020);
		R.setDataHandler(this);
		connect(R,"","","",false);
		lastPing=MyTime.getCurrentTime();
		MyThread=new Thread(this);
		MyThread.start();
	}
	
	public static ServerConnectionSingleton getInstance(){
		if(MyServerConnectionSingleton==null){
			MyServerConnectionSingleton=new ServerConnectionSingleton();
		}
		
		return(MyServerConnectionSingleton);
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
				R.addFinishedAssignment(new PingAssignment(0,""));
			RESPONSE=true;
		}else
			RESPONSE=false;
	}
	
	/**
	Called by Data handler to return assignments to front-end.
	*/
	public void addData(Object o){
		if(o instanceof PingAssignment){
			RESPONSE=true;
		}
	}
	
	public void addFinishedAssignment(Assignment A){};
	public Object getData(int i){return(null);}
	public void resetData(){};
	
	public synchronized void returnAssignment(Assignment A){
		R.addFinishedAssignment(A);
	}

	public void run(){
		while(true){
			try{
				if(MyTime.getCurrentTime()-lastPing>PINGTIME){
					if(R!=null&&R.getID()!=-1){
						lastPing=MyTime.getCurrentTime();
						R.addFinishedAssignment(new PingAssignment(0,""));
						System.out.println("Pinging.");
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	//testing main.
	public static void main(String args[]){
		ServerConnectionSingleton test=ServerConnectionSingleton.getInstance();
	}
}
