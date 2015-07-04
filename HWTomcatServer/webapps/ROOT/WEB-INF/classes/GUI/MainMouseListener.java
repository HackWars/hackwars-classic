package GUI;
/**

MainMouseListener.java

mouse listener for main panel.

*/

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.geom.Point2D;

public class MainMouseListener extends MouseAdapter{
	private JPopupMenu popUp;
	private boolean icons;
	private HashMap iconList;
	private JMenuItem menuItem;
	private PopUpActionListener aL;
	private JDesktopPane panel;
	private Hacker MyHacker;
	
	public MainMouseListener(JPopupMenu popUp,boolean icons,HashMap iconList,Hacker MyHacker,JDesktopPane panel){
		this.popUp=popUp;
		this.icons=icons;
		this.iconList=iconList;
		this.panel=panel;
		this.MyHacker=MyHacker;
		aL=new PopUpActionListener(panel,MyHacker);
	}
	
	public void mousePressed(MouseEvent e) {
		int x = e.getX(),y=e.getY();
		Component com = e.getComponent();
		if(com instanceof JPanel){
			x=x+com.getBounds().x;
			y=y+com.getBounds().y;
		}
		if(com instanceof JButton){
			Container con = com.getParent();
			x=x+con.getBounds().x+com.getBounds().x;
			y=y+con.getBounds().y+com.getBounds().y;
		}
		
		if(e.isPopupTrigger()){
			boolean found=false;
			HashMap temp = new HashMap(iconList);
			Collection C = temp.values();
			Iterator MyIterator = C.iterator();
			DesktopIcon dI=new DesktopIcon("","",0,0,"",MyHacker.getColour(),MyHacker,2,panel);
			Point p;
			Dimension d;
			while (MyIterator.hasNext()&&!found){
				dI=(DesktopIcon)MyIterator.next();
				p=dI.getPoint();
				d =dI.getPreferredSize();
				if(x>p.getX()&&x<(p.getX()+d.width)){
					if(y>p.getY()&&y<(p.getY()+d.height)){
						found=true;
					}
				}
				
				MyIterator.remove();
			}
			if(!found)
				dI=new DesktopIcon("","",0,0,"",MyHacker.getColour(),MyHacker,2,panel);
		    popUp=new JPopupMenu();
		    menuItem = new JMenuItem("Add Icon");
		    menuItem.addActionListener(aL);
		    popUp.add(menuItem);
		    if(dI.getType()==DesktopIcon.USER){
			    if(icons){
				    popUp.addSeparator();
				    
				    menuItem = new JMenuItem("Copy");
				    menuItem.addActionListener(aL);
				    popUp.add(menuItem);
				    
				    menuItem = new JMenuItem("Cut");
				    menuItem.addActionListener(aL);
				    popUp.add(menuItem);
				    
				    menuItem = new JMenuItem("Paste");
				    menuItem.addActionListener(aL);
				    popUp.add(menuItem);
				    
				    menuItem = new JMenuItem("Delete");
				    menuItem.addActionListener(aL);
				    popUp.add(menuItem);
			    }
		    }
		    if(dI.getType()==DesktopIcon.SYSTEM){
			popUp.addSeparator();
			if(dI.getName().equals("Trash")){
				menuItem = new JMenuItem("Open Trash");
				menuItem.addActionListener(aL);
				popUp.add(menuItem);
				
				menuItem = new JMenuItem("Empty Trash");
				menuItem.addActionListener(aL);
				popUp.add(menuItem);
			}
		    }
		    popUp.show(e.getComponent(),e.getX(),e.getY());
		    }
	}

	public void mouseReleased(MouseEvent e) {
		if(e.isPopupTrigger()){
		    popUp=new JPopupMenu();
		    menuItem = new JMenuItem("Add Icon");
		    menuItem.addActionListener(aL);
		    popUp.add(menuItem);
		    
		    if(icons){
			    popUp.addSeparator();
			    
			    menuItem = new JMenuItem("Copy");
			    menuItem.addActionListener(aL);
			    popUp.add(menuItem);
			    
			    menuItem = new JMenuItem("Cut");
			    menuItem.addActionListener(aL);
			    popUp.add(menuItem);
			    
			    menuItem = new JMenuItem("Paste");
			    menuItem.addActionListener(aL);
			    popUp.add(menuItem);
			    
			    menuItem = new JMenuItem("Delete");
			    menuItem.addActionListener(aL);
			    popUp.add(menuItem);
		    }
		    popUp.show(e.getComponent(),e.getX(),e.getY());
		}
	}
    
	public void mouseExited(MouseEvent e) {
	
	}
    
	public void mouseEntered(MouseEvent e) {
	
	}
    
	public void mouseClicked(MouseEvent e) {
		if(e.isPopupTrigger()){
			popUp=new JPopupMenu();
			menuItem = new JMenuItem("Add Icon");
			menuItem.addActionListener(aL);
			popUp.add(menuItem);
			if(icons){
				popUp.addSeparator();
			    
				menuItem = new JMenuItem("Copy");
				menuItem.addActionListener(aL);
				popUp.add(menuItem);
			    
				menuItem = new JMenuItem("Cut");
				menuItem.addActionListener(aL);
				popUp.add(menuItem);
			    
				menuItem = new JMenuItem("Paste");
				menuItem.addActionListener(aL);
				popUp.add(menuItem);
			    
				menuItem = new JMenuItem("Delete");
				menuItem.addActionListener(aL);
				popUp.add(menuItem);
			}
			popUp.show(e.getComponent(),e.getX(),e.getY());
	    }
	}
}
