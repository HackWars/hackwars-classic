package com.plink.dolphinnet;
import com.plink.dolphinnet.util.*;
import java.util.*;
/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

Used to keep track of how many Assignments have been assigned to
Reporters (clients) attached to the server.
*/
public class ClientData{
	public static int MAX_SIZE=100;
	private int id;
	private int jobCount;
	private boolean kill=false;
	private boolean ready=true;
	private ArrayList Jobs=new ArrayList();
	private AssignmentBinaryList outAssignments=new AssignmentBinaryList();

	/////////////////
	//Constructor.
	public ClientData(int id,int jobCount){
		this.id=id;
		this.jobCount=jobCount;
	}

	////////////////
	//Getters.
	public int getID(){
		return(id);
	}
	public int getJobCount(){
		return(jobCount);
	}

	public boolean getKill(){
		return(kill);
	}

	public boolean getReady(){
		return(ready);
	}

	public void setReady(boolean ready){
		this.ready=true;
	}

	////////////////
	//Setters.

	/** Keeps track of the jobs that this client is currently processing.*/
	public void addOutAssignment(Assignment A){
	//	outAssignments.add(A);
	}

	/** Assignment has been returned.*/
	public void removeOutAssignment(Assignment A){
	//	outAssignments.remove(new Integer(A.getID()));
	}

	public void setJobCount(int jobCount){
		this.jobCount=jobCount;
	}

	public void setKill(boolean kill){
		this.kill=kill;
	}

	/** Along with distributed assignments a client can be given specific tasks.*/
	public synchronized void addJob(Object A){
		if(Jobs.size()>=MAX_SIZE){
			Jobs.remove(MAX_SIZE-1);
		}
		Jobs.add(0,A);
	}

	/** Along with distributed assignments a client can be given specific tasks.*/
	public synchronized Object getJob(){
		if(Jobs.size()>0)
			return(Jobs.remove(0));
		return(null);
	}

	/////////////////
	//Methods.
	/**Upon failing the client should return all un-finished jobs.*/
	public void fail(IParty IP){
	//	for(int i=0;i<outAssignments.getData().size();i++)
		//	IP.failedAssignment((Assignment)outAssignments.get(i));
	}

	public String toString(){
		String returnMe="";
		return(returnMe);
	}
}