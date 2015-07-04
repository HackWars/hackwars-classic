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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.*;

import org.w3c.dom.Node;
import org.w3c.dom.html2.HTMLTableCellElement;
import org.w3c.dom.html2.HTMLTableRowElement;
import org.xamjwg.html.domimpl.*;
import org.xamjwg.html.*;

public class TableMatrix {
    private static final NodeFilter ROWS_FILTER = new RowsFilter();
    private static final NodeFilter COLUMNS_FILTER = new ColumnsFilter();
    private final ArrayList ROWS = new ArrayList();
    private final ArrayList ALL_CELLS = new ArrayList();
    private final ArrayList ROW_ELEMENTS = new ArrayList();
    private final HTMLTableElementImpl tableElement;
    private final HtmlParserContext parserContext;
    private final FrameContext frameContext;
    private final RElement relement;
    private final RenderableContainer container;
    
    private SizeInfo[] columnSizes;
    private SizeInfo[] rowSizes;
    private int tableWidth;
    private int tableHeight;

    /**
	 * @param element
	 */
    public TableMatrix(HTMLTableElementImpl element, HtmlParserContext pcontext, FrameContext frameContext, RenderableContainer container, RElement relement) {
		this.tableElement = element;
		this.parserContext = pcontext;
		this.frameContext = frameContext;
		this.relement = relement;
		this.container = container;
		this.reset();
	}
    
	/**
	 * @return Returns the tableHeight.
	 */
	public int getTableHeight() {
		return this.tableHeight;
	}
	/**
	 * @return Returns the tableWidth.
	 */
	public int getTableWidth() {
		return this.tableWidth;
	}
	private int border;
	private int cellSpacingY;
	private int cellSpacingX;
	private int widthsOfExtras;
	private int heightsOfExtras;
	private HtmlLength tableWidthLength;
		
	/**
	 * Note: called by constructor. Builds a matrix with element-based info.
	 */
	public void reset() {
		ROWS.clear();
		ALL_CELLS.clear();
		ROW_ELEMENTS.clear();
		String borderText = this.tableElement.getBorder();
		int border = 0;
		if(borderText != null) {
			try {
				border = Integer.parseInt(borderText);
				if(border < 0) {
					border = 0;
				}
			} catch(NumberFormatException nfe) {
				// ignore
			}
		}
		this.border = border;
		String cellSpacingText = this.tableElement.getCellSpacing();
		int cellSpacing = 1;
		if(cellSpacingText != null) {
			try {
				//TODO: cellSpacing can be a percentage as well
				cellSpacing = Integer.parseInt(cellSpacingText);
				if(cellSpacing < 0) {
					cellSpacing = 0;
				}
			} catch(NumberFormatException nfe) {
				// ignore
			}
		}
		this.cellSpacingX = cellSpacing;
		this.cellSpacingY = cellSpacing;

		this.populateRows();
		this.adjustForCellSpans();
		this.createSizeArrays();		
		
		String tableWidthText = this.tableElement.getWidth();
		HtmlLength tableWidthLength = null;
		if(tableWidthText != null) {
			try {
				tableWidthLength = new HtmlLength(tableWidthText);
			} catch(Exception err) {
				//ignore
			}
		}
		this.tableWidthLength = tableWidthLength;
		// Calculate widths of extras
		SizeInfo[] columnSizes = this.columnSizes;
		int numCols = columnSizes.length;
		int widthsOfExtras = border * 2 + (numCols + 1) * cellSpacing;
		if(border > 0) {
			widthsOfExtras += (numCols * 2);
		}
		this.widthsOfExtras = widthsOfExtras;
		
		// Calculate heights of extras
		SizeInfo[] rowSizes = this.rowSizes;
		int numRows = rowSizes.length;
		int heightsOfExtras = border * 2 + (numRows + 1) * cellSpacing;
		if(border > 0) {
			heightsOfExtras += (numRows * 2);
		}
		this.heightsOfExtras = heightsOfExtras;
	}
	
	public void build(int availWidth, int availHeight) {
		this.determineColumnSizes(this.border, this.cellSpacingX, this.cellSpacingY, availWidth);
		this.determineRowSizes(this.border, this.cellSpacingY, availHeight);
	}

	private void populateRows() {
		HTMLTableElementImpl te = this.tableElement;
		String cellPaddingText = te.getCellPadding();
		int cellPadding = 1;
		if(cellPaddingText != null) {
			try {
				cellPadding = Integer.parseInt(cellPaddingText);
				//TODO: Both cellpadding and cellspacing take percentages.
			} catch(NumberFormatException nfe) {
				// ignore
			}
		}
		ArrayList rowsList = te.getDescendents(ROWS_FILTER);
		Iterator i = rowsList.iterator();
		ArrayList rows = this.ROWS;
		ArrayList rowElements = this.ROW_ELEMENTS;
		while(i.hasNext()) {
			NodeImpl rowNode = (NodeImpl) i.next();
			ArrayList row = new ArrayList();
			int rowIndex = rows.size();
			rows.add(row);
			rowElements.add(rowNode);
			this.populateColumns(rowNode, row, rowIndex, cellPadding);
		}
	}
	
	private void populateColumns(NodeImpl rowNode, ArrayList row, int rowIndex, int cellPadding) {
		ArrayList columnsList = rowNode.getDescendents(COLUMNS_FILTER);
		ArrayList allCells = this.ALL_CELLS;
		Iterator i = columnsList.iterator();
		while(i.hasNext()) {
			HTMLTableCellElementImpl columnNode = (HTMLTableCellElementImpl) i.next();
			ActualCell ac = new ActualCell(columnNode, cellPadding, null, this.parserContext, this.frameContext, this.container, this.relement);
			VirtualCell vc = new VirtualCell(ac, true);
			ac.setTopLeftVirtualCell(vc);
			row.add(vc);
			allCells.add(ac);
		}
	} 
	
	private void adjustForCellSpans() {
		ArrayList rows = this.ROWS;
		int numRows = rows.size();
		for(int r = 0; r < numRows; r++) {
			ArrayList row = (ArrayList) rows.get(r);
			int numCols = row.size();
			for(int c = 0; c < numCols; c++) {
				VirtualCell vc = (VirtualCell) row.get(c);
				if(vc != null && vc.isTopLeft()) {
					ActualCell ac = vc.getActualCell();
					int colspan = ac.getColSpan();
					if(colspan < 1) {
						colspan = 1;
					}
					int rowspan = ac.getRowSpan();
					if(rowspan < 1) {
						rowspan = 1;
					}
					
					// Make sure row exists
					int targetRows = r + rowspan;
					while(rows.size() < targetRows) {
						rows.add(new ArrayList());
					}
					
					numRows = rows.size();
					for(int y = 0; y < rowspan; y++) {
						if(colspan > 1 || y > 0) {
							// Get row
							int nr = r + y;
							ArrayList newRow = (ArrayList) rows.get(nr);

							// Insert missing cells in row
							int xstart = y == 0 ? 1 : 0;
														
							// Insert virtual cells, potentially
							// shifting others to the right.
							for(int cc = xstart; cc < colspan; cc++) {
								int nc = c + cc;
								while(newRow.size() < nc) {
									newRow.add(null);
								}
								newRow.add(nc, new VirtualCell(ac, false));
							}
							if(row == newRow) {
								numCols = row.size();
							}
						}
					}
				}
			}
		}

		// Adjust row and column of virtual cells
		for(int r = 0; r < numRows; r++) {
			ArrayList row = (ArrayList) rows.get(r);
			int numCols = row.size();
			for(int c = 0; c < numCols; c++) {
				VirtualCell vc = (VirtualCell) row.get(c);
				if(vc != null) {	
					vc.setColumn(c);
					vc.setRow(r);
				}
			}
		}
	}
		
	private void createSizeArrays() {
		ArrayList rows = this.ROWS;
		int numRows = rows.size();
		SizeInfo[] rowSizes = new SizeInfo[numRows];
		this.rowSizes = rowSizes;
		int numCols = 0;
		ArrayList rowElements = this.ROW_ELEMENTS;
		for(int i = 0; i < numRows; i++) {
			ArrayList row = (ArrayList) rows.get(i);
			int rs = row.size();
			if(rs > numCols) {
				numCols = rs;
			}			
			SizeInfo rowSizeInfo = new SizeInfo();
			rowSizes[i] = rowSizeInfo;
			HTMLTableRowElement rowElement;
			try {
				rowElement = (HTMLTableRowElement) rowElements.get(i);
			} catch(IndexOutOfBoundsException iob) {
				//Possible if rowspan expands beyond that
				rowElement = null;
			}
			//TODO: TR.height an IE quirk?
			String rowHeightText = rowElement == null ? null : rowElement.getAttribute("height");
			HtmlLength rowHeightLength = null;
			if(rowHeightText != null) {
				try {
					rowHeightLength = new HtmlLength(rowHeightText);
				} catch(Exception err) {
					// ignore
				}
			}
			if(rowHeightLength != null) {
				rowSizeInfo.htmlLength = rowHeightLength;
			}
			else {
				HtmlLength bestHeightLength = null;
				for(int x = 0; x < rs; x++) {
					VirtualCell vc = (VirtualCell) row.get(x);
					if(vc != null) {
						HtmlLength vcHeightLength = vc.getHeightLength();
						if(vcHeightLength != null && vcHeightLength.isPreferredOver(bestHeightLength)) {
							bestHeightLength = vcHeightLength;
						}
					}
				}
				rowSizeInfo.htmlLength = bestHeightLength;
			}
		}
		SizeInfo[] columnSizes = new SizeInfo[numCols];
		this.columnSizes = columnSizes;
		for(int i = 0; i < numCols; i++) {
			HtmlLength bestWidthLength = null;
			
			// Cells with colspan==1 first.
			for(int y = 0; y < numRows; y++) {
				ArrayList row = (ArrayList) rows.get(y);
				VirtualCell vc;
				try {
					vc = (VirtualCell) row.get(i);
				} catch(IndexOutOfBoundsException iob) {
					vc = null;
				}
				if(vc != null) {
					ActualCell ac = vc.getActualCell();
					if(ac.getColSpan() == 1) {
						HtmlLength vcWidthLength = vc.getWidthLength();
						if(vcWidthLength != null && vcWidthLength.isPreferredOver(bestWidthLength)) {
							bestWidthLength = vcWidthLength;
						}
					}
				}
			}
			// Now cells with colspan>1.
			if(bestWidthLength == null) {
				for(int y = 0; y < numRows; y++) {
					ArrayList row = (ArrayList) rows.get(y);
					VirtualCell vc;
					try {
						vc = (VirtualCell) row.get(i);
					} catch(IndexOutOfBoundsException iob) {
						vc = null;
					}
					if(vc != null) {
						ActualCell ac = vc.getActualCell();
						if(ac.getColSpan() > 1) {
							HtmlLength vcWidthLength = vc.getWidthLength();
							if(vcWidthLength != null && vcWidthLength.isPreferredOver(bestWidthLength)) {
								bestWidthLength = vcWidthLength;
							}
						}
					}
				}
			}
			SizeInfo colSizeInfo = new SizeInfo();
			colSizeInfo.htmlLength = bestWidthLength;
			columnSizes[i] = colSizeInfo;
		}
	}
	
	private void determineColumnSizes(int border, int cellSpacingX, int cellSpacingY, int availWidth) {
		int hasBorder = border > 0 ? 1 : 0;
		HtmlLength tableWidthLength = this.tableWidthLength;
		int tableWidth;
		boolean widthKnown;
		if(tableWidthLength != null) {
			tableWidth = tableWidthLength.getLength(availWidth);
			widthKnown = true;
		}
		else {
			tableWidth = availWidth;
			widthKnown = false;
		}
		SizeInfo[] columnSizes = this.columnSizes;
		int widthsOfExtras = this.widthsOfExtras;
		int cellAvailWidth = tableWidth - widthsOfExtras;
		if(cellAvailWidth < 0) {
			tableWidth += (-cellAvailWidth);
			cellAvailWidth = 0;
		}
		// Determine tentative column widths based on specified cell widths
		
		this.determineTentativeSizes(columnSizes, widthsOfExtras, cellAvailWidth, true);
		
		// Pre-render cells. This will give the minimum width of each cell,
		// in addition to the minimum height.
		
		this.preLayout(hasBorder, cellSpacingX, cellSpacingY, widthKnown);

		// Adjust column widths based on minimums of each cell.
		
		this.adjustForRenderWidths(columnSizes, hasBorder, cellSpacingX, widthKnown);
		
		// Adjust for expected total width
		
		int totalUsedInCells = this.adjustWidthsForExpectedMax(columnSizes, cellAvailWidth, widthKnown);
		
		// Calculate actual table width
		this.tableWidth = totalUsedInCells + widthsOfExtras;
	}
	
	/**
	 * This method sets the tentative actual sizes of columns (rows) based
	 * on specified witdhs (heights) if available.
	 * @param columnSizes
	 * @param widthsOfExtras
	 * @param cellAvailWidth
	 */
	private void determineTentativeSizes(SizeInfo[] columnSizes, int widthsOfExtras, int cellAvailWidth, boolean setNoWidthColumns) {
		int numCols = columnSizes.length;
		
		// Look at percentages first
		int widthUsedByPercent = 0;
		for(int i = 0; i < numCols; i++) {
			SizeInfo colSizeInfo = columnSizes[i];
			HtmlLength widthLength = colSizeInfo.htmlLength;
			if(widthLength != null && widthLength.getLengthType() == HtmlLength.LENGTH) {
				int actualSizeInt = widthLength.getLength(cellAvailWidth);
				widthUsedByPercent += actualSizeInt;
				colSizeInfo.actualSize = actualSizeInt;
			}
		}
		// Look at columns with absolute sizes
		int widthUsedByAbsolute = 0;
		int numNoWidthColumns = 0;
		for(int i = 0; i < numCols; i++) {
			SizeInfo colSizeInfo = columnSizes[i];
			HtmlLength widthLength = colSizeInfo.htmlLength;
			if(widthLength != null && widthLength.getLengthType() != HtmlLength.LENGTH) {
				//TODO: MULTI-LENGTH not supported
				int actualSizeInt = widthLength.getRawValue();
				widthUsedByAbsolute += actualSizeInt;
				colSizeInfo.actualSize = actualSizeInt;
			}
			else if(widthLength == null) {
				numNoWidthColumns++;
			}
		}
		
		// Assign all columns without widths now
		int widthUsedByUnspecified = 0;
		if(setNoWidthColumns) {
			int remainingWidth = cellAvailWidth - widthUsedByAbsolute - widthUsedByPercent;
			if(remainingWidth > 0) {
				for(int i = 0; i < numCols; i++) {
					SizeInfo colSizeInfo = columnSizes[i];
					HtmlLength widthLength = colSizeInfo.htmlLength;
					if(widthLength == null) {
						int actualSizeInt = remainingWidth / numNoWidthColumns;
						widthUsedByUnspecified += actualSizeInt;
						colSizeInfo.actualSize = actualSizeInt;
					}
				}
			}		
		}
		
		// Contract if necessary. This is done again later, but this is
		// an optimization, as it may prevent re-layout. (Dubious if needed).
		
		int totalWidthUsed = widthUsedByPercent + widthUsedByAbsolute + widthUsedByUnspecified;
		int difference = totalWidthUsed - cellAvailWidth;
		if(difference > 0) {
			// First, try to contract/expand columns with no width
			if(widthUsedByUnspecified > 0) {
				int expectedNoWidthTotal = widthUsedByUnspecified - difference;
				if(expectedNoWidthTotal < 0) {
					expectedNoWidthTotal = 0;
				}
				double ratio = (double) expectedNoWidthTotal / widthUsedByUnspecified;
				for(int i = 0; i < numCols; i++) {
					SizeInfo sizeInfo = columnSizes[i];
					if(sizeInfo.htmlLength == null) {
						int oldActualSize = sizeInfo.actualSize;
						int newActualSize = (int) Math.round(oldActualSize * ratio);
						sizeInfo.actualSize = newActualSize;
						totalWidthUsed += (newActualSize - oldActualSize);
					}
				}
				difference = totalWidthUsed - cellAvailWidth;
			}
			
			// See if absolutes need to be contracted
			if(difference > 0) {
				if(widthUsedByAbsolute > 0) {
					int expectedAbsoluteWidthTotal = widthUsedByAbsolute - difference;
					if(expectedAbsoluteWidthTotal < 0) {
						expectedAbsoluteWidthTotal = 0;
					}
					double ratio = (double) expectedAbsoluteWidthTotal / widthUsedByAbsolute;
					for(int i = 0; i < numCols; i++) {
						SizeInfo sizeInfo = columnSizes[i];
						HtmlLength widthLength = columnSizes[i].htmlLength;
						if(widthLength != null && widthLength.getLengthType() != HtmlLength.LENGTH) {
							int oldActualSize = sizeInfo.actualSize;
							int newActualSize = (int) Math.round(oldActualSize * ratio);
							sizeInfo.actualSize = newActualSize;
							totalWidthUsed += (newActualSize - oldActualSize);
						}
					}				
					difference = totalWidthUsed - cellAvailWidth;
				}
				
				// See if percentages need to be contracted
				if(difference > 0) {
					if(widthUsedByPercent > 0) {
						int expectedPercentWidthTotal = widthUsedByPercent - difference;
						if(expectedPercentWidthTotal < 0) {
							expectedPercentWidthTotal = 0;
						}
						double ratio = (double) expectedPercentWidthTotal / widthUsedByPercent;
						for(int i = 0; i < numCols; i++) {
							SizeInfo sizeInfo = columnSizes[i];
							HtmlLength widthLength = columnSizes[i].htmlLength;
							if(widthLength != null && widthLength.getLengthType() == HtmlLength.LENGTH) {
								int oldActualSize = sizeInfo.actualSize;
								int newActualSize = (int) Math.round(oldActualSize * ratio);
								sizeInfo.actualSize = newActualSize;
								totalWidthUsed += (newActualSize - oldActualSize);
							}
						}							
					}
				}
			}		
		}
		
		//TODO: Different technique when setNoWidthColumns == false.
	}
	
	/**
	 * Contracts column sizes according to render sizes.
	 */
	private void adjustForRenderWidths(SizeInfo[] columnSizes, int hasBorder, int cellSpacing, boolean tableWidthKnown) {
		int numCols = columnSizes.length;
		for(int i = 0; i < numCols; i++) {
			SizeInfo si = columnSizes[i];
			if(si.actualSize < si.layoutSize) {
				si.actualSize = si.layoutSize;
			}
			else if(!tableWidthKnown && si.htmlLength == null) {
				if(si.actualSize > si.layoutSize) {
					si.actualSize = si.layoutSize;
				}
			}
		}
	}
	
	private void layoutColumn(SizeInfo[] columnSizes, SizeInfo colSize, int col, int cellSpacingX, int hasBorder) {
		SizeInfo[] rowSizes = this.rowSizes;
		ArrayList rows = this.ROWS;
		int numRows = rows.size();
		int actualSize = colSize.actualSize;
		colSize.layoutSize = 0;
		for(int row = 0; row < numRows;) {
			//SizeInfo rowSize = rowSizes[row];
			ArrayList columns = (ArrayList) rows.get(row);
			VirtualCell vc = null;
			try {
				vc = (VirtualCell) columns.get(col);
			} catch(IndexOutOfBoundsException iob) {
				vc = null;
			}
			ActualCell ac = vc == null ? null : vc.getActualCell();
			if(ac != null) {
				int colSpan = ac.getColSpan();
				if(colSpan > 1) {
					int firstCol = ac.getVirtualColumn();
					int cellExtras = (colSpan - 1) * (cellSpacingX + 2 * hasBorder);
					int vcActualWidth = cellExtras;
					for(int x = 0; x < colSpan; x++) {
						vcActualWidth += columnSizes[firstCol + x].actualSize;
					}
					int llw = ac.getLastLayoutWidth();
					if(llw > vcActualWidth) {
						//TODO: better height possible
						Dimension size = ac.layoutFor(vcActualWidth, 0);
						int vcRenderWidth = size.width;
						int denominator = (vcActualWidth - cellExtras);
						int newTentativeCellWidth;
						if(denominator > 0) {
							newTentativeCellWidth = actualSize * (vcRenderWidth - cellExtras) / denominator;
						}
						else {
							newTentativeCellWidth = (vcRenderWidth - cellExtras) / colSpan;
						}
						if(newTentativeCellWidth > colSize.layoutSize) {
							colSize.layoutSize = newTentativeCellWidth;
						}
						int rowSpan = ac.getRowSpan();
						int vch = (size.height - (rowSpan - 1) * (this.cellSpacingY + 2 * hasBorder)) / rowSpan;
						for(int y = 0; y < rowSpan; y++) {
							if(rowSizes[row + y].minSize < vch) {
								rowSizes[row + y].minSize = vch;
							}
						}
					}
					else {
						int denominator = (vcActualWidth - cellExtras);
						int newTentativeCellWidth;
						if(denominator > 0) {
							newTentativeCellWidth = actualSize * (llw - cellExtras) / denominator;
						}
						else {
							newTentativeCellWidth = (vcActualWidth - cellExtras) / colSpan;
						}
						if(newTentativeCellWidth > colSize.layoutSize) {
							colSize.layoutSize = newTentativeCellWidth;
						}						
					}
				}
				else {
					int llw = ac.getLastLayoutWidth();
					if(llw > actualSize) {
						//TODO: better height possible
						Dimension size = ac.layoutFor(actualSize, 0);
						if(size.width > colSize.layoutSize) {
							colSize.layoutSize = size.width;
						}
						int rowSpan = ac.getRowSpan();
						int vch = (size.height - (rowSpan - 1) * (this.cellSpacingY + 2 * hasBorder)) / rowSpan;
						for(int y = 0; y < rowSpan; y++) {
							if(rowSizes[row + y].minSize < vch) {
								rowSizes[row + y].minSize = vch;
							}
						}
					}
					else {
						if(llw > colSize.layoutSize) {
							colSize.layoutSize = llw;
						}
					}
				}
			}
			row = (ac == null ? row + 1 : ac.getVirtualRow() + ac.getRowSpan());
		}
	}
	
	private int adjustWidthsForExpectedMax(SizeInfo[] columnSizes, int cellAvailWidth, boolean expand) {
		int hasBorder = this.border > 0 ? 1 : 0;
		int cellSpacingX = this.cellSpacingX;
		int currentTotal = 0;
		int numCols = columnSizes.length;
		for(int i = 0; i < numCols; i++) {
			currentTotal += columnSizes[i].actualSize;
		}
		int difference = currentTotal - cellAvailWidth;
		if(difference > 0 || (difference < 0 && expand)) {
			// First, try to contract/expand columns with no width
			int noWidthTotal = 0;
			for(int i = 0; i < numCols; i++) {
				if(columnSizes[i].htmlLength == null) {
					noWidthTotal += columnSizes[i].actualSize;
				}
			}
			if(noWidthTotal > 0) {
				int expectedNoWidthTotal = noWidthTotal - difference;
				if(expectedNoWidthTotal < 0) {
					expectedNoWidthTotal = 0;
				}
				double ratio = (double) expectedNoWidthTotal / noWidthTotal;
				for(int i = 0; i < numCols; i++) {
					SizeInfo sizeInfo = columnSizes[i];
					if(sizeInfo.htmlLength == null) {
						int oldActualSize = sizeInfo.actualSize;
						int newActualSize = (int) Math.round(oldActualSize * ratio);
						sizeInfo.actualSize = newActualSize;
						if(difference > 0 && newActualSize < sizeInfo.layoutSize) {
							this.layoutColumn(columnSizes, sizeInfo, i, cellSpacingX, hasBorder);
							if(newActualSize < sizeInfo.layoutSize) {
								newActualSize = sizeInfo.layoutSize;
								sizeInfo.actualSize = newActualSize;
							}
						}
						currentTotal += (newActualSize - oldActualSize);
					}
				}
				difference = currentTotal - cellAvailWidth;
			}
			
			// See if absolutes need to be contracted
			if(difference > 0 || (difference < 0 && expand)) {
				int absoluteWidthTotal = 0;
				for(int i = 0; i < numCols; i++) {
					HtmlLength widthLength = columnSizes[i].htmlLength;
					if(widthLength != null && widthLength.getLengthType() != HtmlLength.LENGTH) {
						absoluteWidthTotal += columnSizes[i].actualSize;
					}
				}
				if(absoluteWidthTotal > 0) {
					int expectedAbsoluteWidthTotal = absoluteWidthTotal - difference;
					if(expectedAbsoluteWidthTotal < 0) {
						expectedAbsoluteWidthTotal = 0;
					}
					double ratio = (double) expectedAbsoluteWidthTotal / absoluteWidthTotal;
					for(int i = 0; i < numCols; i++) {
						SizeInfo sizeInfo = columnSizes[i];
						HtmlLength widthLength = columnSizes[i].htmlLength;
						if(widthLength != null && widthLength.getLengthType() != HtmlLength.LENGTH) {
							int oldActualSize = sizeInfo.actualSize;
							int newActualSize = (int) Math.round(oldActualSize * ratio);
							sizeInfo.actualSize = newActualSize;
							if(difference > 0 && newActualSize < sizeInfo.layoutSize) {
								this.layoutColumn(columnSizes, sizeInfo, i, cellSpacingX, hasBorder);
								if(newActualSize < sizeInfo.layoutSize) {
									newActualSize = sizeInfo.layoutSize;
									sizeInfo.actualSize = newActualSize;
								}
							}
							currentTotal += (newActualSize - oldActualSize);
						}
					}				
					difference = currentTotal - cellAvailWidth;
				}
				// See if percentages need to be contracted
				if(difference > 0 || (difference < 0 && expand)) {
					int percentWidthTotal = 0;
					for(int i = 0; i < numCols; i++) {
						HtmlLength widthLength = columnSizes[i].htmlLength;
						if(widthLength != null && widthLength.getLengthType() == HtmlLength.LENGTH) {
							percentWidthTotal += columnSizes[i].actualSize;
						}
					}
					if(percentWidthTotal > 0) {
						int expectedPercentWidthTotal = percentWidthTotal - difference;
						if(expectedPercentWidthTotal < 0) {
							expectedPercentWidthTotal = 0;
						}
						double ratio = (double) expectedPercentWidthTotal / percentWidthTotal;
						for(int i = 0; i < numCols; i++) {
							SizeInfo sizeInfo = columnSizes[i];
							HtmlLength widthLength = columnSizes[i].htmlLength;
							if(widthLength != null && widthLength.getLengthType() == HtmlLength.LENGTH) {
								int oldActualSize = sizeInfo.actualSize;
								int newActualSize = (int) Math.round(oldActualSize * ratio);
								sizeInfo.actualSize = newActualSize;
								if(difference > 0 && newActualSize < sizeInfo.layoutSize) {
									this.layoutColumn(columnSizes, sizeInfo, i, cellSpacingX, hasBorder);
									if(newActualSize < sizeInfo.layoutSize) {
										newActualSize = sizeInfo.layoutSize;
										sizeInfo.actualSize = newActualSize;
									}
								}
								currentTotal += (newActualSize - oldActualSize);
							}
						}							
					}
				}
			}		
		}
		return currentTotal;
	}
	
	/**
	 * This method renders each cell using already set actual column widths.
	 * It sets minimum row heights based on this.
	 */
	private final void preLayout(int hasBorder, int cellSpacingX, int cellSpacingY, boolean tableWidthKnown) {
		SizeInfo[] colSizes = this.columnSizes;
		SizeInfo[] rowSizes = this.rowSizes;

		// Initialize minSize in rows
		int numRows = rowSizes.length;
		for(int i = 0; i < numRows; i++) {
			rowSizes[i].minSize = 0;
		}
		// Initialize renderSize in columns
		
		int numCols = colSizes.length;
		for(int i = 0; i < numCols; i++) {
			colSizes[i].layoutSize = 0;
		}
		
		ArrayList allCells = this.ALL_CELLS;
		int numCells = allCells.size();
		for(int i = 0; i < numCells; i++) {
			ActualCell cell = (ActualCell) allCells.get(i);
			int col = cell.getVirtualColumn();
			int colSpan = cell.getColSpan();
			int totalWidth;
			if(colSpan > 1) {
				totalWidth = (colSpan - 1) * (cellSpacingX + 2 * hasBorder);
				for(int x = 0; x < colSpan; x++) {
					totalWidth += colSizes[col + x].actualSize;
				}
			}
			else {
				totalWidth = colSizes[col].actualSize;
			}

			//TODO: A tentative height could be used here: Height of
			//table divided by number of rows.
			
			java.awt.Dimension size = cell.layoutFor(totalWidth, 0);

			// Set render widths
			int cellLayoutWidth = size.width;
			if(colSpan > 1) {
				int prevRenderSum = (colSpan - 1) * (cellSpacingX + 2 * hasBorder);
				for(int x = 0; x < colSpan; x++) {
					prevRenderSum += colSizes[col + x].actualSize;
				}
				if(prevRenderSum < cellLayoutWidth || !tableWidthKnown) {
					double ratio = (double) cellLayoutWidth / totalWidth;
					for(int x = 0; x < colSpan; x++) {
						SizeInfo si = colSizes[col + x];
						int newLayoutSize = (int) Math.round(si.actualSize * ratio);
						if(si.layoutSize < newLayoutSize) {
							si.layoutSize = newLayoutSize;
						}
					}					
				}
			}
			else {
				if(colSizes[col].layoutSize < cellLayoutWidth) {
					colSizes[col].layoutSize = cellLayoutWidth;
				}
			}
			
			// Set minimum heights
			int actualCellHeight = size.height;
			int row = cell.getVirtualRow();
			int rowSpan = cell.getRowSpan();
			if(rowSpan > 1) {
				int vch = (actualCellHeight - (rowSpan - 1) * (cellSpacingY + 2 * hasBorder)) / rowSpan;
				for(int y = 0; y < rowSpan; y++) {
					if(rowSizes[row + y].minSize < vch) {
						rowSizes[row + y].minSize = vch;
					}
				}
			}
			else {
				if(rowSizes[row].minSize < actualCellHeight) {
					rowSizes[row].minSize = actualCellHeight;
				}				
			}
		}
	}

	private void determineRowSizes(int border, int cellSpacing, int availHeight) {
		HtmlLength tableHeightLength = this.tableElement.getHeightLength();
		int tableHeight;
		SizeInfo[] rowSizes = this.rowSizes;
		int numRows = rowSizes.length;
		int heightsOfExtras = border * 2 + (numRows + 1) * cellSpacing;
		if(border > 0) {
			heightsOfExtras += (numRows * 2);
		}
		if(tableHeightLength != null) {
			tableHeight = tableHeightLength.getLength(availHeight);
			this.determineRowSizesFixedTH(border, cellSpacing, availHeight, tableHeight);
		}
		else {
			tableHeight = heightsOfExtras;
			for(int row = 0; row < numRows; row++) {
				tableHeight += rowSizes[row].minSize;
			}
			this.determineRowSizesFlexibleTH(border, cellSpacing, availHeight);
		}
	}
	
	private void determineRowSizesFixedTH(int border, int cellSpacing, int availHeight, int tableHeight) {
		int hasBorder = border > 0 ? 1 : 0;
		SizeInfo[] rowSizes = this.rowSizes;
		int numRows = rowSizes.length;
		int heightsOfExtras = this.heightsOfExtras;
		int cellAvailHeight = tableHeight - heightsOfExtras;
		if(cellAvailHeight < 0) {
			cellAvailHeight = 0;
		}
		
		// Look at percentages first
		
		int heightUsedbyPercent = 0;
		int otherMinSize = 0;
		for(int i = 0; i < numRows; i++) {
			SizeInfo rowSizeInfo = rowSizes[i];
			HtmlLength heightLength = rowSizeInfo.htmlLength;
			if(heightLength != null && heightLength.getLengthType() == HtmlLength.LENGTH) {
				int actualSizeInt = heightLength.getLength(cellAvailHeight);
				if(actualSizeInt < rowSizeInfo.minSize) {
					actualSizeInt = rowSizeInfo.minSize;
				}
				heightUsedbyPercent += actualSizeInt;
				rowSizeInfo.actualSize = actualSizeInt;
			}
			else {
				otherMinSize += rowSizeInfo.minSize;
			}
		}
		
		// Check if rows with percent are bigger than they should be
		
		if(heightUsedbyPercent + otherMinSize > cellAvailHeight) {
			double ratio = (double) (cellAvailHeight - otherMinSize) / heightUsedbyPercent; 
			for(int i = 0; i < numRows; i++) {
				SizeInfo rowSizeInfo = rowSizes[i];
				HtmlLength heightLength = rowSizeInfo.htmlLength;
				if(heightLength != null && heightLength.getLengthType() == HtmlLength.LENGTH) {
					int actualSize = rowSizeInfo.actualSize;
					int prevActualSize = actualSize;
					int newActualSize = (int) Math.round(prevActualSize * ratio);
					if(newActualSize < rowSizeInfo.minSize) {
						newActualSize = rowSizeInfo.minSize;
					}
					heightUsedbyPercent += (newActualSize - prevActualSize);
					rowSizeInfo.actualSize = newActualSize;
				}
			}				
		}
		
		// Look at rows with absolute sizes
		
		int heightUsedByAbsolute = 0;
		int noHeightMinSize = 0;
		int numNoHeightColumns = 0;
		for(int i = 0; i < numRows; i++) {
			SizeInfo rowSizeInfo = rowSizes[i];
			HtmlLength heightLength = rowSizeInfo.htmlLength;
			if(heightLength != null && heightLength.getLengthType() != HtmlLength.LENGTH) {
				//TODO: MULTI-LENGTH not supported
				int actualSizeInt = heightLength.getRawValue();
				if(actualSizeInt < rowSizeInfo.minSize) {
					actualSizeInt = rowSizeInfo.minSize;
				}
				heightUsedByAbsolute += actualSizeInt;
				rowSizeInfo.actualSize = actualSizeInt;
			}
			else if(heightLength == null) {
				numNoHeightColumns++;
				noHeightMinSize += rowSizeInfo.minSize;
			}
		}
		
		// Check if absolute sizing is too much
		
		if(heightUsedByAbsolute + heightUsedbyPercent + noHeightMinSize > cellAvailHeight) {
			double ratio = (double) (cellAvailHeight - noHeightMinSize - heightUsedbyPercent) / heightUsedByAbsolute;
			for(int i = 0; i < numRows; i++) {
				SizeInfo rowSizeInfo = rowSizes[i];
				HtmlLength heightLength = rowSizeInfo.htmlLength;
				if(heightLength != null && heightLength.getLengthType() != HtmlLength.LENGTH) {
					int actualSize = rowSizeInfo.actualSize;
					int prevActualSize = actualSize;
					int newActualSize = (int) Math.round(prevActualSize * ratio);
					if(newActualSize < rowSizeInfo.minSize) {
						newActualSize = rowSizeInfo.minSize;
					}
					heightUsedByAbsolute += (newActualSize - prevActualSize);
					rowSizeInfo.actualSize = newActualSize;
				}
			}									
		}
		
		// Assign all rows without heights now
		
		int remainingHeight = cellAvailHeight - heightUsedByAbsolute - heightUsedbyPercent;
		int heightUsedByRemaining = 0;
		for(int i = 0; i < numRows; i++) {
			SizeInfo rowSizeInfo = rowSizes[i];
			HtmlLength heightLength = rowSizeInfo.htmlLength;
			if(heightLength == null) {
				int actualSizeInt = remainingHeight / numNoHeightColumns;
				if(actualSizeInt < rowSizeInfo.minSize) {
					actualSizeInt = rowSizeInfo.minSize;
				}
				heightUsedByRemaining += actualSizeInt;
				rowSizeInfo.actualSize = actualSizeInt;
			}
		}

		// Calculate actual table width

		int totalUsed = heightUsedByAbsolute + heightUsedbyPercent + heightUsedByRemaining;
		if(totalUsed >= cellAvailHeight) {
			this.tableHeight = totalUsed + heightsOfExtras;
		}
		else {
			// Rows too short; expand them
			double ratio = (double) cellAvailHeight / totalUsed;
			for(int i = 0; i < numRows; i++) {
				SizeInfo rowSizeInfo = rowSizes[i];
				int actualSize = rowSizeInfo.actualSize;
				rowSizeInfo.actualSize = (int) Math.round(actualSize * ratio);
			}
			this.tableHeight = tableHeight;
		}
		
		//TODO:
		// This final render is probably unnecessary. Avoid exponential rendering
		// by setting a single height of subcell. Verify that IE only sets height
		// of subcells when height of row or table are specified.
		
		this.finalRender(hasBorder, cellSpacing);
	}

	private void determineRowSizesFlexibleTH(int border, int cellSpacing, int availHeight) {
		int hasBorder = border > 0 ? 1 : 0;
		SizeInfo[] rowSizes = this.rowSizes;
		int numRows = rowSizes.length;
		int heightsOfExtras = this.heightsOfExtras;
		
		// Look at rows with absolute sizes		
		int heightUsedByAbsolute = 0;
		int percentSum = 0;
		for(int i = 0; i < numRows; i++) {
			SizeInfo rowSizeInfo = rowSizes[i];
			HtmlLength heightLength = rowSizeInfo.htmlLength;
			if(heightLength != null && heightLength.getLengthType() == HtmlLength.PIXELS) {
				//TODO: MULTI-LENGTH not supported
				int actualSizeInt = heightLength.getRawValue();
				if(actualSizeInt < rowSizeInfo.minSize) {
					actualSizeInt = rowSizeInfo.minSize;
				}
				heightUsedByAbsolute += actualSizeInt;
				rowSizeInfo.actualSize = actualSizeInt;
			}
			else if(heightLength != null && heightLength.getLengthType() == HtmlLength.LENGTH) {
				percentSum += heightLength.getRawValue();
			}
		}
			
		// Look at rows with no specified heights		
		int heightUsedByNoSize = 0;
		
		// Set sizes to in row height
		for(int i = 0; i < numRows; i++) {
			SizeInfo rowSizeInfo = rowSizes[i];
			HtmlLength widthLength = rowSizeInfo.htmlLength;
			if(widthLength == null) {
				int actualSizeInt = rowSizeInfo.minSize;
				heightUsedByNoSize += actualSizeInt;
				rowSizeInfo.actualSize = actualSizeInt;
			}
		}
		
		// Calculate actual total cell width
		int expectedTotalCellHeight = (int) Math.round((heightUsedByAbsolute + heightUsedByNoSize) / (1 - (percentSum / 100.0)));
		
		// Set widths of columns with percentages		
		int heightUsedByPercent = 0;
		for(int i = 0; i < numRows; i++) {
			SizeInfo rowSizeInfo = rowSizes[i];
			HtmlLength heightLength = rowSizeInfo.htmlLength;
			if(heightLength != null && heightLength.getLengthType() == HtmlLength.LENGTH) {
				int actualSizeInt = heightLength.getLength(expectedTotalCellHeight);
				if(actualSizeInt < rowSizeInfo.minSize) {
					actualSizeInt = rowSizeInfo.minSize;
				}
				heightUsedByPercent += actualSizeInt;
				rowSizeInfo.actualSize = actualSizeInt;
			}
		}
						
		// Set width of table
		this.tableHeight = heightUsedByAbsolute + heightUsedByNoSize + heightUsedByPercent + heightsOfExtras;
		
		// Do a final render to set actual cell sizes		
		this.finalRender(hasBorder, cellSpacing);
	}

	/**
	 * This method renders each cell using already set actual column widths.
	 * It sets minimum row heights based on this.
	 */
	private final void finalRender(int hasBorder, int cellSpacing) {
		ArrayList allCells = this.ALL_CELLS;
		SizeInfo[] colSizes = this.columnSizes;
		SizeInfo[] rowSizes = this.rowSizes;
		int numCells = allCells.size();
		for(int i = 0; i < numCells; i++) {
			ActualCell cell = (ActualCell) allCells.get(i);
			int col = cell.getVirtualColumn();
			int colSpan = cell.getColSpan();
			int totalWidth;
			if(colSpan > 1) {
				totalWidth = (colSpan - 1) * (cellSpacing + 2 * hasBorder);
				for(int x = 0; x < colSpan; x++) {
					totalWidth += colSizes[col + x].actualSize;
				}
			}
			else {
				totalWidth = colSizes[col].actualSize;
			}
			int row = cell.getVirtualRow();
			int rowSpan = cell.getRowSpan();
			int totalHeight;
			if(rowSpan > 1) {
				totalHeight = (rowSpan - 1) * (cellSpacing + 2 * hasBorder);
				for(int y = 0; y < rowSpan; y++) {
					totalHeight += rowSizes[row + y].actualSize;
				}
				cell.layoutFor(totalWidth, totalHeight);
			}
			else {
				totalHeight = rowSizes[row].actualSize;
				if(totalHeight != rowSizes[row].minSize) {
					cell.layoutFor(totalWidth, totalHeight);
				}
			}
		}
	}

	/**
	 * Sets bounds of each cell's component.
	 */
	public final void doLayout(Insets insets) {
		
		// Set row offsets
		
		SizeInfo[] rowSizes = this.rowSizes;
		int numRows = rowSizes.length;
		int yoffset = insets.top;
		int border = this.border;
		int cellSpacingY = this.cellSpacingY;
		int hasBorder = border > 0 ? 1 : 0;
		yoffset += border; 
		for(int i = 0; i < numRows; i++) {
			yoffset += cellSpacingY;
			yoffset += hasBorder;
			SizeInfo rowSizeInfo = rowSizes[i];
			rowSizeInfo.offset = yoffset;
			yoffset += rowSizeInfo.actualSize;
			yoffset += hasBorder;
		}
		
		// Set colum offsets 
		
		SizeInfo[] colSizes = this.columnSizes;
		int numColumns = colSizes.length; 
		int xoffset = insets.left;
		int cellSpacingX = this.cellSpacingX;
		xoffset += border;
		for(int i = 0; i < numColumns; i++) {
			xoffset += cellSpacingX;
			xoffset += hasBorder;
			SizeInfo colSizeInfo = colSizes[i];
			colSizeInfo.offset = xoffset;
			xoffset += colSizeInfo.actualSize;
			xoffset += hasBorder;
		}

		// Set offsets of each cell
		
		ArrayList allCells = this.ALL_CELLS;
		int numCells = allCells.size();
		for(int i = 0; i < numCells; i++) {
			ActualCell cell = (ActualCell) allCells.get(i);
			cell.doLayout(colSizes, rowSizes, hasBorder, cellSpacingX, cellSpacingY);
		}		
	}
	
	public final void paint(Graphics g, Dimension size, Insets insets) {
		ArrayList allCells = this.ALL_CELLS;
		int numCells = allCells.size();
		for(int i = 0; i < numCells; i++) {
			ActualCell cell = (ActualCell) allCells.get(i);
			Rectangle bounds = cell.getBounds();
			Graphics newG = g.create(bounds.x, bounds.y, bounds.width, bounds.height);
			try {
				cell.paint(newG);
			} finally {
				newG.dispose();
			}
		}		

		int border = this.border;
		if(border > 0) {
			// Paint table border
			
			int tableWidth = this.tableWidth;
			int tableHeight = this.tableHeight;
			g.setColor(Color.BLACK); //TODO: Actual border color
			int x = insets.left;
			int y = insets.top;
			for(int i = 0; i < border; i++) {
				g.drawRect(x + i, y + i, tableWidth - i * 2 - 1, tableHeight - i * 2 - 1);
			}

			// Paint cell borders
			
			for(int i = 0; i < numCells; i++) {
				ActualCell cell = (ActualCell) allCells.get(i);
				Rectangle bounds = cell.getBounds();
				int cx = bounds.x - 1;
				int cy = bounds.y - 1;
				int cwidth = bounds.width + 1;
				int cheight = bounds.height + 1;
				g.drawRect(cx, cy, cwidth, cheight);
			}					
		}
	}

	public boolean paintSelection(Graphics g, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		ArrayList allCells = this.ALL_CELLS;
		int numCells = allCells.size();
		for(int i = 0; i < numCells; i++) {
			ActualCell cell = (ActualCell) allCells.get(i);
			Rectangle bounds = cell.getBounds();
			Graphics newG = g.create(bounds.x, bounds.y, bounds.width, bounds.height);
			boolean newInSelection = cell.paintSelection(newG, inSelection, startPoint, endPoint);
			if(inSelection && !newInSelection) {
				return false;
			}
			inSelection = newInSelection;
		}
		return inSelection;
	}

	public boolean extractSelectionText(StringBuffer buffer, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		ArrayList allCells = this.ALL_CELLS;
		int numCells = allCells.size();
		for(int i = 0; i < numCells; i++) {
			ActualCell cell = (ActualCell) allCells.get(i);
			boolean newInSelection = cell.extractSelectionText(buffer, inSelection, startPoint, endPoint);
			if(inSelection && !newInSelection) {
				return false;
			}
			inSelection = newInSelection;
		}
		return inSelection;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#getRenderablePoint(int, int)
	 */
	public RenderablePoint getRenderablePoint(int x, int y) {
		ArrayList allCells = this.ALL_CELLS;
		int numCells = allCells.size();
		for(int i = 0; i < numCells; i++) {
			ActualCell cell = (ActualCell) allCells.get(i);
			Rectangle bounds = cell.getBounds();
			if(bounds.contains(x, y)) {
				RenderablePoint rp = cell.getRenderablePoint(x - bounds.x, y - bounds.y);
				if(rp != null) {
					return rp;
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMouseClick(java.awt.event.MouseEvent, int, int)
	 */
	public void onMouseClick(MouseEvent event, int x, int y) {
		ArrayList allCells = this.ALL_CELLS;
		int numCells = allCells.size();
		for(int i = 0; i < numCells; i++) {
			ActualCell cell = (ActualCell) allCells.get(i);
			Rectangle bounds = cell.getBounds();
			if(bounds.contains(x, y)) {
				cell.onMouseClick(event, x - bounds.x, y - bounds.y);
			}
		}
	}

	private BoundableRenderable armedRenderable;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMouseDisarmed(java.awt.event.MouseEvent)
	 */
	public void onMouseDisarmed(MouseEvent event) {
		BoundableRenderable ar = this.armedRenderable;
		if(ar != null) {
			ar.onMouseDisarmed(event);
			this.armedRenderable = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMousePressed(java.awt.event.MouseEvent, int, int)
	 */
	public void onMousePressed(MouseEvent event, int x, int y) {
		ArrayList allCells = this.ALL_CELLS;
		int numCells = allCells.size();
		for(int i = 0; i < numCells; i++) {
			ActualCell cell = (ActualCell) allCells.get(i);
			Rectangle bounds = cell.getBounds();
			if(bounds.contains(x, y)) {
				cell.onMousePressed(event, x - bounds.x, y - bounds.y);
				this.armedRenderable = cell;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMouseReleased(java.awt.event.MouseEvent, int, int)
	 */
	public void onMouseReleased(MouseEvent event, int x, int y) {
		ArrayList allCells = this.ALL_CELLS;
		int numCells = allCells.size();
		boolean found = false;
		for(int i = 0; i < numCells; i++) {
			ActualCell cell = (ActualCell) allCells.get(i);
			Rectangle bounds = cell.getBounds();
			if(bounds.contains(x, y)) {
				found = true;
				cell.onMouseReleased(event, x - bounds.x, y - bounds.y);
		    	BoundableRenderable oldArmedRenderable = this.armedRenderable;
		    	if(oldArmedRenderable != null && cell != oldArmedRenderable) {
		    		oldArmedRenderable.onMouseDisarmed(event);
		    		this.armedRenderable = null;
		    	}
			}
		}
		if(!found) {
	    	BoundableRenderable oldArmedRenderable = this.armedRenderable;
	    	if(oldArmedRenderable != null) {
	    		oldArmedRenderable.onMouseDisarmed(event);
	    		this.armedRenderable = null;
	    	}
		}
	}

	public Iterator getRenderables() {
		return this.ALL_CELLS.iterator();
	}

	private static class RowsFilter implements NodeFilter {
    	public final boolean accept(Node node) {
    		return (node instanceof HTMLTableRowElement);
    	}
    }

    private static class ColumnsFilter implements NodeFilter {
    	public final boolean accept(Node node) {
    		return (node instanceof HTMLTableCellElement);
    	}
    }
    
    public static class SizeInfo {
    	public HtmlLength htmlLength;
    	public int actualSize;
    	public int layoutSize;
    	public int minSize;
    	public int offset;
    }
}
