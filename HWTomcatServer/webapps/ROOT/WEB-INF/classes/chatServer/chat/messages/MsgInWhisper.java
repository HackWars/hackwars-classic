
package chat.messages;


public class MsgInWhisper extends MessageIn{
    
    private String reciver = null;
    private String message = null;
    
    /** Creates a new instance of MsgWhisper */
    public MsgInWhisper(String sender, String reciver, String message) {
        this.reciver = reciver;
        this.message = message;
        this.setSender(sender);
    }
    
    public String getReciver(){
        return reciver;
    }
    
    public String getMessage(){
        return message;
    }
    
    /**
     *  Returns a copy of the object,
     */
    public MessageIn copy(){
        return new MsgInWhisper(this.getSender(), reciver, message);
    }     
}
