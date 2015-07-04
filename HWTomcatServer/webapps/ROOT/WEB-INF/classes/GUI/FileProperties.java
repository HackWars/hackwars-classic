package GUI;
/**

FileProperties.java
this is the message window.
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import Assignments.*;
import View.*;
import java.text.*;
import java.math.*;
import Browser.*;
import Game.*;
import net.miginfocom.swing.*;
import java.util.*;

public class FileProperties extends Application{
	private Hacker hacker;
	private String fileName,folder;
	private HackerFile file;
	private JLabel name=new JLabel("-"),type=new JLabel("-"),maker=new JLabel("-"),price=new JLabel("-"),cpu=new JLabel("-"),quantity=new JLabel("-");
	private JTextArea description=new JTextArea("-");
	private JPanel panel = new JPanel(),otherPanel = new JPanel();
	private JTabbedPane tabbedPane = new JTabbedPane();
	
	public FileProperties(Hacker hacker,String fileName,String folder){
		this.hacker=hacker;
		this.fileName = fileName;
		this.folder = folder;
		this.setTitle("File Properties -- "+fileName);
		this.setResizable(true);
		this.setMaximizable(false);
		this.setClosable(true);
		this.setIconifiable(true);
		this.addInternalFrameListener(this);
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		setBounds(50,50,400,325);
		hacker.setRequestedFile(Hacker.FILE_PROPERTIES);
		hacker.setFileProperties(this);
		View view = hacker.getView();
		String ip=hacker.getEncryptedIP();
		Object objects[] = {ip,folder,fileName};
		view.setFunction("requestfile");
		view.addFunctionCall(new RemoteFunctionCall(0,"requestfile",objects));
		setLayout(new MigLayout("fill"));
		panel.setLayout(new MigLayout("fill,wrap 2,align leading"));
		otherPanel.setLayout(new MigLayout("fill,wrap 2,align leading"));
		add(tabbedPane,"grow");
		populate();
		hacker.getPanel().add(this);
		setVisible(true);
		//System.out.println(MyHacker.getPanel().getComponentZOrder(this));
	}

	public void populate(){
		JLabel label = new JLabel("Loading...");
		panel.add(label);
		tabbedPane.addTab("General",panel);
	}
	
	public void receivedFile(HackerFile file){
		this.file = file;
		panel.removeAll();
		otherPanel.removeAll();
		Font f = new Font("Dialog",Font.PLAIN,12);
		JLabel nameLabel = new JLabel("Name: ");
		panel.add(nameLabel);
		panel.add(name);
		
		JLabel typeLabel = new JLabel("Type: ");
		panel.add(typeLabel);
		panel.add(type);
		
		JLabel makerLabel = new JLabel("Maker: ");
		panel.add(makerLabel);
		panel.add(maker);
		
		JLabel priceLabel = new JLabel("Price: ");
		panel.add(priceLabel);
		panel.add(price);
		
		JLabel cpuLabel = new JLabel("CPU Cost: ");
		panel.add(cpuLabel);
		panel.add(cpu);
		
		JLabel quantityLabel = new JLabel("Quantity: ");
		panel.add(quantityLabel);
		panel.add(quantity);
		
		JLabel descriptionLabel = new JLabel("Description: ");
		panel.add(descriptionLabel,"align leading");
		JScrollPane sp = new JScrollPane(description);
		panel.add(sp,"grow");
		
		name.setFont(f);
		type.setFont(f);
		maker.setFont(f);
		price.setFont(f);
		cpu.setFont(f);
		quantity.setFont(f);
		description.setFont(f);
		description.setColumns(14);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setEditable(false);
		name.setText(file.getName());
		type.setText(Home.TYPES[file.getType()]);
		maker.setText(file.getMaker());
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		price.setText(nf.format(file.getPrice()));
		description.setText(file.getDescription());
		cpu.setText(""+file.getCPUCost());
		quantity.setText(""+file.getQuantity());
		if(file.getType()==HackerFile.AGP||file.getType()==HackerFile.PCI){
			tabbedPane.addTab("Advanced",getCardPanel(file));
		}
		else if(file.getType()==HackerFile.NEW_FIREWALL){
			tabbedPane.addTab("Advanced",getFirewallPanel(file));
		}
	}
	
	public JPanel getCardPanel(HackerFile file){
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(new MigLayout("wrap 2,align leading"));
		HashMap content = file.getContent();
		float durability=50.0f;
		float max=50.0f; 
		try{//In case it's hardware that hasn't had a durability set for some reason, e.g., old hardware.
			durability = Float.valueOf((String)content.get("currentquality"));
			max = Float.valueOf((String)content.get("maxquality"));
		}catch(Exception e){}
		returnPanel.add(new JLabel("Durability:"));
		returnPanel.add(new JLabel(durability+"/"+max));
		int a1 = Integer.valueOf((String)content.get("attribute0"));
		int a2 = Integer.valueOf((String)content.get("attribute1"));
		int quality1 = Integer.valueOf((String)content.get("quality0"));
		int quality2 = Integer.valueOf((String)content.get("quality1"));
		String values = (String)content.get("bonusdata");
		String[] v = values.split("\\|");
		String value1 = v[0];
		String value2 = v[1];
		returnPanel.add(new JLabel("Attribute 1: "));
		returnPanel.add(new JLabel(value1));
		returnPanel.add(new JLabel("Attribute 2: "));
		returnPanel.add(new JLabel(value2));
		
		int dT = Equipment.DUCT_TAPE_COST[quality1]+Equipment.DUCT_TAPE_COST[quality2];
		int ge = Equipment.GERMANIUM_COST[quality1]+Equipment.GERMANIUM_COST[quality2];
		int si = Equipment.SILICON_COST[quality1]+Equipment.SILICON_COST[quality2];
		int ybco = Equipment.YBCO_COST[quality1]+Equipment.YBCO_COST[quality2];
		int pu = Equipment.PLUTONIUM_COST[quality1]+Equipment.PLUTONIUM_COST[quality2];
		returnPanel.add(new JLabel("Repair Cost:"));
		JPanel repairPanel = new JPanel();
		repairPanel.setLayout(new MigLayout());
		JLabel label = new JLabel(dT+"x",new ImageIcon("images/ducttape.png"),SwingConstants.TRAILING);
		label.setHorizontalTextPosition(SwingConstants.LEADING);
		repairPanel.add(label);
		if(ge>0){
			label = new JLabel(ge+"x",new ImageIcon("images/germanium.png"),SwingConstants.TRAILING);
			label.setHorizontalTextPosition(SwingConstants.LEADING);
			repairPanel.add(label);
		}
		if(si>0){
			label = new JLabel(si+"x",new ImageIcon("images/silicon.png"),SwingConstants.TRAILING);
			label.setHorizontalTextPosition(SwingConstants.LEADING);
			repairPanel.add(label);
		}
		if(ybco>0){
			label = new JLabel(ybco+"x",new ImageIcon("images/YBCO.png"),SwingConstants.TRAILING);
			label.setHorizontalTextPosition(SwingConstants.LEADING);
			repairPanel.add(label);
		}
		if(pu>0){
			label = new JLabel(pu+"x",new ImageIcon("images/plutonium.png"),SwingConstants.TRAILING);
			label.setHorizontalTextPosition(SwingConstants.LEADING);
			repairPanel.add(label);
		}
		returnPanel.add(repairPanel);
		
		//cardInfo.setText(d+"\n"+value1+"\n"+value2);
		//returnPanel.add(cardInfo);
		
		
		return returnPanel;
	
	}
	
	private JPanel getFirewallPanel(HackerFile file){
		HashMap content = file.getContent();
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(new MigLayout("wrap 2,align leading"));
		
		String equipLevel = ""+content.get("equip_level");
		returnPanel.add(new JLabel("Firewall Level Required for Use: "));
		returnPanel.add(new JLabel(equipLevel));
		
		DecimalFormat format = new DecimalFormat("#.##");
		float abs = 0.0f;
		float percent = 0.0f;
		//if(type == PacketPort.BANKING){
		returnPanel.add(new JLabel("Bank Damage Allowed: "));	
		abs = Float.parseFloat(""+content.get("bank_damage_modifier"));
		percent = abs*100;
		returnPanel.add(new JLabel(format.format(percent)+"%"));
		
		abs = Float.parseFloat(""+content.get("attack_damage_modifier"));
		percent = abs*100;
		returnPanel.add(new JLabel("Attack Damage Allowed: "));	
		returnPanel.add(new JLabel(format.format(percent)+"%"));
		
		abs = Float.parseFloat(""+content.get("ftp_damage_modifier"));
		percent = abs*100;
		returnPanel.add(new JLabel("FTP Damage Allowed: "));	
		returnPanel.add(new JLabel(format.format(percent)+"%"));
		
		abs = Float.parseFloat(""+content.get("redirect_damage_modifier"));
		percent = abs*100;
		returnPanel.add(new JLabel("Redirect Damage Allowed: "));	
		returnPanel.add(new JLabel(format.format(percent)+"%"));
		
		abs = Float.parseFloat(""+content.get("http_damage_modifier"));
		percent = abs*100;
		returnPanel.add(new JLabel("HTTP Damage Allowed: "));	
		returnPanel.add(new JLabel(format.format(percent)+"%"));
		
		returnPanel.add(new JLabel("Attack Damage: "));	
		returnPanel.add(new JLabel(""+content.get("attack_damage")));
		
		//special attributes
		
		HashMap specials1 = (HashMap)content.get("specialAttribute1");
		
		String sa1 = (String)specials1.get("short_desc");
		String v1 = (String)specials1.get("value");
		returnPanel.add(new JLabel("Special Attributes: "),"span,wrap");
		if(!sa1.equals("")){
			
			float value = Float.parseFloat(v1);
			percent = (value)*100;
			
			returnPanel.add(new JLabel(percent+sa1),"span,wrap");
		}
		
		
		HashMap specials2 = (HashMap)content.get("specialAttribute2");
		
		String sa2 = (String)specials2.get("short_desc");
		String v2 = (String)specials2.get("value");
		
		if(!sa2.equals("")){
			float value = Float.parseFloat(v2);
			percent = (value)*100;
			returnPanel.add(new JLabel(percent+sa2),"span,wrap");
		}
		
		
		return returnPanel;
		
	}
	

	public void internalFrameClosed(InternalFrameEvent e) {
		//MyHacker.setMessageWindowOpen(false);
	}
	
	public void actionPerformed(ActionEvent e){
	}

}
