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
public class MsgOutChannelKick extends MessageOut{
    
    private String channelName;
    
    /** Creates a new instance of MsgInChannel */
    public MsgOutChannelKick(String reciver, String channelName) {
        this.setReciver(reciver);
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }
    
    public MessageOut copy(){
        return new MsgOutChannelKick(this.getReciver(),channelName);
    }
}
