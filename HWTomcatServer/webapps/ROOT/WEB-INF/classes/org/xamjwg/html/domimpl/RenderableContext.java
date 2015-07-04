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
 * Created on Oct 23, 2005
 */
package org.xamjwg.html.domimpl;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import org.xamjwg.html.renderer.HtmlLength;

public interface RenderableContext {
	public boolean onClick(AWTEvent event, boolean propagate);
	public boolean onEnterPressed(AWTEvent event, boolean propagate);
	public boolean onMouseClick(MouseEvent event, int x, int y, boolean propagate);
	public boolean onMousePressed(MouseEvent event, int x, int y, boolean propagate);
	public boolean onMouseReleased(MouseEvent event, int x, int y, boolean propagate);
	public boolean onMouseDisarmed(MouseEvent event, boolean propagate);
	public HtmlLength getWidthLength();
	public HtmlLength getHeightLength();
	public float getAlignmentX();
	public float getAlignmentY();
	public java.net.URL getFullURL(String spec) throws MalformedURLException;
	public void warn(String message, Throwable err);
	public CSS2PropertiesImpl getStyle();
	public boolean isEqualOrDescendentOf(RenderableContext otherContext);
	public String getAttribute(String name);
	public String getTextContent();
	public void setDocumentItem(String name, Object value);
	public Object getDocumentItem(String name);
}
