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


public class LevelPanel extends JPanel{

	private int level;
	
	public LevelPanel(int level){
		this.level=level;
		setPreferredSize(new Dimension(25,25));
	}
	
	public void setLevel(int level){
		this.level=level;
	}

	public void paintComponent(Graphics g){
		g.setColor(StatsPanel.STAT_BACKGROUND);
		g.fillRect(0,0,getWidth(),getHeight());
		//g.drawImage(icon,0,0,null);
		g.setColor(StatsPanel.TEXT_COLOR);
		g.setFont(StatsPanel.TEXT);
		int x=1;
		//level=maxLevel;
		if(level<100&&level>=10)
			x=5;
		else if(level<10)
			x=8;
			
		g.drawString(""+level,x,18);
		
	}
	

}
