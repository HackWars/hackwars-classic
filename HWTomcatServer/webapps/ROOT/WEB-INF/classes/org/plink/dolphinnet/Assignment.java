package com.plink.dolphinnet;
import java.util.*;
import java.io.*;
/**
<b>DolphinNet<br />
Benjamin E. Coe (2006)</b><br /><br />

An abstract assignment. An assignment is distributed to a Reporter (client). Simply extend on this
class and implement the execute() method. Once execution has finished call finish(). The ID should
be filled in by the IParty managing a group of assignments. The reporterID should be filled in
one an assignment is received by a client for use by the server upon its return. The results of
a calculation should be stored in member variables present in an inheriting class.
*/

public abstract class Assignment implements Serializable{
	//Data.
	private int id;
	private int reporterID;
	private boolean finished;
	private String hash="";

	///////////////////////////////////
	// Constructor.
	public Assignment(int id){
		this.id=id;
		this.finished=false;
		this.hash=HashSingleton.getHash();
	}

	///////////////////////////////////
	// Getters.
	/** Check whether the assignment has finished its task. */
	public boolean isFinished(){
		return(finished);
	}
	
	public String getHash(){
		return(hash);
	}
	
	/** Get the ID given to this assignment by the IParty. */
	public int getID(){
		return(id);

	}
	/** Get the ID of the reporter who completed the assignment. */
	public int getReporterID(){
		return(reporterID);
	}
	//////////////////////////////////
	// Setters.
	public void setReporterID(int id){
		this.reporterID=id;
	}
	public void setID(int id){
		this.id=id;
	}
	//End an assignment early.
	public void kill(){
		this.finished=true;
	}
	/////////////////////////////////
	// Methods.
	/** Set the state of this assignment to finished. */
	protected void finish(){
		finished=true;
	}

	/** Run the assignments implemented task. */
	public abstract Object execute(DataHandler DH);
}
