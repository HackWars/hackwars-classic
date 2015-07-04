package Assignments;

/**
PacketPort.java

The representation of a port that will be sent along in a packet.
*/

import java.util.*;
import java.io.*;
public class PacketWatch implements Serializable{
	private int type=0;
	private boolean on=false;
	private String note="";
	private float cpuCost=0.0f;
	private int searchFireWall=0;
	private float quantity=0.0f;
	private int port=0;
	private int ObservedPorts[]=null;
	
	/**
	Set the port that this is installed on.
	*/
	public void setPort(int port){
		this.port=port;
	}
	
	/**
	Get the port associated with this watch.
	*/
	public int getPort(){
		return(port);
	}
	
	/**
	Set the type of watch that this is.
	*/
	public void setType(int type){
		this.type=type;
	}
	
	/**
	Get the type of watch that this is.
	*/
	public int getType(){
		return(type);
	}
	
	/**
	Set whether this port is on or off.
	*/
	public void setOn(boolean on){
		this.on=on;
	}
	
	/**
	Get whether this port is on or off.
	*/
	public boolean getOn(){
		return(on);
	}
	
	/**
	Set the note associated with this watch.
	*/
	public void setNote(String note){
		this.note=note;
	}
	
	/**
	Get the note associated with this watch.
	*/
	public String getNote(){
		return(note);
	}
	
	/**
	Get the CPU cost associated with this watch.
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
	Set the fire wall that should be searched for when switching a fire-wall.
	*/
	public void setSearchFireWall(int searchFireWall){
		this.searchFireWall=searchFireWall;
	}
	
	/**
	Get the fire wall that should be searched for when switching on a firewall.
	*/
	public int getSearchFireWall(){
		return(searchFireWall);
	}
	
	/**
	Return the quantity value which is used differently depending on the fire wall type.
	*/
	public float getQuantity(){
		return(quantity);
	}
	
	/**
	Set the quantity.
	*/
	public void setQuantity(float quantity){
		this.quantity=quantity;
	}
	
	/**
	Set the ports that should be checked for switching firewalls to etc.
	*/
	public void setObservedPorts(ArrayList ObservedPorts){
		this.ObservedPorts=new int[ObservedPorts.size()];
		for(int i=0;i<ObservedPorts.size();i++){
			this.ObservedPorts[i]=(Integer)ObservedPorts.get(i);
		}
	}
	
	/**
	Get the list of observed ports.
	*/
	public int[] getObservedPorts(){
		return(ObservedPorts);
	}
}
