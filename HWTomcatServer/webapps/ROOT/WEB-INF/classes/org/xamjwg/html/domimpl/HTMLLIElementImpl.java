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
 * Created on Feb 12, 2006
 */
package org.xamjwg.html.domimpl;

import org.w3c.dom.html2.HTMLLIElement;

public class HTMLLIElementImpl extends HTMLElementImpl implements HTMLLIElement {
	public HTMLLIElementImpl(String name, boolean noStyleSheet) {
		super(name, noStyleSheet);
	}

	public HTMLLIElementImpl(String name) {
		super(name);
	}

	public String getType() {
		return this.getAttribute("type");
	}

	public void setType(String type) {
		this.setAttribute("type", type);
	}

	public int getValue() {
		String valueText = this.getAttribute("value");
		if(valueText == null) {
			return 0;
		}
		try {
			return Integer.parseInt(valueText);
		} catch(NumberFormatException nfe) {
			return 0;
		}
	}

	public void setValue(int value) {
		this.setAttribute("value", String.valueOf(value));
	}
}
