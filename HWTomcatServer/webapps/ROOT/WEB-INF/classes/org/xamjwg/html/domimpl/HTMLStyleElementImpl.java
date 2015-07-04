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
 * Created on Nov 27, 2005
 */
package org.xamjwg.html.domimpl;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.html2.HTMLStyleElement;
import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.parser.CSSOMParser;

public class HTMLStyleElementImpl extends HTMLElementImpl implements
		HTMLStyleElement {
	public HTMLStyleElementImpl() {
		super("STYLE", true);
	}
	private boolean disabled;
	public boolean getDisabled() {
		return this.disabled;
	}
	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	public String getMedia() {
		return this.getAttribute("media");
	}
	
	public void setMedia(String media) {
		this.setAttribute("media", media);
	}
	
	public String getType() {
		return this.getAttribute("type");
	}
	
	public void setType(String type) {
		this.setAttribute("type", type);
	}
	
	protected void addNotify(Node parent) {
		/*super.addNotify(parent);
		String text = this.getRawInnerText(true);
		if(text != null && !"".equals(text)) {
			String processedText = CSSUtilities.preProcessCss(text);
			HTMLDocumentImpl doc = (HTMLDocumentImpl) this.getOwnerDocument();
			CSSOMParser parser = new CSSOMParser();
			InputSource is = CSSUtilities.getCssInputSourceForStyleSheet(processedText);
			try {
				CSSStyleSheet sheet = parser.parseStyleSheet(is);
				doc.addStyleSheet(sheet);
			} catch(Exception err) {
				this.warn("Unable to parse style sheet", err);
			}
		}*/
	}
}
