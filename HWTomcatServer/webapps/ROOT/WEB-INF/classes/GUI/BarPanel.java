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


public class BarPanel extends JPanel{

	private BufferedImage levelImage,toLevel,grid;
	private float toNext=0.0f;
	private int level=50;
	private int maxLevel=100;
	public BarPanel(int level,float toNext,int maxLevel){
		if(maxLevel==100)
			levelImage = ImageLoader.getImage("images/levelbar.png");
		else
			levelImage = ImageLoader.getImage("images/totallevelbar.png");
		grid = ImageLoader.getImage("images/bargrid.png");
		toLevel = ImageLoader.getImage("images/experiencebar.png");
		setPreferredSize(new Dimension(100,25));
		this.level = level;
		this.toNext = toNext;
		this.maxLevel=maxLevel;
	}
	
	public void setLevel(int level){
		this.level=level;
	}
	
	public void setToNext(float toNext){
		this.toNext=toNext;
	}

	public void paintComponent(Graphics g){
		g.setColor(StatsPanel.BACKGROUND);
		g.fillRect(0,0,getWidth(),getHeight());
		int l = (int)((float)level/(float)maxLevel*100.0f);
		for(int i=0;i<l;i++){
			g.drawImage(levelImage,i,0,null);
		}
		if(maxLevel==100){
			int toNext = (int)this.toNext;
			for(int i=0;i<toNext;i++){
				g.drawImage(toLevel,i,10,null);
			}
		}
		//if(maxLevel>100)
			g.drawImage(grid,0,0,null);
	}
	

}
