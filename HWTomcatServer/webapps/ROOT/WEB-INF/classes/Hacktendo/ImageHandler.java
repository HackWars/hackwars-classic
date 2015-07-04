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
import javax.imageio.*;
import java.util.zip.*;
import org.apache.axis.encoding.*;
public class ImageHandler{
	//Palette.
	public static byte r[]={(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)255,(byte)255,(byte)255,(byte)255,(byte)153,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)204,(byte)204,(byte)204,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)204,(byte)204,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)255,(byte)255,(byte)153,(byte)204,(byte)153,(byte)102,(byte)153,(byte)204,(byte)255,(byte)153,(byte)153,(byte)102,(byte)153,(byte)204,(byte)255,(byte)204,(byte)255,(byte)255,(byte)102,(byte)153,(byte)102,(byte)102,(byte)153,(byte)204,(byte)255,(byte)153,(byte)102,(byte)153,(byte)204,(byte)255,(byte)153,(byte)204,(byte)255,(byte)255,(byte)51,(byte)102,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)204,(byte)204,(byte)204,(byte)255,(byte)255,(byte)204,(byte)153,(byte)255,(byte)255,(byte)0,(byte)51,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)153,(byte)102,(byte)204,(byte)51,(byte)51,(byte)102,(byte)0,(byte)51,(byte)102,(byte)153,(byte)204,(byte)204,(byte)153,(byte)153,(byte)153,(byte)102,(byte)102,(byte)0,(byte)51,(byte)0,(byte)51,(byte)102,(byte)153,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)204,(byte)0,(byte)0,(byte)0,(byte)51,(byte)102,(byte)153,(byte)204,(byte)51,(byte)153,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)153,(byte)204,(byte)0,(byte)51,(byte)0,(byte)0,(byte)51,(byte)153,(byte)153,(byte)0,(byte)102,(byte)153,(byte)153,(byte)153,(byte)102,(byte)102,(byte)204,(byte)204,(byte)0,(byte)102,(byte)51,(byte)0,(byte)102,(byte)102,(byte)102,(byte)0,(byte)51,(byte)102,(byte)102,(byte)102,(byte)51,(byte)153,(byte)204,(byte)153,(byte)0,(byte)102,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)0,(byte)0,(byte)51,(byte)51,(byte)51,(byte)51,(byte)102,(byte)153,(byte)102,(byte)0,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)51,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)102,(byte)51,(byte)0,(byte)0,(byte)51,(byte)102,(byte)102,(byte)51,(byte)0,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)6,(byte)12,(byte)18,(byte)24,(byte)30,(byte)36,(byte)42,(byte)48,(byte)54,(byte)60,(byte)66,(byte)72,(byte)78,(byte)86,(byte)92,(byte)100,(byte)106,(byte)112,(byte)118,(byte)124,(byte)130,(byte)136,(byte)142,(byte)148,(byte)154,(byte)160,(byte)166,(byte)172,(byte)178,(byte)184,(byte)190,(byte)196,(byte)202,(byte)208,(byte)214,(byte)220,(byte)226,(byte)232,(byte)238,(byte)244};
	public static byte g[]={(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)204,(byte)153,(byte)102,(byte)51,(byte)204,(byte)153,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)255,(byte)255,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)153,(byte)102,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)51,(byte)0,(byte)255,(byte)255,(byte)204,(byte)102,(byte)153,(byte)204,(byte)255,(byte)102,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)102,(byte)0,(byte)255,(byte)255,(byte)204,(byte)153,(byte)153,(byte)204,(byte)255,(byte)102,(byte)51,(byte)102,(byte)102,(byte)102,(byte)0,(byte)51,(byte)102,(byte)0,(byte)255,(byte)255,(byte)153,(byte)204,(byte)255,(byte)204,(byte)255,(byte)153,(byte)102,(byte)153,(byte)153,(byte)51,(byte)0,(byte)0,(byte)51,(byte)0,(byte)204,(byte)204,(byte)102,(byte)153,(byte)204,(byte)255,(byte)255,(byte)204,(byte)153,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)0,(byte)0,(byte)204,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)153,(byte)102,(byte)51,(byte)0,(byte)51,(byte)0,(byte)102,(byte)102,(byte)153,(byte)153,(byte)153,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)102,(byte)51,(byte)51,(byte)204,(byte)102,(byte)153,(byte)204,(byte)255,(byte)255,(byte)153,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)0,(byte)0,(byte)255,(byte)255,(byte)153,(byte)204,(byte)255,(byte)255,(byte)204,(byte)102,(byte)153,(byte)153,(byte)153,(byte)51,(byte)0,(byte)0,(byte)51,(byte)0,(byte)255,(byte)255,(byte)204,(byte)153,(byte)255,(byte)204,(byte)153,(byte)51,(byte)102,(byte)102,(byte)102,(byte)102,(byte)0,(byte)51,(byte)102,(byte)0,(byte)255,(byte)255,(byte)204,(byte)255,(byte)204,(byte)153,(byte)102,(byte)102,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)102,(byte)0,(byte)255,(byte)255,(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)153,(byte)102,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)51,(byte)0,(byte)204,(byte)153,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)204,(byte)153,(byte)102,(byte)51,(byte)6,(byte)12,(byte)18,(byte)24,(byte)30,(byte)36,(byte)42,(byte)48,(byte)54,(byte)60,(byte)66,(byte)72,(byte)78,(byte)86,(byte)92,(byte)100,(byte)106,(byte)112,(byte)118,(byte)124,(byte)130,(byte)136,(byte)142,(byte)148,(byte)154,(byte)160,(byte)166,(byte)172,(byte)178,(byte)184,(byte)190,(byte)196,(byte)202,(byte)208,(byte)214,(byte)220,(byte)226,(byte)232,(byte)238,(byte)244};
	public static byte b[]={(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)51,(byte)102,(byte)102,(byte)51,(byte)0,(byte)51,(byte)0,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)51,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)102,(byte)51,(byte)0,(byte)102,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)0,(byte)0,(byte)51,(byte)51,(byte)51,(byte)51,(byte)102,(byte)153,(byte)102,(byte)0,(byte)102,(byte)51,(byte)0,(byte)102,(byte)102,(byte)102,(byte)51,(byte)0,(byte)102,(byte)102,(byte)102,(byte)51,(byte)153,(byte)204,(byte)153,(byte)0,(byte)51,(byte)0,(byte)0,(byte)51,(byte)153,(byte)153,(byte)102,(byte)0,(byte)153,(byte)153,(byte)153,(byte)102,(byte)102,(byte)204,(byte)204,(byte)0,(byte)0,(byte)0,(byte)51,(byte)102,(byte)153,(byte)204,(byte)153,(byte)51,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)153,(byte)51,(byte)51,(byte)102,(byte)0,(byte)51,(byte)102,(byte)153,(byte)204,(byte)204,(byte)153,(byte)153,(byte)153,(byte)102,(byte)102,(byte)0,(byte)51,(byte)0,(byte)51,(byte)102,(byte)153,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)204,(byte)0,(byte)51,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)153,(byte)102,(byte)204,(byte)204,(byte)51,(byte)102,(byte)51,(byte)102,(byte)153,(byte)255,(byte)204,(byte)204,(byte)204,(byte)255,(byte)204,(byte)255,(byte)204,(byte)153,(byte)255,(byte)255,(byte)102,(byte)153,(byte)102,(byte)102,(byte)255,(byte)204,(byte)153,(byte)102,(byte)153,(byte)255,(byte)204,(byte)153,(byte)153,(byte)204,(byte)255,(byte)255,(byte)153,(byte)204,(byte)153,(byte)255,(byte)204,(byte)153,(byte)102,(byte)153,(byte)153,(byte)255,(byte)204,(byte)153,(byte)102,(byte)204,(byte)255,(byte)255,(byte)204,(byte)204,(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)204,(byte)204,(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)255,(byte)255,(byte)153,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)6,(byte)12,(byte)18,(byte)24,(byte)30,(byte)36,(byte)42,(byte)48,(byte)54,(byte)60,(byte)66,(byte)72,(byte)78,(byte)86,(byte)92,(byte)100,(byte)106,(byte)112,(byte)118,(byte)124,(byte)130,(byte)136,(byte)142,(byte)148,(byte)154,(byte)160,(byte)166,(byte)172,(byte)178,(byte)184,(byte)190,(byte)196,(byte)202,(byte)208,(byte)214,(byte)220,(byte)226,(byte)232,(byte)238,(byte)244};
	
	private BufferedImage Sprite[][]=new BufferedImage[32][8];
	private BufferedImage Tiles[]=new BufferedImage[128];
	private GraphicsConfiguration GC=null;
	
	/**
	flushing, like garbage collecting but better.
	*/
	public void clean(){
		if(Sprite!=null){
		for(int ii=0;ii<Sprite.length;ii++)
			for(int i=0;i<Sprite[ii].length;i++){
				if(Sprite[ii][i]!=null){
					Sprite[ii][i].flush();
					Sprite[ii][i]=null;
				}
			}
		}
			
		for(int i=0;i<Tiles.length;i++){
			if(Tiles[i]!=null){
				Tiles[i].flush();
				Tiles[i]=null; 
			}
		}
	}
	
	/**
	Constructor.
	*/
	public ImageHandler(GraphicsConfiguration GC){
		this.GC=GC;
	}
	
	/**
	Fetch a sprite image based on an Integer key.
	*/
	public BufferedImage getSpriteImage(int id,int frame){
		return(Sprite[id][frame]);
	}
	
	/**
	Fetch a tile based on an Integer key provided.
	*/
	public BufferedImage getTile(int id){
		return(Tiles[id]); 
	}
	
	public void setTile(int id,BufferedImage image){
		Tiles[id]=image;
	}
	
	public void setSpriteImage(int id,int frame,BufferedImage image){
		Sprite[id][frame]=image;
	}
	
	
	public void reset(){
		Sprite=new BufferedImage[32][8];
		Tiles=new BufferedImage[128];
	}
		
	/**
	Load an image from an input stream, in the Hacktendo image format.
	*/
	public void loadImage(InputStream In,int id,int frame){
		try{
			ByteArrayOutputStream bout=new ByteArrayOutputStream();
			int size=0;
			byte buffer[]=new byte[512];
			while((size=In.read(buffer))!=-1)
				bout.write(buffer,0,size);
			byte B[]=Base64.decode(new String(bout.toByteArray()));			
			
			bout=new ByteArrayOutputStream();
			ZipInputStream zin=new ZipInputStream(new ByteArrayInputStream(B));
			zin.getNextEntry();
			int trans=zin.read();
			while((size=zin.read(buffer))!=-1){
				bout.write(buffer,0,size);
			}
			zin.close();
			B=bout.toByteArray();
			
			BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_BYTE_INDEXED,new IndexColorModel(8,256,r,g,b,trans));
			byte[] b=((DataBufferByte)(img).getRaster().getDataBuffer()).getData();
			for(int ii=0;ii<b.length;ii++){
				b[ii]=B[ii];
			}
			
			BufferedImage tempimg=GC.createCompatibleImage(32,32);
			Graphics G=tempimg.getGraphics();
			G.drawImage(img,0,0,null);
			G.dispose();
			img=tempimg;
			
			if(frame==-1){
				Tiles[id]=img;
			}else
				Sprite[id][frame]=img;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	Converts an image to our palette.
	*/
	public void convertImage(String path,GraphicsConfiguration GC){

		for(int i=0;i<16;i++){
			for(int ii=0;ii<8;ii++){
				try{

					loadImage(new FileInputStream("images/space/x"+i+"y"+ii+".himg"),ii*16+i,-1);
					/*BufferedImage Temp=ImageIO.read(new File("images/space.jpg"));
					BufferedImage img = new BufferedImage(32,32,BufferedImage.TYPE_BYTE_INDEXED,new IndexColorModel(8,256,r,g,b));
					Graphics gr = img.createGraphics();
					gr.drawImage(Temp,0,0,32,32,i*32,ii*32,i*32+32,ii*32+32,null);
					gr.dispose();
					TestSprite=img;
					byte[] t=((DataBufferByte)(TestSprite).getRaster().getDataBuffer()).getData();
					
					ByteArrayOutputStream bout=new ByteArrayOutputStream();
					ZipOutputStream zout=new ZipOutputStream(bout);
					zout.setLevel(9);
					zout.putNextEntry(new ZipEntry("image"));
					zout.write(5);
					zout.write(t,0,t.length);
					zout.closeEntry();
					zout.close();

					byte B[]=Base64.encode(bout.toByteArray()).getBytes();
					FileOutputStream FOS=new FileOutputStream("images/space/x"+i+"y"+ii+".himg");
					FOS.write(B);
					FOS.close();*/
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		/*try{
			loadImage(new FileInputStream("images/0.himg"),0,0);
			loadImage(new FileInputStream("images/60.himg"),0,1);
			loadImage(new FileInputStream("images/90.himg"),0,2);
			loadImage(new FileInputStream("images/150.himg"),0,3);
			loadImage(new FileInputStream("images/180.himg"),0,4);
			loadImage(new FileInputStream("images/240.himg"),0,5);
			loadImage(new FileInputStream("images/270.himg"),0,6);
			loadImage(new FileInputStream("images/330.himg"),0,7);
		}catch(Exception e){
			e.printStackTrace();
		}*/
	}
	
	public static void main(String args[]){
	/*	try{
			BufferedReader BR=new BufferedReader(new FileReader("pal.txt"));
			int r;
			int g;
			int b;
			int i=0;;
			String temp;
			int Pal[][]=new int[256][3];
			
			while((temp=BR.readLine())!=null){
				r=new Integer(temp.trim());
				g=new Integer(BR.readLine().trim());
				b=new Integer(BR.readLine().trim());
				Pal[i][0]=r;
				Pal[i][1]=g;
				Pal[i][2]=b;
				i++;
			}
			System.out.print("private byte r[]={");
			for(i=0;i<256;i++){
				System.out.print("(byte)"+Pal[i][0]);
				if(i!=255)
					System.out.print(",");
			}
			System.out.println("};");
			System.out.print("private byte g[]={");
			for(i=0;i<256;i++){
				System.out.print("(byte)"+Pal[i][1]);
				if(i!=255)
					System.out.print(",");
			}
			System.out.println("};");
			System.out.print("private byte b[]={");
			for(i=0;i<256;i++){
				System.out.print("(byte)"+Pal[i][2]);
				if(i!=255)
					System.out.print(",");
			}
			System.out.println("};");
			BR.close();
		}catch(Exception e){
			e.printStackTrace();
		}*/
	}
}//END.
