package com.plink.dolphinnet;
import java.util.*;
import com.plink.dolphinnet.util.*;

/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

An implementation of a binary list used for storing an Editor's (server's) client data.
*/
public class ClientBinaryList extends BinaryList{
	/** Get the key used in comparison.*/
	public Object getKey(Object o){
		ClientData temp=(ClientData)o;
		return(new Integer(temp.getID()));
	}

	/** Compare one object to another.*/
	public int compare(Object o1,Object o2){
		Integer i1=(Integer)o1;
		Integer i2=(Integer)o2;
		return(i1.compareTo(i2));
	}

	public String toString(){
		String returnMe="";
		ArrayList data=getData();
		for(int i=0;i<data.size();i++){
			ClientData temp=(ClientData)data.get(i);
			returnMe+="> "+temp.getID()+"\n";
		}
		return(returnMe);
	}
}