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

public class SpriteFace{
	private ArrayList Polygons=new ArrayList();//Holds triangle data.
	private ArrayList Vertices=new ArrayList();//Holds vertex data.
	private ArrayList Materials=new ArrayList();//Holds materials data.
	private HashMap MaterialsHash=new HashMap();//Holds materials in a HashMap.
	private HashMap ControlGroups=new HashMap();//The control groups of the face.	
	private HashMap ControlGroupsIDs=new HashMap();//The control groups in a hierarchy based on IDs.
	private HashMap ControlGroupCenters=new HashMap();//The center points of control groups.
	
	//Constants.
	private static int MATERIALS=0;
	private static int VERTICES=1;
	private static int TRIANGLES=2;
	private static int VERT_GROUPS=3;
	private static int NONE=4;
	private int currentParse=NONE;

	/**
	This tool loads a model in based on our model format.
	*/
	public void load(String modelPath){
	
		boolean sphere=false;
		try{
			File F=ImageLoader.getFile(modelPath);
			BufferedReader BR=new BufferedReader(new FileReader(modelPath));

		//	BufferedReader BR=new BufferedReader(new FileReader(F));
				
			String data;
			while((data=BR.readLine())!=null){
				if(data.length()==0){//Back to regular mode.
					currentParse=NONE;
					continue;
				}
				
				if(data.contains("#INDEX OF MATERIALS")){
					currentParse=MATERIALS;
					continue;
				}
				
				if(data.contains("#INDEX OF VERTICES")){
					currentParse=VERTICES;
					continue;
				}	
				
				if(data.contains("#LIST OF TRIANGLES")){
					currentParse=TRIANGLES;
					continue;
				}
				
				if(data.contains("#LIST OF VERT GROUPS")){
					currentParse=VERT_GROUPS;
					continue;
				}
				
				if(currentParse==MATERIALS){
					String splitData[]=data.split(",");
					float O[]=new float[]{new Float(splitData[2]),new Float(splitData[3]),new Float(splitData[4])};
					Materials.add(O);
					MaterialsHash.put(splitData[1],O);
				}
				
				if(currentParse==VERTICES){
					String splitData[]=data.split(",");
					float O[]=new float[]{new Float(splitData[1]),new Float(splitData[2]),new Float(splitData[3]),new Float(splitData[4]),new Float(splitData[5]),new Float(splitData[6]),new Float(splitData[1]),new Float(splitData[2]),new Float(splitData[3])};
					
					Vertices.add(O);
				}
				
				if(currentParse==TRIANGLES){
					String splitData[]=data.split(",");
					int O[]=new int[]{new Integer(splitData[0]),new Integer(splitData[1]),new Integer(splitData[2]),new Integer(splitData[3])};
					

					Polygons.add(O);
				}
				
				if(currentParse==VERT_GROUPS){
					String splitData[]=data.split(",");
					int O[]=new int[splitData.length-1];
					for(int i=1;i<splitData.length;i++){
						O[i-1]=new Integer(splitData[i]);
					}
					String splitData2[]=splitData[0].split(" ");					
					String splitData3[]=splitData2[0].split("-");
					
					HashMap CurrentHash=ControlGroupsIDs;
					for(int i=0;i<splitData3.length-1;i++){
						Integer fetch=new Integer(splitData3[i]);
						CurrentHash=(HashMap)((Object[])CurrentHash.get(fetch))[0];
					}
										
					HashMap SubGroups=new HashMap();
					
					String group=splitData2[1];
					int i=0;
					while(ControlGroups.get(group+"["+i+"]")!=null){
						i++;
					}
					group=group+"["+i+"]";
					
					CurrentHash.put(new Integer(splitData3[splitData3.length-1]),new Object[]{SubGroups,group});
					ControlGroups.put(group,new Object[]{O,SubGroups});
				}
			}
			
			BR.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		 
	}
	
	/**
	Set the color of a material.
	*/
	public void setColor(String id,float r,float g,float b){
		float data[]=(float[])MaterialsHash.get(id);
		data[0]=r;
		data[1]=g;
		data[2]=b;
	}
	
	/**
	Scale the feature indicated by the amount provided in each direction.
	*/
	public void rotate(String id,float x,float y,float z){
		int vertices[]=(int[])((Object[])ControlGroups.get(id))[0];
		if(vertices==null)
			return;
		
		float xavg=0.0f;
		float yavg=0.0f;
		float zavg=0.0f;
		
		if(ControlGroupCenters.get(id)==null){
		
			for(int i=0;i<vertices.length;i++){
				float vertex[]=(float[])Vertices.get(vertices[i]);
				xavg+=vertex[0];
				yavg+=vertex[1];
				zavg+=vertex[2];
			}
			xavg/=vertices.length;
			yavg/=vertices.length;
			zavg/=vertices.length;
			
			ControlGroupCenters.put(id,new float[]{xavg,yavg,zavg});
		}else{
			float center[]=(float[])ControlGroupCenters.get(id);
			xavg=center[0];
			yavg=center[1];
			zavg=center[2];
		}

		for(int i=0;i<vertices.length;i++){
			float vertex[]=(float[])Vertices.get(vertices[i]);

			vertex[0]-=xavg;
			vertex[1]-=yavg;
			vertex[2]-=zavg;
	
			Common.rotateX(vertex,x);
			Common.rotateY(vertex,y);
			Common.rotateZ(vertex,z);
			
			vertex[0]+=xavg;
			vertex[1]+=yavg;
			vertex[2]+=zavg;
		}
		
	}
	/**
	Convert from a control group into a group name.
	*/
	public String getGroupName(Object O){
		String GroupName=(String)((Object[])O)[1];
		return(GroupName);
	}
	
	/**
	Get the HashMap used to indicate control groups.
	*/
	public HashMap getControlGroups(){
		return(ControlGroupsIDs);
	}	
	
	/**
	Get the sub control gropus associated with a particular group.
	*/
	public HashMap getControlGroups(String id){
		HashMap Temp=(HashMap)((Object[])ControlGroups.get(id))[1];
		if(Temp.size()==0)
			return(null);
		return(Temp);
	}
	
	/**
	Get the Hash Map that represents the different materials on the sprite.
	*/
	public HashMap getMaterialsHash(){
		return(MaterialsHash);
	}
	
	/**
	Reset the vertices for a given component.
	*/
	public void resetComponent(String id){
		int vertices[]=(int[])((Object[])ControlGroups.get(id))[0];
		if(vertices==null)
			return;
			
		for(int i=0;i<vertices.length;i++){
			float vertex[]=(float[])Vertices.get(vertices[i]);

			vertex[0]=vertex[6];
			vertex[1]=vertex[7];
			vertex[2]=vertex[8];
		}
	}
	
	/**
	Scale the feature indicated by the amount provided in each direction.
	*/
	public void scale(String id,float x,float y,float z){
		int vertices[]=(int[])((Object[])ControlGroups.get(id))[0];
		if(vertices==null)
			return;
		
		float xavg=0.0f;
		float yavg=0.0f;
		float zavg=0.0f;

		if(ControlGroupCenters.get(id)==null){
		
			for(int i=0;i<vertices.length;i++){
				float vertex[]=(float[])Vertices.get(vertices[i]);
				xavg+=vertex[0];
				yavg+=vertex[1];
				zavg+=vertex[2];
			}
			xavg/=vertices.length;
			yavg/=vertices.length;
			zavg/=vertices.length;
			
			ControlGroupCenters.put(id,new float[]{xavg,yavg,zavg});
		}else{
			float center[]=(float[])ControlGroupCenters.get(id);
			xavg=center[0];
			yavg=center[1];
			zavg=center[2];
		}
		
		for(int i=0;i<vertices.length;i++){
			float vertex[]=(float[])Vertices.get(vertices[i]);

			vertex[0]-=xavg;
			vertex[1]-=yavg;
			vertex[2]-=zavg;
			
			vertex[0]*=x;
			vertex[1]*=y;
			vertex[2]*=z;
			
			vertex[0]+=xavg;
			vertex[1]+=yavg;
			vertex[2]+=zavg;
		}
	}
	
	/**
	Move the feature in and out.
	*/
	public void move(String id,float x,float y,float z){
		int vertices[]=(int[])((Object[])ControlGroups.get(id))[0];
		if(vertices==null)
			return;
		
		float xavg=0.0f;
		float yavg=0.0f;
		float zavg=0.0f;

		if(ControlGroupCenters.get(id)==null){
		
			for(int i=0;i<vertices.length;i++){
				float vertex[]=(float[])Vertices.get(vertices[i]);
				xavg+=vertex[0];
				yavg+=vertex[1];
				zavg+=vertex[2];
			}
			xavg/=vertices.length;
			yavg/=vertices.length;
			zavg/=vertices.length;
			
			ControlGroupCenters.put(id,new float[]{xavg,yavg,zavg});
		}else{
			float center[]=(float[])ControlGroupCenters.get(id);
			xavg=center[0];
			yavg=center[1];
			zavg=center[2];
		}
		
		for(int i=0;i<vertices.length;i++){
			float vertex[]=(float[])Vertices.get(vertices[i]);

			vertex[0]-=xavg;
			vertex[1]-=yavg;
			vertex[2]-=zavg;
			
			vertex[0]+=x;
			vertex[1]+=y;
			vertex[2]+=z;
			
			vertex[0]+=xavg;
			vertex[1]+=yavg;
			vertex[2]+=zavg;
		}
	}
	
	/**
	This renders the face in its current state.
	*/
	public void render(GL gl,OpenGLImageHandler MyImageHandler){
		gl.glPushMatrix();
	//	gl.glRotatef(90.0f,1.0f,0.0f,0.0f);
		gl.glScalef(1.2f,1.2f,1.2f);
		gl.glTranslatef(0.0f,1.0f,-1.0f);

		gl.glDisable(gl.GL_TEXTURE_2D);
		for(int i=0;i<Polygons.size();i++){
			int Points[]=(int[])Polygons.get(i);
			float Material[]=(float[])Materials.get((int)Points[3]);
			float v1[]=(float[])Vertices.get(Points[0]);
			float v2[]=(float[])Vertices.get(Points[1]);
			float v3[]=(float[])Vertices.get(Points[2]);
			
			gl.glBegin(GL.GL_TRIANGLES);
			gl.glColor3f(Material[0],Material[1],Material[2]);
			gl.glNormal3f(v1[3],v1[4],v1[5]);
			gl.glVertex3f((float)v1[0],(float)v1[1],(float)v1[2]);
			gl.glNormal3f(v2[3],v2[4],v2[5]);
			gl.glVertex3f((float)v2[0],(float)v2[1],(float)v2[2]);
			gl.glNormal3f(v3[3],v3[4],v3[5]);
			gl.glVertex3f((float)v3[0],(float)v3[1],(float)v3[2]);
			gl.glEnd();
		}
		gl.glEnable(gl.GL_TEXTURE_2D);
		gl.glPopMatrix();
	}
	
	//Testing main.
	public static void main(String args[]){
		SpriteFace SF=new SpriteFace();
		SF.load("images/abstract_face.christ");
	}
}
 