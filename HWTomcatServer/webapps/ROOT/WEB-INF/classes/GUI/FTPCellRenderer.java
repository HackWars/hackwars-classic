package GUI;
/**
FTPCellRenderer.java

this is the cell renderer for the FTP programs list.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import Game.*;

public class FTPCellRenderer extends JLabel implements ListCellRenderer{
	
	private Object[] directory;
	private int add=0;
	
	public FTPCellRenderer(Object[] directory){
		this.directory=directory;
		setOpaque(true);
	}

	public Component getListCellRendererComponent(
                                       JList list,
                                       Object value,
                                       int index,
                                       boolean isSelected,
                                       boolean cellHasFocus) {

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());  
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
	ImageIcon icon=null;
	//System.out.println(">>"+list.getModel().getElementAt(index));
	String pet="";
	if(directory!=null){
		//if(!(((String)(list.getModel().getElementAt(index))).equals(".."))){
			if(index<directory.length){
				if(directory[index] instanceof String){
					icon = ImageLoader.getImageIcon("images/folder.png");
					//pet=(String)list.getModel().getElementAt(index);
				}
				else if (directory[index] instanceof Object[]){
					Object o[] = (Object[])directory[index];
					int type=(int)(Integer)o[1];
					if(type==0||type==2||type==4||type==6)
						icon = ImageLoader.getImageIcon("images/compile.png");
					else if(type==1||type==3||type==5||type==7)
						icon = ImageLoader.getImageIcon("images/script.png");
					else if(type==9)
						icon = ImageLoader.getImageIcon("images/new.png");
					else if(type==8)
						icon = ImageLoader.getImageIcon("images/firewall.png");
					
					//System.out.println(">>>>>"+pet);
				}
				pet = (String)list.getModel().getElementAt(index-add);
			}
		/*}
		else{
			add=1;
			icon = ImageLoader.getImageIcon("images/folder.png");
			pet="..";
			System.out.println("test");
		}*/
			
	}
	setIcon(icon);
        setText(pet);
        setFont(list.getFont());
        return this;
    }
    
    public void setDirectory(Object[] directory){
	    this.directory=directory;
    }

}
