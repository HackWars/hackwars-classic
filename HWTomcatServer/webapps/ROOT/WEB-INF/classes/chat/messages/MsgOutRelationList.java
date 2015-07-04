package chat.messages;

public class MsgOutRelationList extends MessageOut{
    
    private String names[];
    private String comments[];
    private boolean friends[];
    private boolean ignore[];
    private boolean online[];
    /** Creates a new instance of MsgInChannel */
    public MsgOutRelationList(String reciver, String names[], String comments[], boolean friends[], boolean ignore[],  boolean online[]) {
        this.setReciver(reciver);
        //Should clone these,
        this.names = names;
        this.comments = comments;
        this.friends = friends;
        this.ignore = ignore;
        this.online = online;
    }

    
    
    
    /**
     *  Returns a copy of the object,
     */
    public MessageOut copy(){
        return new MsgOutRelationList(this.getReciver(),getNames(),getComments(),getFriends(),getIgnore(), getOnline());
    }

    public String[] getNames() {
        return names;
    }

    public String[] getComments() {
        return comments;
    }

    public boolean[] getFriends() {
        return friends;
    }

    public boolean[] getIgnore() {
        return ignore;
    }
    
    public boolean[] getOnline(){
        return online;
    }
}
