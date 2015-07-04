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
 * Created on Jan 29, 2006
 */
package org.xamjwg.html.test;

import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.w3c.dom.Document;
import org.xamjwg.html.*;
import org.xamjwg.html.domimpl.NodeImpl;
import org.xamjwg.html.gui.HtmlPanel;
import org.xamjwg.html.parser.DocumentBuilderImpl;
import org.xamjwg.html.parser.InputSourceImpl;

/**
 * The <code>SimpleBrowserFrame</code> class implements
 * the {@link org.xamjwg.html.BrowserFrame} interface. 
 * It represents a browser frame component.
 * @author J. H. S.
 */
public class SimpleBrowserFrame extends HtmlPanel implements BrowserFrame {
	private final HtmlParserContext parserContext;
	
	public SimpleBrowserFrame(HtmlParserContext pcontext) {
		this.parserContext = pcontext;
	}

	public Component getComponent() {
		return this;
	}

	public void loadURL(URL url) {
		try {
			String urlText = url.toExternalForm();
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0;) Warrior Test 1.0");
			if(connection instanceof HttpURLConnection) {
				HttpURLConnection hc = (HttpURLConnection) connection;
				hc.setInstanceFollowRedirects(true);
			}
			InputStream in = connection.getInputStream();
			try {
				long time1 = System.currentTimeMillis();
				BufferedInputStream bin = new BufferedInputStream(in, 8192);
				HtmlParserContext pcontext = new SimpleHtmlParserContext();
				DocumentBuilderImpl builder = new DocumentBuilderImpl(pcontext);
				Document document = builder.parse(new InputSourceImpl(bin, urlText, "ISO-8859-1"));
				long time2 = System.currentTimeMillis();
				System.out.println("Parse elapsed=" + (time2 - time1) + " ms.");
				HtmlRendererContext rcontext = new SimpleHtmlRendererContext(this, pcontext);
				this.setDocument(document, rcontext, this.parserContext);
			} finally {
				in.close();
			}
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
}
