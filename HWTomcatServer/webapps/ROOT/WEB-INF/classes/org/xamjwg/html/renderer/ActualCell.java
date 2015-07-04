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
 * Created on Dec 3, 2005
 */
package org.xamjwg.html.renderer;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Dimension;

import org.xamjwg.html.domimpl.*;
import org.xamjwg.html.*;
import org.xamjwg.util.ColorFactory;

public class ActualCell extends RBlock {
	private final HTMLTableCellElementImpl cellElement;
	private VirtualCell topLeftVirtualCell; 
	
	/**
	 * @param element
	 */
	public ActualCell(HTMLTableCellElementImpl element, int cellPadding, RenderState parentRenderState, HtmlParserContext pcontext, FrameContext frameContext, RenderableContainer parentContainer, Renderable parent) {
		super(0, parentRenderState, pcontext, frameContext, parentContainer, parent);
		this.cellElement = element;
		this.setRootNode(element);
		HTMLTableRowElementImpl rowElement = null;
		Object parentNode = element.getParentNode();
		if(parentNode instanceof HTMLTableRowElementImpl) {
			 rowElement = (HTMLTableRowElementImpl) parentNode;
		}
		String bgColor = element.getBgColor();
		if(bgColor == null || "".equals(bgColor)) {
			if(rowElement != null) {
				bgColor = rowElement.getBgColor();
			}
		}
		if(bgColor != null && !"".equals(bgColor)) {
			Color bgc = ColorFactory.getInstance().getColor(bgColor);
			this.backgroundColor = bgc;
		}
		if(this.loadingBackgroundImage == null) {
			String background = element.getAttribute("background");
			if(background != null && !"".equals(background)) {
				this.loadingBackgroundImage = background;
				this.loadBackgroundImage(background);
			}
		}
		int alignXPercent;
		String align = element.getAlign();
		if(align == null || "".equals(align)) {
			if(rowElement != null) {
				align = rowElement.getAlign();
			}
		}
		if(align == null || "".equals(align)) {
			if("TH".equalsIgnoreCase(element.getNodeName())) {
				alignXPercent = 50;
			}
			else {
				alignXPercent = 0;
			}					
		}
		else if("center".equalsIgnoreCase(align) || "middle".equalsIgnoreCase(align)) {
			alignXPercent = 50;
		}
		else if("left".equalsIgnoreCase(align)) {
			alignXPercent = 0;
		}
		else if("right".equalsIgnoreCase(align)) {
			alignXPercent = 100;
		}
		else {
			//TODO: justify, char
			alignXPercent = 0;
		}
		
		int alignYPercent;
		String valign = element.getVAlign();
		if(valign == null || "".equals(valign)) {
			if(rowElement != null) {
				valign = rowElement.getVAlign();
			}
		}
		if(valign == null || "".equals(valign)) {
			alignYPercent = 50;
		}
		else if("top".equalsIgnoreCase(valign)) {
			alignYPercent = 0;
		}
		else if("middle".equalsIgnoreCase(valign) || "center".equalsIgnoreCase(valign)) {
			alignYPercent = 50;
		}
		else if("bottom".equalsIgnoreCase(valign)) {
			alignYPercent = 100;
		}
		else {
			//TODO: baseline
			alignYPercent = 50;
		}
		this.setAlignXPercent(alignXPercent);
		this.setAlignYPercent(alignYPercent);
		if(this.paddingInsets == null) {
			this.paddingInsets = new Insets(cellPadding, cellPadding, cellPadding, cellPadding);
		}
	}
	
	public void setTopLeftVirtualCell(VirtualCell vc) {
		this.topLeftVirtualCell = vc;
	}
	
	public VirtualCell getTopLeftVirtualCell() {
		return this.topLeftVirtualCell;		
	}
	
	private int colSpan = -1;
	private int rowSpan = -1;

	/**
	 * @return Returns the virtualColumn.
	 */
	public int getVirtualColumn() {
		VirtualCell vc = this.topLeftVirtualCell;
		return vc == null ? 0 : vc.getColumn();
	}

	/**
	 * @return Returns the virtualRow.
	 */
	public int getVirtualRow() {
		VirtualCell vc = this.topLeftVirtualCell;
		return vc == null ? 0 : vc.getRow();
	}

	public int getColSpan() {
		int cs = this.colSpan;
		if(cs == -1) {
			cs = this.cellElement.getColSpan();
			if(cs < 1) {
				cs = 1;
			}
			this.colSpan = cs;
		}
		return cs;
	}
	
	public int getRowSpan() {
		int rs = this.rowSpan;
		if(rs == -1) {
			rs = this.cellElement.getRowSpan();
			if(rs < 1) {
				rs = 1;
			}
			this.rowSpan = rs;
		}
		return rs;
	}

	public String getHeightText() {
		return this.cellElement.getHeight();
	}

	public String getWidthText() {
		return this.cellElement.getWidth();
	}
	
//	public Dimension layoutMinWidth() {
//		
//		return this.panel.layoutMinWidth();
//		
//	}
//
	private int lastLayoutWidth;
	private int lastLayoutHeight;
	
	protected Dimension layoutFor(int width, int height) {
		Dimension size = super.layoutFor(width, height);
		this.lastLayoutWidth = size.width;
		this.lastLayoutHeight = size.height;
		return size;
	}
	
	public int getLastLayoutWidth() {
		return this.lastLayoutWidth;
	}
	
	public int getLastLayoutHeight() {
		return this.lastLayoutHeight;
	}
	public void doLayout(TableMatrix.SizeInfo[] colSizes, TableMatrix.SizeInfo[] rowSizes, int hasBorder, int cellSpacingX, int cellSpacingY) {
		int vcol = this.getVirtualColumn();
		int vrow = this.getVirtualRow();
		TableMatrix.SizeInfo colSize = colSizes[this.getVirtualColumn()];
		TableMatrix.SizeInfo rowSize = rowSizes[this.getVirtualRow()];
		int x = colSize.offset;
		int y = rowSize.offset;
		int width;
		int height;
		int colSpan = this.getColSpan();
		if(colSpan > 1) {
			width = 0;
			for(int i = 0; i < colSpan; i++) {
				int vc = vcol + i;
				width += colSizes[vc].actualSize;
				if(i + 1 < colSpan) {
					width += cellSpacingX + hasBorder * 2;
				}
			}
		}
		else {
			width = colSizes[vcol].actualSize;
		}
		int rowSpan = this.getRowSpan();
		if(rowSpan > 1) {
			height = 0;
			for(int i = 0; i < rowSpan; i++) {
				int vr = vrow + i;
				height += rowSizes[vr].actualSize;
				if(i + 1 < rowSpan) {
					height += cellSpacingY + hasBorder * 2;
				}
			}
		}
		else {
			height = rowSizes[vrow].actualSize;
		}
		this.setBounds(x, y, width, height);
		//TODO: Reposition components
	}
}
