package chat.server;

import chat.messages.*;
import util.sql;

import java.lang.Runnable;
import java.lang.Thread;

public class MsgProcessorThread implements Runnable{
    
    public static final int SLEEPTIME_MILASEC = 100;
    private boolean stopRunning = false;
    
    private MainServer mainChat = null; //Needed???
    private UserListing userList = null;
    private ChannelListing channelList = null;
    
    /** Creates a new instance of ProcessMessage */
    MsgProcessorThread(MainServer m, UserListing userList, ChannelListing channelList) throws Exception{
        if(m == null){
            throw new Exception("Null Main given");
        }
        mainChat = m;
        this.userList = userList;
        this.channelList = channelList;
    } 
    
    public void run(){
        
        while(stopRunning == false){
            try{
                MessageIn msg = mainChat.popInMsg();
                if(msg == null){
                    Thread t = Thread.currentThread();
                    t.sleep(SLEEPTIME_MILASEC);
                }else{
                    System.out.println("%%%% Alex MsgProcessorThread " + msg.getClass().getName());
                    processMsg(msg);
                }
            }catch(Exception e){
                System.out.flush();
                String msg = "0: ERROR, Alex's chatProgram.MsgProcessorThread\n1: THREAD CONTINUING\n2:";
                System.out.println(msg);
                e.printStackTrace( System.out);
                System.out.flush();
            }
        }
        


    }
    
    private void processMsg(MessageIn msg){
        User u = getUser(msg);
        
				if(!u.getMuted()){
        
					if(u == null){
							return;
					}
					
					if(msg instanceof MsgInChannelCreate){
							channelCreate( (MsgInChannelCreate) msg, u);
							return;
					}
					
					if(msg instanceof MsgInChannelLeave){
							channelLeave( (MsgInChannelLeave) msg, u);
							return;
					}
					
					if(msg instanceof MsgInChannelText){
							channelText( (MsgInChannelText) msg, u);
							return;
					}
					
					if(msg instanceof MsgInChannelJoin){
							channelJoin( (MsgInChannelJoin) msg, u);
							return;
					}
					
					if(msg instanceof MsgInWhisper){
							whisper( (MsgInWhisper) msg, u);
							return;
					}        
					
					if(msg instanceof MsgInChannelKick){
							channelKick( (MsgInChannelKick) msg, u);
							return;
					}
					
					if(msg instanceof MsgInSubChannels){
							subscribedChannels( (MsgInSubChannels) msg, u);
							return;
					}
					
					if(msg instanceof MsgInRelationList){
							relationList((MsgInRelationList) msg, u);
							return;
					}

					if(msg instanceof MsgInRelationAdd){
							addRelation((MsgInRelationAdd) msg, u);
							return;
					}
				}else{
					return;
				}
        
        String message = "0: WARNING, Alex's chatProgram.processMsg\n1: THREAD CONTINUING, MSG FAILED "; 
        message += "\n2: Unknown message type! " + msg.getClass().getName() ;  
        System.out.println(message);
    }
    
    private void addRelation(MsgInRelationAdd msg, User u){
        UserRelationListing userRelations = u.getRelationList();
        
        try{
            
            UserRelation r = userRelations.addRelation( msg.getNameToAdd() , msg.getComment(),msg.isFriend(), msg.isIgnore());
        }catch(Exception e){
            //this really should not happen
            sendErrorMsg("UNEXPECTED ERROR: Could not add user to list: " + e.getMessage() , u);
            e.printStackTrace();
        }
    }
    
    private void relationList(MsgInRelationList msg, User u){
        UserRelationListing userRelations = u.getRelationList();
		if(userRelations == null){
			System.out.println("%&%& AlexChatMsgProess Usrtrelation lst not intilized");
			return;
		}
		
        UserRelation all[] = userRelations.getAllRelations();
        
        if(all == null){
            return;
        }
        
        int size = all.length;
        String names[] = new String[size];
        String comments[] = new String[size];
        boolean friend[] = new boolean[size];
        boolean ignore[] = new boolean[size];
        
        int loopc = 0;
        while(loopc < size){
            UserRelation cur = all[loopc];
            if(cur != null){
                names[loopc] = cur.getSecondName();
                comments[loopc] = cur.getComment();
                friend[loopc] = cur.isFriend();
                ignore[loopc] = cur.isIgnore();
            }
            loopc++;
        }
        
        UserMsgBox box = u.getUserMsgBox();
        box.addMessage(new MsgOutRelationList(u.getName(),names,comments,friend,ignore  ) );
        
    }
    
    // this message sends all the channels and userlists in them to the user
    private void subscribedChannels( MsgInSubChannels msg, User u){
		//getStringChannelList    	
		UserMsgBox box =  u.getUserMsgBox();
		
		String cList[] = u.getStringChannelList();
		int size = cList.length;
		String uList[][] = new String[size][];
		int loopc = 0;
		while(loopc < size){
			Channel c = channelList.getChannel(cList[loopc]);
			if(c!=null){
				try{
					uList[loopc] = c.getUserNames();
				}catch(Exception e){
					
				}
			}
			loopc++;
		}
		
		
		box.addMessage( new MsgOutSubChannels(u.getName(), cList, uList ) );
    
	}

    private void channelKick(MsgInChannelKick msg, User u){
        User toBeKicked = userList.getUser( msg.getUserToBeKicked() );
        //Couldnt find the user to be kicked!
        if(toBeKicked == null){
            String errText = "Could not kick user named <" + msg.getUserToBeKicked() + "> in the channel <" + msg.getChannelName() + "> ";
            errText += "Could not find the user to be kicked";
            sendErrorMsg(errText, u);
            return;
        }
        
        //Dont let them kick an admin!
        if(toBeKicked.isAdmin() == true){
            String errText = "Could not kick user named <" + msg.getUserToBeKicked() + "> in the channel <" + msg.getChannelName() + "> ";
            errText += "The user you are trying to kick is an admin";
            sendErrorMsg(errText, u);
            return;
        }
        
        Channel c = channelList.getChannel( msg.getChannelName() );
        //is this a valid channel?
        if(c == null){
            String errText = "Could not kick user named <" + msg.getUserToBeKicked() + "> in the channel <" + c.getChannelName() + "> ";
            errText += "The channel could not be found";
            sendErrorMsg(errText, u);
            return;
        }
        
        //Dose the user even have this user?
        if(c.hasUser(toBeKicked) == false){
            String errText = "Could not kick user named <" + msg.getUserToBeKicked() + "> in the channel <" + c.getChannelName()+ "> ";
            errText += "The user is not in the channel";
            sendErrorMsg(errText, u);
            return;
        }
        
        //If the user is neither a admin or a channel admin, give an error 
        if(u != c.getAdmin() && u.isAdmin() == false){
            String errText = "Could not kick user named <" + msg.getUserToBeKicked() + "> in the channel <" + c.getChannelName()+ "> ";
            errText += "You are not the channels Admin, or an Admin";
            sendErrorMsg(errText, u);
            return;
        }

        try{
            ZUserChannel.removeUserfromChannel(toBeKicked, c);
            
            //finailly send the gui that the change was succesful
            UserMsgBox box = toBeKicked.getUserMsgBox();
            box.addMessage( new MsgOutChannelKick( toBeKicked.getName(), msg.getChannelName() ) ); 
        }catch(Exception e){
            sendErrorMsg("GENERAL ERROR Could not kick: " + e.getMessage() , u);
        }
        
    }
    
    private void whisper(MsgInWhisper msg, User u){
        User reciver = userList.getUser(msg.getReciver() );
        if(reciver == null){
            String errText = "Could not whisper user named <" + msg.getReciver() + "> ";
            errText += "The user could not be found";
            sendErrorMsg(errText, u);
            return;
        }
        
        UserRelationListing url = reciver.getRelationList();
        UserRelation ur = url.getRelation(u);
        if( ur != null ){
            if(ur.isIgnore() == true){
                String errText = "Could not whisper user named <" + msg.getReciver() + "> ";
                errText += "The user has you ignored";
                sendErrorMsg(errText, u);
                return;
            }else{
		System.out.println("%&%& AlexChat MsgProcess Not Ignored Rec" + reciver.getName() + " Sender" + u.getName() );
            }
        }else{
            System.out.println("%&%& AlexChat MsgProcess Null User relation Rec " + reciver.getName() + " Sender " + u.getName() );
	}
        
        UserMsgBox box  = reciver.getUserMsgBox();
        box.addMessage(new MsgOutWhisper(reciver.getName(), u.getName(), msg.getMessage()));
        
        box = u.getUserMsgBox();
        box.addMessage(new MsgOutWhisper(reciver.getName(), u.getName(), msg.getMessage()));
    }
    
    /**
     * Handles a user sending text to a channel
     */
    private void channelText(MsgInChannelText msg, User u){
        //first see if the channel exsits,
        Channel c = channelList.getChannel(msg.getChannelName());
        if(c == null){
            String errText = "Could not send text to channel <" + msg.getChannelName() + "> ";
            errText += " Could not find the channel";            
            sendErrorMsg(errText , u);
            return;
        }
        
        //make sure the user sending the message is actually subscribe to this channel
        if(c.hasUser(u) == false){
            String errText = "Could not send text to channel <" + msg.getChannelName() + "> ";
            errText += " You are not subscribed to this channel, join the channel to send messages to it";            
            sendErrorMsg(errText , u);
            return;
        }
        
        //ok spam the message now
        User users[] = c.getSubscribers();
        int size = users.length;
        int loopc = 0;
        
        while(loopc < size){
            try{
                User curUser = users[loopc];
                UserMsgBox box =  curUser.getUserMsgBox();
                
                UserRelationListing url = curUser.getRelationList();
                UserRelation ur = url.getRelation(u);
                if( ur != null && ur.isIgnore() == true){
                    //user has the sender on ignore
                }else{
                    box.addMessage( new MsgOutChannelText(curUser.getName(), msg.getChannelName(), 
                                                      u.getName()           , msg.getMessage() ) );
                }
                

            }catch(Exception e){
                String message = "0: WARNING, Alex's chatProgram.ChannelText \n1: THREAD CONTINUING, MSG FAILED "; 
                message += "\n2: unexpected error! " + e.getLocalizedMessage() ;  
                System.out.println(message);
            }
            loopc++;
        }
    }
    
    /**
     * Handles the channel leaving message
     */
    private void channelLeave(MsgInChannelLeave msg, User u){
        //first see if it allready exsits
        try{
            Channel c = channelList.getChannel(msg.getChannelName());
            if(c == null){
                String errText = "Could not leave channel <" + msg.getChannelName() + "> ";
                errText += " Could not find the channel";            
                sendErrorMsg(errText , u);
                return;
            }
            
            if(c.hasUser(u) == false){
                String errText = "Could not leave channel <" + msg.getChannelName() + "> ";
                errText += " You are not subscribed to the channel";            
                sendErrorMsg(errText , u);
                return;
            }
            

            ZUserChannel.removeUserfromChannel(u, c);
            
        }catch(Exception e){
            //this really should not happen
            sendErrorMsg("UNEXPECTED ERROR: Could not leave channel: " + e.getMessage() , u);
        }
    }
    
    /**
     * Handles the channel join message
     */
    private void channelJoin(MsgInChannelJoin msg, User u){
        //first see if it allready exsits
        try{
            Channel c = channelList.getChannel(msg.getChannelName());
            if(c == null){
                String errText = "Could not subscribe to channel <" + msg.getChannelName() + "> ";
                errText += " Could not find channel";            
                sendErrorMsg(errText , u);
                return;
            }
            
            if(c.getPassword() != null ){
                if(c.getPassword().compareTo(msg.getPassword()) != 0){
                    String errText = "Could not subscribe to channel <" + msg.getChannelName() + "> ";
                    errText += " Incorrect password <" +msg.getPassword()+ "> given";            
                    sendErrorMsg(errText , u);
                    return;
                }
            }
            
            if(u.getNumberOfChannels() + 1 > User.MAX_CHANNELS){
                    String errText = "Could not subscribe to channel <" + msg.getChannelName() + "> ";
                    errText += " User can only subscribe to <" + User.MAX_CHANNELS + "> Channels, leave a channel first";            
                    sendErrorMsg(errText , u);
                    return;
            }
            
            ZUserChannel.addUserToChannel(c, u);
        }catch(Exception e){
            sendErrorMsg("UNEXPECTED ERROR:Could not create channel Error :" + e.getMessage(), u);
        }

    }
    
    /**
     * Handles the create channel message
     */
    private void channelCreate(MsgInChannelCreate msg, User u){
        //first see if it allready exsits
        try{
            Channel c = channelList.getChannel(msg.getChannelName());
            if(c != null){
                sendErrorMsg("Could not create channel <" + msg.getChannelName() + "> Already exists!", u);
		MsgInChannelJoin nMsg = new MsgInChannelJoin( u.getName(), msg.getChannelName(), msg.getPassword() );
		channelJoin(nMsg,u );
                return;
            }
            
            if(u.getNumberOfChannels() + 1 > User.MAX_CHANNELS){
                    String errText = "Could not create the channel <" + msg.getChannelName() + "> ";
                    errText += " User can only subscribe to <" + User.MAX_CHANNELS + "> Channels, leave a channel first";            
                    sendErrorMsg(errText , u);
                    return;
            }
            
            c = channelList.addNewChannel( msg.getChannelName(),msg.getPassword(), u );
            
            //finailly send the gui that the change was succesful
            //UserMsgBox box = u.getUserMsgBox();
            //box.addMessage( new MsgOutChannelJoin(u.getName(), msg.getChannelName(), c.getUserNames() ) );            
            
        }catch(Exception e){
            e.printStackTrace();
            sendErrorMsg("Could not create channel Error:" + e.getMessage(), u);
        }
    }
    
    /**
     * Just sends the user an error message,
     * 
     */
    private void sendErrorMsg(String text, User toSendTo ){
        //get the box
        if(toSendTo != null){
            UserMsgBox box = toSendTo.getUserMsgBox();
            MsgOutError errMsg = new  MsgOutError(toSendTo.getName() , text);
            box.addMessage( errMsg );
        }
    }
    
    /**
     * Take sin the user string and finds its object 
     */
    private User getUser(MessageIn msg){
        User u = userList.getUser(msg.getSender());  
        if(u == null){
            //just make them, 
            try{
                sql s  = SQLConstants.getSQL();
                u = userList.loadPreExistingUser(msg.getSender() , s);
                SQLConstants.closeSQL(s);
            }catch(Exception e){
                String message = "0: WARNING, Alex's chatProgram.getUser\n1: THREAD CONTINUING, MSG FAILED "; 
                message += "\n2: Could not find user named " + msg.getSender() + "\n";  
                message += e.getMessage();
                System.out.println(message);
                return null;
            }
            return u;
        }
        return u;
    }
    
    /**
     * Closes the thread, next message it process it will stop.
     */
    public void close(){
        stopRunning = true;
    }

}
