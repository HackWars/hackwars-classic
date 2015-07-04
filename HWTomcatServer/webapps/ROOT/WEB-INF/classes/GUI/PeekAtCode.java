package GUI;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.util.*;

public class PeekAtCode extends JInternalFrame{

	private Hacker MyHacker;
	private int type,port;
	private String ip;
	private JTabbedPane tabbedPane;
	
	public PeekAtCode(Hacker MyHacker,int type,String ip,int port){
		this.MyHacker=MyHacker;
		this.type=type;
		this.ip=ip;
		this.port=port;
		//System.out.println("Displaying Code");
		setTitle("Peek At Code");
		setBounds(100,100,700,450);
		setLayout(null);
		setClosable(true);
		setIconifiable(true);
		setResizable(false);
		setVisible(true); 
		MyHacker.setPeekAtCode(this);
		tabbedPane = new JTabbedPane();
		add(tabbedPane);
		tabbedPane.setBounds(getInsets().left+2,getInsets().top+2,525,400);
	}
	
	public void setFile(HashMap HM){
		if(type==ShowChoices.BANK){
			JEditorPane editorPane = new JEditorPane();
			editorPane.setEditable(false);
			String stuff=(String)HM.get("deposit");
			stuff = ScriptEditor.openFunctionsDefined(stuff);
			editorPane.setText(stuff);
			JScrollPane sp = new JScrollPane(editorPane);
			tabbedPane.add("Deposit",sp);
			editorPane = new JEditorPane();
			editorPane.setEditable(false);
			stuff=(String)HM.get("withdraw");
			//System.out.println("Length: "+stuff.length());
			stuff = ScriptEditor.openFunctionsDefined(stuff);
			editorPane.setText(stuff);
			sp = new JScrollPane(editorPane);
			tabbedPane.add("Withdraw",sp);
			editorPane = new JEditorPane();
			editorPane.setEditable(false);
			stuff=(String)HM.get("transfer");
			stuff = ScriptEditor.openFunctionsDefined(stuff);
			editorPane.setText(stuff);
			sp = new JScrollPane(editorPane);
			tabbedPane.add("Transfer",sp);
		}
		else if(type==ShowChoices.ATTACK){
			JEditorPane editorPane = new JEditorPane();
			editorPane.setEditable(false);
			String stuff=(String)HM.get("initialize");
			stuff = ScriptEditor.openFunctionsDefined(stuff);
			editorPane.setText(stuff);
			JScrollPane sp = new JScrollPane(editorPane);
			tabbedPane.add("Initialize",sp);
			editorPane = new JEditorPane();
			editorPane.setEditable(false);
			stuff=(String)HM.get("finalize");
			stuff = ScriptEditor.openFunctionsDefined(stuff);
			editorPane.setText(stuff);
			sp = new JScrollPane(editorPane);
			tabbedPane.add("Finalize",sp);
			editorPane = new JEditorPane();
			editorPane.setEditable(false);
			stuff=(String)HM.get("continue");
			stuff = ScriptEditor.openFunctionsDefined(stuff);
			editorPane.setText(stuff);
			sp = new JScrollPane(editorPane);
			tabbedPane.add("Continue",sp);
		}
		else if(type==ShowChoices.FTP){
			JEditorPane editorPane = new JEditorPane();
			editorPane.setEditable(false);
			String stuff=(String)HM.get("put");
			stuff = ScriptEditor.openFunctionsDefined(stuff);
			editorPane.setText(stuff);
			JScrollPane sp = new JScrollPane(editorPane);
			tabbedPane.add("Put",sp);
			editorPane = new JEditorPane();
			editorPane.setEditable(false);
			stuff=(String)HM.get("get");
			stuff = ScriptEditor.openFunctionsDefined(stuff);
			editorPane.setText(stuff);
			sp = new JScrollPane(editorPane);
			tabbedPane.add("Get",sp);
		}
		else if(type==ShowChoices.HTTP){
			JEditorPane editorPane = new JEditorPane();
			editorPane.setEditable(false);
			String stuff=(String)HM.get("enter");
			stuff = ScriptEditor.openFunctionsDefined(stuff);
			editorPane.setText(stuff);
			JScrollPane sp = new JScrollPane(editorPane);
			tabbedPane.add("Enter",sp);
			editorPane = new JEditorPane();
			editorPane.setEditable(false);
			stuff=(String)HM.get("exit");
			stuff = ScriptEditor.openFunctionsDefined(stuff);
			editorPane.setText(stuff);
			sp = new JScrollPane(editorPane);
			tabbedPane.add("Exit",sp);
			editorPane = new JEditorPane();
			editorPane.setEditable(false);
			stuff=(String)HM.get("submit");
			stuff = ScriptEditor.openFunctionsDefined(stuff);
			editorPane.setText(stuff);
			sp = new JScrollPane(editorPane);
			tabbedPane.add("Submit",sp);
		}
		else if(type==ShowChoices.SHIPPING){
			JEditorPane editorPane = new JEditorPane();
			editorPane.setEditable(false);
			String stuff=(String)HM.get("initialize");
			stuff = ScriptEditor.openFunctionsDefined(stuff);
			editorPane.setText(stuff);
			JScrollPane sp = new JScrollPane(editorPane);
			tabbedPane.add("Initialize",sp);
			editorPane = new JEditorPane();
			editorPane.setEditable(false);
			stuff=(String)HM.get("finalize");
			stuff = ScriptEditor.openFunctionsDefined(stuff);
			editorPane.setText(stuff);
			sp = new JScrollPane(editorPane);
			tabbedPane.add("Finalize",sp);
			editorPane = new JEditorPane();
			editorPane.setEditable(false);
			stuff=(String)HM.get("continue");
			stuff = ScriptEditor.openFunctionsDefined(stuff);
			editorPane.setText(stuff);
			sp = new JScrollPane(editorPane);
			tabbedPane.add("Continue",sp);
		}
		else if(type==ShowChoices.LOGS){
			setTitle("Peek at Logs");
			JEditorPane editorPane = new JEditorPane();
			editorPane.setEditable(false);
			String stuff=(String)HM.get("logs");
			editorPane.setText(stuff);
			JScrollPane sp = new JScrollPane(editorPane);
			tabbedPane.add("Logs",sp);
		}
	}
	
}
