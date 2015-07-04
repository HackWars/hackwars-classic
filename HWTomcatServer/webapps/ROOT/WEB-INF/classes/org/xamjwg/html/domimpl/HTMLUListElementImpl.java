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

import org.w3c.dom.html2.HTMLUListElement;

public class HTMLUListElementImpl extends HTMLElementImpl implements
		HTMLUListElement {
	public HTMLUListElementImpl(String name, boolean noStyleSheet) {
		super(name, noStyleSheet);
	}

	public HTMLUListElementImpl(String name) {
		super(name);
	}

	
	public boolean getCompact() {
		String compactText = this.getAttribute("compact");
		return "compact".equalsIgnoreCase(compactText);
	}

	public void setCompact(boolean compact) {
		this.setAttribute("compact", compact ? "compact" : null);
	}

	public String getType() {
		return this.getAttribute("type");
	}

	public void setType(String type) {
		this.setAttribute("type", type);
	}
}
