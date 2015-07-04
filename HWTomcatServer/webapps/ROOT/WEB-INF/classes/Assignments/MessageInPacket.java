package Assignments;
import com.plink.dolphinnet.*;
import java.util.*;
import java.util.zip.*;
import java.io.*;
import chat.messages.*;

/**
This assignments returns the data from an RSS news source.
*/

public class MessageInPacket extends Assignment implements Serializable{

	private ArrayMessageIn MyMessageIn=null;

	/////////////////////////
	// Constructor.
	public MessageInPacket(ArrayMessageIn MyMessageIn){
		super(0);
		this.MyMessageIn=MyMessageIn;
	}
	
	public ArrayMessageIn getMessageIn(){
		return(MyMessageIn);
	}
	
	/** Run the assignments implemented task. */
	public Object execute(DataHandler DH){
		if(DH!=null)
			DH.addData(MyMessageIn);
		finish();
		return(null);
	}
	
}
