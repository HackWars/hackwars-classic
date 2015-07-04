package GUI;
/**

FTP.java
this is a ftp program gui.
*/
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import Game.*;
import View.*;
import Assignments.*;

public class FTP  extends Application implements UndoableEditListener,FocusListener{
	//data
	public final static int SHOP=0;
	public final static int PUBLIC=1;
	public final static int PUT=0;
	public final static int GET=1;
	private JDesktopPane mainPanel=null;
	private JTextArea messageWindow = new JTextArea();
	private JList list=null;
	private JList destList=null;
	private DefaultListModel listModel = new DefaultListModel();
	private DefaultListModel destListModel = new DefaultListModel();
	private Hacker MyHacker=null;
	private Object directory[],shownDirectory[],transferDirectory[],shownTransferDirectory[];
	private int type;
	private View MyView;
	private FTPCellRenderer renderer,transferRenderer;
	private int requestedFolders=0;
	private String requests[] = new String[4];
	private String pathFrom="";
	private JTextField sourceDir,ipField1,ipField2,ipField3,ipField4,passField;
	private int port;
	private FTPMouseListener mouseListener,transferMouseListener;
	private int sizeExpected=-1;
	private String targetIP,pass;
	private int transferType;
	private IPPanel ipPanel;

	public FTP(String name,boolean resize,boolean max,boolean close,boolean iconify,JDesktopPane mainPanel,Hacker MyHacker){

		this.setTitle(name);
		this.setResizable(resize);
		this.setMaximizable(max);
		this.setClosable(close);
		this.setIconifiable(iconify);
		this.addInternalFrameListener(this);
		this.mainPanel=mainPanel;
		this.MyHacker=MyHacker;
		this.directory=MyHacker.getCurrentDirectory();
		MyView = MyHacker.getView();
		//setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		MyHacker.setFTP(this);
		this.setFrameIcon(ImageLoader.getImageIcon("images/ftp.png"));
	}

	
	/**
	populate()
	create the main window and all of its components.
	*/
	public void populate(){
		shownDirectory=null;		//reset the directory that is shown in the source list
		shownTransferDirectory=null;
		listModel = new DefaultListModel();
		destListModel = new DefaultListModel();
		Insets iFrameInsets = this.getInsets();
		Rectangle iFrameSize = this.getBounds();
		String folder="";
		int height=iFrameInsets.top+10;
		if(type==SHOP)
			folder="Store";
		if(type==PUBLIC){
			folder="Public";
			int position=iFrameInsets.left+10;
			JLabel publabel = new JLabel("IP: ");
			add(publabel);
			publabel.setBounds(position,height,publabel.getPreferredSize().width,publabel.getPreferredSize().height);
			position+=publabel.getPreferredSize().width+5;
			
			/*ipField1 = new JTextField(3);
			this.add(ipField1);
			Dimension d = ipField1.getPreferredSize();
			ipField1.setBounds(position,height,d.width,d.height);
			position+=d.width;
			
			JLabel dot = new JLabel(".");
			d = dot.getPreferredSize();
			add(dot);
			dot.setBounds(position,height,d.width,d.height);
			position+=d.width;
			
			ipField2 = new JTextField(3);
			this.add(ipField2);
			d = ipField2.getPreferredSize();
			ipField2.setBounds(position,height,d.width,d.height);
			position+=d.width;
			
			dot = new JLabel(".");
			d = dot.getPreferredSize();
			add(dot);
			dot.setBounds(position,height,d.width,d.height);
			position+=d.width;
			
			ipField3 = new JTextField(2);
			this.add(ipField3);
			d = ipField3.getPreferredSize();
			ipField3.setBounds(position,height,d.width,d.height);
			position+=d.width;
			
			dot = new JLabel(".");
			d = dot.getPreferredSize();
			add(dot);
			dot.setBounds(position,height,d.width,d.height);
			position+=d.width;
			
			ipField4 = new JTextField(3);
			this.add(ipField4);
			d = ipField4.getPreferredSize();
			ipField4.setBounds(position,height,d.width,d.height);
			position+=d.width+30;
			
			ipField1.getDocument().addUndoableEditListener(this);
			ipField2.getDocument().addUndoableEditListener(this);
			ipField3.getDocument().addUndoableEditListener(this);
			ipField4.getDocument().addUndoableEditListener(this);
			ipField1.addFocusListener(this);
			ipField2.addFocusListener(this);
			ipField3.addFocusListener(this);
			ipField4.addFocusListener(this);*/
			
			ipPanel = new IPPanel(MyHacker);
			add(ipPanel);
			Dimension d = ipPanel.getPreferredSize();
			ipPanel.setBounds(position,height,d.width,d.height);
			position += d.width;
			
			publabel = new JLabel("Password: ");
			add(publabel);
			publabel.setBounds(position,height,publabel.getPreferredSize().width,publabel.getPreferredSize().height);
			position+=publabel.getPreferredSize().width+5;
			
			passField = new JPasswordField(10);
			add(passField);
			d = passField.getPreferredSize();
			passField.setBounds(position,height,d.width,d.height);
			position+=d.width+5;
			
			JButton goButton = new JButton("Go");
			add(goButton);
			goButton.addActionListener(this);
			goButton.setBounds(position,height-5,goButton.getPreferredSize().width,goButton.getPreferredSize().height);
			height+=d.height+10;
			
		}
		sourceDir = new JTextField("/home/",15);
		this.add(sourceDir);
		sourceDir.setEditable(false);
		Dimension dim = sourceDir.getPreferredSize();
		sourceDir.setBounds(iFrameInsets.left+10,height,140,dim.height);

		JTextField destDir = new JTextField("/home/"+folder+"/",15);
		this.add(destDir);
		destDir.setEditable(false);
		dim = destDir.getPreferredSize();
		destDir.setBounds(iFrameInsets.left+260,height,140,dim.height);
		list = new JList(listModel);
		renderer = new FTPCellRenderer(shownDirectory);
		list.setCellRenderer(renderer);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		mouseListener = new FTPMouseListener(list,this,FTPMouseListener.FROM);
		list.addMouseListener(mouseListener);
		populateList(0);
		JScrollPane source = new JScrollPane(list);
		this.add(source);
		height+=20;
		source.setBounds(iFrameInsets.left+10,height,140,210);
		
		destList = new JList(destListModel);
		transferRenderer = new FTPCellRenderer(shownTransferDirectory);
		destList.setCellRenderer(transferRenderer);
		destList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		destList.setLayoutOrientation(JList.VERTICAL);
		destList.setVisibleRowCount(-1);
		transferMouseListener = new FTPMouseListener(destList,this,FTPMouseListener.TO);
		destList.addMouseListener(transferMouseListener);

		JScrollPane destination = new JScrollPane(destList);
		this.add(destination);
		destination.setBounds(iFrameInsets.left+260,height,140,210);

		/*JSeparator seperator1 = new JSeparator(SwingConstants.HORIZONTAL);
		this.add(seperator1);
		Dimension sep1 = seperator1.getPreferredSize();
		seperator1.setBounds(iFrameInsets.left+10,iFrameInsets.top+260,iFrameSize.width-65,sep1.height);*/
		
		JButton putButton = new JButton(ImageLoader.getImageIcon("images/right.png"));
		putButton.setActionCommand("Put");
		this.add(putButton);
		Dimension putSize = putButton.getPreferredSize();
		height+=66;
		putButton.setBounds(iFrameInsets.left+175,height,putSize.width,putSize.height);
		putButton.addActionListener(this);
		putButton.setToolTipText("Use this to transfer the selected file to the shop.");
		
		JButton getButton = new JButton(ImageLoader.getImageIcon("images/left.png"));
		getButton.setActionCommand("Get");
		this.add(getButton);
		Dimension getSize = getButton.getPreferredSize();
		height+=60;
		getButton.setBounds(iFrameInsets.left+175,height,getSize.width,getSize.height);
		getButton.addActionListener(this);
		getButton.setToolTipText("Use this to transfer the selected file from the shop.");

		JScrollPane message = new JScrollPane(messageWindow);
		message.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(message);
		height+=104;
		message.setBounds(iFrameInsets.left+10,height,iFrameSize.width-50,90);

		

		/*JSeparator seperator = new JSeparator(SwingConstants.HORIZONTAL);
		this.add(seperator);
		Dimension sep = seperator.getPreferredSize();
		seperator.setBounds(iFrameInsets.left+175,(iFrameInsets.top*2+260)/2+10,putSize.width,sep.height);*/
		
	}

	public void populateList(int size){
		int j=0;
		shownDirectory = new Object[size+1];
		listModel.clear();
		listModel.addElement("..");
		shownDirectory[j]="..";
		j++;
		for(int i=0;i<size;i++){
			if(directory[i] instanceof String){
				if(!((String)(directory[i])).equals("Store")&&!((String)(directory[i])).equals("Public")){
					shownDirectory[j]=directory[i];
					listModel.addElement((String)directory[i]);
					j++;
				}
			}
			else{
				Object dir[] = (Object[])directory[i];
				int type=(Integer)dir[1];
				if(this.type==PUBLIC){
					shownDirectory[j]=dir;
					listModel.addElement((String)dir[0]);
					j++;
				}
				else if(this.type==SHOP){
					if(type==HackerFile.BANKING_COMPILED||type==HackerFile.ATTACKING_COMPILED||type==HackerFile.WATCH_COMPILED||type==HackerFile.FTP_COMPILED||type==HackerFile.NEW_FIREWALL||type==HackerFile.IMAGE||type==HackerFile.AGP||type==HackerFile.PCI||type==HackerFile.GAME||type==HackerFile.HTTP||type==HackerFile.COMMODITY_SLIP||type==HackerFile.SHIPPING_COMPILED){
						shownDirectory[j]=dir;
						listModel.addElement((String)dir[0]);
						j++;
					}
				}
			}
		}
		list.setCellRenderer(new FTPCellRenderer(shownDirectory));
		mouseListener.setDirectory(shownDirectory);
		
	}
	public void populateTransferList(int size){
		int j=0;
		destListModel.clear();
		shownTransferDirectory = new Object[size];
		for(int i=0;i<size;i++){
			if(transferDirectory[i] instanceof String){
				
			}
			else{
				Object dir[] = (Object[])transferDirectory[i];
				int type=(Integer)dir[1];
				if(this.type==PUBLIC){
					shownTransferDirectory[j]=dir;
					destListModel.addElement((String)dir[0]);
					j++;
				}
				else if(this.type==SHOP){
					if(type==HackerFile.BANKING_COMPILED||type==HackerFile.ATTACKING_COMPILED||type==HackerFile.WATCH_COMPILED||type==HackerFile.FTP_COMPILED||type==HackerFile.NEW_FIREWALL||type==HackerFile.IMAGE||type==HackerFile.AGP||type==HackerFile.PCI||type==HackerFile.GAME||type==HackerFile.SHIPPING_COMPILED||type==HackerFile.COMMODITY_SLIP||type==HackerFile.HTTP){
						shownTransferDirectory[j]=dir;
						destListModel.addElement((String)dir[0]);
						j++;
					}
				}
			}
		}	
		destList.setCellRenderer(new FTPCellRenderer(shownTransferDirectory));
		transferMouseListener.setDirectory(shownTransferDirectory);
	}
	
	public void receivedDirectory(Object[] directory){
		boolean requestAgain=false;
		messageWindow.setText(messageWindow.getText()+"Received Directory /home/"+MyHacker.getCurrentFolder()+".\n");
		messageWindow.setCaretPosition(messageWindow.getText().length());
		//System.out.println(MyHacker.getCurrentFolder());
		pathFrom=MyHacker.getCurrentFolder();
		this.directory=directory;
		populateList(directory.length);
	}
	
	public void setSecondaryDirectory(Object[] transferDirectory,boolean allowedDir){
		//System.out.println("Secondary Directory Size:  "+transferDirectory.length);
		messageWindow.setText(messageWindow.getText()+"Received Second Directory /home/"+MyHacker.getSecondaryFolder()+".\n");
		messageWindow.setCaretPosition(messageWindow.getText().length());
		this.transferDirectory=transferDirectory;
		if(allowedDir)populateTransferList(transferDirectory.length);
	}
	
	
	public void setType(int type,int port){
		this.type=type;
		this.port=port;
		populate();
		messageWindow.setText("");
		messageWindow.setCaretPosition(messageWindow.getText().length());
		Object objects[];
		MyHacker.setRequestedDirectory(Hacker.FTP);
		MyHacker.setCurrentFolder("");
		objects = new Object[]{MyHacker.getEncryptedIP(),""};
		MyView.setFunction("requestdirectory");
		MyView.addFunctionCall(new RemoteFunctionCall(Hacker.FTP,"requestdirectory",objects));
		
		String request="";
		if(type==SHOP){
			request="Store/";
			targetIP=MyHacker.getIP();
			MyHacker.setSecondaryFolder("Store");
		}
		if(type==PUBLIC){
			request="Public/";
			//targetIP=ipField1.getText()+"."+ipField2.getText()+"."+ipField3.getText()+"."+ipField4.getText();
			targetIP=MyHacker.getIP();
			pass=passField.getText();
			MyHacker.setSecondaryFolder("Public");
		}
		//System.out.println("Requesting Secondary Directory");
		//System.out.println("Encrypted IP: "+MyHacker.getEncryptedIP());
		objects = new Object[]{targetIP,request,MyHacker.getEncryptedIP(),port};
		MyView.setFunction("requestsecondarydirectory");
		MyView.addFunctionCall(new RemoteFunctionCall(Hacker.FTP,"requestsecondarydirectory",objects));
		setVisible(true);
		
		String[] ip = MyHacker.getIP().split("\\.");
		if(ipField1!=null){
			ipField1.setText(ip[0]);
			ipField2.setText(ip[1]);
			ipField3.setText(ip[2]);
			ipField4.setText(ip[3]);
		}
	}
	
	public void changeDirectory(String name,int type){
		//System.out.println(name);
		if(!name.equals("..")){
			sourceDir.setText(sourceDir.getText()+name+"/");
			String folder = sourceDir.getText();
				String folders[] = folder.split("/");
				String newFolder="";
				for(int i=2;i<folders.length-1;i++){
					 newFolder+=folders[i]+"/";
				}
			MyHacker.setCurrentFolder(newFolder+name);
			//System.out.println("Requesting "+newFolder+name+"/");
			Object objects[] = new Object[]{MyHacker.getEncryptedIP(),newFolder+name+"/"};
			MyHacker.setRequestedDirectory(Hacker.FTP);
			MyView.setFunction("requestdirectory");
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.FTP,"requestdirectory",objects));
			//requestDirectory();
		}
		else{
			if(!sourceDir.getText().equals("/home/")){
				String folder = sourceDir.getText();
				String folders[] = folder.split("/");
				String newFolder="";
				for(int i=2;i<folders.length-1;i++){
					 newFolder+=folders[i]+"/";
				}
				sourceDir.setText("/home/"+newFolder);
				//System.out.println(newFolder);
				//System.out.println(folders[folders.length-1]);
				//System.out.println("Go up a directory");
				Object objects[] = new Object[]{MyHacker.getEncryptedIP(),newFolder};
				MyHacker.setCurrentFolder(newFolder);
				MyHacker.setRequestedDirectory(Hacker.FTP);
				MyView.setFunction("requestdirectory");
				MyView.addFunctionCall(new RemoteFunctionCall(Hacker.FTP,"requestdirectory",objects));
			}
		}
		//list.setSelectedIndex(-1);
	}
		
	public void put(String name,int quantity){
		String folder="",targetIP="",pass="";
		if(type==SHOP){
			targetIP=MyHacker.getIP();
			folder="Store/";
		}
		if(type==PUBLIC){
			targetIP=ipPanel.getIP();//ipField1.getText()+"."+ipField2.getText()+"."+ipField3.getText()+"."+ipField4.getText();
			pass=passField.getText();
			folder="Public/";
		}
		String displayMessage = "Put "+name+".";
		String to = "";
		if(!MyHacker.getCurrentFolder().equals(""))
			to=MyHacker.getCurrentFolder()+"/";
		MyHacker.setRequestedDirectory(Hacker.FTP);
		Object objects[] = {targetIP,port,name,to,folder,MyHacker.getEncryptedIP(),pass,quantity};
		MyView.setFunction("requestdirectory");
		MyView.addFunctionCall(new RemoteFunctionCall(Hacker.FTP,"put",objects));
		messageWindow.setText(messageWindow.getText()+displayMessage+"\n");
		messageWindow.setCaretPosition(messageWindow.getText().length());
	}
	
	public void get(String name,int quantity){
		String folder="",targetIP="";
		if(type==SHOP){
			targetIP=MyHacker.getIP();
			folder="Store/";
		}
		if(type==PUBLIC){
			targetIP=ipPanel.getIP();//ipField1.getText()+"."+ipField2.getText()+"."+ipField3.getText()+"."+ipField4.getText();
			pass=passField.getText();
			folder="Public/";
		}
		String displayMessage = "Get "+name+".";
		String to = "";
		if(!MyHacker.getCurrentFolder().equals(""))
			to=MyHacker.getCurrentFolder()+"/";
		MyHacker.setRequestedDirectory(Hacker.FTP);
		Object objects[] = {targetIP,port,name,to,folder,MyHacker.getEncryptedIP(),pass,quantity};
		MyView.setFunction("get");
		MyView.addFunctionCall(new RemoteFunctionCall(Hacker.FTP,"get",objects));
		messageWindow.setText(messageWindow.getText()+displayMessage+"\n");
		messageWindow.setCaretPosition(messageWindow.getText().length());
	}
	
	public String getIP(){
		if(ipField1!=null){
			return(ipField1.getText()+"."+ipField2.getText()+"."+ipField3.getText()+"."+ipField4.getText());
		}
		else return(MyHacker.getIP());
	}
	
	public String getPass(){
		return(passField.getText());
	}
	
	public int getPort(){
		return(port);
	}
	
	/*public JDesktopPane getPanel(){
		return(mainPanel);
	}*/
	
	public void setQuantity(String name,int quantity){
		if(transferType==PUT)
			put(name,quantity);
		if(transferType==GET)
			get(name,quantity);
	}
	
	public void setTransferType(int transferType){
		this.transferType=transferType;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand()=="Put"){
			if(list.getSelectedIndex()!=-1){
				Object select = shownDirectory[list.getSelectedIndex()];
				String selection=null;
				if(select instanceof String){
				}
				else{
					Object[] o = (Object[])select;
					selection=(String)o[0];
					if(selection!=null){
						transferType=PUT;
						FTPQuantityDialog FQD = new FTPQuantityDialog(this,(int)(Integer)o[2],(String)o[0]);
					}
				}
				
			}
		}
		if (e.getActionCommand()=="Get"){
			if(destList.getSelectedIndex()!=-1){
				Object select = shownTransferDirectory[destList.getSelectedIndex()];
				String selection=null;
				if(select instanceof String){
				}
				else{
					Object[] o = (Object[])select;
					selection=(String)o[0];
					if(selection!=null){
						transferType=GET;
						FTPQuantityDialog FQD = new FTPQuantityDialog(this,(int)(Integer)o[2],(String)o[0]);
					}
				}
				
			}
		}
		
		if(e.getActionCommand().equals("Go")){
			destListModel.clear();
			String request="",targetIP="",pass="";
			if(type==SHOP){
				request="Store/";
				targetIP=MyHacker.getIP();
				MyHacker.setSecondaryFolder("Store");
			}
			if(type==PUBLIC){
				request="Public/";
				targetIP=ipPanel.getIP();//ipField1.getText()+"."+ipField2.getText()+"."+ipField3.getText()+"."+ipField4.getText();
				//targetIP=MyHacker.getIP();
				pass=passField.getText();
				MyHacker.setSecondaryFolder("Public");
			}
			//System.out.println("Requesting Secondary Directory - "+targetIP);
			Object[] objects = new Object[]{targetIP,request,MyHacker.getEncryptedIP(),port};
			MyHacker.setRequestedDirectory(Hacker.FTP);
			MyView.setFunction("requestsecondarydirectory");
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.FTP,"requestsecondarydirectory",objects));
			
		}

	}

	public void internalFrameClosed(InternalFrameEvent e) {
		MyHacker.setFTPOpen(false);
	}
	
	public void undoableEditHappened(UndoableEditEvent e){
		//System.out.println("Undoable Edit Happened");
		if(e.getSource()==ipField1.getDocument()){
			if(ipField1.getText().length()==3){
				ipField2.grabFocus();
			}
		}
		if(e.getSource()==ipField2.getDocument()){
			if(ipField2.getText().length()==3){
				ipField3.grabFocus();
			}
		}
		if(e.getSource()==ipField3.getDocument()){
			if(ipField3.getText().length()==1){
				ipField4.grabFocus();
			}
		}
	}
	
	public void focusGained(FocusEvent e){
		((JTextField)e.getSource()).selectAll();
	}
	
	public void focusLost(FocusEvent e){
	}
}

