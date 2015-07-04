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

import org.xamjwg.html.FormInput;
import org.xamjwg.html.renderer.HtmlLength;
import org.xamjwg.util.Objects;
import org.xamjwg.util.Strings;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSS2Properties;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.html2.HTMLElement;
import com.steadystate.css.parser.CSSOMParser;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

public class HTMLElementImpl extends ElementImpl implements HTMLElement, RenderableContext, CSS2PropertiesContext {	
	private final boolean noStyleSheet;
	
	public HTMLElementImpl(String name, boolean noStyleSheet) {
		super(name);
		this.noStyleSheet = noStyleSheet;
	}
	public HTMLElementImpl(String name) {
		super(name);
		this.noStyleSheet = false;
	}
	private volatile CSS2PropertiesImpl styleDeclarationState;
	
	public CSS2PropertiesImpl getStyleNoCreate() {
		return this.styleDeclarationState;
	}
	
	private final void forgetStyle() {
		synchronized(this.getTreeLock()) {
			this.styleDeclarationState = null;
		}
	}
	
	public CSS2PropertiesImpl getStyle() {
		CSS2PropertiesImpl sds = this.styleDeclarationState;
		if(sds == null) {
			boolean firstSet = false;
			synchronized(this.getTreeLock()) {
				if(this.styleDeclarationState == null) {
					sds = new CSS2PropertiesImpl(this);
					this.styleDeclarationState = sds;
					firstSet = true;
				}
			}
			if(firstSet && !this.noStyleSheet) {
				String style = this.getAttribute("style");
				if(style != null && !"".equals(style)) {
					CSSOMParser parser = new CSSOMParser();
					InputSource inputSource = this.getCssInputSourceForDecl(style);
					try {
						CSSStyleDeclaration sd = parser.parseStyleDeclaration(inputSource);	
						CSS2PropertiesImpl cssprops = (CSS2PropertiesImpl) this.getStyleNoUpdate();
						cssprops.addStyleDeclaration(sd);
					} catch(Exception err) {
						this.warn("Unable to parse style declaration", err);
					}
				}
				this.addStyleSheetDeclarations();
			}
		}
		return sds;
	}
	
	public CSS2Properties getStyleNoUpdate() {
		CSS2PropertiesImpl sds = this.styleDeclarationState;
		if(sds == null) {
			synchronized(this.getTreeLock()) {
				if(this.styleDeclarationState == null) {
					sds = new CSS2PropertiesImpl(this);
					this.styleDeclarationState = sds;
				}
			}
		}
		return sds;
	}
	
	public void setStyle(Object value) {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Cannot set style property");
	}	
	public String getClassName() {
		return this.getAttribute("class");
	}
	public void setClassName(String className) {
		this.setAttribute("class", className);
	}
	
	public String getCharset() {
		return this.getAttribute("charset");
	}

	public void setCharset(String charset) {
		this.setAttribute("charset", charset);
	}

	public void warn(String message, Throwable err) {
		Object doc = this.document;
		if(doc instanceof HTMLDocumentImpl) {
			((HTMLDocumentImpl) doc).getHtmlParserContext().warn(message, err);
		}
	}

	public void warn(String message) {
		Object doc = this.document;
		if(doc instanceof HTMLDocumentImpl) {
			((HTMLDocumentImpl) doc).getHtmlParserContext().warn(message);
		}
	}

	protected int getAttributeAsInt(String name, int defaultValue) {
		String value = this.getAttribute(name);
		try {
			return Integer.parseInt(value);
		} catch(Exception err) {
			this.warn("Bad integer", err);
			return defaultValue;
		}
	}
	
	protected boolean getAttributeAsBoolean(String name, boolean defaultValue) {
		String value = this.getAttribute(name);
		return name.equalsIgnoreCase(value);
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContext#getHeightLength()
	 */
	public HtmlLength getHeightLength() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContext#getWidthLength()
	 */
	public HtmlLength getWidthLength() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContext#getAlignmentX()
	 */
	public float getAlignmentX() {
		return 0.5f;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContext#getAlignmentY()
	 */
	public float getAlignmentY() {
		return 0.5f;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContext#getFullURL(java.lang.String)
	 */
	public URL getFullURL(String spec) throws MalformedURLException {
		Object doc = this.document;
		if(doc instanceof HTMLDocumentImpl) {
			return ((HTMLDocumentImpl) doc).getFullURL(spec);
		}
		else {
			return new java.net.URL(spec);
		}
	}
	
	protected void assignAttributeField(String normalName, String value) {
		if(this.noStyleSheet) {
			super.assignAttributeField(normalName, value);
		}
		else {
			if("CLASS".equals(normalName)) {
				this.forgetStyle();
			}
			else if("STYLE".equals(normalName)) {
				this.forgetStyle();
			}
			else if("ID".equals(normalName)) {
				super.assignAttributeField(normalName, value);
				this.forgetStyle();
			}
			else {
				super.assignAttributeField(normalName, value);
			}
		}
	}
	protected final InputSource getCssInputSourceForDecl(String text) {
		java.io.Reader reader = new StringReader("{" + text + "}");
		InputSource is = new InputSource(reader);
		return is;
	}
	
	
	protected final void addStyleSheetDeclarations() {
		Node pn = this.parentNode;
		if(pn == null) {
			// do later
			return;
		}
		String classNames = this.getClassName();
		if(classNames != null) {
			String id = this.getId();
			String elementName = this.getTagName();
			CSS2PropertiesImpl style = (CSS2PropertiesImpl) this.getStyleNoUpdate();
			String[] classNameArray = Strings.split(classNames);
			for(int i = classNameArray.length; --i >= 0;) {
				String className = classNameArray[i];
				Collection sds = this.findStyleDeclarations(elementName, id, className);
				if(style != null && sds != null) {
					Iterator sdsi = sds.iterator();
					while(sdsi.hasNext()) {
						CSSStyleDeclaration sd = (CSSStyleDeclaration) sdsi.next();
						style.addStyleDeclaration(sd);
					}
				}					
			}
		}
		else {
			String id = this.getId();
			String elementName = this.getTagName();
			CSS2PropertiesImpl style = (CSS2PropertiesImpl) this.getStyleNoUpdate();
			Collection sds = this.findStyleDeclarations(elementName, id, null);
			if(style != null && sds != null) {
				Iterator sdsi = sds.iterator();
				while(sdsi.hasNext()) {
					CSSStyleDeclaration sd = (CSSStyleDeclaration) sdsi.next();
					style.addStyleDeclaration(sd);
				}
			}					
		}
	}
	
	protected final Collection findStyleDeclarations(String elementName, String id, String className) {
		HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;
		if(doc == null) {
			return null;
		}
		StyleSheetAggregator ssa = doc.getStyleSheetAggregator();
		return ssa.getStyleDeclarations(this, elementName, id, className);
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContext#onMouseClick(java.awt.event.MouseEvent, int, int)
	 */
	public boolean onMouseClick(MouseEvent event, int x, int y, boolean propagate) {
		if(propagate) {
			Object parent = this.getParentNode();
			if(parent instanceof HTMLElementImpl) {
				return ((HTMLElementImpl) parent).onMouseClick(event, x, y, propagate);
			}
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContext#onMousePressed(java.awt.event.MouseEvent, int, int)
	 */
	public boolean onMousePressed(MouseEvent event, int x, int y, boolean propagate) {
		if(propagate) {
			Object parent = this.getParentNode();
			if(parent instanceof HTMLElementImpl) {
				return ((HTMLElementImpl) parent).onMousePressed(event, x, y, propagate);
			}
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContext#onMouseReleased(java.awt.event.MouseEvent, int, int)
	 */
	public boolean onMouseReleased(MouseEvent event, int x, int y, boolean propagate) {
		if(propagate) {
			Object parent = this.getParentNode();
			if(parent instanceof HTMLElementImpl) {
				return ((HTMLElementImpl) parent).onMouseReleased(event, x, y, propagate);
			}
		}
		return true;
	}
	public boolean onMouseDisarmed(MouseEvent event, boolean propagate) {
		if(propagate) {
			Object parent = this.getParentNode();
			if(parent instanceof HTMLElementImpl) {
				return ((HTMLElementImpl) parent).onMouseDisarmed(event, propagate);
			}
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.RenderableContext#onClick(java.awt.AWTEvent, boolean)
	 */
	public boolean onClick(AWTEvent event, boolean propagate) {
		if(propagate) {
			Object parent = this.getParentNode();
			if(parent instanceof HTMLElementImpl) {
				return ((HTMLElementImpl) parent).onClick(event, propagate);
			}
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.RenderableContext#onEnterPressed(java.awt.AWTEvent, boolean)
	 */
	public boolean onEnterPressed(AWTEvent event, boolean propagate) {
		if(propagate) {
			Object parent = this.getParentNode();
			if(parent instanceof HTMLElementImpl) {
				return ((HTMLElementImpl) parent).onEnterPressed(event, propagate);
			}
		}
		return true;
	}	
	
	public void repaint() {
		this.repaint(this);
	}
	
	protected void repaint(RenderableContext context) {
		ContainingBlockContext r = this.containingBlockContext;
		if(r != null) {
			r.repaint(this);
		}
		else {
			Object parent = this.getParentNode();
			if(parent instanceof HTMLElementImpl) {
				((HTMLElementImpl) parent).repaint(context);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContext#isEqualOrDescendentOf(org.xamjwg.html.renderer.RenderableContext)
	 */
	public final boolean isEqualOrDescendentOf(RenderableContext otherContext) {
		if(otherContext == this) {
			return true;
		}
		Object parent = this.getParentNode();
		if(parent instanceof HTMLElementImpl) {
			return ((HTMLElementImpl) parent).isEqualOrDescendentOf(otherContext);
		}
		else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContext#getDocumentItem(java.lang.String)
	 */
	public Object getDocumentItem(String name) {
		org.w3c.dom.Document document = this.document;
		return document == null ? null : document.getUserData(name);
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContext#setDocumentItem(java.lang.String, java.lang.Object)
	 */
	public void setDocumentItem(String name, Object value) {
		org.w3c.dom.Document document = this.document;
		if(document == null) {
			return;
		}
		document.setUserData(name, value, null);
	}	
	
	protected FormInput getFormInput(HTMLInputElementImpl submit) {
		// Override in input elements
		return null;
	}
	
	private boolean classMatch(String classTL) {
		String classNames = this.getClassName();
		if(classNames == null) {
			return classTL == null;
		}
		StringTokenizer tok = new StringTokenizer(classNames, " \t\r\n");
		while(tok.hasMoreTokens()) {
			String token = tok.nextToken();
			if(token.toLowerCase().equals(classTL)) {
				return true;
			}
		}
		return false;
	}
	
	public HTMLElementImpl getAncestorWithClass(String elementTL, String classTL) {
		Object nodeObj = this.getParentNode();
		if(nodeObj instanceof HTMLElementImpl) {
			HTMLElementImpl parentElement = (HTMLElementImpl) nodeObj;
			String pelementTL = parentElement.getTagName().toLowerCase();
			if(("*".equals(elementTL) || elementTL.equals(pelementTL)) && parentElement.classMatch(classTL)) {
				return parentElement;
			}
			return parentElement.getAncestorWithClass(elementTL, classTL);
		}
		else {
			return null;
		}
	}

	public HTMLElementImpl getAncestorWithId(String elementTL, String idTL) {
		Object nodeObj = this.getParentNode();
		if(nodeObj instanceof HTMLElementImpl) {
			HTMLElementImpl parentElement = (HTMLElementImpl) nodeObj;
			String pelementTL = parentElement.getTagName().toLowerCase();
			String pid = parentElement.getId();
			String pidTL = pid == null ? null : pid.toLowerCase();
			if(("*".equals(elementTL) || elementTL.equals(pelementTL)) && idTL.equals(pidTL)) {
				return parentElement;
			}
			return parentElement.getAncestorWithId(elementTL, idTL);
		}
		else {
			return null;
		}
	}

	public HTMLElementImpl getAncestor(String elementTL) {
		Object nodeObj = this.getParentNode();
		if(nodeObj instanceof HTMLElementImpl) {
			HTMLElementImpl parentElement = (HTMLElementImpl) nodeObj;
			String pelementTL = parentElement.getTagName().toLowerCase();
			if("*".equals(elementTL) || elementTL.equals(pelementTL)) {
				return parentElement;
			}
			return parentElement.getAncestor(elementTL);
		}
		else {
			return null;
		}
	}
}
