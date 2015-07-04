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

import org.w3c.dom.html2.HTMLFormElement;
import org.w3c.dom.html2.HTMLTextAreaElement;
import org.xamjwg.html.FormInput;

public class HTMLTextAreaElementImpl extends HTMLBaseInputElement implements
		HTMLTextAreaElement {

	public HTMLTextAreaElementImpl(String name, boolean noStyleSheet) {
		super(name, noStyleSheet);
	}

	public HTMLTextAreaElementImpl(String name) {
		super(name);
	}

	public HTMLTextAreaElementImpl() {
		super("TEXTAREA");
	}
	
	protected FormInput getFormInput(HTMLInputElementImpl submit) {
		try {
			return new FormInput(this.getName(), this.getValue());
		} catch(UnsupportedEncodingException uee) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.w3c.dom.html2.HTMLTextAreaElement#getCols()
	 */
	public int getCols() {
		InputContext ic = this.inputContext;
		return ic == null ? 0 : ic.getCols();
	}

	/* (non-Javadoc)
	 * @see org.w3c.dom.html2.HTMLTextAreaElement#getRows()
	 */
	public int getRows() {
		InputContext ic = this.inputContext;
		return ic == null ? 0 : ic.getRows();
	}

	/* (non-Javadoc)
	 * @see org.w3c.dom.html2.HTMLTextAreaElement#getType()
	 */
	public String getType() {
		return "textarea";
	}

	/* (non-Javadoc)
	 * @see org.w3c.dom.html2.HTMLTextAreaElement#setCols(int)
	 */
	public void setCols(int cols) {
		InputContext ic = this.inputContext;
		if(ic != null) {
			ic.setCols(cols);
		}
	}

	/* (non-Javadoc)
	 * @see org.w3c.dom.html2.HTMLTextAreaElement#setRows(int)
	 */
	public void setRows(int rows) {
		InputContext ic = this.inputContext;
		if(ic != null) {
			ic.setRows(rows);
		}
	}	
}
