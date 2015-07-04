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
import jsyntaxpane.*;

public class ScriptEditor extends Application{
	private final int TEXT_LIMIT = 60000; 
	private final String[] banking = {"","lowerDeposit","mediumDeposit","higherDeposit","greaterDeposit","withdraw","lowerTransfer","mediumTransfer","higherTransfer","greaterTransfer","getSourceIP","getMaliciousTargetIP","getTargetIP","getPettyCash","getAmount","getPettyCashTarget","message"};
	private final String[] FTP = {"","put","get","message","getTargetIP","getMaliciousIP","getSourceIP"};
	private final String[] attack = {"","switchAttack","getCPULoad","cancelAttack","getTargetIP","getPortHealth","installScript","emptyPettyCash","showChoices","getPettyCash","getPortHealth","getMaximumCPULoad","getPettyCashTarget","message"};
	private final String[] watch = {"","checkForFireWall","startAttack","checkPettyCash","switchFireWall","switchAnyFireWall","depositPettyCash","checkFireWall","shutdownPorts","shutdownPort","heal","message"};
	private JDesktopPane mainPanel=null;
	private Hacker MyHacker=null;
	private JTabbedPane tabbedPane;
	private ScriptInternalFunctionPane tabs[] = new ScriptInternalFunctionPane[10];
	private boolean fileChooserOpen=false;
	private JLabel MyLabel=new JLabel("  (0:0)");
	private GridBagConstraints c = new GridBagConstraints();
	
	public ScriptEditor(){
		
		
	}
	
	public void setCursorPosition(String text){
		MyLabel.setText(text);
	}
	
	public ScriptEditor(String name,boolean resize,boolean max,boolean close,boolean iconify,JDesktopPane mainPanel,Hacker MyHacker){
		this.setTitle(name);
		this.setResizable(true);
		this.setMaximizable(true);
		this.setClosable(close);
		this.setIconifiable(iconify);
		this.addInternalFrameListener(this);
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		this.mainPanel=mainPanel;
		this.MyHacker=MyHacker;
		setLayout(new GridBagLayout());
		this.setFrameIcon(ImageLoader.getImageIcon("images/edit.png"));
	}

	public void populate(){
		//Menu
		JMenuBar menuBar= new JMenuBar();
		JMenu menu,subMenu;
		JMenuItem menuItem;

		menu=new JMenu("File");
		subMenu = new JMenu("New");
		menu.add(subMenu);
		menuItem=new JMenuItem("Bank Script", ImageLoader.getImageIcon("images/calc.png"));
		menuItem.setMnemonic(KeyEvent.VK_B);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_B, ActionEvent.CTRL_MASK));
		subMenu.add(menuItem);
		menuItem.addActionListener(this);
		menuItem=new JMenuItem("Attack Script",ImageLoader.getImageIcon("images/new.png"));
		menuItem.setMnemonic(KeyEvent.VK_A);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		subMenu.add(menuItem);
		menuItem.addActionListener(this);
		menuItem=new JMenuItem("FTP Script",ImageLoader.getImageIcon("images/ftp.png"));
		menuItem.setMnemonic(KeyEvent.VK_F);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		subMenu.add(menuItem);
		menuItem.addActionListener(this);
		menuItem=new JMenuItem("Watch Script", ImageLoader.getImageIcon("images/watch.png"));
		menuItem.setMnemonic(KeyEvent.VK_W);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		subMenu.add(menuItem);
		menuItem.addActionListener(this);
		menuItem=new JMenuItem("Redirect Script", ImageLoader.getImageIcon("images/redirect.png"));
		menuItem.setMnemonic(KeyEvent.VK_R);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		subMenu.add(menuItem);
		menuItem.addActionListener(this);
		menuItem=new JMenuItem("Challenge Script");
		subMenu.add(menuItem);
		menuItem.addActionListener(this);
		menuItem=new JMenuItem("HTTP Script");
		subMenu.add(menuItem);
		menuItem.addActionListener(this);
		menuItem=new JMenuItem("Text Document");
		subMenu.add(menuItem);
		menuItem.addActionListener(this);


		menuItem=new JMenuItem("Open",ImageLoader.getImageIcon("images/open.png"));
		menuItem.setMnemonic(KeyEvent.VK_O);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);

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

		menuItem=new JMenuItem("Exit",ImageLoader.getImageIcon("images/exit.png"));
		menuItem.setMnemonic(KeyEvent.VK_Q);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
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
		
		/*menuItem=new JMenuItem("Find & Replace");
		menuItem.setMnemonic(KeyEvent.VK_F);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);*/

		menuBar.add(menu);

		menu = new JMenu("Tools");
		
		/*menuItem = new JMenuItem("Insert Function Call",ImageLoader.getImageIcon("images/functioncall.png"));
		menuItem.setMnemonic(KeyEvent.VK_F3);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_F3, 0));
		menuItem.setActionCommand("FunctionCall");
		menu.add(menuItem);
		menuItem.addActionListener(this);*/

		menuItem = new JMenuItem("Test Compile Script",ImageLoader.getImageIcon("images/compiletest.png"));
		menuItem.setActionCommand("CompileTest");
		menuItem.setMnemonic(KeyEvent.VK_F4);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_F4, 0));
		menu.add(menuItem);
		menuItem.addActionListener(this);	

		menuItem = new JMenuItem("Compile Script",ImageLoader.getImageIcon("images/compile.png"));
		menuItem.setMnemonic(KeyEvent.VK_F5);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_F5, 0));
		menuItem.setActionCommand("Compile");
		menu.add(menuItem);
		menuItem.addActionListener(this);
		
		menuItem = new JMenuItem("Run Challenge Script",ImageLoader.getImageIcon("images/compiletest.png"));
		menuItem.setActionCommand("TestChallenge");
		menuItem.setMnemonic(KeyEvent.VK_F4);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_F4, 0));
		menu.add(menuItem);
		menuItem.addActionListener(this);
		
		//menu.add(MyLabel);

		menuBar.add(menu);
		

		this.setJMenuBar(menuBar);

		//toolbar
		JToolBar toolBar = new JToolBar("Tools");

		Insets insets = this.getInsets();
		Rectangle size = this.getBounds();

		JButton button = new JButton(ImageLoader.getImageIcon("images/new.png"));
		button.setActionCommand("New");
		button.setToolTipText("Create a new script.");
		button.addActionListener(this);
		toolBar.add(button);

		button = new JButton(ImageLoader.getImageIcon("images/open.png"));
		button.setActionCommand("Open");
		button.setToolTipText("Open an existing script.");
		button.addActionListener(this);
		toolBar.add(button);

		button = new JButton(ImageLoader.getImageIcon("images/close.png"));
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

		toolBar.addSeparator();

		/*button = new JButton(ImageLoader.getImageIcon("images/functioncall.png"));
		button.setActionCommand("FunctionCall");
		button.setToolTipText("Add a function call to script.");
		button.addActionListener(this);
		toolBar.add(button);*/

		button = new JButton(ImageLoader.getImageIcon("images/compiletest.png"));
		button.setActionCommand("CompileTest");
		button.setToolTipText("Test Compile script.");
		button.addActionListener(this);
		toolBar.add(button);

		button = new JButton(ImageLoader.getImageIcon("images/compile.png"));
		button.setActionCommand("Compile");
		button.setToolTipText("Compile script.");
		button.addActionListener(this);
		toolBar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/compiletest.png"));
		button.setActionCommand("TestChallenge");
		button.setToolTipText("Run Challenge script.");
		button.addActionListener(this);
		toolBar.add(button);
		
		//toolBar.add(MyLabel);
		
		/*button = new JButton(ImageLoader.getImageIcon("images/compiletest.png"));
		button.setActionCommand("RunBash");
		button.setToolTipText("Run Bash script.");
		button.addActionListener(this);
		toolBar.add(button);*/
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy=0;
		c.weightx=1.0;
		c.gridx=0;
		Dimension dim = toolBar.getPreferredSize();
		//toolBar.setBounds(insets.left,insets.top,size.width,dim.height);
		this.add(toolBar,c);

		//Editor
		c.gridy=1;
		c.weighty=0.9;
		c.fill=GridBagConstraints.BOTH;
		tabbedPane = new JTabbedPane();
		this.add(tabbedPane,c);
		//tabbedPane.setBounds(insets.left,insets.top+dim.height,size.width-20,size.height-insets.top-dim.height-70);
	}

	public void newBankScript(){
		int index=0;
		if(tabbedPane.getTabCount()!=0)
			index=tabbedPane.getTabCount();
		String[] newTabs = {"Deposit","Withdraw","Transfer"};
		tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.BANKING,"Untitled",tabbedPane,true,this);
		tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
		tabbedPane.addTab("Untitled *",tabs[index]);
		ScriptEditorPane ep[] = tabs[index].getTabs();
		ep[0].setText("int main(){\n\n}");
		ep[1].setText("int main(){\n\n}");
		ep[2].setText("int main(){\n\n}");
		ep[0].requestFocusInWindow();
		if(tabbedPane.getTabCount()!=1){
			index = tabbedPane.getTabCount();
			tabbedPane.setSelectedIndex(index-1);
		}

	}
	public void newAttackScript(){
		int index=0;
		if(tabbedPane.getTabCount()!=0)
			index=tabbedPane.getTabCount();
		String[] newTabs = {"Initialize","Finalize","Continue"};
		tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.ATTACK,"Untitled",tabbedPane,true,this);
		tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
		tabbedPane.addTab("Untitled *",tabs[index]);
		ScriptEditorPane ep[] = tabs[index].getTabs();
		ep[0].setText("int main(){\n\n}");
		ep[1].setText("int main(){\n\n}");
		ep[2].setText("int main(){\n\n}");
		ep[0].requestFocusInWindow();
		if(tabbedPane.getTabCount()!=1){
			index = tabbedPane.getTabCount();
			tabbedPane.setSelectedIndex(index-1);
		}
	}
	
	public void newShippingScript(){
		int index=0;
		if(tabbedPane.getTabCount()!=0)
			index=tabbedPane.getTabCount();
		String[] newTabs = {"Initialize","Finalize","Continue"};
		tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.SHIPPING,"Untitled",tabbedPane,true,this);
		tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
		tabbedPane.addTab("Untitled *",tabs[index]);
		ScriptEditorPane ep[] = tabs[index].getTabs();
		ep[0].setText("int main(){\n\n}");
		ep[1].setText("int main(){\n\n}");
		ep[2].setText("int main(){\n\n}");
		ep[0].requestFocusInWindow();
		if(tabbedPane.getTabCount()!=1){
			index = tabbedPane.getTabCount();
			tabbedPane.setSelectedIndex(index-1);
		}
	}
	
	public void newHTTPScript(){
		int index=0;
		if(tabbedPane.getTabCount()!=0)
			index=tabbedPane.getTabCount();
		String[] newTabs = {"enter","exit","submit"};
		tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.HTTP,"Untitled",tabbedPane,true,this);
		tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
		tabbedPane.addTab("Untitled *",tabs[index]);
		ScriptEditorPane ep[] = tabs[index].getTabs();
		ep[0].setText("int main(){\n\n}");
		ep[1].setText("int main(){\n\n}");
		ep[2].setText("int main(){\n\n}");
		ep[0].requestFocusInWindow();
		if(tabbedPane.getTabCount()!=1){
			index = tabbedPane.getTabCount();
			tabbedPane.setSelectedIndex(index-1);
		}
	}
	
	public void newFTPScript(){
		int index=0;
		if(tabbedPane.getTabCount()!=0)
			index=tabbedPane.getTabCount();
		String[] newTabs = {"Put","Get"};
		tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.FTP,"Untitled",tabbedPane,true,this);
		tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
		tabbedPane.addTab("Untitled *",tabs[index]);
		ScriptEditorPane ep[] = tabs[index].getTabs();
		ep[0].setText("int main(){\n\n}");
		ep[1].setText("int main(){\n\n}");
		ep[0].requestFocusInWindow();
		if(tabbedPane.getTabCount()!=1){
			index = tabbedPane.getTabCount();
			tabbedPane.setSelectedIndex(index-1);
		}
	}
	
	public void newWatchScript(){
		int index=0;
		if(tabbedPane.getTabCount()!=0)
			index=tabbedPane.getTabCount();
		String[] newTabs = {"Fire"};
		tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.WATCH,"Untitled",tabbedPane,true,this);
		tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
		tabbedPane.addTab("Untitled *",tabs[index]);
		ScriptEditorPane ep[] = tabs[index].getTabs();
		ep[0].setText("int main(){\n\n}");
		ep[0].requestFocusInWindow();
		if(tabbedPane.getTabCount()!=1){
			index = tabbedPane.getTabCount();
			tabbedPane.setSelectedIndex(index-1);
		}
	}
	public void newText(){
		int index=0;
		if(tabbedPane.getTabCount()!=0)
			index=tabbedPane.getTabCount();
		String[] newTabs = {"Content"};
		tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.TEXT,"Untitled",tabbedPane,true,this);
		tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
		tabbedPane.addTab("Untitled *",tabs[index]);
		ScriptEditorPane ep[] = tabs[index].getTabs();
		ep[0].requestFocusInWindow();
		if(tabbedPane.getTabCount()!=1){
			index = tabbedPane.getTabCount();
			tabbedPane.setSelectedIndex(index-1);
		}
		
	}
	public void newChallenge(){
		int index=0;
		if(tabbedPane.getTabCount()!=0)
			index=tabbedPane.getTabCount();
		String[] newTabs = {"Content"};
		tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.CHALLENGE,"Untitled",tabbedPane,true,this);
		tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
		tabbedPane.addTab("Untitled *",tabs[index]);
		ScriptEditorPane ep[] = tabs[index].getTabs();
		ep[0].setText("int main(){\n\n}");
		ep[0].requestFocusInWindow();
		if(tabbedPane.getTabCount()!=1){
			index = tabbedPane.getTabCount();
			tabbedPane.setSelectedIndex(index-1);
		}
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		MyHacker.setScriptEditorOpen(false);

	}
	
	public void openFile(String answer,String folder){
		//REQUEST FILE
		MyHacker.setRequestedFile(Hacker.SCRIPT_EDITOR);
		View MyView = MyHacker.getView();
		String ip=MyHacker.getEncryptedIP();
		Object objects[] = {ip,folder,answer};
		MyView.setFunction("requestfile");
		MyView.addFunctionCall(new RemoteFunctionCall(0,"requestfile",objects));
	}
	public void receivedFile(HackerFile HF){
		if(HF.getType()==HackerFile.BANKING_SCRIPT){
			int index=0;
			if(tabbedPane.getTabCount()!=0)
				index=tabbedPane.getTabCount();
			String[] newTabs = {"Deposit","Withdraw","Transfer"};
			tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.BANKING,HF.getName(),tabbedPane,false,this);
			tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
			tabs[index].setFileLocation(MyHacker.getCurrentFolder());
			tabbedPane.addTab(HF.getName(),tabs[index]);
			if(tabbedPane.getTabCount()!=1){
				index = tabbedPane.getTabCount();
				tabbedPane.setSelectedIndex(index-1);
			}
			if(index==0)
				index=1;
			ScriptEditorPane ep[] = tabs[index-1].getTabs();
			HashMap HM = HF.getContent();
			String deposit = openFunctionsDefined((String)HM.get("deposit"));
			String withdraw = openFunctionsDefined((String)HM.get("withdraw"));
			String transfer = openFunctionsDefined((String)HM.get("transfer"));
			ep[0].setText(deposit);
			ep[1].setText(withdraw);
			ep[2].setText(transfer);
			ep[0].requestFocusInWindow();
			ep[0].repaint();
		}
		
		if(HF.getType()==HackerFile.ATTACKING_SCRIPT){
			int index=0;
			if(tabbedPane.getTabCount()!=0)
				index=tabbedPane.getTabCount();
			String[] newTabs = {"Initialize","Finalize","Continue"};
			tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.ATTACK,HF.getName(),tabbedPane,false,this);
			tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
			tabs[index].setFileLocation(MyHacker.getCurrentFolder());
			tabbedPane.addTab(HF.getName(),tabs[index]);
			if(tabbedPane.getTabCount()!=1){
				index = tabbedPane.getTabCount();
				tabbedPane.setSelectedIndex(index-1);
			}
			if(index==0)
				index=1;
			ScriptEditorPane ep[] = tabs[index-1].getTabs();
			HashMap HM = HF.getContent();
			String initialize = openFunctionsDefined((String)HM.get("initialize"));
			String finalize = openFunctionsDefined((String)HM.get("finalize"));
			String continues = openFunctionsDefined((String)HM.get("continue"));
			ep[0].setText(initialize);
			ep[1].setText(finalize);
			ep[2].setText(continues);
			ep[0].requestFocusInWindow();
			ep[0].repaint();
		}
		
		if(HF.getType()==HackerFile.SHIPPING_SCRIPT){
			int index=0;
			if(tabbedPane.getTabCount()!=0)
				index=tabbedPane.getTabCount();
			String[] newTabs = {"Initialize","Finalize","Continue"};
			tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.SHIPPING,HF.getName(),tabbedPane,false,this);
			tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
			tabs[index].setFileLocation(MyHacker.getCurrentFolder());
			tabbedPane.addTab(HF.getName(),tabs[index]);
			if(tabbedPane.getTabCount()!=1){
				index = tabbedPane.getTabCount();
				tabbedPane.setSelectedIndex(index-1);
			}
			if(index==0)
				index=1;
			ScriptEditorPane ep[] = tabs[index-1].getTabs();
			HashMap HM = HF.getContent();
			String initialize = openFunctionsDefined((String)HM.get("initialize"));
			String finalize = openFunctionsDefined((String)HM.get("finalize"));
			String continues = openFunctionsDefined((String)HM.get("continue"));
			ep[0].setText(initialize);
			ep[1].setText(finalize);
			ep[2].setText(continues);
			ep[0].requestFocusInWindow();
			ep[0].repaint();
		}
		
		
		if(HF.getType()==HackerFile.FTP_SCRIPT){
			int index=0;
			if(tabbedPane.getTabCount()!=0)
				index=tabbedPane.getTabCount();
			String[] newTabs = {"Put","Get"};
			tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.FTP,HF.getName(),tabbedPane,false,this);
			tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
			tabs[index].setFileLocation(MyHacker.getCurrentFolder());
			tabbedPane.addTab(HF.getName(),tabs[index]);
			if(tabbedPane.getTabCount()!=1){
				index = tabbedPane.getTabCount();
				tabbedPane.setSelectedIndex(index-1);
			}
			if(index==0)
				index=1;
			ScriptEditorPane ep[] = tabs[index-1].getTabs();
			HashMap HM = HF.getContent();
			String put = openFunctionsDefined((String)HM.get("put"));
			String get = openFunctionsDefined((String)HM.get("get"));
			ep[0].setText(put);
			ep[1].setText(get);
			ep[0].requestFocusInWindow();
			ep[0].repaint();
		}
		if(HF.getType()==HackerFile.WATCH_SCRIPT){
			int index=0;
			if(tabbedPane.getTabCount()!=0)
				index=tabbedPane.getTabCount();
			String[] newTabs = {"Fire"};
			tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.WATCH,HF.getName(),tabbedPane,false,this);
			tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
			tabbedPane.addTab(HF.getName(),tabs[index]);
			tabs[index].setFileLocation(MyHacker.getCurrentFolder());
			if(tabbedPane.getTabCount()!=1){
				index = tabbedPane.getTabCount();
				tabbedPane.setSelectedIndex(index-1);
			}
			if(index==0)
				index=1;
			ScriptEditorPane ep[] = tabs[index-1].getTabs();
			HashMap HM = HF.getContent();
			String fire = openFunctionsDefined((String)HM.get("fire"));
			ep[0].setText(fire);
			ep[0].requestFocusInWindow();
			ep[0].repaint();
		}
		if(HF.getType()==HackerFile.TEXT){
			int index=0;
			if(tabbedPane.getTabCount()!=0)
				index=tabbedPane.getTabCount();
			String[] newTabs = {"Content"};
			tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.TEXT,HF.getName(),tabbedPane,false,this);
			tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
			tabs[index].setFileLocation(MyHacker.getCurrentFolder());
			tabbedPane.addTab(HF.getName(),tabs[index]);
			if(tabbedPane.getTabCount()!=1){
				index = tabbedPane.getTabCount();
				tabbedPane.setSelectedIndex(index-1);
			}
			if(index==0)
				index=1;
			ScriptEditorPane ep[] = tabs[index-1].getTabs();
			HashMap HM = HF.getContent();
			ep[0].setText((String)HM.get("data"));
			ep[0].requestFocusInWindow();
			ep[0].repaint();
		}
		if(HF.getType()==HackerFile.HTTP_SCRIPT){
			//System.out.println("Opening HTTP Script");
			int index=0;
			if(tabbedPane.getTabCount()!=0)
				index=tabbedPane.getTabCount();
			String[] newTabs = {"Enter","Exit","Submit"};
			tabs[index] = new ScriptInternalFunctionPane(newTabs,ScriptInternalFunctionPane.HTTP,HF.getName(),tabbedPane,false,this);
			tabs[index].setBounds(this.getInsets().left,this.getInsets().top,500,200);
			tabs[index].setFileLocation(MyHacker.getCurrentFolder());
			tabbedPane.addTab(HF.getName(),tabs[index]);
			if(tabbedPane.getTabCount()!=1){
				index = tabbedPane.getTabCount();
				tabbedPane.setSelectedIndex(index-1);
			}
			if(index==0)
				index=1;
			ScriptEditorPane ep[] = tabs[index-1].getTabs();
			HashMap HM = HF.getContent();
			String initialize = openFunctionsDefined((String)HM.get("enter"));
			String finalize = openFunctionsDefined((String)HM.get("exit"));
			String continues = openFunctionsDefined((String)HM.get("submit"));
			ep[0].setText(initialize);
			ep[1].setText(finalize);
			ep[2].setText(continues);
			ep[0].requestFocusInWindow();
			ep[0].repaint();
		}
		
	}
			
	
	public void saveFile(String answer,String folder){
		View MyView = MyHacker.getView();
		String ip=MyHacker.getEncryptedIP();
		String username=MyHacker.getUsername();
		HackerFile HF=new HackerFile(0);
		//System.out.println(tabbedPane.getSelectedIndex());
		ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
		if(SIFP==null){
			//System.out.println("NULL");
		}
		else{
			ScriptEditorPane eps[] = SIFP.getTabs();
			if(SIFP.getType()==ScriptInternalFunctionPane.BANKING){
				HF = new HackerFile(ScriptInternalFunctionPane.BANKING);
				HF.setName(answer);
				HF.setLocation(folder);
				HashMap HM = new HashMap();
				String deposit=eps[0].getText();
				if(deposit.length()>TEXT_LIMIT)
					deposit=deposit.substring(0,TEXT_LIMIT);
				String withdraw=eps[1].getText();
				if(withdraw.length()>TEXT_LIMIT)
					withdraw=withdraw.substring(0,TEXT_LIMIT);
				String transfer=eps[2].getText();
				if(transfer.length()>TEXT_LIMIT)
					transfer=transfer.substring(0,TEXT_LIMIT);
				eps[0].setLastSaved(eps[0].getText());
				eps[1].setLastSaved(eps[1].getText());
				eps[2].setLastSaved(eps[2].getText());
				eps[0].setChanged(false);
				eps[1].setChanged(false);
				eps[2].setChanged(false);
				HM.put("deposit",functionsDefined(deposit));
				HM.put("withdraw",functionsDefined(withdraw));
				HM.put("transfer",functionsDefined(transfer));
				HF.setContent(HM);
				HF.setMaker(username);
				
			}
			if(SIFP.getType()==ScriptInternalFunctionPane.ATTACK){
				HashMap HM = new HashMap();
				HF = new HackerFile(ScriptInternalFunctionPane.ATTACK);
				HF.setName(answer);
				HF.setType(HackerFile.ATTACKING_COMPILED);
				HF.setLocation(folder);
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
				HF.setContent(HM);
				HF.setMaker(username);
			}
			
			if(SIFP.getType()==ScriptInternalFunctionPane.SHIPPING){
				HashMap HM = new HashMap();
				HF = new HackerFile(ScriptInternalFunctionPane.SHIPPING);
				HF.setName(answer);
				HF.setType(HackerFile.SHIPPING_COMPILED);
				HF.setLocation(folder);
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
				HF.setContent(HM);
				HF.setMaker(username);
				
			}

			if(SIFP.getType()==ScriptInternalFunctionPane.FTP){
				HashMap HM = new HashMap();
				HF = new HackerFile(ScriptInternalFunctionPane.FTP);
				HF.setName(answer);
				HF.setType(HackerFile.FTP_COMPILED);
				HF.setLocation(folder);
				String put=eps[0].getText();
				if(put.length()>TEXT_LIMIT)
					put=put.substring(0,TEXT_LIMIT);
				String get=eps[1].getText();
				if(get.length()>TEXT_LIMIT)
					get=get.substring(0,TEXT_LIMIT);
				HM.put("get",functionsDefined(get));
				HM.put("put",functionsDefined(put));
				HF.setContent(HM);
				HF.setMaker(username);
				
			}
			if(SIFP.getType()==ScriptInternalFunctionPane.WATCH){
				HashMap HM = new HashMap();
				HF = new HackerFile(ScriptInternalFunctionPane.WATCH);
				HF.setName(answer);
				HF.setType(HackerFile.WATCH_COMPILED);
				HF.setLocation(folder);
				String fire=eps[0].getText();
				if(fire.length()>TEXT_LIMIT)
					fire=fire.substring(0,TEXT_LIMIT);
				HM.put("fire",functionsDefined(fire));
				HF.setContent(HM);
				HF.setMaker(username);
									
			}
			if(SIFP.getType()==ScriptInternalFunctionPane.HTTP){
				HashMap HM = new HashMap();
				//System.out.println("Compiling HTTP");
				HF = new HackerFile(ScriptInternalFunctionPane.HTTP);
				HF.setName(answer);
				HF.setType(HackerFile.HTTP);
				HF.setLocation(folder);
				String enter=eps[0].getText();
				if(enter.length()>TEXT_LIMIT)
					enter=enter.substring(0,TEXT_LIMIT);
				String exit=eps[1].getText();
				if(exit.length()>TEXT_LIMIT)
					exit=exit.substring(0,TEXT_LIMIT);
				String submit=eps[2].getText();
				if(submit.length()>TEXT_LIMIT)
					submit=submit.substring(0,TEXT_LIMIT);
				HM.put("enter",functionsDefined(enter));
				HM.put("exit",functionsDefined(exit));
				HM.put("submit",functionsDefined(submit));
				
				HF.setContent(HM);
				HF.setMaker(username);
									
			}
			if(SIFP.getType()==ScriptInternalFunctionPane.TEXT){
				HF = new HackerFile(ScriptInternalFunctionPane.TEXT);
				HF.setName(answer);
				HF.setLocation(folder);
				HashMap HM = new HashMap();
				String content=eps[0].getText();
				if(content.length()>TEXT_LIMIT)
					content=content.substring(0,TEXT_LIMIT);
				eps[0].setLastSaved(eps[0].getText());
				eps[0].setChanged(false);
				HM.put("data",content);
				HF.setContent(HM);
				HF.setMaker(username);
			}
			if(SIFP.getType()==ScriptInternalFunctionPane.CHALLENGE){
				HF = new HackerFile(ScriptInternalFunctionPane.TEXT);
				HF.setName(answer);
				HF.setLocation(folder);
				HashMap HM = new HashMap();
				String content=eps[0].getText();
				if(content.length()>TEXT_LIMIT)
					content=content.substring(0,TEXT_LIMIT);
				eps[0].setLastSaved(eps[0].getText());
				eps[0].setChanged(false);
				HM.put("data",content);
				HF.setContent(HM);
				HF.setMaker(username);
			}
			HF.setQuantity(1);
			HF.setType(SIFP.getType());
			if(SIFP.getType()==ScriptInternalFunctionPane.CHALLENGE)
				HF.setType(ScriptInternalFunctionPane.TEXT);
			Object objects[] = {ip,folder,HF};
			MyView.setFunction("savefile");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"savefile",objects));
			objects =new Object[]{ip,folder};
			MyView.addFunctionCall(new RemoteFunctionCall(0,"requestdirectory",objects));
			SIFP.setTitle(answer);
			tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(),answer);
		}
	}
	public void addFunctionCall(String function,String field1,String field2,int fields){
		String startText="",endText="";
		String insert = function+"(";
		if(fields>0)
			insert+=field1;
		if(fields>1)
			insert+=","+field2;
		insert+=")";
		ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
		ScriptEditorPane SEP = SIFP.getCurrentTab();
		int position = SEP.getCaretPosition();

		try{
			startText = SEP.getText(0,position);
		}catch(Exception ble){System.out.println("Bad Location");}
		try{
			endText = SEP.getText(position,SEP.getText().length()-startText.length());
		}catch(Exception ble){System.out.println("Bad Location");}
		SEP.setText(startText+insert+endText);
	}
	
	public void setFileChooserClosed(){
		fileChooserOpen=false;
	}
	
	public static String functionsDefined(String function){
		if(function.indexOf("#define functions")!=-1){
			function = function.replaceAll("#define","//#define");
			return function;
		}
		//function = "int main(){\n"+function+"\n}";
		return function;
	}
	
	public static String openFunctionsDefined(String function){
		if(function.indexOf("#define functions")!=-1){
			function = function.replaceAll("//#define","#define");
			return function;
		}
		//function = "int main(){\n"+function+"\n}";
		//function = function.substring(0,function.length()-2);
		//function = function.replaceAll("int main\\(\\)\\{\\\n","");
		return function;
	}
	
	public void actionPerformed(ActionEvent e){
		String ac = e.getActionCommand();
		if(ac==null){
			System.out.println(e.paramString());
			return;
		}
		if(ac.equals("Exit")){
			doDefaultCloseAction();
		}
		if (ac.equals("New")){
			String[] options = {"Banking","Attack","FTP","Watch","Text","Challenge","HTTP","Redirect"};
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
			if(answer.equals("Banking")){
				newBankScript();
			}
			if(answer.equals("Attack")){
				newAttackScript();
			}
			if(answer.equals("FTP")){
				newFTPScript();
			}
			if(answer.equals("Watch")){
				newWatchScript();
			}
			if(answer.equals("Text")){
				newText();
			}
			if(answer.equals("Challenge")){
				newChallenge();
			}
			if(answer.equals("HTTP")){
				newHTTPScript();
			}
			if(answer.equals("Redirect")){
				newShippingScript();
			}
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
			int index = tabbedPane.getSelectedIndex();
			if(tabbedPane.getTabCount()>0){
				if(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()).equals("Untitled *")){
					HackerFileChooser HFC = new HackerFileChooser(MyHacker,HackerFileChooser.SAVE,MyHacker.getCurrentFolder(),MyHacker.getCurrentDirectory(),this);
					mainPanel.add(HFC);
					HFC.moveToFront();
				}
				else{
					ScriptInternalFunctionPane SIFP = tabs[index];
					//SIFP.setChanged(false);
					ScriptEditorPane eps[] = SIFP.getTabs();
					for(int i=0;i<eps.length;i++){
						eps[i].setChanged(false);
						eps[i].setLastSaved(eps[i].getText());
					}
					tabbedPane.setTitleAt(index,SIFP.getTitle());
					saveFile(SIFP.getTitle(),SIFP.getFileLocation());
				}
			}
		}
		if(ac.equals("Save As")){
			if(tabbedPane.getTabCount()>0){	
				if(!fileChooserOpen){
					HackerFileChooser HFC = new HackerFileChooser(MyHacker,HackerFileChooser.SAVE,MyHacker.getCurrentFolder(),MyHacker.getCurrentDirectory(),this);
					mainPanel.add(HFC);
					HFC.moveToFront();
					fileChooserOpen=true;
				}
			}
		        
		}
		if(ac.equals("Open")){
			if(!fileChooserOpen){
				HackerFileChooser HFC = new HackerFileChooser(MyHacker,HackerFileChooser.OPEN,MyHacker.getCurrentFolder(),MyHacker.getCurrentDirectory(),this);
				mainPanel.add(HFC);
				HFC.moveToFront();
				fileChooserOpen=true;
			}
		}
		
		if(ac.equals("Bank Script")){
			newBankScript();
		}
		if(ac.equals("Attack Script")){
			newAttackScript();
		}
		if(ac.equals("FTP Script")){
			newFTPScript();
		}
		if(ac.equals("Watch Script")){
			newWatchScript();
		}
		if(ac.equals("Challenge Script")){
			newChallenge();
		}
		if(ac.equals("Text Document")){
			newText();
		}
		if(ac.equals("HTTP Script")){
			newHTTPScript();
		}
		if(ac.equals("Redirect Script")){
			newShippingScript();
		}
		
		if(ac.equals("FunctionCall")){
			if(tabbedPane.getTabCount()>0){
				String[] options=null;
				String[] firstLabels=null;
				String[] secondLabels=null;
				float[] cpuCosts=null;
				float[] prices=null;
				String[] bankingFirstLabels = {"IP: "};
				ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
				ScriptEditorPane SEP = SIFP.getCurrentTab();
				int type = SIFP.getType();
				
				if(type==ScriptInternalFunctionPane.BANKING){
					options=banking;
				}
				if(type==ScriptInternalFunctionPane.FTP){
					options=FTP;
				}
				if(type==ScriptInternalFunctionPane.ATTACK){
					options=attack;
				}
				if(type==ScriptInternalFunctionPane.WATCH){
					options=watch;
				}
				if(type!=ScriptInternalFunctionPane.TEXT){
					FunctionCallDialog fcd = new FunctionCallDialog(options,firstLabels,secondLabels,cpuCosts,prices,type,this);
				}
				
			}
		}

		if(ac.equals("CompileTest")){
			if(tabbedPane.getTabCount()>0){
				//check to see if it will compile.
				View MyView = MyHacker.getView();
				String ip=MyHacker.getIP();
				String username=MyHacker.getUsername();
				HackerFile HF=new HackerFile(0);
				ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
				String folder = SIFP.getFileLocation();
				if(SIFP.getType()!=ScriptInternalFunctionPane.CHALLENGE&&SIFP.getType()!=ScriptInternalFunctionPane.TEXT){
					ScriptEditorPane eps[] = SIFP.getTabs();
					HashMap HM = new HashMap();
					int type=0;
					if(SIFP.getType()==ScriptInternalFunctionPane.BANKING){
						HF = new HackerFile(ScriptInternalFunctionPane.BANKING);
						HF.setName(SIFP.getTitle()+".bin");
						HF.setType(HackerFile.BANKING_COMPILED);
						HF.setLocation(folder);
						String deposit=eps[0].getText();
						if(deposit.length()>TEXT_LIMIT)
							deposit=deposit.substring(0,TEXT_LIMIT);
						String withdraw=eps[1].getText();
						if(withdraw.length()>TEXT_LIMIT)
							withdraw=withdraw.substring(0,TEXT_LIMIT);
						String transfer=eps[2].getText();
						if(transfer.length()>TEXT_LIMIT)
							transfer=transfer.substring(0,TEXT_LIMIT);
						HM.put("deposit",functionsDefined(deposit));
						HM.put("withdraw",functionsDefined(withdraw));
						HM.put("transfer",functionsDefined(transfer));
						HF.setContent(HM);
						HF.setMaker(username);
						type=0;
						
						
					}
					if(SIFP.getType()==ScriptInternalFunctionPane.ATTACK){
						HF = new HackerFile(ScriptInternalFunctionPane.ATTACK);
						HF.setName(SIFP.getTitle()+".bin");
						HF.setType(HackerFile.ATTACKING_COMPILED);
						HF.setLocation(folder);
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
						HF.setContent(HM);
						HF.setMaker(username);
						type=2;
						
					}
					
					if(SIFP.getType()==ScriptInternalFunctionPane.SHIPPING){
						HF = new HackerFile(ScriptInternalFunctionPane.SHIPPING);
						HF.setName(SIFP.getTitle()+".bin");
						HF.setType(HackerFile.SHIPPING_COMPILED);
						HF.setLocation(folder);
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
						HF.setContent(HM);
						HF.setMaker(username);
						type=23;
						
					}
					
					
					if(SIFP.getType()==ScriptInternalFunctionPane.FTP){
						HF = new HackerFile(ScriptInternalFunctionPane.FTP);
						HF.setName(SIFP.getTitle()+".bin");
						HF.setType(HackerFile.FTP_COMPILED);
						HF.setLocation(folder);
						String put=eps[0].getText();
						if(put.length()>TEXT_LIMIT)
							put=put.substring(0,TEXT_LIMIT);
						String get=eps[1].getText();
						if(get.length()>TEXT_LIMIT)
							get=get.substring(0,TEXT_LIMIT);
						HM.put("get",functionsDefined(get));
						HM.put("put",functionsDefined(put));
						HF.setContent(HM);
						HF.setMaker(username);
						type=6;
						
					}
					if(SIFP.getType()==ScriptInternalFunctionPane.WATCH){
						HF = new HackerFile(ScriptInternalFunctionPane.WATCH);
						HF.setName(SIFP.getTitle()+".bin");
						HF.setType(HackerFile.WATCH_COMPILED);
						HF.setLocation(folder);
						String fire=eps[0].getText();
						if(fire.length()>TEXT_LIMIT)
							fire=fire.substring(0,TEXT_LIMIT);
						HM.put("fire",functionsDefined(fire));
						HF.setContent(HM);
						HF.setMaker(username);
						type=4;
											
					}
					if(SIFP.getType()==ScriptInternalFunctionPane.HTTP){
						//System.out.println("Compiling HTTP");
						HF = new HackerFile(ScriptInternalFunctionPane.HTTP);
						HF.setName(SIFP.getTitle()+".bin");
						HF.setType(HackerFile.HTTP);
						HF.setLocation(folder);
						String enter=eps[0].getText();
						if(enter.length()>TEXT_LIMIT)
							enter=enter.substring(0,TEXT_LIMIT);
						String exit=eps[1].getText();
						if(exit.length()>TEXT_LIMIT)
							exit=exit.substring(0,TEXT_LIMIT);
						String submit=eps[2].getText();
						if(submit.length()>TEXT_LIMIT)
							submit=submit.substring(0,TEXT_LIMIT);
						HM.put("enter",functionsDefined(enter));
						HM.put("exit",functionsDefined(exit));
						HM.put("submit",functionsDefined(submit));
						HF.setContent(HM);
						HF.setMaker(username);
						type=12;
						
											
					}
					HashMap levels = new HashMap();
					levels.put("Attack",new Integer(MyHacker.getStatsPanel().getAttackIcon().getLevel()));
					levels.put("Merchanting",new Integer(MyHacker.getStatsPanel().getMerchantingIcon().getLevel()));
					levels.put("Watch",new Integer(MyHacker.getStatsPanel().getWatchIcon().getLevel()));
					levels.put("HTTP",new Integer(MyHacker.getStatsPanel().getHTTPIcon().getLevel()));
					levels.put("Redirect",new Integer(MyHacker.getStatsPanel().getRedirectIcon().getLevel()));

					try{
						Object[] params = new Object[]{new Integer(type),HM,levels};
						HashMap result = (HashMap)XMLRPCCall.execute("http://"+MyView.getIP()+":8081/xmlrpc","hackerRPC.compileApplication", params);
						if(((String)(result.get("error"))).length()>0)
							//System.out.println(result.get("error"));
							JOptionPane.showMessageDialog(this,
								result.get("error"),
								"Compiling Error",
								JOptionPane.ERROR_MESSAGE);
						else{
							float price = (float)(double)(Double)result.get("price");
							NumberFormat nf = NumberFormat.getCurrencyInstance();
							JOptionPane.showMessageDialog(this,"Compiling Cost:  "+nf.format(price)+"\nCPU Cost:   "+result.get("cpucost"));
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(this,
								"Use Test Challenge Button For Challenges.",
								"Error",
								JOptionPane.ERROR_MESSAGE);
				}
				
			}

		}
		if(ac.equals("Compile")){
			if(tabbedPane.getTabCount()>0){
				//String folder=MyHacker.getCurrentFolder();
				//compile code and close tab.
				View MyView = MyHacker.getView();
				String ip=MyHacker.getEncryptedIP();
				String username=MyHacker.getUsername();
				HackerFile HF=new HackerFile(0);
				ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
				String folder = SIFP.getFileLocation();
				if(SIFP.getType()!=ScriptInternalFunctionPane.CHALLENGE&&SIFP.getType()!=ScriptInternalFunctionPane.TEXT){
					ScriptEditorPane eps[] = SIFP.getTabs();
					HashMap HM = new HashMap();
					int type=0;
					if(SIFP.getType()==ScriptInternalFunctionPane.BANKING){
						HF = new HackerFile(ScriptInternalFunctionPane.BANKING);
						HF.setName(SIFP.getTitle()+".bin");
						HF.setType(HackerFile.BANKING_COMPILED);
						HF.setLocation(folder);
						String deposit=eps[0].getText();
						if(deposit.length()>TEXT_LIMIT)
							deposit=deposit.substring(0,TEXT_LIMIT);
						String withdraw=eps[1].getText();
						if(withdraw.length()>TEXT_LIMIT)
							withdraw=withdraw.substring(0,TEXT_LIMIT);
						String transfer=eps[2].getText();
						if(transfer.length()>TEXT_LIMIT)
							transfer=transfer.substring(0,TEXT_LIMIT);
						
						HM.put("deposit",functionsDefined(deposit));
						HM.put("withdraw",functionsDefined(withdraw));
						HM.put("transfer",functionsDefined(transfer));
						HF.setContent(HM);
						HF.setMaker(username);
						type=0;
						
						
					}
					if(SIFP.getType()==ScriptInternalFunctionPane.ATTACK){
						HF = new HackerFile(ScriptInternalFunctionPane.ATTACK);
						HF.setName(SIFP.getTitle()+".bin");
						HF.setType(HackerFile.ATTACKING_COMPILED);
						HF.setLocation(folder);
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
						HF.setContent(HM);
						HF.setMaker(username);
						type=2;
						
					}
					
					if(SIFP.getType()==ScriptInternalFunctionPane.SHIPPING){
						HF = new HackerFile(ScriptInternalFunctionPane.SHIPPING);
						HF.setName(SIFP.getTitle()+".bin");
						HF.setType(HackerFile.SHIPPING_COMPILED);
						HF.setLocation(folder);
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
						HF.setContent(HM);
						HF.setMaker(username);
						type=23;
						
					}
					
					
					if(SIFP.getType()==ScriptInternalFunctionPane.FTP){
						HF = new HackerFile(ScriptInternalFunctionPane.FTP);
						HF.setName(SIFP.getTitle()+".bin");
						HF.setType(HackerFile.FTP_COMPILED);
						HF.setLocation(folder);
						String put=eps[0].getText();
						if(put.length()>TEXT_LIMIT)
							put=put.substring(0,TEXT_LIMIT);
						String get=eps[1].getText();
						if(get.length()>TEXT_LIMIT)
							get=get.substring(0,TEXT_LIMIT);
					
						HM.put("get",functionsDefined(get));
						HM.put("put",functionsDefined(put));
						HF.setContent(HM);
						HF.setMaker(username);
						type=6;
						
					}
					if(SIFP.getType()==ScriptInternalFunctionPane.WATCH){
						HF = new HackerFile(ScriptInternalFunctionPane.WATCH);
						HF.setName(SIFP.getTitle()+".bin");
						HF.setType(HackerFile.WATCH_COMPILED);
						HF.setLocation(folder);
						String fire=eps[0].getText();
						if(fire.length()>TEXT_LIMIT)
							fire=fire.substring(0,TEXT_LIMIT);
						HM.put("fire",functionsDefined(fire));
						HF.setContent(HM);
						HF.setMaker(username);
						type=4;
											
					}
					if(SIFP.getType()==ScriptInternalFunctionPane.HTTP){
						//System.out.println("Compiling HTTP");
						HF = new HackerFile(ScriptInternalFunctionPane.HTTP);
						HF.setName(SIFP.getTitle()+".bin");
						HF.setType(HackerFile.HTTP);
						HF.setLocation(folder);
						String enter=eps[0].getText();
						if(enter.length()>TEXT_LIMIT)
							enter=enter.substring(0,TEXT_LIMIT);
						String exit=eps[1].getText();
						if(exit.length()>TEXT_LIMIT)
							exit=exit.substring(0,TEXT_LIMIT);
						String submit=eps[2].getText();
						if(submit.length()>TEXT_LIMIT)
							submit=submit.substring(0,TEXT_LIMIT);
						HM.put("enter",functionsDefined(enter));
						HM.put("exit",functionsDefined(exit));
						HM.put("submit",functionsDefined(submit));
						HF.setContent(HM);
						HF.setMaker(username);
						type=12;
											
					}
					HF.setQuantity(1);
					HashMap levels = new HashMap();
					levels.put("Attack",new Integer(MyHacker.getStatsPanel().getAttackIcon().getLevel()));
					levels.put("Merchanting",new Integer(MyHacker.getStatsPanel().getMerchantingIcon().getLevel()));
					levels.put("Watch",new Integer(MyHacker.getStatsPanel().getWatchIcon().getLevel()));
					levels.put("HTTP",new Integer(MyHacker.getStatsPanel().getHTTPIcon().getLevel()));
					levels.put("Redirect",new Integer(MyHacker.getStatsPanel().getRedirectIcon().getLevel()));
					try{
						Object[] params = new Object[]{new Integer(type),HM,levels};
						HashMap result = (HashMap)XMLRPCCall.execute("http://"+MyView.getIP()+":8081/xmlrpc","hackerRPC.compileApplication", params);
						if(((String)(result.get("error"))).length()>0)
							JOptionPane.showMessageDialog(this,
								result.get("error"),
								"Compiling Error",
								JOptionPane.ERROR_MESSAGE);
						else{
							//System.out.println(result.get("cpucost"));
							//System.out.println(result.get("price"));
							Object[] options = {"Yes","No"};
							float price = (float)(double)(Double)result.get("price");
							NumberFormat nf = NumberFormat.getCurrencyInstance();
							if(price>MyHacker.getPettyCash()){
								JOptionPane.showMessageDialog(this,
									"Compiling Cost:  "+nf.format(price)+"\nPetty Cash:   "+nf.format(MyHacker.getPettyCash())+"\nCPU Cost:   "+result.get("cpucost")+"\nYou don't have enough money in your petty cash to compile this script.",
								"Not Enough Money",
								JOptionPane.ERROR_MESSAGE);
							}
							else{
								int n = JOptionPane.showOptionDialog(this,"Compiling Cost:  "+nf.format(price)+"\nPetty Cash:   "+nf.format(MyHacker.getPettyCash())+"\nCPU Cost:   "+result.get("cpucost")+"\nAre You Sure you want to compile this?",
								    "Compile?",
								    JOptionPane.YES_NO_CANCEL_OPTION,
								    JOptionPane.QUESTION_MESSAGE,
								    null,
								    options,
								    options[1]);
								if(n==0){
									HF.setCPUCost((float)(double)(Double)(result.get("cpucost")));
									Object objects[] = {ip,folder,HF,price};
									MyView.setFunction("compilefile");
									MyView.addFunctionCall(new RemoteFunctionCall(0,"compilefile",objects));
								}
							}
						}
						
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(this,
								"Use Test Challenge Button For Challenges.",
								"Error",
								JOptionPane.ERROR_MESSAGE);
				}
				
			}
			
		}
		
		if(ac.equals("TestChallenge")){
			if(tabbedPane.getTabCount()>0){
				//check to see if it will compile.
				View MyView = MyHacker.getView();
				String ip=MyHacker.getIP();
				String username=MyHacker.getUsername();
				HackerFile HF=new HackerFile(0);
				ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
				String folder = SIFP.getFileLocation();
				ScriptEditorPane ep = SIFP.getCurrentTab();
				String function=ep.getText();
				if(function.length()>TEXT_LIMIT)
					function=function.substring(0,TEXT_LIMIT);
				function = "int Main(){\n"+function+"\n}";
				String s = (String)JOptionPane.showInputDialog(
				    this,
				    "Challenge ID:",
				    "Run Challenge",
				    JOptionPane.PLAIN_MESSAGE,
				    null,
				    null,
				    null);
				Object[] objects = new Object[]{MyHacker.getEncryptedIP(),function,s};
				MyView.setFunction("dochallenge");
				MyView.addFunctionCall(new RemoteFunctionCall(0,"dochallenge",objects));
			}

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
				SyntaxDocument document = (SyntaxDocument)SEP.getDocument();
				document.doUndo();
				
			}
		}
		if(ac.equals("Redo")){
			if(tabbedPane.getTabCount()>0){
				ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
				ScriptEditorPane SEP = SIFP.getCurrentTab();
				SyntaxDocument document = (SyntaxDocument)SEP.getDocument();
				document.doRedo();
			}
		}
		if(ac.equals("RunBash")){
			try{
				BashLinker BL = new BashLinker(MyHacker);
				ScriptInternalFunctionPane SIFP = tabs[tabbedPane.getSelectedIndex()];
				String folder = SIFP.getFileLocation();
				ScriptEditorPane ep = SIFP.getCurrentTab();
				String function=ep.getText();
				RunFactory.runAllCode(function,BL);
			}catch(Exception ex){
				//ex.printStackTrace();
				//System.out.println("Error Found: "+ex.getMessage());
				MyHacker.showMessage(ex.getMessage());
				
			}
		}

	}

}

