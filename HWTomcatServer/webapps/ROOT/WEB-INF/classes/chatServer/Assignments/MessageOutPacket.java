package Assignments;
import com.plink.dolphinnet.*;
import java.util.*;
import java.util.zip.*;
import java.io.*;
import chat.messages.*;

/**
This assignments returns the data from an RSS news source.
*/

public class MessageOutPacket extends Assignment implements Serializable{

	private ArrayMessageOut MyMessageOut=null;

	/////////////////////////
	// Constructor.
	public MessageOutPacket(ArrayMessageOut MyMessageOut){
		super(0);
		this.MyMessageOut=MyMessageOut;
	}
	
	/** Run the assignments implemented task. */
	public Object execute(DataHandler DH){
		if(DH!=null)
			DH.addData(MyMessageOut);
		finish();
		return(null);
	}
	
}
