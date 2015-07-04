package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;

public class ForumTableCellRenderer extends JLabel implements TableCellRenderer{
	
	private ForumPanel MyForumPanel;
	private Hacker MyHacker;
	//private Object[] directory;
	
	public ForumTableCellRenderer(ForumPanel MyForumPanel,Hacker MyHacker){
		this.MyForumPanel=MyForumPanel;
		this.MyHacker=MyHacker;
		//this.directory=directory;
		this.setOpaque(true);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, 
							int row, int col){
		if(col==1)
			setHorizontalAlignment(RIGHT);
		else if(col==2)
			setHorizontalAlignment(CENTER);
		else
			setHorizontalAlignment(LEFT);
		if(col==0){
			setText("<html><u><font color=\"#0000FF\">"+(String)value+"</font></u></html>");
			setToolTipText("View Thread");
		}
		else if(col==2){
			setText("<html><u><font color=\"#0000FF\">"+(String)value+"</font></u></html>");
			setToolTipText("View Profile");
		}
		else
			setText((String)value);
		if(row%2==0)
				setBackground(Color.white);
			else
				setBackground(MyHacker.getColour());
				
			
			//setForeground(Color.black);
		Font f = new Font("Dialog",Font.PLAIN,12);
		if(col==3)
			f=new Font("Dialog",Font.PLAIN,10);
		setFont(f);
		return(this);
	}
	
}
