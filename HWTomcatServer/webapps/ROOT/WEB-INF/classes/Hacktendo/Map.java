package Hacktendo;
/*
Porgrammer: Ben Coe/C-Dog.(2007)<br />

The Game Map Object
*/

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;

public class Map{
	private int scriptID;
	private byte tiles[]=new byte[1080];
	public Map(int scriptID,byte[] tiles){
		this.scriptID=scriptID;
		this.tiles=tiles;
	}
	
	public int getScriptID(){
		return(scriptID);
	}
	
	public void setScriptID(int scriptID){
		this.scriptID=scriptID;
	}
	
	public boolean isPassable(int index){
		byte t=(byte)tiles[index];
		int passability=(t&0x80)>>7;
		if(passability==1)
			return(false);
		return(true);
	}
	
	public void setID(int index,int tileID){
		byte t=(byte)tiles[index];
		int passability=(t&0x80)>>7;
		if(passability==1){
			t=(byte)(tileID|0x80);
		}
		tiles[index]=t;
	}
	
	public void setPassable(int index,boolean passable){
		byte t=(byte)tiles[index];
		t=(byte)(t&0x7F);
		if(!passable){
			t=(byte)(t|0x80);
		}
		tiles[index]=t;
	}
	
	public int getTile(int index){
		byte t=(byte)tiles[index];
		return((t&0x7F));
	}
	
	public void setTile(int index,int tile,boolean passable){
		tiles[index]=(byte)tile;
		setPassable(index,passable);
	}
	
	//Testing main.
	public static void main(String args[]){
		Map M=new Map(0,null);
		M.getTile(0);
	}
}
