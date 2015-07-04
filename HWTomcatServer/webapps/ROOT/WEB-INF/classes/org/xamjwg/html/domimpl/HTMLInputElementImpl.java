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
package org.xamjwg.html.domimpl;

import java.awt.AWTEvent;
import java.io.UnsupportedEncodingException;

import org.w3c.dom.html2.HTMLInputElement;
import org.xamjwg.html.FormInput;

public class HTMLInputElementImpl extends HTMLBaseInputElement implements
		HTMLInputElement {

	public HTMLInputElementImpl(String name, boolean noStyleSheet) {
		super(name, noStyleSheet);
	}

	public HTMLInputElementImpl(String name) {
		super(name);
	}
	
	private boolean defaultChecked;
	
	public boolean getDefaultChecked() {
		return this.defaultChecked;
	}

	public void setDefaultChecked(boolean defaultChecked) {
		this.defaultChecked = defaultChecked;
	}

	public boolean getChecked() {
		InputContext ic = this.inputContext;
		return ic == null ? false : ic.getChecked();
	}

	public void setChecked(boolean checked) {
		InputContext ic = this.inputContext;
		if(ic != null) {
			ic.setChecked(checked);
		}
	}

	public int getMaxLength() {
		InputContext ic = this.inputContext;
		return ic == null ? 0 : ic.getMaxLength();
	}

	public void setMaxLength(int maxLength) {
		InputContext ic = this.inputContext;
		if(ic != null) {
			ic.setMaxLength(maxLength);
		}
	}

	public int getSize() {
		InputContext ic = this.inputContext;
		return ic == null ? 0 : ic.getControlSize();
	}

	public void setSize(int size) {
		InputContext ic = this.inputContext;
		if(ic != null) {
			ic.setControlSize(size);
		}
	}

	public String getSrc() {
		return this.getAttribute("src");
	}

	public void setSrc(String src) {
		this.setAttribute("src", src);
	}

	public String getType() {
		return this.getAttribute("type");
	}

	public void setType(String type) {
		this.setAttribute("type", type);
	}

	public String getUseMap() {
		return this.getAttribute("usemap");
	}

	public void setUseMap(String useMap) {
		this.setAttribute("usemap", useMap);
	}

	public void click() {
		InputContext ic = this.inputContext;
		if(ic != null) {
			ic.click();
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.RenderableContext#onClick(java.awt.AWTEvent, boolean)
	 */
	public boolean onClick(AWTEvent event, boolean propagate) {
		if(super.onClick(event, propagate)) {
			String type = this.getAttribute("type");
			if("submit".equalsIgnoreCase(type)) {
				HTMLFormElementImpl form = (HTMLFormElementImpl) this.getForm();
				if(form != null) {
					form.submit(this);
				}				
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.RenderableContext#onEnterPressed(java.awt.AWTEvent, boolean)
	 */
	public boolean onEnterPressed(AWTEvent event, boolean propagate) {
		if(super.onEnterPressed(event, propagate)) {
			HTMLFormElementImpl form = (HTMLFormElementImpl) this.getForm();
			if(form != null) {
				form.submit();
			}		
			return true;
		}
		else {
			return false;
		}
	}

	protected FormInput getFormInput(HTMLInputElementImpl submit) {
		try {
			String type = this.getType();
			String name = this.getName();
			if(name == null) {
				return null;
			}
			if(type == null) {
				return new FormInput(name, this.getValue());
			}
			else { 
				type = type.toLowerCase();
				if("text".equals(type) || "password".equals(type) || "".equals(type)) {
					return new FormInput(name, this.getValue());
				}
				else if("submit".equals(type)) {
					if(submit == this) {
						return new FormInput(name, this.getValue());
					}
					else {
						return null;
					}
				}
				else if("radio".equals(type) || "checkbox".equals(type)) {
					if(this.getChecked()) {
						return new FormInput(name, this.getValue());
					}
					else {
						return null;
					}
				}
				else {
					return null;
				}
			}
		} catch(UnsupportedEncodingException uee) {
			return null;
		}
	}
}
