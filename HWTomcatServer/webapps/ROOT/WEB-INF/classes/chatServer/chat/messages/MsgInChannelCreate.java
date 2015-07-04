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
public class MsgInChannelCreate extends MessageIn{
    
    private String channelName;
    private String password;
    
    /** Creates a new instance of MsgInChannel */
    public MsgInChannelCreate(String sender, String channelName, String password) {
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
    
    public MessageIn copy(){
        return new MsgInChannelCreate(this.getSender(),channelName, password);
    }
}
