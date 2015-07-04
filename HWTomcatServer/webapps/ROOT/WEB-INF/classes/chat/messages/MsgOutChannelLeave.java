package chat.messages;


public class MsgOutChannelLeave extends MessageOut{
    
    private String channelName;
    
    /** Creates a new instance of MsgChannel */
    public MsgOutChannelLeave(String reciver, String channelName) {
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
        return new MsgOutChannelLeave(this.getReciver(), channelName);
    }    
}
