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
public class SQLUserRelation {
    
    /* SQL constants */
    public static final String TABLE_NAME = "userrelation";
        public static final String FIRST_USER = "usera";
        public static final String SECOND_USER = "userb";
        public static final String COMMENT = "comment";
            public static final int MAX_COMMENT_SIZE = 14;
        public static final String RELATION = "relation";
            public static final String RELATION_FRIEND = "FRIEND";
            public static final String RELATION_IGNORE = "IGNORE";  
            
    
    static void updateSQL(sql s, UserRelation u) throws Exception{
        //Kudos if you have an idea what I'm doing here
        String cmd = "UPDATE " + TABLE_NAME + " , " + SQLUser.USER_TABLE 
                //new values comma deliminated
                + " SET "   + COMMENT + " = '" + u.getComment() + "'," + RELATION + " = " + u.getRelationSQL()
                //what will be changed
                + " WHERE " + FIRST_USER + "= (" + SQLUser.getSQLStringSelectKey(u.getMainName()) + ") AND " 
                            + SECOND_USER + "= (" + SQLUser.getSQLStringSelectKey(u.getSecondName()) +") ;";
				
        s.processUpdate(cmd);
    }
    
    static UserRelation newUserRelation(String mainUser, String secondaryUser, String comment, sql s) throws Exception{
        String cmd = "insert into " + TABLE_NAME 
                + "(" + FIRST_USER + "," + SECOND_USER + "," + COMMENT + ")"
                + " values( (" + SQLUser.getSQLStringSelectKey(mainUser) + ") ,(" +SQLUser.getSQLStringSelectKey(secondaryUser) + ") ,'" + comment +"')";
		//System.out.println(cmd);
				
        
        try{
            s.processUpdate(cmd);
            UserRelation relation = new UserRelation(mainUser, secondaryUser, comment, null );            
            return relation;
        }catch(Exception e){
            //couldnt find it
            return null;
        }       
    }
    
    
    static UserRelation[] getRelationList(sql s, String name) throws Exception{
        String cmd = "SELECT " + SQLUser.USER_NAME + " , " + COMMENT + " , " + RELATION
                + " from " + TABLE_NAME  + " , " + SQLUser.USER_TABLE 
                + " Where " + FIRST_USER + " = (" + SQLUser.getSQLStringSelectKey(name) + ") AND " 
                            + SECOND_USER + " = "  + SQLUser.USER_KEY + " AND "
                            +SQLUser.USER_NAME +" IS NOT NULL " +";" ; 
        
        //System.out.println(cmd);

        ArrayList<String> result = s.processQuery(cmd);
        if(result==null){
                return new UserRelation[0];
        }
        UserRelation relationList[] = new UserRelation[result.size()/3 ];
        
        int loopc = 0;
        int loopx = 0;
        while(loopc < result.size() ){
            UserRelation relation;
            
            String curName = result.get(loopc);
            if(curName != null){
                String comment = result.get(loopc + 1);
                String prop = result.get(loopc + 2);
                relation = new UserRelation(name,curName,comment,prop);
                relationList[loopx] = relation;
            }
            
            loopx++;
            loopc+=3;
        }
        return relationList;
    }
    
    
}
