package chat.messages;

public class MsgOutRelationAdd extends MessageOut{
    
    private String name;
    private String comment;
    private boolean friend;
    private boolean ignore;
    private boolean online;
    
    /** Creates a new instance of MsgInChannel */
    public MsgOutRelationAdd(String reciver, String name, String comment, boolean friend, boolean ignore, boolean online) {
        this.setReciver(reciver);
        //Should clone these,
        this.name = name;
        this.comment = comment;
        this.friend = friend;
        this.ignore = ignore;
        this.online = online;
    }

    
    /**
     *  Returns a copy of the object,
     */
    public MessageOut copy(){
        return new MsgOutRelationAdd(this.getReciver(),getName(),getComment(),isFriend(),isIgnore(), isOnline());
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
        return online;
    }
}
