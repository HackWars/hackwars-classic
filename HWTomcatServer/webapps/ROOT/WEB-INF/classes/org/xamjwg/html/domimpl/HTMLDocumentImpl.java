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
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.html2.HTMLElement;
import org.w3c.dom.html2.HTMLCollection;
import org.w3c.dom.html2.HTMLDocument;
import org.xamjwg.util.*;
import org.xamjwg.html.*;
import org.xamjwg.html.io.*;
import org.xamjwg.html.parser.HtmlParser;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;


import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class HTMLDocumentImpl extends NodeImpl implements HTMLDocument {
	private final ElementFactory factory;
	private final HtmlParserContext context;
	private WritableLineReader reader;
	private Thread openThread;
	
	public HTMLDocumentImpl(HtmlParserContext context) {
		this.factory = ElementFactory.getInstance();
		this.context = context;
	}

	public HTMLDocumentImpl(HtmlParserContext context, WritableLineReader reader, String documentURI) {
		this.factory = ElementFactory.getInstance();
		this.context = context;
		this.reader = reader;
		this.documentURI = documentURI;
	}

	private Map elementsById = new WeakValueHashMap();
	
	/**
	 * Caller should synchronize on document.
	 */
	void setElementById(String id, Element element) {
		this.elementsById.put(id, element);
	}
	
	void removeElementById(String id) {
		this.elementsById.remove(id);
	}
	
	private volatile String baseURI;

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.NodeImpl#getbaseURI()
	 */
	public String getBaseURI() {
		String buri = this.baseURI;
		return buri == null ? this.documentURI : buri;
	}

	public void setBaseURI(String value) {
		this.baseURI = value;
	}

	public String getTextContent() throws DOMException {
		return null;
	}
	
	public void setTextContent(String textContent) throws DOMException {
		// NOP
	}

	private String title;
	
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private String referrer;
	
	public String getReferrer() {
		return this.referrer;
	}

	private String domain;
	
	public String getDomain() {
		return this.domain;
	}

	private String url;
	
	public String getURL() {
		return this.url;
	}

	private HTMLElement body;
	
	public HTMLElement getBody() {
		return this.body;
	}

	public void setBody(HTMLElement body) {
		this.body = body;
	}

	private HTMLCollection images;
	private HTMLCollection applets;
	private HTMLCollection links;
	private HTMLCollection forms;
	private HTMLCollection anchors;
	
	public HTMLCollection getImages() {
		Object lock = this.getTreeLock();
		synchronized(lock) {
			if(this.images == null) {
				this.images = new FilteredHTMLCollectionImpl(this.elementsById, new ImageFilter(), lock);
			}
			return this.images;
		}
	}

	public HTMLCollection getApplets() {
		Object lock = this.getTreeLock();
		synchronized(lock) {
			if(this.applets == null) {
				this.applets = new FilteredHTMLCollectionImpl(this.elementsById, new AppletFilter(), lock);
			}
			return this.applets;
		}
	}

	public HTMLCollection getLinks() {
		Object lock = this.getTreeLock();
		synchronized(lock) {
			if(this.links == null) {
				this.links = new FilteredHTMLCollectionImpl(this.elementsById, new LinkFilter(), lock);
			}
			return this.links;
		}
	}

	public HTMLCollection getForms() {
		Object lock = this.getTreeLock();
		synchronized(lock) {
			if(this.forms == null) {
				this.forms = new FilteredHTMLCollectionImpl(this.elementsById, new FormFilter(), lock);
			}
			return this.forms;
		}
	}

	public HTMLCollection getAnchors() {
		Object lock = this.getTreeLock();
		synchronized(lock) {
			if(this.anchors == null) {
				this.anchors = new FilteredHTMLCollectionImpl(this.elementsById, new AnchorFilter(), lock);
			}
			return this.anchors;
		}
	}

	public String getCookie() {
		return this.context.getCookie();
	}

	public void setCookie(String cookie) throws DOMException {
		this.context.setCookie(cookie);
	}
	
	public void open() {
		synchronized(this.getTreeLock()) {
			if(this.reader != null) {
				if(this.reader instanceof LocalWritableLineReader) {
					try {
						this.reader.close();
					} catch(IOException ioe) {
						//ignore
					}
					this.reader = null;
				}
				else {
					// Do not close http/file documents in progress.
					return;
				}
			}
			this.removeAllChildren();
			try {
				this.reader = new LocalWritableLineReader(new EmptyInputStream(), "UTF-8");
			} catch(UnsupportedEncodingException ueo) {
				// Not possible
				throw new IllegalStateException(ueo.getMessage());
			}
			if(this.openThread == null) {
				this.openThread = new Thread(new OpenThreadRunnable(), "openThread-" + this.hashCode());
				this.openThread.start();
			}
			this.getTreeLock().notify();
		}
	}

	public void load(String charset) throws IOException,SAXException,UnsupportedEncodingException {
		WritableLineReader reader;
		synchronized(this.getTreeLock()) {
			this.removeAllChildren();
			this.setTitle(null);
			this.setBaseURI(null);
			this.styleSheets.clear();
			this.styleSheetAggregator = null;
			reader = this.reader;
		}
		if(reader != null) {
			try {
				ErrorHandler errorHandler = new LocalErrorHandler(this.context);
				String systemId = this.documentURI;
				String publicId = systemId;
				HtmlParser parser = new HtmlParser(this.context, this, errorHandler, publicId, systemId);
				parser.parse(reader);
			} finally {
				try {
					reader.close();
				} catch(Exception err) {
					this.getHtmlParserContext().warn("load(): Unable to close stream", err);
				}
				synchronized(this.getTreeLock()) {
					this.reader = null;
				}
			}
		}
	}
	
	public void close() {
		synchronized(this.getTreeLock()) {
			if(this.reader instanceof LocalWritableLineReader) {
				try {
					this.reader.close();
				} catch(java.io.IOException ioe) {
					// ignore
				}
				this.reader = null;
			}
			else {
				// do nothing
			}
			//TODO: cause it to render
		}
	}

	public void write(String text) {
		synchronized(this.getTreeLock()) {
			if(this.reader != null) {
				try {
					this.reader.write(text);
				} catch(IOException ioe) {
					//ignore
				}
			}
		}
	}

	public void writeln(String text) {
		synchronized(this.getTreeLock()) {
			if(this.reader != null) {
				try {
					this.reader.write(text + "\r\n");
				} catch(IOException ioe) {
					//ignore
				}
			}
		}
	}

	public NodeList getElementsByName(String elementName) {
		return this.getNodeList(new ElementNameFilter(elementName));
	}
	
	private DocumentType doctype;
	
	public DocumentType getDoctype() {
		return this.doctype;
	}

	public void setDoctype(DocumentType doctype) {
		this.doctype = doctype;
	}
	
	public Element getDocumentElement() {
		synchronized(this.getTreeLock()) {
			ListSet nl = this.nodeList;
			if(nl != null) {
				Iterator i = nl.iterator();
				while(i.hasNext()) {
					Object node = i.next();
					if(node instanceof Element) {
						return (Element) node;
					}
				}
			}
			return null;
		}
	}

	public Element createElement(String tagName)
			throws DOMException {
		return this.factory.createElement(this, tagName);
	}
	
	/* (non-Javadoc)
	 * @see org.w3c.dom.Document#createDocumentFragment()
	 */
	public DocumentFragment createDocumentFragment() {
		//TODO: According to documentation, when a document
		//fragment is added to a node, its children are added,
		//not itself.
		DocumentFragmentImpl node = new DocumentFragmentImpl();
		node.setOwnerDocument(this);
		return node;
	}

	public Text createTextNode(String data) {
		TextImpl node = new TextImpl(data);
		node.setOwnerDocument(this);
		return node;
	}

	public Comment createComment(String data) {
		CommentImpl node = new CommentImpl(data);
		node.setOwnerDocument(this);
		return node;
	}

	public CDATASection createCDATASection(String data)
			throws DOMException {
		CDataSectionImpl node = new CDataSectionImpl(data);
		node.setOwnerDocument(this);
		return node;
	}

	public ProcessingInstruction createProcessingInstruction(
			String target, String data) throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "HTML document");
	}

	public Attr createAttribute(String name) throws DOMException {
		return new AttrImpl(name);
	}

	public EntityReference createEntityReference(String name)
			throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "HTML document");
	}

	public NodeList getElementsByTagName(String tagname) {
		return this.getNodeList(new TagNameFilter(tagname));
	}

	public Node importNode(Node importedNode, boolean deep)
			throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Not implemented");
	}

	public Element createElementNS(String namespaceURI,
			String qualifiedName) throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "HTML document");
	}

	public Attr createAttributeNS(String namespaceURI,
			String qualifiedName) throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "HTML document");
	}

	public NodeList getElementsByTagNameNS(String namespaceURI,
			String localName) {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "HTML document");
	}

	public Element getElementById(String elementId) {
		synchronized(this.getTreeLock()) {
			return (Element) this.elementsById.get(elementId);
		}
	}

	private String inputEncoding;
	
	public String getInputEncoding() {
		return this.inputEncoding;
	}
	
	private String xmlEncoding;

	public String getXmlEncoding() {
		return this.xmlEncoding;
	}

	private boolean xmlStandalone;
	
	public boolean getXmlStandalone() {
		return this.xmlStandalone;
	}

	public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
		this.xmlStandalone = xmlStandalone;
	}

	private String xmlVersion = null;
	
	public String getXmlVersion() {
		return this.xmlVersion;
	}

	public void setXmlVersion(String xmlVersion) throws DOMException {
		this.xmlVersion = xmlVersion;
	}

	private boolean strictErrorChecking = true;
	
	public boolean getStrictErrorChecking() {
		return this.strictErrorChecking;
	}

	public void setStrictErrorChecking(boolean strictErrorChecking) {
		this.strictErrorChecking = strictErrorChecking;
	}

	private String documentURI;
	
	public String getDocumentURI() {
		return this.documentURI;
	}

	public void setDocumentURI(String documentURI) {
		this.documentURI = documentURI;
	}

	public Node adoptNode(Node source) throws DOMException {
		if(source instanceof NodeImpl) {
			NodeImpl node = (NodeImpl) source;
			node.setOwnerDocument(this, true);
			return node;
		}
		else {
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Invalid Node implementation");
		}
	}

	private DOMConfiguration domConfig;
	
	public DOMConfiguration getDomConfig() {
		synchronized(this.getTreeLock()) {
			if(this.domConfig == null) {
				this.domConfig = new DOMConfigurationImpl();
			}
			return this.domConfig;
		}
	}

	public void normalizeDocument() {
		//TODO: Normalization options from domConfig
		synchronized(this.getTreeLock()) {
			this.visitImpl(new NodeVisitor() {
				public void visit(Node node) {
					node.normalize();
				}
			});
		}
	}

	public Node renameNode(Node n, String namespaceURI,
			String qualifiedName) throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "No renaming");
	}

	private DOMImplementation domImplementation;
	
	/* (non-Javadoc)
	 * @see org.w3c.dom.Document#getImplementation()
	 */
	public DOMImplementation getImplementation() {
		synchronized(this.getTreeLock()) {
			if(this.domImplementation == null) {
				this.domImplementation = new DOMImplementationImpl(this.context);
			}
			return this.domImplementation;
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.NodeImpl#getLocalName()
	 */
	public String getLocalName() {
		// Always null for document
		return null;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.NodeImpl#getNodeName()
	 */
	public String getNodeName() {
		return "#document";
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.NodeImpl#getNodeType()
	 */
	public short getNodeType() {
		return Node.DOCUMENT_NODE;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.NodeImpl#getNodeValue()
	 */
	public String getNodeValue() throws DOMException {
		// Always null for document
		return null;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.NodeImpl#setNodeValue(java.lang.String)
	 */
	public void setNodeValue(String nodeValue) throws DOMException {
		throw new DOMException(DOMException.INVALID_MODIFICATION_ERR, "Cannot set node value of document");
	}
	
	public final HtmlParserContext getHtmlParserContext() {
		return this.context;
	}
	
	private HtmlRendererContext rendererContext;
	
	public final HtmlRendererContext getHtmlRendererContext() {
		return this.rendererContext;
	}
	
	public final void setHtmlRendererContext(HtmlRendererContext ctx) {
		this.rendererContext = ctx;
	}
	
	final URL getFullURL(String uri) {
		try {
			URL documentURL = new URL(this.getBaseURI());
			return new URL(documentURL, uri);
		} catch(MalformedURLException mfu) {
			this.context.error("Unable to create URL for: " + uri, mfu);
			return null;
		}
	}	

	private final Collection styleSheets = new LinkedList();
	
	final void addStyleSheet(CSSStyleSheet ss) {
		synchronized(this.getTreeLock()) {
			this.styleSheets.add(ss);
			this.styleSheetAggregator = null;
		}
	}
	
	private StyleSheetAggregator styleSheetAggregator = null;
	
	final StyleSheetAggregator getStyleSheetAggregator() {
		synchronized(this.getTreeLock()) {
			StyleSheetAggregator ssa = this.styleSheetAggregator;
			if(ssa == null) {
				ssa = new StyleSheetAggregator(this);
				try {
					ssa.addStyleSheets(this.styleSheets);
				} catch(MalformedURLException mfu) {
					this.getHtmlParserContext().warn("getStyleSheetAggregator()", mfu);
				}
				this.styleSheetAggregator = ssa;
			}
			return ssa;
		}		
	}
	
	private class ImageFilter implements NodeFilter {
		public boolean accept(Node node) {
			return "IMG".equals(node.getNodeName());
		}
	}

	private class AppletFilter implements NodeFilter {
		public boolean accept(Node node) {
			//TODO: "OBJECT" elements that are applets too.
			return "APPLET".equals(node.getNodeName());
		}
	}

	private class LinkFilter implements NodeFilter {
		public boolean accept(Node node) {
			String nodeName = node.getNodeName();
			return "LINK".equals(nodeName);
		}
	}

	private class AnchorFilter implements NodeFilter {
		public boolean accept(Node node) {
			String nodeName = node.getNodeName();
			return "A".equals(nodeName) || "ANCHOR".equals(nodeName);			
		}
	}
	
	private class FormFilter implements NodeFilter {
		public boolean accept(Node node) {
			String nodeName = node.getNodeName();
			return "FORM".equals(nodeName);
		}
	}

	private class ElementNameFilter implements NodeFilter {
		private final String name;
		
		public ElementNameFilter(String name) {
			this.name = name;
		}
		
		public boolean accept(Node node) {
			return (node instanceof Element) &&
				this.name.equals(((Element) node).getAttribute("name"));
		}
	}
	
	private class TagNameFilter implements NodeFilter {
		private final String name;
		
		public TagNameFilter(String name) {
			this.name = name.toUpperCase();
		}
		
		public boolean accept(Node node) {
			return (node instanceof Element) &&
				this.name.equals(((Element) node).getNodeName());
		}
	}
	
	private class OpenThreadRunnable implements Runnable {
		public void run() {
			for(;;) {
				Object lock = HTMLDocumentImpl.this.getTreeLock();
				try {
					synchronized(lock) {
						while(HTMLDocumentImpl.this.reader == null) {
							lock.wait();
						}
					}
					HTMLDocumentImpl.this.load("UTF-8");
				} catch(Throwable err) {
					HTMLDocumentImpl.this.context.warn("OpenThreadRunnable.run()", err);
				}
			}
		}
	}

	/**
	 * Tag class.
	 * @author J. H. S.
	 */
	private static class LocalWritableLineReader extends WritableLineReader {

		/**
		 * @param stream
		 * @param charset
		 * @throws UnsupportedEncodingException
		 */
		public LocalWritableLineReader(InputStream stream, String charset) throws UnsupportedEncodingException {
			super(stream, charset);
		}

		/**
		 * @param reader
		 */
		public LocalWritableLineReader(LineNumberReader reader) {
			super(reader);
		}

		/**
		 * @param reader
		 */
		public LocalWritableLineReader(Reader reader) {
			super(reader);
		}
	}
}
