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

import org.xamjwg.html.domimpl.RenderableContext;
import org.xamjwg.util.WrapperLayout;
import javax.swing.*;

public class InputRadioControl extends BaseInputControl {
	private final JRadioButton widget; 
	
	public InputRadioControl(RenderableContext renderableContext) {
		super(renderableContext);
		this.setLayout(WrapperLayout.getInstance());
		String checkedText = renderableContext.getAttribute("checked");
		JRadioButton radio = new JRadioButton();
		radio.setOpaque(false);
		radio.setSelected("checked".equalsIgnoreCase(checkedText));
		String name = renderableContext.getAttribute("name");
		if(name != null) {
			String key = "radio.group." + name;
			ButtonGroup group = (ButtonGroup) renderableContext.getDocumentItem(key);
			if(group == null) {
				group = new ButtonGroup();
				renderableContext.setDocumentItem(key, group);
			}
			group.add(radio);
		}
		this.widget = radio; 
		this.add(radio);
	}	
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#click()
	 */
	public void click() {
		this.widget.doClick();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#getChecked()
	 */
	public boolean getChecked() {
		return this.widget.isSelected();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#setChecked(boolean)
	 */
	public void setChecked(boolean checked) {
		this.widget.setSelected(checked);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#setDisabled(boolean)
	 */
	public void setDisabled(boolean disabled) {
		super.setDisabled(disabled);
		this.widget.setEnabled(!disabled);
	}
}
