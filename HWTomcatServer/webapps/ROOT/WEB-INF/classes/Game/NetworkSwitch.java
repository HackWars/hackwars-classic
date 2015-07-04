package Game;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import java.net.URL;
import util.zip;
/**
NetworkSwitch.java

Used to distribute function calls to the computers in the computer handling system.
*/

import java.util.*;

public class NetworkSwitch {
	private Computer MyComputer=null;
	private ComputerHandler MyComputerHandler=null;

	//Constructor.
	public NetworkSwitch(Computer MyComputer,ComputerHandler MyComputerHandler){
		this.MyComputerHandler=MyComputerHandler;
		this.MyComputer=MyComputer;
	}
	
    public ComputerHandler getMyComputerHandler() {
        return MyComputerHandler;
    }
    
	/*
	The switch.
	*/
	public void addData(ApplicationData AD,String ip){
		if(ip.equals("062.153.7.142")){//This is a reserved IP used for linking in external networking.
			//Has a website been served for a front-end client?
			if(AD.getFunction().equals("webpage")){
				System.out.println("Attempting to return site.");
									
				Object O[]=(Object[])AD.getParameters();
				try{
					XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
					config.setServerURL(new URL("http://localhost:8081/xmlrpc"));
					XmlRpcClient client = new XmlRpcClient();
					client.setConfig(config);
					Object[] params=null;
					params = new Object[]{(String)O[0],zip.zipString((String)O[1]),(Integer)O[3]};
					String result = (String) client.execute("hackerRPC.returnWebsite", params);
				}catch(Exception e){
					e.printStackTrace();
				}
						
			}
		}else if(MyComputer!=null&&MyComputer.getIP().equals(ip)){
			MyComputer.addData(AD);
		}else{
			MyComputerHandler.addData(AD,ip);
		}
	}
}