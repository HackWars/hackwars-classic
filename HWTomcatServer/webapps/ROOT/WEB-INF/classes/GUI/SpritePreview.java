package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class SpritePreview extends JInternalFrame{
	public static int SCALE=2;
	private BufferedImage image=null;
	public SpritePreview(BufferedImage image,int index,SpriteHandler spriteHandler){
		this.image=image;
		/*Image im = image.getScaledInstance(64,128,Image.SCALE_FAST);
		image = new BufferedImage(64,128,BufferedImage.TYPE_BYTE_INDEXED);
		Graphics g = image.getGraphics();
		g.drawImage(im,0,0,null);*/
		setClosable(true);
		setTitle("Preview");
		setBounds(50,10,image.getWidth()*SCALE+15,image.getHeight()*SCALE+35);
		setVisible(true);
		ImagePanel panel = new ImagePanel(image,this,SCALE,index,spriteHandler);
		add(panel);
	}

}
