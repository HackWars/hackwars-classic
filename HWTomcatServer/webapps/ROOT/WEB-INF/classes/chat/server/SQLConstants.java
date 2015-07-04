package chat.server;
import util.sql;

public class SQLConstants {
    public static final String DATABASE_NAME = "alex_chat";
    public static final String LOGIN_NAME = "root";  //not very safe, but its server side, should be anyways
    public static final String LOGIN_PASSWORD = "green";
    public static final String LOGIN_ADDRESS = "localhost";

    static sql getSQL(){
        sql s = new sql(SQLConstants.LOGIN_ADDRESS,  SQLConstants.DATABASE_NAME
        ,SQLConstants.LOGIN_NAME, SQLConstants.LOGIN_PASSWORD);

        return s;
        
    }
    
    static void closeSQL(sql s){
        s.close();
    }
}
