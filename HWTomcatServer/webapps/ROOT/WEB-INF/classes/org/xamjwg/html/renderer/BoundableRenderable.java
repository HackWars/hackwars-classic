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
package org.xamjwg.html.renderer;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import org.xamjwg.html.domimpl.RenderableContext;

public interface BoundableRenderable extends Renderable {
	public Rectangle getBounds();
	public Renderable getParent();
	public void setBounds(int x, int y, int with, int height);
	public void setX(int x);
	public void setHeight(int height);
	public void onMouseClick(MouseEvent event, int x, int y);
	public RenderablePoint getRenderablePoint(int x, int y);
	
	/**
	 * @param event
	 * @param x
	 * @param y
	 * @return The leaf BoundableRenderable instance.
	 */
	public void onMousePressed(MouseEvent event, int x, int y);
	public void onMouseReleased(MouseEvent event, int x, int y);
	public void onMouseDisarmed(MouseEvent event);
	public void repaint();
	public void invalidateState(RenderableContext context);
	
	/**
	 * 
	 * @param g
	 * @param inSelection
	 * @param startPoint
	 * @param endPoint
	 * @return True iff it's in selection when finished painting.
	 */
	public boolean paintSelection(Graphics g, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint);
	public boolean extractSelectionText(StringBuffer buffer, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint);
	public void repaint(int x, int y, int width, int height);
	public void setParent(Renderable parent);
}
