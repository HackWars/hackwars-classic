package DBAdmin.Quest;
/*
HackWars (2008)

Description: A slapped together tool for editing drop tables in HackWars.

*/

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.imageio.*;
import java.util.*;
import javax.swing.text.*;
import javax.swing.event.*;

public class DialogWidget extends JInternalFrame implements MouseListener{
	//The graphical list part of this widget.
	
	private int xStart=0,yStart=0,width,height,x,y;
	private Dialog dialog;
	private String name;
	private JLabel nameLabel,addUserLabel,addFunctionLabel,addConditionLabel;
	private JTextArea npcText=new JTextArea();
	private JPanel optionPanel = new JPanel(),functionPanel = new JPanel(),conditionPanel = new JPanel();
	private ArrayList userOptions = new ArrayList(),functions = new ArrayList(),entryConditions = new ArrayList();
	private boolean clone = false;
	private int index = -1;
	public DialogWidget(Dialog dialog,int x,int y,int width,int height,String name){
		this.dialog=dialog;
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.name=name;
		setTitle(name);
		setResizable(true);
		setIconifiable(false);
		setLayout(new GridBagLayout());
		//setBackground(new Color(0,0,255));
		populate();
		this.setBounds(x,y,width,height);
		setVisible(true);
	}
	
	public DialogWidget(Dialog dialog,String name,int index){
		clone=true;
		this.dialog=dialog;
		this.name=name;
		this.index=index;
		setTitle(name);
		setResizable(false);
		setIconifiable(false);
		setVisible(true);
		nameLabel = new JLabel(name);
		nameLabel.setFont(new Font("dialog",Font.PLAIN,20));
		add(nameLabel);
		addMouseListener(this);
	
	}
	
	public void populate(){
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		nameLabel = new JLabel(name);
		nameLabel.setFont(new Font("dialog",Font.PLAIN,20));
		nameLabel.addMouseListener(this);
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(nameLabel,c);
		
		conditionPanel.setLayout(new GridBagLayout());
		c.gridy=0;
		c.fill=GridBagConstraints.NONE;
		c.weighty=0.0;
		c.weightx=0.0;
		
		addConditionLabel = new JLabel("Add Entry Condition...");
		addConditionLabel.addMouseListener(this);
		addConditionLabel.setForeground(Color.blue);
		conditionPanel.add(addConditionLabel,c);
		c.weighty=1.0;
		c.weightx=1.0;
		c.fill=GridBagConstraints.BOTH;
		conditionPanel.add(new JLabel(""),c);
		JScrollPane conditionScroll = new JScrollPane(conditionPanel);
		
		c.gridy=1;
		c.weighty = 0.0;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		add(conditionScroll,c);
		
		npcText.setText("NPC Dialog");
		JScrollPane npcScroll = new JScrollPane(npcText);
		c.gridy=2;
		c.weighty=0.4;
		c.fill=GridBagConstraints.BOTH;
		add(npcScroll,c);
		
		
		optionPanel.setLayout(new GridBagLayout());
		c.gridy=0;
		c.fill=GridBagConstraints.NONE;
		c.weighty=0.0;
		c.weightx=0.0;
		
		addUserLabel = new JLabel("Add User Option...");
		addUserLabel.addMouseListener(this);
		addUserLabel.setForeground(Color.blue);
		optionPanel.add(addUserLabel,c);
		c.weighty=1.0;
		c.weightx=1.0;
		c.fill=GridBagConstraints.BOTH;
		optionPanel.add(new JLabel(""),c);
		JScrollPane sp = new JScrollPane(optionPanel);
		//sp.setMinimumSize(new Dimension(100,100));
		
		c.gridy=3;
		c.weighty = 0.4;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		add(sp,c);
		
		functionPanel.setLayout(new GridBagLayout());
		c.gridy=0;
		c.fill=GridBagConstraints.NONE;
		c.weighty=0.0;
		c.weightx=0.0;
		
		addFunctionLabel = new JLabel("Add Function...");
		addFunctionLabel.setForeground(Color.blue);
		addFunctionLabel.addMouseListener(this);
		functionPanel.add(addFunctionLabel,c);
		c.weighty=1.0;
		c.weightx=1.0;
		c.fill=GridBagConstraints.BOTH;
		functionPanel.add(new JLabel(""),c);
		
		JScrollPane functionScroll = new JScrollPane(functionPanel);
		c.gridy=4;
		c.weighty = 0.2;
		add(functionScroll,c);
		
		
	}
	
	public void makeOptionsPanel(){
		optionPanel.removeAll();
		GridBagConstraints c = new GridBagConstraints();
		Object[] options = userOptions.toArray();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weighty=0.0;
		c.weightx=0.0;
		for(int i=0;i<options.length;i++){
			c.gridy=i;
			optionPanel.add((UserOption)options[i],c);
		}
		c.gridy++;
		optionPanel.add(addUserLabel,c);
		c.weighty=1.0;
		c.weightx=1.0;
		c.gridy++;
		c.fill=GridBagConstraints.BOTH;
		optionPanel.add(new JLabel(""),c);
		optionPanel.validate();
	}
	
	
	public String getTitle(){
		return name;
	}
	
	public Dialog getDialog(){
		return dialog;
	}
	
	public int[] getLinks(){
		int[] links = new int[userOptions.size()];
		Object[] options = userOptions.toArray();
		for(int i=0;i<options.length;i++){
			links[i] = ((UserOption)options[i]).getLinkTo();
		}
		return links;
		
	}
	
	public void addUserOption(String dialog,int linkTo){
		DialogBack back = this.dialog.getBack();
		if(linkTo!=back.getCurrentIndex()){
			UserOption option = new UserOption(this,dialog,linkTo);
			userOptions.add(option);
			makeOptionsPanel();
			back.setCurrentDialog(back.getCurrentIndex());
		}else{
			System.out.println("Can't link to self");
		}
	}
	
	public void mouseClicked(MouseEvent e){
		if(e.getSource()==addUserLabel){
			UserOptionDialog UOD = new UserOptionDialog(this,null,dialog.getTitles());
		}
		if(e.getSource()==addFunctionLabel){
			System.out.println("Add Function");
		}
		if(e.getClickCount()==2){
			if(!clone){
				if(e.getSource()==nameLabel){
					//System.out.println("Changing Name");
					String answer = (String)JOptionPane.showInputDialog(
						this,
						"Title:",
						"Set Title",
						JOptionPane.PLAIN_MESSAGE,
						null,
						null,
						"");
					if(answer!=null){
						name=answer;
						nameLabel.setText(name);
						setTitle(name);
						dialog.getBack().setComboBox();
					}
				}
			}
			else{
				dialog.getBack().setCurrentDialog(index);
			
			}
		}
	}
	
	
	public void mouseEntered(MouseEvent e){
		
	}
	
	public void mousePressed(MouseEvent e){
		
	}
	
	public void mouseReleased(MouseEvent e){
		
	}
	
	public void mouseExited(MouseEvent e){
		
	}
	
	
	public DialogWidget clone(int index){
		return(new DialogWidget(dialog,name,index));
	}
	
}//END.
