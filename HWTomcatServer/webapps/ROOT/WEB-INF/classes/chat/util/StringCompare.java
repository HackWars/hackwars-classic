
package chat.util;
import java.util.Comparator;


public class StringCompare implements Comparator{
    
    public int compare(Object obj1, Object obj2){
        String s1 = (String) obj1;
        String s2 = (String) obj2;
        
        int value = s1.compareToIgnoreCase(s2);
        return value;
    }
    
}
