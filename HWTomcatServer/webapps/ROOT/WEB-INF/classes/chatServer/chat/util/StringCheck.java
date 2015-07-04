/*
 * Certain strings are simply not allowed, this gives the ok or not ok
 * 
 * 
 */

package chat.util;

/**
 *
 * @author Owner
 */
public class StringCheck {
    public static String SQL_REGEX_CHECK = "([a-zA-Z]||[0-9]||[ \\._-]||[\\u003C\\u003E])*";
    
    public static boolean validSQLString(String s){
        return s.matches(SQL_REGEX_CHECK);
    }
    
}
