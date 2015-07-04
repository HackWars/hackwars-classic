
package Game.MMO;
/*
Programmer: Ben Coe/Cam(2007)<br />

This singleton maintains a list of the different 3D chat worlds based on the current network a player is on.
*/

import Hacktendo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.zip.*;
import java.util.Iterator;
import org.apache.axis.encoding.*;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import java.util.concurrent.Semaphore;
import Hackscript.Model.*;
import GUI.Sound;
import View.*;
import javax.media.opengl.glu.*;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;
import com.plink.Hack3D.*;
import java.nio.*;
import java.util.*;

public class WorldSingleton extends RenderEngine{
	private static WorldSingleton MyWorldSingleton=null;
	private HashMap Worlds=new HashMap();
	
	//Get an instance of this world singleton.
	public synchronized static WorldSingleton getInstance(){
		if(MyWorldSingleton==null){
			MyWorldSingleton=new WorldSingleton();
		}
		return(MyWorldSingleton);
	}
	
	//Constructor.
	private WorldSingleton(){
		MMOEngine A=new MMOEngine("game");
		Worlds.put("game",A);
	}
	
	/**
	Add a player to this game.
	*/
	public void addPlayer(String network,String ip,String name,boolean NPC){
		MMOEngine World=(MMOEngine)Worlds.get(network);
		World.addPlayer(ip,name,NPC);
	}
	
	/**
	Remove a player from this game.
	*/
	public void removePlayer(String network,String ip){
		MMOEngine World=(MMOEngine)Worlds.get(network);
		World.removePlayer(network);
	}
	
	/**
	Add a new target for a specific sprite.
	*/
	public void addTargetData(String network,String ip,int x,int y,int currentX,int currentY){
		MMOEngine World=(MMOEngine)Worlds.get(network);
		World.addTargetData(ip,x,y,currentX,currentY);
	}
	
	/**
	Add activation data if a player has attempted to activate another sprite.
	*/
	public void addActivationData(String network,String ip,int activateID,int activationType){
		MMOEngine World=(MMOEngine)Worlds.get(network);
		World.addActivationData(ip,activateID,activationType);
	}
	
	/**
	Get the current packet for the IP provided.
	*/
	public HacktendoPacket getPacket(String network,String ip){
		MMOEngine World=(MMOEngine)Worlds.get(network);
		return(World.getPacket(ip));
	}
	
	/**
	Invalidate a single player from every list, this is performed when a player re-logs in.
	*/
	public void invalidatePlayer(String network,String ip){
		MMOEngine World=(MMOEngine)Worlds.get(network);
		World.invalidatePlayer(ip);
	}	
}