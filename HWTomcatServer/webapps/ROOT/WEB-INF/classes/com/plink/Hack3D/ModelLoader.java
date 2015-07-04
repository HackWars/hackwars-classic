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

import javax.media.opengl.glu.*;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import GUI.*;

import java.util.*;
import Hacktendo.*;

public class ModelLoader{
	private static ArrayList TempModelData=new ArrayList();//Used to hold temporary model data.

	/**
	This tool loads a model in based on our model format.
	*/
	public static void load(GL gl,String modelPath,OpenGLImageHandler MyImageHandler){
		boolean sphere=false;
		try{
			File F=ImageLoader.getFile(modelPath);
			BufferedReader BR=new BufferedReader(new FileReader(F));
				
			String data;
			while((data=BR.readLine())!=null){
				String splitData[]=data.split("\t");
				
				for(int i=0;i<27;i+=9){
					String vertData[]=splitData[i].split(" ");
					float vert[]=new float[3];
					vert[0]=new Float(vertData[0]);
					vert[1]=new Float(vertData[1]);
					vert[2]=new Float(vertData[2]);
	
					String normData[]=splitData[i+1].split(" ");
					float norm[]=new float[3];
					norm[0]=new Float(normData[0]);
					norm[1]=new Float(normData[1]);
					norm[2]=new Float(normData[2]);
	
					String uvData[]=splitData[i+2].split(" ");
					float uv[]=new float[3];
					uv[0]=new Float(uvData[0]);
					uv[1]=new Float(uvData[1]);
					
					String rgbaData[]=splitData[i+3].split(" ");
					float rgba[]=new float[4];
					rgba[0]=new Float(rgbaData[0]);
					rgba[1]=new Float(rgbaData[1]);
					rgba[2]=new Float(rgbaData[2]);
					rgba[3]=new Float(rgbaData[3]);

					String drgbaData[]=splitData[i+4].split(" ");
					float drgba[]=new float[4];
					drgba[0]=new Float(drgbaData[0]);
					drgba[1]=new Float(drgbaData[1]);
					drgba[2]=new Float(drgbaData[2]);
					drgba[3]=new Float(drgbaData[3]);
					
					String srgbaData[]=splitData[i+5].split(" ");
					float srgba[]=new float[4];
					srgba[0]=new Float(srgbaData[0]);
					srgba[1]=new Float(srgbaData[1]);
					srgba[2]=new Float(srgbaData[2]);
					srgba[3]=new Float(srgbaData[3]);
					
					String ergbaData[]=splitData[i+6].split(" ");
					float ergba[]=new float[4];
					ergba[0]=new Float(ergbaData[0]);
					ergba[1]=new Float(ergbaData[1]);
					ergba[2]=new Float(ergbaData[2]);
					ergba[3]=new Float(ergbaData[3]);
					
					Float shine=new Float(splitData[i+7]);
					
					String tex=splitData[i+8];
					tex=tex.replaceAll(" ","");
					
					TempModelData.add(new Object[]{vert,norm,uv,rgba,drgba,srgba,ergba,shine,tex});
				}
			}
			
			BR.close();
			
			//Now draw the model.
			

			gl.glPushMatrix();
			for(int i=0;i<TempModelData.size();i++){
				Object O[]=(Object[])TempModelData.get(i);
				float vert[]=(float[])O[0];
				float norm[]=(float[])O[1];
				float uv[]=(float[])O[2];
				float rgba[]=(float[])O[3];
				
				if(i%3==0){
					Texture T=MyImageHandler.getStockTexture("Images/"+(String)O[8]);
					T.bind();
					gl.glBegin(gl.GL_TRIANGLES);
				}
							
				
				gl.glColor4f(rgba[0],rgba[1],rgba[2],rgba[3]);
				gl.glMaterialfv( gl.GL_FRONT, gl.GL_DIFFUSE, (float[])O[4],0);
				gl.glMaterialfv( gl.GL_FRONT, gl.GL_SPECULAR, (float[])O[5],0);
				gl.glMaterialfv( gl.GL_FRONT, gl.GL_EMISSION, (float[])O[6],0);
				gl.glMaterialf( gl.GL_FRONT, gl.GL_SHININESS,(float)(Float)O[7]);
				gl.glTexCoord2f(uv[1],uv[0]);
				gl.glNormal3f(norm[0],norm[1],norm[2]);
				gl.glVertex3f(vert[0],vert[1],vert[2]);
				
				if((i+1)%3==0){
					gl.glEnd();
				}				
			}
			gl.glPopMatrix();
			gl.glMaterialfv( gl.GL_FRONT, gl.GL_DIFFUSE,new float[]{0.0f,0.0f,0.0f,0.0f},0);
			gl.glMaterialfv( gl.GL_FRONT, gl.GL_SPECULAR,new float[]{0.0f,0.0f,0.0f,0.0f},0);
			gl.glMaterialfv( gl.GL_FRONT, gl.GL_EMISSION,new float[]{0.0f,0.0f,0.0f,0.0f},0);
			gl.glMaterialf( gl.GL_FRONT, gl.GL_SHININESS,0.0f);
			
			TempModelData.clear();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
 