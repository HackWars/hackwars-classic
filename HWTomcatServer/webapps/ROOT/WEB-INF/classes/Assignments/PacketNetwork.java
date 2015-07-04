package Assignments;

/**
PacketNetwork.java

Network data.
*/

import java.util.*;
import java.io.*;
public class PacketNetwork implements Serializable{
	private String name="";
	private String storeIP="";
	private ArrayList regularNPCs=null;
	private ArrayList questNPCs=null;
	private ArrayList miningNPCs=null;
    private ArrayList storeNPCs=null;
	
	/**
	Getters/Setters.
	*/
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return(name);
	}
    
    public void setStoreNPCs(ArrayList storeNPCs) {
        this.storeNPCs=storeNPCs;
    }
    
    public ArrayList getStoreNPCs() {
        return(storeNPCs);
    }
	
	public void setStoreIP(String storeIP){
		this.storeIP=storeIP;
	}
	
	public String getStoreIP(){
		return(storeIP);
	}
	
	public void setAttackNPCs(ArrayList regularNPCs){
		this.regularNPCs=regularNPCs;
	}
	
	public void setQuestNPCs(ArrayList questNPCs){
		this.questNPCs=questNPCs;
	}
	
	public void setMiningNPCs(ArrayList miningNPCs){
		this.miningNPCs=miningNPCs;
	}
	
	public ArrayList getQuestNPCs(){
		return(questNPCs);
	}
	
	public ArrayList getMiningNPCs(){
		return(miningNPCs);
	}
	
	public ArrayList getRegularNPCs(){
		return(regularNPCs);
	}
}
