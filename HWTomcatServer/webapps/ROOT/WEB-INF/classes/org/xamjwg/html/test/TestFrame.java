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
 * Created on Oct 22, 2005
 */
package org.xamjwg.html.test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import org.xamjwg.html.parser.*;
import org.xamjwg.html.*;
import org.xamjwg.html.domimpl.*;
import org.xamjwg.html.gui.HtmlPanel;
import org.xamjwg.util.IO;
import org.w3c.dom.*;
import java.io.*;

/**
 * A Swing frame that can be used to test the
 * Cobra HTML rendering engine. 
 * @author J. H. S.
 */
public class TestFrame extends JFrame {	
	public TestFrame() throws HeadlessException {
		super();
		init();
	}

	public TestFrame(GraphicsConfiguration gc) {
		super(gc);
		init();
	}
	
	public TestFrame(String title) throws HeadlessException {
		super(title);
		init();
	}

	public TestFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		init();
	}
	
	private JTree tree;
	private HtmlPanel htmlPanel;
	private JTextArea textArea;
	
	private void init() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		final JTextField textField = new JTextField();
		JButton button = new JButton("Parse & Render");
		JTabbedPane tabbedPane = new JTabbedPane();
		JTree tree = new JTree();
		JScrollPane scrollPane = new JScrollPane(tree);
		
		this.tree = tree;
		
		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(bottomPanel, BorderLayout.CENTER);
		
		topPanel.add(textField, BorderLayout.CENTER);
		topPanel.add(button, BorderLayout.EAST);
		
		bottomPanel.add(tabbedPane, BorderLayout.CENTER);
		
		HtmlPanel panel = new HtmlPanel();
		this.htmlPanel = panel;	
		
		JTextArea textArea = new JTextArea();
		this.textArea = textArea;
		
		tabbedPane.addTab("HTML", panel);
		tabbedPane.addTab("Tree", scrollPane);
		tabbedPane.addTab("Source", textArea);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				process(textField.getText());
			}
		});
	}

	private void process(String uri) {
		try {
			System.out.println("process(): uri=" + uri);
			URL url = new URL(uri);
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0;) Cobra/0.95.1");
			connection.setRequestProperty("Cookie", "");
			if(connection instanceof HttpURLConnection) {
				HttpURLConnection hc = (HttpURLConnection) connection;
				hc.setInstanceFollowRedirects(true);
				int responseCode = hc.getResponseCode();
				System.out.println("process(): Response code: " + responseCode);
			}
			InputStream in = connection.getInputStream();
			byte[] content;
			try {
				content = IO.load(in, 8192);
			} finally {
				in.close();
			}
			String source = new String(content, "ISO-8859-1");
			this.textArea.setText(source);
			long time1 = System.currentTimeMillis();
			InputStream bin = new MyByteArrayInputStream(content);
			HtmlParserContext context = new SimpleHtmlParserContext();
			HtmlRendererContext rcontext = new SimpleHtmlRendererContext(this.htmlPanel, context);
			DocumentBuilderImpl builder = new DocumentBuilderImpl(context, rcontext);
			Document document = builder.parse(new InputSourceImpl(bin, uri, "ISO-8859-1"));
			long time2 = System.currentTimeMillis();
			System.out.println("Parse elapsed=" + (time2 - time1) + " ms.");
			this.tree.setModel(new NodeTreeModel(document));
			this.htmlPanel.setDocument(document, rcontext, context);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	private class MyByteArrayInputStream extends ByteArrayInputStream {

		/**
		 * @param buf
		 * @param offset
		 * @param length
		 */
		public MyByteArrayInputStream(byte[] buf, int offset, int length) {
			super(buf, offset, length);
			// TODO Auto-generated constructor stub
		}

		/**
		 * @param buf
		 */
		public MyByteArrayInputStream(byte[] buf) {
			super(buf);
			// TODO Auto-generated constructor stub
		}

		/* (non-Javadoc)
		 * @see java.io.ByteArrayInputStream#close()
		 */
		public void close() throws IOException {
			super.close();
		}

		/* (non-Javadoc)
		 * @see java.io.ByteArrayInputStream#read()
		 */
		public synchronized int read() {
			int r = super.read();
			return r;
		}

		/* (non-Javadoc)
		 * @see java.io.ByteArrayInputStream#read(byte[], int, int)
		 */
		public synchronized int read(byte[] b, int off, int len) {
			int r = super.read(b, off, len);
			return r;
		}
		
		
	}
}
