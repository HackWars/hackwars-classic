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
 * Created on Dec 3, 2005
 */
package org.xamjwg.html.domimpl;

import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.html2.HTMLCollection;

public class ChildHTMLCollection implements HTMLCollection {
	private final NodeImpl rootNode;
	private final NodeFilter nodeFilter;
	private final Object lock;

	/**
	 * @param node
	 * @param filter
	 * @param lock
	 */
	public ChildHTMLCollection(NodeImpl node, NodeFilter filter, Object lock) {
		super();
		rootNode = node;
		nodeFilter = filter;
		this.lock = lock;
	}
	
	public int getLength() {
		NodeCounter nc = new NodeCounter();
		synchronized(this.lock) {
			this.rootNode.visitImpl(nc);
		}
		return nc.getCount();
	}

	public Node item(int index) {
		NodeScanner ns = new NodeScanner(index);
		synchronized(this.lock) {
			try {
				this.rootNode.visitImpl(ns);
			} catch(StopVisitorException sve) {
				//ignore
			}
		}
		return ns.getNode();
	}

	public Node namedItem(String name) {
		org.w3c.dom.Document doc = this.rootNode.getOwnerDocument();
		if(doc == null) {
			return null;
		}
		//TODO: This might get elements that are not descendents.
		Node node = (Node) doc.getElementById(name);
		if(node != null && this.nodeFilter.accept(node)) {
			return node;
		}
		return null;
	}

	private final class NodeCounter implements NodeVisitor {
		private int count = 0;
		
		public final void visit(Node node) {
			if(nodeFilter.accept(node)) {
				this.count++;
				throw new SkipVisitorException();
			}
		}
		
		public int getCount() {
			return this.count;
		}
	}	

	private final class NodeScanner implements NodeVisitor {
		private int count = 0;
		private Node foundNode = null;
		private final int targetIndex;
		
		public NodeScanner(int idx) {
			this.targetIndex = idx;
		}
		
		public final void visit(Node node) {
			if(nodeFilter.accept(node)) {
				if(this.count == this.targetIndex) {
					this.foundNode = node;
					throw new StopVisitorException();
				}
				this.count++;
				throw new SkipVisitorException();
			}
		}
		
		public Node getNode() {
			return this.foundNode;
		}
	}	
}
