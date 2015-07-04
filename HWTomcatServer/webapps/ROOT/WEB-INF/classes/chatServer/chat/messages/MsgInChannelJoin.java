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
public class MsgInChannelJoin extends MessageIn{
    
    private String password;
    private String channelName;
    
    
    /** Creates a new instance of MsgInChannel */
    public MsgInChannelJoin(String sender, String channelName, String password) {
        this.setSender(sender);
        this.channelName = channelName;
        this.password = password;
    }

    public String getChannelName() {
        return channelName;
    }
    
    public String getPassword(){
        return password;
    }
    
    /**
     *  Returns a copy of the object,
     */
    public MessageIn copy(){
        return new MsgInChannelJoin(this.getSender(),password, channelName);
    }
}
