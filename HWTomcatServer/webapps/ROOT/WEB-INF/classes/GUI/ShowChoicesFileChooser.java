package GUI;
/**

AttackFileChooser.java
this is for the attack pane to choose a file to install.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Game.*;
import View.*;
import Assignments.*;

public class ShowChoicesFileChooser extends JInternalFrame implements ActionListener,MouseListener{
	//data
	private String folder;
	private Object[] directory,shownDirectory;
	private Hacker MyHacker;
	private int type;
	private JList fileList;
	private DefaultListModel listModel = new DefaultListModel();
	private Object answer=null;
	private JTextField ipField,folderField;
	private ScriptEditorCellRenderer renderer;
	private JSpinner moneySpinner;
	private int port;
	private String ip;
	private int id;
	private int attackPort = 0;
	public ShowChoicesFileChooser(Hacker MyHacker,int type,String ip,int port,int attackPort){
		this.ip=ip;
		this.port=port;
		this.MyHacker=MyHacker;
		this.attackPort = attackPort;
		//System.out.println("Attack port in file: "+attackPort);
		String request="";
		if(type==0){
			request="Store/";
			this.folder=request;
		}
		if(type==1){
			request="Public/";
			this.folder=request;
		}
		id = MyHacker.setShowChoicesFileChooser(this);
		MyHacker.setRequestedDirectory(Hacker.SHOW_CHOICES_FILE_CHOOSER);
		View MyView = MyHacker.getView();
		Object objects[] = {ip,request,MyHacker.getEncryptedIP(),port};
		//MyHacker.setCurrentFolder("");
		MyView.setFunction("requestsecondarydirectory");
		MyView.addFunctionCall(new RemoteFunctionCall(id,"requestsecondarydirectory",objects));
		setTitle("Choose File");
		setFrameIcon(ImageLoader.getImageIcon("images/open.png"));
		setBounds(100,100,420,280);
		setLayout(null);
		setClosable(false);
		setIconifiable(false);
		setResizable(false);
		populate();
		setVisible(true);
	}
	
	public synchronized void populate(){
		Insets insets = getInsets();
		Dimension d = getSize();
		int position=insets.top+10;
		/*folderField = new JTextField(30);
		folderField.setEditable(false);
		folderField.setBackground(new Color(255,255,255));
		folderField.setText("home/"+folder);
		add(folderField);
		folderField.setBounds(insets.left+5,position,folderField.getPreferredSize().width,folderField.getPreferredSize().height);
		position+=5+folderField.getPreferredSize().height;*/
		
		fileList = new JList(listModel);
		listModel.addElement("..");
		populateList();
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.setLayoutOrientation(JList.VERTICAL_WRAP);
		fileList.setVisibleRowCount(6);
		renderer = new ScriptEditorCellRenderer(shownDirectory,null);
		fileList.setCellRenderer(renderer);
		fileList.setSelectedIndex(0);
		fileList.addMouseListener(this);
		
		JScrollPane scrollPane = new JScrollPane(fileList);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);  
		add(scrollPane);
		scrollPane.setBounds(insets.left+5,position,d.width-20,d.height-160);
		position+=5+scrollPane.getSize().height;
		/*JLabel label;
		ipField = new JTextField(MyHacker.getIP());
		if(type==HackerFile.BANKING_COMPILED||type==HackerFile.FTP_COMPILED){
			label = new JLabel("Malicious IP:");
			add(label);
			label.setBounds(insets.left+5,position,label.getPreferredSize().width,label.getPreferredSize().height);
			
			add(ipField);
			ipField.setBounds(insets.left+5+label.getPreferredSize().width,position,ipField.getPreferredSize().width,ipField.getPreferredSize().height);
			position+=5+ipField.getPreferredSize().height;
		}
		if(type==HackerFile.BANKING_COMPILED){
			label = new JLabel("Petty Cash Target:");
			add(label);
			label.setBounds(insets.left+5,position,label.getPreferredSize().width,label.getPreferredSize().height);
			SpinnerNumberModel model = new SpinnerNumberModel(0,0,50000000.0f,1);
			moneySpinner = new JSpinner(model);
			add(moneySpinner);
			moneySpinner.setBounds(insets.left+5+label.getPreferredSize().width,position,moneySpinner.getPreferredSize().width,moneySpinner.getPreferredSize().height);
			position+=5+moneySpinner.getPreferredSize().height;
		}*/
		
		
		JButton accept=new JButton("Take");
		accept.addActionListener(this);
		accept.setActionCommand("Accept");
		add(accept);
		accept.setBounds(insets.left+5,position,accept.getPreferredSize().width,accept.getPreferredSize().height);
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		add(cancel);
		cancel.setBounds(insets.left+10+accept.getPreferredSize().width,position,cancel.getPreferredSize().width,cancel.getPreferredSize().height);
	}
	
	
	public synchronized void populateList(){
		boolean success=false;
		int count=0;
		int j=0;
		
		while(!success){
		
			try{
				shownDirectory = new Object[directory.length+1];
				listModel.clear();
				for(int i=0;i<directory.length;i++){
					if(directory[i] instanceof String){
						/*if(!((String)(directory[i])).equals("Store")&&!((String)(directory[i])).equals("Public")){
							//System.out.println(directory[i]);
							shownDirectory[j]=directory[i];
							listModel.addElement((String)directory[i]);
							j++;
						}*/
					}
					else{
						Object dir[] = (Object[])directory[i];
						int type=(Integer)dir[1];
						//if(type==HackerFile.BANKING_COMPILED||type==HackerFile.ATTACKING_COMPILED||type==HackerFile.WATCH_COMPILED||type==HackerFile.FTP_COMPILED){
						//if(type==this.type){
							if(j<shownDirectory.length){
								shownDirectory[j]=dir;
								listModel.addElement((String)dir[0]);
								j++;
							}
						//}
					}
					renderer = new ScriptEditorCellRenderer(shownDirectory,null);
					fileList.setCellRenderer(renderer);
				}
				success=true;
			}catch(NullPointerException e){
			}
			count++;
			if(count>100)
				break;
			
			try{
				Thread.currentThread().sleep(50);
			}catch(Exception e){
			}
		}
	}
	
	public Object getAnswer(){
		return(answer);
	}
	
	public void setDirectory(Object[] directory){
		//System.out.println("Received Directory in Show Choices");
		this.directory=directory;
		populateList();
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Accept")){
			String blah="";
			try{
				Object o =shownDirectory[fileList.getSelectedIndex()];
				if(o instanceof Object[]){
					Object[] file=(Object[])o;
					View MyView = MyHacker.getView();
					String name = (String)file[0];
					Object objects[] = {ip,port,name,folder,"",MyHacker.getEncryptedIP(),attackPort};
					MyView.setFunction("malget");
					MyView.addFunctionCall(new RemoteFunctionCall(0,"malget",objects));
					dispose();
				}
				else{
					
				}
				//answer = directory[fileList.getSelectedIndex()];
			}catch(ArrayIndexOutOfBoundsException ex){
			}
			
		}
		
		if(e.getActionCommand().equals("Cancel")){
			//MyHacker.setAttackFileChooser(null);
			dispose();
		}
		
	}
	
	public void mouseEntered(MouseEvent e){

	}

	public void mouseExited(MouseEvent e){

	}

	public void mousePressed(MouseEvent e){

	}

	public void mouseReleased(MouseEvent e){

	}

	public void mouseClicked(MouseEvent e){
		if(e.getClickCount()==2){
			Object o = shownDirectory[fileList.getSelectedIndex()];
			if(o instanceof String){
				String fileName = (String)o;
				if(!fileName.equals("..")){
					folder=folder+(String)o+"/";
					folderField.setText("home/"+folder);
					MyHacker.setCurrentFolder(folder);
					//System.out.println(folder);
					//REQUEST DIRECTORY
					MyHacker.setRequestedDirectory(Hacker.ATTACK_FILE_CHOOSER);
					View MyView = MyHacker.getView();
					String ip=MyHacker.getEncryptedIP();
					Object objects[] = {ip,folder};
					MyView.setFunction("requestdirectory");
					MyView.addFunctionCall(new RemoteFunctionCall(id,"requestdirectory",objects));
				}
				else{
					String folders[] = folder.split("/");
					String newFolder="";
					for(int i=0;i<folders.length-1;i++){
						 newFolder+=folders[i]+"/";
					}
					folder=newFolder;
					MyHacker.setCurrentFolder(folder);
					MyHacker.setRequestedDirectory(Hacker.ATTACK_FILE_CHOOSER);
					folderField.setText("home/"+folder);
					View MyView = MyHacker.getView();
					String ip=MyHacker.getEncryptedIP();
					Object objects[] = {ip,folder};
					MyView.setFunction("requestdirectory");
					MyView.addFunctionCall(new RemoteFunctionCall(id,"requestdirectory",objects));
				}
			}
			else{
				String blah="";
				try{
					Object[] file=(Object[])o;
					View MyView = MyHacker.getView();
					String name = (String)file[0];
					Object objects[] = {ip,port,name,folder,"",MyHacker.getEncryptedIP(),attackPort};
					MyView.setFunction("malget");
					MyView.addFunctionCall(new RemoteFunctionCall(0,"malget",objects));
					dispose();
					//answer = directory[fileList.getSelectedIndex()];
				}catch(ArrayIndexOutOfBoundsException ex){
				}
				MyHacker.setShowChoicesFileChooser(null);
				dispose();
			}
		}		
	}
		
}
