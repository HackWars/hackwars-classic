package chat.util;

import java.util.concurrent.Semaphore;
import java.io.*;

public class ErrorLog{
	public static final String FILE_NAME = "ChatErrLog.txt";
	public static final Semaphore semi = new Semaphore(1, true);

	public static void addMessage( String msgToLog ){
		try{
			semi.acquire();
                	FileOutputStream outStream = new FileOutputStream(FILE_NAME,true);
                	PrintStream outP = new PrintStream( outStream );
			outP.println ( msgToLog );

			outP.close();
			outStream.close();
		}catch(Exception e){
			e.printStackTrace( System.out );			
		}finally{
			semi.release();
		}		
	}	
		

	public static void addMessage( Exception exc ){
		try{
			semi.acquire();
                	FileOutputStream outStream = new FileOutputStream(FILE_NAME,true);
                	PrintStream outP = new PrintStream( outStream );
			//p.println ( msgToLog );
			exc.printStackTrace( outP );

			outStream.close();
		}catch( Exception e){
			e.printStackTrace( System.out);			
		}finally{
			semi.release();
		}		
	}
}
