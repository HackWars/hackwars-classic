package View;
/*
Programmed: Ben Coe.(2006)<br />
Allows an editor to choose tiles to place.

*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ImageWidget extends JButton{
	/////////////////////
	// data.
	private Rectangle drawBounds=null;
	private aImage bgImage=null;

	///////////////////////////////////////////////////
	// CONSTRUCTOR
	// Description:
	// Set up new countrywidget based on name and
	// position.
	///////////////////////////////////////////////////
	public ImageWidget(int width,int height,String path){
		//Set style.
		drawBounds=new Rectangle(0,0,width,height);
		this.setEnabled(false);
		//this.setLayout(null);
		this.setOpaque(false);
		this.setBounds(0,0,width,height);
		this.setVisible(true);
		this.setBorder(null);
		bgImage=new aImage(path,this);;
	}


	///////////////////////////////////////////////////
	// paintComponent()
	// Description:
	// Take care of custom painting of widget.
	///////////////////////////////////////////////////
	public void paintComponent(Graphics g){
		super.paintComponent(g);//Paint all parents
		if(bgImage!=null)
			bgImage.paint(g,drawBounds,this);//Paint background.
	}
}//END.
