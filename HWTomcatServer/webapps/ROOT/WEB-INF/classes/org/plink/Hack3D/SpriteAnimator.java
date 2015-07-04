package com.plink.Hack3D;
/**
This tool is used to animate models.
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
public class SpriteAnimator{
	private HashMap Animations=new HashMap();
	
	//Constants.
	private static int OBJECTS=0;
	private static int NONE=1;
	private int currentParse=NONE;
	
	private int animationCount=0;

	/**
	This tool loads a model in based on our model format.
	*/
	public void load(String modelPath){
	
		boolean sphere=false;
		try{
		//	File F=ImageLoader.getFile(modelPath);
		//	BufferedReader BR=new BufferedReader(new FileReader(modelPath));

			//First we read in each of the animations which are represented by folders.
			String folders[]=(new File("animations")).list();
			for(int i=0;i<folders.length;i++){
				//Now load in each of the frames.
				File temp=(new File("animations/"+folders[i]));
				
				
				if(temp.isDirectory()){//This will represent a single animation.
					String files[]=temp.list();
					for(int ii=0;ii<files.length;ii++){
						String data[]=files[ii].split("&");
						if(data.length==2){//It must be an animation frame.
							String data2[]=(data[1].split("\\."))[0].split("-");
							int index=new Integer(data2[0]);
							int size=new Integer(data2[1])+1;
													
							ArrayList Animation=(ArrayList)Animations.get(data[0]);
							if(Animation==null){
								Animation=new ArrayList();
								for(int iii=0;iii<size;iii++){
									Animation.add(new HashMap());
								}
								Animations.put(data[0],Animation);
							}
							
							HashMap CurrentTransformation=(HashMap)Animation.get(index);
							
							//We now load the actual animation information.
							BufferedReader BR=new BufferedReader(new FileReader(new File("animations/"+folders[i]+"/"+files[ii])));
							String fileData;
							currentParse=NONE;
							while((fileData=BR.readLine())!=null){
								if(fileData.length()==0){//Back to regular mode.
									currentParse=NONE;
									continue;
								}
								
								if(fileData.contains("#OBJECT ID")){
									currentParse=OBJECTS;
									continue;
								}

								if(currentParse==OBJECTS){
								
									String splitData[]=fileData.split(",");
									
									ArrayList Transformations=(ArrayList)CurrentTransformation.get(splitData[0]);
									if(Transformations==null){
										Transformations=new ArrayList();
										CurrentTransformation.put(splitData[0],Transformations);
									}
										
									String O[]=splitData[1].split("-");
									Transformations.add(new Object[]{new Integer(O[0]),new Integer(O[1]),new Float(splitData[2])});
								}
								
							}
						}
						
						
					}
				}
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		 
	}
	
	/**
	Get the current animation hashmap.
	*/
	public HashMap getCurrentAnimation(String key){
		ArrayList ThisAnimation=(ArrayList)Animations.get(key);
		animationCount=animationCount+1;
		animationCount=animationCount%(ThisAnimation.size()*3);
		return((HashMap)ThisAnimation.get(animationCount/3));
	}
	
	//Testing main.
	public static void main(String args[]){
		SpriteAnimator SB=new SpriteAnimator();
		SB.load("images/abstract_face.christ");
	}
}
 