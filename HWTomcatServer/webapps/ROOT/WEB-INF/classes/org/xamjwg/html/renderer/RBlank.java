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
 * Created on May 21, 2005
 */
package org.xamjwg.html.renderer;
import java.awt.*;

import org.xamjwg.html.domimpl.RenderableContext;
/**
 * @author J. H. S.
 */
public class RBlank extends BaseBoundableRenderable {
	public final int width;
	public final int height;
	public final int ascentPlusLeading;
	private final FontMetrics fontMetrics;
	private final RenderState renderState;
	private final RenderableContext renderableContext;
	
	public RBlank(RenderableContext me, RenderState renderState, FontMetrics fm, int width, RenderableContainer container) {
		super(container);
		this.renderableContext = me;
		this.width = width;
		this.renderState = renderState;
		this.fontMetrics = fm;
		this.height = fm.getHeight();
		this.ascentPlusLeading = fm.getAscent() + fm.getLeading();
	}
	
	public void onMouseClick(java.awt.event.MouseEvent event, int x, int y) {
		RenderableContext me = this.renderableContext;
		if(me != null) {
			me.onMouseClick(event, x, y, true);
		}
	}	

	public void onMousePressed(java.awt.event.MouseEvent event, int x, int y) {
		RenderableContext me = this.renderableContext;
		if(me != null) {
			me.onMousePressed(event, x, y, true);
		}
	}	

	public void onMouseReleased(java.awt.event.MouseEvent event, int x, int y) {
		RenderableContext me = this.renderableContext;
		if(me != null) {
			me.onMouseReleased(event, x, y, true);
		}
	}
	public void onMouseDisarmed(java.awt.event.MouseEvent event) {
		RenderableContext me = this.renderableContext;
		if(me != null) {
			me.onMouseDisarmed(event, true);
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.markup.Renderable#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		RenderState rs = this.renderState;
		Color bkg = rs.getTextBackgroundColor();
		if(bkg != null) {
			Color paneBkg = this.getPaneColor();
			if(!bkg.equals(paneBkg)) {
				Color oldColor = g.getColor();
				try {
					g.setColor(bkg);
					g.fillRect(0, 0, this.width, this.height);
				} finally {
					g.setColor(oldColor);
				}				
			}
		}
		int td = rs.getTextDecorationMask();
		if(td != 0) {
			if((td & RenderState.MASK_TEXTDECORATION_UNDERLINE) != 0) {
				int lineOffset = this.ascentPlusLeading + 2;
				g.drawLine(0, lineOffset, this.width, lineOffset);
			}
			if ((td & RenderState.MASK_TEXTDECORATION_LINE_THROUGH) != 0) {
				FontMetrics fm = this.fontMetrics;
				int lineOffset = fm.getLeading() + (fm.getAscent() + fm.getDescent()) / 2;
				g.drawLine(0, lineOffset, this.width, lineOffset);
			}
			if ((td & RenderState.MASK_TEXTDECORATION_OVERLINE) != 0) {
				int lineOffset = this.fontMetrics.getLeading();
				g.drawLine(0, lineOffset, this.width, lineOffset);
			}
			if ((td & RenderState.MASK_TEXTDECORATION_BLINK) != 0) { 
				//TODO
			}
		}
		Color over = rs.getOverlayColor();
		if(over != null) {
			Color oldColor = g.getColor();
			try {
				g.setColor(over);
				g.fillRect(0, 0, width, height);
			} finally {
				g.setColor(oldColor);
			}				
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#paintSelection(java.awt.Graphics, boolean, org.xamjwg.html.renderer.RenderablePoint, org.xamjwg.html.renderer.RenderablePoint)
	 */
	public boolean paintSelection(Graphics g, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		if(this == startPoint.renderable || this == endPoint.renderable) {
			if(inSelection) {
				return false;
			}
		}
		else if(!inSelection) {
			return false;
		}
		g.setColor(SELECTION_COLOR);
		g.setXORMode(SELECTION_XOR);
		g.fillRect(0, 0, this.width, this.height);
		return true;
	}
	
	public boolean extractSelectionText(StringBuffer buffer, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		if(this == startPoint.renderable || this == endPoint.renderable) {
			if(inSelection) {
				return false;
			}
		}
		else if(!inSelection) {
			return false;
		}
		buffer.append(' ');
		return true;
	}

	public void repaint() {
		this.renderState.invalidate();
		super.repaint();
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#invalidateState(org.xamjwg.html.renderer.RenderableContext)
	 */
	public void invalidateState(RenderableContext context) {
		this.renderState.invalidate();
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#getRenderable(int, int)
	 */
	public RenderablePoint getRenderablePoint(int x, int y) {
		return new RenderablePoint(this, x, y);
	}
}
