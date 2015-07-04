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

public class AttributeSelectorPanel extends JTabbedPane implements AdjustmentListener{
	private JScrollBar Selectors[]=new JScrollBar[9];
	private static final String labels[]=new String[]{"Movement X:","Movement Y:","Movement Z:","Scale X:","Scale Y:","Scale Z:","Rotation X:","Rotation Y:","Rotation Z"};
	private SpriteFace MySpriteFace=null;
	private String Component="";
	private float moveX=50.0f;
	private float moveY=50.0f;
	private float moveZ=50.0f;
	private float rotateX=50.0f;
	private float rotateY=50.0f;
	private float rotateZ=50.0f;
	private float scaleX=50.0f;
	private float scaleY=50.0f;
	private float scaleZ=50.0f;
	private boolean root=false;
	private AttributeSelectorPanel Parent=null;
	private ArrayList Children=new ArrayList();	

	//Constructor.
	public AttributeSelectorPanel(String Component,SpriteFace MySpriteFace,AttributeSelectorPanel Parent){
		if(Parent==null){
			root=true;
			this.Parent=this;
		}else
			this.Parent=Parent;

		this.MySpriteFace=MySpriteFace;
		this.Component=Component;
	
		JPanel DefaultPanel=new JPanel();
		DefaultPanel.setLayout(new BoxLayout(DefaultPanel,BoxLayout.Y_AXIS));
		DefaultPanel.setVisible(true);

		for(int i=0;i<Selectors.length;i++){
			Selectors[i]=new JScrollBar(JScrollBar.HORIZONTAL,50,1,0,100);
			DefaultPanel.add(new JLabel(labels[i]));
			DefaultPanel.add(Selectors[i]);
			Selectors[i].addAdjustmentListener(this);
		}
		
		JScrollPane MyScrollPane=new JScrollPane(DefaultPanel);
		MyScrollPane.setVisible(true);
		MyScrollPane.setBounds(0,0,200,350);
		MyScrollPane.getViewport().add(DefaultPanel);

		this.addTab("Default",null,MyScrollPane,"");
		
		if(MySpriteFace.getControlGroups(Component)!=null){
			addSubPanel();
		}
		
		this.setPreferredSize(new Dimension(150,350));
		
	}
	
	/**
	Add a sub-panel to this widget.
	*/
	public void addSubPanel(){
		
		JPanel AttributeFrame=new JPanel();
		AttributeFrame.setLayout(new BoxLayout(AttributeFrame,BoxLayout.Y_AXIS));
		JScrollPane ScrollPane=new JScrollPane();
		HashMap ControlGroups=MySpriteFace.getControlGroups(Component);
		Iterator MyIterator=ControlGroups.entrySet().iterator();
		
		int i=0;
		
		JPanel BigPanel=new JPanel();
		BigPanel.setLayout(new BoxLayout(BigPanel,BoxLayout.Y_AXIS));
		BigPanel.setVisible(true);

		while(MyIterator.hasNext()){
			Map.Entry MyEntry=(Map.Entry)MyIterator.next();
			AttributeSelectorPanel ASP=new AttributeSelectorPanel(MySpriteFace.getGroupName(MyEntry.getValue()),MySpriteFace,Parent);
			Children.add(ASP);
			ASP.setVisible(true);
			BigPanel.add(new JLabel("<html><h2>"+MySpriteFace.getGroupName(MyEntry.getValue())+"</h2></html>"));

			BigPanel.add(ASP);
			i++;
		}
		
		ScrollPane.getViewport().add(BigPanel);
		ScrollPane.setVisible(true);
		ScrollPane.setBounds(0,0,200,350);
		AttributeFrame.add(ScrollPane);
		AttributeFrame.setBounds(0,0,200,350);
		AttributeFrame.setVisible(true);
		this.addTab("More Options",AttributeFrame);
	}
	
	public void applyTransformation(){
		if(root)
			MySpriteFace.resetComponent(Component);
		

		float tmoveX=(moveX-50.0f)/20.0f;
		
		float tmoveY=(moveY-50.0f)/20.0f;
		
		float tmoveZ=(moveZ-50.0f)/20.0f;
		
		MySpriteFace.move(Component,tmoveX,tmoveY,tmoveZ);
		
		float tscaleX=(scaleX-50.0f)/20.0f+1.0f;
		
		float tscaleY=(scaleY-50.0f)/20.0f+1.0f;
		
		float tscaleZ=(scaleZ-50.0f)/20.0f+1.0f;
		
		MySpriteFace.scale(Component,tscaleX,tscaleY,tscaleZ);
		
		float trotateX=(rotateX-50.0f)*3.6f;
		
		float trotateY=(rotateY-50.0f)*3.6f;
		
		float trotateZ=(rotateZ-50.0f)*3.6f;
		
		MySpriteFace.rotate(Component,trotateX,trotateY,trotateZ);

		for(int i=0;i<Children.size();i++){
			AttributeSelectorPanel ASP=(AttributeSelectorPanel)Children.get(i);
			ASP.applyTransformation();
		}

	}

	//Action listeners.
	public void adjustmentValueChanged(AdjustmentEvent ae){


	
	 	moveX=(float)Selectors[0].getValue();
		
		moveY=(float)Selectors[1].getValue();
		
		moveZ=(float)Selectors[2].getValue();
		
		
		scaleX=(float)Selectors[3].getValue();
		
		scaleY=(float)Selectors[4].getValue();
		
		scaleZ=(float)Selectors[5].getValue();
		
		
		rotateX=(float)Selectors[6].getValue();
		
		rotateY=(float)Selectors[7].getValue();
		
		rotateZ=(float)Selectors[8].getValue();
		
		Parent.applyTransformation();
	}
	
	//Testing main.
	public static void main(String args[]){
	/*	JFrame MyFrame=new JFrame();
		AttributeSelectorPanel ASP=new AttributeSelectorPanel("Hello World:",null);
		ASP.setVisible(true);
		MyFrame.add(ASP);
		MyFrame.pack();
		MyFrame.setVisible(true);*/
	}
}//END.
