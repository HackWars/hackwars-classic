package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.text.*;
import java.math.*;
import java.net.*;
import java.io.*;
import java.awt.image.*;


public class IconPanel extends JPanel{

	private BufferedImage icon;
	
	public IconPanel(BufferedImage icon){
		this.icon=icon;
		setPreferredSize(new Dimension(25,25));
	}

	public void paintComponent(Graphics g){
		g.setColor(StatsPanel.STAT_BACKGROUND);
		g.fillRect(0,0,getWidth(),getHeight());
		g.drawImage(icon,0,0,null);
		
	}
	

}
