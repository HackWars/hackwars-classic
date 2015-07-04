package chat.server;
import chat.messages.*;
import chat.util.*;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

/**
 *
 */
public class Channel {
    
    public static final int MAX_CHANNEL_NAME_SIZE  = 18;
    public static final int MAX_USERS = 120;
    
    //THREAD SAFE!!
    public LinkedList <User>listedUsers = new LinkedList<User>();
    private Semaphore semiListedUsers = new Semaphore(1, true);
    
    private String name = null;
    
    private boolean removeWithZeroUsers = true;
    private User channelAdmin = null;
    private String password;
    
    private ChannelListing parent = null;
    
    /** 
     * Creates a new channel 
     * with a starting user 
     *
     *  
     */
     Channel(String name, String password,User creator, ChannelListing parent) throws Exception{
        if(name == null){
            throw new Exception("Invalid Channel name, null, Could not create channel");
        }
        if(name.length() >  MAX_CHANNEL_NAME_SIZE){
            throw new Exception("Invalid Channel name, null, Could not create channel");
        }
        if( StringCheck.validSQLString( name) == false ){
            throw new Exception("The name contains invalid cahracters");
        }
        
        if(password != null){
            if(password.length() == 0){
                password = null;
            }
        }
        this.password = password;
        this.name = name;
        
        this.channelAdmin = null;
        this.parent = parent;
        
        ZUserChannel.addUserToChannel(this, creator);
        
    }
    
    /**
     *   THIS IS ONLY FOR USERCHANNEL NOTHING ELSE SHOULD TOUCH this
     * 
     * Subscribes a user to this channel,
     *  Note the user may have to many subscriptions and throw an exception
     *  The channel may have to many users and throw an exception
     */
    void zAddUser(User u) throws Exception{
        if(u.getLoggedOn() == false){
            throw new Exception("ERROR, User not logged on");
        }
        
        try{
            semiListedUsers.acquire();
            if(this.listedUsers.size() > Channel.MAX_USERS ){
                throw new ExceptionChannelFull("ERROR, Too many users are already subscribed to this channel");
            }
            
            //set the new channel admin!
            if(channelAdmin == null){
                channelAdmin = u;
                channelAdmin.getUserMsgBox().addMessage( msg);                
            }
            
            if( listedUsers.contains(u) == true ){
                    return;
            }else{
                listedUsers.addLast(u);
            }
        }catch(Exception e){
            throw e;
        }finally{
            semiListedUsers.release();
        }
    }
    
    /**
     * Removes a user from the channel,
     *  It also unsubsribes that user
     *
     */
    void zRemoveUser(User toRemove) throws Exception{
        if(listedUsers.contains(toRemove) == false){
                return;
        }
		
        try{
            semiListedUsers.acquire();
            listedUsers.remove( toRemove  );
            
            //handle channel admin
            if(toRemove == channelAdmin){
                if(listedUsers.size() != 0){
                    channelAdmin = listedUsers.getFirst();
                    channelAdmin.getUserMsgBox().addMessage( msg);
                }else{
                    channelAdmin = null;
                }
            }
            

        }catch(Exception e){
            throw e;
        }finally{
            semiListedUsers.release();
        }
    }
    
    public boolean hasUser(User u){
        try{
            this.semiListedUsers.acquire();
            return listedUsers.contains(u);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            this.semiListedUsers.release();
        }
                
    }
    
    /**
     * Removes all users from the channel,
     *
     *  NOT TESTED
     */
    void destoyChannel(){
        
        Iterator<User> it = listedUsers.listIterator();
        while(it.hasNext() == true){
            try{
                ZUserChannel.removeUserfromChannel(it.next(), this);
            }catch(Exception e){
                return;
            }
        }
        
    }
    
    /**
     * gets a list of all subscribed users
     */
    public User[] getSubscribers(){
        try{
            this.semiListedUsers.acquire();
            Object oArray[] = this.listedUsers.toArray();
            User uArray[] = new User[oArray.length];

            int loopc = 0;
            while(loopc < oArray.length){
                uArray[loopc] = (User) oArray[loopc];
                loopc++;
            }
            return uArray;
        }catch(Exception e){
            
        }finally{
            this.semiListedUsers.release();
        }
        return null;
    }
    
    /**
     * returns the channels name
     */
    public String getChannelName(){
        return this.name;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public String[] getUserNames() throws Exception{
        String ret[];
        
        //String ret[] = this.listedUsers.size();
        try{
            semiListedUsers.acquire();
            int size = this.listedUsers.size();
            ret = new String[size];
            
            int loopc = 0;
            while(loopc < size){
                ret[loopc] = this.listedUsers.get(loopc).getName();
                loopc++;
            }
        }catch(Exception e){
            throw e;
        }finally{
            semiListedUsers.release();
        }
        
        return ret;
    }
    
    public User getAdmin(){
        return this.channelAdmin;
    }
    
    public int getNumberOfUsers(){
        return listedUsers.size();
    }

    public boolean getDestroyWithZero(){
        return this.removeWithZeroUsers;
    }
    
    public void setDestroyWithZeroUsers(boolean remove){
        this.removeWithZeroUsers = remove;
    }
    
    public ChannelListing getParent(){
        return this.parent;
    }
}
