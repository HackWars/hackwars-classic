package Game;

/**
ApplicationData.java

Used to distribute function calls to the computers in the computer handling system.
*/

import java.util.*;

public class ApplicationData {

	public static final int INSIDE=0;
	public static final int OUTSIDE=1;
	private int source=INSIDE;
	private Object parameters=null;//A generic object array of data.
	private String function="";//The function call being requested.
	private int port=0;//The port that this ApplicationData should be delivered to.
	
	private int sourcePort=0;//Port of the computer generating this function call.
	private String sourceIP="";//IP of the computer generating this request.
	
	//Constructor.
	public ApplicationData(String function,Object parameters,int port,String sourceIP){
		this.sourceIP=sourceIP;
		this.parameters=parameters;
		this.function=function;
		this.port=port;
	}
	
	/**
	Where does this message originate from?
	*/
	public void setSource(int source){
		this.source=source;
	}
	
	/**
	Get where the message comes from.
	*/
	public int getSource(){
		return(source);
	}
	
	/**
	getPort()
	returns the port that is being targeted with this function.
	*/
	public int getPort(){
		return(port);
	}
	
	/**
	The IP that generated this ApplicationData.
	*/
	public String getSourceIP(){
		return(sourceIP);
	}
	
	/**
	Set the port of the source of this application data.
	*/
	public void setSourcePort(int sourcePort){
		this.sourcePort=sourcePort;
	}
	
	/**
	Get the port of the source of this application data message.
	*/
	public int getSourcePort(){
		return(sourcePort);
	}
	
	/**
	getParameters()
	returns the parameter object.
	*/
	public Object getParameters(){
		return(parameters);
	}
	
	
	/**
	setParameters(Object parameters)
	sets the parameters object.
	*/
	public void setParameters(Object parameters){
		this.parameters = parameters;
	}
	
	/**
	getFunction()
	returns the function call.
	*/
	public String getFunction(){
		return(function);
	}
	
	/**
	setFunction(String function)
	sets the function call.
	*/
	public void setFunction(String function){
		this.function=function;
	}
	
}