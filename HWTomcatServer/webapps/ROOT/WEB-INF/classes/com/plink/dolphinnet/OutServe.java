package com.plink.dolphinnet;
import com.plink.dolphinnet.util.*;
import javax.swing.*;
import java.util.*;
import java.util.zip.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

This class provides an abstract basis for sending outbound data on a socket.
You must implement the getGuiObject() which checks for objects to be output and the specialInit()
method which is provided for any implementation specific initialization.
*/
abstract public class OutServe implements Runnable{
	//Data.
	private int id=0;
	private Object Parent=null;

	private InputStream in=null;
	private ObjectOutputStream out=null;
	private Socket SSend=null;
	private int socketTimeOut=100000;

	private int threadSleepTime=50;
	private volatile Thread t=null;

	private long timeOut=300000;//About 3 minutes.
	private long timeStamp=0;
	
	public void clean(){
		try{
			t=null;
			kill();
		}
		catch(Exception e){
		//	e.printStackTrace();
		}
	}

	////////////////////////////
	//Constructor.
	public OutServe(Object Parent,Socket SSend){
		this.Parent=Parent;
		this.SSend=SSend;
		setAlive();
	}

	///////////////////////////
	//Getters.
	public boolean getAlive(){
		Calendar time = Calendar.getInstance();
		 if(time.getTimeInMillis()-timeStamp>timeOut)
		 	return(false);
		 return(true);
	}
	public Object getParent(){
		return(Parent);
	}
	public int getID(){
		return(id);
	}
	public Socket getSocket(){
		return(SSend);
	}
	///////////////////////////
	//Setters.
	public void setID(int id){
		this.id=id;
	}

	public void setThreadSleepTime(int threadSleepTime){
		this.threadSleepTime=threadSleepTime;
	}

	public void setAlive(){
		Calendar time = Calendar.getInstance();
		timeStamp=time.getTimeInMillis();
	}

	public void setTimeOut(int timeOut){
		this.timeOut=timeOut;
	}

	public void setSocketTimeOut(int socketTimeOut){
		this.socketTimeOut=socketTimeOut;
	}

	//////////////////////////
	// Methods.
	public void run(){
		Thread thisThread = Thread.currentThread();
		while(thisThread==t){
			try{
				if(!getAlive())
					kill();

				Object o=getOutObject(id);

				if(o!=null){

					if(this instanceof EditorOutServe)
						in.read();

					out.writeObject(o);
		
					out.flush();
					out.reset();
					setAlive();
				}
		

			}catch(Exception e){
				if(!(e instanceof java.net.SocketTimeoutException)){
				//	this.kill();
				}
			}
			
			try{
				t.sleep(threadSleepTime);
			}catch(Exception e){
				Thread.currentThread().interrupt();	
			}
		}

		//Clean up data.
		if(Parent instanceof Editor){
			Editor p=(Editor)Parent;
			p.removeClient(getID());
		}

	}

	/** Kill this process. **/
	public void kill(){
		if(t!=null){
			Thread moribund = t;
			t = null;
			moribund.interrupt();
		}
		
		try{
			if(out!=null)
				out.close();
			if(SSend!=null)
				SSend.close();
			if(in!=null)
				in.close();
		}catch(Exception e){
		//	e.printStackTrace();
		}
		Parent=null;
		SSend=null;
		in=null;
		out=null;
	}

	/** Start the server running. */
	public void execute(){
		t=new Thread(this,"com/plink/dolphinnet/OutServe");
		t.start();
	}

	/** Get any objects that are currently in queue to be sent on this connection. */
	abstract public Object getOutObject(int id);

	/** Perform any special initialization steps. */
	abstract public void specialInit(Socket SSend);

	/** Initialize the server (must be run before executing). */
	public void init(int socketTimeOut){
		this.socketTimeOut=socketTimeOut;
		try{
			//Set up our object output stream.
			specialInit(SSend);
			SSend.setSoTimeout(socketTimeOut);
			SSend.setTcpNoDelay(true);
			SSend.setKeepAlive(true);
			//this.SSend.setTrafficClass(0x02|0x04|0x08|0x10);
			in=SSend.getInputStream();
			out=new ObjectOutputStream(new BufferedOutputStream(SSend.getOutputStream(),20000));
			out.flush();
		}catch(Exception e){
		//	e.printStackTrace();
		}
	}
}
