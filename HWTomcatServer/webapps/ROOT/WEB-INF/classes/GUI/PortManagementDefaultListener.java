package GUI;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;

public class PortManagementDefaultListener implements ActionListener{

	private int port;
	private Hacker MyHacker;
	private PortManagement MyPortManagement;
	
	public PortManagementDefaultListener(int port,Hacker MyHacker,PortManagement MyPortManagement){
		this.MyHacker=MyHacker;
		this.port=port;
		this.MyPortManagement=MyPortManagement;
	}
	
	public void actionPerformed(ActionEvent e){
		View MyView = MyHacker.getView();
		//System.out.println("Default Clicked");
		String type = MyPortManagement.getType(port).getText();
		int typeSend=-1;
		if(type.equals("<html><u>Bank</u></html>"))
			typeSend=PacketPort.BANKING;
		if(type.equals("<html><u>Attack</u></html>"))
			typeSend=PacketPort.ATTACK;
		if(type.equals("<html><u>FTP</u></html>")){
			//System.out.println("FTP");
			typeSend=PacketPort.FTP;
		}
		if(type.equals("<html><u>HTTP</u></html>"))
			typeSend=PacketPort.HTTP;
		if(type.equals("<html><u>Redirect</u></html>"))
			typeSend=PacketPort.SHIPPING;
		if(typeSend!=-1){
			//System.out.println("Default Changed");
			Object objects[] = {MyHacker.getEncryptedIP(),new Integer(port),new Integer(typeSend)};
			MyView.setFunction("setdefaultport");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"setdefaultport",objects));
		}
		else{
			//System.out.println("Changing for no good reason");
		}
	}
}
		
