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

package org.xamjwg.html.js;

import org.w3c.dom.Document;
import org.xamjwg.html.HtmlParserContext;
import org.xamjwg.html.HtmlRendererContext;
import org.xamjwg.html.domimpl.HTMLDocumentImpl;

public class Navigator {
	private final HtmlParserContext context;
	private final Document document;

	/**
	 * @param context
	 */
	public Navigator(HtmlParserContext context, Document document) {
		super();
		this.context = context;
		this.document = document;
	}
	
	private HtmlRendererContext getRendererContext() {
		Object doc = this.document;
		if(doc instanceof HTMLDocumentImpl) {
			HTMLDocumentImpl di = (HTMLDocumentImpl) doc;
			return di.getHtmlRendererContext();
		}
		else {
			return null;
		}
	}
	
	public String getAppCodeName() {
		return this.context.getAppCodeName();
	}

	public String getAppName() {
		return this.context.getAppName();
	}
	
	public String getAppVersion() {
		return this.context.getAppVersion();
	}

	public String getAppMinorVersion() {
		return this.context.getAppMinorVersion();
	}
	
	public String getPlatform() {
		return this.context.getPlatform();
	}
	
	public String getUserAgent() {
		return this.context.getUserAgent();
	}	
}