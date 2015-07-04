package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

public class BigMapView extends JInternalFrame implements InternalFrameListener{
	private int map=0;
	private MapMaker mapMaker=null;
	private MapView mapView=null;
	
	public BigMapView(MapMaker mapMaker,int map){
		this.mapMaker=mapMaker;
		this.map=map;
		setTitle("Map View");
		setIconifiable(true);
		setBounds(100,100,380,240);
		setLayout(null);
		mapView = new MapView(mapMaker,map);
		add(mapView);
		mapView.setBounds(0,0,getWidth(),getHeight());
		setVisible(true);
		addInternalFrameListener(this);
	}
	
	public void setMap(int map){
		this.map=map;
		mapView.setMap(map);
	}
	
	public void setScreen(int x, int y){
		mapView.setScreen(x,y);
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
		repaint();

	}

	public void internalFrameActivated(InternalFrameEvent e) {
		repaint();

	}

	public void internalFrameDeactivated(InternalFrameEvent e) {

	}

}
