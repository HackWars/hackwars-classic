package GUI;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Hacktendo.*;
import java.util.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import Game.MMO.*;

public class HacktendoMMO extends Application implements FocusListener,MouseListener{

	private Hacker hacker=null;
	private String[] games = new String[5];
	private MMOViewport viewport=null;
	private BufferedImage image = null;
	private boolean loaded=false;
	
	public HacktendoMMO(Hacker hacker){
	
		this.hacker=hacker;
		hacker.getPanel().add(this);
		this.setBounds(getParent().getWidth()/2-320,getParent().getHeight()/2-240,640,480);
	
		setTitle("Hacktendo 3D Chat");
		setMaximizable(false);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		setVisible(true);
		setFocusable(true);
		//addMouseListener(viewport);

		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		addInternalFrameListener(this);
		viewport = new MMOViewport(Color.black,hacker.getPanel().getGraphicsConfiguration(),hacker,(JInternalFrame)this);
		addFocusListener(this);
		getParent().setComponentZOrder(this,0);
		
		loaded=true;
		this.populate();
	}
	
	public void addPacket(HacktendoPacket Packet){
		viewport.addPacket(Packet);
	}
	
	public void setDebug(boolean debug){
		if(viewport!=null)
			viewport.setDebug(debug);
	}
	
	public void populate(){
	
	}
	
	public void openGame(String game){	
		String xml="";
		try{
			BufferedReader BR=new BufferedReader(new FileReader(game));
			String s;
			while((s=BR.readLine())!=null)
				xml+=s;
		}catch(Exception e){
			e.printStackTrace();
		}
	
		while(!loaded){
			try{
				Thread.currentThread().sleep(1);
			}catch(Exception e){}
		}
		
		viewport.loadGame(xml);
	}
		
	public void internalFrameIconified(InternalFrameEvent e) {
		JDesktopIcon DI = getDesktopIcon();
		DI.setBackground(new Color(41,42,41));
		
		JButton b = (JButton)DI.getUI().getAccessibleChild(DI,0);
		JLabel l = (JLabel)DI.getUI().getAccessibleChild(DI,1);
		b.setContentAreaFilled(false);
		b.setForeground(Color.WHITE);
		
		DI.getUI().update(DI.getGraphics(),DI);
		DI.setLocation(2,142);
	    
	}
	
	public void internalFrameClosing(InternalFrameEvent e) {
		setVisible(false);
		//viewport.stop();
	}
	public void internalFrameClosed(InternalFrameEvent e) {
	}
	
	public void internalFrameActivated(InternalFrameEvent e){
	System.out.println("Internal Frame Activated");
		if(viewport!=null){
			viewport.gainedFocus();
		}
	}
	
	public void internalFrameDeactivated(InternalFrameEvent e){
	System.out.println("Internal Frame deactivated");
		if(viewport!=null){
			viewport.lostFocus();
		}
	}
	
	public void focusGained(FocusEvent e){
	/*System.out.println("Focus Gained");
		if(viewport!=null){
			viewport.gainedFocus();
		}*/
	
	}
	
	public void focusLost(FocusEvent e){
	/*System.out.println("Focus Lost");
		if(viewport!=null){
			viewport.lostFocus();
		}*/
	}
	
}
