package Hacktendo;
import java.util.*;
import java.io.*;
import com.plink.dolphinnet.util.*;

/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

An abstract basis for a binary searchable list. You must implement getKey() which is used to extract
the primary key from the list of objects you are storing and compare() which is used to compare
two objects.
*/
public class SpriteByID extends BinaryList{

	/** Get the Key used in comparison.*/
	public Object getKey(Object o){
		Sprite S=(Sprite)o;
		return(new Integer(S.getSpriteID()));
	}

	/** Compare one object to another.*/
	public int compare(Object o1,Object o2){
		Integer A=(Integer)o1;
		Integer B=(Integer)o2;
		return(A.compareTo(B));
	}

	public String toString(){
		return("");
	}
}