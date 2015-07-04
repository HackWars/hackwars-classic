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
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.StyledDocument;
import javax.swing.text.Style;
import javax.swing.text.SimpleAttributeSet;
import Browser.*;
import jsyntaxpane.*;
import jsyntaxpane.syntaxkits.*;

public class WebsiteEditor extends Application implements UndoableEditListener,ComponentListener,ChangeListener{
	private JDesktopPane mainPanel=null;
	private Hacker MyHacker=null;
	private JEditorPane editorPane;//,previewPane;
	private HtmlHandler previewPane;
	private ScriptInternalFunctionPane tabs[] = new ScriptInternalFunctionPane[10];
	private UndoManager undoManager = new UndoManager();
	private View MyView;
	private String title;
	private JToolBar toolBar;
	private JTabbedPane tabbedPane;
	
	public WebsiteEditor(String name,boolean resize,boolean max,boolean close,boolean iconify,JDesktopPane mainPanel,Hacker MyHacker){
		setTitle(name);
		setResizable(true);
		setMaximizable(true);
		setClosable(close);
		setIconifiable(iconify);
		addInternalFrameListener(this);
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		addComponentListener(this);
		this.mainPanel=mainPanel;
		this.MyHacker=MyHacker;
		MyView=MyHacker.getView();

		this.setFrameIcon(ImageLoader.getImageIcon("images/edit.png"));
	}

	public void receivedPage(String title,String body){
		//System.out.println("Setting stuff");
		this.title=title;
		setTitle("Website Editor - "+title);
		editorPane.setText(body);
		previewPane.parseDocument(body,this);
	}
	public void populate(){
		//Menu
		JMenuBar menuBar= new JMenuBar();
		JMenu menu,subMenu;
		JMenuItem menuItem;
		JButton button;

		menu=new JMenu("File");
		
		menuItem=new JMenuItem("Save",ImageLoader.getImageIcon("images/save.png"));
		menuItem.setMnemonic(KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_S, ActionEvent.CTRL_MASK));
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

		menuItem=new JMenuItem("Set Title");
		menu.add(menuItem);
		menuItem.addActionListener(this);

		menuItem=new JMenuItem("Undo",ImageLoader.getImageIcon("images/undo.png"));
		menuItem.setMnemonic(KeyEvent.VK_Z);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);

		menuItem=new JMenuItem("Redo",ImageLoader.getImageIcon("images/redo.png"));
		menuItem.setMnemonic(KeyEvent.VK_Z);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_Z, 3));
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
		
		menu.addSeparator();
		
		menuItem=new JMenuItem("Bold",ImageLoader.getImageIcon("images/bold.png"));
		menuItem.setMnemonic(KeyEvent.VK_B);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_B, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);
		
		menuItem=new JMenuItem("Italics",ImageLoader.getImageIcon("images/italics.png"));
		menuItem.setMnemonic(KeyEvent.VK_I);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);
		
		menuItem=new JMenuItem("Underline",ImageLoader.getImageIcon("images/underline.png"));
		menuItem.setMnemonic(KeyEvent.VK_U);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);
		
		menuItem=new JMenuItem("Insert Link",ImageLoader.getImageIcon("images/link.png"));
		menuItem.setActionCommand("Link");
		menuItem.setMnemonic(KeyEvent.VK_L);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);
		
		menuItem=new JMenuItem("New Line",ImageLoader.getImageIcon("images/linebreak.png"));
		menuItem.setActionCommand("Linebreak");
		menuItem.setMnemonic(KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);
		
		menuItem=new JMenuItem("Page Header",ImageLoader.getImageIcon("images/header.png"));
		menuItem.setActionCommand("header");
		menuItem.setMnemonic(KeyEvent.VK_H);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(this);
		
		menu.addSeparator();
		
		menuItem=new JMenuItem("Centre Text",ImageLoader.getImageIcon("images/centre.png"));
		menuItem.setActionCommand("Centre");
		menu.add(menuItem);
		menuItem.addActionListener(this);
		
		menuItem=new JMenuItem("Left Justify Text",ImageLoader.getImageIcon("images/textleft.png"));
		menuItem.setActionCommand("Left");
		menu.add(menuItem);
		menuItem.addActionListener(this);
		
		menuItem=new JMenuItem("Right Justify Text",ImageLoader.getImageIcon("images/textright.png"));
		menuItem.setActionCommand("Right");
		menu.add(menuItem);
		menuItem.addActionListener(this);

		menuBar.add(menu);

		this.setJMenuBar(menuBar);

		//toolbar
		toolBar = new JToolBar("Tools");

		Insets insets = this.getInsets();
		Rectangle size = this.getBounds();

		button = new JButton(ImageLoader.getImageIcon("images/save.png"));
		button.setActionCommand("Save");
		button.setToolTipText("Save current script.");
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
		
		button = new JButton(ImageLoader.getImageIcon("images/bold.png"));
		button.setActionCommand("Bold");
		button.setToolTipText("Make text bold.");
		button.addActionListener(this);
		toolBar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/italics.png"));
		button.setActionCommand("Italics");
		button.setToolTipText("Make text italics.");
		button.addActionListener(this);
		toolBar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/underline.png"));
		button.setActionCommand("Underline");
		button.setToolTipText("Make text underlined.");
		button.addActionListener(this);
		toolBar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/link.png"));
		button.setActionCommand("Link");
		button.setToolTipText("insert link.");
		button.addActionListener(this);
		toolBar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/linebreak.png"));
		button.setActionCommand("Linebreak");
		button.setToolTipText("new line.");
		button.addActionListener(this);
		toolBar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/header.png"));
		button.setActionCommand("Header");
		button.setToolTipText("Page Header.");
		button.addActionListener(this);
		toolBar.add(button);
		
		toolBar.addSeparator();
		
		button = new JButton(ImageLoader.getImageIcon("images/centre.png"));
		button.setActionCommand("Centre");
		button.setToolTipText("Centre text.");
		button.addActionListener(this);
		toolBar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/textleft.png"));
		button.setActionCommand("Left");
		button.setToolTipText("Left Justify Text.");
		button.addActionListener(this);
		toolBar.add(button);
		
		button = new JButton(ImageLoader.getImageIcon("images/textright.png"));
		button.setActionCommand("Right");
		button.setToolTipText("Right Justify.");
		button.addActionListener(this);
		toolBar.add(button);


		Dimension dim = toolBar.getPreferredSize();
		toolBar.setBounds(insets.left,insets.top,size.width,dim.height);
		this.add(toolBar);

		//Editor
		tabbedPane = new JTabbedPane();
		editorPane = new JEditorPane();
		JScrollPane sp = new JScrollPane(editorPane);
		DefaultSyntaxKit.initKit();
		editorPane.setContentType("text/xml");
		Object objects[] = {MyHacker.getEncryptedIP()};
		if(MyView!=null){
			MyHacker.setSiteRequest(Hacker.WEBSITE_EDITOR);
			MyView.setFunction("requestpage");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"requestpage",objects));
		}
		//editorPane.setText("<b>Test</b>");
		//editorPane.addCaretListener(this);
		editorPane.getDocument().addUndoableEditListener(this);
		/*previewPane = new JTextPane();
		previewPane.setEditorKit(new HTMLEditorKit());
		previewPane.setText(editorPane.getText());
		previewPane.setEditable(false);*/
		previewPane = new HtmlHandler();
		//previewPane.createView();
		previewPane.getView().setBounds(insets.left,insets.top+dim.height,size.width-20,size.height-insets.top-dim.height-70);
		
		
		JScrollPane scrollPane = new JScrollPane(sp);
		JScrollPane scrollPane2 = new JScrollPane(previewPane.getView());
		this.add(tabbedPane);
		tabbedPane.addTab("Site",scrollPane);
		tabbedPane.addTab("Preview",scrollPane2);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tabbedPane.setBounds(insets.left,insets.top+dim.height,size.width-20,size.height-insets.top-dim.height-70);
		tabbedPane.addChangeListener(this);
	}
	
	public void insertLink(String link,String name){
		int startPosition = editorPane.getCaretPosition();
		SyntaxDocument s = (SyntaxDocument)editorPane.getDocument();
		try{
			s.insertString(startPosition,"<a href=\""+link+"\">"+name+"</a>",new SimpleAttributeSet());
		}catch(Exception ex){}
		editorPane.grabFocus();
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		MyHacker.setScriptEditorOpen(false);

	}
	
	
	public void actionPerformed(ActionEvent e){
		String ac = e.getActionCommand();
	
		if(ac.equals("Copy")){
			editorPane.copy();
		}
		if(ac.equals("Cut")){
			editorPane.cut();
		}
		if(ac.equals("Paste")){
			editorPane.paste();
		}
		if(ac.equals("Undo")){
			SyntaxDocument document = (SyntaxDocument)editorPane.getDocument();
			document.doUndo();
		}
		if(ac.equals("Redo")){
			SyntaxDocument document = (SyntaxDocument)editorPane.getDocument();
			document.doRedo();
		}
		if(ac.equals("Bold")){
			
			int startPosition = editorPane.getSelectionStart();
			int endPosition = editorPane.getSelectionEnd();
			SyntaxDocument s = (SyntaxDocument)editorPane.getDocument();
			//Style st = s.addStyle("plain",new SimpleAttributeSet());
			try{
				s.insertString(startPosition,"<b>",new SimpleAttributeSet());
				s.insertString(endPosition+3,"</b>",new SimpleAttributeSet());
			}catch(Exception ex){}
			editorPane.grabFocus();
		}
		if(ac.equals("Italics")){
			
			int startPosition = editorPane.getSelectionStart();
			int endPosition = editorPane.getSelectionEnd();
			SyntaxDocument s = (SyntaxDocument)editorPane.getDocument();
			//Style st = s.addStyle("plain",new SimpleAttributeSet());
			try{
				s.insertString(startPosition,"<i>",new SimpleAttributeSet());
				s.insertString(endPosition+3,"</i>",new SimpleAttributeSet());
			}catch(Exception ex){}
			editorPane.grabFocus();
		}
		if(ac.equals("Underline")){
			
			int startPosition = editorPane.getSelectionStart();
			int endPosition = editorPane.getSelectionEnd();
			SyntaxDocument s = (SyntaxDocument)editorPane.getDocument();
			try{
				s.insertString(startPosition,"<u>",new SimpleAttributeSet());
				s.insertString(endPosition+3,"</u>",new SimpleAttributeSet());
			}catch(Exception ex){}
			editorPane.grabFocus();
		}
		if(ac.equals("Header")){
			
			int startPosition = editorPane.getSelectionStart();
			int endPosition = editorPane.getSelectionEnd();
			SyntaxDocument s = (SyntaxDocument)editorPane.getDocument();
			try{
				s.insertString(startPosition,"<h1>",new SimpleAttributeSet());
				s.insertString(endPosition+4,"</h1>",new SimpleAttributeSet());
			}catch(Exception ex){}
			editorPane.grabFocus();
		}
		if(ac.equals("Centre")){
			
			int startPosition = editorPane.getSelectionStart();
			SyntaxDocument s = (SyntaxDocument)editorPane.getDocument();
			try{
				s.insertString(startPosition,"<center>",new SimpleAttributeSet());
				int endPosition = editorPane.getSelectionEnd();
				s.insertString(endPosition,"</center>",new SimpleAttributeSet());
			}catch(Exception ex){}
			editorPane.grabFocus();
		}
		if(ac.equals("Left")){
			
			int startPosition = editorPane.getSelectionStart();
			SyntaxDocument s = (SyntaxDocument)editorPane.getDocument();
			try{
				s.insertString(startPosition,"<div align=\"left\">",new SimpleAttributeSet());
				int endPosition = editorPane.getSelectionEnd();
				s.insertString(endPosition,"</div>",new SimpleAttributeSet());
			}catch(Exception ex){}
			editorPane.grabFocus();
		}
		if(ac.equals("Right")){
			
			int startPosition = editorPane.getSelectionStart();
			SyntaxDocument s = (SyntaxDocument)editorPane.getDocument();
			try{
				s.insertString(startPosition,"<div align=\"right\">",new SimpleAttributeSet());
				int endPosition = editorPane.getSelectionEnd();
				s.insertString(endPosition,"</div>",new SimpleAttributeSet());
			}catch(Exception ex){}
			editorPane.grabFocus();
		}
		if(ac.equals("Linebreak")){
			
			int startPosition = editorPane.getSelectionStart();
			SyntaxDocument s = (SyntaxDocument)editorPane.getDocument();
			try{
				s.insertString(startPosition,"<br />\n",new SimpleAttributeSet());
			}catch(Exception ex){}
			editorPane.grabFocus();
		}
		if(ac.equals("Link")){
			LinkDialog ld = new LinkDialog(this);
		}
		
		if(ac.equals("Exit")){
			doDefaultCloseAction();
		}
		if(ac.equals("Save")){
			String ip=MyHacker.getEncryptedIP();
			Object objects[] = {ip,title,editorPane.getText()};
			MyView.setFunction("savepage");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"savepage",objects));	
			JOptionPane.showMessageDialog(this, "Your page has been saved.");
		}
		if(ac.equals("Set Title")){
			String answer = (String)JOptionPane.showInputDialog(this,
						"Title:",
						"Set Title",
						JOptionPane.PLAIN_MESSAGE,
						ImageLoader.getImageIcon("images/info.png"),
						null,
						null);
			if(answer!=null){
				title=answer;
				setTitle("Website Editor - "+title);
			}
		}

	}
	/*public void caretUpdate(CaretEvent e){
		try{
			if(editorPane.getText().length()>0)
				previewPane.parseDocument(editorPane.getText());
		}catch(Exception ex){}
	}*/
	public void undoableEditHappened(UndoableEditEvent e) {
		if(e.getEdit().isSignificant())
			undoManager.addEdit(e.getEdit());
	}
	
	
	public void componentHidden(ComponentEvent e) {
	 }

	 public void componentMoved(ComponentEvent e) {
        
	 }

	 public void componentResized(ComponentEvent e) {
		 Insets insets = getInsets();
		Rectangle size = getBounds();
		 Dimension dim = toolBar.getPreferredSize();
		toolBar.setBounds(insets.left,insets.top,size.width,dim.height);
		 tabbedPane.setBounds(insets.left,insets.top+dim.height,size.width-20,size.height-insets.top-dim.height-70);
	 }

	 public void componentShown(ComponentEvent e) {

	 }
	 public void stateChanged(ChangeEvent e){
		 try{
			if(editorPane.getText().length()>0)
				previewPane.parseDocument(editorPane.getText(),this);
		}catch(Exception ex){}
	 }
}

