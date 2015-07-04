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

The Reporter (client) end implementation of an inbound server.
*/
public class ReporterInServe extends InServe{
	////////////////////////////
	//Constructor.
	public ReporterInServe(Object parent,Socket socket){
		super(parent,socket);
	}

	/** Dispatch a received object to the appropriate location. */
	public void putInObject(Object o){
		Reporter r=(Reporter)this.getParent();

		try{

			if(o instanceof Assignment){//We got an assignment to execute.
				Assignment a=(Assignment)o;
				r.addAssignment(a);
			}else

			if(o instanceof Integer){//We got a kill message.
				Integer io=(Integer)o;
				//It is an ID message.
				if(io.intValue()>-1)
					r.setID(io.intValue());
				else{//It is a kill message.
					r.killAllAssignments();
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/** Perform and special inizialization steps. */
	public void specialInit(){

	}
}