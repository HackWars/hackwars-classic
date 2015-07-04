package HackerSearch.util;
import com.plink.dolphinnet.util.*;
import java.util.*;
import java.io.Serializable;

/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

An implementation of a binary list used for storing an Editor's (server's) client data.
*/
public class SiteDataBinaryList extends BinaryList implements Serializable{
	
	/** Get the key used in comparison.*/
	public Object getKey(Object o){
		SiteData temp=(SiteData)o;
		return(temp.getAddress());
	}

	/** Compare one object to another.*/
	public int compare(Object o1,Object o2){
		String s1=(String)o1;
		String s2=(String)o2;
		return(s1.compareTo(s2));
	}
	
	public String toString(){
		return(null);
	}
}