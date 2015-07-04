package HackerSearch.util;
import com.plink.dolphinnet.util.*;
import java.util.*;
import java.io.Serializable;

/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

An implementation of a binary list used for storing an Editor's (server's) client data.
*/
public class RankBinaryList extends BinaryList implements Serializable{
	
	/** Get the key used in comparison.*/
	public Object getKey(Object o){
		SiteIndex temp=(SiteIndex)o;
		return(new Double(temp.getRank()));
	}

	/** Compare one object to another.*/
	public int compare(Object o1,Object o2){
		Double s1=(Double)o1;
		Double s2=(Double)o2;
		s1=new Double(s1.doubleValue()*-1.0);
		s2=new Double(s2.doubleValue()*-1.0);
		return(s1.compareTo(s2));
	}
	
	public String toString(){
		String returnMe="";
		for(int i=0;i<getData().size();i++){
			SiteIndex SI=(SiteIndex)getData().get(i);
			returnMe+=SI.getAddress()+"->"+SI.getRank()+"\n";
		}
		return(returnMe);
	}
}