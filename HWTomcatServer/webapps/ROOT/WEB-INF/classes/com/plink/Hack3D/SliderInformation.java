package com.plink.Hack3D;
/**
This tool is used to perform particle effects.
*/

import javax.media.opengl.*;
import com.sun.opengl.util.texture.*;
import com.sun.opengl.util.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import Hacktendo.*;
import javax.media.opengl.glu.*;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import java.util.*;
 
public class SliderInformation{
	//#GROUP ID	Name	XROT(name, min, max)	YROT	ZROT	scaleX	scaleY	scaleZ	scaleAll	XPOS	YPOS	ZPOS	
	private String name="";
	private HashMap xRot=null;
	private HashMap yRot=null;
	private HashMap zRot=null;
	private HashMap xScale=null;
	private HashMap yScale=null;
	private HashMap zScale=null;
	private HashMap scaleAll=null;
	private HashMap xPos=null;
	private HashMap yPos=null;
	private HashMap zPos=null;
	
	//Parsing step.
	public void parse(HashMap insertHere,String line){
		System.out.println("ATTEMPTING TO PARSE:");
		System.out.println(line);
		String data[]=line.split("\t");
		name=data[1];
		xRot=getHash(data[2]);
		yRot=getHash(data[3]);
		zRot=getHash(data[4]);
		xScale=getHash(data[5]);
		yScale=getHash(data[6]);
		zScale=getHash(data[7]);
		scaleAll=getHash(data[8]);
		xPos=getHash(data[9]);
		yPos=getHash(data[10]);
		zPos=getHash(data[11]);
				
		insertHere.put(name,this);
	}
	
	//perform a sub-parsing step on the data.
	private HashMap getHash(String line){
	
		if(line.equals("0"))
			return(null);
			
		HashMap returnMe=new HashMap();
		
		String data[]=line.split(",");
		
		returnMe.put("name",data[0]);
		returnMe.put("min",new Float(data[1]));
		returnMe.put("max",new Float(data[2]));
		
		return(returnMe);
	}
	
	//Getters.
	public String getName(){
		return(name);
	}
	
	public HashMap getXRotation(){
		return(xRot);
	}
	
	public HashMap getZRotation(){
		return(zRot);
	}
	
	public HashMap getYRotation(){
		return(yRot);
	}
	
	public HashMap getXScale(){
		return(xScale);
	}
	
	public HashMap getYScale(){
		return(yScale);
	}
	
	public HashMap getZScale(){
		return(zScale);
	}
	
	public HashMap getScaleAll(){
		return(scaleAll);
	}
	
	public HashMap getXPosition(){
		return(xPos);
	}
	
	public HashMap getYPosition(){
		return(yPos);
	}
	
	public HashMap getZPosition(){
		return(zPos);
	}
	
	//To string.
	public String toString(){
		String returnMe="";
		returnMe+="Name: "+name+"\n";
		/*
		returnMe+="XRot: "+xRot+"\n";
		returnMe+="YRot: "+yRot+"\n";
		returnMe+="ZRot: "+zRot+"\n";
		returnMe+="XScale: "+xScale+"\n";
		returnMe+="YScale: "+yScale+"\n";
		returnMe+="ZScale: "+zScale+"\n";
		returnMe+="XPos: "+xPos+"\n";
		returnMe+="YPos: "+yPos+"\n";
		returnMe+="ZPos: "+zPos+"\n";
		returnMe+="---------------------\n";
		*/
		return(returnMe);
	}
}