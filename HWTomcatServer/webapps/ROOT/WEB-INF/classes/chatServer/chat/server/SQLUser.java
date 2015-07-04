/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chat.server;
import java.util.ArrayList;
import util.*;


/**
 *
 * @author Owner
 */
public class SQLUser {
    
    //SQL constants
    static final String USER_TABLE = "user";
    static final String USER_NAME = "username";
    static final String USER_NICK = "usernick";
    static final String USER_KEY = "userkey";    
    static final String USER_PROPERTIES = "properties";
    static final String USER_P_ADMIN  = "ADMIN";
    static final String USER_P_NO_WHISPER  = "NO_WHISPER";
    static final String USER_P_NO_CHANNEL_CREATION  = "NO_CHANNEL_CREATION";
    static final String USER_P_NO_CHANNEL_JOIN  = "NO_CHANNEL_JOIN";
    static final String USER_MUTED = "USER_MUTED";
       
    /**
     * 
     * @param s - simplified sql from util.sql
     * @param userName - The anme of the users sql desired
     * @return - the int value of the key
     * @throws java.lang.Exception - SQL error, could not find, 
     */
    public static int getUserKey(sql s, String userName) throws Exception{
        String sqlMain = "SELECT " + USER_KEY
                + "FROM " + USER_TABLE
                + "WHERE " + USER_NAME + " = '" + userName + "' ;";
        
        try{
            ArrayList<String> result = s.processQuery(sqlMain);
            if(result == null || result.size() == 0){
                throw new Exception("SQL ERROR, could not find userKey named <" + userName + ">");
            }
            int key = Integer.parseInt( result.get(0) );
            return key;
            
        }catch(Exception e){
            throw e;
        }
    }
    
    /**
     * 
     * @param userName
     * @return A String with the sql select statment to get the user key
     */
    public static String getSQLStringSelectKey(String userName){
        String sqlMain = " SELECT " + USER_KEY
                + " FROM " + USER_TABLE
                + " WHERE " + USER_NAME + " = '" + userName + "' ";
        
        return sqlMain;
    }
    
    /**
     * Updates the users SQL
     *  nickName,
     *  Properties
     */
    static void updateSQL(sql s, User u) throws Exception{
        String cmd = "UPDATE " + USER_TABLE + " SET " 
                //new values comma deliminated
                + USER_NICK + " = '" + u.getNick() + "'," + USER_PROPERTIES + " = " + u.getProptiesSQL()
                    //what will be changed
                + " WHERE " + USER_NAME + " = '" + u.getName() + "' ;";
				
        try{
            s.processUpdate(cmd);
        }catch(Exception e){
            throw e;
        }        
    }    
    
    /**
     * Deletes a user SQL, 
     *   This is one way!!!
     *   The user realtions are not also deleted, so becarful calling this
     */
    static void deleteUser(sql s, User u) throws Exception{
        String cmd = "DELETE FROM " + USER_TABLE + " WHERE " 
                + USER_NAME + " = " + u.getName() + ";" ;
        
        try{
            s.processUpdate(cmd);
        }catch(Exception e){
            throw e;
        }
    }    
    
    
    static User createUser(sql s, String userName, String userNick, UserListing listing)throws Exception{
        String cmd = "insert into " + USER_TABLE 
                + "(" + USER_NAME + "," + USER_NICK + ")"
                + " values('" + userName + "','" + userNick + "')";
        
        s.processUpdate(cmd);
        return new User(userName,userNick,null, listing);
    }
    
    /**
     * This loads up a user from the database
     * 
     * @param s - SQL from util.sql, Simplified JDBC call
     * @param userName - The name of the user to load
     * @return
     * @throws java.lang.Exception
     */
    static User getSqlData(sql s, String userName, UserListing listing) throws Exception{
        userName = userName.toLowerCase();
        
        //get its key + properties + nick
        String cmd = "Select "+ USER_KEY + "," + USER_PROPERTIES + "," + USER_NICK 
                +" from " + USER_TABLE 
                + " Where " + USER_NAME + " ='" + userName + "';"; 
        
        ArrayList<String> result = s.processQuery(cmd);
        if(result == null || result.size() == 0){
            return null;
        }

        int key = Integer.valueOf( result.get(0) );
        String prop = result.get(1) ;
        String userNick = result.get(2);

        return new User(userName, userNick, prop, listing);
            
    }    
    
    
}
