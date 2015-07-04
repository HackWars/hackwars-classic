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

The Editor class serves as a server for distributing parallel tasks to the connected Reporters (Clients).
To add assignments to the Editor you must extend on the IParty class which receives results and
on the Assignment class which represents concrete calculations.
*/

public class Editor implements Runnable{
	//Data.
	private ArrayList Assignments;
	private ClientBinaryList Clients=null;
	private int maxAssignments=1024;
	private int clientJobSize=2;
	private IParty IP=null;
	private Thread t=null;
	//Sockets Data.
	private ServerSocket SReceive=null;
	private ServerSocket SSend=null;
	private int receiveSocket=1011;
	private int sendSocket=1010;
	private int socketTimeOut=1000;
	private int outSocketTimeOut=150000;
	private int inSocketTimeOut=150000;
	private int timeOut=180000;
	private int acount=0;
	private int lastConnect=0;

	//////////////////
	//Constuctors.
	public Editor(){
		//Create the queue that assignments will be posted to.
		Assignments=new ArrayList();
		Clients=new ClientBinaryList();
		try{
			//Create sockets for sending and receiving.
			SSend=new ServerSocket(sendSocket);
			SReceive=new ServerSocket(receiveSocket);
			SSend.setSoTimeout(socketTimeOut);
			SReceive.setSoTimeout(socketTimeOut);
		}catch(Exception e){
			e.printStackTrace();
		}
		//Initialize the server thread.
		t=new Thread(this,"com/plink/dolphinnet/Editor");
		t.start();
	}

	public Editor(int maxAssignments,int socketTimeOut,int receiveSocket,int sendSocket){
		//Create the array that assignments will be posted to.
		Assignments=new ArrayList();
		Clients=new ClientBinaryList();

		//Set instance variables based on constructor.
		this.socketTimeOut=socketTimeOut;
		this.maxAssignments=maxAssignments;
		this.receiveSocket=receiveSocket;
		this.sendSocket=sendSocket;

		//Create the server end sockets.
		try{
			//Create sockets for sending and receiving.
			SSend=new ServerSocket(sendSocket);
			SReceive=new ServerSocket(receiveSocket);
			SSend.setSoTimeout(socketTimeOut);
			SReceive.setSoTimeout(socketTimeOut);
		}catch(Exception e){
			e.printStackTrace();
		}
		//Initialize the server thread.
		t=new Thread(this,"com/plink/dolphinnet/Editor");
		t.start();
	}

	//////////////////////////////////
	// Setters.

	/** Set the timeout length for connections to server.*/
	public void setTimeOut(int timeOut){
		this.timeOut=timeOut;
	}

	/** Add the current IParty that is to receive results from the Editor.*/
	public void setIParty(IParty IP){
		this.IP=IP;
	}

	public void setMaxAssignments(int maxAssignments){
		this.maxAssignments=maxAssignments;
	}

	public void setClientJobSize(int clientJobSize){
		this.clientJobSize=clientJobSize;
	}

	/** Add a new Assignment to the assignments list. */
	public synchronized void addAssignment(Assignment A) throws Exception{
		if(IP==null)
			throw(new Exception("No IParty has yet been attached."));
		if(Assignments.size()>=maxAssignments)
			throw(new Exception("Assignment list is full."));
		Assignments.add((Object)A);

		//Logging.
		//System.out.println("Assignment has been added to assignment queue.");
	}
	
	/** Send an assignment to a specific client.*/
	public synchronized void addAssignment(int ClientID,Assignment A){
		ClientBinaryList MyClientBinaryList=getClients();
		ClientData MyClientData=(ClientData)MyClientBinaryList.get(new Integer(ClientID));
		if(MyClientData!=null){
			MyClientData.addJob(A);
		}
	}

	/** Return an assignment to the current IParty. */
	public synchronized void returnAssignment(Assignment A){
		int id=A.getReporterID();
		IP.returnAssignment(A);
		ClientData Client=(ClientData)Clients.get((Object)new Integer(id));
		//if(Client.getJobCount()<clientJobSize)
			Client.setJobCount(Client.getJobCount()+1);

		//Logging.
		//System.out.println("Assignment Returned.");
		Client.removeOutAssignment(A);
	}

	/** Fetch an Assignment for a Reporter (client).*/
	public synchronized Object getAssignment(int id){
		ClientData Client=(ClientData)Clients.get((Object)new Integer(id));

		//Kill all running assignments.
		if(Client.getKill()){
			Client.setKill(false);
			return(new Integer(-1));
		}

		//Client has no job space.
		//if(Client.getJobCount()<=0)
		//	return(null);

		//Get assignment from local assignment list.
		Object temp=Client.getJob();
		if(temp!=null){
			if(temp instanceof Assignment){
				//Client.setJobCount(Client.getJobCount()-1);
				Client.addOutAssignment((Assignment)temp);
			}
			//System.out.println("Dispatching Assignment.");
			return(temp);
		}

		if(Assignments.size()<=0)
			return(null);

		Client.setJobCount(Client.getJobCount()-1);

		//Logging.
		//System.out.println("Dispatching Assignment");

		temp=Assignments.remove(0);
		Client.addOutAssignment((Assignment)temp);
		return((Assignment)temp);
	}

	/** Registers a client with the distributed server.*/
	public synchronized void addClient(int id){
		Clients.add((Object)new ClientData(id,clientJobSize));
		System.out.println("Client "+id+" Attached");
		acount++;
	}

	/** Get the current id that should be used for the client.*/
	public synchronized int getID(){
		return(acount);
	}

	/** Remove a client from the server.*/
	public synchronized void removeClient(int id){
		ClientData Client=(ClientData)Clients.get((Object)new Integer(id));
		Client.fail(IP);
		Clients.remove((Object)new Integer(id));
	}

	/** Kill all the Assignments that are currently running.*/
	public synchronized void killAll(){
		ArrayList Data=Clients.getData();
		for(int i=0;i<Data.size();i++){
			ClientData temp=(ClientData)Data.get(i);
			temp.setKill(true);
		}
		int size=Assignments.size();
		for(int i=0;i<size;i++)
			Assignments.remove(0);
	}

	/** Get the list of current clients.*/
	public synchronized ClientBinaryList getClients(){
		return(Clients);
	}

	/** The run method listens for a connection on the Send and Receive ports. */
	private int gCount=0;
	public void run(){
		while(true){
			//Check for a client to pass data to.
			try{
				//Garbabe collect every 100 iterations (inSocketTimeOut * 1000).
				if(gCount==1000){
					System.out.println("Garbage collecting.");
					System.runFinalization();
					System.gc();
					gCount=0;
				}else
					gCount++;

				Socket temp=SSend.accept();
				EditorOutServe out=new EditorOutServe(this,temp);
				out.init(outSocketTimeOut);
				out.setTimeOut(timeOut);
				out.execute();
				System.out.println("Creating Send Port.");
			}catch(Exception e){
			}

			//Check for a client to receive data from.
			try{
				Socket temp=SReceive.accept();
				EditorInServe in=new EditorInServe(this,temp);
				in.init(inSocketTimeOut);
				in.setTimeOut(timeOut);
				in.execute();
				System.out.println("Creating Receive Port.");
			}catch(Exception e){
			}
		}
	}
}
