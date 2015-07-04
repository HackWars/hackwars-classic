package chat.messages;
import java.io.Serializable;



public abstract class MessageOut implements Serializable{
    
    private String reciver = null;

    public String getReciver(){
        return reciver;
    }
    
    void setReciver(String reciver){
        this.reciver = reciver;
    }
    
    /**
     *  Returns a copy of the object,
     */
    public MessageOut copy(){
        return null;
    }    
    
}
