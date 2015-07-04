package Assignments;
import com.plink.dolphinnet.*;
import java.util.*;
import java.io.*;
import java.math.*;
import util.*;
/**
An implementation of the abstract base assignment...This is where the
processing to be distributed onto other computers should be placed.
Every instance of an assignment is distributed to a specific Reporter
(client). So the more Assignments the more division of work.
*/

public class RemoteFunctionCall extends Assignment implements Serializable{
	private Object parameters;
	private String function;
	private int connectionID;
	private byte[] byteFunction=null;
	/////////////////////////
	// Constructor.
	public RemoteFunctionCall(int id,String function,Object parameters){
		super(id);
		if(function!=null)
			byteFunction=Encryption.getInstance().encrypt(function.getBytes());
		this.parameters = parameters;
	}
	/////////////////////////
	// Getters.
	public Object getParameters(){
		return(parameters);
	}

	public String getFunction(){
		return(function);
	}
	
	public void setFunction(String function){
		this.function=function;
	}
	
	public void decryptFunction(Encryption MyEncryption,String ip){
		this.function=new String(MyEncryption.decrypt(byteFunction,ip));
	}

	/** Run the assignments implemented task. */
	public Object execute(DataHandler DH){
		DH.addData(this);
		finish();
		return(null);
	}
}
