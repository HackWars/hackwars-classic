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

public class FireWallLabel extends JLabel implements MouseListener{
	public static final int QUEST=0;
	public static final int SHIPPING=1;
	public static final int REGULAR=2;
	public static final int STORE=3;
	private String name,ip,commodity="",title="";
	private int type=0;
	private Hacker hacker;
	private ToolTip toolTip;
	private HashMap content;
	public FireWallLabel(String name,Hacker hacker){
		
		
		this.name=name;
		this.hacker=hacker;
		addMouseListener(this);
		//setActionCommand(name);
		//setIcon(ImageLoader.getImageIcon("images/off32.png"));
		setText(name);
		String tooltiptext = "";
		toolTip = new ToolTip(tooltiptext,hacker);
		//hacker.getPanel().setComponentZOrder(toolTip,0);
	}
	
	public void setContent(HashMap content){
		this.content = content;
		String text = "<html>";
		text +="<h4>"+content.get("name")+"</h4>";
		DecimalFormat format = new DecimalFormat("#.##");
		float abs = 0.0f;
		float percent = 0.0f;
		
		abs = Float.parseFloat(""+content.get("bank_damage_modifier"));
		percent = abs*100;
		text +="<b>Bank Damage Allowed: </b>";
		text+=format.format(percent)+"%<br>";
		
		abs = Float.parseFloat(""+content.get("attack_damage_modifier"));
		percent = abs*100;
		text +="<b>Attack Damage Allowed: </b>";
		text+=format.format(percent)+"%<br>";
		
		abs = Float.parseFloat(""+content.get("ftp_damage_modifier"));
		percent = abs*100;
		text +="<b>FTP Damage Allowed: </b>";
		text+=format.format(percent)+"%<br>";
		
		abs = Float.parseFloat(""+content.get("redirect_damage_modifier"));
		percent = abs*100;
		text +="<b>Redirect Damage Allowed: </b>";
		text+=format.format(percent)+"%<br>";
	
		abs = Float.parseFloat(""+content.get("http_damage_modifier"));
		percent = abs*100;
		text +="<b>HTTP Damage Allowed: </b>";
		text+=format.format(percent)+"%<br>";
		
		text+="<br><b>Attack Damage: </b>"+content.get("attack_damage");
		
		//special attributes
		
		HashMap specials1 = (HashMap)content.get("specialAttribute1");
		
		String sa1 = (String)specials1.get("short_desc");
		String v1 = (String)specials1.get("value");
		
		if(!sa1.equals("")){
			
			float value = Float.parseFloat(v1);
			percent = (value)*100;
			text+="<br> "+percent+sa1;
		}
		
		
		HashMap specials2 = (HashMap)content.get("specialAttribute2");
		
		String sa2 = (String)specials2.get("short_desc");
		String v2 = (String)specials2.get("value");
		
		if(!sa2.equals("")){
			float value = Float.parseFloat(v2);
			percent = (value)*100;
			text+="<br>"+percent+sa2;
		}
		
		text+="<br></html>";
		//System.out.println(text);
		toolTip.setText(text);
	}
	
	public void setType(int type){
		this.type = type;
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
	
	}
	
	public String toString(){
		return getText();
	}
}

