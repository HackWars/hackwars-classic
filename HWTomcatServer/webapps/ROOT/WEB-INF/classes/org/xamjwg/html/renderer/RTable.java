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
package org.xamjwg.html.renderer;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import org.xamjwg.html.domimpl.*;
import org.xamjwg.html.*;

public class RTable extends BaseElementRenderable {
	private final TableMatrix tableMatrix;
	
	public RTable(HTMLTableElementImpl renderableContext, HtmlParserContext pcontext, FrameContext frameContext, RenderableContainer container) {
		super(container);
		this.tableMatrix = new TableMatrix(renderableContext, pcontext, frameContext, container, this);
	}

	private Insets getInsets() {
		return BodyLayout.ZERO_INSETS;
	}
	
	public void paint(Graphics g) {
		Dimension size = this.getBounds().getSize();
		Insets insets = this.getInsets();
		TableMatrix tm = this.tableMatrix;
		tm.paint(g, size, insets);
	}
	
	private volatile int lastAvailWidth = -1;
	private volatile int lastAvailHeight = -1;
	private volatile Dimension layoutSize = null;
	
	public Dimension layout(int availWidth, int availHeight) {
		TableMatrix tm = this.tableMatrix;
		if(this.layoutSize == null || availWidth != this.lastAvailWidth || availHeight != this.lastAvailHeight) {
			this.lastAvailHeight = availHeight;
			this.lastAvailWidth = availWidth;
			tm.build(availWidth, availHeight);
			// Note: doLayout invalidates, but tableMatrix is set right after.
			tm.doLayout(this.getInsets());
			Dimension newSize = new Dimension(tm.getTableWidth(), tm.getTableHeight());
			this.layoutSize = newSize;
			this.markValidated();
			return newSize;
		}
		else {
			return this.layoutSize;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.UIControl#paintSelection(java.awt.Graphics, boolean, org.xamjwg.html.renderer.RenderablePoint, org.xamjwg.html.renderer.RenderablePoint)
	 */
	public boolean paintSelection(Graphics g, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		return this.tableMatrix.paintSelection(g, inSelection, startPoint, endPoint);
	}

	public boolean extractSelectionText(StringBuffer buffer, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		return this.tableMatrix.extractSelectionText(buffer, inSelection, startPoint, endPoint);
	}
	
	public void invalidate() {
		super.invalidate();
		this.lastAvailHeight = -1;
		this.lastAvailWidth = -1;
		this.layoutSize = null;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#getRenderablePoint(int, int)
	 */
	public RenderablePoint getRenderablePoint(int x, int y) {
		return this.tableMatrix.getRenderablePoint(x, y);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#invalidateState(org.xamjwg.html.domimpl.RenderableContext)
	 */
	public void invalidateState(RenderableContext context) {
		this.invalidate();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMouseClick(java.awt.event.MouseEvent, int, int)
	 */
	public void onMouseClick(MouseEvent event, int x, int y) {
		this.tableMatrix.onMouseClick(event, x, y);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMouseDisarmed(java.awt.event.MouseEvent)
	 */
	public void onMouseDisarmed(MouseEvent event) {
		this.tableMatrix.onMouseDisarmed(event);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMousePressed(java.awt.event.MouseEvent, int, int)
	 */
	public void onMousePressed(MouseEvent event, int x, int y) {
		this.tableMatrix.onMousePressed(event, x, y);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMouseReleased(java.awt.event.MouseEvent, int, int)
	 */
	public void onMouseReleased(MouseEvent event, int x, int y) {
		this.tableMatrix.onMouseReleased(event, x, y);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RCollection#getRenderables()
	 */
	public Iterator getRenderables() {
		return this.tableMatrix.getRenderables();
	}
}
