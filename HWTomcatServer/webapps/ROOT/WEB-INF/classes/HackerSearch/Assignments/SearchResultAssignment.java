package HackerSearch.Assignments;
import com.plink.dolphinnet.*;
import java.util.*;
import java.util.zip.*;
import java.io.*;

/**
This assignments returns the data from an RSS news source.
*/

public class SearchResultAssignment extends Assignment implements Serializable{
	private ArrayList SearchResults=new ArrayList();
	private ArrayList SearchTerms=new ArrayList();
	private int size=0;
	private int current=0;
	private int commentCount=0;
	private double ranking=0.0;
	
	/////////////////////////
	// Constructor.
	public SearchResultAssignment(int id){
		super(id);
	}
	
	public void setSize(int size){
		this.size=size;
	}
	
	public int getSize(){
		return(size);
	}
	
	public void setCommentCount(int commentCount){
		this.commentCount=commentCount;
	}
	
	public int getCommentCount(){
		return(commentCount);
	}
	
	public double getRanking(){
		return(ranking);
	}
	
	public void setRanking(double ranking){
		this.ranking=ranking;
	}
	
	public void setCurrent(int current){
		this.current=current;
	}
	
	public int getCurrent(){
		return(current);
	}
	
	public void addResult(SearchResult SR){
		SearchResults.add(SR);
	}
	
	public void addSearchTerm(String Term){
		SearchTerms.add(Term);
	}
	
	public ArrayList getSearchTerms(){
		return(SearchTerms);
	}
	
	public ArrayList getResults(){
		return(SearchResults);
	}
	
	/** Run the assignments implemented task. */
	public Object execute(DataHandler DH){
		DH.addData(this);
		finish();
		return(null);
	}
}
