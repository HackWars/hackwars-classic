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

public class HacktendoPlayer extends Application implements FocusListener,MouseListener{

	private Hacker hacker=null;
	private String[] games = new String[5];
	private OpenGLViewport viewport=null;
	private BufferedImage image = null;
	private boolean loaded=false;
	
	public HacktendoPlayer(Hacker hacker){
	
		this.hacker=hacker;
		hacker.getPanel().add(this);
		this.setBounds(getParent().getWidth()/2-320,getParent().getHeight()/2-240,640,480);
	
		setTitle("Hacktendo Game Player");
		setMaximizable(false);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		setVisible(true);
		setFocusable(true);
		//addMouseListener(viewport);

		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		addInternalFrameListener(this);
		viewport = new OpenGLViewport(Color.black,hacker.getPanel().getGraphicsConfiguration(),hacker,(JInternalFrame)this);
		addFocusListener(this);
		getParent().setComponentZOrder(this,0);
		
		loaded=true;
		this.populate();
	}
	
	public void setDebug(boolean debug){
		if(viewport!=null)
			viewport.setDebug(debug);
	}
	
	public void populate(){
	
	}
	
	public void openGame(String game){	
	
		while(!loaded){
			try{
				Thread.currentThread().sleep(1);
			}catch(Exception e){}
		}
		
		viewport.loadGame(game);
	}
	
	public void openGame(String game,String fileName,HashMap LoadFile){
		setVisible(true);
		while(!loaded){
			try{
				Thread.currentThread().sleep(1);
			}catch(Exception e){}
		}
		viewport.setFileName(fileName);
		viewport.setLoadFile(LoadFile);
		viewport.loadGame(game);
	}
		

	public void internalFrameClosing(InternalFrameEvent e) {
		setVisible(false);
		viewport.stop();
	}
	public void internalFrameClosed(InternalFrameEvent e) {
	}
	
	public void internalFrameActivated(InternalFrameEvent e){
	//System.out.println("Internal Frame Activated");
		if(viewport!=null){
			viewport.gainedFocus();
		}
	}
	
	public void internalFrameDeactivated(InternalFrameEvent e){
	//System.out.println("Internal Frame deactivated");
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
