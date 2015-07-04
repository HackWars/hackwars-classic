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

package org.xamjwg.html;

import java.net.URL;

/**
 * The <code>HtmlParserContext</code> interface must be implemented 
 * in order to use
 * the HTML parser in {@link org.xamjwg.html.parser.DocumentBuilderImpl}.
 * A simple implementation of this interface is provided in 
 * {@link org.xamjwg.html.test.SimpleHtmlParserContext}. 
 * @author J. H. S.
 */
public interface HtmlParserContext {
	/**
	 * Gets a semi-colon-separated list of name=value pairs
	 * corresponding to persisted cookies available in the current
	 * document context.
	 */
	public String getCookie();
	
	/**
	 * Addes a cookie in the current document context. 
	 * The specfication is equivalent to that of a Set-Cookie HTTP header. 
	 */
	public void setCookie(String cookie);
	
	/**
	 * Informs context about a warning.
	 */
	public void warn(String message, Throwable throwable);

	/**
	 * Informs context about an error.
	 */
	public void error(String message, Throwable throwable);
	
	/**
	 * Informs context about a warning.
	 */
	public void warn(String message);

	/**
	 * Informs context about an error.
	 */
	public void error(String message);
		
	/**
	 * Creates an instance of {@link org.xamjwg.html.HttpRequest} which
	 * can be used by the renderer to load images, for example. 
	 */
	public HttpRequest createHttpRequest();

	/**
	 * Gets browser "code" name.
	 */
	public String getAppCodeName();

	/**
	 * Gets browser application name.
	 */
	public String getAppName();

	/**
	 * Gets browser application version.
	 */
	public String getAppVersion();

	/**
	 * Gets browser application minor version.
	 */
	public String getAppMinorVersion();

	/**
	 * Gets browser language code. See <a href="http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes">ISO 639-1 codes</a>.
	 */
	public String getBrowserLanguage();

	/**
	 * Returns a value indicating whether cookies are
	 * enabled in the browser. 
	 */
	public boolean isCookieEnabled();
	
	/**
	 * Gets the name of the user's operating system.
	 */
	public String getPlatform();

	/**
	 * Should return the string used in
	 * the User-Agent header. 
	 */
	public String getUserAgent();
}
