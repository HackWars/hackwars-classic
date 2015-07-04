package Game.MMO;
/*
Programmer: Ben Coe(2007)<br />

This is the game that is maintained server-side, it runs all the same logic as a client, in an attempt to synchronize everything.
*/

import Hacktendo.*;
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

public class MMOEngine extends OpenGLRenderEngine implements Runnable{
	// MMO ENGINE SPECIFIC STUFF.
	private String level="";
	private Thread MyThread=null;
	private HashMap Players=new HashMap();
	private SpriteEventList MySpriteEventList=new SpriteEventList();//Keeps track of recent sprite events.
	
	//Getters and Setters.
	public String getLevel(){
		return(level);
	}
	
	/**
	Add a player to this game.
	*/
	public synchronized void addPlayer(String ip,String name,boolean NPC){
		if(Players.get(ip)!=null)
			return;
				
		Sprite BodySprite=new Sprite(this);//Create the torso of the player.
		BodySprite.setX(200);
		BodySprite.setY(200);
		BodySprite.setZ(0);
		BodySprite.setScriptID(7);
		BodySprite.setImageID(1);
		BodySprite.setWidth(24);
		BodySprite.setHeight(30);
		BodySprite.setXRotation(90);
		BodySprite.setAutoCollide(false);
		BodySprite.setZOffset(-16);
		
		Sprite FeetSprite=new Sprite(this);//Create the feet of the player.
		FeetSprite.setParameter("xTarget",new TypeInteger(200));
		FeetSprite.setParameter("yTarget",new TypeInteger(200));
		FeetSprite.setParameter("moveCount",new TypeInteger(0));
		FeetSprite.setParameter("newTarget",new TypeBoolean(false));
		FeetSprite.setParameter("destroy",new TypeBoolean(false));
		FeetSprite.setParameter("activated",new TypeBoolean(false));
		FeetSprite.setParameter("body",new TypeInteger(BodySprite.getSpriteID()));
		FeetSprite.setImageID(1);
		FeetSprite.setWidth(10);
		FeetSprite.setHeight(12);
		FeetSprite.setX(200);
		FeetSprite.setY(200);
		FeetSprite.setZ(0);
		FeetSprite.setScriptID(6);
		FeetSprite.setMask(255,255,255,0);
		FeetSprite.setAutoCollide(true);
		FeetSprite.setZOffset(-32);
		
		Players.put(ip,FeetSprite);//The sprite that will receive events from this player.
				
		SpriteEvent SE1=new SpriteEvent();
		SE1.setIP("");
		SE1.setName(name);
		SE1.setNPC(NPC);
		SE1.setSprite(BodySprite);
		MySpriteEventList.add(BodySprite.getSpriteID(),SE1);
		
		SpriteEvent SE2=new SpriteEvent();
		SE2.setIP(ip);
		SE2.setName(name);
		SE2.setNPC(NPC);
		SE2.setSprite(FeetSprite);
		MySpriteEventList.add(FeetSprite.getSpriteID(),SE2);
	}
	
	/**
	Remove a player from this game.
	*/
	public synchronized void removePlayer(String ip){
		Sprite S=(Sprite)Players.get(ip);
		S.setParameter("destroy",true);
	}
	
	/**
	Add a new target for a specific sprite.
	*/
	public void addTargetData(String ip,int x,int y,int currentX,int currentY){
		Sprite S=(Sprite)Players.get(ip);
		S.setParameter("xTarget",new TypeInteger(x));
		S.setParameter("yTarget",new TypeInteger(y));
		S.setParameter("newTarget",new TypeBoolean(true));
		S.setX(currentX);
		S.setY(currentY);
	}
	
	/**
	Add activation data if a player has attempted to activate another sprite.
	*/
	public void addActivationData(String ip,int activateID,int activationType){
		Sprite S=sprites[0].getByID(activateID);
		if(S==null)
			S=sprites[1].getByID(activateID);
		if(S==null)
			return;
		
		S.setParameter("activationType",new TypeInteger(activationType));
		S.setParameter("activated",new TypeBoolean(true));
	}
	
	/**
	Invalidate a sprite with the ID provided.
	*/
	public void invalidate(int id){
		Sprite S=sprites[0].getByID(id);
		if(S==null)
			S=sprites[1].getByID(id);
		if(S==null)
			return;
		MySpriteEventList.invalidate(id,S);
	}
	
	/**
	Get the current packet for the IP provided.
	*/
	public HacktendoPacket getPacket(String ip){
		return(MySpriteEventList.getPacket(ip));
	}
	
	/**
	Invalidates an invidual player throughout all the the sprites loaded in.
	*/
	public void invalidatePlayer(String ip){
		MySpriteEventList.invalidatePlayer(ip);
	}
	
	
	////////// THE STUFF BELOW IS ALL SPECIFIC TO RENDER ENGINES SOME IS PROBABLY REDUNDANT.
	private static final int MAX_SPRITE=3000;//Maximum number of sprites allowed at any one time.

	private int spriteCount=0;//Current count of active sprites.
	
	private SpriteBinaryList sprites[] = new SpriteBinaryList[2];//The 3-Layer Stack of Sprites.
	
	private Hacktendo.Map Maps[] = new Hacktendo.Map[9];//The 9 Game Maps.
	private int currentMap=0;//The Current Map.
	private TerrainHandler MyTerrainHandler=null;
	
	private OpenGLImageHandler MyImageHandler=null;//Used for Loading Images.
	
	private CollisionHandler MyCollisionHandler=new CollisionHandler(1440,768,this);//Efficient Box Based Collision
	
	//Key events.
	private ArrayList DestroySprites=new ArrayList();
	private HashMap RegisterKeyDown=new HashMap(10);
	private ArrayList KeysUp=new ArrayList();
	private ArrayList KeysDown=new ArrayList();
		
	private ArrayList Work=new ArrayList();//An array of scripts that has currently been distributed.
	int work=0;

	private ArrayList Scripts=new ArrayList();//The Script Resources INDEX=ID Scripts are Compiled.
	private ArrayList ScriptThreads=new ArrayList();//The actual thread objects.
	
	private boolean initialized=false;//Has the program been initialized.
	private final Semaphore available = new Semaphore(1, true);//For concurrent access to shared lists.
	private Sound MySound=new Sound();//The sound engine.
	private int[] transparent = new int[64];
	private Object FireWatch[]=null;
	private HashMap SaveFile=null;//The data that will be saved to this game's save file.
	private HashMap LoadFile=null;//The hash map of data loaded off a player's disk from playing the game previously.
	private String taskName="";
	private String questID="";
	private String ip=null;
	private boolean debug=false;
	private boolean beat=false;
	
	//Sky data.
	public static final int NONE=0;
	public static final int DAY=0;
	public static final int NIGHT=0;
	public static final int STORM=0;
	private String skyImage="";
	private int skyType=0;
	private float rotateSkyZ=0.1f;
	private float currentSkyZ=0.0f;
	
	/**
	Set the Z level of the water.
	*/
	public void setWaterZ(int waterZ){
		MyTerrainHandler.setWaterHeight(waterZ);
	}
		
	public void setDebug(boolean debug){
	}
	
	public void setDebugMessage(String debugMessage){
	//	System.out.println(debugMessage);
	}

	/**
	Set whether the game has been beaten. Used by NPCs.
	*/
	public void setBeat(boolean beat){
		this.beat=true;
	}
	
	/**
	Has the game been beaten in this session.
	*/
	public boolean getBeat(){
		return(beat);
	}

	/**
	Return the current map structure that is running
	*/
	public Hacktendo.Map getCurrentMap(){
		return(Maps[currentMap]);
	}
	
	/**
	Get the terrain handler being used.
	*/
	public TerrainHandler getTerrainHandler(){
		return(MyTerrainHandler);
	}
	
	//Constructor.
	public MMOEngine(String level){
		this.level=level;
		
		//Load the game associated with this node.
		String xml="";
		try{
			BufferedReader BR=new BufferedReader(new FileReader(level+".xml"));
			String s;
			while((s=BR.readLine())!=null)
				xml+=s;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		MyImageHandler=new OpenGLImageHandler(null);
		MyTerrainHandler=new TerrainHandler(MyImageHandler,this);//The terrain handler.

		sprites[0] = new SpriteBinaryList();//Create the initial Hash Maps with MAX_SPRITE capacity.
		sprites[1] = new SpriteBinaryList();

		//Initialize the Worker Threads.
		for(int i=0;i<35;i++){//Was 70
			ScriptThreads.add(new ScriptThread(this));
		}
		
		//Start this engine running.
		MyThread=new Thread(this);
		MyThread.start();
		
		loadGame(xml);
	}
	
	/**
	Clean the buffered image resources being used.
	*/
	public void clean(){
		MyImageHandler.clean();		
	}
	
	/**
	Set the watch to fire.
	*/
	public void setFireWatch(Object FireWatch[]){
		this.FireWatch=FireWatch;
	}
	
	/**
	Set the save parameters.
	*/
	public void setSaveFile(HashMap SaveFile){
		this.SaveFile=SaveFile;
	}
	
	
	/**
	Set the save parameters.
	*/
	public HashMap getSaveFile(){
		return(SaveFile);
	}
	
	/**
	Get the quest this is associated with.
	*/
	public String getQuestID(){
		return(questID);
	}
	
	/**
	Get the task this is associated with.
	*/
	public String getTaskName(){
		return(taskName);
	}
	
	/**
	Set the HashMap that has been loaded off this player's personal HD.
	*/
	public void setLoadFile(HashMap LoadFile){
		this.LoadFile=LoadFile;
	}
	
	/**
	Get the HashMap loaded from a file.
	*/
	public HashMap getLoadFile(){
		return(LoadFile);
	}
	
	/**
	Get the fire watch info.
	*/
	public Object[] getFireWatch(){
		return(FireWatch);
	}
	
	public String getIP(){
		return ip;
	}
	
	/**
	Cleans all the resources associated with the current level.
	*/
	public void cleanLevel(){
		try{
			available.acquire();
		
			currentMap=0;
			spriteCount=0;
			DestroySprites.clear();
			Sprite.spriteCount=0;
			sprites[0].clear();
			sprites[1].clear();
			MyTerrainHandler.resetHeightData();
			
			Work.clear();
			work=0;
			
			available.release();
		}catch(Exception e){
			e.printStackTrace();
			available.release();
		}
	}
	
	public void resetGlobals(){
		for(int i=0;i<35;i++){
			ScriptThread ST = (ScriptThread)ScriptThreads.get(i);
			ST.resetGlobals();
		}
	}
	
	/**
	Used by worker threads to retrieve scripts to execute.
	*/
	public Object[] getWork(){
		try{
		available.acquire();
		
			if(Work.size()==0){
				available.release();
				return(null);
			}
			
			int W[]=(int[])Work.remove(0);
			available.release();
			return(new Object[]{new Integer(W[0]),((Model[])Scripts.get(W[1]))[W[2]],new Integer(W[1])});
		
		}catch(Exception e){
			e.printStackTrace();
			available.release();
		}
		return(null);
	}
	
	/**
	Used by worker threads to report that they have completed work.
	*/
	public void returnWork(){
		try{
			available.acquire();
			work--;
			if(work<0)
				work=0;
			available.release();
		}catch(Exception e){
			e.printStackTrace();
			available.release();
		}
	}

	/**
	Some operations that a sprite performs require locking.
	*/
	public void lockForSprite(){
		try{
			available.acquire();
		}catch(Exception e){
			e.printStackTrace();
			available.release();
		}
	}
	
	public void unlockForSprite(){
		try{
			available.release();
		}catch(Exception e){
			e.printStackTrace();
			available.release();
		}
	}

	/**
	Add a sprite to the render engine.
	*/
	public void addSprite(Sprite sprite,int z,int renderType){

		try{
			available.acquire();
			
			if(spriteCount<MAX_SPRITE){
				spriteCount++;
				if(sprite.getRenderType()!=Sprite.FLAT){
					sprites[0].add(sprite);
				}else{
					sprites[1].add(sprite);
				}
			}
			available.release();
		}catch(Exception e){
			e.printStackTrace();
			available.release();
		}
	}
	
	/**
	Add a sprite to the render engine.
	*/
	public void addSpriteNoLock(Sprite sprite,int z,int renderType){

		if(spriteCount<MAX_SPRITE){
			spriteCount++;
			if(sprite.getRenderType()!=Sprite.FLAT){
				sprites[0].add(sprite);
			}else{
				sprites[1].add(sprite);
			}
		}

	}
	
	
	/**
	Add a sprite to the differed deletion queue.
	*/
	public void deleteSprite(Sprite DeleteMe,int id,int z,int renderType){
	
		try{		
			available.acquire();
			if(renderType!=Sprite.FLAT)
				DestroySprites.add(new int[]{id,0,5});
			else{
				DestroySprites.add(new int[]{id,1,5});
			}
			available.release();
		}catch(Exception e){
			e.printStackTrace();
			available.release();
		}
	}
	
	/**
	Get the HashMap of sprites associated with the given Z.
	*/
	public SpriteBinaryList getSprites(int z,int renderType){
		if(renderType!=Sprite.FLAT)
			return(sprites[0]);
		
		return(sprites[1]);
	}
	
	/**
	Get a specific sprite based on ID and Z.
	*/
	public Sprite getSprite(int z,int renderType,int spriteID){
		if(renderType!=Sprite.FLAT)
			return((Sprite)sprites[0].getByID(spriteID));

		return((Sprite)sprites[1].getByID(spriteID));
	}
	
	/**
	Load another map, running the initialize script.
	*/
	public void setMap(int map){
		if(map<9&&map>=0){
			initialized=false;
			//while(work>0);//Wait for work to finish.
			int breakCount=0;
			try{
			while(work>0){
				breakCount++;
				Thread.sleep(1);
				System.out.print("");
				if(breakCount>50){
					work=0;
					break;
				}
		
			}
			}catch(Exception e){
			e.printStackTrace();
			}
			cleanLevel();
			currentMap=map;
			initializeMap();
		}
	}
	
	public void setMap(int map,int scriptID){
		Maps[map].setScriptID(scriptID);
		if(map<9&&map>=0){
			initialized=false;
			//while(work>0);//Wait for work to finish.
			int breakCount=0;
			try{
			while(work>0){
				breakCount++;
				Thread.sleep(1);
				if(breakCount>50){
					work=0;
					break;
				}
		
			}
			}catch(Exception e){
			e.printStackTrace();
			}
			cleanLevel();
			currentMap=map;
			initializeMap();
		}
	}
	
	/**
	Return the integer key associated with the current map.
	*/
	public int getMap(){
		return(currentMap);
	}
	
	/**
	Returns a sprite without an ID being provided. Checks all three layers.
	*/
	public Sprite getSprite(int spriteID){
		Sprite returnMe=null;
		if((returnMe=(Sprite)sprites[0].getByID(new Integer(spriteID)))!=null){
			return(returnMe);
		}
		
		if((returnMe=(Sprite)sprites[1].getByID(new Integer(spriteID)))!=null){
			return(returnMe);
		}
	
		return(null);
	}
	
	/**
	Change a tile to the index provided.
	*/
	public void setTile(int index,int tileID){
		Maps[index].setID(index,tileID);
	}
	
	/**
	Check whether the tile provided is passable.
	*/
	public boolean isPassable(int index){
		return(Maps[currentMap].isPassable(index));
	}

	/**
	Get a tile based on the index.
	*/
	public int getTile(int index){
		return Maps[currentMap].getTile(index);
	}
	

	
	/**
	Initialize the map.
	*/
	public void initializeMap(){
		try{
			
			work++;
			Work.add(new int[]{-1,Maps[currentMap].getScriptID(),0});
			while(work>0){
				Thread.sleep(5);
			}
			
			initialized=true;
		}catch(Exception e){
		
		}
	}
	
	public int getTransparent(int index){
		return transparent[index];
	}
	
	public BufferedImage getImage(int index,int frame){
		return(MyImageHandler.getSpriteBufferedImage(index,frame));
	}
	
	/**
	Render the non-sprite models.
	*/
	public void renderModels(){
	
		for(int ii=0;ii<sprites[0].getIDArray().size();ii++){
			Sprite S=(Sprite)sprites[0].getIDArray().get(ii);
						
			
			//Add the sprite and tiles into the collision handler.
			if(!S.getInactive()&&S.getRunning()==false){

				int tx=(S.getX()>>5)-1;
				int ty=(S.getY()>>5)-1;
				for(int x=tx;x<tx+2;x++){
					for(int y=ty;y<ty+2;y++){
						int index=x+y*45;
						if(index>=0&&index<1080&&!isPassable(index))
								MyCollisionHandler.insert(new Integer(index));
					}
				}

				MyCollisionHandler.insert(S);
				
			}
		}
	}
	
	/**
	Render the sprite layer.
	*/
	public void renderSprites(){
	
		for(int ii=0;ii<sprites[1].getIDArray().size();ii++){
			Sprite S=(Sprite)sprites[1].getIDArray().get(ii);
					
			//Add the sprite and tiles into the collision handler.
			if(!S.getInactive()&&S.getRunning()==false){

				int tx=(S.getX()>>5)-1;
				int ty=(S.getY()>>5)-1;
				for(int x=tx;x<tx+2;x++){
					for(int y=ty;y<ty+2;y++){
						int index=x+y*45;
						if(index>=0&&index<1080&&!isPassable(index))
								MyCollisionHandler.insert(new Integer(index));
					}
				}

				MyCollisionHandler.insert(S);
				
			}

		}
	}
					
	/**
	Update the current screen.
	*/
	int counter=0;
	public void updateScreen(){
		
		try{
			counter++;//Used for various modulus based re-calculations
					
			if(!initialized)
				return;
							
			available.acquire();
			work++;
			Work.add(new int[]{-1,Maps[currentMap].getScriptID(),1});
			available.release();
					

			//Draw Sprites Here.
			renderModels();
			renderSprites();
					
			available.acquire();

			for(int i=0;i<2;i++){//Add the sprites into the processing array.
				for(int ii=0;ii<sprites[i].getIDArray().size();ii++){
					Sprite S=(Sprite)sprites[i].getIDArray().get(ii);
					
					if(S.getDrawn()||S.getOffscreenProcessing()&&!S.getInactive()){
						work++;
						
						if(S.getRunning()==false&&S.getScriptID()!=-1)
							Work.add(new int[]{S.getSpriteID(),S.getScriptID(),0});
					}
				}
			}
			
			available.release();
						
			MyCollisionHandler.reset();			
						
			//Wait on all the threads performing actions to finish.
			int breakCount=0;
			while(work>1){//We wait on the work to finish.
				breakCount++;
				Thread.sleep(1);
				if(breakCount>50){	
					work=0;
					break;
				}
			}
		
			//Destroy the sprites.
			available.acquire();
			Iterator MyIterator=DestroySprites.iterator();
						
			while(MyIterator.hasNext()){
			
				int I[]=(int[])MyIterator.next();
				MyIterator.remove();
				if(I[1]!=0){
					sprites[I[1]].removeByID(new Integer(I[0]));
				}else{
					sprites[0].removeByID(new Integer(I[0]));
				}
				
				spriteCount--;
			}
			available.release();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
		
	/**
	Add a primative object to the drawing list.
	*/
	public void addGraphics(Object[] O){
	}
	
	/**
	Decrement the sprite counter.
	*/
	public void decrementSpriteCount(){
		spriteCount--;
	}
	
	/**
	Load a game.
	*/
	public synchronized void loadGame(String xml){
		try{
		
		Scripts.clear();
		Node N = null;
		LoadXML LX = new LoadXML();
		try{
			LX.loadString(xml);
			N = LX.findNodeRecursive("ip",0);
			N = LX.findNodeRecursive(N,"#text",0);
			ip = N.getNodeValue();
			N = LX.findNodeRecursive("data",0);
			N = LX.findNodeRecursive(N,"#text",0);
			
			String info=N.getNodeValue();
			/**
			MAYBE THIS GAME IS PART OF A QUEST? IF SO WE SHOULD CHECK SOME EXTRA PARAMETERS.
			*/
			try{
				N = LX.findNodeRecursive("questid",0);
				N = LX.findNodeRecursive(N,"#text",0);
				questID = N.getNodeValue();
				
				N = LX.findNodeRecursive("task",0);
				N = LX.findNodeRecursive(N,"#text",0);
				taskName = N.getNodeValue();
				
			}catch(Exception e){
				//e.printStackTrace();
			}
			
			int size=0;
			byte buffer[]=new byte[512];
			byte B[]=Base64.decode(info);			
			
			ByteArrayOutputStream bout=new ByteArrayOutputStream();
			ZipInputStream zin=new ZipInputStream(new ByteArrayInputStream(B));
			zin.getNextEntry();
			int trans=zin.read();
			while((size=zin.read(buffer))!=-1){
				bout.write(buffer,0,size);
			}
			zin.close();
			info = bout.toString();
						
			LX.loadString(info);
			/*
			*
			*	TILES
			*
			*
			*/
			Node tiles = LX.findNodeRecursive("tiles",0);
			int i=0;
			while((N=LX.findNodeRecursive(tiles,"tile",i))!=null){
				//Load the palette.
				Node pal=LX.findNodeRecursive(N,"palette",0);
				byte ri[]=new byte[256];
				byte gi[]=new byte[256];
				byte bi[]=new byte[256];
				
				if(pal!=null){
					pal = LX.findNodeRecursive(pal,"#text",0);
					String data=pal.getNodeValue();
					B=Base64.decode(data);
					bout=new ByteArrayOutputStream();
					zin=new ZipInputStream(new ByteArrayInputStream(B));
					zin.getNextEntry();
					//trans=zin.read();
					while((size=zin.read(buffer))!=-1){
						bout.write(buffer,0,size);
					}
					zin.close();
					B = bout.toByteArray();
					for(int index=0;index<256;index++){
						ri[index]=B[index];
						gi[index]=B[index+256];
						bi[index]=B[index+512];
					}
				}else{
					ri=ImageHandler.r;
					gi=ImageHandler.g;
					bi=ImageHandler.b;
				}		
			
				Node temp = LX.findNodeRecursive(N,"id",0);
				temp=LX.findNodeRecursive(temp,"#text",0);
				int id = (int)Integer.parseInt(temp.getNodeValue());
				temp = LX.findNodeRecursive(N,"data",0);
				temp=LX.findNodeRecursive(temp,"#text",0);
				String data = temp.getNodeValue();
				B=Base64.decode(data);	
				bout=new ByteArrayOutputStream();
				zin=new ZipInputStream(new ByteArrayInputStream(B));
				zin.getNextEntry();
				//trans=zin.read();
				while((size=zin.read(buffer))!=-1){
					bout.write(buffer,0,size);
				}
				zin.close();
				B = bout.toByteArray();
				
				BufferedImage image = new BufferedImage(64,64,BufferedImage.TYPE_BYTE_INDEXED,new IndexColorModel(8,256,ri,gi,bi));
				byte b[] = ((DataBufferByte)(image).getRaster().getDataBuffer()).getData();
				try{
				for(int ii=0;ii<b.length;ii++)
					b[ii]=B[ii];
					
				MyImageHandler.setTileBufferedImage(id,image);
				}catch(Exception e){}
				i++;
			}
			
			/*
			*
			*	SPRITES
			*
			*
			*/
			tiles = LX.findNodeRecursive("sprites",0);
			i=0;
			BufferedImage image= null;
			while((N=LX.findNodeRecursive(tiles,"sprite",i))!=null){
				//Load the palette.
				Node pal=LX.findNodeRecursive(N,"palette",0);
				byte ri[]=new byte[256];
				byte gi[]=new byte[256];
				byte bi[]=new byte[256];
				
				if(pal!=null){
					pal = LX.findNodeRecursive(pal,"#text",0);
					String data=pal.getNodeValue();
					B=Base64.decode(data);
					bout=new ByteArrayOutputStream();
					zin=new ZipInputStream(new ByteArrayInputStream(B));
					zin.getNextEntry();
					//trans=zin.read();
					while((size=zin.read(buffer))!=-1){
						bout.write(buffer,0,size);
					}
					zin.close();
					B = bout.toByteArray();
					for(int index=0;index<256;index++){
						ri[index]=B[index];
						gi[index]=B[index+256];
						bi[index]=B[index+512];
					}
				}else{
					ri=ImageHandler.r;
					gi=ImageHandler.g;
					bi=ImageHandler.b;
				}			
			
				Node temp = LX.findNodeRecursive(N,"id",0);
				temp=LX.findNodeRecursive(temp,"#text",0);
				int id = (int)Integer.parseInt(temp.getNodeValue());
				Node frames = LX.findNodeRecursive(N,"frames",0);
				int ii=0;
				//this.sprites[id]=new Sprite();
				while((temp=LX.findNodeRecursive(frames,"frame",ii))!=null){				
				
					Node temp2 = LX.findNodeRecursive(temp,"data",0);
					temp2 = LX.findNodeRecursive(temp,"#text",0);
					String data = temp2.getNodeValue();
					B=Base64.decode(data);	
					bout=new ByteArrayOutputStream();
					zin=new ZipInputStream(new ByteArrayInputStream(B));
					zin.getNextEntry();
					//trans=zin.read();
					while((size=zin.read(buffer))!=-1){
						bout.write(buffer,0,size);
					}
					zin.close();
					B = bout.toByteArray();
					ByteArrayInputStream bin = new ByteArrayInputStream(B);
					trans = bin.read();
					transparent[i]=trans;
					try{
						bout = new ByteArrayOutputStream();
						while((size=bin.read(buffer))!=-1){
							bout.write(buffer,0,size);
						}
						B = bout.toByteArray();
						//System.out.println(B.length);
					}catch(Exception e){
						e.printStackTrace();
					}
					image = new BufferedImage(64,64,BufferedImage.TYPE_BYTE_INDEXED,new IndexColorModel(8,256,ri,gi,bi,trans));
					byte[] b = ((DataBufferByte)(image).getRaster().getDataBuffer()).getData();
					if(B.length>0)
					for(int iii=0;iii<b.length;iii++)
						b[iii]=B[iii];
				
					MyImageHandler.setSpriteBufferedImage(id,ii,image);
					ii++;
				}
				
				//System.out.println(i+": "+id);
				i++;
			}
			
			/*
			*
			*	SCRIPTS
			*
			*/
			tiles = LX.findNodeRecursive("scripts",0);
			i=0;
			while((N=LX.findNodeRecursive(tiles,"script",i))!=null){
				Node temp = LX.findNodeRecursive(N,"id",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				int id = Integer.parseInt(temp.getNodeValue());
				temp = LX.findNodeRecursive(N,"name",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				String name = temp.getNodeValue();
				temp = LX.findNodeRecursive(N,"type",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				int type=Integer.parseInt(temp.getNodeValue());
				String[] scripts = new String[3];
				if(type==0){
					temp = LX.findNodeRecursive(N,"initialize",0);
					temp = LX.findNodeRecursive(temp,"#text",0);
					String data = temp.getNodeValue();
					B=Base64.decode(data);	
					bout=new ByteArrayOutputStream();
					zin=new ZipInputStream(new ByteArrayInputStream(B));
					zin.getNextEntry();
					//trans=zin.read();
					while((size=zin.read(buffer))!=-1){
						bout.write(buffer,0,size);
					}
					zin.close();
					data = bout.toString();
					//HM.put("initialize",data);
					scripts[0]=data;
					temp = LX.findNodeRecursive(N,"continue",0);
					temp = LX.findNodeRecursive(temp,"#text",0);
					data = temp.getNodeValue();
					B=Base64.decode(data);	
					bout=new ByteArrayOutputStream();
					zin=new ZipInputStream(new ByteArrayInputStream(B));
					zin.getNextEntry();
					//trans=zin.read();
					while((size=zin.read(buffer))!=-1){
						bout.write(buffer,0,size);
					}
					zin.close();
					data = bout.toString();
					//HM.put("continue",data);
					scripts[1]=data;
					temp = LX.findNodeRecursive(N,"finalize",0);
					temp = LX.findNodeRecursive(temp,"#text",0);
					data = temp.getNodeValue();
					B=Base64.decode(data);	
					bout=new ByteArrayOutputStream();
					zin=new ZipInputStream(new ByteArrayInputStream(B));
					zin.getNextEntry();
					//trans=zin.read();
					while((size=zin.read(buffer))!=-1){
						bout.write(buffer,0,size);
					}
					zin.close();
					data = bout.toString();
					//HM.put("finalize",data);
					scripts[2]=data;
				}
				else{
					temp = LX.findNodeRecursive(N,"fire",0);
					temp = LX.findNodeRecursive(temp,"#text",0);
					String data = temp.getNodeValue();
					B=Base64.decode(data);	
					bout=new ByteArrayOutputStream();
					zin=new ZipInputStream(new ByteArrayInputStream(B));
					zin.getNextEntry();
					//trans=zin.read();
					while((size=zin.read(buffer))!=-1){
						bout.write(buffer,0,size);
					}
					zin.close();
					data = bout.toString();
					//HM.put("fire",data);
					scripts[0]=data;
					
				}
				//Script script = new Script(name,HM,type,id);
				Model C[]=new Model[3];
				try{
				C[0]=RunFactory.compileCode(scripts[0]);
				C[0].setMaximumIterations(50000);
				if(type==0){
					
					C[1]=RunFactory.compileCode(scripts[1]);
					C[1].setMaximumIterations(50000);
					C[2]=RunFactory.compileCode(scripts[2]);
					C[2].setMaximumIterations(50000);

				}
				}catch(Exception e){
					System.out.println(scripts[1]);
					System.out.println(scripts[2]);
					e.printStackTrace();
				}
				Scripts.add(C);
				i++;
			}
			
			/*
			*
			*	MAP
			*
			*/
			tiles = LX.findNodeRecursive("maps",0);
			i=0;
			LoadXML LX2 = new LoadXML();
			while((N=LX.findNodeRecursive(tiles,"map",i))!=null){
				Node temp = LX.findNodeRecursive(N,"id",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				int id = Integer.parseInt(temp.getNodeValue());
				//System.out.println("Loading Map "+id);
				temp = LX.findNodeRecursive(N,"script",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				int scriptID=Integer.parseInt(temp.getNodeValue());
				//mapMaker.setScript(id,scriptID);
				temp = LX.findNodeRecursive(N,"data",0);
				temp = LX.findNodeRecursive(temp,"#text",0);
				String data = temp.getNodeValue();
				B=Base64.decode(data);	
				bout=new ByteArrayOutputStream();
				zin=new ZipInputStream(new ByteArrayInputStream(B));
				zin.getNextEntry();
				//trans=zin.read();
				while((size=zin.read(buffer))!=-1){
					bout.write(buffer,0,size);
				}
				zin.close();
				data = bout.toString();
				//System.out.println(data);
				LX2.loadString(data);
				Node tiles2 =  LX2.findNodeRecursive("tiles",0);
				int ii=0;
				Node N2=null;
				byte[] mapTiles=new byte[1080];
				while((N2=LX2.findNodeRecursive(tiles2,"tile",ii))!=null){
					Node temp2 = LX2.findNodeRecursive(N2,"#text",0);
					int tileID = Integer.parseInt(temp2.getNodeValue());
					mapTiles[ii]=(byte)tileID;
					ii++;
				}
				Maps[id]=new Hacktendo.Map(scriptID,mapTiles);
				i++;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		initializeMap();
		
		}catch(Exception e2){
			e2.printStackTrace();
		}
	}
	
	//The runnable portion of this MMO Engine.
	public void run(){
		while(true){
			try{
				long currentTime=Time.getInstance().getCurrentTime();

				updateScreen();
	
				long runTime=Time.getInstance().getCurrentTime()-currentTime;
				long sleepTime=(long)Math.max(33-runTime,5);
								
				MyThread.sleep(sleepTime);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	//Testing main.
	public static void main(String args[]){
		MMOEngine MMOE=new MMOEngine("game");
	}
}
