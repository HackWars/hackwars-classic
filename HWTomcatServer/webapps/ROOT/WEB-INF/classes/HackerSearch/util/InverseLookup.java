package HackerSearch.util;
import com.plink.dolphinnet.util.*;
import java.util.*;
import java.io.Serializable;

/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

An implementation of a binary list used for storing an Editor's (server's) client data.
*/
public class InverseLookup extends BinaryList implements Serializable{
	/** Get the key used in comparison.*/
	public Object getKey(Object o){
		WordIndex temp=(WordIndex)o;
		return((Object)temp.getWord());
	}

	/** Compare one object to another.*/
	public int compare(Object o1,Object o2){
		String s1=(String)o1;
		String s2=(String)o2;
		return(s1.compareTo(s2));
	}

	public String toString(){
		String returnMe="";
		for(int i=0;i<this.getData().size();i++){
			WordIndex WI=(WordIndex)this.getData().get(i);
			returnMe+=WI.getWord()+"\n";
		}
		return(returnMe);
	}
}