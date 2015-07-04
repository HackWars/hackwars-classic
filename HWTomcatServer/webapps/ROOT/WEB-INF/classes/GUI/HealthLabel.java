package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import Assignments.*;
import java.util.*;
import View.*;
import java.text.*;

public class HealthLabel extends JLabel implements MouseListener{
	private Hacker hacker;
	private ToolTip toolTip;
	private int port;
	private Component parent;
	private float health = 100.0f;
	public HealthLabel(String name,Hacker hacker,int port,Component parent){
		
		this.parent = parent;
		this.hacker=hacker;
		this.port = port;
		addMouseListener(this);
		//setActionCommand(name);
		//setIcon(ImageLoader.getImageIcon("images/off32.png"));
		setText("<html><u>"+name+"</u></html>");
		String tooltiptext = "";
		toolTip = new ToolTip(tooltiptext,hacker);
		//hacker.getPanel().setComponentZOrder(toolTip,0);
	}
	
	public void setHealth(float health){
		this.health = health;
		float discount = hacker.getHealDiscount();
		float cost = (100.0f-health)*2.0f;
		float save = 1.0f-discount;
		float newCost = cost*discount;
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		NumberFormat df = new DecimalFormat("0.00");
		String text = "<html>Cost:           "+nf.format(cost)+"<br>Discount:     "+df.format(save*100)+"%<br>Total:           "+nf.format(newCost)+"</html>";
		setText("<html><u>"+df.format(health)+"</u></html>");
		toolTip.setText(text);
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
	
	public void mouseEntered(MouseEvent e){
        if (!getText().equals("")) {
            toolTip.show(e.getX()+getLocationOnScreen().x-hacker.getPanel().getLocationOnScreen().x,e.getY()+getLocationOnScreen().y-hacker.getPanel().getLocationOnScreen().y);
        }
	}

	public void mouseExited(MouseEvent e){
		toolTip.setVisible(false);
	}

	public void mousePressed(MouseEvent e){

	}

	public void mouseReleased(MouseEvent e){

	}

	public void mouseClicked(MouseEvent e){
System.out.println("MOuse clicked");
		float discount = hacker.getHealDiscount();
		float cost = (100.0f-health)*2.0f;
		float save = 1.0f-discount;
		float newCost = cost*discount;
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		NumberFormat df = new DecimalFormat("0.00");
		//System.out.println("It would cost you "+nf.format(cost)+" to heal this port");
		if (hacker.getPreference(OptionPanel.HEALING_CONFIRM_KEY).equals("true")) {
			Object[] options = {"Yes","Cancel"};
			String message = "Are You Sure?\n\nCost:           "+nf.format(cost)+"\nDiscount:     "+df.format(save*100)+"%\nTotal:           "+nf.format(newCost);
			Object[] retValue = ConfirmationPanel.showYesNoDialog(parent, "Heal?", message, "Always heal, don't ask for confirmation");
			if((int)(Integer)retValue[0] == OptionDialog.OPTION_YES){
				heal();
			}
			// if they checked the checkbox, always heal
			if ((boolean)(Boolean)retValue[1] == true) {
				hacker.setPreference(OptionPanel.HEALING_CONFIRM_KEY, "false");
			}
		} else {
			heal();
		}
	}
	
	public String toString(){
		return getText();
	}
	
	public void heal(){
System.out.println("Healing port "+port);		
		View MyView = hacker.getView();
        Object objects[] = {hacker.getEncryptedIP(),port};
        MyView.setFunction("healport");
        MyView.addFunctionCall(new RemoteFunctionCall(0,"healport",objects));
	}
}

