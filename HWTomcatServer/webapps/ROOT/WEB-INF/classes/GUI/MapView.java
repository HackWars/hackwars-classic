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

public class MapView extends JPanel implements MouseListener{

	private MapMaker mapMaker=null;
	private int map=0;
	private int screenx=0,screeny=0;
	
	public MapView(MapMaker mapMaker,int map){
		this.mapMaker=mapMaker;
		this.map=map;
		addMouseListener(this);
	}
	
	public void setMap(int map){
		this.map=map;
		repaint();
	}
	
	public void setScreen(int x,int y){
		screenx=x;
		screeny=y;
		repaint();
	}
	
	public void paintComponent(Graphics g){
		g.setColor(new Color(238,238,238));
		g.fillRect(0,0,getWidth(),getHeight());
		int[] mapTiles = mapMaker.getMap(map);
		Tile[] tiles = mapMaker.getTiles();
		if(tiles!=null&&mapTiles!=null){
			//System.out.println("Tiles not null");
			int y=0;
			for(int i=0;i<1080;i++){
				int x = i%45;
				//int index = screenx*9+screeny*72*8+x+y*72;
				
				if(mapTiles[i]!=-1){
					Tile tile = tiles[mapTiles[i]];
					if(tile!=null){
						BufferedImage BI = tile.getImage();
						g.drawImage(BI,x*8,y*8,x*8+8,y*8+8,0,0,32,32,this);
					}
				}
				if((i+1)%45==0)
					y++;
			}
		}
		g.setColor(Color.black);
		g.drawRect(screenx*72,screeny*64,72,64);
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
		int y = e.getY();
		
		int screenx = x/72;
		int screeny = y/64;
		if(screenx>HacktendoCreator.X_SCREENS-1)
			screenx=HacktendoCreator.X_SCREENS-1;
		if(screeny>HacktendoCreator.Y_SCREENS-1)
			screeny=HacktendoCreator.Y_SCREENS-1;
		mapMaker.setScreen(screenx,screeny);
	}

}
