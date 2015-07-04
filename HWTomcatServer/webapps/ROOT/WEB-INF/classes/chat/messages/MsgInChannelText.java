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
public class MsgInChannelText extends MessageIn{
    
    private String message;
    private String channelName;
    
    /** Creates a new instance of MsgInChannel */
    public MsgInChannelText(String sender, String message, String channelName) {
        this.setSender(sender);
        this.channelName = channelName;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getChannelName() {
        return channelName;
    }
    
    /**
     *  Returns a copy of the object,
     */
    public MessageIn copy(){
        return new MsgInChannelText(this.getSender(),message, channelName);
    }
    
}
