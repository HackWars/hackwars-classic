package chat.messages;

public class MsgInRelationAdd extends MessageIn{
    
    private String nameToAdd;
    private String comment;
    private boolean friend;
    private boolean ignore;
    
    /** Creates a new instance of MsgInChannel */
    public MsgInRelationAdd(String sender, String nameToAdd, String comment, boolean friend, boolean ignore) {
        this.setSender(sender);
        this.nameToAdd = nameToAdd;
        this.comment = comment;
        this.friend = friend;
        this.ignore  = ignore;
    }

    
    /**
     *  Returns a copy of the object,
     */
    public MessageIn copy(){
        return new MsgInRelationAdd(this.getSender(),getNameToAdd(),getComment(),friend,ignore);
    }

    public String getNameToAdd() {
        return nameToAdd;
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
}
