/*
 * MsgInChannel.java
 *
 * Created on December 27, 2007, 5:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package chat.messages;

/**
 *
 * @author Owner
 */
public class MsgInChannelKick extends MessageIn{
    
    private String channelName;
    private String userToBeKicked;
    
    /** Creates a new instance of MsgInChannel */
    public MsgInChannelKick(String sender, String channelName, String userToBeKicked) {
        this.setSender(sender);
        this.channelName = channelName;
        this.userToBeKicked = userToBeKicked;
    }

    public String getChannelName() {
        return channelName;
    }
    
    public String getUserToBeKicked(){
        return userToBeKicked;
    }
    
    public MessageIn copy(){
        return new MsgInChannelKick(this.getSender(),channelName, userToBeKicked);
    }
}
