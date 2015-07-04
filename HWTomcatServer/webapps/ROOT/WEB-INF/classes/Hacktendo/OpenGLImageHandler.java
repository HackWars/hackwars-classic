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
import GUI.*;

import javax.media.opengl.glu.*;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import com.plink.Hack3D.*;
import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;
import com.plink.Hack3D.*;
import java.util.*;

public class OpenGLImageHandler{
	//Palette.
	public static byte r[]={(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)255,(byte)255,(byte)255,(byte)255,(byte)153,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)204,(byte)204,(byte)204,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)204,(byte)204,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)255,(byte)255,(byte)153,(byte)204,(byte)153,(byte)102,(byte)153,(byte)204,(byte)255,(byte)153,(byte)153,(byte)102,(byte)153,(byte)204,(byte)255,(byte)204,(byte)255,(byte)255,(byte)102,(byte)153,(byte)102,(byte)102,(byte)153,(byte)204,(byte)255,(byte)153,(byte)102,(byte)153,(byte)204,(byte)255,(byte)153,(byte)204,(byte)255,(byte)255,(byte)51,(byte)102,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)204,(byte)204,(byte)204,(byte)255,(byte)255,(byte)204,(byte)153,(byte)255,(byte)255,(byte)0,(byte)51,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)153,(byte)102,(byte)204,(byte)51,(byte)51,(byte)102,(byte)0,(byte)51,(byte)102,(byte)153,(byte)204,(byte)204,(byte)153,(byte)153,(byte)153,(byte)102,(byte)102,(byte)0,(byte)51,(byte)0,(byte)51,(byte)102,(byte)153,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)204,(byte)0,(byte)0,(byte)0,(byte)51,(byte)102,(byte)153,(byte)204,(byte)51,(byte)153,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)153,(byte)204,(byte)0,(byte)51,(byte)0,(byte)0,(byte)51,(byte)153,(byte)153,(byte)0,(byte)102,(byte)153,(byte)153,(byte)153,(byte)102,(byte)102,(byte)204,(byte)204,(byte)0,(byte)102,(byte)51,(byte)0,(byte)102,(byte)102,(byte)102,(byte)0,(byte)51,(byte)102,(byte)102,(byte)102,(byte)51,(byte)153,(byte)204,(byte)153,(byte)0,(byte)102,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)0,(byte)0,(byte)51,(byte)51,(byte)51,(byte)51,(byte)102,(byte)153,(byte)102,(byte)0,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)51,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)102,(byte)51,(byte)0,(byte)0,(byte)51,(byte)102,(byte)102,(byte)51,(byte)0,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)6,(byte)12,(byte)18,(byte)24,(byte)30,(byte)36,(byte)42,(byte)48,(byte)54,(byte)60,(byte)66,(byte)72,(byte)78,(byte)86,(byte)92,(byte)100,(byte)106,(byte)112,(byte)118,(byte)124,(byte)130,(byte)136,(byte)142,(byte)148,(byte)154,(byte)160,(byte)166,(byte)172,(byte)178,(byte)184,(byte)190,(byte)196,(byte)202,(byte)208,(byte)214,(byte)220,(byte)226,(byte)232,(byte)238,(byte)244};
	public static byte g[]={(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)204,(byte)153,(byte)102,(byte)51,(byte)204,(byte)153,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)255,(byte)255,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)153,(byte)102,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)51,(byte)0,(byte)255,(byte)255,(byte)204,(byte)102,(byte)153,(byte)204,(byte)255,(byte)102,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)102,(byte)0,(byte)255,(byte)255,(byte)204,(byte)153,(byte)153,(byte)204,(byte)255,(byte)102,(byte)51,(byte)102,(byte)102,(byte)102,(byte)0,(byte)51,(byte)102,(byte)0,(byte)255,(byte)255,(byte)153,(byte)204,(byte)255,(byte)204,(byte)255,(byte)153,(byte)102,(byte)153,(byte)153,(byte)51,(byte)0,(byte)0,(byte)51,(byte)0,(byte)204,(byte)204,(byte)102,(byte)153,(byte)204,(byte)255,(byte)255,(byte)204,(byte)153,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)0,(byte)0,(byte)204,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)153,(byte)102,(byte)51,(byte)0,(byte)51,(byte)0,(byte)102,(byte)102,(byte)153,(byte)153,(byte)153,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)102,(byte)51,(byte)51,(byte)204,(byte)102,(byte)153,(byte)204,(byte)255,(byte)255,(byte)153,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)0,(byte)0,(byte)255,(byte)255,(byte)153,(byte)204,(byte)255,(byte)255,(byte)204,(byte)102,(byte)153,(byte)153,(byte)153,(byte)51,(byte)0,(byte)0,(byte)51,(byte)0,(byte)255,(byte)255,(byte)204,(byte)153,(byte)255,(byte)204,(byte)153,(byte)51,(byte)102,(byte)102,(byte)102,(byte)102,(byte)0,(byte)51,(byte)102,(byte)0,(byte)255,(byte)255,(byte)204,(byte)255,(byte)204,(byte)153,(byte)102,(byte)102,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)102,(byte)0,(byte)255,(byte)255,(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)153,(byte)102,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)51,(byte)0,(byte)204,(byte)153,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)204,(byte)153,(byte)102,(byte)51,(byte)6,(byte)12,(byte)18,(byte)24,(byte)30,(byte)36,(byte)42,(byte)48,(byte)54,(byte)60,(byte)66,(byte)72,(byte)78,(byte)86,(byte)92,(byte)100,(byte)106,(byte)112,(byte)118,(byte)124,(byte)130,(byte)136,(byte)142,(byte)148,(byte)154,(byte)160,(byte)166,(byte)172,(byte)178,(byte)184,(byte)190,(byte)196,(byte)202,(byte)208,(byte)214,(byte)220,(byte)226,(byte)232,(byte)238,(byte)244};
	public static byte b[]={(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)51,(byte)102,(byte)102,(byte)51,(byte)0,(byte)51,(byte)0,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)51,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)102,(byte)51,(byte)0,(byte)102,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)0,(byte)0,(byte)51,(byte)51,(byte)51,(byte)51,(byte)102,(byte)153,(byte)102,(byte)0,(byte)102,(byte)51,(byte)0,(byte)102,(byte)102,(byte)102,(byte)51,(byte)0,(byte)102,(byte)102,(byte)102,(byte)51,(byte)153,(byte)204,(byte)153,(byte)0,(byte)51,(byte)0,(byte)0,(byte)51,(byte)153,(byte)153,(byte)102,(byte)0,(byte)153,(byte)153,(byte)153,(byte)102,(byte)102,(byte)204,(byte)204,(byte)0,(byte)0,(byte)0,(byte)51,(byte)102,(byte)153,(byte)204,(byte)153,(byte)51,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)153,(byte)51,(byte)51,(byte)102,(byte)0,(byte)51,(byte)102,(byte)153,(byte)204,(byte)204,(byte)153,(byte)153,(byte)153,(byte)102,(byte)102,(byte)0,(byte)51,(byte)0,(byte)51,(byte)102,(byte)153,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)204,(byte)0,(byte)51,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)153,(byte)102,(byte)204,(byte)204,(byte)51,(byte)102,(byte)51,(byte)102,(byte)153,(byte)255,(byte)204,(byte)204,(byte)204,(byte)255,(byte)204,(byte)255,(byte)204,(byte)153,(byte)255,(byte)255,(byte)102,(byte)153,(byte)102,(byte)102,(byte)255,(byte)204,(byte)153,(byte)102,(byte)153,(byte)255,(byte)204,(byte)153,(byte)153,(byte)204,(byte)255,(byte)255,(byte)153,(byte)204,(byte)153,(byte)255,(byte)204,(byte)153,(byte)102,(byte)153,(byte)153,(byte)255,(byte)204,(byte)153,(byte)102,(byte)204,(byte)255,(byte)255,(byte)204,(byte)204,(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)204,(byte)204,(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)255,(byte)255,(byte)153,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)6,(byte)12,(byte)18,(byte)24,(byte)30,(byte)36,(byte)42,(byte)48,(byte)54,(byte)60,(byte)66,(byte)72,(byte)78,(byte)86,(byte)92,(byte)100,(byte)106,(byte)112,(byte)118,(byte)124,(byte)130,(byte)136,(byte)142,(byte)148,(byte)154,(byte)160,(byte)166,(byte)172,(byte)178,(byte)184,(byte)190,(byte)196,(byte)202,(byte)208,(byte)214,(byte)220,(byte)226,(byte)232,(byte)238,(byte)244};
	
	private Texture Sprite[][]=new Texture[64][8];
	private Texture Tiles[]=new Texture[128];
	
	private BufferedImage BISprite[][]=new BufferedImage[64][8];
	private BufferedImage BITiles[]=new BufferedImage[128];
	
	private GraphicsConfiguration GC=null;
	
	private HashMap StockTextures=new HashMap();//A hash map of our tock textures.

	/**
	Fetch a tile buffered image by ID.
	*/
	public BufferedImage getTileImage(int index){
		return(BITiles[index]);
	}
	
	/**
	Constructor.
	*/
	public OpenGLImageHandler(GraphicsConfiguration GC){
		this.GC=GC;
	}
	
	/**
	Add a stock image to our accessible textures.
	*/
	public void addStockTexture(String file){
		try{
			BufferedImage BI=null;
			BI=ImageIO.read(ImageLoader.getFile(file));
			Texture T=TextureLoader.load(BI);
			StockTextures.put(file,new Object[]{T,BI});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	Return a stock texture based on name.
	*/
	public Texture getStockTexture(String file){
		return((Texture)((Object[])StockTextures.get(file))[0]);
	}
	
	/**
	Reload the textures, this is required if the window loses focus.
	*/
	public void reloadTextures(){
		
	/*	for(int i=0;i<128;i++){
				if(Tiles[i]!=null){
					if(BITiles[i]!=null)
						Tiles[i].updateImage(TextureIO.newTextureData(BITiles[i],true));
				}else{
					
					if(BITiles[i]!=null){
						Tiles[i]=TextureLoader.load(BITiles[i]);
						Tiles[i].enable();
					}
				
				}
		}
		
		Iterator MyIterator=StockTextures.entrySet().iterator();//Reload our stock textures.
		while(MyIterator.hasNext()){
			java.util.Map.Entry Entry=(java.util.Map.Entry)MyIterator.next();
			String file=(String)Entry.getKey();
			Texture T=(Texture)((Object[])Entry.getValue())[0];
			BufferedImage B=(BufferedImage)((Object[])Entry.getValue())[1];
			if(T!=null)
				T.updateImage(TextureIO.newTextureData(B,true));
			else
				T=TextureLoader.load(B);
		}
		
		for(int i=0;i<64;i++){
			for(int ii=0;ii<8;ii++){
				if(Sprite[i][ii]!=null){
					if(BISprite[i][ii]!=null)
						Sprite[i][ii].updateImage(TextureIO.newTextureData(BISprite[i][ii],true));
				}else{
					if(BISprite[i][ii]!=null){
						Sprite[i][ii]=TextureLoader.load(BISprite[i][ii]);
						Sprite[i][ii].enable();
					}
				}
			}
		}*/
		
	}
	
	/**
	flushing, like garbage collecting but better.
	*/
	public void clean(){

	
		if(Sprite!=null){
		for(int ii=0;ii<Sprite.length;ii++)
			for(int i=0;i<Sprite[ii].length;i++){
				if(Sprite[ii][i]!=null){
					Sprite[ii][i]=null;
					BISprite[ii][i].flush();
				}
			}
		}
			
		for(int i=0;i<Tiles.length;i++){
			if(Tiles[i]!=null){
				Tiles[i]=null; 
				BITiles[i].flush();
			}
		}
		
		Iterator MyIterator=StockTextures.entrySet().iterator();//Reload our stock textures.
		while(MyIterator.hasNext()){
			java.util.Map.Entry Entry=(java.util.Map.Entry)MyIterator.next();
			String file=(String)Entry.getKey();
			Texture T=(Texture)((Object[])Entry.getValue())[0];
			BufferedImage B=(BufferedImage)((Object[])Entry.getValue())[1];
			B.flush();
			B=null;
			T=null;
		}
	

	}
	
	/**
	Fetch a sprite image based on an Integer key.
	*/
	public Texture getSpriteImage(int id,int frame){
		return(Sprite[id][frame]);
	}
	
	public BufferedImage getSpriteBufferedImage(int id,int frame){
		return(BISprite[id][frame]);
	}
	
	public void setSpriteBufferedImage(int id,int frame,BufferedImage image){
		BISprite[id][frame]=image;
	}
	
	
	public void setTileBufferedImage(int id,BufferedImage image){
		BITiles[id]=image;
	}
	
	/**
	Fetch a tile based on an Integer key provided.
	*/
	public Texture getTile(int id){
		return(Tiles[id]); 
	}
	
	public void setTile(int id,Texture image){
		Tiles[id]=image;
	}
	
	public void setSpriteImage(int id,int frame,Texture image){
		Sprite[id][frame]=image;
	}
	
	
	public void reset(){
		Sprite=new Texture[64][8];
		Tiles=new Texture[128];
		BISprite=new BufferedImage[64][8];
		BITiles=new BufferedImage[128];
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
			
			Texture Temp=TextureLoader.load(img);
			
			if(frame==-1){
				Tiles[id]=Temp;
				BITiles[id]=img;
			}else{
				Sprite[id][frame]=Temp;
				BISprite[id][frame]=img;
			}
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
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
}//END.
