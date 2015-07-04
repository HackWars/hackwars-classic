package HackerSearch.Assignments;

/**
A WebFile contains both data and
*/
import java.io.*;
public class SearchResult implements Serializable{
	private String title;
	private String description;
	private String address;
	
	public void setTitle(String title){
		this.title=title;
	}
	
	
	public String getTitle(){
		return(title);
	}
	
	public void setDescription(String description){
		this.description=description;
	}
	
	public String getDescription(){
		return(description);
	}
	
	public void setAddress(String address){
		this.address=address;
	}
	
	public String getAddress(){
		return(address);
	}
}
