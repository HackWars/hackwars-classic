/*
 * MsgOutError.java
 *
 * Created on December 21, 2007, 7:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package chat.messages;

/**
 *
 * @author Owner
 */
public class MsgOutError extends MessageOut{
    
    private String message = null;
    
    /** Creates a new instance of MsgOutError */
    public MsgOutError(String reciver, String message) {
        this.setReciver( reciver);
        this.message = message;
    }
    
    public String getMessage(){
        return this.message;
    }
    
    /**
     *  Returns a copy of the object,
     */
    public MessageOut copy(){
        return new MsgOutError(this.getReciver(), message);
    }        
    
}
