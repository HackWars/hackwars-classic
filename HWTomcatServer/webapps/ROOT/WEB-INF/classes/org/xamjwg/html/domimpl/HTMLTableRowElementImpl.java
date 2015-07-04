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
 * Created on Dec 4, 2005
 */
package org.xamjwg.html.domimpl;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.html2.HTMLCollection;
import org.w3c.dom.html2.HTMLElement;
import org.w3c.dom.html2.HTMLTableRowElement;

public class HTMLTableRowElementImpl extends HTMLElementImpl implements
		HTMLTableRowElement {
	public HTMLTableRowElementImpl(String name) {
		super(name, true);
	}
	
	public HTMLTableRowElementImpl() {
		super("TR", true);
	}
	
	public int getRowIndex() {
		NodeImpl parent = (NodeImpl) this.getParentNode();
		if(parent == null) {
			return -1;
		}
		try {
			parent.visit(new NodeVisitor() {
				private int count = 0;
				
				public void visit(Node node) {
					if(node instanceof HTMLTableRowElementImpl) {
						if(HTMLTableRowElementImpl.this == node) {
							throw new StopVisitorException(new Integer(this.count));
						}
						this.count++;
					}
				}
			});
		} catch(StopVisitorException sve) {
			return ((Integer) sve.getTag()).intValue();
		}
		return -1;
	}
	
	public int getSectionRowIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public HTMLCollection getCells() {
		NodeFilter filter = new NodeFilter() {
			public boolean accept(Node node) {
				return node instanceof HTMLTableCellElementImpl;
			}
		};
		return new ChildHTMLCollection(this, filter, this.getTreeLock());
	}

	public String getAlign() {
		return this.getAttribute("align");
	}

	public void setAlign(String align) {
		this.setAttribute("align", align);
	}

	public String getBgColor() {
		return this.getAttribute("bgcolor");
	}

	public void setBgColor(String bgColor) {
		this.setAttribute("bgcolor", bgColor);
	}

	public String getCh() {
		return this.getAttribute("ch");
	}

	public void setCh(String ch) {
		this.setAttribute("ch", ch);
	}

	public String getChOff() {
		return this.getAttribute("choff");
	}

	public void setChOff(String chOff) {
		this.setAttribute("choff", chOff);
	}

	public String getVAlign() {
		return this.getAttribute("valign");
	}

	public void setVAlign(String vAlign) {
		this.setAttribute("valign", vAlign);
	}

	public HTMLElement insertCell(int index) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteCell(int index) throws DOMException {
		// TODO Auto-generated method stub

	}
}
