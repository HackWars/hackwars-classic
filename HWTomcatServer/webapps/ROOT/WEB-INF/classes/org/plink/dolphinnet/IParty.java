package com.plink.dolphinnet;
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

An IParty provides an abstract basis for managing a series of parallel calculations. You must implement
the returnAssignment() method to receive responses from the Editor (server). An IParty
attaches an ID to an Assignment and sends it to the Editor (server) for distribution. Upon receiving
a completed Assignment the IParty should use the ID and the instance variables of that class
to assemble a complete result.<br /><br />

Only one IParty can be associated with the Editor (server) at any given time. For this reason the creation
of some sort of harness makes sense for controling the flow of multiple IParty calculation using
one central server.
*/
abstract public class IParty{
	//Data
	private Editor editor=null;

	/////////////////////////////
	// Constructors.
	public IParty(){
		this.editor=null;
	}

	/** An editor represents the server for distributing Assignments. And is required. */
	public IParty(Editor editor){
		this.editor=editor;
		editor.setIParty(this);
	}
	/////////////////////////////
	// Getters.
	public Editor getEditor(){
		return(editor);
	}
	/////////////////////////////
	// Setters.

	/** An editor represents the server for distributing Assignments. And is required. */
	public void setEditor(Editor editor){
		this.editor=editor;
		editor.setIParty(this);
	}
	/////////////////////////////
	// Methods.

	/** Add an assignment to the Editor for processing.*/
	public void addAssignment(Assignment a) throws Exception{
		try{
			editor.addAssignment(a);
		}catch(Exception e){
			throw(e);
		}
	}
	

	/** Add an assignment to a specific client for processing.*/
	public void addAssignment(int ClientID,Assignment a) throws Exception{
		try{
			editor.addAssignment(ClientID,a);
		}catch(Exception e){
			throw(e);
		}
	}

	/** Receive a completed assignment.*/
	abstract public void returnAssignment(Assignment a);

	/** Receive a failed assignment.*/
	abstract public void failedAssignment(Assignment a);
}