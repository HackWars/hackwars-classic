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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;

public class FontNameRenderState implements RenderState {
	private final RenderState prevRenderState;
	private final String fontName;
	
	public FontNameRenderState(RenderState prevRenderState, String fontName) {
		this.prevRenderState = prevRenderState;
		this.fontName = fontName;
	}

	private Font iFont;
	
	public Font getFont() {
		Font f = this.iFont;
		if(f != null) {
			return f;
		}
		Font parentFont = this.prevRenderState.getFont();
		f = new Font(this.fontName, parentFont.getStyle(), parentFont.getSize());
		this.iFont = f;
		return f;		
	}

	private FontMetrics iFontMetrics;
	
	public FontMetrics getFontMetrics() {
		FontMetrics fm = this.iFontMetrics;
		if(fm == null) {
			//TODO getFontMetrics deprecated. How to get text width?
			fm = Toolkit.getDefaultToolkit().getFontMetrics(this.getFont());
			this.iFontMetrics = fm;
		}
		return fm;
	}

	public Color getColor() {
		return this.prevRenderState.getColor();
	}

	public Color getTextBackgroundColor() {
		return this.prevRenderState.getTextBackgroundColor();
	}

	public Color getOverlayColor() {
		return this.prevRenderState.getOverlayColor();
	}

	public int getTextDecorationMask() {
		return this.prevRenderState.getTextDecorationMask();
	}

	public int getBlankWidth() {
		return this.prevRenderState.getBlankWidth();
	}

	public boolean isHighlight() {
		return this.prevRenderState.isHighlight();
	}

	public void setHighlight(boolean highlight) {
		this.prevRenderState.setHighlight(highlight);
	}

	public void repaint() {
		this.prevRenderState.repaint();
	}
	
	public void invalidate() {
		this.prevRenderState.invalidate();
	}
}
