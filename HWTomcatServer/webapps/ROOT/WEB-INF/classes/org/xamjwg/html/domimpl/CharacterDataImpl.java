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
 * Created on Sep 3, 2005
 */
package org.xamjwg.html.domimpl;

import org.w3c.dom.*;

public abstract class CharacterDataImpl extends NodeImpl implements
		CharacterData {
	protected volatile String text;
	
	public CharacterDataImpl() {
		super();
	}
	
	public CharacterDataImpl(String text) {
		this.text = text;
	}
	
	public String getClassName() {
		return "HTMLCharacterData";
	}

	public String getTextContent() throws DOMException {
		return this.text;
	}

	public void setTextContent(String textContent) throws DOMException {
		this.text = textContent;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.NodeImpl#cloneNode(boolean)
	 */
	public Node cloneNode(boolean deep) {
		CharacterDataImpl newNode = (CharacterDataImpl) super.cloneNode(deep);
		newNode.setData(this.getData());
		return newNode;
	}


	public void appendData(String arg) throws DOMException {
		// TODO Auto-generated method stub

	}

	public void deleteData(int offset, int count)
			throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getData() throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void insertData(int offset, String arg)
			throws DOMException {
		// TODO Auto-generated method stub

	}

	public void replaceData(int offset, int count, String arg)
			throws DOMException {
		// TODO Auto-generated method stub

	}

	public void setData(String data) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String substringData(int offset, int count)
			throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

}
