package GUI;
/**

ScriptEditorPane.java
Editing Section inside the script editor.

*/
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import jsyntaxpane.*;
import jsyntaxpane.syntaxkits.*;

public class ScriptEditorPane extends JEditorPane implements CaretListener,UndoableEditListener{
	private ScriptInternalFunctionPane parentTab;
	private boolean changed=false;
	private String lastSaved;
	private UndoManager undoManager = null;
	private ScriptEditor Parent=null;
	private String type;
	private boolean started=false;
	public ScriptEditorPane(ScriptInternalFunctionPane parentTab,boolean changed,String type,ScriptEditor Parent){
		this.Parent=Parent;
		this.parentTab=parentTab;
		this.changed=changed;
		this.type=type;
		//addCaretListener(this);
		//setFont(new Font("monospaced",Font.PLAIN,10));
		DefaultSyntaxKit.initKit();
		
		/*if(tokenMarker!=null)
			setTokenMarker(tokenMarker);
		setTabs();
		getInputHandler().addKeyBinding("C+S",parentTab.getScriptEditor());
		getInputHandler().addKeyBinding("C+Z",parentTab.getScriptEditor());
		getInputHandler().addKeyBinding("C+Y",parentTab.getScriptEditor());*/
	}
	
	public void setText(String text){
		super.setText(text);
		if(!started){
			undoManager = new UndoManager();
			getDocument().addUndoableEditListener(this);
			started = true;
		}
		//setCaretPosition(0);
	}

	
	public void setTabs()
	{
		FontMetrics fm = getFontMetrics( getFont() );
		int charWidth = fm.charWidth( 'w' );
		int tabWidth = charWidth * 5;
 
		TabStop[] tabs = new TabStop[19];
 
		for (int j = 0; j < tabs.length; j++)
		{
			int tab = j + 1;
			tabs[j] = new TabStop( tab * tabWidth );
		}
 
		TabSet tabSet = new TabSet(tabs);
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setTabSet(attributes, tabSet);
		int length = getDocument().getLength();
		//getStyledDocument().setParagraphAttributes(0, length, attributes, false);
	}
	
	public void setChanged(boolean changed){
		this.changed=changed;
	}

	public boolean getChanged(){
		return(changed);
	}

	public void setLastSaved(String lastSaved){
		this.lastSaved=lastSaved;
	}
    
	public UndoManager getUndoManager(){
		return(undoManager);
	}
    
	public void caretUpdate(CaretEvent e){
		
		if(!changed){			
			String s = getText();
			if(!s.equals(lastSaved)){
				changed=true;
				parentTab.setChanged();
			}
		}
		
		//Update the current line position.
        /*String content = this.getText();
        
		String splitContent[]=content.split("\n",-1);
		int i;
		int prev=0;
		int current=0;
		int dot=e.getDot();
		for(i=0;i<splitContent.length;i++){
            current+=splitContent[i].length()+1;
			if(current>dot)
				break;
			prev=current;
		}
		int line=i+1;
		int character=(dot-prev)+1;
		Parent.setCursorPosition(line,character);*/
		if(getDocument() instanceof SyntaxDocument){
			SyntaxDocument sDoc = (SyntaxDocument) getDocument();
			Token t = sDoc.getTokenAt(e.getDot());
			if (t != null) {
				try {
					String tData = sDoc.getText(t.start, Math.min(t.length, 40));
					if (t.length > 40) {
						tData += "...";
					}
					Parent.setCursorPosition(t.toString() + ": " + tData);
				} catch (BadLocationException ex) {
					// should not happen.. and if it does, just ignore it
					System.err.println(ex);
					ex.printStackTrace();
				}
			}
		}
	}
	public void undoableEditHappened(UndoableEditEvent e) {
		if(!changed&&e.getEdit().isSignificant()){			
			String s = getText();
			if(!s.equals(lastSaved)){
				changed=true;
				parentTab.setChanged();
			}
		}
	}

}

