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

package org.xamjwg.html.test;

import org.xamjwg.html.HtmlParserContext;
import org.xamjwg.html.HttpRequest;

/**
 * The <code>SimpleHtmlParserContext</code> is a dummy implementation
 * of the {@link org.xamjwg.html.HtmlParserContext} interface. Methods
 * in this class should be overridden to provide functionality
 * such as cookies.
 * @author J. H. S.
 */
public class SimpleHtmlParserContext implements HtmlParserContext {
	public SimpleHtmlParserContext() {
		super();
	}

	/**
	 * Creates a {@link org.xamjwg.html.test.SimpleHttpRequest} instance. 
	 * Override if a custom mechanism to make requests is needed.
	 */
	public HttpRequest createHttpRequest() {
		return new SimpleHttpRequest(this);
	}	

	public String getCookie() {
		return "";
	}
	
	public void setCookie(String cookie) {
		this.warn("setCookie(): Not overridden");
	}
	
	public void warn(String message, Throwable throwable) {
		System.out.println("WARN: " + message);
		throwable.printStackTrace();
	}
	
	public void error(String message, Throwable throwable) {
		System.out.println("ERROR: " + message);
		throwable.printStackTrace();		
	}
	
	public void warn(String message) {
		System.out.println("WARN: " + message);
	}
	
	public void error(String message) {
		System.out.println("ERROR: " + message);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.HtmlParserContext#getAppCodeName()
	 */
	public String getAppCodeName() {
		return "Cobra";
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.HtmlParserContext#getAppMinorVersion()
	 */
	public String getAppMinorVersion() {
		return "0";
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.HtmlParserContext#getAppName()
	 */
	public String getAppName() {
		return "Browser";
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.HtmlParserContext#getAppVersion()
	 */
	public String getAppVersion() {
		return "1";
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.HtmlParserContext#getBrowserLanguage()
	 */
	public String getBrowserLanguage() {
		return "EN";
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.HtmlParserContext#getPlatform()
	 */
	public String getPlatform() {
		return System.getProperty("os.name");
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.HtmlParserContext#getUserAgent()
	 */
	public String getUserAgent() {
		return "Mozilla/4.0 (compatible;) Cobra";
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.HtmlParserContext#isCookieEnabled()
	 */
	public boolean isCookieEnabled() {
		this.warn("isCookieEnabled(): Not overridden - returning false");
		return false;
	}
}

