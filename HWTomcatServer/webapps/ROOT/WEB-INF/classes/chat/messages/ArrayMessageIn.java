package chat.messages;
import java.util.List;
import java.io.Serializable;

public class ArrayMessageIn implements Serializable{
    
    private MessageIn list[] = null;
    
    public ArrayMessageIn(List<MessageIn> msgInList) throws Exception{
        if(msgInList == null){
            throw new Exception("Null value given");
        }
        if(msgInList.size() <= 0){
            throw new Exception("No messages given, arraylist size zero");
        }
        
        int loopc = 0;
        int size = msgInList.size();
        list = new MessageIn[size];
        
        while(loopc < size){
            list[loopc] = msgInList.get(loopc);
            loopc++;
        }
        
    }
    
    public MessageIn getMessage(int index) throws Exception{
        if(index < 0 || index > list.length){
            throw new Exception("Invalid postion <" + index + ">" );
        }
        return list[index];
    }
    
    public int getSize(){
        return list.length;
    }
}
