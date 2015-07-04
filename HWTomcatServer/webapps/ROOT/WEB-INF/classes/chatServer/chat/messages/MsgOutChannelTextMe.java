package chat.messages;


public class MsgOutChannelTextMe extends MessageOut{
    
    private String channelName;
    private String senderName;
    private String message;
    
    /** Creates a new instance of MsgChannel */
    public MsgOutChannelTextMe(String reciver, String channelName, String senderName, String message) {
        this.channelName = channelName;
        this.senderName = senderName;
        this.message = message;
        this.setReciver(reciver); 
		System.out.println("MessageOutChannelTextMe created");
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
        return new MsgOutChannelTextMe(this.getReciver(), channelName, senderName,message );
    }    
}
