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
		run=true;
		MyThread=new Thread(this,"Time");
		MyThread.start();
	}
	
	boolean run=true;
	public void clean(){
		run=false;
		MyTime=null;
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
		while(run){
			Calendar time = Calendar.getInstance();
			currentTime=time.getTimeInMillis();
			try{
				MyThread.sleep(1);
			}catch(Exception e){
			}
		}
		MyTime=null;
	}
	
	//Testing main.
	public static void main(String args[]){
		Time MyTime=new Time();
		while(true){
			System.out.println(MyTime.getCurrentTime());
		}
	}
}
