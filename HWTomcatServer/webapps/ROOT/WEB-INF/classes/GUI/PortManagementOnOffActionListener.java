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

public class PortManagementOnOffActionListener implements ActionListener{
	//data
	int index;
	PortManagement MyPortManagement;
	
	public PortManagementOnOffActionListener(int index,PortManagement MyPortManagement){
		this.index=index;
		this.MyPortManagement=MyPortManagement;
	}
	
	public void actionPerformed(ActionEvent e){
		//System.out.println(index);
		JButton button = MyPortManagement.getButton(index);
		boolean onoff = MyPortManagement.getOn(index);
		if(onoff){
			button.setIcon(ImageLoader.getImageIcon("images/active.png"));
			MyPortManagement.setOnOff(index,false);
		}
		else{
			button.setIcon(ImageLoader.getImageIcon("images/active.png"));
			MyPortManagement.setOnOff(index,true);
		}
	}
	
}
