package GUI;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import View.*;
import Assignments.*;

/**
 * This is the popup menu for the Equipment Manager.
 * 
 */

public class Inventory extends JButton implements MouseListener,ActionListener {
	public static final Color VALUE=Color.red;
	public static final Color CONSUMER=new Color(41,144,231);
	public static final Color PREMIUM=Color.orange;
	public static final Color EXPERIMENTAL=Color.yellow;
	public static final Color ALIEN=Color.green;
	public static final int AGP=0;
	public static final int PCI=1;
	public static final int HD=2;
	public static final int MEMORY=3;
	public static final int CPU=4;
	private final String[] types=new String[]{"AGP Card","PCI Card","Hard Drive","Memory","CPU"};
	private final String[] images=new String[]{"images/pci.png","images/pci.png","images/hdinv.png","images/memoryinv.png","images/cpuinv.png"};
	private EquipmentPopUp EPU;
	private String name,a1,a2;
	private Color textColor;
	private Hacker MyHacker;
	private String fileName;
	private int type;
	private int equipped=-1;
	private float price=-1.0f;
	private String ip="",durability;
	private int[] cost;
	private SingleEquipmentPanel sePanel = null;
	public Inventory(Hacker MyHacker,EquipmentPopUp EPU,String name,String a1,String a2,Color textColor,int type,String fileName,int equipped,String durability,int[] cost){
		this.EPU=EPU;
		this.name=name;
		this.a1=a1;
		this.a2=a2;
		this.textColor=textColor;
		this.MyHacker=MyHacker;
		this.type=type;
		this.fileName=fileName;
		this.equipped=equipped;
		this.durability=durability;
		this.cost=cost;
		setBorderPainted(false);
		setContentAreaFilled(false);
		ImageIcon image = ImageLoader.getImageIcon(images[type]);
		setIcon(image);
		addMouseListener(this);
		//setBackground(textColor);
	}
	
	public void setPrice(float price){
		this.price=price;
	}
	
	public void setIP(String ip){
		this.ip=ip;
	}
	
	public void setEquipmentPanel(SingleEquipmentPanel sePanel){
		this.sePanel = sePanel;
	}
	
	/*public void paintComponent(Graphics g){
		g.setColor(new Color(1.0f,0.0f,0.0f,0.80f));
		g.fillRect(0,0,getWidth(),getHeight());
	}*/
	
	public void mouseEntered(MouseEvent e){
		//show Panel
		int x = ((JButton)e.getSource()).getLocationOnScreen().x-MyHacker.getPanel().getLocationOnScreen().x;//getX()+e.getX()+10;
		int y = ((JButton)e.getSource()).getLocationOnScreen().y-MyHacker.getPanel().getLocationOnScreen().y;//getY()+e.getY()+10;
		
		EPU.setBounds(x,y,400,100);
		EPU.setInfo(name,types[type],a1,a2,textColor,durability,cost);
		EPU.setPrice(price);
		EPU.setVisible(true);
		MyHacker.getPanel().setComponentZOrder(EPU,0);
		
		
	}
	

	public void mouseExited(MouseEvent e){
		//contentPane.remove(EPU);
		EPU.setVisible(false);

	}
	

	public void mousePressed(MouseEvent e){

	}

	public void mouseReleased(MouseEvent e){

	}

	public void mouseClicked(MouseEvent e){
		if(equipped!=100){
			if(type==AGP||type==PCI){
				if(equipped==-1){
					if(type==AGP){
						//install
						
						JPopupMenu menu = new JPopupMenu();
						JMenuItem menuItem;
						
						menuItem = new JMenuItem("Equip");
						menuItem.addActionListener(this);
						menu.add(menuItem);
						
						menuItem = new JMenuItem("Repair");
						menuItem.addActionListener(this);
						menu.add(menuItem);
						
						menuItem = new JMenuItem("Delete");
						menuItem.addActionListener(this);
						menu.add(menuItem);

						
						EPU.setVisible(false);
						menu.show(MyHacker.getPanel(),getLocationOnScreen().x-MyHacker.getPanel().getLocationOnScreen().x,getLocationOnScreen().y-MyHacker.getPanel().getLocationOnScreen().y);
					
						
					}
					if(type==PCI){
						//show pop up asking about which slot to install to
						JPopupMenu menu = new JPopupMenu();
						JMenuItem menuItem;
						
						menuItem = new JMenuItem("Slot 1");
						menuItem.addActionListener(this);
						menu.add(menuItem);
						
						menuItem = new JMenuItem("Slot 2");
						menuItem.addActionListener(this);
						menu.add(menuItem);
						
						menuItem = new JMenuItem("Repair");
						menuItem.addActionListener(this);
						menu.add(menuItem);

						menuItem = new JMenuItem("Delete");
						menuItem.addActionListener(this);
						menu.add(menuItem);

						
						EPU.setVisible(false);
						menu.show(MyHacker.getPanel(),getLocationOnScreen().x-MyHacker.getPanel().getLocationOnScreen().x,getLocationOnScreen().y-MyHacker.getPanel().getLocationOnScreen().y);
						
					}
				}
				else{
					JPopupMenu menu = new JPopupMenu();
					JMenuItem menuItem;
					
					menuItem = new JMenuItem("Unequip");
					menuItem.addActionListener(this);
					menu.add(menuItem);
					
					menuItem = new JMenuItem("Repair");
					menuItem.addActionListener(this);
					menu.add(menuItem);
					
					EPU.setVisible(false);
					menu.show(MyHacker.getPanel(),getLocationOnScreen().x-MyHacker.getPanel().getLocationOnScreen().x,getLocationOnScreen().y-MyHacker.getPanel().getLocationOnScreen().y);
				}
			}
		}
		else{
			JPopupMenu menu = new JPopupMenu();
			JMenuItem menuItem;
			
			menuItem = new JMenuItem("Purchase");
			menuItem.addActionListener(this);
			menu.add(menuItem);

			EPU.setVisible(false);
			menu.show(MyHacker.getPanel(),getLocationOnScreen().x-MyHacker.getPanel().getLocationOnScreen().x,getLocationOnScreen().y-MyHacker.getPanel().getLocationOnScreen().y);
		}
			
	}
	
	public void actionPerformed(ActionEvent e){
		
		View MyView = MyHacker.getView();
		
		if(e.getActionCommand().equals("Slot 1")){	
			MyHacker.setRequestedDirectory(Hacker.EQUIPMENT);
			Object[] params = new Object[]{MyHacker.getEncryptedIP(),1,fileName};
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.EQUIPMENT,"installequipment",params));
		}
		if(e.getActionCommand().equals("Slot 2")){
			MyHacker.setRequestedDirectory(Hacker.EQUIPMENT);
			Object[] params = new Object[]{MyHacker.getEncryptedIP(),2,fileName};
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.EQUIPMENT,"installequipment",params));
		}
		if(e.getActionCommand().equals("Purchase")){
			Object[] params = {ip};
			String result = (String)XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/domain.php","domainLookup", params);
			Object objects[] = {result,MyHacker.getEncryptedIP(),fileName,1};
			MyView.setFunction("requestpurchase");
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.BROWSER,"requestpurchase",objects));
			MyHacker.setRequestedDirectory(Hacker.BROWSER);	
		}
		if(e.getActionCommand().equals("Unequip")){
			Object[] params = new Object[]{MyHacker.getEncryptedIP(),equipped,null};
			MyHacker.setRequestedDirectory(Hacker.EQUIPMENT);
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.EQUIPMENT,"installequipment",params));
			//dispose();
			//setVisible(false);
			if(sePanel!=null){
				//sePanel.remove(this);
				sePanel.reset(this);
			}
			
		}
		if(e.getActionCommand().equals("Equip")){
			Object[] params = new Object[]{MyHacker.getEncryptedIP(),0,fileName};
			MyHacker.setRequestedDirectory(Hacker.EQUIPMENT);
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.EQUIPMENT,"installequipment",params));
		}
		if(e.getActionCommand().equals("Repair")){
			Object[] params = new Object[]{MyHacker.getEncryptedIP(),equipped,fileName};
			MyHacker.setRequestedDirectory(Hacker.EQUIPMENT);
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.EQUIPMENT,"repairequipment",params));
			
		}
		if (e.getActionCommand().equals("Delete")) {
            int n = showDeleteDialog(fileName);
			if(n==0){
				Object objects[] = {MyHacker.getEncryptedIP(),"",fileName};
				MyView.setFunction("deletefile");
				MyView.addFunctionCall(new RemoteFunctionCall(Hacker.EQUIPMENT,"deletefile",objects));
				MyHacker.setRequestedDirectory(Hacker.EQUIPMENT);
			}
		}
		
	}
    

    public int showOptionsDialog(String title, String message, Object[] options) {
        int n = JOptionPane.showOptionDialog(this, message, title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[1]);
        return(n);
    }
    
    public int showDeleteDialog(String message) {
        return (showYesNoDialog("Delete?", "Are you sure you want to delete " + message + "?"));
	}

    public int showYesNoDialog(String title, String message) {
        Object[] options = {"Yes", "No"};
        return (showOptionsDialog(title, message, options));
    }

}
