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
 * Created on Nov 19, 2005
 */
package org.xamjwg.html.test;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.EventObject;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xamjwg.html.*;
import org.xamjwg.util.*;

/**
 * The <code>SimpleHttpRequest</code> class implements
 * the {@link org.xamjwg.html.HttpRequest} interface.
 * The <code>HttpRequest</code> implementation provided
 * by this class is simple, with no caching. It creates
 * a new thread for each new asynchronous request.
 * @author J. H. S.
 */
public class SimpleHttpRequest implements HttpRequest {
	private int readyState;
	private int status;
	private String statusText;
	private byte[] responseBytes;
	private java.util.Map responseHeadersMap;
	private String responseHeaders;
	private final HtmlParserContext context;
	
	public SimpleHttpRequest(HtmlParserContext context) {
		super();
		this.context = context;
	}

	public synchronized int getReadyState() {
		return this.readyState;
	}

	public synchronized String getResponseText() {
		byte[] bytes = this.responseBytes;
		//TODO: proper charset
		try {
			return bytes == null ? null : new String(bytes, "ISO-8859-1");
		} catch(UnsupportedEncodingException uee) {
			return null;
		}
	}

	public synchronized Document getResponseXML() {
		byte[] bytes = this.responseBytes;
		if(bytes == null) {
			return null;
		}
		java.io.InputStream in = new ByteArrayInputStream(bytes);
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
		} catch(Exception err) {
			//err.printStackTrace();
			return null;
		}
	}

	public synchronized byte[] getResponseBytes() {
		return this.responseBytes;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.HttpRequest#getResponseImage()
	 */
	public Image getResponseImage() {
		byte[] bytes = this.responseBytes;
		if(bytes == null) {
			return null;
		}
		return Toolkit.getDefaultToolkit().createImage(bytes);
	}

	public synchronized int getStatus() {
		return this.status;
	}

	public synchronized String getStatusText() {
		return this.statusText;
	}

	public void abort() {
		URLConnection c;
		synchronized(this) {
			c = this.connection;
		}
		if(c instanceof HttpURLConnection) {
			((HttpURLConnection) c).disconnect();
		}
		else if(c != null) {
			try {
				c.getInputStream().close();
			} catch(IOException ioe) {
				//ioe.printStackTrace();
			}
		}
	}

	public synchronized String getAllResponseHeaders() {
		return this.responseHeaders;
	}

	public synchronized String getResponseHeader(String headerName) {
		Map headers = this.responseHeadersMap;
		return headers == null ? null : (String) headers.get(headerName);
	}

	public void open(String method, String url) {
		this.open(method, url, true);
	}

	public void open(String method, URL url) {
		this.open(method, url, true, null, null);
	}

	public void open(String method, String url, boolean asyncFlag) {
		this.open(method, url, asyncFlag, null);
	}

	public void open(String method, String url, boolean asyncFlag,
			String userName) {
		this.open(method, url, asyncFlag, userName, null);
	}

	public void open(String method, String url, boolean asyncFlag,
			String userName, String password) {
		try {
			URL urlObj = new URL(url);
			this.openSync(method, urlObj, userName, password);
		} catch(MalformedURLException mfu) {
			this.changeState(HttpRequest.STATE_COMPLETE, 0, "", null);			
		}
	}

	public void open(final String method, final java.net.URL url, boolean asyncFlag,
			final String userName, final String password) {
		if(asyncFlag) {
			// Should use a thread pool instead
			new Thread("Request") {
				public void run() {
					openSync(method, url, userName, password);
				}
			}.start();
		}
		else {
			this.openSync(method, url, userName, password);
		}
	}

	private void changeState(int readyState, int status, String statusMessage, byte[] bytes) {
		synchronized(this) {
			this.readyState = readyState;
			this.status = status;
			this.statusText = statusMessage;
			this.responseBytes = bytes;
		}
		this.readyEvent.fireEvent(null);
	}
	
	private String getAllResponseHeaders(URLConnection c) {
		int idx = 0;
		String value;
		StringBuffer buf = new StringBuffer();
		while((value = c.getHeaderField(idx)) != null) {
			String key = c.getHeaderFieldKey(idx);
			buf.append(key); buf.append(": "); buf.append(value);
			idx++;
		}
		return buf.toString();
	}
	
	private java.net.URLConnection connection;
	
	private void openSync(String method, java.net.URL url, 
			String userName, String password) {
		try {	
			
			this.abort();
			URLConnection c = url.openConnection();
			synchronized(this) {
				this.connection = c;
			}
			try {
				this.changeState(HttpRequest.STATE_LOADING, 0, "", null);
				java.io.InputStream in = c.getInputStream();
				int contentLength = c.getContentLength();
				byte[] bytes = IO.load(in, contentLength == -1 ? 4096 : contentLength);
				int status = 0;
				String statusText = "";
				if(c instanceof HttpURLConnection) {
					HttpURLConnection hc = (HttpURLConnection) c;
					status = hc.getResponseCode();
					statusText = hc.getResponseMessage();
				}
				synchronized(this) {
					this.responseHeaders = this.getAllResponseHeaders(c);
					this.responseHeadersMap = c.getHeaderFields();
				}
				this.changeState(HttpRequest.STATE_COMPLETE, status, statusText, bytes);
			} finally {
				synchronized(this) {
					this.connection = null;
				}
			}
		} catch(Exception err) {
			this.changeState(HttpRequest.STATE_COMPLETE, 0, "", null);
			this.context.error("Request failed on url=" + url, err);
		}
	}

	private final EventDispatch readyEvent = new EventDispatch(); 
	
	public void addReadyStateChangeListener(final ReadyStateChangeListener listener) {
		readyEvent.addListener(new GenericEventListener() {
		    public void processEvent(EventObject event) {
		    	listener.readyStateChanged();
		    }
		});
	}
}
