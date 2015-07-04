
package Hacktendo;
/*
Programmer: Ben Coe/Cam(2007)<br />

Render engine takes care of most of the technical aspects of drawing a map, loading resources, etc.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.util.zip.*;
import org.apache.axis.encoding.*;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import java.util.concurrent.Semaphore;
import Hackscript.Model.*;
import View.*;
import GUI.*;
import javax.media.opengl.glu.*;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;
import com.plink.Hack3D.*;
import java.nio.*;
import Game.MMO.*;
import Assignments.*;

public class ClientRenderEngine extends OpenGLRenderEngine implements KeyListener, MouseMotionListener, MouseListener{
	private static final int SPRITE_SCRIPT=6;//The script that represents the basis for the 2D sprite.

	private HashMap Sprites=new HashMap();//Links the sprites created to their server-side ID.
	private boolean activationEvent=false;
	private int activationID=0;
	private int activationType=0;
	private boolean targetEvent=false;
	private int targetX=0;
	private int targetY=0;
	private Hacker MyHacker=null;
	private int playerID=0;//The ID of the sprite that is the player.
	private Sprite playerSprite=null;//The sprite that currently represents the player.

	public ClientRenderEngine(OpenGLImageHandler MyImageHandler,GLCanvas MyGLCanvas,Hacker hacker){
		super(MyImageHandler,MyGLCanvas,hacker);
		MyHacker=hacker;
	}
	
	//Debug message outputted to standard out.
	public void setDebugMessage(String debugMessage){
		System.out.println(debugMessage);
	}
	
	/**
	Has a sprite attempted to activate an object.
	*/
	public void activate(int id,int activationType){
		activationEvent=true;
		activationID=id;
		this.activationType=activationType;
	}
	
	/**
	Has a sprite attemted to move.
	*/
	public void target(int targetX,int targetY){	
		this.targetX=targetX;
		this.targetY=targetY;
		targetEvent=true;
	}
	
	/**
	Force a packet to be sent.
	*/
	public void sendPacket(){
		//Also save stuff to a player's local hard-drive.
		
		if(activationEvent){
			View MyView = MyHacker.getView();
			Object[] send = new Object[]{new Integer(activationID),new Integer(activationType),MyHacker.getIP()};
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HACKTENDO_PLAYER,"hacktendoActivate",send));
		}else if(targetEvent){
			View MyView = MyHacker.getView();
			Object[] send=null;
			if(playerSprite!=null)
				send = new Object[]{new Integer(targetX),new Integer(targetY),MyHacker.getIP(),new Integer(playerSprite.getX()),new Integer(playerSprite.getY())};
			else
				send = new Object[]{new Integer(targetX),new Integer(targetY),MyHacker.getIP(),new Integer(targetX),new Integer(targetY)};

			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HACKTENDO_PLAYER,"hacktendoTarget",send));
		}
		
		activationEvent=false;
		targetEvent=false;
	}
	
	/**
	Update or add a sprite to the client side game.
	*/
	public void addPacket(HacktendoPacket Packet){
		while(!getInitialized()){//Make sure things have loaded before we start mucking with stuff.
			try{
				Thread.sleep(5);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	
		do{
			Sprite S=(Sprite)Sprites.get(new Integer(Packet.getID()));
			boolean setRenderType=false;
			if(S==null){
				S=new Sprite(this);
				setRenderType=true;
				S.setParameter("destroy",new TypeBoolean(false));
				S.setParameter("globalID",new TypeInteger(Packet.getID()));
				S.setParameter("id",new TypeInteger(Packet.getID()));
				Sprites.put(new Integer(Packet.getID()),S);
				S.setOffscreenProcessing(false);
			}
			S.setScriptID(Packet.getScriptID());
			S.setImageID(Packet.getImage());
				
			if(S.getSpriteID()!=playerID||setRenderType){//Allow the sprite to move client side.
				S.setX(Packet.getX());
				S.setY(Packet.getY());
				S.setZ(Packet.getZ());
				S.setParameter("xTarget",new TypeInteger(Packet.getTargetX()));
				S.setParameter("yTarget",new TypeInteger(Packet.getTargetY()));
				S.setParameter("newTarget",new TypeBoolean(true));
			}
			
			if(Packet.getExplodeSprite())
				S.explode();
			if(Packet.getDestroySprite())
				S.setParameter("destroy",new TypeBoolean(true));
			S.setFrame(Packet.getFrame());
			S.setXRotation(Packet.getXRotation());
			S.setYRotation(Packet.getYRotation());
			S.setZRotation(Packet.getZRotation());
						
			S.setWidth(Packet.getWidth());
			S.setHeight(Packet.getHeight());
			S.setDepth(Packet.getDepth());
			S.setZOffset(Packet.getZOffset()*-1);
			
			if(setRenderType)
				S.setRenderType(Packet.getRenderType());
			
		}while(Packet.next()>0);
		
		// Takes the form Object[]{ID,IP,Name,NPC,BODY_ID}
		if(Packet.getReferenceArray()!=null){
			for(int i=0;i<Packet.getReferenceArray().size();i++){
				Object O[]=(Object[])Packet.getReferenceArray().get(i);
				Sprite S=(Sprite)Sprites.get((Integer)O[0]);
				
				System.out.println("ID: "+O[0]);
				if(S!=null){
					String ip=(String)O[1];
					String name=(String)O[2];
					boolean npc=(Boolean)O[3];
					
					S.setParameter("ip",new TypeString(ip));
					S.setParameter("name",new TypeString(name));
					S.setParameter("npc",new TypeBoolean(npc));		
					
					if(S.getScriptID()==SPRITE_SCRIPT){
						S.setOffscreenProcessing(true);
						S.setAutoCollide(true);
						Sprite S2=(Sprite)Sprites.get((Integer)O[4]);
						S.setParameter("body",new TypeInteger(S2.getSpriteID()));
					}
								
					if(ip.equals(MyHacker.getIP())){
						if(S.getScriptID()==SPRITE_SCRIPT){
							playerSprite=S;
						}
						playerID=S.getSpriteID();
						System.out.println("We are setting the player to equal : "+S.getSpriteID()+" This Is Sprite ID: "+O[0]);
						HacktendoLinker.addGlobal("player",new TypeInteger(S.getSpriteID()));
					}
				}
			}
		}
		
		
	}
}
