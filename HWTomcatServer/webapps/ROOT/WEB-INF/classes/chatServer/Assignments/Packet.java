package Assignments;
import com.plink.dolphinnet.*;
import java.util.*;
import java.util.zip.*;
import java.io.*;

/**
This assignments returns the data from an RSS news source.
*/

public class Packet extends Assignment implements Serializable{

	/////////////////////////
	// Constructor.
	public Packet(){
		super(0);
	}
	
	/** Run the assignments implemented task. */
	public Object execute(DataHandler DH){
		if(DH!=null)
			DH.addData(this);
		finish();
		return(null);
	}
	
}
