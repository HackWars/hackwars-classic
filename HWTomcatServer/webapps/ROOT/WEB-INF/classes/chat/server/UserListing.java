

package chat.server;
import util.sql;
import chat.util.*;

import java.util.TreeMap;
import java.util.concurrent.Semaphore;


public class UserListing {
    
    
    TreeMap <String,User>userMap = new TreeMap<String,User>( new StringCompare() );
    private Semaphore semi = new Semaphore(1, true);
    
    /** Creates a new instance of UserListing */
    UserListing() {
    }
    
    /*
     * Loads in an exsiting user
     *  for HACK WARS name = IP
     */
    User loadPreExistingUser(String uniqueName, sql s) throws Exception{
        return loadUser(uniqueName,uniqueName,s);
    }
    
    /**
     * Loads in a new user
     *  for HACK WARS name = IP
     *
     *  Giving it a user that allready exsits loads it normally, and it ignores uniqueNick and goes with
     *  what it is in the sql
     */    
    public User loadUser(String uniqueName, String uniqueNick, sql s) throws Exception{
        try{
            //first lets see if it allready exsits in memory
            User u = this.getUser(uniqueName);
            if( u != null ){
                return u;
            }
            
            //dose it exsit in the sql?
            u = SQLUser.getSqlData(s, uniqueName);
            if(u != null){
               this.addUser(u);
               u.setupUserRelation(s);
               return u;
            }
            
            //Ok this has NEVER bin created befor time to make it
            u = SQLUser.createUser(s, uniqueName, uniqueNick);
            userMap.put(uniqueName, u);
            u.setupUserRelation(s);
            return u;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    
    private void addUser(User u) throws Exception{
        try{
            
            semi.acquire();
            userMap.put(u.getName(), u);
        }catch(Exception e){
            throw e;
        } finally{
            semi.release();
        }
    }
    
    /**
     * User logged out, or shutting down just the chat program.
     *  Removes them memory, and updates the database IF needed
     */
    public void removeUser(String uniqueName, sql s) throws Exception{
        User u = getUser(uniqueName);
        if(u == null){
            throw new Exception("Could not log user out, Could not find user <" +uniqueName+ ">");
        }
        if(u == null){
            throw new Exception("Could not log user out, Null SQL given");
        }
        try{
            u.userLoggedOut(s);
        }catch(Exception e){

        }
        removeUser(u);
    }
    
	
	private void removeUser(User u){
        try{
            semi.acquire();
            userMap.remove(u.getName() );
        }catch(Exception e){
        }finally{
            semi.release();
        }
	}
	
    /**
     * Returns a User with a specific name (Hack wars-IP)
     * returns null if dose not exsit
     */
    public User getUser(String uniqueName) {
        try{
            semi.acquire();
            User u = null;

            u = userMap.get(uniqueName);

            return u;
        }catch(Exception e){
            return null;
        }finally{
            semi.release();
        }
    }
    
    /**
     * Logs ALL users out
     */
    public void close(sql s){
        Object array[] = userMap.values().toArray();
        
        int loopc = 0;
        while(loopc < array.length){
            if(array[loopc] instanceof User){
                User u = (User) array[loopc];
                try{
                    
                    u.userLoggedOut(s); 
                }catch(Exception e){
                    
                }
            }
            
            loopc++;
        }
        
    }
    
}
