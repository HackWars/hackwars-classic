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

public class TileHandler extends JPanel implements ActionListener,ListSelectionListener{

	private HacktendoCreator hacktendoCreator=null;
	private JTabbedPane parent=null;
	private JFileChooser fc=new JFileChooser();
	private TilePanel tilePanel=null;
	private int index=0;
	
	public TileHandler(HacktendoCreator hacktendoCreator,JTabbedPane parent){
		this.hacktendoCreator=hacktendoCreator;
		this.parent=parent;
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.addChoosableFileFilter(new ImageFilter());
		fc.setAcceptAllFileFilterUsed(false);
		setLayout(null);
		populate();
	}
	
	private void populate(){
		tilePanel = new TilePanel(hacktendoCreator,this);
		tilePanel.setBounds(0,0,256,512);
		tilePanel.setPreferredSize(new Dimension(260,525));
		JScrollPane sp = new JScrollPane(tilePanel);
		add(sp);
		sp.setBounds(10,10,280,280);
		
		JButton importButton = new JButton("Import");
		add(importButton);
		importButton.addActionListener(this);
		importButton.setBounds(310,40,importButton.getPreferredSize().width,importButton.getPreferredSize().height);
	}

	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Import")){
			//show file browser
			//int index = list.getSelectedIndex();
			int choice = fc.showDialog(this,"Import");
			if(choice==JFileChooser.APPROVE_OPTION){
				File f = fc.getSelectedFile();
				try{
					BufferedImage BI = ImageIO.read(f);
					BufferedImage img=null;
					if(BI.getColorModel() instanceof IndexColorModel)
						img=BI;
					else
						img = new BufferedImage(BI.getWidth(),BI.getHeight(),BufferedImage.TYPE_BYTE_INDEXED);
					Graphics gr = img.createGraphics();
					gr.drawImage(BI,0,0,null);
					gr.dispose();
					int index = tilePanel.getSelectedIndex();
					hacktendoCreator.importTiles(img,index);
					//image = img;
				}catch(Exception ex){ex.printStackTrace();}
				//System.out.println(f.getName());
			}
			
			
		}
	}
	
	 public void valueChanged(ListSelectionEvent e) {
	 }
}
