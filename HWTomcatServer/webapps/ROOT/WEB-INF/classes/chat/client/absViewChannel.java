package chat.client;

/**
 *
 * @author Owner
 */
public interface absViewChannel {
    
    /**
     * Adds a text string to the channel
     */
    public void addStringMsg(String s,boolean whisper,boolean isSender, boolean isMe);
    
    /**
     * Returns the channels name
     */
    public String getChannelName();
    
    /**
     *  Adds a user to the channel
     */
    public void addUser(String s);
    
    /**
     * Removes a user from the channel
     */
    public void removeUser(String s);
    
}
