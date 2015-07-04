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
package org.xamjwg.html.domimpl;

import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.html2.HTMLLinkElement;
import org.xamjwg.html.HtmlParserContext;
import org.xamjwg.html.HtmlRendererContext;
import org.xamjwg.html.HttpRequest;
import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.parser.CSSOMParser;

public class HTMLLinkElementImpl extends HTMLElementImpl implements
		HTMLLinkElement {	
	public HTMLLinkElementImpl(String name) {
		super(name, "LINK".equalsIgnoreCase(name));
	}

	private boolean disabled;
	
	public boolean getDisabled() {
		return this.disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	private String href;

	public String getHref() {
		return this.href;
	}

	public void setHref(String href) {
		this.setAttribute("href", href);
	}

	public String getHreflang() {
		return this.getAttribute("hreflang");
	}

	public void setHreflang(String hreflang) {
		this.setAttribute("hreflang", hreflang);
	}

	public String getMedia() {
		return this.getAttribute("media");
	}

	public void setMedia(String media) {
		this.setAttribute("media", media);
	}

	public String getRel() {
		return this.getAttribute("rel");
	}

	public void setRel(String rel) {
		this.setAttribute("rel", rel);
	}

	public String getRev() {
		return this.getAttribute("rev");
	}

	public void setRev(String rev) {
		this.setAttribute("rev", rev);
	}

	public String getTarget() {
		return this.getAttribute("target");
	}

	public void setTarget(String target) {
		this.setAttribute("target", target);
	}

	public String getType() {
		return this.getAttribute("type");
	}

	public void setType(String type) {
		this.setAttribute("type", type);
	}

	protected void assignAttributeField(String normalName, String value) {
		if("HREF".equals(normalName)) {
			this.href = value;
		}
		else {
			super.assignAttributeField(normalName, value);
		}
	}
	
	protected void addNotify(Node parent) {
		super.addNotify(parent);
		String rel = this.getAttribute("rel");
		if(rel != null && rel.toLowerCase().indexOf("stylesheet") != -1) {		
			HTMLDocumentImpl doc = (HTMLDocumentImpl) this.getOwnerDocument();
			try {
				CSSStyleSheet sheet = CSSUtilities.parse(this.href, doc, doc.getBaseURI());
				if(sheet != null) {
					doc.addStyleSheet(sheet);
				}
			} catch(MalformedURLException mfe) {
				this.warn("addNotify(): Cannot parse CSS", mfe);
			}
		}
	}
	
	public boolean onMousePressed(MouseEvent event, int x, int y, boolean propagate) {
		if(!this.disabled) {
			boolean superTest = super.onMouseReleased(event, x, y, false);
			if(superTest) {
				this.getStyle().setOverlayColor("#9090FF80");
			}
			return superTest;			
		}
		else {
			return super.onMousePressed(event, x, y, propagate);
		}
	}

	public boolean onMouseDisarmed(MouseEvent event, boolean propagate) {
		this.getStyle().setOverlayColor(null);
		return super.onMouseDisarmed(event, propagate);
	}

	public boolean onMouseReleased(MouseEvent event, int x, int y, boolean propagate) {
		this.getStyle().setOverlayColor(null);
		if(!this.disabled) {
			boolean superTest = super.onMouseReleased(event, x, y, false);
			if(superTest) {
				this.navigate();
			}
			return superTest;		
		}
		else {
			return super.onMouseReleased(event, x, y, propagate);
		}
	}
	
	private void navigate() {
		HtmlRendererContext rcontext = this.getHtmlRendererContext();
		if(rcontext != null) {
			String href = this.getHref();
			if(href != null && !"".equals(href)) {
				String target = this.getTarget();
				try {
					URL url = this.getFullURL(href);
					rcontext.navigate(url, target);
				} catch(MalformedURLException mfu) {
					this.warn("Malformed URI: " + href, mfu);
				}
			}
		}
	}
}
