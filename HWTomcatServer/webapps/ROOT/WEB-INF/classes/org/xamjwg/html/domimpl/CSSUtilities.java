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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleSheet;
import org.xamjwg.html.HtmlParserContext;
import org.xamjwg.html.HttpRequest;

import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.parser.CSSOMParser;

public class CSSUtilities {
	private CSSUtilities() {
	}

	public static String preProcessCss(String text) {
		//TODO: Fix parser to optimize away this
		try {
			BufferedReader reader = new BufferedReader(new StringReader(text));
			String line;
			StringBuffer sb = new StringBuffer();
			while((line = reader.readLine()) != null) {
				if(!line.trim().startsWith("//")) {
					sb.append(line);
					sb.append("\r\n");
				}			
			}
			return sb.toString();
		} catch(IOException ioe) {
			// not possible
			throw new IllegalStateException(ioe.getMessage());
		}
	}
	
	public static InputSource getCssInputSourceForStyleSheet(String text) {
		java.io.Reader reader = new StringReader(text);
		InputSource is = new InputSource(reader);
		return is;
	}
		
	public static CSSStyleSheet parse(String href, HTMLDocumentImpl doc, String baseUri) throws MalformedURLException {
		/*href=href.toLowerCase();
		HtmlParserContext context = doc.getHtmlParserContext();
		final HttpRequest request = context.createHttpRequest();
		URL baseURL = new URL(baseUri);
		URL scriptURL = new URL(baseURL, href);
		String scriptURI = scriptURL == null ? href : scriptURL.toExternalForm();
		// Perform a synchronous request
		request.open("GET", scriptURI, false);			
		int status = request.getStatus();
		if(status != 200 && status != 0) {
			// Note: status 0 could be a file
			throw new IllegalStateException("Error status: " + request.getStatus());
		}
		
		String text = request.getResponseText();
		if(text != null && !"".equals(text)) {
			String processedText = preProcessCss(text);
			CSSOMParser parser = new CSSOMParser();
			InputSource is = getCssInputSourceForStyleSheet(processedText);
			is.setURI(scriptURI);
			try {
				CSSStyleSheetImpl sheet = (CSSStyleSheetImpl) parser.parseStyleSheet(is);
				sheet.setHref(scriptURI);
				return sheet;
			} catch(Exception err) {					
				context.warn("Unable to parse style sheet", err);
				return null;
			}
		}		
		else {
			return null;
		}*/
		return(null);
	}
}
