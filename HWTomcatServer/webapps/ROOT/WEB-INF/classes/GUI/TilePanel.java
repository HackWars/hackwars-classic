package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class TilePanel extends JPanel implements MouseListener{

	private HacktendoCreator hacktendoCreator=null;
	private TileHandler tileHandler=null;
	private int tileX=0,tileY=0;
	
	public TilePanel(HacktendoCreator hacktendoCreator,TileHandler tileHandler){
		 this.hacktendoCreator=hacktendoCreator;
		 this.tileHandler=tileHandler;
		 addMouseListener(this);
	}
	
	public int getSelectedIndex(){
		return tileX+tileY*8;
	}
	
	public void paintComponent(Graphics g){
		g.setColor(new Color(238,238,238));
		g.fillRect(0,0,getWidth(),getHeight());
		
		Tile[] tiles = hacktendoCreator.getTiles();
		int y=0;
		int x=0;
		g.setColor(new Color(0.0f,0.0f,0.0f,0.30f));
		for(int i=0;i<tiles.length;i++){
			x = (i%8)*32;
			if(tiles[i]!=null){
				
				BufferedImage BI = tiles[i].getImage();
				g.drawImage(BI,x,y*32,32,32,this);
			}
			g.drawString(""+i,x+5,(y*32)+16);
			if((i+1)%8==0)
				y++;
			
		}
		g.setColor(Color.black);
		
		for(int i=0;i<256/32+1;i++)
			g.drawLine(i*32,0,i*32,512-1);
		
		for(int i=0;i<512/32+1;i++)
			g.drawLine(0,i*32,256/32*32,i*32);
		g.setColor(new Color(255,0,0));
		g.drawRect(tileX*32,tileY*32,32,32);
		g.setColor(new Color(0.0f,0.0f,0.0f,0.30f));
		g.fillRect(tileX*32,tileY*32,32,32);	
		
	}
	
	public void mouseEntered(MouseEvent e){

	}

	public void mouseExited(MouseEvent e){

	}

	public void mousePressed(MouseEvent e){

	}

	public void mouseReleased(MouseEvent e){

	}

	public void mouseClicked(MouseEvent e){
		
		int x = e.getX();
		int y= e.getY();
		
		tileX=x/32;
		tileY=y/32;
		repaint();

	}
}
