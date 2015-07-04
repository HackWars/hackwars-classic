

package chat.client;
import chat.messages.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.TreeMap;
import java.util.Set;
import GUI.*;
import java.util.HashMap;
/**
 *  Setting up
 *  Since the View and controller both need to talk to each other, and you cant exactly create both
 *   At once pointing to each other, you need to do the following
 *   
 *   Crete the controller,,
 *   Crete the View
 *   controller.setChannelList(view);
 * 
 *   After that simply give all messages from the server to processMessage(Message)
 * 
 *   And take the messages in its que and give it to the server.
 * 
 */
public class ChatController {
    public static final String DEFAULT_CHANNEL_NAME = "~CHAT MESSAGES";
    public static final String CHAT_MESSAGES_NAME = "~CHAT MESSAGES";
    public static final String GAME_MESSAGES_NAME = "~GAME MESSAGES";
    public static final String TAG_BOLD = "";
    public static final String TAG_UNBOLD = "";
    public static final String NAME_DELIMETER = ":";
    public static HashMap Admins = new HashMap();
    
    public String userName = null;
    private Hacker MyHacker = null;
    private ArrayList<MessageIn> toSend = new ArrayList<MessageIn>();
    private Semaphore semi = new Semaphore(1, true);
    
    private absViewChannelList channelList = null;
    private absViewRelationList relationList = null;
    
    /** Creates a new instance of chatController */
    public ChatController(String userName,Hacker MyHacker) throws Exception{
	    this.MyHacker=MyHacker;
        if(userName == null){
            throw new Exception("Null name given");
        }
        this.userName = userName;
	

    }
    
    /**
     * Takes in a message and processes it
     */
    public void processMessage(MessageOut msg) throws Exception{
        
        //System.out.println("Got a message " + msg.getClass().getName());
        
        if(msg == null){
            throw new Exception("Null Message recived");
        }
        if(channelList == null){
            throw new Exception("ChatController needs to be connected to a absViewChannelList, please set it with setChannelList");
        }
        /*if(msg.getReciver().compareToIgnoreCase(userName) != 0 && msg.getSender().compareToIgnoreCase(userName) != 0){
            throw new Exception("Received a message designated for another user:" + msg.getReciver());
        }*/
        
        //Channel text message
        if(msg instanceof MsgOutChannelText){
            processMsgChannelText( (MsgOutChannelText)msg  );
            return;
        }
        
		//Channel text /me
		if (msg instanceof MsgOutChannelTextMe) {
			processMsgChannelTextMe( (MsgOutChannelTextMe)msg  );
		}

        //Channel join message
        if(msg instanceof MsgOutChannelJoin){
            processMsgChannelJoin( (MsgOutChannelJoin)msg  );
            return;
        }
        
        //Channel leave message
        if(msg instanceof MsgOutChannelLeave){
            processMsgChannelLeave( (MsgOutChannelLeave)msg  );
            return;
        }
        
        //error message
        if(msg instanceof MsgOutError){
            processMsgError( (MsgOutError)msg  );
            return;
        }
        
        if(msg instanceof MsgOutChannelAdd){
			//System.out.println("Channel Added");
            processMessageChannelAdd( (MsgOutChannelAdd)msg  );
            return;
        }        
        
        if(msg instanceof MsgOutChannelRemove){
            processChannelRemove( (MsgOutChannelRemove)msg  );
            return;
        }        
      
        if(msg instanceof MsgOutWhisper){
            processWhisper( (MsgOutWhisper)msg  );
            return;
        }              
	
	if(msg instanceof MsgOutSubChannels){
	   ProcessMsgSubChannel( (MsgOutSubChannels) msg );
	   return;
	}
        
        /*if(this.relationList == null){
            throw new Exception("please setRelationList with something not null");
        }
        
        if(msg instanceof MsgOutRelationList){
            processRelationList((MsgOutRelationList) msg);
            return;
        }
        
        if(msg instanceof MsgOutRelationAdd){
            processRelationAdd((MsgOutRelationAdd) msg);
            return;
        } */  
        
        
        //throw new Exception("Unrecognized Message type! " + msg.toString() );
    }
    
    
    private void processRelationAdd(MsgOutRelationAdd msg ){
        String name = msg.getName();
        String comment = msg.getComment();
        boolean friend = msg.isFriend();
        boolean ignore = msg.isIgnore();
        boolean online = msg.isOnline();
        
        StructRelation struct = new StructRelation(name, comment, friend, ignore, online);
        this.relationList.addRelation(struct);
       // System.out.println("single Add to Relation Tree " + msg.getName());
    }
    
    private void processRelationList(MsgOutRelationList msg){
        String names[] = msg.getNames();
        String comments[] = msg.getComments();
        boolean friends[] = msg.getFriends();
        boolean ignore[] = msg.getIgnore();
        boolean online[] = msg.getOnline();
        
        if(names == null){
            return;
        }
        
        int size = names.length;
        int loopc = 0;
        while(loopc < size){
            if(names[loopc] != null){
                try{
                    //System.out.println("Giving info to view for relation");
                    StructRelation struct = new StructRelation(names[loopc], comments[loopc], friends[loopc], ignore[loopc], online[loopc]);
                    this.relationList.addRelation(struct);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            loopc++;
        }
        
    }
    
    private void processWhisper(MsgOutWhisper msg){
        //absViewChannel c = channelList.getChannel( GAME_MESSAGES_NAME );
		String s = "";
		boolean isSender = true;
		if(msg.getReciver().compareToIgnoreCase(userName) == 0 ){
			s = TAG_BOLD + msg.getSender() + TAG_UNBOLD + NAME_DELIMETER + msg.getMessage();
			isSender = false;
		}
		else{
			s = TAG_BOLD + "to "+ msg.getReciver() + TAG_UNBOLD + NAME_DELIMETER + msg.getMessage();
		}
        //c.addStringMsg(s,true);
        int loopc=0;
        TreeMap channelListing = channelList.getChannelListing();
        Set keys = channelListing.keySet();
        int size = channelListing.size();
        Object[] o = keys.toArray();
        while(loopc < size){
            String name = (String)o[loopc];
            absViewChannel c = channelList.getChannel(name);
            try{
                if(!name.equals(GAME_MESSAGES_NAME))
                    c.addStringMsg(s,true,isSender, false);
            }catch(Exception e){
                e.printStackTrace();
            }
            loopc++;
        }	
    }
            
    private void processMessageChannelAdd(MsgOutChannelAdd msg){
				//Add this user to our admin hashmap, if they are an admin.
				if(msg.getAdmin())
					Admins.put(msg.getUsertoAdd(),"");
    
        absViewChannel c = channelList.getChannel( msg.getChannelName() );
		if(c!=null)
			c.addUser(msg.getUsertoAdd());
    }
    
    private void processChannelRemove(MsgOutChannelRemove msg){
        absViewChannel c = channelList.getChannel( msg.getChannelName() );
        if(c == null){
            //System.out.println("AlexChat.COntroller,  Bad messge trying to remove user from a channel not subscribed to");
	    return;
        }
        c.removeUser(msg.getUserToRemove());
    }
    
    private void ProcessMsgSubChannel(MsgOutSubChannels msg){
		////System.out.println("Sub Channel process");
		int loopc = 0;
		String sc[] =  msg.getSubscribedChannels();
		String allUsers[][] = msg.getUsers();
		if(sc == null){
			return;	
		}
	
		int size = sc.length;
		while(loopc < size){	
			absViewChannel c = channelList.getChannel(sc[loopc]);
			if(c == null){
				try{
					c = channelList.addChannel( sc[loopc] );
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			try{
				int loopx = 0;
				String users[] = allUsers[loopc];
				int sizex = users.length;
				while(loopx < sizex){
					//Add this user to our admin hashmap, if they are an admin.
					if(msg.isAdmin(users[loopx]))
						Admins.put(users[loopx],"");
				
					c.addUser(users[loopx]);
					loopx++;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			loopc++;
		}	
	
    }

    private void processMsgChannelText(MsgOutChannelText m){
		//System.out.println("Processing Message" + m.getChannelName()+": "+m.getMessage());
        absViewChannel c = channelList.getChannel( m.getChannelName() );

        //for some reason the channel cant be found, add it
        if(c == null){
            try{
		    //System.out.println("Adding Channel "+m.getChannelName());
                c = channelList.addChannel( m.getChannelName() );
            }catch(Exception e){
                e.printStackTrace();
                return;            
            }
            ////System.out.println("Alex ChatController.processMessage Out of synch! with channel list, auto added channel" );
        }
        String s=m.getMessage();
        if(!m.getChannelName().equals(GAME_MESSAGES_NAME))
            s = TAG_BOLD + m.getSenderName() + NAME_DELIMETER + TAG_UNBOLD + m.getMessage();
        c.addStringMsg(s,false,false, false);
        ((viewMain)channelList).messageReceived(m.getChannelName());
        return;
    }

    private void processMsgChannelTextMe(MsgOutChannelTextMe m){
		//System.out.println("Processing Message" + m.getChannelName()+": "+m.getMessage());
        absViewChannel c = channelList.getChannel( m.getChannelName() );

        //for some reason the channel cant be found, add it
        if(c == null){
            try{
		    //System.out.println("Adding Channel "+m.getChannelName());
                c = channelList.addChannel( m.getChannelName() );
            }catch(Exception e){
                e.printStackTrace();
                return;            
            }
            ////System.out.println("Alex ChatController.processMessage Out of synch! with channel list, auto added channel" );
        }
        String s=m.getMessage();
        if(!m.getChannelName().equals(GAME_MESSAGES_NAME))
            s = TAG_BOLD + m.getSenderName() + m.getMessage() + TAG_UNBOLD;
        c.addStringMsg(s,false,false, true);
        ((viewMain)channelList).messageReceived(m.getChannelName());
        return;
    }          
    
    private void processMsgChannelJoin(MsgOutChannelJoin msg){
        MsgOutChannelJoin m =  (MsgOutChannelJoin)msg;
        absViewChannel c;

        try{
            c = channelList.getChannel( m.getChannelName() );
            if(c != null){
                return;
            }
            c = channelList.addChannel( m.getChannelName() );
        }catch(Exception e){
           //System.out.println("Alex ChatController.processMessage OUT OF SYNCH! " );
            e.printStackTrace();
            return;
        }
        int loopc = 0;
        String toAdd[] = m.getUserList();
        while(loopc< toAdd.length){
            try{
                c.addUser(toAdd[loopc]);
            }catch(Exception e){
                e.printStackTrace();
                return;
            }
            loopc++;
        }
        return;
    }    
    
    private void processMsgChannelLeave(MsgOutChannelLeave msg){
        try{
            channelList.removeChannel(msg.getChannelName() );
        }catch(Exception e){
            ////System.out.println("Alex ChatController.processMessage OUT OF SYNCH! " );
            e.printStackTrace();
            return;
        }
    }
    
    private void processMsgError(MsgOutError msg){
        absViewChannel c = channelList.getChannel( DEFAULT_CHANNEL_NAME );
        String s = TAG_BOLD + "ERROR: " + TAG_UNBOLD + msg.getMessage();
        c.addStringMsg(s,false,false, false);
    }
    
    /**
     * Takes in a message and processes it
     */
    public void processMessage(ArrayMessageOut outMsg) throws Exception{
        if(outMsg == null){
            throw new Exception("Error: Null message given");
        }

        int loopc = 0;
        int size = outMsg.getSize();
        while(loopc < size){
            try{
                processMessage( outMsg.getMessage(loopc) );
            }catch(Exception e){
                throw e;
            }
            
            loopc++;
        }
    }
    
    /**
     * Returns all the messages to be sent to the server
     */
    public ArrayMessageIn popMessages() throws Exception{
        try{
            semi.acquire();
        }catch(Exception e){}
        if(toSend.isEmpty() == true){
            semi.release();
            return null;
        }
        
        ArrayMessageIn ret = new ArrayMessageIn(toSend);
        toSend = new ArrayList<MessageIn>();
        
        semi.release();
        return ret;
    }
    
    private void addMessage(MessageIn inMsg){
        try{
            semi.acquire();
        }catch(Exception e){}
        
        toSend.add(inMsg);
            
        semi.release();
    }
    
    /**
     * Setsup communication with the view useing the abstract classes
     *  absViewChannelList - List of all channels, usually a window that contains them all,  
     *  absViewChannel - The seperate chat channels, usually tabs inside the main chat window
     *  
     */
    public void setChannelList(absViewChannelList list) throws Exception{
        if(list == null){
            throw new Exception("Null absViewChannelList given");
        }
        channelList = list;
	list.addChannel(GAME_MESSAGES_NAME);
        list.addChannel(DEFAULT_CHANNEL_NAME);
        
	//resynch channels
        addMessage(  new MsgInSubChannels(userName) );        
    }
    
    /**
     * Setsup communication with the view in an indirect way.
     * 
     */
    public void setRelationList(absViewRelationList relationList){
        this.relationList = relationList;
        
        //get their user list
        addMessage(  new MsgInRelationList(userName) ); 
    }
    
    /**
     *  Sends a message to the server, the message is then sent to all ppl in the channel,
     *   Including the sender
     * 
     * @param channel - The name of the channel the text message is being sent to
     * @param message - The actual text of the message
     */
    public void sendChannelText(String channel, String message) throws Exception{
        if(message == null){
            throw new Exception("Can not send null text!!!");
        }
        if( channelList.getChannel(channel) == null){
            throw new Exception("Not in channel, " + channel + "Can not send to it!");
        }
        addMessage(new MsgInChannelText(userName, message, channel) );
    }

    public void sendMeText(String channel, String message) throws Exception{
        if(message == null){
            throw new Exception("Can not send null text!!!");
        }
        if( channelList.getChannel(channel) == null){
            throw new Exception("Not in channel, " + channel + "Can not send to it!");
        }
        addMessage(new MsgInChannelTextMe(userName, message, channel) );
    }


    
    /**
     *  Sends a message to the server requesting to join a channel
     */
    public void sendChannelJoin(String channelName, String password) throws Exception{
        if( channelList.getChannel(channelName) != null){
            throw new Exception("Allready in channel, " + channelName + " cant join the channel twice!");
        }
        addMessage(new MsgInChannelJoin(userName, channelName, password) );
    }
    
    /**
     *  Sends a message to the server requesting to join a channel
     */
    public void sendChannelCreate(String channelName, String password) throws Exception{
        if( channelList.getChannel(channelName) != null){
            throw new Exception("Allready in channel, " + channelName + " can not create it!");
        }
        addMessage(new MsgInChannelCreate(userName, channelName, password) );
    }
    
    /**
     * The user leaves the channel, and will no longer be able to send or recive messages from the cahnnel
     * 
     * @param channelName - The name of the channel to leave
     * @throws java.lang.Exception - General exceptions
     */
    public void sendChannelLeave(String channelName) throws Exception{
        if( channelList.getChannel(channelName) == null){
            throw new Exception("Not in channel, <" + channelName + ">Can not leave it!!");
        }
        addMessage(new MsgInChannelLeave(userName, channelName) );
    }
    
    /**
     * Sends a whisper
     */
    public void sendWhisper(String user, String message) throws Exception{
        if(user == null){
            throw new Exception("Null user given");
        }
        if(message == null || message.length() == 0){
            throw new Exception("Invalid message given");
        }
        addMessage(new MsgInWhisper(userName,user, message) );
        
    }
    
    /**
    Sends a message used to create a new moderator.
    */
    public void createMod(String user){
			String name=userName;
			if(user!=null){
           if (name.equals("geckotoss") || name.equals("johnny_heart") || name.equals("mecha_cephalon") || 
					name.equals("sixteen_faces")) {
						addMessage(new MsgInAddAdmin(userName,user));
					}
			}
    }
    
     /**
    Sends a message used to create a new moderator.
    */
    public void stfu(String user){
			if(user!=null){
				addMessage(new MsgInMute(userName,user));
			}
    }
    
    /**
     * Sends a messge to add to the user relationship list
     */
    public void sendRelationAdd(String userToAdd, String comment, boolean friend, boolean ignore ) throws Exception{
        if(userToAdd == null){
            throw new Exception("Null user added");
        }
        if(comment == null){
            comment = "";
        }
        
        addMessage(new MsgInRelationAdd(this.userName, userToAdd,comment,friend, ignore));
    }
    
    /**
     * Removes a user from a channel temperairly
     */
    public void sendChannelKick(String userToKick, String channel) throws Exception{
        if(userToKick == null){
            throw new Exception("Null user added");
        }
        
        addMessage(new MsgInChannelKick(this.userName, channel, userToKick));
    }
    
    public Hacker getHacker(){
	    return MyHacker;
    }
       
}
