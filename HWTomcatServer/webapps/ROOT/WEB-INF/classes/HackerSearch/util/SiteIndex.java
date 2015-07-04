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
public class SiteIndex implements Serializable{
	private double rank;
	private String address;
	
	public SiteIndex(){
	
	}
	
	public void setRank(double rank){
		this.rank=rank;
	}
	
	public double getRank(){
		return(rank);
	}
	
	public void setAddress(String address){
		this.address=address;
	}
	
	public String getAddress(){
		return(address);
	}
	
	public SiteIndex clone(){
		SiteIndex returnMe=new SiteIndex();
		returnMe.setRank(rank);
		returnMe.setAddress(address);
		return(returnMe);
	}
}