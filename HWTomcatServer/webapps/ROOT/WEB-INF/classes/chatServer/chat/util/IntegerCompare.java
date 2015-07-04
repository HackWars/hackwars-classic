

package chat.util;
import java.util.Comparator;


public class IntegerCompare implements Comparator{
    
    public int compare(Object obj1, Object obj2){
        Integer int1 = (Integer) obj1;
        Integer int2 = (Integer) obj2;

        return int1 - int2;
    }
    
}
