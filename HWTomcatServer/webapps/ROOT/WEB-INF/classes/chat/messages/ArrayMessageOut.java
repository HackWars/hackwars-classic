package chat.messages;
import java.util.List;
import java.io.Serializable;

public class ArrayMessageOut implements Serializable{
    
    private MessageOut list[] = null;
    
    public ArrayMessageOut(List<MessageOut> msgOutList) throws Exception{
        if(msgOutList == null){
            throw new Exception("Null value given");
        }
        if(msgOutList.size() <= 0){
            throw new Exception("No messages given, arraylist size zero");
        }
        
        int loopc = 0;
        int size = msgOutList.size();
        list = new MessageOut[size];
        
        while(loopc < size){
            list[loopc] = msgOutList.get(loopc);
            loopc++;
        }
        
    }
    
    public MessageOut getMessage(int index) throws Exception{
        if(index < 0 || index > list.length){
            throw new Exception("Invalid postion <" + index + ">" );
        }
        return list[index];
    }
    
    public int getSize(){
        return list.length;
    }
}
