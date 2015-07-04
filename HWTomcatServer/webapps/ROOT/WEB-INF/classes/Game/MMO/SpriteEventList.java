package Game.MMO;
import com.plink.dolphinnet.*;
import java.util.*;
import java.io.*;
import java.math.*;
import Hacktendo.Sprite;
/**
This provides information about the state of sprites in a Hacktendo game.
*/
public class SpriteEventList{
	private HashMap SpriteEvents=new HashMap();
	
	/**
	Add a new sprite event.
	*/
	public synchronized void add(int id,SpriteEvent SE){
		SpriteEvents.put(new Integer(id),SE);
	}
	
	/**
	Invalidate a sprite with the ID provided.
	*/
	public synchronized void invalidate(int id,Sprite S){
		SpriteEvent SE=(SpriteEvent)SpriteEvents.get(new Integer(id));
		if(SE==null){//We've never seen this sprite.
			SE=new SpriteEvent();
			SE.setSprite(S);
			SpriteEvents.put(new Integer(id),SE);
		}
		SE.invalidate();
	}
	
	/**
	Get the current packet for the player indicated by the IP.
	*/
	public synchronized HacktendoPacket getPacket(String ip){
		HacktendoPacket returnMe=new HacktendoPacket();
		Iterator Events=SpriteEvents.entrySet().iterator();
		while(Events.hasNext()){
			SpriteEvent SE=(SpriteEvent)(((Map.Entry)Events.next()).getValue());
			if(SE.getPlayersPinged().get(ip)==null){
				SE.getPacket(ip,returnMe);
			}
		}
		return(returnMe);
	}
	
	/**
	Iterate through the sprite event arrays and invalidate for a specific player.
	*/
	public synchronized void invalidatePlayer(String ip){
		HacktendoPacket returnMe=new HacktendoPacket();
		Iterator Events=SpriteEvents.entrySet().iterator();
		while(Events.hasNext()){
			SpriteEvent SE=(SpriteEvent)(((Map.Entry)Events.next()).getValue());
			SE.invalidatePlayer(ip);
		}
	}
}
