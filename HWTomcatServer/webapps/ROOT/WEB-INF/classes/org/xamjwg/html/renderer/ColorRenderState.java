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

public class ColorRenderState implements RenderState {
	private final RenderState prevRenderState;
	private final Color color;
	
	public ColorRenderState(RenderState prevRenderState, Color color) {
		this.prevRenderState = prevRenderState;
		this.color = color;
	}

	public Font getFont() {
		return this.prevRenderState.getFont();
	}

	public FontMetrics getFontMetrics() {
		return this.prevRenderState.getFontMetrics();
	}

	public Color getColor() {
		return this.color;
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
