package Assignments;
import com.plink.dolphinnet.*;
import java.util.*;
import java.util.zip.*;
import java.io.*;

/**
This assignments returns the data from an RSS news source.
*/

public class PingAssignment extends Assignment implements Serializable{
	private String user="";

	/////////////////////////
	// Constructor.
	public PingAssignment(int id,String user){
		super(id);
		this.user=user;
	}
	
	public String getUser(){
		return(user);
	}
	
	/** Run the assignments implemented task. */
	public Object execute(DataHandler DH){
		if(DH!=null)
			DH.addData(this);
		finish();
		return(null);
	}
}
