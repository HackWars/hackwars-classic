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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.html2.HTMLScriptElement;
import org.xamjwg.html.*;
import org.xamjwg.html.parser.HtmlParser;
import org.mozilla.javascript.*;

public class HTMLScriptElementImpl extends HTMLElementImpl implements
		HTMLScriptElement {
	public HTMLScriptElementImpl() {
		super("SCRIPT", true);
	}

	private String text;
	
	public String getText() {
		String t = this.text;
		if(t == null) {
			return this.getRawInnerText(true);
		}
		else {
			return t;
		}
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHtmlFor() {
		return this.getAttribute("htmlFor");
	}

	public void setHtmlFor(String htmlFor) {
		this.setAttribute("htmlFor", htmlFor);
	}

	public String getEvent() {
		return this.getAttribute("event");
	}

	public void setEvent(String event) {
		this.setAttribute("event", event);
	}

	private boolean defer;
	
	public boolean getDefer() {
		return this.defer;
	}

	public void setDefer(boolean defer) {
		this.defer = defer;
	}

	private String src;
	
	public String getSrc() {
		return this.src;
	}

	public void setSrc(String src) {
		this.setAttribute("src", src);
	}

	public String getType() {
		return this.getAttribute("type");
	}

	public void setType(String type) {
		this.setAttribute("type", type);
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.ElementImpl#assignAttributeField(java.lang.String, java.lang.String)
	 */
	protected void assignAttributeField(String normalName, String value) {
		super.assignAttributeField(normalName, value);
		if("SRC".equals(normalName)) {
			this.src = value;
		}
	}

	protected final void addNotify(Node parent) {
		super.addNotify(parent);
		/*String text;
		String scriptURI;
		int baseLineNumber;
		String src = this.getSrc();
		Document doc = this.document;
		if(!(doc instanceof HTMLDocumentImpl)) {
			throw new IllegalStateException("no valid document");
		}
		if(src == null) {
			text = this.getText();
			scriptURI = doc.getBaseURI();
			baseLineNumber = 1; //TODO: Line number of inner text??
		}
		else {
			HtmlParserContext context = this.getHtmlParserContext();
			if(context == null) {
				throw new IllegalStateException("no HTML context");
			}
			final HttpRequest request = context.createHttpRequest();
			java.net.URL scriptURL = ((HTMLDocumentImpl) doc).getFullURL(src);
			scriptURI = scriptURL == null ? src : scriptURL.toExternalForm();
			// Perform a synchronous request
			request.open("GET", scriptURI, false);
			int status = request.getStatus();
			if(status != 200 && status != 0) {
				// Note: status 0 could be a file.
				throw new IllegalStateException("Error status: " + request.getStatus());
			}
			text = request.getResponseText();
			baseLineNumber = 1;
		}
		Context ctx = (Context) Context.getCurrentContext();
		if(ctx == null) {
			throw new IllegalStateException("No Javascript context associated with current thread");
		}
		Scriptable scope = (Scriptable) doc.getUserData(HtmlParser.SCOPE_KEY);
		if(ctx == null || scope == null) {
			throw new IllegalStateException("Scriptable (scope) instance was expected to be keyed as UserData to document using " + HtmlParser.SCOPE_KEY);
		}
		try {
			ctx.evaluateString(scope, text, scriptURI, baseLineNumber, null);
		} catch(EcmaError ecmaError) {
			this.warn("Javascript error at " + ecmaError.getSourceName() + ":" + ecmaError.getLineNumber() + ": " + ecmaError.getMessage());
		} catch(Throwable err) {
			this.warn("Unable to evaluate Javascript code", err);
		}*/
		this.warn("Unable to evaluate Javascript code");
	}
}
