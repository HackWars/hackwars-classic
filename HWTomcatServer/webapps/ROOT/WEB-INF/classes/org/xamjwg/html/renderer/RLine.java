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

import org.xamjwg.html.domimpl.RenderableContext;

/**
 * @author J. H. S.
 */
public class RLine extends BaseBoundableRenderable implements RCollection {
	private final ArrayList renderables = new ArrayList(8);
	private final RenderState startRenderState;
	private final int availHeight;
	private RenderState currentRenderState;
	private int baseLineOffset;
	private int desiredMaxWidth;
	
	public RLine(RenderableContainer container, RenderState lastRenderState, int availHeight, int x, int y, int desiredMaxWidth, int height) {
		super(container);
		Rectangle b = this.bounds;
		b.x = x;
		b.y = y;
		b.width = 0;
		b.height = height;
		this.desiredMaxWidth = desiredMaxWidth;
		this.startRenderState = lastRenderState;
		this.currentRenderState = lastRenderState;
		this.availHeight = availHeight;
	}
	
//	public final void shiftContentsX(int shiftBy) {
//		ArrayList renderables = this.renderables;
//		int numRenderables = renderables.size();
//		BoundableRenderable lastRenderable = null;
//		for(int i = 0; i < numRenderables; i++) {
//			Object r = renderables.get(i);
//			if(r instanceof BoundableRenderable) {
//				BoundableRenderable br = (BoundableRenderable) r;
//				Rectangle oldBounds = br.getBounds();
//				lastRenderable = br;
//				br.setX(oldBounds.x + shiftBy);
//				if(r instanceof RUIControl) {
//					((RUIControl) r).updateWidgetBounds();
//				}
//			}
//		}
//		if(lastRenderable != null) {
//			Rectangle rbounds = lastRenderable.getBounds();
//			this.bounds.x = rbounds.x + rbounds.width;
//		}
//	}
		
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.markup.Renderable#getBounds()
	 */
	public Rectangle getBounds() {
		return this.bounds;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.markup.Renderable#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		Iterator i = this.renderables.iterator();
		while(i.hasNext()) {
			Object r = i.next();
			if(r instanceof BoundableRenderable) {
				BoundableRenderable br = (BoundableRenderable) r;
				Rectangle bounds = br.getBounds();
				Graphics newG = g.create(bounds.x, bounds.y, bounds.width, bounds.height);
				br.paint(newG);
			}
			else {
				((Renderable) r).paint(g);
			}
		}
	}
		
	public final void addRenderState(RenderState rs) {
		this.renderables.add(new RStyleChanger(rs));
		this.currentRenderState = rs;
	}
	
	public final void addStyleChanger(RStyleChanger sc) {
		this.renderables.add(sc);
		this.currentRenderState = sc.getRenderState();
	}
	
//	public boolean couldAdd(int width) {
//		int offset = this.offset;
//		if(offset == 0) {
//			return true;
//		}
//		return offset + width <= this.bounds.width;
//	}
//	
		
	public final void add(Renderable renderable) throws OverflowException {
		if(renderable instanceof RWord) {
			this.addWord((RWord) renderable);
		}
		else if(renderable instanceof RBlank) {
			this.addBlank((RBlank) renderable);
		}
		else if(renderable instanceof RElement) {
			this.addElement((RElement) renderable);
		}
		else if(renderable instanceof RUIControl) {
			this.addUIControl((RUIControl) renderable);
		}
		else if(renderable instanceof RStyleChanger) {
			this.addStyleChanger((RStyleChanger) renderable);
		}
		else {
			throw new IllegalArgumentException("Can't add " + renderable);
		}
	}
	
	public final void addWord(RWord wordInfo) throws OverflowException {
		// Check if it fits horzizontally
		int offset = this.bounds.width;
		int wiwidth = wordInfo.width;
		if(offset != 0 && offset + wiwidth > this.desiredMaxWidth) {
			ArrayList renderables = this.renderables;
			ArrayList overflow = null;
			boolean cancel = false;
			for(int i = renderables.size(); --i >= 0;) {
				Renderable renderable = (Renderable) renderables.get(i);
				if(renderable instanceof RWord || !(renderable instanceof BoundableRenderable)) {
					if(overflow == null) {
						overflow = new ArrayList();
					}
					if(renderable != wordInfo && renderable instanceof RWord && ((RWord) renderable).getBounds().x == 0) {
						// Can't overflow words starting at offset zero.
						// Note that all or none should be overflown.
						cancel = true;
						// No need to set offset - set later.
						break;
					}
					if(renderable instanceof RWord) {
						int newOffset = ((RWord) renderable).getBounds().x;
						this.bounds.width = newOffset;
					}
					overflow.add(0, renderable);
					renderables.remove(i);
				}
				else {
					break;
				}
			}
			if(!cancel) {
				if(overflow == null) {
					throw new OverflowException(Collections.singleton(wordInfo));
				}
				else {
					overflow.add(wordInfo);
					throw new OverflowException(overflow);
				}
			}
		}

		// Add it
		this.renderables.add(wordInfo);
		wordInfo.setParent(this);
		
		int extraHeight = 0;
		int maxDescent = this.bounds.height - this.baseLineOffset;
		if(wordInfo.descent > maxDescent) {
			extraHeight += (wordInfo.descent - maxDescent);
		}
		int maxAscentPlusLeading = this.baseLineOffset;
		if(wordInfo.ascentPlusLeading > maxAscentPlusLeading) {
			extraHeight += (wordInfo.ascentPlusLeading - maxAscentPlusLeading);
		}
		if(extraHeight > 0) {
			this.adjustHeight(this.bounds.height + extraHeight, 0.0f);
		}
		else {
			int x = offset;
			offset += wiwidth;
			this.bounds.width = offset;
			wordInfo.setBounds(x, this.baseLineOffset - wordInfo.ascentPlusLeading, wiwidth, wordInfo.height);
		}
	}

	public final void addBlank(RenderableContext me) {
		//NOTE: Blanks may be added without concern for wrapping (?)
		int x = this.bounds.width;
		if(x > 0) {
			RenderState rs = this.currentRenderState;
			FontMetrics fm = rs.getFontMetrics();
			int width = fm.charWidth(' ');
			RBlank rblank = new RBlank(me, rs, fm, width, this.container);
			rblank.setBounds(x, this.baseLineOffset - rblank.ascentPlusLeading, width, rblank.height);
			this.renderables.add(rblank);
			rblank.setParent(this);
			this.bounds.width = x + width;
		}
	}

	public final void addBlank(RBlank rblank) {
		//NOTE: Blanks may be added without concern for wrapping (?)
		int x = this.bounds.width;
		RenderState rs = this.currentRenderState;
		FontMetrics fm = rs.getFontMetrics();
		int width = fm.charWidth(' ');
		rblank.setBounds(x, this.baseLineOffset - rblank.ascentPlusLeading, width, rblank.height);
		this.renderables.add(rblank);
		rblank.setParent(this);
		this.bounds.width = x + width;
	}

	private final void layoutUIControl(RUIControl rwidget, int x) {
		UIControl widget = rwidget.widget;
		//int boundsw = this.bounds.width;
		int desiredMaxWidth = this.desiredMaxWidth;
		int componentHeight = this.availHeight;
		int pw = widget.getPreferredWidth(desiredMaxWidth, componentHeight);
		if(pw == -1) {
			pw = desiredMaxWidth - x;
		}
		int ph = widget.getPreferredHeight(desiredMaxWidth, componentHeight);
		if(ph == -1) {
			Dimension ps = widget.getPreferredSize();
			ph = ps.height;
			int charHeight = this.currentRenderState.getFontMetrics().getHeight();
			if(ph < charHeight) {
				ph = charHeight;
			}
		}
		this.layoutUIControl(rwidget, x, pw, ph);		
	}

	private final void layoutUIControl(RUIControl rwidget, int x, int width, int height) {
		UIControl widget = rwidget.widget;
		int yoffset;
		if(height >= this.bounds.height) {
			yoffset = 0;
		}
		else {
			yoffset = (int) ((this.bounds.height - height) * widget.getAlignmentY());
		}
		// This also sets bounds in the AWT component
		rwidget.setBounds(x, yoffset, width, height);
	}
	
	public final void addUIControl(RUIControl rwidget) throws OverflowException {
		// Check if it fits horizontally
		rwidget.setParent(this);
		UIControl widget = rwidget.widget;
		int boundsw = this.bounds.width;
		int desiredMaxWidth = this.desiredMaxWidth;
		int componentHeight = this.availHeight;
		int pw = widget.getPreferredWidth(desiredMaxWidth, componentHeight);
		int offset = boundsw;
		if(pw == -1) {
			pw = desiredMaxWidth - offset;
		}
		if(offset != 0 && offset + pw > desiredMaxWidth) {
			throw new OverflowException(Collections.singleton(rwidget));
		}
		this.renderables.add(rwidget);
		
		int boundsh = this.bounds.height;
		int ph = widget.getPreferredHeight(desiredMaxWidth, componentHeight);
		if(ph == -1) {
			Dimension ps = widget.getPreferredSize();
			ph = ps.height;
			int charHeight = this.currentRenderState.getFontMetrics().getHeight(); 
			if(ph < charHeight) {
				ph = charHeight;
			}
		}
		if(ph > boundsh) {
			this.adjustHeight(ph, widget.getAlignmentY());
		}
		else {
			int prevOffset = boundsw;
			this.layoutUIControl(rwidget, prevOffset, pw, ph);
			int newX = prevOffset + pw;
			this.bounds.width = newX;
		}
	}
	
	private final void layoutElement(RElement rwidget, int x) {
		int desiredMaxWidth = this.desiredMaxWidth;
		int componentHeight = this.availHeight;
		Dimension layoutSize = rwidget.layout(desiredMaxWidth, componentHeight);
		this.layoutElement(rwidget, x, layoutSize.width, layoutSize.height);		
	}

	private final void layoutElement(RElement rwidget, int x, int width, int height) {
		int yoffset;
		if(height >= this.bounds.height) {
			yoffset = 0;
		}
		else {
			yoffset = (int) ((this.bounds.height - height) * rwidget.getAlignmentY());
		}
		rwidget.setBounds(x, yoffset, width, height);
	}
	
	public final void addElement(RElement rwidget) throws OverflowException {
		rwidget.setParent(this);
		// Check if it fits horizontally
		int boundsw = this.bounds.width;
		int desiredMaxWidth = this.desiredMaxWidth;
		int componentHeight = this.availHeight;
		Dimension layoutSize = rwidget.layout(desiredMaxWidth, componentHeight);
		int pw = layoutSize.width;
		int offset = boundsw;
		if(offset != 0 && offset + pw > desiredMaxWidth) {
			throw new OverflowException(Collections.singleton(rwidget));
		}
		//Note: Renderable for widget doesn't paint the widget, but
		//it's needed for height readjustment.
		this.renderables.add(rwidget);
		
		int boundsh = this.bounds.height;
		int ph = layoutSize.height;
		if(ph > boundsh) {
			this.adjustHeight(ph, rwidget.getAlignmentY());
		}
		else {
			int prevOffset = boundsw;
			this.layoutElement(rwidget, prevOffset, pw, ph);
			int newX = prevOffset + rwidget.getBounds().width;
			this.bounds.width = newX;
		}		
	}

	private void adjustHeight(int newHeight, float alignmentY) {
		// Set new line height
		this.bounds.height = newHeight;
		Renderable[] rarray = (Renderable[]) this.renderables.toArray(Renderable.EMPTY_ARRAY);
		int rlength = rarray.length;
		
		// Find max baseline 
		int maxDescent = 0;
		for(int i = 0; i < rlength; i++) {
			Renderable r = rarray[i];
			if(r instanceof RWord) {
				RWord rword = (RWord) r;
				int descent = rword.descent;
				if(descent > maxDescent) {
					maxDescent = descent;
				}
			}
		}
		
		// Find max ascent 
		int maxAscentPlusLeading = 0;
		for(int i = 0; i < rlength; i++) {
			Renderable r = rarray[i];
			if(r instanceof RWord) {
				RWord rword = (RWord) r;
				int ascentPlusLeading = rword.ascentPlusLeading;
				if(ascentPlusLeading > maxAscentPlusLeading) {
					maxAscentPlusLeading = ascentPlusLeading;
				}
			}
		}
		
		int maxBaseline = newHeight - maxDescent;
		int minBaseline = maxAscentPlusLeading;
		
		//TODO What if the descent is huge and the ascent tiny?
		int baseline = (int) (minBaseline + alignmentY * (maxBaseline - minBaseline));
		this.baseLineOffset = baseline;
		
		// Change bounds of renderables accordingly
		int x = 0;
		this.currentRenderState = this.startRenderState;
		for(int i = 0; i < rlength; i++) {
			Renderable r = rarray[i];
			if(r instanceof RWord) {
				RWord rword = (RWord) r;
				int w = rword.width;
				rword.setBounds(x, baseline - rword.ascentPlusLeading, w, rword.height);
				x += w;
			}
			else if(r instanceof RBlank) {
				RBlank rblank = (RBlank) r;
				int w = rblank.width;
				rblank.setBounds(x, baseline - rblank.ascentPlusLeading, w, rblank.height);
				x += w;
			}
			else if(r instanceof RUIControl) {
				RUIControl rwidget = (RUIControl) r;
				this.layoutUIControl(rwidget, x);
				x += rwidget.getBounds().width;
			}
			else if(r instanceof RElement) {
				RElement rwidget = (RElement) r;
				this.layoutElement(rwidget, x);
				x += rwidget.getBounds().width;
			}
			else if(r instanceof RStyleChanger) {
				this.currentRenderState = ((RStyleChanger) r).getRenderState();
			}
		}
		this.bounds.width = x;
		//TODO: Could throw OverflowException when we add floating widgets
	}
	
	public void onMouseClick(java.awt.event.MouseEvent event, int x, int y) {
		Renderable[] rarray = (Renderable[]) this.renderables.toArray(Renderable.EMPTY_ARRAY);
		BoundableRenderable r = MarkupUtilities.findRenderable(rarray, x, y, false);
		if(r != null) {
			Rectangle rbounds = r.getBounds();
			r.onMouseClick(event, x - rbounds.x, y - rbounds.y);
		}
	}
    private BoundableRenderable mousePressTarget;
    
	public void onMousePressed(java.awt.event.MouseEvent event, int x, int y) {
		Renderable[] rarray = (Renderable[]) this.renderables.toArray(Renderable.EMPTY_ARRAY);
		BoundableRenderable r = MarkupUtilities.findRenderable(rarray, x, y, false);
		if(r != null) {
			this.mousePressTarget = r;
			Rectangle rbounds = r.getBounds();
			r.onMousePressed(event, x - rbounds.x, y - rbounds.y);
		}
	}
	
	public RenderablePoint getRenderablePoint(int x, int y) {
		Renderable[] rarray = (Renderable[]) this.renderables.toArray(Renderable.EMPTY_ARRAY);
		BoundableRenderable br = MarkupUtilities.findRenderable(rarray, x, y, false);
		if(br != null) {
			Rectangle rbounds = br.getBounds();
			return br.getRenderablePoint(x - rbounds.x, y - rbounds.y);
		}
		else {
			return null;
		}
	}

	public void onMouseReleased(java.awt.event.MouseEvent event, int x, int y) {
		Renderable[] rarray = (Renderable[]) this.renderables.toArray(Renderable.EMPTY_ARRAY);
		BoundableRenderable r = MarkupUtilities.findRenderable(rarray, x, y, false);
		if(r != null) {
			Rectangle rbounds = r.getBounds();
			r.onMouseReleased(event, x - rbounds.x, y - rbounds.y);
	    	BoundableRenderable oldArmedRenderable = this.mousePressTarget;
	    	if(oldArmedRenderable != null && r != oldArmedRenderable) {
	    		oldArmedRenderable.onMouseDisarmed(event);
	    		this.mousePressTarget = null;
	    	}
		}
		else {
	    	BoundableRenderable oldArmedRenderable = this.mousePressTarget;
	    	if(oldArmedRenderable != null) {
	    		oldArmedRenderable.onMouseDisarmed(event);
	    		this.mousePressTarget = null;
	    	}			
		}
	}
	
	public void onMouseDisarmed(java.awt.event.MouseEvent event) {
		BoundableRenderable target = this.mousePressTarget;
		if(target != null) {
			this.mousePressTarget = null;
			target.onMouseDisarmed(event);
		}
	}
	
	public Color getPaneColor() {
		return this.container.getBackground();
	}
	
	public final void adjustHorizontalBounds(int newX, int newMaxWidth) throws OverflowException {
		this.bounds.x = newX;
		this.desiredMaxWidth = newMaxWidth;
		int topX = newX + newMaxWidth;
		ArrayList renderables = this.renderables;
		int size = renderables.size();
		ArrayList overflown = null;
		Rectangle lastInLine = null;
		for(int i = 0; i < size; i++) {
			Object r = renderables.get(i);
			if(overflown == null) {
				if(r instanceof BoundableRenderable) {
					BoundableRenderable br = (BoundableRenderable) r;
					Rectangle brb = br.getBounds();
					int x2 = brb.x + brb.width;
					if(x2 > topX) {
						overflown = new ArrayList(1);
					}
					else {
						lastInLine = brb;
					}
				}
			}
			/* must not be else here */
			if(overflown != null) {
				//TODO: This could break a word across markup boundary.
				overflown.add(r);
				renderables.remove(i--);
				size--;
			}
		}
		if(overflown != null) {
			if(lastInLine != null) {
				this.bounds.width = lastInLine.x + lastInLine.width;
			}
			throw new OverflowException(overflown);
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#invalidateState(org.xamjwg.html.renderer.RenderableContext)
	 */
	public void invalidateState(RenderableContext context) {
		Object[] renderables = this.renderables.toArray();
		int length = renderables.length;
		for(int i = 0; i < length; i++) {
			Object r = renderables[i];
			if(r instanceof BoundableRenderable) {
				((BoundableRenderable) r).invalidateState(context);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#paintSelection(java.awt.Graphics, boolean, org.xamjwg.html.renderer.RenderablePoint, org.xamjwg.html.renderer.RenderablePoint)
	 */
	public boolean paintSelection(Graphics g, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		Iterator i = this.renderables.iterator();
		if(!inSelection) {
			BoundableRenderable startR = startPoint.renderable;
			BoundableRenderable endR = endPoint.renderable;
			while(i.hasNext()) {
				Object r = i.next();
				if(r instanceof RElement || r == startR || r == endR) {
					BoundableRenderable br = (BoundableRenderable) r;
					Rectangle bounds = br.getBounds();
					Graphics newG = g.create(bounds.x, bounds.y, bounds.width, bounds.height);
					boolean newInSelection = br.paintSelection(newG, inSelection, startPoint, endPoint);
					if(newInSelection != inSelection || r == startR || r == endR) {
						inSelection = newInSelection;
						break;
					}
				}
			}					
		}
		if(inSelection) {
			//TODO: Could be optimized by just scanning
			//for renderable and painting at the line level.
			while(i.hasNext()) {
				Object r = i.next();
				if(r instanceof BoundableRenderable) {
					BoundableRenderable br = (BoundableRenderable) r;
					Rectangle bounds = br.getBounds();
					Graphics newG = g.create(bounds.x, bounds.y, bounds.width, bounds.height);
					if(!br.paintSelection(newG, inSelection, startPoint, endPoint)) {
						inSelection = false;
						break;
					}
				}
			}
		}
		return inSelection;
	}
	
	public boolean extractSelectionText(StringBuffer buffer, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		Iterator i = this.renderables.iterator();
		if(!inSelection) {
			BoundableRenderable startR = startPoint.renderable;
			BoundableRenderable endR = endPoint.renderable;
			while(i.hasNext()) {
				Object r = i.next();
				if(r instanceof RElement || r == startR || r == endR) {
					BoundableRenderable br = (BoundableRenderable) r;
					boolean newInSelection = br.extractSelectionText(buffer, inSelection, startPoint, endPoint);
					if(newInSelection != inSelection || r == startR || r == endR) {
						inSelection = newInSelection;
						break;
					}
				}
			}					
		}
		if(inSelection) {
			//TODO: Could be optimized by just scanning
			//for renderable and painting at the line level.
			while(i.hasNext()) {
				Object r = i.next();
				if(r instanceof BoundableRenderable) {
					BoundableRenderable br = (BoundableRenderable) r;
					if(!br.extractSelectionText(buffer, inSelection, startPoint, endPoint)) {
						inSelection = false;
						break;
					}
				}
			}
		}
		return inSelection;
	}

	public void invalidate() {
		Iterator i = this.getRenderables();
		while(i.hasNext()) {
			Object r = i.next();
			if(r instanceof RCollection) {
				((RCollection) r).invalidate();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RCollection#getRenderables()
	 */
	public Iterator getRenderables() {
		return this.renderables.iterator();
	}
	
	public void updateWidgetBounds() {
		Iterator i = this.getRenderables();
		while(i.hasNext()) {
			Object r = i.next();
			if(r instanceof RCollection) {
				((RCollection) r).updateWidgetBounds();
			}
			else if(r instanceof RUIControl) {
				((RUIControl) r).updateWidgetBounds();
			}
		}
	}
}
