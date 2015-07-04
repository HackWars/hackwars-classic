package chat.server;
import chat.util.*;
import util.sql;
import chat.messages.*;

import java.util.TreeMap;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class UserRelationListing {
    
    private Semaphore semi = new Semaphore(1, true);
    private TreeMap <String,UserRelation> relationMap = new TreeMap<String,UserRelation>( new StringCompare() );  
    private User theUser;
    
    private int sqlKey;
    
    public UserRelationListing(User theUser)throws Exception{
        this(theUser,(sql)null);
    }
    
    public UserRelationListing(User theUser, sql s)throws Exception {
        this.theUser = theUser;
        if(s == null){
            s = SQLConstants.getSQL();
            loadSQLRelations(theUser.getName(),s);
            s.close();
            
        }else{
            loadSQLRelations(theUser.getName(),s);
        }
        
    }
    
    private void loadSQLRelations(String userName, sql s) throws Exception{
        UserRelation list[] = null;
        try{
            list = SQLUserRelation.getRelationList(s, userName);
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
        int size = list.length;
        int loopc = 0;
        
        while(loopc < size){
            UserRelation r = list[loopc];
            AddUserRelation(r);
            loopc++;
        }

    }
    
    void AddUserRelation(UserRelation r){
        if(r == null){
            return;
        }
        
        try{
            semi.acquire();
            this.relationMap.put(r.getSecondName(), r);
        }catch(Exception e){
            
        }finally{
            semi.release();
        }
    }
    
    /**
     * gets the userRelation of another user,
     *   returns null if their is none
     */
    public UserRelation getRelation(User u){
        return getRelation(u.getName());
    }
    
    /**
     * gets the userRelation of another user,
     *   returns null if their is none
     */
    public UserRelation getRelation(String userName){
        try{
            semi.acquire();
            return relationMap.get(userName);
        }catch(Exception e){
            //really shouldnt happen
            return null;
        }finally{
            semi.release();
        }
    }
    
    /**
     * Creates a user relation with another user
     */
    public UserRelation addRelation(String name, String comment, boolean friend, boolean ignore, sql s) throws Exception{
        UserRelation r = getRelation(name);
        if(r != null){
            r.setComment(comment);
            r.setFriend(friend);
            r.setIgnore(ignore);
            UserMsgBox box = this.theUser.getUserMsgBox();
            box.addMessage(new MsgOutRelationAdd(theUser.getName(),name, comment,friend,ignore));
            return r;
        }
        
        try{
            s = SQLConstants.getSQL();
            r = SQLUserRelation.newUserRelation(this.theUser.getName(), name, comment, s);
            this.AddUserRelation(r);
            UserMsgBox box = this.theUser.getUserMsgBox();
            box.addMessage(new MsgOutRelationAdd(theUser.getName(),name, comment,friend,ignore));
            return r;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public UserRelation addRelation(String name, String comment, boolean friend, boolean ignore) throws Exception{
        sql s = null;
        try{
            s = SQLConstants.getSQL();
            return addRelation(name,comment,friend,ignore,s);
        }catch(Exception e){
            throw e;
        }finally{
            if(s != null){
                s.close();
            }
        }
    }
    
    public void close(sql s){
       if(s == null){
           s = SQLConstants.getSQL();
           close2(s);
           s.close();
       }else{
           close2(s);
       }
    }
    
    private void close2(sql s){
        try{
            int loopc = 0;
            Object rel[] = relationMap.values().toArray();
            while(loopc < rel.length){
                UserRelation r = (UserRelation) rel[loopc];
                try{
                    r.close(s);
                }catch(Exception e){

                }
                loopc++;
            }
        }catch(Exception e){
        }finally{
            semi.release();
        }
        
    }
    
    /**
     * Gets all relations
     */
    public UserRelation[] getAllRelations(){
        int loopc = 0;
        UserRelation result[] = null;
        try{
            semi.acquire();
            Object array[] = this.relationMap.values().toArray();
            int size = array.length;
            result = new UserRelation[size];
            while(loopc < size){
                try{
                    result[loopc] = (UserRelation)array[loopc];
                }catch(Exception e){
                    result[loopc] = null;
                }
                loopc++;
            }
            
        }catch(Exception e){
            return null;
        }finally{
            semi.release();
        }
        return result;
    }
}
