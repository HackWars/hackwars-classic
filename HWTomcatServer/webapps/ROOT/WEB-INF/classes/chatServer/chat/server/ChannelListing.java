/*
 * ChannelListing.java
 *
 * Created on December 19, 2007, 12:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package chat.server;
import util.sql;
import chat.util.*;

import java.util.TreeMap;
import java.util.Map;
import java.util.Collections;


/**
 *
 * @author Owner
 */
public class ChannelListing {
    
    //THREAD SAFE!!
    private Map <String,Channel> channelMap = Collections.synchronizedMap(new TreeMap<String,Channel>( new StringCompare() ));
    
    /** Creates a new instance of ChannelListing */
    ChannelListing() {
    }
    
    /** Sees if their is a particular channel with a given name
      *  returns null on not found
      */
    Channel getChannel(String name){
        Channel c;
        try{
            c = channelMap.get(name);
        }catch(Exception e){
            return null;
        }
        if(c == null){
            return null;
        }
        return c;
    }    
    
    Channel addNewChannel(String uniqueName,String password, User creator)  throws Exception{
        Channel c;
        try{
            
            //Is the name taken?
            c = getChannel(uniqueName);
            if(c != null){
                throw new Exception("Could not create channel named <" + uniqueName + "> name allready taken");
            }
            
            //create and add it
            c = new Channel(uniqueName,password, creator, this);  
            channelMap.put(uniqueName, c );
            
        }catch(Exception e){
            throw e;
        }
        return c;
    }
    
    void removeChannel(String name) throws Exception{
        try{
            Channel c = getChannel(name);
            c.destoyChannel();
			channelMap.remove(name);
        }catch(Exception e){
            throw e;
        }
    }
    
    void removeChannel(Channel c) throws Exception{
        try{
            c.destoyChannel();
			channelMap.remove(c.getChannelName() );			
        }catch(Exception e){
            throw e;
        }
    }
    
}
