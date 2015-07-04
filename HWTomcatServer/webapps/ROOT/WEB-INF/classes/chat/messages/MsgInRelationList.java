package chat.messages;

public class MsgInRelationList extends MessageIn{
    

    
    /** Creates a new instance of MsgInChannel */
    public MsgInRelationList(String sender) {
        this.setSender(sender);
    }

    
    /**
     *  Returns a copy of the object,
     */
    public MessageIn copy(){
        return new MsgInRelationList(this.getSender());
    }
}
