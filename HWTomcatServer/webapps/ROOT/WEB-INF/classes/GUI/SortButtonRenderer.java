package GUI;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import net.miginfocom.swing.*;
 
/**
 * @version 1.0 02/25/99
 */
public class SortButtonRenderer extends JButton implements TableCellRenderer {
  public static final int NONE = 0;
  public static final int DOWN = 1;
  public static final int UP   = 2;
  
  int pushedColumn;
  Hashtable state;
  JButton downButton,upButton,ducttape,germanium,silicon,ybco,plutonium;
  
  public SortButtonRenderer() {
    pushedColumn   = -1;
    state = new Hashtable();
    setContentAreaFilled(false);
    setMargin(new Insets(0,0,0,0));
    setHorizontalTextPosition(LEFT);
    setIcon(new BlankIcon());
    
    // perplexed  
    // ArrowIcon(SwingConstants.SOUTH, true)   
    // BevelArrowIcon (int direction, boolean isRaisedView, boolean isPressedView)
    
    downButton = new JButton();
    downButton.setMargin(new Insets(0,0,0,0));
    downButton.setHorizontalTextPosition(LEFT);
    downButton.setIcon(new BevelArrowIcon(BevelArrowIcon.UP, false, false));
    downButton.setPressedIcon(new BevelArrowIcon(BevelArrowIcon.UP, false, true));
	downButton.setContentAreaFilled(false);
    
    upButton = new JButton();
    upButton.setMargin(new Insets(0,0,0,0));
    upButton.setHorizontalTextPosition(LEFT);
    upButton.setIcon(new BevelArrowIcon(BevelArrowIcon.DOWN, false, false));
    upButton.setPressedIcon(new BevelArrowIcon(BevelArrowIcon.DOWN, false, true));
	upButton.setContentAreaFilled(false);
	
	ducttape = new JButton(ImageLoader.getImageIcon("images/ducttape.png"));
    ducttape.setMargin(new Insets(0,0,0,0));
    ducttape.setHorizontalTextPosition(LEFT);
	ducttape.setContentAreaFilled(false);
	ducttape.setBorderPainted(false);
	ducttape.setBorder(null);
	
	germanium = new JButton(ImageLoader.getImageIcon("images/germanium.png"));
    germanium.setMargin(new Insets(0,0,0,0));
    germanium.setHorizontalTextPosition(LEFT);
	germanium.setContentAreaFilled(false);
	germanium.setBorderPainted(false);
	germanium.setBorder(null);
	
	silicon = new JButton(ImageLoader.getImageIcon("images/silicon.png"));
    silicon.setMargin(new Insets(0,0,0,0));
    silicon.setHorizontalTextPosition(LEFT);
	silicon.setContentAreaFilled(false);
	silicon.setBorderPainted(false);
	silicon.setBorder(null);
    
	ybco = new JButton(ImageLoader.getImageIcon("images/YBCO.png"));
    ybco.setMargin(new Insets(0,0,0,0));
    ybco.setHorizontalTextPosition(LEFT);
	ybco.setContentAreaFilled(false);
	ybco.setBorderPainted(false);
	ybco.setBorder(null);
	
	plutonium = new JButton(ImageLoader.getImageIcon("images/plutonium.png"));
    plutonium.setMargin(new Insets(0,0,0,0));
    plutonium.setHorizontalTextPosition(LEFT);
	plutonium.setContentAreaFilled(false);
	plutonium.setBorderPainted(false);
	plutonium.setBorder(null);
  }
  
  public Component getTableCellRendererComponent(JTable table, Object value,
                   boolean isSelected, boolean hasFocus, int row, int column) {
				   
	String title = (String)value;
	Object obj = state.get(new Integer(column));
	if(title.equals("Duct Tape")||title.equals("Germanium")||title.equals("Silicon")||title.equals("YBCO")||title.equals("Plutonium")){
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("fillx,ins 0,gap 0 0 "));
		JButton button  = new JButton();
		if(title.equals("Duct Tape")){
			button = ducttape;
		}
		else if(title.equals("Germanium")){
			button = germanium;
		}
		else if(title.equals("Silicon")){
			button = silicon;
		}
		else if(title.equals("YBCO")){
			button = ybco;
		}
		else if(title.equals("Plutonium")){
			button = plutonium;
		}
		panel.add(button);
		JButton arrow = new JButton();
		if (obj != null) {
		  if (((Integer)obj).intValue() == DOWN) {
			arrow = downButton;
		  } else {
			arrow = upButton;
		  }
		  panel.add(arrow);
		}
		arrow.setText("");
		boolean isPressed = (column == pushedColumn);
		arrow.getModel().setPressed(isPressed);
		arrow.getModel().setArmed(isPressed);
		arrow.setHorizontalAlignment(JButton.LEFT);
		panel.setPreferredSize(new Dimension(32,button.getPreferredSize().height+1));
		panel.setBorder(BorderFactory.createLineBorder(new Color(122,138,153)));
		return panel;
	}
	else {
		JButton button = this;
		if (obj != null) {
		  if (((Integer)obj).intValue() == DOWN) {
			button = downButton;
		  } else {
			button = upButton;
		  }
		}
		button.setText((value ==null) ? "" : value.toString());
		boolean isPressed = (column == pushedColumn);
		button.getModel().setPressed(isPressed);
		button.getModel().setArmed(isPressed);
		button.setHorizontalAlignment(JButton.LEFT);
		return button;
	}
  }
  
  public void setPressedColumn(int col) {
    pushedColumn = col;
  }
  
  public void setSelectedColumn(int col) {
    if (col < 0) return;
    Integer value = null;
    Object obj = state.get(new Integer(col));
    if (obj == null) {
      value = new Integer(DOWN);
    } else {
      if (((Integer)obj).intValue() == DOWN) {
        value = new Integer(UP);
      } else {
        value = new Integer(DOWN);
      }
    }
    state.clear();
    state.put(new Integer(col), value);
  } 
  
  public int getState(int col) {
    int retValue;
    Object obj = state.get(new Integer(col));
    if (obj == null) {
      retValue = NONE;
    } else {
      if (((Integer)obj).intValue() == DOWN) {
        retValue = DOWN;
      } else {
        retValue = UP;
      }
    }
    return retValue;
  } 
}