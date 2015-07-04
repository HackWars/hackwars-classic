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
import net.miginfocom.swing.*;


public class XAttributeSelectorPanel extends JPanel{
	//scrollbars for each component
	private JScrollBar Selectors[]=new JScrollBar[10];
	//all scrollbars for the entire model
	public ArrayList<JScrollBar> AllSelectors = new ArrayList<JScrollBar>();
	//Temporarily used for editing sprite faces.
	private SpriteFace  MySpriteFace=null;
	
	//Constructor.
	public XAttributeSelectorPanel(SpriteFace MySpriteFace){
		this.MySpriteFace=MySpriteFace;
	
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		MySpriteFace.load("abstract_face.christ");
		//vertex groups that make up the sprite face
		HashMap components = MySpriteFace.getControlGroups();

		Set entries = components.entrySet();
	    Iterator it = entries.iterator();

		//add the tab panel for the components
		addTabPanel(it, this);
		
		//time to add the event listeners to the program
		Iterator<JScrollBar> itr = AllSelectors.iterator();
		while (itr.hasNext()) {
			JScrollBar selector = itr.next();
			//event handling function, for ease of reading we'll write the full function at the end
			AdjustmentListener al = new AdjustmentListener() {
				public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
					//function to actually perform the transformations
					applyTransformation();
				}
			};
			//adding the listener
			selector.addAdjustmentListener(al);
		}					
	}
	
	//create the tab panels (recursively) and populate them with the appropriate selectors
	public void addTabPanel(Iterator MyIterator, Container container){
		JTabbedPane tabPanel = new JTabbedPane();

		//go through model components and run the function to add controls
		while(MyIterator.hasNext()){
			Map.Entry MyEntry=(Map.Entry)MyIterator.next();
			String component = MySpriteFace.getGroupName(MyEntry.getValue());
			addControlPanel(component, tabPanel);
		}
		//recursion
		container.add(tabPanel);
	}

	//function to add the controls
	public void addControlPanel(String component, JTabbedPane tabPanel){
		//create the swing panels/layout stuff
		JPanel bigPanel = new JPanel();
		bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("wrap 4"));
		
		//retrieve the scrollbar info for the component we're working on
		SliderInformation si = MySpriteFace.getSliderInformation(component);
				
		//list of transformation names
		String[] sNames = {"xRot", "yRot", "zRot", "xScale", "yScale", "zScale", "xyzScale", "xPos", "yPos", "zPos"};

		//create a list of hashmaps and populate it with the component controls
		ArrayList<HashMap> sliders = new ArrayList<HashMap>();		
		sliders.add(si.getXRotation());
		sliders.add(si.getYRotation());
		sliders.add(si.getZRotation());
		sliders.add(si.getXScale());
		sliders.add(si.getYScale());
		sliders.add(si.getZScale());
		sliders.add(si.getScaleAll());
		sliders.add(si.getXPosition());
		sliders.add(si.getYPosition());
		sliders.add(si.getZPosition());

		Iterator<HashMap> itr = sliders.iterator();
		
		int count = 0;
		
		//time to create and add the scrollbars
		while (itr.hasNext()) {
			HashMap ctrl = itr.next();
			if (ctrl!=null) {
				System.out.print(ctrl);
				
				//get min and max values for selector
				Object mn = ctrl.get("min");
				Object mx = ctrl.get("max");
				//create code so selector knows which component to act on and which transformation to perform
				String name = ctrl.get("name").toString();
				String sName = component + "," + sNames[count];
				
				//scrollbars can't handle float values, so we multiply everything by 100
				Float min = (Float)mn;
				Float max = (Float)mx;
				min = min * 100;
				max = max * 100;
				//and convert them to ints
				int intMax = max.intValue();
				int intMin = min.intValue();
				
				//honestly i dont' really know what the hell this is, but it needs to be zero
				int extent = 0;

				//set initial scrollbar value, determine based on transformation type
				int value = 0;
				if (sNames[count].contains("Rot")) {
					value = 0;
				}
				else if (sNames[count].contains("Scale")) {
					value = 100;
				}
				else if (sNames[count].contains("Pos")) {
					//scrollbars can't handle float values, so we must multiply everything by 100
					value = 0;
				}
				
				//finally construct the scrollbar, name it and add it to the panel and list of scrollbars
				JScrollBar jsb = new JScrollBar(JScrollBar.HORIZONTAL, value, extent, intMin, intMax);
				jsb.setName(sName);
				AllSelectors.add(jsb);
				panel.add(new JLabel(name));
				panel.add(jsb);
			}
			count++;
		}

		//more swing stuff, adding the panel with the scrollbars to the main panel and setting visibilty, etc...
		JScrollPane scrollPane=new JScrollPane(panel);
		scrollPane.setVisible(true);
		scrollPane.setBounds(0,0,200,350);
		scrollPane.getViewport().add(panel);
		bigPanel.add(scrollPane);

		HashMap components = MySpriteFace.getControlGroups(component);
		if(components!=null){
			Set entries = components.entrySet();
			Iterator it = entries.iterator();
			addTabPanel(it, bigPanel);
		}		

		tabPanel.addTab(component, bigPanel);
	}

	public void applyTransformation(){		
		//reset the root component
		JScrollBar sRoot = AllSelectors.get(0);
		String rName = sRoot.getName();
		String[] arName = rName.split(",");
		String rComponent = arName[0];
		MySpriteFace.resetComponent(rComponent);

		//iteterate through the scrollbars and apply each transformation
		Iterator<JScrollBar> itr = AllSelectors.iterator();
		while (itr.hasNext()) {
			//get the scrollbar and its properties
			JScrollBar selector = itr.next();
			String sName = selector.getName();
			String[] name = sName.split(",");
			String component = name[0];
			String transform = name[1];
			
			//get the int value of the scrollbar, convert to float
			Integer intValue = selector.getValue();
			Float value = intValue.floatValue();
			//divide by 100 to get the proper decimal value
			value = value / 100;
			
			//perform transformations
			if (transform.equals("xScale")) {	
				MySpriteFace.scale(component, value, 1, 1);
			}
			else if (transform.equals("yScale")) {
				MySpriteFace.scale(component, 1, value, 1);
			}
			else if (transform.equals("zScale")) {
				MySpriteFace.scale(component, 1, 1, value);
			}
			else if (transform.equals("xyzScale")) {
				MySpriteFace.scale(component, value, value, value);
			}
			else if (transform.equals("xRot")) {
				MySpriteFace.rotate(component,value,0,0);
			}
			else if (transform.equals("yRot")) {
				MySpriteFace.rotate(component,0,value,0);
			}
			else if (transform.equals("zRot")) {
				MySpriteFace.rotate(component,0,0,value);
			}
			else if (transform.equals("xPos")) {
				MySpriteFace.move(component,value, 0, 0);
			}
			else if (transform.equals("yPos")) {
				MySpriteFace.move(component,0, value, 0);
			}
			else if (transform.equals("zPos")) {
				MySpriteFace.move(component,0, 0, value);
			}
			else {
				System.out.print("Ain't Got No Transformation");
			}
		}
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
