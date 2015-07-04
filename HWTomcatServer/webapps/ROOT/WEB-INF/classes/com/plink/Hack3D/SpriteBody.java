package com.plink.Hack3D;
/**
This tool is used to load in models.
*/

import javax.media.opengl.*;
import com.sun.opengl.util.texture.*;
import com.sun.opengl.util.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.nio.*;

import javax.media.opengl.glu.*;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import GUI.*;

import java.util.*;
import Hacktendo.OpenGLImageHandler;
public class SpriteBody{
	private HashMap Vertices=new HashMap();//Holds vertex data.
	private HashMap ObjectHash=new HashMap();//Holds materials in a HashMap.
	private HashMap ParentChild=new HashMap();
	private HashMap ObjectBuffers=new HashMap();
	
	//Constants.
	private static int PARENT_CHILD=0;
	private static int VERTICES=1;
	private static int OBJECTS=2;
	private static int NONE=3;
	private int currentParse=NONE;

	/**
	This tool loads a model in based on our model format.
	*/
	public void load(String modelPath){
	
		boolean sphere=false;
		try{
		//	File F=ImageLoader.getFile(modelPath);
		//	BufferedReader BR=new BufferedReader(new FileReader(modelPath));

			BufferedReader BR=new BufferedReader(new FileReader(new File("body.christ")));
				
			String data;
			while((data=BR.readLine())!=null){
				if(data.length()==0){//Back to regular mode.
					currentParse=NONE;
					continue;
				}
				
				if(data.contains("PARENT,CHILD")){
					currentParse=PARENT_CHILD;
					continue;
				}
				
				if(data.contains("#VERTICES")){
					currentParse=VERTICES;
					continue;
				}	
		
				if(data.contains("#OBJECT ID")){
					currentParse=OBJECTS;
					continue;
				}
				
				if(currentParse==VERTICES){
				
					String splitData[]=data.split(",");
					
					float O[]=new float[]{new Float(splitData[1]),new Float(splitData[2]),new Float(splitData[3]),new Float(splitData[4]),new Float(splitData[5]),new Float(splitData[6]),new Float(0.0f),new Float(0.0f)};
					
					Vertices.put(splitData[0],O);
				}
				
				if(currentParse==OBJECTS){
				
					String splitData[]=data.split(",");
										
					String O[]=new String[splitData.length-2];
					for(int i=2;i<splitData.length;i++){
						O[i-2]=splitData[i];
					}
					
					ObjectHash.put(splitData[0],O);
				}
				
				if(currentParse==PARENT_CHILD){
				
					String splitData[]=data.split(",");
					ArrayList Children=(ArrayList)ParentChild.get(splitData[0]);
					if(Children==null){
						Children=new ArrayList();
						ParentChild.put(splitData[0],Children);
					}
					Children.add(new Object[]{splitData[1],new Float(splitData[2]),new Float(splitData[3]),new Float(splitData[4])});
				}
			}
			
			BR.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		 
	}
	
	/**
	Build the vertex arrays used to render the body parts.
	*/
	public void buildBuffers(GL gl){
		Iterator MyIterator=ObjectHash.entrySet().iterator();
		while(MyIterator.hasNext()){
			Map.Entry Entry=(Map.Entry)MyIterator.next();
			String data[]=(String[])Entry.getValue();
			float textures[]=new float[data.length*2];
			float vertices[]=new float[data.length*3];
			float normals[]=new float[data.length*3];
			
			int countVert=0;
			int countTex=0;
			for(int i=0;i<data.length;i++){
			
				float fdata[]=(float[])Vertices.get(data[i]);
				vertices[countVert]=fdata[0];
				vertices[countVert+1]=fdata[1];
				vertices[countVert+2]=fdata[2];
				
				normals[countVert]=fdata[3];
				normals[countVert+1]=fdata[4];
				normals[countVert+2]=fdata[5];
				
				textures[countTex]=fdata[6];
				textures[countTex+1]=fdata[7];
				
				countTex+=2;
				countVert+=3;
			}

			FloatBuffer textureBuffer=BufferUtil.newFloatBuffer(textures.length);
			FloatBuffer vertexBuffer=BufferUtil.newFloatBuffer(vertices.length);
			FloatBuffer normalBuffer=BufferUtil.newFloatBuffer(normals.length);
		
			for(int i=0;i<normals.length;i++)
				normalBuffer.put(normals[i]);
			normalBuffer.rewind();
			
			for(int i=0;i<vertices.length;i++)
				vertexBuffer.put(vertices[i]);
			vertexBuffer.rewind();
			
			for(int i=0;i<textures.length;i++)
				textureBuffer.put(textures[i]);
			textureBuffer.rewind();

			ObjectBuffers.put(Entry.getKey(),new Object[]{new Integer(data.length),vertexBuffer,normalBuffer,textureBuffer});
		}
		
	}
		
	/**
	This renders the face in its current state.
	*/
	public void render(GL gl,OpenGLImageHandler MyImageHandler,HashMap Animations,SpriteFace MySpriteFace){
	

		gl.glPushMatrix();
		gl.glDisable(gl.GL_TEXTURE_2D);

		gl.glTranslatef(0.0f,10.0f,0.0f);
		gl.glColor4f(1.0f,0.0f,0.0f,1.0f);
		gl.glRotatef(90.0f,1.0f,0.0f,0.0f);
		ArrayList Work=new ArrayList();
		Work.add(new Object[]{"1",new Float(0.0f),new Float(0.0f),new Float(0.0f),new Float(0.0f),new Float(0.0f),new Float(0.0f)});
		
		
		int count=0;
		while(Work.size()>0){
			gl.glPushMatrix();

			
			Object[] CurrentWork=(Object[])Work.remove(0);
						
			ArrayList Transform=(ArrayList)Animations.get(CurrentWork[0]);
			
			float tx=0.0f;
			float ty=0.0f;
			float tz=0.0f;
			float rotX=0.0f;
			float rotY=0.0f;
			float rotZ=0.0f;
			/*
			for(int i=0;i<Transform.size();i++){
			
				Object O[]=(Object[])Transform.get(i);
				int type=(Integer)O[0];
				int axis=(Integer)O[1];
				float translation=(Float)O[2];
				float axisData[]=new float[]{0.0f,0.0f,0.0f};

			
				if(translation!=0.0){
					if(type==0){
						if(axis==0)
							rotX=translation;
						else if(axis==1)
							rotY=translation;
						else if(axis==2)
							rotZ=translation;
					
				
					}else{
						if(axis==0)
							tx=translation/10.0f;
						else if(axis==1)
							ty=translation/10.0f;
						else if(axis==2)
							tz=translation/10.0f;
					}
				}
			}*/
			
			gl.glTranslatef((Float)CurrentWork[1]+tx,(Float)CurrentWork[2]+ty,(Float)CurrentWork[3]+tz);
			
			if(rotX!=0.0)
				gl.glRotatef(rotX,1.0f,0.0f,0.0f);
			if(rotY!=0.0)
				gl.glRotatef(rotY,0.0f,1.0f,0.0f);
			if(rotZ!=0.0)
				gl.glRotatef(rotZ,0.0f,0.0f,1.0f);
		
			Object O[]=(Object[])ObjectBuffers.get(CurrentWork[0]);
			int size=(Integer)O[0];
			FloatBuffer vertexBuffer=(FloatBuffer)O[1];
			FloatBuffer normalBuffer=(FloatBuffer)O[2];
			FloatBuffer textureBuffer=(FloatBuffer)O[3];

			gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
			//gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
			
			//gl.glTexCoordPointer(2,GL.GL_FLOAT,0,textureBuffer);
			gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertexBuffer);
			gl.glNormalPointer(GL.GL_FLOAT, 0, normalBuffer);
			//DetailTexture.bind();
			
			if(count!=13){
				gl.glDrawArrays(GL.GL_TRIANGLES, 0, size);
			}else{
				MySpriteFace.render(gl,null);
			}
			
			gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL.GL_NORMAL_ARRAY);

			//gl.glDisable(gl.GL_TEXTURE_2D);
			ArrayList MyChildren=(ArrayList)ParentChild.get(CurrentWork[0]);//Add the children object.
			
			if(MyChildren!=null)
				for(int i=0;i<MyChildren.size();i++){
					Object O2[]=(Object[])MyChildren.get(i);
					

					float x=(Float)O2[1];
					float y=(Float)O2[2];
					float z=(Float)O2[3];
					
					float lock[]=new float[]{x,y,z};
					
					if(rotY!=0.0){
						Common.rotateY(lock,rotY);

					}
					
					if(rotX!=0.0){
						Common.rotateX(lock,rotX);
					}
					
					if(rotZ!=0.0){
						Common.rotateZ(lock,rotZ);
					}
					
					float cx=(Float)CurrentWork[1];
					float cy=(Float)CurrentWork[2];
					float cz=(Float)CurrentWork[3];
					
					lock[0]+=tx+cx;
					lock[1]+=ty+cy;
					lock[2]+=tz+cz;
					
					Work.add(new Object[]{O2[0],new Float(lock[0]),new Float(lock[1]),new Float(lock[2]),new Float(0.0f),new Float(0.0f),new Float(0.0f)});
				}
				
			gl.glPopMatrix();
			
			count++;
		}
		
		gl.glPopMatrix();
		gl.glDisable(gl.GL_TEXTURE_2D);
	}
	
	//Testing main.
	public static void main(String args[]){
		SpriteBody SB=new SpriteBody();
		SB.load("images/abstract_face.christ");
		SB.buildBuffers(null);
	}
}
 