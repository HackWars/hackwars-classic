package GUI;


import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import java.net.URL;

public class XMLRPCCall{


	public static Object execute(String url,String method,Object[] send){
		Object result=null;
		try{
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(url));
			//config.setServerURL(new URL("http://localhost/Hacker2/Hacker/help/tutoriallist.php"));
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);
			result = client.execute(method, send);
		}catch(Exception e){e.printStackTrace();}
		
		return result;
	
	
	}
}
