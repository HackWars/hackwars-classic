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
 * Created on Oct 23, 2005
 */
package org.xamjwg.html.parser;

import java.util.Set;

class ElementInfo {
	public final int endElementType;
	public final boolean childElementOk;
	public final Set stopTags;
	
	public static final int END_ELEMENT_FORBIDDEN = 0;
	public static final int END_ELEMENT_OPTIONAL = 1;
	public static final int END_ELEMENT_REQUIRED = 2;
	
	/**
	 * @param ok
	 * @param type
	 */
	public ElementInfo(boolean ok, int type) {
		super();
		this.childElementOk = ok;
		this.endElementType = type;
		this.stopTags = null;
	}

	/**
	 * @param ok
	 * @param type
	 */
	public ElementInfo(boolean ok, int type, Set stopTags) {
		super();
		this.childElementOk = ok;
		this.endElementType = type;
		this.stopTags = stopTags;
	}
}
