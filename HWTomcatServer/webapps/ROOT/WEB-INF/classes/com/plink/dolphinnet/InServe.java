package com.plink.dolphinnet;
import com.plink.dolphinnet.util.*;
import javax.swing.*;
import java.util.*;
import java.util.zip.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import com.plink.dolphinnet.assignments.*;

/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

This class provides an abstract basis for receiving data on a socket.
You must implement putGuiObject() which delivers received object to the appropriate
location and the specialInit() method which is provided for any implementation specific initialization.]
*/

abstract public class InServe implements Runnable{
	//Data.
	private int id=0;
	private Object Parent=null;

	private volatile Thread t=null;
	private int threadSleepTime=50;

	private int socketTimeOut=200;
	private Socket SReceive=null;
	private ObjectInputStream in=null;
	private OutputStream out=null;

	private long timeOut=300000;//About 3 minutes.
	private long timeStamp=0;
	
	public void clean(){
		try{
			t=null;
			kill();
		}catch(Exception e){
		//	e.printStackTrace();
		}
	}
	
	////////////////////////////
	//Constructor.
	public InServe(Object Parent,Socket SReceive){
		this.Parent=Parent;
		this.SReceive=SReceive;
		this.timeOut=timeOut;
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
		return(SReceive);
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
		//this.socketTimeOut=socketTimeOut;
	}

	///////////////////////////
	// Methods.
	public void run(){		
	
		Thread thisThread = Thread.currentThread();
		while(thisThread==t){
			try{
				if(!getAlive())
					kill();

				if(this instanceof ReporterInServe)
					out.write(10);

				Object o=in.readObject();

				if(o!=null){
					if(o instanceof ZippedAssignment){
						putInObject(((ZippedAssignment)o).getAssignment());
					}else
						putInObject(o);
					setAlive();
				}


			}catch(Exception e){
			//	e.printStackTrace();
				if(!(e instanceof java.net.SocketTimeoutException)){
			//		this.kill();
				}
			}
			
			try{
				t.sleep(threadSleepTime);
			}catch(Exception wake){
				Thread.currentThread().interrupt();	
			}
		}

		//Clean up data.
		Parent=null;
		try{
			if(in!=null)
				in.close();
			if(SReceive!=null)
				SReceive.close();
		}catch(Exception e){
		//	e.printStackTrace();
		}
		in=null;
		SReceive=null;
		t=null;
	}

	/** Start the server. */
	public void execute(){
		t=new Thread(this,"com/plink/dolphinnet/InServe");
		t.start();
	}

	/** Kill this process. */
	public void kill(){
		if(t!=null){
			Thread moribund = t;
			t = null;
			moribund.interrupt();
		}
		
		try{
			if(out!=null)
				out.close();
			if(in!=null)
				in.close();
			if(SReceive!=null)
				SReceive.close();
		}catch(Exception e){
		//	e.printStackTrace();
		}

		id=0;
		Parent=null;
		SReceive=null;
		in=null;
		out=null;
	}

	/** Dispatch a received object to the appropriate location. */
	abstract public void putInObject(Object o);

	/** Perform and special initialization steps. */
	abstract public void specialInit();

	/** Initialize the socket.*/
	public void init(int socketTimeOut){
		this.socketTimeOut=socketTimeOut;
		try{
			//Setup our object input stream.
			specialInit();
			this.SReceive.setSoTimeout(this.socketTimeOut);
			this.SReceive.setTcpNoDelay(true);
			this.SReceive.setKeepAlive(true);
			//this.SReceive.setTrafficClass(0x02|0x04|0x08|0x10);
			out=SReceive.getOutputStream();
			in=new ObjectInputStream(new BufferedInputStream(SReceive.getInputStream(),20000));
			//in.reset();
		}catch(Exception e){
		//	e.printStackTrace();
		}
	}
}
