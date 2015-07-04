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
 * Created on Feb 12, 2006
 */
package org.xamjwg.html.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.html2.HTMLLIElement;
import org.w3c.dom.html2.HTMLOListElement;
import org.xamjwg.html.domimpl.*;
import org.xamjwg.html.*;

public class RList extends BaseElementRenderable {
	private final HTMLElementImpl rootElement;
	private final int nesting;
	private final HtmlParserContext parserContext;
	private final FrameContext frameContext;
	
	public RList(HTMLElementImpl listElement, int nesting, HtmlParserContext pcontext, FrameContext frameContext, RenderableContainer container) {
		super(container);
		this.frameContext = frameContext;
		this.parserContext = pcontext;
		this.rootElement = listElement;
		this.nesting = nesting;
	}

	private Dimension layoutSize = null;
	private int lastAvailHeight = -1;
	private int lastAvailWidth = -1;
		
	private Insets getInsets() {
		return BodyLayout.ZERO_INSETS; 
	}
	
	private static final int INDENT = 36;
	private static final int ITEM_SPACING = 4;
	private final Collection blocks = new ArrayList();
	
	public Dimension layout(int availWidth, int availHeight) {
		if(this.layoutSize == null || availHeight != this.lastAvailHeight || availWidth != this.lastAvailWidth) {
			this.lastAvailHeight = availHeight;
			this.lastAvailWidth = availWidth;
			Insets insets = this.getInsets();
			int extraWidth = insets.right + insets.left;
			int blockWidth = availWidth - INDENT - extraWidth;
			int maxBlockWidth = blockWidth;
			ArrayList liDescendents = this.rootElement.getDescendents(new LiFilter());
			int size = liDescendents.size();
			int y = insets.top;
			int newNesting = this.nesting + 1;
			this.blocks.clear();
			for(int i = 0; i < size; i++) {
				HTMLElementImpl liElement = (HTMLElementImpl) liDescendents.get(i);
				RBlock block = new RBlock(newNesting, this.parserContext, this.frameContext, this.container, this);
				block.setParent(this);
				this.blocks.add(block);
				block.setRootNode(liElement);
				Dimension renderSize = block.layout(blockWidth, 0);
				int actualBlockWidth;
				if(renderSize.width > blockWidth) {
					actualBlockWidth = renderSize.width;
				}
				else {
					actualBlockWidth = blockWidth; 
				}
				int blockHeight = renderSize.height;				
				block.setBounds(insets.left + INDENT, y, actualBlockWidth, blockHeight);
				y += blockHeight + ITEM_SPACING;
				if(actualBlockWidth > maxBlockWidth) {
					maxBlockWidth = actualBlockWidth;
				}
			}
			int ph = y - ITEM_SPACING + insets.bottom;
			int pw = maxBlockWidth + INDENT + insets.left + insets.right;
			Dimension newSize = new Dimension(pw, ph);
			this.layoutSize = newSize;
			this.markValidated();
			return newSize;
		}
		else {
			return this.layoutSize;
		}
	}
	
	
	private static final int BULLET_SPACING = 8; 
	private static final int BULLET_BOTTOM_PADDING = 4; 
	private static final int BULLET_WIDTH = 5;
	private static final int BULLET_HEIGHT = 5;
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		HTMLElementImpl rootElement = this.rootElement;
		String type = rootElement.getAttribute("type");
		int bulletType = 0;
		boolean numbered = rootElement instanceof HTMLOListElement; 
		FontMetrics fm = null;
		int bulletNumber = 0;
		if(numbered) {
			HTMLOListElementImpl oList = (HTMLOListElementImpl) rootElement;
			bulletNumber = oList.getStart();
			Font f = g.getFont();
			fm = Toolkit.getDefaultToolkit().getFontMetrics(f);			
		}
		else {
			if("disc".equalsIgnoreCase(type)) {
				bulletType = 0;
			} else if("circle".equalsIgnoreCase(type)) {
				bulletType = 1;
			}
			else if ("square".equals(type)) {
				bulletType = 2;
			}
			else {
				bulletType = this.nesting;
			}			
		}
		Iterator i = this.blocks.iterator();
		while(i.hasNext()) {
			Object c = i.next();
			if(c instanceof RBlock) {
				RBlock hp = (RBlock) c;
				Rectangle bounds = hp.getBounds();
				Graphics newG = g.create(bounds.x, bounds.y, bounds.width, bounds.height);
				hp.paint(newG);
				int lineHeight = hp.getFirstLineHeight();
				int bulletRight = bounds.x - BULLET_SPACING;
				int bulletBottom = bounds.y + lineHeight;
				if(numbered) {
					// TODO: value attribute from LI element
					// TODO: type attribute from LI element
					String numberText = bulletNumber + "."; 				
					int bulletLeft = bulletRight - fm.stringWidth(numberText);
					int bulletY = bulletBottom - fm.getDescent();
					g.drawString(numberText, bulletLeft, bulletY);
				}
				else {
					bulletBottom -= BULLET_BOTTOM_PADDING;
					int bulletTop = bulletBottom - BULLET_HEIGHT;
					int bulletLeft = bulletRight - BULLET_WIDTH;
					if(bulletType == 0) {
						g.fillOval(bulletLeft, bulletTop, BULLET_WIDTH, BULLET_HEIGHT);
					}
					else if(bulletType == 1) {
						g.drawOval(bulletLeft, bulletTop, BULLET_WIDTH, BULLET_HEIGHT);
					}
					else {
						g.fillRect(bulletLeft, bulletTop, BULLET_WIDTH, BULLET_HEIGHT);
					}
				}
				bulletNumber++;
			}
		}
	}

	public boolean paintSelection(Graphics g, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		Iterator i = this.blocks.iterator();
		while(i.hasNext()) {
			Object c = i.next();
			if(c instanceof RBlock) {
				RBlock hp = (RBlock) c;
				Rectangle bounds = hp.getBounds();
				Graphics subG = g.create(bounds.x, bounds.y, bounds.width, bounds.height);
				boolean newInSelection = hp.paintSelection(subG, inSelection, startPoint, endPoint);
				if(inSelection && !newInSelection) {
					return false;
				}
				inSelection = newInSelection;
			}
		}
		return inSelection;
	}		

	public boolean extractSelectionText(StringBuffer buffer, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		Iterator i = this.blocks.iterator();
		while(i.hasNext()) {
			Object c = i.next();
			if(c instanceof RBlock) {
				RBlock hp = (RBlock) c;
				boolean newInSelection = hp.extractSelectionText(buffer, inSelection, startPoint, endPoint);
				if(inSelection && !newInSelection) {
					return false;
				}
				inSelection = newInSelection;
			}
		}
		return inSelection;
	}		

	/* (non-Javadoc)
	 * @see java.awt.Container#doLayout()
	 */
	public void doLayout() {
		// nop (already done by doLayoutImpl)
	}

	public void invalidate() {
		super.invalidate();
		this.lastAvailHeight = -1;
		this.lastAvailWidth = -1;
		this.layoutSize = null;
	}
	
	private static class LiFilter implements NodeFilter
	{
		/* (non-Javadoc)
		 * @see org.xamjwg.html.domimpl.NodeFilter#accept(org.w3c.dom.Node)
		 */
		public boolean accept(Node node) {
			return node instanceof HTMLLIElement;
		}
	}


	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#getRenderablePoint(int, int)
	 */
	public RenderablePoint getRenderablePoint(int x, int y) {
		Iterator i = this.blocks.iterator();
		while(i.hasNext()) {
			Object r = i.next();
			if(r instanceof BoundableRenderable) {
				BoundableRenderable br = (BoundableRenderable) r;
				Rectangle bounds = br.getBounds();
				if(bounds.contains(x, y)) {
					RenderablePoint rp = br.getRenderablePoint(x - bounds.x, y - bounds.y);
					if(rp != null) {
						return rp;	
					}
				}
			}
		}
		return null;
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
		Iterator i = this.blocks.iterator();
		while(i.hasNext()) {
			Object r = i.next();
			if(r instanceof BoundableRenderable) {
				BoundableRenderable br = (BoundableRenderable) r;
				Rectangle bounds = br.getBounds();
				if(bounds.contains(x, y)) {
					br.onMouseClick(event, x - bounds.x, y - bounds.y);
				}
			}
		}
	}

	private BoundableRenderable armedRenderable = null;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMouseDisarmed(java.awt.event.MouseEvent)
	 */
	public void onMouseDisarmed(MouseEvent event) {
		BoundableRenderable ar = this.armedRenderable;
		if(ar != null) {
			ar.onMouseDisarmed(event);
			this.armedRenderable = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMousePressed(java.awt.event.MouseEvent, int, int)
	 */
	public void onMousePressed(MouseEvent event, int x, int y) {
		Iterator i = this.blocks.iterator();
		while(i.hasNext()) {
			Object r = i.next();
			if(r instanceof BoundableRenderable) {
				BoundableRenderable br = (BoundableRenderable) r;
				Rectangle bounds = br.getBounds();
				if(bounds.contains(x, y)) {
					br.onMousePressed(event, x - bounds.x, y - bounds.y);
					this.armedRenderable = br;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMouseReleased(java.awt.event.MouseEvent, int, int)
	 */
	public void onMouseReleased(MouseEvent event, int x, int y) {
		Iterator i = this.blocks.iterator();
		boolean found = false;
		while(i.hasNext()) {
			Object r = i.next();
			if(r instanceof BoundableRenderable) {
				BoundableRenderable br = (BoundableRenderable) r;
				Rectangle bounds = br.getBounds();
				if(bounds.contains(x, y)) {
					found = true;
					br.onMouseReleased(event, x - bounds.x, y - bounds.y);
			    	BoundableRenderable oldArmedRenderable = this.armedRenderable;
			    	if(oldArmedRenderable != null && br != oldArmedRenderable) {
			    		oldArmedRenderable.onMouseDisarmed(event);
			    		this.armedRenderable = null;
			    	}
				}
			}
		}
		if(!found) {
	    	BoundableRenderable oldArmedRenderable = this.armedRenderable;
	    	if(oldArmedRenderable != null) {
	    		oldArmedRenderable.onMouseDisarmed(event);
	    		this.armedRenderable = null;
	    	}
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RCollection#getRenderables()
	 */
	public Iterator getRenderables() {
		return this.blocks.iterator();
	}
}
