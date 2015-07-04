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

import java.net.URL;
import javax.swing.JOptionPane;
import org.xamjwg.html.*;
import org.xamjwg.html.gui.HtmlPanel;
import org.xamjwg.html.js.FilteredObjectList;

/**
 * The <code>SimpleHtmlRendererContext</code> class implements
 * the {@link org.xamjwg.html.HtmlRendererContext} interface.
 * Note that this class provides only dummy implementations
 * of most methods, which should be overridden to provide
 * real-world functionality. 
 * @author J. H. S.
 */
public class SimpleHtmlRendererContext implements HtmlRendererContext {
	private final HtmlPanel parentComponent;
	private final HtmlParserContext parserContext;

	/**
	 * Constructs a SimpleHtmlRendererContext.
	 * @param contextComponent The component that will render HTML.
	 * @deprecated This constructor should no longer be used.
	 */
	public SimpleHtmlRendererContext(HtmlPanel contextComponent) {
		super();
		this.parentComponent = contextComponent;
		this.parserContext = null;
	}
	
	/**
	 * Constructs a SimpleHtmlRendererContext.
	 * @param contextComponent The component that will render HTML.
	 * @param pcontext A parser context.
	 */
	public SimpleHtmlRendererContext(HtmlPanel contextComponent, HtmlParserContext pcontext) {
		super();
		this.parentComponent = contextComponent;
		this.parserContext = pcontext;
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

	public void navigate(URL href, String target) {
		this.warn("navigate(): Not overriden; href=" + href + "; target=" + target);
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.HtmlContext#submitForm(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.xamjwg.html.FormInput[])
	 */
	public void submitForm(String method, java.net.URL action, String target, String enctype, FormInput[] formInputs) {
		this.warn("submitForm(): Not overridden; method=" + method + "; action=" + action + "; target=" + target);
	}

	// Methods useful to Window below:
	
	public void alert(String message) {
		JOptionPane.showMessageDialog(this.parentComponent, message);
	}
	
	public void back() {
		this.warn("back(): Not overridden");
	}
	
	public void blur() {
		this.warn("back(): Not overridden");
	}

	public void close() {
		this.warn("close(): Not overridden");
	}
	
	public boolean confirm(String message) {
		int retValue = JOptionPane.showConfirmDialog(parentComponent, message, "Confirm", JOptionPane.YES_NO_OPTION);
		return retValue == JOptionPane.YES_OPTION;
	}
		
	public void focus() {
		this.warn("focus(): Not overridden");
	}
	
	public WindowInfo open(String url, String windowName, String windowFeatures, boolean replace) {
		this.warn("open(): Not overridden");
		return null;
	}

	public String prompt(String message, String inputDefault) {
		return JOptionPane.showInputDialog(parentComponent, message);
	}

	public void scroll(int x, int y) {
		this.warn("scroll(): Not overridden");
	}
	
	public boolean isClosed() {
		this.warn("isClosed(): Not overridden");
		return false;
	}
	
	public String getDefaultStatus() {
		this.warn("getDefaultStatus(): Not overridden");
		return "";
	}
	
	public FilteredObjectList getFrames() {
		this.warn("getFrames(): Not overridden");
		return null;
	}
	
	public int getLength() {
		this.warn("getLength(): Not overridden");
		return 0;
	}
	
	public String getName() {
		this.warn("getName(): Not overridden");
		return "";
	}
	
	public WindowInfo getParent() {
		this.warn("getParent(): Not overridden");
		return null;
	}
	
	public WindowInfo getOpener() {
		this.warn("getOpener(): Not overridden");
		return null;
	}
	
	public void setOpener(HtmlRendererContext opener) {
		this.warn("setOpener(): Not overridden");
	}
	
	public String getStatus() {
		this.warn("getStatus(): Not overridden");
		return "";
	}
	
	public void setStatus(String message) {
		this.warn("setStatus(): Not overridden");
	}
	
	public WindowInfo getTop() {
		this.warn("getTop(): Not overridden");
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.HtmlContext#createBrowserFrame()
	 */
	public BrowserFrame createBrowserFrame() {
		HtmlParserContext pc = this.parserContext;
		if(pc == null) {
			pc = this.parentComponent.getHtmlParserContext();
		}
		return new SimpleBrowserFrame(pc);
	}		
}
