/*
 * absViewChannel.java
 *
 * Created on December 30, 2007, 7:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package chat.client;
import java.util.TreeMap;
/**
 *
 * @author Owner
 */
public interface absViewChannelList {
    

    
    /**
     * The VIEW creates a new channel
     *  -- The view should create a new window/tab to display incoming chat messages 
     */
    public absViewChannel addChannel(String channelName) throws Exception;
    
    /**
     * The VIEW stops displaying a channel
     *  -- The view should remove the window/tab that is displaying messages
     */
    public void removeChannel(String channelName) throws Exception;
    
    /**
     * The view should return the abstract channel
     * 
     * If it dosent exsit return NULL
     */
    public absViewChannel getChannel(String channelName);
    
    public TreeMap getChannelListing();
    
}
