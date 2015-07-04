package GUI;
/**
PortManagementOnOffActionListener.java

this is the action listener for the port management window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;

public class WatchOnOffActionListener implements ActionListener{
	//data
	private int index;
	private WatchManager MyWatchManager;
	private JButton button;
	
	public WatchOnOffActionListener(int index,WatchManager MyWatchManager,JButton button){
		this.index=index;
		this.MyWatchManager=MyWatchManager;
		this.button=button;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	public void actionPerformed(ActionEvent e){
		//System.out.println(index);
		/*JButton button = MyPortManagement.getButton(index);
		boolean onoff = MyPortManagement.getOn(index);
		if(onoff){
			button.setIcon(ImageLoader.getImageIcon("images/off.png"));
			MyPortManagement.setOnOff(index,false);
		}
		else{
			button.setIcon(ImageLoader.getImageIcon("images/on.png"));
			MyPortManagement.setOnOff(index,true);
		}*/
		button.setIcon(ImageLoader.getImageIcon("images/active.png"));
		MyWatchManager.setOnOff(index);
	}
	
}
