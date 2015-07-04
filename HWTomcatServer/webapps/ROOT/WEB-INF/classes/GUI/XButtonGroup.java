package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Collection;

public class XButtonGroup extends ButtonGroup {
	public String name = new String();	
	public int select;
	public ArrayList<JRadioButton> bgList = new ArrayList<JRadioButton>(10);

	public String getName() {
		return name;
	}

	public void addButton (JRadioButton rb) {
		bgList.add(rb);
	}

    public String getSelect() {
		bgList.trimToSize();
        JRadioButton rb;
        String retVal = "";
		for (int i = 0; i < bgList.size(); i ++) {
            rb = bgList.get(i);
            if (rb.isSelected()) {
                return rb.getText().toLowerCase();
            }
            // if we didn't find any value, return the last one -- hopefully this will never happen
            if ((i+1) == bgList.size()) {
                retVal = rb.getText().toLowerCase();
            }
		}
        return(retVal);
    }

	public void setSelect(String select) {
		bgList.trimToSize();
        JRadioButton rb;
		for (int i = 0; i < bgList.size(); i ++) {
            rb = bgList.get(i);
			if (rb.getText().toLowerCase().equals(select)) {
				rb.setSelected(true);
			}
		}
	}

	public XButtonGroup(String text) {
		name = text;
	}
}
