package Assignments;
import com.plink.dolphinnet.*;
import java.util.*;
import java.io.*;
import java.math.*;
/**
An implementation of the abstract base assignment...This is where the
processing to be distributed onto other computers should be placed.
Every instance of an assignment is distributed to a specific Reporter
(client). So the more Assignments the more division of work.
*/

public class LoginAssignment extends Assignment implements Serializable{
	private String user;
	private String pass;
	private String ip;
	private byte[] publicKey=null;

	/////////////////////////
	// Constructor.
	public LoginAssignment(int id,String user,String pass,String ip){
		super(id);
		this.user=user.toLowerCase();
		this.pass=pass;
		this.ip=ip;
	}
	/////////////////////////
	// Getters.
	public String getPass(){
		return(pass);
	}

	public String getUser(){
		return(user);
	}
	
	public String getIP(){
		return(ip);
	}
	
	public void setPublicKey(byte publicKey[]){
		this.publicKey=publicKey;
	}
	
	public byte[] getPublicKey(){
		return(publicKey);
	}

	/** Run the assignments implemented task. */
	public Object execute(DataHandler DH){
		DH.addData(this);
		finish();
		return(null);
	}
}
