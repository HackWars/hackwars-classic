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
 * Created on Oct 8, 2005
 */
package org.xamjwg.html.domimpl;

import org.w3c.dom.Node;
import org.w3c.dom.html2.HTMLCollection;
import java.util.*;

public class FilteredHTMLCollectionImpl implements HTMLCollection {
	private final Map sourceMap;
	private final NodeFilter filter;
	private final Object lock;
	
	public FilteredHTMLCollectionImpl(Map sourceMap, NodeFilter filter, Object lock) {
		this.sourceMap = sourceMap;
		this.filter = filter;
		this.lock = lock;
	}

	public int getLength() {
		synchronized(this.lock) {
			int count = 0;
			Iterator i = this.sourceMap.values().iterator();
			while(i.hasNext()) {
				Node node = (Node) i.next();
				if(this.filter.accept(node)) {
					count++;
				}
			}
			return count;
		}
	}

	public Node item(int index) {
		synchronized(this.lock) {
			int count = 0;
			Iterator i = this.sourceMap.values().iterator();
			while(i.hasNext()) {
				Node node = (Node) i.next();
				if(this.filter.accept(node)) {
					if(count == index) {
						return node;
					}
					count++;
				}
			}
			return null;
		}
	}

	public Node namedItem(String name) {
		synchronized(this.lock) {
			Node node = (Node) this.sourceMap.get(name);
			if(node != null && this.filter.accept(node)) {
				return node;
			}
			return null;
		}
	}
}
