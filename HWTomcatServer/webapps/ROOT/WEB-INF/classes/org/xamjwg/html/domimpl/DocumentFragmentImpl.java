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
 * Created on Oct 9, 2005
 */
package org.xamjwg.html.domimpl;

import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;

public class DocumentFragmentImpl extends NodeImpl implements DocumentFragment {
	public DocumentFragmentImpl() {
		super();
	}

	public String getLocalName() {
		return null; 
	}

	public String getNodeName() {
		return "#document-fragment";
	}

	public String getNodeValue() throws DOMException {
		return null;
	}

	public void setNodeValue(String nodeValue) throws DOMException {
	}

	public short getNodeType() {
		return org.w3c.dom.Node.DOCUMENT_FRAGMENT_NODE;
	}
}
