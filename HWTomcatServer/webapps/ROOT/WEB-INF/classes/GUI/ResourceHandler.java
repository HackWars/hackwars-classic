package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ResourceHandler extends JInternalFrame implements InternalFrameListener{

	private HacktendoCreator hacktendoCreator=null;
	private SpriteHandler spriteHandler=null;
	private TileHandler tileHandler=null;
	//private MusicHandler musicHandler=null;
	private ScriptHandler scriptHandler=null;
	
	public ResourceHandler(HacktendoCreator hacktendoCreator){
		setDoubleBuffered(true);
		this.hacktendoCreator=hacktendoCreator;
		setTitle("Resource Handler");
		setIconifiable(true);
		setBounds(10,10,500,400);
		setLayout(null);
		populate();
		addInternalFrameListener(this);
		setVisible(true);
	}
	
	public void populate(){
		JTabbedPane tb = new JTabbedPane();
		add(tb);
		tb.setBounds(5,5,475,350);
		
		spriteHandler = new SpriteHandler(hacktendoCreator,tb);
		tb.addTab("Sprites",spriteHandler);
		
		tileHandler = new TileHandler(hacktendoCreator,tb);
		
		tb.addTab("Tiles",tileHandler);
		
		tb.addTab("Music",null);
		tb.setEnabledAt(tb.indexOfTab("Music"),false);
		
		scriptHandler = new ScriptHandler(hacktendoCreator,tb);
		
		tb.addTab("Scripts",scriptHandler);
	}
	
	public void repopulateScripts(){
		scriptHandler.populate();
	}
	
	
	
	
	public void internalFrameIconified(InternalFrameEvent e) {
		JDesktopIcon DI = getDesktopIcon();
		DI.setBackground(new Color(41,42,41));
		
		JButton b = (JButton)DI.getUI().getAccessibleChild(DI,0);
		JLabel l = (JLabel)DI.getUI().getAccessibleChild(DI,1);
		//b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		b.setForeground(Color.WHITE);
		
		//b.setBorderPainted(false);
		
		DI.getUI().update(DI.getGraphics(),DI);
		//getDesktopIcon().setLocation(162,182);
		//getDesktopIcon().moveToFront();
	}
	
	public void internalFrameClosing(InternalFrameEvent e) {
	}

	public void internalFrameClosed(InternalFrameEvent e) {
	}

	public void internalFrameOpened(InternalFrameEvent e) {
	}

	public void internalFrameDeiconified(InternalFrameEvent e) {

	}

	public void internalFrameActivated(InternalFrameEvent e) {

	}

	public void internalFrameDeactivated(InternalFrameEvent e) {

	}
}
