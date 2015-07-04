package Hacktendo;
/*
Porgrammer: Ben Coe.(2007)<br />

The viewport that an experiment takes place in.

*/

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
import GUI.*;

public class Viewport extends JComponent implements Runnable,FocusListener,MouseListener{
	/////////////////////
	// Data.
	private int width;//Width of Viewport.
	private int height;//Height of Viewport.
	private String fileName="";//The name of the current game that's running.
	
	private GraphicsConfiguration GC=null;//Parent's graphics configuration.
	private BufferedImage BackBuffer=null;//The volatile back buffer.
	private BufferedImage Background=null;//The Volatile Background.
	private ImageHandler MyImageHandler=null;
	private RenderEngine MyRenderEngine=null;
	private BufferedImage image=null;

	private Thread MyThread=null;
	private boolean running=true;
	private Time MyTime=new Time();
	private long sleepTime=50;
	private long startTime;
	private final Semaphore available = new Semaphore(1, true);//Make it thread safe.
	
	private Color bgColor=null;//The Background Color.
	
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
	public Viewport(Color bgColor,GraphicsConfiguration GC,Hacker hacker){

		MyImageHandler=new ImageHandler(GC);
	
		MyRenderEngine=new RenderEngine(MyImageHandler,this,hacker);
		this.hacker=hacker;
		this.GC=GC;
		this.width=288;
		this.height=256;
		this.setDoubleBuffered(false);
		
		this.setLayout(null);//Use X,Y coordinates for layout.
		//this.setOpaque(false);//Widget is transparent.
		this.setSize(width*2,height*2);//Set the size of the widget.
		this.setVisible(true);//Initially visible.
		//this.setIgnoreRepaint(true);
		this.enableInputMethods(true);
		
		//Add Listeners.
		addKeyListener(MyRenderEngine);
		addMouseListener(this);
		super.setFocusable(true);
		this.setBorder(null);
		
		//Set the Background Color.
		if(bgColor!=null){
			Background=GC.createCompatibleImage(width,height,Transparency.BITMASK);//Create the back buffer.
			Graphics G=Background.getGraphics();
			G.setColor(bgColor);
			G.fillRect(0,0,width,height);
			G.dispose();
		}
		
		//Create a fast back buffer for drawing.
		//BackBuffer=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_INDEXED,new IndexColorModel(8,256,ImageHandler.r,ImageHandler.g,ImageHandler.b));
		BackBuffer=GC.createCompatibleImage(width,height,Transparency.BITMASK);//Create the back buffer.
		
		width=width*2;
		height=height*2;
		addFocusListener(this);
	}
	
	/**
	Stop the thread associated with this viewport.
	*/
	public void stop(){
		running=false;
		MyThread=null;
		MyRenderEngine.cleanLevel();
		MyRenderEngine.resetGlobals();
		Object[] fireWatch = MyRenderEngine.getFireWatch();
		MyRenderEngine.setFireWatch(null);
		
		//Fire a watch on the game maker's computer.
		if(fireWatch!=null){
			//System.out.println("Sending Trigger");
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
		//System.out.println(MyRenderEngine.getTaskName()+" >> "+MyRenderEngine.getQuestID());
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
	}
	
	/**
	Start animating the viewport.
	*/
	public void start(){
		running=true;
		MyThread=new Thread(this);
		MyThread.start();
	}
	
	public void paintComponent(Graphics g){
		if(image!=null){
			g.drawImage(image,0,0,width,height,null);
		}else
		
		try{
			available.acquire();
			g.drawImage(BackBuffer,0,0,width,height,null);
			available.release();
		}catch(Exception e){
			available.release();
		}
	}
	
	/**
	Re-draw the experimental environment at a given frame-rate.
	*/
	private Graphics2D ComponentGraphics2D=null;
	private Graphics ComponentGraphics=null;
	private Graphics BackBufferGraphics=null;
	private AffineTransform xform=null;
	
	int i=0;
	int totaltime=0;
	public void run(){
		if(image!=null){
			try{
				Thread.sleep(1000);
				image=null;
			}catch(Exception e){
			
			}
		}
	
		while(running){
			
			try{
			
			available.acquire();
			long startTime=MyTime.getCurrentTime();
			
			if(BackBufferGraphics==null)
				BackBufferGraphics=BackBuffer.getGraphics();
			
			BackBufferGraphics.drawImage(Background,0,0,this);
			
			MyRenderEngine.updateScreen(BackBufferGraphics);
			
			available.release();
			
			long endTime=MyTime.getCurrentTime();

			if(sleepTime-(endTime-startTime)>0){		
                MyThread.sleep(sleepTime-(endTime-startTime));
			}else{
			}
							
            }catch(Exception e){
               e.printStackTrace();
            }
			
			repaint();	
			i++;
		}
	}
	boolean started=false;
	public synchronized void loadGame(String xml){
		if(!started&&xml!=null&&xml.length()>0){
	
		try{
			//System.out.println("Loading Logi Image");
			image = ImageIO.read(ImageLoader.getFile("images/logo.png"));
			repaint();
			start();
		}catch(Exception e){e.printStackTrace();}
		
		MyRenderEngine.loadGame(xml);
		//System.out.println("Setting image to null");
		
		started=true;
		}
	}
	
	//Testing main.
	public static void main(String args[]){
		JFrame JF=new JFrame("Testing Suite");
		JF.setSize(576,512);
		Viewport AwesomeViewMax=new Viewport(Color.BLACK,JF.getGraphicsConfiguration(),null);
		AwesomeViewMax.start();
		JF.add(AwesomeViewMax);
		JF.setVisible(true);
		try{
			String xml="";
			BufferedReader BR=new BufferedReader(new FileReader("game.xml"));
			String s;
			while((s=BR.readLine())!=null)
				xml+=s;
			AwesomeViewMax.loadGame(xml);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void focusLost(FocusEvent fe){
	
	}
	
	public void mouseExited(MouseEvent me){}
	
	public void mouseEntered(MouseEvent me){}
	public void mouseReleased(MouseEvent me){}
	public void mousePressed(MouseEvent me){}
	public void mouseClicked(MouseEvent me){
		super.requestFocus();
		super.requestFocusInWindow();
	}


	
	public void focusGained(FocusEvent fe){
		super.requestFocus();
		super.requestFocusInWindow();
	}
}//END.
