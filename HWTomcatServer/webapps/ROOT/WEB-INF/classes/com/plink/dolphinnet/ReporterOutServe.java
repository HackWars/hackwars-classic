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

The Reporter (client) end implementation of an outbound server.
*/
public class ReporterOutServe extends OutServe{
	int i=0;
	///////////////////////
	// Constructor.
	ReporterOutServe(Object parent,Socket socket){
		super(parent,socket);
	}

	/** Get any objects that are currently in queue to be output on this connection. */
	public synchronized Object getOutObject(int id){
		Reporter parent=(Reporter)getParent();
		Object o=parent.getAssignment();
		return(o);
	}

	/** Perform and special initialization steps. */
	public void specialInit(Socket socket){
	}
}