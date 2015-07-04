package Hacktendo;
/*
HackerLinker.java

Contains all the functions available to a player using the Hack Wars scriptting language.
 */

import java.util.ArrayList;
import Hackscript.Model.*;
import java.util.*;
import util.*;
import GUI.Sound;
import Hacktendo.Functions.*;

public class HacktendoLinker extends Linker{
    
    //the global variables
	public static final float PI_OVER_180=(float)(Math.PI/180.0);
	public static final float V180_OVER_PI=(float)(180.0/Math.PI);
    
    //the functions
    private HashMap functions = new HashMap();
	
	//globals.
	public static HashMap Globals=new HashMap();
	public static HashMap Timers=new HashMap();
	private RenderEngine RE=null;
	private int spriteID=-1;
    
    /** 
	Creates a new instance of structInfo. 
	*/
    public HacktendoLinker(RenderEngine RE) {
		this.RE=RE;
				
		functions.put("setXSpeed",new SetXSpeed(RE,this));
		functions.put("setySpeed",new SetYSpeed(RE,this));
		functions.put("deleteTimeStamp",new DeleteTimeStamp(RE,this));
		functions.put("checkTimeStamp",new CheckTimeStamp(RE,this));
		functions.put("createTimeStamp",new CreateTimeStamp(RE,this));
		functions.put("getX",new GetX(RE,this));
		functions.put("getY",new GetY(RE,this));
		functions.put("getZ",new GetZ(RE,this));
		functions.put("getFrame",new GetFrame(RE,this));
		functions.put("getImage",new GetImage(RE,this));
		functions.put("getScript",new GetScript(RE,this));
		functions.put("setX",new SetX(RE,this));
		functions.put("setY",new SetY(RE,this));
		functions.put("setZ",new SetZ(RE,this));
		functions.put("setFrame",new SetFrame(RE,this));
		functions.put("setImage",new SetImage(RE,this));
		functions.put("setScript",new SetScript(RE,this));
		functions.put("setWidth",new SetWidth(RE,this));
		functions.put("setHeight",new SetHeight(RE,this));
		functions.put("getWidth",new GetWidth(RE,this));
		functions.put("getHeight",new GetHeight(RE,this));
		functions.put("setCollisionType",new SetCollisionType(RE,this));
		functions.put("setGlobal",new SetGlobal(RE,this));
		functions.put("getGlobal",new GetGlobal(RE,this));
		functions.put("setLocal",new SetLocal(RE,this));
		functions.put("getLocal",new GetLocal(RE,this));
		functions.put("getKeyUpCount",new GetKeyUpCount(RE,this));
		functions.put("getKeyDownCount",new GetKeyDownCount(RE,this));
		functions.put("getNextKeyUp",new GetNextKeyUp(RE,this));
		functions.put("getNextKeyDown",new GetNextKeyDown(RE,this));
		functions.put("messageSprite",new MessageSprite(RE,this));
		functions.put("getMessageCount",new GetMessageCount(RE,this));
		functions.put("getCollidedCount",new GetCollidedCount(RE,this));
		functions.put("getFlag",new GetFlag(RE,this));
		functions.put("getMessage",new GetMessage(RE,this));
		functions.put("nextMessage",new NextMessage(RE,this));
		functions.put("createSprite",new CreateSprite(RE,this));
		functions.put("destroySprite",new DestroySprite(RE,this));
		functions.put("getID",new GetID(RE,this));
		functions.put("changeMap",new ChangeMap(RE,this));
		functions.put("setMusic",new SetMusic(RE,this));
		functions.put("playSound",new PlaySound(RE,this));
		functions.put("nextCollided",new NextCollided(RE,this));
		functions.put("collidedWith",new CollidedWith(RE,this));
		functions.put("collidedType",new CollidedType(RE,this));
		functions.put("save",new Save(RE,this));
		functions.put("load",new Load(RE,this));
		functions.put("getMapID",new GetMapID(RE,this));
		functions.put("triggerWatch",new TriggerWatch(RE,this));
		functions.put("isPassable",new IsPassable(RE,this));
		functions.put("setViewportZ",new SetViewportZ(RE,this));
		functions.put("getViewportZ",new GetViewportZ(RE,this));
		functions.put("setViewportX",new SetViewportX(RE,this));
		functions.put("getViewportX",new GetViewportX(RE,this));
		functions.put("setViewportY",new SetViewportY(RE,this));
		functions.put("getViewportY",new GetViewportY(RE,this));
		functions.put("drawRectangle",new DrawRectangle(RE,this));
		functions.put("fillRectangle",new FillRectangle(RE,this));
		functions.put("drawText",new DrawText(RE,this));
		functions.put("drawLine",new DrawLine(RE,this));
		functions.put("debug",new Debug(RE,this));
		functions.put("sqrt",new Sqrt(RE,this));
		functions.put("abs",new Abs(RE,this));
		functions.put("tan",new Tan(RE,this));
		functions.put("cos",new Cos(RE,this));
		functions.put("sin",new Sin(RE,this));
		functions.put("atan",new Atan(RE,this));
		functions.put("acos",new Acos(RE,this));
		functions.put("asin",new Asin(RE,this));
		functions.put("rand",new Rand(RE,this));
		functions.put("floor",new Floor(RE,this));
		functions.put("pow",new Pow(RE,this));
		functions.put("intValue",new IntValue(RE,this));
		functions.put("isGlobalSet",new IsGlobalSet(RE,this));
		functions.put("isLocalSet",new IsLocalSet(RE,this));
		functions.put("char",new Char(RE,this));
		functions.put("substr",new Substr(RE,this));
		functions.put("length",new Length(RE,this));
		functions.put("parseInt",new ParseInt(RE,this));
		functions.put("parseFloat",new ParseFloat(RE,this));
		functions.put("strlen",new Strlen(RE,this));
		functions.put("replaceAll",new ReplaceAll(RE,this));
		functions.put("indexOf",new IndexOf(RE,this));
		functions.put("setBeat",new setBeat(RE,this));
		
		//Fancy new 3D functions.
		functions.put("setViewportXRotation",new setViewportXRotation(RE,this));
		functions.put("getViewportXRotation",new getViewportXRotation(RE,this));
		functions.put("setViewportYRotation",new setViewportYRotation(RE,this));
		functions.put("getViewportYRotation",new getViewportYRotation(RE,this));
		functions.put("setViewportZRotation",new setViewportZRotation(RE,this));
		functions.put("getViewportZRotation",new getViewportZRotation(RE,this));
		functions.put("setRenderType",new setRenderType(RE,this));
		functions.put("getRenderType",new getRenderType(RE,this));
		functions.put("setXRotation",new setXRotation(RE,this));
		functions.put("getXRotation",new getXRotation(RE,this));
		functions.put("setYRotation",new setYRotation(RE,this));
		functions.put("getYRotation",new getYRotation(RE,this));
		functions.put("setZRotation",new setZRotation(RE,this));
		functions.put("getZRotation",new getZRotation(RE,this));
		functions.put("drawTextWorld",new DrawTextWorld(RE,this));
		functions.put("drawRectangleWorld",new DrawRectangleWorld(RE,this));
		functions.put("fillRectangleWorld",new FillRectangleWorld(RE,this));
		functions.put("setAutoCollide",new setAutoCollide(RE,this));
		functions.put("getAutoCollide",new getAutoCollide(RE,this));
		functions.put("setMask",new setMask(RE,this));
		functions.put("setDepth",new SetDepth(RE,this));
		
		//FUNCTIONS FOR SETTING MAP PARAMETERS.
		functions.put("getPassable",new getPassable(RE,this));
		functions.put("setPassable",new setPassable(RE,this));
		functions.put("getTile",new getTile(RE,this));
		functions.put("setTile",new setTile(RE,this));
		functions.put("setOffscreenProcessing",new setOffscreenProcessing(RE,this));
		functions.put("setZOffset",new setZOffset(RE,this));
		functions.put("getViewportWidth",new GetViewportWidth(RE,this));
		functions.put("getViewportHeight",new GetViewportHeight(RE,this));
		functions.put("setTerrain",new setTerrain(RE,this));
		functions.put("setIgnoreTerrain",new setIgnoreTerrain(RE,this));
		functions.put("setRepeatTexture",new setRepeatTexture(RE,this));

		functions.put("getTerrainHeight",new getTerrainHeight(RE,this));
		functions.put("getMouseScreenX",new getMouseScreenX(RE,this));
		functions.put("getMouseScreenY",new getMouseScreenY(RE,this));
		functions.put("getMouseWorldX",new getMouseWorldX(RE,this));
		functions.put("getMouseWorldY",new getMouseWorldY(RE,this));
		functions.put("getMouseWorldZ",new getMouseWorldZ(RE,this));
		functions.put("getMouseTileX",new getMouseTileX(RE,this));
		functions.put("getMouseTileY",new getMouseTileY(RE,this));
		functions.put("getMouseDown",new getMouseDown(RE,this));
		functions.put("getMouseClicked",new getMouseClicked(RE,this));
		functions.put("setCollideWithWater",new setCollideWithWater(RE,this));
		functions.put("setMaxStepUp",new setMaxStepUp(RE,this));
		functions.put("setMaxStepDown",new setMaxStepDown(RE,this));
		functions.put("setRenderWater",new setRenderWater(RE,this));
		functions.put("setRenderShadows",new setRenderShadows(RE,this));
		functions.put("setSky",new setSky(RE,this));
		functions.put("setWaterHeight",new setWaterHeight(RE,this));
		functions.put("explodeSprite",new explodeSprite(RE,this));
		
		//Special functions for networking.
		functions.put("activate",new activate(RE,this));
		functions.put("setTarget",new setTarget(RE,this));
		functions.put("invalidate",new invalidate(RE,this));
	}
	
	/**
	Reset the global timers and variables being used.
	*/
	public static void resetGlobals(){
		Globals.clear();
		Timers.clear();
	}
	
	/**
	Set the sprite ID of the current sprite's script that's running.
	*/
	public void setSpriteID(int id){
		this.spriteID=id;
	}
	/**
	Run a linker function.
	*/
    public Variable runFunction(String name, ArrayList parameters){
	
		Variable result=null;
		
		try{
			//System.out.println(name);
			LinkerFunctions LF = (LinkerFunctions)functions.get(name);
			if(LF!=null){
				Object O = LF.execute(parameters);
					
				if(O==null)
					return(new TypeInteger(0));
				else{
					if(O instanceof Variable)
						return((Variable)O);

					if(O instanceof Integer)
						return(new TypeInteger((int)(Integer)O));
					if(O instanceof Float)
						return(new TypeFloat((float)(Float)O));
					if(O instanceof Boolean)
						return(new TypeBoolean((boolean)(Boolean)O));
					if(O instanceof String)
						return(new TypeString((String)O));
					else
						return(null);
				}
				
			}
			else{
				System.out.println("Couldn't find function "+name);
			}
			
		}catch(Exception e){				
			if(e.getMessage()!=null)
			if(e.getMessage().equals("ChangeMap")){//force exception.
				killExecution();
			}else{
				e.printStackTrace();
			}
		}
		return(null);
	}
    
    public void addTimer(String key,Long time){
		Timers.put(key,time);    
    }
    
    public long checkTimer(String key){
	    return (Long)Timers.get(key);
    }
    
    public void removeTimer(String key){
	    Timers.remove(key);
    }
    
    public int getSpriteID(){
	    return spriteID;
    }
    
    public static void addGlobal(String key, Object value){
	    Globals.put(key,value);
    }
    
    public Object getGlobal(String key){
	    return Globals.get(key);
    }
}
