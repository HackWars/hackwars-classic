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
 * Created on Jan 15, 2006
 */
package org.xamjwg.html.renderer;

import java.awt.Graphics;

import org.xamjwg.html.domimpl.InputContext;
import org.xamjwg.html.domimpl.RenderableContext;

public class BaseInputControl extends BaseControl implements InputContext {
	public BaseInputControl(RenderableContext renderableContext) {
		super(renderableContext);
		this.setOpaque(false);
		String sizeText = renderableContext.getAttribute("size");
		if(sizeText != null) {
			try {
				this.size = Integer.parseInt(sizeText);
			} catch(NumberFormatException nfe) {
				// ignore
			}
		}
	}
	
	protected int size = -1;
	
	protected int getPreferredWidthImpl(int availWidth, int availHeight) {
		int size = this.size;
		if(size == -1) {
			java.awt.Dimension ps = this.getPreferredSize();
			return ps == null ? 1 : ps.width;
		}
		else {
			return size;
		}
	}
	
	protected int getPreferredHeightImpl(int availWidth, int availHeight) {
		java.awt.Dimension ps = this.getPreferredSize();
		return ps == null ? 1 : ps.height;		
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#blur()
	 */
	public void blur() {
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#click()
	 */
	public void click() {
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#focus()
	 */
	public void focus() {
		this.requestFocus();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#getChecked()
	 */
	public boolean getChecked() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#getDisabled()
	 */
	public boolean getDisabled() {
		return !this.isEnabled();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#getMaxLength()
	 */
	public int getMaxLength() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#getReadOnly()
	 */
	public boolean getReadOnly() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#getTabIndex()
	 */
	public int getTabIndex() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#getValue()
	 */
	public String getValue() {
		return this.value;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#select()
	 */
	public void select() {
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#setChecked(boolean)
	 */
	public void setChecked(boolean checked) {
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#setDisabled(boolean)
	 */
	public void setDisabled(boolean disabled) {
		this.setEnabled(!disabled);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#setMaxLength(int)
	 */
	public void setMaxLength(int maxLength) {
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean readOnly) {
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#setSize(int)
	 */
	public void setControlSize(int size) {
		this.size = size;
		this.invalidate();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#setTabIndex(int)
	 */
	public void setTabIndex(int tabIndex) {
	}

	private String value;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#setValue(java.lang.String)
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#getTextSize()
	 */
	public int getControlSize() {
		return this.size;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#getCols()
	 */
	public int getCols() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#getRows()
	 */
	public int getRows() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#setCols(int)
	 */
	public void setCols(int cols) {
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.InputContext#setRows(int)
	 */
	public void setRows(int rows) {
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.UIControl#paintSelection(java.awt.Graphics, boolean, org.xamjwg.html.renderer.RenderablePoint, org.xamjwg.html.renderer.RenderablePoint)
	 */
	public boolean paintSelection(Graphics g, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		return inSelection;
	}		
}
