/*
Programmer: Ben Coe.(2007)<br />

This is a viewport used specifically for networked game communication.
*/

package Hacktendo;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.glu.*;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import com.sun.opengl.util.GLUT;
import Assignments.*;
import View.*;
import java.text.*;
import java.math.*;
import Browser.*;
import Game.*;

//Stuff borrowed from Hacktendo.
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.util.Map;
import java.util.concurrent.Semaphore;
import GUI.Sound;
import javax.imageio.*;
import GUI.ImageLoader;
import GUI.Hacker;
import View.*;
import Assignments.*;
import Hacktendo.*;
import Game.MMO.*;

import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;
import com.plink.Hack3D.*;

public class SpriteViewport implements FocusListener,MouseListener,GLEventListener,KeyListener{
	/////////////////////
	// Data.
	private String fileName="";//The name of the current game that's running.
	//Hacktendo stuff.
	private GraphicsConfiguration GC=null;//Parent's graphics configuration.
	private BufferedImage BackBuffer=null;//The volatile back buffer.
	private BufferedImage Background=null;//The Volatile Background.
	private SpriteFace  MySpriteFace=new SpriteFace();//Temporarily used for editing sprite faces.
	private SpriteBody MySpriteBody=null;
	private SpriteAnimator MySpriteAnimator=null;

	private GLCanvas MyGLCanvas=new GLCanvas();
	private GLU MyGLU=null;
	private GLUT MyGLUT=null;
	private FPSAnimator MyAnimator=null;
	private boolean reloadTextures=false;
	private boolean focus=true;
	private JInternalFrame Parent=null;
	private Hacker hacker;
	
	private int viewportX=0;
	private int viewportY=0;
	private int viewportZ=-10;
	private float rotateX=0.0f;
	private float rotateY=0.0f;
	private float rotateZ=0.0f;

	/**
	Intialize the viewport with the given width/height background-color and graphics configuration.
	*/
	public SpriteViewport(Color bgColor,GraphicsConfiguration GC,Hacker hacker,JInternalFrame Parent){
		MySpriteFace.load("abstract_face.christ");

		this.Parent=Parent;
		this.hacker=hacker;

		MyGLCanvas.addGLEventListener(this);
		MyGLCanvas.addKeyListener(this);
		MyGLCanvas.setBackground(Color.white);
		MyGLCanvas.setFocusable(true);
		MyGLCanvas.requestFocusInWindow();
		MyGLCanvas.addMouseListener(this);
		MyGLCanvas.addKeyListener(this);
		MyGLCanvas.addMouseListener(this);
		

		Parent.add(MyGLCanvas);	
		
		JFrame AttributeFrame=new JFrame();
		AttributeFrame.setLayout(new BoxLayout(AttributeFrame.getContentPane(),BoxLayout.Y_AXIS));
	
		XAttributeSelectorPanel ASP=new XAttributeSelectorPanel(MySpriteFace);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(ASP);

		//add the root control pane to the frame
		AttributeFrame.add(panel);


		//tabPane


		/*

		//Create the widgets for changing color.
		JScrollPane ScrollPane2=new JScrollPane();
		MyIterator=MySpriteFace.getMaterialsHash().entrySet().iterator();
		JPanel ColorPanel=new JPanel();
		ColorPanel.setLayout(new BoxLayout(ColorPanel,BoxLayout.Y_AXIS));
		ColorPanel.setVisible(true);
		while(MyIterator.hasNext()){
			Map.Entry MyEntry=(Map.Entry)MyIterator.next();
			ColorSelectorPanel ASP=new ColorSelectorPanel((String)MyEntry.getKey(),MySpriteFace);
			ASP.setVisible(true);
			ColorPanel.add(ASP);
			i++;
		}
		
		ScrollPane.getViewport().add(BigPanel);
		ScrollPane.setVisible(true);
		ScrollPane.setBounds(0,0,200,350);
		ScrollPane2.getViewport().add(ColorPanel);
		ScrollPane2.setVisible(true);
		ScrollPane2.setBounds(0,0,200,350);
		
		ColorShape.addTab("Sprite Shape",ScrollPane);
		ColorShape.addTab("Sprite Color",ScrollPane2);
		
		AttributeFrame.add(ColorShape);

		*/
		AttributeFrame.pack();
		AttributeFrame.setBounds(0,0,200,350);
		AttributeFrame.setVisible(true);

	}
	
	/**
	Populate.
	*/
	public void populate(){
		Parent.add(MyGLCanvas);
	}
	
	/**
	Stop the thread associated with this viewport.
	*/
	public void stop(){
	
		try{
			MyAnimator.stop();
			MyAnimator=null;
		}catch(Exception e){}
			
		System.gc();
		Parent.add(MyGLCanvas);
		MyAnimator=null;
		reloadTextures=false;
	}


	//Testing main.
	public static void main(String args[]){
		JFrame W=new JFrame();
			
		JInternalFrame MyFrame=new JInternalFrame();
		MyFrame.setBounds(0,0,640,480);
		W.setBounds(0,0,640,480);
		SpriteViewport Test=new SpriteViewport(Color.BLACK,MyFrame.getGraphicsConfiguration(),null,MyFrame);
	
		MyFrame.setVisible(true);
		W.add(MyFrame);
		W.setVisible(true);
		
	}
	
	private int iterationCount=0;
	public synchronized void display(GLAutoDrawable drawable) {
		
		iterationCount++;
	    GL gl = drawable.getGL();
		
		if(MySpriteBody==null){//Load the sprite model.
			MySpriteBody=new SpriteBody();
			MySpriteBody.load("abstract_face.christ");
			MySpriteBody.buildBuffers(gl);
			
			MySpriteAnimator=new SpriteAnimator();
			MySpriteAnimator.load("");
		}
		
		if(MyAnimator==null){
			MyAnimator=new FPSAnimator(drawable,33);
			MyAnimator.start();
		}
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		

		MyGLU.gluLookAt(viewportX,viewportY,viewportZ,viewportX,viewportY,-viewportZ,0.0f,-1.0f,0.0f);
		
		gl.glPushMatrix();
		
		gl.glTranslatef(0.0f,2.0f,0.0f);
		gl.glRotatef(rotateX,1.0f,0.0f,0.0f);
		gl.glRotatef(rotateY,0.0f,1.0f,0.0f);
		gl.glRotatef(rotateZ,0.0f,0.0f,1.0f);
		
		//HashMap BodyAnimation=MySpriteAnimator.getCurrentAnimation("WALK");
		MySpriteFace.render(gl, null);
		gl.glPopMatrix();

		gl.glFlush();
	}

    public void reshape(GLAutoDrawable drawable,int xstart,int ystart,int width, int height){
      GL  gl  = drawable.getGL();
	  gl.glViewport(0,0, width, height);
	  gl.glMatrixMode(gl.GL_PROJECTION);
	  gl.glLoadIdentity();
	  MyGLU.gluPerspective(45.0f, (float)width/height, 1.0f, 50000.0f);
	  gl.glMatrixMode(gl.GL_MODELVIEW);
	  gl.glLoadIdentity();
    }

	public void init(GLAutoDrawable drawable) {
	
		if(MyGLU==null);
			MyGLU=new GLU();
		if(MyGLUT==null)
			MyGLUT=new GLUT();
		
		GL gl = drawable.getGL();
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_AUTO_NORMAL);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glBlendFunc (GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable (GL.GL_BLEND);
		gl.glEnable( GL.GL_LIGHTING);
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_AMBIENT,new float[]{0.5f,0.5f,0.5f,1.0f},0);
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_DIFFUSE,new float[]{1.0f,1.0f,1.0f,1.0f},0);
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_POSITION,new float[]{1.0f,2.0f,-1.0f,1.0f},0);
		gl.glEnable( GL.GL_LIGHT0);
		gl.glEnable( GL.GL_COLOR_MATERIAL);
		gl.glCullFace(gl.GL_BACK); 
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,boolean deviceChanged) {
	}
	
	//Methods for KeyListener.
	public void keyPressed(KeyEvent e){	
		try{
		int key=e.getKeyCode();
		if(key=='='){
			viewportZ+=1;
		}
		
		if(key=='-'){
			viewportZ-=1;
		}
		
		if(key==37){
			rotateY+=2;
		}
		
		if(key==39){
			rotateY-=2;
		}
		
		if(key==38){
			rotateX-=2;
		}
		
		if(key==40){
			rotateX+=2;
		}

		}catch(Exception exception){
		
		}
	}
	
	public void gainedFocus(){
		focus=true;
		Parent.add(MyGLCanvas);
		reloadTextures=true;
	}
	
	public void lostFocus(){
		focus=false;
		try{
			if(MyAnimator!=null){
				MyAnimator.stop();
				MyAnimator=null;
			}
		}catch(Exception e){
		}
		Parent.remove(MyGLCanvas);

	}
	
	
	public void focus(){
		MyGLCanvas.requestFocus();
		MyGLCanvas.requestFocusInWindow();
	}

	public void keyReleased(KeyEvent e){
	}
	
	public void keyTyped(KeyEvent e) {
	}
	
	public void mouseExited(MouseEvent me){}
	
	public void mouseEntered(MouseEvent me){}
	public void mouseReleased(MouseEvent me){}
	public void mousePressed(MouseEvent me){}
	public void mouseClicked(MouseEvent me){
	}
	
	public void focusGained(FocusEvent fe){

	}

	public void focusLost(FocusEvent fe){

	}
}//END.
