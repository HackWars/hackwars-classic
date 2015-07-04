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

public class ImagePanel extends JPanel implements MouseListener{

	private BufferedImage image=null;
	private BufferedImage original=null;
	private JComponent parent=null;
	private int scale=2;
	private int index=0;
	private SpriteHandler spriteHandler;
	public ImagePanel(BufferedImage image,JComponent parent,int scale,int index,SpriteHandler spriteHandler){
		 this.image=image;
		 original=image;
		 this.parent=parent;
		 this.index=index;
		 this.spriteHandler=spriteHandler;
		 this.scale=scale;
		 //setBounds(
		 addMouseListener(this);
	}
	
	public void setImage(BufferedImage image){
		this.image=image;
		original=image;
		repaint();
	}
	
	public void paintComponent(Graphics g){
		g.setColor(new Color(238,238,238));
		if(image!=null){
			if(parent instanceof SpritePreview){
				g.fillRect(0,0,parent.getWidth(),parent.getHeight());
				g.drawImage(image,0,0,image.getWidth()*scale/2,image.getHeight()*scale/2,0,0,image.getWidth(),image.getHeight(),this);
				g.setColor(Color.black);
				g.drawRect(0,0,image.getWidth()*scale,image.getHeight()*scale);
				g.drawLine(32*scale,0,32*scale,128*scale);
				g.drawLine(0,32*scale,64*scale,32*scale);
				g.drawLine(0,64*scale,64*scale,64*scale);
				g.drawLine(0,96*scale,64*scale,96*scale);
			}
			else{
				g.setColor(new Color(238,238,238));
				g.fillRect(0,0,256,256);
				g.drawImage(image,0,0,image.getWidth()*scale/2,image.getHeight()*scale/2,0,0,image.getWidth(),image.getHeight(),this);
			}
		}
		else{
			g.setColor(new Color(238,238,238));
			g.fillRect(0,0,256,256);
		}
	}
	
	public void mouseEntered(MouseEvent e){

	}

	public void mouseExited(MouseEvent e){

	}

	public void mousePressed(MouseEvent e){

	}

	public void mouseReleased(MouseEvent e){

	}

	public void mouseClicked(MouseEvent e){
		if(parent instanceof SpritePreview){
			int trans=-1;
		
			if(e.getButton()==e.BUTTON1){
		
				byte b[] = ((DataBufferByte)(image).getRaster().getDataBuffer()).getData();
				int index = e.getX()/scale+e.getY()/scale*image.getWidth();
				trans = b[index];
				if(trans<(byte)0){
					trans+=256;
				}
				byte B[]=((DataBufferByte)(image).getRaster().getDataBuffer()).getData();
				
				
				IndexColorModel MyIndexColorModel=(IndexColorModel)image.getColorModel();
				
				byte ri[]=new byte[256];
				byte gi[]=new byte[256];
				byte bi[]=new byte[256];
				for(int i=0;i<256;i++){
					int c[]=MyIndexColorModel.getComponents(i,null,0);
					ri[i]=(byte)c[0];
					gi[i]=(byte)c[1];
					bi[i]=(byte)c[2];
				}
				
				BufferedImage BI = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_BYTE_INDEXED,new IndexColorModel(8,256,ri,gi,bi,trans));
				
				b=((DataBufferByte)(BI).getRaster().getDataBuffer()).getData();
				for(int ii=0;ii<b.length;ii++){
					b[ii]=B[ii];
				}
				
				image=BI;
			}

			spriteHandler.setSprite(this.index,image,trans);
			repaint();
		}
			

	}
}
