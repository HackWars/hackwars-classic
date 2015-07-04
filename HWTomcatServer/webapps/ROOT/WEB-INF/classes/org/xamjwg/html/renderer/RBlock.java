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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;

import org.w3c.dom.css.CSS2Properties;
import org.xamjwg.html.HtmlParserContext;
import org.xamjwg.html.HttpRequest;
import org.xamjwg.html.ReadyStateChangeListener;
import org.xamjwg.html.domimpl.*;
import org.xamjwg.util.ColorFactory;

/**
 * @author J. H. S.
 */
public class RBlock extends BaseElementRenderable implements ContainingBlockContext, NodeRenderer, RenderableContainer, ImageObserver {
	//private static final Logger logger = Logger.getLogger(WScrollableBody.class);
	protected final BodyLayout bodyLayout;
	protected final RenderState parentRenderState;
	protected final HtmlParserContext parserContext;
	protected final FrameContext frameContext;

	protected volatile RenderState renderState;
	protected volatile NodeImpl rootNode;

	protected Color backgroundColor;
	protected Image backgroundImage;
	protected int backgroundRepeat;
	protected String loadingBackgroundImage;
	protected int backgroundXPosition;
	protected boolean backgroundXPositionAbsolute;
	protected int backgroundYPosition;
	protected boolean backgroundYPositionAbsolute;
	
	protected RenderablePoint startSelection;
	protected RenderablePoint endSelection;
	public RBlock(int listNesting, HtmlParserContext pcontext, FrameContext frameContext, RenderableContainer parentContainer, Renderable parent) {
		this(listNesting, null, pcontext, frameContext, parentContainer, parent);
	}
	
	public RBlock(int listNesting, RenderState parentRenderState, HtmlParserContext pcontext, FrameContext frameContext, RenderableContainer parentContainer, Renderable parent) {
		super(parentContainer);
		this.setParent(parent);
		this.frameContext = frameContext;
		this.parserContext = pcontext;
		this.parentRenderState = parentRenderState;
		this.bodyLayout = new BodyLayout(this, listNesting, pcontext, frameContext, this);
	}
		
	public BoundableRenderable getRenderable(Point point) {
		return this.bodyLayout.getRenderable(point);
	}
	
	private Insets defaultPaddingInsets = null;
	
	public void setDefaultPaddingInsets(Insets insets) {
		this.defaultPaddingInsets = insets;
	}
	
	public int getFirstLineHeight() {
		return this.bodyLayout.getFirstLineHeight();
	}

	public void setSelectionEnd(RenderablePoint rpoint) {
		this.endSelection = rpoint;
	}
	
	public void setSelectionStart(RenderablePoint rpoint) {
		this.startSelection = rpoint;
	}
	
	protected Insets marginInsets;
	protected Insets paddingInsets;
	protected Insets borderInsets;
	
	public Insets getInsets() {
		Insets mi = this.marginInsets;
		Insets pi = this.paddingInsets;
		Insets bi = this.borderInsets;
		if(mi == null && pi == null && bi == null) {
			return BodyLayout.ZERO_INSETS;
		}
		else {
			int top = 0;
			int bottom = 0;
			int left = 0;
			int right = 0;
			if(mi != null) {
				top += mi.top;
				left += mi.left;
				bottom += mi.bottom;
				right += mi.right;
			}
			if(pi != null) {
				top += pi.top;
				left += pi.left;
				bottom += pi.bottom;
				right += pi.right;
			}
			if(bi != null) {
				top += bi.top;
				left += bi.left;
				bottom += bi.bottom;
				right += bi.right;
			}
			return new Insets(top, left, bottom, right);
		}
	}
	
	private void clearStyle() {
		this.renderState = null;
		this.marginInsets = null;
		this.paddingInsets = this.defaultPaddingInsets;
		this.borderInsets = null;
		this.backgroundColor = null;
		this.backgroundImage = null;
		this.backgroundRepeat = BR_REPEAT;
		this.loadingBackgroundImage = null;
		this.backgroundXPosition = 0;
		this.backgroundXPositionAbsolute = true;
		this.backgroundYPosition = 0;
		this.backgroundYPositionAbsolute = true;
		this.borderTopColor = null;
		this.borderLeftColor = null;
		this.borderBottomColor = null;
		this.borderRightColor = null;
		this.loadingBackgroundImage = null;
	}
	
	private void applyStyle(HTMLElementImpl rootElement) {
		CSS2PropertiesImpl props = rootElement.getStyle();
		this.renderState = new BlockRenderState(this.parentRenderState, props);
		String align = props.getTextAlign();
		int alignXPercent;
		if(align == null || "".equals(align)) {
			alignXPercent = 0;
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
		this.setAlignXPercent(alignXPercent);
		RenderState rs = this.renderState;
		this.marginInsets = HtmlValues.getMarginInsets(props, rs);
		Insets paddingIns = HtmlValues.getPaddingInsets(props, rs);
		if(paddingIns == null) {
			this.paddingInsets = this.defaultPaddingInsets;
		}
		else {
			this.paddingInsets = paddingIns;
		}
		this.borderInsets = null;
		this.backgroundColor = null;
		this.backgroundImage = null;
		this.backgroundRepeat = BR_REPEAT;
		this.loadingBackgroundImage = null;
		this.backgroundXPosition = 0;
		this.backgroundXPositionAbsolute = true;
		this.backgroundYPosition = 0;
		this.backgroundYPositionAbsolute = true;
		this.borderTopColor = null;
		this.borderLeftColor = null;
		this.borderBottomColor = null;
		this.borderRightColor = null;
		String background = props.getBackground();
		if(background != null && !"".equals(background)) {
			this.applyBackground(background);
		}
		String backgroundColorText = props.getBackgroundColor();
		if(backgroundColorText != null && !"".equals(backgroundColorText)) {
			this.backgroundColor = ColorFactory.getInstance().getColor(backgroundColorText);
		}		
		String backgroundImageText = props.getBackgroundImage();
		if(backgroundImageText != null && !"".equals(backgroundImageText)) {
			String backgroundImage = HtmlValues.getURIFromStyleValue(backgroundImageText);
			if(backgroundImage != null) {
				this.loadingBackgroundImage = backgroundImage;
				this.loadBackgroundImage(backgroundImage);
			}
		}
		String backgroundRepeatText = props.getBackgroundRepeat();
		if(backgroundRepeatText != null && !"".equals(backgroundRepeatText)) {
			this.applyBackgroundRepeat(backgroundRepeatText);
		}
		String backgroundPositionText = props.getBackgroundPosition();
		if(backgroundPositionText != null && !"".equals(backgroundPositionText)) {
			this.applyBackgroundPosition(backgroundPositionText);
		}
		String border = props.getBorder();
		if(border != null && !"".equals(border)) {
			this.applyBorder(border);
		}
		this.borderInsets = HtmlValues.getBorderInsets(this.borderInsets, props, rs);
		String borderColorText = props.getBorderColor();
		if(borderColorText != null && !"".equals(borderColorText)) {
			Color[] colorsArray = HtmlValues.getColors(borderColorText);
			this.borderTopColor = colorsArray[0];
			this.borderLeftColor = colorsArray[1];
			this.borderBottomColor = colorsArray[2];
			this.borderRightColor = colorsArray[3];
		}
		String borderTopColorText = props.getBorderTopColor();
		if(borderTopColorText != null && !"".equals(borderTopColorText)) {
			this.borderTopColor = ColorFactory.getInstance().getColor(borderTopColorText);
		}
		String borderLeftColorText = props.getBorderLeftColor();
		if(borderLeftColorText != null && !"".equals(borderLeftColorText)) {
			this.borderLeftColor = ColorFactory.getInstance().getColor(borderLeftColorText);
		}
		String borderBottomColorText = props.getBorderBottomColor();
		if(borderBottomColorText != null && !"".equals(borderBottomColorText)) {
			this.borderBottomColor = ColorFactory.getInstance().getColor(borderBottomColorText);
		}
		String borderRightColorText = props.getBorderRightColor();
		if(borderRightColorText != null && !"".equals(borderRightColorText)) {
			this.borderRightColor = ColorFactory.getInstance().getColor(borderRightColorText);
		}
	}
	
	private void applyBorder(String border) {
		String[] tokens = HtmlValues.splitCssValue(border);
		for(int i = 0; i < tokens.length; i++) {
			String token = tokens[i];
			if(HtmlValues.isLength(token)) {
				int pixelSize = HtmlValues.getPixelSize(token, this.renderState, 0);
				Insets bi = new Insets(pixelSize, pixelSize, pixelSize, pixelSize);
				this.borderInsets = bi;
			}
			else if(ColorFactory.getInstance().isColor(token)) {
				Color color = ColorFactory.getInstance().getColor(token);
				this.borderLeftColor = color;
				this.borderRightColor = color;
				this.borderTopColor = color;
				this.borderBottomColor = color;
			}
			else if(HtmlValues.isBorderStyle(token)) {
				//TODO
				if("solid".equalsIgnoreCase(token)) {
					Insets bi = this.borderInsets;
					if(bi == null) {
						bi = new Insets(2, 2, 2, 2);
						this.borderInsets = bi;
					}
				}
			}
		}
	}
	
	private Color borderTopColor;
	private Color borderLeftColor;
	private Color borderBottomColor;
	private Color borderRightColor;

	private Color getBorderTopColor() {
		Color c = this.borderTopColor;
		return c == null ? Color.black : c;
	}
	
	private Color getBorderLeftColor() {
		Color c = this.borderLeftColor;
		return c == null ? Color.black : c;
	}

	private Color getBorderBottomColor() {
		Color c = this.borderBottomColor;
		return c == null ? Color.black : c;
	}

	private Color getBorderRightColor() {
		Color c = this.borderRightColor;
		return c == null ? Color.black : c;
	}

	private void applyBackgroundRepeat(String backgroundRepeatText) {
		if("repeat".equalsIgnoreCase(backgroundRepeatText)) {
			this.backgroundRepeat = BR_REPEAT;
		}
		else if("repeat-x".equalsIgnoreCase(backgroundRepeatText)) {
			this.backgroundRepeat = BR_REPEAT_X;
		}
		else if("repeat-y".equalsIgnoreCase(backgroundRepeatText)) {
			this.backgroundRepeat = BR_REPEAT_Y;
		}
		else if("no-repeat".equalsIgnoreCase(backgroundRepeatText)) {
			this.backgroundRepeat = BR_NO_REPEAT;
		}		
	}
	
	private void applyBackgroundHorizontalPositon(String xposition) {
		if(xposition.endsWith("%")) {
			this.backgroundXPositionAbsolute = false;
			try {
				this.backgroundXPosition = (int) Double.parseDouble(xposition.substring(0, xposition.length() - 1).trim());
			} catch(NumberFormatException nfe) {
				this.backgroundXPosition = 0;
			}
		}
		else if("center".equalsIgnoreCase(xposition)) {
			this.backgroundXPositionAbsolute = false;
			this.backgroundXPosition = 50;			
		}
		else if("right".equalsIgnoreCase(xposition)) {
			this.backgroundXPositionAbsolute = false;
			this.backgroundXPosition = 100;			
		}
		else if("left".equalsIgnoreCase(xposition)) {
			this.backgroundXPositionAbsolute = false;
			this.backgroundXPosition = 0;			
		}
		else {
			this.backgroundXPositionAbsolute = true;
			this.backgroundXPosition = HtmlValues.getPixelSize(xposition, this.renderState, 0);
		}		
	}

	private void applyBackgroundVerticalPosition(String yposition) {
		if(yposition.endsWith("%")) {
			this.backgroundYPositionAbsolute = false;
			try {
				this.backgroundYPosition = (int) Double.parseDouble(yposition.substring(0, yposition.length() - 1).trim());
			} catch(NumberFormatException nfe) {
				this.backgroundYPosition = 0;
			}
		}
		else if("center".equalsIgnoreCase(yposition)) {
			this.backgroundYPositionAbsolute = false;
			this.backgroundYPosition = 50;			
		}
		else if("bottom".equalsIgnoreCase(yposition)) {
			this.backgroundYPositionAbsolute = false;
			this.backgroundYPosition = 100;			
		}
		else if("top".equalsIgnoreCase(yposition)) {
			this.backgroundYPositionAbsolute = false;
			this.backgroundYPosition = 0;			
		}
		else {
			this.backgroundYPositionAbsolute = true;
			this.backgroundYPosition = HtmlValues.getPixelSize(yposition, this.renderState, 0);
		}						
	}
	
	private void applyBackgroundPosition(String position) {
		StringTokenizer tok = new StringTokenizer(position, " \t\r\n");
		if(tok.hasMoreTokens()) {
			String xposition = tok.nextToken();
			this.applyBackgroundHorizontalPositon(xposition);
			if(tok.hasMoreTokens()) {
				String yposition = tok.nextToken();
				this.applyBackgroundVerticalPosition(yposition);
			}
		}
	}
	
	private void applyBackground(String background) {
		//TODO: Property values do not appear in order necessarily
		String[] tokens = HtmlValues.splitCssValue(background);
		boolean hasXPosition = false;
		for(int i = 0; i < tokens.length; i++) {
			String token = tokens[i];
			if(ColorFactory.getInstance().isColor(token)) {
				this.backgroundColor = ColorFactory.getInstance().getColor(token);
			}	
			else if(isUrl(token)) {
				String backgroundImage = HtmlValues.getURIFromStyleValue(token);
				if(backgroundImage != null) {
					this.loadingBackgroundImage = backgroundImage;
					this.loadBackgroundImage(backgroundImage);
				}
			}
			else if(isBackgroundRepeat(token)) {
				this.applyBackgroundRepeat(token);
			}
			else if(isBackgroundPosition(token)) {
				if(hasXPosition) {
					this.applyBackgroundVerticalPosition(token);
				}
				else {
					hasXPosition = true;
					this.applyBackgroundHorizontalPositon(token);
				}
			}
		}
	}

	private boolean isBackgroundPosition(String token) {
		return HtmlValues.isLength(token) ||
			token.endsWith("%") ||
			token.equalsIgnoreCase("top") ||
			token.equalsIgnoreCase("center") ||
			token.equalsIgnoreCase("bottom") ||
			token.equalsIgnoreCase("left") ||
			token.equalsIgnoreCase("right");			
	}
	
	private boolean isUrl(String token) {
		return token.toLowerCase().startsWith("url(");
	}

	private boolean isBackgroundRepeat(String repeat) {
		String repeatTL = repeat.toLowerCase();
		return repeatTL.indexOf("repeat") != -1;
	}
	
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
		// This is so that a loading image doesn't cause
		// too many repaint events.
		if((infoflags & ImageObserver.ALLBITS) != 0 || (infoflags & ImageObserver.FRAMEBITS) != 0) {
			this.repaint();
		}
		return true;
	}		

	protected void loadBackgroundImage(final String uri) {
		Object rn = this.rootNode;
		if(rn instanceof RenderableContext) {
			RenderableContext rc = (RenderableContext) rn;
			HtmlParserContext ctx = this.parserContext;
			if(ctx != null) {
				final HttpRequest request = ctx.createHttpRequest();
				request.addReadyStateChangeListener(new ReadyStateChangeListener() {
					public void 
					readyStateChanged() {
						int readyState = request.getReadyState();
						if(readyState == HttpRequest.STATE_COMPLETE) {
							RBlock.this.loadingBackgroundImage = null;
							int status = request.getStatus();
							if(status == 200 || status == 0) {
								Image img = request.getResponseImage();
								RBlock.this.backgroundImage = img;
								synchronized(this) {}
								// Cause observer to be called
								int w = img.getWidth(RBlock.this);
								int h = img.getHeight(RBlock.this);
								// Maybe image already done...
								if(w != -1 && h != -1) {
									RBlock.this.repaint();
								}
							}							
						}
					}
				});
				try {
					URL fullUrl = rc.getFullURL(uri);
					request.open("GET", fullUrl);
				} catch(MalformedURLException mfu) {
					rc.warn("Bad image url: " + uri, mfu);
				}
			}
		}
	}
	
	protected void prePaint(java.awt.Graphics g) {
		Dimension size = this.getBounds().getSize();
		int startWidth = size.width;
		int startHeight = size.height;
		int totalWidth = startWidth;
		int totalHeight = startHeight;
		int startX = 0;
		int startY = 0;
		Insets marginInsets = this.marginInsets;
		if(marginInsets != null) {
			totalWidth -= (marginInsets.left + marginInsets.right);
			totalHeight -= (marginInsets.top + marginInsets.bottom);
			startX += marginInsets.left;
			startY += marginInsets.top;
		}
		Insets borderInsets = this.borderInsets;
		if(borderInsets != null) {
			int btop = borderInsets.top;
			int bleft = borderInsets.left;
			int bright = borderInsets.right;
			int bbottom = borderInsets.bottom;
			
			// Paint top border
			if(btop > 0) {
				g.setColor(this.getBorderTopColor());
				for(int i = 0; i < btop; i++) {
					int leftOffset = (i * bleft) / btop;
					int rightOffset = (i * bright) / btop;
					g.drawLine(startX + leftOffset, startY + i, startX + totalWidth - rightOffset - 1, startY + i);
				}
			}
			if(bright > 0) {
				g.setColor(this.getBorderRightColor());
				int lastX = startX + totalWidth - 1;
				for(int i = 0; i < bright; i++) {
					int topOffset = (i * btop) / bright;
					int bottomOffset = (i * bbottom) / bright;
					g.drawLine(lastX - i, startY + topOffset, lastX - i, startY + totalHeight - bottomOffset - 1);
				}				
			}
			if(bleft > 0) {
				g.setColor(this.getBorderLeftColor());
				for(int i = 0; i < bleft; i++) {
					int topOffset = (i * btop) / bleft;
					int bottomOffset = (i * bbottom) / bleft;
					g.drawLine(startX + i, startY + topOffset, startX + i, startY + totalHeight - bottomOffset - 1);
				}				
			}
			if(bbottom > 0) {
				g.setColor(this.getBorderBottomColor());
				int lastY = startY + totalHeight - 1;
				for(int i = 0; i < bbottom; i++) {
					int leftOffset = (i * bleft) / bbottom;
					int rightOffset = (i * bright) / bbottom;					
					g.drawLine(startX + leftOffset, lastY - i, startX + totalWidth - rightOffset - 1, lastY - i);
				}				
			}

			// Adjust client area border
			totalWidth -= (bleft + bright);
			totalHeight -= (btop + bbottom);
			startX += bleft;
			startY += btop;

		}
		if(totalWidth != startWidth || totalHeight != startHeight) {
			g.clipRect(startX, startY, totalWidth, totalHeight);
		}
		Color bkg = this.backgroundColor;
		if(bkg != null && bkg.getAlpha() > 0) {
			g.setColor(bkg);
			Rectangle bkgBounds = g.getClipBounds();
			g.fillRect(bkgBounds.x, bkgBounds.y, bkgBounds.width, bkgBounds.height);
		}
		Image image = this.backgroundImage;
		if(image != null) {
			int w = image.getWidth(this);
			int h = image.getHeight(this);
			if(w != -1 && h != -1) {
				switch(this.backgroundRepeat) {
				case BR_NO_REPEAT: {
					int imageX;
					if(this.backgroundXPositionAbsolute) {
						imageX = startX + this.backgroundXPosition;
					}
					else {
						imageX = startX + (this.backgroundXPosition * (totalWidth - w)) / 100;
					}
					int imageY;
					if(this.backgroundYPositionAbsolute) {
						imageY = startY + this.backgroundYPosition;
					}
					else {
						imageY = startY + (this.backgroundYPosition * (totalHeight - h)) / 100;
					}
					g.drawImage(image, imageX, imageY, this);
					break;
				}
				case BR_REPEAT_X: {
					int imageY;
					if(this.backgroundYPositionAbsolute) {
						imageY = startY + this.backgroundYPosition;
					}
					else {
						imageY = startY + (this.backgroundYPosition * (totalHeight - h)) / 100;
					}
					int topX = startX + totalWidth;
					for(int x = startX; x < topX; x += w) {
						g.drawImage(image, x, imageY, this);
					}
					break;
				}
				case BR_REPEAT_Y: {
					int imageX;
					if(this.backgroundXPositionAbsolute) {
						imageX = startX + this.backgroundXPosition;
					}
					else {
						imageX = startX + (this.backgroundXPosition * (totalWidth - w)) / 100;
					}
					int topY = startY + totalHeight;
					for(int y = startY; y < topY; y += h) {
						g.drawImage(image, imageX, y, this);
					}
					break;
				}
				default: {
					int topX = startX + totalWidth;
					int topY = startY + totalHeight;
					for(int x = startX; x < topX; x += w) {
						for(int y = startY; y < topY; y += h) {
							g.drawImage(image, x, y, this);
						}
					}
					break;
				}
				}
			}
		}
	}


	private static final int BR_NO_REPEAT = 0;
	private static final int BR_REPEAT = 1;
	private static final int BR_REPEAT_X = 2;
	private static final int BR_REPEAT_Y = 3;
	
	public void setRootNode(NodeImpl node) {
		this.setRootNode(node, true);
	}

	public void setRootNode(NodeImpl node, boolean setContainer) {
		NodeImpl prevNode = this.rootNode;
		if(node != prevNode) {
			if(node instanceof RenderableContext && setContainer) {
				node.setContainingBlockContext(this);
			}
			this.rootNode = node;
			this.lastComponentSize = null;
			if(node instanceof HTMLElementImpl) {
				HTMLElementImpl rootElement = (HTMLElementImpl) node;
				this.applyStyle(rootElement);
			}
			else if(node instanceof HTMLDocumentImpl) {
				this.clearStyle();
//				HTMLElementImpl rootElement = (HTMLElementImpl) ((HTMLDocumentImpl) node).getDocumentElement();
//				if(rootElement != null) {
//					this.applyStyle(rootElement);
//				}
			}
			this.invalidate();
		}
	}
		
	public NodeImpl getRootNode() {
		return this.rootNode;
	}
		
	public void paint(Graphics g) {
		this.prePaint(g);
		//long time1 = System.currentTimeMillis();
		int numRenderables = 0;
		try {
			Rectangle clipBounds = g.getClipBounds();
			Iterator i = this.bodyLayout.getRenderables(clipBounds);
			int lineCount = 0;
			while(i.hasNext()) {
				lineCount++;
				Object robj = i.next();
				if(robj instanceof BoundableRenderable) {
					BoundableRenderable renderable = (BoundableRenderable) robj;
					Rectangle bounds = renderable.getBounds();
					//if(logger.isDebugEnabled()) logger.debug("paintComponent(): renderable=" + renderable + ",bounds=" + bounds);
					Graphics rg = g.create(bounds.x, bounds.y, bounds.width, bounds.height);
					renderable.paint(rg);
					numRenderables++;
				}
				else {
					((Renderable) robj).paint(g);
				}
			}
			
			// Paint FrameContext selection
			
			RenderablePoint start = this.startSelection;
			RenderablePoint end = this.endSelection;
			boolean inSelection = false;
			if(start != null && end != null && !start.equals(end)) {
				lineCount = 0;
				Renderable[] renderables = this.bodyLayout.getRenderablesArray();
				for(int j = 0; j < renderables.length; j++) {
					lineCount++;
					Object robj = renderables[j];
					if(robj instanceof BoundableRenderable) {
						BoundableRenderable renderable = (BoundableRenderable) robj;
						Rectangle bounds = renderable.getBounds();
						//if(logger.isDebugEnabled()) logger.debug("paintComponent(): renderable=" + renderable + ",bounds=" + bounds);
						Graphics rg = g.create(bounds.x, bounds.y, bounds.width, bounds.height);
						boolean newInSelection = renderable.paintSelection(rg, inSelection, start, end);
						if(inSelection && !newInSelection) {
							inSelection = false;
							break;
						}
						inSelection = newInSelection;
					}
				}
			}
		} finally {
			//long time2 = System.currentTimeMillis();
			//System.out.println("NonScrollableHtmlPanel.paintComponent(): numRenderables=" + numRenderables + ",time=" + (time2 - time1) + " ms.");
		}
	}
	
	
	
	private int alignXPercent;
	private int alignYPercent;	
	private Dimension lastComponentSize;
	private Dimension lastResultingSize;
	private int lastLayoutAvailHeight;

	/**
	 * 
	 * @param componentSize
	 * @param availHeight
	 * @return The size needed to render the content, including insets.
	 */
	private Dimension ensureLayout(Dimension componentSize, int availHeight) {
		//long time1 = System.currentTimeMillis();
		try {
			Dimension lcs = this.lastComponentSize;
			Dimension lrs = this.lastResultingSize;
			if(lrs != null && lcs != null && lcs.width < lrs.width && componentSize.width < lrs.width && availHeight == this.lastLayoutAvailHeight) {
				return lrs;
			}
			else {
				//Note: This caching is and should be independent of component invalidation.
				this.lastComponentSize = componentSize;
				NodeImpl node = this.rootNode;
				if(node != null) {
					RenderState rs = this.renderState;
					if(rs == null) {
						rs = new BlockRenderState();
					}
					Dimension size = this.bodyLayout.layout(componentSize, availHeight, node, this.alignXPercent, this.alignYPercent, rs);
					//if(size.width > componentSize.width) {
					//	System.out.println("ensureLayout(): Resulting width=" + size.width + " larger than component width=" + componentSize.width);
					//}
					this.lastResultingSize = size;
					this.lastLayoutAvailHeight = availHeight;
					return size;
				}
				else {
					Dimension size = new Dimension(0, 0);
					this.lastResultingSize = null;
					this.lastLayoutAvailHeight = availHeight;
					return size;
				}
			}
		} finally {
			//long time2 = System.currentTimeMillis();
			//System.out.println("ensureLayout(): time=" + (time2 - time1) + " ms.");
		}
	}
	
	//private int availHeight = -1;
		
	/**
	 * @param width The width available, including insets.
	 * @param height The height available, including insets.
	 */
	protected Dimension layoutFor(int width, int height) {
		this.lastAvailHeight = -1;
		this.lastAvailWidth = -1;
		Dimension newSize = this.ensureLayout(new Dimension(width, height), height);
		this.layoutSize = newSize;
		this.markValidated();
		return newSize;
	}
	
	public int getAlignXPercent() {
		return alignXPercent;
	}
	
	public void setAlignXPercent(int alignXPercent) {
		this.alignXPercent = alignXPercent;
	}
	
	public int getAlignYPercent() {
		return alignYPercent;
	}
	
	public void setAlignYPercent(int alignYPercent) {
		this.alignYPercent = alignYPercent;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.ContainingBlockContext#repaint(org.xamjwg.html.renderer.RenderableContext)
	 */
	public void invalidateState(RenderableContext renderableContext) {
		Renderable[] renderables = this.bodyLayout.getRenderablesArray();
		int length = renderables.length;
		for(int i = 0; i < length; i++) {
			Object r = renderables[i];
			if(r instanceof BoundableRenderable) {
				((BoundableRenderable) r).invalidateState(renderableContext);
			}
		}
	}
	
	private int getDeclaredWidth(int availWidth) {
		Object rootNode = this.rootNode;
		if(rootNode instanceof HTMLElementImpl) {
			HTMLElementImpl element = (HTMLElementImpl) rootNode;
			CSS2Properties props = element.getStyle();
			String widthText = props.getWidth();
			if(widthText == null || "".equals(widthText)) {
				return -1;
			}
			return HtmlValues.getPixelSize(widthText, this.renderState, -1, availWidth);			
		}
		else {
			return -1;
		}
	}

	private int getDeclaredHeight(int availHeight) {
		Object rootNode = this.rootNode;
		if(rootNode instanceof HTMLElementImpl) {
			HTMLElementImpl element = (HTMLElementImpl) rootNode;
			CSS2Properties props = element.getStyle();
			String heightText = props.getHeight();
			if(heightText == null || "".equals(heightText)) {
				return -1;
			}
			return HtmlValues.getPixelSize(heightText, this.renderState, -1, availHeight);			
		}
		else {
			return -1;
		}		
	}

	private Dimension layoutSize = null;
	private int lastAvailHeight = -1;
	private int lastAvailWidth = -1;
		
	public Dimension layout(int availWidth, int availHeight) {
		if(this.layoutSize == null || availHeight != this.lastAvailHeight || availWidth != this.lastAvailWidth) {
			this.lastAvailHeight = availHeight;
			this.lastAvailWidth = availWidth;
			int dw = this.getDeclaredWidth(availWidth);
			int tentativeWidth = dw == -1 ? availWidth : dw;
			int dh = this.getDeclaredHeight(availHeight);
			int tentativeHeight = dh == -1 ? availHeight : dh;
			Dimension size = this.ensureLayout(new Dimension(tentativeWidth, tentativeHeight), availHeight);
			if(size.width > tentativeWidth) {
				tentativeWidth = size.width;
			}
			tentativeHeight = dh == -1 ? size.height : Math.max(dh, size.height);
			Dimension newSize = new Dimension(tentativeWidth, tentativeHeight);
			this.layoutSize = newSize;
			this.markValidated();
			return newSize;
		}
		else {
			return this.layoutSize;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.UIControl#getBackgroundColor()
	 */
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.UIControl#paintSelection(java.awt.Graphics, boolean, org.xamjwg.html.renderer.RenderablePoint, org.xamjwg.html.renderer.RenderablePoint)
	 */
	public boolean paintSelection(Graphics g, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		Renderable[] renderables = this.bodyLayout.getRenderablesArray();
		for(int i = 0; i < renderables.length; i++) {
			Object robj = renderables[i];
			if(robj instanceof BoundableRenderable) {
				BoundableRenderable renderable = (BoundableRenderable) robj;
				Rectangle bounds = renderable.getBounds();
				//if(logger.isDebugEnabled()) logger.debug("paintComponent(): renderable=" + renderable + ",bounds=" + bounds);
				Graphics rg = g.create(bounds.x, bounds.y, bounds.width, bounds.height);
				boolean newInSelection = renderable.paintSelection(rg, inSelection, startPoint, endPoint);
				if(inSelection && !newInSelection) {
					return false;
				}
				inSelection = newInSelection;
			}
		}
		return inSelection;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContainer#add(java.awt.Component)
	 */
	public Component add(Component component) {
		return this.container.add(component);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#getRenderablePoint(int, int)
	 */
	public RenderablePoint getRenderablePoint(int x, int y) {
	    BoundableRenderable br = this.bodyLayout.getRenderable(new Point(x, y));
	    if(br != null) {
	    	Rectangle bounds = br.getBounds();
	    	return br.getRenderablePoint(x - bounds.x, y - bounds.y);
	    }
	    else {
	    	return null;
	    }
	}
	
	public void invalidate() {
		super.invalidate();
		this.lastAvailHeight = -1;
		this.lastAvailWidth = -1;
		this.lastComponentSize = null;
		this.lastLayoutAvailHeight = -1;
		this.lastResultingSize = null;
		this.layoutSize = null;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMouseClick(java.awt.event.MouseEvent, int, int)
	 */
	public void onMouseClick(MouseEvent event, int x, int y) {
	    BoundableRenderable br = this.bodyLayout.getRenderable(new Point(x, y));
	    if(br != null) {
	    	Rectangle bounds = br.getBounds();
	    	br.onMouseClick(event, x - bounds.x, y - bounds.y);
	    }
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMouseDisarmed(java.awt.event.MouseEvent)
	 */
	public void onMouseDisarmed(MouseEvent event) {
		BoundableRenderable br = this.armedRenderable;
		if(br != null) {
			br.onMouseDisarmed(event);
			this.armedRenderable = null;
		}
	}

	private BoundableRenderable armedRenderable;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMousePressed(java.awt.event.MouseEvent, int, int)
	 */
	public void onMousePressed(MouseEvent event, int x, int y) {
	    BoundableRenderable br = this.bodyLayout.getRenderable(new Point(x, y));
	    if(br != null) {
	    	Rectangle bounds = br.getBounds();
	    	br.onMousePressed(event, x - bounds.x, y - bounds.y);
	    	this.armedRenderable = br;
	    }
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#onMouseReleased(java.awt.event.MouseEvent, int, int)
	 */
	public void onMouseReleased(MouseEvent event, int x, int y) {
	    BoundableRenderable br = this.bodyLayout.getRenderable(new Point(x, y));
	    if(br != null) {
	    	Rectangle bounds = br.getBounds();
	    	br.onMouseReleased(event, x - bounds.x, y - bounds.y);
	    	BoundableRenderable oldArmedRenderable = this.armedRenderable;
	    	if(oldArmedRenderable != null && br != oldArmedRenderable) {
	    		oldArmedRenderable.onMouseDisarmed(event);
	    		this.armedRenderable = null;
	    	}
	    }
	    else {
	    	BoundableRenderable oldArmedRenderable = this.armedRenderable;
	    	if(oldArmedRenderable != null) {
	    		oldArmedRenderable.onMouseDisarmed(event);
	    		this.armedRenderable = null;
	    	}	    	
	    }
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContainer#getBackground()
	 */
	public Color getBackground() {
		return this.container.getBackground();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RCollection#getRenderables()
	 */
	public Iterator getRenderables() {
		return this.bodyLayout.getRenderables();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.ContainingBlockContext#repaint(org.xamjwg.html.domimpl.RenderableContext)
	 */
	public void repaint(RenderableContext renderableContext) {
		this.invalidateState(renderableContext);
		this.repaint();
	}

	public boolean extractSelectionText(StringBuffer buffer, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		Renderable[] renderables = this.bodyLayout.getRenderablesArray();
		for(int i = 0; i < renderables.length; i++) {
			Object robj = renderables[i];
			if(robj instanceof BoundableRenderable) {
				BoundableRenderable renderable = (BoundableRenderable) robj;
				//if(logger.isDebugEnabled()) logger.debug("paintComponent(): renderable=" + renderable + ",bounds=" + bounds);
				boolean newInSelection = renderable.extractSelectionText(buffer, inSelection, startPoint, endPoint);
				if(inSelection && !newInSelection) {
					return false;
				}
				inSelection = newInSelection;
			}
		}
		return inSelection;
	}

}