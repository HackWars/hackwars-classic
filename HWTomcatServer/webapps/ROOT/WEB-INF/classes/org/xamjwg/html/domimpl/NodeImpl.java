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

import java.util.*;
import org.w3c.dom.*;
import org.xamjwg.util.*;
import org.xamjwg.html.*;

public abstract class NodeImpl implements Node {
	private static final NodeImpl[] EMPTY_ARRAY = new NodeImpl[0];
	
	public NodeImpl() {
		super();
	}

	protected ContainingBlockContext containingBlockContext;
	
	public void setContainingBlockContext(ContainingBlockContext containingBlockContext) {
		this.containingBlockContext = containingBlockContext;
	}
	
	public ContainingBlockContext getContainingBlockContext() {
		return this.containingBlockContext;
	}
		
	protected final Object getTreeLock() {
		Object doc = this.getOwnerDocument();
		return doc == null ? this : doc;
	}
	
	protected ListSet nodeList;
	
	public Node appendChild(Node newChild)
			throws DOMException {
		synchronized(this.getTreeLock()) {
			ListSet nl = this.nodeList;
			if(nl == null) {
				nl = new ListSet();
				this.nodeList = nl;
			}
			nl.add(newChild);
		}
		if(newChild instanceof NodeImpl) {
			((NodeImpl) newChild).addNotify(this);
		}
		return newChild;
	}
	
	protected void removeAllChildren() {
		synchronized(this.getTreeLock()) {
			ListSet nl = this.nodeList;
			if(nl != null) {
				nl.clear();
				//this.nodeList = null;
			}
		}		
	}

	protected NodeList getNodeList(NodeFilter filter) {
		Collection collection = new ArrayList();
		synchronized(this.getTreeLock()) {
			this.appendChildrenToCollection(filter, collection);
		}		
		return new NodeListImpl(collection);
	}

	public NodeImpl[] getChildrenArray() {
		ListSet nl = this.nodeList;
		synchronized(this.getTreeLock()) {
			return nl == null ? null : (NodeImpl[]) nl.toArray(NodeImpl.EMPTY_ARRAY);
		}
	}
	
	/**
	 * Gets descendent nodes that match according to
	 * the filter, but it does not nest into matching nodes.
	 */
	public ArrayList getDescendents(NodeFilter filter) {
		ArrayList al = new ArrayList();
		synchronized(this.getTreeLock()) {
			this.extractDescendentsArrayImpl(filter, al);
		}
		return al;
	}
	
	private void extractDescendentsArrayImpl(NodeFilter filter, ArrayList al) {
		ListSet nl = this.nodeList;
		if(nl != null) {
			Iterator i = nl.iterator();
			while(i.hasNext()) {
				NodeImpl n = (NodeImpl) i.next();
				if(filter.accept(n)) {
					al.add(n);
				}
				else if(n.getNodeType() == Node.ELEMENT_NODE) {
					n.extractDescendentsArrayImpl(filter, al);
				}
			}
		}
	}

	private void appendChildrenToCollection(NodeFilter filter, Collection collection) {
		ListSet nl = this.nodeList;
		if(nl != null) {
			Iterator i = nl.iterator();
			while(i.hasNext()) {
				NodeImpl node = (NodeImpl) i.next();
				if(filter.accept(node)) {
					collection.add(node);
				}
				node.appendChildrenToCollection(filter, collection);
			}
		}
	}
	
	public Node cloneNode(boolean deep) {
		try {
			Class thisClass = this.getClass();
			Node newNode = (Node) thisClass.newInstance();
			NodeList children = this.getChildNodes();
			int length = children.getLength();
			for(int i = 0; i < length; i++) {
				Node child = (Node) children.item(i);
				Node newChild = deep ? child.cloneNode(deep) : child;
				newNode.appendChild(newChild);
			}
			if(newNode instanceof Element) {
				Element elem = (Element) newNode;
				NamedNodeMap nnmap = this.getAttributes();
				if(nnmap != null) {
					int nnlength = nnmap.getLength();
					for(int i = 0; i < nnlength; i++) {
						Attr attr = (Attr) nnmap.item(i);
						elem.setAttributeNode((Attr) attr.cloneNode(true));
					}
				}
			}
			return newNode;
		} catch(Exception err) {
			throw new IllegalStateException(err.getMessage());
		}
	}

	private int getNodeIndex() {
		NodeImpl parent = (NodeImpl) this.getParentNode();
		return parent == null ? -1 : parent.getChildIndex(this);
	}
	
	private int getChildIndex(Node child) {
		synchronized(this.getTreeLock()) {
			return this.nodeList.indexOf(child);
		}
	}

	private boolean isAncestorOf(Node other) {
		NodeImpl parent = (NodeImpl) other.getParentNode();
		if(parent == this) {
			return true;
		}
		else if(parent == null) {
			return false;
		}
		else {
			return this.isAncestorOf(parent);
		}
	}
	
	public short compareDocumentPosition(Node other)
			throws DOMException {
		Node parent = this.getParentNode();
		if(!(other instanceof NodeImpl)) {
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Unknwon node implementation");
		}
		if(parent != null && parent == other.getParentNode()) {
			int thisIndex = this.getNodeIndex();
			int otherIndex = ((NodeImpl) other).getNodeIndex();
			if(thisIndex == -1 || otherIndex == -1) {
				return Node.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC;
			}
			if(thisIndex < otherIndex) {
				return Node.DOCUMENT_POSITION_FOLLOWING;
			}
			else {
				return Node.DOCUMENT_POSITION_PRECEDING;
			}
		}
		else if(this.isAncestorOf(other)) {
			return Node.DOCUMENT_POSITION_CONTAINED_BY;
		}
		else if(((NodeImpl) other).isAncestorOf(this)) {
			return Node.DOCUMENT_POSITION_CONTAINS;
		}
		else {
			return Node.DOCUMENT_POSITION_DISCONNECTED;
		}
	}

	public NamedNodeMap getAttributes() {
		return null;
	}

	protected volatile Document document;
	
	public Document getOwnerDocument() {
		return this.document;
	}

	void setOwnerDocument(Document value) {
	    this.document = value;
	}
	
	void setOwnerDocument(Document value, boolean deep) {
		this.document = value;
		if(deep) {
			ListSet nl = this.nodeList;
			if(nl != null) {
				Iterator i = nl.iterator();
				while(i.hasNext()) {
					NodeImpl child = (NodeImpl) i.next();
					child.setOwnerDocument(value, deep);
				}
			}
		}
	}
	
	void visitImpl(NodeVisitor visitor) {
		try {
		    visitor.visit(this);
		} catch(SkipVisitorException sve) {
			return;
		} catch(StopVisitorException sve) {
			throw sve;
		}
		ListSet nl = this.nodeList;
		if(nl != null) {
			Iterator i = nl.iterator();
			while(i.hasNext()) {
				NodeImpl child = (NodeImpl) i.next();
				try {
					child.visitImpl(visitor);
				} catch(StopVisitorException sve) {
					throw sve;
				}
			}
		}
	}
	
	void visit(NodeVisitor visitor) {
		synchronized(this.getTreeLock()) {
			this.visitImpl(visitor);
		}
	}
	
	public Node insertBefore(Node newChild, Node refChild)
			throws DOMException {
		synchronized(this.getTreeLock()) {
			ListSet nl = this.nodeList;
			int idx = nl == null ? -1 : nl.indexOf(refChild);
			if(idx == -1) {
				throw new DOMException(DOMException.NOT_FOUND_ERR, "refChild not found");
			}
			nl.add(idx, newChild);
		}
		if(newChild instanceof NodeImpl) {
			((NodeImpl) newChild).addNotify(this);
		}
		return newChild;
	}

	protected Node insertAt(Node newChild, int idx)
	throws DOMException {
		synchronized(this.getTreeLock()) {
			ListSet nl = this.nodeList;
			nl.add(idx, newChild);
		}
		if(newChild instanceof NodeImpl) {
			((NodeImpl) newChild).addNotify(this);
		}
		return newChild;
	}

	public Node replaceChild(Node newChild, Node oldChild)
			throws DOMException {
		synchronized(this.getTreeLock()) {
			ListSet nl = this.nodeList;
			int idx = nl == null ? -1 : nl.indexOf(oldChild);
			if(idx == -1) {
				throw new DOMException(DOMException.NOT_FOUND_ERR, "oldChild not found");
			}
			nl.set(idx, newChild);
		}
		return newChild;
	}

	public Node removeChild(Node oldChild)
			throws DOMException {
		synchronized(this.getTreeLock()) {
			ListSet nl = this.nodeList;
			if(nl == null || !nl.remove(oldChild)) {
				throw new DOMException(DOMException.NOT_FOUND_ERR, "oldChild not found");
			}
		}
		return oldChild;
	}

	public Node removeChildAt(int index)
	throws DOMException {
		synchronized(this.getTreeLock()) {
			ListSet nl = this.nodeList;
			if(nl == null) {
				throw new DOMException(DOMException.INDEX_SIZE_ERR, "Empty list of children");
			}
			Node n = (Node) nl.remove(index);
			if (n == null) {
				throw new DOMException(DOMException.INDEX_SIZE_ERR, "No node with that index");
			}
			return n;
		}
	}

	public boolean hasChildNodes() {
		synchronized(this.getTreeLock()) {
			ListSet nl = this.nodeList;
			return nl != null && !nl.isEmpty();
		}
	}

	public String getBaseURI() {
		Document document = this.getOwnerDocument();
		return document == null ? null : document.getBaseURI();
	}

	public NodeList getChildNodes() {
		synchronized(this.getTreeLock()) {
			ListSet nl = this.nodeList;
			return new NodeListImpl(nl == null ? Collections.EMPTY_LIST : nl);
		}
		
	}

	public Node getFirstChild() {
		synchronized(this.getTreeLock()) {
			ListSet nl = this.nodeList;
			try {
				return nl == null ? null : (Node) nl.get(0);
			} catch(IndexOutOfBoundsException iob) {
				return null;
			}
		}
	}

	public Node getLastChild() {
		synchronized(this.getTreeLock()) {
			ListSet nl = this.nodeList;
			try {
				return nl == null ? null : (Node) nl.get(nl.size() - 1);
			} catch(IndexOutOfBoundsException iob) {
				return null;
			}
		}
	}

	private Node getPreviousTo(Node node) {
		synchronized(this.getTreeLock()) {
			ListSet nl = this.nodeList;
			int idx = nl == null ? -1 : nl.indexOf(node);
			if(idx == -1) {
				throw new DOMException(DOMException.NOT_FOUND_ERR, "node not found");
			}
			try {
				return (Node) nl.get(idx-1);
			} catch(IndexOutOfBoundsException iob) {
				return null;
			}
		}
	}
	
	private Node getNextTo(Node node) {
		synchronized(this.getTreeLock()) {
			ListSet nl = this.nodeList;
			int idx = nl == null ? -1 : nl.indexOf(node);
			if(idx == -1) {
				throw new DOMException(DOMException.NOT_FOUND_ERR, "node not found");
			}
			try {
				return (Node) nl.get(idx+1);
			} catch(IndexOutOfBoundsException iob) {
				return null;
			}
		}
	}

	public Node getPreviousSibling() {
		NodeImpl parent = (NodeImpl) this.getParentNode();
		return parent == null ? null : parent.getPreviousTo(this);
	}

	public Node getNextSibling() {
		NodeImpl parent = (NodeImpl) this.getParentNode();
		return parent == null ? null : parent.getNextTo(this);
	}

	public Object getFeature(String feature, String version) {
		//TODO What should this do?
		return null;
	}

	private Map userData;

	//TODO: Inform handlers on cloning, etc.
	private List userDataHandlers;
	
	public Object setUserData(String key, Object data,
			UserDataHandler handler) {
		synchronized(this.getTreeLock()) {
			if(this.userDataHandlers == null) {
				this.userDataHandlers = new LinkedList();
			}
			this.userDataHandlers.add(handler);
			if(this.userData == null) {
				this.userData = new HashMap();
			}
			return this.userData.put(key, data);
		}
	}

	public Object getUserData(String key) {
		synchronized(this.getTreeLock()) {
			Map ud = this.userData;
			return ud == null ? null : ud.get(key);
		}
	}

	public abstract String getLocalName();

	public boolean hasAttributes() {
		return false;
	}

	public String getNamespaceURI() {
		return null;
	}

	public abstract String getNodeName();

	public abstract String getNodeValue() throws DOMException;

	private volatile String prefix;
	
	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) throws DOMException {
		this.prefix = prefix;
	}

	public abstract void setNodeValue(String nodeValue) throws DOMException;

	public abstract short getNodeType();

	public String getTextContent() throws DOMException {
		StringBuffer sb = new StringBuffer();
		synchronized(this.getTreeLock()) {
			Iterator i = this.nodeList.iterator();
			while(i.hasNext()) {
				Node node = (Node) i.next();
				short type = node.getNodeType();
				switch(type) {
				case Node.CDATA_SECTION_NODE:
				case Node.TEXT_NODE:
					String textContent = node.getTextContent();
					if(textContent != null) {
						sb.append(textContent);
					}
					break;
				default:
					break;
				}
			}
		}
		return sb.toString();
	}

	public void setTextContent(String textContent) throws DOMException {
		synchronized(this.getTreeLock()) {
			this.removeChildrenImpl(new TextFilter());
			if(textContent != null && !"".equals(textContent)) {
				TextImpl t = new TextImpl(textContent);
				t.setOwnerDocument(this.getOwnerDocument());
				this.nodeList.add(t);
			}
		}
	}
	
	protected void removeChildrenImpl(NodeFilter filter) {
		ListSet nl = this.nodeList;
		int len = nl.size();
		for(int i = len; --i >= 0;) {
			Node node = (Node) nl.get(i);
			if(filter.accept(node)) {
				nl.remove(i);
			}
		}
	}
	
	public Node insertAfter(Node newChild, Node refChild) {
		synchronized(this.getTreeLock()) {
			ListSet nl = this.nodeList;
			int idx = nl == null ? -1 : nl.indexOf(refChild);
			if(idx == -1) {
				throw new DOMException(DOMException.NOT_FOUND_ERR, "refChild not found");
			}
			nl.add(idx+1, newChild);
		}
		if(newChild instanceof NodeImpl) {
			((NodeImpl) newChild).addNotify(this);
		}
		return newChild;
	}
	
	public Text replaceAdjacentTextNodes(Text node, String textContent) {
		synchronized(this.getTreeLock()) {
			int idx = this.nodeList.indexOf(node);
			if(idx == -1) {
				throw new DOMException(DOMException.NOT_FOUND_ERR, "Node not a child");
			}
			int firstIdx = idx;
			List toDelete = new LinkedList();
			for(int adjIdx = idx; --adjIdx >= 0;) {
				Object child = this.nodeList.get(adjIdx);
				if(child instanceof Text) {
					firstIdx = adjIdx;
					toDelete.add(child);
				}
			}
			int length = this.nodeList.size();
			for(int adjIdx = idx; ++adjIdx < length;) {
				Object child = this.nodeList.get(adjIdx);
				if(child instanceof Text) {
					toDelete.add(child);
				}
			}
			this.nodeList.removeAll(toDelete);
			TextImpl textNode = new TextImpl(textContent); 
			this.nodeList.add(firstIdx, textNode);
			return textNode;
		}		
	}
	
	public Text replaceAdjacentTextNodes(Text node) {
		synchronized(this.getTreeLock()) {
			int idx = this.nodeList.indexOf(node);
			if(idx == -1) {
				throw new DOMException(DOMException.NOT_FOUND_ERR, "Node not a child");
			}
			StringBuffer textBuffer = new StringBuffer();
			int firstIdx = idx;
			List toDelete = new LinkedList();
			for(int adjIdx = idx; --adjIdx >= 0;) {
				Object child = this.nodeList.get(adjIdx);
				if(child instanceof Text) {
					firstIdx = adjIdx;
					toDelete.add(child);
					textBuffer.append(((Text) child).getNodeValue());
				}
			}
			int length = this.nodeList.size();
			for(int adjIdx = idx; ++adjIdx < length;) {
				Object child = this.nodeList.get(adjIdx);
				if(child instanceof Text) {
					toDelete.add(child);
					textBuffer.append(((Text) child).getNodeValue());
				}
			}
			this.nodeList.removeAll(toDelete);
			TextImpl textNode = new TextImpl(textBuffer.toString()); 
			this.nodeList.add(firstIdx, textNode);
			return textNode;
		}		
	}
		
	protected volatile Node parentNode;
	
	public Node getParentNode() {
		return this.parentNode;
	}
	
	public boolean isSameNode(Node other) {
		return this == other;
	}

	public boolean isSupported(String feature, String version) {
		return ("HTML".equals(feature) && version.compareTo("4.01") <= 0);
	}

	public String lookupNamespaceURI(String prefix) {
		return null;
	}

	public boolean equalAttributes(Node arg) {
		return false;
	}
	
	public boolean isEqualNode(Node arg) {
		return arg instanceof NodeImpl &&
			this.getNodeType() == arg.getNodeType() &&
			Objects.equals(this.getNodeName(), arg.getNodeName()) &&
			Objects.equals(this.getNodeValue(), arg.getNodeValue()) &&
			Objects.equals(this.getLocalName(), arg.getLocalName()) &&
			Objects.equals(this.nodeList, ((NodeImpl) arg).nodeList) &&
			this.equalAttributes(arg);
	}

	public boolean isDefaultNamespace(String namespaceURI) {
		return namespaceURI == null;
	}

	public String lookupPrefix(String namespaceURI) {
		return null;
	}

	public void normalize() {
		synchronized(this.getTreeLock()) {
			List textNodes = new LinkedList();
			Iterator i = this.nodeList.iterator();
			boolean prevText = false;
			while(i.hasNext()) {
				Node child = (Node) i.next();
				if(child.getNodeType() == Node.TEXT_NODE) {
					if(!prevText) {
						prevText = true;
						textNodes.add(child);
					}
				}
				else {
					prevText = false;
				}
			}
			i = textNodes.iterator();
			while(i.hasNext()) {
				Text text = (Text) i.next();
				this.replaceAdjacentTextNodes(text);
			}
		}		
	}
	
	public String toString() {
		return this.getNodeName();
	}
	
	public HtmlParserContext getHtmlParserContext() {
		Object doc = this.document;
		if(doc instanceof HTMLDocumentImpl) {
			return ((HTMLDocumentImpl) doc).getHtmlParserContext();
		}
		else {
			return null;
		}
	}
	
	public HtmlRendererContext getHtmlRendererContext() {
		Object doc = this.document;
		if(doc instanceof HTMLDocumentImpl) {
			return ((HTMLDocumentImpl) doc).getHtmlRendererContext();
		}
		else {
			return null;
		}
	}

	protected void addNotify(Node parent) {
		this.parentNode = parent;
	}
}
