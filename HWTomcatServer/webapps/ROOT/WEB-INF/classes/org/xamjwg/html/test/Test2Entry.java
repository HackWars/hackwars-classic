package org.xamjwg.html.test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import org.w3c.dom.Document;
import org.xamjwg.html.*;
import org.xamjwg.html.parser.*;
import org.xamjwg.html.gui.*;
import org.xml.sax.InputSource;

import javax.swing.*;

/**
 * Minimal rendering example: google.com.
 * @author J. H. S.
 */
public class Test2Entry {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String uri = "http://google.com";
		URL url = new URL(uri);
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();
		// A Reader should be created with the correct charset.
		Reader reader = new InputStreamReader(in);
		// InputSourceImpl constructor with URI recommended
		// so the renderer can resolve page component URLs.
		InputSource is = new InputSourceImpl(reader, uri);
		HtmlParserContext parserContext = new LocalHtmlParserContext();
		HtmlPanel htmlPanel = new HtmlPanel();
		HtmlRendererContext rendererContext = new LocalHtmlRendererContext(htmlPanel, parserContext);
		DocumentBuilderImpl builder = new DocumentBuilderImpl(parserContext, rendererContext);
		Document document = builder.parse(is);
		JFrame frame = new JFrame();
		frame.getContentPane().add(htmlPanel);
		htmlPanel.setDocument(document, rendererContext, parserContext);
		// Set the size of the JFrame when the root
		// component does not have a preferred size.
		frame.setSize(600, 400);
		frame.setVisible(true);
	}

	private static class LocalHtmlParserContext extends SimpleHtmlParserContext {
		// Override methods here to implement browser functionality
	}
	
	private static class LocalHtmlRendererContext extends SimpleHtmlRendererContext {
		// Override methods here to implement browser functionality
		
		public LocalHtmlRendererContext(HtmlPanel contextComponent, HtmlParserContext pcontext) {
			super(contextComponent, pcontext);
		}
	}
}
