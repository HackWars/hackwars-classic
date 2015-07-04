package GUI;
/**

HackerFileChooser.java
this is for the script editor to save and open files.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Game.*;
import View.*;
import Assignments.*;

public class HacktendoFileChooser extends JInternalFrame implements ActionListener,MouseListener{
	//data
	public static final int SAVE=0;
	public static final int OPEN=1;
	public static final int PACK=2;
	private String folder;
	private Object[] directory,shownDirectory;
	private Hacker MyHacker;
	private int type;
	private JList fileList;
	private DefaultListModel listModel = new DefaultListModel();
	private Object answer=null;
	private HacktendoCreator hacktendoCreator;
	private JTextField saveField,folderField;
	private HacktendoCellRenderer renderer;
	private int allowedType=HackerFile.GAME_PROJECT;
	
	public HacktendoFileChooser(Hacker MyHacker,int type,String folder,Object[] directory,HacktendoCreator hacktendoCreator){
		this.MyHacker=MyHacker;
		this.type=type;
		this.folder=folder;
		this.directory=directory;
		//directory = MyHacker.getCurrentDirectory();
		//System.out.println(folder);
		MyHacker.setHacktendoFileChooser(this);
		MyHacker.setRequestedDirectory(Hacker.HACKTENDO_FILE_CHOOSER);
		if(folder.equals("Store")||folder.equals("Public")||directory==null){
			this.folder="";
			View MyView = MyHacker.getView();
			Object objects[] = {MyHacker.getEncryptedIP(),""};
			MyHacker.setCurrentFolder("");
			MyView.setFunction("requestdirectory");
			System.out.println("Requesting Directory");
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HACKTENDO_FILE_CHOOSER,"requestdirectory",objects));
		}
		this.hacktendoCreator=hacktendoCreator;
		if(type==OPEN){
			setTitle("Open Game");
			setFrameIcon(ImageLoader.getImageIcon("images/open.png"));
		}
		else if(type==SAVE){
			setTitle("Save Game");
			setFrameIcon(ImageLoader.getImageIcon("images/save.png"));
		}
		else if(type==PACK){
			allowedType=HackerFile.GAME;
			setTitle("Pack Game");
			setFrameIcon(ImageLoader.getImageIcon("images/save.png"));
		}
		setBounds(100,100,360,270);
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
		//listModel.addElement("..");
		
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.setLayoutOrientation(JList.VERTICAL_WRAP);
		fileList.setVisibleRowCount(6);
		/*renderer = new ScriptEditorCellRenderer(directory,this);
		fileList.setCellRenderer(renderer);*/
		fileList.setSelectedIndex(0);
		fileList.addMouseListener(this);
		populateList();
		
		JScrollPane scrollPane = new JScrollPane(fileList);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);  
		add(scrollPane);
		scrollPane.setBounds(insets.left+5,position,d.width-20,d.height-120);
		scrollPane.setBounds(insets.left+5,position,d.width-20,d.height-140);
		position+=5+scrollPane.getSize().height;
		saveField = new JTextField(20);
		saveField.requestFocusInWindow();
		add(saveField);
		
		saveField.setBounds(insets.left+5,position,saveField.getPreferredSize().width,saveField.getPreferredSize().height);
		position+=5+saveField.getPreferredSize().height;
		JButton accept = new JButton();
		if(type==SAVE)
			accept.setText("Save");
		if(type==OPEN)
			accept.setText("Open");
		if(type==PACK)
			accept.setText("Pack");
		
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
		try{
			if(directory!=null){
				shownDirectory = new Object[directory.length+1];
				int j=0;
				listModel.clear();
				if(!folder.equals("")){
					listModel.addElement("..");
					shownDirectory[j] = "..";
					j++;
				}
				for(int i=0;i<directory.length;i++){
					if(directory[i] instanceof String){
						String dir = (String)directory[i];
						if(!dir.equals("Store")&&!dir.equals("Public")){
							shownDirectory[j]=dir;
							j++;
							listModel.addElement(dir);
						}
					}
					else{
						
						Object dir[] = (Object[])directory[i];
						int type=(Integer)dir[1];
						//System.out.println(dir[0]+" -- "+type);
						
						if(type==allowedType){
							shownDirectory[j]=dir;
							j++;
							listModel.addElement((String)dir[0]);
						}
					}
				}
				renderer = new HacktendoCellRenderer(shownDirectory,this);
				fileList.setCellRenderer(renderer);
			}
		}catch(NullPointerException e){
			populateList();
		}
	}
	
	public Object getAnswer(){
		return(answer);
	}
	public void setSaveField(String text){
		saveField.setText(text);
	}
	
	public void setDirectory(Object[] directory){
		System.out.println("Received Directory");
		this.directory=directory;
		//renderer.setDirectory(directory);
		populateList();
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Accept")){
			if(type==OPEN){
				String blah="";
				try{
					Object ob[] = (Object[])shownDirectory[fileList.getSelectedIndex()];
					blah = (String)ob[0];
					//System.out.println(blah);
					//answer = directory[fileList.getSelectedIndex()];
				}catch(ArrayIndexOutOfBoundsException ex){
					//System.out.println("No File Selected");
				}
				hacktendoCreator.openGame(blah,folder);
			}
			else if(type==SAVE){
				hacktendoCreator.saveGame(saveField.getText(),folder);
			}
			else if(type==PACK){
				hacktendoCreator.packGame(saveField.getText(),folder);
			}
			MyHacker.setFileChooser(null);
			
			dispose();
		}
		
		if(e.getActionCommand().equals("Cancel")){
			MyHacker.setFileChooser(null);
			
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
					folder=folder+fileName+"/";
					folderField.setText("home/"+folder);
					//System.out.println(folder);
					MyHacker.setCurrentFolder(folder);
					//REQUEST DIRECTORY
					View MyView = MyHacker.getView();
					String ip=MyHacker.getEncryptedIP();
					Object objects[] = {ip,folder};
					MyView.setFunction("requestdirectory");
					MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HACKTENDO_FILE_CHOOSER,"requestdirectory",objects));
					MyHacker.setRequestedDirectory(Hacker.HACKTENDO_FILE_CHOOSER);
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
					//System.out.println(newFolder);
					View MyView = MyHacker.getView();
					String ip=MyHacker.getEncryptedIP();
					MyHacker.setRequestedDirectory(Hacker.HACKTENDO_FILE_CHOOSER);
					Object objects[] = {ip,folder};
					MyView.setFunction("requestdirectory");
					MyView.addFunctionCall(new RemoteFunctionCall(Hacker.HACKTENDO_FILE_CHOOSER,"requestdirectory",objects));
				}
			}
			else{
				
			}
			/*if(o instanceof Object[]){
				String name=(String)o[0];
				int type=(int)o[1];
				if(type==OPEN){
					MyScriptEditor.openFile(name);
					MyHacker.setFileChooser(null);
					dispose();
				}
			}*/
		}		
	}
		
}
