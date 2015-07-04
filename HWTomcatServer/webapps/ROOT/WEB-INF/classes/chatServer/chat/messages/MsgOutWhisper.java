
package chat.messages;


public class MsgOutWhisper extends MessageOut{
    
    private String sender = null;
    private String message = null;
    
    /** Creates a new instance of MsgWhisper */
    public MsgOutWhisper(String reciver, String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.setReciver(reciver);
    }
    
    public String getSender(){
        return sender;
    }
    
    public String getMessage(){
        return message;
    }
    
    /**
     *  Returns a copy of the object,
     */
    public MessageOut copy(){
        return new MsgOutWhisper(this.getReciver(), sender, message);
    }     
}
