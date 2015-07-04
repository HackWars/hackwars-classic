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
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.html2.HTMLImageElement;
import org.xamjwg.html.*;
import org.xamjwg.html.domimpl.RenderableContext;

public class HrControl extends BaseControl {
	private final RenderState renderState;
	
	public HrControl(RenderableContext renderableContext, RenderState renderState) {
		super(renderableContext);
		this.renderState = renderState;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension size = this.getSize();
		int offset = 8;
		int x = offset;
		int y = size.height / 2 - 1;
		int width = size.width - offset * 2;
		g.setColor(Color.black);
		g.drawRect(x, y, width, 2);
	}

	public boolean paintSelection(Graphics g, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		return inSelection;
	}		

	protected int getPreferredWidthImpl(int availWidth, int availHeight) {
		return availWidth;
	}
	
	protected int getPreferredHeightImpl(int availWidth, int availHeight) {
		FontMetrics fm = this.renderState.getFontMetrics();
		return fm.getHeight();
	}	
}
