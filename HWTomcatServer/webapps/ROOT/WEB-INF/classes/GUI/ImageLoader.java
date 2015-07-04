package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.net.*;

public class ImageLoader{

	public static String tmpDir = System.getProperty("java.io.tmpdir");;
	public static ImageIcon PCI_ICON;
	public static ImageIcon FOLDER_ICON;
	public static ImageIcon TEXT_ICON;
	public static ImageIcon FIREWALL_ICON;
	public static ImageIcon SCRIPT_ICON;
	public static ImageIcon IMAGE_ICON;
	public static File STATS;
	public static BufferedImage DUCT_TAPE;
	public static BufferedImage GERMANIUM;
	public static BufferedImage SILICON;
	public static BufferedImage YBCO;
	public static BufferedImage PLUTONIUM;
	
	public static void init(){
		PCI_ICON = getImageIcon("images/pci.png");
		FOLDER_ICON = getImageIcon("images/folderBig.png");
		TEXT_ICON = getImageIcon("images/textfile.png");
		FIREWALL_ICON = getImageIcon("images/firewallHome.png");
		SCRIPT_ICON = getImageIcon("images/scriptHome.png");
		IMAGE_ICON = getImageIcon("images/image.png");
		STATS = getFile("images/sidebar.png");
		DUCT_TAPE = getImage("images/ducttape.png");
		GERMANIUM = getImage("images/germanium.png");
		SILICON = getImage("images/silicon.png");
		YBCO = getImage("images/YBCO.png");
		PLUTONIUM = getImage("images/plutonium.png");
	}
	
	public static ImageIcon getImageIcon(String location){
		File F=new File(tmpDir+"/"+location);
		if(F.exists()){
			return new ImageIcon(tmpDir+"/"+location);
		}
		try{
			File checkdir=new File(tmpDir+"/images");
			if(!checkdir.exists())
				checkdir.mkdir();
				
			URL U=new URL("http://www.crackjawpublishing.com/hackwars/"+location);
			InputStream in=U.openStream();
			FileOutputStream out=new FileOutputStream(new File(tmpDir+"/"+location));
			byte buf[]=new byte[256];
			int size=0;
			while((size=in.read(buf))>0){
				out.write(buf,0,size);
			}
			return new ImageIcon(tmpDir+"/"+location);
		}catch(Exception e){
		}
		return(null);
	}
	
	public static File getFile(String location){
		File F=new File(tmpDir+"/"+location);
		if(F.exists()){
			return new File(tmpDir+"/"+location);
		}
		try{
			File checkdir=new File(tmpDir+"/images");
			if(!checkdir.exists())
				checkdir.mkdir();
		
			URL U=new URL("http://www.crackjawpublishing.com/hackwars/"+location);
			InputStream in=U.openStream();
			FileOutputStream out=new FileOutputStream(new File(tmpDir+"/"+location));
			byte buf[]=new byte[256];
			int size=0;
			while((size=in.read(buf))>0){
				out.write(buf,0,size);
			}
			return new File(tmpDir+"/"+location);
		}catch(Exception e){
		}
		return(null);
	}
	
	public static BufferedImage getImage(String location){
		try{
			return ImageIO.read(getFile(location).toURL());
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
