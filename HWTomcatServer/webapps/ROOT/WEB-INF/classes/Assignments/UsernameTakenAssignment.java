package Assignments;
import com.plink.dolphinnet.*;
import java.util.*;
import java.util.zip.*;
import java.io.*;

/**
This assignments returns the data from an RSS news source.
*/

public class UsernameTakenAssignment extends Assignment implements Serializable{

	/////////////////////////
	// Constructor.
	public UsernameTakenAssignment(int id){
		super(id);
	}
	
	/** Run the assignments implemented task. */
	public Object execute(DataHandler DH){
		DH.addData(this);
		finish();
		return(null);
	}
}
