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
public class MsgInChannelNoDelete extends MessageIn{
    
    private String channelName;
    
    
    /** Creates a new instance of MsgInChannel */
    public MsgInChannelNoDelete(String sender, String channelName) {
        this.setSender(sender);
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }
    
    /**
     *  Returns a copy of the object,
     */
    public MessageIn copy(){
        return new MsgInChannelNoDelete(this.getSender(), channelName);
    }
}
