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
package org.xamjwg.html.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.*;

import org.xamjwg.html.domimpl.RenderableContext;

public abstract class BaseControl extends JComponent implements UIControl {
	protected final RenderableContext renderableContext;
	
	/**
	 * @param context
	 */
	public BaseControl(RenderableContext renderableContext) {
		this.renderableContext = renderableContext;
	}
	
	private int cachedPreferredWidth = -1;
	private int cachedPreferredHeight = -1;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.UIControl#getPreferredHeight()
	 */
	public int getPreferredHeight(int availWidth, int availHeight) {
		int ph = this.cachedPreferredHeight;
		if(ph != -1) {
			return ph;
		}
		ph = this.getPreferredHeightImpl(availWidth, availHeight);
		this.cachedPreferredHeight =  ph;
		return ph;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.UIControl#getPreferredWidth()
	 */
	public int getPreferredWidth(int availWidth, int availHeight) {
		int pw = this.cachedPreferredWidth;
		if(pw != -1) {
			return pw;
		}
		pw = this.getPreferredWidthImpl(availWidth, availHeight);
		this.cachedPreferredWidth =  pw;
		return pw;
	}
	
	protected int getPreferredWidthImpl(int availWidth, int availHeight) {
		HtmlLength width = this.renderableContext.getWidthLength();
		return width == null ? 1 : width.getLength(availWidth);
	}
	
	protected int getPreferredHeightImpl(int availWidth, int availHeight) {
		HtmlLength height = this.renderableContext.getHeightLength();
		return height == null ? 1 : height.getLength(availHeight);
	}
	
	private Dimension cachedPreferredSize;
	
	public Dimension getPreferredSize() {
		Dimension ps = this.cachedPreferredSize;
		if(ps != null) {
			return ps;
		}
		ps = super.getPreferredSize();
		this.cachedPreferredSize = ps;
		return ps;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getAlignmentX()
	 */
	public float getAlignmentX() {
		return this.renderableContext.getAlignmentX();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getAlignmentY()
	 */
	public float getAlignmentY() {
		return this.renderableContext.getAlignmentY();
	}

	public void invalidate() {
		super.invalidate();
		this.cachedPreferredSize = null;
		this.cachedPreferredWidth = -1;
		this.cachedPreferredHeight = -1;
	}
	
	public void invalidateAndRepaint() {
		Component c = this;
		while(c != null && !(c instanceof ScrollableHtmlBlock)) {
			c = c.getParent();
		}
		if(c instanceof ScrollableHtmlBlock) {
			ScrollableHtmlBlock panel = (ScrollableHtmlBlock) c;
			panel.revalidate();
			panel.repaint();
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.UIControl#getBackgroundColor()
	 */
	public Color getBackgroundColor() { 
		return this.getBackground();
	}
}
