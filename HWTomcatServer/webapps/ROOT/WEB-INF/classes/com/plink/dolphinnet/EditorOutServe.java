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

The Editor (server) end implementation of an outbound server.
*/
public class EditorOutServe extends OutServe{
	///////////////////////
	// Constructor.
	EditorOutServe(Object parent,Socket socket){
		super(parent,socket);
	}

	/** Get any objects that are currently in queue to be output on this connection. */
	public synchronized Object getOutObject(int id){
		Editor parent=(Editor)getParent();
		Object o=parent.getAssignment(getID());

		return(o);
	}

	/** Perform and special initialization steps. */
	public void specialInit(Socket socket){
			//Get and distribute client IDs
			Editor parent=(Editor)getParent();
			setID(parent.getID());
			parent.addClient(getID());
			ClientBinaryList b=parent.getClients();
			ClientData c=(ClientData)b.get(new Integer(getID()));
			c.addJob(new Integer(getID()));
	}
}