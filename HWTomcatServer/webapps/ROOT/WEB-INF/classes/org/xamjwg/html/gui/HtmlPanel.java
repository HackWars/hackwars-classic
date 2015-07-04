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
package org.xamjwg.html.gui;

import java.awt.Component;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.EventListener;
import java.util.EventObject;
import javax.swing.*;
import Controller.*;
import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.xamjwg.html.*;
import org.xamjwg.html.domimpl.*;
import org.xamjwg.html.renderer.*;
import org.xamjwg.html.test.SimpleHtmlParserContext;
import org.xamjwg.util.EventDispatch2;
import org.xamjwg.util.WrapperLayout;

/**
 * The <code>HtmlPanel</code> class is a Swing
 * component that can render an HTML DOM. 
 * @author J. H. S.
 */
public class HtmlPanel extends JComponent implements FrameContext {
	private final EventDispatch2 selectionDispatch = new SelectionDispatch();
	
	/**
	 * Constructs an <code>HtmlPanel</code>.
	 */
	public HtmlPanel() {
		super();
		this.setLayout(WrapperLayout.getInstance());
	}
	
	/**
	Register parent. (Added by Ben Coe to fix graphics rendering bug.)
	*/
	private Coezilla Parent=null;
	public void registerParent(Coezilla Parent){
		this.Parent=Parent;
	}

	private boolean isFrameSet = false;
	private NodeRenderer nodeRenderer = null;
	private NodeImpl rootNode;
	private HtmlParserContext parserContext;
	private HtmlBlock htmlBlock;

	private void setUpBodyScrollable(HtmlParserContext pcontext) {
		ScrollableHtmlBlock shp = new ScrollableHtmlBlock(Color.WHITE, true, pcontext, this);
		
		this.htmlBlock = shp;
		shp.setDefaultPaddingInsets(new Insets(1, 1, 1, 1));
		JScrollPane pane = new JScrollPane(shp);
		
		//pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.removeAll();
		this.add(pane);
		this.nodeRenderer = shp;
	}

	/**
	 * Scrolls the document such that x and y coordinates
	 * are placed in the upper-left corner of the panel.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public void scroll(int x, int y) {
		try {
			Object root = this.getComponent(0);
			if(root instanceof JScrollPane) {
				((JScrollPane) root).getViewport().scrollRectToVisible(new Rectangle(x, y, 1, 1));
			}
		} catch(IndexOutOfBoundsException iob) {
			// ignore
		}
	}

	public HtmlParserContext getHtmlParserContext() {
		return this.parserContext;
	}
	
	public void clean(){
		JComponent JC=(JComponent)nodeRenderer;
		if(JC!=null){
		Component CleanMe[]=JC.getComponents();
		if(CleanMe!=null){
			for(int i=0;i<CleanMe.length;i++){
				if(CleanMe[i] instanceof ImgControl){
					ImgControl IC=(ImgControl)CleanMe[i];
					IC.getImage().flush();
					IC.removeAll();
				}
			}
		}
	
		}
		//nodeRenderer.removeAll();
		this.removeAll();
	}
	
	/**
	 * Sets an HTML DOM node to be rendered. 
	 * @param node This should
	 * normally be a Document instance obtained with
	 * {@link org.xamjwg.html.parser.DocumentBuilderImpl}.
	 * @param rcontext A renderer context.
	 * @param pcontext A parser context.
	 * @deprecated This overload of <code>setDocument</code> should no longer be used.
	 */
	public void setDocument(Document node, HtmlRendererContext rcontext) {
		this.setDocument(node, rcontext, new SimpleHtmlParserContext());
	}

	/**
	 * Sets an HTML DOM node to be rendered. 
	 * @param node This should
	 * normally be a Document instance obtained with
	 * {@link org.xamjwg.html.parser.DocumentBuilderImpl}.
	 * @param rcontext A renderer context.
	 * @param pcontext A parser context.
	 */
	public void setDocument(Document node, HtmlRendererContext rcontext, HtmlParserContext pcontext) {
		if(!(node instanceof HTMLDocumentImpl)) {
			throw new IllegalArgumentException("Only nodes of type HTMLDocumentImpl are currently supported. Use DocumentBuilderImpl.");
		}
		
		this.parserContext = pcontext;
		HTMLDocumentImpl nodeImpl = (HTMLDocumentImpl) node;
		nodeImpl.setHtmlRendererContext(rcontext);
		this.rootNode = nodeImpl;
		NodeImpl fsrn = this.getFrameSetRootNode(nodeImpl);
		boolean newIfs = fsrn != null;
		
		
		if(newIfs != this.isFrameSet || this.getComponentCount() == 0) {
			this.isFrameSet = newIfs;
			if(newIfs) {
				this.htmlBlock = null;
				FrameSetPanel fsp = new FrameSetPanel();
				this.removeAll();
				this.add(fsp);
				this.nodeRenderer = fsp;
				fsp.setRootNode(fsrn);
			}
			else {
				this.setUpBodyScrollable(pcontext);
			}		
			this.repaint();
		}		
		NodeRenderer nr = this.nodeRenderer;
		if(nr != null) {
			if(newIfs) {
				nr.setRootNode(fsrn);
			}
			else {
				nr.setRootNode(nodeImpl);
			}
		}
		
		Parent.htmlUpdate();
	}

	/**
	 * Gets the HTML DOM node currently rendered if any.
	 */
	public NodeImpl getRootNode() {
		return this.rootNode;
	}
	
	private NodeImpl getFrameSetRootNode(NodeImpl node) {
		if(node instanceof Document) {
			ElementImpl element = (ElementImpl) ((Document) node).getDocumentElement();
			if(element != null && "HTML".equalsIgnoreCase(element.getTagName())) {
				return this.getFrameSet(element);
			}
			else {
				return this.getFrameSet(node);
			}
		}
		else {
			return null;
		}
	}
	
	private NodeImpl getFrameSet(NodeImpl node) {
		NodeImpl[] children = node.getChildrenArray();
		int length = children.length;
		NodeImpl frameSet = null;
		for(int i = 0; i < length; i++) {
			NodeImpl child = children[i];
			if(child instanceof Text) {
				String textContent = ((Text) child).getTextContent();
				if(textContent != null && !"".equals(textContent.trim())) {
					return null;
				}
			}
			else if(child instanceof ElementImpl) {
				String tagName = child.getNodeName();
				if("FRAMESET".equalsIgnoreCase(tagName)) {
					frameSet = child;
				}
				else {
					if(this.hasSomeHtml((ElementImpl) child)) {
						return null;
					}
				}
			}
		}
		return frameSet;
	}
	
	private boolean hasSomeHtml(ElementImpl element) {
		String tagName = element.getTagName();
		if("HEAD".equalsIgnoreCase(tagName) || "TITLE".equalsIgnoreCase(tagName)) {
			return false;
		}
		NodeImpl[] children = element.getChildrenArray();
		if(children != null) {
			int length = children.length;
			for(int i = 0; i < length; i++) {
				NodeImpl child = children[i];
				if(child instanceof Text) {
					String textContent = ((Text) child).getTextContent();
					if(textContent != null && !"".equals(textContent.trim())) {
						return false;
					}
				}
				else if(child instanceof ElementImpl) {
					if(this.hasSomeHtml((ElementImpl) child)) {
						return false;
					}
				}
			}
		}
		return true;		
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.FrameContext#expandSelection(org.xamjwg.html.renderer.RenderablePoint)
	 */
	public void expandSelection(RenderablePoint rpoint) {
		HtmlBlock block = this.htmlBlock;
		if(block != null) {
			block.setSelectionEnd(rpoint);
			block.repaint();
			this.selectionDispatch.fireEvent(new SelectionChangeEvent(this, block.isSelectionAvailable()));
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.FrameContext#resetSelection(org.xamjwg.html.renderer.RenderablePoint)
	 */
	public void resetSelection(RenderablePoint rpoint) {
		HtmlBlock block = this.htmlBlock;
		if(block != null) {
			block.setSelectionStart(rpoint);
			block.setSelectionEnd(rpoint);
			block.repaint();
		}
		this.selectionDispatch.fireEvent(new SelectionChangeEvent(this, false));
	}
	
	/**
	 * Gets the selection text. 
	 * Note: This method should be invoked in the GUI thread.
	 */
	public String getSelectionText() {
		HtmlBlock block = this.htmlBlock;
		if(block == null) {
			return null;
		}
		else {
			return block.getSelectionText();
		}
	}

	/**
	 * Adds listener of selection changes. Note that it does 
	 * not have any effect on framesets.
	 * @param listener
	 */
	public void addSelectionChangeListener(SelectionChangeListener listener) {
		this.selectionDispatch.addListener(listener);
	}

	/**
	 * Removes a listener of selection changes that was
	 * previously added.
	 */
	public void removeSelectionChangeListener(SelectionChangeListener listener) {
		this.selectionDispatch.removeListener(listener);
	}
	
	private class SelectionDispatch extends EventDispatch2 {
		/* (non-Javadoc)
		 * @see org.xamjwg.util.EventDispatch2#dispatchEvent(java.util.EventListener, java.util.EventObject)
		 */
		protected void dispatchEvent(EventListener listener, EventObject event) {
			((SelectionChangeListener) listener).selectionChanged((SelectionChangeEvent) event);
		}
	}
}
