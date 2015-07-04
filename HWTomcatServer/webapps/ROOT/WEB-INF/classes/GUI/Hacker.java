package GUI;
/**
Hacker.java

main file for front end.
 */
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.Toolkit;
import View.*;
import java.awt.geom.Point2D;
import java.util.*;
import Assignments.*;
import Game.*;
import java.text.*;
import javax.imageio.*;
import java.net.URL;
import java.io.*;
import chat.client.*;
import chat.messages.*;
import Game.MMO.*;
import javax.swing.JInternalFrame.JDesktopIcon;
import net.miginfocom.swing.*;
import Hacktendo.*;

public class Hacker implements ActionListener,WindowListener,ComponentListener{
	//data
	//List of constants for determining what application is requesting stuff

	private HacktendoMMO HMMO=null;
     public final static int SCRIPT_EDITOR=0;
     public final static int HOME=1;
     public final static int BROWSER=2;
     public final static int FILE_CHOOSER=3;
     public final static int INSTALL_FILE_CHOOSER=4;
     public final static int ATTACK_FILE_CHOOSER=5;
     public final static int WEBSITE_EDITOR=6;
     public final static int FIREWALL_FILE_CHOOSER=7;
     public final static int FTP=8;
     public final static int WATCH_INSTALL_FILE_CHOOSER=9;
     public final static int SHOW_CHOICES_FILE_CHOOSER=10;
     public final static int IMAGE_VIEWER=11;
     public final static int BOUNTY_FILE_CHOOSER=12;
     public final static int EQUIPMENT=13;
	 public final static int HACKTENDO_CREATOR=14;
	 public final static int HACKTENDO_PLAYER=15;
	 public final static int HACKTENDO_FILE_CHOOSER=16;
	 public final static int COMMAND_PROMPT=17;
	 public final static int FILE_PROPERTIES=18;
	 public final static int FIREWALL_BROWSER=19;
	 
	 //message types
	public static final int GAME_MESSAGE = 0;
	public static final int POPUP_ERROR = 1;
	public static final int POPUP_MESSAGE = 2;
	public static final int ATTACK_MESSAGE = 3;
	public static final int REDIRECT_MESSAGE = 4;
	 
     private JFrame frame;// = new JFrame("Hacker");  //main frame
     private Desktop panel = new Desktop();  //panel that all applications are on
     private Color desktopColour;  ///background colour of main panel.
     //whether specified application is open or not.
     private boolean FTPOpen=false,depositOpen=false,transferOpen=false,withdrawOpen=false;
     private boolean scriptEditorOpen=false,messagerOpen=false,attackOpen=false,scanOpen=false;
     private boolean colourOpen=false,portManagementOpen=false,homeOpen=false,watchOpen=false;
     private static boolean preferencesOpen = false;
     private boolean messageWindowOpen=false;
     private HashMap icons = new HashMap();  //list of all desktop icons...probably going to get rid of this
     private JPopupMenu popUp;  //pop up menu for right clicking on main panel...also probably going to get rid of
     private float pettyCash,bankMoney; //money in petty cash and bank
     private View MyView; //View that is running this and connecting to the server
     private int xpTable[] = new int[100];  //table of experience values for each level
     private StatIcon MerchantingIcon,AttackIcon,WatchIcon,ScanIcon,FireWallIcon,HTTPIcon; //icons for showing levels for each stat
     private TotalLevelIcon TotalIcon; //total level icon
     private CPULoadIcon CPUIcon; //cpu load icon
     private Object currentDirectory[],secondaryDirectory[]; //directory that is currently being served. Store or Public directory in FTP
     private PacketPort ports[]; //ports used in Port Management
     private PortManagement portManagement; //port Management window
     private MessageWindow MyMessageWindow; //message window for displaying errors.
     private HashMap MyAttackPane = new HashMap(); //attack pane windows for each port.
	 //private HashMap MyShippingPane[] = new AttackPane[32]; //attack pane windows for each port.
     private int defaultAttackPort=3,defaultFTPPort=4,defaultBankPort=0,defaultHTTPPort=0,defaultRedirectPort=0; //default port values for applications
     private ScriptEditor MyScriptEditor=null; //script editor
     private String currentFolder,secondaryFolder; //current folder being displayed.
     private HackerFileChooser fileChooser;  //file chooser used in script editor
     private AttackFileChooser attackFileChooser; //file chooser used in attack panes
     private String ip,username;  //ip and username of player. 
     private WebsiteEditor MyWebsiteEditor=null;  //website editor application
     private InstallFileChooser installFileChooser;  //file chooser used in port management
     private Home MyHome=null;  //home application
     private int fileRequest=0,directoryRequest=0,siteRequest;  //which application requested a file/directory/site.
     private WebBrowser MyWebBrowser=null;  //web browser application.
     private FireWallFileChooser firewallFileChooser;  //file chooser used in port management for installing a firewall.
     private WatchInstallFileChooser watchInstallFileChooser; //file chooser used in watch manager for installing a watch.
     private ShowChoicesFileChooser showChoicesFileChooser;
     private HacktendoFileChooser hacktendoFileChooser;
     private JLabel money; //money label on main panel.
     private FTP MyFTP=null;  //FTP program
     private PortScan MyPortScan=null;  //port scan program
     private WatchManager MyWatchManager=null;  //watch manager program.
     private int HDType=0,CPUType=0,HDQuantity=0,memoryType=0;  //hard drive type, cpu type, and amount of files in the hard drive.
     private PeekAtCode peekAtCode;
     private ImageViewer MyImageViewer;
     private JTextArea message;
     private boolean loading=true;
     private StatsPanel SP;
     private int hackCount=-1,countDown=-1,voteCount=-1;
     private CountDown countDownLabel;
     private boolean counting=false;
     private int serverLoad=-1;
     private LogWindow MyLogWindow=null;
     private PersonalSettings MyPersonalSettings=null;
	 private OptionPanel options=null;
     private HashMap zombieAttacks=new HashMap();
     private viewMain MyViewMain=null;
     private ChatController MyChatController=null;
     private int height=768,width=1024;
     private String encryptedIP=null;
     private viewRelationList MyViewRelation=null;
     private TutorialWindow tutorialWindow=null;
     private BountyFileChooser bountyFileChooser=null;
     //Panel Holding Classes for non essentials
     private Help MyHelp = null;
     private JLabel networkLabel;
     private NetworkButton root,science,philosophy,physics,drama,literature,economics,arts;
     private Equipment EQ;
     private int HDMax;
     private HomeIcon requestedFileIcon=null;
     private MapPanel MP;
     private OptionPanel optionPanel;
     private StatsList statList;
     //private boolean showAttack=true,showScan=true,showDetails=true;
     private boolean[] portColumns;
     private HashMap directoryRequests = new HashMap();
     private int showChoices = 100;
	 private HacktendoPlayer hacktendoPlayer=null;
	 private HacktendoCreator hacktendoCreator=null;
	 private boolean paidHacktendo=true;
	 private ArrayList bookmarks = new ArrayList();
	 private boolean proPack = false;
	 private int ductTape=0,germanium=0,silicon=0,ybco=0,plutonium=0;
	 private String storeIP="";
     private Withdraw withdrawWindow;
     private Transfer transferWindow;
     private Deposit depositWindow;
	 private float healDiscount=1.0f;
	 private JMenuBar menuBar;
	 private TaskBar taskBar = null;
	 private int iconCount = 0;
	 private CommandPrompt commandPromptRequestedDirectory;
	 private boolean offline;
	 private FileProperties fileProperties;
     private HashMap preferences; // user preferences
     private boolean isOnLoad = true; // used to determine if the log window should be shown on load
     private int votesLeft = 0;
	 private FirewallBrowser firewallBrowser;
	 private Object[] healthInfo = new Object[32];
     //private ChatResizeLine sep;
     //constructor
     public Hacker(View MyView,String username,String ip,boolean npc,String encryptedIP,boolean offline) {
		ImageLoader.init();
		Application.setHacker(this);
		this.MyView = MyView;
		this.encryptedIP=encryptedIP;
		//System.out.println("Starting Hacker");
		frame = new JFrame("Hack Wars");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(null);
		frame.pack();
		frame.setLocation(50,50);
		frame.setSize(1024,600);
		frame.setResizable(true);
		frame.addWindowListener(this);
		frame.addComponentListener(this);
		//frame.setMimimumSize(new Dimension(800,600));
		//frame.setDefaultLookAndFeelDecorated(false);
		try{
			File fi = ImageLoader.getFile("images/logoback.png");
			panel.setLogo(ImageIO.read(fi.toURL()));
		}catch(Exception e){e.printStackTrace();}
		Image im = ImageLoader.getImageIcon("images/HackerIcon.png").getImage();
		frame.setIconImage(im);
		panel.setLayout(null);
		Insets insets = panel.getInsets();
		Color c = new Color(41,42,41);
		desktopColour=c;
		panel.setBackground(c);
		Insets frameInsets = frame.getInsets();
		Rectangle frameSize = frame.getBounds();
		Dimension frameBounds = frame.getSize();
		//System.out.println(frameSize.width+"  "+frameSize.height);
		MyMessageWindow = new MessageWindow(panel,this,frameSize.width,frameSize.height,c);
		//panel.add(MyMessageWindow);
		try{
            // just create the chat controller, nothing more
			MyChatController = new ChatController(username.toLowerCase(),this);
            
            // set up main GUI panel, add a tab placeholder to it for the channels, setup a bunch of listeners, and associate the chat controller you just created with the "viewMain" (bs naming convention)
            //viewMain impmlements absViewChannelList
			MyViewMain = new viewMain(MyChatController,new Dimension(frameSize.width,160));
            
            // This creates the first two  channels, General and Chat Messages
			MyChatController.setChannelList(MyViewMain);
            
			//MyViewRelation = new viewRelationList(MyChatController);
			//MyChatController.setRelationList(MyViewRelation);
		} catch(Exception e) {
            e.printStackTrace();
        }
		panel.add(MyViewMain);
		MyViewMain.setBounds(0,frameSize.height-210,frameSize.width-50,160);
		//sep = new ChatResizeLine(MyViewMain);
		//panel.add(sep);
		//sep.setForeground(Color.white);
		//sep.setBounds(0,frameSize.height-212,frameSize.width,5);
		//MyMessageWindow.setBounds(0,frameSize.height-170,frameSize.width,170);
		//Starting Up stuff.
		this.username=username;
		this.ip=ip;
		frame.setTitle("Hack Wars - "+ip);  //sets the title of the program
		//LoadingScreen LS = new LoadingScreen();
		//frame.add(LS);
		//LS.setBounds(200,200,200,150);
		//frame.repaint();
		//LS.repaint();
		//get Function Packs here	
		if(!offline){
			Object[] functions = (Object[])XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/functions.php","getFunctionPacks",new Object[]{ip});
			//paidHacktendo=(Boolean)functions[1];
			proPack = (Boolean)functions[2];
		}
		else{
			proPack = false;
		}
		//proPack=false;

		frame.add(panel);
		panel.setBounds(0,0,frameSize.width-50,frameSize.height-50);
		createMenu();  //creates the menu to be displayed in the main frame
		//System.out.println("Creating XP Table");
		//LS.change("Creating XP Table");
		createXPTable();  //fills in the experience table.
		//System.out.println("XP Table Created");
		startHome();  //creates the file browser.
		//System.out.println("Home Directory Created");
		//startFTP();  //creates tht ftp program.
		
		
		//request the main directory.
		/*Object objects[] = {ip,""};
		if(MyView!=null){
			MyView.addFunctionCall(new RemoteFunctionCall(0,"requestdirectory",objects));
		}*/
		currentFolder="";
		
		
		int totalLevel =0;
		
		//add all of the level icons to the main panel.
		//System.out.println("Creating Desktop Icons");
		//LS.change("Creating Desktop Icons");
		SP = new StatsPanel(panel,this,320,325,frameBounds.width-335,1,c,MerchantingIcon,AttackIcon,WatchIcon,ScanIcon,FireWallIcon,TotalIcon,CPUIcon,money,HTTPIcon);
		panel.add(SP);
		//LS.change("Done");
		//frame.remove(LS);
		networkLabel = new JLabel("");
		networkLabel.setForeground(Color.white);
		panel.add(networkLabel);
		networkLabel.setBounds(90,1,1,1);
		networkLabel.setVisible(false);
		/*try{
			File fi = ImageLoader.getFile("images/map.png");
			//panel.setMap(ImageIO.read(fi.toURL()));
			MP = new MapPanel(panel,this,121,157,1,1,ImageIO.read(fi.toURL()),c);
		}catch(Exception e){e.printStackTrace();}*/
		/*if(MP!=null)
			panel.add(MP);*/
		loading=false;
		
		countDownLabel = new CountDown(this);
		countDownLabel.setText("<html><font color=\"#FFFFFF\">Server Shutdown in 2:59</font></html>");
		panel.add(countDownLabel);
		countDownLabel.setBounds(frameSize.width-countDownLabel.getPreferredSize().width-30,frameSize.height-210-countDownLabel.getPreferredSize().height,countDownLabel.getPreferredSize().width,countDownLabel.getPreferredSize().height);
		countDownLabel.setVisible(false);
			frame.setVisible(true);
		MyView.finishedLoading();
		statList = new StatsList(this);
		
		if(!offline){
			Object[] settings = (Object[])XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/settings.php","getSettings",new Object[]{ip});
			if(settings!=null){
				Object[] portC = (Object[])settings[4];//new Object[]{true,true,true,true,true,true,true,true,true,true};
				portColumns = new boolean[10];
				for(int i=0;i<portC.length;i++){
					portColumns[i]=(boolean)(Boolean)portC[i];
					//System.out.println(i+" "+portColumns[i]);
				}
				//portColumns[9] = true;
				Object[] bookmarks_xml_rpc = (Object[])XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/bookmarks.php","getBookmarks",new Object[]{ip});
				for(int i=0;i<bookmarks_xml_rpc.length;i++){
					bookmarks.add(bookmarks_xml_rpc[i]);
					
				}
			}
			else{
				portColumns = new boolean[]{true,true,true,true,true,true,true,true,true,true,true};
			}
		}else{
			portColumns = new boolean[]{true,true,true,true,true,true,true,true,true,true,true};		
			//showTutorial = false;
		}
		
		//Bootstrap the 3D chat component.
		//HMMO=new HacktendoMMO(this);
		//HMMO.openGame("game.xml");
		
		//This requests the initial equipment and the message is used server-side to determine the GUI is actually ready.
		Object[] params = new Object[]{encryptedIP};
		setRequestedDirectory(EQUIPMENT);
		MyView.addFunctionCall(new RemoteFunctionCall(EQUIPMENT,"requestequipment",params));
		MyView.addFunctionCall(new RemoteFunctionCall(0,"fetchports",params));
		
		frame.pack();
    }
	
	
	
	/**
	Add a packet for the Hacktendo MMO engine.
	*/
	public void addHacktendoPacket(HacktendoPacket Packet){
		//HMMO.addPacket(Packet);
	}
	
	public void setVotesLeft(int votesLeft){
		this.votesLeft = votesLeft;
		if(MyWebBrowser!=null){
			MyWebBrowser.setVotesLeft(votesLeft);
		}
	}
	
	public int getVotesLeft(){
		return(votesLeft);
	}
    
    /**
            Return a users preference for a given system option.
            */
    public String getPreference(String preference) {
        if (preferences != null) {
            Object o = preferences.get(preference);
            if (o != null) {
                return (String)o;
            }
        }
        return "";
    }
    
    /**
    Get all of the preferences for this user.
    */
    public HashMap getPreferences() {
        return preferences;
        //return new HashMap();
    }
    
    /**
    Set all the preferences for this user.  Done on startup, or when Apply is clicked from the Preferences window.
    */
    public void setPreferences(HashMap prefs) {
        this.preferences = prefs;
    }
    
    /**
    Set a single user preference.  This will be done from the option popup.
    */
    public void setPreference(String key, String value) {
        if (preferences == null) {
            preferences = new HashMap();
        }
        Object oldValue = preferences.get(key);
        if (oldValue != null) {
            if (!((String)oldValue).equals(value)) {
                preferences.put(key, value);
                // Inefficient, sure, but this will happen very rarely.  It's currently what they call "the least of our worries." aka/ "Fuck it."  If needed, create a new server function, "setpreference" that takes in only this preference.
                sendPreferences();
            }
        } else {
            preferences.put(key, value);
            sendPreferences();
        }
    }
    
    /**
        Send the preferences to the server.  Done when a user clicks Apply on the Preferences window.
        */
    public void sendPreferences() {
        Object[] objects = {encryptedIP, preferences};
        MyView.setFunction("setpreferences");
        MyView.addFunctionCall(new RemoteFunctionCall(0,"setpreferences",objects));
    }
    
	public void setTutorial(){
        boolean showTutorial = false;
        if (preferences != null) {
            String val = getPreference(OptionPanel.ATTACK_TUTORIAL_KEY);
            if ( val.equals("true") || val.equals("") ) {
                showTutorial = true;
            }
        }
        if(showTutorial&&!offline){
            setTutorial(true);
        }
	}
	
	public void setTutorial(boolean show){
		tutorialWindow = new TutorialWindow(this);
		panel.add(tutorialWindow);
		tutorialWindow.moveToFront();
		tutorialWindow.setVisible(true);
		
	}
	
	public void setHealDiscount(float healDiscount){
		this.healDiscount=healDiscount;
	}
	
	public float getHealDiscount(){
		return(healDiscount);
	}
	
    public void setNetwork(PacketNetwork network){
		if(MP==null){
			MP = new MapPanel(this,400,400,50,50);
			panel.add(MP);
		}
		storeIP = network.getStoreIP();
		MP.setNetwork(network);
        
        // check the user preferences for network -- refer to OptionPanel for the list of options
        String val = getPreference(OptionPanel.NETWORK_KEY);
        if (val.equals("true") || val.equals("")) {
            MP.setVisible(true);
        }
		//panel.setComponentZOrder(MP,1);
        // make the tutorial window popup infront of the network window
		if(tutorialWindow==null) {
			setTutorial();
        }
	}
	
	public String getStoreIP(){
		return storeIP;
	}
	
	public void resetTitle(){
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		float cpu = SP.getCPULoadIcon().getPercent();
		NumberFormat cpuFormat = new DecimalFormat("0.00");
		frame.setTitle("Hack Wars - "+ip+" | CPU: "+cpuFormat.format(cpu)+"% | HD: "+HDQuantity+"/"+HDMax+" | "+nf.format(pettyCash)+" | "+"DT: "+ductTape+" | GE: "+germanium+" | SI: "+silicon+" | YBCO: "+ybco+" | PU: "+plutonium);
	}
    
    public void update(View MyView,String username,String ip,boolean npc,String encryptedIP){
		this.MyView=MyView;
		this.username=username;
		this.ip=ip;
		this.encryptedIP=encryptedIP;
    }
	
	public ArrayList getBookmarks(){
		return bookmarks;
	}
	
	public void addBookmark(String domain,String name,String folder,String id){
		bookmarks.add(new Object[]{domain,name,folder,id});
	}
	
	public boolean getProPack(){
		return proPack;
	}
    
    public JLabel getNetworkLabel(){
	    return networkLabel;
    }
    
    public void resetNetworkButtons(){
	    //MP.resetButtons();
    }
    
    public void moveToNetwork(String name){
	    /*resetNetworkButtons();
	    if(name.equals("ROOT")){
		    MP.getRoot().moveHere();
		    showMessage("You have been moved to the root network");
	    }
	    else if(name.equals("Arts")){
		    MP.getArts().moveHere();
		    showMessage("You have been moved to the Arts network");
	    }
	    else if(name.equals("Drama")){
		    MP.getDrama().moveHere();
		    showMessage("You have been moved to the Drama network");
	    }
	    else if(name.equals("Philosophy")){
		    MP.getPhilosophy().moveHere();
		    showMessage("You have been moved to the Philosophy network");
	    }
	    else if(name.equals("Science")){
		    MP.getScience().moveHere();
		    showMessage("You have been moved to the Science network");
	    }
	    else if(name.equals("Physics")){
		    MP.getPhysics().moveHere();
		    showMessage("You have been moved to the Physics network");
	    }
	    else if(name.equals("Economics")){
		    MP.getEconomics().moveHere();
		    showMessage("You have been moved to the Economics network");
	    }
	    else if(name.equals("Literature")){
		    MP.getLiterature().moveHere();
		    showMessage("You have been moved to the Literature network");
	    }
	    else
	    	System.out.println("Unknown Network: "+name);*/
	       
    }
	    
    public JFrame getFrame(){
	    return frame;
    }
    
    public boolean getLoading(){
	    return(loading);
    }
    
    public JDesktopPane getPanel(){
	    return(panel);
    }
	
    public void setServerLoad(int serverLoad){
	    if(serverLoad!=this.serverLoad){
		    this.serverLoad=serverLoad;
		    System.out.println("Players Connected: "+serverLoad);
	    }
    }
    /**
    setDefaultBank(int port) <br />
    sets the value for the default bank port.
    */
    public void setDefaultBank(int port){
	    defaultBankPort=port;
    }
    
    public int getDefaultBank() {
        return defaultBankPort;
    }
    
    /**
    setDefaultAttack(int port) <br />
    sets the value for the default attack port.
    */
    public void setDefaultAttack(int port){
	    defaultAttackPort=port;
    }
    
    public int getDefaultAttack() {
        return defaultAttackPort;
    }
	
	public void setDefaultRedirect(int port){
	    defaultRedirectPort=port;
    }
    
    public int getDefaultRedirect() {
        return defaultRedirectPort;
    }
    
    /**
    setDefaultFTP(int port) <br />
    sets the value for the default ftp port.
    */
    public void setDefaultFTP(int port){
	   defaultFTPPort=port;
    }
    
    
    /**
    setDefaultHTTP(int port) <br />
    sets the value for the default http port.
    */
    public void setDefaultHTTP(int port){
	defaultHTTPPort=port;
    }
    
    public void setMerchantingXP(float xp){
	    int previousLevel = SP.getMerchantingIcon().getLevel();
	    float previousXP = SP.getMerchantingIcon().getXP();
	    SP.getMerchantingIcon().setXP(xp);
	    int newLevel = SP.getMerchantingIcon().getLevel();
	    if(newLevel>previousLevel&&previousXP!=0)
		    showMessage("Congratulations, you now have level "+newLevel+" merchanting!");
	    if(previousXP!=xp)
		    SP.repaint();
	    if(previousXP!=xp&&previousXP!=0){
		    float xpChange = xp-previousXP;
		    statList.add("+"+xpChange+" Merchanting XP");
	    }
    }
    
     public void setAttackXP(float xp){
	    int previousLevel = SP.getAttackIcon().getLevel();
	    float previousXP = SP.getAttackIcon().getXP();
	    SP.getAttackIcon().setXP(xp);
	    int newLevel = SP.getAttackIcon().getLevel();
	    if(newLevel>previousLevel&&previousXP!=0)
		    showMessage("Congratulations, you now have level "+newLevel+" attack!");
	    if(previousXP!=xp)
		    SP.repaint();
	    if(previousXP!=xp&&previousXP!=0){
		    float xpChange = xp-previousXP;
		    statList.add("+"+xpChange+" Attack XP");
	    }
    }
    
     public void setWatchXP(float xp){
	    int previousLevel = SP.getWatchIcon().getLevel();
	    float previousXP = SP.getWatchIcon().getXP();
	    SP.getWatchIcon().setXP(xp);
	    int newLevel = SP.getWatchIcon().getLevel();
	    if(newLevel>previousLevel&&previousXP!=0)
		    showMessage("Congratulations, you now have level "+newLevel+" watch!");
	    if(previousXP!=xp)
		    SP.repaint();
	    if(previousXP!=xp&&previousXP!=0){
		    float xpChange = xp-previousXP;
		    statList.add("+"+xpChange+" Watch XP");
	    }
    }
    
     public void setScanXP(float xp){
	    int previousLevel = SP.getScanIcon().getLevel();
	    float previousXP = SP.getScanIcon().getXP();
	    SP.getScanIcon().setXP(xp);
	    int newLevel = SP.getScanIcon().getLevel();
	    if(newLevel>previousLevel&&previousXP!=0)
		    showMessage("Congratulations, you now have level "+newLevel+" scanning!");
	    if(previousXP!=xp)
		    SP.repaint();
	    if(previousXP!=xp&&previousXP!=0){
		    float xpChange = xp-previousXP;
		    statList.add("+"+xpChange+" Scan XP");
	    }
    }
    
     public void setFirewallXP(float xp){
	    int previousLevel = SP.getFireWallIcon().getLevel();
	    float previousXP = SP.getFireWallIcon().getXP();
	    SP.getFireWallIcon().setXP(xp);
	    int newLevel = SP.getFireWallIcon().getLevel();
	    if(newLevel>previousLevel&&previousXP!=0)
		    showMessage("Congratulations, you now have level "+newLevel+" firewall!");
	    if(previousXP!=xp)
		    SP.repaint();
	    if(previousXP!=xp&&previousXP!=0){
		    float xpChange = xp-previousXP;
		    statList.add("+"+xpChange+" Firewall XP");
	    }
    }
    public void setHTTPXP(float xp){
	    int previousLevel = SP.getHTTPIcon().getLevel();
	    float previousXP = SP.getHTTPIcon().getXP();
	    SP.getHTTPIcon().setXP(xp);
	    int newLevel = SP.getHTTPIcon().getLevel();
	    if(newLevel>previousLevel&&previousXP!=0)
		    showMessage("Congratulations, you now have level "+newLevel+" HTTP!");
	    if(previousXP!=xp)
		    SP.repaint();
	    if(previousXP!=xp&&previousXP!=0){
		    float xpChange = xp-previousXP;
		    statList.add("+"+xpChange+" HTTP XP");
	    }
    }
    public void setRedirectXP(float xp){
	    int previousLevel = SP.getRedirectIcon().getLevel();
	    float previousXP = SP.getRedirectIcon().getXP();
	    SP.getRedirectIcon().setXP(xp);
	    int newLevel = SP.getRedirectIcon().getLevel();
	    if(newLevel>previousLevel&&previousXP!=0)
		    showMessage("Congratulations, you now have level "+newLevel+" Redirect!");
	    if(previousXP!=xp)
		    SP.repaint();
	    if(previousXP!=xp&&previousXP!=0){
		    float xpChange = xp-previousXP;
		    statList.add("+"+xpChange+" Redirect XP");
	    }
    }
	
	public void setRepairXP(float xp){
	    int previousLevel = SP.getRepairIcon().getLevel();
	    float previousXP = SP.getRepairIcon().getXP();
	    SP.getRepairIcon().setXP(xp);
	    int newLevel = SP.getRepairIcon().getLevel();
	    if(newLevel>previousLevel&&previousXP!=0)
		    showMessage("Congratulations, you now have level "+newLevel+" Repair!");
	    if(previousXP!=xp)
		    SP.repaint();
	    if(previousXP!=xp&&previousXP!=0){
		    float xpChange = xp-previousXP;
		    statList.add("+"+xpChange+" Repair XP");
	    }
    }
    
	
	
	public void setCommodities(float[] commodities){
		ductTape = (int)commodities[0];
		germanium = (int)commodities[1];
		silicon = (int)commodities[2];
		ybco = (int)commodities[3];
		plutonium = (int)commodities[4];
		
		SP.setCommodities(commodities);
		statChanged();
	}
	
    public ChatController getChatController(){
	    return(MyChatController);
    }
    
    
    /**
    setCPUType(int CPUType) <br />
    sets the type of cpu currently installed on the players computer.
    */
    public void setCPUType(int CPUType){
	    this.CPUType=CPUType;
    }
    
    /**
    setHDType(int HDType) <br />
    sets the type of hard drive currently installed on the players computer.
    */
    public void setHDType(int HDType){
	    if(HDType!=this.HDType){
		    this.HDType=HDType;
		    MyHome.setStatusLabel();
	    }
    }
    
    /**
    setHDQuantity(int HDQuantity) <br />
    sets the amount of files on the players hard drive.
    */
    public void setHDQuantity(int HDQuantity){
	    if(HDQuantity!=this.HDQuantity){
		    this.HDQuantity=HDQuantity;
		    MyHome.setStatusLabel();
			resetTitle();
	    }
    }
    
    public void setHDMax(int HDMax){
	    if(HDMax!=this.HDMax){
		    this.HDMax=HDMax;
		    //System.out.println(HDMax);
		    MyHome.setStatusLabel();
			resetTitle();
	    }
    }
    
    public int getHDMax(){
	    return HDMax;
    }
    
    public void setMemoryType(int memoryType){
	    this.memoryType=memoryType;
    }
    
    /**
    getCPUType() <br />
    returns the current cpu type installed on the players computer.
    */
    public int getCPUType(){
	    return(CPUType);
    }
    
    /**
    getHDType() <br />
    returns the current hard drive type installed on the players computer.
    */
    public int getHDType(){
	    return(HDType);
    }
    
    /**
    getHDQuantity() <br />
    returns the current amount of files on the players hard drive.
    */
    public int getHDQuantity(){
	    return(HDQuantity);
    }
    
    public int getMemoryType(){
	    return(memoryType);
    }
    
    /**
    createXPTable() <br />
    this creates the experience table used to determine level.
    */
    private void createXPTable(){
	    int xp=83; //experience to reach level 1.
	    int xpDiff=83;  
	    //xpTable[0]=0;
	    for(int i=0;i<100;i++){
		    xpTable[i]=xp;
		    xpDiff+=xpDiff/9.525;
		    xp+=xpDiff;
	    }
    }
    
    /**
    getXPTable() <br />
    this returns the experience table used to determine level.
    */
    public int[] getXPTable(){
	    return(xpTable);
    }
    
    /**
    setPorts(PacketPort ports[]) <br />
    sets the array of ports.
    */
    public void setPorts(PacketPort ports[]){
	    this.ports=ports;
	    if(portManagement!=null) {
		    portManagement.setPorts(ports); //set the ports in the port management window. 
        }
        updateDepositWindow();
		updateWithdrawWindow();
		updateTransferWindow();
		updateAttackWindows();
    }
    
    public PacketPort[] getPorts() {
        return this.ports;
    }
    
    public void setHealth(Object[] data){
	   // System.out.println("Received Health Update");
    	if(portManagement!=null)
    		portManagement.gotHealth(data);
		
		for(int i=0;i<data.length;i++){
			Object[] port = (Object[])data[i];
			int portnum = (int)(Integer)port[0];
			float health = (float)(Float)port[1];
			int healCount = (int)(Integer)port[4];
			int windowHandle = (int)(Integer)port[6];
			AttackPane attackPane = (AttackPane)MyAttackPane.get(""+windowHandle);
			if(attackPane!=null){
				attackPane.setHealth(health,healCount);
			}
		}
    }
	
    /**
    getCurrentDirectory() <br />
    returns the current working directory.
    */
    public Object[] getCurrentDirectory(){
	    return(currentDirectory);
    }
    
    /**
    getCurrentFolder() <br />
    returns the current working folder.
    */
    public String getCurrentFolder(){
	    return(currentFolder);
    }
    
    /**
    getSecondaryFolder() <br />
    returns the current secondary folder. (Store or Public)
    */
    public String getSecondaryFolder(){
	    return(secondaryFolder);
    }
    
    /**
    setCurrentFolder(String currentFolder) <br />
    sets the current working folder.
    */
    public void setCurrentFolder(String currentFolder){
	    this.currentFolder = currentFolder;
    }
    
    /**
    setSecondaryFolder(String secondaryFolder) <br />
    sets the current secondary folder.
    */
    public void setSecondaryFolder(String secondaryFolder){
	    this.secondaryFolder=secondaryFolder;
    }
    
    /**
    setFileChooser(HackerFileChooser fileChooser) <br />
    sets the file chooser being used by the script editor so that the program knows where to send the directory.
    */
    public void setFileChooser(HackerFileChooser fileChooser){
	    this.fileChooser=fileChooser;
    }
    
    public void setHacktendoFileChooser(HacktendoFileChooser fileChooser){
	    this.hacktendoFileChooser=fileChooser;
    }
    
    /**
    setAttackFileChooser(AttackFileChooser fileChooser) <br />
    sets the file chooser being used by the attack pane so that the program knows where to send the directory.
    */
    public void setAttackFileChooser(AttackFileChooser fileChooser){
	    this.attackFileChooser=fileChooser;
    }
    
    /**
    setInstallFileChooser(InstallFileChooser fileChooser) <br />
    sets the file chooser being used by the port management so that the program knows where to send the directory.
    */
    public void setInstallFileChooser(InstallFileChooser fileChooser){
	    installFileChooser=fileChooser;
    }
    
    public void setBountyFileChooser(BountyFileChooser fileChooser){
	    bountyFileChooser=fileChooser;
    }
    
    /**
    setWatchInstallFileChooser(WatchInstallFileChooser fileChooser) <br />
    sets the file chooser being used by the watch manager so that the program knows where to send the directory.
    */
    public void setWatchInstallFileChooser(WatchInstallFileChooser fileChooser){
	    watchInstallFileChooser=fileChooser;
    }
    
    /**
    setFireWallFileChooser(FireWallFileChooser fileChooser) <br />
    sets the file chooser being used by the port management so that the program knows where to send the directory.
    */
    public void setFireWallFileChooser(FireWallFileChooser fileChooser){
	    firewallFileChooser=fileChooser;
    }
    
     /**
    setShowChoicesFileChooser(ShowChoicesFileChooser fileChooser) <br />
    sets the file chooser being used by the show choices dialog so that the program knows where to send the directory.
    */
    public int setShowChoicesFileChooser(ShowChoicesFileChooser fileChooser){
	    directoryRequests.put(new Integer(showChoices),fileChooser);
	    showChoices++;
	    return showChoices-1;
    }
    
    public void setFTP(FTP MyFTP){
	    this.MyFTP=MyFTP;
    }
    
    /**
    receivedDirectory(Object[] currentDirectory) <br />
    called from View when a directory is received by the server.
    */
    public synchronized void receivedDirectory(Object[] currentDirectory){
	if(currentDirectory!=null){
		int directoryRequest = (Integer)currentDirectory[0];
		if(directoryRequest!=BROWSER&&directoryRequest!=EQUIPMENT)
			this.currentDirectory = sortDirectory(currentDirectory,0,directoryRequest,false);
		else if(directoryRequest==BROWSER)
			MyWebBrowser.setStore(currentDirectory);  //if the web browser requested the directory, send it without sorting.
		else if(directoryRequest==EQUIPMENT){
			//System.out.println("Received Equipment");
			if(EQ!=null)
				EQ.receivedInventory(currentDirectory);
		}
	}
			
    }
	
	public void setCommandPromptRequestedDirectory(CommandPrompt cp){
		commandPromptRequestedDirectory = cp;
	}
    
    /**
    receivedSecondaryDirectory(Object[] secondaryDirectory) <br />
    called from View when a Store or Public directory is received from the server.
    */
    public synchronized void receivedSecondaryDirectory(Object[] secondaryDirectory,boolean allowedDir){
	   //System.out.println("Received Secondary Directory -- "+secondaryDirectory.length);
	   if(secondaryDirectory!=null){
		   int directoryRequest = (Integer)secondaryDirectory[0];
		   if(directoryRequest!=EQUIPMENT)
			   this.secondaryDirectory=sortDirectory(secondaryDirectory,1,directoryRequest,allowedDir);
		   else{
			   if(EQ!=null)
				   EQ.receivedEquipment(secondaryDirectory);
		   }
	   }
    }
    
    /**
    receivedScan(PacketPort scan[]) <br />
    called from View when a scan is received from the server and sent on to Port Scan application.
    */
    public void receivedScan(PacketPort scan[]){
        if(MyPortScan!=null) {
            MyPortScan.receivedScan(scan);
        }
		else{
			startScan("");
		}
    }
    
    /**
    receivedWatched(PacketWatch watches[]) <br />
    called from View when watches are received from the server and sent on to Watch Manager.
    */
    public void receivedWatches(PacketWatch watches[]){
	    if(MyWatchManager!=null)
		    MyWatchManager.receivedWatches(watches);
    }
    
    /**
    Object[] sortDirectory(Object[] directory,int type) <br />
    sorts the directory passed in by directories alphabetically, the files alphabetically. <br />
    type - 0 for primary directory, 1 for secondary directory.
    */
    public synchronized Object[] sortDirectory(Object[] directory,int type,int directoryRequest, boolean allowedDir){
	    Object returnMe[] = new Object[directory.length-1];  //array to be returned when done sorting.
	    int dcount=0,fcount=0;  //counts for directorys and files.
	    for(int i=1;i<directory.length;i++){
		    if(directory[i] instanceof String){
			    dcount++; //if it is a directory increase directory count.
		    }
		    else{
			    fcount++;  //if it is a file increase file count.
		    }
	    }
	    String dirs[] = new String[dcount];  //array for directories.
	    Object files[] = new Object[fcount]; //array for files.
	    dcount=0;
	    fcount=0;
	    //fill in directory and file arrays.
	    for(int i=1;i<directory.length;i++){
		    if(directory[i] instanceof String){
			dirs[dcount]=(String)directory[i];
			dcount++;
		    }
		    else{
			    files[fcount]=directory[i];
			    fcount++;
		    }
	    }
	    java.util.Arrays.sort(dirs, String.CASE_INSENSITIVE_ORDER);  //sort the directory array alphabetically.
	    
	    //sort the file array alphabetically.
	    boolean changed=true;
	    Object temp;
	    while(changed){
		    changed=false;
		    for(int i=0;i<files.length-1;i++){
			    Object blah[] = (Object[])files[i]; //file 1
			    Object blah2[] = (Object[])files[i+1];  //file 2
			    String thing = (String)blah[0];  //file name of file 1
			    if(thing.compareToIgnoreCase((String)blah2[0])>0){  //if file 1 come after file 2 alphabetically
				    temp =files[i];  //store file 1 in a temporary place.
				    files[i]=files[i+1]; //make file 1 now file 2.
				    files[i+1]=temp; //make file 2 now file 1.
				    changed=true; //it has been changed.
			    }
		    }
	    }
	    //add directories at the start of the array.
	    for(int i=0;i<dcount;i++){
		    returnMe[i]=dirs[i]; 
	    }
	    
	    //add files and the end of the array.
	    for(int i=0;i<fcount;i++){
		    returnMe[dcount+i]=files[i];
	    }
	    
	    //if it a primary directory send it on to where it needs to go.
	    if(type==0){
		    if(directoryRequest==FILE_CHOOSER){
			    if(fileChooser!=null)
				    fileChooser.setDirectory(returnMe);
		    }
		    else if(directoryRequest==ATTACK_FILE_CHOOSER){
			    if(attackFileChooser!=null)
				    attackFileChooser.setDirectory(returnMe);
		    }
		    else if(directoryRequest==INSTALL_FILE_CHOOSER){
			    if(installFileChooser!=null)
				    installFileChooser.setDirectory(returnMe);
		    }
		    else if(directoryRequest==HACKTENDO_FILE_CHOOSER){
		    	if(hacktendoFileChooser!=null)
		    		hacktendoFileChooser.setDirectory(returnMe);
			}
		    /*if(directoryRequest==BROWSER){
			    	MyWebBrowser.setStore(returnMe);
		    }*/
		    else if(directoryRequest==FIREWALL_FILE_CHOOSER){
			    if(firewallFileChooser!=null)
				    firewallFileChooser.setDirectory(returnMe);
		    }
		    else if(directoryRequest==FTP){
			    MyFTP.receivedDirectory(returnMe);
		    }
		    else if(directoryRequest==WATCH_INSTALL_FILE_CHOOSER){
			    if(watchInstallFileChooser!=null)
				    watchInstallFileChooser.setDirectory(returnMe);
		    }
		    else if(directoryRequest==HOME){
			   // System.out.println("Sending Directory to Home");
			    MyHome.setDirectory(returnMe);
		    }
		    else if(directoryRequest==BOUNTY_FILE_CHOOSER){
			    bountyFileChooser.setDirectory(returnMe);
		    }
			else if(directoryRequest==COMMAND_PROMPT){
				commandPromptRequestedDirectory.receivedDirectory(returnMe);
			}
			else if(directoryRequest==FIREWALL_BROWSER){
				firewallBrowser.setDirectory(returnMe);
			}
			
	    }
	    else{  //if it is a secondary directory send it on the the FTP program.
		    if(directoryRequest==FTP)
			    MyFTP.setSecondaryDirectory(returnMe,allowedDir);
		    else if(directoryRequest>=100){
			    ShowChoicesFileChooser showChoicesFileChooser = (ShowChoicesFileChooser)directoryRequests.get(directoryRequest);
			    if(showChoicesFileChooser!=null){
				    showChoicesFileChooser.setDirectory(returnMe);
			    }
		    }
	    }
	    return(returnMe);
    }
	
	/**
	This is only used for Hacktendo Games. It is the save data loaded off a player's HD.
	*/
	HashMap LoadFile=null;
	public HashMap getLoadFile(){
		return(LoadFile);
	}
	
	public void setLoadFile(HashMap LoadFile){
		this.LoadFile=LoadFile;
	}	
    
    /**
    receivedFile(HackerFile file) <br />
    called from View when a file is received from the server.
    */
    public void receivedFile(HackerFile file){
	    //if the file was requested from the script editor, send it there.
	    if(fileRequest==SCRIPT_EDITOR){
			if(MyScriptEditor!=null)
				MyScriptEditor.receivedFile(file);
		}
	    //if the file was requested from the file browser, send it there.
	    else if(fileRequest==HOME){
		    if(MyHome.getShow()==Home.ICONS){
			    if(requestedFileIcon!=null)
				    requestedFileIcon.receivedFile(file);
		    }
		    else if(MyHome.getShow()==Home.TABLE)
			    MyHome.setProperties(file);
	    }
	    else if(fileRequest==IMAGE_VIEWER){
		    MyImageViewer.gotImage(file);
		}
		else if(fileRequest==HACKTENDO_CREATOR){
			hacktendoCreator.receivedGame(file);
		}
		else if(fileRequest==HACKTENDO_PLAYER){
			HashMap contents = file.getContent();
			if(contents!=null){
				String xml = (String)contents.get("data");
				xml=xml.replaceAll("&gt;",">");
				startHacktendoPlayer(xml,file.getName(),LoadFile);
			}
		}
		else if(fileRequest==COMMAND_PROMPT){
			commandPromptRequestedDirectory.receivedFile(file);
		}
		else if(fileRequest == FILE_PROPERTIES){
			fileProperties.receivedFile(file);
		}
    }
    
    /**
    setRequestedFile(int app) <br />
    sets which application requested a file from the server.
    */
    public void setRequestedFile(int app){
	    fileRequest=app;
    }
    
	public void setFileProperties(FileProperties fileProperties){
		this.fileProperties = fileProperties;
	}
	
    /**
    setRequestedDirectory(int app) <br />
    sets which application requested a directory from the server.
    */
    public void setRequestedDirectory(int app){
	    directoryRequest=app;
    }
    public int getRequestedDirectory(){
	return(directoryRequest);    
    }
    
    public void setRequestedFileIcon(HomeIcon HI){
	    requestedFileIcon=HI;
    }
    /**
    setSiteRequest(int app) <br />
    sets which application requested a site.
    */
    public void setSiteRequest(int app){
	    siteRequest=app;
    }
    
    public void setImageViewer(ImageViewer im){
	    MyImageViewer=im;
    }
    
    /**
    receivedPage(String title,String page) <br />
    called from View when a web page is received from the server.
    */
    public void receivedPage(String title,String page){
	    if(siteRequest==BROWSER)
		    MyWebBrowser.receivedPage(title,page);
	    else{
		    if(MyWebsiteEditor!=null)
		    MyWebsiteEditor.receivedPage(title,page);
	    }
	    
		    
    }
    
    public void setPeekAtCode(PeekAtCode peekAtCode){
	    this.peekAtCode=peekAtCode;
    }
    
    public void startLogWindow(){
	    if(MyLogWindow==null){
		    MyLogWindow = new LogWindow(this);
		    panel.add(MyLogWindow);
		    MyLogWindow.setVisible(true);
		    MyLogWindow.moveToFront();
	    }
	    else{
		    MyLogWindow.setVisible(true);
		    if(MyLogWindow.isIcon()){
    		    try{
    			    MyLogWindow.setIcon(false);
    		    }catch(Exception e){}
            }
            MyLogWindow.moveToFront();
	    }
	    
    }
    
    public void startPersonalSettings(String username){
		    MyPersonalSettings = new PersonalSettings(username,this);
		    panel.add(MyPersonalSettings);
		    MyPersonalSettings.setVisible(true);
		    MyPersonalSettings.moveToFront();
    }


    public void showOptions(String username){
			Dimension d = panel.getSize();
		    options = new OptionPanel(this, d);
		    panel.add(options);
		    options.setVisible(true);
		    options.moveToFront();
    }


    public void showPersonalSettings(String username){
	    //if(MyPersonalSettings==null)
		    startPersonalSettings(username);
	    /*if(!MyPersonalSettings.isVisible())
		    MyPersonalSettings.setVisible(true);
	    if(MyPersonalSettings.isIcon()){
		    try{
		    	MyPersonalSettings.setIcon(false);
		    }catch(Exception e){}
	    }*/
    }
    
    public void receivedLogUpdate(String[] logUpdate){
        boolean show = true;
        if (isOnLoad == true) {
            isOnLoad = false;
            // check the user preferences to see if we should show the log window on startup
            String val = getPreference(OptionPanel.LOG_WINDOW_KEY);
            if (val.equals("true") || val.equals("")) {
                show = true;
            } else {
                show = false;
            }
        }

        if (show) {
            if(MyLogWindow==null&&logUpdate.length!=0){
                MyLogWindow = new LogWindow(this);
                panel.add(MyLogWindow);
                MyLogWindow.setVisible(true);
                MyLogWindow.moveToFront();
            }
            
            String log="";
            if(logUpdate.length!=0){
                for(int i=0;i<logUpdate.length;i++){
                    if(logUpdate[i]!=null&&!logUpdate[i].equals("null"))
                        log+=logUpdate[i]+"\n";
                }
                
                MyLogWindow.addLine(log);
                MyLogWindow.setVisible(true);
            }
        }
    }
    
    public void showCode(HashMap HM){
	    peekAtCode.setFile(HM);
    }
    
    /**
    String getIP() <br />
    returns the ip of the player.
    */
    public String getIP(){
	    return(ip);
    }
    
    public String getEncryptedIP(){
	    return(encryptedIP);
    }
    
    
    /**
    String getUsername() <br />
    returns the username of the player.
    */
    public String getUsername(){
	    return(username);
    }
    
    public String getFTPIP(){
	    if(MyFTP!=null)
		    return(MyFTP.getIP());
	    else
		    return(ip);
    }
    
    public String getFTPPass(){
	    return(MyFTP.getPass());
    }
    
    public int getFTPPort(){
	    if(MyFTP!=null)
		    return(MyFTP.getPort());
	    else
		    return(0);
    }
    
    /**
    getMerchantingIcon() <br />
    this returns the icon that represents the merchanting statistic.
    */
    public StatIcon getMerchantingIcon(){
	    return(MerchantingIcon);
    }
    
    /**
    getAttackIcon() <br />
    this returns the icon that represents the attack statistic.
    */
    public StatIcon getAttackIcon(){
	    return(AttackIcon);
    }
    
    /**
    getWatchIcon() <br />
    this returns the icon that represents the watch statistic.
    */
    public StatIcon getWatchIcon(){
	    return(WatchIcon);
    }
    
    /**
    getScanIcon() <br />
    this returns the icon that represents the scanning statistic.
    */
    public StatIcon getScanIcon(){
	    return(ScanIcon);
    }
    
    /**
    getFireWallIcon() <br />
    this returns the icon that represents the firewall statistic.
    */
    public StatIcon getFireWallIcon(){
	    return(FireWallIcon);
    }
    
    /**
    getTotalLevelIcon() <br />
    this returns the icon that represents the total level.
    */
    public TotalLevelIcon getTotalLevelIcon(){
	    return(TotalIcon);
    }
    
    /**
    getCPULoadIcon() <br />
    returns the icon that represents the cpu load.
    */
    public CPULoadIcon getCPULoadIcon(){
	    return(CPUIcon);
    }
    
    /**
    createMenu() <br />
    this creates the main menu for the program.
    */
    private void createMenu(){
	JMenu menu, subMenu;
	JMenuItem menuItem;
	JRadioButtonMenuItem rbMenuItem;
	JCheckBoxMenuItem cbMenuItem;

	//Create the menu bar.
	menuBar = new JMenuBar();
	//menuBar.setLayout(new FlowLayout(FlowLayout.LEADING,2,0));
	menuBar.setLayout(new MigLayout("fillx,gap 2 0,top,left,ins 0"));
	//APPLICATIONS
	menu = new JMenu("Applications");
	menuBar.add(menu);

	subMenu = new JMenu("Banking");
	menu.add(subMenu);
	menuItem = new JMenuItem("Deposit",ImageLoader.getImageIcon("images/calc.png"));
	subMenu.add(menuItem);
	menuItem.addActionListener(this);
	menuItem = new JMenuItem("Withdraw",ImageLoader.getImageIcon("images/calc.png"));
	subMenu.add(menuItem);
	menuItem.addActionListener(this);
	menuItem = new JMenuItem("Transfer",ImageLoader.getImageIcon("images/calc.png"));
	subMenu.add(menuItem);
	menuItem.addActionListener(this);

	subMenu = new JMenu("Internet");
	menu.add(subMenu);
	menuItem = new JMenuItem("Web Browser",ImageLoader.getImageIcon("images/browser.png"));
	subMenu.add(menuItem);
	menuItem.addActionListener(this);
	menuItem = new JMenuItem("Store",ImageLoader.getImageIcon("images/browser.png"));
	subMenu.add(menuItem);
	menuItem.addActionListener(this);
	menuItem = new JMenuItem("Site Editor",ImageLoader.getImageIcon("images/edit.png"));
	subMenu.add(menuItem);
	menuItem.addActionListener(this);


	subMenu = new JMenu("Hacking Tools");
	menu.add(subMenu);
	menuItem = new JMenuItem("Port Scan",ImageLoader.getImageIcon("images/scan.png"));
	subMenu.add(menuItem);
	menuItem.addActionListener(this);
	menuItem = new JMenuItem("Attack Port",ImageLoader.getImageIcon("images/attack.png"));
	subMenu.add(menuItem);
	menuItem.addActionListener(this);
	menuItem = new JMenuItem("Redirect Port",ImageLoader.getImageIcon("images/redirect.png"));
	subMenu.add(menuItem);
	menuItem.addActionListener(this);
	menuItem = new JMenuItem("Zombie Attack",ImageLoader.getImageIcon("images/attack.png"));
	subMenu.add(menuItem);
	menuItem.addActionListener(this);
	

	menuItem = new JMenuItem("Script Editor",ImageLoader.getImageIcon("images/edit.png"));
	menu.add(menuItem);
	menuItem.addActionListener(this);
	
	menuItem = new JMenuItem("Create Bounty");
	menu.add(menuItem);
	menuItem.addActionListener(this);
	
	if(paidHacktendo){
		menuItem = new JMenuItem("Hacktendo Game Creator");
		menu.add(menuItem);
		menuItem.addActionListener(this);
	}

	/*menuItem = new JMenuItem("Command Prompt");
	menu.add(menuItem);
	menuItem.addActionListener(this);*/
/*
	menuItem = new JMenuItem("Hacktendo Game Player");
	menu.add(menuItem);
	menuItem.addActionListener(this);
*/
	
	//PLACES
	menu = new JMenu("Places");
	menuBar.add(menu);

	menuItem = new JMenuItem("Shop FTP",ImageLoader.getImageIcon("images/ftp.png"));
	menu.add(menuItem);
	menuItem.addActionListener(this);
	
	menuItem = new JMenuItem("Public FTP",ImageLoader.getImageIcon("images/ftp.png"));
	menu.add(menuItem);
	menuItem.addActionListener(this);
	
	menuItem = new JMenuItem("Home",ImageLoader.getImageIcon("images/home.png"));
	menu.add(menuItem);
	menuItem.addActionListener(this);
	
	menuItem = new JMenuItem("Network",ImageLoader.getImageIcon("images/home.png"));
	menu.add(menuItem);
	menuItem.addActionListener(this);
	
	menuItem = new JMenuItem("Log Window");
	menu.add(menuItem);
	menuItem.addActionListener(this);

	
	//SYSTEM
	menu = new JMenu("System");
	menuBar.add(menu);
	subMenu = new JMenu("Administration");

	menuItem = new JMenuItem("Port Management",ImageLoader.getImageIcon("images/ports.png"));
	menu.add(menuItem);
	menuItem.addActionListener(this);

	menuItem = new JMenuItem("Watch Manager",ImageLoader.getImageIcon("images/watch.png"));
	menu.add(menuItem);
	menuItem.addActionListener(this);

	menuItem = new JMenuItem("Equipment Manager",ImageLoader.getImageIcon("images/cpu.png"));
	menu.add(menuItem);
	menuItem.addActionListener(this);

	menuItem = new JMenuItem("Firewall Manager",ImageLoader.getImageIcon("images/firewall.png"));
	menu.add(menuItem);
	menuItem.addActionListener(this);

	menuItem = new JMenuItem("Set Public FTP Password");
	menuItem.setActionCommand("FTPPass");
	menu.add(menuItem);
	menuItem.addActionListener(this);

	menuItem = new JMenuItem("Personal Settings");
	menu.add(menuItem);
	menuItem.addActionListener(this);
    
	menuItem = new JMenuItem("Preferences...");
	menuItem.setActionCommand("preferences");
	menuItem.addActionListener(this);
    menu.add(menuItem);

	//TUTORIAL
	menu = new JMenu("Tutorials");
	menuBar.add(menu);

	menuItem = new JMenuItem("First Attack",ImageLoader.getImageIcon("images/attack.png"));
	menu.add(menuItem);
	menuItem.addActionListener(this);
	menuBar.setPreferredSize(new Dimension(menuBar.getPreferredSize().width,30));
	
	JButton left = new JButton(ImageLoader.getImageIcon("images/taskBarLeft.png"));
	left.setContentAreaFilled(false);
	left.setPreferredSize(new Dimension(16,left.getPreferredSize().height));
	left.setActionCommand("Left");
	left.setEnabled(false);

	JButton right = new JButton(ImageLoader.getImageIcon("images/taskBarRight.png"));
	right.setContentAreaFilled(false);
	right.setPreferredSize(new Dimension(16,right.getPreferredSize().height));
	right.setActionCommand("Right");
	right.setEnabled(false);
	
	taskBar = new TaskBar(left,right);
	taskBar.setPreferredSize(new Dimension(menuBar.getPreferredSize().width,25));
	//taskBar.setLayout(new FlowLayout(FlowLayout.LEADING,2,0));
	menu.setBackground(taskBar.getBackground());
	
	//taskBar.setBorder(menuBar.getBorder());
	menuBar.add(taskBar,"growx");
	
	left.addActionListener(taskBar);
	menuBar.add(left,"w 16:16:16");
	
	right.addActionListener(taskBar);
	menuBar.add(right,"w 16:16:16");
	
	frame.setJMenuBar(menuBar);
    }
    
	public void addMinimizedFrame(JDesktopIcon icon){
		taskBar.addIcon(icon);
		menuBar.repaint();
	}
	
	public void removeMinimizedFrame(JDesktopIcon icon){
		taskBar.removeIcon(icon);
		menuBar.repaint();
	
	}
	
    /**
    createPopUPMenu() <br />
    this creates the right click pop up menu
    */
    private void createPopUpMenu(){
	    popUp = new JPopupMenu();
	    panel.add(popUp);
	    
	    
    }
    
    /**
    getColour() <br />
    this returns the desktop colour.
    */
    public Color getColour(){
	    return(desktopColour);
    }
    
    /**
    getIconList() <br />
    this returns all of the desktop icons.
    */
    public HashMap getIconList(){
	    return(icons);
    }
    
    /**
    getPopUp() <br />
    this returns the right click pop up menu.
    */
    public JPopupMenu getPopUp(){
	    return(popUp);
    }
    
    /**
    setPettyCash(float pettyCash) <br />
    this sets the current value for petty cash.
    */
    public void setPettyCash(float pettyCash){
	    if(pettyCash!=this.pettyCash){
		NumberFormat nf = NumberFormat.getCurrencyInstance();  //formats the values to be currency.
		    float moneyChange = pettyCash-this.pettyCash;
		    if(moneyChange>0)
			    statList.add("+"+nf.format(moneyChange));
		    else
			    statList.add(nf.format(moneyChange));
		    this.pettyCash=pettyCash;
		    Insets insets = panel.getInsets();
		    Dimension frameBounds = frame.getSize();
		    
		    //SP.getCashLabel().setText("<html> Petty Cash:  "+nf.format(pettyCash)+"<br>Bank: "+nf.format(getBankMoney())+"</html>");
		    pettyCash*=100;
		    pettyCash=(float)Math.floor(pettyCash);
		    pettyCash/=100;
		    SP.getPettyCash().setText(nf.format(pettyCash));
		    //money.setForeground(new Color(14,236,19));
		    //money.setBounds(frameBounds.width-money.getPreferredSize().width-20,insets.top,money.getPreferredSize().width,money.getPreferredSize().height);
		    //SP.statChanged();
			resetTitle();
	    }
    }
    
    /**
    setBankMoney(float bankMoney) <br />
    this sets the current value for bank money.
    */
    public void setBankMoney(float bankMoney){
	    if(bankMoney!=this.bankMoney){
		    this.bankMoney=bankMoney;
		    Insets insets = panel.getInsets();
		    Dimension frameBounds = frame.getSize();
		    NumberFormat nf = NumberFormat.getCurrencyInstance();
		    bankMoney*=100;
		    bankMoney=(float)Math.floor(bankMoney);
		    bankMoney/=100;
		    SP.getBank().setText(nf.format(bankMoney));
		   // SP.getCashLabel().setText("<html> Petty Cash:  "+nf.format(getPettyCash())+"<br>Bank: "+nf.format(bankMoney)+"</html>");
		    //money.setForeground(new Color(14,236,19));
		    //SP.getCashLabel().setBounds(frameBounds.width-money.getPreferredSize().width-20,insets.top,money.getPreferredSize().width,money.getPreferredSize().height);
		  //  SP.statChanged();
	    }
    }
    
    public void setHackOMeter(int count){
	    if(count!=hackCount){
		    hackCount=count;
		    SP.setHackOMeter(count);
	    }
    }
    
    public void setVoteOMeter(int count){
	    if(count!=voteCount){
		    voteCount=count;
		    SP.setVoteOMeter(count);
	    }
    }
    
    // ASK FOR $ EVERY TIME ATTACKING
    public boolean getShowAttack(){
	    return getBooleanPreference(OptionPanel.ATTACK_CONFIRM_KEY);
    }
    
    public void setShowAttack(boolean showAttack){
        setPreference(OptionPanel.ATTACK_CONFIRM_KEY, ((Boolean)showAttack).toString());
    }
    
    // $ FOR REDIRECT
    public boolean getShowRedirect(){
	    return getBooleanPreference(OptionPanel.REDIRECT_CONFIRM_KEY);
    }

    public void setShowRedirect(boolean showRedirect) {
        setPreference(OptionPanel.REDIRECT_CONFIRM_KEY, ((Boolean)showRedirect).toString());
    }
    
    public boolean getShowAttackDetails(){
        return getBooleanPreference(OptionPanel.ATTACK_DETAILS_KEY);
    }
    
    public void setShowAttackDetails(boolean isSelected) {
        setPreference(OptionPanel.ATTACK_DETAILS_KEY, ((Boolean)isSelected).toString());
    }
    
    public boolean getShowRedirectDetails() {
        return getBooleanPreference(OptionPanel.REDIRECT_DETAILS_KEY);
    }
    
    public boolean getBooleanPreference(String key) {
        String val = getPreference(key);
        if (val.equals("true") || val.equals("")) {
            return true;
        }
        return false;
    }
    
    public void setShowRedirectDetails(boolean isSelected) {
        setPreference(OptionPanel.REDIRECT_DETAILS_KEY, ((Boolean)isSelected).toString());
    }
    
    public boolean[] getPortColumns(){
	    return portColumns;
    }
    

    public boolean getShowScan(){
        return getBooleanPreference(OptionPanel.SCAN_CONFIRM_KEY);
    }

    public void setShowScan(boolean showScan){
        setPreference(OptionPanel.SCAN_CONFIRM_KEY, ((Boolean)showScan).toString());
    }
    
    public void setPortColumns(boolean[] columns){
	    this.portColumns=columns;
	    Object[] o = new Object[columns.length];
	    for(int i=0;i<o.length;i++)
		    o[i] = new Boolean(columns[i]);
	    XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/settings.php","setPortColumns",new Object[]{ip,o});
    }
    
    public void setCountDown(int count){
	    if(count!=countDown&&!counting){
		    counting=true;
		   // System.out.println("Told to start count");
		    countDown=count;
		    countDownLabel.startCount(count);
		    
	    }
	    
    }
	    
    public void statChanged(){
		resetTitle();
	    //SP.statChanged();
    }
    
    public StatsPanel getStatsPanel(){
	    return(SP);
    }
    
    /**
    getPettyCash() <br />
    this returns the current value of petty cash.
    */
    public float getPettyCash(){
	    return(pettyCash);
    }
    
    /**
    getBankMoney() <br />
    this returns the current value of bank money.
    */
    public float getBankMoney(){
	    return(bankMoney);
    }
    
    /**
    getView() <br />
    this returns the view handler.
    */
    public View getView(){
		return(MyView);
    }

    /**
    setDesktopColour(Color newColour) <br />
    this sets the desktop colour
    */
    public void setDesktopColour(Color newColour){
	panel.setBackground(newColour);
    }

    /**
    setFTPOpen(boolean FTPOpen) <br />
    this lets the program know that the FTP program is open, so that the user can't open another one.
    */
    public void setFTPOpen(boolean FTPOpen){
	    this.FTPOpen=FTPOpen;
    }

    /**
            * setDepositOpen(boolean depositOpen) <br />
            * this lets the program know that the deposit program is open, so that the user can't open another one.
            */
    public void setDepositOpen(boolean depositOpen){
	    this.depositOpen=depositOpen;
    }
    
    public boolean getDepositOpen() {
        return this.depositOpen;
    }
    
    /**
             * Get a Vector of all the bank ports this computer has installed.
              * @return Vector
            */
    public Vector getBankPorts() {
        return getPortsOfType(Port.BANKING);
    }
    
    public Vector getRedirectPorts() {
        return getPortsOfType(Port.REDIRECT);
    }
    
    public Vector getAttackPorts() {
        return getPortsOfType(Port.ATTACK);
    }
    
    /**
    * Get all ports of the given type.  "type" comes from Port.java.
    */
    private Vector getPortsOfType(int type) {
        Vector portsOfType = new Vector();
        if (ports != null) {
            for (int i = 0; i < ports.length; i++) {
                if (ports[i].getType() == type && (ports[i].getOn() == true) && (ports[i].getDummy() == false)) {
					String note = ports[i].getNote();
					if(note.length()>10){
						note = note.substring(0,7)+"...";
					}
                    portsOfType.add(ports[i].getNumber() + ": " + ports[i].getNote());
                }
            }
        }
        return portsOfType;
    }
    
    /**
            * Update the deposit window if it's open.  This will be triggered whenever the list of bank ports changes.
            * @return void
            */
    public void updateDepositWindow() {
        // if the deposit window is open, reset the values in the port combo box and repaint it
        if (depositOpen && depositWindow != null) {
            //get the currently selected item
            depositWindow.populateBankPorts();
            depositWindow.setSelectedBankPort();
            depositWindow.validate();
            //depositWindow.repaint();
        }
    }
	
	/**
            * Update the withdraw window if it's open.  This will be triggered whenever the list of bank ports changes.
            * @return void
            */
    public void updateWithdrawWindow() {
        // if the withdraw window is open, reset the values in the port combo box and repaint it
        if (withdrawOpen && withdrawWindow != null) {
            //get the currently selected item
            withdrawWindow.populateBankPorts();
            withdrawWindow.setSelectedBankPort();
            withdrawWindow.validate();
            //depositWindow.repaint();
        }
    }

	/**
            * Update the transfer window if it's open.  This will be triggered whenever the list of bank ports changes.
            * @return void
            */
    public void updateTransferWindow() {
        // if the transfer window is open, reset the values in the port combo box and repaint it
        if (transferOpen && transferWindow != null) {
            //get the currently selected item
            transferWindow.populateBankPorts();
            transferWindow.setSelectedBankPort();
            transferWindow.validate();
            //depositWindow.repaint();
        }
    }
	
	public void updateAttackWindows() {
		Object[] attackPanes = MyAttackPane.values().toArray(new AttackPane[MyAttackPane.size()]);
		for(int i=0;i<attackPanes.length;i++){
			AttackPane attackPane = (AttackPane)attackPanes[i];
			if(attackPane.getType() == AttackPane.ATTACK){
				attackPane.setPorts(getAttackPorts());
			}
			else if(attackPane.getType() == AttackPane.REDIRECT){
				attackPane.setPorts(getRedirectPorts());
			}
		}
	
	
	}
	
    /**
    setTransferOpen(boolean FTPOpen) <br />
    this lets the program know that the transfer program is open, so that the user can't open another one.
    */
    public void setTransferOpen(boolean FTPOpen){
	    this.transferOpen=FTPOpen;
    }

    /**
    setWithdrawOpen(boolean FTPOpen) <br />
    this lets the program know that the withdraw program is open, so that the user can't open another one.
    */
    public void setWithdrawOpen(boolean FTPOpen){
	    this.withdrawOpen=FTPOpen;
    }
    
    public void setPreferencesOpen(boolean isOpen) {
        this.preferencesOpen = isOpen;
    }

    /**
    setScriptEditorOpen(boolean FTPOpen) <br />
    this lets the program know that the script editor is open, so that the user can't open another one.
    */
    public void setScriptEditorOpen(boolean FTPOpen){
	    this.scriptEditorOpen=FTPOpen;
    }

    /**
    setMessagerOpen(boolean FTPOpen) <br />
    this lets the program know that the instant messager is open, so that the user can't open another one.
    */
    public void setMessagerOpen(boolean FTPOpen){
	    this.messagerOpen=FTPOpen;
    }

    /**
    setColourOpen(boolean FTPOpen) <br />
    this lets the program know that the desktop colour program is open, so that the user can't open another one.
    */
    public void setColourOpen(boolean FTPOpen){
	    this.colourOpen=FTPOpen;
    }
    
    /**
    setAttackOpen(boolean FTPOpen) <br />
    this lets the program know that the attack program is open, so that the user can't open another one.
    */
    public void setAttackOpen(boolean FTPOpen){
	    this.attackOpen=FTPOpen;
    }
    
    /**
    setScanOpen(boolean FTPOpen) <br />
    this lets the program know that the port scan program is open, so that the user can't open another one.
    */
    public void setScanOpen(boolean FTPOpen){
	    this.scanOpen=FTPOpen;
    }
    
    /**
    setPortManagementOpen(boolean FTPOpen) <br />
    this lets the program know that the port management program is open, so that the user can't open another one.
    */
    public void setPortManagementOpen(boolean FTPOpen){
	    this.portManagementOpen=FTPOpen;
    }
    
    /**
    setHomeOpen(boolean FTPOpen) <br />
    this lets the program know that the file browser is open, so that the user can't open another one.
    */
    public void setHomeOpen(boolean FTPOpen){
	    this.homeOpen=FTPOpen;
    }

    /**
    setWatchOpen(boolean FTPOpen) <br />
    this lets the program know that the watch manager is open, so that the user can't open another one.
    */
    public void setWatchOpen(boolean FTPOpen){
	    this.watchOpen=FTPOpen;
    }
    
     /**
    setMessageWindowOpen(boolean FTPOpen) <br />
    this lets the program know that the message window is open, so that the user can't open another one.
    */
    public void setMessageWindowOpen(boolean FTPOpen){
	    this.messageWindowOpen=FTPOpen;
    }
    
    /**
    startFTP() <br />
    this opens the FTP program.
    */
    public void startFTP(int type,int port){
		    Insets insets = panel.getInsets();
		    MyFTP = new FTP("FTP 1.0",false,false,true,true,panel,this);
		    panel.add(MyFTP);
		    MyFTP.setLayout(null);
		    MyFTP.setBounds(insets.left+100,insets.top+5,500,450);
		    MyFTP.setVisible(false);
		    MyFTP.setType(type,port);
		    FTPOpen=true;
    }
    
    public void showFTP(int type,int port){
	    if(MyFTP==null)
		    startFTP(type,port);
	    MyFTP.setType(type,port);
	    MyFTP.populate();
	    /*if(!MyFTP.isVisible()){
		    MyFTP.setVisible(true);
	    }*/
	    if(MyFTP.isIcon()){
		    try{
			    MyFTP.setIcon(false);
		    }catch(Exception e){}
	    }
        MyFTP.moveToFront();
    }
    /**
    startDeposit()
    this opens the deposit program.
    */
    public void startDeposit(int port){
	    if(!depositOpen){
    		Insets insets = panel.getInsets();
    		depositWindow = new Deposit("Deposit",false,false,true,true,panel,this,port);
    		panel.add(depositWindow);
    		depositWindow.setLayout(null);
    		depositWindow.setBounds(insets.left+100,insets.top+5,350,140);
    		depositWindow.setVisible(true);
    		depositWindow.populate();
    		depositOpen=true;
	    } else {
            depositWindow.setUsePort(port);
            updateDepositWindow();
            depositWindow.moveToFront();
        }
    }
    
    /**
    startWithdraw()
    this opens the withdraw program.
    */
    public void startWithdraw(int port){
	    if(!withdrawOpen){
    		Insets insets = panel.getInsets();
    		withdrawWindow = new Withdraw("Withdraw",false,false,true,true,panel,this,port);
    		panel.add(withdrawWindow);
    		withdrawWindow.setLayout(null);
    		withdrawWindow.setBounds(insets.left+100,insets.top+5,300,100);
    		withdrawWindow.setVisible(true);
    		withdrawWindow.populate();
    		withdrawOpen=true;
	    } else {
            withdrawWindow.moveToFront();
        }
    }
    
    /**
    startTransfer()
    this opens the transfer program.
    */
    public void startTransfer(int port){
	    if(!transferOpen){
    		Insets insets = panel.getInsets();
    		transferWindow = new Transfer("Transfer",false,false,true,true,panel,this,port);
    		panel.add(transferWindow);
    		transferWindow.setLayout(null);
    		transferWindow.setBounds(insets.left+100,insets.top+5,300,150);
    		transferWindow.setVisible(true);
    		transferWindow.populate();
    		transferOpen=true;
	    } else {
            transferWindow.moveToFront();
        }
    }

    /**
    startScriptEditor()
    this opens the script editor.
    */
    public void startScriptEditor(){
	    Insets insets = panel.getInsets();
	    MyScriptEditor = new ScriptEditor("Script Editor",false,false,true,true,panel,this);
	    panel.add(MyScriptEditor);
	    //MyScriptEditor.setLayout(null);
	    MyScriptEditor.setBounds(insets.left+10,insets.top+10,700,500);
	    MyScriptEditor.setVisible(false);
	    MyScriptEditor.populate();
    }

    public void showScriptEditor(){
	    if(MyScriptEditor==null)
		    startScriptEditor();
	    if(!MyScriptEditor.isVisible())
		    MyScriptEditor.setVisible(true);
	    if(MyScriptEditor.isIcon()){
		    try{
			    MyScriptEditor.setIcon(false);
		    }catch(Exception e){}
	    }
        MyScriptEditor.moveToFront();
    }
	
	public void showScriptEditor(String folder,String filename){
	    if(MyScriptEditor==null)
		    startScriptEditor();
	    if(!MyScriptEditor.isVisible())
		    MyScriptEditor.setVisible(true);
	    if(MyScriptEditor.isIcon()){
		    try{
			    MyScriptEditor.setIcon(false);
		    }catch(Exception e){}
	    }
		MyScriptEditor.openFile(filename,folder);
        MyScriptEditor.moveToFront();
    }
    /**
    startAttack()
    this opens the attack program.
    */
    public void startAttack(){
	    /*Insets insets = panel.getInsets();
	    for(int i=0;i<32;i++){
		    MyAttackPane[i] = new AttackPane("Attack -- Port "+i,false,false,true,true,panel,this,"",0,i, AttackPane.ATTACK);
		    panel.add(MyAttackPane[i]);
		    MyAttackPane[i].setLayout(null);
		    MyAttackPane[i].setBounds(insets.left+10,insets.top+10,400,400);
		    MyAttackPane[i].setVisible(false);
		    MyAttackPane[i].populate();
	    }*/
	
    }
    
     public void startZombieAttack(String ip,int port){
	    Insets insets = panel.getInsets();
	    ZombieAttackPane ZAP= new ZombieAttackPane("Zombie Attack -- "+ip+":"+port,false,false,true,true,panel,this,ip,port,0);
	    panel.add(ZAP);
	    ZAP.setLayout(null);
	    ZAP.setBounds(insets.left+10,insets.top+10,400,400);
	    ZAP.setVisible(true);
	    ZAP.populate();
	    zombieAttacks.put(ip+":"+port,ZAP);
    }
    
    
    
    /**
    showAttack(int port)
    this shows the attack pane when needed.
    */
    public AttackPane showAttack(int port){
	    /*if(MyAttackPane[port]!=null){
		    if(!MyAttackPane[port].isVisible())
			    MyAttackPane[port].setVisible(true);
		    if(MyAttackPane[port].isIcon()){
		    try{
			    MyAttackPane[port].setIcon(false);
		    }catch(Exception e){}
	    }
	    }
	    else{*/
		    Insets insets = panel.getInsets();
			AttackPane attackPane = new AttackPane("Attack -- Port "+port,false,false,true,true,panel,this,"",0,port, AttackPane.ATTACK);
		    panel.add(attackPane);
		    attackPane.setVisible(true);
		    attackPane.populate();
			MyAttackPane.put(""+attackPane.getWindowHandle(),attackPane);
	    //}
		attackPane.moveToFront();
		return(attackPane);
    }
	
	 public AttackPane showRedirect(int port){
	    /*if(MyShippingPane[port]!=null){
		    if(!MyShippingPane[port].isVisible())
			    MyShippingPane[port].setVisible(true);
		    if(MyShippingPane[port].isIcon()){
		    try{
			    MyShippingPane[port].setIcon(false);
		    }catch(Exception e){}
	    }
	    }
	    else{*/
		    Insets insets = panel.getInsets();
		    AttackPane attackPane = new AttackPane("Redirect -- Port "+port,false,false,true,true,panel,this,"",0,port, AttackPane.REDIRECT);
		    panel.add(attackPane);
		    //attackPane.setBounds(insets.left+10,insets.top+10,400,400);
		    attackPane.setVisible(true);
		    attackPane.populate();
	    //}
		attackPane.moveToFront();
		MyAttackPane.put(""+attackPane.getWindowHandle(),attackPane);
		return(attackPane);
    }
	
    public void showAttack(int port,int x,int y){
	    /*if(MyAttackPane[port]!=null){
		    if(!MyAttackPane[port].isVisible())
			    MyAttackPane[port].setVisible(true);
		    if(MyAttackPane[port].isIcon()){
			    try{
				    MyAttackPane[port].setIcon(false);
			    }catch(Exception e){}
		    }
		    MyAttackPane[port].setBounds(x,y,MyAttackPane[port].getWidth(),MyAttackPane[port].getHeight());
	    }
	    else{
		    Insets insets = panel.getInsets();
		    MyAttackPane[port] = new AttackPane("Attack -- Port "+port,false,false,true,true,panel,this,"",0,port, AttackPane.ATTACK);
		    panel.add(MyAttackPane[port]);
		    MyAttackPane[port].setLayout(null);
		    MyAttackPane[port].setBounds(x,y,400,400);
		    MyAttackPane[port].setVisible(true);
		    MyAttackPane[port].populate();
	    }
		MyAttackPane[port].moveToFront();   */
    }
    
    public void showAttack(int port,int x,int y,String ip,int targetPort){
	    /*if(MyAttackPane[port]!=null){
		    if(!MyAttackPane[port].isVisible())
			    MyAttackPane[port].setVisible(true);
		    if(MyAttackPane[port].isIcon()){
			    try{
				    MyAttackPane[port].setIcon(false);
			    }catch(Exception e){}
		    }
		    MyAttackPane[port].setIP(ip);
		    MyAttackPane[port].setTargetPort(targetPort);
		    MyAttackPane[port].setBounds(x,y,MyAttackPane[port].getWidth(),MyAttackPane[port].getHeight());
	    }
	    else{
		    Insets insets = panel.getInsets();
		    MyAttackPane[port] = new AttackPane("Attack -- Port "+port,false,false,true,true,panel,this,ip,targetPort,port, AttackPane.ATTACK);
		    panel.add(MyAttackPane[port]);
		    MyAttackPane[port].setLayout(null);
		    MyAttackPane[port].setBounds(x,y,400,400);
		    MyAttackPane[port].setVisible(true);
		    MyAttackPane[port].populate();
		    MyAttackPane[port].setIP(ip);
			MyAttackPane[port].setTargetPort(targetPort);
	    }
        MyAttackPane[port].moveToFront();*/
    }
	
	 public void showAttack(int port,String ip,int targetPort){
	    /*if(MyAttackPane[port]!=null){
		    if(!MyAttackPane[port].isVisible())
			    MyAttackPane[port].setVisible(true);
		    if(MyAttackPane[port].isIcon()){
			    try{
				    MyAttackPane[port].setIcon(false);
			    }catch(Exception e){}
		    }
		    MyAttackPane[port].setIP(ip);
		    MyAttackPane[port].setTargetPort(targetPort);
		    //MyAttackPane[port].setBounds(x,y,MyAttackPane[port].getWidth(),MyAttackPane[port].getHeight());
	    }
	    else{*/
		    Insets insets = panel.getInsets();
		    AttackPane attackPane = new AttackPane("Attack -- Port "+port,false,false,true,true,panel,this,"",targetPort,port, AttackPane.ATTACK);
		    panel.add(attackPane);
		    attackPane.setVisible(true);
		    attackPane.populate();
		    attackPane.setIP(ip);
			attackPane.setTargetPort(targetPort);
	    //}
        attackPane.moveToFront();
		MyAttackPane.put(""+attackPane.getWindowHandle(),attackPane);
    }
    
    public void beginAttack(int port){
		//MyAttackPane[port].attack();    
    }
	
	public void showRedirect(int port,int x,int y){
	    /*if(MyShippingPane[port]!=null){
		    if(!MyShippingPane[port].isVisible())
			    MyShippingPane[port].setVisible(true);
		    if(MyShippingPane[port].isIcon()){
			    try{
				    MyShippingPane[port].setIcon(false);
			    }catch(Exception e){}
		    }
		    MyShippingPane[port].setBounds(x,y,MyShippingPane[port].getWidth(),MyShippingPane[port].getHeight());
	    }
	    else{
		    Insets insets = panel.getInsets();
		    MyShippingPane[port] = new AttackPane("Redirect -- Port "+port,false,false,true,true,panel,this,"",0,port, AttackPane.REDIRECT);
		    panel.add(MyShippingPane[port]);
		    MyShippingPane[port].setLayout(null);
		    MyShippingPane[port].setBounds(x,y,400,400);
		    MyShippingPane[port].setVisible(true);
		    MyShippingPane[port].populate();
	    }
		MyShippingPane[port].moveToFront();   */
    }
    
    public void showRedirect(int port,int x,int y,String ip,int targetPort){
	    /*if(MyShippingPane[port]!=null){
		    if(!MyShippingPane[port].isVisible())
			    MyShippingPane[port].setVisible(true);
		    if(MyShippingPane[port].isIcon()){
			    try{
				    MyShippingPane[port].setIcon(false);
			    }catch(Exception e){}
		    }
		    MyShippingPane[port].setIP(ip);
		    MyShippingPane[port].setTargetPort(targetPort);
		    MyShippingPane[port].setBounds(x,y,MyShippingPane[port].getWidth(),MyShippingPane[port].getHeight());
	    }
	    else{
		    Insets insets = panel.getInsets();
		    MyShippingPane[port] = new AttackPane("Redirect -- Port "+port,false,false,true,true,panel,this,ip,targetPort,port, AttackPane.REDIRECT);
		    panel.add(MyShippingPane[port]);
		    MyShippingPane[port].setLayout(null);
		    MyShippingPane[port].setBounds(x,y,400,400);
		    MyShippingPane[port].setVisible(true);
		    MyShippingPane[port].populate();
		    MyShippingPane[port].setIP(ip);
			MyShippingPane[port].setTargetPort(targetPort);
	    }
        MyShippingPane[port].moveToFront();*/
    }
	
	 public void showRedirect(int port,String ip,int targetPort){
	    /*if(MyShippingPane[port]!=null){
		    if(!MyShippingPane[port].isVisible())
			    MyShippingPane[port].setVisible(true);
		    if(MyShippingPane[port].isIcon()){
			    try{
				    MyShippingPane[port].setIcon(false);
			    }catch(Exception e){}
		    }
		    MyShippingPane[port].setIP(ip);
		    MyShippingPane[port].setTargetPort(targetPort);
		    //MyShippingPane[port].setBounds(x,y,MyShippingPane[port].getWidth(),MyShippingPane[port].getHeight());
	    }
	    else{*/
		    Insets insets = panel.getInsets();
		    AttackPane attackPane = new AttackPane("Redirect -- Port "+port,false,false,true,true,panel,this,"",targetPort,port, AttackPane.REDIRECT);
		    panel.add(attackPane);
		    attackPane.setVisible(true);
		    attackPane.populate();
		    attackPane.setIP(ip);
			attackPane.setTargetPort(targetPort);
			MyAttackPane.put(""+attackPane.getWindowHandle(),attackPane);
	    //}
        attackPane.moveToFront();
    }
    
    public void beginRedirect(int port){
		/*if(MyShippingPane[port]!=null){
			MyShippingPane[port].attack();    
		}*/
    }
    
    
    public AttackPane getDefaultAttackPane(){
	    /*if(MyAttackPane[defaultAttackPort]==null){
		    Insets insets = panel.getInsets();
		    MyAttackPane[defaultAttackPort] = new AttackPane("Attack -- Port "+defaultAttackPort,false,false,true,true,panel,this,"",0,defaultAttackPort, AttackPane.ATTACK);
		    panel.add(MyAttackPane[defaultAttackPort]);
		    MyAttackPane[defaultAttackPort].setLayout(null);
		    MyAttackPane[defaultAttackPort].setBounds(insets.left+10,insets.top+10,400,400);
		    MyAttackPane[defaultAttackPort].setVisible(false);
		    MyAttackPane[defaultAttackPort].populate();
	    }*/
	    //return(MyAttackPane[defaultAttackPort]);
		return(null);
    }
	
	public AttackPane getDefaultShippingPane(){
	    /*if(MyShippingPane[defaultRedirectPort]==null){
		    Insets insets = panel.getInsets();
		    MyShippingPane[defaultRedirectPort] = new AttackPane("Redirect -- Port "+defaultRedirectPort,false,false,true,true,panel,this,"",0,defaultRedirectPort, AttackPane.REDIRECT);
		    panel.add(MyShippingPane[defaultRedirectPort]);
		    MyShippingPane[defaultRedirectPort].setLayout(null);
		    MyShippingPane[defaultRedirectPort].setBounds(insets.left+10,insets.top+10,400,400);
		    MyShippingPane[defaultRedirectPort].setVisible(false);
		    MyShippingPane[defaultRedirectPort].populate();
	    }
	    return(MyShippingPane[defaultRedirectPort]);*/
		return(null);
    }
	
	public void startNetwork() {
		if(MP==null){
		    MP = new MapPanel(this,400,400,50,50);
			panel.add(MP);
		}
		MP.setVisible(true);
		if(MP.isIcon()){
			try{
				MP.setIcon(false);
			}catch(Exception e){}
		}
        MP.moveToFront();
    }
    
    public void startPreferences() {
        if (!preferencesOpen) {
            setPreferencesOpen(true);
            Dimension d = panel.getSize();
            optionPanel = new OptionPanel(this, d);
            panel.add(optionPanel);
            optionPanel.moveToFront();
            optionPanel.setVisible(true);
        } else {
            optionPanel.moveToFront();
        }
    }
    
		    
    /**
    startScan()
    this opens the port scan program.
    */
    public void startScan(String ip){
	    if(!scanOpen){
		    Insets insets = panel.getInsets();
		    MyPortScan = new PortScan("Port Scan",false,false,true,true,panel,this,ip,0);
		    panel.add(MyPortScan);
		    MyPortScan.setLayout(null);
		    MyPortScan.setBounds(insets.left+10,insets.top+10,500,400);
		    MyPortScan.setVisible(true);
		    scanOpen=true;
	    }
	    else{
		    if(MyPortScan.isIcon()){
		        try{
                    MyPortScan.setIcon(false);
		    	}catch(Exception e){}
	    	    }
		    MyPortScan.setIPValue(ip);
            MyPortScan.moveToFront();
	    }
        
    }
    
    /**
    startPortManagement()
    this opens the port management program.
    */
    public void startPortManagement(){
	    if(!portManagementOpen){
		    Insets insets = panel.getInsets();
		    portManagement = new PortManagement("Port Management",false,false,true,true,panel,this);
		    panel.add(portManagement);
		    portManagement.setLayout(null);
		    portManagement.setBounds(insets.left+10,insets.top+10,850,400);
		    portManagement.setVisible(true);
		    portManagement.populate();
		    portManagementOpen=true;
	    }
	    else{
		    if(portManagement.isIcon()){
			    try{
				    portManagement.setIcon(false);
			    }catch(Exception e){}
		    }
            portManagement.moveToFront();
	    }
    }
    
     public void startPortManagement(int x, int y){
	    if(!portManagementOpen){
		    Insets insets = panel.getInsets();
		    portManagement = new PortManagement("Port Management",false,false,true,true,panel,this);
		    panel.add(portManagement);
		    portManagement.setLayout(null);
		    portManagement.setBounds(x,y,780,400);
		    portManagement.setVisible(true);
		    portManagement.populate();
		    portManagementOpen=true;
	    }
	    else{
		    if(portManagement.isIcon()){
			    try{
				    portManagement.setIcon(false);
			    }catch(Exception e){}
		    }
            portManagement.moveToFront();
	    }
    }
    
    /**
    startHome()
    this opens the file browser.
    */
    public void startHome(){
	    if(!homeOpen){
		    Insets insets = panel.getInsets();
		    MyHome = new Home("File Browser",false,false,true,true,panel,this);
		    panel.add(MyHome);
		    MyHome.setBounds(insets.left+10,insets.top+10,750,500);
		    MyHome.setVisible(false);
		    MyHome.populate();
	    }
    }
    
    public void showFirewallBrowser(){
	    if(firewallBrowser==null){
		    firewallBrowser = new FirewallBrowser(this);
			panel.add(firewallBrowser);
		    firewallBrowser.setBounds(40,40,750,500);
		}
		firewallBrowser.setVisible(true);
	    if(firewallBrowser.isIcon()){
		    try{
			    firewallBrowser.setIcon(false);
		    }catch(Exception e){}
	    }
		firewallBrowser.startUp();
        firewallBrowser.moveToFront();
    }
	
	public void showHome(){
	    if(MyHome==null)
		    startHome();
	    MyHome.setVisible(true);
	    if(MyHome.isIcon()){
		    try{
			    MyHome.setIcon(false);
		    }catch(Exception e){}
	    }
	    MyHome.startUp();
        MyHome.moveToFront();
    }
    
    
    /**
    startWatchManager()
    this opens the watch manager.
    */
    public void startWatchManager(){
	    if(!watchOpen){
		    Insets insets = panel.getInsets();
		    MyWatchManager = new WatchManager("Watch Manager",false,false,true,true,panel,this);
		    panel.add(MyWatchManager);
		    MyWatchManager.setLayout(null);
		    MyWatchManager.setBounds(insets.left+10,insets.top+10,850,420);
		    MyWatchManager.setVisible(true);
		    MyWatchManager.populate();
		    watchOpen=true;
	    }else{
		    if(MyWatchManager.isIcon()){
			    try{
				    MyWatchManager.setIcon(false);
			    }catch(Exception e){}
		    }
            MyWatchManager.moveToFront();
	    }
    }

    /**
    startWebsiteEditor()
    this opens the watch manager.
    */
    public void startWebsiteEditor(){
	    Insets insets = panel.getInsets();
	    MyWebsiteEditor = new WebsiteEditor("Website Editor",false,false,true,true,panel,this);
	    panel.add(MyWebsiteEditor);
	    MyWebsiteEditor.setLayout(null);
	    MyWebsiteEditor.setBounds(insets.left+10,insets.top+10,500,400);
	    MyWebsiteEditor.setVisible(false);
	    MyWebsiteEditor.populate();
    }
    public void showWebsiteEditor(){
	    if(MyWebsiteEditor==null)
		    startWebsiteEditor();
	    MyWebsiteEditor.setVisible(true);
	    if(MyWebsiteEditor.isIcon()){
		    try{
			    MyWebsiteEditor.setIcon(false);
		    }catch(Exception e){}
	    }
        MyWebsiteEditor.moveToFront();
	    siteRequest=WEBSITE_EDITOR;
	    Object objects[] = {encryptedIP};
	    MyView.setFunction("requestpage");
	    MyView.addFunctionCall(new RemoteFunctionCall(0,"requestpage",objects));
    }
    
    public void startWebBrowser(){
	    Insets insets = panel.getInsets();
	    MyWebBrowser = new WebBrowser("Web Browser",false,false,true,true,panel,this);
	    panel.add(MyWebBrowser);
	    MyWebBrowser.setLayout(null);
	    MyWebBrowser.setBounds(insets.left+3,insets.top+3,780,520);
	    MyWebBrowser.setVisible(false);
	    MyWebBrowser.populate();
    }
    
    public void showWebBrowser(){
	    if(MyWebBrowser==null)
		    startWebBrowser();
	    if(!MyWebBrowser.isVisible()){
		    MyWebBrowser.setVisible(true);
	    }
	    if(MyWebBrowser.isIcon()){
		    try{
			    MyWebBrowser.setIcon(false);
		    }catch(Exception e){}
	    }
        MyWebBrowser.moveToFront();
	    frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
	
	public void showWebBrowser(String ip){
	    if(MyWebBrowser==null)
		    startWebBrowser();
	    if(!MyWebBrowser.isVisible()){
		    MyWebBrowser.setVisible(true);
	    }
	    if(MyWebBrowser.isIcon()){
		    try{
			    MyWebBrowser.setIcon(false);
		    }catch(Exception e){}
	    }
        MyWebBrowser.moveToFront();
	    frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		MyWebBrowser.setLink(ip);
    }
	
	
	
	public void startHacktendoCreator(){
		if(hacktendoCreator==null){
			hacktendoCreator = new HacktendoCreator(this);
		}
		if(!hacktendoCreator.isVisible())
			hacktendoCreator.setVisible(true);
		if(hacktendoCreator.isIcon()){
		    try{
			    hacktendoCreator.setIcon(false);
		    }catch(Exception e){}
	    }
        hacktendoCreator.moveToFront();
	}
	
	public void startHacktendoPlayer(){
		if(hacktendoPlayer==null){
			hacktendoPlayer = new HacktendoPlayer(this);
		}
		hacktendoPlayer.setDebug(false);

		if(!hacktendoPlayer.isVisible())
			hacktendoPlayer.setVisible(true);
		if(hacktendoPlayer.isIcon()){
		    try{
			    hacktendoPlayer.setIcon(false);
		    }catch(Exception e){}
	    }
        hacktendoPlayer.moveToFront();
		
	}
	
	public void startHacktendoPlayerDebug(String xml){
		if(hacktendoPlayer==null){
			hacktendoPlayer = new HacktendoPlayer(this);
		}
		hacktendoPlayer.setDebug(true);
		if(!hacktendoPlayer.isVisible())
			hacktendoPlayer.setVisible(true);
		if(hacktendoPlayer.isIcon()){
		    try{
			    hacktendoPlayer.setIcon(false);
		    }catch(Exception e){}
	    }
		hacktendoPlayer.openGame(xml);	
        hacktendoPlayer.moveToFront();
		
	}
			
	public void startHacktendoPlayer(String xml,String fileName,HashMap LoadFile){
		if(hacktendoPlayer==null){
			hacktendoPlayer = new HacktendoPlayer(this);
		}
		if(!hacktendoPlayer.isVisible())
			hacktendoPlayer.setVisible(true);
		if(hacktendoPlayer.isIcon()){
		    try{
			    hacktendoPlayer.setIcon(false);
		    }catch(Exception e){}
	    }
		hacktendoPlayer.openGame(xml,fileName,LoadFile);
        hacktendoPlayer.moveToFront();
	}
    
    public void goToStore(){
	    if(MyWebBrowser==null)
		    startWebBrowser();
	    if(!MyWebBrowser.isVisible()){
		    MyWebBrowser.setVisible(true);
	    }
	    if(MyWebBrowser.isIcon()){
		    try{
			    MyWebBrowser.setIcon(false);
		    }catch(Exception e){}
	    }
	    MyWebBrowser.goToStore();
        MyWebBrowser.moveToFront();
	    /*if(tutorialWindow.getStep()==0){
		    //tutorialWindow.nextStep();
	    }*/
    }
    /**
    showMessage(String message)
    shows a message received from the server.
    */
    public void showMessage(Object message){
	    //MyMessageWindow.setMessage(newMessage);
		String newMessage = "";
		String originalMessage = "";
		int type = 0;
        String preferenceKey = "";
		Object[] messageArray = null;
		if(message instanceof String){
			newMessage = (String)message;
		}
		else if(message instanceof Object[]){
			messageArray = (Object[])message;
			newMessage = (String)messageArray[0];
			originalMessage = newMessage;
			type = (Integer)messageArray[1];
            // right now, only the popus with a checkbox confirmation have 3 objects
            preferenceKey = (String)messageArray[2];
		}
	    boolean show=true;
		if( type != ATTACK_MESSAGE && type != REDIRECT_MESSAGE) {
			int index = newMessage.indexOf(";");
			//System.out.println(index);
			if(index==3){
				String[] s = newMessage.split(";");
				if(s[0].equals("pop")){
					//System.out.println("Popup Message");
					newMessage="";
					for(int i=1;i<s.length;i++){
						newMessage+=s[i];
					}
					show=false;
					PopUp PU = new PopUp(this,newMessage);
					panel.add(PU);
					PU.setVisible(true);
					PU.moveToFront();
				}
			}
			/*else if(index==5){
				String[] s = newMessage.split(";");
			if(s[0].equals("sound")){
				Sound.play(Integer.parseInt(s[1]));
				show=false;
			}
			}*/
			else if(index==7){
				String[] s = newMessage.split(";");
				if(s[0].equals("network")){
					moveToNetwork(s[1]);
					show=false;
				}
			}
			if(show){
				Calendar c = Calendar.getInstance();
				int hour=c.get(Calendar.HOUR);
				int minute=c.get(Calendar.MINUTE);
				int seconds=c.get(Calendar.SECOND);
				int ampm=c.get(Calendar.AM_PM);
				String pm="PM";
				String second;
				String minutes;
				if(seconds<10)
					second="0"+seconds;
				else
					second=""+seconds;
				if(minute<10)
					minutes="0"+minute;
				else
					minutes=""+minute;
				if(ampm==0)
					pm="AM";
				if(hour==0)
					hour=12;
				//System.out.println(newMessage);
                newMessage = newMessage.replaceAll("\\\n", " ");
				newMessage = "("+hour+":"+minutes+":"+second+" "+pm+") "+newMessage;
				MsgOutChannelText MO=new MsgOutChannelText(username,ChatController.GAME_MESSAGES_NAME,"Admin",newMessage);
				try{
					MyChatController.processMessage(MO);
				}catch(Exception e){e.printStackTrace();}
			}
		}
		if(type == POPUP_ERROR){
			JOptionPane.showMessageDialog(frame,originalMessage,"Error",JOptionPane.ERROR_MESSAGE);
		}
		else if(type == POPUP_MESSAGE){
//System.out.println("POPUPMESSAGE: " + preferenceKey + ": " + getBooleanPreference(preferenceKey));
			if(!preferenceKey.equals("")){
				if (getBooleanPreference(preferenceKey)) {
					String title = "HackOS Confirmation";
					Object[] values = ConfirmationPanel.showOKDialog(frame, title, originalMessage);
					boolean checked = (boolean)(Boolean)values[1];
					if (checked == true) {
	//System.out.println("SUCKA BE CHECKED! (prf = " + preferenceKey);
						setPreference(preferenceKey, "false");
					}
				}
			}else{
				JOptionPane.showMessageDialog(frame, originalMessage);
			}
		}
		else if(type == ATTACK_MESSAGE || type == REDIRECT_MESSAGE){
			Object[] portInfo = (Object[])messageArray[3];
			if(portInfo != null){
				int windowHandle = (Integer)portInfo[0];
				if(MyAttackPane.get(""+windowHandle)!=null){
					AttackPane attackPane = (AttackPane)MyAttackPane.get(""+windowHandle);
					attackPane.addMessage(originalMessage);
					
				}
			}
		
		}
		/*else if(type == REDIRECT_MESSAGE){
			Object[] portInfo = (Object[])messageArray[3];
			if(portInfo!=null){
				int windowHandle = (Integer)portInfo[0];
				if(MyShippingPane[port]!=null){
					MyShippingPane[port].addMessage(originalMessage);
				}
			
			}
		}*/
	    
    }
    
    
    public void showZombieMessage(float damage,int port,String ip){
	    //System.out.println("Received Zombie Damage Message");
	    if(zombieAttacks.get(ip+":"+port)!=null){
		    ((ZombieAttackPane)zombieAttacks.get(ip+":"+port)).addMessage(damage);
	    }
    }
    
    /**
    showAttackMessage(int damage,int port)
    shows a damage message in an attack window.
    */
    public void showAttackMessage(float damage,int windowHandle,boolean firewall,boolean mining){
		//System.out.println("Received attack message: "+damage+" "+port+" "+firewall+" "+mining);
		//if(!mining){
			if(MyAttackPane.get(""+windowHandle)!=null){
				AttackPane attackPane = (AttackPane)MyAttackPane.get(""+windowHandle);
				attackPane.addMessage(damage,firewall);
			}else{
				/*Insets insets = panel.getInsets();
				MyAttackPane[port] = new AttackPane("Attack -- Port "+port,false,false,true,true,panel,this,"",0,port, AttackPane.ATTACK);
				panel.add(MyAttackPane[port]);
				MyAttackPane[port].setLayout(null);
				MyAttackPane[port].setBounds(insets.left+10,insets.top+10,400,400);
				MyAttackPane[port].setVisible(false);
				MyAttackPane[port].populate();
				MyAttackPane[port].addMessage(damage,firewall);*/
			}
		/*}
		else{
			if(MyShippingPane[port]!=null){
				MyShippingPane[port].addMessage(damage,firewall);
			}else{
				Insets insets = panel.getInsets();
				MyShippingPane[port] = new AttackPane("Redirect -- Port "+port,false,false,true,true,panel,this,"",0,port, AttackPane.REDIRECT);
				panel.add(MyShippingPane[port]);
				MyShippingPane[port].setLayout(null);
				MyShippingPane[port].setBounds(insets.left+10,insets.top+10,400,400);
				MyShippingPane[port].setVisible(false);
				MyShippingPane[port].populate();
				MyShippingPane[port].addMessage(damage,firewall);
			}
			
		}*/
	    
    }
	
	public void setAttacking(int windowHandle,boolean attacking){
		AttackPane attackPane = (AttackPane)MyAttackPane.get(""+windowHandle);
		if(attackPane!=null){
			attackPane.setAttacking(attacking);
		}
	}
    
    public void showChoices(Object[] choices){
	    for(int i=0;i<choices.length;i++){
		    Object[] o = (Object[])choices[i];
		    ShowChoices SC = new ShowChoices((int)(Integer)o[2],(String)o[0],(int)(Integer)o[1],(int)(Integer)o[3],this);
		    panel.add(SC);
		    SC.setBounds(10,10,280,150);
		    SC.setVisible(true);
	    }
    }

    public void startHelp(){
	    Insets insets = panel.getInsets();
	    MyHelp = new Help("Help",false,false,true,true,panel,this);
	    MyHelp.setBounds(insets.left+10,insets.top+10,700,500);
	    MyHelp.populate();
	    panel.add(MyHelp);
	    MyHelp.setVisible(true);
	    MyHelp.moveToFront();
    }
    
    public void showHelp(){
	    if(MyHelp==null)
		    startHelp();
	    if(!MyHelp.isVisible()){
		    MyHelp.setVisible(true);
	    }
	    if(MyHelp.isIcon()){
		    try{
			    MyHelp.setIcon(false);
		    }catch(Exception e){}
	    }
	    frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
	
	public void showEquipmentManager(){
		if(EQ==null)
			EQ = new Equipment(this);
		if(!EQ.isVisible()){
			EQ.setVisible(true);
		}
		if(EQ.isIcon()){
			try{
				EQ.setIcon(false);
			}catch(Exception e){}
		}
        EQ.moveToFront();
	}
			
    
    public TutorialWindow getTutorial(){
	    return tutorialWindow;
    }
    
    public void setFrameTitle(String message){
	    frame.setTitle("Hack Wars - "+ip+" - "+message);
    }
	
	
	/**
	convenience functions for command prompt / bash linker.
    **/
	
	public void portOnOff(int port,boolean on){
		Object objects[] = {getEncryptedIP(),port,on};
		MyView.setFunction("portonoff");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"portonoff",objects));
	}
	
	public void watchOnOff(int watchID,boolean on){
		Object objects[] = {getEncryptedIP(),new Integer(watchID),new Boolean(on)};
		MyView.setFunction("setwatchonoff");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"setwatchonoff",objects));
	}
	
	
    /**
    actionPerformed(ActionEvent e)
    runs the actions of the main menu.
    */
    public void actionPerformed(ActionEvent e){
	    if(e.getActionCommand().equals("Exit")){
		    //System.exit(0);
		    MyView.exitProgram();
		    frame.dispose();
		    //MyViewRelation.dispose();
		    
	    }
	    if(e.getActionCommand().equals("Shop FTP")){
		    startFTP(0,defaultFTPPort);
	    }
	    if(e.getActionCommand().equals("Public FTP")){
		    startFTP(1,defaultFTPPort);
	    }
	    if(e.getActionCommand().equals("Deposit")){
		    startDeposit(defaultBankPort);
	    }
	    if(e.getActionCommand().equals("Withdraw")){
		    startWithdraw(defaultBankPort);
	    }
	    if(e.getActionCommand().equals("Transfer")){
		    startTransfer(defaultBankPort);
	    }
	    if(e.getActionCommand().equals("Script Editor")){
		    showScriptEditor();
	    }
	    if(e.getActionCommand().equals("Help")){
		    frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		    showHelp();
	    }
	    if(e.getActionCommand().equals("Attack Port")){
		    showAttack(defaultAttackPort);
	    }
		if(e.getActionCommand().equals("Redirect Port")){
		    showRedirect(defaultRedirectPort);
	    }
	    if(e.getActionCommand().equals("Port Scan")){
		    startScan("");
	    }
	    if(e.getActionCommand().equals("Port Management")){
		    startPortManagement();
	    }
	    if(e.getActionCommand().equals("Home")){
		    showHome();
	    }
		if(e.getActionCommand().equals("Firewall Manager")){
			showFirewallBrowser();
		}
	    if(e.getActionCommand().equals("Watch Manager")){
		    startWatchManager();
	    }
	    if(e.getActionCommand().equals("Site Editor")){
		    showWebsiteEditor();
	    }
	    if(e.getActionCommand().equals("Web Browser")){
		    frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		    showWebBrowser();
	    }
	    if(e.getActionCommand().equals("Store")){
		    goToStore();
	    }
		if(e.getActionCommand().equals("Network")){
			startNetwork();
		}
	    if(e.getActionCommand().equals("FTPPass")){
		    String answer = (String)JOptionPane.showInputDialog(
                    panel,
                    "New Password:",
                    "Set FTP Password",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "");
		    if(answer!=null){
			   // System.out.println(answer);
			    Object[] objects = {encryptedIP,answer};
			    MyView.setFunction("setftppassword");
			    MyView.addFunctionCall(new RemoteFunctionCall(0,"setftppassword",objects));
		    }
	    }
        if (e.getActionCommand().equals("preferences")) {
            startPreferences();
        }
	    if(e.getActionCommand().equals("Log Window")){
		    startLogWindow();
	    }
	    if(e.getActionCommand().equals("Personal Settings")){
		    showPersonalSettings(username);
	    }
	    if(e.getActionCommand().equals("Options")){
		    showOptions(username);
	    }
	    if(e.getActionCommand().equals("Zombie Attack")){
		    ZombieAttackDialog ZAD = new ZombieAttackDialog(this);		    
	    }
	    if(e.getActionCommand().equals("Create Bounty")){
		    BountyWindow BW = new BountyWindow(this);
	    }
	    if(e.getActionCommand().equals("Equipment Manager")){
		    showEquipmentManager();
	    }
	    if(e.getActionCommand().equals("Hacktendo Game Creator")){
		 startHacktendoCreator();
	    }
		if(e.getActionCommand().equals("Hacktendo Game Player")){
			startHacktendoPlayer();
		}
        if(e.getActionCommand().equals("First Attack")){
			setTutorial(true);
		}
		if(e.getActionCommand().equals("Command Prompt")){
			CommandPrompt CP = new CommandPrompt(this);
		
		}

    }
    
    public void windowOpened(WindowEvent e){
    }
    
    public void windowClosing(WindowEvent e){
	    MyView.exitProgram();
	    //MyViewRelation.dispose();
	    //System.out.println("Window Closing");
    }
    
    public void windowClosed(WindowEvent e){
	   // System.out.println("Window Closed");
    }
    
    public void windowIconified(WindowEvent e){
    }
    
    public void windowDeiconified(WindowEvent e){
    }
    public void windowActivated(WindowEvent e){
    }
    public void windowDeactivated(WindowEvent e){
    }
    
    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
        
    }

    public void componentResized(ComponentEvent e) {
	    Rectangle frameSize = frame.getBounds();
	    Dimension frameBounds = frame.getSize();
	    int change = (frameSize.height-height)/2;
	    if(frameBounds.width<1024)
		    frame.setSize(1024,frameBounds.height);
	    if(frameBounds.height<600)
		    frame.setSize(frameBounds.width,600);
            panel.setBounds(0,0,frameSize.width,frameSize.height-50);
	    if(MyMessageWindow!=null)
		    MyMessageWindow.move(frameSize);
	    if(SP!=null)
		    SP.move(frameBounds);
	    if(countDownLabel!=null)
		    countDownLabel.setBounds(frameSize.width-countDownLabel.getPreferredSize().width-30,frameSize.height-210-change-countDownLabel.getPreferredSize().height,countDownLabel.getPreferredSize().width,countDownLabel.getPreferredSize().height);
	    if(MyViewMain!=null){
		    MyViewMain.redoSize(new Dimension(frameSize.width,160+change));
	    	    MyViewMain.setBounds(0,frameSize.height-225-change,frameSize.width,MyViewMain.getBounds().height);
		    MyViewMain.validate();
	    }
	    //if(sep!=null){
		    //sep.setBounds(0,frameSize.height-212-change,frameSize.width,5);
	    //}
	    height=frameSize.height;
	    width=frameSize.width;
		if(taskBar!=null){
			taskBar.enableButtons();
		}
    }

    public void componentShown(ComponentEvent e) {

    }
    
}
