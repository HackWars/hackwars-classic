/*
Programmer: Ben Coe.(2007)<br />

This is the new OpenGL Hacktendo Player.
*/

package Hacktendo;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.glu.*;
import javax.media.opengl.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import com.sun.opengl.util.*;
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

import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;
import com.plink.Hack3D.*;

public class OpenGLViewport implements FocusListener,MouseListener,GLEventListener,KeyListener{
	/////////////////////
	// Data.
	private String fileName="";//The name of the current game that's running.
	//Hacktendo stuff.
	private GraphicsConfiguration GC=null;//Parent's graphics configuration.
	private BufferedImage BackBuffer=null;//The volatile back buffer.
	private BufferedImage Background=null;//The Volatile Background.
	private OpenGLImageHandler MyImageHandler=null;
	private OpenGLRenderEngine MyRenderEngine=null;
	private BufferedImage image=null;
	private GLCanvas MyGLCanvas=null;
	public static GLU MyGLU=null;
	public static GLUT MyGLUT=null;
	private FPSAnimator MyAnimator=null;
	private boolean reloadTextures=false;
	private boolean focus=true;
	private JInternalFrame Parent=null;
	private Hacker hacker;
	
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
	public OpenGLViewport(){}
	public OpenGLViewport(Color bgColor,GraphicsConfiguration GC,Hacker hacker,JInternalFrame Parent){
		//Create the OpenGL cavas.
		GLCapabilities caps = new GLCapabilities();
		caps.setDoubleBuffered(true);// request double buffer display mode
		caps.setHardwareAccelerated(true);
			
		MyGLCanvas=new GLCanvas(caps);
		MyGLCanvas.setAutoSwapBufferMode(true);
	
		this.Parent=Parent;
		this.hacker=hacker;
		//System.out.println("Making Image Handler and Render Engine");
		MyImageHandler=new OpenGLImageHandler(Parent.getGraphicsConfiguration());
		MyRenderEngine=new OpenGLRenderEngine(MyImageHandler,MyGLCanvas,hacker);
		//System.out.println("Done making Image Handler and Render Engine");
		MyGLCanvas.addGLEventListener(this);
		MyGLCanvas.addKeyListener(this);
		MyGLCanvas.setBackground(Color.white);
		MyGLCanvas.setFocusable(true);
		MyGLCanvas.requestFocusInWindow();
		MyGLCanvas.addMouseListener(this);
		Parent.addKeyListener(MyRenderEngine);
		
		MyGLCanvas.addMouseListener(MyRenderEngine);
		MyGLCanvas.addMouseMotionListener(MyRenderEngine);
		MyGLCanvas.addKeyListener(MyRenderEngine);
		MyGLCanvas.addKeyListener(this);
		MyGLCanvas.addMouseListener(this);
		//MyGLCanvas.setRealized(true);
		Parent.add(MyGLCanvas);
		MyGLCanvas.setVisible(true);
	}
	
	/**
	Populate.
	*/
	public void populate(){
		//Parent.add(MyGLCanvas);
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
		Object[] fireWatch = MyRenderEngine.getFireWatch();
		MyRenderEngine.setFireWatch(null);
		
		//Fire a watch on the game maker's computer.
		if(fireWatch!=null){
			String note = (String)fireWatch[0];
			HashMap HM = (HashMap)fireWatch[1];
			HM.put("username",hacker.getUsername());
			View MyView = hacker.getView();
			Object[] send = new Object[]{note,HM,hacker.getUsername(),MyRenderEngine.getIP()};
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HACKTENDO_PLAYER,"requesttrigger",send));
		}
		
		//Save to a file on the player's computer.
		if(MyRenderEngine.getSaveFile()!=null&&!fileName.equals("")){
			//Also save stuff to a player's local hard-drive.
			View MyView = hacker.getView();
			Object[] send = new Object[]{fileName,MyRenderEngine.getSaveFile(),hacker.getIP()};
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HACKTENDO_PLAYER,"requestsave",send));
			MyRenderEngine.setSaveFile(null);
		}
		
		//If this game was played as part of a quest we need to tell the server it was beaten.
		if(MyRenderEngine.getTaskName()!=""){
			if(MyRenderEngine.getBeat()){			
				//Also save stuff to a player's local hard-drive.
				View MyView = hacker.getView();
				Object[] send = new Object[]{fileName,MyRenderEngine.getQuestID(),MyRenderEngine.getTaskName(),hacker.getIP()};
				MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HACKTENDO_PLAYER,"requesttask",send));
			}
		}
		
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
		OpenGLViewport Test=new OpenGLViewport(Color.BLACK,MyFrame.getGraphicsConfiguration(),null,MyFrame);
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
	
	public synchronized void display(GLAutoDrawable drawable) {
	    GL gl = drawable.getGL();
				
		if(loadGame){//Load a game if required.
			//System.out.println("Loading a game");
			MyRenderEngine.setGL(gl);
			MyRenderEngine.loadGame(xml);
			loadGame=false;
			MyRenderEngine.regenerateLists();
		}
		if(MyAnimator==null&&started){
			MyAnimator=new FPSAnimator(drawable,33,true);
			MyAnimator.start();
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
		
		MyGLU.gluLookAt(MyRenderEngine.getViewX(),MyRenderEngine.getViewY(),MyRenderEngine.getViewZ(),MyRenderEngine.getViewX(),MyRenderEngine.getViewY(),-MyRenderEngine.getViewZ(),0.0f,-1.0f,0.0f);
		
		if(!reloadTextures){
			MyRenderEngine.updateScreen(gl,MyGLUT,MyGLU,Parent.getWidth(),Parent.getHeight(),drawable);
		}
		
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
		gl.glEnable(GL.GL_RESCALE_NORMAL);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glBlendFunc (GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable (GL.GL_BLEND);
		gl.glEnable( GL.GL_LIGHTING);
		gl.glEnable( GL.GL_LIGHT1);
		
		gl.glLightfv(gl.GL_LIGHT1, gl.GL_AMBIENT,new float[]{0.5f,0.5f,0.5f,1.0f},0);
		gl.glLightfv(gl.GL_LIGHT1, gl.GL_DIFFUSE,new float[]{1.0f,1.0f,1.0f,1.0f},0);	
	//	gl.glLightfv(gl.GL_LIGHT1, gl.GL_POSITION,new float[]{1.0f,2.0f,-1.0f,1.0f},0);
		gl.glLightfv(gl.GL_LIGHT1, gl.GL_POSITION,new float[]{1.5f,2.0f,-3.0f,1.0f},0);
		
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
			MyRenderEngine.setViewZ(MyRenderEngine.getViewZ()+15);
		}
		
		if(key=='-'){
			MyRenderEngine.setViewZ(MyRenderEngine.getViewZ()-15);
		}
		
		/*
		37 LEFT
		38 UP
		39 RIGHT 
		40 DOWN
		*/
		}catch(Exception exception){
		
		}
	}
	
	public void gainedFocus(){
		/*focus=true;
		Parent.add(MyGLCanvas);
		reloadTextures=true;*/
	//	MyGLCanvas.setVisible(true);
	
	}
	
	public void lostFocus(){
	/**	focus=false;
		try{
			if(MyAnimator!=null){
				MyAnimator.stop();
				MyAnimator=null;
			}
		}catch(Exception e){
		}
		Parent.remove(MyGLCanvas);*/
		//MyGLCanvas.setVisible(false);
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
