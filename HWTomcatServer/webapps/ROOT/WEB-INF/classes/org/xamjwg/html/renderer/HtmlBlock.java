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
 * Created on Apr 16, 2005
 */
package org.xamjwg.html.renderer;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

import javax.swing.*;

import org.xamjwg.html.HtmlParserContext;
import org.xamjwg.html.domimpl.*;
import org.xamjwg.util.ColorFactory;

/**
 * @author J. H. S.
 */
public class HtmlBlock extends JComponent implements ContainingBlockContext, NodeRenderer, RenderableContainer, ClipboardOwner {
	//private static final Logger logger = Logger.getLogger(WScrollableBody.class);
	protected final FrameContext frameContext;	
	protected RenderablePoint startSelection;
	protected RenderablePoint endSelection;
	protected final RBlock rblock;

	public HtmlBlock(int listNesting, HtmlParserContext pcontext, FrameContext frameContext) {
		this(listNesting, ColorFactory.TRANSPARENT, null, false, pcontext, frameContext);
	}
	
	public HtmlBlock(int listNesting, Color background, RenderState parentRenderState, boolean opaque, HtmlParserContext pcontext, FrameContext frameContext) {
		this.frameContext = frameContext;
		this.rblock = new RBlock(listNesting, parentRenderState, pcontext, frameContext, this, null);
		this.setOpaque(opaque);
		this.setBackground(background);
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				if("copy".equals(command)) {
					String selection = HtmlBlock.this.getSelectionText();
					if(selection != null) {
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(new StringSelection(selection), HtmlBlock.this);
					}
				}
			}
		};
		this.registerKeyboardAction(actionListener, "copy", KeyStroke.getKeyStroke(KeyEvent.VK_COPY, 0), JComponent.WHEN_FOCUSED);		
		this.registerKeyboardAction(actionListener, "copy", KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), JComponent.WHEN_FOCUSED);		
		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				onMouseClick(e);
			}
			public void mouseEntered(MouseEvent e) { 
			}
			public void mouseExited(MouseEvent e) {	
				onMouseExited(e);
			}
			public void mousePressed(MouseEvent e) {
				onMousePressed(e);
			}
			public void mouseReleased(MouseEvent e) {
				onMouseReleased(e);
			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {
			/* (non-Javadoc)
			 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
			 */
			public void mouseDragged(MouseEvent e) {
				onMouseDragged(e);
			}

			/* (non-Javadoc)
			 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
			 */
			public void mouseMoved(MouseEvent arg0) {
			}
		});
	}
	
	public void setDefaultPaddingInsets(Insets insets) {
		this.rblock.setDefaultPaddingInsets(insets);
	}
	
	public int getFirstLineHeight() {
		return this.rblock.getFirstLineHeight();
	}

	public void setSelectionEnd(RenderablePoint rpoint) {
		this.endSelection = rpoint;
	}
	
	public void setSelectionStart(RenderablePoint rpoint) {
		this.startSelection = rpoint;
	}	
	
	public boolean isSelectionAvailable() {
		RenderablePoint start = this.startSelection;
		RenderablePoint end = this.endSelection;
		return start != null && end != null && !start.equals(end);
	}
	
	public void setRootNode(NodeImpl node) {
		this.setRootNode(node, true);
	}

	public void setRootNode(NodeImpl node, boolean setContainer) {
		this.rblock.setRootNode(node, setContainer);
		this.revalidate();
		this.repaint();
	}
	
	public NodeImpl getRootNode() {
		return this.rblock.getRootNode();
	}
	
	private void onMouseClick(MouseEvent event) {
		Point point = event.getPoint();
		BoundableRenderable r = this.rblock.getRenderable(point);
		if(r != null) {
			Rectangle bounds = r.getBounds();
			r.onMouseClick(event, point.x - bounds.x, point.y - bounds.y);
		}
	}
	
	private BoundableRenderable mousePressTarget;
	private void onMousePressed(MouseEvent event) {
		this.requestFocus();
		Point point = event.getPoint();
		BoundableRenderable r = this.rblock.getRenderable(point);
		if(r != null) {
			this.mousePressTarget = r;
			Rectangle bounds = r.getBounds();
			int rx = point.x - bounds.x;
			int ry = point.y - bounds.y;
			r.onMousePressed(event, rx, ry);
			RenderablePoint rp = r.getRenderablePoint(rx, ry);
			if(rp != null) {
				this.frameContext.resetSelection(rp);
			}
			else {
				this.frameContext.resetSelection(null);
			}
		}
		else {
			this.frameContext.resetSelection(null);
		}
	}
	
	private void onMouseReleased(MouseEvent event) {
		Point point = event.getPoint();
		BoundableRenderable r = this.rblock.getRenderable(point);
		if(r != null) {
			Rectangle bounds = r.getBounds();
			r.onMouseReleased(event, point.x - bounds.x, point.y - bounds.y);
		}
		BoundableRenderable oldTarget = this.mousePressTarget;
		if(oldTarget != null) {
			this.mousePressTarget = null;
			if(oldTarget != r) {
				oldTarget.onMouseDisarmed(event);
			}
		}
	}
	
	private void onMouseExited(MouseEvent event) {
		BoundableRenderable oldTarget = this.mousePressTarget;
		if(oldTarget != null) {
			this.mousePressTarget = null;
			oldTarget.onMouseDisarmed(event);
		}
	}	

	private Rectangle getAbsoluteBounds(BoundableRenderable br) {
		int x = 0;
		int y = 0;
		Renderable parent = br;
		while(parent instanceof BoundableRenderable) {
			BoundableRenderable current = (BoundableRenderable) parent;
			Rectangle bounds = current.getBounds();
			x += bounds.x;
			y += bounds.y;
			parent = current.getParent();
		}
		Rectangle oldBounds = br.getBounds();
		return new Rectangle(x, y, oldBounds.width, oldBounds.height);
	}
	
	private void onMouseDragged(MouseEvent event) {
		Point point = event.getPoint();
		RenderablePoint rp = this.rblock.getRenderablePoint(point.x, point.y);
		if(rp != null) {
			BoundableRenderable br = rp.renderable;
			Rectangle abounds = this.getAbsoluteBounds(br);
			this.scrollRectToVisible(abounds);
			this.frameContext.expandSelection(rp);
		}
		else {
			Rectangle bounds = new Rectangle(point.x, point.y, 1, 1);
			this.scrollRectToVisible(bounds);
		}
	}
	
	public void paint(Graphics g) {
		if(g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		super.paint(g);
	}
	
	public void update(Graphics g) {
		this.paint(g);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics arg0) {
		//long time1 = System.currentTimeMillis();
		try {
			super.paintComponent(arg0);
			if(this.isOpaque()) {
				Rectangle clipBounds = arg0.getClipBounds();
				arg0.setColor(this.getBackground());
				arg0.fillRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);
			}
			this.rblock.paint(arg0);
			
			// Paint FrameContext selection
			
			RenderablePoint start = this.startSelection;
			RenderablePoint end = this.endSelection;
			if(start != null && end != null && !start.equals(end)) {
				this.rblock.paintSelection(arg0, false, start, end);
			}
		} finally {
			//long time2 = System.currentTimeMillis();
			//System.out.println("NonScrollableHtmlPanel.paintComponent(): numRenderables=" + numRenderables + ",time=" + (time2 - time1) + " ms.");
		}
	}
		
	/* (non-Javadoc)
	 * @see java.awt.Component#doLayout()
	 */
	public void doLayout() {
		if(EventQueue.isDispatchThread()) {
			Dimension size = this.getSize();
			this.rblock.setBounds(0, 0, size.width, size.height);
			this.removeAll();
			this.rblock.layout(size.width, size.height);
			this.rblock.updateWidgetBounds();
		}
		else {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					HtmlBlock.this.doLayout();
				}
			});
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.ContainingBlockContext#repaint(org.xamjwg.html.renderer.RenderableContext)
	 */
	public void repaint(RenderableContext renderableContext) {
		this.rblock.invalidateState(renderableContext);
		this.repaint();
	}

	public void invalidate() {
		super.invalidate();
		this.rblock.invalidate();
	}

	public String getSelectionText() {
		RenderablePoint start = this.startSelection;
		RenderablePoint end = this.endSelection;
		if(start != null && end != null) {
			StringBuffer buffer = new StringBuffer();
			this.rblock.extractSelectionText(buffer, false, start, end);
			return buffer.toString();
		}
		else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer.Clipboard, java.awt.datatransfer.Transferable)
	 */
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
	}
}