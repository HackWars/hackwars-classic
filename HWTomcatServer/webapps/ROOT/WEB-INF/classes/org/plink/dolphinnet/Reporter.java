package com.plink.dolphinnet;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import com.plink.dolphinnet.assignments.*;

/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

The Reporter acts as the client and connects to a central server (Editor). It simply receives Assignments
and throws them into a runnable thread. It continually checks the threads for completion and
returns finished Assignments (instance variables having been filled out) to the Editor (server).
<br /><br />
The Future: Some method should be implemented for distributing new implementations of Assignments
to the Reporter. As of right now the Assignment class must be present on the client and server
side.
*/
public class Reporter implements Runnable{

	///////////////////////////////////
	//Data.
	private int id=-1;

	private DataHandler DH;
	private ArrayList Processes=null;
	private ArrayList FinishedAssignments=null;
	private volatile Thread t=null;
	private ReporterInServe in=null;
	private ReporterOutServe out=null;
	private int socketTimeOut=10000;
	private int timeOut=180000;
	private boolean killAllAssignments=false;
	
	public void clean(){
		DH=null;
		in.clean();
		out.clean();
		t=null;
	}
	
	public int getFinishedCount(){
		System.out.println("Processes: "+Processes.size());
		return(FinishedAssignments.size());
	}

	//Our theading inner class.
	private class RunAssignment implements Runnable{
		private volatile Thread t;
		private Assignment A;
		private boolean finished=false;
		/////////////
		//Constructor.
		RunAssignment(Assignment A){
			this.A=A;
			t=new Thread(this);
			t.start();
		}
		/////////////
		// Getters.
		public Assignment getAssignment(){
			return(A);
		}

		public boolean isFinished(){
			return(finished);
		}

		/////////////
		//Methods.
		public void kill(){
			finished=true;
			t = null;
			A.kill();
		}

		public void run(){
			Thread thisThread = Thread.currentThread();
			while(thisThread==t){
				Object T=null;
				if(!finished)
					T=(Object)A.execute(DH);
				if(DH!=null)
					DH.addData(T);
				A.setReporterID(getID());

				kill();

				try{
					Thread.sleep(100);
				}catch(Exception e){

				}
			}
		}
	}

	//////////////////////////////
	// Getters.
	public int getID(){
		return(id);
	}
	///////////////////////////////
	// Setters.
	public void setSocketTimeOut(int socketTimeOut){
		this.socketTimeOut=socketTimeOut;
	}

	public void setID(int id){
		this.id=id;
	}

	public void setDataHandler(DataHandler DH){
		this.DH=DH;
	}

	public void kill(){
		t = null;
		in.kill();
		out.kill();
		DH=null;
		Processes=null;
		FinishedAssignments=null;
	}

	/** Kill all the assignments that are currently running.*/
	public synchronized void killAllAssignments(){
		killAllAssignments=true;
	}
	//////////////////////////////.
	// Constructor.
	public Reporter(String address,int socketTimeOut,int inPort,int outPort){
		this.id=-1;
		this.socketTimeOut=socketTimeOut;
	
		////////////
		Processes=new ArrayList();
		FinishedAssignments=new ArrayList();

		//Set up the server.
		try{
			//Create the inboud socket.
			Socket s=new Socket(address,inPort,true);
			in=new ReporterInServe(this,s);
			in.setID(id);
			in.setTimeOut(timeOut);
			in.init(150000);
			in.execute();

			//Create the outbound socket.
			Socket s2=new Socket(address,outPort,true);
			out=new ReporterOutServe(this,s2);
			out.setID(id);
			out.setTimeOut(timeOut);
			out.init(150000);
			out.execute();
		}catch(Exception e){
		//	e.printStackTrace();
		}

		//Set up the execution thread.
		t=new Thread(this);
		t.start();
	}

	//////////////////////////////
	// Methods.
	public void run(){
		Thread thisThread = Thread.currentThread();
		while(thisThread==t){
			if(killAllAssignments){
				for(int i=0;i<Processes.size();i++){
					RunAssignment temp=(RunAssignment)Processes.get(i);
					temp.kill();
					Assignment A=temp.getAssignment();
					//FinishedAssignments.add(A);
					Processes.remove(i);
					break;
				}
				killAllAssignments=false;
			}
			for(int i=0;i<Processes.size();i++){
				RunAssignment temp=(RunAssignment)Processes.get(i);
				if(temp!=null)
				if(temp.isFinished()){
					temp.kill();
					Assignment A=temp.getAssignment();
					//FinishedAssignments.add(new ZippedAssignment(0,A));
					Processes.remove(i);
					break;
				}
			}
			try{
				Thread.sleep(100);
			}catch(Exception e){

			}
		}
		DH=null;
	}

	/** Set the timeout for connections to server.*/
	public void setTimeOut(int timeOut){
		this.timeOut=timeOut;
		in.setTimeOut(timeOut);
		out.setTimeOut(timeOut);
	}

	/** Adds an assignment to the assignment list for execution.*/
	public synchronized void addAssignment(Assignment A){
		try{
			A.setReporterID(getID());
			RunAssignment R=new RunAssignment(A);
			Processes.add(R);
		}catch(Exception e){
		//	e.printStackTrace();
		}
	}

	/** Add an external finished assignment to the list.*/
	public synchronized void addFinishedAssignment(Assignment A){
		A.setReporterID(getID());
		FinishedAssignments.add(A);
	}

	/** Get an assignment from the completed assignment list.*/
	public synchronized Object getAssignment(){
		if(FinishedAssignments.size()<=0)
			return(null);
		return(FinishedAssignments.remove(0));
	}
}