package GUI;
/**
ScriptEditorCellRenderer.java

this is the cell renderer for the script editor file open/save dialog.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import Game.*;

public class HacktendoCellRenderer extends JLabel implements ListCellRenderer{
	
	private Object[] directory;
	private HacktendoFileChooser hacktendoFileChooser; 
	public HacktendoCellRenderer(Object[] directory,HacktendoFileChooser hacktendoFileChooser){
		this.directory=directory;
		this.hacktendoFileChooser=hacktendoFileChooser;
		setOpaque(true);
	}

	public Component getListCellRendererComponent(
                                       JList list,
                                       Object value,
                                       int index,
                                       boolean isSelected,
                                       boolean cellHasFocus) {

        
	ImageIcon icon=null;
	String pet="";
	if(directory!=null){
		try{
			
			if(directory[index] instanceof String){
				icon = ImageLoader.getImageIcon("images/folder.png");
				pet=(String)directory[index];
			}
			else{
				Object dir[] = (Object[])directory[index];
				int type=(Integer)dir[1];
				if(type==0||type==2||type==4||type==6||type==12)
					icon = ImageLoader.getImageIcon("images/compile.png");
				else if(type==1||type==3||type==5||type==7||type==15||type==16)
					icon = ImageLoader.getImageIcon("images/script.png");
				else if(type==9)
					icon = ImageLoader.getImageIcon("images/new.png");
				else if(type==8)
					icon = ImageLoader.getImageIcon("images/firewall.png");
				pet = (String)dir[0];
								
			}
		}catch(IndexOutOfBoundsException e){
		}
		catch(NullPointerException e){
			//e.printStackTrace();
		}
		if (isSelected) {
		    if(hacktendoFileChooser!=null)	
			    hacktendoFileChooser.setSaveField(pet);
		    setBackground(list.getSelectionBackground());
		    setForeground(list.getSelectionForeground());  
		} else {
		    setBackground(list.getBackground());
		    setForeground(list.getForeground());
		}
	}
	setIcon(icon);
        setText(pet);
        setFont(list.getFont());
        return this;
    }
    
    public void setDirectory(Object[] directory){
	    //System.out.println("directory received in renderer");
	    this.directory=directory;
    }

}
