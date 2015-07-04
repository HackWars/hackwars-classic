package Game;

/**
Computer.java

A computer in the Hacker game essentially represents bot a user and their computer, connections between
the server and front-end are handled through this class.
*/

import java.util.*;
import util.*;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import java.util.concurrent.Semaphore;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import View.*;
import Server.*;
import Assignments.*;
import java.io.*;
import java.net.URL;
import java.text.*;
import java.awt.*;
import javax.imageio.*;
import java.awt.image.*;

public class CentralLogging implements Runnable{
	private BufferedWriter Out=null;
	private static CentralLogging Instance=null;
	boolean run=true;
	private final Semaphore available = new Semaphore(1, true);//Make it thread safe.
	private ArrayList Tasks=new ArrayList();//The array of tasks.
	private static final long SLEEP_TIME=50;//How often can we process a remote call?
	private Thread MyThread=null;
	
	//Constructor.
	private CentralLogging(){
		try{
			MyThread=new Thread(this,"CentralLogging");
			MyThread.start();
		}catch(Exception e){
		
		}
	}
	
	/**
	Get an instance of this writer.
	*/
	public static CentralLogging getInstance(){
		if(Instance==null)
			Instance=new CentralLogging();
		return(Instance);
	}

	/**
	Add a string to be outputted to the log file.
	*/
	public void addOutput(String data){
		try{
			available.acquire();
			Tasks.add(data);
			available.release();
		}catch(Exception e){
			available.release();
		}
	}

	/**
    Fetch execution tasks from the stack.
    */
    public synchronized void run(){
        while(run){
					
            long startTime=Time.getInstance().getCurrentTime();
							
			try{
				//LOCK OUR LIST AND POP ONE ENTRY.
				available.acquire();
				Iterator MyIterator=Tasks.iterator();
				Object o=null;
				if(MyIterator.hasNext()){
					o=MyIterator.next();
					MyIterator.remove();
				}
				available.release();
				if(o!=null){
					Out=new BufferedWriter(new FileWriter("transactionlog.txt",true));
					Out.write((String)o);
					Out.close();
				}
				
			}catch(Exception e){
				available.release();
			}
				
			//Sleep to cut down on processor load.
			if(Tasks.size()==0){
				try{
				 long endTime=Time.getInstance().getCurrentTime();
				 if(SLEEP_TIME-(endTime-startTime)>0)
					MyThread.sleep(SLEEP_TIME-(endTime-startTime));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
        }
    }
}
