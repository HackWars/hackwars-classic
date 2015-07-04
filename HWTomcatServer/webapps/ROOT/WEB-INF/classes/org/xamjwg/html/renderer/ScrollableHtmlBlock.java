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

import javax.swing.*;
import org.xamjwg.html.*;

/**
 * @author J. H. S.
 */
public class ScrollableHtmlBlock extends HtmlBlock implements Scrollable {
	//private static final Logger logger = Logger.getLogger(WScrollableBody.class);
	private Dimension viewSize = null;
	
	public ScrollableHtmlBlock(HtmlParserContext pcontext, FrameContext frameContext) {
		super(0, Color.WHITE, null, false, pcontext, frameContext);
		this.setAutoscrolls(true);
	}

	public ScrollableHtmlBlock(Color bkgColor, boolean opaque, HtmlParserContext pcontext, FrameContext frameContext) {
		super(0, bkgColor, null, opaque, pcontext, frameContext);
		this.setAutoscrolls(true);
	}
	
	public Dimension getPreferredSize() {
		if(this.viewSize == null) {
			Container parent = this.getParent();
			if(parent != null) {
				return parent.getSize();
			}
			else {
				return new Dimension(0, 0);
			}
		}
		return this.viewSize;
	}
	
	public Dimension getPreferredScrollableViewportSize() {
		return this.getPreferredSize();
	}
	
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		FontMetrics fm = this.getFontMetrics(this.getFont());
		return fm.getHeight();
	}

	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getScrollableBlockIncrement(java.awt.Rectangle, int, int)
	 */
	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
		// TODO Page height
		return 400;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getScrollableTracksViewportWidth()
	 */
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getScrollableTracksViewportHeight()
	 */
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics arg0) {
		//long time1 = System.currentTimeMillis();
		//int numRenderables = 0;
		try {
			Object parent = this.getParent();
			if(parent instanceof JViewport) {
				JViewport vp = (JViewport) parent;
				Dimension viewSize = vp.getViewSize();
				Dimension extentSize = vp.getExtentSize();
				if(extentSize.width > viewSize.width || extentSize.height > viewSize.height) {
					Dimension newViewSize = new Dimension(viewSize);
					if(extentSize.width > viewSize.width) {
						newViewSize.width = extentSize.width;
					}
					if(extentSize.height > viewSize.height) {
						newViewSize.height = extentSize.height;
					}
					vp.setViewSize(newViewSize);
					vp.revalidate();
					return;
				}
			}
			super.paintComponent(arg0);
		} finally {
			//long time2 = System.currentTimeMillis();
			//System.out.println("ScrollableHtmlPanel.paintComponent(): numRenderables=" + numRenderables + ",time=" + (time2 - time1) + " ms.");
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#doLayout()
	 */
	public void doLayout() {
		if(!EventQueue.isDispatchThread()) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					ScrollableHtmlBlock.this.doLayout();
				}
			});
			return;
		}
		//long time1 = System.currentTimeMillis();
		try {
			Dimension containerSize = this.getSize();
			this.removeAll();
			this.rblock.setBounds(0, 0, containerSize.width, containerSize.height);
			Dimension size = this.rblock.layout(containerSize.width, containerSize.height);
			this.rblock.updateWidgetBounds();
			Object parent = this.getParent();
			if(parent instanceof JViewport) {
				JViewport vp = (JViewport) parent;
				Dimension oldViewSize = vp.getViewSize();
				Dimension newViewSize = new Dimension();
				Dimension extentSize = vp.getExtentSize();
				newViewSize.width = Math.max(extentSize.width, size.width);
				newViewSize.height = Math.max(extentSize.height, size.height);
				if(oldViewSize.width != newViewSize.width || oldViewSize.height != newViewSize.height) {
					this.viewSize = newViewSize;
					vp.setViewSize(newViewSize);
				}
			}
			else {
				//logger.warn("doLayout(): Parent not a JViewport");
			}
		} finally {
			//long time2 = System.currentTimeMillis();
			//System.out.println("doLayout(): time=" + (time2 - time1) + " ms.");
		}
	}
}
