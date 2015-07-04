package util;
/**
UserBinaryList.java<br />
(c) Vulgate 2007<br />

This implementation of a binary list holds users currently logged into the game.
*/

import java.util.*;
import com.plink.dolphinnet.util.*;

public class UserBinaryList extends BinaryList{
    /** Get the key used in comparison.*/

    public Object getKey(Object o){
        User temp=(User)o;
        return((Object)temp.getName());
    }

    /** Compare one object to another.*/
    public int compare(Object o1,Object o2){
        String s1=(String)o1;
        String s2=(String)o2;
        return(s1.compareTo(s2));
    }

    /**
    Return a string representation of the data.
    */
    public String toString(){
        String returnMe="";
		ArrayList Data=this.getData();
		for(int i=0;i<Data.size();i++){
			User U=(User)Data.get(i);
			System.out.println(U.getName());
		}
        return(returnMe);
    }
}

