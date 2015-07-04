package GUI;
/**
ScriptInternalFunctionPane.java

internal frame inside a new script that has tabs for all functions.

*/
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;


public class ScriptInternalFunctionPane extends JTabbedPane{
	public static final int BANKING=1;
	public static final int ATTACK=3;
	public static final int SHIPPING=24;
	public static final int FTP=7;
	public static final int WATCH=5;
	public static final int TEXT=9;
	public static final int CHALLENGE=10;
	public static final int HTTP=15;
	public static final int MAP=100;
	public static final int SPRITE=101;
	//public static final int CLUE=16;
	private String title;
	private ScriptEditorPane editorPanes[];
	private int type;
	private JTabbedPane parentTab;
	private String location="";
	private ScriptEditor MyScriptEditor;
	public ScriptInternalFunctionPane(String[] tabs,int type,String title,JTabbedPane parentTab,boolean changed,ScriptEditor MyScriptEditor){
		this.type=type;
		this.title=title;
		this.parentTab=parentTab;
		this.MyScriptEditor=MyScriptEditor;
		JScrollPane sp;
		editorPanes = new ScriptEditorPane[tabs.length];
		//JEditorPane ep;
		//TokenMarker tokenMarker= null;
		String t="text/xml";
		if(type==CHALLENGE)
			t="text/challenge";
		else if(type==BANKING)
			t="text/banking";
		else if(type==ATTACK)
			t="text/attack";
		else if(type==FTP)
			t="text/ftp";
		else if(type==WATCH)
			t="text/watch";
		else if(type==HTTP)
			t="text/http";
		else if(type==SHIPPING)
			t="text/redirect";
		else if(type==MAP)
			t="text/hacktendo";
		else if(type==SPRITE)
			t="text/hacktendo";
		for(int i=0;i<tabs.length;i++){
			editorPanes[i] = new ScriptEditorPane(this,changed,t,MyScriptEditor);
			sp=new JScrollPane(editorPanes[i]);
			sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			addTab(tabs[i],sp);
			//DefaultSyntaxKit.initKit();
			editorPanes[i].setContentType(t);
		}
		//editorPanes[0].grabFocus();
		
	}
	
	public void setFileLocation(String location){
		this.location=location;
	}
	
	public String getFileLocation(){
		return(location);
	}
	
	public ScriptEditorPane[] getTabs(){
		return(editorPanes);
	}
	
	public ScriptEditorPane getCurrentTab(){
		return(editorPanes[getSelectedIndex()]);
	}
	
	public int getType(){
		return(type);
	}
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public String getTitle(){
		return(title);
	}
	
	public void setChanged(){
		parentTab.setTitleAt(parentTab.getSelectedIndex(),title+" *");
	}
	
	public ScriptEditor getScriptEditor(){
		return MyScriptEditor;
	}

}

