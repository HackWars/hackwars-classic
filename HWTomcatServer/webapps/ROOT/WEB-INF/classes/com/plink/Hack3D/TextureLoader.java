package com.plink.Hack3D;
/**
This helper class is used to load textures.
*/

import javax.media.opengl.*;
import com.sun.opengl.util.texture.*;
import com.sun.opengl.util.*;
import java.io.*;
import java.awt.image.*;
 
public class TextureLoader{
 
 
	/**
	* Texture loader utilizes JOGL's provided utilities to produce a texture.
	*
	* @param fileName relative filename from execution point
	* @return a texture binded to the OpenGL context
	*/
 
	public synchronized static Texture load(String fileName){
		Texture text = null;
		try{
			text = TextureIO.newTexture(new File(fileName), true);
			text.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
			text.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return text;
	}

	public synchronized static Texture load(BufferedImage BI){
		Texture text = null;
		try{
			text = TextureIO.newTexture(BI, true);
			text.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
			text.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return text;
	}
 
}
 