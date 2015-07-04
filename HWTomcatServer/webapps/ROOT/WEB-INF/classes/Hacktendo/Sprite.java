package Hacktendo;
/*
Programmer: Ben Coe.(2007)<br />

One of the game sprites.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import com.plink.Hack3D.*;
import java.util.concurrent.Semaphore;

public class Sprite{
	
	private final Semaphore available = new Semaphore(1, true);//For concurrent access to shared lists.

	//The constatns for collision type.
	public static final int TILE=0;
	public static final int SPRITE=1;
	public static final int MOUSE=2;
	
	//What shape is this sprite. (we can use some stock models);
	public static final int FLAT=0;
	public static final int SPHERE=1;
	public static final int CUBE=2;
	public static final int CONE=3;
	public static final int SPRITE_3D=4;
	private int renderType=0;
	
	public static int spriteCount=0;//Integer used to assign this sprite's ID.
	private int spriteID=-1;//The ID of this sprite.
	
	private int imageID=-1;//What image should be rendered for this sprite.
	private int frame=0;//What frame of the chipset should be drawn.
	
	//The position of this sprite.
	private int x=0;
	private int y=0;
	private int z=0;
	private float rotateX=0.0f;
	private float rotateY=0.0f;
	private float rotateZ=0.0f;
	private int lastX=0;
	private int lastY=0;
	private int width=32;
	private int height=32;
	private int zOffset=0;
	private int depth=32;//The depth of the object.
	private int r=255;//Colors for masking.
	private int g=255;
	private int b=255;
	private int a=255;
	private float lastHeight=-1.0f;//How high off the ground was this sprite in a previous frame?
	
	private long lastAccessed=0;//Used to time when it should be deleted.
	
	private int scriptID=-1;//The script that should be executed for this sprite.
	
	private HashMap parameters=new HashMap();//Local variables for this sprite.
	private ArrayList Messages=new ArrayList();//Messages sent from other sprites.
	private ArrayList Collided=new ArrayList();//Other objects that have been collided with.
	
	private RenderEngine RE=null;//The underlying render engine.
	
	private boolean running=false;//Is this sprite currently active.
	private boolean inactive=false;	
	private boolean autoCollide=false;
	private boolean offscreenProcessing=true;
	private boolean repeatTexture=false;
	private boolean ignoreTerrain=false;
	private boolean collideWithWater=false;
	private boolean exploded=false;
	private int maxStepUp=20;
	private int maxStepDown=20;
	private boolean drawn=false;
	private boolean initialized=false;
	private int collisionType=0;//0=Pixel Based 1=Rectangular.
	private boolean invalidated=true;
	private int renderListID=-1;
	private int shadowRenderListID=-1;
	
	/**
	Get whether or not this sprite has been invalidated and needs to be redrawn.
	*/
	public boolean getInvalidated(){
		return(invalidated);
	}
	
	public void setInvalidated(boolean invalidated){
		this.invalidated=invalidated;
	}
		
	/**
	Return the render list associated with this sprite, if one exists.
	*/
	public void setRenderListID(int renderListID){
		this.renderListID=renderListID;
	}
	
	public int getRenderListID(){
		return(renderListID);
	}
	
	/**
	Get whether or not this sprite has been initialized.
	*/
	public boolean getInitialized(){
		return(initialized);
	}
	
	public void setInitialized(boolean initialized){
		this.initialized=initialized;
	}
	
	/**
	Get the render list used for this object's shadow.
	*/
	public void setShadowRenderListID(int shadowRenderListID){
		this.shadowRenderListID=shadowRenderListID;
	}
	
	public int getShadowRenderListID(){
		return(shadowRenderListID);
	}
	
	/**
	Get and set whether this sprite has been drawn in the last round of rendering.
	*/
	public boolean getDrawn(){
		return(drawn);
	}
	
	public void setDrawn(boolean drawn){
		this.drawn=drawn;
	}
		
	/**
	Flags for whether or not the sprite is currently being rendered.
	*/
	public void setInactive(boolean inactive){
		((OpenGLRenderEngine)RE).returnListIDs(this);
		renderListID=-1;
		shadowRenderListID=-1;
		this.inactive=inactive;
	}
	
	/**
	Get whether or not this sprite has been exploded since its last frame.
	*/
	public boolean getExploded(){
		return(exploded);
	}
	
	/**
	Set the max step up value, when performing terrain collision.
	*/
	public void setMaxStepUp(int maxStepUp){
		this.maxStepUp=maxStepUp;
	}
	
	/**
	Set the max step down variable when performing terrain collision.
	*/
	public void setMaxStepDown(int maxStepDown){
		this.maxStepDown=maxStepDown;
	}
	
	/**
	Set whether or not this sprite should collide with water
	*/
	public void setCollideWithWater(boolean collideWithWater){
		this.collideWithWater=collideWithWater;
	}
	
	/**
	Return whether this sprite's texture should be repeated, currently only works for flat sprites.
	*/
	public boolean repeatTexture(){
		return(repeatTexture);
	}
	
	public void setRepeatTexture(boolean repeatTexture){
		this.repeatTexture=repeatTexture;
		invalidated=true;
	}
	
	/**
	Should this sprite ignore terrain height when being rendered?
	*/
	public boolean ignoreTerrain(){
		return(ignoreTerrain);
	}
	
	public void setIgnoreTerrain(boolean ignoreTerrain){
		this.ignoreTerrain=ignoreTerrain;
	}
	
	/**
	Cause this sprite to explode.
	*/
	public void explode(){
		OpenGLRenderEngine GLRenderEngine=(OpenGLRenderEngine)RE;
		ParticleEngine MyParticleEngine=GLRenderEngine.getParticleEngine();
		if(MyParticleEngine!=null)
			MyParticleEngine.explodeSprite(this,GLRenderEngine.getTerrainHandler());
		exploded=true;
	}
	
	/**
	Get the height of the terrain at this sprite's feet.
	*/
	public int getTerrainHeight(){
		TerrainHandler MyTerrainHandler=((OpenGLRenderEngine)RE).getTerrainHandler();
		float positionX=this.x+(float)width/2.0f;
		float positionY=this.y+(float)height/2.0f;
		float newPositionX=x+(float)width/2.0f;
		float currentHeight=MyTerrainHandler.getHeight((int)(positionX/16.0f),(int)(positionY/16.0f),(int)(positionX%16.0f),(int)(positionY%16.0f));
		return((int)currentHeight);
	}
	
	public boolean getInactive(){
		return(inactive);
	}
	
	public void setRunning(boolean running){
		this.running=running;
	}
	
	public boolean getRunning(){
		return(running);
	}
	
	public boolean getOffscreenProcessing(){
		return(offscreenProcessing);
	}
	
	public void setOffscreenProcessing(boolean offscreenProcessing){
		this.offscreenProcessing=offscreenProcessing;
	}
	
	/**
	Z offset can be used to fiddle with the positioning of things on the Z plane.
	*/
	public void setZOffset(int zOffset){
		this.zOffset=zOffset;
		invalidated=true;
	}
	
	public int getZOffset(){
		return(zOffset);
	}
	
	/**
	Should the sprite be auto collided with tiles.
	*/
	public void setAutoCollide(boolean autoCollide){
		this.autoCollide=autoCollide;
	}
	
	public boolean getAutoCollide(){
		return(autoCollide);
	}
	
	/**
	How should we render this sprite?
	*/
	public void setRenderType(int renderType){
	
		RE.lockForSprite();
		SpriteBinaryList H=RE.getSprites(z,this.renderType);
		
		if(H.removeByID(new Integer(spriteID))!=null)
			RE.decrementSpriteCount();
		this.renderType=renderType;
		RE.addSpriteNoLock(this,z,renderType);

		RE.unlockForSprite();
		
		invalidated=true;
	}
	
	public int getRenderType(){
		return(renderType);
	}
	
	//Constructor.
	public Sprite(RenderEngine RE){
		this.RE=RE;
		
		RE.lockForSprite();
		spriteCount++;
		this.spriteID=spriteCount-1;
		RE.unlockForSprite();
	}
	
	/**
	Get and set the unique ID of this sprite.
	*/
	public void setSpriteID(int spriteID){
		this.spriteID=spriteID;
	}
	
	public int getSpriteID(){
		return spriteID;
	}
	
	/**
	Set the current image for this sprite.
	*/
	public void setImageID(int imageID){
		this.imageID=imageID;
		invalidated=true;
	}
	
	public int getImageID(){
		return imageID;
	}
	
	/**
	Set the current frame of the frameset being used.
	*/
	public void setFrame(int frame){
		this.frame=frame;
		invalidated=true;
	}
	
	public int getFrame(){
		return frame;
	}
	
	/**
	Sets the position on the screen that this sprite should be rendered.
	*/
	public void setX(int x){

		if(autoCollide){//If auto-collide is true we don't let the sprite pass through an impassable tile.
			if(x<0||x>1440)
				return;
		
			if(lastHeight!=-1){//Make sure we can't change height too drastically if we're using a height map.
				TerrainHandler MyTerrainHandler=((OpenGLRenderEngine)RE).getTerrainHandler();
				float positionX=this.x+((float)width/2.0f);
				float positionY=this.y+((float)height/2.0f);
				
				float newPositionX=x+((float)width/2.0f);
				
				if(!MyTerrainHandler.stepAllowed((int)(newPositionX/16.0f),(int)(positionY/16.0f),(int)(newPositionX%16.0f),(int)(positionY%16.0f)))
					return;
				
				/*float currentHeight=MyTerrainHandler.getHeight((int)(positionX/16.0f),(int)(positionY/16.0f),(int)(positionX%16.0f),(int)(positionY%16.0f));
				float newHeight=MyTerrainHandler.getHeight((int)(newPositionX/16.0f),(int)(positionY/16.0f),(int)(newPositionX%16.0f),(int)(positionY%16.0f));
								
				if(newHeight-currentHeight>maxStepUp||currentHeight-newHeight>maxStepDown||(newHeight<MyTerrainHandler.getWaterHeight()&&collideWithWater&&((OpenGLRenderEngine)RE).getRenderWater()))
					return;*/
					
				//lastHeight=newHeight;
			}/*else{//Set the initial height of this sprite.
				TerrainHandler MyTerrainHandler=((OpenGLRenderEngine)RE).getTerrainHandler();
				float positionX=this.x+(float)width/2.0f;
				float positionY=this.y+(float)height/2.0f;
				lastHeight=MyTerrainHandler.getHeight((int)(positionX/16.0f),(int)(positionY/16.0f),(int)(positionX%16.0f),(int)(positionY%16.0f));
			}*/
		
		
			if(this.x==x)
				return;
		
			int mod=0;
			int xRebound=this.x-x;
			
			if(xRebound<0)
				mod=width;
				
			int tx=((x+mod)>>5);
			int ty=(y>>5);
			int tileIndex=tx+ty*45;

			tx=((x+mod)>>5);
			ty=((y+height)>>5);
			int tileIndex2=tx+ty*45;
									
			if(tileIndex>=0&&tileIndex<1080&&!RE.getCurrentMap().isPassable(tileIndex)){		
				
				if(xRebound>0){
					this.x=(((x+mod)/32)+1)*32;
				}else{
					this.x=(((x+mod)/32)-1)*32;
					this.x+=32-width-1;
				}
				
			}else if(tileIndex2>=0&&tileIndex2<1080&&!RE.getCurrentMap().isPassable(tileIndex2)){
				if(xRebound>0){
					this.x=(((x+mod)/32)+1)*32;
				}else{
					this.x=(((x+mod)/32)-1)*32;
					this.x+=32-width-1;
				}
			}else
				this.x=x;
			
		}else
			this.x=x;
	
		invalidated=true;
	}
	
	/**
	Sets the position on the screen that this sprite should be rendered.
	*/
	public void setY(int y){
	
		if(autoCollide){//If auto-collide is true we don't let the sprite pass through an impassable tile.
			if(y<0||y>768)
				return;
		
			//if(lastHeight!=-1){//Make sure we can't change height too drastically if we're using a height map.
				TerrainHandler MyTerrainHandler=((OpenGLRenderEngine)RE).getTerrainHandler();
				float positionX=this.x+(float)width/2.0f;
				float positionY=this.y+(float)height/2.0f;
				float newPositionY=y+(float)height/2.0f;

				
				if(!MyTerrainHandler.stepAllowed((int)(positionX/16.0f),(int)(newPositionY/16.0f),(int)(positionX%16.0f),(int)(newPositionY%16.0f)))
					return;
				
			/*	float currentHeight=MyTerrainHandler.getHeight((int)(positionX/16.0f),(int)(positionY/16.0f),(int)(positionX%16.0f),(int)(positionY%16.0f));
				float newHeight=MyTerrainHandler.getHeight((int)(positionX/16.0f),(int)(newPositionY/16.0f),(int)(positionX%16.0f),(int)(newPositionY%16.0f));
				
				if(newHeight-currentHeight>maxStepUp||currentHeight-newHeight>maxStepDown||(newHeight<MyTerrainHandler.getWaterHeight()&&collideWithWater&&((OpenGLRenderEngine)RE).getRenderWater()))
					return;
					
				lastHeight=newHeight;*/
		/*	}else{//Set the initial height of this sprite.
				TerrainHandler MyTerrainHandler=((OpenGLRenderEngine)RE).getTerrainHandler();
				float positionX=this.x+(float)width/2.0f;
				float positionY=this.y+(float)height/2.0f;
				lastHeight=MyTerrainHandler.getHeight((int)(positionX/16.0f),(int)(positionY/16.0f),(int)(positionX%16.0f),(int)(positionY%16.0f));
			}*/
		
			if(this.y==y)
				return;
		
			int mod=0;
			int yRebound=this.y-y;
			
			if(yRebound<0)
				mod=height;
				
			int tx=(x>>5);
			int ty=((y+mod)>>5);
			int tileIndex=tx+ty*45;
			
			tx=((x+width)>>5);
			ty=((y+mod)>>5);
			int tileIndex2=tx+ty*45;
									
			if(tileIndex>=0&&tileIndex<1080&&!RE.getCurrentMap().isPassable(tileIndex)){	
				if(yRebound>0){
					this.y=(((y+mod)/32)+1)*32;
				}else{
					this.y=(((y+mod)/32)-1)*32;
					this.y+=32-height-1;
				}
			}else if(tileIndex2>=0&&tileIndex2<1080&&!RE.getCurrentMap().isPassable(tileIndex2)){
				if(yRebound>0){
					this.y=(((y+mod)/32)+1)*32;
				}else{
					this.y=(((y+mod)/32)-1)*32;
					this.y+=32-height-1;
				}
			}else
				this.y=y;
			
		}else
			this.y=y;
			
		invalidated=true;
	}
	
	/**
	Sets how deep the object is.
	*/
	public void setDepth(int depth){
		this.depth=depth;
		invalidated=true;
	}
	
	public int getDepth(){
		return(depth);
	}
	
	/**
	Sets the position on the screen that this sprite should be rendered.
	*/
	public void setZ(int z){
		RE.lockForSprite();
		SpriteBinaryList H=RE.getSprites(this.z,renderType);

		if(H.removeByID(new Integer(spriteID))!=null)
			RE.decrementSpriteCount();
		this.z=z;
		RE.addSpriteNoLock(this,z,renderType);

		RE.unlockForSprite();
		
		invalidated=true;
	}
	
	
	/**
	Get the position of the sprite.
	*/
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getZ(){
		return z;
	}
	
	/**
	Get/Set the rotation of the sprite.
	*/
	public void setXRotation(float rotateX){
		this.rotateX=rotateX;
		invalidated=true;
	}
	
	public float getXRotation(){
		return(rotateX);
	}
	
	public void setYRotation(float rotateY){
		this.rotateY=rotateY;
		invalidated=true;
	}
	
	public float getYRotation(){
		return(rotateY);
	}
	
	public void setZRotation(float rotateZ){
		this.rotateZ=rotateZ;
		invalidated=true;
	}
	
	public float getZRotation(){
		return(rotateZ);
	}
	
	/**
	Set the last time this sprite was accessed (used to differ deletion.
	*/
	public void setLastAccessed(long lastAccessed){
		this.lastAccessed=lastAccessed;
	}
	
	public long getLastAccessed(){
		return lastAccessed;
	}
	
	/**
	The script attached to this sprite.
	*/
	public void setScriptID(int scriptID){
		this.scriptID=scriptID;
	}
	
	public int getScriptID(){
		return scriptID;
	}
	
	/**
	Set a local variable for this sprite.
	*/
	public void setParameter(String key,Object value){
		try{
			available.acquire();
			parameters.put(key,value);
			available.release();
		}catch(Exception e){
			available.release();
		}
	}
	
	public synchronized Object getParameter(String key){
		return parameters.get(key);
	}
	
	/**
	Set a message from another sprite.
	*/
	public int getMessageCount(){
		return(Messages.size());
	}
	
	public void setMessage(String flag,Object data){
		try{
			available.acquire();
			Messages.add(new Object[]{flag,data});
			available.release();
		}catch(Exception e){
			available.release();
		}
	}
	
	public String getFlag(){
		if(Messages.size()>0){
			return((String)((Object[])Messages.get(0))[0]);
		}
		return("");
	}
	
	public Object getMessage(){
		if(Messages.size()>0){
			return((Object)((Object[])Messages.get(0))[1]);
		}
		return("");
	}
	
	public void nextMessage(){
		try{
			available.acquire();
			if(Messages.size()>0){
				Messages.remove(0);
			}
			available.release();
		}catch(Exception e){
			available.release();
		}
	}
	
	/**
	Get the center of this sprite.
	*/
	public Point getCenter(){
		return new Point(x+width/2,y+height/2);
	}
	
	/**
	Get the diameter of this sprite.
	*/
	public int getDiameter(){
		return (int)Math.sqrt(width*width+height*height);
	}
	
	/**
	Set the width and height of this sprite.
	*/
	public void setWidth(int width){
		this.width=width;
		invalidated=true;
	}
	
	public void setHeight(int height){
		this.height=height;
		invalidated=true;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	/**
	Get and set objects this sprite has collided with.
	*/
	public int getCollidedCount(){
		return(Collided.size());
	}
	
	public void setCollided(Integer id,Integer type){
		Collided.add(new Object[]{id,type});
	}
	
	public void resetCollided(){
		exploded=false;
		Collided.clear();
		Messages.clear();
	}
	
	public int getCollidedWith(){
		if(Collided.size()>0){
			return((Integer)((Object[])Collided.get(0))[0]);
		}
		return(-1);
	}
	
	public int getCollidedType(){
		if(Collided.size()>0){
			return((Integer)((Object[])Collided.get(0))[1]);
		}
		return(0);
	}
	
	public void nextCollided(){	
		if(Collided.size()>0){
			Collided.remove(0);
		}
	}
	
	public int getCollisionType(){
		return collisionType;
	}
	
	public void setCollisionType(int collisionType){
		this.collisionType=collisionType;
	}

	/**
	Get the bounding rectangle of this sprite.
	*/
	public Rectangle getRectangle(){
		Rectangle r = new Rectangle(x,y,width,height);
		return r;
	}
	
	/**
	set the masking color for this sprite.
	*/
	public void setMask(int r,int g,int b,int a){
		this.r=r;
		this.g=g;
		this.b=b;
		this.a=a;
		invalidated=true;
	}
	
	public int getR(){
		return(r);
	}
	
	public int getG(){
		return(g);
	}
	
	public int getB(){
		return(b);
	}
	
	public int getA(){
		return(a);
	}
}
