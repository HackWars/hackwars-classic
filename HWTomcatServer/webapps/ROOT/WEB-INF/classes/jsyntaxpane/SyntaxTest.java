package jsyntaxpane;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import jsyntaxpane.syntaxkits.*;
public class SyntaxTest{

	public SyntaxTest(){
		
		JFrame frame = new JFrame();
		frame.setSize(300,300);
		
		JEditorPane editor = new JEditorPane();
		DefaultSyntaxKit.initKit();
		editor.setContentType("text/banking");
		
		frame.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.weightx=1.0;
		c.weighty=1.0;
		c.fill=GridBagConstraints.BOTH;
		
		frame.add(editor,c);
		
		frame.pack();
		frame.setVisible(true);
	
	
	}
	
	public static void main(String args[]){
		
		SyntaxTest s = new SyntaxTest();
	}



}
