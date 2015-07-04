package GUI;
/**

Home.java
Represents the file browser.
*/
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Game.*;
import View.*;
import Assignments.*;
import java.text.*;
import java.util.*;
import java.net.URL;

public class Home extends Application implements ComponentListener,TableModelListener, KeyListener {
	
    private final static HashMap makers = new HashMap();
    static {
        makers.put("Alexander", 15.0f);
        makers.put("Low", 30.0f);
        makers.put("Medium", 150.0f);
        makers.put("High", 1500.0f);
        makers.put("Rare", 15000.0f);
        makers.put("Holiday", 250.0f);
        makers.put("I", 15.0f);
        makers.put("II", 23.0f);
        makers.put("III", 36.0f);
        makers.put("IV", 56.0f);
        makers.put("V", 87.0f);
        makers.put("VI", 134.0f);
        makers.put("VII", 208.0f);
        makers.put("VIII", 322.0f);
        makers.put("IX", 500.0f);
        makers.put("X", 775.0f);
        makers.put("XI", 1201.0f);
        makers.put("XII", 1861.0f);
        makers.put("XIII", 2885.0f);
        makers.put("XIV", 4471.0f);
        makers.put("XV", 6930.0f);
        makers.put("XVI", 10742.0f);
        makers.put("XVII", 16649.0f);
        makers.put("XVIII", 25807.0f);
        makers.put("XIX", 40000.0f);
        makers.put("XX", 62000.0f);
        makers.put("Trash.I", 15.0f);
        makers.put("Trash.II", 17.0f);
        makers.put("Trash.III", 19.0f);
        makers.put("Trash.IV", 21.0f);
        makers.put("Trash.V", 24.0f);
        makers.put("Trash.VI", 27.0f);
        makers.put("Trash.VII", 30.0f);
        makers.put("Trash.VIII", 34.0f);
        makers.put("Trash.IX", 38.0f);
        makers.put("Trash.X", 43.0f);
        makers.put("Trash.XI", 49.0f);
        makers.put("Trash.XII", 55.0f);
        makers.put("Trash.XIII", 62.0f);
        makers.put("Trash.XIV", 69.0f);
        makers.put("Trash.XV", 78.0f);
        makers.put("Trash.XVI", 88.0f);
        makers.put("Trash.XVII", 99.0f);
        makers.put("Trash.XVIII", 111.0f);
        makers.put("Trash.XIX", 125.0f);
        makers.put("Trash.XX", 141.0f);
        makers.put("Trash.XXI", 158.0f);
        makers.put("Trash.XXII", 178.0f);
        makers.put("Trash.XXIII", 200.0f);
        makers.put("Trash.XXIV", 225.0f);
        makers.put("Trash.XXV", 253.0f);
        makers.put("Trash.XXVI", 285.0f);
        makers.put("Trash.XXVII", 321.0f);
        makers.put("Trash.XXVIII", 361.0f);
        makers.put("Trash.XXIX", 406.0f);
        makers.put("Trash.XXX", 457.0f);
        makers.put("Trash.XXXI", 514.0f);
        makers.put("Trash.XXXII", 578.0f);
        makers.put("Trash.XXXIII", 650.0f);
        makers.put("Trash.XXXIV", 731.0f);
        makers.put("Trash.XXXV", 823.0f);
        makers.put("Trash.XXXVI", 926.0f);
        makers.put("Trash.XXXVII", 1041.0f);
        makers.put("Trash.XXXVIII", 1171.0f);
        makers.put("Trash.XXXIX", 1318.0f);
        makers.put("Trash.XL", 1483.0f);
        makers.put("Token.Silverlight", 500.0f);
        makers.put("Token.Draconis", 250.0f);
        makers.put("Xyphex.I", 15.0f);
        makers.put("Xyphex.II", 23.0f);
        makers.put("Xyphex.III", 36.0f);
        makers.put("Xyphex.IV", 56.0f);
        makers.put("Xyphex.V", 87.0f);
        makers.put("Xyphex.VI", 134.0f);
        makers.put("Xyphex.VII", 208.0f);
        makers.put("Xyphex.VIII", 322.0f);
        makers.put("Xyphex.IX", 500.0f);
        makers.put("Xyphex.X", 775.0f);
        makers.put("Xyphex.XI", 1201.0f);
        makers.put("Xyphex.XII", 1861.0f);
        makers.put("Xyphex.XIII", 2885.0f);
        makers.put("Xyphex.XIV", 4471.0f);
        makers.put("Xyphex.XV", 6930.0f);
        makers.put("Xyphex.XVI", 10742.0f);
        makers.put("Xyphex.XVII", 16649.0f);
        makers.put("Xyphex.XVIII", 25807.0f);
        makers.put("Xyphex.XIX", 40000.0f);
        makers.put("Xyphex.XX", 62000.0f);
    }

	//indexes in array
	public static final int NAME = 0;
	public static final int TYPE = 1;
	public static final int QUANTITY = 2;
	public static final int MAKER = 3;
    public static final int SELL_TO_STORE_PRICE = 4;
	public static final int YOUR_STORE_PRICE = 5;
	public static final int CPU_COST = 6;
	public static final int DESCRIPTION = 7;
	
	//table columns
	public static final String[] columns = {"Name","Type","Quantity","Maker","Store Buy Price","Your Price", "CPU Cost","Description"};

	public static final int TABLE=0;
	public static final int ICONS=1;
	public static final String TYPES[]={"Banking","Banking Script","Attack","Attack Script","Watch","Watch Script","FTP","FTP Script","FireWall","Text","CPU","HD","HTTP","Memory","Image","HTTP Script","Secret Document","Bounty","AGP Card License","PCI Card License","Game Project","Game","Game","Redirect","Redirect Script","Quest","Commodity","Challenge","Firewall","Trash"};
	private final int HDMAX[]={15,30,60,90,120,20};
	private Hacker MyHacker;
	private JDesktopPane mainPanel;
	private Object[] directory,shownDirectory;
	private JToolBar toolBar;
	private SpringLayout layout;
	private JPanel properties,iconView,tableView;
	private JLabel name=new JLabel("-"),type=new JLabel("-"),maker=new JLabel("-"),price=new JLabel("-"),cpu=new JLabel("-"),quantity=new JLabel("-");
	private JTextArea description=new JTextArea("-"),cardInfo=new JTextArea("");
	private JList fileList;
	private DefaultListModel listModel = new DefaultListModel();
	private HomeCellRenderer cellRenderer;
	private String folder;
	private String fileProp="";
	private JPanel buttonPanel,statusBar;
	private JTable table;
	private SortableTableModel tableModel;
	private JLabel statusLabel;
	private String[] buttonList = new String[0];
	private HackerFile file=null;
	private HomeList homeList;
	private HomePopUp HPU;
	private JScrollPane mainScroll;
	private int show=0;
	
	public Home(String name,boolean resize,boolean max,boolean close,boolean iconify,JDesktopPane mainPanel,Hacker MyHacker){
		this.setTitle(name);
		this.setResizable(true);
		this.setMaximizable(true);
		this.setClosable(close);
		this.setIconifiable(iconify);
		this.addInternalFrameListener(this);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.MyHacker=MyHacker;
		this.mainPanel=mainPanel;
		directory = MyHacker.getCurrentDirectory();
		folder = MyHacker.getCurrentFolder();
		cellRenderer = new HomeCellRenderer(directory,this);
		this.setFrameIcon(ImageLoader.getImageIcon("images/home.png"));
		addComponentListener(this);
		HPU=new HomePopUp();
		HPU.setVisible(false);
		mainPanel.add(HPU);
		//layout=new SpringLayout();
		setLayout(new GridBagLayout());
	}
	
	public void populate(){
		createMenu();
		createToolbar();
		GridBagConstraints c = new GridBagConstraints();
		Insets insets = this.getInsets();
		int height=insets.top+5+toolBar.getPreferredSize().height;
		int width=insets.left+2;
		Rectangle size = this.getBounds();
		buttonPanel = new JPanel();
		JButton button = new JButton("Home");
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING,2,0));
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		//c.anchor = GridBagConstraints.FIRST_LINE_START;
		//c.gridwidth = 2;
		c.weightx = 1.0;
		c.weighty = 0.0;
		buttonPanel.setMinimumSize(new Dimension(0,button.getPreferredSize().height+5));
		add(buttonPanel,c);
		//buttonPanel.setBounds(width,height,size.width-110,button.getPreferredSize().height+5);
		
		height+=30+buttonPanel.getPreferredSize().height;
		//tableView = new JPanel();
		//tableView.setLayout(null);
		properties = new JPanel();
		properties.setBackground(Color.white);
		//properties.setBorder(BorderFactory.createLineBorder(Color.black));
		JScrollPane propScroll = new JScrollPane(properties);
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0.4;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.7;
		//add(propScroll,c);
		//propScroll.setBounds(2,0,width+240,size.height-165);
		//populateProperties();
		//width+=250;
		
		Object[][] data = {};
		tableModel = new SortableTableModel(SortableTableModel.HOME);
		tableModel.setDataVector(data,columns);
		tableModel.addTableModelListener(this);
		table = new JTable(tableModel);
		table.addMouseListener(this);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setAutoCreateColumnsFromModel(false);
		
		//table.setAutoCreateRowSorter(true);
		//TableSorter ts = new TableSorter();
		SortButtonRenderer renderer = new SortButtonRenderer();
		TableColumnModel model = table.getColumnModel();
		int n = columns.length;
		int totalWidth = 0;
		int[] columnWidth = new int[]{174,105,68,60,110,80,74,1500};
		for (int i=0;i<n;i++) {
		  model.getColumn(i).setHeaderRenderer(renderer);
		  model.getColumn(i).setPreferredWidth(columnWidth[i]);
		  //totalWidth+=columnWidth[i];
		}
		JTableHeader header = table.getTableHeader();
		header.addMouseListener(new HeaderListener(header,renderer));
		//table.setPreferredSize(new Dimension(totalWidth,10000));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.addKeyListener(this);
		mainScroll = new JScrollPane(table);
		mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		c.gridx = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(mainScroll,c);
		//mainScroll.setBounds(252,0,size.width-267,size.height-165);
		//height+=mainScroll.getSize().height+5;
		//add(tableView);
		//tableView.setBounds(2,height,size.width-10,size.height-165);
		//tableView.setVisible(true);
		c.gridx = 0;
		c.gridy++;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		//c.gridwidth = 2;
		statusBar = new JPanel();
		//statusBar.setBounds(0,size.height-85,size.width,25);
		statusBar.setLayout(new FlowLayout(FlowLayout.LEADING,2,0));
		int HDQuantity = MyHacker.getHDQuantity();
		int HDMax = HDMAX[MyHacker.getHDType()];
		float HDPercent = (float)HDQuantity/(float)HDMax;
		NumberFormat nf = NumberFormat.getPercentInstance();
		statusLabel = new JLabel("File Quota: "+HDQuantity+"/"+HDMax+" ("+nf.format(HDPercent)+")");
		statusBar.add(statusLabel);
		//statusLabel.setBounds(2,2,statusLabel.getPreferredSize().width+20,statusLabel.getPreferredSize().height);
		statusBar.setMinimumSize(new Dimension(0,statusLabel.getPreferredSize().height+5));
		add(statusBar,c);
	}
	
	private void createMenu(){
		JMenuBar menuBar=new JMenuBar();
		JMenu menu,submenu;
		JMenuItem menuItem;
		
		menu = new JMenu("File");
		
		
		menuItem = new JMenuItem("New Folder",ImageLoader.getImageIcon("images/newfolder.png"));
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menu.addSeparator();
		
		menuItem = new JMenuItem("Exit",ImageLoader.getImageIcon("images/exit.png"));
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuBar.add(menu);
		
		menu = new JMenu("Edit");
		menuBar.add(menu);
		
		menuItem = new JMenuItem("Delete",ImageLoader.getImageIcon("images/delete.png"));
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		this.setJMenuBar(menuBar);
		
	}
	
	private void createToolbar(){
		toolBar = new JToolBar("Tools");
		JButton button;
		
		button = new JButton(ImageLoader.getImageIcon("images/refresh.png"));
		button.addActionListener(this);
		button.setActionCommand("Refresh");
		button.setToolTipText("Refresh.");
		toolBar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/newfolder.png"));
		button.addActionListener(this);
		button.setActionCommand("New Folder");
		button.setToolTipText("Create a new folder.");
		toolBar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/up.png"));
		button.addActionListener(this);
		button.setActionCommand("Up");
		button.setToolTipText("Up One Directory.");
		toolBar.add(button);
		
		toolBar.addSeparator();
		
		button = new JButton(ImageLoader.getImageIcon("images/close.png"));
		button.addActionListener(this);
		button.setActionCommand("Delete");
		button.setToolTipText("Delete selected file(s) or folder(s).");
		toolBar.add(button);
		
		toolBar.addSeparator();
		
		button = new JButton(ImageLoader.getImageIcon("images/calc.png"));
		button.addActionListener(this);
		button.setActionCommand("Sell");
		button.setToolTipText("Sell file(s) to Store.");
		toolBar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/decompile.png"));
		button.addActionListener(this);
		button.setActionCommand("Decompile");
		button.setToolTipText("Decompile the selected file.");
		toolBar.add(button);
				
		Insets insets = this.getInsets();
		Rectangle size = this.getBounds();
		Dimension dim = toolBar.getPreferredSize();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		//c.gridwidth = 2;
		this.add(toolBar,c);
		//toolBar.setBounds(insets.left+2,insets.top+5,size.width,dim.height);
		//layout.putConstraint(SpringLayout.NORTH,toolBar,0,SpringLayout.NORTH,this);
		//layout.putConstraint(SpringLayout.WEST,toolBar,0,SpringLayout.WEST,this);
	}

	
	public void populateProperties(){
		Font f = new Font("Dialog",Font.PLAIN,12);
		layout=new SpringLayout();
		properties.setLayout(layout);
		JLabel header = new JLabel("<html><h1><u>Properties</u></h1></html>");
		properties.add(header);
		
		layout.putConstraint(SpringLayout.NORTH,header,5,SpringLayout.NORTH,properties);
		layout.putConstraint(SpringLayout.WEST,header,1,SpringLayout.WEST,properties);
		
		JLabel nameLabel = new JLabel("Name: ");
		properties.add(nameLabel);
		layout.putConstraint(SpringLayout.NORTH,nameLabel,15+header.getPreferredSize().height,SpringLayout.NORTH,header);
		layout.putConstraint(SpringLayout.WEST,nameLabel,1,SpringLayout.WEST,properties);
		
		JLabel typeLabel = new JLabel("Type: ");
		properties.add(typeLabel);
		layout.putConstraint(SpringLayout.NORTH,typeLabel,5+nameLabel.getPreferredSize().height,SpringLayout.NORTH,nameLabel);
		layout.putConstraint(SpringLayout.WEST,typeLabel,1,SpringLayout.WEST,properties);
		
		JLabel makerLabel = new JLabel("Maker: ");
		properties.add(makerLabel);
		layout.putConstraint(SpringLayout.NORTH,makerLabel,5+typeLabel.getPreferredSize().height,SpringLayout.NORTH,typeLabel);
		layout.putConstraint(SpringLayout.WEST,makerLabel,1,SpringLayout.WEST,properties);
		
		JLabel priceLabel = new JLabel("Price: ");
		properties.add(priceLabel);
		layout.putConstraint(SpringLayout.NORTH,priceLabel,5+makerLabel.getPreferredSize().height,SpringLayout.NORTH,makerLabel);
		layout.putConstraint(SpringLayout.WEST,priceLabel,1,SpringLayout.WEST,properties);
		
		JLabel cpuLabel = new JLabel("CPU Cost: ");
		properties.add(cpuLabel);
		layout.putConstraint(SpringLayout.NORTH,cpuLabel,5+priceLabel.getPreferredSize().height,SpringLayout.NORTH,priceLabel);
		layout.putConstraint(SpringLayout.WEST,cpuLabel,1,SpringLayout.WEST,properties);
		
		JLabel quantityLabel = new JLabel("Quantity: ");
		properties.add(quantityLabel);
		layout.putConstraint(SpringLayout.NORTH,quantityLabel,5+cpuLabel.getPreferredSize().height,SpringLayout.NORTH,cpuLabel);
		layout.putConstraint(SpringLayout.WEST,quantityLabel,1,SpringLayout.WEST,properties);
		
		JLabel descriptionLabel = new JLabel("Description: ");
		properties.add(descriptionLabel);
		layout.putConstraint(SpringLayout.NORTH,descriptionLabel,5+quantityLabel.getPreferredSize().height,SpringLayout.NORTH,quantityLabel);
		layout.putConstraint(SpringLayout.WEST,descriptionLabel,1,SpringLayout.WEST,properties);
		
		int width=descriptionLabel.getPreferredSize().width+5;
		
		properties.add(name);
		name.setFont(f);
		layout.putConstraint(SpringLayout.NORTH,name,15+header.getPreferredSize().height,SpringLayout.NORTH,header);
		layout.putConstraint(SpringLayout.WEST,name,width,SpringLayout.WEST,properties);
		
		properties.add(type);
		type.setFont(f);
		layout.putConstraint(SpringLayout.NORTH,type,5+nameLabel.getPreferredSize().height,SpringLayout.NORTH,nameLabel);
		layout.putConstraint(SpringLayout.WEST,type,width,SpringLayout.WEST,properties);
		
		properties.add(maker);
		maker.setFont(f);
		layout.putConstraint(SpringLayout.NORTH,maker,5+typeLabel.getPreferredSize().height,SpringLayout.NORTH,typeLabel);
		layout.putConstraint(SpringLayout.WEST,maker,width,SpringLayout.WEST,properties);
		
		properties.add(price);
		price.setFont(f);
		price.addMouseListener(this);
		layout.putConstraint(SpringLayout.NORTH,price,5+makerLabel.getPreferredSize().height,SpringLayout.NORTH,makerLabel);
		layout.putConstraint(SpringLayout.WEST,price,width,SpringLayout.WEST,properties);
		
		JButton button = new JButton(ImageLoader.getImageIcon("images/edit.png"));
		button.setActionCommand("Price");
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.addActionListener(this);
		properties.add(button);
		layout.putConstraint(SpringLayout.NORTH,button,5+makerLabel.getPreferredSize().height,SpringLayout.NORTH,makerLabel);
		layout.putConstraint(SpringLayout.WEST,button,20+priceLabel.getBounds().x+priceLabel.getBounds().width,SpringLayout.WEST,priceLabel);
		
		
		properties.add(cpu);
		cpu.setFont(f);
		layout.putConstraint(SpringLayout.NORTH,cpu,5+priceLabel.getPreferredSize().height,SpringLayout.NORTH,priceLabel);
		layout.putConstraint(SpringLayout.WEST,cpu,width,SpringLayout.WEST,properties);
		
		properties.add(quantity);
		quantity.setFont(f);
		layout.putConstraint(SpringLayout.NORTH,quantity,5+cpuLabel.getPreferredSize().height,SpringLayout.NORTH,cpuLabel);
		layout.putConstraint(SpringLayout.WEST,quantity,width,SpringLayout.WEST,properties);
		
		properties.add(description);
		description.setColumns(14);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setEditable(false);
		description.addMouseListener(this);
		//description.setBorder(BorderFactory.createLineBorder(Color.black));
		layout.putConstraint(SpringLayout.NORTH,description,5+quantityLabel.getPreferredSize().height,SpringLayout.NORTH,quantityLabel);
		layout.putConstraint(SpringLayout.WEST,description,width,SpringLayout.WEST,properties);
		
		button = new JButton(ImageLoader.getImageIcon("images/edit.png"));
		button.setActionCommand("Description");
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.addActionListener(this);
		properties.add(button);
		layout.putConstraint(SpringLayout.NORTH,button,descriptionLabel.getPreferredSize().height,SpringLayout.NORTH,descriptionLabel);
		layout.putConstraint(SpringLayout.WEST,button,20+descriptionLabel.getBounds().x+descriptionLabel.getBounds().width,SpringLayout.WEST,descriptionLabel);
		
		properties.add(cardInfo);
		cardInfo.setColumns(14);
		cardInfo.setLineWrap(true);
		cardInfo.setWrapStyleWord(true);
		cardInfo.setEditable(false);
		//description.setBorder(BorderFactory.createLineBorder(Color.black));
		layout.putConstraint(SpringLayout.NORTH,cardInfo,45+description.getPreferredSize().height,SpringLayout.NORTH,description);
		layout.putConstraint(SpringLayout.WEST,cardInfo,width,SpringLayout.WEST,properties);
		
	}
	
	public void startUp(){
		//System.out.println("Requesting Directory From Home");
		View MyView = MyHacker.getView();
		Object objects[] = {MyHacker.getEncryptedIP(),""};
		MyHacker.setCurrentFolder("");
		folder="";
		MyView.setFunction("requestdirectory");
		MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HOME,"requestdirectory",objects));
		MyHacker.setRequestedDirectory(Hacker.HOME);
		int HDQuantity = MyHacker.getHDQuantity();
		int HDMax = MyHacker.getHDMax();
		float HDPercent = (float)HDQuantity/(float)HDMax;
		NumberFormat nf = NumberFormat.getPercentInstance();
		statusLabel.setText("File Quota: "+HDQuantity+"/"+HDMax+" ("+nf.format(HDPercent)+")");
		statusLabel.setBounds(2,2,statusLabel.getPreferredSize().width+20,statusLabel.getPreferredSize().height);
	}
	
	public void setDirectory(Object[] directory){
		//System.out.println("Setting Directory");
		//System.out.println(directory[0]);
		
		//tableModel.resetData();
		//homeList.reset();
		
		int j=0;
		if(this.directory==null||this.directory.length!=directory.length){
			tableModel.resetData();
			try{
				if(directory!=null){
					//System.out.println("Setting shownDirectory");
					shownDirectory = new Object[directory.length];
					Object[] rowData=new Object[columns.length];
					for(int i=0;i<directory.length;i++){
						if(directory[i] instanceof String){
							if(!((String)(directory[i])).equals("Store")&&!((String)(directory[i])).equals("Public")){
								shownDirectory[j]=directory[i];
								rowData[NAME] = (String)directory[i];
								rowData[TYPE] = "Directory";
								rowData[QUANTITY] = 1;
								rowData[YOUR_STORE_PRICE] = 0.0f;
                                rowData[SELL_TO_STORE_PRICE] = 0.0f;
								rowData[MAKER] = "--";
								rowData[CPU_COST] = 0.0f;
								rowData[DESCRIPTION] = "--";
								tableModel.addRow(rowData);
								j++;
							}
						}
						else{
							Object dir[] = (Object[])directory[i];
							int type=(Integer)dir[1];
							shownDirectory[j]=dir;
							rowData[NAME] = (String)dir[0];
							rowData[TYPE] = TYPES[type];
							rowData[QUANTITY] = (Integer)dir[2];						
							rowData[YOUR_STORE_PRICE] = (Float)dir[3];
                            float price = getPrice((String)dir[4]);
                            rowData[SELL_TO_STORE_PRICE] = (Float)dir[7];
							rowData[MAKER] = (String)dir[4];
							rowData[CPU_COST] = (Float)dir[5];
							rowData[DESCRIPTION] = (String)dir[6];
							tableModel.addRow(rowData);
							j++;
						}
						
					}
					table.getColumnModel().getColumn(0).setCellRenderer(new HomeTableCellRenderer(this,shownDirectory));
					table.revalidate();
					table.repaint();
					populateButtons();
				}
			}catch(NullPointerException e){
				e.printStackTrace();
			}
			this.directory=directory;
		}
	}
	
	public void setStatusLabel(){
		int HDQuantity = MyHacker.getHDQuantity();
		int HDMax = MyHacker.getHDMax();
		float HDPercent = (float)HDQuantity/(float)HDMax;
		NumberFormat nf = NumberFormat.getPercentInstance();
		statusLabel.setText("File Quota: "+HDQuantity+"/"+HDMax+" ("+nf.format(HDPercent)+")");
		statusLabel.setBounds(2,2,statusLabel.getPreferredSize().width+20,statusLabel.getPreferredSize().height);
	}
		
	
	public void getProperties(String fileName,boolean get){
		if(!fileProp.equals(fileName)&&get){
			fileProp=fileName;
			//System.out.println("Getting properties of "+fileName+" in folder "+folder);
			MyHacker.setRequestedFile(Hacker.HOME);
			View MyView = MyHacker.getView();
			String ip=MyHacker.getEncryptedIP();
			Object objects[] = {ip,folder,fileName};
			MyView.setFunction("requestfile");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"requestfile",objects));
		}
		if(!get){
			fileProp="";
			name.setText(fileName);
			type.setText("Directory");
			maker.setText("-");
			price.setText("-");
			description.setText("-");
			cpu.setText("-");
			quantity.setText("-");
			cardInfo.setText("");
			
		}
	}
	
	public void changeDirectory(String name){
		//System.out.println("Change Directory to "+name);
		if(!name.equals("..")){
			//sourceDir.setText(sourceDir.getText()+name+"/");
			//requests[requestedFolders]=name;
			//requestedFolders++;
			//String folders[] = folder.split("/");
			String newFolder="";
			String[] newList = new String[buttonList.length+1];
			for(int i=0;i<buttonList.length;i++){
				 newFolder+=buttonList[i]+"/";
				 newList[i]=buttonList[i];
			}
			newList[newList.length-1]=name;
			//System.out.println(newList.length);
			buttonList=newList;
			MyHacker.setCurrentFolder(newFolder+name);
			folder = newFolder+name;
			//System.out.println("Changing To "+folder);
			//System.out.println("Requesting "+newFolder+name+"/");
			Object objects[] = new Object[]{MyHacker.getEncryptedIP(),newFolder+name+"/"};
			View MyView=MyHacker.getView();
			MyHacker.setRequestedDirectory(Hacker.HOME);
			MyView.setFunction("requestdirectory");
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HOME,"requestdirectory",objects));
			//requestDirectory();
			//populateButtons();
		}
		else{
			if(!folder.equals("")){
				
				String folders[] = folder.split("/");
				String newFolder="";
				String[] newList = new String[buttonList.length-1];
				for(int i=0;i<folders.length-1;i++){
					 newFolder+=buttonList[i]+"/";
					 newList[i]=buttonList[i];
				}
				buttonList=newList;
				//sourceDir.setText("/home/"+newFolder);
				//System.out.println(newFolder);
				folder=newFolder;
				//System.out.println(folders[folders.length-1]);
				//System.out.println("Go up a directory");
				Object objects[] = new Object[]{MyHacker.getEncryptedIP(),newFolder};
				MyHacker.setCurrentFolder(newFolder);
				MyHacker.setRequestedDirectory(Hacker.HOME);
				View MyView = MyHacker.getView();
				MyView.setFunction("requestdirectory");
				MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HOME,"requestdirectory",objects));
			}
		}
	}
	
	public void callDirectory(String location){
		//System.out.println("Location: "+location);
		String folders[] = location.split("/");
		//System.out.println("Folders Size: "+folders.length);
		String newFolder="";
		String[] newList;
		if(!location.equals("")){
			newList = new String[folders.length];
			for(int i=0;i<folders.length;i++){
				//System.out.println("Adding Folder: "+folders[i]);
				if(!folders[i].equals("")){
						newFolder+=folders[i]+"/";
						newList[i]=folders[i];
				}
			}
		}
		else{
			newList = new String[0];
		}
		buttonList=newList;
		folder=newFolder;
		Object objects[] = new Object[]{MyHacker.getEncryptedIP(),location};
		MyHacker.setCurrentFolder(location);
		MyHacker.setRequestedDirectory(Hacker.HOME);
		View MyView = MyHacker.getView();
		MyView.setFunction("requestdirectory");
		MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HOME,"requestdirectory",objects));
	}
	
	public void setProperties(HackerFile HF){
		file=HF;
		name.setText(HF.getName());
		type.setText(TYPES[HF.getType()]);
		maker.setText(HF.getMaker());
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		price.setText(nf.format(HF.getPrice()));
		description.setText(HF.getDescription());
		cpu.setText(""+HF.getCPUCost());
		quantity.setText(""+HF.getQuantity());
		cardInfo.setText("");
		if(HF.getType()==HackerFile.AGP||HF.getType()==HackerFile.PCI){
			HashMap content = HF.getContent();
			float durability=50.0f;
			float max=50.0f; 
			try{//In case it's hardware that hasn't had a durability set for some reason, e.g., old hardware.
				durability = Float.valueOf((String)content.get("currentquality"));
				max = Float.valueOf((String)content.get("maxquality"));
			}catch(Exception e){}
			
			String d = "Durability: "+durability+"/"+max;
			String values = (String)content.get("bonusdata");
			String[] v = values.split("\\|");
			String value1 = v[0];
			String value2 = v[1];
			
			cardInfo.setText(d+"\n"+value1+"\n"+value2);
			/*if(quality1+quality2==18)
				System.out.println("---------------------------\nAlien \n---------------------------");
			else if(quality1+quality2>=14)
				System.out.println("---------------------------\nExperimental \n---------------------------");
			else if(quality1+quality2>=9)
				System.out.println("---------------------------\nPremium \n---------------------------");
			else if(quality1+quality2>5)
				System.out.println("---------------------------\nConsumer \n---------------------------");
			else
				System.out.println("---------------------------\nValue \n---------------------------");
			System.out.println(value1);
			System.out.println(value2);*/
		}
	}
	
	public void populateButtons(){
		buttonPanel.removeAll();
		buttonPanel.repaint();
		JButton button = new JButton("Home");
		buttonPanel.add(button);
		if(buttonList.length==0)
			button.setEnabled(false);
		button.addActionListener(new HomeActionListener("",this));
		button.setBounds(2,2,button.getPreferredSize().width,button.getPreferredSize().height);
		String folder="";
		int x=button.getPreferredSize().width+5;
		for(int i=0;i<buttonList.length;i++){
			
			folder+=buttonList[i]+"/";
			if(!folder.equals("/")){
				//System.out.println("Button name: "+folder);
				button = new JButton(buttonList[i]);
				buttonPanel.add(button);
				button.setBounds(x,2,button.getPreferredSize().width,button.getPreferredSize().height);
				if(i==buttonList.length-1)
					button.setEnabled(false);
				button.addActionListener(new HomeActionListener(folder,this));
				x+=button.getPreferredSize().width+5;
			}
		}
		buttonPanel.doLayout();
	}
	
	public void changePrice(String fileName,float price){
		View MyView = MyHacker.getView();
		Object objects[] = {MyHacker.getEncryptedIP(),folder,fileName,new Float(price)};
		MyView.setFunction("setfileprice");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"setfileprice",objects));
	}
	
	public void changeDescription(String fileName,String description){
		View MyView = MyHacker.getView();
		Object objects[] = {MyHacker.getEncryptedIP(),folder,fileName,description};
		MyView.setFunction("setfiledescription");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"setfiledescription",objects));
	}
	
	public String getFolder(){
		return folder;
	}
	
	public void sellFile(int index,int quantity){
    
        int[] selRows = table.getSelectedRows();
        Object[] allFiles = new Object[1];
        if (selRows.length == 0) {
                return;
        }
        String fileName = (String)tableModel.getValueAt(index,NAME);
		String maker = (String)tableModel.getValueAt(index,MAKER);
		String typeString = (String)tableModel.getValueAt(index,TYPE);
		float storeBuyPrice = (Float)tableModel.getValueAt(index,SELL_TO_STORE_PRICE);
        int type = getType(typeString);
		//if(maker.equals("Alexander")||maker.equals("Low")||maker.equals("Medium")||maker.equals("High")||maker.equals("Rare")||maker.equals("Holiday")){
        if (storeBuyPrice!=0.0f) {
            float price = storeBuyPrice;
			float compilingcost=0.0f;
			/*if(type!=HackerFile.FIREWALL&&type!=HackerFile.AGP&&type!=HackerFile.PCI){
				HashMap HM = file.getContent();
				try {
					HashMap levels = new HashMap();
					levels.put("Attack",new Integer(100));
					levels.put("Merchanting",new Integer(100));
					levels.put("Watch",new Integer(100));
					Object[] params = new Object[]{new Integer(type),HM,levels};
					HashMap result = (HashMap)XMLRPCCall.execute("http://"+MyHacker.getView().getIP()+":8081/xmlrpc","hackerRPC.compileApplication",params);
					compilingcost = (float)(double)(Double)result.get("price");
				} catch(Exception ex){ex.printStackTrace();}
			}*/
         
            allFiles[0] = new Object[]{folder,fileName,maker,quantity};
            // check to see that there's a bank port on
            if ( (MyHacker.getBankPorts()).size() > 0) {
				Object objects[] = new Object[]{MyHacker.getEncryptedIP(),allFiles};
				View MyView=MyHacker.getView();
				MyHacker.setRequestedDirectory(Hacker.HOME);
				MyView.setFunction("sellfilemulti");
                MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HOME,"sellfilemulti",objects));
        		int maxQuantity = (Integer)tableModel.getValueAt(index,QUANTITY);
        		if(quantity == maxQuantity){
        			removeRow(index);
        		} else {
        			tableModel.setValueAt((maxQuantity-quantity),index,QUANTITY);
        		}
			} else {
                // send a message saying that shit is busticated.
                String message = "You can't sell files without a non-dummy bank port turned on.";
                MyHacker.showMessage(message);
            }
		}
	}
	
	public int getType(String typeString){
		for(int i=0;i<TYPES.length;i++){
			if(typeString.equals(TYPES[i])){
				return(i);
			}
		}
		return(0);
	}
	
    public void removeRow(int row){
		tableModel.removeRow(row);
		Object[] temp = new Object[directory.length-1];
		int in=0;
		for(int i=0;i<temp.length;i++){
			if(i!=row){
				temp[in] = directory[i];
				in++;
			}
		}
		directory = temp;
	}
    
    public void removeRows(int[] rows) {
        int numRows = rows.length;
        // create a convenience hashmap of the rows
        HashMap tempHash = new HashMap();
        for (int i = 0; i < numRows; i++) {
            tempHash.put(rows[i], i);
        }
        tableModel.removeRows(rows);
		Object[] temp = new Object[directory.length - numRows];
		int in=0;
        
		for(int i=0; i < temp.length; i++){
            if (tempHash.get(i) == null) {
				temp[in] = directory[i];
				in++;
			}
		}
		directory = temp;
    }
	
    public int getShow() {
		return show;
	}
    
	public void internalFrameClosed(InternalFrameEvent e) {
		MyHacker.setHomeOpen(false);
	}
    
    private boolean isFolder(String type) {
        if (type.equals("Directory")) {
            return true;
        }
        return false;
    }
    
    private void deleteFile() {
        int[] selRows = table.getSelectedRows();
        if (selRows.length == 0) {
                return;
        }
        
        int n = -1;
        if (selRows.length == 1) {
            int selRow = selRows[0];
            String selectedFile = (String)tableModel.getValueAt(selRow,NAME);
            String type = (String)tableModel.getValueAt(selRow,TYPE);
            if (isFolder(type)) {
                n = showDeleteDialog("the folder '" + selectedFile + "' and all of its contents");
            } else {
                n = showDeleteDialog("'" + selectedFile + "'");
            }
        } else {
            n = showDeleteDialog("these " + selRows.length + " items");
        }
        
        if (n == 0) {
            Object[] allFiles = new Object[selRows.length];
            for (int i = 0; i < selRows.length; i++) {
                String[] details = new String[3];
                //build up the object[] to send to the server
                int selRow = selRows[i];
                String selectedFile = (String)tableModel.getValueAt(selRow,NAME);
                String type = (String)tableModel.getValueAt(selRow,TYPE);
                details[0] = folder;
                details[1] = selectedFile;
                if (isFolder(type)) {
                    details[2] = "directory";
                } else {
                    details[2] = "file";
                }
                allFiles[i] = details;
            }
            Object objects[] = {MyHacker.getEncryptedIP(), allFiles};
            View MyView = MyHacker.getView();
            MyView.setFunction("deletemulti");
            MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HOME,"deletemulti",objects));
            MyHacker.setCurrentFolder(folder);
            MyHacker.setRequestedDirectory(Hacker.HOME);
            removeRows(selRows);
        }
    }
    
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Exit")){
			this.doDefaultCloseAction();
			//mainPanel.repaint();
		}
		if(e.getActionCommand().equals("Delete")){
            deleteFile();
		}
		/*if(e.getActionCommand().equals("Cut")){
			if(table.getSelectedRow()!=-1){
				String selectedFile = (String)table.getModel().getValueAt(table.getSelectedRow(),0);
				//System.out.println("Cut File "+selectedFile);
			}
		}
		if(e.getActionCommand().equals("Copy")){
			if(table.getSelectedRow()!=-1){
				String selectedFile = (String)table.getModel().getValueAt(table.getSelectedRow(),0);
				//System.out.println("Copy File "+selectedFile);
			}
		}*/
		if(e.getActionCommand().equals("New Folder")){
			String answer = (String)JOptionPane.showInputDialog(mainPanel,"Name of Folder:","New Folder",JOptionPane.PLAIN_MESSAGE,null,null,"");
			if(!answer.equals("")&&answer.matches("([a-zA-Z]||[0-9]||[ \\._-])*")){
				if(answer.equalsIgnoreCase("store")) JOptionPane.showMessageDialog(mainPanel,"Error: invalid folder name: "+answer);
				View MyView = MyHacker.getView();
				Object objects[] = {MyHacker.getEncryptedIP(),folder+"/"+answer+"/"};
				MyHacker.setRequestedDirectory(Hacker.HOME);
				MyView.setFunction("createfolder");
				MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HOME,"createfolder",objects));
			}
			//System.out.println("New Folder: "+answer);
		}
		if(e.getActionCommand().equals("Up")){
			changeDirectory("..");
		}
		if(e.getActionCommand().equals("Refresh")){
			Object objects[] = new Object[]{MyHacker.getEncryptedIP(),folder};
			View MyView=MyHacker.getView();
			MyHacker.setRequestedDirectory(Hacker.HOME);
			MyView.setFunction("requestdirectory");
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HOME,"requestdirectory",objects));
		}
		if(e.getActionCommand().equals("Price")){
			HomePriceDialog HPD = new HomePriceDialog(this);
		}
		if(e.getActionCommand().equals("Description")){
			HomeDescriptionDialog HDD = new HomeDescriptionDialog(this,description.getText());
		}
		if(e.getActionCommand().equals("Sell")){
            int[] selRows = table.getSelectedRows();
            if (selRows.length == 0) {
                    return;
            }
            int n = -1;
            if (selRows.length == 1) {
                int selRow = selRows[0];
                String type = (String)tableModel.getValueAt(selRow,TYPE);
				String maker = (String)tableModel.getValueAt(selRow,MAKER);
				int quantity = (Integer)tableModel.getValueAt(selRow,QUANTITY);
				String fileName = (String)tableModel.getValueAt(selRow,NAME);
				float storeBuyPrice = (Float)tableModel.getValueAt(selRow,SELL_TO_STORE_PRICE);
				float price=0.0f;
                // make sure they can sell the file; can only sell files made by entries in the 'makers' hashmap
				if(storeBuyPrice!=0.0f) {
					price = getPrice(maker);
					HomeQuantityDialog HQD = new HomeQuantityDialog(this,MyHacker,quantity,fileName,storeBuyPrice,selRow);
				} else {
                    MyHacker.showMessage("You can only sell Firewalls, Cards, and non-free Scripts.");
                }
            } else {
                n = showSellDialog("" + selRows.length);
                if (n == 0) {
                    Object[] allFiles = new Object[selRows.length];
                    boolean okay = true;
                    for (int i = 0; i < selRows.length; i++) {
                        Object[] details = new Object[4];
                        //build up the object[] to send to the server
                        int selRow = selRows[i];
                        String type = (String)tableModel.getValueAt(selRow,TYPE);
                        String maker = (String)tableModel.getValueAt(selRow,MAKER);
                        Integer quantity = (Integer)tableModel.getValueAt(selRow,QUANTITY);
                        String selectedFile = (String)tableModel.getValueAt(selRow,NAME);
						float storeBuyPrice = (Float)tableModel.getValueAt(selRow,SELL_TO_STORE_PRICE);
                        if (isFolder(type)) {
                            MyHacker.showMessage("You cannot sell folders.");
                            okay = false;
                            break;
                        }
                        if ( storeBuyPrice == 0.0f ) {
                            MyHacker.showMessage("You can not sell the following file: " + selectedFile);
                            okay = false;
                            break;
                        }
                        details[0] = folder;
                        details[1] = selectedFile;
                        details[2] = maker;
                        details[3] = quantity;
                        allFiles[i] = details;
//System.out.println("details[name] = " + selectedFile + ", quantity = " + quantity);
                    }
                    if (okay) {
                        if ((MyHacker.getBankPorts()).size() > 0) {
                            Object objects[] = {MyHacker.getEncryptedIP(), allFiles};
                            View MyView = MyHacker.getView();
                            MyView.setFunction("sellmulti");
                            MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HOME,"sellfilemulti",objects));
                            MyHacker.setCurrentFolder(folder);
                            MyHacker.setRequestedDirectory(Hacker.HOME);
                            removeRows(selRows);
                        } else {
                            String message = "You can't sell files without a non-dummy bank port turned on.";
                            MyHacker.showMessage(message);
                        }
                    }
                }
            }
		}
		if(e.getActionCommand().equals("Decompile")){
			if(show==ICONS){
				//HackerFile file = null;
				HomeIcon selectedIcon = homeList.getSelected();
				if(selectedIcon!=null){
					file = selectedIcon.getFile();
				}
				else
					file=null;
			}
			//if(file!=null){
                int selRow = table.getSelectedRow();
				String maker = (String)tableModel.getValueAt(selRow,MAKER);
				int type = getType((String)tableModel.getValueAt(selRow,TYPE));
                String name = (String)tableModel.getValueAt(selRow,NAME);
				float price=0.0f;
				//if(table.getSelectedRow()!=-1){
					if(maker.toLowerCase().equals(MyHacker.getUsername().toLowerCase())){
						if(type==HackerFile.BANKING_COMPILED||type==HackerFile.ATTACKING_COMPILED||type==HackerFile.WATCH_COMPILED||type==HackerFile.HTTP||type==HackerFile.FTP_COMPILED||type==HackerFile.SHIPPING_COMPILED){
							Object[] options = {"Yes","No"};
							NumberFormat nf = NumberFormat.getCurrencyInstance();
							int n = JOptionPane.showOptionDialog(this,"Are You Sure you want to decompile "+name+"?",
								    "Decompile?",
								    JOptionPane.YES_NO_CANCEL_OPTION,
								    JOptionPane.QUESTION_MESSAGE,
								    null,
								    options,
								    options[1]);
							if(n==0){
								float compilingcost=0.0f;
								/*HashMap HM = file.getContent();	
								try{
									HashMap levels = new HashMap();
									levels.put("Attack",new Integer(100));
									levels.put("Merchanting",new Integer(100));
									levels.put("Watch",new Integer(100));
									Object[] params = new Object[]{new Integer(type),HM,levels};
									HashMap result = (HashMap)XMLRPCCall.execute("http://"+MyHacker.getView().getIP()+":8081/xmlrpc","hackerRPC.compileApplication",params);
									compilingcost = (float)(double)(Double)result.get("price");
								}catch(Exception ex){ex.printStackTrace();}	*/
								//Object[] selected = (Object[])shownDirectory[table.getSelectedRow()];
								Object objects[] = new Object[]{MyHacker.getEncryptedIP(),folder,name,compilingcost*0.9f};
								View MyView=MyHacker.getView();
								MyHacker.setRequestedDirectory(Hacker.HOME);
								MyView.setFunction("decompilefile");
								MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HOME,"decompilefile",objects));
							}
						}
						else{
							JOptionPane.showMessageDialog(this,
								"You can only decompile scripts that are compiled.",
								"Can't Decompile That File",
								JOptionPane.ERROR_MESSAGE);
						}
					}
					else{ //can't decompile this because maker isn't right
						JOptionPane.showMessageDialog(this,
						"You can only decompile scripts that you wrote.",
						"Can't Decompile That File",
						JOptionPane.ERROR_MESSAGE);
					}
				//}
			//}
		}
		if(e.getActionCommand().equals("View")){
			JComboBox cb = (JComboBox)e.getSource();
			String selected = (String)cb.getSelectedItem();
			if(selected.equals("List View")){
				iconView.setVisible(false);
				tableView.setVisible(true);
				show=0;
			}
			else if(selected.equals("Icon View")){
				tableView.setVisible(false);
				iconView.setVisible(true);
				show=1;
			}
		}
		if(e.getActionCommand().equals("Properties")){
			int selRow = table.getSelectedRow();
			String name = (String)tableModel.getValueAt(selRow,NAME);
			FileProperties FP = new FileProperties(MyHacker,name,folder);
		
		}
	}
	
    public static float getPrice(String maker) {
        float price = 0.0f;
        Object o = makers.get(maker);
        if (o != null) {
            price = ((Float)o).floatValue();
        }
        return(price);
    }
    
	public void mouseClicked(MouseEvent e){
		if(e.getClickCount()==2){
			int selRow = table.getSelectedRow();
			if(selRow!=-1){
				String type = (String)tableModel.getValueAt(selRow,TYPE);
				String fileName = (String)tableModel.getValueAt(selRow,NAME);
				int fileType = getType((String)tableModel.getValueAt(selRow,TYPE));
				if(type.equals("Directory")){
					//System.out.println(selected);
					changeDirectory(fileName);
				}
				else if(type.equals(TYPES[HackerFile.IMAGE])){
					Object[] o =  new Object[]{fileName,fileType};
					ImageViewer im = new ImageViewer("Image Viewer",false,false,true,true,mainPanel,MyHacker,o,folder);
					im.setBounds(100,100,450,450);
					im.populate();
					im.moveToFront();
					mainPanel.add(im);
					im.setVisible(true);
				}
				else if(type.equals(TYPES[HackerFile.GAME])||type.equals(TYPES[HackerFile.QUEST_GAME])){		
					String encryptedIP = MyHacker.getEncryptedIP();
					View MyView = MyHacker.getView();
					MyHacker.setRequestedFile(Hacker.HACKTENDO_PLAYER);
					Object[] objects = {encryptedIP,folder,fileName};
					MyView.setFunction("requestgame");
					MyView.addFunctionCall(new RemoteFunctionCall(0,"requestgame",objects));
				}
				else if(type.equals(TYPES[HackerFile.BANKING_SCRIPT])||type.equals(TYPES[HackerFile.ATTACKING_SCRIPT])||type.equals(TYPES[HackerFile.WATCH_SCRIPT])||type.equals(TYPES[HackerFile.SHIPPING_SCRIPT])||type.equals(TYPES[HackerFile.TEXT])||type.equals(TYPES[HackerFile.FTP_SCRIPT])){
					MyHacker.showScriptEditor(folder,fileName);
				}
			}
		}
		if(e.isPopupTrigger()){
			showPopUpMenu(e);
		}
	}
	
	public void mousePressed(MouseEvent e){
		if(e.isPopupTrigger()){
			showPopUpMenu(e);
		}
	}
	
	public void mouseReleased(MouseEvent e){
		if(e.isPopupTrigger()){
			showPopUpMenu(e);
		}
	}
	
	public void showPopUpMenu(MouseEvent e){
		Point p = e.getPoint();

		// get the row index that contains that coordinate
		int rowNumber = table.rowAtPoint( p );

		// Get the ListSelectionModel of the JTable
		ListSelectionModel model = table.getSelectionModel();

		// set the selected interval of rows. Using the "rowNumber"
		// variable for the beginning and end selects only that one row.
		model.setSelectionInterval( rowNumber, rowNumber );
		JPopupMenu menu = new JPopupMenu();
		JMenuItem menuItem;
		
		menuItem = new JMenuItem("Properties");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menu.show(table,e.getX(),e.getY());
	}
	
	public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
        
    }

    public void componentResized(ComponentEvent e){
		//table.sizeColumnsToFit(false);
		if(table!=null){
			table.doLayout();
		}
	 /*   	Rectangle bounds = getBounds();
		if(toolBar!=null)
			toolBar.setBounds(2,5,bounds.width,toolBar.getBounds().height);	 
		if(mainScroll!=null)
			mainScroll.setBounds(mainScroll.getBounds().x,mainScroll.getBounds().y,bounds.width-25-mainScroll.getBounds().x,bounds.height-165);
		if(homeList!=null)
			homeList.resize(bounds.width-25-mainScroll.getBounds().x,bounds.height-165);
		if(statusBar!=null)
			statusBar.setBounds(0,mainScroll.getBounds().y+mainScroll.getBounds().height+5,bounds.width,25);
		if(folder!=null)
			callDirectory(folder);*/
    }

    public void componentShown(ComponentEvent e) {

    }
	
	public void tableChanged(TableModelEvent e){
		int row = table.getSelectedRow();//e.getFirstRow();
        int column = e.getColumn();
		if(row!=-1&&column!=-1){
			String columnName = tableModel.getColumnName(column);
			Object data = tableModel.getValueAt(row, column);
			String fileName = (String)tableModel.getValueAt(row,0);
			if(columnName.equals(columns[YOUR_STORE_PRICE])){
				//changing price
				float price = (float)(Float)data;
				changePrice(fileName,price);
			}
			else if(columnName.equals(columns[DESCRIPTION])){
				//changing description
				String description = (String)data;
				changeDescription(fileName,description);
			}
		}
	}
	
	class HeaderListener extends MouseAdapter {
    
        JTableHeader   header;
        SortButtonRenderer renderer;
      
        HeaderListener(JTableHeader header,SortButtonRenderer renderer) {
          this.header   = header;
          this.renderer = renderer;
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
  
	
}
