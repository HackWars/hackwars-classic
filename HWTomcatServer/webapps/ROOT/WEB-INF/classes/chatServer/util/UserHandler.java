package util;

/**
UserHandler.java<br />
(c) Vulgate 2007<br />

Handles a list of connected users, manages sending packets and the like.
*/

import java.util.*;
import com.plink.dolphinnet.util.*;
import com.plink.dolphinnet.*;
import util.*;
import Server.*;
import Assignments.*;
import chat.server.*;
import chat.messages.*;
import java.util.concurrent.Semaphore;

public class UserHandler implements Runnable{
    //Data.
    private Time MyTime=null;
	private UserBinaryList MyUserBinaryList=new UserBinaryList();
    private static final long sleepTime=5;
    private ArrayList ExecuteStack=new ArrayList();
    private Thread MyThread=null;
	private ChatServer MyChatServer=null;
	private MainServer MyMainServer=null;
	private final Semaphore available = new Semaphore(1, true);//Make it thread safe.
    
    public UserHandler(Time MyTime,ChatServer MyChatServer){
		try{
			MyMainServer=new MainServer();
		}catch(Exception e){
			e.printStackTrace();
		}
	
		this.MyChatServer=MyChatServer;
        this.MyTime=MyTime;
        MyThread=new Thread(this,"User Handler Thread.");
        MyThread.start();
    }
	
    //Private classes.
    private class UserTask{
        private int connectionID;
        private Thread T=null;
		private String user;
		private String pass;
		private boolean failed=false;
		private boolean loaded=false;
		private boolean relogged=false;
		private User MyUser=null;
        public UserTask(int connectionID,String user,String pass){
			this.user=user;
			this.pass=pass;
			this.connectionID=connectionID;
            MyUser=(User)MyUserBinaryList.get(this.user);
			if(MyUser!=null)
				relogged=true;
			run();
        }
		
		public int getConnectionID(){
			return(connectionID);
		}
        
        public boolean getLoaded(){
            return(loaded);
        }
        
		public boolean getFailed(){
			return(failed);
		}
        
    public void run(){
        if(relogged){
            MyUser.setConnectionID(connectionID);
            MyUser.reconnect();
            System.out.println("Player Reconnected.");
        }else{
            User U=new User(MyTime,user,MyMainServer);
            U.setConnectionID(connectionID);
            MyUserBinaryList.add(U);
            System.out.println("Player Connect for the first time.");
        }
            loaded=true;
            System.out.println("Player is now loaded.");
        }
    }

    
    public void loadPlayer(int connectionID,String user,String pass){
		try{
			available.acquire();
			System.out.println("Loading Player"+user+" >>> "+pass);
			ExecuteStack.add(new UserTask(connectionID,user,pass));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			available.release();
		}
    }
	
	/**
	Add packet.
	*/
	public void addPacket(MessageInPacket MIP){
		try{
			available.acquire();
			ExecuteStack.add(MIP);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			available.release();
		}
	}
	
	/**
	Ping a player.
	*/
	public void pingPlayer(PingAssignment MyPingAssignment){
		try{
			System.out.println("Starting to ping.");
			available.acquire();
			ExecuteStack.add(MyPingAssignment);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			available.release();
		}
		System.out.println("Ping Finished.");
	}
    
    /**
    Fetch execution tasks from the stack.
    */
    public void run(){
        while(true){
            long startTime=MyTime.getCurrentTime();
			Iterator MyIterator=null;

			try{
				
				available.acquire();
				MyIterator=ExecuteStack.iterator();

				//Check for users to load.
				while(MyIterator.hasNext()){
					Object o=MyIterator.next();
				
					if(o instanceof UserTask){
						UserTask MyUserTask=(UserTask)o;
						if(MyUserTask.getLoaded()){
							MyIterator.remove();
						}else
						
						if(MyUserTask.getFailed()){
							//Tell the game server that the login has failed.
							MyIterator.remove();
							MyChatServer.logout(MyUserTask.getConnectionID());
						}
					}else if(o instanceof MessageInPacket){
						//System.out.println("Got a new message in.");
						//System.out.println(((MessageInPacket)o).getMessageIn().toString());
						MyIterator.remove();
						MyMainServer.queMessage(((MessageInPacket)o).getMessageIn());
					}else if(o instanceof PingAssignment){
						MyIterator.remove();
						String user=((PingAssignment)o).getUser();
						User MyUser=(User)MyUserBinaryList.get(user);
						if(MyUser!=null){
							MyUser.ping();
						}
					}
				}
				
			
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				available.release();
			}
			
			//Generate current packet updates.
			try{
				available.acquire();
				MyIterator=MyUserBinaryList.getData().iterator();
				while(MyIterator.hasNext()){
					User MyUser=(User)MyIterator.next();
					if(MyUser.packetTimeOut()&&!MyUser.playerTimeOut()){
						Assignment A=MyUser.getPacket();
						if(A!=null){
							MyChatServer.dispatchPacket(A,MyUser.getConnectionID());
							System.out.println("Sending Packet to player.");
						}
					}else if(MyUser.playerTimeOut()){
						System.out.println("Starting to boot player.");
						MyIterator.remove();
						System.out.println("Trying to start logout.");
						MyUser.logout();
						System.out.println("You're probably hung here.");
						MyChatServer.logout(MyUser.getConnectionID());
						System.out.println("Player booted.");
					}
				}
			
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				available.release();
			}
            
            try{
             long endTime=MyTime.getCurrentTime();
             if(sleepTime-(endTime-startTime)>0)
                MyThread.sleep(sleepTime-(endTime-startTime));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

