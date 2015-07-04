/**
Hack Wars 2008

An OpenGL frame for 3D graphics.
*/

package GUI;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.text.*;
import java.math.*;
import Browser.*;
import Game.*;


public class OpenGLWindow extends JFrame implements GLEventListener {

	private GLCanvas MyGLCanvas=new GLCanvas();

	public OpenGLWindow(int width,int height){
		MyGLCanvas.addGLEventListener(this);
		this.add(MyGLCanvas);
		this.setSize(width,height);
		this.setVisible(true);
	}

	public void init(GLAutoDrawable drawable) {
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
	}

	public void display(GLAutoDrawable drawable) {
	    GL gl = drawable.getGL();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glOrtho(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex2f(-0.5f, -0.5f);
		gl.glVertex2f(-0.5f, 0.5f);
		gl.glVertex2f(0.5f, 0.5f);
		gl.glVertex2f(0.5f, -0.5f);
		gl.glEnd();
		gl.glFlush();
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
	}

}