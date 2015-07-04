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
package org.xamjwg.html;

import java.awt.Component;
import java.net.URL;

/**
 * The <code>BrowserFrame</code> represents a browser frame.
 * A simple implementation of this interface is
 * provided in {@link org.xamjwg.html.test.SimpleBrowserFrame}.
 * @author J. H. S.
 */
public interface BrowserFrame {
	/**
	 * Gets the component that renders the frame.
	 * This can be a {@link org.xamjwg.html.gui.HtmlPanel}. 
	 */
	public Component getComponent();
	
	/**
	 * Loads a URL in the frame.
	 */
	public void loadURL(URL url);
}
