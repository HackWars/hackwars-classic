package chat.server;
import chat.messages.MessageOut;
import chat.messages.ArrayMessageOut;

import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.List;

public class UserMsgBox {
    
    LinkedList <MessageOut> outBox = new LinkedList<MessageOut>() ;  
    private Semaphore semi = new Semaphore(1, true);
    
    void addMessage(MessageOut msg){
        try{
            semi.acquire();
            outBox.addLast(msg);
            semi.release();
        }catch(Exception e){
            System.out.println("AlexChat.UserMsgBox, Unexpected Error " + e.getMessage());
        }
    }
    
    /**
     * Pops out a messageArray, null if empty;
     */
    public ArrayMessageOut popMessage(){
        ArrayMessageOut ret;
        
        if(outBox.size() == 0){
            return null;
        }
        
        try{
            semi.acquire();
            ret = new ArrayMessageOut(outBox);
            //Empty the box;
            while(outBox.isEmpty() == false){
                outBox.remove();
            }
            
        }catch(Exception e){
            ret = null;
        }
        semi.release();
        
        return ret;
    }
    
}
