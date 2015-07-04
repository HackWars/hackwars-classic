package GUI;
/**

CommandPrompt.java
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
import net.miginfocom.swing.*;
import Hackscript.Model.*;

public class CommandPrompt extends Application implements KeyListener{

	public static final int BASH = 0;
	public static final int SHELL = 1;

	private final Color TYPE_COLORS[]={Color.blue,Color.green,Color.blue,Color.green,Color.blue,Color.green,Color.blue,Color.green,Color.red,Color.white,Color.white,Color.white,Color.blue,Color.white,Color.white,Color.green,Color.white,Color.white,Color.yellow,Color.yellow,Color.green,Color.blue,Color.blue,Color.blue,Color.green,Color.white,Color.cyan,Color.pink};
	private Hacker hacker;
	private JPanel promptPanel;
	private JTextField entryField;
	private ArrayList commandList = new ArrayList();
	private int commandListPosition = -1;
	private String folder = "";
	private JScrollPane scrollPane;
	private Object[] directory;
	private HashMap directories = new HashMap(),files = new HashMap();
	private int runFileType = 0;
	public CommandPrompt(Hacker hacker){
		this.hacker = hacker;
		hacker.setCommandPromptRequestedDirectory(this);
		View MyView = hacker.getView();
		MyView.setFunction("requestdirectory");
		Object objects[] = new Object[]{hacker.getEncryptedIP(),""};
		MyView.addFunctionCall(new RemoteFunctionCall(Hacker.COMMAND_PROMPT,"requestdirectory",objects));
		MyHacker.setRequestedDirectory(Hacker.COMMAND_PROMPT);
		setTitle("Command Prompt");
		setIconifiable(true);
		setResizable(true);
		setMaximizable(true);
		setClosable(true);
		//setFrameIcon(ImageLoader.getImageIcon("images/HackerIcon.png"));
		addInternalFrameListener(this);
		setBackground(Color.black);
		hacker.getPanel().add(this);
		setLayout(new MigLayout("fill,ins 0,align leading"));
		promptPanel = new JPanel();
		promptPanel.setLayout(new MigLayout("fillx,ins 0,align leading,wrap 2"));
		promptPanel.setBackground(Color.black);
		
		JLabel label = new JLabel("> ");
		label.setForeground(Color.white);
		//label.setFont(new Font("monospace",10,Font.PLAIN));
		promptPanel.add(label,"split");
		
		entryField = new JTextField();
		entryField.setBackground(Color.black);
		entryField.setForeground(Color.white);
		entryField.setBorder(null);
		entryField.addKeyListener(this);
		promptPanel.add(entryField,"growx,wrap");
		entryField.addActionListener(this);
		
		scrollPane = new JScrollPane(promptPanel);
		scrollPane.setPreferredSize(new Dimension(500,300));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//setBounds(100,100,100,100);
		add(scrollPane,"grow");
		setVisible(true);
		pack();
		
		
	}
	
	
	public void receivedDirectory(Object[] directory){
		this.directory = directory;
		setDirectories();
	}
	
	private void setDirectories(){
		directories = new HashMap();
		files = new HashMap();
		for(int i=0;i<directory.length;i++){
			if(directory[i] instanceof String){
				if(!((String)(directory[i])).equals("Store")&&!((String)(directory[i])).equals("Public")){
					directories.put((String)directory[i],"");
				}
				
			}
			else{
				Object[] dir = (Object[])directory[i];
				files.put((String)dir[0],directory[i]);
			}
		}
	}
	
	private void showDirectoryListing(){
		/*JLabel label = new JLabel("cms");
		label.setForeground(Color.white);
		promptPanel.add(label,"gapx 0px 20px");
		label = new JLabel("directory");
		label.setForeground(Color.green);
		promptPanel.add(label,"growx");
		label = new JLabel("AGPCard.license");
		label.setForeground(Color.white);
		promptPanel.add(label,"gapx 0px 20px");
		label = new JLabel("AGP Card");
		label.setForeground(Color.red);
		promptPanel.add(label,"growx");*/
		JLabel label;
		for(int i=0;i<directory.length;i++){
			if(directory[i] instanceof String){
				if(!((String)(directory[i])).equals("Store")&&!((String)(directory[i])).equals("Public")){
					//rowData[NAME] = (String)directory[i];
					label = new JLabel((String)directory[i]);
					label.setForeground(Color.white);
					promptPanel.add(label,"gapx 0px 20px");
					label = new JLabel("directory");
					label.setForeground(Color.green);
					promptPanel.add(label,"growx,wrap");
				}
			}
			else{
				Object dir[] = (Object[])directory[i];
				label = new JLabel((String)dir[0]);
				label.setForeground(Color.white);
				promptPanel.add(label,"gapx 0px 20px");
				int type = (Integer)dir[1];
				label = new JLabel(Home.TYPES[type]);
				label.setForeground(TYPE_COLORS[type]);
				promptPanel.add(label,"growx,wrap");
				/*
				int type=(Integer)dir[1];
				shownDirectory[j]=dir;
				rowData[NAME] = (String)dir[0];
				rowData[TYPE] = TYPES[type];
				rowData[QUANTITY] = (Integer)dir[2];						
				rowData[YOUR_STORE_PRICE] = (Float)dir[3];
				float price = getPrice((String)dir[4]);
				rowData[SELL_TO_STORE_PRICE] = price;
				rowData[MAKER] = (String)dir[4];
				rowData[CPU_COST] = (Float)dir[5];
				rowData[DESCRIPTION] = (String)dir[6];
				tableModel.addRow(rowData);
				j++;*/
			}
		
		}
	
	
	}
	
	private void showDirectoryListing(int showType){
		/*JLabel label = new JLabel("cms");
		label.setForeground(Color.white);
		promptPanel.add(label,"gapx 0px 20px");
		label = new JLabel("directory");
		label.setForeground(Color.green);
		promptPanel.add(label,"growx");
		label = new JLabel("AGPCard.license");
		label.setForeground(Color.white);
		promptPanel.add(label,"gapx 0px 20px");
		label = new JLabel("AGP Card");
		label.setForeground(Color.red);
		promptPanel.add(label,"growx");*/
		JLabel label;
		for(int i=0;i<directory.length;i++){
			if(directory[i] instanceof String){
				if(!((String)(directory[i])).equals("Store")&&!((String)(directory[i])).equals("Public")){
					if(showType==-1){
						//rowData[NAME] = (String)directory[i];
						label = new JLabel((String)directory[i]);
						label.setForeground(Color.white);
						promptPanel.add(label);
						label = new JLabel("directory");
						label.setForeground(Color.green);
						promptPanel.add(label,"growx,wrap");
					}
				}
			}
			else{
				Object dir[] = (Object[])directory[i];
				int type = (Integer)dir[1];
				if(type==showType){
					
					label = new JLabel((String)dir[0]);
					label.setForeground(Color.white);
					promptPanel.add(label);
					
					label = new JLabel(Home.TYPES[type]);
					label.setForeground(TYPE_COLORS[type]);
					promptPanel.add(label,"growx,wrap");
				}
			}
		
		}
	
	
	}
	
	private void changeDirectory(String directory){
		if(directories.get(directory)!=null||directory.equals("..")){
			if(directory.equals("..")){
				String folderParts[] = folder.split("/");
				String newFolder = "";
				for(int i=0;i<folderParts.length-1;i++){
					newFolder+=folderParts[i]+"/";
				}
				folder = newFolder;
			}
			else{
				folder += directory+"/";
			}
			Object objects[] = new Object[]{hacker.getEncryptedIP(),folder};
			View MyView = hacker.getView();
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.COMMAND_PROMPT,"requestdirectory",objects));
			MyHacker.setRequestedDirectory(Hacker.COMMAND_PROMPT);
		}
		else if(directories.get(directory)==null){
			showErrorMessage("Directory not found.");
		
		}
	
	}
	
	private void runFile(String filename){
		runFileType = BASH;
		showErrorMessage("Running File "+filename+"...");
		Object[] file = (Object[])files.get(filename);
		if(file == null){
			showErrorMessage("File not found.");
			showPrompt();
			return;
		}
		MyHacker.setRequestedFile(Hacker.COMMAND_PROMPT);
		View MyView = hacker.getView();
		String ip=hacker.getEncryptedIP();
		Object objects[] = {ip,folder,filename};
		MyView.setFunction("requestfile");
		MyView.addFunctionCall(new RemoteFunctionCall(Hacker.COMMAND_PROMPT,"requestfile",objects));
		
	}
	
	public void receivedFile(HackerFile file){
		if(runFileType==BASH){
			int type = file.getType();
			if(type!=HackerFile.TEXT){
				showErrorMessage("Invalid file format. Must be of type 'text'.");
				showPrompt();
				return;
			}
			HashMap contents = file.getContent();
			String data = (String)contents.get("data");
			String[] commands = data.split("\n");
			for(int i=0;i<commands.length;i++){
				showErrorMessage("Running command "+commands[i]+"...");
				parseCommand(commands[i].trim());
			}
		}
		else if(runFileType==SHELL){
			try{
				BashLinker BL = new BashLinker(hacker);
				HashMap contents = file.getContent();
				String function = (String)contents.get("fire");
				RunFactory.runCode(function,BL,10000);
			}catch(Exception ex){
				//ex.printStackTrace();
				//System.out.println("Error Found: "+ex.getMessage());
				showErrorMessage(ex.getMessage());
				
			}
		}
		showPrompt();
	}
	
	private void portOnOff(int port,boolean on){
		hacker.portOnOff(port,on);
	}
	
	private void watchOnOff(int watchID,boolean on){
		hacker.watchOnOff(watchID,on);
	}
	
	private void deposit(int port,float amount){
		if(amount<0.0f){
			amount += hacker.getPettyCash();
		}
		View MyView = hacker.getView();
		String ip = hacker.getEncryptedIP();
		Object objects[] = {new Float(amount),ip,new Integer(port)};
		MyView.setFunction("deposit");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"deposit",objects));
	}
	private void withdraw(int port,float amount){
		View MyView = hacker.getView();
		String ip = hacker.getEncryptedIP();
		Object objects[] = {new Float(amount),ip,new Integer(port)};
		MyView.setFunction("withdraw");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"withdraw",objects));
	}
	
	private void runShellScript(String filename){
		runFileType = SHELL;
		Object[] file = (Object[])files.get(filename);
		if(file == null){
			showErrorMessage("File not found.");
			showPrompt();
			return;
		}
		MyHacker.setRequestedFile(Hacker.COMMAND_PROMPT);
		View MyView = hacker.getView();
		String ip=hacker.getEncryptedIP();
		Object objects[] = {ip,folder,filename};
		MyView.setFunction("requestfile");
		MyView.addFunctionCall(new RemoteFunctionCall(Hacker.COMMAND_PROMPT,"requestfile",objects));
	}
	
	private void showErrorMessage(String message){
		JLabel label = new JLabel(message);
		label.setForeground(Color.white);
		promptPanel.add(label,"growx,span,wrap");
	}
	
	private boolean parseCommand(String command){
		String[] commandParts = command.split(" ");
		String function = commandParts[0];
		boolean show = true;
		if(function.equals("cd")){
			changeDirectory(commandParts[1]);
		}
		else if(function.equals("ls")||function.equals("dir")){
			if(commandParts.length==1){
				showDirectoryListing();
			}
			else{
				try{
					showDirectoryListing(Integer.parseInt(commandParts[1]));
				}catch(NumberFormatException e){
					showErrorMessage(function+" takes in a integer as an argument to show only certain file types.");
				}
			}
		}
		else if(function.equals("openportmanagement")){
			if(commandParts.length==1){
				hacker.startPortManagement();
			}
			else if(commandParts.length==3){
				try{
					hacker.startPortManagement(Integer.parseInt(commandParts[1]),Integer.parseInt(commandParts[2]));
				}catch(NumberFormatException e){
					showErrorMessage(function + " takes in two integers for x,y position of the port management screen.");
				}
			}
			else{
				showErrorMessage(function + " takes in two integers for x,y position of the port management screen.");
			}
		}
		else if(function.equals("attack")){
			if(commandParts.length == 2){
				int port = Integer.parseInt(commandParts[1]);
				hacker.beginAttack(port);
			}
			else if(commandParts.length == 4){
				int port = Integer.parseInt(commandParts[1]);
				String targetIP = commandParts[2];
				int targetPort = Integer.parseInt(commandParts[3]);
				hacker.showAttack(port,targetIP,targetPort);
				//hacker.beginAttack(port);
			}
			else if(commandParts.length == 6){
				int port = Integer.parseInt(commandParts[1]);
				String targetIP = commandParts[2];
				int targetPort = Integer.parseInt(commandParts[3]);
				int x = Integer.parseInt(commandParts[4]);
				int y = Integer.parseInt(commandParts[5]);
				hacker.showAttack(port,x,y,targetIP,targetPort);
				//hacker.beginAttack(port);
			}
			else{
				showErrorMessage("Wrong argument number for attack.");
			}
			
		}
		else if(function.equals("redirect")){
			if(commandParts.length == 2){
				int port = Integer.parseInt(commandParts[1]);
				hacker.beginRedirect(port);
			}
			else if(commandParts.length == 4){
				int port = Integer.parseInt(commandParts[1]);
				String targetIP = commandParts[2];
				int targetPort = Integer.parseInt(commandParts[3]);
				hacker.showRedirect(port,targetIP,targetPort);
				//hacker.beginRedirect(port);
			}
			else if(commandParts.length == 6){
				int port = Integer.parseInt(commandParts[1]);
				String targetIP = commandParts[2];
				int targetPort = Integer.parseInt(commandParts[3]);
				int x = Integer.parseInt(commandParts[4]);
				int y = Integer.parseInt(commandParts[5]);
				hacker.showRedirect(port,x,y,targetIP,targetPort);
				//hacker.beginRedirect(port);
			}
			else{
				showErrorMessage("Wrong argument number for redirect.");
			}
			
		}
		else if(function.equals("turnonport")){
			if(commandParts.length == 2){
				int port = Integer.parseInt(commandParts[1]);
				portOnOff(port,true);
			}
			else{
				showErrorMessage("Wrong arguments for turnonport.");
			}
		}
		else if(function.equals("turnoffport")){
			if(commandParts.length == 2){
				int port = Integer.parseInt(commandParts[1]);
				portOnOff(port,false);
			}
			else{
				showErrorMessage("Wrong arguments for turnoffport.");
			}
		}
		else if(function.equals("turnonwatch")){
			if(commandParts.length == 2){
				int port = Integer.parseInt(commandParts[1]);
				watchOnOff(port,true);
			}
			else{
				showErrorMessage("Wrong arguments for turnonwatch.");
			}
		}
		else if(function.equals("turnoffwatch")){
			if(commandParts.length == 2){
				int port = Integer.parseInt(commandParts[1]);
				watchOnOff(port,false);
			}
			else{
				showErrorMessage("Wrong arguments for turnoffwatch.");
			}
		}
		else if(function.equals("edit")){
			if(commandParts.length == 2){
				String filename = commandParts[1];
				Object[] file = (Object[])files.get(filename);
				if(file == null){
					showErrorMessage("File not found.");
					return true;
				}
				int type = (Integer)file[1];
				if(type == HackerFile.BANKING_SCRIPT || type == HackerFile.ATTACKING_SCRIPT || type == HackerFile.WATCH_SCRIPT || 
				   type == HackerFile.SHIPPING_SCRIPT || type == HackerFile.TEXT || type == HackerFile.FTP_SCRIPT){
					hacker.showScriptEditor(folder,filename);
				}
				else{
					showErrorMessage("You can't edit that file.");
				}
			}
			else{
				showErrorMessage("Wrong arguments for edit.");
			}
		}
		else if(function.equals("deposit")){
			if(commandParts.length == 3){
				int port = Integer.parseInt(commandParts[1]);
				float amount = 0.0f;
				String amountPart = commandParts[2];
				if(amountPart.equals("all")){
					amount = hacker.getPettyCash();
				}
				else{
					amount = Float.parseFloat(commandParts[2]);
				}
				
				deposit(port,amount);
			
			}
		
		}
		else if(function.equals("withdraw")){
			if(commandParts.length == 3){
				int port = Integer.parseInt(commandParts[1]);
				float amount = Float.parseFloat(commandParts[2]);
				
				withdraw(port,amount);
			
			}
		
		}
		else if(function.equals("sh")){
			if(commandParts.length == 2){
				String filename = commandParts[1];
				runShellScript(filename);
				show = false;
			}
		}		
		else if(function.indexOf("./") != -1){
			runFile(function.substring(2,function.length()));
			show = false;
		}
		else{
			showErrorMessage("Command "+function+" not found.");
		}
		
		return show;
	
	}
	
	private void showPrompt(){
		JLabel label = new JLabel(folder + ">");
		label.setForeground(Color.white);
		promptPanel.add(label,"split,span");
		entryField.setText("");
		promptPanel.add(entryField,"growx,span,wrap");
		promptPanel.validate();
		promptPanel.repaint();
		validate();
		repaint();
		SwingUtilities.invokeLater(new Runnable()
		{
		public void run()
		{
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
		}
		}); 
		
		entryField.requestFocusInWindow();
	}
	
	public void actionPerformed(ActionEvent e){
		String command = entryField.getText();
		promptPanel.remove(entryField);
		
		JLabel label = new JLabel(command);
		label.setForeground(Color.white);
		promptPanel.add(label,"growx,wrap");
		
		boolean show = parseCommand(command);
		
		if(show){
			showPrompt();
		}
			
		//if(commandList.size()!=0){
			String lastCommand = null;
			try{
				lastCommand = (String)commandList.get(commandList.size()-1);
			}catch(Exception ex){}
			
			if((lastCommand==null||!lastCommand.equals(command))&&!command.equals("")){
				commandList.add(command);
			}
			commandListPosition = commandList.size();
		//}
	
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if(commandListPosition==-1){
				commandListPosition = commandList.size();
			}
			if(commandListPosition!=0){
				commandListPosition--;
				String command = (String)commandList.get(commandListPosition);
				entryField.setText((String)commandList.get(commandListPosition));
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(commandListPosition==-1){
				commandListPosition = commandList.size();
			}
			if(commandListPosition+1<commandList.size()){
				commandListPosition++;
				String command = (String)commandList.get(commandListPosition);
				entryField.setText((String)commandList.get(commandListPosition));
			}
		}
//		System.out.println("Key Pressed");
    }
  
  public void keyReleased(KeyEvent e) {
  }
  
  
  public void keyTyped(KeyEvent e) {
    
    
  }

}