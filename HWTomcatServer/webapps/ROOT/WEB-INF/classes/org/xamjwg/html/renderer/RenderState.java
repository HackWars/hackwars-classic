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
 * Created on Apr 16, 2005
 */
package org.xamjwg.html.renderer;
import java.awt.*;
import java.util.*;
import org.w3c.dom.css.CSS2Properties;
import org.xamjwg.html.domimpl.CSS2PropertiesContext;
import org.xamjwg.html.domimpl.CSS2PropertiesImpl;
import org.xamjwg.util.*;
//TODO: Later optimize by creating an interface for this class, with special
//implementations for common elements, like B, I, U, A, etc.
/**
 * @author J. H. S.
 */
public interface RenderState extends CSS2PropertiesContext {
	public static final int MASK_TEXTDECORATION_UNDERLINE = 1;
	public static final int MASK_TEXTDECORATION_OVERLINE = 2;
	public static final int MASK_TEXTDECORATION_LINE_THROUGH = 4;
	public static final int MASK_TEXTDECORATION_BLINK = 8;
	
	public Font getFont();
	public Color getColor();
	public Color getTextBackgroundColor();
	public Color getOverlayColor();
	public int getTextDecorationMask();
	public FontMetrics getFontMetrics();
	public int getBlankWidth();
	public boolean isHighlight();
	public void setHighlight(boolean highlight);
	public void invalidate();
}
