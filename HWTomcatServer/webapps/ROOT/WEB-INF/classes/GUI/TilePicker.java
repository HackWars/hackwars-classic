package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class TilePicker extends JPanel implements MouseListener{
	private MapMaker mapMaker=null;
	private int mx=0,my=0,chosen=0;
	public TilePicker(MapMaker mapMaker){
		this.mapMaker=mapMaker;
		addMouseListener(this);
	}
	
	public int getChosen(){
		return chosen;
	}
	

	public void paintComponent(Graphics g){
		g.setColor(new Color(238,238,238));
		g.fillRect(0,0,getWidth(),getHeight());
		g.setColor(Color.black);
		//g.drawLine(0,0,getWidth(),0);
		
		/*g.drawLine(32,0,32,320);
		g.drawLine(64,0,64,320);
		g.drawLine(96,0,96,320);
		g.drawLine(32,0,,320);*/
		Tile[] tiles = mapMaker.getTiles();
		int y=0;
		for(int i=0;i<tiles.length;i++){
			int x = (i%4)*32;
			if(tiles[i]!=null){
				//int x = tiles[i].getTileX()*32;
				//int y = tiles[i].getTileY()*32;
				//System.out.println(i+": "+x+"  "+y);
				BufferedImage BI = tiles[i].getImage();
				g.drawImage(BI,x,y*32,32,32,this);
				
			}
			if((i+1)%4==0)
					y++;
		}
		for(int i=0;i<getWidth()/32+1;i++)
			g.drawLine(i*32,0,i*32,getHeight()-1);
		
		for(int i=0;i<getHeight()/32+1;i++)
			g.drawLine(0,i*32,getWidth()/32*32,i*32);
		g.setColor(new Color(255,0,0));
		g.drawRect(mx*32,my*32,32,32);
		g.setColor(new Color(0.0f,0.0f,0.0f,0.30f));
		g.fillRect(mx*32,my*32,32,32);	
		g.dispose();
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
		//System.out.println("Click");
		int x = e.getX();
		int y = e.getY();
		
		mx=x/32;
		my=y/32;
		chosen=mx+my*(getWidth()/32);
		repaint();
		//System.out.println("Tile X: "+mx+"\nTile Y: "+my);

	}
}
