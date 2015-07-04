/*
    GNU LESSER GENERAL PUBLIC LICENSE
    Copyright (C) 2006 The XAMJ Project

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Contact info: info@xamjwg.org
*/
/*
 * Created on Jan 15, 2006
 */
package org.xamjwg.html.renderer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.xamjwg.html.domimpl.RenderableContext;
import org.xamjwg.util.WrapperLayout;
import javax.swing.*;

public class InputButtonControl extends BaseInputControl {
	private final JButton widget; 
	
	public InputButtonControl(final RenderableContext renderableContext) {
		super(renderableContext);
		this.setLayout(WrapperLayout.getInstance());
		String text = renderableContext.getAttribute("value");
		if(text == null || text == "") {
			String type = renderableContext.getAttribute("type");
			if("submit".equalsIgnoreCase(type)) {
				text = "Submit";
			}
			else if("reset".equalsIgnoreCase(type)) {
				text = "Reset";
			}
			else {
				text = "";
			}
		}
		JButton widget = new JButton(text);
		this.widget = widget;
		this.add(widget);
		widget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				renderableContext.onClick(event, true);
			}
		});
	}	
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#click()
	 */
	public void click() {
		this.widget.doClick();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#getValue()
	 */
	public String getValue() {
		return this.widget.getText();
	}

	public void setDisabled(boolean disabled) {
		super.setDisabled(disabled);
		this.widget.setEnabled(!disabled);
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#setValue(java.lang.String)
	 */
	public void setValue(String value) {
		this.widget.setText(value);
	}
}
