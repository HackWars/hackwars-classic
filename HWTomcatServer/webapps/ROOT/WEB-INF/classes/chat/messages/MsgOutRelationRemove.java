package chat.messages;

public class MsgOutRelationRemove extends MessageOut{
    
    private String name;

    
    /** Creates a new instance of MsgInChannel */
    public MsgOutRelationRemove(String reciver, String name) {
        this.setReciver(reciver);
        this.name = name;
    }

    
    /**
     *  Returns a copy of the object,
     */
    public MessageOut copy(){
        return new MsgOutRelationRemove(this.getReciver(),name);
    }
}
