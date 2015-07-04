package GUI;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import net.miginfocom.swing.*;

public class ToolTip extends JPanel{
	private Color BACKGROUND_COLOR = new Color(184,207,229);
	private Color BORDER_COLOR = new Color(99,130,191);
	private int PADDING = 5;
	private String tip = "";
	private Hacker hacker;
	private JLabel label = new JLabel("");
	public ToolTip(String tip,Hacker hacker){
		this.tip = tip;
		this.hacker = hacker;
		setBackground(BACKGROUND_COLOR);
		setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
		setLayout(new MigLayout("align leading,ins "+PADDING+",fill"));
		label.setText(tip);
		add(label);
		setVisible(false);
		setPreferredSize(new Dimension(label.getPreferredSize().width+PADDING*4,label.getPreferredSize().height+PADDING*2));
		hacker.getPanel().add(this);
	}
	
	public void show(int x, int y){
		setVisible(true);
		setBounds(x,y,label.getPreferredSize().width+PADDING*4,label.getPreferredSize().height+PADDING*2);
		hacker.getPanel().setComponentZOrder(this,0);
	}
	
	public void setText(String text){
		if(label!=null&&text!=null){
			try{
				label.setText(text);
				setBounds(getBounds().x,getBounds().y,label.getPreferredSize().width+PADDING*4,label.getPreferredSize().height+PADDING*2);
			}catch(Exception e){}
		}
	}
	
	public void move(int x){
		setBounds(x,getBounds().y,label.getPreferredSize().width+PADDING*2,label.getPreferredSize().height+PADDING*2);
	
	}

}
