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
 * Created on Nov 12, 2005
 */
package org.xamjwg.html;

import org.w3c.dom.Document;

/**
 * The <code>WindowInfo</code> class roughly corresponds to
 * the Window class in Javascript.
 * @author J. H. S.
 */
public class WindowInfo {
	public final Document document;
	public final HtmlParserContext context;
	
	/**
	 * Constructs a <code>WindowInfo</code> instance.
	 * @param context An instance of <code>HtmlContext</code>.
	 * @param document An instance of <code>Document</code>.
	 */
	public WindowInfo(HtmlParserContext context, Document document) {
		super();
		this.context = context;
		this.document = document;
	}
}
