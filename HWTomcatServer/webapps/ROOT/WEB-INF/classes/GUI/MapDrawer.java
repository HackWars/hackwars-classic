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

public class MapDrawer extends JPanel implements MouseListener,MouseMotionListener{
	private MapMaker mapMaker=null;
	private int map=0;
	private boolean grid=true;
	private int screenx=0,screeny=0;
	private BufferedImage X=null;
			
	public MapDrawer(MapMaker mapMaker){
		this.mapMaker=mapMaker;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void setMap(int map){
		this.map=map;
		repaint();
	}
	
	public int getMap(){
		return map;
	}
	
	public void setGrid(boolean grid){
		this.grid=grid;
		repaint();
	}
	
	public void setScreen(int x,int y){
		screenx=x;
		screeny=y;
		repaint();
	}

	public void paintComponent(Graphics g){
	
		//Load the transparent X image.
		if(X==null){
			try{
				File fi = ImageLoader.getFile("images/dummyoff.png");
				X = ImageIO.read(fi.toURL());
			}catch(Exception e){}
		}
		
		
		g.setColor(new Color(238,238,238));
		g.fillRect(0,0,getWidth(),getHeight());
		int[] mapTiles = mapMaker.getMap(map);
		Tile[] tiles = mapMaker.getTiles();
		int y=0;
		if(tiles!=null&&mapTiles!=null){
			for(int i=0;i<110;i++){
				int x = i%10;
				int index = screenx*9+screeny*45*8+x+y*45-46;
				if(index>=0){
					try{
						if(mapTiles[index]!=-1){
							Tile tile = tiles[mapTiles[index]];
							if(tile!=null){
								BufferedImage BI = tile.getImage();
								g.drawImage(BI,x*32,y*32,32,32,this);
								if(mapMaker.getPassable(index,map)){
									g.drawImage(X,x*32,y*32,32,32,this);
								}
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){
						//System.out.println("Out of Bounds: "+index);
					}
				}
				if((i+1)%10==0)
					y++;
			}
		}
		g.setColor(Color.black);
		g.drawRect(0,0,getWidth()-1,getHeight()-1);
		if(grid){
			for(int i=0;i<getWidth()/32+1;i++)
				g.drawLine(i*32,0,i*32,getHeight()-1);
			
			for(int i=0;i<getHeight()/32+1;i++)
				g.drawLine(0,i*32,getWidth()-1,i*32);
		}
		g.setColor(new Color(0.0f,0.0f,0.0f,0.30f));
		for(int i=0;i<getHeight()/32;i++){
			g.fillRect(0,i*32,32,32);
			if(i>0&&i<getHeight()/32-1&&screenx>0)
				g.fillPolygon(new int[]{21,11,21},new int[]{i*32+11,i*32+16,i*32+21},3);
		}
		for(int i=0;i<getHeight()/32;i++){
			g.fillRect(getWidth()/32*32-32,i*32,32,32);
			if(i>0&&i<getHeight()/32-1&&screenx<HacktendoCreator.X_SCREENS-1)
				g.fillPolygon(new int[]{getWidth()-21,getWidth()-11,getWidth()-21},new int[]{i*32+11,i*32+16,i*32+21},3);
		}
		
		for(int i=1;i<getWidth()/32-1;i++){
			g.fillRect(i*32,0,32,32);
			if(screeny>0)
				g.fillPolygon(new int[]{i*32+11,i*32+16,i*32+21},new int[]{21,11,21},3);
		}
		for(int i=1;i<getWidth()/32-1;i++){
			g.fillRect(i*32,getHeight()/32*32-32,32,32);
			if(screeny<HacktendoCreator.Y_SCREENS-1)
				g.fillPolygon(new int[]{i*32+11,i*32+16,i*32+21},new int[]{getHeight()-21,getHeight()-11,getHeight()-21},3);
		}
		
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
		
		int mx=x/32;
		int my=y/32;
		int index = screenx*9+screeny*45*8+mx+my*45-46;
		//System.out.println(index);
		if(mx>0&&mx<10&&my>0&&my<9){
			if(e.getButton()==e.BUTTON1){
				mapMaker.drawTile(index,map);
			}else{
				mapMaker.setPassable(index,map);
			}
		}
		else{
			if(mx==0)
				screenx--;
			if(mx==10)
				screenx++;
			if(my==0)
				screeny--;
			if(my==9)
				screeny++;
			if(screenx<0)
				screenx=0;
			if(screeny<0)
				screeny=0;
			if(screenx>HacktendoCreator.X_SCREENS-1)
				screenx=HacktendoCreator.X_SCREENS-1;
			if(screeny>HacktendoCreator.Y_SCREENS-1)
				screeny=HacktendoCreator.Y_SCREENS-1;
			//System.out.println(screeny);
			mapMaker.setScreen(screenx,screeny);
		}
		repaint();
		//System.out.println("Tile X: "+mx+"\nTile Y: "+my);

	}
	
	public void mouseMoved(MouseEvent e){
		mapMaker.setPosition(e.getX()-32,e.getY()-32);	
	}
	
	public void mouseDragged(MouseEvent e){
		
	}
}
