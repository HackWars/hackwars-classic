package util;

/**
Time.java<br />
(c) Vulgate 2007<br /><br />

Used for centralized time keeping.
*/

import java.util.*;

public class Time implements Runnable{
    //Data.
    private long currentTime;
	private Thread MyThread;
	private static Time MyTime=null;
	
	/**
	Constructor.
	*/
	public Time(){
		MyThread=new Thread(this);
		MyThread.start();
	}
	
	/**
	Singleton approach.
	*/
	public static Time getInstance(){
		if(MyTime==null){
			MyTime=new Time();
		}

		return(MyTime);
	}
	
	/**
	long getCurrentTime()<br />
	Return the current time.
	*/
	public long getCurrentTime(){
		return(currentTime);
	}
	
	/**
	run()<br />
	Run this thread, contantly updating time.
	*/
	public void run(){
		while(true){
			Calendar time = Calendar.getInstance();
			currentTime=time.getTimeInMillis();
			try{
				MyThread.sleep(10);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	//Testing main.
	public static void main(String args[]){
		Time MyTime=new Time();
		while(true){
			System.out.println(MyTime.getCurrentTime());
		}
	}
}