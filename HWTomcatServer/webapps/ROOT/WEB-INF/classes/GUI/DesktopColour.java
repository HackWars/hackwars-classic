package GUI;
/**

DesktopColour.java
this is the desktop colour chooser window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class DesktopColour extends Application implements ChangeListener{
	private JDesktopPane mainPanel=null;
	private Hacker MyHacker=null;
	private JColorChooser colourChooser;
	public DesktopColour(String name,boolean resize,boolean max,boolean close,boolean iconify,JDesktopPane mainPanel,Hacker MyHacker){
		this.setTitle(name);
		this.setResizable(resize);
		this.setMaximizable(max);
		this.setClosable(close);
		this.setIconifiable(iconify);
		this.addInternalFrameListener(this);
		this.mainPanel=mainPanel;
		this.MyHacker=MyHacker;
		this.setFrameIcon(new ImageIcon("images/colour.png"));
	}

	public void populate(){

		Insets insets = this.getInsets();
		Rectangle r = this.getBounds();
		colourChooser = new JColorChooser();
		colourChooser.setPreviewPanel(new JPanel());
		this.add(colourChooser);
		colourChooser.setBounds(insets.left,insets.top,r.width-20,r.height-20);
		colourChooser.getSelectionModel().addChangeListener(this);


	}

	public void internalFrameIconified(InternalFrameEvent e) {
	    mainPanel.repaint();
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		MyHacker.setColourOpen(false);
	}

	public void stateChanged(ChangeEvent e){
		MyHacker.setDesktopColour(colourChooser.getColor());
		//mainPanel.repaint();
	}
}
