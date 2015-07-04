package util;

/**
User.java<br />
&copy; Vulgate 2007<br /><br />

This class represents the user.
*/

import java.util.*;
import Assignments.*;
import util.*;
import chat.messages.*;
import chat.server.*;
import com.plink.dolphinnet.*;

public class User{
    //data
    private int connectionID;
    private Time MyTime;
    private String Name;
    private long lastAccessed;
    private long lastSent;
    private static final long packetRate=500;
//	private static final long playerTimeOut=120000;
	private static final long playerTimeOut=90000;

	private UserMsgBox MyUserMsgBox=null;
	private MainServer MyMainServer=null;

    //constructor
    public User(Time MyTime,String Name,MainServer MyMainServer){
		this.MyTime=MyTime;
        this.Name = Name;
		try{
		
			System.out.println("Logged in <" + Name + ">");
			MyUserMsgBox=MyMainServer.userLogsOn(Name);
			MyMainServer.autoChannel(Name,"General");
			MyMainServer.autoChannel(Name,"Trade");
			MyMainServer.autoChannel(Name,"Help");
			
                       
                        
			//MyMainServer.queMessage( new MsgInChannelCreate(Name,"Main Game Chat", null) );
		}catch(Exception e){
			e.printStackTrace();
		}
		lastAccessed=MyTime.getCurrentTime();
		this.MyMainServer=MyMainServer;
    }
	
	/**
	Reconnect a player to the server.
	*/
	public void reconnect(){
		System.out.println("Reconnecting <" + Name + ">s");
		try{
			
			MyUserMsgBox=MyMainServer.userLogsOn(Name);
			MyMainServer.autoChannel(Name,"General");
			MyMainServer.autoChannel(Name,"Trade");
			MyMainServer.autoChannel(Name,"Help");
			System.out.println("Reconneted SUCCESS <" + Name + ">s");
		
		}catch(Exception e){
			System.out.println("Reconnecting ERROR <" + Name + ">s");
			e.printStackTrace();
		}
		lastAccessed=MyTime.getCurrentTime();
	}
	
	/**
	Get the unique name associated with this user.
	*/
	public String getName(){
		return(Name);
	}
	
	/**
	Ping the player.
	*/
	public void ping(){
		lastAccessed=MyTime.getCurrentTime();
	}
	
	/**
	Has a player timed out?
	*/
	public boolean playerTimeOut(){
		if(MyTime.getCurrentTime()-lastAccessed>playerTimeOut)
			return(true);
		return(false);
	}

    /**
    setConnectionID(int connectionID)<br />
    sets the connection id.
    */
    public void setConnectionID(int connectionID){
        this.connectionID = connectionID;
    }

    /**
    getConnectionID()<br />
    returns the connection id.
    */
    public int getConnectionID(){
        return(connectionID);
    }
	
	/**
	Is it time to send another packet?
	*/
	public boolean packetTimeOut(){
		if(MyTime.getCurrentTime()-lastSent>packetRate)
			return(true);
		return(false);
	}
	
	/**
	Logout a player.
	*/
	public void logout(){
		try{
			System.out.println("Logging off <" + this.Name + ">");
			MyMainServer.userLoggedOff(Name);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
    /**
    getPacket() <br />
    returns a packet
    */
    public Assignment getPacket(){
	
        lastSent=MyTime.getCurrentTime();

        if(MyUserMsgBox==null){
                //System.out.println("=====================");
                System.out.println("Util.User Error msg box null: "+Name);
                //System.out.println("Broke Everything.");
                return(null);
        }

        ArrayMessageOut AMO=MyUserMsgBox.popMessage();
        try{
             System.out.println("Logging off <" + AMO.getMessage(0).getClass().getName() + ">");
        }catch(Exception e){

        }
        
        if(AMO!=null){
            return(new MessageOutPacket(AMO));
        }else{ 
            return(null);
        }
    }
}//class

