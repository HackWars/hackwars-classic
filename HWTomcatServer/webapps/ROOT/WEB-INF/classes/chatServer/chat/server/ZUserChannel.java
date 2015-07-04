package chat.server;
import chat.messages.*;
import chat.util.ErrorLog;

/**
 * User and channel talk togeather when adding and removing users
 * This handles that communication SAFELY
 * 
 * @author Alexander Morrison
 */
public class ZUserChannel {
    
    public static void addUserToChannel(Channel c, User u) throws Exception{
        try{
            u.zSubscribeToChannel(c);
        }catch(Exception e){
	    ErrorLog.addMessage(e);
            throw e;
        }
        
        try{
            c.zAddUser(u);
        }catch(Exception e){
            u.zUnSubscribeToChannel(c);
            ErrorLog.addMessage(e);
	    throw e;
        }        
        
        //finailly send the gui-client that the join was succesful
        UserMsgBox box = u.getUserMsgBox();
        box.addMessage( new MsgOutChannelJoin(u.getName(), c.getChannelName(), c.getUserNames() ) ); 

        //notify all users in the cahnnel some one joined!
        User users[] = c.getSubscribers();
        int size = users.length;
        int loopc = 0;
        String channelName = c.getChannelName();
        String userName = u.getName();
        
        
        while(loopc < size){
            try{
                User curUser = users[loopc];
                UserMsgBox boxz =  curUser.getUserMsgBox();
                boxz.addMessage( new MsgOutChannelAdd(curUser.getName(), channelName, userName,u.isAdmin() ) );
            }catch(Exception e){
                //to hell with them
            	ErrorLog.addMessage(e);
	    }
            loopc++;
        }
        
    }
    
    
    public static void removeUserfromChannel(User userToRemove, Channel theChannel) throws Exception{
        ChannelListing list = theChannel.getParent();
        
		//System.out.println("%&%& Closing" + theChannel.getChannelName() );
        try{
            userToRemove.zUnSubscribeToChannel(theChannel);
        }catch(Exception e){
	    ErrorLog.addMessage(e);
            throw e;
        }
        
		//System.out.println("%&%& Closing2" + theChannel.getChannelName() );
        try{
            theChannel.zRemoveUser(userToRemove);
        }catch(Exception e){
            userToRemove.zSubscribeToChannel(theChannel);
            ErrorLog.addMessage(e);
	    throw e;
        }
        
		//System.out.println("%&%& Closing3" + theChannel.getChannelName() );
        //finailly send the gui that the change was succesful
        UserMsgBox box = userToRemove.getUserMsgBox();
        box.addMessage( new MsgOutChannelLeave(userToRemove.getName(), theChannel.getChannelName() ) );
        
        User users[] = theChannel.getSubscribers(); 
        int size = users.length;
        
		//System.out.println("%&%& Closing4" + theChannel.getChannelName() );
		
        if(size == 0){
            list.removeChannel(theChannel);
            return;
        }
       
		//System.out.println("%&%& Closing5" + theChannel.getChannelName() );
        //notify all users in the cahnnel some one left
        int loopc = 0;

        while(loopc < size){
            try{
                User toTell = users[loopc];
                UserMsgBox boxz =  toTell.getUserMsgBox();
                MsgOutChannelRemove msg = new MsgOutChannelRemove(toTell.getName(), theChannel.getChannelName(), 
                                                          userToRemove.getName() );
                boxz.addMessage( msg );
            }catch(Exception e){
                //to hell with them
		ErrorLog.addMessage(e);
            }
            loopc++;
        }
    }
}
