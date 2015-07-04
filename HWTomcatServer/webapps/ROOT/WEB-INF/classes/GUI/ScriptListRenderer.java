package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class ScriptListRenderer extends JLabel implements ListCellRenderer{

	private HacktendoCreator hacktendoCreator=null;
	
	public ScriptListRenderer(HacktendoCreator hacktendoCreator){
		this.hacktendoCreator=hacktendoCreator;
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}
	
	public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus) {
		
		//int selectedIndex = ((Integer)value).intValue();
		
		if (isSelected) {
			setBackground(list.getSelectionBackground());
		} else {
			setBackground(list.getBackground());
		}
		Script script =hacktendoCreator.getScript(index);
		if(script!=null){
			if(script.getType()==Script.MAP)
				setForeground(Color.red);
			else
				setForeground(Color.blue);
			String name = script.getName();
			
			setText(index+": "+name);
		}
		else{
			setText(index+": ");
		}
		
		return this;
	}
}
