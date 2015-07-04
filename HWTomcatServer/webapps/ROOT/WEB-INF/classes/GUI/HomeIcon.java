package GUI;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import View.*;
import Assignments.*;
import Game.*;
import java.util.*;
import javax.imageio.*;
import java.net.URL;
import java.awt.image.*;

public class HomeIcon extends JPanel implements MouseListener,ActionListener{
	public static final Color VALUE=Color.red;
	public static final Color CONSUMER=new Color(41,144,231);
	public static final Color PREMIUM=Color.orange;
	public static final Color EXPERIMENTAL=Color.yellow;
	public static final Color ALIEN=Color.green;
	public static final int BANKING_COMPILED=0;
	public static final int BANKING_SCRIPT=1;
	public static final int ATTACKING_COMPILED=2;
	public static final int ATTACKING_SCRIPT=3;
	public static final int WATCH_COMPILED=4;
	public static final int WATCH_SCRIPT=5;
	public static final int FTP_COMPILED=6;
	public static final int FTP_SCRIPT=7;
	public static final int FIREWALL=8;
	public static final int TEXT=9;
	public static final int CPU=10;
	public static final int HD=11;
	public static final int HTTP=12;
	public static final int MEMORY=13;
	public static final int IMAGE=14;
	public static final int HTTP_SCRIPT=15;
	public static final int CLUE=16;
	public static final int BOUNTY=17;
	public static final int AGP=18;
	public static final int PCI=19;
	public static final int GAME_PROJECT=20;
	public static final int GAME=21;
	public static final int QUEST_GAME=22;
	public static final int SHIPPING_COMPILED=23;
	public static final int SHIPPING_SCRIPT=24;
	public static final int FOLDER=25;
	
	
	
	private final String[] types=new String[]{"Compiled Banking Script","Banking Script","Compiled Attack Script",
						"Attack Script","Compiled Watch Script","Watch Script","Compiled FTP Script",
						"FTP Script","Firewall","Text File","CPU","Hard Drive","Compiled HTTP Script",
						"Memory","Image","HTTP Script","Document","Bounty","AGP Card","PCI Card","Game Project","Game","Special Game","Compiled Shipping Script","Shipping Script","Quest","Commodity","Challenge","Folder"};
	private ImageIcon[] images=new ImageIcon[]{ImageLoader.SCRIPT_ICON,ImageLoader.TEXT_ICON,ImageLoader.SCRIPT_ICON,
							ImageLoader.TEXT_ICON,ImageLoader.SCRIPT_ICON,ImageLoader.TEXT_ICON,
							ImageLoader.SCRIPT_ICON,ImageLoader.TEXT_ICON,ImageLoader.FIREWALL_ICON,
							ImageLoader.TEXT_ICON,ImageLoader.SCRIPT_ICON,ImageLoader.SCRIPT_ICON,
							ImageLoader.SCRIPT_ICON,ImageLoader.SCRIPT_ICON,ImageLoader.IMAGE_ICON,
							ImageLoader.TEXT_ICON,ImageLoader.TEXT_ICON,ImageLoader.TEXT_ICON,
							ImageLoader.PCI_ICON,ImageLoader.PCI_ICON,ImageLoader.FOLDER_ICON,ImageLoader.FOLDER_ICON,ImageLoader.FOLDER_ICON,ImageLoader.FOLDER_ICON,ImageLoader.FOLDER_ICON,ImageLoader.FOLDER_ICON,ImageLoader.FOLDER_ICON,ImageLoader.FOLDER_ICON,ImageLoader.FOLDER_ICON};
	private HomePopUp HPU;
	private String name,a1,a2;
	private Color textColor;
	private Hacker MyHacker;
	private String fileName;
	private int type;
	private int equipped=-1;
	private float price=-1.0f;
	private String folder="";
	private HomeList invList;
	private JLabel label;
	private JButton button;
	private boolean selected=false;
	private HackerFile file=null;
	public HomeIcon(Hacker MyHacker,HomeList invList,HomePopUp HPU,String name,int type,String folder){
		this.HPU=HPU;
		this.name=name;
		this.MyHacker=MyHacker;
		this.type=type;
		this.folder=folder;
		this.invList=invList;
		//setIcon(images[type]);
		setLayout(null);
		//addMouseListener(this);
		populate();
		//setBackground(textColor);
	}
	
	public void populate(){
		button = new JButton(images[type]);
		//System.out.println(images[type]);
		add(button);
		button.setBounds(5,2,images[type].getIconWidth()+5,images[type].getIconHeight());
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.addMouseListener(this);
		//button.setBounds(5,2,50,50);
		button.setOpaque(false);
		//System.out.println(name);
		label = new JLabel(name);
		label.setBounds(2,images[type].getIconHeight()+4,50,label.getPreferredSize().height);
		label.setOpaque(false);
		//label.addMouseListener(this);
		setComponentZOrder(label,0);
		//System.out.println(label.getBounds().y);
		
	}
	
	
	public void receivedFile(HackerFile HF){
		file=HF;
		if(type!=PCI&&type!=AGP)
			HPU.setInfo(name,types[type],HF.getDescription(),HF.getPrice(),HF.getQuantity(),HF.getMaker());
		else{
			String cardName = HF.getDescription();
			HashMap content = HF.getContent();
			if(content.get("attribute0")!=null){
				int quality1 = Integer.valueOf((String)content.get("quality0"));
				int quality2 = Integer.valueOf((String)content.get("quality1"));
				String values = (String)content.get("bonusdata");
				String[] v = values.split("\\|");
				String value1 = v[0];
				String value2 = v[1];
				Color type=Inventory.VALUE;
				if(quality1+quality2==18)
					type=Inventory.ALIEN;
				else if(quality1+quality2>=14)
					type=Inventory.EXPERIMENTAL;
				else if(quality1+quality2>=9)
					type=Inventory.PREMIUM;
				else if(quality1+quality2>5)
					type=Inventory.CONSUMER;
				HPU.setInfo(cardName,types[this.type],value1,value2,type,HF.getPrice(),HF.getQuantity(),HF.getMaker(),HF.getName());
			}
		}
		//HPU.setVisible(true);
		MyHacker.getPanel().setComponentZOrder(HPU,0);
	}
	
	public int getType(){
		return type;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	public void setSelected(boolean selected){
		this.selected=selected;
	}
	
	public HackerFile getFile(){
		return file;
	}
	
	public void mouseEntered(MouseEvent e){
		//show Panel
		if(file==null||type==CLUE){
			if(type!=FOLDER){
				MyHacker.setRequestedFileIcon(this);
				MyHacker.setRequestedFile(Hacker.HOME);
				View MyView = MyHacker.getView();
				Object objects[] = {MyHacker.getEncryptedIP(),folder,name};
				MyView.setFunction("requestfile");
				MyView.addFunctionCall(new RemoteFunctionCall(0,"requestfile",objects));
				int x=0;
				int y=0;
				try{	
					x = getLocationOnScreen().x-MyHacker.getPanel().getLocationOnScreen().x;//getX()+e.getX()+10;
					y = getLocationOnScreen().y-MyHacker.getPanel().getLocationOnScreen().y+60;//getY()+e.getY()+10;
				}catch(IllegalComponentStateException ex){
					return;
				}
				HPU.setBounds(x,y,400,100);
				HPU.setVisible(true);
			}
		}
		else{
			int x=0;
			int y=0;
			try{	
				x = getLocationOnScreen().x-MyHacker.getPanel().getLocationOnScreen().x;//getX()+e.getX()+10;
				y = getLocationOnScreen().y-MyHacker.getPanel().getLocationOnScreen().y+60;//getY()+e.getY()+10;
			}catch(IllegalComponentStateException ex){
				return;
			}
			HPU.setBounds(x,y,400,100);
			receivedFile(file);
			HPU.setVisible(true);
		}
		
	}
	

	public void mouseExited(MouseEvent e){
		//contentPane.remove(EPU);
		HPU.setVisible(false);

	}
	

	public void mousePressed(MouseEvent e){
		if(e.isPopupTrigger()){
			JPopupMenu menu = new JPopupMenu();
			JMenuItem menuItem;
			
			menuItem = new JMenuItem("Set Price");
			menuItem.addActionListener(this);
			menu.add(menuItem);
			menuItem = new JMenuItem("Set Description");
			menuItem.addActionListener(this);
			menu.add(menuItem);

			HPU.setVisible(false);
			menu.show(MyHacker.getPanel(),getLocationOnScreen().x-MyHacker.getPanel().getLocationOnScreen().x,getLocationOnScreen().y-MyHacker.getPanel().getLocationOnScreen().y);

			invList.resetBackgrounds();
			setBackground(CONSUMER);
			selected=true;
			HPU.setVisible(false);
		}
	}
	

	public void mouseReleased(MouseEvent e){
		if(e.isPopupTrigger()){
			JPopupMenu menu = new JPopupMenu();
			JMenuItem menuItem;
			
			menuItem = new JMenuItem("Set Price");
			menuItem.addActionListener(this);
			menu.add(menuItem);
			menuItem = new JMenuItem("Set Description");
			menuItem.addActionListener(this);
			menu.add(menuItem);

			HPU.setVisible(false);
			menu.show(MyHacker.getPanel(),getLocationOnScreen().x-MyHacker.getPanel().getLocationOnScreen().x,getLocationOnScreen().y-MyHacker.getPanel().getLocationOnScreen().y);

			invList.resetBackgrounds();
			setBackground(CONSUMER);
			selected=true;
			HPU.setVisible(false);
		}

	}

	public void mouseClicked(MouseEvent e){
		if(e.isPopupTrigger()){
			JPopupMenu menu = new JPopupMenu();
			JMenuItem menuItem;
			
			menuItem = new JMenuItem("Set Price");
			menuItem.addActionListener(this);
			menu.add(menuItem);
			menuItem = new JMenuItem("Set Description");
			menuItem.addActionListener(this);
			menu.add(menuItem);

			HPU.setVisible(false);
			menu.show(MyHacker.getPanel(),getLocationOnScreen().x-MyHacker.getPanel().getLocationOnScreen().x,getLocationOnScreen().y-MyHacker.getPanel().getLocationOnScreen().y);
			invList.resetBackgrounds();
			setBackground(CONSUMER);
			selected=true;
			HPU.setVisible(false);
			
		}
		else{
			invList.resetBackgrounds();
			setBackground(CONSUMER);
			selected=true;
			HPU.setVisible(false);
			
		}
		if(e.getClickCount()==2){
			if(type==FOLDER){
				invList.changeDirectory(name);
			}
			if(type==IMAGE){
				Object[] o = new Object[]{name,IMAGE}; 
				ImageViewer im = new ImageViewer("Image Viewer",false,false,true,true,MyHacker.getPanel(),MyHacker,o,folder);
				im.setBounds(100,100,450,450);
				im.populate();
				im.moveToFront();
				MyHacker.getPanel().add(im);
				im.setVisible(true);
			}else if(type==HackerFile.GAME||type==HackerFile.QUEST_GAME){
					String encryptedIP = MyHacker.getEncryptedIP();
					View MyView = MyHacker.getView();
					MyHacker.setRequestedFile(Hacker.HACKTENDO_PLAYER);
					Object[] objects = {encryptedIP,folder,name};
					MyView.setFunction("requestgame");
					MyView.addFunctionCall(new RemoteFunctionCall(0,"requestgame",objects));
			}
		}
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Set Price")){
			HomePriceDialog HPD = new HomePriceDialog(invList.getHome());
			file=null;
		}
		if(e.getActionCommand().equals("Set Description")){
			HomeDescriptionDialog HDD = new HomeDescriptionDialog(invList.getHome(),file.getDescription());
			file=null;
		}
	}
}
