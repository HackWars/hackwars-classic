package Game.MMO;
import com.plink.dolphinnet.*;
import java.util.*;
import java.io.*;
import java.math.*;
/**
This provides information about the state of sprites in a Hacktendo game.
*/
public class HacktendoPacket extends Assignment implements Serializable{
	private ArrayList SpriteEvents=new ArrayList();//This array list keeps track of the events inacted on sprites since the last update.
	private ArrayList SpriteReference=null; // Takes the form Object[]{ID,IP,Name,NPC}
	private int currentEvent=0;
	
	/**
	Read the next event in this packet.
	*/
	public int next(){
		if(currentEvent==SpriteEvents.size()-1){
			return(-1);
		}
		currentEvent++;
		return(currentEvent);
	}
	
	/**
	Add reference information to a given ID.
	*/
	public void addSpriteReference(int id,String ip,String Name,boolean NPC,int bodyID){
		if(SpriteReference==null)
			SpriteReference=new ArrayList();
		SpriteReference.add(new Object[]{new Integer(id),ip,Name,new Boolean(NPC),new Integer(bodyID)});
	}
	
	/**
	Get the Sprite Events array.
	*/
	public ArrayList getSpriteEvents(){
		return(SpriteEvents);
	}
	
	/**
	Get the reference array.
	*/
	public ArrayList getReferenceArray(){
		return(SpriteReference);
	}

	/**
	Add the sprite update to the packet as one big list of variables (these are bit-shifted into very few data points.
	*/
	public void addData(int x,int y,int z,   int targetX,int targetY,boolean explode,boolean destroy,int image,int frame,    int model,int rotateX,int rotateY,int rotateZ,  int width,int height,int depth,  int zOffset,int scriptID,int id){
		//Pack the first integer.
		int a=0;
		if(x>2047)
			x=2047;
		if(y>1023)
			y=1023;
		if(z>2047)
			z=2047;
		a=a|(x<<21);
		a=a|(y<<11);
		a=a|z;
		
		//Pack the second integer.
		int b=0;
		int iexplode=0;
		int idestroy=0;
		if(explode)
			iexplode=1;
		if(destroy)
			idestroy=1;
		
		if(targetX>2047)
			targetX=2047;
		if(targetX<0)
			targetX=0;
			
		if(targetY>1023)
			targetY=1023;
		if(targetY<0)
			targetY=0;
									
		if(image>31)
			image=31;
		if(frame>7)
			frame=7;
		
		b=b|(targetX<<21);
		b=b|(targetY<<11);
		b=b|(iexplode<<10);
		b=b|(idestroy<<9);
		b=b|(image<<3);
		b=b|(frame);
		
		//Pack the third integer.
		int c=0;
		if(model>31)
			model=31;
		while(rotateX<0)
			rotateX+=360;
		while(rotateX>360)
			rotateX-=360;
		while(rotateY<0)
			rotateY+=360;
		while(rotateY>360)
			rotateY-=360;
		while(rotateZ<0)
			rotateZ+=360;
		while(rotateZ>360)
			rotateZ-=360;
		
		c=c|(model<<27);
		c=c|(rotateX<<18);
		c=c|(rotateY<<9);
		c=c|(rotateZ);
										
		//Pack the fourth integer.
		int d=0;
		if(width>2047)
			width=2047;
		if(height>1023)
			height=1023;
		if(depth>2047)
			depth=2047;
		d=d|(width<<21);
		d=d|(height<<11);
		d=d|depth;
		
		//Pack the fifth integer.
		int e=0;
		if(zOffset>63)
			zOffset=63;
		if(scriptID>255)
			scriptID=255;
		if(id>262143)
			id=262143;
			
		e=e|(zOffset<<26);
		e=e|(scriptID<<18);
		e=e|id;
		
		int data[]=new int[]{a,b,c,d,e};
		SpriteEvents.add(data);
	}
	
	/**
	Get the x position of the sprite.
	*/
	public int getX(){	
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int a=data[0];
		a=a>>21;
		if(a<0)
			a=2048+a;
		return(a);
	}
	
	/**
	Get the y position of the sprite.
	*/
	public int getY(){
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int a=data[0];
		a=a<<11;
		a=a>>22;
		if(a<0)
			a=1024+a;
		return(a);
	}
	
	/**
	Get the z position of the sprite.
	*/
	public int getZ(){		
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int a=data[0];
		a=a<<21;
		a=a>>21;
		if(a<0)
			a=2048+a;
		return(a);
	}
	
	/**
	Get the target x position of the sprite.
	*/
	public int getTargetX(){
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int b=data[1];
		b=b>>21;
		if(b<0)
			b=2048+b;
		return(b);
	}
	
	/**
	Get the target y position of the sprite.
	*/
	public int getTargetY(){
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int b=data[1];
		b=b<<11;
		b=b>>22;
		if(b<0)
			b=1024+b;
		return(b);
	}
	
	/**
	Get whether or not the sprite indicated by this current event should explode.
	*/
	public boolean getExplodeSprite(){
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int b=data[1];
		b=b<<21;
		b=b>>31;
		if(b<0)
			b=1;
		if(b>0)
			return(true);
		return(false);
	}
	
	/**
	Get whether or not the sprite indicated by this current event should be destroyed.
	*/
	public boolean getDestroySprite(){
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int b=data[1];
		b=b<<22;
		b=b>>31;
		if(b<0)
			b=1;
		if(b>0)
			return(true);
		return(false);
	}
	
	
	/**
	Get the image that should be rendred for this sprite.
	*/
	public int getImage(){
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int b=data[1];
		b=b<<23;
		b=b>>26;
		if(b<0)
			b=32+b;
		return(b);
	}
	
	/**
	Get the frame that should be rendred for this sprite.
	*/
	public int getFrame(){
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int b=data[1];
		b=b<<28;
		b=b>>28;
		if(b<0)
			b=8+b;
		return(b);
	}
	
	/**
	Get the current model type that should be used.
	*/
	public int getRenderType(){
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int c=data[2];
		c=c>>27;
		if(c<0)
			c=32+c;
		return(c);
	}
	
	/**
	Get this model's X rotation.
	*/
	public int getXRotation(){
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int c=data[2];
		c=c<<5;
		c=c>>23;
		if(c<0)
			c=512+c;
		return(c);
	}
	
	/**
	Get this model's Y rotation.
	*/
	public int getYRotation(){
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int c=data[2];
		c=c<<14;
		c=c>>23;
		if(c<0)
			c=512+c;
		return(c);
	}
	
	/**
	Get this model's Z rotation.
	*/
	public int getZRotation(){
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int c=data[2];
		c=c<<23;
		c=c>>23;
		if(c<0)
			c=512+c;
		return(c);
	}
	
	/**
	Get width of the sprite.
	*/
	public int getWidth(){
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int a=data[3];
		a=a>>21;
		if(a<0)
			a=2048+a;
		return(a);
	}
	
	/**
	Get the height of the sprite.
	*/
	public int getHeight(){
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int a=data[3];
		a=a<<11;
		a=a>>22;
		if(a<0)
			a=1024+a;
		return(a);
	}
	
	/**
	Get the depth of the sprite.
	*/
	public int getDepth(){		
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int a=data[3];
		a=a<<21;
		a=a>>21;
		if(a<0)
			a=2048+a;
		return(a);
	}
	
	/**
	Get the depth of the sprite.
	*/
	public int getZOffset(){		
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int a=data[4];
		a=a>>26;
		return(a);
	}
	
	/**
	Get the script ID of the sprite.
	*/
	public int getScriptID(){		
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int a=data[4];
		a=a<<6;
		a=a>>24;
		if(a<0)
			a=256+a;
		return(a);
	}
	
	/**
	Get the ID of this sprite.
	*/
	public int getID(){		
		int data[]=(int[])SpriteEvents.get(currentEvent);
		int a=data[4];
		a=a<<14;
		a=a>>14;
		if(a<0)
			a=262144+a;
		return(a);
	}

	public HacktendoPacket(){
		super(0);
	}

	/** Run the assignments implemented task. */
	public Object execute(DataHandler DH){
		DH.addData(this);
		finish();
		return(null);
	}
	
	//Testing main.
/*	public static void main(String args[]){
		HacktendoPacket HP=new HacktendoPacket();
		HP.addData(20,30,40,    777,666,true,true,8,7,   31,350,-20,360,  2001,997,1982,  -9,128,86920);
		System.out.println("X: "+HP.getX()+" Y:"+HP.getY()+" Z:"+HP.getZ());
		System.out.println("Target X: "+HP.getTargetX()+" Target Y: "+HP.getTargetY()+" Explode Sprite:"+HP.getExplodeSprite()+" Destroy Sprite:"+HP.getDestroySprite()+" Sprite Image:"+HP.getImage()+" Sprite Frame:"+HP.getFrame());
		System.out.println("Render Type: "+HP.getRenderType()+" X Rotation: "+HP.getXRotation()+" Y Rotation:"+HP.getYRotation()+" Z Rotation: "+HP.getZRotation());
		System.out.println("Width: "+HP.getWidth()+" Depth: "+HP.getDepth()+"Height:"+HP.getHeight());
		System.out.println("ZOffset: "+HP.getZOffset()+" ID: "+HP.getID()+" Script ID:"+HP.getScriptID());
	}*/
}
