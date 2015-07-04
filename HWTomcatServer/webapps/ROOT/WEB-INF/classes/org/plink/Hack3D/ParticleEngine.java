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
 
public class ParticleEngine{
	//Variables.
	private Particle Particles[]=new Particle[1024];
	private int currentParticle=0;

	/**
	This function take a sprite and based on its width, height, and depth divides
	it up into particles.
	*/
	public synchronized void explodeSprite(Sprite S,TerrainHandler MyTerrainHandler){
		int xStart=(int)(S.getX()+(float)S.getWidth()/2.0f);
		int yStart=(int)(S.getY()+(float)S.getHeight()/2.0f);
		int zStart;
		if(!S.ignoreTerrain()){//Should we ignore the terrain when rendering this sprite on the Z?
			float heightMod=MyTerrainHandler.getHeight((int)(xStart/16.0f),(int)(yStart/16.0f),(int)(xStart%16.0f),(int)(yStart%16.0f));
			zStart=(int)((-3.0f-((S.getZ()+1)*32.0f))-heightMod);
		}else{
			zStart=(int)(-3.0f-((S.getZ()+1)*32.0f));
		}
		zStart-=S.getZOffset();
	
		float textureStepWidth=1.0f/(S.getWidth()/4.0f);
		float textureStepHeight=1.0f/(S.getHeight()/4.0f);
	
		xStart-=S.getWidth()/2;
		yStart-=S.getHeight()/2;
		zStart-=S.getDepth()/2;

		
		for(int x=xStart;x<xStart+S.getWidth();x+=4){
			for(int y=yStart;y<yStart+S.getHeight();y+=4){
				for(int z=zStart;z<zStart+S.getDepth();z+=4){
				
					float xv=(float)(Math.random()*8.0f)-4.0f;
					float yv=(float)(Math.random()*8.0f)-4.0f;
					float zv=(float)(Math.random()*8.0f)-4.0f;
					float color=(float)Math.random();
					float uStart=(x-xStart)*textureStepWidth;
					float uStop=uStart+textureStepWidth;
					float vStart=(y-yStart)*textureStepHeight;
					float vStop=vStart+textureStepHeight;
										
					float zg=3.0f*color;
					
					if(Particles[currentParticle]==null){
						Particles[currentParticle]=new Particle((float)x,(float)y,(float)z,xv,yv,zv,0.0f,0.0f,zg,uStart,vStart,uStop,vStop,S.getImageID(),S.getFrame(),40,color);
					}else{
						Particles[currentParticle].update((float)x,(float)y,(float)z,xv,yv,zv,0.0f,0.0f,zg,uStart,vStart,uStop,vStop,S.getImageID(),S.getFrame(),40,color);
					}

					currentParticle+=1;
					currentParticle=currentParticle%1024;
				}
			}
		}
	}
	
	/**
	Render the current particles in the particle engine.
	*/
	public void render(GL gl,OpenGLImageHandler MyImageHandler){
		for(int i=0;i<1024;i++){
			if(Particles[i]==null)
				return;
			if(Particles[i].getAlive()){
				Particles[i].updateParticle(gl,MyImageHandler);
			}
		}
	}
	
}
 