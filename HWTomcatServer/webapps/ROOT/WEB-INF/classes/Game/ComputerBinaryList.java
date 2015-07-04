package Game;
/**
UserBinaryList.java<br />
(c) Vulgate 2007<br />

This implementation of a binary list holds users currently logged into the game.
*/

import java.util.*;
import com.plink.dolphinnet.util.*;

public class ComputerBinaryList extends BinaryList{
    /** Get the key used in comparison.*/

    public Object getKey(Object o){
        Computer temp=(Computer)o;
        return((Object)temp.getIP());
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
        return(returnMe);
    }
}

