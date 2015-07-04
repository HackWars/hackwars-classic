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

import java.io.UnsupportedEncodingException;
import org.w3c.dom.Node;
import org.w3c.dom.html2.HTMLFormElement;
import org.xamjwg.html.FormInput;

public class HTMLBaseInputElement extends HTMLElementImpl {
	public HTMLBaseInputElement(String name, boolean noStyleSheet) {
		super(name, noStyleSheet);
	}

	public HTMLBaseInputElement(String name) {
		super(name);
	}
	
	protected InputContext inputContext;
	
	public void setInputContext(InputContext ic) {
		this.inputContext = ic;
	}

	public String getDefaultValue() {
		return this.getAttribute("defaultValue");
	}

	public void setDefaultValue(String defaultValue) {
		this.setAttribute("defaultValue", defaultValue);
	}

	public HTMLFormElement getForm() {
		Node parent = this.getParentNode();
		while(parent != null && !(parent instanceof HTMLFormElement)) {
			parent = parent.getParentNode();
		}
		return (HTMLFormElement) parent;
	}

	public String getAccept() {
		return this.getAttribute("accept");
	}

	public void setAccept(String accept) {
		this.setAttribute("accept", accept);
	}

	public String getAccessKey() {
		return this.getAttribute("accessKey");
	}

	public void setAccessKey(String accessKey) {
		this.setAttribute("accessKey", accessKey);
	}

	public String getAlign() {
		return this.getAttribute("align");
	}

	public void setAlign(String align) {
		this.setAttribute("align", align);
	}

	public String getAlt() {
		return this.getAttribute("alit");
	}

	public void setAlt(String alt) {
		this.setAttribute("alt", alt);
	}

	public boolean getDisabled() {
		InputContext ic = this.inputContext;
		return ic == null ? false : ic.getDisabled();
	}

	public void setDisabled(boolean disabled) {
		InputContext ic = this.inputContext;
		if(ic != null) {
			ic.setDisabled(disabled);
		}
	}

	public String getName() {
		return this.getAttribute("name");
	}

	public void setName(String name) {
		this.setAttribute("name", name);
	}

	public boolean getReadOnly() {
		InputContext ic = this.inputContext;
		return ic == null ? false : ic.getReadOnly();
	}

	public void setReadOnly(boolean readOnly) {
		InputContext ic = this.inputContext;
		if(ic != null) {
			ic.setReadOnly(readOnly);
		}
	}

	public int getTabIndex() {
		InputContext ic = this.inputContext;
		return ic == null ? 0 : ic.getTabIndex();
	}

	public void setTabIndex(int tabIndex) {
		InputContext ic = this.inputContext;
		if(ic != null) {
			ic.setTabIndex(tabIndex);
		}
	}

	public String getValue() {
		InputContext ic = this.inputContext;
		if(ic != null) {
			//Note: Per HTML Spec, it does not set attribute.
			return ic.getValue();
		}
		else {
			return this.getAttribute("value");
		}
	}

	public void setValue(String value) {
		InputContext ic = this.inputContext;
		if(ic != null) {
			//Note: Per HTML Spec, it does not set attribute.
			ic.setValue(value);
		}
		else {
			this.setAttribute("value", value);
		}
	}

	public void blur() {
		InputContext ic = this.inputContext;
		if(ic != null) {
			ic.blur();
		}
	}

	public void focus() {
		InputContext ic = this.inputContext;
		if(ic != null) {
			ic.focus();
		}
	}

	public void select() {
		InputContext ic = this.inputContext;
		if(ic != null) {
			ic.select();
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.HTMLElementImpl#assignAttributeField(java.lang.String, java.lang.String)
	 */
	protected void assignAttributeField(String normalName, String value) {
		if("VALUE".equals(normalName)) {
			InputContext ic = this.inputContext;
			if(ic != null) {
				ic.setValue(value);
			}
		}
		else {
			super.assignAttributeField(normalName, value);
		}
	}
}
