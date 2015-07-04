package GUI;
/**
PortManagementActionListener.java

this is the action listener for the port management window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;

public class WatchActionListener implements ActionListener{
	//data
	private int watchID;
	private WatchManager MyWatchManager;
	private JTextField tf;
	public WatchActionListener(int watchID,WatchManager MyWatchManager,JTextField tf){
		this.watchID=watchID;
		this.MyWatchManager=MyWatchManager;
		this.tf=tf;
	}
	
	public void actionPerformed(ActionEvent e){
		MyWatchManager.saveNote(tf.getText(),watchID);
		//System.out.println(tf.getText());
	}
	
}
