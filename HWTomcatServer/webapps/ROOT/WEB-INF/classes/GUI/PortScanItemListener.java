package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;

public class PortScanItemListener implements ItemListener{
	
	private int row,col;
	private PortScan MyPortScan;
	
	public PortScanItemListener(PortScan MyPortScan, int row, int col){
		this.MyPortScan=MyPortScan;
		this.row=row;
		this.col=col;
	}
	
	public void itemStateChanged(ItemEvent e) {
		//System.out.println("Click");
	}
}
