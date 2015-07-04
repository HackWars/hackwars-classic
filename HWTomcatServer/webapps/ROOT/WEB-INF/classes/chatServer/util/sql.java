package util;

/**
Programmed: Ben Coe.(2005)<br />
sql.java
Used to make calls to an SQL database.
*/

import java.util.*;
import java.sql.*;
import javax.sql.DataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.*;

public class sql{
    private boolean connect;
    private Connection c;
    private MysqlDataSource dataSource;

	private static String ConfigConnection = null;
	private static String ConfigUsername = null;
	private static String ConfigPassword = null;
	private static int ConfigPort = 0;

    //Constructors.
    /**
    Depricated connects to database using hard-coded variables.
    */
    public sql(){
        connect=false;

        //Connect to the database.
        c=null;
        dataSource = new MysqlDataSource();
        dataSource.setDatabaseName("volgate");
        dataSource.setServerName("www.mariealighieri.com");
        dataSource.setPort(3306);
        try{
            c = dataSource.getConnection("deepwater","awa878");
            connect=true;
        }catch(Exception e){
        
        }
    }

    public sql(String Connection,String DB,String Username,String Password){
		if(ConfigConnection == null) {
			try {
				BufferedReader BR = new BufferedReader(new FileReader("db.ini"));
				ConfigConnection = BR.readLine();
				ConfigUsername = BR.readLine();
				ConfigPassword = BR.readLine();
				ConfigPort = new Integer(BR.readLine());
			} catch (Exception e) {
			//	e.printStackTrace();
			}
			
			if(ConfigConnection == null) {
				ConfigConnection = "localhost";
				ConfigUsername = "root";
				ConfigPassword = "";
				ConfigPort = 3306;
			} else {
				System.out.println("Configuration file sucessfully loaded.");
			}
		}
		
		Connection = ConfigConnection;
		Username = ConfigUsername;
		Password = ConfigPassword;
	
        connect=false;

        //Connect to the database.
        c=null;
        dataSource = new MysqlDataSource();
        dataSource.setDatabaseName(DB);
        dataSource.setServerName(Connection);
        dataSource.setPort(ConfigPort);
        try{
            c = dataSource.getConnection(Username,Password);
            connect=true;
        }catch(Exception e){}
    }

    /**
    ArrayList process()<br />
    Returns an ArrayList of strings representing a flattend collection
    of MySql matches. CMD is an SQL query.
    */
    public ArrayList process(String cmd){
            ArrayList returnMe=null;
            if(connect==false)
                return(null);

            Statement stmt=null;
            try{
                stmt = this.c.createStatement();
                ResultSet rs = stmt.executeQuery(cmd);
                while (rs.next()){
                    int i=1;
                    boolean or=false;
                    while(!or){
                        try{
                            String s=rs.getString(i);
                            if(returnMe==null)
                                returnMe=new ArrayList();
                            returnMe.add(s);

                            i++;
                        }catch(Exception ore){
                            or=true;
                        }
                    }
                }
            }catch(Exception ee){
                try{//Maybe it's an update.
                    int i = stmt.executeUpdate(cmd);
                }catch(Exception ue){
                    ue.printStackTrace();
                }
            }

        return(returnMe);
    }
	
    
    /**
    ArrayList processException()<br />
    Returns an ArrayList of strings representing a flattend collection
    of MySql matches. CMD is an SQL query.
    Throws Exceptions instead of stack traces.
    */
    public ArrayList<String> processQuery(String cmd) throws Exception{
        ArrayList<String> returnMe=null;
        if(connect==false)
            return(null);

        Statement stmt=null;

        stmt = this.c.createStatement();
        ResultSet rs = stmt.executeQuery(cmd);
        while (rs.next()){
            int i=1;
            boolean or=false;
            while(!or){
                try{
                    String s=rs.getString(i);
                    if(returnMe==null)
                        returnMe=new ArrayList<String>();
                    returnMe.add(s);

                    i++;
                }catch(Exception ore){
                    or=true;
                }
            }
        }


        return(returnMe);
    }
    
    /* Processes change */
    public boolean processUpdate(String cmd) throws Exception{
        Statement stmt=null;
        try{
            stmt = this.c.createStatement();
            stmt.executeUpdate(cmd);
        }catch(Exception e){
            throw e;
        }
        return true;
    }
    
    public void close(){
            try{
                    c.close();
            }catch(Exception e){
                    e.printStackTrace();
            }
    }
}