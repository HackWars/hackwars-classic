package com.plink.Hack3D;
/**
This class controls terrain meshes.
*/

import javax.media.opengl.*;
import com.sun.opengl.util.texture.*;
import com.sun.opengl.util.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import Hacktendo.*;
import java.awt.*;
import java.nio.*;
import GUI.*;


import javax.media.opengl.glu.*;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;
 
public class TerrainHandler{

	public static final int SHADOW_WIDTH=900;
	public static final int SHADOW_HEIGHT=480;
	public static final int TILES_X=45;
	public static final int TILES_Y=24;
	public static final int TILE_SIZE=32;
	public static final int WIDTH=90;
	public static final int HEIGHT=48;
	private float heightStep=0.4f;
	private Object HeightData[][]=null;
	private Object NormalData[][]=null;
	private OpenGLImageHandler MyImageHandler=null;
	private OpenGLRenderEngine MyRenderEngine=null;
	private final Semaphore available = new Semaphore(1, true);//For concurrent access to shared lists.
	
	//Structures used for creating terrain and shadows.
	private Texture AllTerrain=null;
	private Texture DetailTexture=null;	
	private Texture ShadowTexture=null;
	private FloatBuffer textureBuffer=null;
	private FloatBuffer textureDetailBuffer=null;
	private FloatBuffer textureShadowBuffer=null;
	private FloatBuffer vertexBuffer=null;
	private FloatBuffer normalBuffer=null;
	private IntBuffer image=null;
	private GLPbuffer DrawBuffer=null;//An offscreen surface for performing shadow calculations.
		
	//Constructor.
	public TerrainHandler(OpenGLImageHandler MyImageHandler,OpenGLRenderEngine MyRenderEngine){
		this.MyImageHandler=MyImageHandler;
		this.MyRenderEngine=MyRenderEngine;
		HeightData=new Object[WIDTH][HEIGHT];
		NormalData=new Object[WIDTH][HEIGHT];
		for(int i=0;i<WIDTH;i++)
			for(int ii=0;ii<HEIGHT;ii++){
				float newPoint[]=new float[4];
				HeightData[i][ii]=newPoint;
				float newNormal[][]=new float[4][3];
				NormalData[i][ii]=newNormal;
			}
	}
	
	/**
	Load a texture that represents all the current tile terrain.
	*/
	public void loadAllTerrainTexture(){
		BufferedImage Temp=new BufferedImage(1024,512,BufferedImage.TYPE_INT_RGB);
		Graphics G=Temp.getGraphics();
		for(int i=0;i<8;i++){
			for(int ii=0;ii<16;ii++){
				BufferedImage TempImage=MyImageHandler.getTileImage(i*16+ii);
				G.drawImage(TempImage,ii*64,i*64,ii*64+64,i*64+64,0,0,64,64,null);
			}
		}
		G.dispose();
		AllTerrain=TextureLoader.load(Temp);
		Temp.flush();
	}
				
	/**
	Reset the height data.
	*/
	public void resetHeightData(){			
		AllTerrain=null;
		DetailTexture=null;
		for(int i=0;i<WIDTH;i++)
			for(int ii=0;ii<HEIGHT;ii++){
				float newPoint[]=new float[4];
				HeightData[i][ii]=newPoint;
			}
		waterHeight=20;
	}
	
	/**
	Set the image that should be used as a height map
	*/
	public void setHeightMapImage(int heightMapImage){
		
		try{
			available.acquire();
		
			BufferedImage BI=MyImageHandler.getTileImage(heightMapImage);
			BufferedImage Temp=new BufferedImage(90,48,BI.TYPE_INT_RGB);
			Graphics G=Temp.getGraphics();
			G.drawImage(BI,0,0,90,48,0,0,64,64,null);
			G.dispose();
			loadHeightMap(Temp);
			generateNormals();
			Temp.flush();
			Temp=null;
			MyRenderEngine.regenerateLists();
			
			available.release();
		}catch(Exception e){
			e.printStackTrace();
			available.release();
		}
	}
	
	/**
	Load in the terrain height map from a bitmap image.
	*/
	public void loadHeightMap(BufferedImage HeightImage){
		try{
			int ImageData[]=HeightImage.getRGB(0,0,HeightImage.getWidth(),HeightImage.getHeight(),null,0,HeightImage.getWidth());
			PerlinNoise.perlinNoiseBW(HeightImage.getWidth(),HeightImage.getHeight(),(float)1,64,12413,ImageData);
			HeightData=new Object[HeightImage.getWidth()][HeightImage.getHeight()];

			for(int x=0;x<HeightImage.getWidth();x++){
				for(int y=0;y<HeightImage.getHeight();y++){
					float newPoint[]=new float[4];
					float above[]=null;
					float left[]=null;
					if(x>0){
						left=(float[])HeightData[x-1][y];
					}
					
					if(y>0){
						above=(float[])HeightData[x][y-1];
					}
					
					float height=(float)((ImageData[x+(y*HeightImage.getWidth())]&0x00FF0000)>>16);
					
					//First point.
					if(above!=null)
						newPoint[0]=above[3];
					else if(left!=null)
						newPoint[0]=left[1];
					else
						newPoint[0]=height*heightStep;
						
					//Second point.
					if(above!=null)
						newPoint[1]=above[2];
					else
						newPoint[1]=height*heightStep;
						
					//Third Point.
					newPoint[2]=height*heightStep;
					
					//Fourth Point.
					if(left!=null)
						newPoint[3]=left[2];
					else
						newPoint[3]=height*heightStep;

					HeightData[x][y]=newPoint;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	Get whether or not an object is allowed to step on this position of the terrain.
	*/
	public boolean stepAllowed(int tx,int ty,int internalX,int internalY){
	
		float tileSizeX=(float)TILE_SIZE*(float)TILES_X/(float)HeightData.length;
		float tileSizeY=(float)TILE_SIZE*(float)TILES_Y/(float)HeightData[0].length;
		
		float ix=(float)internalX;///16.0f;
		float iy=(float)internalY;///16.0f;

		float height=0.0f;
		
		try{
			available.acquire();
	
			if(tx<0||tx>=HeightData.length||ty<0||ty>=HeightData[0].length){
				available.release();
				return(false);
			}
			
			float data[]=(float[])HeightData[tx][ty];
						
			if(internalX<internalY){

				float v1[]=new float[]{tileSizeX,0.0f,data[2]-data[3]};
				float v2[]=new float[]{0.0f,-tileSizeY,data[0]-data[3]};
				float normal[]=crossProduct(v1,v2);
				
				if(normal[2]>-0.75){
					available.release();
					return(false);
				}
			}else{

				float v1[]=new float[]{-tileSizeX,0.0f,data[0]-data[1]};
				float v2[]=new float[]{0.0f,tileSizeY,data[2]-data[1]};
				float normal[]=crossProduct(v1,v2);
				
				if(normal[2]>-0.75){
					available.release();
					return(false);
				}
			}
			
			available.release();

		}catch(Exception e){
			e.printStackTrace();
			available.release();
		}
		
		return(true);
	}
	
	/**
	Get the height of a given tile based on the current terrain loaded in.
	*/
	public float getHeight(int tx,int ty,int internalX,int internalY){
	
		float tileSizeX=(float)TILE_SIZE*(float)TILES_X/(float)HeightData.length;
		float tileSizeY=(float)TILE_SIZE*(float)TILES_Y/(float)HeightData[0].length;
		
		float ix=(float)internalX;///16.0f;
		float iy=(float)internalY;///16.0f;

		float height=0.0f;
		
		try{
			available.acquire();
	
			if(tx<0||tx>=HeightData.length||ty<0||ty>=HeightData[0].length){
				available.release();
				return(0.0f);
			}
			
			float data[]=(float[])HeightData[tx][ty];
						
			if(internalX<internalY){

				float v1[]=new float[]{tileSizeX,0.0f,data[2]-data[3]};
				float v2[]=new float[]{0.0f,-tileSizeY,data[0]-data[3]};
				float normal[]=crossProduct(v1,v2);
								
				height=data[3]+(normal[0]*ix+normal[1]*(iy-tileSizeY))/(-normal[2]);
			}else{

				float v1[]=new float[]{-tileSizeX,0.0f,data[0]-data[1]};
				float v2[]=new float[]{0.0f,tileSizeY,data[2]-data[1]};
				float normal[]=crossProduct(v1,v2);
				
				height=data[1]+(normal[0]*(ix-tileSizeX)+normal[1]*iy)/(-normal[2]);
			}
								
			available.release();
			
		}catch(Exception e){
			e.printStackTrace();
			available.release();
		}
		
		return((float)height);
	}
	
	/**
	Set the z level that the water should be rendered at.
	*/
	private int waterHeight=20;
	public void setWaterHeight(int waterHeight){
		this.waterHeight=waterHeight;
	}
	
	public int getWaterHeight(){
		return(waterHeight);
	}
	
	/**
	It can be specified that below a specific point on the map be treated as a water layer.
	*/
	
	private Object WaterHeight[][]=new Object[16][16];
	private int WaterData[]=null;
	private float WaterAdd[]=new float[256];
	private float WaterCount[]=new float[256];
	private int waveCount=0;
	private static final int WAVE_SIZE=16;
	
	public void renderWater(GL gl){
		if(WaterData==null){//Move the waves.
			WaterData=new int[256];
			for(int i=0;i<256;i++){
				WaterData[i]=(int)(Math.random()*256);
				WaterAdd[i]=(float)Math.random();
			}
		}else{
			for(int i=0;i<256;i++){
				WaterData[i]=(int)(Math.sin(WaterCount[i]/10)*256);
				WaterCount[i]+=WaterAdd[i];
			}
		}
	
		for(int x=0;x<WAVE_SIZE;x++){//Calculate the current wave state.
			for(int y=0;y<WAVE_SIZE;y++){
				float newPoint[]=new float[4];
				float above[]=null;
				float left[]=null;
				if(x>0){
					left=(float[])WaterHeight[x-1][y];
				}
				
				if(y>0){
					above=(float[])WaterHeight[x][y-1];
				}
				
				float height=(float)WaterData[x+(y*WAVE_SIZE)]/20;
				
				//First point.
				if(above!=null)
					newPoint[0]=above[3];
				else if(left!=null)
					newPoint[0]=left[1];
				else
					newPoint[0]=height*heightStep;
					
				//Second point.
				if(above!=null)
					newPoint[1]=above[2];
				else
					newPoint[1]=height*heightStep;
					
				//Third Point.
				newPoint[2]=height*heightStep;
				
				//Fourth Point.
				if(left!=null)
					newPoint[3]=left[2];
				else
					newPoint[3]=height*heightStep;

				WaterHeight[x][y]=newPoint;
			}
		}
		
		float tileSizeX=(float)TILE_SIZE*(float)TILES_X/(float)WaterHeight.length;
		float tileSizeY=(float)TILE_SIZE*(float)TILES_Y/(float)WaterHeight[0].length;
						
		float startX=0;
		float startY=0;

		float currentY=startY;
		float currentX=startX;
		
		//Water.bind();
		gl.glDisable(gl.GL_TEXTURE_2D);
		gl.glNormal3f(0.0f,0.0f,-1.0f);

		for(int y=0;y<WAVE_SIZE;y++){//Actually render the surface.
			
			currentX=startX;
			
			for(int x=0;x<WAVE_SIZE;x++){
				float data[]=(float[])WaterHeight[x][y];
				
				float percentX=(float)x/(float)WaterHeight.length;
				float percentY=(float)y/(float)WaterHeight[0].length;
				
				gl.glPushMatrix();
				gl.glBegin(GL.GL_POLYGON);
				gl.glColor4f(0.0f,0.3f,0.7f,0.8f);
				
				gl.glVertex3f(currentX,currentY,-data[0]-4-waterHeight);
				gl.glVertex3f(currentX,currentY+tileSizeY,-data[3]-4-waterHeight);
				gl.glVertex3f(currentX+tileSizeX,currentY+tileSizeY,-data[2]-4-waterHeight);
				gl.glVertex3f(currentX+tileSizeX,currentY,-data[1]-4-waterHeight);
				
				gl.glEnd();
				gl.glPopMatrix();
								
				currentX+=tileSizeX;
			}
			currentY+=tileSizeY;
		}

		gl.glEnable(gl.GL_TEXTURE_2D);
	}
	
	/**
	Generate the vertex based normals for the terrain.
	*/
	public void generateNormals(){
		float tileSizeX=(float)TILE_SIZE*(float)TILES_X/(float)HeightData.length;
		float tileSizeY=(float)TILE_SIZE*(float)TILES_Y/(float)HeightData[0].length;
	
		for(int width=0;width<WIDTH;width++)
			for(int height=0;height<HEIGHT;height++){
			
				float TL[]=null;
				float TM[]=null;
				float TR[]=null;
				float ML[]=null;
				float MM[]=null;
				float MR[]=null;
				float BL[]=null;
				float BM[]=null;
				float BR[]=null;
			
				//Get the top row.
				if(width-1>0&&height-1>0)
					TL=(float[])HeightData[width-1][height-1];
				if(width>0&&height-1>0)
					TM=(float[])HeightData[width][height-1];
				if(width+1<WIDTH&&height-1>0)
					TR=(float[])HeightData[width+1][height-1];
					
				//Get the middle row.
				if(width-1>0)
					ML=(float[])HeightData[width-1][height];
				MM=(float[])HeightData[width][height];
				if(width+1<WIDTH)
					MR=(float[])HeightData[width+1][height];
					
				//Get the bottom row.
				if(width-1>0&&height+1<HEIGHT)
					BL=(float[])HeightData[width-1][height+1];
				if(height+1<HEIGHT)
					BM=(float[])HeightData[width][height+1];
				if(width+1<WIDTH&&height+1<HEIGHT)
					BR=(float[])HeightData[width+1][height+1];
					
				{//Generate the normal for the first point.
					float nCount=0.0f;
					float vavg[]=new float[]{0.0f,0.0f,0.0f};
					if(TL!=null){
						float v1[]=new float[]{-tileSizeX,0.0f,TL[3]-MM[0]};
						float v2[]=new float[]{0.0f,-tileSizeY,TL[1]-MM[0]};
						float result[]=crossProduct(v2,v1);
						vavg[0]+=result[0];
						vavg[1]+=result[1];
						vavg[2]+=result[2];
						nCount+=1.0f;
					}
					if(TM!=null){
						float v1[]=new float[]{0.0f,-tileSizeY,TM[0]-MM[0]};
						float v2[]=new float[]{tileSizeX,0.0f,TM[2]-MM[0]};
						float result[]=crossProduct(v2,v1);
						vavg[0]+=result[0];
						vavg[1]+=result[1];
						vavg[2]+=result[2];
						nCount+=1.0f;
					}
					
					if(ML!=null){
						float v1[]=new float[]{0.0f,tileSizeY,ML[2]-MM[0]};
						float v2[]=new float[]{-tileSizeX,0.0f,ML[0]-MM[0]};
						float result[]=crossProduct(v2,v1);
						vavg[0]+=result[0];
						vavg[1]+=result[1];
						vavg[2]+=result[2];
						nCount+=1.0f;
					}
					
					
					float v1[]=new float[]{0.0f,tileSizeY,MM[3]-MM[0]};
					float v2[]=new float[]{tileSizeX,0.0f,MM[1]-MM[0]};
					float result[]=crossProduct(v1,v2);
					vavg[0]+=result[0];
					vavg[1]+=result[1];
					vavg[2]+=result[2];
					nCount+=1.0f;
					
					//Now average and normalize the vector. Congrats one point found.
					result[0]=vavg[0]/nCount;
					result[1]=vavg[1]/nCount;
					result[2]=vavg[2]/nCount;
					float total=(float)Math.sqrt(result[0]*result[0]+result[1]*result[1]+result[2]*result[2]);
					result[0]=result[0]/total;
					result[1]=result[1]/total;
					result[2]=result[2]/total;
					
					float[][] data=(float[][])NormalData[width][height];
					data[0]=result;
				}
				
					
				{//Generate the normal for the second point.
					float nCount=0.0f;
					float vavg[]=new float[]{0.0f,0.0f,0.0f};
					if(TM!=null){
						float v1[]=new float[]{-tileSizeX,0.0f,TM[3]-MM[1]};
						float v2[]=new float[]{0.0f,-tileSizeY,TM[1]-MM[1]};
						float result[]=crossProduct(v2,v1);
						vavg[0]+=result[0];
						vavg[1]+=result[1];
						vavg[2]+=result[2];
						nCount+=1.0f;
					}
					if(TR!=null){
						float v1[]=new float[]{0.0f,-tileSizeY,TR[0]-MM[1]};
						float v2[]=new float[]{tileSizeX,0.0f,TR[2]-MM[1]};
						float result[]=crossProduct(v2,v1);
						vavg[0]+=result[0];
						vavg[1]+=result[1];
						vavg[2]+=result[2];
						nCount+=1.0f;
					}
					
					if(MR!=null){
						float v1[]=new float[]{0.0f,tileSizeY,MR[3]-MM[1]};
						float v2[]=new float[]{tileSizeX,0.0f,MR[1]-MM[1]};
						float result[]=crossProduct(v1,v2);
						vavg[0]+=result[0];
						vavg[1]+=result[1];
						vavg[2]+=result[2];
						nCount+=1.0f;
					}
					
					float v1[]=new float[]{0.0f,tileSizeY,MM[2]-MM[1]};
					float v2[]=new float[]{-tileSizeX,0.0f,MM[0]-MM[1]};
					float result[]=crossProduct(v2,v1);
					vavg[0]+=result[0];
					vavg[1]+=result[1];
					vavg[2]+=result[2];
					nCount+=1.0f;

					//Now average and normalize the vector. Congrats one point found.
					result[0]=vavg[0]/nCount;
					result[1]=vavg[1]/nCount;
					result[2]=vavg[2]/nCount;
					float total=(float)Math.sqrt(result[0]*result[0]+result[1]*result[1]+result[2]*result[2]);
					result[0]=result[0]/total;
					result[1]=result[1]/total;
					result[2]=result[2]/total;
					
					float[][] data=(float[][])NormalData[width][height];
					data[1]=result;
				}
				
				{//Generate the normal for the third point.
					float nCount=0.0f;
					float vavg[]=new float[]{0.0f,0.0f,0.0f};

					if(MR!=null){
						float v1[]=new float[]{0.0f,-tileSizeY,MR[0]-MM[2]};
						float v2[]=new float[]{tileSizeX,0.0f,MR[2]-MM[2]};
						float result[]=crossProduct(v2,v1);
						vavg[0]+=result[0];
						vavg[1]+=result[1];
						vavg[2]+=result[2];
						nCount+=1.0f;
					}
					
					if(BR!=null){
						float v1[]=new float[]{0.0f,tileSizeY,BR[3]-MM[2]};
						float v2[]=new float[]{tileSizeX,0.0f,BR[1]-MM[2]};
						float result[]=crossProduct(v1,v2);
						vavg[0]+=result[0];
						vavg[1]+=result[1];
						vavg[2]+=result[2];
						nCount+=1.0f;
					}
					
					if(BM!=null){
						float v1[]=new float[]{0.0f,tileSizeY,BM[2]-MM[2]};
						float v2[]=new float[]{-tileSizeX,0.0f,BM[0]-MM[2]};
						float result[]=crossProduct(v2,v1);
						vavg[0]+=result[0];
						vavg[1]+=result[1];
						vavg[2]+=result[2];
						nCount+=1.0f;
					}
					
					
					float v1[]=new float[]{-tileSizeX,0.0f,MM[3]-MM[2]};
					float v2[]=new float[]{0.0f,-tileSizeY,MM[1]-MM[2]};
					float result[]=crossProduct(v2,v1);
					vavg[0]+=result[0];
					vavg[1]+=result[1];
					vavg[2]+=result[2];
					nCount+=1.0f;
				

					//Now average and normalize the vector. Congrats one point found.
					result[0]=vavg[0]/nCount;
					result[1]=vavg[1]/nCount;
					result[2]=vavg[2]/nCount;
					float total=(float)Math.sqrt(result[0]*result[0]+result[1]*result[1]+result[2]*result[2]);
					result[0]=result[0]/total;
					result[1]=result[1]/total;
					result[2]=result[2]/total;
					
					float[][] data=(float[][])NormalData[width][height];
					data[2]=result;
				}
				
				{//Generate the normal for the third point.
					float nCount=0.0f;
					float vavg[]=new float[]{0.0f,0.0f,0.0f};
					
					if(ML!=null){
						float v1[]=new float[]{-tileSizeX,0.0f,ML[3]-MM[3]};
						float v2[]=new float[]{0.0f,-tileSizeY,ML[1]-MM[3]};
						float result[]=crossProduct(v2,v1);
						vavg[0]+=result[0];
						vavg[1]+=result[1];
						vavg[2]+=result[2];
						nCount+=1.0f;
					}

					
					if(BM!=null){
						float v1[]=new float[]{0.0f,tileSizeY,BM[3]-MM[3]};
						float v2[]=new float[]{tileSizeX,0.0f,BM[1]-MM[3]};
						float result[]=crossProduct(v1,v2);
						vavg[0]+=result[0];
						vavg[1]+=result[1];
						vavg[2]+=result[2];
						nCount+=1.0f;
					}
					
					
					if(BL!=null){
						float v1[]=new float[]{0.0f,tileSizeY,BL[2]-MM[3]};
						float v2[]=new float[]{-tileSizeX,0.0f,BL[0]-MM[3]};
						float result[]=crossProduct(v2,v1);
						vavg[0]+=result[0];
						vavg[1]+=result[1];
						vavg[2]+=result[2];
						nCount+=1.0f;
					}
					
					float v1[]=new float[]{0.0f,-tileSizeY,MM[0]-MM[3]};
					float v2[]=new float[]{tileSizeX,0.0f,MM[2]-MM[3]};
					float result[]=crossProduct(v2,v1);
					vavg[0]+=result[0];
					vavg[1]+=result[1];
					vavg[2]+=result[2];
					nCount+=1.0f;

					//Now average and normalize the vector. Congrats one point found.
					result[0]=vavg[0]/nCount;
					result[1]=vavg[1]/nCount;
					result[2]=vavg[2]/nCount;
					float total=(float)Math.sqrt(result[0]*result[0]+result[1]*result[1]+result[2]*result[2]);
					result[0]=result[0]/total;
					result[1]=result[1]/total;
					result[2]=result[2]/total;
					
					float[][] data=(float[][])NormalData[width][height];
					data[3]=result;
				}
				
				
			}
	}
	
	/**
	Prints a vector.
	*/
	public void printVector(float[] vector){
		System.out.print("Vector{");
		for(int i=0;i<3;i++){
			System.out.print(vector[i]);
			if(i!=2)
				System.out.print(",");
		}
		System.out.println("}");
	}
	
	/**
	Render the height map.
	*/
	TextureData TD=null;

	public void render(GL gl,GLU glu,OpenGLImageHandler MyImageHandler,Map MyMap,int sx,int sy,int ex,int ey,int renderListID){
		if(AllTerrain==null){
			loadAllTerrainTexture();
		}
		
		if(DetailTexture==null){
			try{
				BufferedImage BI=null;
				BI=ImageIO.read(ImageLoader.getFile("images/detail_texture.png"));
				DetailTexture=TextureLoader.load(BI);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		if(image!=null&&sx==0){
				if(TD==null){
					TD=new TextureData(GL.GL_RGBA,SHADOW_WIDTH,SHADOW_HEIGHT,0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE,true,false,false,image,null);
					ShadowTexture=TextureIO.newTexture(TD);
				}else{
					TD.flush();
					TD.setBuffer(image);
					ShadowTexture.updateImage(TD);
				}
		}
		
		float tileSizeX=(float)TILE_SIZE*(float)TILES_X/(float)HeightData.length;
		float tileSizeY=(float)TILE_SIZE*(float)TILES_Y/(float)HeightData[0].length;
								
		float startX=(float)sx*tileSizeX;
		float startY=(float)sy*tileSizeY;

		float currentY=startY;
		float currentX=startX;
			
		//We can generate data on vertex arrays in an attempt to save memory.
		
		float textures[]=new float[(ey-sy)*(ex-sx)*2*4];
		float texturesDetail[]=new float[(ey-sy)*(ex-sx)*2*4];
		float texturesShadow[]=new float[(ey-sy)*(ex-sx)*2*4];
		float vertices[]=new float[(ey-sy)*(ex-sx)*3*4];
		float normals[]=new float[(ey-sy)*(ex-sx)*3*4];
		
		float addShadowX=1.0f/90.0f;
		float addShadowY=1.0f/48.0f;
		//Texture T=null;

		int intertwinedPosition=0;
		
		float yTex=1.0f/(ey-sy);
		float xTex=1.0f/(ex-sx);
								
		for(int y=sy;y<ey;y++){
			
			currentX=startX;
			
			for(int x=sx;x<ex;x++){
				float[][] normal_data=(float[][])NormalData[x][y];
				float data[]=(float[])HeightData[x][y];
				
				float xf=(float)x;
				float tXMod=(float)xf%(float)((float)HeightData.length/45.0f);
				float stepX=1.0f/(float)((float)HeightData.length/45.0f);
				float initialX=tXMod*stepX;
			
											
				float stepY=1.0f/(float)((float)HeightData[0].length/24.0f);
				float yf=(float)y;
				float tYMod=(float)yf%(float)((float)HeightData[0].length/24.0f);
				float initialY=tYMod*stepY;
				
				float percentX=(float)xf/(float)HeightData.length;
				float percentY=(float)yf/(float)HeightData[0].length;
	
				//Find out the texture ID associated with this tile and build an offset based on it.
				int TileIndex=MyMap.getTile((int)((float)TILES_X*percentX)+(int)((float)TILES_Y*percentY)*TILES_X);
				float xTIOff=((float)(TileIndex%16))*(1.0f/16.0f);
				float yTIOff=((float)(TileIndex/16))*(1.0f/8.0f);

				if(AllTerrain!=null){
				
					textures[intertwinedPosition*2]=(initialX/16.0f)+xTIOff;
					textures[intertwinedPosition*2+1]=(initialY/8.0f)+yTIOff;
					texturesDetail[intertwinedPosition*2]=0.0f;
					texturesDetail[intertwinedPosition*2+1]=0.0f;
					texturesShadow[intertwinedPosition*2]=x*addShadowX;
					texturesShadow[intertwinedPosition*2+1]=1.0f-(y*addShadowY);
					normals[intertwinedPosition*3]=normal_data[0][0];
					normals[intertwinedPosition*3+1]=normal_data[0][1];
					normals[intertwinedPosition*3+2]=normal_data[0][2];
					vertices[intertwinedPosition*3]=currentX;
					vertices[intertwinedPosition*3+1]=currentY;
					vertices[intertwinedPosition*3+2]=-data[0]-4;
					
					textures[intertwinedPosition*2+2]=(initialX/16.0f)+xTIOff;
					textures[intertwinedPosition*2+3]=(initialY/8.0f)+(stepY/8.0f)+yTIOff;
					texturesDetail[intertwinedPosition*2+2]=0.0f;
					texturesDetail[intertwinedPosition*2+3]=1.0f;
					texturesShadow[intertwinedPosition*2+2]=x*addShadowX;
					texturesShadow[intertwinedPosition*2+3]=1.0f-(y*addShadowY+addShadowY);
					normals[intertwinedPosition*3+3]=normal_data[3][0];
					normals[intertwinedPosition*3+4]=normal_data[3][1];
					normals[intertwinedPosition*3+5]=normal_data[3][2];
					vertices[intertwinedPosition*3+3]=currentX;
					vertices[intertwinedPosition*3+4]=currentY+tileSizeY;
					vertices[intertwinedPosition*3+5]=-data[3]-4;

					textures[intertwinedPosition*2+4]=(initialX/16.0f)+(stepX/16.0f)+xTIOff;
					textures[intertwinedPosition*2+5]=(initialY/8.0f)+(stepY/8.0f)+yTIOff;
					texturesDetail[intertwinedPosition*2+4]=1.0f;
					texturesDetail[intertwinedPosition*2+5]=1.0f;
					texturesShadow[intertwinedPosition*2+4]=x*addShadowX+addShadowX;
					texturesShadow[intertwinedPosition*2+5]=1.0f-(y*addShadowY+addShadowY);
					normals[intertwinedPosition*3+6]=normal_data[2][0];
					normals[intertwinedPosition*3+7]=normal_data[2][1];
					normals[intertwinedPosition*3+8]=normal_data[2][2];
					vertices[intertwinedPosition*3+6]=currentX+tileSizeX;
					vertices[intertwinedPosition*3+7]=currentY+tileSizeY;
					vertices[intertwinedPosition*3+8]=-data[2]-4;
				
					textures[intertwinedPosition*2+6]=(initialX/16.0f)+(stepX/16.0f)+xTIOff;
					textures[intertwinedPosition*2+7]=(initialY/8.0f)+yTIOff;
					texturesDetail[intertwinedPosition*2+6]=1.0f;
					texturesDetail[intertwinedPosition*2+7]=0.0f;
					texturesShadow[intertwinedPosition*2+6]=x*addShadowX+addShadowX;
					texturesShadow[intertwinedPosition*2+7]=1.0f-(y*addShadowY);
					normals[intertwinedPosition*3+9]=normal_data[1][0];
					normals[intertwinedPosition*3+10]=normal_data[1][1];
					normals[intertwinedPosition*3+11]=normal_data[1][2];
					vertices[intertwinedPosition*3+9]=currentX+tileSizeX;
					vertices[intertwinedPosition*3+10]=currentY;
					vertices[intertwinedPosition*3+11]=-data[1]-4;
					
					intertwinedPosition+=4;
				}
								
				currentX+=tileSizeX;
			}
			currentY+=tileSizeY;
		}

		if(textureBuffer==null)
			textureBuffer = BufferUtil.newFloatBuffer(textures.length);
		else
			textureBuffer.rewind();
			
		for(int i=0;i<textures.length;i++)
			textureBuffer.put(textures[i]);
		textureBuffer.rewind();
		
		if(textureDetailBuffer==null)
			textureDetailBuffer = BufferUtil.newFloatBuffer(texturesDetail.length);
		else
			textureDetailBuffer.rewind();
			
		for(int i=0;i<texturesDetail.length;i++)
			textureDetailBuffer.put(texturesDetail[i]);
		textureDetailBuffer.rewind();
		
		if(textureShadowBuffer==null)
			textureShadowBuffer = BufferUtil.newFloatBuffer(texturesShadow.length);
		else
			textureShadowBuffer.rewind();
			
		for(int i=0;i<texturesShadow.length;i++)
			textureShadowBuffer.put(texturesShadow[i]);
		textureShadowBuffer.rewind();
		
		if(vertexBuffer==null)
			vertexBuffer = BufferUtil.newFloatBuffer(vertices.length);
		else
			vertexBuffer.rewind();
			
		for(int i=0;i<vertices.length;i++)
			vertexBuffer.put(vertices[i]);
		vertexBuffer.rewind();
		
		if(normalBuffer==null)
			normalBuffer = BufferUtil.newFloatBuffer(normals.length);
		else
			normalBuffer.rewind();
			
		for(int i=0;i<normals.length;i++)
			normalBuffer.put(normals[i]);
		normalBuffer.rewind();
		
	
		gl.glPushMatrix();
		gl.glNewList(renderListID, GL.GL_COMPILE);//Start the render list.
		
		gl.glTexCoordPointer(2,GL.GL_FLOAT,0,textureBuffer);
		gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertexBuffer);
		gl.glNormalPointer(GL.GL_FLOAT, 0, normalBuffer);

		//Actually render the terrain.
		gl.glLightfv(gl.GL_LIGHT1, gl.GL_POSITION,new float[]{1.5f,2.0f,-3.0f,1.0f},0);
		AllTerrain.bind();
		gl.glDrawArrays(GL.GL_QUADS, 0, (ey-sy)*(ex-sx)*4);
		
	/*	gl.glTexCoordPointer(2,GL.GL_FLOAT,0,textureDetailBuffer);
		DetailTexture.bind();
		gl.glDrawArrays(GL.GL_QUADS, 0, (ey-sy)*(ex-sx)*4);
		
		if(image!=null){
			gl.glTexCoordPointer(2,GL.GL_FLOAT,0,textureShadowBuffer);
			ShadowTexture.bind();
			gl.glDrawArrays(GL.GL_QUADS, 0, (ey-sy)*(ex-sx)*4);
		}*/
						
		gl.glEndList();				

		gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL.GL_NORMAL_ARRAY);
		gl.glPopMatrix();

	}
	
	//These structures are used in creating a texture.
	public void generateShadows(GLAutoDrawable drawable,GLU glu){
		if(GLDrawableFactory.getFactory().canCreateGLPbuffer()){//Can we create our shadows buffer?
			try{

				//Create an offscreen surface for generating shadows.
				if(DrawBuffer==null){
					GLCapabilities caps = new GLCapabilities();
					caps.setDoubleBuffered(true);// request double buffer display mode
					caps.setHardwareAccelerated(true);
					DrawBuffer=GLDrawableFactory.getFactory().createGLPbuffer(caps,null,SHADOW_WIDTH,SHADOW_HEIGHT,drawable.getContext());
				}
				
				GLContext Context=DrawBuffer.getContext();
				Context.makeCurrent();
				GL gl2=Context.getGL();
								
				//Intitialize this PBuffer.
				gl2.glEnable(GL.GL_TEXTURE_2D);
				gl2.glClearDepth(1.0f);
				gl2.glEnable(GL.GL_DEPTH_TEST);
				gl2.glDepthFunc(GL.GL_LEQUAL);
				gl2.glBlendFunc (GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
				gl2.glEnable (GL.GL_BLEND);
				gl2.glEnable( GL.GL_COLOR_MATERIAL);
				gl2.glCullFace(GL.GL_BACK); 
				gl2.glEnable( GL.GL_COLOR_MATERIAL);
				gl2.glCullFace(GL.GL_BACK); 
				
				//Intialize the viewport.
				gl2.glMatrixMode(GL.GL_PROJECTION);
				gl2.glLoadIdentity();
				glu.gluPerspective(45.0f, (float)SHADOW_WIDTH/SHADOW_HEIGHT, 1.0f, 50000.0f);
				
				gl2.glMatrixMode(GL.GL_MODELVIEW);
				gl2.glLoadIdentity();

				gl2.glViewport(0, 0, SHADOW_WIDTH, SHADOW_HEIGHT);
				gl2.glClearColor(0.0f,0.0f,0.0f,0.0f);
				gl2.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
				
				glu.gluLookAt(720,384,-920.0f,720,384,920.0f,0.0f,-1.0f,0.0f);
				
				if(image==null)
					image = BufferUtil.newIntBuffer( SHADOW_WIDTH * SHADOW_HEIGHT );
				else
					image.rewind();
					
				MyRenderEngine.renderShadowsNoCull(gl2);
				gl2.glReadPixels( 0, 0, SHADOW_WIDTH, SHADOW_HEIGHT, gl2.GL_RGBA, gl2.GL_UNSIGNED_BYTE, image );
												
				Context.release();
			}catch(Exception e){
			}
		}
	}
	
	/**
	Performs the cross product on two 3D vectors.
	*/
	public float[] crossProduct(float A[],float B[]){
		float result[]=new float[3];
		result[0]=A[1]*B[2]-A[2]*B[1];
		result[1]=A[2]*B[0]-A[0]*B[2];
		result[2]=A[0]*B[1]-A[1]*B[0];
		float total=(float)Math.sqrt(result[0]*result[0]+result[1]*result[1]+result[2]*result[2]);
		result[0]=result[0]/total;
		result[1]=result[1]/total;
		result[2]=result[2]/total;
		return(result);
	}
}
 