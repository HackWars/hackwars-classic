package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;

public class HomeTableCellRenderer extends JLabel implements TableCellRenderer{
	
	private final ImageIcon FOLDER = ImageLoader.getImageIcon("images/folder.png");
	private final ImageIcon COMPILE = ImageLoader.getImageIcon("images/compile.png");
	private final ImageIcon SCRIPT = ImageLoader.getImageIcon("images/script.png");
	private final ImageIcon NEW = ImageLoader.getImageIcon("images/new.png");
	private final ImageIcon FIREWALL = ImageLoader.getImageIcon("images/firewall.png");
	private final ImageIcon CPU = ImageLoader.getImageIcon("images/cpu.png");
	
	private Home MyHome;
	private Object[] directory;
	
	public HomeTableCellRenderer(Home MyHome,Object[] directory){
		this.MyHome=MyHome;
		this.directory=directory;
		this.setOpaque(true);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, 
							int row, int col){
		boolean get=true;
		//System.out.println("ROw: "+row);
		try{
			if(directory!=null){
				if (row < directory.length) {
					SortableTableModel model = (SortableTableModel)table.getModel();
					String modelType = (String)model.getValueAt(row,1);
					//System.out.println(row+":"+modelType);
					if(modelType.equals("Directory")){
						setIcon(FOLDER);
						get=false;
					}
					else if(modelType.equals(Home.TYPES[0])||modelType.equals(Home.TYPES[2])||modelType.equals(Home.TYPES[4])||modelType.equals(Home.TYPES[6])||modelType.equals(Home.TYPES[12])||modelType.equals(Home.TYPES[23])){
						setIcon(COMPILE);
					}
					else if(modelType.equals(Home.TYPES[1])||modelType.equals(Home.TYPES[3])||modelType.equals(Home.TYPES[5])||modelType.equals(Home.TYPES[7])||modelType.equals(Home.TYPES[12])||modelType.equals(Home.TYPES[14])||modelType.equals(Home.TYPES[15])||modelType.equals(Home.TYPES[24])){
						setIcon(SCRIPT);
					}
					else if(modelType.equals(Home.TYPES[9])){
						setIcon(NEW);
					}
					else if(modelType.equals(Home.TYPES[8])){
						setIcon(FIREWALL);
					}
					else if(modelType.equals(Home.TYPES[10])||modelType.equals(Home.TYPES[18])||modelType.equals(Home.TYPES[19])){
						setIcon(CPU);
					}
					else{
						setIcon(null);
					}
					setText((String)value);
					setFont(new Font("Dialog",Font.PLAIN,12));
					if(isSelected){
						//MyHome.getProperties((String)value,get);
						setBackground(table.getSelectionBackground());
					}
					else
						setBackground(table.getBackground());
				}
			}
		}catch(NullPointerException e){	
		}catch(ArrayIndexOutOfBoundsException aeo) {
				System.out.println("DIRECTORY LENGTH: " +directory.length + ", ROW: " + row);
		}
		return(this);
	}
	
}
