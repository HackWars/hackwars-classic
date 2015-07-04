package HackerSearch.Assignments;
/**
(C) Ben Coe 2007 <br />
Control the parsing of visited HTML documents. And pre-proccessing on user's
own HTML documents, for the server-side indexer.
*/

import java.io.*;
import java.net.URL;
import java.util.*;
import com.plink.dolphinstem.util.*;
import com.plink.dolphinstem.*;
import com.plink.dolphinnet.*;
import HackerSearch.util.*;

public class SearchAssignment extends Assignment{
	//Data.
	private int index=0;
	private boolean querySet=false;
	private String query="";
	
	//Constructor.
	public SearchAssignment(int id){
		super(id);
	}

	public void setIndex(int index){
		this.index=index;
	}
	
	public int getIndex(){
		return(this.index);
	}
	
	public boolean getQuerySet(){
		return(querySet);
	}
	
	/** Index the title of the page using the ranking algorithm.*/
	public void setVector(String query){
		this.query=query;
	}
	
	public WordBinaryList getVector(){
	
		WordBinaryList TitleVector=null;
		ArrayList PrimeMe=new ArrayList();
		PrimeMe.add(query);
		TextSource Prime=new TextSource("source",PrimeMe);
		Prime.setStopWordFile(SearchHandler.STOP_WORD_LOCATION);
		
		Prime.prime();
		ArrayList temp=Prime.getItems();
		if(temp!=null)
		if(temp.size()>0)
			TitleVector=((ItemData)temp.get(0)).getVectorData();
		
		return(TitleVector);
		
		/*if(this.Vector!=null){
			if(Vector.getData().size()!=0)
				querySet=true;
		}*/
	}

	/** Run the assignments implemented task. */
	public Object execute(DataHandler DH){
		DH.addData(this);
		finish();
		return(null);
	}
}
