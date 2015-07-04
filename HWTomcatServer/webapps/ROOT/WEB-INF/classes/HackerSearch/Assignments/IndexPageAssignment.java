package HackerSearch.Assignments;
/**
(C) Ben Coe 2007 <br />
Control the parsing of visited HTML documents. And pre-proccessing on user's
own HTML documents, for the server-side indexer.
*/

import java.io.*;
import java.net.URL;
import java.util.*;
import com.plink.dolphinnet.*;

public class IndexPageAssignment extends Assignment{
	//Data.
	
	private String title="";
	private String address="";
	private String content="";
	
	//Constructor.
	public IndexPageAssignment(int id,String title,String address,String content){
		super(id);
		this.title=title;
		this.address=address;
		this.content=content;
	}
	
	public String getTitle(){
		return(title);
	}
	
	public String getAddress(){
		return(address);
	}
	
	public String getContent(){
		return(content);
	}

	/** Run the assignments implemented task. */
	public Object execute(DataHandler DH){
		DH.addData(this);
		finish();
		return(null);
	}
}
