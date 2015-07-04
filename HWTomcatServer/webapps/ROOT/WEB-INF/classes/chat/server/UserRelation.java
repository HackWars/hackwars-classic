package chat.server;
import util.sql;


public class UserRelation {
    public boolean ignore = false;
    public boolean friend = false;
    private String comment = "";
    private boolean update = false;
    private String userMainName = null;
    private String userSecondaryName = null;
    
    
    UserRelation(String userMainName, String userSecondaryName, String comment, String properties ) throws Exception{
        this.userMainName = userMainName;
        this.userSecondaryName = userSecondaryName;
        this.setComment(comment);
        this.loadSQLRelation(properties);
    }
    
    public boolean isIgnore() {
        return ignore;
    }
    
    public void setIgnore(boolean ignore){
        //this.ignore = ignore;
		// hack fix by silverlight to allow ignore to be undone
		// TODO: find a 'real' way to fix
		this.ignore = !this.ignore;
        this.update = true;
    }
    
    public boolean isFriend() {
        return friend;
    }
    
    public void setFriend(boolean friend){
        this.friend = friend;
        this.update = true;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) throws Exception{
        if(comment == null){
            comment = "";
        }
        
        if(comment.length() > SQLUserRelation.MAX_COMMENT_SIZE){
            throw new Exception("Comments can not exceed a length of " + SQLUserRelation.MAX_COMMENT_SIZE);
        }
        
        this.comment = comment;
        this.update = true;
    }
        
    void close(sql s) throws Exception{
        //if(this.update == true){
            SQLUserRelation.updateSQL(s, this); 
        //}
    }
    
    //Converties all the properties to one nice SQL staetment (Probably)
    String getRelationSQL(){
        String res = "('";
        boolean first = true;
        
        if(this.isFriend() != false){
            res += SQLUserRelation.RELATION_FRIEND;
            first = false;
        }
        
        if(this.isIgnore() != false){
            if(first == false){
                res +=",";
            }
            res += SQLUserRelation.RELATION_IGNORE;
            first = false;
        }
        res += "')";
        return res;
    }
    
    //Give it the sql propeties and it will load them
    void loadSQLRelation(String s){
		//System.out.println("Alex User Relation, " + s);
        if(s == null){
            this.setFriend(false);
            this.setIgnore(false);
            return;
        }

        if(s.indexOf(SQLUserRelation.RELATION_FRIEND) != -1){
            this.setFriend(true);
        }
        if(s.indexOf(SQLUserRelation.RELATION_IGNORE) != -1){
			//System.out.println("Alex User Relation, IGNORE");
            this.setIgnore(true);
        }
    }
    

    
    public String getSecondName(){
        return this.userSecondaryName;
    }
    
    public String getMainName(){
        return this.userMainName;
    }
    
}
