package chat.messages;
import java.util.*;

public class MsgOutSubChannels extends MessageOut{
    
    private String subscribedChannels[] = null;
		private String users[][] = null;
		private HashMap admins=new HashMap();
	
    /** Creates a new instance of MsgInChannel */
    public MsgOutSubChannels(String reciver, String subscribedChannels[] , String users[][]) {
        this.setReciver(reciver);
        this.subscribedChannels = subscribedChannels;
		this.users = users;
    }
    
    public String[] getSubscribedChannels(){
        return subscribedChannels;
    }
    
		public String[][] getUsers(){
			return users;
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
	MessageOut ret = new MsgOutSubChannels( this.getReciver(), subscribedChannels, users );
        return ret;
    }
    
}
