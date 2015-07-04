package chat.messages;


public class MsgOutChannelRemove extends MessageOut{
    
    private String channelName;
    private String userToRemove;
    
    /** Creates a new instance of MsgChannel */
    public MsgOutChannelRemove(String reciver, String channelName, String userToRemove) {
        this.channelName = channelName;
        this.userToRemove = userToRemove;
        this.setReciver(reciver); 
    }

    public String getChannelName() {
        return channelName;
    }

    public String getUserToRemove(){
        return userToRemove;
    }
    
    /**
     *  Returns a copy of the object,
     */
    public MessageOut copy(){
        return new MsgOutChannelRemove(this.getReciver(), channelName, userToRemove);
    }    
    
}
