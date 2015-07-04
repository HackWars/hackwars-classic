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

public class BountyFileChooser extends JInternalFrame implements ActionListener,MouseListener{
	//data
	private String folder;
	private Object[] directory,shownDirectory;
	private BountyWindow MyBountyWindow;
	private int type;
	private JList fileList;
	private DefaultListModel listModel = new DefaultListModel();
	private int port;
	private ScriptEditorCellRenderer renderer;
	private JTextField folderField;
	private Hacker MyHacker;
	
	public BountyFileChooser(Hacker MyHacker,BountyWindow MyBountyWindow){
		this.MyHacker=MyHacker;
		this.MyBountyWindow=MyBountyWindow;
		MyHacker.setBountyFileChooser(this);
		MyHacker.setRequestedDirectory(Hacker.BOUNTY_FILE_CHOOSER);
		this.folder="";
		View MyView = MyHacker.getView();
		Object objects[] = {MyHacker.getEncryptedIP(),""};
		MyHacker.setCurrentFolder("");
		MyView.setFunction("requestdirectory");
		MyView.addFunctionCall(new RemoteFunctionCall(Hacker.BOUNTY_FILE_CHOOSER,"requestdirectory",objects));
		setTitle("Choose File");
		setFrameIcon(ImageLoader.getImageIcon("images/open.png"));
		setBounds(100,100,360,340);
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
		scrollPane.setBounds(insets.left+5,position,d.width-20,d.height-120);
		//scrollPane.setBounds(insets.left+5,position,d.width-20,d.height-140);
		position+=5+scrollPane.getSize().height;
		
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
		if(directory!=null){
			shownDirectory = new Object[directory.length+1];
			listModel.clear();
			if(!folder.equals("")){
				listModel.addElement("..");
				shownDirectory[j]="..";
				j++;
			}
			try{
				for(int i=0;i<directory.length;i++){
					if(directory[i] instanceof String){
						if(!((String)(directory[i])).equals("Store")&&!((String)(directory[i])).equals("Public")){
							//System.out.println(directory[i]);
							shownDirectory[j]=directory[i];
							listModel.addElement((String)directory[i]);
							j++;
						}
					}
					else{
						Object dir[] = (Object[])directory[i];
						int type=(Integer)dir[1];
						if(type==HackerFile.BANKING_COMPILED||type==HackerFile.ATTACKING_COMPILED||type==HackerFile.FTP_COMPILED||type==HackerFile.HTTP){
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
	}
	
	public void setDirectory(Object[] directory){
		this.directory=directory;
		//renderer.setDirectory(directory);
		
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
					//MyPortManagement.install(port,folder,blah,type);
					MyBountyWindow.fileSelected(blah,folder);
					MyHacker.setBountyFileChooser(null);
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
						MyHacker.setRequestedDirectory(Hacker.BOUNTY_FILE_CHOOSER);
						Object objects[] = {ip,folder};
						MyView.setFunction("requestdirectory");
						MyView.addFunctionCall(new RemoteFunctionCall(Hacker.BOUNTY_FILE_CHOOSER,"requestdirectory",objects));
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
						MyHacker.setRequestedDirectory(Hacker.BOUNTY_FILE_CHOOSER);
						String ip=MyHacker.getEncryptedIP();
						Object objects[] = {ip,folder};
						MyView.setFunction("requestdirectory");
						MyView.addFunctionCall(new RemoteFunctionCall(Hacker.BOUNTY_FILE_CHOOSER,"requestdirectory",objects));
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
					MyHacker.setRequestedDirectory(Hacker.BOUNTY_FILE_CHOOSER);
					Object objects[] = {ip,folder};
					MyView.setFunction("requestdirectory");
					MyView.addFunctionCall(new RemoteFunctionCall(Hacker.BOUNTY_FILE_CHOOSER,"requestdirectory",objects));
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
					MyHacker.setRequestedDirectory(Hacker.BOUNTY_FILE_CHOOSER);
					String ip=MyHacker.getEncryptedIP();
					Object objects[] = {ip,folder};
					MyView.setFunction("requestdirectory");
					MyView.addFunctionCall(new RemoteFunctionCall(Hacker.BOUNTY_FILE_CHOOSER,"requestdirectory",objects));
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
				//MyPortManagement.install(port,folder,blah,type);
				MyBountyWindow.fileSelected(blah,folder);
				MyHacker.setBountyFileChooser(null);
				dispose();
			}
		}		
	}
		
}
