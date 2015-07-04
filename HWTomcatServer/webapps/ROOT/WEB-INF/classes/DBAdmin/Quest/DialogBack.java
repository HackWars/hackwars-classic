package DBAdmin.Quest;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.imageio.*;
import java.util.*;
import javax.swing.text.*;


public class DialogBack extends JDesktopPane implements ActionListener,MouseListener{

	private ArrayList dialogs = new ArrayList();
	private Dialog dialog;
	private int mx=0,my=0;
	private JButton create;
	private JComboBox comboBox;
	
	public DialogBack(Dialog dialog){
		this.dialog=dialog;
		setLayout(null);
		setBackground(Color.white);
		addMouseListener(this);
		populate();
		
	}
	
	public void populate(){
		create = new JButton("New Dialog");
		add(create);
		create.setBounds(50,750,create.getPreferredSize().width,create.getPreferredSize().height);
		create.addActionListener(this);
		comboBox = new JComboBox(getTitles());
		add(comboBox);
		comboBox.addActionListener(this);
		
	}
	
	public void setCurrentDialog(int index){
		//removeAll();
		//System.out.println("Setting Current Dialog to index: "+index);
		JInternalFrame[] frames = getAllFrames();
		for(int i=0;i<frames.length;i++){
			DialogWidget d = (DialogWidget)frames[i];
			//System.out.println("Removing Frame: "+d.getTitle());
			remove(frames[i]);
			repaint();
		}
		
		if(index!=-1){
			DialogWidget widget = (DialogWidget)dialogs.get(index);
			widget.setBounds(10,10,600,600);
			add(widget);
			
			int[] links = widget.getLinks();
			int x=650;
			int y=10;
			for(int i=0;i<links.length;i++){
				if(links[i]!=-1){
					DialogWidget w = ((DialogWidget)dialogs.get(links[i])).clone(links[i]);
					//System.out.println("Adding For "+widget.getTitle()+": "+w.getTitle());
					add(w);
					w.setBounds(x,y+(50*i),200,50);
				}
			}
		}
		comboBox.setSelectedIndex(index+1);
	}
	
	public void setComboBox(){
		int index = comboBox.getSelectedIndex();
		comboBox.removeAllItems();
		String[] titles = getTitles();
		for(int i=0;i<titles.length;i++){
			comboBox.addItem(titles[i]);
		}
		comboBox.setSelectedIndex(index);
		
	}
	
	public int getCurrentIndex(){
		return(comboBox.getSelectedIndex()-1);
	}
	
	public int addDialog(DialogWidget dialogWidget){
		dialogs.add(dialogWidget);
		//add(dialogWidget);
		comboBox.addItem(dialogWidget.getTitle());
		dialog.repaint();
		return (dialogs.size()-1);
	}
	
	public String[] getTitles(){
		Object[] dialogArray = dialogs.toArray();
		
		String[] titles = new String[dialogArray.length+1];
		titles[0] = "None";
		for(int i=0;i<dialogArray.length;i++){
			DialogWidget widget = (DialogWidget)dialogArray[i];
			titles[i+1] = widget.getTitle();
		}
		return titles;
		
	}
	
	public String getTitle(int index){
		if(index==-1)
			return "None";
		DialogWidget widget = (DialogWidget)dialogs.get(index);
		return widget.getTitle();
	}


	public void paintComponent(Graphics g){
		if(create!=null){
			create.setBounds(10,getHeight()-create.getPreferredSize().height-5,create.getPreferredSize().width,create.getPreferredSize().height);
			comboBox.setBounds(20+create.getPreferredSize().width,getHeight()-create.getPreferredSize().height-5,comboBox.getPreferredSize().width,comboBox.getPreferredSize().height);
		}
		/*g.setColor(Color.white);
		g.fillRect(0,0,getWidth(),getHeight());
		Object[] d = dialogs.toArray();
		for(int i=0;i<d.length;i++){
			DialogWidget dW = (DialogWidget)d[i];
			int[] link = dW.getLink();
			for(int j=0;j<link.length;j++){
				if(link[j]!=-1){
					int startX = dW.getX()+dW.getWidth();
					int startY = dW.getY()+dW.getHeight()-(3-j)*dW.getHeight()/3+30;
					DialogWidget dwTo = (DialogWidget)dialogs.get(link[j]);
					if(dwTo!=null){
						int endX = dwTo.getX();
						int endY = dwTo.getY();
						g.setColor(Color.black);
						g.drawLine(startX,startY,endX,endY);
						g.drawLine(endX,endY,endX,endY);
						//endX+=15;
						float slope = ((float)(endX-startX)/(float)(endY-startY));
						float angle = (float)Math.toDegrees(Math.atan(slope));
						//System.out.println(link[j]+" Angle: "+ angle);
						float cAngle = 90.0f-angle;
						int move = (int)(10.0*Math.cos(cAngle));
						int move2 = (int)(10.0*Math.cos(angle));
						//System.out.println("Move 1: "+move);
						//System.out.println("Move 2: "+move2);
					//	System.out.println(link[j]+"  "+angle);
						//System.out.println(move);
						//g.fillPolygon(new int[]{endX-move,endX,endX-move2},new int[]{endY-move2,endY,endY+move},3);
						//g.drawLine(endX-5,endY-5,endX+5,endY);
						//g.drawLine(endX-5,endY+5,endX+5,endY);
					}
					else{
						System.out.println(j+" was null");
					}
					
				}
			}
			
		}
		
		for(int i=0;i<d.length;i++){
			DialogWidget dW = (DialogWidget)d[i];
			int[] links = dW.getBack();
			for(int j=0;j<links.length;j++){
				int link = links[j];
				if(link!=-1){
					int startX = dW.getX();
					int startY = dW.getY()+dW.getHeight()/2;
					DialogWidget dwTo = (DialogWidget)dialogs.get(link);
					if(dwTo!=null){
						int endX = dwTo.getX()+dwTo.getWidth();
						int endY = dwTo.getY()+dwTo.getHeight();
						g.setColor(Color.red);
						g.drawLine(startX,startY,endX,endY);
						float slope = ((float)(endX-startX)/(float)(endY-startY));
						float angle = (float)Math.atan(slope);
						int move = (int)(6.0*Math.cos(angle));
						//g.fillPolygon(new int[]{endX+move,endX,endX+move},new int[]{endY-move,endY,endY+move},3);
						//g.drawLine(endX+5,endY-5,endX-5,endY);
						//g.drawLine(endX+5,endY+5,endX-5,endY);
					}
					
				}
			}
			
		}*/
	}
	
	
	public void mouseClicked(MouseEvent e){
		if(e.isPopupTrigger()){
			showPopup(e);
		}
	}
	
	public void mouseEntered(MouseEvent e){
		
	}
	
	public void mousePressed(MouseEvent e){
		if(e.isPopupTrigger()){
			showPopup(e);
		}
	}
	
	public void mouseReleased(MouseEvent e){
		if(e.isPopupTrigger()){
			showPopup(e);
		}
	}
	
	public void mouseExited(MouseEvent e){
		
	}
	
	private void showPopup(MouseEvent e){
		JPopupMenu menu = new JPopupMenu();
		JMenuItem menuItem;
		
		menuItem = new JMenuItem("New Panel");
		menu.add(menuItem);
		menuItem.addActionListener(this);
		
		mx=e.getX();
		my=e.getY();
		menu.show(this,e.getX(),e.getY());
		
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("New Dialog")){
			dialog.newPanel();
		}
		
		if(e.getSource() instanceof JComboBox){
			int index = comboBox.getSelectedIndex();
			if(index!=-1){
				setCurrentDialog(index-1);
			}
		}
	}

}
