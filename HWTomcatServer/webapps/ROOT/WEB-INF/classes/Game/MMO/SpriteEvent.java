package Game.MMO;
import com.plink.dolphinnet.*;
import java.util.*;
import java.io.*;
import java.math.*;
import Hackscript.Model.*;
import Hacktendo.*;
/**
This provides information about the state of sprites in a Hacktendo game.
*/
public class SpriteEvent{
	private boolean explode=false;
	private boolean alive=true;
	private String ip="";
	private String name="";
	private Sprite MySprite;
	private Boolean NPC=false;
	private HashMap PlayersPinged=new HashMap();//Players that should currently be pinged with an update of this sprite.
	private HashMap PlayersInitialized=new HashMap();//This Hashmap keeps track of players who have been sent this sprite's IP,Name, and NPC type.
	
	//Getters and Setters.
	public void setIP(String ip){
		this.ip=ip;
	}
	
	public String getIP(){
		return(ip);
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return(name);
	}
	
	public void setSprite(Sprite MySprite){
		this.MySprite=MySprite;
	}
	
	public void setNPC(boolean NPC){
		this.NPC=NPC;
	}
	
	public HashMap getPlayersPinged(){
		return(PlayersPinged);
	}
	
	/**
	Fetch the current compact packet associated with this sprite.
	*/
	public HacktendoPacket getPacket(String ip,HacktendoPacket returnMe){	
		PlayersPinged.put(ip,new Boolean(true));
		
		if(MySprite.getParameter("xTarget")!=null)
			returnMe.addData(MySprite.getX(),MySprite.getY(),MySprite.getZ(),(Integer)((TypeInteger)MySprite.getParameter("xTarget")).getRawValue(),(Integer)((TypeInteger)MySprite.getParameter("yTarget")).getRawValue(),explode,!alive,MySprite.getImageID(),MySprite.getFrame(),MySprite.getRenderType(),(int)MySprite.getXRotation(),(int)MySprite.getYRotation(),(int)MySprite.getZRotation(),MySprite.getWidth(),MySprite.getHeight(),MySprite.getDepth(),MySprite.getZOffset(),MySprite.getScriptID(),MySprite.getSpriteID());
		else
			returnMe.addData(MySprite.getX(),MySprite.getY(),MySprite.getZ(),MySprite.getX(),MySprite.getY(),explode,!alive,MySprite.getImageID(),MySprite.getFrame(),MySprite.getRenderType(),(int)MySprite.getXRotation(),(int)MySprite.getYRotation(),(int)MySprite.getZRotation(),MySprite.getWidth(),MySprite.getHeight(),MySprite.getDepth(),MySprite.getZOffset(),MySprite.getScriptID(),MySprite.getSpriteID());
		
		if(PlayersInitialized.get(ip)==null){
			if(MySprite.getParameter("body")!=null)
				returnMe.addSpriteReference(MySprite.getSpriteID(),this.ip,name,NPC,(Integer)((TypeInteger)MySprite.getParameter("body")).getRawValue());
			else
				returnMe.addSpriteReference(MySprite.getSpriteID(),this.ip,name,NPC,0);
			PlayersInitialized.put(ip,new Boolean(true));
		}
		return(returnMe);
	}
	
	/**
	Return whether or not this sprite is still alive.
	*/
	public boolean getAlive(){
		return(alive);
	}
	
	/**
	Invalidate player.
	*/
	public void invalidatePlayer(String ip){
		PlayersPinged.remove(ip);
		PlayersInitialized.remove(ip);
	}
	
	/**
	Invalidate this sprite.
	*/
	public void invalidate(){
		explode=false;
	
		if(MySprite.getInactive())//Set a flag indicating whether or not this sprite is still alive.
			alive=false;
			
		explode=MySprite.getExploded();
		
		PlayersPinged.clear();
	}
}
