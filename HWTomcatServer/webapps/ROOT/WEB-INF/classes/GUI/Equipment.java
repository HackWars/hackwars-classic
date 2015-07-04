package GUI;
/**

AttackPane.java
this is the attack window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.lang.*;
import Game.*;
import java.text.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.util.*;
import net.miginfocom.swing.*;

public class Equipment extends Application implements KeyListener,TableModelListener {
	public static final int HEAL_RATE=0;
	public static final int DAMAGE_BONUS=1;
	public static final int WATCH_BONUS=2;
	public static final int HD_BONUS=3;
	public static final int BANKING_BONUS=4;
	public static final int HEAL_COST_BONUS=5;
	public static final int CPU_BONUS=6;
	public static final int FREEZE_IMMUNE=7;
	public static final int DESTROY_WATCH_IMMUNE=8;
	public static final int[] DUCT_TAPE_COST = new int[]{1,2,3,4,5,6,7,8,9,10};
	public static final int[] GERMANIUM_COST = new int[]{0,0,0,2,4,6,8,10,12,14};
	public static final int[] SILICON_COST = new int[]{0,0,0,0,0,4,8,12,16,20};
	public static final int[] YBCO_COST = new int[]{0,0,0,0,0,0,0,6,12,18};
	public static final int[] PLUTONIUM_COST = new int[]{0,0,0,0,0,0,0,0,8,16};
	
	public final static String[] VALUES = new String[]{"% Regular Heal Rate"," Attack Damage"," Watch(es)"," Files","% Regular Cut on Depositing/Transferring","% Heal Cost"," Regular CPU Points",
									" Redirecting Damage","% Chance of Freeze Immunity","% Chance of Destroy Watch Immunity"};
	public final static Object[] ACTUALVALUES = new Object[]{new int[]{-25,-25,25,25,25,50,50,50,50,75},
								new int[]{-2,-1,1,2,2,3,3,4,5,8},
								new int[]{-1,-1,1,1,1,2,2,2,2,3},
								new int[]{-5,-5,5,5,5,10,10,10,10,20},
								new int[]{1,1,-1,-1,-1,-1,-2,-2,-3,-4},
								new int[]{25,25,-5,-10,-10,-20,-20,-20,-30,-50},
								new int[]{-10,-10,5,5,5,10,10,10,10,20},
								new int[]{-2,-1,1,2,2,3,3,4,5,8},
								new int[]{100,100,100,100,100,100,100,100,100,100},
								new int[]{100,100,100,100,100,100,100,100,100,100}
								};	
	
	public static final String[] cpuNames={"Polonium 500MHz","Polonium 800Mhz","Polonium 1.4Ghz","Polonium 2.2Ghz","Polonium 3Ghz","Polonium Dual 2.4 Ghz","Polonium 600MHz"};
	public static final String[] cpuValues={"Run up to 50 CPU Points","Run up to 100 CPU Points","Run up to 150 CPU Points","Run up to 200 CPU Points","Run up to 250 CPU Points","Run up to 300 CPU Points","Run up to 75 CPU Points"};
	public static final String[] hdNames={"Ocean Gate 200MB","Ocean Gate 500MB","Ocean Gate 1GB","Ocean Gate 2GB","Ocean Gate 4GB","Ocean Gate 250MB"};
	public static final String[] hdValues={"Store up to 20 Files","Store up to 40 Files","Store up to 80 Files","Store up to 100 Files","Store up to 150 Files","Store up to 30 Files"};
	public static final String[] memNames={"Kansas 16MB","Kansas 64MB","Kansas 128MB","Kansas 256MB","Kansas 32MB"}; 
	public static final String[] memPortValues={"Use up to 8 Ports","Use up to 16 Ports","Use up to 24 Ports","Use up to 32 Ports","Use up to 8 Ports"};
	public static final String[] memWatchValues={"Run up to 4 Watches","Run up to 6 Watches","Run up to 8 Watches","Run up to 12 Watches","Run up to 5 Watches"};
	
	//COLUMNS
	public static final int FILE_NAME = 0;
	public static final int ATTRIBUTE1 = 1;
	public static final int ATTRIBUTE2 = 2;
	public static final int STORE_PRICE = 3;
	public static final int YOUR_STORE_PRICE = 4;
	public static final int DUCT_TAPE = 5;
	public static final int GERMANIUM = 6;
	public static final int SILICON = 7;
	public static final int YBCO = 8;
	public static final int PLUTONIUM = 9;
	public static final int CURRENT_DURABILITY = 10;
	public static final int MAX_DURABILITY = 11;
	public static final int NAME = 12;
	
	private Hacker MyHacker;
	private JPanel panel;
	private int x,y,mouseX,mouseY;
	private JButton b;
	private Container contentPane;
	private EquipmentPopUp EPU = new EquipmentPopUp();
	private InventoryList agplist,pcilist;
	private HashMap cardList;
	//private Inventory agp,pci1,pci2;
	private SingleEquipmentPanel agp,pci1,pci2,cpu,hd,memory;
	private int[] attributes = {0,0,0,0,0,0,0};
	public static Object[] columns = new Object[]{"File Name","Attribute 1","Attribute 2","Store Buy Price","Your Store Price","Duct Tape","Germanium","Silicon","YBCO","Plutonium","Durability","Max Durability","Name"};
	private JTable agpTable,pciTable;
	private SortableTableModel agpTableModel,pciTableModel;
	private JTabbedPane tableTabbedPane;
	/*private int healRate=0;
	private int damage=0;
	private int watches=0;
	private int files=0;
	private int cut=0;
	private int healCost=0;*/
	private boolean freeze=false;
	private boolean destroyWatches=false;
    
    private JPopupMenu agpEquipmentMenu = new JPopupMenu();
    private JPopupMenu pciEquipmentMenu = new JPopupMenu();

	public Equipment(Hacker MyHacker){
		setTitle("Equipment");
		setResizable(true);
		setMaximizable(true);
		setClosable(true);
		setIconifiable(true);
		//this.addInternalFrameListener(this);
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		this.MyHacker=MyHacker;
		MyHacker.getPanel().add(this);
		//setBounds(50,50,285,415);
		addInternalFrameListener(this);
		setLayout(new MigLayout("wrap 6, align leading,fill,h 100:n:n"));
		populate();
		setVisible(true);
		moveToFront();
		View MyView = MyHacker.getView();
		Object[] params = new Object[]{MyHacker.getEncryptedIP()};
		MyHacker.setRequestedDirectory(Hacker.EQUIPMENT);
		MyView.addFunctionCall(new RemoteFunctionCall(Hacker.EQUIPMENT,"requestequipment",params));
		this.setFrameIcon(ImageLoader.getImageIcon("images/cpu.png"));
	}

	public void populate(){
		setBackground(Color.black);
		agp = new SingleEquipmentPanel("AGP CARD",MyHacker,EPU);
		add(agp,"growx,shrink 0,h 100:n:n");
		pci1 = new SingleEquipmentPanel("PCI CARD SLOT 1",MyHacker,EPU);
		add(pci1,"growx,shrink 0,h 100:n:n");
		pci2 = new SingleEquipmentPanel("PCI CARD SLOT 2",MyHacker,EPU);
		add(pci2,"growx,shrink 0,h 100:n:n");
		cpu = new SingleEquipmentPanel("CPU",MyHacker,EPU);
		add(cpu,"growx,shrink 0,h 100:n:n");
		hd = new SingleEquipmentPanel("Hard Drive",MyHacker,EPU);
		add(hd,"growx,shrink 0,h 100:n:n");
		memory = new SingleEquipmentPanel("Memory",MyHacker,EPU);
		add(memory,"growx,shrink 0,h 100:n:n");
		
		JToolBar toolbar = new JToolBar();
		
		JButton button = new JButton(ImageLoader.getImageIcon("images/close.png"));
		button.addActionListener(this);
		button.setActionCommand("Delete");
		button.setToolTipText("delete selected card.");
		toolbar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/calc.png"));
		button.addActionListener(this);
		button.setActionCommand("Sell");
		button.setToolTipText("Sell Card to Store.");
		toolbar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/equip16.png"));
		button.addActionListener(this);
		button.setActionCommand("Equip");
		button.setToolTipText("Equip Card.");
		toolbar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/repair16.png"));
		button.addActionListener(this);
		button.setActionCommand("Repair");
		button.setToolTipText("Repair Card.");
		toolbar.add(button);
		
		add(toolbar,"span,wrap,growx,shrink 0");
		tableTabbedPane = new JTabbedPane();
		tableTabbedPane.setPreferredSize(new Dimension(450,300));
		Object[][] data = {};
		

        // PCI popup menu
        JMenu slotMenu = new JMenu("Equip");
        JMenuItem menuItem = new JMenuItem("Slot 1");
        menuItem.addActionListener(this);
        slotMenu.add(menuItem);
        menuItem = new JMenuItem("Slot 2");
        menuItem.addActionListener(this);
        slotMenu.add(menuItem);
        pciEquipmentMenu.add(slotMenu);
        menuItem = new JMenuItem("Repair");
        menuItem.addActionListener(this);
        pciEquipmentMenu.add(menuItem);
        menuItem = new JMenuItem("Sell");
        menuItem.addActionListener(this);
        pciEquipmentMenu.add(menuItem);
        pciEquipmentMenu.addSeparator();
        menuItem = new JMenuItem("Delete");
        menuItem.addActionListener(this);
        pciEquipmentMenu.add(menuItem);
        
        // AGP popup menu
        menuItem = new JMenuItem("Equip");
        menuItem.addActionListener(this);
        agpEquipmentMenu.add(menuItem);
        menuItem = new JMenuItem("Repair");
        menuItem.addActionListener(this);
        agpEquipmentMenu.add(menuItem);
        menuItem = new JMenuItem("Sell");
        menuItem.addActionListener(this);
        agpEquipmentMenu.add(menuItem);
        agpEquipmentMenu.addSeparator();
        menuItem = new JMenuItem("Delete");
        menuItem.addActionListener(this);
        agpEquipmentMenu.add(menuItem);
        
		agpTableModel = new SortableTableModel(SortableTableModel.EQUIPMENT);
		agpTableModel.setDataVector(data,columns);
		agpTable = new JTable(agpTableModel);
		agpTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		agpTable.setAutoCreateColumnsFromModel(false);
		agpTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        agpTable.addKeyListener(this);
        agpTable.addMouseListener(this);
		agpTableModel.addTableModelListener(this);
		SortButtonRenderer renderer = new SortButtonRenderer();
		JTableHeader header = agpTable.getTableHeader();
		header.addMouseListener(new HeaderListener(header,renderer,agpTable));
		int n = columns.length;
		int totalWidth = 0;
		TableColumnModel model = agpTable.getColumnModel();
	
		int[] columnWidth = new int[]{174,174,174,110,110,60,60,60,60,60,80,115,350};
		for (int i=0;i<n;i++) {
		  model.getColumn(i).setHeaderRenderer(renderer);
		  model.getColumn(i).setPreferredWidth(columnWidth[i]);
		  //totalWidth+=columnWidth[i];
		}
		
		pciTableModel = new SortableTableModel(SortableTableModel.EQUIPMENT);
		pciTableModel.setDataVector(data,columns);
		pciTable = new JTable(pciTableModel);
		pciTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pciTable.setAutoCreateColumnsFromModel(false);
		pciTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        pciTable.addKeyListener(this);
        pciTable.addMouseListener(this);
		pciTableModel.addTableModelListener(this);
		renderer = new SortButtonRenderer();
		header = pciTable.getTableHeader();
		header.addMouseListener(new HeaderListener(header,renderer,pciTable));
		n = columns.length;
		model = pciTable.getColumnModel();
		for (int i=0;i<n;i++) {
		  model.getColumn(i).setHeaderRenderer(renderer);
		  model.getColumn(i).setPreferredWidth(columnWidth[i]);
		  //totalWidth+=columnWidth[i];
		}
		JScrollPane sp = new JScrollPane(agpTable);
		tableTabbedPane.addTab("AGP",sp);
		sp = new JScrollPane(pciTable);
		tableTabbedPane.addTab("PCI",sp);
		add(tableTabbedPane,"span,grow,shrink 2000");
		
		pack();
		setMinimumSize(new Dimension(getWidth(),140));
		MyHacker.getPanel().add(EPU);
		MyHacker.getPanel().setComponentZOrder(EPU,0);
		EPU.setVisible(false);
		EPU.setOpaque(false);
	}
    
    // called whenever inventory is received from the server
	public void receivedInventory(Object[] inventory){
		cardList = new HashMap();
		agpTableModel.resetData();
		pciTableModel.resetData();
		for(int i=1;i<inventory.length;i++){
			if(inventory[i]!=null){
				//System.out.println(i+": Equipment");
				if(inventory[i] instanceof HackerFile){
					HackerFile HF = (HackerFile)inventory[i];
					Object rowData[] = new Object[columns.length];
					HashMap content = HF.getContent();
					int a1 = Integer.valueOf((String)content.get("attribute0"));
					int a2 = Integer.valueOf((String)content.get("attribute1"));
					int quality1 = Integer.valueOf((String)content.get("quality0"));
					int quality2 = Integer.valueOf((String)content.get("quality1"));
					
					float durability=50.0f;
					float max=50.0f; 
					try{//In case it's hardware that hasn't had a durability set for some reason, e.g., old hardware.
						durability = Float.valueOf((String)content.get("currentquality"));
						max = Float.valueOf((String)content.get("maxquality"));
					}catch(Exception e){}
					
					String d = durability+"/"+max;
					String values = (String)content.get("bonusdata");
					String[] v = values.split("\\|");
					String value1 = v[0];
					String value2 = v[1];
                    
                    AttributeCellObject attribute1 = new AttributeCellObject(quality1, a1, value1);
                    AttributeCellObject attribute2 = new AttributeCellObject(quality2, a2, value2);
                    
					int dT = DUCT_TAPE_COST[quality1]+DUCT_TAPE_COST[quality2];
					int ge = GERMANIUM_COST[quality1]+GERMANIUM_COST[quality2];
					int si = SILICON_COST[quality1]+SILICON_COST[quality2];
					int ybco = YBCO_COST[quality1]+YBCO_COST[quality2];
					int pu = PLUTONIUM_COST[quality1]+PLUTONIUM_COST[quality2];
					rowData[FILE_NAME] = HF.getName();					
					rowData[ATTRIBUTE1] = attribute1;
					rowData[ATTRIBUTE2] = attribute2;
					rowData[DUCT_TAPE] = dT;
					rowData[GERMANIUM] = ge;
					rowData[SILICON] = si;
					rowData[YBCO] = ybco;
					rowData[PLUTONIUM] = pu;
					rowData[CURRENT_DURABILITY] = durability;
					rowData[MAX_DURABILITY] = max;
					rowData[NAME] = HF.getDescription();
					float price = Home.getPrice(HF.getMaker());
					rowData[STORE_PRICE] = price;
					rowData[YOUR_STORE_PRICE] = HF.getPrice();
					cardList.put(HF.getName(),HF);
					if(HF.getType()==HackerFile.AGP){
						agpTableModel.addRow(rowData);
					}
					else if(HF.getType()==HackerFile.PCI){
						pciTableModel.addRow(rowData);
					}
				}
			}
		}
	}
	
	public static Inventory getInventoryObject(HackerFile HF,int equipped,Hacker MyHacker,EquipmentPopUp EPU){
		String name = HF.getDescription();
		HashMap content = HF.getContent();
		int a1 = Integer.valueOf((String)content.get("attribute0"));
		int a2 = Integer.valueOf((String)content.get("attribute1"));
		int quality1 = Integer.valueOf((String)content.get("quality0"));
		int quality2 = Integer.valueOf((String)content.get("quality1"));
		
		float durability=50.0f;
		float max=50.0f; 
		try{//In case it's hardware that hasn't had a durability set for some reason, e.g., old hardware.
			durability = Float.valueOf((String)content.get("currentquality"));
			max = Float.valueOf((String)content.get("maxquality"));
		}catch(Exception e){}
		
		String d = "Durability: "+durability+"/"+max;
		float cut = durability/max;
		int att1 = ((int[])ACTUALVALUES[a1])[quality1];
		att1*=cut;
		int att2 = ((int[])ACTUALVALUES[a2])[quality2];
		att2*=cut;
		//String value1 = ((String[])VALUES[a1])[quality1];
		//String value2 = ((String[])VALUES[a2])[quality2];
		String value1 = att1+VALUES[a1];
		String value2 = att2+VALUES[a2];
		String values = (String)content.get("bonusdata");
		String[] v = values.split("\\|");
		value1 = v[0];
		value2 = v[1];
		Color type=Inventory.VALUE;
		if(quality1+quality2==18)
			type=Inventory.ALIEN;
		else if(quality1+quality2>=14)
			type=Inventory.EXPERIMENTAL;
		else if(quality1+quality2>=9)
			type=Inventory.PREMIUM;
		else if(quality1+quality2>5)
			type=Inventory.CONSUMER;
		int total = quality1+quality2;
		int dT = DUCT_TAPE_COST[quality1]+DUCT_TAPE_COST[quality2];
		int ge = GERMANIUM_COST[quality1]+GERMANIUM_COST[quality2];
		int si = SILICON_COST[quality1]+SILICON_COST[quality2];
		int ybco = YBCO_COST[quality1]+YBCO_COST[quality2];
		int pu = PLUTONIUM_COST[quality1]+PLUTONIUM_COST[quality2];
		int cost[] = new int[]{dT,ge,si,ybco,pu};
		/*String cost = "It will cost "+dT+"x DT";
		if(ge>0)
			cost+=","+ge+"x GE";
		if(si>0)
			cost+=","+si+"x SI";
		if(ybco>0)
			cost+=","+ybco+"x YBCO";
		if(pu>0)
			cost+=","+pu+"x PU";
		cost+= " to fix.";*/
		int cardType=0;
		if(HF.getType()==HackerFile.AGP)
			cardType = Inventory.AGP;
		else
			cardType = Inventory.PCI;
		Inventory agp = new Inventory(MyHacker,EPU,name,value1,value2,type,cardType,HF.getName(),equipped,d,cost);
		return(agp);
	}
	
    // the stuff that you have equipped already
	public void receivedEquipment(Object[] equip){
		attributes = new int[]{0,0,0,0,0,0,0};
		freeze=false;
		destroyWatches=false;
		if(equip[1]!=null){
			if(equip[1] instanceof HackerFile){
				HackerFile HF = (HackerFile)equip[1];
				Inventory agp = getInventoryObject(HF,0,MyHacker,EPU);
				/*panel.add(agp);
				agp.setBounds(40,30,47,42);*/
				this.agp.setInventory(agp);
			}
		}
		if(equip[2]!=null){
			if(equip[2] instanceof HackerFile){
				HackerFile HF = (HackerFile)equip[2];
				Inventory pci1 = getInventoryObject(HF,1,MyHacker,EPU);
				this.pci1.setInventory(pci1);
				/*panel.add(pci1);
				pci1.setBounds(40,100,47,42);*/
			}
		}
		if(equip[3]!=null){
			if(equip[3] instanceof HackerFile){
				HackerFile HF = (HackerFile)equip[3];
				
				Inventory pci2 = getInventoryObject(HF,2,MyHacker,EPU);
				this.pci2.setInventory(pci2);
				/*panel.add(pci2);
				pci2.setBounds(40,170,47,42);*/
			}
		}
		/*System.out.println("Total Damage Bonus: "+attributes[DAMAGE_BONUS]);
		System.out.println("Heal Rate Bonus: "+attributes[HEAL_RATE]);
		System.out.println("Total Watch Bonus: "+attributes[WATCH_BONUS]);
		System.out.println("Total HD Bonus: "+attributes[HD_BONUS]);
		System.out.println("Total Banking Bonus: "+attributes[BANKING_BONUS]);
		System.out.println("Total CPU Bonus: "+attributes[CPU_BONUS]);
		System.out.println("Total Heal Cost Bonus: "+attributes[HEAL_COST_BONUS]);
		System.out.println("Freeze Immunity: "+freeze);
		System.out.println("Destroy Watches Immunity: "+destroyWatches);*/
		
	}
	
	public void changePrice(String fileName,float price){
		View MyView = MyHacker.getView();
		Object objects[] = {MyHacker.getEncryptedIP(),"",fileName,new Float(price)};
		MyView.setFunction("setfileprice");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"setfileprice",objects));
	}
	
	public JTable getSelectedTable(){
		String tab = tableTabbedPane.getTitleAt(tableTabbedPane.getSelectedIndex());
		if(tab.equals("AGP")) {
			return(agpTable);
		}
		else if(tab.equals("PCI")) {
			return(pciTable);
		}
		return null;
	}
	
	public SortableTableModel getSelectedModel(){
		String tab = tableTabbedPane.getTitleAt(tableTabbedPane.getSelectedIndex());
		if(tab.equals("AGP")){
			return(agpTableModel);
		}
		else if(tab.equals("PCI")){
			return(pciTableModel);
		}
	
		return null;
	}
	
	
	public void actionPerformed(ActionEvent e){
		String tab = tableTabbedPane.getTitleAt(tableTabbedPane.getSelectedIndex());
		JTable table = getSelectedTable();
		SortableTableModel model = getSelectedModel();
		View MyView = MyHacker.getView();
		
		int row = table.getSelectedRow();
		if(row!=-1){
			String name = (String)model.getValueAt(row,FILE_NAME);
			HackerFile HF = (HackerFile)cardList.get(name);
			if(HF!=null){
				String ac = e.getActionCommand();
				if(ac.equals("Equip")){
					//System.out.println("Retrieved file "+HF.getName()+" "+HF.getDescription());
					if(tab.equals("AGP")){
						//System.out.println("Equipping "+HF.getName());
						Object[] params = new Object[]{MyHacker.getEncryptedIP(),0,HF.getName()};
						MyHacker.setRequestedDirectory(Hacker.EQUIPMENT);
						MyView.addFunctionCall(new RemoteFunctionCall(Hacker.EQUIPMENT,"installequipment",params));
						model.removeRow(row);
					}
					else if(tab.equals("PCI")){
						JPopupMenu menu = new JPopupMenu();
						JMenuItem menuItem;
						
						menuItem = new JMenuItem("Slot 1");
						menuItem.addActionListener(this);
						menu.add(menuItem);
						
						menuItem = new JMenuItem("Slot 2");
						menuItem.addActionListener(this);
						menu.add(menuItem);
                        
                        if (e.getSource() instanceof JButton) {
    						JButton button = (JButton)e.getSource();
    						menu.show(button,button.getX(),button.getY());
                        } else if (e.getSource() instanceof JMenuItem) {
                            
                            JMenuItem mi = (JMenuItem)e.getSource();
                            menu.show(mi, mi.getX(), mi.getY());
                        }
					}
				}
				else if(ac.equals("Slot 1")){
					Object[] params = new Object[]{MyHacker.getEncryptedIP(),1,HF.getName()};
					MyHacker.setRequestedDirectory(Hacker.EQUIPMENT);
					MyView.addFunctionCall(new RemoteFunctionCall(Hacker.EQUIPMENT,"installequipment",params));
					model.removeRow(row);
				
				}
				else if(ac.equals("Slot 2")){
					Object[] params = new Object[]{MyHacker.getEncryptedIP(),2,HF.getName()};
					MyHacker.setRequestedDirectory(Hacker.EQUIPMENT);
					MyView.addFunctionCall(new RemoteFunctionCall(Hacker.EQUIPMENT,"installequipment",params));
					model.removeRow(row);
				}
				else if(ac.equals("Repair")){
					Object[] params = new Object[]{MyHacker.getEncryptedIP(),-1,HF.getName()};
					MyHacker.setRequestedDirectory(Hacker.EQUIPMENT);
					MyView.addFunctionCall(new RemoteFunctionCall(Hacker.EQUIPMENT,"repairequipment",params));
				}
				else if(ac.equals("Delete")){
                    deleteFile();
				}
				else if(ac.equals("Sell")){
					
					boolean ok = false;
					PacketPort[] ports = MyHacker.getPorts();
					if (ports != null) {
						for (int i = 0; i < ports.length; i++) {
							if (ports[i].getType() == Port.BANKING && (ports[i].getOn() == true) && (ports[i].getDummy() == false)) {
								ok = true;
								break;
							}
						}
					}
					
					if(ok){
						int n = showYesNoDialog("Sell Card","Are you sure you want to sell "+HF.getName()+" for "+NumberFormat.getCurrencyInstance().format(Home.getPrice(HF.getMaker()))+"?");
						if(n == 0){
							Object[] allFiles = new Object[1];
							Object[] details = new Object[4];
							details[0] = "";
							details[1] = HF.getName();
							details[2] = HF.getMaker();
							details[3] = 1;
							allFiles[0] = details;
							Object objects[] = new Object[]{MyHacker.getEncryptedIP(),allFiles};
							MyHacker.setRequestedDirectory(Hacker.HOME);
							MyView.setFunction("sellfilemulti");
							MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HOME,"sellfilemulti",objects));
							model.removeRow(row);
						}
					}
					
				
				
				}
			}
		}
	}
	
		class HeaderListener extends MouseAdapter {
    
        JTableHeader   header;
        SortButtonRenderer renderer;
		JTable table;
        HeaderListener(JTableHeader header,SortButtonRenderer renderer,JTable table) {
          this.header   = header;
          this.renderer = renderer;
		  this.table = table;
        }
      
        public void mousePressed(MouseEvent e) {
            if ( (header.getCursor().getType() != Cursor.E_RESIZE_CURSOR) && (header.getCursor().getType() != Cursor.W_RESIZE_CURSOR) ) {
              int col = header.columnAtPoint(e.getPoint());
              int sortCol = header.getTable().convertColumnIndexToModel(col);
			  if(sortCol!=-1){
				  TableColumn column = table.getColumnModel().getColumn(sortCol);
				  //int width = 
				  renderer.setPressedColumn(col);
				  renderer.setSelectedColumn(col);
				  header.repaint();
				  
				  if (header.getTable().isEditing()) {
					header.getTable().getCellEditor().stopCellEditing();
				  }
				  
				  boolean isAscent;
				  if (SortButtonRenderer.DOWN == renderer.getState(col)) {
					isAscent = true;
				  } else {
					isAscent = false;
				  }
				  ((SortableTableModel)header.getTable().getModel())
					.sortByColumn(sortCol, isAscent);    
				}
			}
        }
      
        public void mouseReleased(MouseEvent e) {
            if ( (header.getCursor().getType() != Cursor.E_RESIZE_CURSOR) && (header.getCursor().getType() != Cursor.W_RESIZE_CURSOR) ) {
              int col = header.columnAtPoint(e.getPoint());
              renderer.setPressedColumn(-1);                // clear
              header.repaint();
            }
        }
  }
  
  /* Implements KeyListener */
  public void keyPressed(KeyEvent e) {
  }
  
  public void keyReleased(KeyEvent e) {
  }
  
  
  public void keyTyped(KeyEvent e) {
    // if the delete key is pressed, delete the file.
    if (e.getKeyChar() == KeyEvent.VK_DELETE) {
        deleteFile();
    }
  }
  
  private void deleteFile() {
    JTable table = getSelectedTable();
    SortableTableModel model = getSelectedModel();
    View MyView = MyHacker.getView();
    int row = table.getSelectedRow();
    if (row == -1) {
        return;
    }
    
    String name = (String)model.getValueAt(row,FILE_NAME);
    HackerFile HF = (HackerFile)cardList.get(name);
    int n = showDeleteDialog(HF.getName());
    if(n==0){
        Object objects[] = {MyHacker.getEncryptedIP(),"",HF.getName()};
        MyView.setFunction("deletefile");
        MyView.addFunctionCall(new RemoteFunctionCall(Hacker.EQUIPMENT,"deletefile",objects));
        MyHacker.setRequestedDirectory(Hacker.EQUIPMENT);
        model.removeRow(row);
    }
  }
  
  public void tableChanged(TableModelEvent e){
        JTable table = getSelectedTable();
        SortableTableModel tableModel = getSelectedModel();
        int row = table.getSelectedRow();//e.getFirstRow();
        int column = e.getColumn();
        if(row!=-1&&column!=-1){
            String columnName = tableModel.getColumnName(column);
            Object data = tableModel.getValueAt(row, column);
            String fileName = (String)tableModel.getValueAt(row,FILE_NAME);
            if(columnName.equals(columns[YOUR_STORE_PRICE])){
                //changing price
                float price = (float)(Float)data;
                changePrice(fileName,price);
            }
        }
    }

    private void doMouseAction(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JTable table = getSelectedTable();
            
			// get the coordinates of the mouse click
			Point p = e.getPoint();
 
			// get the row index that contains that coordinate
			int rowNumber = table.rowAtPoint( p );
 
			// Get the ListSelectionModel of the JTable
			ListSelectionModel model = table.getSelectionModel();
 
			// set the selected interval of rows. Using the "rowNumber"
			// variable for the beginning and end selects only that one row.
			model.setSelectionInterval( rowNumber, rowNumber );

            if (table == agpTable) {
                agpEquipmentMenu.show(table, e.getX(), e.getY());
            } else {
                pciEquipmentMenu.show(table, e.getX(), e.getY());
            }
        }
    }
    
    public void mousePressed(MouseEvent e) {
        doMouseAction(e);
    }
    
    public void mouseReleased(MouseEvent e) {
        doMouseAction(e);
    }
    
}

