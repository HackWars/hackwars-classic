package GUI;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.JInternalFrame.JDesktopIcon;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.lang.*;
import Game.*;
import java.text.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.util.*;
import net.miginfocom.swing.*;

public class TaskBar extends JPanel implements ActionListener{
	private final static int ICON_WIDTH = 122;
	private JPanel iconPanel;
	private JScrollPane viewport;
	private int iconCount = 0,viewX = 0;
	private JButton left,right;
	public TaskBar(JButton left,JButton right){
		this.left = left;
		this.right = right;
		setLayout(new MigLayout("fill,aligny top,ins 0,novisualpadding,gap 0"));
		iconPanel = new JPanel();
		iconPanel.setLayout(new FlowLayout(FlowLayout.LEADING,2,0));
		//iconPanel.setInsets(new Insets(0,0,0,0));
		viewport = new JScrollPane(iconPanel);
		viewport.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		viewport.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		viewport.setBorder(null);
		viewport.setViewportBorder(null);
		//viewport.setInsets(new Insets(0,0,0,0));
		iconPanel.setPreferredSize(new Dimension(10000,30));
		//viewport.setPreferredSize(new Dimension(getPreferredSize().width,25));
		add(viewport,"grow,pos 0 0 container.x2 container.y2");
		
		
	
	}
	
	public void addIcon(JDesktopIcon icon){
		iconPanel.add(icon);
		iconCount++;
		SwingUtilities.invokeLater(new Runnable()
		{
		public void run()
		{
		viewport.getHorizontalScrollBar().setValue(iconCount*ICON_WIDTH-getWidth());
		}
		}); 
		viewX = (iconCount*ICON_WIDTH-getWidth());
		enableButtons();
	}
	
	public void removeIcon(JDesktopIcon icon){
		iconPanel.remove(icon);
		iconCount --;
		enableButtons();
		
	}
	
	public void enableButtons(){
		if(viewX>0){
			left.setEnabled(true);
		}
		else{
			left.setEnabled(false);
		}
		
		if(viewX<iconCount*ICON_WIDTH-getWidth()){
			right.setEnabled(true);
		}
		else{
			right.setEnabled(false);
		}
	
	}
	
	public void actionPerformed(ActionEvent e){
		String ac = e.getActionCommand();
		Rectangle visible = getVisibleRect();
		final Point viewPoint = viewport.getViewport().getViewPosition();
		if(ac.equals("Left")){
			if(viewPoint.x>0){
				//System.out.println("Scrolling Left + "+(viewPoint.x-ICON_WIDTH));
				//viewport.scrollRectToVisible(new Rectangle(visible.x-160,0,visible.width,visible.height));
				SwingUtilities.invokeLater(new Runnable()
				{
				public void run()
				{
				viewport.getHorizontalScrollBar().setValue(viewPoint.x-ICON_WIDTH);
				}
				}); 
				//viewport.setViewPosition(new Point(visible.x-160,0));
				viewX -=ICON_WIDTH;
				enableButtons();
				iconPanel.validate();
				iconPanel.repaint();
				viewport.validate();
				viewport.repaint();
				validate();
				repaint();
			}
		}
		else if(ac.equals("Right")){
			//System.out.println("Scrolling Right + "+(viewPoint.x+ICON_WIDTH));
			//viewport.scrollRectToVisible(new Rectangle(visible.x+160,0,visible.width,visible.height));
			//viewport.setViewPosition(new Point(visible.x+160,0));
			//System.out.println((viewX+getWidth())+"   "+(iconCount*ICON_WIDTH));
			if(iconCount>getWidth()/ICON_WIDTH&&(viewX+getWidth()<(iconCount*ICON_WIDTH))){
				SwingUtilities.invokeLater(new Runnable()
				{
				public void run()
				{
				viewport.getHorizontalScrollBar().setValue(viewPoint.x+ICON_WIDTH);
				}
				}); 
				viewX +=ICON_WIDTH;
				enableButtons();
				iconPanel.validate();
				iconPanel.repaint();
				viewport.validate();
				viewport.repaint();
				validate();
				repaint();
			}
		}
	}
	

}