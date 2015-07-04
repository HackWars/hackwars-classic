package Assignments;

/**
PacketPort.java

The representation of a port that will be sent along in a packet.
*/

import java.util.*;
import java.io.*;
public class PacketPort implements Serializable{

	//Types of Ports.
	public static final int BANKING=0;
	public static final int FTP=1;
	public static final int ATTACK=2;
	public static final int HTTP=3;
	public static final int SHIPPING=4;
    public static final int REDIRECT=4;

	private HashMap MyFireWall=null;
	private int type=0;
	private int number;
	private boolean dummy=false;
	private boolean on=false;
	private boolean attacking=false;
	private float cpuCost=0.0f;
	private float health=0.0f;
	private String note="";
	private int isDefault=-1;
	private float maxCPUCost=0.0f;
	
	/**
	Constructor.
	*/
	public PacketPort(){
	}
	
	/**
	Set the note associated with this port..
	*/
	public void setNote(String note){
		this.note=note;
	}
	
	/**
	Get the note associated with this port.
	*/
	public String getNote(){
		return(note);
	}
	
	/**
	Set the number associated with this port.
	*/
	public void setNumber(int number){
		this.number=number;
	}
	
	/**
	Get the number associated with this port.
	*/
	public int getNumber(){
		return(number);
	}
	
	/**
	getFireWall()
	returns the firewall installed on the port.
	*/
	public HashMap getFireWall(){
		return(this.MyFireWall);
	}
	
	/**
	setFireWall(FireWall MyFireWall)
	installs the firewall on the port.
	*/
	public void setFireWall(HashMap MyFireWall){
        this.MyFireWall=MyFireWall;
	}
	
	/**
	getType()
	gets the type for this port.
	*/
	public int getType(){
		return(this.type);
	}
	
	/**
	setType(int type)
	sets the type of the port.
	*/
	public void setType(int type){
		this.type=type;
	}
	
	/**
	getOn()
	Gets whether this port is currently on.
	*/
	public boolean getOn(){
		return(on);
	}
	
	/**
	setOn()
	Set whether this port is currently on.
	*/
	public void setOn(boolean on){
		this.on=on;
	}
	
	/**
	getAttacking()
	Gets whether this port is currently attacking.
	*/
	public boolean getAttacking(){
		return(attacking);
	}
	
	/**
	setOn()
	Set whether this port is currently on.
	*/
	public void setAttacking(boolean attacking){
		this.attacking=attacking;
	}
	
	/**
	Get the CPU cost associated with this port.
	*/
	public float getCPUCost(){
		return(cpuCost);
	}

	/**
	Set the CPU cost associated with this port.
	*/
	public void setCPUCost(float cpuCost){
		this.cpuCost=cpuCost;
	}
	
	/**
	Get the health of this port.
	*/
	public float getHealth(){
		return(health);
	}
	
	/**
	Set the current health of this port.
	*/
	public void setHealth(float health){
		this.health=health;
	}
	
	/**
	Set whether this port is a dummy port.
	*/
	public void setDummy(boolean dummy){
		this.dummy=dummy;
	}
	
	/**
	Get whether or not this is a dummy port.
	*/
	public boolean getDummy(){
		return(dummy);
	}
	
	/**
	Get whether this port is default for this class of ports.
	*/
	public int getDefault(){
		return(isDefault);
	}
	
	/**
	Set whether this port is the default for this class of ports.
	*/
	public void setDefault(int isDefault){
		this.isDefault=isDefault;
	}
	
	
	public void setMaxCPUCost(float maxCPUCost){
		this.maxCPUCost = maxCPUCost;
	}
	
	public float getMaxCPUCost(){
		return(maxCPUCost);
	}
	
	/**
	To make Computer.java better so when we add new port types (If there are more to add) then we don't need to modify that, just this.
	*/
	//* Ben, can you check this and see what you think?
	public int getParamIndex(){
		switch(type)
		{
			case BANKING:
				return 2;
				
			case FTP:
				return 4;
				
			case ATTACK:
				return 3;
			
			case HTTP:
				return 5;
				
			case SHIPPING:
				return 7;
				
    		default:
				return 0;
		}
	}
	//*/
}
