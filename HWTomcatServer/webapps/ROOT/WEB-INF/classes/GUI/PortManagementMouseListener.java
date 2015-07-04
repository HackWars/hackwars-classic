package GUI;
/**
PortManagementMouseListener.java
this is the mouse listener for the port management window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.text.*;

public class PortManagementMouseListener implements MouseListener,ActionListener{
	//data
	public final static int NAME=0;
	public final static int HEALTH=1;
	public final static int FIREWALL=2;
	private PortManagement MyPortManagement;
	private Hacker MyHacker;
	private int port;
	private JDesktopPane mainPanel;
	private int field;

	public PortManagementMouseListener(PortManagement MyPortManagement,int port,Hacker MyHacker,JDesktopPane mainPanel,int field){
		this.MyPortManagement=MyPortManagement;
		this.MyHacker=MyHacker;
		this.port=port;
		this.mainPanel=mainPanel;
		this.field=field;
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
		//if(e.isPopupTrigger()){
			JPopupMenu menu = new JPopupMenu();
			JMenuItem menuItem;
			Point p=null;
			if(field==NAME){
				JLabel type=MyPortManagement.getType(port);
				p= type.getLocation();
				if(type.getText().equals("<html><u>Click Here To Install Script</u></html>")){
					menuItem = new JMenuItem("Install Application");
					menu.add(menuItem);
					menuItem.addActionListener(this);
				}
				else{
					menuItem = new JMenuItem("Replace Application");
					menu.add(menuItem);
					menuItem.addActionListener(this);
					
					menuItem = new JMenuItem("Uninstall Port");
					menu.add(menuItem);
					menuItem.addActionListener(this);
					if(MyPortManagement.getOn(port)){
						if(type.getText().equals("<html><u>Attack</u></html>")){
							menu.addSeparator();
							menuItem = new JMenuItem("Open Attack Window");
							menu.add(menuItem);
							menuItem.addActionListener(this);
						}
						else if(type.getText().equals("<html><u>Bank</u></html>")){
							menu.addSeparator();
							menuItem = new JMenuItem("Deposit using this port");
							menuItem.setActionCommand("Deposit");
							menu.add(menuItem);
							menuItem.addActionListener(this);
							menuItem = new JMenuItem("Withdraw using this port");
							menuItem.setActionCommand("Withdraw");
							menu.add(menuItem);
							menuItem.addActionListener(this);
							menuItem = new JMenuItem("Transfer using this port");
							menuItem.setActionCommand("Transfer");
							menu.add(menuItem);
							menuItem.addActionListener(this);
						}
						else if(type.getText().equals("<html><u>Redirect</u></html>")){
							menu.addSeparator();
							menuItem = new JMenuItem("Open Redirect Window");
							menu.add(menuItem);
							menuItem.addActionListener(this);
						}
					}
				}
			}
			else if(field==HEALTH){
				float h = MyPortManagement.getHealthValue(port);
				float discount = MyHacker.getHealDiscount();
				float cost = (100.0f-h)*2.0f;
				float save = 1.0f-discount;
				float newCost = cost*discount;
				NumberFormat nf = NumberFormat.getCurrencyInstance();
				NumberFormat df = new DecimalFormat("0.00");
				//System.out.println("It would cost you "+nf.format(cost)+" to heal this port");
                if (MyHacker.getPreference(OptionPanel.HEALING_CONFIRM_KEY).equals("true")) {
                    Object[] options = {"Yes","Cancel"};
                    String message = "Are You Sure?\n\nCost:           "+nf.format(cost)+"\nDiscount:     "+df.format(save*100)+"%\nTotal:           "+nf.format(newCost);
                    Object[] retValue = ConfirmationPanel.showYesNoDialog(MyPortManagement, "Heal?", message, "Always heal, don't ask for confirmation");
                    if((int)(Integer)retValue[0] == OptionDialog.OPTION_YES){
                        heal();
                    }
                    // if they checked the checkbox, always heal
                    if ((boolean)(Boolean)retValue[1] == true) {
                        MyHacker.setPreference(OptionPanel.HEALING_CONFIRM_KEY, "false");
                    }
                } else {
                    heal();
                }
			}
			else if(field==FIREWALL){
				JLabel firewall=MyPortManagement.getFireWall(port);
				p=firewall.getLocation();
				if(!firewall.getText().equals("") && !firewall.getText().equals("<html><u>" + Constants.firewalls[0] +  "</u></html>")){
					menuItem = new JMenuItem("Replace FireWall");
					menu.add(menuItem);
					menuItem.addActionListener(this);
					menuItem = new JMenuItem("Uninstall FireWall");
					menu.add(menuItem);
					menuItem.addActionListener(this);
				} else if (firewall.getText().equals("<html><u>" + Constants.firewalls[0] +  "</u></html>")) {
                    menuItem = new JMenuItem("Install Firewall");
                    menu.add(menuItem);
                    menuItem.setActionCommand("Replace FireWall");
                    menuItem.addActionListener(this);
                } else {
					menuItem = new JMenuItem("Install An Application First");
					menu.add(menuItem);
				}
			}
			if(field!=HEALTH)
				menu.show(MyPortManagement.getPanel(port),e.getX()+p.x,e.getY()+p.y);
				
	}
    
    private void heal() {
        View MyView = MyHacker.getView();
        Object objects[] = {MyHacker.getEncryptedIP(),port};
        MyView.setFunction("healport");
        MyView.addFunctionCall(new RemoteFunctionCall(0,"healport",objects));
    }
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Open Attack Window")){
			Hacker MyHacker=MyPortManagement.getHacker();
			MyHacker.showAttack(port);
		}
		if(e.getActionCommand().equals("Open Redirect Window")){
			Hacker MyHacker=MyPortManagement.getHacker();
			MyHacker.showRedirect(port);
		}
		if(e.getActionCommand().equals("Deposit")){
			Hacker MyHacker = MyPortManagement.getHacker();
			MyHacker.startDeposit(port);
		}
		if(e.getActionCommand().equals("Withdraw")){
			Hacker MyHacker = MyPortManagement.getHacker();
			MyHacker.startWithdraw(port);
		}
		if(e.getActionCommand().equals("Transfer")){
			Hacker MyHacker = MyPortManagement.getHacker();
			MyHacker.startTransfer(port);
		}
		if(e.getActionCommand().equals("Install Application")){
			MyPortManagement.setFunction(PortManagement.INSTALL);
			InstallFileChooser IFC = new InstallFileChooser(MyHacker,MyHacker.getCurrentFolder(),MyHacker.getCurrentDirectory(),MyPortManagement,port);
			mainPanel.add(IFC);
			IFC.moveToFront();
			//System.out.println("Install");
		}
		if(e.getActionCommand().equals("Replace Application")){
			MyPortManagement.setFunction(PortManagement.REPLACE);
			InstallFileChooser IFC = new InstallFileChooser(MyHacker,MyHacker.getCurrentFolder(),MyHacker.getCurrentDirectory(),MyPortManagement,port);
			mainPanel.add(IFC);
			IFC.moveToFront();
		}
		if(e.getActionCommand().equals("Uninstall Port")){
			Object[] options = {"Yes","Cancel"};
			int n = JOptionPane.showOptionDialog(MyPortManagement,
			    "Are You Sure?",
			    "Uninstall Port "+port,
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    options,
			    options[1]);
			if(n==0){
				View MyView = MyHacker.getView();
				Object objects[] = {MyHacker.getEncryptedIP(),port};
				MyView.setFunction("uninstallport");
				MyView.addFunctionCall(new RemoteFunctionCall(0,"uninstallport",objects));
                MyPortManagement.uninstallPort(port);
			}
		}
		if(e.getActionCommand().equals("Heal Port")){
			JLabel health=MyPortManagement.getHealth(port);
			float h = MyPortManagement.getHealthValue(port);
			float cost = (100.0f-h)*2.0f;
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			//System.out.println("It would cost you "+nf.format(cost)+" to heal this port");
			Object[] options = {"Yes","Cancel"};
			int n = JOptionPane.showOptionDialog(MyPortManagement,
			    "Are You Sure?\nIt would cost you "+nf.format(cost)+" to heal this port",
			    "Heal Port "+port,
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    options,
			    options[1]);
			if(n==0){
				View MyView = MyHacker.getView();
				Object objects[] = {MyHacker.getEncryptedIP(),port};
				MyView.setFunction("healport");
				MyView.addFunctionCall(new RemoteFunctionCall(0,"healport",objects));
			}
		}
		if(e.getActionCommand().equals("Replace FireWall")){
			FireWallFileChooser FFC = new FireWallFileChooser(MyHacker,MyHacker.getCurrentFolder(),MyHacker.getCurrentDirectory(),MyPortManagement,port);
			mainPanel.add(FFC);
			FFC.moveToFront();
		}
		if(e.getActionCommand().equals("Uninstall FireWall")){
			View MyView = MyHacker.getView();
			Object objects[] = {MyHacker.getEncryptedIP(),port};
			MyView.setFunction("deletefirewall");
			MyView.addFunctionCall(new RemoteFunctionCall(0,"deletefirewall",objects));
		}
		
	}
}

