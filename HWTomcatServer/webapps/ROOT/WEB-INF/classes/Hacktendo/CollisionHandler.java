package Hacktendo;
/*
Porgrammer: Ben Coe.(2007)<br />

Controls collisions between multiple objects on the screen.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import util.*;
import java.util.*;
import java.awt.geom.*;
import util.*;
import Game.MMO.*;

public class CollisionHandler{
	/////////////////////
	// Data.
	private ArrayList Containers[][]=null;//The containers that objects can be placed on in the screen.
	private int xContainers=0;//Number of containers in the X direction.
	private int yContainers=0;//Number of containers in the Y direction.
	private int diameter=0;
	private RenderEngine RE=null;
	private boolean TileCheck[]=new boolean[1080];
	
	/**
	Construct the CollisionHandler based on the dimensions of the viewport.
	*/
	public CollisionHandler(float width,float height,RenderEngine RE){
		this.RE=RE;
		this.diameter=32;
		xContainers=(int)(width/diameter)+4;
		yContainers=(int)(height/diameter)+4;
		
		Containers=new ArrayList[xContainers][yContainers];
		for(int i=0;i<xContainers;i++)
			for(int ii=0;ii<yContainers;ii++)
				Containers[i][ii]=new ArrayList();
		
	}
	
	/**
	Test for the intersection of two convex polygons.
	*/
	public boolean collide(Polygon A,Polygon B){
		return(false);
	}
	
	/**
	Insert an object into a container and test for collisions.
	*/
	private ArrayList TestList=new ArrayList();
	private ArrayList CancelDoubles=new ArrayList();
	private Rectangle R1=null;
	private Point TP=null;//The center point of the collided object.

	public void insert(Object TO){
	
		try{
			TestList.clear();//Clear the collision list.
			CancelDoubles.clear();//Clear the list that checks for double counts.

			//Fetch the object we are attempting to insert.
			if(TO instanceof Sprite){
				Sprite S = (Sprite)TO;
				TP=S.getCenter();
				R1=S.getRectangle();
			}else if(TO instanceof Integer){
				int index = (Integer)TO;
				if(index>=1080||index<0)
					return;
				if(TileCheck[index])
					return;
				TileCheck[index]=true;
				
				int y = (index/45)*32;
				int x = (index%45)*32;
				int x1=x+16;
				int y1=y+16;
				TP=new Point(x1,y1);
				R1 = new Rectangle(x,y,32,32);
			}else{
				int index = (int)(float)(Float)TO;
				if(index>=1080||index<0)
					return;
				
				int y = (index/45)*32;
				int x = (index%45)*32;
				int x1=x+16;
				int y1=y+16;
				TP=new Point(x1,y1);
				R1 = new Rectangle(x,y,32,32);
			}
			
			int minusx=((int)R1.getWidth()/32);
			int minusy=((int)R1.getWidth()/32);
			if(minusx==0)
				minusx=1;
			if(minusy==0)
				minusy=1;
			
			int cx=(int)(TP.getX()/diameter);
			int cy=(int)(TP.getY()/diameter);
			int minx=Math.max(-2,cx-minusx);
			int maxx=Math.min(xContainers,cx+minusx);
			int miny=Math.max(-2,cy-minusy);
			int maxy=Math.min(xContainers,cy+minusy);
						
			for(int x=minx;x<maxx;x++)
				for(int y=miny;y<maxy;y++){
					if(x>-2&&y>-2&&x<xContainers-2&&y<yContainers-2){
						TestList.addAll(Containers[x+2][y+2]);
						Containers[x+2][y+2].add(TO);
					}
				}				
								
			boolean TOInteger = (TO instanceof Integer);
			
			for(int i=0;i<TestList.size();i++){
				boolean collided=false;
			
				Object O = TestList.get(i);
				if(CancelDoubles.contains(O))//We've already intersected this object.
					continue;
				
				Point TP2=null;
				Rectangle R2 = null;
				
				//Fetch the sprite to compare to.
				if(O instanceof Sprite){
					Sprite testSprite = (Sprite)O;
					R2 = testSprite.getRectangle();
				}else if(O instanceof Integer){
					int index = (Integer)O;
					int y = (index/45)*32;
					int x = (index%45)*32;
					int x1=x+16;
					int y1=y+16;
					TP=new Point(x1,y1);
					R2 = new Rectangle(x,y,32,32);
				}else if(O instanceof Float){
					int index = (int)(float)(Float)O;
					int y = (index/45)*32;
					int x = (index%45)*32;
					int x1=x+16;
					int y1=y+16;
					TP=new Point(x1,y1);
					R2 = new Rectangle(x,y,32,32);
				}
												
				if(R1.intersects(R2)){
					CancelDoubles.add(O);
					//Test for pixel level collision. This improved upon algorithm scales the image on the fly.
					if((TO instanceof Sprite)&&(O instanceof Sprite)&&((Sprite)TO).getCollisionType()==0&&((Sprite)O).getCollisionType()==0){
						//Calculate the portion of the buffer we must test against.
						Sprite S1=(Sprite)TO;
						Sprite S2=(Sprite)O;
					
						Rectangle Intersection=R1.intersection(R2);
						double S1XStart=Intersection.getX()-R1.getX();
						if(S1XStart<0)
							S1XStart=0.0;
						double S1XAdd=S1.getWidth()/32.0;
						
						double S2XStart=Intersection.getX()-R2.getX();
						if(S2XStart<0)
							S2XStart=0.0;
						double S2XAdd=S2.getWidth()/32.0;
						
						double S1YStart=Intersection.getY()-R1.getY();
						if(S1YStart<0)
							S1YStart=0.0;
						double S1YAdd=S1.getHeight()/32.0;
						
						double S2YStart=Intersection.getY()-R2.getY();
						if(S2YStart<0)
							S2YStart=0.0;
						double S2YAdd=S2.getHeight()/32.0;
						
						double S1YStop=S1YStart+Intersection.getHeight();
						if(S1YStop>S1.getHeight())
							S1YStop=(double)S1.getHeight();
						
						double S2YStop=S2YStart+Intersection.getHeight();
						if(S2YStop>S2.getHeight())
							S2YStop=(double)S2.getHeight();
						
						double S1XStop=S1XStart+Intersection.getWidth();
						if(S1XStop>S1.getWidth())
							S1XStop=S1.getWidth();
																		
						double S2XStop=S2XStart+Intersection.getWidth();
						if(S2XStop>S2.getWidth())
							S2XStop=S2.getWidth();
						
						//Generate the rasters for collision detection.
						BufferedImage TempImage=RE.getImage(S1.getImageID(),S1.getFrame());
						byte b1[]=null;
						b1 = ((DataBufferByte)(TempImage).getRaster().getDataBuffer()).getData();
						
						byte b2[]=null;
						TempImage=RE.getImage(S2.getImageID(),S2.getFrame());
						b2 = ((DataBufferByte)(TempImage).getRaster().getDataBuffer()).getData();												
						
						
						double S1ResetY=S1YStart;
						double S2ResetY=S2YStart;
						int x1,y1,x2,y2;
						
						while(S1XStart<S1XStop){
							S1YStart=S1ResetY;
							S2YStart=S2ResetY;
							while(S1YStart<S1YStop){
								x1=(int)S1XStart;
								y1=(int)S1YStart;
								x2=(int)S2XStart;
								y2=(int)S2YStart;
								if(b1[(int)(x1/S1XAdd)+(int)(y1/S1YAdd)*32]!=RE.getTransparent(S1.getImageID())&&b2[(int)(x2/S2XAdd)+(int)(y2/S2YAdd)*32]!=RE.getTransparent(S2.getImageID())){
									collided=true;
									break;
								}
							
								S1YStart+=1.0;
								S2YStart+=1.0;
							}
							
							if(collided)
								break;
																					
							S1XStart+=1.0;
							S2XStart+=1.0;
						}
						
					}else{
						collided=true;
					}
				}
						
				//If a collision has taken place set a flag in the effected sprites.
				if(collided){					
					if(!TOInteger&&!(TO instanceof Float)){
						Sprite S1=(Sprite)TO;
						if(O instanceof Sprite){
							Sprite S = (Sprite)O;
							if(S1.getSpriteID()!=S.getSpriteID()){
								S.setCollided(S1.getSpriteID(),Sprite.SPRITE);
								S1.setCollided(S.getSpriteID(),Sprite.SPRITE);
							}
						}
						else{
							if(O instanceof Integer)
								S1.setCollided((int)(Integer)O,Sprite.TILE);
							else
								S1.setCollided((int)(float)(Float)O,Sprite.MOUSE);
						}
					}
					else if(!(O instanceof Integer)&&!(O instanceof Float)){
						if(TO instanceof Integer){
							Sprite S = (Sprite)O;
							S.setCollided((int)(Integer)TO,Sprite.TILE);
						}else{
							Sprite S = (Sprite)O;
							S.setCollided((int)(float)(Float)TO,Sprite.MOUSE);
						}
					}
				}
				
					
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	Reset the containers for the next round of collision detection.
	*/
	public void reset(){
	
		for(int i=0;i<xContainers;i++)
			for(int ii=0;ii<yContainers;ii++){
				Containers[i][ii].clear();
			}
		for(int i=0;i<1080;i++)
			TileCheck[i]=false;
	}
}//END.

