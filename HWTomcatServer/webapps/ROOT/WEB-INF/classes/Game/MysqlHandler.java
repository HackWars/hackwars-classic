package Game;


import java.util.*;
import java.util.concurrent.Semaphore;

public class MysqlHandler{
	
	private static ArrayList work = new ArrayList(); 
	private static final Semaphore available = new Semaphore(1, true);
	private static MysqlHandler MyInstance = new MysqlHandler();
	
	
	public MysqlHandler(){
		for(int i=0;i<10;i++){
			CheckOutHandler c = new CheckOutHandler();
		}
	}
	
	public static Object[] getWork(){
		try{
			available.acquire();
			
			if(work.size()==0){
				available.release();
				return(null);
			}
			Object O[]=(Object[])work.remove(0);
			available.release();
			return (O);

		}catch(Exception e){
			available.release();
		}
		return(null);
	}
	
	public static void addWork(Object[] work1){
		try{
			available.acquire();
			boolean found=false;
			for(int i=0;i<work.size();i++){
				String ip1=(String)work1[0];
				String ip2=(String)((Object[])work.get(i))[0];
				if(ip2.equals(ip1)){
					work.set(i,work1);
					found=true;
				}
			}
			
			if(!found){
				CheckOutHandler.SAVE_COUNTER++;
				work.add(work1);
			}
			available.release();
		}catch(Exception e){
			available.release();
		}
	}
	
	
}
