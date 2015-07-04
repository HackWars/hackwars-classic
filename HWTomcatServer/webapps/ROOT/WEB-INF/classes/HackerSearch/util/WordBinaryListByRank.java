package HackerSearch.util;
import com.plink.dolphinstem.*;
import com.plink.dolphinnet.util.*;
import java.util.*;
import java.io.Serializable;

/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

An implementation of a binary list used for storing an Editor's (server's) client data.
*/
public class WordBinaryListByRank extends BinaryList implements Serializable{
	/** Get the key used in comparison.*/
	public Object getKey(Object o){
		WordData temp=(WordData)o;
		return(new Double(temp.getFrequency()));
	}

	/** Compare one object to another.*/
	public int compare(Object o1,Object o2){
		Double s1=(Double)o1;
		Double s2=(Double)o2;
		return(s2.compareTo(s1));
	}

	public String toString(){
		String returnMe="";
		ArrayList data=getData();
		for(int i=0;i<data.size();i++){
			WordData temp=(WordData)data.get(i);
			returnMe+="> "+temp.getData()+" x "+temp.getFrequency()+"\n";
		}
		return(returnMe);
	}

	public void addList(WordBinaryList L){
		for(int i=0;i<L.getData().size();i++){
			WordData A=(WordData)L.getData().get(i);
			WordData B=(WordData)this.get(A.getData());
			if(B!=null){
				B.setFrequency(B.getFrequency()+1.0);
			//	B.setFrequency(B.getFrequency()+A.getFrequency());
			}else{
				this.add(new WordData(A.getData(),1.0));
				//this.add(A);
			}
		}
	}

	public WordBinaryList clone(){
		WordBinaryList Clone=new WordBinaryList();
		for(int i=0;i<this.getData().size();i++){
			WordData temp=(WordData)this.getData().get(i);
			Clone.add(temp.clone());
		}
		return(Clone);
	}

	public void zero(){
		for(int i=0;i<getData().size();i++){
			WordData temp=(WordData)getData().get(i);
			temp.setFrequency(0);
		}
	}
}