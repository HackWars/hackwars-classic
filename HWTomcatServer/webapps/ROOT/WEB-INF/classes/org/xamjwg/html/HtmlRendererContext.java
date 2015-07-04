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
 * Created on Aug 28, 2005
 */
package org.xamjwg.html;

import java.net.URL;

/**
 * The <code>HtmlRendererContext</code> interface must be implemented 
 * in order to use the Cobra HTML renderer. 
 * @author J. H. S.
 */
public interface HtmlRendererContext {	
	/**
	 * Performs a hyperlink click. Implementations should
	 * retrieve the URL content, parse it and render it.
	 * @param url The destination URL.
	 * @param target Same as the target attribute in the HTML anchor tag, i.e. _top, _blank, etc.
	 */	
	public void navigate(URL url, String target);
	
	/**
	 * Submits an HTML form.
	 * @param method The request method, GET or POST.
	 * @param action The destination URL.
	 * @param target Same as the target attribute in the FORM tag, i.e. _blank, _top, etc.
	 * @param enctype The encoding type.
	 * @param formInputs An array of {@link org.xamjwg.html.FormInput} instances.
	 */
	public void submitForm(String method, URL action, String target, String enctype, FormInput[] formInputs);

	/**
	 * Creates a {@link org.xamjwg.html.BrowserFrame} instance.
	 */
	public BrowserFrame createBrowserFrame();
	
	//------ Methods useful for Window implementation:
	
	/**
	 * Opens an alert dialog.
	 * @param message Message shown by the dialog.
	 */
	public void alert(String message);
	
	/**
	 * Goes to the previous page in the browser's history. 
	 */
	public void back();
	
	/**
	 * Relinquishes focus.
	 */
	public void blur();
	
	/**
	 * Closes the browser window, provided this
	 * is allowed for the current context.
	 */
	public void close();

	/**
	 * Opens a confirmation dialog.
	 * @param message The message shown by the confirmation dialog.
	 * @return True if the user selects YES. 
	 */
	public boolean confirm(String message);

	/**
	 * Requests focus for the current window.
	 */
	public void focus();
	
	/**
	 * Opens a separate browser window and renders a URL. 
	 * @param url The URL to be rendered.
	 * @param windowName The name of the new window.
	 * @param windowFeatures The features of the new window (same as in Javascript open method). 
	 * @param replace 
	 * @return A new {@link org.xamjwg.html.WindowInfo} instance.
	 */
	public WindowInfo open(String url, String windowName, String windowFeatures, boolean replace);
	
	/**
	 * Shows a prompt dialog.
	 * @param message The message shown by the dialog.
	 * @param inputDefault The default input value.
	 * @return The user's input value.
	 */
	public String prompt(String message, String inputDefault);
	
	/**
	 * Scrolls the client area.
	 * @param x Document's x coordinate.
	 * @param y Document's y coordinate.
	 */
	public void scroll(int x, int y);
	
	/**
	 * Gets a value indicating if the window is closed.
	 */
	public boolean isClosed();
	
	public String getDefaultStatus();	
		
	/**
	 * Gets the window name.
	 */
	public String getName();
	
	/**
	 * Gets the parent of the window in the current context.
	 */
	public WindowInfo getParent();
	
	/**
	 * Gets the opener of the window in the current context.
	 */
	public WindowInfo getOpener();
	
	/**
	 * Sets the context that opened the window of the current one.
	 * @param opener A {@link org.xamjwg.html.HtmlRendererContext}. 
	 */
	public void setOpener(HtmlRendererContext opener);
	
	/**
	 * Gets the window status text.
	 */
	public String getStatus();
	
	/**
	 * Sets the window status text.
	 * @param message A string.
	 */
	public void setStatus(String message);
	
	/**
	 * Gets the top-most browser window.
	 */
	public WindowInfo getTop();
}
