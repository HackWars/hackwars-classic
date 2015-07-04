package GUI;


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

public class StoreActionListener implements ActionListener{
	
	private Hacker MyHacker;
	private String name,ip;
	private JSpinner spinner;
	private WebBrowser MyWebBrowser;
	
	public StoreActionListener(Hacker MyHacker,String name,JSpinner spinner,String ip,WebBrowser MyWebBrowser){
		this.MyHacker=MyHacker;
		this.MyWebBrowser=MyWebBrowser;
		this.name=name;
		this.spinner=spinner;
		this.ip=ip;
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Buy")){
			int quantity = (int)((Integer)spinner.getValue());
			//System.out.println("Buy "+quantity+" copies of "+name);
			View MyView = MyHacker.getView();
			Object[] params = new Object[]{ip};
			String result = (String)XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/domain.php","domainLookup", params);
			Object objects[] = {result,MyHacker.getEncryptedIP(),name,quantity};
			MyView.setFunction("requestpurchase");
			MyView.addFunctionCall(new RemoteFunctionCall(Hacker.BROWSER,"requestpurchase",objects));
			MyHacker.setRequestedDirectory(Hacker.BROWSER);
			//MyWebBrowser.removeProducts();
		}
	}
}
		
