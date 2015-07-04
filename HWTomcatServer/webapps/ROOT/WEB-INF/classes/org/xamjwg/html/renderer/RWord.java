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
 * Created on Apr 17, 2005
 */
package org.xamjwg.html.renderer;
import java.awt.*;

import org.xamjwg.html.domimpl.RenderableContext;
/**
 * @author J. H. S.
 */
public class RWord extends BaseBoundableRenderable {
	private final RenderState renderState;
	public final String word;
    public final FontMetrics fontMetrics; 
    public final int descent;
    public final int ascentPlusLeading;
    public final int width;
    public final int height;
    private final RenderableContext renderableContext;
    
//	private final int textDecoration;
//	private final Color backgroundColor;
	
	//private final RenderState renderState;
	public RWord(RenderableContext me, String word, RenderState renderState, RenderableContainer container) {
		super(container);
		this.renderableContext = me;
		this.renderState = renderState;
		this.word = word;
		FontMetrics fm = renderState.getFontMetrics();
		this.fontMetrics = fm;
		this.descent = fm.getDescent();
		this.ascentPlusLeading = fm.getAscent() + fm.getLeading();
		this.width = fm.stringWidth(word);
		this.height = fm.getHeight();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.markup.Renderable#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		RenderState rs = this.renderState;
		String word = this.word;
		int width = this.width;
	    int ascentPlusLeading = this.ascentPlusLeading;
		int height = this.height;
		int textDecoration = rs.getTextDecorationMask();
		Color bkg = rs.getTextBackgroundColor();
		if(bkg != null) {
			Color paneBkg = this.getPaneColor();
			if(!bkg.equals(paneBkg)) {
				Color oldColor = g.getColor();
				try {
					g.setColor(bkg);
					g.fillRect(0, 0, width, height);
				} finally {
					g.setColor(oldColor);
				}				
			}
		}
		g.drawString(word, 0, ascentPlusLeading);
		int td = textDecoration;
		if(td != 0) {
			if((td & RenderState.MASK_TEXTDECORATION_UNDERLINE) != 0) {
				int lineOffset = ascentPlusLeading + 2;
				g.drawLine(0, lineOffset, width, lineOffset);
			}
			if ((td & RenderState.MASK_TEXTDECORATION_LINE_THROUGH) != 0) {
				FontMetrics fm = this.fontMetrics;
				int lineOffset = fm.getLeading() + (fm.getAscent() + fm.getDescent()) / 2;
				g.drawLine(0, lineOffset, width, lineOffset);
			}
			if ((td & RenderState.MASK_TEXTDECORATION_OVERLINE) != 0) {
				FontMetrics fm = this.fontMetrics;
				int lineOffset = fm.getLeading();
				g.drawLine(0, lineOffset, width, lineOffset);
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
	
	public boolean paintSelection(Graphics g, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		int startX = -1;
		int endX = -1;
		if(this == startPoint.renderable) {
			startX = startPoint.x;
		}
		if(this == endPoint.renderable) {
			endX = endPoint.x;
		}
		if(!inSelection && startX == -1 && endX == -1) {
			return false;
		}
		if(startX != -1 && endX != -1) {
			if(endX < startX) {
				int temp = startX;
				startX = endX;
				endX = temp;
			}
		}
		else if(startX != -1 && endX == -1 && inSelection) {
			endX = startX;
			startX = -1;
		} 
		else if(startX == -1 && endX != -1 && !inSelection) {
			startX = endX;
			endX = -1;
		}
		int width1 = -1;
		int width2 = -1;
		char[] wordChars = this.word.toCharArray();
		if(startX != -1) {
			width1 = 0;
			FontMetrics fm = this.fontMetrics;
			for(int len = 0; len < wordChars.length; len++) {
				int w = fm.charsWidth(wordChars, 0, len);
				if(w > startX) {
					break;
				}
				width1 = w;
			}
		}
		if(endX != -1) {
			width2 = 0;
			FontMetrics fm = this.fontMetrics;
			for(int len = 0; len < wordChars.length; len++) {
				int w = fm.charsWidth(wordChars, 0, len);
				if(w > endX) {
					break;
				}
				width2 = w;
			}
		}
		if(width1 != -1 || width2 != -1) {
			int startPaint = width1 == -1 ? 0 : width1;
			int endPaint = width2 == -1 ? this.width : width2;
			g.setColor(SELECTION_COLOR);
			g.setXORMode(SELECTION_XOR);
			g.fillRect(startPaint, 0, endPaint, this.height);
		}
		else {
			if(inSelection) {
				g.setColor(SELECTION_COLOR);
				g.setXORMode(SELECTION_XOR);
				g.fillRect(0, 0, this.width, this.height);
				return true;
			}
		}
		if(width1 != -1 && width2 != -1) {
			return false;
		}
		else {
			return !inSelection;
		}
	}

	public boolean extractSelectionText(StringBuffer buffer, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		int startX = -1;
		int endX = -1;
		if(this == startPoint.renderable) {
			startX = startPoint.x;
		}
		if(this == endPoint.renderable) {
			endX = endPoint.x;
		}
		if(!inSelection && startX == -1 && endX == -1) {
			return false;
		}
		if(startX != -1 && endX != -1) {
			if(endX < startX) {
				int temp = startX;
				startX = endX;
				endX = temp;
			}
		}
		else if(startX != -1 && endX == -1 && inSelection) {
			endX = startX;
			startX = -1;
		} 
		else if(startX == -1 && endX != -1 && !inSelection) {
			startX = endX;
			endX = -1;
		}
		int index1 = -1;
		int index2 = -1;
		char[] wordChars = this.word.toCharArray();
		if(startX != -1) {
			index1 = 0;
			FontMetrics fm = this.fontMetrics;
			for(int len = 0; len < wordChars.length; len++) {
				int w = fm.charsWidth(wordChars, 0, len);
				if(w > startX) {
					break;
				}
				index1 = len;
			}
		}
		if(endX != -1) {
			index2 = 0;
			FontMetrics fm = this.fontMetrics;
			for(int len = 0; len < wordChars.length; len++) {
				int w = fm.charsWidth(wordChars, 0, len);
				if(w > endX) {
					break;
				}
				index2 = len;
			}
		}
		if(index1 != -1 || index2 != -1) {
			int startIndex = index1 == -1 ? 0 : index1;
			int endIndex = index2 == -1 ? wordChars.length : index2;
			buffer.append(wordChars, startIndex, endIndex - startIndex);
		}
		else {
			if(inSelection) {
				buffer.append(wordChars);
				return true;
			}
		}
		if(index1 != -1 && index2 != -1) {
			return false;
		}
		else {
			return !inSelection;
		}
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

	public RenderablePoint getRenderablePoint(int x, int y) {
		return new RenderablePoint(this, x, y);
	}
}
