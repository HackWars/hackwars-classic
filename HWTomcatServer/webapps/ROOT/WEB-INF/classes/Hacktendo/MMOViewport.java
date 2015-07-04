/*
Programmer: Ben Coe.(2007)<br />

This is a viewport used specifically for networked game communication.
*/

package Hacktendo;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.glu.*;
import javax.media.opengl.*;
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
import java.util.concurrent.Semaphore;
import GUI.Sound;
import javax.imageio.*;
import GUI.ImageLoader;
import GUI.Hacker;
import View.*;
import Assignments.*;
import java.util.*;
import Hacktendo.*;
import Game.MMO.*;

import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;
import com.plink.Hack3D.*;

public class MMOViewport extends OpenGLViewport implements FocusListener,MouseListener,GLEventListener,KeyListener{
	/////////////////////
	// Data.
	private String fileName="";//The name of the current game that's running.
	//Hacktendo stuff.
	private GraphicsConfiguration GC=null;//Parent's graphics configuration.
	private BufferedImage BackBuffer=null;//The volatile back buffer.
	private BufferedImage Background=null;//The Volatile Background.
	private OpenGLImageHandler MyImageHandler=null;
	private ClientRenderEngine MyRenderEngine=null;
	private BufferedImage image=null;

	private GLCanvas MyGLCanvas=null;
	private GLU MyGLU=null;
	private GLUT MyGLUT=null;
	private FPSAnimator MyAnimator=null;
	private boolean reloadTextures=false;
	private boolean focus=true;
	private JInternalFrame Parent=null;
	private Hacker hacker;
	
	/**
	Add a packet.
	*/
	public void addPacket(HacktendoPacket Packet){
		MyRenderEngine.addPacket(Packet);
	}
	
	/**
	Is the game currently running in debug mode?
	*/
	public void setDebug(boolean debug){
		MyRenderEngine.setDebug(debug);
	}
	
	/**
	Set the file name associated with the current game that's running.
	*/
	public void setFileName(String fileName){
		this.fileName=fileName;
	}
	
	/**
	Set the load file associated with this game, e.g., the saved instance of the previous game.
	*/
	public void setLoadFile(HashMap LoadFile){
		MyRenderEngine.setLoadFile(LoadFile);
	}
	
	/**
	Intialize the viewport with the given width/height background-color and graphics configuration.
	*/
	public MMOViewport(Color bgColor,GraphicsConfiguration GC,Hacker hacker,JInternalFrame Parent){
		GLCapabilities caps = new GLCapabilities();
		caps.setDoubleBuffered(true);// request double buffer display mode
		caps.setHardwareAccelerated(true);
			
		MyGLCanvas=new GLCanvas(caps);
		MyGLCanvas.setAutoSwapBufferMode(true);
	
		this.Parent=Parent;
		this.hacker=hacker;
		//System.out.println("Making Image Handler and Render Engine");
		MyImageHandler=new OpenGLImageHandler(Parent.getGraphicsConfiguration());
		MyRenderEngine=new ClientRenderEngine(MyImageHandler,MyGLCanvas,hacker);
		//System.out.println("Done making Image Handler and Render Engine");
		MyGLCanvas.addGLEventListener(this);
		MyGLCanvas.addKeyListener(this);
		MyGLCanvas.setBackground(Color.white);
		MyGLCanvas.setFocusable(true);
		MyGLCanvas.requestFocusInWindow();
		MyGLCanvas.addMouseListener(this);
		//MyGLCanvas.addFocusListener(this);
		//MyGLCanvas.setAutoSwapBufferMode(true);
		//Parent.addFocusListener(this);
		Parent.addKeyListener(MyRenderEngine);
		
		MyGLCanvas.addMouseListener(MyRenderEngine);
		MyGLCanvas.addMouseMotionListener(MyRenderEngine);
		MyGLCanvas.addKeyListener(MyRenderEngine);
		MyGLCanvas.addKeyListener(this);
		MyGLCanvas.addMouseListener(this);
		
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
	
		MyRenderEngine.cleanLevel();
		MyRenderEngine.resetGlobals();
		
		System.gc();
		started=false;
		Parent.add(MyGLCanvas);
		MyAnimator=null;
		reloadTextures=false;
	}

	/**
	Load a game if loading is not already taking place.
	*/
	private boolean started=false;
	private boolean loadGame=false;
	private String xml=null;
	public synchronized void loadGame(String xml){
		if(!started&&xml!=null&&xml.length()>0){
			MyAnimator=null;
			loadGame=true;
			this.xml=xml;
			started=true;
			Parent.add(MyGLCanvas);		
			Parent.setBounds(Parent.getParent().getWidth()/2-320,Parent.getParent().getHeight()/2-240,640,480);

			MyGLCanvas.requestFocus();
			MyGLCanvas.requestFocusInWindow();
		}
	}
	
	//Testing main.
	public static void main(String args[]){
		JFrame W=new JFrame();
			
		JInternalFrame MyFrame=new JInternalFrame();
		MyFrame.setBounds(0,0,640,480);
		W.setBounds(0,0,640,480);
		MMOViewport Test=new MMOViewport(Color.BLACK,MyFrame.getGraphicsConfiguration(),null,MyFrame);
		try{
			String xml="";
			BufferedReader BR=new BufferedReader(new FileReader("game.xml"));
			String s;
			while((s=BR.readLine())!=null)
				xml+=s;
			Test.loadGame(xml);
		}catch(Exception e){
			e.printStackTrace();
		}
		MyFrame.setVisible(true);
		W.add(MyFrame);
		W.setVisible(true);
		
	}
	
	private int iterationCount=0;
	public synchronized void display(GLAutoDrawable drawable) {
		iterationCount++;
	    GL gl = drawable.getGL();
		
		if(loadGame){//Load a game if required.
			//System.out.println("Loading a game");
			MyRenderEngine.setGL(gl);
			MyRenderEngine.loadGame(xml);
			loadGame=false;
			MyRenderEngine.regenerateLists();
		}
		if(MyAnimator==null&&started){
			//System.out.println("Starting Animator");
			MyAnimator=new FPSAnimator(drawable,33);
			MyAnimator.start();
			//System.out.println("Animator Started");
		}
	
		gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		
		if(reloadTextures){//If we lost and re-gained focus we must reload our textures.
			MyImageHandler.reloadTextures();
			MyRenderEngine.regenerateLists();
			MyRenderEngine.viewChange();
			reloadTextures=false;
			init(drawable);
		}
		
		OpenGLViewport.MyGLU.gluLookAt(MyRenderEngine.getViewX(),MyRenderEngine.getViewY(),MyRenderEngine.getViewZ(),MyRenderEngine.getViewX(),MyRenderEngine.getViewY(),-MyRenderEngine.getViewZ(),0.0f,-1.0f,0.0f);
		

		if(!reloadTextures){
			MyRenderEngine.updateScreen(gl,OpenGLViewport.MyGLUT,OpenGLViewport.MyGLU,Parent.getWidth(),Parent.getHeight(),drawable);
		}
		
		gl.glFlush();
		
		if(iterationCount%10==0)
			MyRenderEngine.sendPacket();
	}


}//END.
