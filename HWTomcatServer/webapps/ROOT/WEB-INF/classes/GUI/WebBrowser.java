package GUI;
/**

WebBrowser.java
this is the web browser.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import View.*;
import Game.*;
import java.util.*;
import Assignments.*;
import java.lang.Math.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.Style;
import java.io.*;
import java.net.*;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import util.*;
import java.util.concurrent.Semaphore;
import java.text.*;
import Browser.*;
import org.lobobrowser.html.*;
//import org.lobobrowser.html.gui.DocumentNotification;
import net.miginfocom.swing.*;
public class WebBrowser extends Application implements ComponentListener{
	private JDesktopPane mainPanel=null;
	private Hacker MyHacker=null;
	private Object directory[];
	private View MyView;
	private HtmlHandler browse;
	private JTabbedPane tabbedPane;
	private JTextField urlField=new JTextField();
	private JPanel banking = new JPanel();
	private JPanel attack = new JPanel();
	private JPanel FTP = new JPanel();
	private JPanel watch = new JPanel();
	private JPanel firewall = new JPanel();
	private JPanel http = new JPanel();
	private JPanel cpu = new JPanel();
	private JPanel hd = new JPanel();
	private JPanel ram = new JPanel();
	private JPanel images = new JPanel();
	private JPanel bounty = new JPanel();
	private JPanel games = new JPanel();
	private JPanel commodities = new JPanel();
	private JPanel redirect = new JPanel();
	private ForumPanel forumPanel;
	private String ip;
	private int count=0,bcount=0,acount=0,ftcount=0,wcount=0,ficount=0,hcount=0,ccount=0,hdcount=0,mcount=0,icount=0,bocount=0,gcount=0,commodityCount=0,redirectCount=0;
	private JTabbedPane tb;
	private String[] ips = new String[10]; 
	private int position=0;
	private int tbYPosition=0;
	private JToolBar toolBar;
	private InventoryList agp,pci;
	private EquipmentPopUp EPU = new EquipmentPopUp();
	private JMenuBar menuBar = null;
	private JMenu bookmarksMenu = new JMenu();
	private String currentTitle="";
	private JLabel votesLeftLabel;
	public WebBrowser(String name,boolean resize,boolean max,boolean close,boolean iconify,JDesktopPane mainPanel,Hacker MyHacker){
		setTitle(name);
		setResizable(true);
		setMaximizable(true);
		setClosable(close);
		setIconifiable(iconify);
		addInternalFrameListener(this);
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		this.mainPanel=mainPanel;
		this.MyHacker=MyHacker;
		MyView=MyHacker.getView();
		addComponentListener(this);
		this.setFrameIcon(ImageLoader.getImageIcon("images/browser.png"));
	}
	
	public void receivedPage(String title,String page){
		currentTitle = title;
		setTitle("Web Browser - "+title);
		tb.setTitleAt(tb.getSelectedIndex(),title);
		browse.parseDocument(page,this);
		tb.updateUI();
		//tb.repaint();
		//tb.revalidate();
	}
	
	public void createMenu(){
		if(menuBar!=null)
			setJMenuBar(null);
		menuBar= new JMenuBar();
		JMenu menu,subMenu;
		JMenuItem menuItem;
		//System.out.println(
		menu=new JMenu("File");
		
		menuItem=new JMenuItem("Exit",ImageLoader.getImageIcon("images/exit.png"));
		menuItem.setMnemonic(KeyEvent.VK_Q);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);
		menuBar.add(menu);

		menu = new JMenu("Edit");

		menuItem=new JMenuItem("Copy",ImageLoader.getImageIcon("images/copy.png"));
		menuItem.setMnemonic(KeyEvent.VK_C);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);

		menuItem=new JMenuItem("Cut",ImageLoader.getImageIcon("images/cut.png"));
		menuItem.setMnemonic(KeyEvent.VK_X);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);

		menuItem=new JMenuItem("Paste",ImageLoader.getImageIcon("images/paste.png"));
		menuItem.setMnemonic(KeyEvent.VK_V);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);
		menuBar.add(menu);
		
		bookmarksMenu  = new JMenu("Bookmarks");
		menuItem = new JMenuItem("Store");
		bookmarksMenu.add(menuItem);
		menuItem.addActionListener(this);
		menuItem = new JMenuItem("Search");
		bookmarksMenu.add(menuItem);
		menuItem.addActionListener(this);
		bookmarksMenu.addSeparator();
		
		
		//add in user bookmarks
		ArrayList bookmarks = MyHacker.getBookmarks();
		Object[] marks = bookmarks.toArray();
		for(int i=0;i<marks.length;i++){
			Object[] mark = (Object[])marks[i];
			String domain = (String)mark[0];
			String name = (String)mark[1];
			String folder = (String)mark[2];
			menuItem = new JMenuItem(name);
			bookmarksMenu.add(menuItem);
			menuItem.addActionListener(this);
			menuItem.setActionCommand("mark-"+domain);
			
			
		}
		
		
		bookmarksMenu.addSeparator();
		menuItem = new JMenuItem("Add Bookmark");
		bookmarksMenu.add(menuItem);
		menuItem.setActionCommand("Bookmark");
		menuItem.addActionListener(this);
		menuItem = new JMenuItem("Manage Bookmarks");
		bookmarksMenu.add(menuItem);
		menuItem.addActionListener(this);

		
		menuBar.add(bookmarksMenu);

		this.setJMenuBar(menuBar);
	}
		

	public void populate(){
		//Menu
		createMenu();
		setLayout(new MigLayout("fill,wrap 2"));
		JButton button;
		//toolbar
		toolBar = new JToolBar("Tools");

		Insets insets = this.getInsets();
		Rectangle size = this.getBounds();
		
		button = new JButton(ImageLoader.getImageIcon("images/back.png"));
		button.setActionCommand("Back");
		button.setToolTipText("Go back to previous page.");
		button.addActionListener(this);
		toolBar.add(button);

		button = new JButton(ImageLoader.getImageIcon("images/forward.png"));
		button.setActionCommand("Forward");
		button.setToolTipText("Go forward to previous page.");
		button.addActionListener(this);
		toolBar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/browserhome.png"));
		button.setActionCommand("Home");
		button.setToolTipText("Go to your page.");
		button.addActionListener(this);
		toolBar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/refresh.png"));
		button.setActionCommand("Refresh");
		button.setToolTipText("Refresh this page.");
		button.addActionListener(this);
		toolBar.add(button);
		
		toolBar.addSeparator();
		button = new JButton(ImageLoader.getImageIcon("images/vote.png"));
		button.setActionCommand("Vote");
		button.setToolTipText("Vote for this page.");
		button.addActionListener(this);
		toolBar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/add.png"));
		button.setActionCommand("Bookmark");
		button.setToolTipText("Bookmark this page.");
		button.addActionListener(this);
		toolBar.add(button);
		
		toolBar.addSeparator();
		
		votesLeftLabel = new JLabel("Votes Remaining: "+MyHacker.getVotesLeft());
		toolBar.add(votesLeftLabel);
		
		Dimension dim = toolBar.getPreferredSize();
		toolBar.setBounds(insets.left,insets.top,size.width,dim.height);
		this.add(toolBar,"span,growx");
		int height=insets.top+dim.height;
		JPanel urlpanel = new JPanel();
		
		urlpanel.setBorder(new LeftRoundedBorder(true));
		int urlFieldSize = urlField.getPreferredSize().height;
		urlpanel.setLayout(new MigLayout("fillx, align leading,h n:n:"+(urlFieldSize+6)));
		JButton goButton = new JButton(ImageLoader.getImageIcon("images/right.png"));
		goButton.setBorderPainted(false);
		goButton.setContentAreaFilled(false);
		urlpanel.add(goButton,"y -5,align trailing,id button,h n:n:"+(urlFieldSize+6)+",x (visual.x2-40)" );
		//goButton.setBounds(size.width-260,2,20,20);
		goButton.setActionCommand("URL");
		goButton.addActionListener(this);
		urlpanel.add(urlField,"growx,pos 30 0 (visual.x2-42) pref.y2,h "+(urlFieldSize-1)+",h n:n:"+(urlFieldSize+6));
		//urlField.setBounds(30,2,size.width-320,urlField.getPreferredSize().height-1);
		//urlField.setBackground(new Color(0,0,0,0));
		urlField.setActionCommand("URL");
		urlField.addActionListener(this);
		
		//urlField.setBackground(getBackground());
		urlField.setBorder(null);
		//urlpanel.setBounds(insets.left,height,size.width-240,urlField.getPreferredSize().height+6);
		//System.out.println(urlField.getPreferredSize().height);
		add(urlpanel,"growx,h n:21:21");
		
		JPanel searchPanel = new JPanel();
		searchPanel.setBorder(new LeftRoundedBorder(false));
		searchPanel.setLayout(null);
		
		JTextField searchField = new JTextField("Search");
		searchPanel.add(searchField);
		//searchField.setBounds(10,2,150,searchField.getPreferredSize().height-1);
		searchField.addActionListener(this);
		searchField.setActionCommand("SearchField");
		searchField.setBorder(BorderFactory.createLineBorder(Color.black));
		add(searchField,"shp 50,align trailing,w 200:n:n,h n:21:21");
		searchField.setBounds(size.width-220,height,170,searchField.getPreferredSize().height+4);
		
		height+=urlField.getPreferredSize().height+10;
		
		/*browse = new JEditorPane();
		browse.setContentType("text/html");
		browse.addHyperlinkListener(this);
		browse.setEditable(false);
		browse.setBackground(new Color(255,255,255));*/
		browse = new HtmlHandler();
		//browse.createView();
		browse.setParent(this);
		//browse.setEditorKit(new HTMLEditorKit());
		try{
			Object[] params = new Object[]{"",""};
			String result = (String)XMLRPCCall.execute("http://"+MyView.getIP()+":8081/xmlrpc","hackerRPC.doSearch", params);
			browse.parseDocument(result,this);
		}catch(Exception e){
			e.printStackTrace();
		}
		urlField.setText("Search");
		ips[0]="Search";
		//System.out.println(MyHacker.getView().getIP());
		//forumPanel = new ForumPanel("http://"+MyHacker.getView().getIP()+"/forum/",this.getBounds(),this,MyHacker);
		
		tb = new JTabbedPane();
		JScrollPane scrollPane = new JScrollPane(browse.getView());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		tb.addTab("Site",browse.getView());
		JPanel panel = new JPanel();
		JLabel tlabel = new JLabel("Site");
		JButton tbutton = new JButton(ImageLoader.getImageIcon("images/delete.png"));
		tbutton.setBorderPainted(false);
		tbutton.setContentAreaFilled(false);
		panel.add(tlabel);
		panel.add(tbutton);
		//tb.setTabComponentAt(0,panel);
		//tb.addTab("Forum",forumPanel.getView());
		//add(tb,"grow,span,h 200:400:n");
		//tb.setBounds(insets.left,height,size.width-20,size.height-insets.top-dim.height-305);
		tbYPosition=height;
		height+=size.height-insets.top-dim.height-255;
		MyHacker.getPanel().add(EPU);
		MyHacker.getPanel().setComponentZOrder(EPU,0);
		EPU.setVisible(false);
		EPU.setOpaque(false);
		banking.setLayout(null);
		attack.setLayout(null);
		FTP.setLayout(null);
		watch.setLayout(null);
		firewall.setLayout(null);
		http.setLayout(null);
		cpu.setLayout(null);
		hd.setLayout(null);
		ram.setLayout(null);
		images.setLayout(null);
		games.setLayout(null);
		commodities.setLayout(null);
		redirect.setLayout(null);
		pci = new InventoryList();
		agp = new InventoryList();
		pci.setPaint(false);
		pci.setCount(InventoryList.STORE);
		agp.setPaint(false);
		agp.setCount(InventoryList.STORE);
		tabbedPane = new JTabbedPane();
		
		JScrollPane bankingScroll = new JScrollPane(banking);
		bankingScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane attackScroll = new JScrollPane(attack);
		attackScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane FTPScroll = new JScrollPane(FTP);
		FTPScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane watchScroll = new JScrollPane(watch);
		watchScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane firewallScroll = new JScrollPane(firewall);
		firewallScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane httpScroll = new JScrollPane(http);
		httpScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane imageScroll = new JScrollPane(images);
		imageScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane gamesScroll = new JScrollPane(games);
		gamesScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane agpScroll = new JScrollPane(agp);
		agpScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane pciScroll = new JScrollPane(pci);
		pciScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane commodityScroll = new JScrollPane(commodities);
		commodityScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane redirectScroll = new JScrollPane(redirect);
		redirectScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane cpuScroll = new JScrollPane(cpu);
		cpuScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane hdScroll = new JScrollPane(hd);
		hdScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane ramScroll = new JScrollPane(ram);
		ramScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane bountyScroll = new JScrollPane(bounty);
		bountyScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		tabbedPane.addTab("Banking",bankingScroll);
		tabbedPane.addTab("Attack",attackScroll);
		tabbedPane.addTab("FTP",FTPScroll);
		tabbedPane.addTab("Watch",watchScroll);
		tabbedPane.addTab("FW",firewallScroll);
		tabbedPane.addTab("HTTP",httpScroll);
		tabbedPane.addTab("Redirect",redirectScroll);
		//tabbedPane.addTab("Images",imageScroll);
		tabbedPane.addTab("Games",gamesScroll);
		tabbedPane.addTab("AGP",agpScroll);
		tabbedPane.addTab("PCI",pciScroll);
		tabbedPane.addTab("Commodities",commodityScroll);
		tabbedPane.addTab("CPU",cpuScroll);
		tabbedPane.addTab("HD",hdScroll);
		tabbedPane.addTab("Memory",ramScroll);
		tabbedPane.addTab("Bounties",bountyScroll);
		tabbedPane.setEnabledAt(tabbedPane.indexOfTab("CPU"),false);
		tabbedPane.setEnabledAt(tabbedPane.indexOfTab("HD"),false);
		tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Memory"),false);
		tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Bounties"),false);
		tabbedPane.setMinimumSize(new Dimension(0,0));
		//add(tabbedPane,"growx,span");
		//tabbedPane.setBounds(insets.left,height,size.width-20,size.height-height-20);
		//System.out.println(tabbedPane.getSize().height);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(tb);
		splitPane.setBottomComponent(tabbedPane);
		splitPane.setPreferredSize(new Dimension(780,400));
		splitPane.setOneTouchExpandable(true);
		splitPane.setBorder(null);
		splitPane.setContinuousLayout(true);
		add(splitPane,"grow,span");
		pack();
		splitPane.setDividerLocation(0.6);
	}
	
	public void setVotesLeft(int votesLeft){
		votesLeftLabel.setText("Votes Remaining: "+votesLeft);
	}
	
	public void setStore(Object[] directory){
		//System.out.println("Got Store");
		this.directory=directory;
		getStore();
		
	}
	
	public void goToStore(){
		removeProducts();
		MyHacker.setSiteRequest(Hacker.BROWSER);
		MyHacker.setRequestedDirectory(Hacker.BROWSER);
		urlField.setText(MyHacker.getStoreIP());
		position++;
		if(position==10)
			shiftIPS();
		ips[position]=MyHacker.getStoreIP();
		setLink(MyHacker.getStoreIP());
	}
	
	public void getStore(){
		sortProducts();
		removeProducts();
		for(int i =1;i<directory.length;i++){
			if(directory[i] instanceof HackerFile){
				HackerFile o = (HackerFile)directory[i];
				receivedFile(o);
			}
		}
	}
	
	public void sortProducts(){
		boolean changed=true;
		HackerFile temp;
		while(changed){
			changed=false;
			for(int i=1;i<directory.length-1;i++){
				if(directory[i] instanceof HackerFile){
					HackerFile blah = (HackerFile)directory[i]; 
					HackerFile blah2 = (HackerFile)directory[i+1];
					if(blah.getPrice()>blah2.getPrice()){
						temp =blah;  
						directory[i]=blah2; 
						directory[i+1]=temp; 
						changed=true; 
					}
				}
			}
		}	
	}
	
	public void removeProducts(){
		banking.removeAll();
		banking.repaint();
		attack.removeAll();
		attack.repaint();
		watch.removeAll();
		watch.repaint();
		FTP.removeAll();
		FTP.repaint();
		http.removeAll();
		http.repaint();
		firewall.removeAll();
		firewall.repaint();
		cpu.removeAll();
		cpu.repaint();
		hd.removeAll();
		hd.repaint();
		ram.removeAll();
		ram.repaint();
		images.removeAll();
		bounty.removeAll();
		games.removeAll();
		commodities.removeAll();
		redirect.removeAll();
		pci.reset();
		agp.reset();
		tabbedPane.setEnabledAt(tabbedPane.indexOfTab("CPU"),false);
		tabbedPane.setEnabledAt(tabbedPane.indexOfTab("HD"),false);
		tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Memory"),false);
		tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Bounties"),false);
		if(urlField.getText().length()>=5){
			if(urlField.getText().equals(MyHacker.getStoreIP())){
				tabbedPane.setEnabledAt(tabbedPane.indexOfTab("CPU"),true);
				tabbedPane.setEnabledAt(tabbedPane.indexOfTab("HD"),true);
				tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Memory"),true);
				tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Bounties"),true);
			}
		}
		
		count=0;
		bcount=0;
		acount=0;
		ftcount=0;
		ficount=0;
		wcount=0;
		hcount=0;
		ccount=0;
		hdcount=0;
		mcount=0;
		icount=0;
		bocount=0;
		gcount=0;
		commodityCount=0;
		redirectCount=0;
	}
	
	public void receivedFile(HackerFile HF){
		//System.out.println("Received "+HF.getName());
		JPanel change=null;
		int x=0;
		JPanel add = getPanel(HF.getName(),HF.getPrice(),HF.getMaker(),HF.getCPUCost(),HF.getQuantity(),HF.getDescription(),HF.getType(),HF.getContent());
		if(HF.getType()==HackerFile.BANKING_COMPILED){
			change=banking;
			bcount+=20+add.getPreferredSize().width;
			x=bcount-add.getPreferredSize().width;
			
		}
		if(HF.getType()==HackerFile.ATTACKING_COMPILED){
			change=attack;
			acount+=20+add.getPreferredSize().width;
			x=acount-add.getPreferredSize().width;
			
		}
		if(HF.getType()==HackerFile.WATCH_COMPILED){
			change=watch;
			wcount+=20+add.getPreferredSize().width;
			x=wcount-add.getPreferredSize().width;
			
		}
		if(HF.getType()==HackerFile.FTP_COMPILED){
			change=FTP;
			ftcount+=20+add.getPreferredSize().width;
			x=ftcount-add.getPreferredSize().width;
			
		}
		if(HF.getType()==HackerFile.SHIPPING_COMPILED){
			change=redirect;
			redirectCount+=20+add.getPreferredSize().width;
			x=redirectCount-add.getPreferredSize().width;
			
		}
		if(HF.getType()==HackerFile.NEW_FIREWALL){
			change=firewall;
			ficount+=20+add.getPreferredSize().width;
			x=ficount-add.getPreferredSize().width;
			
		}
		if(HF.getType()==HackerFile.HTTP){
			change=http;
			hcount+=20+add.getPreferredSize().width;
			x=hcount-add.getPreferredSize().width;
			
		}
		if(HF.getType()==HackerFile.CPU){
			change=cpu;
			ccount+=20+add.getPreferredSize().width;
			x=ccount-add.getPreferredSize().width;
		}
		if(HF.getType()==HackerFile.HD){
			change=hd;
			hdcount+=20+add.getPreferredSize().width;
			x=hdcount-add.getPreferredSize().width;
		}
		if(HF.getType()==HackerFile.MEMORY){
			change=ram;
			mcount+=20+add.getPreferredSize().width;
			x=mcount-add.getPreferredSize().width;
		}
		if(HF.getType()==HackerFile.IMAGE){
			change=images;
			x=icount;
			icount+=20+add.getPreferredSize().width;
		}
		if(HF.getType()==HackerFile.BOUNTY){
			change=bounty;
			x=bocount;
			bocount+=20+add.getPreferredSize().width;
		}
		if(HF.getType()==HackerFile.GAME){
			change=games;
			x=gcount;
			gcount+=20+add.getPreferredSize().width;
		}
		if(HF.getType()==HackerFile.COMMODITY_SLIP){
			change=commodities;
			x=commodityCount;
			commodityCount+=20+add.getPreferredSize().width;
		}
			
		if(HF.getType()==HackerFile.AGP){
			String name = HF.getDescription();
			
			Inventory inv = Equipment.getInventoryObject(HF,100,MyHacker,EPU);//new Inventory(MyHacker,EPU,name,value1,value2,type,Inventory.AGP,HF.getName(),100,d,new int[]{0,0,0,0,0});
			float price = HF.getPrice();
			price-=price*((MyHacker.getStatsPanel().getMerchantingIcon().getLevel()-50.0f)/100.0f);
			inv.setPrice(price);
			inv.setIP(urlField.getText());
			agp.addInventory(inv);
			
		}
		if(HF.getType()==HackerFile.PCI){
			String name = HF.getDescription();
			
			Inventory inv = Equipment.getInventoryObject(HF,100,MyHacker,EPU);//new Inventory(MyHacker,EPU,name,value1,value2,type,Inventory.PCI,HF.getName(),100,d,new int[]{0,0,0,0,0});
			float price = HF.getPrice();
			price-=price*((MyHacker.getStatsPanel().getMerchantingIcon().getLevel()-50.0f)/100.0f);
			inv.setPrice(price);
			inv.setIP(urlField.getText());
			pci.addInventory(inv);
			
		}
		
		if(change!=null){
			change.add(add);
			add.setBounds(x,2,add.getPreferredSize().width,add.getPreferredSize().height);
			change.setPreferredSize(new Dimension(x+20+add.getPreferredSize().width,add.getPreferredSize().height));
		}
		//System.out.println(add.getPreferredSize().height);
		//getStore();
		
	}
	public JPanel getPanel(String name,float price,String maker,float cpu,int quantity,String description,int type,HashMap HM){
		JPanel jp = new JPanel();
		jp.setLayout(null);
		Insets insets = jp.getInsets();
		int largest=0;
		int height = 0;
		if(type!=HackerFile.NEW_FIREWALL){
			JLabel namel = new JLabel("Name:     "+name);
			jp.add(namel);
			namel.setBounds(insets.left,insets.top,namel.getPreferredSize().width,namel.getPreferredSize().height);
			height = insets.top+namel.getPreferredSize().height+2;
			largest = namel.getPreferredSize().width;
			
		}
		else{
			FireWallLabel namel = new FireWallLabel(name,MyHacker);
			namel.setContent(HM);
			jp.add(namel);
			namel.setBounds(insets.left,insets.top,namel.getPreferredSize().width,namel.getPreferredSize().height);
			height = insets.top+namel.getPreferredSize().height+2;
			largest = namel.getPreferredSize().width;
		}
		
		
		
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		price-=price*((MyHacker.getStatsPanel().getMerchantingIcon().getLevel()-50.0f)/100.0f);
		JLabel pricel = new JLabel("Price:   "+nf.format(price));
		jp.add(pricel);
		pricel.setBounds(insets.left,height,pricel.getPreferredSize().width,pricel.getPreferredSize().height);
		height+=pricel.getPreferredSize().height+2;
		if(pricel.getPreferredSize().width>largest)
			largest=pricel.getPreferredSize().width;
		String level="0";
		int currentLevel=0;
		int leveli=0;
		if(type!=HackerFile.FIREWALL&&type!=HackerFile.CPU&&type!=HackerFile.HD&&type!=HackerFile.MEMORY){
			JLabel makerl = new JLabel("Maker:    "+maker);
			jp.add(makerl);
			makerl.setBounds(insets.left,height,makerl.getPreferredSize().width,makerl.getPreferredSize().height);
			height+=makerl.getPreferredSize().height+2;
			if(makerl.getPreferredSize().width>largest)
				largest=makerl.getPreferredSize().width;
		}
		else if(urlField.getText().equals(MyHacker.getStoreIP())){
			JLabel levell;
			
			//System.out.println(level);
			if(type==HackerFile.FIREWALL){
			    level = (String)HM.get("level");
			    leveli = (int)Integer.valueOf(level);
				currentLevel = MyHacker.getStatsPanel().getFireWallIcon().getLevel();
				levell = new JLabel("FireWall Level Required: "+level);
			}
			else{
				level = (String)HM.get("level");
				leveli = (int)Integer.valueOf(level);
				currentLevel = MyHacker.getStatsPanel().getTotalLevel();
				levell = new JLabel("Total Level Required: "+level);
			}
			jp.add(levell);
			levell.setBounds(insets.left,height,levell.getPreferredSize().width,levell.getPreferredSize().height);
			height+=levell.getPreferredSize().height+2;
			if(levell.getPreferredSize().width>largest)
				largest=levell.getPreferredSize().width;
				
		}
		if(type!=HackerFile.CPU&&type!=HackerFile.HD&&type!=HackerFile.MEMORY&&type!=HackerFile.COMMODITY_SLIP){
			JLabel cpul = new JLabel("CPU Cost: "+cpu);
			jp.add(cpul);
			cpul.setBounds(insets.left,height,cpul.getPreferredSize().width,cpul.getPreferredSize().height);
			height+=cpul.getPreferredSize().height+2;
			if(cpul.getPreferredSize().width>largest)
				largest=cpul.getPreferredSize().width;
		}
		JLabel quantityl = new JLabel("Quantity: ");
		jp.add(quantityl);
		quantityl.setBounds(insets.left,height,quantityl.getPreferredSize().width,quantityl.getPreferredSize().height);
		if(quantity==-1)
			quantity=1;
		SpinnerModel model = new SpinnerNumberModel(1,1,quantity,1);
		JSpinner spinner = new JSpinner(model);
		jp.add(spinner);
		spinner.setBounds(insets.left+2+quantityl.getPreferredSize().width,height,spinner.getPreferredSize().width,spinner.getPreferredSize().height);
		JLabel quantityLabel = new JLabel("/"+quantity);
		jp.add(quantityLabel);
		quantityLabel.setBounds(insets.left+4+quantityl.getPreferredSize().width+spinner.getPreferredSize().width,height,quantityLabel.getPreferredSize().width,quantityLabel.getPreferredSize().height);
		height+=quantityl.getPreferredSize().height+2;
		if((quantityl.getPreferredSize().width+spinner.getPreferredSize().width+quantityLabel.getPreferredSize().width)>largest)
			largest=quantityl.getPreferredSize().width+spinner.getPreferredSize().width+quantityLabel.getPreferredSize().width;
		
		JButton button = new JButton("Buy");
		if(currentLevel<leveli)
			button.setEnabled(false);
		jp.add(button);
		button.addActionListener(new StoreActionListener(MyHacker,name,spinner,urlField.getText(),this));
		button.setBounds(insets.left,height,button.getPreferredSize().width,button.getPreferredSize().height);
		height+=button.getPreferredSize().height+2;
		
		JTextArea ta = new JTextArea(description);
		ta.setEditable(false);
		ta.setBackground(jp.getBackground());
		ta.setLineWrap(true);
		ta.setColumns(10);
		ta.setWrapStyleWord(true);
		//System.out.println(ta.getPreferredSize().width+"  "+(insets.left+2+quantityl.getPreferredSize().width));
		jp.add(ta);
		ta.setBounds(insets.left+2+largest,insets.top,ta.getPreferredSize().width,height);
		Dimension d = new Dimension(insets.left+ta.getPreferredSize().width+largest,height);
		jp.setPreferredSize(d);
		//System.out.println(d.width);
		return(jp);

	}		
	
	public void newSearch(String value,String page){
		//System.out.println("Form Input Name: "+name);
		//System.out.println("Form Input Value: "+value);
		//value = HTMLFilter.getURLSafe(value);
		try{
			//browse.parseDocument(new URL("http://"+MyHacker.getView().getIP()+":8081/login.html?mode=search&query="+value));
			//XML-RPC STUFF
			//value = value.replaceAll("%20"," ");
			Object[] params = new Object[]{value,page};
			String result = (String)XMLRPCCall.execute("http://"+MyView.getIP()+":8081/xmlrpc","hackerRPC.doSearch", params);
			//System.out.println("Parsing new search -- "+result);
			tb.setTitleAt(tb.getSelectedIndex(),"Search");
			browse.parseDocument(result,this);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void submitForm(FormInput[] formInputs){
		//System.out.println("Submitting Form");
		HashMap send = new HashMap();
		for(int i=0;i<formInputs.length;i++){
			//System.out.println(formInputs[i].getName()+"   "+formInputs[i].getValue());
			send.put(formInputs[i].getName(),formInputs[i].getTextValue());
		}
		Object[] params = new Object[]{urlField.getText().split("\\?")[0].trim()};
		String result = (String)XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/domain.php","domainLookup", params);
		Object objects[] = new Object[]{result,MyHacker.getEncryptedIP(),send};
		MyView.setFunction("submit");
		MyView.addFunctionCall(new RemoteFunctionCall(Hacker.BROWSER,"submit",objects));
		
	}
	
	public void setLink(URL href){
		//String link = 
		//System.out.println(href);
		String link=href.toString();
		String[] queries = link.split("\\?");
		//System.out.println(link);
		if(link.equals("http://hackwars.net/clue")){
			//System.out.println("clue "+ips[position]);
			Object[]clueobjects = new Object[]{MyHacker.getEncryptedIP(),ips[position]};
			MyView.setFunction("cluedata");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"cluedata",clueobjects));
			return;
		}
			
		//System.out.println("{"+href+"}");
		if(!href.getPath().replaceAll("/","").toLowerCase().equals("")){
		//	System.out.println("Clicked--"+href.getPath().replaceAll("/",""));
			removeProducts();
			
			
			if(MyView!=null){
				count=0;
				bcount=0;
				acount=0;
				ftcount=0;
				ficount=0;
				wcount=0;
				hcount=0;
				mcount=0;
				icount=0;
				bocount=0;
				gcount=0;
				MyHacker.setSiteRequest(Hacker.BROWSER);
				MyHacker.setRequestedDirectory(Hacker.BROWSER);
				
				urlField.setText(href.getPath().replaceAll("/",""));
				position++;
				if(position==10){
					shiftIPS();
				}
				Object objects[];
				if(!ips[position-1].toLowerCase().equals("search")&&!ips[position-1].equals(MyHacker.getStoreIP())){
					Object[] params = new Object[]{ips[position-1].split("\\?")[0].trim()};
					String result = (String)XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/domain.php","domainLookup", params);
					objects = new Object[]{result,MyHacker.getEncryptedIP()};
					MyView.setFunction("exit");
					MyView.addFunctionCall(new RemoteFunctionCall(0,"exit",objects));
				}
				HashMap HM = new HashMap();
				ips[position]=href.getPath().replaceAll("/","");
				String q = href.getQuery();
				if(q!=null){
					String[] vars = q.split("\\&");
					
					for(int i=0;i<vars.length;i++){
						String[] query = vars[i].split("=");
						HM.put(query[0],query[1]);
						//System.out.println(query[0]+"   "+query[1]);
					}
				}
				Object[] params = new Object[]{href.getPath().replaceAll("/","").trim(),HM};
				String result = (String)XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/domain.php","domainLookup", params);
				//System.out.println(HM.get("q"));
				objects = new Object[]{result.trim(),MyHacker.getEncryptedIP(),HM};
				MyView.setFunction("requestwebpage");
				MyView.addFunctionCall(new RemoteFunctionCall(Hacker.BROWSER,"requestwebpage",objects));
			}
		}
		else{
			String[] vars = href.getQuery().split("\\&");
			String[] query = vars[0].split("=");
			String[] page = vars[1].split("=");
			try{
				newSearch(query[1],page[1]);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	public void setLink(String link){
		removeProducts();
		if(MyView!=null){
			count=0;
			bcount=0;
			acount=0;
			ftcount=0;
			ficount=0;
			wcount=0;
			hcount=0;
			mcount=0;
			icount=0;
			bocount=0;
			gcount=0;
			MyHacker.setSiteRequest(Hacker.BROWSER);
			MyHacker.setRequestedDirectory(Hacker.BROWSER);
			
			urlField.setText(link);
			if(!link.toLowerCase().equals("search")){
				Object[] objects;
				if(position!=0){
					if(!ips[position-1].toLowerCase().equals("search")&&!ips[position-1].equals(MyHacker.getStoreIP())){
						Object[] params = new Object[]{ips[position-1].split("\\?")[0].trim()};
						String result = (String)XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/domain.php","domainLookup", params);
						objects = new Object[]{result,MyHacker.getEncryptedIP()};
						MyView.setFunction("exit");
						MyView.addFunctionCall(new RemoteFunctionCall(0,"exit",objects));
					}
				}
				URL href=null;
				HashMap HM = new HashMap();
				try{
					href = new URL("http://"+link);
				}catch(Exception e){
					//System.out.println("malformed URL");
				}
				//System.out.println(href);
				if(href!=null){
					String q = href.getQuery();
					//System.out.println(q);
					if(q!=null){
						String[] vars = q.split("\\&");
						for(int i=0;i<vars.length;i++){
							String[] query = vars[i].split("=");
							HM.put(query[0],query[1]);
							//System.out.println(query[0]+"   "+query[1]);
						}
					}
				}
				//System.out.println(href);
				Object[] params = new Object[]{href.getHost().replaceAll("/","").trim()};
				String result = (String)XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/domain.php","domainLookup", params);
				objects = new Object[]{result,MyHacker.getEncryptedIP(),HM};
				MyView.setFunction("requestwebpage");
				MyView.addFunctionCall(new RemoteFunctionCall(Hacker.BROWSER,"requestwebpage",objects));
			}
			else{
				newSearch("","");
			}
			
		}
	}
	
	public void shiftIPS(){
		for(int i=1;i<10;i++){
			ips[i-1]=ips[i];
		}
		position=9;
	}

	public void removeBookmark(int pos){
		//System.out.println("Removing Position :"+(pos+3));
		bookmarksMenu.remove(pos+3);
		
	}
	
	public void editBookmark(int pos,String name){
		JMenuItem menuItem = bookmarksMenu.getItem(pos+3);
		menuItem.setText(name);
	}

	
	public void internalFrameClosed(InternalFrameEvent e) {
		MyHacker.setScriptEditorOpen(false);

	}
	
	
	public void actionPerformed(ActionEvent e){
		String ac = e.getActionCommand();
		
		if(ac.equals("Exit")){
			doDefaultCloseAction();
		}
		if(ac.equals("URL")){
			removeProducts();
			ip = urlField.getText();
			if(MyView!=null){
				count=0;
				bcount=0;
				acount=0;
				ftcount=0;
				ficount=0;
				wcount=0;
				hcount=0;
				mcount=0;
				icount=0;
				bocount=0;
				gcount=0;
				MyHacker.setSiteRequest(Hacker.BROWSER);
				MyHacker.setRequestedDirectory(Hacker.BROWSER);
				position++;
				if(position==10)
					shiftIPS();
				ips[position]=ip;
				setLink(ip);
				//removeProducts();
				
			}
				
		}
		if(ac.equals("Store")){
			removeProducts();
			MyHacker.setSiteRequest(Hacker.BROWSER);
			MyHacker.setRequestedDirectory(Hacker.BROWSER);
			urlField.setText(MyHacker.getStoreIP());
			position++;
			if(position==10)
				shiftIPS();
			ips[position]=MyHacker.getStoreIP();
			setLink(MyHacker.getStoreIP());
		}
		if(ac.toLowerCase().equals("search")){
			removeProducts();
			position++;
			if(position==10)
				shiftIPS();
			ips[position]="Search";
			newSearch("","");
			urlField.setText("Search");
		}
		if(ac.equals("Back")){
			if(tb.getSelectedIndex()==0){
				if(position!=0){
					position--;
					setLink(ips[position]);
				}
			}
			if(tb.getSelectedIndex()==1){
				forumPanel.goBack();
			}
		}
		if(ac.equals("Forward")){
			if(tb.getSelectedIndex()==0){
				if(position!=10&&ips[position+1]!=null){
					position++;
					setLink(ips[position]);
				}
			}
			if(tb.getSelectedIndex()==1){
				forumPanel.goForward();
			}
		}
		if(ac.equals("Refresh")){
			MyHacker.setSiteRequest(Hacker.BROWSER);
			MyHacker.setRequestedDirectory(Hacker.BROWSER);
			setLink(ips[position]);
			
		}
		if(ac.equals("Home")){
			//System.out.println("Going Home");
			position++;
			if(position==10)
				shiftIPS();
			ips[position]=MyHacker.getIP();
			urlField.setText(MyHacker.getIP());
			removeProducts();
			MyHacker.setSiteRequest(Hacker.BROWSER);
			MyHacker.setRequestedDirectory(Hacker.BROWSER);
			setLink(MyHacker.getIP());
			
		}
		if(ac.equals("Vote")){
			Object[] params = new Object[]{urlField.getText().trim()};
			String result = (String)XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/domain.php","domainLookup", params);
			Object[] objects ={result,MyHacker.getEncryptedIP()};
			MyView.setFunction("vote");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"vote",objects));
		}
		
		if(ac.equals("Bookmark")){
			if(urlField.getText().indexOf(".hw")!=-1||MyHacker.getProPack()){
				
				
				String s = (String)JOptionPane.showInputDialog(
						this,
						"Name:",
						currentTitle);
				
				if(s!=null){
					Object[] params = new Object[]{MyHacker.getIP(),urlField.getText(),s,""};
					String id = (String)XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/bookmarks.php","addBookmark",params);
					
					//createMenu();
					JMenuItem menuItem = new JMenuItem(s);
					bookmarksMenu.add(menuItem,bookmarksMenu.getItemCount()-3);
					menuItem.addActionListener(this);
					menuItem.setActionCommand("mark-"+urlField.getText());
					
					MyHacker.addBookmark(urlField.getText(),s,"",id);
				}
			}
			else{
				MyHacker.showMessage("As a free account, you can only bookmark upgraded accounts (.hw sites).");
			}
			
		}
		else if(ac.equals("Manage Bookmarks")){
			ManageBookmarks mb = new ManageBookmarks(MyHacker,this);
			MyHacker.getPanel().add(mb);
		    mb.setBounds(50,50,300,300);
		    mb.setVisible(true);
			
			
		}
		else if(ac.indexOf("mark")!=-1&&!ac.equals("Bookmark")){
			String[] split = ac.split("-");
			String domain = split[1];
			position++;
			if(position==10)
				shiftIPS();
			ips[position]=domain;
			urlField.setText(domain);
			removeProducts();
			MyHacker.setSiteRequest(Hacker.BROWSER);
			MyHacker.setRequestedDirectory(Hacker.BROWSER);
			setLink(domain);
			
		}
		else if(ac.equals("SearchField")){
			JTextField tf = (JTextField)e.getSource();
			String search = tf.getText();
			removeProducts();
			position++;
			if(position==10)
				shiftIPS();
			ips[position]="Search";
			newSearch(search,"");
			urlField.setText("Search");
		}
	}
	
	 public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
        
    }

    public void componentResized(ComponentEvent e) {
		//browse.getView().validate();
		//browse.getView().repaint();
		//browse.getView().addNotification(new DocumentNotification(DocumentNotification.GENERIC, null));//setPreferredWidth(tb.getWidth()-10);
		validate();
		repaint();
	   /* Insets insets = this.getInsets();
	    Rectangle frameSize = this.getBounds();
	    Dimension frameBounds = this.getSize();
	    tb.setBounds(insets.left,tbYPosition,frameSize.width-20,frameSize.height-insets.top-305);
	    tabbedPane.setBounds(insets.left,tbYPosition+tb.getSize().height,frameSize.width-20,200);
	    Dimension dim = toolBar.getPreferredSize();
	    toolBar.setBounds(insets.left,insets.top,frameSize.width,dim.height);
	    String link=ips[position];*/
	    /*if(!link.toLowerCase().equals("search")){
			setLink(link);
		}
		else{
			newSearch("","");
		}*/
		//forumPanel.resize();
            //panel.setBounds(0,0,frameSize.width,frameSize.height-50);
    }

    public void componentShown(ComponentEvent e) {

    }
	
}


