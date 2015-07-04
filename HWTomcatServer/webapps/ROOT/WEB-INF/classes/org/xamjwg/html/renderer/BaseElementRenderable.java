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

import java.awt.Rectangle;
import java.util.Iterator;

public abstract class BaseElementRenderable extends BaseBoundableRenderable implements RElement {
	protected boolean invalid = true;
	
	public BaseElementRenderable(RenderableContainer container) {
		super(container);
	}

	public float getAlignmentX() {
		return 0.0f;
	}

	public float getAlignmentY() {
		return 0.0f;
	}
	
	public void invalidate() {
		if(!this.invalid) {
			this.invalid = true;
			Iterator i = this.getRenderables();
			while(i.hasNext()) {
				Object r = i.next();
				if(r instanceof RCollection) {
					((RCollection) r).invalidate();
				}
			}
		}
	}
	
	public void markValidated() {
		this.invalid = false;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BaseBoundableRenderable#setBounds(int, int, int, int)
	 */
	public void setBounds(int x, int y, int width, int height) {
		Rectangle oldBounds = this.bounds;
		boolean shifted = oldBounds.x != x || oldBounds.y != y;
		super.setBounds(x, y, width, height);
		if(shifted) {
			this.updateWidgetBounds();
		}
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
