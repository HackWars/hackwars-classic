package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;

public class PortScanTableCellRenderer extends JCheckBox implements TableCellRenderer{
	
	public PortScan MyPortScan;
	
	public PortScanTableCellRenderer(PortScan MyPortScan){
		this.MyPortScan=MyPortScan;
		this.setOpaque(true);
		setHorizontalAlignment(CENTER);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, 
							int row, int col){
		if(col==3){
			int checked = (Integer)value;
			//table.getColumnModel().getColumn(vColIndex).
			setEnabled(false);
			if(checked==1)
				setSelected(true);
			else
				setSelected(false);
			if(checked==-1)
				return(new JLabel("-"));
			
		}
		if(col==4){
			boolean check = (Boolean)value;
			setEnabled(false);
			setSelected(check);
			//addItemListener(new PortScanItemListener(MyPortScan,row,col));
			
			
		}
		if(col==0){
			JLabel label =new JLabel(String.valueOf((int)(Integer)value));
			label.setFont(new Font("Dialog",Font.PLAIN,12));
			return(label);
		}
		if(col==1){
			JLabel label =new JLabel((String)value);
			label.setFont(new Font("Dialog",Font.PLAIN,12));
			return(label);
		}
		if(col==2){
			JLabel label =new JLabel((String)value);
			label.setFont(new Font("Dialog",Font.PLAIN,12));
			return(label);
		}
		setBackground(table.getBackground());
		return(this);
	}
	
}
