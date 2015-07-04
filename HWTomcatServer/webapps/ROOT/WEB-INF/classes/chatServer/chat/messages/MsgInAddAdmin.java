
package chat.messages;


public class MsgInAddAdmin extends MessageIn{
    
    private String reciver = null;
    
    /** Creates a new instance of MsgWhisper */
    public MsgInAddAdmin(String sender, String reciver) {
        this.reciver = reciver;
        this.setSender(sender);
    }
    
    public String getReciver(){
        return reciver;
    }
    
    /**
     *  Returns a copy of the object,
     */
    public MessageIn copy(){
        return new MsgInAddAdmin(this.getSender(), reciver);
    }     
}
