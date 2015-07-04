package GUI;
/**

InstallFileChooser.java
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

public class WatchInstallFileChooser extends JInternalFrame implements ActionListener,MouseListener{
	//data
	private String folder;
	private Object[] directory,shownDirectory;
	private Hacker MyHacker;
	private int type;
	private JList fileList;
	private DefaultListModel listModel = new DefaultListModel();
	private int port;
	private ScriptEditorCellRenderer renderer;
	private WatchManager MyWatchManager;
	private JTextField folderField;
	private JComboBox comboBox;
	private JSpinner portSpinner;
	
	public WatchInstallFileChooser(Hacker MyHacker,String folder,Object[] directory,WatchManager MyWatchManager){
		this.MyHacker=MyHacker;
		this.folder=folder;
		this.directory=directory;
		this.type=type;
		this.MyWatchManager=MyWatchManager;
		this.port=port;
		MyHacker.setWatchInstallFileChooser(this);
		MyHacker.setRequestedDirectory(Hacker.WATCH_INSTALL_FILE_CHOOSER);
		this.folder="";
		View MyView = MyHacker.getView();
		Object objects[] = {MyHacker.getEncryptedIP(),""};
		MyHacker.setCurrentFolder("");
		MyView.setFunction("requestdirectory");
		MyView.addFunctionCall(new RemoteFunctionCall(Hacker.WATCH_INSTALL_FILE_CHOOSER,"requestdirectory",objects));
		setTitle("Choose File");
		setFrameIcon(ImageLoader.getImageIcon("images/open.png"));
		setBounds(100,100,420,400);
		setLayout(null);
		setClosable(false);
		setIconifiable(false);
		setResizable(false);
		populate();
		setVisible(true);
	}	
	
	public void populate(){
		Insets insets = getInsets();
		Dimension d = getSize();
		int position=insets.top+10;
		folderField = new JTextField(30);
		folderField.setEditable(false);
		folderField.setBackground(new Color(255,255,255));
		folderField.setText("home/"+folder);
		add(folderField);
		folderField.setBounds(insets.left+5,position,folderField.getPreferredSize().width,folderField.getPreferredSize().height);
		position+=5+folderField.getPreferredSize().height;
		
		fileList = new JList(listModel);
		listModel.addElement("..");
		//populateList();
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
		//scrollPane.setBounds(insets.left+5,position,d.width-20,d.height-140);
		position+=5+scrollPane.getSize().height;
		
		JLabel label = new JLabel("Type");
		add(label);
		label.setBounds(insets.left+5,position,label.getPreferredSize().width,label.getPreferredSize().height);
		
		String[] options = {"Health","Petty Cash","Scan"};
		comboBox = new JComboBox(options);
		comboBox.setEditable(false);
		add(comboBox);
		comboBox.setBounds(insets.left+10+label.getPreferredSize().width,position,comboBox.getPreferredSize().width,comboBox.getPreferredSize().height);
		position+=5+comboBox.getPreferredSize().height;
		
		label = new JLabel("Port: ");
		add(label);
		label.setBounds(insets.left+5,position,label.getPreferredSize().width,label.getPreferredSize().height);
		
		SpinnerModel model = new SpinnerNumberModel(0,0,31,1);
		portSpinner = new JSpinner(model);
		add(portSpinner);
		portSpinner.setBounds(insets.left+10+label.getPreferredSize().width,position,portSpinner.getPreferredSize().width,portSpinner.getPreferredSize().height);
		position+=5+portSpinner.getPreferredSize().height;
		
		JButton accept=new JButton("Install");
		accept.addActionListener(this);
		accept.setActionCommand("Accept");
		add(accept);
		accept.setBounds(insets.left+5,position,accept.getPreferredSize().width,accept.getPreferredSize().height);
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		add(cancel);
		cancel.setBounds(insets.left+10+accept.getPreferredSize().width,position,cancel.getPreferredSize().width,cancel.getPreferredSize().height);
	}
	
	
	public void populateList(){
		int j=0;
		try{
			shownDirectory = new Object[directory.length+1];
			listModel.clear();
			if(!folder.equals("")){
				listModel.addElement("..");
				shownDirectory[j]="..";
				j++;
			}
		
			for(int i=0;i<directory.length;i++){
				if(directory[i] instanceof String){
					if(!((String)(directory[i])).equals("Store")&&!((String)(directory[i])).equals("Public")&&!((String)(directory[i])).equals("Trash")){
						//System.out.println(directory[i]);
						shownDirectory[j]=directory[i];
						listModel.addElement((String)directory[i]);
						j++;
					}
				}
				else{
					Object dir[] = (Object[])directory[i];
					int type=(Integer)dir[1];
					if(type==HackerFile.WATCH_COMPILED){
						shownDirectory[j]=dir;
						listModel.addElement((String)dir[0]);
						j++;
					}
				}
				renderer = new ScriptEditorCellRenderer(shownDirectory,null);
				fileList.setCellRenderer(renderer);
			}
		}catch(NullPointerException e){
			populateList();
		}
	}
	
	public void setDirectory(Object[] directory){
		this.directory=directory;
		populateList();
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Accept")){
			String blah="";
			try{
				Object o =shownDirectory[fileList.getSelectedIndex()];
				if(o instanceof Object[]){
					Object ob[] = (Object[])o;
					blah = (String)ob[0];
                    setVisible(false);
					MyWatchManager.install(folder,blah,(String)comboBox.getSelectedItem(),(int)(Integer)portSpinner.getValue());
					MyHacker.setInstallFileChooser(null);
					dispose();
				}
				else{
					String fileName = (String)o;
					if(!fileName.equals("..")){
						folder=folder+(String)o+"/";
						folderField.setText("home/"+folder);
						MyHacker.setCurrentFolder(folder);
						//REQUEST DIRECTORY
						View MyView = MyHacker.getView();
						String ip=MyHacker.getEncryptedIP();
						MyHacker.setRequestedDirectory(Hacker.WATCH_INSTALL_FILE_CHOOSER);
						Object objects[] = {ip,folder};
						MyView.setFunction("requestdirectory");
						MyView.addFunctionCall(new RemoteFunctionCall(Hacker.WATCH_INSTALL_FILE_CHOOSER,"requestdirectory",objects));
					}
					else{
						String folders[] = folder.split("/");
						String newFolder="";
						for(int i=0;i<folders.length-1;i++){
							 newFolder+=folders[i]+"/";
						}
						folder=newFolder;
						MyHacker.setCurrentFolder(folder);
						folderField.setText("home/"+folder);
						View MyView = MyHacker.getView();
						MyHacker.setRequestedDirectory(Hacker.WATCH_INSTALL_FILE_CHOOSER);
						String ip=MyHacker.getEncryptedIP();
						Object objects[] = {ip,folder};
						MyView.setFunction("requestdirectory");
						MyView.addFunctionCall(new RemoteFunctionCall(Hacker.WATCH_INSTALL_FILE_CHOOSER,"requestdirectory",objects));
					}
				}
			}catch(ArrayIndexOutOfBoundsException ex){
			}
			
		}
		
		if(e.getActionCommand().equals("Cancel")){
			MyHacker.setInstallFileChooser(null);
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
					//REQUEST DIRECTORY
					View MyView = MyHacker.getView();
					String ip=MyHacker.getEncryptedIP();
					MyHacker.setRequestedDirectory(Hacker.WATCH_INSTALL_FILE_CHOOSER);
					Object objects[] = {ip,folder};
					MyView.setFunction("requestdirectory");
					MyView.addFunctionCall(new RemoteFunctionCall(Hacker.WATCH_INSTALL_FILE_CHOOSER,"requestdirectory",objects));
				}
				else{
					String folders[] = folder.split("/");
					String newFolder="";
					for(int i=0;i<folders.length-1;i++){
						 newFolder+=folders[i]+"/";
					}
					folder=newFolder;
					MyHacker.setCurrentFolder(folder);
					folderField.setText("home/"+folder);
					View MyView = MyHacker.getView();
					MyHacker.setRequestedDirectory(Hacker.WATCH_INSTALL_FILE_CHOOSER);
					String ip=MyHacker.getEncryptedIP();
					Object objects[] = {ip,folder};
					MyView.setFunction("requestdirectory");
					MyView.addFunctionCall(new RemoteFunctionCall(Hacker.WATCH_INSTALL_FILE_CHOOSER,"requestdirectory",objects));
				}
				
			}
			else{
				String blah="";
				try{
					Object ob[] = (Object[])o;
					blah = (String)ob[0];
					//answer = directory[fileList.getSelectedIndex()];
				}catch(ArrayIndexOutOfBoundsException ex){
				}
                setVisible(false);
				MyWatchManager.install(folder,blah,(String)comboBox.getSelectedItem(),(int)(Integer)portSpinner.getValue());
				MyHacker.setWatchInstallFileChooser(null);
				dispose();
			}
		}		
	}
		
}
