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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import org.xamjwg.html.domimpl.RenderableContext;
import org.xamjwg.util.WrapperLayout;

public class InputTextAreaControl extends BaseInputControl {
	private final JTextComponent widget;
	
	public InputTextAreaControl(RenderableContext renderableContext) {
		super(renderableContext);
		this.setLayout(WrapperLayout.getInstance());
		String colsStr = renderableContext.getAttribute("cols");
		if(colsStr != null) {
			try {
				this.setCols(Integer.parseInt(colsStr));
			} catch(NumberFormatException nfe) {
				// ignore
			}
		}
		String rowsStr = renderableContext.getAttribute("rows");
		if(rowsStr != null) {
			try {
				this.setRows(Integer.parseInt(rowsStr));
			} catch(NumberFormatException nfe) {
				// ignore
			}
		}		
		String value = renderableContext.getTextContent();
		JTextComponent widget = this.createTextField(value);
		this.widget = widget;
		this.add(new JScrollPane(widget));
	}

	protected JTextComponent createTextField(String value) {
		JTextArea textArea = new JTextArea();
		textArea.setText(value);
		return textArea;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BaseInputControl#getCols()
	 */
	public int getCols() {
		return this.cols;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BaseInputControl#getRows()
	 */
	public int getRows() {
		return this.rows;
	}

	private int cols = -1;
	private int rows = -1;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BaseInputControl#setCols(int)
	 */
	public void setCols(int cols) {
		this.cols = cols;
		this.invalidate();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BaseInputControl#setRows(int)
	 */
	public void setRows(int rows) {
		this.rows = rows;
		this.invalidate();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BaseInputTextControl#getPreferredWidthImpl(int, int)
	 */
	protected int getPreferredWidthImpl(int availWidth, int availHeight) {
		int cols = this.cols;
		if(cols == -1) {
			return 100;
		}
		else {
			Font f = this.widget.getFont();
			FontMetrics fm = this.widget.getFontMetrics(f);
			Insets insets = this.widget.getInsets();
			return insets.left + insets.right + fm.charWidth('*') * cols;
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BaseInputControl#getPreferredHeightImpl(int, int)
	 */
	protected int getPreferredHeightImpl(int availWidth, int availHeight) {
		int rows = this.rows;
		if(rows == -1) {
			return 100;
		}
		else {
			Font f = this.widget.getFont();
			FontMetrics fm = this.widget.getFontMetrics(f);
			Insets insets = this.widget.getInsets();
			return insets.top + insets.bottom + fm.getHeight() * rows;
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BaseInputControl#getReadOnly()
	 */
	public boolean getReadOnly() {
		return !this.widget.isEditable();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BaseInputControl#getValue()
	 */
	public String getValue() {
		return this.widget.getText();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BaseInputControl#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean readOnly) {
		this.widget.setEditable(readOnly);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BaseInputControl#setValue(java.lang.String)
	 */
	public void setValue(String value) {
		this.widget.setText(value);
	}	
}
