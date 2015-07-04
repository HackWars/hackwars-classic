package Hacktendo;
/*
Programmer: Ben Coe/C-Dog.(2007)<br />

Thread pool.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import Hackscript.Model.*;

public class ScriptThread implements Runnable{
	private static int threadCount=0;
	private int id=0;
	private final long sleepTime=2;
	private Thread MyThread=null;
	private boolean running=true;
	private RenderEngine RE=null;
	private HacktendoLinker HL=null;
	private int i=0;
	private long totaltime=0;
	public ScriptThread(RenderEngine RE){
		this.RE=RE;
		id=threadCount;
		threadCount++;
		HL=new HacktendoLinker(RE);
		MyThread=new Thread(this);
		MyThread.start();
	}
	
	public void destroy(){
		running=false;
		MyThread=null;
	}
	
	public void resetGlobals(){
		HL.resetGlobals();
	}
	
	public void run(){
		while(running){
		
			try{				
				Object O[]=RE.getWork();
				if(O!=null){
					int spriteID=(Integer)O[0];
					Model script=(Model)O[1];
					int scriptID=(Integer)O[2];
					
					Sprite S=null;
					try{ 

						HL.setSpriteID(spriteID);	
						S=RE.getSprite(spriteID);//Reset the collision handler for this sprite.
						script.execute(HL);

						if(S!=null)
							S.resetCollided();
					
					}catch(Exception e){	
										
						if(S!=null)
							S.resetCollided();
																
						if((e instanceof ModelError)&&(!e.getMessage().equals("Runtime error execution terminated"))){						
							String message="";
							message+="(Error in script "+scriptID+") ";
							message+=e.getMessage();
							RE.setDebugMessage(message);
						}
					}
				
					RE.returnWork();
				}

                MyThread.sleep(sleepTime);
            }catch(Exception e){
				e.printStackTrace();
            }
		}
	}
}
