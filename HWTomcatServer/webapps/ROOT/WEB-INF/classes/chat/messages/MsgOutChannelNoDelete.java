package chat.messages;


public class MsgOutChannelNoDelete extends MessageOut{
    
    private String channelName;
    
    /** Creates a new instance of MsgChannel */
    public MsgOutChannelNoDelete(String reciver, String channelName) {
        this.channelName = channelName;

        this.setReciver(reciver); 
    }

    public String getChannelName() {
        return channelName;
    }

    
    /**
     *  Returns a copy of the object,
     */
    public MessageOut copy(){
        return new MsgOutChannelNoDelete(this.getReciver(), channelName);
    }    
}
