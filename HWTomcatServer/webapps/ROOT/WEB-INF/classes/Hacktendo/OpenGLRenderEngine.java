
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
import GUI.Sound;
import View.*;
import GUI.*;
import javax.media.opengl.glu.*;
import javax.media.opengl.*;
import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;
import com.plink.Hack3D.*;
import java.nio.*;

public class OpenGLRenderEngine extends RenderEngine implements KeyListener, MouseMotionListener, MouseListener{
	//DATA FROM MOUSE EVENTS.
	private int mouseX=0;
	private int mouseY=0;
	private int mouseWorldX=0;
	private int mouseWorldY=0;
	private int mouseWorldZ=0;
	private int mouseTileX=0;
	private int mouseTileY=0;
	private boolean mouseDown=false;
	private boolean mouseClicked=false;

	//SHAPE CONSTANTS.
	public static final int RECTANGLE=0;
	public static final int FILL_RECTANGLE=1;
	public static final int TEXT=2;
	public static final int LINE=3;
	public static final int TEXT_WORLD=4;
	public static final int RECTANGLE_WORLD=5;
	public static final int FILL_RECTANGLE_WORLD=6;

	public static final int MINIMUM_Z=-3300;
	public static final int MAXIMUM_Z=-10;
	public static final int INITIAL_Z=-379;

	private static final int MAX_SPRITE=400;//Maximum number of sprites allowed at any one time.
	private int spriteLists=-1;
	private int currentSpriteList=0;
	private int currentShadowList=0;
	private ArrayList availableSpriteLists=new ArrayList();
	private ArrayList availableShadowLists=new ArrayList();
	private int spriteCount=0;//Current count of active sprites.
	
	private SpriteBinaryList sprites[] = new SpriteBinaryList[2];//The 3-Layer Stack of Sprites.
	
	private Map Maps[] = new Map[9];//The 9 Game Maps.
	private int currentMap=0;//The Current Map.
	private TerrainHandler MyTerrainHandler=null;
	//private SpriteFace MySpriteFace=null;
	//private SpriteAnimator MySpriteAnimator=null;
	//private SpriteBody MySpriteBody=null;
	private ParticleEngine MyParticleEngine=new ParticleEngine();
	
	//Viewport Position and Movement Information.
	private boolean viewChanged=true;
	private int viewX = 0;
	private int viewY = 0;
	private int viewZ = INITIAL_Z;
	private int speedY = 0;
	private int speedX = 0;
	private int speedZ = 0;
	private int minViewX=0;
	private int minViewY=0;
	private int maxViewX=0;
	private int maxViewY=0;
	private float frustum[][]=new float[6][4];
	private long endXMove = -1;
	private long endYMove = -1;
	private long endZMove = -1;
	private float rotateX=0.0f;
	private float rotateY=0.0f;
	private float rotateZ=0.0f;
	
	private OpenGLImageHandler MyImageHandler=null;//Used for Loading Images.
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
	private final Semaphore available2 = new Semaphore(1, true);//For concurrent access to shared lists.

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
	private boolean renderWater=false;
	private boolean renderShadows=true;
	
	//Sky data.
	public static final int NONE=0;
	public static final int DAY=0;
	public static final int NIGHT=0;
	public static final int STORM=0;
	private String skyImage="";
	private int skyType=0;
	private float rotateSkyZ=0.1f;
	private float currentSkyZ=0.0f;
	
	//Terrain data.
	private int loadTerrain=0;//The id of the terrain that should be loaded.
	private boolean REQUEST_LOAD_TERRAIN=false;
	
	/**
	Allow the player to request that a new terrain be generated.
	*/
	public void requestLoadTerrain(int loadTerrain){
		this.loadTerrain=loadTerrain;
		REQUEST_LOAD_TERRAIN=true;
	}
	
	/**
	Is hacktendo in debug mode?
	*/
	public boolean isDebug(){
		return(debug);
	}

	/**
	Set whether or not the transparent water layer should be rendered.
	*/
	public void setRenderWater(boolean renderWater){
		this.renderWater=renderWater;
	}
	
	public boolean getRenderWater(){
		return(renderWater);
	}
	
	/**
	Get the particle engine associated with this.
	*/
	public ParticleEngine getParticleEngine(){
		return(MyParticleEngine);
	}
	
	/**
	Set whether or not shadows should be rendered.
	*/
	public void setRenderShadows(boolean renderShadows){
		this.renderShadows=renderShadows;
	}
	
	/**
	Set the current sky properties.
	*/
	public void setSky(int skyType){
		this.skyType=skyType;
		if(skyType==1){
			skyImage="images/sky.jpg";
			rotateSkyZ=0.1f;
		}else
		
		if(skyType==2){
			skyImage="images/stars.jpg";
			rotateSkyZ=0.0f;
		}else
		
		if(skyType==3){
			skyImage="images/storm.jpg";
			rotateSkyZ=0.1f;
		}else
			this.skyType=0;
	}
	
	/**
	Set the Z level of the water.
	*/
	public void setWaterZ(int waterZ){
		MyTerrainHandler.setWaterHeight(waterZ);
	}
		
	public void setDebug(boolean debug){
		this.debug=debug;
	}
	
	public void setDebugMessage(String debugMessage){
		if(debug)
			MyHacker.showMessage(debugMessage);
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
	public Map getCurrentMap(){
		return(Maps[currentMap]);
	}
	
	/**
	Get the terrain handler being used.
	*/
	public TerrainHandler getTerrainHandler(){
		return(MyTerrainHandler);
	}
	
	/**
	Set the different rotation values.
	*/
	public void setXRotation(float rotateX){
		this.rotateX=rotateX;
		viewChanged=true;
	}
	
	public float getXRotation(){
		return(rotateX);
	}
	
	/**
	Set the different rotation values.
	*/
	public void setYRotation(float rotateY){
		this.rotateY=rotateY;
		viewChanged=true;
	}
	
	public float getYRotation(){
		return(rotateY);
	}
	
	/**
	Set the different rotation values.
	*/
	public void setZRotation(float rotateZ){
		this.rotateZ=rotateZ;
		viewChanged=true;
	}
	
	public float getZRotation(){
		return(rotateZ);
	}

	/**
	Constructor.
	*/
	public OpenGLRenderEngine(){
		sprites[0] = new SpriteBinaryList();//Create the initial Hash Maps with MAX_SPRITE capacity.
		sprites[1] = new SpriteBinaryList();
	}
	
	public OpenGLRenderEngine(OpenGLImageHandler MyImageHandler,Component C,Hacker MyHacker){
		
		computeShadowMatrix();
		MyTerrainHandler=new TerrainHandler(MyImageHandler,this);//The terrain handler.

		this.MyHacker=MyHacker;
		MySound.startSound();//Initizlize the Sound Handler.
		this.MyImageHandler=MyImageHandler;//Register the Image Loader.
		this.C=C;//Register the Parent Component.
		sprites[0] = new SpriteBinaryList();//Create the initial Hash Maps with MAX_SPRITE capacity.
		sprites[1] = new SpriteBinaryList();

		//Initialize the Worker Threads.
		for(int i=0;i<50;i++){//Was 70
			ScriptThreads.add(new ScriptThread(this));
		}
		
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
			available2.acquire();
		
			currentMap=0;
			speedX=0;
			speedY=0;
			endXMove=-1;
			endYMove=-1;
			spriteCount=0;
			
			rotateX=0.0f;
			rotateY=0.0f;
			rotateZ=0.0f;
			viewX=0;
			viewY=0;
			viewZ=INITIAL_Z;
			DestroySprites.clear();
			Sprite.spriteCount=0;
			sprites[0].clear();
			sprites[1].clear();
			MyTerrainHandler.resetHeightData();
			skyType=0;
			renderShadows=true;
			renderWater=false;
			currentSpriteList=spriteLists;
			currentShadowList=spriteLists+MAX_SPRITE;
			
			Work.clear();
			graphics.clear();
			work=0;
			
			available2.release();
			available.release();
		}catch(Exception e){
			e.printStackTrace();
			available.release();
		}
	}
	
	public void resetGlobals(){
		for(int i=0;i<50;i++){
			ScriptThread ST = (ScriptThread)ScriptThreads.get(i);
			ST.resetGlobals();
		}
	}
	
	/**
	Used by worker threads to retrieve scripts to execute.
	*/
	public Object[] getWork(){
		try{
			available2.acquire();
			
			if(Work.size()==0){
				available2.release();
				return(null);
			}
			
			int W[]=(int[])Work.remove(0);
			available2.release();
			return(new Object[]{new Integer(W[0]),((Model[])Scripts.get(W[1]))[W[2]],new Integer(W[1])});
		
		}catch(Exception e){
			e.printStackTrace();
			available2.release();
		}
		return(null);
	}
	
	/**
	Used by worker threads to report that they have completed work.
	*/
	public void returnWork(){
		try{
			available2.acquire();
			work--;
			if(work<0)
				work=0;
			available2.release();
		}catch(Exception e){
			e.printStackTrace();
			available2.release();
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
	Set the position of the viewport.
	*/
	public void setViewX(int x){
		viewX=x;
		viewChanged=true;
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
	public void setViewZ(int z){
		viewZ=z;
	
		if(viewZ>=MAXIMUM_Z)
			viewZ=MAXIMUM_Z;		
		if(viewZ<=MINIMUM_Z)
			viewZ=MINIMUM_Z;
			
							
		viewChanged=true;
	}
	
	/**
	Get the position of the viewport.
	*/
	public int getViewZ(){
		return(viewZ);
	}
	
	
	/**
	Set the position of the viewport.
	*/
	public void setViewY(int y){
		this.viewY=y;
		viewChanged=true;
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
		try{
			sphere=-1;//Force the compiled lists to re-load.
			loadTerrain=-1;

			work++;
			Work.add(new int[]{-1,Maps[currentMap].getScriptID(),0});
			while(work>0){
				Thread.sleep(5);
			}
			
			initialized=true;
			viewChanged=true;
			//regenerateLists();
			
			REQUEST_LOAD_TERRAIN=true;
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
	Fetch the dimensions of the viewing frustum.
	*/
	public int getMaxViewX(){
		return(maxViewX);
	}
	
	public int getMinViewX(){
		return(minViewX);
	}
	
	public int getMinViewY(){
		return(minViewY);
	}
	
	public int getMaxViewY(){
		return(maxViewY);
	}
	
	public int getViewportWidth(){
		return(maxViewX-minViewX);
	}
	
	public int getViewportHeight(){
		return(maxViewY-minViewY);
	}
	
	/**
	Fetches the model view matrix.
	*/
	public float[] getModelView(){
		return(modl);
	}
	
	/**
	Fetch the GL object, only can be done within a GL thread.
	*/
	private GL gl;
	public GL getGL(){
		return(gl);
	}
	
	public void setGL(GL gl){
		this.gl=gl;
	}
	
	/**
	This function extracts the frustum planes for testing an object against.
	*/

	//It's less expensive to not declare these arrays in each frame.
	private float proj[]=new float[16];
	private float modl[]=new float[16];
	private double projd[]=new double[16];
	private double modld[]=new double[16];
	private double tempModl[]=new double[16];
	private float clip[]=new float[16];
	private int view[]=new int[4];
	private double obj[]=new double[3];
	
	private void extractFrustum(GL gl,GLU glu,int parentWidth,int parentHeight)
	{
		float t;
	
	   /* Get the current PROJECTION matrix from OpenGL */
	   gl.glGetFloatv( GL.GL_PROJECTION_MATRIX, proj,0 );
	   gl.glGetDoublev( GL.GL_PROJECTION_MATRIX, projd,0 );

	   /* Get the current MODELVIEW matrix from OpenGL */
	   gl.glGetFloatv( GL.GL_MODELVIEW_MATRIX, modl,0 );
	   gl.glGetDoublev( GL.GL_MODELVIEW_MATRIX, modld,0 );

	   
	   /* Get the viewport position */
	   gl.glGetIntegerv(GL.GL_VIEWPORT,view,0);

	   /* Combine the two matrices (multiply projection by modelview) */
	   clip[ 0] = modl[ 0] * proj[ 0] + modl[ 1] * proj[ 4] + modl[ 2] * proj[ 8] + modl[ 3] * proj[12];
	   clip[ 1] = modl[ 0] * proj[ 1] + modl[ 1] * proj[ 5] + modl[ 2] * proj[ 9] + modl[ 3] * proj[13];
	   clip[ 2] = modl[ 0] * proj[ 2] + modl[ 1] * proj[ 6] + modl[ 2] * proj[10] + modl[ 3] * proj[14];
	   clip[ 3] = modl[ 0] * proj[ 3] + modl[ 1] * proj[ 7] + modl[ 2] * proj[11] + modl[ 3] * proj[15];

	   clip[ 4] = modl[ 4] * proj[ 0] + modl[ 5] * proj[ 4] + modl[ 6] * proj[ 8] + modl[ 7] * proj[12];
	   clip[ 5] = modl[ 4] * proj[ 1] + modl[ 5] * proj[ 5] + modl[ 6] * proj[ 9] + modl[ 7] * proj[13];
	   clip[ 6] = modl[ 4] * proj[ 2] + modl[ 5] * proj[ 6] + modl[ 6] * proj[10] + modl[ 7] * proj[14];
	   clip[ 7] = modl[ 4] * proj[ 3] + modl[ 5] * proj[ 7] + modl[ 6] * proj[11] + modl[ 7] * proj[15];

	   clip[ 8] = modl[ 8] * proj[ 0] + modl[ 9] * proj[ 4] + modl[10] * proj[ 8] + modl[11] * proj[12];
	   clip[ 9] = modl[ 8] * proj[ 1] + modl[ 9] * proj[ 5] + modl[10] * proj[ 9] + modl[11] * proj[13];
	   clip[10] = modl[ 8] * proj[ 2] + modl[ 9] * proj[ 6] + modl[10] * proj[10] + modl[11] * proj[14];
	   clip[11] = modl[ 8] * proj[ 3] + modl[ 9] * proj[ 7] + modl[10] * proj[11] + modl[11] * proj[15];

	   clip[12] = modl[12] * proj[ 0] + modl[13] * proj[ 4] + modl[14] * proj[ 8] + modl[15] * proj[12];
	   clip[13] = modl[12] * proj[ 1] + modl[13] * proj[ 5] + modl[14] * proj[ 9] + modl[15] * proj[13];
	   clip[14] = modl[12] * proj[ 2] + modl[13] * proj[ 6] + modl[14] * proj[10] + modl[15] * proj[14];
	   clip[15] = modl[12] * proj[ 3] + modl[13] * proj[ 7] + modl[14] * proj[11] + modl[15] * proj[15];

	   /* Extract the numbers for the RIGHT plane */
	   frustum[0][0] = clip[ 3] - clip[ 0];
	   frustum[0][1] = clip[ 7] - clip[ 4];
	   frustum[0][2] = clip[11] - clip[ 8];
	   frustum[0][3] = clip[15] - clip[12];

	   /* Normalize the result */
	   t = (float)Math.sqrt( frustum[0][0] * frustum[0][0] + frustum[0][1] * frustum[0][1] + frustum[0][2] * frustum[0][2] );
	   frustum[0][0] /= t;
	   frustum[0][1] /= t;
	   frustum[0][2] /= t;
	   frustum[0][3] /= t;

	   /* Extract the numbers for the LEFT plane */
	   frustum[1][0] = clip[ 3] + clip[ 0];
	   frustum[1][1] = clip[ 7] + clip[ 4];
	   frustum[1][2] = clip[11] + clip[ 8];
	   frustum[1][3] = clip[15] + clip[12];

	   /* Normalize the result */
	   t = (float)Math.sqrt( frustum[1][0] * frustum[1][0] + frustum[1][1] * frustum[1][1] + frustum[1][2] * frustum[1][2] );
	   frustum[1][0] /= t;
	   frustum[1][1] /= t;
	   frustum[1][2] /= t;
	   frustum[1][3] /= t;

	   /* Extract the BOTTOM plane */
	   frustum[2][0] = clip[ 3] + clip[ 1];
	   frustum[2][1] = clip[ 7] + clip[ 5];
	   frustum[2][2] = clip[11] + clip[ 9];
	   frustum[2][3] = clip[15] + clip[13];

	   /* Normalize the result */
	   t = (float)Math.sqrt( frustum[2][0] * frustum[2][0] + frustum[2][1] * frustum[2][1] + frustum[2][2] * frustum[2][2] );
	   frustum[2][0] /= t;
	   frustum[2][1] /= t;
	   frustum[2][2] /= t;
	   frustum[2][3] /= t;

	   /* Extract the TOP plane */
	   frustum[3][0] = clip[ 3] - clip[ 1];
	   frustum[3][1] = clip[ 7] - clip[ 5];
	   frustum[3][2] = clip[11] - clip[ 9];
	   frustum[3][3] = clip[15] - clip[13];

	   /* Normalize the result */
	   t = (float)Math.sqrt( frustum[3][0] * frustum[3][0] + frustum[3][1] * frustum[3][1] + frustum[3][2] * frustum[3][2] );
	   frustum[3][0] /= t;
	   frustum[3][1] /= t;
	   frustum[3][2] /= t;
	   frustum[3][3] /= t;

	   /* Extract the FAR plane */
	   frustum[4][0] = clip[ 3] - clip[ 2];
	   frustum[4][1] = clip[ 7] - clip[ 6];
	   frustum[4][2] = clip[11] - clip[10];
	   frustum[4][3] = clip[15] - clip[14];

	   /* Normalize the result */
	   t = (float)Math.sqrt( frustum[4][0] * frustum[4][0] + frustum[4][1] * frustum[4][1] + frustum[4][2] * frustum[4][2] );
	   frustum[4][0] /= t;
	   frustum[4][1] /= t;
	   frustum[4][2] /= t;
	   frustum[4][3] /= t;

	   /* Extract the NEAR plane */
	   frustum[5][0] = clip[ 3] + clip[ 2];
	   frustum[5][1] = clip[ 7] + clip[ 6];
	   frustum[5][2] = clip[11] + clip[10];
	   frustum[5][3] = clip[15] + clip[14];

	   /* Normalize the result */
	   t = (float)Math.sqrt( frustum[5][0] * frustum[5][0] + frustum[5][1] * frustum[5][1] + frustum[5][2] * frustum[5][2] );
	   frustum[5][0] /= t;
	   frustum[5][1] /= t;
	   frustum[5][2] /= t;
	   frustum[5][3] /= t;
	   
	   //FIND THE MAX AND MIN BOUNDS OF THE LOWEST DRAWING LAYER OF THE ACTUAL LEVEL.

		FloatBuffer Pixel=FloatBuffer.allocate(1);
		gl.glReadPixels(0, 0, 1, 1,GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, Pixel);
	
		glu.gluUnProject(0.0,(double)parentHeight,(double)Pixel.get(0),modld,0,projd,0,view,0,(double[])obj,0);
		minViewX=(int)obj[0];
		minViewY=(int)obj[1];
		
		gl.glReadPixels(parentWidth, parentHeight, 1, 1,GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, Pixel);
		glu.gluUnProject((double)parentWidth,0.0,(double)Pixel.get(0),modld,0,projd,0,view,0,(double[])obj,0);
		maxViewX=(int)obj[0];
		maxViewY=(int)obj[1];
		
		if(maxViewX<minViewX){
			int temp=maxViewX;
			maxViewX=minViewX;
			minViewX=temp;
		}
		
		if(maxViewY<minViewY){
			int temp=maxViewY;
			maxViewY=minViewY;
			minViewY=temp;
		}
		
		viewChanged=false;
	}
	
	/**
	Test whether an object has intersected the frustum using a
	spherical bounding box.
	*/
	public boolean sphereInFrustum(float x,float y,float z,float radius)
	{
	   int p;

	   for( p = 0; p < 6; p++ )
		  if( frustum[p][0] * x + frustum[p][1] * y + frustum[p][2] * z + frustum[p][3] <= -radius )
			 return(false);
	   return(true);
	}
	
	/**
	Force the render lists to be re-generated.
	*/
	public void regenerateLists(){
		generateLists=true;
	}
	
	/**
	This function generates compiled vertex arrays for several stock 3D objects.
	*/
	boolean generateLists=false;
	private GLUquadric Sphere=null;
	private int startList=-1;
	private int sphere=-1;
	private int map[]=new int[15];
	private int cube=-1;
	private int cone=-1;
	private int tree=-1;
	private int coneLow=-1;
	private int sphereLow=-1;
	private int shadowBuffer=-1;
	private boolean texturesLoaded=false;

	public void generateLists(GL gl,GLU glu,GLUT glut){

		//Load some stock textures.
		if(!texturesLoaded){
			MyImageHandler.addStockTexture("images/sky.jpg");
			MyImageHandler.addStockTexture("images/stars.jpg");
			MyImageHandler.addStockTexture("images/storm.jpg");
			texturesLoaded=true;
		}
		
		
		if(gl.glIsList(startList)){
			gl.glDeleteLists(startList,22);
		}
		
		
		startList = gl.glGenLists(22);//Generate the compiled vertex arrays.
	
		//Generate a sphere for use as a special object.
		gl.glPushMatrix();
		GLUquadric qobj = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(qobj, GLU.GLU_FILL);
		glu.gluQuadricNormals(qobj, GLU.GLU_SMOOTH);
		glu.gluQuadricTexture(qobj,true);
		gl.glNewList(startList, GL.GL_COMPILE);
		gl.glTranslatef(0.0f,0.0f,16.0f);
		glu.gluSphere(qobj, 16.0f, 15, 10);
		gl.glEndList();
		gl.glPopMatrix();
		glu.gluDeleteQuadric(qobj);
		
		//Generate the Map.
		for(int i=1;i<16;i++){
				int x=(i-1)%5;
				int y=(i-1)/5;
		
				MyTerrainHandler.render(gl,glu,MyImageHandler,Maps[currentMap],x*18,y*16,x*18+18,y*16+16,startList+i);
		}
		
		//Generate a cube.
		gl.glNewList(startList+16, GL.GL_COMPILE);

		gl.glPushMatrix();
		
		//Cube drawing code borrowed from NeHe.
		gl.glRotatef(180.0f,0.0f,0.0f,1.0f);
		gl.glTranslatef(-48.0f,-48.0f,-32.0f);
		gl.glBegin(GL.GL_QUADS);
			// Top.
			gl.glNormal3f(0.0f,0.0f,-1.0f);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(64.0f, 64.0f,  32.0f);	// Bottom Left Of The Texture and Quad
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 32.0f, 64.0f,  32.0f);	// Bottom Right Of The Texture and Quad
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 32.0f,  32.0f,  32.0f);	// Top Right Of The Texture and Quad
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(64.0f,  32.0f,  32.0f);	// Top Left Of The Texture and Quad
			// Bottom
			gl.glNormal3f(0.0f,0.0f,1.0f);
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(64.0f, 64.0f, 64.0f);	// Bottom Right Of The Texture and Quad
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(64.0f,  32.0f, 64.0f);	// Top Right Of The Texture and Quad
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 32.0f,  32.0f, 64.0f);	// Top Left Of The Texture and Quad
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 32.0f, 64.0f, 64.0f);	// Bottom Left Of The Texture and Quad
			
			// Top Face
			gl.glNormal3f(0.0f,-1.0f,0.0f);
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(64.0f,  32.0f, 64.0f);	// Top Left Of The Texture and Quad
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(64.0f,  32.0f,  32.0f);	// Bottom Left Of The Texture and Quad
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 32.0f,  32.0f,  32.0f);	// Bottom Right Of The Texture and Quad
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 32.0f,  32.0f, 64.0f);	// Top Right Of The Texture and Quad
			// Bottom Face
			gl.glNormal3f(0.0f,1.0f,0.0f);
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(64.0f, 64.0f, 64.0f);	// Top Right Of The Texture and Quad
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 32.0f, 64.0f, 64.0f);	// Top Left Of The Texture and Quad
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 32.0f, 64.0f,  32.0f);	// Bottom Left Of The Texture and Quad
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(64.0f, 64.0f,  32.0f);	// Bottom Right Of The Texture and Quad
			
			// Right face
			gl.glNormal3f(1.0f,0.0f,0.0f);
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 32.0f, 64.0f, 64.0f);	// Bottom Right Of The Texture and Quad
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 32.0f,  32.0f, 64.0f);	// Top Right Of The Texture and Quad
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 32.0f,  32.0f,  32.0f);	// Top Left Of The Texture and Quad
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 32.0f, 64.0f,  32.0f);	// Bottom Left Of The Texture and Quad
			// Left Face
			gl.glNormal3f(-1.0f,0.0f,0.0f);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(64.0f, 64.0f, 64.0f);	// Bottom Left Of The Texture and Quad
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(64.0f, 64.0f,  32.0f);	// Bottom Right Of The Texture and Quad
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(64.0f,  32.0f,  32.0f);	// Top Right Of The Texture and Quad
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(64.0f,  32.0f, 64.0f);	// Top Left Of The Texture and Quad
			
			gl.glNormal3f(0.0f,0.0f,-1.0f);
		gl.glEnd();
		gl.glPopMatrix();
		gl.glEndList();
		
		//Build a cone.
		gl.glPushMatrix();
		qobj = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(qobj, GLU.GLU_FILL);
		glu.gluQuadricNormals(qobj, GLU.GLU_SMOOTH);
		glu.gluQuadricTexture(qobj,true);
		gl.glNewList(startList+17, GL.GL_COMPILE);
		gl.glTranslatef(0.0f,0.0f,-16.0f);
		glu.gluCylinder(qobj, 0.0f, 16.0f, 32.0f,15, 10);
		gl.glEndList();
		gl.glPopMatrix();
		glu.gluDeleteQuadric(qobj);
		

		//Generate a smaller sphere used for shadow.
		gl.glPushMatrix();
		qobj = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(qobj, GLU.GLU_FILL);
		glu.gluQuadricNormals(qobj, GLU.GLU_SMOOTH);
		glu.gluQuadricTexture(qobj,true);
		gl.glNewList(startList+19, GL.GL_COMPILE);
		gl.glTranslatef(0.0f,0.0f,16.0f);
		glu.gluSphere(qobj, 16.0f, 8, 8);
		gl.glEndList();
		gl.glPopMatrix();
		glu.gluDeleteQuadric(qobj);

		//Build a smaller cone used for shadow.
		gl.glPushMatrix();
		qobj = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(qobj, GLU.GLU_FILL);
		glu.gluQuadricNormals(qobj, GLU.GLU_SMOOTH);
		glu.gluQuadricTexture(qobj,true);
		gl.glNewList(startList+20, GL.GL_COMPILE);
		gl.glTranslatef(0.0f,0.0f,-16.0f);
		glu.gluCylinder(qobj, 0.0f, 16.0f, 32.0f,8, 8);
		gl.glEndList();
		gl.glPopMatrix();
		glu.gluDeleteQuadric(qobj);
		

		sphere=startList;//A sphere primitive.
		for(int i=1;i<16;i++)
			map[i-1]=startList+i;//The Game's map.
		cube=startList+16;
		cone=startList+17;
		tree=startList+18;
		sphereLow=startList+19;
		coneLow=startList+20;
		
		viewChanged=true;
	}
	
	/**
	Force a view changed message.
	*/
	public void viewChange(){
		viewChanged=true;
	}
	
	/**
	Apply the painter's algorithm to the sprite array provided.
	*/
	public ArrayList applyPaintersAlgorithm(ArrayList Sprites){
		IndexByZ SpriteIndexByZ=new IndexByZ();
		for(int i=0;i<Sprites.size();i++){
			Sprite S=(Sprite)Sprites.get(i);
			float positionX=(float)S.getX()+(float)S.getWidth()/2.0f;
			float positionY=(float)S.getY()+(float)S.getHeight()/2.0f;
			float positionZ;
			if(!S.ignoreTerrain()){//Should we ignore the terrain when rendering this sprite on the Z?
				float heightMod=MyTerrainHandler.getHeight((int)(positionX/16.0f),(int)(positionY/16.0f),(int)(positionX%16.0f),(int)(positionY%16.0f));
				positionZ=(float)(-3.0f-((S.getZ()+1)*32.0f))-heightMod;
			}else{
				positionZ=(float)(-3.0f-((S.getZ()+1)*32.0f));
			}
			
			positionZ=positionX*proj[2]+positionY*proj[6]+positionZ*proj[10];
			
			SpriteIndexByZ.add(new Object[]{new Float(positionZ),new Integer(i)});
		}
		return(SpriteIndexByZ.getData());
	}
	
	/**
	This function performs a depth check based on vector magnitude during the shadow rendering step.
	*/
	public float depthCheck(GL gl,GLU glu,float x,float y,float z){
		
		glu.gluProject(x,y,z,modld,0,projd,0,view,0,(double[])obj,0);
	
		FloatBuffer Pixel=FloatBuffer.allocate(1);
		gl.glReadPixels((int)obj[0],(int)obj[1], 1, 1,GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, Pixel);
				
		if(obj[0]<view[0]||obj[0]>view[2]||obj[1]<view[1]||obj[1]>view[3])
			return(0.0f);
	
		glu.gluUnProject(obj[0],obj[1],(double)Pixel.get(0),modld,0,projd,0,view,0,(double[])obj,0);
		float rx=(int)obj[0];
		float ry=(int)obj[1];
		float rz=(int)obj[2];
				
		rx=rx-x;
		ry=ry-y;
		rz=rz-z;
		float magnitude=(float)Math.sqrt(rx*rx+ry*ry+rz*rz);
				
		return(magnitude);
	}
	
	/**
	Invalidate the sprites causing their render lists to be re-generated.
	*/
	public void invalidateSprites(){
		for(int i=0;i<2;i++){//Add the sprites into the processing array.
			for(int ii=0;ii<sprites[i].getIDArray().size();ii++){
				Sprite S=(Sprite)sprites[i].getIDArray().get(ii);
				S.setInvalidated(true);
				S.setRenderListID(-1);
			}
		}
	}
	
	/**
	Compute the matrix used for projecting the shadow.
	*/
	private double mShadow[]=new double[16];
	private double pNormal[]=new double[]{0.0,0.0,-1.0,0.0};
	private double lightPosition[]=new double[]{1.5f,2.0f,-3.0f,0.0f};
	void computeShadowMatrix(){
	
		double dot=pNormal[0]*lightPosition[0]+pNormal[1]*lightPosition[1]+pNormal[2]*lightPosition[2]+pNormal[3]*lightPosition[3];

		mShadow[0]  = dot - lightPosition[0] * pNormal[0];
		mShadow[4]  = 0.f - lightPosition[0] * pNormal[1];
		mShadow[8]  = 0.f - lightPosition[0] * pNormal[2];
		mShadow[12] = 0.f - lightPosition[0] * pNormal[3];

		mShadow[1]  = 0.f - lightPosition[1] * pNormal[0];
		mShadow[5]  = dot - lightPosition[1] * pNormal[1];
		mShadow[9]  = 0.f - lightPosition[1] * pNormal[2];
		mShadow[13] = 0.f - lightPosition[1] * pNormal[3];

		mShadow[2]  = 0.f - lightPosition[2] * pNormal[0];
		mShadow[6]  = 0.f - lightPosition[2] * pNormal[1];
		mShadow[10] = dot - lightPosition[2] * pNormal[2];
		mShadow[14] = 0.f - lightPosition[2] * pNormal[3];

		mShadow[3]  = 0.f - lightPosition[3] * pNormal[0];
		mShadow[7]  = 0.f - lightPosition[3] * pNormal[1];
		mShadow[11] = 0.f - lightPosition[3] * pNormal[2];
		mShadow[15] = dot - lightPosition[3] * pNormal[3];
	}
	
	/**
	Render the shadows of the models.
	*/
	public void renderModelShadows(GL gl,GLU glu){

		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDisable(gl.GL_TEXTURE_2D);

		gl.glColor4f(0.0f,0.0f,0.0f,0.3f);
	
		for(int ii=0;ii<sprites[0].getIDArray().size();ii++){
			Sprite S=(Sprite)sprites[0].getIDArray().get(ii);
			if(S.getRenderType()==Sprite.FLAT)
				continue;
			
			int maxRadius=(int)Math.max(S.getWidth(),S.getHeight());
			maxRadius=(int)Math.max(maxRadius,S.getDepth());
			
			float positionX=(float)S.getX()+(float)S.getWidth()/2.0f;
			float positionY=(float)S.getY()+(float)S.getHeight()/2.0f;
			float positionZ;
			float heightMod=MyTerrainHandler.getHeight((int)(positionX/16.0f),(int)(positionY/16.0f),(int)(positionX%16.0f),(int)(positionY%16.0f));
			if(!S.ignoreTerrain()){//Should we ignore the terrain when rendering this sprite on the Z?
				positionZ=(float)(-3.0f-((S.getZ()+1)*32.0f))-heightMod;
			}else{
				positionZ=(float)(-3.0f-((S.getZ()+1)*32.0f));
			}
			
			if(!S.getInactive()&&sphereInFrustum(positionX,positionY,positionZ,maxRadius*2)){
			
			//	if(S.getInvalidated()||S.getShadowRenderListID()==-1){//Should we re-generate this sprite's list?
				/*	int list=S.getShadowRenderListID();
					if(list==-1){
						list=(Integer)availableShadowLists.remove(0);
						S.setShadowRenderListID(list);
					}
					
					gl.glNewList(list,GL.GL_COMPILE);*/
			
					gl.glPushMatrix();								
								
					float xscale=(float)S.getWidth()/32.0f;
					float yscale=(float)S.getHeight()/32.0f;
					float zscale=(float)S.getDepth()/32.0f;
					int renderType=S.getRenderType();
						
					gl.glTranslatef((float)S.getX(),(float)S.getY(),-heightMod);
						
					gl.glMultMatrixd(mShadow,0);
									
					gl.glTranslatef((float)S.getWidth()/2.0f,(float)S.getHeight()/2.0f,positionZ+S.getZOffset()+heightMod);
																						
					if(S.getXRotation()!=0.0f)//Set the current rotation of this sprite.
						gl.glRotatef(S.getXRotation(),1.0f,0.0f,0.0f);
					if(S.getYRotation()!=0.0f)
						gl.glRotatef(S.getYRotation(),0.0f,1.0f,0.0f);
					if(S.getZRotation()!=0.0f)
						gl.glRotatef(S.getZRotation(),0.0f,0.0f,1.0f);
						
					gl.glScalef(xscale,yscale,zscale);//Scale the sprite.
						
					gl.glGetDoublev( GL.GL_MODELVIEW_MATRIX, tempModl,0 );
					double z=positionZ+S.getZOffset()+heightMod-S.getDepth()/2;
					double x=(float)S.getWidth()/2.0f;
					double y=(float)S.getHeight()/2.0f;
					
					x=x*mShadow[0]+y*mShadow[1]+z*mShadow[2];
					y=x*mShadow[4]+y*mShadow[5]+z*mShadow[6];
					z=x*mShadow[8]+y*mShadow[9]+z*mShadow[10];
					x=S.getX()-x;
					y=S.getY()-y;
					float heightMod2=MyTerrainHandler.getHeight((int)(x/16.0f),(int)(y/16.0f),(int)(x%16.0f),(int)(y%16.0f));
						
					if(depthCheck(gl,glu,positionX,positionY,-heightMod-3.0f)+depthCheck(gl,glu,(float)x,(float)y,-heightMod2-3.0f)<maxRadius){
																																													
						//Render the sprite differently based on the render type specified.
						if(renderType==Sprite.SPHERE){
							gl.glCallList(sphereLow);
						}else if(renderType==Sprite.CUBE){
							gl.glCallList(cube);
						}else if(renderType==Sprite.CONE){
							gl.glCallList(coneLow);
						}
						
					}
					
					gl.glPopMatrix();
				//	gl.glEndList();
				/*}
				gl.glCallList(S.getShadowRenderListID());*/
			}
			
		}
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_TEXTURE_2D);
	}
	
	/**
	Render all models no culling.
	*/
	
	/**
	Render the non-sprite models.
	*/
	public void renderShadowsNoCull(GL gl){

		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDisable(gl.GL_TEXTURE_2D);

		gl.glColor4f(0.0f,0.0f,0.0f,0.5f);
	
		for(int ii=0;ii<sprites[0].getIDArray().size();ii++){
			Sprite S=(Sprite)sprites[0].getIDArray().get(ii);
			if(S.getRenderType()==Sprite.FLAT)
				continue;
			
			int maxRadius=(int)Math.max(S.getWidth(),S.getHeight());
			maxRadius=(int)Math.max(maxRadius,S.getDepth());
			
			float positionX=(float)S.getX()+(float)S.getWidth()/2.0f;
			float positionY=(float)S.getY()+(float)S.getHeight()/2.0f;
			float positionZ;
			float heightMod=0.0f;
			positionZ=(float)(-3.0f-((S.getZ()+1)*32.0f));
			
			if(!S.getInactive()){
			
					gl.glPushMatrix();								
								
					float xscale=(float)S.getWidth()/32.0f;
					float yscale=(float)S.getHeight()/32.0f;
					float zscale=(float)S.getDepth()/32.0f;
					int renderType=S.getRenderType();
						
					gl.glTranslatef((float)S.getX(),(float)S.getY(),-heightMod);
						
					gl.glMultMatrixd(mShadow,0);
									
					gl.glTranslatef((float)S.getWidth()/2.0f,(float)S.getHeight()/2.0f,positionZ+S.getZOffset()+heightMod);
																						
					if(S.getXRotation()!=0.0f)//Set the current rotation of this sprite.
						gl.glRotatef(S.getXRotation(),1.0f,0.0f,0.0f);
					if(S.getYRotation()!=0.0f)
						gl.glRotatef(S.getYRotation(),0.0f,1.0f,0.0f);
					if(S.getZRotation()!=0.0f)
						gl.glRotatef(S.getZRotation(),0.0f,0.0f,1.0f);
						
					gl.glScalef(xscale,yscale,zscale);//Scale the sprite.
						
					gl.glGetDoublev( GL.GL_MODELVIEW_MATRIX, tempModl,0 );
					double z=positionZ+S.getZOffset()+heightMod-S.getDepth()/2;
					double x=(float)S.getWidth()/2.0f;
					double y=(float)S.getHeight()/2.0f;
					
					x=x*mShadow[0]+y*mShadow[1]+z*mShadow[2];
					y=x*mShadow[4]+y*mShadow[5]+z*mShadow[6];
					z=x*mShadow[8]+y*mShadow[9]+z*mShadow[10];
					x=S.getX()-x;
					y=S.getY()-y;
					float heightMod2=MyTerrainHandler.getHeight((int)(x/16.0f),(int)(y/16.0f),(int)(x%16.0f),(int)(y%16.0f));
						
																																												
					//Render the sprite differently based on the render type specified.
					if(renderType==Sprite.SPHERE){
						gl.glCallList(sphereLow);
					}else if(renderType==Sprite.CUBE){
						gl.glCallList(cube);
					}else if(renderType==Sprite.CONE){
						gl.glCallList(coneLow);
					}
						
					
					gl.glPopMatrix();			
			}
			
		}
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_TEXTURE_2D);
	}
	
	
	/**
	Render the non-sprite models.
	*/
	public void renderModels(GL gl){
	
		for(int ii=0;ii<sprites[0].getIDArray().size();ii++){
			Sprite S=(Sprite)sprites[0].getIDArray().get(ii);
						
			S.setDrawn(false);
			
			int maxRadius=(int)Math.max(S.getWidth(),S.getHeight());
			maxRadius=(int)Math.max(maxRadius,S.getDepth());
			
			float positionX=(float)S.getX()+(float)S.getWidth()/2.0f;
			float positionY=(float)S.getY()+(float)S.getHeight()/2.0f;
			float positionZ;
			if(!S.ignoreTerrain()){//Should we ignore the terrain when rendering this sprite on the Z?
				float heightMod=MyTerrainHandler.getHeight((int)(positionX/16.0f),(int)(positionY/16.0f),(int)(positionX%16.0f),(int)(positionY%16.0f));
				positionZ=(float)(-3.0f-((S.getZ()+1)*32.0f))-heightMod;
			}else{
				positionZ=(float)(-3.0f-((S.getZ()+1)*32.0f));
			}
			
			if(!S.getInactive()&&sphereInFrustum(positionX,positionY,positionZ,maxRadius*2)){
			
				S.setDrawn(true);
				int renderType=S.getRenderType();
				
				if(S.getInvalidated()||S.getRenderListID()==-1){//Should we re-generate this sprite's list?
				
					if(renderType!=Sprite.SPRITE_3D){//Sprite is in a constantly moving state no need for render list.
						int list=S.getRenderListID();
						if(list==-1){
							list=(Integer)availableSpriteLists.remove(0);
							S.setRenderListID(list);
						}
						gl.glNewList(list,GL.GL_COMPILE);

					}
										

					Texture T=MyImageHandler.getSpriteImage(S.getImageID(),S.getFrame());
					gl.glPushMatrix();
					T.bind();
								
					float xscale=(float)S.getWidth()/32.0f;
					float yscale=(float)S.getHeight()/32.0f;
					float zscale=(float)S.getDepth()/32.0f;
					
					gl.glColor4f((float)S.getR()/256.0f,(float)S.getG()/256.0f,(float)S.getB()/256.0f,(float)S.getA()/256.0f);
											
					gl.glTranslatef(positionX,positionY,positionZ+S.getZOffset());
					
					if(S.getXRotation()!=0.0f)//Set the current rotation of this sprite.
						gl.glRotatef(S.getXRotation(),1.0f,0.0f,0.0f);
					if(S.getYRotation()!=0.0f)
						gl.glRotatef(S.getYRotation(),0.0f,1.0f,0.0f);
					if(S.getZRotation()!=0.0f)
						gl.glRotatef(S.getZRotation(),0.0f,0.0f,1.0f);
						
					gl.glScalef(xscale,yscale,zscale);//Scale the sprite.
				
					
					//Render the sprite differently based on the render type specified.
					if(renderType==Sprite.SPHERE){
						gl.glCallList(sphere);
					}else if(renderType==Sprite.CUBE){
						gl.glCallList(cube);
					}else if(renderType==Sprite.CONE){
						gl.glCallList(cone);
					}else if(renderType==Sprite.SPRITE_3D){
					/*	gl.glRotatef(90.0f,1.0f,0.0f,0.0f);
						HashMap BodyAnimation=MySpriteAnimator.getCurrentAnimation("WALK");
						MySpriteBody.render(gl,null,BodyAnimation,MySpriteFace);
						gl.glEnable(GL.GL_TEXTURE_2D);*/
					}
					
					gl.glPopMatrix();
				
					gl.glEndList();
					
					if(renderType!=Sprite.SPRITE_3D)
						S.setInvalidated(false);
				}
				if(renderType!=Sprite.SPRITE_3D)
					gl.glCallList(S.getRenderListID());
			}
			
			//Add the sprite and tiles into the collision handler.
			if(!S.getInactive()&&S.getDrawn()||S.getOffscreenProcessing()&&S.getRunning()==false){

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
	public void renderSprites(GL gl){
		
		if(renderWater)
			MyTerrainHandler.renderWater(gl);
		
		ArrayList IndexArray=applyPaintersAlgorithm(sprites[1].getIDArray());
	
		for(int ii=0;ii<IndexArray.size();ii++){
			int fetchIndex=(Integer)((Object[])IndexArray.get(ii))[1];
			
			Sprite S=(Sprite)sprites[1].getIDArray().get(fetchIndex);
			S.setDrawn(false);
						
			int maxRadius=(int)Math.max(S.getWidth(),S.getHeight());
			maxRadius=(int)Math.max(maxRadius,S.getDepth());
			
			float positionX=(float)S.getX()+(float)S.getWidth()/2.0f;
			float positionY=(float)S.getY()+(float)S.getHeight()/2.0f;
			float positionZ;
			if(!S.ignoreTerrain()){//Should we ignore the terrain when rendering this sprite on the Z?
				float heightMod=MyTerrainHandler.getHeight((int)(positionX/16.0f),(int)(positionY/16.0f),(int)(positionX%16.0f),(int)(positionY%16.0f));
				positionZ=(float)(-3.0f-((S.getZ()+1)*32.0f))-heightMod;
			}else{
				positionZ=(float)(-3.0f-((S.getZ()+1)*32.0f));
			}
			
			if(!S.getInactive()&&sphereInFrustum(positionX,positionY,positionZ,maxRadius)){
				S.setDrawn(true);
			
				if(S.getInvalidated()||S.getRenderListID()==-1){//Should we re-generate this sprite's list?
					int list=S.getRenderListID();
					if(list==-1){
						list=(Integer)availableSpriteLists.remove(0);
						S.setRenderListID(list);
					}
					
					gl.glNewList(list,GL.GL_COMPILE);
								
					Texture T=MyImageHandler.getSpriteImage(S.getImageID(),S.getFrame());
										
					gl.glPushMatrix();
					T.bind();
					
					float xscale=(float)S.getWidth()/32.0f;
					float yscale=(float)S.getHeight()/32.0f;
					float zscale=(float)S.getDepth()/32.0f;
					int renderType=S.getRenderType();
					
					gl.glColor4f((float)S.getR()/256.0f,(float)S.getG()/256.0f,(float)S.getB()/256.0f,(float)S.getA()/256.0f);
											
					gl.glTranslatef(positionX,positionY,positionZ+S.getZOffset());
					
					if(S.getXRotation()!=0.0f)//Set the current rotation of this sprite.
						gl.glRotatef(S.getXRotation(),1.0f,0.0f,0.0f);
					if(S.getYRotation()!=0.0f)
						gl.glRotatef(S.getYRotation(),0.0f,1.0f,0.0f);
					if(S.getZRotation()!=0.0f)
						gl.glRotatef(S.getZRotation(),0.0f,0.0f,1.0f);
						
					gl.glScalef(xscale,yscale,zscale);//Scale the sprite.
					
					//Render the sprite differently based on the render type specified.
					float heightTex=(float)S.getHeight()/32.0f;
					float widthTex=(float)S.getWidth()/32.0f;
					
					if(!S.repeatTexture()){//How should this texture be rendered.
						heightTex=1.0f;
						widthTex=1.0f;
					}else{
						gl.glTexParameterf( gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_S, gl.GL_REPEAT );
						gl.glTexParameterf( gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_T, gl.GL_REPEAT );
					}
										
					gl.glNormal3f(0.0f,0.0f,-1.0f);
					gl.glBegin(GL.GL_POLYGON);
					gl.glTexCoord2f(0.0f,0.0f);
					gl.glVertex3f(-16.0f,-16.0f,0.0f);
					gl.glTexCoord2f(0.0f,heightTex);
					gl.glVertex3f(-16.0f,16.0f,0.0f);
					gl.glTexCoord2f(widthTex,heightTex);
					gl.glVertex3f(16.0f,16.0f,0.0f);
					gl.glTexCoord2f(widthTex,0.0f);
					gl.glVertex3f(16.0f, -16.0f,0.0f);
					gl.glEnd();
					
					gl.glPopMatrix();
					gl.glEndList();
				}
				gl.glCallList(S.getRenderListID());
			}
			
			//Add the sprite and tiles into the collision handler.
			if(!S.getInactive()&&S.getDrawn()||S.getOffscreenProcessing()&&S.getRunning()==false){

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
	Draw Text in world coordinates rather than screen coordinates.
	*/
	public void drawTextWorld(GL gl,GLUT glut,Object O[]){
		String text = (String)O[1];
		String face = (String)O[2];
		int size = (int)(Integer)O[3];
		float x = (float)(int)(Integer)O[4];				
		float y = (float)(int)(Integer)O[5];				
		float r = (float)(int)(Integer)O[6]/256.0f;
		float g = (float)(int)(Integer)O[7]/256.0f;
		float b = (float)(int)(Integer)O[8]/256.0f;
		float a = (float)(int)(Integer)O[9]/256.0f;
		float z = (float)(int)(Integer)O[10];
																										
		gl.glPushMatrix();
		gl.glColor4f(r,g,b,a);
		gl.glTranslatef((float)x,(float)y,(float)z);
		
		float scale=size/200.0f;
		gl.glScalef(scale,scale,scale);
		
		gl.glRotatef(180.0f-rotateX,1.0f,0.0f,0.0f);
		gl.glRotatef(rotateY,0.0f,1.0f,0.0f);
		if(rotateZ!=0.0f)
			gl.glRotatef(rotateZ,0.0f,0.0f,1.0f);
		
		glut.glutStrokeString(glut.STROKE_ROMAN,text);
		gl.glPopMatrix();
	}
	
	/**
	Draw a rectangle in world coordinates rather than screen coordinates.
	*/
	public void drawRectangleWorld(GL gl,GLUT glut,Object O[]){
		float x = (float)(int)(Integer)O[1];
		float y = (float)(int)(Integer)O[2];
		float width = (float)(int)(Integer)O[3];
		float height = (float)(int)(Integer)O[4];
		float r = (float)(int)(Integer)O[5]/256.0f;
		float g = (float)(int)(Integer)O[6]/256.0f;
		float b = (float)(int)(Integer)O[7]/256.0f;
		float a = (float)(int)(Integer)O[8]/256.0f;
		float z = (float)(int)(Integer)O[9];
		
		gl.glPushMatrix();
		
		gl.glTranslatef(x,y,z);

		if(rotateX!=0.0f)
			gl.glRotatef(180.0f-rotateX,1.0f,0.0f,0.0f);
		if(rotateY!=0.0f)
			gl.glRotatef(rotateY,0.0f,1.0f,0.0f);
		if(rotateZ!=0.0f)
			gl.glRotatef(rotateZ,0.0f,0.0f,1.0f);
			
		gl.glBegin(GL.GL_LINES);
		gl.glColor4f(r,g,b,a);
		gl.glVertex3f(-width/2,-height/2,0.0f);
		gl.glVertex3f(-width/2,height/2,0.0f);
		gl.glVertex3f(-width/2,-height/2,0.0f);
		gl.glVertex3f(width/2,-height/2,0.0f);
		gl.glVertex3f(width/2,-height/2,0.0f);
		gl.glVertex3f(width/2,height/2,0.0f);
		gl.glVertex3f(-width/2,height/2,0.0f);
		gl.glVertex3f(width/2,height/2,0.0f);
		gl.glEnd();
		gl.glPopMatrix();
	}
	
	/**
	Draw a rectangle in world coordinates rather than screen coordinates.
	*/
	public void fillRectangleWorld(GL gl,GLUT glut,Object O[]){
		float x = (float)(int)(Integer)O[1];
		float y = (float)(int)(Integer)O[2];
		float width = (float)(int)(Integer)O[3];
		float height = (float)(int)(Integer)O[4];
		float r = (float)(int)(Integer)O[5]/256.0f;
		float g = (float)(int)(Integer)O[6]/256.0f;
		float b = (float)(int)(Integer)O[7]/256.0f;
		float a = (float)(int)(Integer)O[8]/256.0f;
		float z = (float)(int)(Integer)O[9];
		
		gl.glPushMatrix();
		gl.glTranslatef(x,y,z);

		if(rotateX!=0.0f)
			gl.glRotatef(180.0f-rotateX,1.0f,0.0f,0.0f);
		if(rotateY!=0.0f)
			gl.glRotatef(rotateY,0.0f,1.0f,0.0f);
		if(rotateZ!=0.0f)
			gl.glRotatef(rotateZ,0.0f,0.0f,1.0f);
			
		gl.glBegin(GL.GL_POLYGON);
		gl.glColor4f(r,g,b,a);
		gl.glVertex3f(0.0f,0.0f,0.0f);
		gl.glVertex3f(0.0f,height,0.0f);
		gl.glVertex3f(width,height,0.0f);
		gl.glVertex3f(width,0.0f,0.0f);
		gl.glEnd();
		gl.glPopMatrix();
	}
	
	/**
	Find the current tile and world position of the mouse pointer.
	*/
	public void findWorldMouse(GL gl,GLU glu,GLUT glut){
	
		FloatBuffer Pixel=FloatBuffer.allocate(1);
		gl.glReadPixels(mouseX,C.getHeight()-mouseY, 1, 1,GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, Pixel);
	
		glu.gluUnProject(mouseX,C.getHeight()-mouseY,(double)Pixel.get(0),modld,0,projd,0,view,0,(double[])obj,0);
		float x=(int)obj[0];
		float y=(int)obj[1];
		float z=(int)obj[2];
	
		mouseWorldX=(int)x;
		mouseWorldY=(int)y;
		mouseWorldZ=(int)(z*-1);
		
		mouseTileX=(int)((mouseWorldX)/32.0f);
		mouseTileY=(int)((mouseWorldY)/32.0f);
				
	}
	
	/**
	Get the actual mouse X, Y coordinates in screen coordinates.
	*/
	public int getMouseScreenX(){
		float xScale=288.0f/C.getWidth();
		return((int)(mouseX*xScale));
	}
	
	public int getMouseScreenY(){
		float yScale=256.0f/C.getHeight();
		return((int)(mouseY*yScale));
	}
	
	public int getMouseWorldX(){
		return(mouseWorldX);
	}
	
	public int getMouseWorldY(){
		return(mouseWorldY);
	}
	
	public int getMouseWorldZ(){
		return(mouseWorldZ);
	}
	
	public int getMouseTileX(){
		return(mouseTileX);
	}
	
	public int getMouseTileY(){
		return(mouseTileY);
	}
	
	public boolean getMouseDown(){
		return(mouseDown);
	}
	
	public boolean getMouseClicked(){
		return(mouseClicked);
	}
	
	public void resetMouseClicked(){
		this.mouseClicked=false;
	}
	
	/**
	Get whether or not this is currently initialized.
	*/
	public boolean getInitialized(){
		return(initialized);
	}
	
	/**
	Return IDs.
	*/
	public void returnListIDs(Sprite S){
		try{
			available.acquire();
			availableShadowLists.add(S.getShadowRenderListID());
			availableSpriteLists.add(S.getRenderListID());
			available.release();
		}catch(Exception e){
			available.release();
		}
	}
				
	/**
	Update the current screen.
	*/
	int counter=0;
	public synchronized void updateScreen(GL gl,GLUT glut,GLU glu,int parentWidth,int parentHeight,GLAutoDrawable drawable){
		
		boolean shouldResetMouse=false;//Should we reset the click state of the mouse?
		if(mouseClicked)
			shouldResetMouse=true;
		
		try{
			counter++;//Used for various modulus based re-calculations
					
			if(!initialized){
				return;
			}
				
			this.gl=gl;
								
			//PRE-GENERATE SOME PRIMITIVE SHAPES THAT CAN BE USED AS PRIMITIVE DRAWIG OBJECTS.
			if(sphere==-1||generateLists){
				generateLists(gl,glu,glut);
				generateLists=false;
				invalidateSprites();
				if(spriteLists==-1){
					spriteLists=gl.glGenLists(MAX_SPRITE*2);	
					currentSpriteList=spriteLists;
					currentShadowList=spriteLists+MAX_SPRITE;
					availableSpriteLists.clear();
					availableShadowLists.clear();
					for(int i=0;i<MAX_SPRITE;i++){//Reset the available indexes.
						availableSpriteLists.add(new Integer(spriteLists+i));
						availableShadowLists.add(new Integer(spriteLists+MAX_SPRITE+i));
					}

				}else{
					//gl.glDeleteLists(spriteLists,MAX_SPRITE*2);
					spriteLists=gl.glGenLists(MAX_SPRITE*2);	
					availableSpriteLists.clear();
					availableShadowLists.clear();
					for(int i=0;i<MAX_SPRITE;i++){//Reset the avilable indexes.
						availableSpriteLists.add(new Integer(spriteLists+i));
						availableShadowLists.add(new Integer(spriteLists+MAX_SPRITE+i));
					}
					
					currentSpriteList=spriteLists;
					currentShadowList=spriteLists+MAX_SPRITE;
				}
			}
			
		/*	if(MySpriteFace==null){//Intialize Sprite related stuff.
				MySpriteFace=new SpriteFace();
				MySpriteFace.load("abstract_face.christ");
				MySpriteBody=new SpriteBody();
				MySpriteBody.load("images/abstract_face.christ");
				MySpriteBody.buildBuffers(gl);
				MySpriteAnimator=new SpriteAnimator();
				MySpriteAnimator.load("");
			}*/
			
									
			//RE-CALCULATE THE SCREEN RELATIVE SCREEN SIZE.
			if(viewChanged){
				gl.glDisable(GL.GL_TEXTURE_2D);
				gl.glColor4f(1.0f,1.0f,1.0f,0.0f);
				gl.glPushMatrix();
				gl.glTranslatef(-10000.0f,-10000.0f,0.0f);
				gl.glBegin(GL.GL_POLYGON);
				gl.glVertex3f(0.0f,0.0f,0.0f);
				gl.glVertex3f(0.0f,20000.0f,0.0f);
				gl.glVertex3f(20000.0f, 20000.0f,0.0f);
				gl.glVertex3f(20000.0f, 0.0f,0.0f);
				gl.glEnd();
				gl.glColor4f(1.0f,1.0f,1.0f,1.0f);
				gl.glPopMatrix();
				gl.glEnable(GL.GL_TEXTURE_2D);
				extractFrustum(gl,glu,parentWidth,parentHeight);
				gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
			}
			
			available2.acquire();
			work++;
			Work.add(new int[]{-1,Maps[currentMap].getScriptID(),1});
			long currentTime=Time.getInstance().getCurrentTime();
			available2.release();
			
			//For scrolling movement.
			if(endXMove-currentTime<=0)
				speedX=0;
			if(endYMove-currentTime<=0)
				speedY=0;

			setViewX(viewX+speedX);
			setViewY(viewY+speedY);	
				
			//Draw the level's map.
			for(int x=0;x<5;x++){
				for(int y=0;y<3;y++){				
					if(sphereInFrustum((float)(x*288.0f+144.0f),(float)(y*256.0f+128.0f),0.0f,288.0f)){
						gl.glCallList(map[x+y*5]);
					}
				}
			}
			
			//Render the sky.
			if(skyType!=0){
				Texture Sky=MyImageHandler.getStockTexture(skyImage);
				gl.glPushMatrix();//Our sky texture.
					gl.glDisable(GL.GL_LIGHTING);
					Sky.bind();
					gl.glTranslatef(720.0f,384.0f,0.0f);
					gl.glScalef(60.0f,60.0f,60.0f);
					currentSkyZ+=rotateSkyZ;
					gl.glRotatef(currentSkyZ,0.0f,0.0f,1.0f);
					gl.glTranslatef(0.0f,0.0f,-16.0f);
					gl.glCallList(sphere);
					gl.glEnable(GL.GL_LIGHTING);
				gl.glPopMatrix();
			}
			

			//Draw Sprites Here.
			//if(renderShadows)		
			//	renderModelShadows(gl,glu);
			
			renderModels(gl);
			renderSprites(gl);
			
			//Find the current tile and world position of the mouse.
			if(counter%5==0)
				findWorldMouse(gl,glu,glut);
			MyCollisionHandler.insert(new Float(mouseTileX+mouseTileY*45));
			
			available2.acquire();

			for(int i=0;i<2;i++){//Add the sprites into the processing array.
				for(int ii=0;ii<sprites[i].getIDArray().size();ii++){
					Sprite S=(Sprite)sprites[i].getIDArray().get(ii);
					
					if((S.getDrawn()||S.getOffscreenProcessing()||!S.getInitialized())&&!S.getInactive()){
						S.setInitialized(true);
						work++;
						
						if(S.getRunning()==false&&S.getScriptID()!=-1)
							Work.add(new int[]{S.getSpriteID(),S.getScriptID(),0});
					}
				}
			}
			
			available2.release();
						
			MyCollisionHandler.reset();
			MyParticleEngine.render(gl,MyImageHandler);
						
			//Wait on all the threads performing actions to finish.
			int breakCount=0;
			while(work>1){//We wait on the work to finish.
				breakCount++;
				Thread.sleep(1);
				if(breakCount>20){	
					work=0;
					System.out.println("Had to break.");
					break;
				}
			}
				
			//THROUGH A LOT OF TRIAL AND ERROR THESE TRANSLATIONS GIVE US A SCREEN IN THE FOREGROUND TO PERFORM 2D DRAWING ON.			
			Object[] obj = graphics.toArray();
			if(obj.length>0){//Should we do any 2D rendering?
			
				gl.glDisable(gl.GL_LIGHTING);
				gl.glDisable(gl.GL_TEXTURE_2D);
							
				gl.glDisable(gl.GL_DEPTH_TEST);
				for(int i=0;i<obj.length;i++){//Check for any world based drawing first.
					Object[] O = (Object[])obj[i];
					int type = (int)(Integer)O[0];
					if(type==TEXT_WORLD){
						drawTextWorld(gl,glut,O);
					}else
					
					if(type==RECTANGLE_WORLD){
						drawRectangleWorld(gl,glut,O);
					}else
					
					if(type==FILL_RECTANGLE_WORLD){
						fillRectangleWorld(gl,glut,O);
					}
				}
				gl.glEnable(gl.GL_DEPTH_TEST);
				
				
				//Switch to an ortho projection.
				gl.glMatrixMode(gl.GL_PROJECTION);
				gl.glLoadIdentity();
				glu.gluOrtho2D(0.0f,288.0f,256.0f,0.0f);
				gl.glMatrixMode(gl.GL_MODELVIEW);
				gl.glLoadIdentity();
				
				float fix=0.0f;
				for(int i=0;i<obj.length;i++){
						fix+=0.01f;

						Object[] O = (Object[])obj[i];
						int type = (int)(Integer)O[0];
						if(type==TEXT){
								String text = (String)O[1];
								String face = (String)O[2];
								int size = (int)(Integer)O[3];
								float x = (float)(int)(Integer)O[4];                                
								float y = (float)(int)(Integer)O[5];                                
								float r = (float)(int)(Integer)O[6]/256.0f;
								float g = (float)(int)(Integer)O[7]/256.0f;
								float b = (float)(int)(Integer)O[8]/256.0f;
								float a = (float)(int)(Integer)O[9]/256.0f;
																																																								
								gl.glPushMatrix();
								gl.glColor4f(r,g,b,a);
								gl.glTranslatef((float)x,(float)y,fix);
								gl.glRotatef(180.0f,0.0f,0.0f,1.0f);
								gl.glRotatef(180.0f,0.0f,1.0f,0.0f);

								float scale=0.007f*size;
								gl.glScalef(scale,scale,scale);
																
								glut.glutStrokeString(glut.STROKE_ROMAN,text);
								gl.glPopMatrix();
						}
						else if(type==RECTANGLE){
								float x = (float)(int)(Integer)O[1];
								float y = (float)(int)(Integer)O[2];
								float width = (float)(int)(Integer)O[3];
								float height = (float)(int)(Integer)O[4];
								float r = (float)(int)(Integer)O[5]/256.0f;
								float g = (float)(int)(Integer)O[6]/256.0f;
								float b = (float)(int)(Integer)O[7]/256.0f;
								float a = (float)(int)(Integer)O[8]/256.0f;
																
								gl.glPushMatrix();
								gl.glBegin(GL.GL_LINES);
								gl.glColor4f(r,g,b,a);
								gl.glVertex3f(x,y,fix);
								gl.glVertex3f(x,height+y,fix);
								
								gl.glVertex3f(x,y,fix);
								gl.glVertex3f(x+width,y,fix);
								
								gl.glVertex3f(x+width,y,fix);
								gl.glVertex3f(x+width,y+height,fix);
								
								gl.glVertex3f(x,y+height,fix);
								gl.glVertex3f(x+width,y+height,fix);
								gl.glEnd();
								gl.glPopMatrix();
						}
						else if(type==FILL_RECTANGLE){
								float x = (float)(int)(Integer)O[1];
								float y = (float)(int)(Integer)O[2];
								float width = (float)(int)(Integer)O[3];
								float height = (float)(int)(Integer)O[4];
								float r = (float)(int)(Integer)O[5]/256.0f;
								float g = (float)(int)(Integer)O[6]/256.0f;
								float b = (float)(int)(Integer)O[7]/256.0f;
								float a = (float)(int)(Integer)O[8]/256.0f;
																								
								gl.glPushMatrix();
								gl.glBegin(GL.GL_POLYGON);
								gl.glColor4f(r,g,b,a);
								gl.glVertex3f(x,y,fix);
								gl.glVertex3f(x,height+y,fix);
								gl.glVertex3f(width+x, height+y,fix);
								gl.glVertex3f(width+x, y,fix);
								gl.glEnd();
								gl.glPopMatrix();
						}

				}
			}//End of 2D rendering.
									
			//Switch back to a regular projection matrix.
			gl.glMatrixMode(gl.GL_PROJECTION);
			gl.glEnable(gl.GL_LIGHTING);
			gl.glEnable(gl.GL_TEXTURE_2D);
			gl.glLoadIdentity();
			glu.gluPerspective(45.0f, (float)parentWidth/(float)parentHeight, 1.0f, 50000.0f);
			
			//Apply the camera rotations.
			if(rotateX!=0.0f)
				gl.glRotatef(rotateX,1.0f,0.0f,0.0f);
			if(rotateY!=0.0f)
				gl.glRotatef(rotateY,0.0f,1.0f,0.0f);
			if(rotateZ!=0.0f)
				gl.glRotatef(rotateZ,0.0f,0.0f,1.0f);
			
			gl.glMatrixMode(gl.GL_MODELVIEW);
			gl.glColor4f(1.0f,1.0f,1.0f,1.0f);
				
			graphics.clear();

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
			available.release();
			available2.release();
			e.printStackTrace();
		}
		
		if(shouldResetMouse)
			mouseClicked=false;//Reset the mouse.
			
		//Reload terrain if need be.
		if(REQUEST_LOAD_TERRAIN){
			REQUEST_LOAD_TERRAIN=false;
			drawable.getContext().release();
			MyTerrainHandler.generateShadows(drawable,glu);
			drawable.getContext().makeCurrent();
			if(loadTerrain!=-1)
				MyTerrainHandler.setHeightMapImage(loadTerrain);
			else 
				regenerateLists();
		}

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
					
				Texture Temp=TextureLoader.load(image);
					
				MyImageHandler.setTile(id,Temp);
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
						
					Texture Temp=TextureLoader.load(image);
						
					MyImageHandler.setSpriteImage(id,ii,Temp);
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
				Maps[id]=new Map(scriptID,mapTiles);
				i++;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("Done Loading, Initializing Map now");
		initializeMap();
		
		}catch(Exception e2){
			e2.printStackTrace();
		}
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
	
	/**
	Mouse events.
	*/
	public void mouseDragged(MouseEvent e){
		mouseX=(int)(e.getX());
		mouseY=(int)(e.getY());
	}
	
	public void mouseMoved(MouseEvent e){
		mouseX=(int)(e.getX());
		mouseY=(int)(e.getY());
	}
	
	public void mouseClicked(MouseEvent e){
		mouseClicked=true;
	}
	
	public void mouseEntered(MouseEvent e){
	
	}

	public void mouseExited(MouseEvent e){
	
	}

	public void mousePressed(MouseEvent e){
		mouseDown=true;
	}
	
	public void mouseReleased(MouseEvent e){
		mouseDown=false;
	}
}
