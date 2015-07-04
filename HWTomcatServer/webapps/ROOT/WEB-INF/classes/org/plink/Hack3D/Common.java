package com.plink.Hack3D;
/**
Common functions required for various 3D effect.
*/

import javax.media.opengl.*;
import com.sun.opengl.util.texture.*;
import com.sun.opengl.util.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import Hacktendo.*;
import javax.media.opengl.glu.*;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
 
public class Common{
	/**
	Performs the cross product on two 3D vectors.
	*/
	public static float[] crossProduct(float A[],float B[]){
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
	
	/**
	Rotates a point by the angle provided around the x axis.
	*/
	public static void rotateX(float[] A,float angle){
		angle=angle*((float)Math.PI/180.0f);
		
		float A1=A[1];
		
		A[1]=A1*(float)Math.cos(angle)+A[2]*(float)-Math.sin(angle);
		A[2]=A1*(float)Math.sin(angle)+A[2]*(float)Math.cos(angle);
	}
	
	/**
	Rotates a point by the angle provided around the y axis.
	*/
	public static void rotateY(float[] A,float angle){
		angle=angle*((float)Math.PI/180.0f);
		
		float A0=A[0];
		
		A[0]=A0*(float)Math.cos(angle)+A[2]*(float)Math.sin(angle);
		A[2]=A0*(float)-Math.sin(angle)+A[2]*(float)Math.cos(angle);
	}
	
	/**
	Rotates around a combination of all axis.
	*/
	public static void rotate(float[] A,float rotX,float rotY,float rotZ){
		rotX=rotX*((float)Math.PI/180.0f);
		rotY=rotY*((float)Math.PI/180.0f);
		rotZ=rotZ*((float)Math.PI/180.0f);

/*cos(y)*cos(z)                                     cos(y)*sin(z)                            -sin(y)
sin(x)*sin(y)*cos(z)+cos(x)*-sin(z)   		  sin(x)*sin(y)*sin(z)+cos(x)*cos(z)       sin(x)*cos(y)
cos(x)*sin(y)*cos(z)+sin(x)*sin(z)                cos(x)*sin(y)*sin(z)+cos(z)*-sin((x)     cos(x)*cos(y)

a,b,c*/

		float A0=A[0];
		float A1=A[1];
		float A2=A[2];
		
		float cosx=(float)Math.cos(rotX);
		float sinx=(float)Math.sin(rotX);
		float cosy=(float)Math.cos(rotY);
		float siny=(float)Math.sin(rotY);
		float cosz=(float)Math.cos(rotZ);
		float sinz=(float)Math.sin(rotZ);
		
		A[0]=A0*(cosy*cosz)+     A1*(sinx*siny*cosz+cosx*-sinz)      +A2*(cosx*siny*cosz+sinx*sinz);
		A[1]=A0*(cosy*sinz)+     A1*(sinx*siny*sinz+cosx*cosz)       +A2*(cosx*siny*sinz+cosz*-sinx);
		A[2]=A0*(-siny)+         A1*(sinx*cosy)                      +A2+(cosx*cosy);
	}
	
	/**
	Rotates a point by the angle provided around the z axis.
	*/
	public static void rotateZ(float[] A,float angle){
		angle=angle*((float)Math.PI/180.0f);
		
		float A0=A[0];
				
		A[0]=A0*(float)Math.cos(angle)+A[1]*(float)-Math.sin(angle);
		A[1]=A0*(float)Math.sin(angle)+A[1]*(float)Math.cos(angle);
	}
}
 