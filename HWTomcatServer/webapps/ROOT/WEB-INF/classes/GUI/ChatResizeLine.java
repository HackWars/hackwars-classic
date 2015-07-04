package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import chat.client.*;
public class ChatResizeLine extends JSeparator implements MouseListener,MouseMotionListener{

	private int x,y;
	private viewMain MyViewMain;
	public ChatResizeLine(viewMain MyViewMain){
		this.MyViewMain=MyViewMain;
		addMouseMotionListener(this);
		addMouseListener(this);
		setToolTipText("Drag to resize chat");
	}
	
	
	public void setPoint(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	public void mouseClicked(MouseEvent e){
	}
	
	public void mouseEntered(MouseEvent e){
	}
	
	public void mouseExited(MouseEvent e){
	}
	
	public void mousePressed(MouseEvent e){
		setPoint(e.getX(),e.getY());
		//System.out.println("Clicked Y: "+e.getY());
	}
	public void mouseReleased(MouseEvent e){
	}

	public void mouseDragged(MouseEvent e){
		int mx = e.getX();
		int my = e.getY();
		//System.out.println("Dragged Y: "+my);
		if(Math.abs(my-y)>5){
			//System.out.println("Resize");
			setBounds(0,getBounds().y+(my-y),getBounds().width,5);
			MyViewMain.redoSize(new Dimension(MyViewMain.getBounds().width,MyViewMain.getBounds().height-(my-y)));
			MyViewMain.setBounds(0,MyViewMain.getBounds().y+(my-y),MyViewMain.getBounds().width,MyViewMain.getBounds().height);
			MyViewMain.validate();
		}
	}
	
	public void mouseMoved(MouseEvent e){
		
	}

}
