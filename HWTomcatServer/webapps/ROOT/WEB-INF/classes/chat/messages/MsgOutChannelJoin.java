package chat.messages;

import java.util.*;
public class MsgOutChannelJoin extends MessageOut{
    
    private String channelName;
    private String userList[];
    private HashMap admins=new HashMap();
    
    /** Creates a new instance of MsgChannel */
    public MsgOutChannelJoin(String reciver, String channelName, String userList[]) {
        this.channelName = channelName;
        this.userList = userList;
        this.setReciver(reciver); 
    }

    public String getChannelName() {
        return channelName;
    }

    public String[] getUserList(){
        return userList;
    }
		
		public void setAdmin(ArrayList adminList){
			for(int i=0;i<adminList.size();i++)
				admins.put((String)adminList.get(i),"");
		}
    
    public boolean isAdmin(String userName){
			if(admins.get(userName)!=null){
				return(true);
			}
			return(false);
    }
    /**
     *  Returns a copy of the object,
     */
    public MessageOut copy(){
        return new MsgOutChannelJoin(this.getReciver(), channelName, userList);
    }    
    
}
