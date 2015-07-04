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
 * Created on Jan 29, 2006
 */
package org.xamjwg.html.renderer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.*;

import org.xamjwg.html.*;
import org.xamjwg.html.domimpl.*;
import org.xamjwg.util.WrapperLayout;

public class FrameSetPanel extends JComponent implements NodeRenderer {
	public FrameSetPanel() {
		super();
		this.setLayout(WrapperLayout.getInstance());
	}

	private HtmlLength[] getLengths(String spec) {
		if(spec == null) {
			return new HtmlLength[] { new HtmlLength("1*") };
		}
		StringTokenizer tok = new StringTokenizer(spec, ",");
		ArrayList lengths = new ArrayList();
		while(tok.hasMoreTokens()) {
			String token = tok.nextToken().trim();
			try {
				lengths.add(new HtmlLength(token));
			} catch(Exception err) {
				// ignore
			}			
		}
		return (HtmlLength[]) lengths.toArray(HtmlLength.EMPTY_ARRAY);
	}
	
	private HTMLElementImpl[] getSubFrames(HTMLElementImpl parent) {
		NodeImpl[] children = parent.getChildrenArray();
		ArrayList subFrames = new ArrayList();
		for(int i = 0; i < children.length; i++) {
			NodeImpl child = children[i];
			if(child instanceof HTMLElementImpl) {
				String nodeName = child.getNodeName();
				if("FRAME".equalsIgnoreCase(nodeName) || "FRAMESET".equals(nodeName)) {
					subFrames.add(child);
				}
			}
		}
		return (HTMLElementImpl[]) subFrames.toArray(new HTMLElementImpl[0]);
	}
	
	public void setRootNode(NodeImpl node) {
		if(!(node instanceof HTMLElementImpl)) {
			throw new IllegalArgumentException("node=" + node);
		}
		HTMLElementImpl element = (HTMLElementImpl) node;
		HtmlRendererContext context = element.getHtmlRendererContext();
		this.htmlContext = context;
		if(context != null) {
			String rows = element.getAttribute("rows");
			String cols = element.getAttribute("cols");
			HtmlLength[] rowLengths = this.getLengths(rows);
			HtmlLength[] colLengths = this.getLengths(cols);
			this.rowHtmlLengths = rowLengths;
			this.colHtmlLengths = colLengths;
			HTMLElementImpl[] subframes = this.getSubFrames(element);
			Component[] frameComponents = new Component[subframes.length];
			this.frameComponents = frameComponents;
			for(int i = 0; i < subframes.length; i++) {
				HTMLElementImpl frameElement = subframes[i];
				if(frameElement != null && "FRAMESET".equalsIgnoreCase(frameElement.getTagName())) {
					FrameSetPanel fsp = new FrameSetPanel();
					fsp.setRootNode(frameElement);
					frameComponents[i] = fsp;
				}
				else {
					BrowserFrame frame = context.createBrowserFrame();
					if(frameElement != null) {
						String src = frameElement.getAttribute("src");
						if(src != null) {
							java.net.URL url;
							try {
								url = frameElement.getFullURL(src);
								if(url != null) {
									frame.loadURL(url);
								}
							} catch(MalformedURLException mfu) {
								// ignore
							}
						}
					}					
					frameComponents[i] = frame.getComponent();				
				}
				
			}
		}
	}
	
	private HtmlLength[] rowHtmlLengths;
	private HtmlLength[] colHtmlLengths;
	private HtmlRendererContext htmlContext;
	private Component[] frameComponents;

	public void doLayout() {
		this.removeAll();
		HtmlLength[] rhl = this.rowHtmlLengths;
		HtmlLength[] chl = this.colHtmlLengths;
		Component[] fc = this.frameComponents;
		if(rhl != null && chl != null && fc != null) {
			Dimension size = this.getSize();
			Insets insets = this.getInsets();
			int width = size.width - insets.left - insets.right;
			int height = size.height - insets.left - insets.right;
			int[] colLengths = this.getAbsoluteLengths(chl, width);
			int[] rowLengths = this.getAbsoluteLengths(rhl, height);
			this.add(this.getSplitPane(this.htmlContext, colLengths, 0, colLengths.length, rowLengths, 0, rowLengths.length, fc));
		}
		super.doLayout();		
	}
	
	private int[] getAbsoluteLengths(HtmlLength[] htmlLengths, int totalSize) {
		int[] absLengths = new int[htmlLengths.length];
		int totalSizeNonMulti = 0;
		int sumMulti = 0;
		for(int i = 0; i < htmlLengths.length; i++) {
			HtmlLength htmlLength = htmlLengths[i];
			int lengthType = htmlLength.getLengthType();
			if(lengthType == HtmlLength.PIXELS) {
				int absLength = htmlLength.getRawValue();
				totalSizeNonMulti += absLength;
				absLengths[i] = absLength;
			}
			else if(lengthType == HtmlLength.LENGTH) {
				int absLength = htmlLength.getLength(totalSize);
				totalSizeNonMulti += absLength;
				absLengths[i] = absLength;				
			}
			else {
				sumMulti += htmlLength.getRawValue();
			}
		}
		int remaining = totalSize - totalSizeNonMulti;
		if(remaining > 0 && sumMulti > 0) {
			for(int i = 0; i < htmlLengths.length; i++) {
				HtmlLength htmlLength = htmlLengths[i];
				if(htmlLength.getLengthType() == HtmlLength.MULTI_LENGTH) {
					int absLength = (remaining * htmlLength.getRawValue()) / sumMulti;
					absLengths[i] = absLength;
				}
			}				
		}
		return absLengths;
	}
	
	private Component getSplitPane(HtmlRendererContext context, int[] colLengths, int firstCol, int numCols, int[] rowLengths, int firstRow, int numRows, Component[] frameComponents) {
		if(numCols == 1) {
			int frameindex = colLengths.length * firstRow + firstCol;
			Component topComponent = frameindex < frameComponents.length ? frameComponents[frameindex] : null;
			if(numRows == 1) {
				return topComponent;
			}
			else {
			    Component bottomComponent = this.getSplitPane(context, colLengths, firstCol, numCols, rowLengths, firstRow + 1, numRows - 1, frameComponents);
				JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topComponent, bottomComponent);
				sp.setDividerLocation(rowLengths[firstRow]);
				return sp;
			}
		}
		else {
			Component rightComponent = this.getSplitPane(context, colLengths, firstCol + 1, numCols - 1, rowLengths, firstRow, numRows, frameComponents);
			Component leftComponent = this.getSplitPane(context, colLengths, firstCol, 1, rowLengths, firstRow, numRows, frameComponents);
			JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftComponent, rightComponent);
			sp.setDividerLocation(colLengths[firstCol]);
			return sp;
		}
	}
}
