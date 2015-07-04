package HackerSearch.util;
import com.plink.dolphinnet.util.*;
import com.plink.dolphinstem.*;
import java.util.*;
import java.io.Serializable;

/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

An implementation of a binary list used for storing an Editor's (server's) client data.
*/
public class SiteData implements Serializable{
	private String address;
	private String description;
	private String title;
	private WordBinaryList MyTerms=null;
	
	public SiteData(){
	
	}
	
	public void setAddress(String address){
		this.address=address;
	}
	
	public String getAddress(){
		return(address);
	}
	
	public void setDescription(String description){
		this.description=description;
	}
	
	public String getDescription(){
		return(description);
	}
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public String getTitle(){
		return(title);
	}
	
	public void setTerms(WordBinaryList MyTerms){
		this.MyTerms=MyTerms;
	}
	
	public WordBinaryList getTerms(){
		return(MyTerms);
	}
}