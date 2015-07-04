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
 
public class Particle{
	public static final float PARTICLE_SIZE=2.0f;

	private float x=0.0f;
	private float y=0.0f;
	private float z=0.0f;
	private float xv=0.0f;
	private float yv=0.0f;
	private float zv=0.0f;
	private float xg=0.0f;
	private float yg=0.0f;
	private float zg=0.30625f;
	private float uStart=0.0f;
	private float vStart=0.0f;
	private float uEnd=0.0f;
	private float vEnd=0.0f;
	private int imageID=0;
	private int imageFrame=0;
	private int life=0;
	private int maxLife=0;
	private int currentLife=0;
	private boolean alive=true;
	private float rotateX=0.0f;
	private float currentX=0.0f;
	private float rotateY=0.0f;
	private float currentY=0.0f;
	private float rotateZ=0.0f;
	private float currentZ=0.0f;
	private int heat=0;
	
		 	 
	/**
	Constructor.
	*/
	public Particle(float x,float y,float z,float xv,float yv,float zv,float xg,float yg,float zg,float uStart,float vStart,float uEnd,float vEnd,int imageID,int imageFrame,int life,float heat){
		rotateX=(float)(Math.random()*3.0f);
		rotateY=(float)(Math.random()*3.0f);
		rotateZ=(float)(Math.random()*3.0f);
		this.maxLife=life;
		this.x=x;
		this.y=y;
		this.z=z;
		this.xv=xv;
		this.yv=yv;
		this.zv=zv;
		this.xg=xg;
		this.yg=yg;
		this.zg=zg;
		this.uStart=uStart;
		this.vStart=vStart;
		this.uEnd=uEnd;
		this.vEnd=vEnd;
		this.imageID=imageID;
		this.imageFrame=imageFrame;
		this.life=life;
		this.r=heat;
		this.g=0.0f;
		this.b=0.0f;
	}
	
	/**
	Update the particle settings.
	*/
	public void update(float x,float y,float z,float xv,float yv,float zv,float xg,float yg,float zg,float uStart,float vStart,float uEnd,float vEnd,int imageID,int imageFrame,int life,float heat){
		rotateX=(float)(Math.random()*3.0f);
		rotateY=(float)(Math.random()*3.0f);
		rotateZ=(float)(Math.random()*3.0f);
		this.maxLife=life;
		currentLife=0;
		alive=true;
		this.x=x;
		this.y=y;
		this.z=z;
		this.xv=xv;
		this.yv=yv;
		this.zv=zv;
		this.xg=xg;
		this.yg=yg;
		this.zg=zg;
		this.uStart=uStart;
		this.vStart=vStart;
		this.uEnd=uEnd;
		this.vEnd=vEnd;
		this.imageID=imageID;
		this.imageFrame=imageFrame;
		this.life=life;
		this.r=heat;
		this.g=0.0f;
		this.b=0.0f;
	}
	
	/**
	Get whether or not the particle is alive.
	*/
	public boolean getAlive(){
		return(alive);
	}
	
	/**
	Updates the particle and renders it.
	*/
	private float r=1.0f;
	private float g=0.0f;
	private float b=0.0f;
	public void updateParticle(GL gl,OpenGLImageHandler MyImageHandler){
		currentLife++;
		currentX+=rotateX;
		currentY+=rotateY;
		currentZ+=rotateZ;
		
		if(currentLife>life){
			alive=false;
		}
		
		x+=xv;
		y+=yv;
		z+=zv;
		xv+=xg;
		yv+=yg;
		zv+=zg;
		
		r-=0.003125;//Vary the color.
		if(r<0)
			r=0;
		
		if(alive){
			gl.glPushMatrix();
			Texture T=MyImageHandler.getSpriteImage(imageID,imageFrame);
			T.bind();
			
			gl.glColor4f(1.0f,0.7f,0.7f,1.0f-((float)currentLife/(float)maxLife));
			gl.glTranslatef(x,y,z);
			
			gl.glRotatef(currentX,1.0f,0.0f,0.0f);
			gl.glRotatef(currentY,0.0f,1.0f,0.0f);
			gl.glRotatef(currentZ,0.0f,0.0f,1.0f);
						
			gl.glNormal3f(0.0f,0.0f,-1.0f);
			gl.glBegin(GL.GL_POLYGON);
			gl.glTexCoord2f(uStart,vStart);
			gl.glVertex3f(-PARTICLE_SIZE,-PARTICLE_SIZE,0.0f);
			gl.glTexCoord2f(uStart,vEnd);
			gl.glVertex3f(-PARTICLE_SIZE,PARTICLE_SIZE,0.0f);
			gl.glTexCoord2f(uEnd,vEnd);
			gl.glVertex3f(PARTICLE_SIZE,PARTICLE_SIZE,0.0f);
			gl.glTexCoord2f(uEnd,vStart);
			gl.glVertex3f(PARTICLE_SIZE, -PARTICLE_SIZE,0.0f);
			gl.glEnd();
			gl.glPopMatrix();
		}
	}
}
 