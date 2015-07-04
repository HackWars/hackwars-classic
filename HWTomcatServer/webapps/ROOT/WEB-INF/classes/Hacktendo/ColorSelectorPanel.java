/*
Programmer: Ben Coe.(2007)<br />

This creates a panel that can be used to modify face control points.
*/

package Hacktendo;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.glu.*;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import com.sun.opengl.util.GLUT;
import Assignments.*;
import View.*;
import java.text.*;
import java.math.*;
import Browser.*;
import Game.*;

//Stuff borrowed from Hacktendo.
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.concurrent.Semaphore;
import GUI.Sound;
import javax.imageio.*;
import GUI.ImageLoader;
import GUI.Hacker;
import View.*;
import Assignments.*;
import java.util.*;
import Hacktendo.*;
import Game.MMO.*;
import java.util.Map;
import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;
import com.plink.Hack3D.*;

public class ColorSelectorPanel extends JPanel implements ChangeListener{
	private JColorChooser Selectors=null;
	private SpriteFace MySpriteFace=null;
	private String Component="";
	
	//Constructor.
	public ColorSelectorPanel(String Component,SpriteFace MySpriteFace){
		this.MySpriteFace=MySpriteFace;
		this.Component=Component;
	
		
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.setVisible(true);

		Selectors=new JColorChooser();
		Selectors.getSelectionModel().addChangeListener(this);
		this.add(new JLabel("<html><h2>"+Component+" Color </h2></html>"));
		this.add(Selectors);
				
	}

	public void stateChanged(ChangeEvent e) {
		Color newColor = Selectors.getColor();
		float r=(float)newColor.getRed()/256.0f;
		float g=(float)newColor.getGreen()/256.0f;
		float b=(float)newColor.getBlue()/256.0f;
		MySpriteFace.setColor(Component,r,g,b);
	}

	//Testing main.
	public static void main(String args[]){
	}
}//END.
