package Assignments;
import com.plink.dolphinnet.*;
import java.util.*;
import java.util.zip.*;
import java.io.*;

/**
This assignments returns the data from an RSS news source.
*/

public class LoginSuccessAssignment extends Assignment implements Serializable{

	private boolean NPC=false;
	private String ip="";
	private String encryptedIP="";
	private byte publicKey[]=null;

	/////////////////////////
	// Constructor.
	public LoginSuccessAssignment(int id,String ip,String encryptedIP,boolean NPC){
		super(id);
		this.NPC=NPC;
		this.ip=ip;
		this.encryptedIP=encryptedIP;
	}
	
	public String getIP(){
		return(ip);
	}
	
	public String getEncryptedIP(){
		return(encryptedIP);
	}
	
	public boolean isNPC(){
		return(NPC);
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
