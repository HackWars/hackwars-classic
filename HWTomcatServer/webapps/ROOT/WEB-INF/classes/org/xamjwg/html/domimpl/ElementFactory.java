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

import java.util.*;
import org.w3c.dom.html2.*;
import org.w3c.dom.*;

public class ElementFactory {
	private final Map builders = new HashMap();
	
	private ElementFactory() {
		Map builders = this.builders;
		builders.put("HTML", new HTMLElementBuilder.Html());
		builders.put("BODY", new HTMLElementBuilder.Body());
		builders.put("SPAN", new HTMLElementBuilder.Span());
		builders.put("SCRIPT", new HTMLElementBuilder.Script());
		builders.put("IMG", new HTMLElementBuilder.Img());
		builders.put("STYLE", new HTMLElementBuilder.Style());
		builders.put("LINK", new HTMLElementBuilder.Link());
		builders.put("A", new HTMLElementBuilder.A());
		builders.put("ANCHOR", new HTMLElementBuilder.Anchor());
		builders.put("TABLE", new HTMLElementBuilder.Table());
		builders.put("TD", new HTMLElementBuilder.Td());
		builders.put("TH", new HTMLElementBuilder.Th());
		builders.put("TR", new HTMLElementBuilder.Tr());
		builders.put("FORM", new HTMLElementBuilder.Form());
		builders.put("INPUT", new HTMLElementBuilder.Input());
		builders.put("TEXTAREA", new HTMLElementBuilder.Textarea());
		builders.put("FRAMESET", new HTMLElementBuilder.Frameset());
		builders.put("FRAME", new HTMLElementBuilder.Frame());
		builders.put("UL", new HTMLElementBuilder.Ul());
		builders.put("OL", new HTMLElementBuilder.Ol());
		builders.put("LI", new HTMLElementBuilder.Li());
		builders.put("PRE", new HTMLElementBuilder.Pre());
		builders.put("DIV", new HTMLElementBuilder.Div());
		builders.put("HR", new HTMLElementBuilder.Hr());
	}
	
	private static ElementFactory instance;
	
	public static ElementFactory getInstance() {
		synchronized(ElementFactory.class) {
			if(instance == null) {
				instance = new ElementFactory();
			}
			return instance;
		}
	}
	
	public HTMLElement createElement(HTMLDocumentImpl document, String name) throws DOMException {
		String normalName = name.toUpperCase();
		HTMLElementBuilder builder = (HTMLElementBuilder) this.builders.get(normalName);
		if(builder == null) {
			//TODO: IE would assume name is html text here.
			HTMLElementImpl element = new HTMLElementImpl(name);
			element.setOwnerDocument(document);
			return element;
		}
		else {
			return builder.create(document);
		}
	}
}
