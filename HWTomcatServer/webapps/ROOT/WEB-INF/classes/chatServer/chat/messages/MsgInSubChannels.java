package chat.messages;
import java.util.*;

public class MsgInSubChannels extends MessageIn{
    
    /** Creates a new instance of MsgInChannel */
    public MsgInSubChannels(String sender) {
        this.setSender(sender);
    }
    
    /**
     *  Returns a copy of the object,
     */
    public MessageIn copy(){
        return new MsgInSubChannels(this.getSender() );
    }
    
}
