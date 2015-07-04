package GUI;
/**

ScriptEditor.java
this is the deposit window.
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
import java.net.URL;
import java.text.*;
import Hackscript.Model.*;
import Hacktendo.*;

public class HacktendoScriptEditor extends ScriptEditor{
	private final int TEXT_LIMIT = 30000; 
	private JDesktopPane mainPanel=null;
	private JTabbedPane tabbedPane;
	private ScriptInternalFunctionPane tabs[] = new ScriptInternalFunctionPane[10];
	private boolean fileChooserOpen=false;
	private HacktendoCreator MyHacktendoCreator=null;
	private JLabel MyLabel=new JLabel("  (0:0)");

	
	public HacktendoScriptEditor(String name,boolean resize,boolean max,boolean close,boolean iconify,JDesktopPane mainPanel,HacktendoCreator MyHacktendoCreator){
		this.setTitle(name);
		this.setResizable(resize);
		this.setMaximizable(max);
		this.setClosable(close);
		this.setIconifiable(iconify);
		this.addInternalFrameListener(this);
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		this.mainPanel=mainPanel;
		this.MyHacktendoCreator=MyHacktendoCreator;
		setLayout(null);
	        setBounds(20,20,700,500);
		setVisible(true);
		populate();
		this.setFrameIcon(ImageLoader.getImageIcon("images/edit.png"));
	}
	
	public void setCursorPosition(int line,int character){
		MyLabel.setText("  ("+line+":"+character+")");
	}

	public void populate(){
		//Menu
		JMenuBar menuBar= new JMenuBar();
		JMenu menu,subMenu;
		JMenuItem menuItem;

		menu=new JMenu("File");

		menuItem=new JMenuItem("Close",ImageLoader.getImageIcon("images/close.png"));
		menuItem.setMnemonic(KeyEvent.VK_C);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_C, ActionEvent.ALT_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);

		menuItem=new JMenuItem("Save",ImageLoader.getImageIcon("images/save.png"));
		menuItem.setMnemonic(KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);

		menuItem=new JMenuItem("Save As",ImageLoader.getImageIcon("images/saveas.png"));
		menuItem.setMnemonic(KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_S, ActionEvent.ALT_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);

		menuBar.add(menu);

		menu = new JMenu("Edit");

		menuItem=new JMenuItem("Undo",ImageLoader.getImageIcon("images/undo.png"));
		menuItem.setMnemonic(KeyEvent.VK_Z);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);

		menuItem=new JMenuItem("Redo",ImageLoader.getImageIcon("images/redo.png"));
		menuItem.setMnemonic(KeyEvent.VK_Y);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);

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

		this.setJMenuBar(menuBar);

		//toolbar
		JToolBar toolBar = new JToolBar("Tools");

		Insets insets = this.getInsets();
		Rectangle size = this.getBounds();

		JButton button = new JButton(ImageLoader.getImageIcon("images/close.png"));
		button.setActionCommand("Close");
		button.setToolTipText("Close current script.");
		button.addActionListener(this);
		toolBar.add(button);

		button = new JButton(ImageLoader.getImageIcon("images/save.png"));
		button.setActionCommand("Save");
		button.setToolTipText("Save current script.");
		button.addActionListener(this);
		toolBar.add(button);

		button = new JButton(ImageLoader.getImageIcon("images/saveas.png"));
		button.setActionCommand("Save As");
		button.setToolTipText("Save current script under new name.");
		button.addActionListener(this);
		toolBar.add(button);

		toolBar.addSeparator();

		button = new JButton(ImageLoader.getImageIcon("images/undo.png"));
		button.setActionCommand("Undo");
		button.setToolTipText("Undo last action.");
		button.addActionListener(this);
		toolBar.add(button);

		button = new JButton(ImageLoader.getImageIcon("images/redo.png"));
		button.setActionCommand("Redo");
		button.setToolTipText("Redo last undo.");
		button.addActionListener(this);
		toolBar.add(button);

		button = new JButton(ImageLoader.getImageIcon("images/copy.png"));
		button.setActionCommand("Copy");
		button.setToolTipText("Copy highlighted text.");
		button.addActionListener(this);
		toolBar.add(button);

		button = new JButton(ImageLoader.getImageIcon("images/cut.png"));
		button.setActionCommand("Cut");
		button.setToolTipText("Cut highlighted text.");
		button.addActionListener(this);
		toolBar.add(button);

		button = new JButton(ImageLoader.getImageIcon("images/paste.png"));
		button.setActionCommand("Paste");
		button.setToolTipText("Paste copied or cut text.");
		button.addActionListener(this);
		toolBar.add(button);

		button = new JButton(ImageLoader.getImageIcon("images/compile.png"));
		button.setActionCommand("Compile");
		button.setToolTipText("Test compile this script.");
		button.addActionListener(this);
		toolBar.add(button);
		
		//toolBar.add(MyLabel);

		Dimension dim = toolBar.getPreferredSize();
		toolBar.setBounds(insets.left,insets.top,size.width,dim.height);
		this.add(toolBar);

		//Editor
		tabbedPane = new JTabbedPane();
		this.add(tabbedPane);
		tabbedPane.setBounds(insets.left,insets.top+dim.height,size.width-20,size.height-insets.top-dim.height-70);
	}

	public void openFile(String answer){
		int id = Integer.parseInt(answer);
		Script script = MyHacktendoCreator.getScript(id);
		String name=script.getName();
		HashMap HM = script.getScripts();
		if(HM!=null){
			int type = script.getType();
			if(type==Script.MAP){
				int index=0;
				if(tabbedPane.getTabCount()!=0)
					index=tabbedPane.getTabCount();
				String[] newTabs = {"Initialize","Finalize","Continue"};
				tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.MAP,name,tabbedPane,false,this);
				tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
				tabs[index].setFileLocation(""+id);
				tabbedPane.addTab(id+": "+name,tabs[index]);
				if(tabbedPane.getTabCount()!=1){
					index = tabbedPane.getTabCount();
					tabbedPane.setSelectedIndex(index-1);
				}
				if(index==0)
					index=1;
				ScriptEditorPane ep[] = tabs[index-1].getTabs();
				String initialize = openFunctionsDefined((String)HM.get("initialize"));
				String finalize = openFunctionsDefined((String)HM.get("finalize"));
				String continues = openFunctionsDefined((String)HM.get("continue"));
				ep[0].setText(initialize);
				ep[1].setText(finalize);
				ep[2].setText(continues);
				ep[0].requestFocusInWindow();
			}
			else if(type==Script.SPRITE){
				int index=0;
				if(tabbedPane.getTabCount()!=0)
					index=tabbedPane.getTabCount();
				String[] newTabs = {"Fire"};
				tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.SPRITE,name,tabbedPane,false,this);
				tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
				tabbedPane.addTab(id+": "+name,tabs[index]);
				tabs[index].setFileLocation(""+id);
				if(tabbedPane.getTabCount()!=1){
					index = tabbedPane.getTabCount();
					tabbedPane.setSelectedIndex(index-1);
				}
				if(index==0)
					index=1;
				ScriptEditorPane ep[] = tabs[index-1].getTabs();
				String fire = openFunctionsDefined((String)HM.get("fire"));
				ep[0].setText(fire);
			}
		}
		
	}
			
	
	public void saveFile(String name,int id){
		ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
		ScriptEditorPane eps[] = SIFP.getTabs();
		HashMap HM = new HashMap();
		if(SIFP.getType()==ScriptInternalFunctionPane.MAP){
			String initialize=eps[0].getText();
			if(initialize.length()>TEXT_LIMIT)
				initialize=initialize.substring(0,TEXT_LIMIT);
			String finalize=eps[1].getText();
			if(finalize.length()>TEXT_LIMIT)
				finalize=finalize.substring(0,TEXT_LIMIT);
			String continues=eps[2].getText();
			if(continues.length()>TEXT_LIMIT)
				continues=continues.substring(0,TEXT_LIMIT);
			HM.put("initialize",functionsDefined(initialize));
			HM.put("finalize",functionsDefined(finalize));
			HM.put("continue",functionsDefined(continues));	
			
		}
		else if(SIFP.getType()==SIFP.SPRITE){
			String fire=eps[0].getText();
			if(fire.length()>TEXT_LIMIT)
				fire=fire.substring(0,TEXT_LIMIT);
			HM.put("fire",functionsDefined(fire));	
		}
		//HM.put("name",name);
		//HM.put("type",SIFP.getType());
		int type=0;
		if(SIFP.getType()==SIFP.SPRITE)
			type=1;
		Script script = new Script(name,HM,type,id);
		MyHacktendoCreator.setScript(id,script);
		
	}
	
	public static String functionsDefined(String function){
		if(function.indexOf("#define functions")!=-1){
			function = function.replaceAll("#define","//#define");
			return function;
		}
		function = "int main(){\n"+function+"\n}";
		return function;
	}
	
	public static String openFunctionsDefined(String function){
		if(function.indexOf("#define functions")!=-1){
			function = function.replaceAll("//#define","#define");
			return function;
		}
		//function = "int main(){\n"+function+"\n}";
		function = function.substring(0,function.length()-2);
		function = function.replaceAll("int main\\(\\)\\{\\\n","");
		return function;
	}
	
	public void newSpriteScript(){
		int index=0;
		if(tabbedPane.getTabCount()!=0)
			index=tabbedPane.getTabCount();
		String[] newTabs = {"Fire"};
		tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.SPRITE,"Untitled",tabbedPane,true,this);
		tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
		tabbedPane.addTab("Untitled *",tabs[index]);
		if(tabbedPane.getTabCount()!=1){
			index = tabbedPane.getTabCount();
			tabbedPane.setSelectedIndex(index-1);
		}
		tabs[index-1].getCurrentTab().requestFocusInWindow();
	}
	
	public void newMapScript(){
		int index=0;
		if(tabbedPane.getTabCount()!=0)
			index=tabbedPane.getTabCount();
		String[] newTabs = {"Initialize","Finalize","Continue"};
		tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.MAP,"Untitled",tabbedPane,true,this);
		tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
		tabbedPane.addTab("Untitled *",tabs[index]);
		if(tabbedPane.getTabCount()!=1){
			index = tabbedPane.getTabCount();
			tabbedPane.setSelectedIndex(index-1);
		}
		tabs[index-1].getCurrentTab().requestFocusInWindow();
	}
	
	public void actionPerformed(ActionEvent e){
		String ac = e.getActionCommand();
		if(ac==null){
			//System.out.println(e.paramString());
			return;
		}
		if(ac.equals("Exit")){
			doDefaultCloseAction();
		}
		if (ac.equals("New")){
			String[] options = {"Map Script","Sprite Script"};
			String title = "New Script";
			String answer = (String)JOptionPane.showInputDialog(this,
				"Which Type of Script?",
				title,
				JOptionPane.PLAIN_MESSAGE,
				ImageLoader.getImageIcon("images/newBig.png"),
				options,
				"Banking");
			if(answer==null)
				return;
			ac=answer;
		}
		if(ac.equals("Map Script")){
			newMapScript();
		}
		if(ac.equals("Sprite Script")){
			newSpriteScript();
		}
		if(ac.equals("Close")){
			String options[] = {"Save","Don't Save","Cancel"};
			int i = tabbedPane.getSelectedIndex();
			ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
			ScriptEditorPane eps[] = SIFP.getTabs();
			boolean changed=false;
			for(int j=0;j<eps.length;j++){
				if(eps[j].getChanged()){
					//this has been changed, ask if they want to save it or not.
					changed=true;
				}
			}
			if(changed){
				int n = JOptionPane.showOptionDialog(this,
						"Would you like to save "+SIFP.getTitle()+"?",
						"Save?",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[2]);
				if(n==0){
					//do the saving stuff here.
				}
				if(n!=2){
					tabbedPane.remove(i);
					for(int index=i;index<tabbedPane.getTabCount();index++){
						tabs[index]=tabs[index+1];
					}
				}
			}
			else{
				tabbedPane.remove(i);
				for(int index=i;index<tabbedPane.getTabCount();index++){
						tabs[index]=tabs[index+1];
				}
			}
		}
		if(ac.equals("Save")){
			ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
			int id = Integer.parseInt(SIFP.getFileLocation());
			saveFile(MyHacktendoCreator.getScript(id).getName(),Integer.parseInt(SIFP.getFileLocation()));
			
			
		}
		if(ac.equals("Save As")){
			ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
			String name = SIFP.getName();
			String answer = (String)JOptionPane.showInputDialog(
				    this,
				    "Name:",
				    "Save As",
				    JOptionPane.PLAIN_MESSAGE,
				    null,
				    null,
				    name);
			if(answer!=null){
				SIFP.setName(answer);
				tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(),SIFP.getFileLocation()+": "+answer);
				saveFile(answer,Integer.parseInt(SIFP.getFileLocation()));
			}
			
		}
		if(ac.equals("Open")){
			
		}
		
		if(ac.equals("Copy")){
			if(tabbedPane.getTabCount()>0){
				ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
				ScriptEditorPane SEP = SIFP.getCurrentTab();
				SEP.copy();
			}
		}
		if(ac.equals("Cut")){
			if(tabbedPane.getTabCount()>0){
				ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
				ScriptEditorPane SEP = SIFP.getCurrentTab();
				SEP.cut();
			}
		}
		if(ac.equals("Paste")){
			if(tabbedPane.getTabCount()>0){
				ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
				ScriptEditorPane SEP = SIFP.getCurrentTab();
				SEP.paste();
			}
		}
		if(ac.equals("Undo")){
			if(tabbedPane.getTabCount()>0){
				ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
				ScriptEditorPane SEP = SIFP.getCurrentTab();
				UndoManager UM = SEP.getUndoManager();
				try{
					UM.undo();
				}catch(CannotUndoException cue){}
			}
		}
		if(ac.equals("Redo")){
			if(tabbedPane.getTabCount()>0){
				ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
				ScriptEditorPane SEP = SIFP.getCurrentTab();
				UndoManager UM = SEP.getUndoManager();
				try{
					UM.redo();
				}catch(CannotRedoException cre){}
			}
		}
		
		//Test compile the script.
		if(ac.equals("Compile")){
			ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
			int id = Integer.parseInt(SIFP.getFileLocation());
			HashMap ScriptCheck=MyHacktendoCreator.getScript(id).getScripts();
			String event=SIFP.getTitleAt(SIFP.getSelectedIndex());
			ScriptEditorPane eps[] = SIFP.getTabs();
			try{
				for(int i=0;i<eps.length;i++){
					String code = functionsDefined(eps[i].getText());
					RunFactory.compileCode(code);
				}
				
				JOptionPane.showMessageDialog(this,
					"Script compiled successfully.",
					"Script compiled successfully",
					JOptionPane.INFORMATION_MESSAGE);
			}catch(Exception e2){
				if(e2 instanceof ModelError){
							JOptionPane.showMessageDialog(this,
								e2.getMessage(),
								"Error compiling "+event+".",
								JOptionPane.ERROR_MESSAGE);
				}
			}
			
		}
	}
	public void internalFrameIconified(InternalFrameEvent e) {
		JDesktopIcon DI = getDesktopIcon();
		DI.setBackground(new Color(41,42,41));
		
		JButton b = (JButton)DI.getUI().getAccessibleChild(DI,0);
		JLabel l = (JLabel)DI.getUI().getAccessibleChild(DI,1);
		//b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		b.setForeground(Color.WHITE);
		
		//b.setBorderPainted(false);
		
		DI.getUI().update(DI.getGraphics(),DI);
		//getDesktopIcon().setLocation(162,182);
		//getDesktopIcon().moveToFront();
	}
	
	public void internalFrameDeiconified(InternalFrameEvent e){
	
	
	}
}

