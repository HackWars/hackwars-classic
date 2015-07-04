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

The Editor (server) end implementation of an inbound server.
*/
public class EditorInServe extends InServe{
	////////////////////////////
	//Constructor.
	public EditorInServe(Object parent,Socket socket){
		super(parent,socket);
		super.setThreadSleepTime(50);
	}

	/** Dispatch a received object to the appropriate location. */
	public synchronized void putInObject(Object o){
		Editor e=(Editor)this.getParent();

		if(o instanceof Assignment){
			Assignment a=(Assignment)o;
			e.returnAssignment(a);
		}

		if(o instanceof Integer){
			e.killAll();
		}
	}

	/** Perform and special inizialization steps. */
	public void specialInit(){}
}