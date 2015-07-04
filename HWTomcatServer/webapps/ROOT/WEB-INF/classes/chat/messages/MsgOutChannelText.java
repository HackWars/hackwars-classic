package chat.messages;


public class MsgOutChannelText extends MessageOut{
    
    private String channelName;
    private String senderName;
    private String message;
    
    /** Creates a new instance of MsgChannel */
    public MsgOutChannelText(String reciver, String channelName, String senderName, String message) {
        this.channelName = channelName;
        this.senderName = senderName;
        this.message = message;
        this.setReciver(reciver); 
    }

    public String getChannelName() {
        return channelName;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessage() {
        return message;
    }
    
    /**
     *  Returns a copy of the object,
     */
    public MessageOut copy(){
        return new MsgOutChannelText(this.getReciver(), channelName, senderName,message );
    }    
}
