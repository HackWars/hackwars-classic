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

public class SpriteHandler extends JPanel implements ActionListener,ListSelectionListener{
	public static byte r[]={(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)255,(byte)255,(byte)255,(byte)255,(byte)153,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)204,(byte)204,(byte)204,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)204,(byte)204,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)255,(byte)255,(byte)153,(byte)204,(byte)153,(byte)102,(byte)153,(byte)204,(byte)255,(byte)153,(byte)153,(byte)102,(byte)153,(byte)204,(byte)255,(byte)204,(byte)255,(byte)255,(byte)102,(byte)153,(byte)102,(byte)102,(byte)153,(byte)204,(byte)255,(byte)153,(byte)102,(byte)153,(byte)204,(byte)255,(byte)153,(byte)204,(byte)255,(byte)255,(byte)51,(byte)102,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)204,(byte)204,(byte)204,(byte)255,(byte)255,(byte)204,(byte)153,(byte)255,(byte)255,(byte)0,(byte)51,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)153,(byte)102,(byte)204,(byte)51,(byte)51,(byte)102,(byte)0,(byte)51,(byte)102,(byte)153,(byte)204,(byte)204,(byte)153,(byte)153,(byte)153,(byte)102,(byte)102,(byte)0,(byte)51,(byte)0,(byte)51,(byte)102,(byte)153,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)204,(byte)0,(byte)0,(byte)0,(byte)51,(byte)102,(byte)153,(byte)204,(byte)51,(byte)153,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)153,(byte)204,(byte)0,(byte)51,(byte)0,(byte)0,(byte)51,(byte)153,(byte)153,(byte)0,(byte)102,(byte)153,(byte)153,(byte)153,(byte)102,(byte)102,(byte)204,(byte)204,(byte)0,(byte)102,(byte)51,(byte)0,(byte)102,(byte)102,(byte)102,(byte)0,(byte)51,(byte)102,(byte)102,(byte)102,(byte)51,(byte)153,(byte)204,(byte)153,(byte)0,(byte)102,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)0,(byte)0,(byte)51,(byte)51,(byte)51,(byte)51,(byte)102,(byte)153,(byte)102,(byte)0,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)51,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)102,(byte)51,(byte)0,(byte)0,(byte)51,(byte)102,(byte)102,(byte)51,(byte)0,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)6,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0};
	public static byte g[]={(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)204,(byte)153,(byte)102,(byte)51,(byte)204,(byte)153,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)255,(byte)255,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)153,(byte)102,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)51,(byte)0,(byte)255,(byte)255,(byte)204,(byte)102,(byte)153,(byte)204,(byte)255,(byte)102,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)102,(byte)0,(byte)255,(byte)255,(byte)204,(byte)153,(byte)153,(byte)204,(byte)255,(byte)102,(byte)51,(byte)102,(byte)102,(byte)102,(byte)0,(byte)51,(byte)102,(byte)0,(byte)255,(byte)255,(byte)153,(byte)204,(byte)255,(byte)204,(byte)255,(byte)153,(byte)102,(byte)153,(byte)153,(byte)51,(byte)0,(byte)0,(byte)51,(byte)0,(byte)204,(byte)204,(byte)102,(byte)153,(byte)204,(byte)255,(byte)255,(byte)204,(byte)153,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)0,(byte)0,(byte)204,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)153,(byte)102,(byte)51,(byte)0,(byte)51,(byte)0,(byte)102,(byte)102,(byte)153,(byte)153,(byte)153,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)102,(byte)51,(byte)51,(byte)204,(byte)102,(byte)153,(byte)204,(byte)255,(byte)255,(byte)153,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)0,(byte)0,(byte)255,(byte)255,(byte)153,(byte)204,(byte)255,(byte)255,(byte)204,(byte)102,(byte)153,(byte)153,(byte)153,(byte)51,(byte)0,(byte)0,(byte)51,(byte)0,(byte)255,(byte)255,(byte)204,(byte)153,(byte)255,(byte)204,(byte)153,(byte)51,(byte)102,(byte)102,(byte)102,(byte)102,(byte)0,(byte)51,(byte)102,(byte)0,(byte)255,(byte)255,(byte)204,(byte)255,(byte)204,(byte)153,(byte)102,(byte)102,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)102,(byte)0,(byte)255,(byte)255,(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)153,(byte)102,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)51,(byte)0,(byte)204,(byte)153,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)204,(byte)153,(byte)102,(byte)51,(byte)6,(byte)12,(byte)18,(byte)24,(byte)30,(byte)36,(byte)42,(byte)48,(byte)54,(byte)60,(byte)66,(byte)72,(byte)78,(byte)86,(byte)92,(byte)100,(byte)106,(byte)112,(byte)118,(byte)124,(byte)130,(byte)136,(byte)142,(byte)148,(byte)154,(byte)160,(byte)166,(byte)172,(byte)178,(byte)184,(byte)190,(byte)196,(byte)202,(byte)208,(byte)214,(byte)220,(byte)226,(byte)232,(byte)238,(byte)244};
	public static byte b[]={(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)51,(byte)102,(byte)102,(byte)51,(byte)0,(byte)51,(byte)0,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)51,(byte)51,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)102,(byte)51,(byte)0,(byte)102,(byte)51,(byte)51,(byte)51,(byte)51,(byte)51,(byte)0,(byte)0,(byte)51,(byte)51,(byte)51,(byte)51,(byte)102,(byte)153,(byte)102,(byte)0,(byte)102,(byte)51,(byte)0,(byte)102,(byte)102,(byte)102,(byte)51,(byte)0,(byte)102,(byte)102,(byte)102,(byte)51,(byte)153,(byte)204,(byte)153,(byte)0,(byte)51,(byte)0,(byte)0,(byte)51,(byte)153,(byte)153,(byte)102,(byte)0,(byte)153,(byte)153,(byte)153,(byte)102,(byte)102,(byte)204,(byte)204,(byte)0,(byte)0,(byte)0,(byte)51,(byte)102,(byte)153,(byte)204,(byte)153,(byte)51,(byte)204,(byte)204,(byte)153,(byte)102,(byte)51,(byte)153,(byte)51,(byte)51,(byte)102,(byte)0,(byte)51,(byte)102,(byte)153,(byte)204,(byte)204,(byte)153,(byte)153,(byte)153,(byte)102,(byte)102,(byte)0,(byte)51,(byte)0,(byte)51,(byte)102,(byte)153,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)204,(byte)0,(byte)51,(byte)51,(byte)102,(byte)153,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)153,(byte)102,(byte)204,(byte)204,(byte)51,(byte)102,(byte)51,(byte)102,(byte)153,(byte)255,(byte)204,(byte)204,(byte)204,(byte)255,(byte)204,(byte)255,(byte)204,(byte)153,(byte)255,(byte)255,(byte)102,(byte)153,(byte)102,(byte)102,(byte)255,(byte)204,(byte)153,(byte)102,(byte)153,(byte)255,(byte)204,(byte)153,(byte)153,(byte)204,(byte)255,(byte)255,(byte)153,(byte)204,(byte)153,(byte)255,(byte)204,(byte)153,(byte)102,(byte)153,(byte)153,(byte)255,(byte)204,(byte)153,(byte)102,(byte)204,(byte)255,(byte)255,(byte)204,(byte)204,(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)204,(byte)204,(byte)255,(byte)204,(byte)153,(byte)102,(byte)51,(byte)255,(byte)255,(byte)153,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)204,(byte)204,(byte)255,(byte)255,(byte)255,(byte)255,(byte)6,(byte)12,(byte)18,(byte)24,(byte)30,(byte)36,(byte)42,(byte)48,(byte)54,(byte)60,(byte)66,(byte)72,(byte)78,(byte)86,(byte)92,(byte)100,(byte)106,(byte)112,(byte)118,(byte)124,(byte)130,(byte)136,(byte)142,(byte)148,(byte)154,(byte)160,(byte)166,(byte)172,(byte)178,(byte)184,(byte)190,(byte)196,(byte)202,(byte)208,(byte)214,(byte)220,(byte)226,(byte)232,(byte)238,(byte)244};
	private HacktendoCreator hacktendoCreator=null;
	private JList list=null;
	private int frame=0;
	private DefaultListModel listModel=new DefaultListModel();
	private JTabbedPane parent=null;
	private JFileChooser fc=new JFileChooser();
	private ImagePanel imagePanel;
	private Sprite selectedSprite=null;
	private JButton prevFrame,nextFrame;

	public SpriteHandler(HacktendoCreator hacktendoCreator,JTabbedPane parent){
		this.hacktendoCreator = hacktendoCreator;
		this.parent=parent;
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.addChoosableFileFilter(new ImageFilter());
		fc.setAcceptAllFileFilterUsed(false);
		setLayout(null);
		populate();
	}
	
	private void populate(){
		
		for(int i=0;i<64;i++){
			listModel.addElement(""+i);
		}
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.addListSelectionListener(this);
		
		JScrollPane sp = new JScrollPane(list);
		add(sp);
		sp.setBounds(0,10,100,parent.getHeight()-50);
		
		JButton importButton = new JButton("Import");
		add(importButton);
		importButton.addActionListener(this);
		importButton.setBounds(110,40,importButton.getPreferredSize().width,importButton.getPreferredSize().height);
		
		JButton exportButton = new JButton("Export");
		add(exportButton);
		exportButton.addActionListener(this);
		exportButton.setEnabled(false);
		exportButton.setToolTipText("Coming Soon");
		exportButton.setBounds(110,80,exportButton.getPreferredSize().width,exportButton.getPreferredSize().height);
		
		imagePanel = new ImagePanel(null,this,8,0,this);
		add(imagePanel);
		imagePanel.setBounds(200,10,256,256);
		
		prevFrame = new JButton(ImageLoader.getImageIcon("images/left.png"));
		add(prevFrame);
		prevFrame.addActionListener(this);
		prevFrame.setActionCommand("Prev");
		prevFrame.setContentAreaFilled(false);
		prevFrame.setBorderPainted(false);
		prevFrame.setBounds(200,280,prevFrame.getPreferredSize().width,prevFrame.getPreferredSize().height);
		prevFrame.setEnabled(false);
		
		nextFrame = new JButton(ImageLoader.getImageIcon("images/right.png"));
		add(nextFrame);
		nextFrame.addActionListener(this);
		nextFrame.setContentAreaFilled(false);
		nextFrame.setBorderPainted(false);
		nextFrame.setActionCommand("Next");
		nextFrame.setBounds(424,280,nextFrame.getPreferredSize().width,nextFrame.getPreferredSize().height);
		
		
	}
	
	public void setSprite(int index,BufferedImage image,int trans){
		hacktendoCreator.setSprite(index,image,trans);
		selectedSprite = hacktendoCreator.getSprite(index);
		imagePanel.setImage(selectedSprite.getFrame(frame));
		imagePanel.repaint();
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Import")){
			//show file browser
			int index = list.getSelectedIndex();
			if(index!=-1){
				int choice = fc.showDialog(this,"Import");
				if(choice==JFileChooser.APPROVE_OPTION){
					File f = fc.getSelectedFile();
					try{
						BufferedImage BI = ImageIO.read(f);
						BufferedImage img = null;
						if(BI.getColorModel() instanceof IndexColorModel)
							img=BI;
						else
							img = new BufferedImage(BI.getWidth(),BI.getHeight(),BufferedImage.TYPE_BYTE_INDEXED);
						
						Graphics gr = img.createGraphics();
						gr.drawImage(BI,0,0,null);
						gr.dispose();
						//image = img;
						SpritePreview spritePreview = new SpritePreview(img,index,this);
						hacktendoCreator.getPanel().add(spritePreview);
						spritePreview.moveToFront();
					}catch(Exception ex){ex.printStackTrace();}
					//System.out.println(f.getName());
				}
			}
			
			
		}
		if(e.getActionCommand().equals("Export")){
			//show file browser
		}
		if(e.getActionCommand().equals("Next")){
			frame++;
			if(frame<7)
				nextFrame.setEnabled(true);
			else
				nextFrame.setEnabled(false);
			if(frame>0)
				prevFrame.setEnabled(true);
			else
				prevFrame.setEnabled(false);
			imagePanel.setImage(selectedSprite.getFrame(frame));
			imagePanel.repaint();
		}
		if(e.getActionCommand().equals("Prev")){
			frame--;
			if(frame<7)
				nextFrame.setEnabled(true);
			else
				nextFrame.setEnabled(false);
			if(frame>0)
				prevFrame.setEnabled(true);
			else
				prevFrame.setEnabled(false);
			imagePanel.setImage(selectedSprite.getFrame(frame));
			imagePanel.repaint();
		}
	}
	
	 public void valueChanged(ListSelectionEvent e) {
		 int index = list.getSelectedIndex();
		 if(index!=-1){
			 selectedSprite = hacktendoCreator.getSprite(index);
			 if(selectedSprite!=null)
				 imagePanel.setImage(selectedSprite.getFrame(frame));
			 else
				 imagePanel.setImage(null);
		 }
		 
	 }
	 
	 public static void main(String args[]){
		 BufferedImage img = new BufferedImage(512,512,BufferedImage.TYPE_INT_RGB);
		 Graphics2D g = img.createGraphics();
		 int index=0;
		 for(int x=0;x<16;x++){
			 for(int y=0;y<16;y++){
				 int red = r[index];
				 int blue = b[index];
				 int green = SpriteHandler.g[index];
				 if(red<0)
					 red += 256;
				 if(green<0)
					 green += 256;
				 if(blue<0)
					 blue += 256;
				 //System.out.println(red+"  "+green+"  "+blue);
				 Color col = new Color(red,green,blue);
				 g.setColor(col);
				 g.fillRect(y*32,x*32,32,32);
				 index++;
			 }
		 }
		// byte[] b = ((DataBufferByte)(img).getRaster().getDataBuffer()).getData();
		 
		 try{
			 File f = new File("pallete.png");
			 ImageIO.write(img,"png",f);
		 }catch(Exception e){e.printStackTrace();}
		 
		 
	 }
}
