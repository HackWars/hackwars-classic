/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chat.client;

/**
 *
 * @author Owner
 */
public class StructRelation {
    private String name;
    private String comment;
    private boolean friend;
    private boolean ignore;
    private boolean online;
    
    public StructRelation(String name, String comment, boolean friend, boolean ignore, boolean online){
        this.name = name;
        this.comment = comment;
        this.friend = friend;
        this.ignore = ignore;
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public boolean isFriend() {
        return friend;
    }

    public boolean isIgnore() {
        return ignore;
    }
    
    public boolean isOnline(){
        return this.online;
    }
    
}
