package chat.messages;


public class MsgOutChannelAdd extends MessageOut{
    
    private String channelName;
    private String userToAdd;
    private boolean admin=false;
    
    /** Creates a new instance of MsgChannel */
    public MsgOutChannelAdd(String reciver, String channelName, String userToAdd, boolean admin) {
        this.channelName = channelName;
        this.userToAdd = userToAdd;
        this.admin=admin;
        this.setReciver(reciver); 
    }

    public String getChannelName() {
        return channelName;
    }

    public String getUsertoAdd(){
        return userToAdd;
    }
    
    public boolean getAdmin(){
			return(admin);
    }
    
    /**
     *  Returns a copy of the object,
     */
    public MessageOut copy(){
        return new MsgOutChannelAdd(this.getReciver(), channelName, userToAdd,admin);
    } 
    
}
