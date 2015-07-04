package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ScriptHandler extends JPanel implements ActionListener,ListSelectionListener{

	private HacktendoCreator hacktendoCreator=null;
	private JList list=null;
	private DefaultListModel listModel=new DefaultListModel();
	private JTabbedPane parent=null;
	
	public ScriptHandler(HacktendoCreator hacktendoCreator,JTabbedPane parent){
		this.hacktendoCreator=hacktendoCreator;
		this.parent=parent;
		setLayout(null);
		populate();
	}
	
	public void populate(){
		removeAll();
		listModel.removeAllElements();
		Script[] scripts = hacktendoCreator.getScripts();
		if(scripts!=null){
			for(int i=0;i<scripts.length;i++){
				Script current = scripts[i];
				if(current!=null)
					listModel.addElement(i+": "+current.getName());
				else
					listModel.addElement(i+": ");
			}	
		}
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.setCellRenderer(new ScriptListRenderer(hacktendoCreator));
		list.addListSelectionListener(this);
		
		JScrollPane sp = new JScrollPane(list);
		add(sp);
		sp.setBounds(0,10,100,parent.getHeight()-50);
		
		JButton mapButton = new JButton("New Map Script");
		add(mapButton);
		mapButton.addActionListener(this);
		mapButton.setBounds(110,40,mapButton.getPreferredSize().width,mapButton.getPreferredSize().height);
		
		JButton spriteButton = new JButton("New Sprite Script");
		add(spriteButton);
		spriteButton.addActionListener(this);
		spriteButton.setBounds(110,80,spriteButton.getPreferredSize().width,spriteButton.getPreferredSize().height);
		
		JButton editButton = new JButton("Edit Script");
		add(editButton);
		editButton.addActionListener(this);
		editButton.setBounds(110,120,editButton.getPreferredSize().width,editButton.getPreferredSize().height);
		
		
	}
	
	public void actionPerformed(ActionEvent e){
		String ac = e.getActionCommand();
		if(ac.equals("New Map Script")){
			hacktendoCreator.newScript(Script.MAP);
			populate();
		}
		if(ac.equals("New Sprite Script")){
			hacktendoCreator.newScript(Script.SPRITE);
			populate();
		}
		if(ac.equals("Edit Script")){
			int index = list.getSelectedIndex();
			if(index!=-1)
				hacktendoCreator.editScript(index);
		}
		
	}
	
	public void valueChanged(ListSelectionEvent e) {
	 
	}

}
