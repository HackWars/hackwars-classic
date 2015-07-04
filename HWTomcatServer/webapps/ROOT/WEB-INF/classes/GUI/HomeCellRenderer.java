package GUI;
/**
HomeCellRenderer.java

this is the cell renderer for home.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import Game.*;

public class HomeCellRenderer extends JLabel implements ListCellRenderer{
	
	private final ImageIcon FOLDER = ImageLoader.getImageIcon("images/folder.png");
	private final ImageIcon COMPILE = ImageLoader.getImageIcon("images/compile.png");
	private final ImageIcon SCRIPT = ImageLoader.getImageIcon("images/script.png");
	private final ImageIcon NEW = ImageLoader.getImageIcon("images/new.png");
	private final ImageIcon FIREWALL = ImageLoader.getImageIcon("images/firewall.png");
	private Object[] directory;
	private Home MyHome; 
	public HomeCellRenderer(Object[] directory,Home MyHome){
		this.directory=directory;
		this.MyHome=MyHome;
		setOpaque(true);
	}

	public Component getListCellRendererComponent(
                                       JList list,
                                       Object value,
                                       int index,
                                       boolean isSelected,
                                       boolean cellHasFocus) {

        
	ImageIcon icon=null;
	String pet;
	boolean prop=false; 
	if(directory[index] instanceof String){
		icon = FOLDER;
		pet=(String)directory[index];
	}
	else{
		prop=true;
		Object dir[] = (Object[])directory[index];
		int type=(Integer)dir[1];
		if(type==0||type==2||type==4||type==6||type==12)
			icon = COMPILE;
		else if(type==1||type==3||type==5||type==7||type==15||type==16)
			icon = SCRIPT;
		else if(type==9)
			icon = NEW;
		else if(type==8)
			icon = FIREWALL;
		pet = (String)dir[0];
		
	}
	
	if (isSelected) {
	    if(MyHome!=null&&prop)	
		    MyHome.getProperties(pet,true);
	    if(MyHome!=null&&!prop)
		    MyHome.getProperties(pet,false);
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());  
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
	setIcon(icon);
        setText(pet);
	setFont(new Font("Dialog",Font.PLAIN,16));
        return this;
    }
    
    public void setDirectory(Object[] directory){
	    this.directory=directory;
    }

}
