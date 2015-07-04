package chat.server;

import chat.messages.*;
import util.sql;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.lang.Thread;

public class MainServer {
    
    public static final int MAX_AUTO_CHANNELS = 200;
    
    private ChannelListing channels = new ChannelListing();
    private UserListing users = new UserListing();
    
    //First In Last Out (FIFO)
    //This way messages allways get sent out if throtttled,
    // Actually things are still pretty screwed if we get throttled, this just makes it look better,
    LinkedList <MessageIn>msgList = new LinkedList<MessageIn>() ; 
    LinkedList <MessageIn>msgListSQL = new LinkedList<MessageIn>() ;
    private Semaphore semiSQL = new Semaphore(1, true);
    private Semaphore semi = new Semaphore(1, true);
    
    private MsgProcessorThread messageProccsor = null;
    //private Thread messageProccsorSQL = null;
    
    /** Creates a new instance of MessageHandler */
    public MainServer() throws Exception{
        //time to run threads
        messageProccsor = new MsgProcessorThread(this,users,channels );
        Thread t = new Thread(messageProccsor, "AlexChat MsgProccesor");
        t.start();
        
    }

    
    /**
     * Call this when a user logs on to the server,
     *
     * Hackwars
     *  userName = ip
     *  userNick = Login name, 
     */
    /*private void userLogsOn(String userName, sql s) throws Exception{
	try{
            users.loadUser(userName,userName, s);
	}catch(Exception e){
	    users.getUser(userName);
	}
    } */   
    
    /**
     * Call this when a user logs on to the server,
     *
     * Hackwars
     *  userName = ip
     *  userNick = Login name, 
     */
    public UserMsgBox userLogsOn(String userName) throws Exception{
	User u;	
        u = users.getUser(userName);
        if(u != null){
            return u.getUserMsgBox();
        }
        
	try{        
	   sql s  = SQLConstants.getSQL();
           u = users.loadUser(userName,userName, s);
           SQLConstants.closeSQL(s);
        }catch(Exception e){
            throw e;
	}
        return u.getUserMsgBox();
    }    
    
    /**
     * This function logs a user off, freeing up memory, and not sending any messages to the user
     * This function will automatically connect to sql if not provided
     * HAckWars
     *   userName = ip
     */
    public void userLoggedOff(String userName ) throws Exception{
        sql s  = SQLConstants.getSQL();
        users.removeUser(userName, s);
        SQLConstants.closeSQL(s);
    }    
    
    /**
     * This function logs a user off, freeing up memory, and not sending any messages to the user
     * HAckWars
     *   userName = ip
     */
    public void userLoggedOff(String userName, sql s) throws Exception{
        
        users.removeUser(userName, s);
    }
    
    /**
     *  When the server recives a message put it in here,
     */
    public void queMessage(MessageIn inMsg) throws Exception{
        if(inMsg == null){
            throw new Exception("Error: Null message given");
        }
        
        if(inMsg.getSqlNeeded() == true){
            semiSQL.acquire();
            msgListSQL.addLast(inMsg);
            semiSQL.release();
        }else{
            semi.acquire();
            msgList.addLast(inMsg);
            semi.release();
        }
    }
    
    /**
     *  When the server recives a message put it in here,
     */
    public void queMessage(ArrayMessageIn inMsg) throws Exception{
        if(inMsg == null){
            throw new Exception("Error: Null message given");
        }
        
        int loopc = 0;
        int size = inMsg.getSize();
        while(loopc < size){
            try{
                queMessage( inMsg.getMessage(loopc) );
            }catch(Exception e){
                //dont care
            }
            
            loopc++;
        }
        
    }
    
    MessageIn popInMsg() throws Exception{
        semi.acquire();
        MessageIn toReturn;
        try{
            toReturn = msgList.removeFirst();
        }catch(Exception e){
            toReturn =  null; 
        }
        
        semi.release();
        return toReturn;
    }
    
    MessageIn popInSQLMsg() throws Exception{
        semiSQL.acquire();
        MessageIn toReturn;
        try{
            toReturn = msgListSQL.removeFirst();
        }catch(Exception e){
            toReturn =  null;
        }
        
        semiSQL.release();
        return toReturn;
    }
    
    //Closes everything down
    public void close(){
        messageProccsor.close();
        //TODO sql message processor
        
        sql s  = SQLConstants.getSQL();
        users.close(s);
        SQLConstants.closeSQL(s);
        
        //TODO ChannelListing
        
        
    }
    
    /**
     * This function sets up a channel if not created, then the user joins it,
     *  If the channel is full a channelName-# is added until its not full,
     * 
     *  A minor bug is if someone cretes this channel manually with a password,
     *   users can bypass the password,  but I really dont care. At all.
     */
    public void autoChannel(String userName, String channelName) throws Exception{
        User u = users.getUser(userName);
        if(u == null){
            throw new Exception("Could not find the user!!");
        }
        
        int currentNumber = 0;
        while(currentNumber < MAX_AUTO_CHANNELS){
            String curName = channelName + '-' + currentNumber;
            //get the channel
            Channel c = channels.getChannel(curName);
            
            //dosent exsit create,
            if(c == null){
                try{
                    c = channels.addNewChannel(curName, "", u);
                    return;
                }catch(Exception e){
                    //cant find it or crete it?
                    //Bad channel name, or soemthing else equally wierd
                    throw e;
                }
            }
            
            //try and add the user,
            try{
                ZUserChannel.addUserToChannel(c, u);
                c.setAdminCanKick(false);
                return;
            }catch(Exception e){
                //Ok add user can throw a too many user exception and thats fine
                //ANYTHIGN else though and somethings messed up
                if(!(e instanceof ExceptionChannelFull)){
                    throw e;
                }
            }
            currentNumber++;
        }
        
        System.out.println("Alex.ChatProgram Too many users to auto channel <"+ channelName +">");
        throw new Exception("Hit the maxinum number of auto channles " + MAX_AUTO_CHANNELS);
    }
    
    public User getUser(String name){
        return this.users.getUser(name);
    }
    
}
