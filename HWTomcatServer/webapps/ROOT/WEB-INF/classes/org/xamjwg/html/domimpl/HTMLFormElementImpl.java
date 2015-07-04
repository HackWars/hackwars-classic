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
 * Created on Jan 14, 2006
 */
package org.xamjwg.html.domimpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html2.HTMLCollection;
import org.w3c.dom.html2.HTMLFormElement;
import org.xamjwg.html.FormInput;
import org.xamjwg.html.HtmlParserContext;
import org.xamjwg.html.HtmlRendererContext;

public class HTMLFormElementImpl extends HTMLElementImpl implements
		HTMLFormElement {

	public HTMLFormElementImpl(String name, boolean noStyleSheet) {
		super(name, noStyleSheet);
	}

	public HTMLFormElementImpl(String name) {
		super(name);
	}

	public HTMLFormElementImpl() {
		super("FORM");
	}

	public Object namedItem(final String name) {
		try {
			this.visit(new NodeVisitor() {
				public void visit(Node node) {
					if(HTMLFormElementImpl.isInput(node)) {
						if(name.equals(((Element) node).getAttribute("name"))) {
							throw new StopVisitorException(node);
						}
					}
				}
			});
		} catch(StopVisitorException sve) {
			return sve.getTag();
		}
		return null;
	}
	
	public Object item(final int index) {
		try {
			this.visit(new NodeVisitor() {
				private int current = 0;
				
				public void visit(Node node) {
					if(HTMLFormElementImpl.isInput(node)) {
						if(this.current == index) {
							throw new StopVisitorException(node);
						}
						this.current++;
					}
				}
			});
		} catch(StopVisitorException sve) {
			return sve.getTag();
		}
		return null;
	}

	public HTMLCollection getElements() {
		return new ChildHTMLCollection(this, new InputFilter(), this.getTreeLock());
	}

	public int getLength() {
		return new ChildHTMLCollection(this, new InputFilter(), this.getTreeLock()).getLength();
	}

	public String getName() {
		return this.getAttribute("name");
	}

	public void setName(String name) {
		this.setAttribute("name", name);
	}

	public String getAcceptCharset() {
		return this.getAttribute("acceptCharset");
	}

	public void setAcceptCharset(String acceptCharset) {
		this.setAttribute("acceptCharset", acceptCharset);
	}

	public String getAction() {
		return this.getAttribute("action");
	}

	public void setAction(String action) {
		this.setAttribute("action", action);
	}

	public String getEnctype() {
		return this.getAttribute("enctype");
	}

	public void setEnctype(String enctype) {
		this.setAttribute("enctype", enctype);
	}

	public String getMethod() {
		String method = this.getAttribute("method");
		if(method == null) {
			method = "GET";
		}
		return method;
	}

	public void setMethod(String method) {
		this.setAttribute("method", method);
	}

	public String getTarget() {
		return this.getAttribute("target");
	}

	public void setTarget(String target) {
		this.setAttribute("target", target);
	}

	public void submit() {
		this.submit(null);
	}

	final void submit(final HTMLInputElementImpl submitButton) {
		HtmlRendererContext context = this.getHtmlRendererContext();
		if(context != null) {
			final ArrayList formInputs = new ArrayList();
			this.visit(new NodeVisitor() {
				public void visit(Node node) {
					if(node instanceof HTMLElementImpl) {
						FormInput fi = ((HTMLElementImpl) node).getFormInput(submitButton);
						if(fi != null) {
							if(fi.getName() == null) {
								throw new IllegalStateException("Form input does not have a name: " + node);
							}
							formInputs.add(fi);
						}
					}
				}
			});
			FormInput[] fia = (FormInput[]) formInputs.toArray(FormInput.EMPTY_ARRAY);
			String href = this.getAction();
			if(href == null) {
				href = this.getBaseURI();
			}
			try {
				URL url = this.getFullURL(href);
				context.submitForm(this.getMethod(), url, this.getTarget(), this.getEnctype(), fia);
			} catch(MalformedURLException mfu) {
				this.warn("submit()", mfu);
			}
		}
	}

	public void reset() {
		// TODO Auto-generated method stub
	}

	static boolean isInput(Node node) {
		String name = node.getNodeName().toLowerCase();
		return name.equals("input") || name.equals("textarea") || name.equals("select");
	}
	
	private class InputFilter implements NodeFilter {
		/* (non-Javadoc)
		 * @see org.xamjwg.html.domimpl.NodeFilter#accept(org.w3c.dom.Node)
		 */
		public boolean accept(Node node) {
			return HTMLFormElementImpl.isInput(node);
		}
	}
}
