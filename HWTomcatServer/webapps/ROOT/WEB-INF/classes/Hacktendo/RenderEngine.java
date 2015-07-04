package Hacktendo;
/*
Porgrammer: Ben Coe/Cam(2007)<br />

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
import GUI.Sound;
import View.*;
import GUI.*;

public class RenderEngine implements KeyListener{
	//SHAPE CONSTANTS.
	public static final int RECTANGLE=0;
	public static final int FILL_RECTANGLE=1;
	public static final int TEXT=2;
	public static final int LINE=3;
	public static final int OVAL=4;
	public static final int FILL_OVAL=5;
	public static final int ARC=6;
	public static final int FILL_ARC=7;

	private static final int MAX_SPRITE=400;//Maximum number of sprites allowed at any one time.
	private int spriteCount=0;//Current count of active sprites.
	
	private SpriteBinaryList sprites = new SpriteBinaryList();//The 3-Layer Stack of Sprites.
	
	private Map Maps[] = new Map[9];//The 9 Game Maps.
	private int currentMap=0;//The Current Map.
	
	//Viewport Position and Movement Information.
	private int viewX = 0;
	private int viewY = 0;
	private int speedY = 0;
	private int speedX = 0;
	private long endXMove = -1;
	private long endYMove = -1;
	
	
	private ImageHandler MyImageHandler=null;//Used for Loading Images.
	private Component C=null;//Parent Component.
	
	private CollisionHandler MyCollisionHandler=new CollisionHandler(1440,768,this);//Efficient Box Based Collision
	
	//Key events.
	private ArrayList DestroySprites=new ArrayList();
	private HashMap RegisterKeyDown=new HashMap(10);
	private ArrayList KeysUp=new ArrayList();
	private ArrayList KeysDown=new ArrayList();
	
	private ArrayList graphics = new ArrayList();//A stack of current graphics primatives.
	
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
	private Hacker MyHacker=null;
	private boolean beat=false;
	
	//Is hacktendo in debug mode?
	public boolean isDebug(){
		return(debug);
	}
	
	public void setDebug(boolean debug){
		this.debug=debug;
	}
	
	public void setDebugMessage(String debugMessage){
		if(debug)
			MyHacker.showMessage(debugMessage);
	}
	
	/**
	Return the current map structure that is running
	*/
	public Map getCurrentMap(){
		return(Maps[currentMap]);
	}
	
	/**
	Set whether the game has been beaten. Used by NPCs.
	*/
	public void setBeat(boolean beat){
		this.beat=true;
	}
	
	public boolean getBeat(){
		return(beat);
	}

	/**
	Constructor.
	*/
	public RenderEngine(){
		sprites = new SpriteBinaryList();//Create the initial Hash Maps with MAX_SPRITE capacity.
	}
	
	public RenderEngine(ImageHandler MyImageHandler,Component C,Hacker MyHacker){
		this.MyHacker=MyHacker;
		MySound.startSound();//Initizlize the Sound Handler.
		this.MyImageHandler=MyImageHandler;//Register the Image Loader.
		this.C=C;//Register the Parent Component.
		sprites = new SpriteBinaryList();//Create the initial Hash Maps with MAX_SPRITE capacity.
		//Initialize the Worker Threads.
		for(int i=0;i<70;i++){
			ScriptThreads.add(new ScriptThread(this));
		}
		
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
			speedX=0;
			speedY=0;
			endXMove=-1;
			endYMove=-1;
			spriteCount=0;
			DestroySprites.clear();
			Sprite.spriteCount=0;
			sprites.clear();
			Work.clear();
			graphics.clear();
			work=0;
			
			available.release();
		}catch(Exception e){
			available.release();
		}
	}
	
	public void resetGlobals(){
		for(int i=0;i<70;i++){
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
			available.release();
		}
		return(null);
	}
	
	/**
	Used by worker threads to report that they have completed work.
	*/
	public void returnWork(){
		work--;
		if(work<0)
			work=0;
	}
	
	/**
	Some operations that a sprite performs require locking.
	*/
	public void lockForSprite(){
		try{
			available.acquire();
		}catch(Exception e){
			available.release();
		}
	}
	
	public void unlockForSprite(){
		try{
			available.release();
		}catch(Exception e){
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
				sprites.add(sprite);
			}
			available.release();
		}catch(Exception e){
			available.release();
		}
	}
	
	/**
	Add a sprite to the render engine.
	*/
	public void addSpriteNoLock(Sprite sprite,int z,int renderType){
		if(spriteCount<MAX_SPRITE){
			spriteCount++;
			sprites.add(sprite);
		}
	}
	
	/**
	Add a sprite to the differed deletion queue.
	*/
	public void deleteSprite(Sprite DeleteMe,int id,int z,int renderType){
		try{
			available.acquire();
			DestroySprites.add(new int[]{id,z,5});
			available.release();
		}catch(Exception e){
			available.release();
		}
	}
	
	/**
	Get the HashMap of sprites associated with the given Z.
	*/
	public SpriteBinaryList getSprites(int z,int renderType){
		return(sprites);
	}
	
	/**
	Get a specific sprite based on ID and Z.
	*/
	public Sprite getSprite(int z,int renderType,int spriteID){
		return (Sprite)sprites.getByID(spriteID);
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
				if(breakCount>50){
					work=0;
					break;
				}
		
			}
			}catch(Exception e){}
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
			}catch(Exception e){}
			cleanLevel();
			currentMap=map;
			initializeMap();
		}
	}
	
	/**
	Decrement the sprite counter.
	*/
	public void decrementSpriteCount(){
		spriteCount--;
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

		if((returnMe=(Sprite)sprites.getByID(new Integer(spriteID)))!=null){
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
	Set the position of the viewport.
	*/
	public void setViewX(int x){
		viewX=x;
	}
	
	/**
	Get the position of the viewport.
	*/
	public int getViewX(){
		return(viewX);
	}
	
	/**
	Set the position of the viewport.
	*/
	public void setViewY(int y){
		viewY=y;
	}
	
	/**
	Get the position of the viewport.
	*/
	public int getViewY(){
		return viewY;
	}

	/**
	Set the scrolling speed of viewport.
	*/
	public void setSpeedX(int speed){
		speedX=speed;
	}
	
	/**
	Get the scrolling speed of the viewport.
	*/
	public int getSpeedX(){
		return speedX;
	}
	
	/**
	Set the scrolling speed of viewport.
	*/
	public void setSpeedY(int speed){
		speedY=speed;
	}
	
	/**
	Get the scrolling speed of the viewport.
	*/
	public int getSpeedY(){
		return speedY;
	}
	
	/**
	Set the ending time of the viewport movement.
	*/
	public void setXEndTime(int time){
		endXMove=Time.getInstance().getCurrentTime()+time;
	}

	/**
	Set the ending time of the viewport movement.
	*/
	public void setYEndTime(int time){
		endYMove=Time.getInstance().getCurrentTime()+time;
	}
	
	/**
	Initialize the map.
	*/
	public void initializeMap(){
		work++;
		Work.add(new int[]{-1,Maps[currentMap].getScriptID(),0});
		while(work>0);
			initialized=true;
	}
	
	public int getTransparent(int index){
		return transparent[index];
	}
	
	public BufferedImage getImage(int index,int frame){
		return MyImageHandler.getSpriteImage(index,frame);
	}
	
	/**
	Update the current screen.
	*/
	//private int run=0;
	//private long totalTime=0;
	public void updateScreen(Graphics G){
		try{
	
		if(!initialized)
			return;
		//System.out.println("Map has been initialized");
		long currentTime=Time.getInstance().getCurrentTime();
		
		available.acquire();
		work++;
		Work.add(new int[]{-1,Maps[currentMap].getScriptID(),1});
		available.release();
	
		if(endXMove-currentTime<=0)
			speedX=0;
		if(endYMove-currentTime<=0)
			speedY=0;

		viewX+=speedX;
		viewY+=speedY;	

		int tileX=viewX/32;
		int tileY=viewY/32;		
		
		int shiftx=viewX%32;
		int shifty=viewY%32;
		
		int startX=tileX-1;
		int startY=tileY-1;
		int endX=tileX+10;
		int endY=tileY+9;
				
		for(int x=startX;x<endX;x++){
			for(int y=startY;y<endY;y++){
				if(x>=0&&y>=0&&x<45&&y<24)
					G.drawImage(MyImageHandler.getTile(Maps[currentMap].getTile(x+y*45)),((x-startX)<<5)-shiftx,((y-startY)<<5)-shifty,null);

			}
		}
				
		//Draw Sprites Here.
		int VIEW_X_PLUS_320=viewX+320;
		int VIEW_X_MINUS_64=viewX-64;
		int VIEW_Y_MINUS_64=viewY-64;
		int VIEW_Y_PLUS_288=viewY+288;
		//long runTime = 0;
		
		available.acquire();

		for(int ii=0;ii<sprites.getIDArray().size();ii++){
			Sprite S=(Sprite)sprites.getIDArray().get(ii);
		
		if(!S.getInactive())
		if(S.getX()>VIEW_X_MINUS_64&&S.getX()<VIEW_X_PLUS_320&&S.getY()>VIEW_Y_MINUS_64&&S.getY()<VIEW_Y_PLUS_288){
			G.drawImage(MyImageHandler.getSpriteImage(S.getImageID(),S.getFrame()),S.getX()-viewX+32,S.getY()-viewY+32,null);
		}
		
		int tx=(S.getX()>>5)-1;
		int ty=(S.getY()>>5)-1;
		for(int x=tx;x<tx+3;x++){
			for(int y=ty;y<ty+3;y++){
				int index=x+y*45;
				if(index>=0&&index<1080&&isPassable(index))
					MyCollisionHandler.insert(new Integer(index));
			}
		}

		if(!S.getInactive())
			MyCollisionHandler.insert(S);
		}
			
		available.release();

		for(int ii=0;ii<sprites.getIDArray().size();ii++){
			Sprite S=(Sprite)sprites.getIDArray().get(ii);
							
			work++;
			
			if(S.getRunning()==false&&!S.getInactive())
				Work.add(new int[]{S.getSpriteID(),S.getScriptID(),0});
		}
		available.release();

		MyCollisionHandler.reset();
		//Wait on all the threads performing actions to finish.
		int breakCount=0;
		while(work>0){
			breakCount++;
			Thread.sleep(1);
			if(breakCount>50){
				work=0;
				break;
			}
		}
			
		//Draw Graphics Here.
		Object[] obj = graphics.toArray();
		for(int i=0;i<obj.length;i++){
			Object[] O = (Object[])obj[i];
			int type = (int)(Integer)O[0];
			if(type==RECTANGLE){
				int x = (int)(Integer)O[1];
				int y = (int)(Integer)O[2];
				int width = (int)(Integer)O[3];
				int height = (int)(Integer)O[4];
				int r = (int)(Integer)O[5];
				int g = (int)(Integer)O[6];
				int b = (int)(Integer)O[7];
				int a = (int)(Integer)O[8];
				G.setColor(new Color(r,g,b,a));
				G.drawRect(x,y,width,height);
			}
			else if(type==FILL_RECTANGLE){
				int x = (int)(Integer)O[1];
				int y = (int)(Integer)O[2];
				int width = (int)(Integer)O[3];
				int height = (int)(Integer)O[4];
				int r = (int)(Integer)O[5];
				int g = (int)(Integer)O[6];
				int b = (int)(Integer)O[7];
				int a = (int)(Integer)O[8];
				G.setColor(new Color(r,g,b,a));
				G.fillRect(x,y,width,height);
			}
			else if(type==TEXT){
				String text = (String)O[1];
				String face = (String)O[2];
				int size = (int)(Integer)O[3];
				int x = (int)(Integer)O[4];
				int y = (int)(Integer)O[5];
				int r = (int)(Integer)O[6];
				int g = (int)(Integer)O[7];
				int b = (int)(Integer)O[8];
				int a = (int)(Integer)O[9];
				G.setColor(new Color(r,g,b,a));
				G.setFont(new Font(face,0,size));
				G.drawString(text,x,y);
			}
			else if(type==OVAL){
				int x = (int)(Integer)O[1];
				int y = (int)(Integer)O[2];
				int width = (int)(Integer)O[3];
				int height = (int)(Integer)O[4];
				int r = (int)(Integer)O[5];
				int g = (int)(Integer)O[6];
				int b = (int)(Integer)O[7];
				int a = (int)(Integer)O[8];
				G.setColor(new Color(r,g,b,a));
				G.drawOval(x,y,width,height);
			}
			else if(type==FILL_OVAL){
				int x = (int)(Integer)O[1];
				int y = (int)(Integer)O[2];
				int width = (int)(Integer)O[3];
				int height = (int)(Integer)O[4];
				int r = (int)(Integer)O[5];
				int g = (int)(Integer)O[6];
				int b = (int)(Integer)O[7];
				int a = (int)(Integer)O[8];
				G.setColor(new Color(r,g,b,a));
				G.fillOval(x,y,width,height);
			}
			else if(type==ARC){
				int x = (int)(Integer)O[1];
				int y = (int)(Integer)O[2];
				int width = (int)(Integer)O[3];
				int height = (int)(Integer)O[4];
				int startAngle = (int)(Integer)O[5];
				int endAngle = (int)(Integer)O[6];
				int r = (int)(Integer)O[7];
				int g = (int)(Integer)O[8];
				int b = (int)(Integer)O[9];
				int a = (int)(Integer)O[10];
				G.setColor(new Color(r,g,b,a));
				G.drawArc(x,y,width,height,startAngle,endAngle);
			}
			else if(type==FILL_ARC){
				int x = (int)(Integer)O[1];
				int y = (int)(Integer)O[2];
				int width = (int)(Integer)O[3];
				int height = (int)(Integer)O[4];
				int startAngle = (int)(Integer)O[5];
				int endAngle = (int)(Integer)O[6];
				int r = (int)(Integer)O[7];
				int g = (int)(Integer)O[8];
				int b = (int)(Integer)O[9];
				int a = (int)(Integer)O[10];
				G.setColor(new Color(r,g,b,a));
				G.fillArc(x,y,width,height,startAngle,endAngle);
			}
		}
		graphics.clear();	
		//Destroy the sprites.
		available.acquire();
		Iterator MyIterator=DestroySprites.iterator();
		while(MyIterator.hasNext()){
			spriteCount--;
			int I[]=(int[])MyIterator.next();
			MyIterator.remove();
			
			sprites.removeByID(new Integer(I[0]));
		}
		available.release();
		
		}catch(Exception e){
		
		}
		//run++;
	}
	
	/**
	Get the key up count.
	*/
	public int getKeyUpCount(){
		return(KeysUp.size());
	}
	
	/**
	Get the key down count.
	*/
	public int getKeyDownCount(){
		return(KeysDown.size());
	}
	
	/**
	Return the next key up message, and clear the array.
	*/
	public int getNextKeyUp(){
		if(KeysUp.size()>0){
			int k=(Integer)KeysUp.get(KeysUp.size()-1);
			KeysUp.remove(KeysUp.size()-1);
			return(k);
		}
		return(0);
	}
	
	/**
	Return the next key down message, and clear the array.
	*/
	public int getNextKeyDown(){
		if(KeysDown.size()>0){
			int k=(Integer)KeysDown.get(KeysDown.size()-1);
			KeysDown.remove(KeysDown.size()-1);
			return(k);
		}
		return(0);
	}
	
	/**
	Add a primative object to the drawing list.
	*/
	public void addGraphics(Object[] O){
		graphics.add(O);
	}
	
	/**
	Load a game.
	*/
	public synchronized void loadGame(String xml){
		Scripts.clear();
		MyImageHandler.reset();
		Node N = null;
		LoadXML LX = new LoadXML();
		try{
			LX.loadString(xml);
			N = LX.findNodeRecursive("ip",0);
			N = LX.findNodeRecursive(N,"#text",0);
			ip = N.getNodeValue();
			N = LX.findNodeRecursive("data",0);
			N = LX.findNodeRecursive(N,"#text",0);
			String info = N.getNodeValue();
			
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
				
				System.out.println(questID);
				System.out.println(taskName);
			}catch(Exception e){
				e.printStackTrace();
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
				//this.tiles[id]=new Tile(B,id);
				BufferedImage image = new BufferedImage(32,32,BufferedImage.TYPE_BYTE_INDEXED,new IndexColorModel(8,256,ImageHandler.r,ImageHandler.g,ImageHandler.b));
				byte b[] = ((DataBufferByte)(image).getRaster().getDataBuffer()).getData();
				try{
				for(int ii=0;ii<b.length;ii++)
					b[ii]=B[ii];
				MyImageHandler.setTile(id,image);
				}catch(Exception e){}
				i++;
			}
			//System.out.println("Done Tiles");
			//System.out.println("Starting Sprites");
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
					//System.out.println(data);
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
					image = new BufferedImage(32,32,BufferedImage.TYPE_BYTE_INDEXED,new IndexColorModel(8,256,ImageHandler.r,ImageHandler.g,ImageHandler.b,trans));
					byte[] b = ((DataBufferByte)(image).getRaster().getDataBuffer()).getData();
					if(B.length>0)
					for(int iii=0;iii<b.length;iii++)
						b[iii]=B[iii];
					MyImageHandler.setSpriteImage(id,ii,image);
					ii++;
				}
				try{
					byte[] b = ((DataBufferByte)(image).getRaster().getDataBuffer()).getData();
					FileOutputStream FOS=new FileOutputStream("test.txt");
					for(int in=0;in<b.length;in++){
						if(b[in]!=trans)
							FOS.write(new String("\n"+b[in]).getBytes());
						else
							FOS.write(new String("\n").getBytes());
					}
					FOS.close();
				}catch(Exception ex){
					ex.printStackTrace();
				}
				//System.out.println(i+": "+id);
				i++;
			}
			//System.out.println("Done Sprites");
			//System.out.println("Starting Scripts");
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
				if(type==0){
					
					C[1]=RunFactory.compileCode(scripts[1]);
					//System.out.println(scripts[1]);
					C[2]=RunFactory.compileCode(scripts[2]);

				}
				}catch(Exception e){
					System.out.println(id);
					e.printStackTrace();
				}
				Scripts.add(C);
				i++;
			}
			//resourceHandler.repopulateScripts();
			//System.out.println("Done Scripts");
			//System.out.println("Starting Map");
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
				System.out.println("Loading Map "+id);
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
					//System.out.println("Drawing index "+ii+" on map "+id+" tile id "+tileID);
					//mapMaker.drawTile(ii,id,tileID);
					ii++;
				}
				Maps[id]=new Map(scriptID,mapTiles);
				i++;
			}
			
			System.out.println("Done Loading");
		}catch(Exception e){
			e.printStackTrace();
		}
		initializeMap();
	}

	/**
	Play a sound based on an index.
	*/
	public void play(int index){
		MySound.play(index);
	}
	
	//Methods for KeyListener.
	public void keyPressed(KeyEvent e){	
		try{
			if(RegisterKeyDown.get(new Integer(e.getKeyCode()))==null){
				KeysDown.add(new Integer(e.getKeyCode()));
				RegisterKeyDown.put(new Integer(e.getKeyCode()),new Boolean(true));
			}
		}catch(Exception exception){
		
		}
	}
	
	public void keyReleased(KeyEvent e){
		KeysUp.add(new Integer(e.getKeyCode()));
		RegisterKeyDown.remove(new Integer(e.getKeyCode()));
	}
	
	public void keyTyped(KeyEvent e) {
	}
}
