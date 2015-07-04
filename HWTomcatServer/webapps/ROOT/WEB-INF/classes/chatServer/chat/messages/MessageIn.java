package chat.messages;
import java.io.Serializable;


public abstract class MessageIn implements Serializable{
    private String sender;
    
    void setSender(String sender){
        this.sender = sender;
    }
    
    public String getSender(){
        return sender;
    }
    
    /**
     * Some messages require SQL access like 
     *   changing user properties, or user realtions like ignore firend,
     *   
     *   if this is setup correctly the model can give the message to the correct thread, 
     *   allowing cpu to be used more effiecently.
     *
     *  If its not setup correctly, the model will throw an exception,
     */
    public boolean getSqlNeeded(){
        return false;
    }
    
    /**
     *  Returns a copy of the object,
     */
    public MessageIn copy(){
        return null;
    }    
    
}
