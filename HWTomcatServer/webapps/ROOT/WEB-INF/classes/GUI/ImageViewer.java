package GUI;
/**

MessageWindow.java
this is the message window.
*/

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

public class ImageViewer extends Application{
	private JDesktopPane mainPanel=null;
	private Hacker MyHacker=null;
	private HtmlHandler imagePane;
	private Object[] image;
	private ImageViewerPanel panel;
	public ImageViewer(String name,boolean resize,boolean max,boolean close,boolean iconify,JDesktopPane mainPanel,Hacker MyHacker,Object[] image,String path){
		//System.out.println("Starting ImageViewer");
		this.setTitle(name);
		this.setResizable(resize);
		this.setMaximizable(max);
		this.setClosable(close);
		this.setIconifiable(iconify);
		this.addInternalFrameListener(this);
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		this.mainPanel=mainPanel;
		this.MyHacker=MyHacker;
		this.image=image;
		
		MyHacker.setRequestedFile(Hacker.IMAGE_VIEWER);
		MyHacker.setImageViewer(this);
		View MyView = MyHacker.getView();
		String ip=MyHacker.getEncryptedIP();
		Object objects[] = {ip,path,image[0]};
		//System.out.println(path+"|"+image[0]);
		MyView.setFunction("requestfile");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"requestfile",objects));
				
		//this.setFrameIcon(ImageLoader.getImageIcon("images/calc.png"));
	}

	public void populate(){
		//System.out.println("Populating ImageViewer");
		/*imagePane = new HtmlHandler();
		imagePane.createView();
		imagePane.setBounds(0,0,400,400);
		JTabbedPane scrollPane = new JTabbedPane();
		add(scrollPane);
		scrollPane.setBounds(10,10,getBounds().width-20,getBounds().height-75);
		scrollPane.add((String)image[0],imagePane.getView());*/
		panel = new ImageViewerPanel();
		add(panel);
		panel.setBounds(0,0,getBounds().width,getBounds().height);
		
	}
	
	public void gotImage(HackerFile image){
		//System.out.println("Received File");
		//System.out.println(image.getContent().get("data"));
		String path = (String)image.getContent().get("data");
		aImage aI = new aImage(path,panel);
		panel.setAImage(aI);
		int width = aI.getWidth();
		int height = aI.getHeight();
		panel.setBounds(0,0,width,height);
		setBounds(100,100,width+2,height+30);
		//panel.repaint();
		//<img src=\""+image.getContent().get("data")+"\">
		/*String doc = "<html><body><img src=\""+image.getContent().get("data")+"\"></body></html>";
		imagePane.parseDocument(doc);*/
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		//MyHacker.setMessageWindowOpen(false);
	}

}
