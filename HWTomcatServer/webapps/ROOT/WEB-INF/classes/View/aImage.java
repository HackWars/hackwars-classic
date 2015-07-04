package View;
/**

   Programmer: Ben Coe<br />
   (2005)<br  />
   Loads in an Java.Image from either a web-source
   our a byte array provided.

*/


import java.awt.*;
import java.awt.image.*;
import java.net.URL;
import java.io.*;
import javax.swing.*;
import java.util.*;

public class aImage{
	private BufferedImage data;

	//////////////////////////
	// Data.
	private int width;
	private	int height;
	private int ix;
	private int iy;


	public BufferedImage getBufferedImage(){
		return(data);
	}

	//////////////////////////
	// Used for internal scaling of images.
	private aImage(BufferedImage bi,int w,int h,int x,int y){
		data=bi;
		width=w;
		height=h;
		ix=x;
		iy=y;
	}

	//////////////////////////////////////////////
	// aImage()
	// Description: Loads an image from the byte
	// buffer provided.
	//////////////////////////////////////////////
	public aImage(byte input[],int xoff,int yoff,Component c){
		ix=0;
		iy=0;

		Image image=getRawImage(input,c);

		//Set the image size.
    	width=32;
    	height=64;

        data=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g2d=data.createGraphics();
		g2d.drawImage(image,0,0,32,64,xoff,yoff,xoff+16,yoff+16,c);
	}

	////////////////////////////////////////////
	// aImage()
	// Description: Loads an image from the URL
	// path provided.
	////////////////////////////////////////////
	public aImage(String path,Component c){
		ix=0;
		iy=0;

		Image image=getRawImage(path,c);
    	//getRawImage(path,c);

    	width=image.getWidth(c);
    	height=image.getHeight(c);

      //  data=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
     	data=new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR_PRE);
    	Graphics2D g2d=data.createGraphics();
	    g2d.drawImage(image,0,0,c);
	}

	///////////////////////////////////////////
	// getRawImage()
	// Description: Used during the scaling of images
	// loads the data in off the disk. In this instance
	// the image is loaded from a byte array.
	///////////////////////////////////////////
	public Image getRawImage(byte input[],Component c){
		Image image=null;
		try{
			image = c.getToolkit().createImage(input,0,input.length);
		}catch(Exception e){
			System.err.println(e);
		}

		MediaTracker tracker = new MediaTracker(c);
    	tracker.addImage(image, 0);
    	try {
    		tracker.waitForAll();
    	}catch(Exception e){
			System.err.println(e);
		}


		return(image);
	}

	///////////////////////////////////////////
	// getRawImage()
	// Description: Loads in an image from a
	// string path URL.
	///////////////////////////////////////////
	public Image getRawImage(String path,Component c){
		Image image=null;
		try{
			//File temp=new File(path);
			//image = c.getToolkit().getImage(path);

			image = c.getToolkit().getImage(new URL(path));
		}catch(Exception e){
			e.printStackTrace();
		}

		MediaTracker tracker = new MediaTracker(c);
    	tracker.addImage(image, 0);
    	try {
    		tracker.waitForAll();
    	}catch(Exception e){
			System.err.println(e);
		}

		return(image);
	}

	///////////////////////////////////////////
	// zoomTo()
	// Description: Zooms in the current aImage to the
	// specific point.
	///////////////////////////////////////////
	public aImage zoomTo(int ix,int iy,float xper,float yper,float scale,float oldscale,Component c){
		int x=ix+(int)((float)width*xper/oldscale);
		int y=iy+(int)((float)height*yper/oldscale);

		//Make sure it's not full sized.
		if(scale<1.0f)
			return(null);

		x-=(int)((float)width/scale/2.0f);
		y-=(int)((float)height/scale/2.0f);
		if(x<0)
			x=0;
		if(y<0)
			y=0;

		BufferedImage temp=null;
		Image tempImage=null;

		//Fit x to screen.
		int safewidth=(int)(width/scale);
		while(safewidth+x>width)
			if(x>0)
				x--;

		//Fit y to screen.
		int safeheight=(int)(height/scale);
		while(safeheight+y>height)
			if(y>0)
				y--;

		temp=data.getSubimage(x,y,safewidth,safeheight);

		return(new aImage(temp,safewidth,safeheight,x,y));
	}

	/////////////////////////////////
	// GETTERS
	/////////////////////////////////
	public Point3D getBounds(){
		return(new Point3D((float)width,(float)height,1.0f));
	}

	//Get the Startx and Starty of our image.
	public Point3D getIxIy(){
		return(new Point3D((float)ix,(float)iy,1.0f));
	}
	
	public int getWidth(){
		return(width);
	}
	
	public int getHeight(){
		return(height);
	}

	/////////////////////////////////
	// Paint this component.
	/////////////////////////////////
	public void paint(Graphics g,Rectangle area,Component c){
		g.drawImage(data,area.x,area.y,area.width+area.x,area.height+area.y,0,0,width,height,c);
	}
}
