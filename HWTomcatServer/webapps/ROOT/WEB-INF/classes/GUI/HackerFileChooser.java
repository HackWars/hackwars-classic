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

public class HackerFileChooser extends JInternalFrame implements ActionListener,MouseListener{
	//data
	public static final int SAVE=0;
	public static final int OPEN=1;
	private String folder;
	private Object[] directory,shownDirectory;
	private Hacker MyHacker;
	private int type;
	private JList fileList;
	private DefaultListModel listModel = new DefaultListModel();
	private Object answer=null;
	private ScriptEditor MyScriptEditor;
	private JTextField saveField,folderField;
	private ScriptEditorCellRenderer renderer;
	
	public HackerFileChooser(Hacker MyHacker,int type,String folder,Object[] directory,ScriptEditor MyScriptEditor){
		this.MyHacker=MyHacker;
		this.type=type;
		this.folder=folder;
		this.directory=directory;
		directory = MyHacker.getCurrentDirectory();
		//System.out.println(folder);
		MyHacker.setFileChooser(this);
		MyHacker.setRequestedDirectory(Hacker.FILE_CHOOSER);
		if(folder.equals("Store")||folder.equals("Public")||directory==null){
			this.folder="";
			View MyView = MyHacker.getView();
			Object objects[] = {MyHacker.getEncryptedIP(),""};
			MyHacker.setCurrentFolder("");
			MyView.setFunction("requestdirectory");
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.FILE_CHOOSER,"requestdirectory",objects));
		}
		this.MyScriptEditor=MyScriptEditor;
		if(type==OPEN){
			setTitle("Open File");
			setFrameIcon(ImageLoader.getImageIcon("images/open.png"));
		}
		if(type==SAVE){
			setTitle("Save File");
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
						if(type==HackerFile.BANKING_SCRIPT||type==HackerFile.ATTACKING_SCRIPT||type==HackerFile.WATCH_SCRIPT||type==HackerFile.FTP_SCRIPT||type==HackerFile.HTTP_SCRIPT||type==HackerFile.TEXT||type==HackerFile.SHIPPING_SCRIPT){
							shownDirectory[j]=dir;
							j++;
							listModel.addElement((String)dir[0]);
						}
					}
				}
				renderer = new ScriptEditorCellRenderer(shownDirectory,this);
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
				MyScriptEditor.openFile(blah,folder);
			}
			if(type==SAVE){
				MyScriptEditor.saveFile(saveField.getText(),folder);
			}
			MyHacker.setFileChooser(null);
			MyScriptEditor.setFileChooserClosed();
			dispose();
		}
		
		if(e.getActionCommand().equals("Cancel")){
			MyHacker.setFileChooser(null);
			MyScriptEditor.setFileChooserClosed();
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
					MyView.addFunctionCall(new RemoteFunctionCall(Hacker.FILE_CHOOSER,"requestdirectory",objects));
					MyHacker.setRequestedDirectory(Hacker.FILE_CHOOSER);
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
					MyHacker.setRequestedDirectory(Hacker.FILE_CHOOSER);
					Object objects[] = {ip,folder};
					MyView.setFunction("requestdirectory");
					MyView.addFunctionCall(new RemoteFunctionCall(Hacker.FILE_CHOOSER,"requestdirectory",objects));
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
