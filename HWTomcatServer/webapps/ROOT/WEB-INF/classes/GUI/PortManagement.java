package GUI;
/**

PortManagement.java
this is the port management window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.util.*;

public class PortManagement extends Application implements ItemListener, FocusListener{
	public final static int INSTALL=0;
	public final static int REPLACE=1;
	private final HashMap columnWidths = new HashMap();
	private JDesktopPane mainPanel=null;
	private Hacker MyHacker=null;
	private JTabbedPane tabbedPane;
	private JButton button[]= new JButton[32],dummyButtons[] = new JButton[32],defaultButtons[] = new JButton[32];
	private boolean on[] = new boolean[32],defaults[] = new boolean[32],dummy[] = new boolean[32];
	private JLabel type[] = new JLabel[32];
	private JLabel health[] = new JLabel[32],healCountLabel[] = new JLabel[32];
	private FireWallLabel firewall[] = new FireWallLabel[32];
	private JTextField cpuCost[] = new JTextField[32];
	private JTextField noteField[] = new JTextField[32];
    private String oldNotes[] = new String[32];
	private float[] healths = new float[32];
	private int[] healCount = new int[32];
	//private JCheckBox defaultBoxes[] = new JCheckBox[32];
	private PacketPort ports[];
	private JPanel panel1=new JPanel(),panel2=new JPanel(),panel3=new JPanel(),panel4=new JPanel();
	private View MyView;
	private int function,width;
	private boolean[] columns = {true,true,true,true,true,true,true,true,true,true};//{true,false,false,false,false,false,true,false,false};

	
	public PortManagement(String name,boolean resize,boolean max,boolean close,boolean iconify,JDesktopPane mainPanel,Hacker MyHacker){
		this.setTitle(name);
		this.setResizable(resize);
		this.setMaximizable(max);
		this.setClosable(close);
		this.setIconifiable(iconify);
		this.addInternalFrameListener(this);
		this.mainPanel=mainPanel;
		this.MyHacker=MyHacker;
		MyView = MyHacker.getView();
		String ip=MyHacker.getEncryptedIP();
		Object objects[] = {ip};
		MyView.setFunction("fetchports");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"fetchports",objects));
		this.setFrameIcon(ImageLoader.getImageIcon("images/port.png"));
		columns = MyHacker.getPortColumns();
		createMenu();
		setColumnWidths();
	}
	
	public void setColumnWidths(){
		columnWidths.put("Port",new Integer(40));
		columnWidths.put("OnOff",new Integer(60));
		columnWidths.put("Type",new Integer(63));
		columnWidths.put("Firewall",new Integer(155));
		columnWidths.put("CPUCost",new Integer(90));
		columnWidths.put("Note",new Integer(150));
		columnWidths.put("Health",new Integer(53));
		columnWidths.put("HealCount",new Integer(65));
		columnWidths.put("Default",new Integer(60));
		columnWidths.put("Dummy",new Integer(60));
		
	}

	public synchronized void setPorts(PacketPort ports[]){
		this.ports=ports;
		//resetFields();
		//populate();
        
		addData();
	}
	
    /**
    * 
    */
	public synchronized void addData(){
		PacketPort temp;
		int type,port;
		float cpucost,health,maxcpu;
		String note;
		HashMap firewall;
		boolean on,dummy;
		int defaults;
		for(int i=0; i < ports.length; i++){
			temp = ports[i];
			port=temp.getNumber();
			type=temp.getType();
			cpucost=temp.getCPUCost();
			firewall=temp.getFireWall();
			note=temp.getNote();
			on=temp.getOn();
			health=temp.getHealth();
			defaults = temp.getDefault();
			dummy = temp.getDummy();
			maxcpu = temp.getMaxCPUCost();
            
            // set if this is a default port
			if(defaults==0||defaults==-1){
				defaultButtons[port].setIcon(ImageLoader.getImageIcon("images/dummyoff.png"));
				this.defaults[port]=false;
			} else {
				this.defaults[port]=true;
				defaultButtons[port].setIcon(ImageLoader.getImageIcon("images/dummyon.png"));
			}
            
            // set if this port is on/off
			if(on){
				button[port].setIcon(ImageLoader.getImageIcon("images/on.png"));
				this.on[port]=true;
			} else {
				button[port].setIcon(ImageLoader.getImageIcon("images/off.png"));
				this.on[port]=false;                
            }
            
            // set if this is a dummy port
			if(dummy){
				this.dummy[port]=true;
				dummyButtons[port].setIcon(ImageLoader.getImageIcon("images/dummyon.png"));
			} else {
				this.dummy[port]=false;
				dummyButtons[port].setIcon(ImageLoader.getImageIcon("images/dummyoff.png"));
			}
			
            // set the type of the port
            switch (type) {
                case PacketPort.BANKING: this.type[port].setText("<html><u>" + Constants.applicationType[0] + "</u></html>"); break;
                case PacketPort.ATTACK: this.type[port].setText("<html><u>" + Constants.applicationType[1] + "</u></html>"); break;
                case PacketPort.SHIPPING: this.type[port].setText("<html><u>" + Constants.applicationType[2] + "</u></html>"); break;
                case PacketPort.FTP: this.type[port].setText("<html><u>" + Constants.applicationType[3] + "</u></html>"); break;
                case PacketPort.HTTP: this.type[port].setText("<html><u>" + Constants.applicationType[4] + "</u></html>"); break;
            }
			
			Insets panelInsets = panel1.getInsets();
			int bL=port;
			while(bL>7)
				bL-=8;
            
			Dimension d = button[port].getPreferredSize();
			bL=panelInsets.top+(bL*d.height)+25;
			Dimension labelSize = this.type[port].getPreferredSize();
			this.type[port].setBounds(this.type[port].getBounds().x,this.type[port].getBounds().y,labelSize.width,labelSize.height);
            
            // set the cpu cost of this port
			cpuCost[port].setEditable(true);
			cpuCost[port].setText(Constants.ONE_DIGIT.format(cpucost)+"/"+Constants.ONE_DIGIT.format(maxcpu));
			cpuCost[port].setEditable(false);
            
            // set the text of the firewall
			this.firewall[port].setText("<html><u>"+firewall.get("name")+"</u></html>");//Constants.firewalls[firewall]+"</u></html>");
            this.firewall[port].setType(type);
			this.firewall[port].setContent(firewall);
            //set the note
			noteField[port].setText(note);
            oldNotes[port] = note;
            
            // set the port health
			this.health[port].setText("<html><u>"+health+"</u></html>");
			healths[port]=health;
			
		}
	}
	
	public void gotHealth(Object[] data){
		//System.out.println("Receieved A Health Update in Port Management   "+data.length);
        
		for(int i=0;i<data.length;i++){
			Object[] port = (Object[])data[i];
			int portnum = (int)(Integer)port[0];
			float cpu = (float)(Float)port[2];
			float health = (float)(Float)port[1];
			HashMap fire = (HashMap)port[3];
			int healCount = (int)(Integer)port[4];
			float maxcpu = (float)(Float)port[5];
			if(cpuCost[portnum]!=null){
				cpuCost[portnum].setText(Constants.ONE_DIGIT.format(cpu)+"/"+Constants.ONE_DIGIT.format(maxcpu));
			}
			if(this.health[portnum]!=null){
				this.health[portnum].setText("<html><u>"+health+"</u></html>");
				if(health<95.0f) {
					this.health[portnum].setForeground(Color.red);
				} else {
					this.health[portnum].setForeground(Color.blue);
				}
			}
			if(firewall[portnum]!=null){
				firewall[portnum].setText("<html><u>"+fire.get("name")+"</u></html>");
				firewall[portnum].setContent(fire);
			}
			
			healths[portnum]=health;
			this.healCount[portnum] = healCount;
			
			if(healCountLabel[portnum]!=null){
				healCountLabel[portnum].setText(""+(10-healCount));
			}
		}
        
        // set the tab colour to red if any port health is less than 95
		if(tabbedPane!=null){
			int numberOfTabs = tabbedPane.getTabCount();
			boolean isBlue = true;
			for (int i = 0; i < numberOfTabs; i++) {
				for (int j = i*8; j < i*8+8; j++) {
					if (healths[j] < 95.0f) {
						isBlue = false;
						break;
					}
				}
				if (!isBlue) {
					tabbedPane.setForegroundAt(i, Color.red);
				} else {
					tabbedPane.setForegroundAt(i, Color.black);
				}
				isBlue = true;
			}
		}
	}
	
	public void setOn(){
		for(int i=0;i<32;i++){
			on[i]=false;
			button[i] = new JButton(ImageLoader.getImageIcon("images/off.png"));
		}
		
	}
	
	public void setNote(){
		for(int i=0;i<32;i++){
			noteField[i]=new JTextField(12);
		}
		
	}
	
	public void setType(){
		for(int i=0;i<32;i++){
			type[i]=new JLabel("");
		}
		
	}
	public void setFireWall(){
		for(int i=0;i<32;i++){
			firewall[i]=new FireWallLabel("",MyHacker);
		}
		
	}
	public void setCPUCost(){
		for(int i=0;i<32;i++){
			cpuCost[i]=new JTextField(12);
		}
		
	}
	
	public void setHealth(){
		for(int i=0;i<32;i++){
			health[i]=new JLabel("");
			healths[i]=100.0f;
		}
	}
	public void setDefaults(){
		for(int i=0;i<32;i++){
			defaultButtons[i]=new JButton(ImageLoader.getImageIcon("images/dummyoff.png"));
		}
	}
	
	public void setDummy(){
		for(int i=0;i<32;i++){
			dummy[i]=false;
			dummyButtons[i]=new JButton(ImageLoader.getImageIcon("images/dummyoff.png"));
		}
	}
	
	public void setHealCounts(){
		for(int i=0;i<32;i++){
			healCountLabel[i] = new JLabel("10");
			healCount[i] = 10;
		}
	}
	
	public void resetFields(){
		for(int i=0;i<32;i++){
			if(health[i]!=null)
				health[i].setText("");
				
		}
		for(int i=0;i<32;i++){
			if(cpuCost[i]!=null)
				cpuCost[i].setText("");
		}
		for(int i=0;i<32;i++){
			if(firewall[i]!=null)
				firewall[i].setText("");
		}
		for(int i=0;i<32;i++){
			if(type[i]!=null){
				type[i].setText("<html><u>Click Here To Install Script</u></html>");
				type[i].setBounds(this.type[i].getBounds().x,this.type[i].getBounds().y,type[i].getPreferredSize().width,type[i].getPreferredSize().height);
			}
		}
		for(int i=0;i<32;i++){
			if(noteField[i]!=null)
			noteField[i].setText("");
		}
		for(int i=0;i<32;i++){
			if(button[i]!=null)
				button[i].setIcon(ImageLoader.getImageIcon("images/off.png"));
			this.on[i]=false;
			this.defaults[i]=false;
			this.dummy[i]=false;
			if(defaultButtons[i]!=null){
				defaultButtons[i].setIcon(ImageLoader.getImageIcon("images/dummyoff.png"));
			}
			if(dummyButtons[i]!=null){
				dummyButtons[i].setIcon(ImageLoader.getImageIcon("images/dummyoff.png"));
			}
		}
		
	}
	
	public void createMenu(){
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu;
		JCheckBoxMenuItem menuItem;
		
		menu = new JMenu("Columns");
		
		menuItem = new JCheckBoxMenuItem("Port Number");
		menu.add(menuItem);
		menuItem.setSelected(columns[0]);
		menuItem.addItemListener(this);
		
		menuItem = new JCheckBoxMenuItem("On/Off");
		menu.add(menuItem);
		menuItem.setSelected(columns[1]);
		menuItem.addItemListener(this);
		
		menuItem = new JCheckBoxMenuItem("Type");
		menu.add(menuItem);
		menuItem.setSelected(columns[2]);
		menuItem.addItemListener(this);
		
		menuItem = new JCheckBoxMenuItem("Firewall");
		menu.add(menuItem);
		menuItem.setSelected(columns[3]);
		menuItem.addItemListener(this);
		
		menuItem = new JCheckBoxMenuItem("CPU Cost");
		menu.add(menuItem);
		menuItem.setSelected(columns[4]);
		menuItem.addItemListener(this);
		
		menuItem = new JCheckBoxMenuItem("Note");
		menu.add(menuItem);
		menuItem.setSelected(columns[5]);
		menuItem.addItemListener(this);
		
		menuItem = new JCheckBoxMenuItem("Health");
		menu.add(menuItem);
		menuItem.setSelected(columns[6]);
		menuItem.addItemListener(this);
		
		menuItem = new JCheckBoxMenuItem("Heals Left");
		menu.add(menuItem);
		menuItem.setSelected(columns[9]);
		menuItem.addItemListener(this);
		
		menuItem = new JCheckBoxMenuItem("Default");
		menu.add(menuItem);
		menuItem.setSelected(columns[7]);
		menuItem.addItemListener(this);
		
		menuItem = new JCheckBoxMenuItem("Dummy");
		menu.add(menuItem);
		menuItem.setSelected(columns[8]);
		menuItem.addItemListener(this);
		
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		
	}
	
	public synchronized void populate(){
		setType();
		setOn();
		setCPUCost();
		setFireWall();
		setNote();
		setHealth();
		setDefaults();
		setDummy();
		setHealCounts();
		//removeAll();
		try{
			remove(tabbedPane);
		}catch(Exception e){}
		Insets insets = this.getInsets();
		tabbedPane=new JTabbedPane();
		this.add(tabbedPane);
		
		panel1.setLayout(null);
		panel2.setLayout(null);
		panel3.setLayout(null);
		panel4.setLayout(null);
		//button[1] = new JButton(ImageLoader.getImageIcon("images/on.png"));
		Insets panelInsets = panel1.getInsets();
		Dimension d,labelSize;
		
		panel1 = makePanel(0);
		panel2 = makePanel(8);
		panel3 = makePanel(16);
		panel4 = makePanel(24);
		
		tabbedPane.addTab("0-7",panel1);
		if(MyHacker.getMemoryType()>0&&MyHacker.getMemoryType()<4)
			tabbedPane.addTab("8-15",panel2);
		if(MyHacker.getMemoryType()>1&&MyHacker.getMemoryType()<4)
			tabbedPane.addTab("16-23",panel3);
		if(MyHacker.getMemoryType()>2&&MyHacker.getMemoryType()<4)
			tabbedPane.addTab("24-31",panel4);
		tabbedPane.setBounds(insets.left+5,insets.top+5,width+10,this.getHeight()-80);
		Rectangle b = getBounds();
		setBounds(b.x,b.y,width+40,b.height);
		
	}
	
	public JPanel makePanel(int start){
		Dimension d,labelSize=new Dimension(0,0);
		JPanel panel1=new JPanel();
		panel1.setLayout(null);
		Insets panelInsets = panel1.getInsets();
		JLabel label;
		int x=panelInsets.left+2;
		if(columns[0]){
			label =new JLabel("Port");
			panel1.add(label);
			labelSize = label.getPreferredSize();
			label.setBounds(x,panelInsets.top+5,labelSize.width,labelSize.height);
			x+=(int)(Integer)columnWidths.get("Port");
		}
		
		if(columns[1]){
			label =new JLabel("On/Off");
			panel1.add(label);
			labelSize = label.getPreferredSize();
			label.setBounds(x,panelInsets.top+5,labelSize.width,labelSize.height);
			x+=(int)(Integer)columnWidths.get("OnOff");
		}
		if(columns[2]){
			label =new JLabel("Type");
			panel1.add(label);
			labelSize = label.getPreferredSize();
			label.setBounds(x,panelInsets.top+5,labelSize.width,labelSize.height);
			x+=(int)(Integer)columnWidths.get("Type");
		}
		
		if(columns[3]){
			label =new JLabel("Firewall");
			panel1.add(label);
			labelSize = label.getPreferredSize();
			label.setBounds(x,panelInsets.top+5,labelSize.width,labelSize.height);
			x+=(int)(Integer)columnWidths.get("Firewall");
		}
		
		if(columns[4]){
			label =new JLabel("CPU Cost");
			panel1.add(label);
			labelSize = label.getPreferredSize();
			label.setBounds(x,panelInsets.top+5,labelSize.width,labelSize.height);
			x+=(int)(Integer)columnWidths.get("CPUCost");
		}
		
		if(columns[5]){
			label =new JLabel("Note");
			panel1.add(label);
			labelSize = label.getPreferredSize();
			label.setBounds(x,panelInsets.top+5,labelSize.width,labelSize.height);
			x+=(int)(Integer)columnWidths.get("Note");
		}
		
		if(columns[6]){
			label =new JLabel("Health");
			panel1.add(label);
			labelSize = label.getPreferredSize();
			label.setBounds(x,panelInsets.top+5,labelSize.width,labelSize.height);
			x+=(int)(Integer)columnWidths.get("Health");
		}
		
		if(columns[9]){
			label =new JLabel("Heals Left");
			panel1.add(label);
			labelSize = label.getPreferredSize();
			label.setBounds(x,panelInsets.top+5,labelSize.width,labelSize.height);
			x+=(int)(Integer)columnWidths.get("HealCount");
		}
		
		if(columns[7]){
			label =new JLabel("Default");
			panel1.add(label);
			labelSize = label.getPreferredSize();
			label.setBounds(x,panelInsets.top+5,labelSize.width,labelSize.height);
			x+=(int)(Integer)columnWidths.get("Default");
		}
		
		if(columns[8]){
			label =new JLabel("Dummy");
			panel1.add(label);
			labelSize = label.getPreferredSize();
			label.setBounds(x,panelInsets.top+5,labelSize.width,labelSize.height);
			x+=(int)(Integer)columnWidths.get("Dummy");
		}
		
		JButton saveButton;
		int bL=0;
		Font f = new Font("Dialog",Font.PLAIN,12);
		x = panelInsets.left+5;
		for(int i=start;i<start+8;i++){
			x = panelInsets.left+5;
			button[i] = new JButton(ImageLoader.getImageIcon("images/off.png"));
			button[i].setBorderPainted(false);
			button[i].setContentAreaFilled(false);
			button[i].setOpaque(false);
			button[i].setToolTipText("Click here to turn port on/off");
			button[i].addActionListener(new PortManagementOnOffActionListener(i,this));
			d=button[i].getPreferredSize();
			bL=panelInsets.top+((i-start)*d.height)+25;
			if(columns[0]){
				label = new JLabel(String.valueOf(i));
				panel1.add(label);
				labelSize=label.getPreferredSize();
				label.setBounds(x+3,bL+8,labelSize.width,labelSize.height);
				x+=(int)(Integer)columnWidths.get("Port");
			}
			
			if(columns[1]){
				panel1.add(button[i]);	
				button[i].setBounds(x-8,bL,d.width,d.height);
				x+=(int)(Integer)columnWidths.get("OnOff");
			}
			type[i] = new JLabel("Bank");
			type[i].setFont(f);
			type[i].setForeground(Color.blue);
			//type[i].setEditable(false);
			//type[i].setBorder(null);
			if(columns[2]){
				panel1.add(type[i]);
				type[i].setText("<html><u>Click Here To Install Script</u></html>");
				labelSize=type[i].getPreferredSize();
				
				type[i].setToolTipText("Click for Options");
				type[i].setBounds(x,bL+8,labelSize.width,labelSize.height);
				type[i].addMouseListener(new PortManagementMouseListener(this,i,MyHacker,mainPanel,PortManagementMouseListener.NAME));
				x+=(int)(Integer)columnWidths.get("Type");
			}
            // set the length of the label to the longest possible firewall
			firewall[i] = new FireWallLabel("Click here to install a firewall",MyHacker);
			firewall[i].setFont(f);
			firewall[i].setForeground(Color.blue);
			//firewall[i].setEditable(false);
			//firewall[i].setBorder(null);
			if(columns[3]){
				panel1.add(firewall[i]);
				labelSize=firewall[i].getPreferredSize();
				firewall[i].setBounds(x,bL+8,labelSize.width,labelSize.height);
				x+=(int)(Integer)columnWidths.get("Firewall");
			}
				
			firewall[i].setText("");
			//firewall[i].setToolTipText("Click for Options");
			firewall[i].addMouseListener(new PortManagementMouseListener(this,i,MyHacker,mainPanel,PortManagementMouseListener.FIREWALL));
			cpuCost[i] = new JTextField("0.0",6);
			cpuCost[i].setBorder(null);
			cpuCost[i].setEditable(false);
			if(columns[4]){
				panel1.add(cpuCost[i]);
				labelSize=cpuCost[i].getPreferredSize();
				cpuCost[i].setBounds(x,bL+8,labelSize.width,labelSize.height);
				x+=(int)(Integer)columnWidths.get("CPUCost");
			}
			//saveButton = new JButton(ImageLoader.getImageIcon("images/save.png"));
			if(columns[5]){
				panel1.add(noteField[i]);
				noteField[i].setBounds(x,bL+8,noteField[i].getPreferredSize().width,noteField[i].getPreferredSize().height);
                noteField[i].addFocusListener(this);
				//panel1.add(saveButton);
				//saveButton.setBounds(x+136,bL+4,24,24);
				x+=(int)(Integer)columnWidths.get("Note");
			}
            
			health[i] = new JLabel("<html><u>100.0</u></html>");
			health[i].setFont(f);
			health[i].setToolTipText("Click to Heal Port");
			health[i].setForeground(Color.blue);
			if(columns[6]){
				panel1.add(health[i]);
				labelSize=health[i].getPreferredSize();
				health[i].setBounds(x,bL+8,labelSize.width,labelSize.height);
				health[i].addMouseListener(new PortManagementMouseListener(this,i,MyHacker,mainPanel,PortManagementMouseListener.HEALTH));
				x+=(int)(Integer)columnWidths.get("Health");
			}
			
			healCountLabel[i] = new JLabel("10");
			healCountLabel[i].setFont(f);
			healCountLabel[i].setToolTipText("Number of Heals Left");
			//health[i].setForeground(Color.blue);
			if(columns[9]){
				panel1.add(healCountLabel[i]);
				labelSize=healCountLabel[i].getPreferredSize();
				healCountLabel[i].setBounds(x+20,bL+8,labelSize.width,labelSize.height);
				//healCountLabel[i].addMouseListener(new PortManagementMouseListener(this,i,MyHacker,mainPanel,PortManagementMouseListener.HEALTH));
				x+=(int)(Integer)columnWidths.get("HealCount");
			}
			
			defaultButtons[i] = new JButton(ImageLoader.getImageIcon("images/dummyoff.png"));
			defaultButtons[i].setBorderPainted(false);
			defaultButtons[i].setContentAreaFilled(false);
			defaultButtons[i].setToolTipText("Click here to make this port the default");
			defaultButtons[i].addActionListener(new PortManagementDefaultListener(i,MyHacker,this));
			if(columns[7]){
				panel1.add(defaultButtons[i]);
				defaultButtons[i].setBounds(x-2,bL+8,defaultButtons[i].getPreferredSize().width,defaultButtons[i].getPreferredSize().height);
				x+=(int)(Integer)columnWidths.get("Default");
			}
			dummyButtons[i] = new JButton(ImageLoader.getImageIcon("images/dummyoff.png"));
			dummyButtons[i].setBorderPainted(false);
			dummyButtons[i].setContentAreaFilled(false);
			dummyButtons[i].setToolTipText("Click here to make this port a dummy port");
			dummyButtons[i].addActionListener(new PortManagementDummyListener(i,MyHacker,this));
			if(columns[8]){
				panel1.add(dummyButtons[i]);
				dummyButtons[i].setBounds(x-2,bL+8,dummyButtons[i].getPreferredSize().width,dummyButtons[i].getPreferredSize().height);
				x+=(int)(Integer)columnWidths.get("Dummy");
			}
		}
		width=x;
		return(panel1);
		
		
	}
    
    public void uninstallPort(int port) {
        button[port].setIcon(ImageLoader.getImageIcon("images/off.png"));
        type[port].setText("<html><u>Click Here To Install Script</u></html>");
        type[port].setBounds(type[port].getX(), type[port].getY(), type[port].getPreferredSize().width, type[port].getPreferredSize().height);
        firewall[port].setText("");
        cpuCost[port].setText("");
        noteField[port].setText("");
        oldNotes[port] = "";
        health[port] = new JLabel("<html><u>100.0</u></html>");
        healCountLabel[port].setText("10");
        defaultButtons[port].setIcon(ImageLoader.getImageIcon("images/dummyoff.png"));
        dummyButtons[port].setIcon(ImageLoader.getImageIcon("images/dummyoff.png"));
    }
    
    // 2 methods required to implement FocusListener
    public void focusGained(FocusEvent e) {
    }
    
    public void focusLost(FocusEvent e) {
        int index = -1;
        for (int i = 0; i < 32; i++) {
            if (noteField[i] == (JTextField)(e.getSource()) ) {
                index = i;
                break;
            }
        }
        
        // if the note has changed, save it
        if (index != -1 && noteField[index] != null && oldNotes[index] != null) {
            if (!oldNotes[index].equals(noteField[index].getText())) {
                saveNote(noteField[index].getText(), index);
            }
            // I think this should be in the if above -- how would it ever make a difference if the if (the notes aren't the same) doesn't apply?
            oldNotes[index] = noteField[index].getText();
        }
        
    }

	public JTextField getNote(int index){
		return(noteField[index]);
	}
	
	public JButton getButton(int index){
		return(button[index]);
	}
	
	public boolean getOn(int index){
		return(on[index]);
	}
	public JLabel getType(int index){
		return(type[index]);
	}
	
	public JLabel getHealth(int index){
		return(health[index]);
	}
	public float getHealthValue(int index){
		return(healths[index]);
	}
	public JLabel getFireWall(int index){
		return(firewall[index]);
	}
	
	public boolean getDummy(int index){
		return(dummy[index]);
	}
	
	public void setOnOff(int index,boolean set){
		Object objects[] = {MyHacker.getEncryptedIP(),index,set};
		MyView.setFunction("portonoff");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"portonoff",objects));
		on[index]=set;
	}
	
	public void saveNote(String note,int index){
        //System.out.println("SAVING THE NOTE: " + note + " at index = " + index);
		Object objects[] = {MyHacker.getEncryptedIP(),index,note};
		MyView.setFunction("saveportnote");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"saveportnote",objects));
	}
	
	public JPanel getPanel(int port){
		if(port>=0&&port<8)
			return(panel1);
		else if(port>=8&&port<16)
			return(panel2);
		else if(port>=16&&port<24)
			return(panel3);
		else if(port>=24&&port<32)
			return(panel4);
		else return(null);
	}
	
	public Hacker getHacker(){
		return(MyHacker);
	}
	
	public void internalFrameClosed(InternalFrameEvent e) {
		MyHacker.setPortManagementOpen(false);
	}
	
	public void install(int port, String folder, String name,int type){
		//System.out.println(folder+" "+name+" "+port+" "+type);
		Object objects[] = {MyHacker.getEncryptedIP(),port,folder,name};
		if(function==INSTALL){
			MyView.setFunction("installapplication");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"installapplication",objects));
            
            // add the name of the application that was just installed to the note field
            setNote(name, port);
            
		} else if(function==REPLACE) {
            // ask if they really want to replace this application
            String message = "Are You Sure?\n This will permanently remove the currently installed application from your computer.";
            String title = "Uninstall application on port " + port;
    		int n = showYesCancelDialog(title, message);
    		if(n==0){
    			MyView.setFunction("replaceapplication");
    			MyView.addFunctionCall(new RemoteFunctionCall(0,"replaceapplication",objects));
                
                // if the note field is blank replace the note, otherwise ask if they really want to replace the note
                if (noteField[port].getText().equals("")) {
                    setNote(name, port);
                } else {
                    String pref = MyHacker.getPreference(OptionPanel.APP_NOTE_KEY);
//System.out.println("SHOW REPLACE NOTE?: " + pref);
                    if (pref.equals("ask")) {
                        message = "Would you like to set the note of this port to be the name of the new application?";
                        title = "Replace note?";

                        Object[] checked = ConfirmationPanel.showYesNoDialog(MyHacker.getFrame(), title, message);
                        if ((boolean)(Boolean)checked[1])
                     //System.out.println("Yes / No?"  + checked[0]);
                       // System.out.println("Checked?: " + checked[1]);

                        if (n == OptionDialog.OPTION_YES) {
                            setNote(name, port);
                        }
                        if ((boolean)(Boolean)checked[1] == true) {
                            if (n == OptionDialog.OPTION_YES) {
                                MyHacker.setPreference(OptionPanel.APP_NOTE_KEY, "always");
                            } else {
                                MyHacker.setPreference(OptionPanel.APP_NOTE_KEY, "never");
                            }
                        }
                    } else if (pref.equals("always")) {
                        setNote(name, port);
                    } else {
                        // "never"
                    }
                    
                }
            }
		}
	}
	
    private void setNote(String name, int port) {
        noteField[port].setText(name);
        saveNote(name, port);
        oldNotes[port] = name;
    }
    
	public void installFireWall(int port,String folder,String name){
		Object objects[] = {MyHacker.getEncryptedIP(),port,folder,name};
		MyView.setFunction("installfirewall");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"installfirewall",objects));
	}
	
	public void setFunction(int function){
		this.function=function;
	}
	
    /**
    * Called when a column is shown/hidden from the menu.
    */
	public void itemStateChanged(ItemEvent e){
		JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)e.getSource();
		String button = menuItem.getText();
		
		if(button.equals("Port Number"))
			columns[0]=menuItem.isSelected();
		else if(button.equals("On/Off"))
			columns[1]=menuItem.isSelected();
		else if(button.equals("Type"))
			columns[2]=menuItem.isSelected();
		else if(button.equals("Firewall"))
			columns[3]=menuItem.isSelected();
		else if(button.equals("CPU Cost"))
			columns[4]=menuItem.isSelected();
		else if(button.equals("Note"))
			columns[5]=menuItem.isSelected();
		else if(button.equals("Health"))
			columns[6]=menuItem.isSelected();
		else if(button.equals("Default"))
			columns[7]=menuItem.isSelected();
		else if(button.equals("Dummy"))
			columns[8]=menuItem.isSelected();
		else if(button.equals("Heals Left")){
			columns[9] = menuItem.isSelected();
		}
		populate();		
		addData();
		MyHacker.setPortColumns(columns);
	}
}
