package chat.server;
import chat.messages.*;

/**
 *
 * @author Owner
 */
public class ZUserChannel {
    
    public static void addUserToChannel(Channel c, User u) throws Exception{
        try{
            u.zSubscribeToChannel(c);
        }catch(Exception e){
            throw e;
        }
        
        try{
            c.zAddUser(u);
        }catch(Exception e){
            u.zUnSubscribeToChannel(c);
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
                boxz.addMessage( new MsgOutChannelAdd(curUser.getName(), channelName, userName ) );
            }catch(Exception e){
                //to hell with them
            }
            loopc++;
        }
        
    }
    
    
    public static void removeUserfromChannel(User userToRemove, Channel theChannel) throws Exception{
        ChannelListing list = theChannel.getParent();
        
        try{
            userToRemove.zUnSubscribeToChannel(theChannel);
        }catch(Exception e){
            throw e;
        }
        
        try{
            theChannel.zRemoveUser(userToRemove);
        }catch(Exception e){
            userToRemove.zSubscribeToChannel(theChannel);
            throw e;
        }
        
        //finailly send the gui that the change was succesful
        UserMsgBox box = userToRemove.getUserMsgBox();
        box.addMessage( new MsgOutChannelLeave(userToRemove.getName(), theChannel.getChannelName() ) );
        
        User users[] = theChannel.getSubscribers(); 
        int size = users.length;
        
        if(size == 0){
            list.removeChannel(theChannel);
            return;
        }
       

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
            }
            loopc++;
        }
    }
}
