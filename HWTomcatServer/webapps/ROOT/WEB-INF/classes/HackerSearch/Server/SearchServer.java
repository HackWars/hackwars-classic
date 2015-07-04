package HackerSearch.Server;

/**
(c) Vulgate 2007<br />
The main server entry point, pulls everything together.
*/

import com.plink.dolphinnet.*;
import com.plink.dolphinnet.util.*;
import HackerSearch.util.*;
import java.io.*;
import java.math.*;
import java.util.*;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Calendar;
import HackerSearch.Assignments.*;
import Assignments.*;

public class SearchServer{

    //Data.
	private boolean RESULT=false;
	private static SearchServer MySearchServer=null;
	private static SearchHandler MySearchHandler=null;
	private Assignment Result=null;
	private HashMap Results=new HashMap();

    public SearchServer(boolean fake){//Used for constructor just keep this here in IPartys.
    }
	
	public SearchServer(){}//Default Constructor.
	
	public static SearchServer getInstance(){
		if(MySearchServer==null){
			MySearchServer=new SearchServer(false);
			MySearchHandler=SearchHandler.getInstance(MySearchServer);
		}
		
		return(MySearchServer);
	}
	
	/**
	Get whether a result has been returned.
	*/
	public boolean hasResult(int STAMP){
		if(Results.get(new Integer(STAMP))!=null)
			return(true);
		return(false);
	//	return(RESULT);
	}
	
	/**
	Get the results.
	*/
	public Assignment getResult(int STAMP){
		return((Assignment)Results.get(new Integer(STAMP)));
	//	return(Result);
	}
	
	/**
	Dispatch a packet assignment.
	*/
	public void dispatchPacket(Assignment DispatchMe,int connectionID){
		Results.put(new Integer(DispatchMe.getID()),DispatchMe);
	//	Result=DispatchMe;
	//	RESULT=true;
	}
	
	/**
	Do search.
	*/
	public SearchResultAssignment requestSearch(Assignment MyAssignment){
		return(MySearchHandler.requestSearch((SearchAssignment)MyAssignment));
	}
	
	/** Receive a completed assignment.*/
    public void returnAssignment(Assignment MyAssignment){
	/*	if(MyAssignment instanceof SearchAssignment){
			RESULT=false;
			SearchAssignment SA=(SearchAssignment)MyAssignment;
			MySearchHandler.requestSearch(SA);
		}else*/
		
		if(MyAssignment instanceof IndexPageAssignment){
			IndexPageAssignment IPA=(IndexPageAssignment)MyAssignment;
			MySearchHandler.indexPage(IPA.getTitle(),IPA.getAddress(),IPA.getContent());
		}
	}
}