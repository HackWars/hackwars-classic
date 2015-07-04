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
import java.io.*;

import org.w3c.dom.html2.HTMLImageElement;
import org.xamjwg.html.*;
import org.xamjwg.html.domimpl.RenderableContext;

public class ImgControl extends BaseControl {
	private volatile Image image;
	private final HtmlParserContext htmlParserContext;
	private String lastSrc;
	
	public ImgControl(RenderableContext renderableContext, HtmlParserContext pcontext) {
		super(renderableContext);
		this.htmlParserContext = pcontext;
	}
	
	public void addNotify() {
		super.addNotify();
		this.checkImgSrc();
	}
	
	public Image getImage(){
		return(image);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension size = this.getSize();
		Insets insets = this.getInsets();
		synchronized(this) {}
		Image image = this.image;
		if(image != null) {
			g.drawImage(image, 
					insets.left, insets.top, 
					size.width - insets.left - insets.right, 
					size.height - insets.top - insets.bottom, this);
		}
		else {
			//TODO: alt
		}
	}
	
	private int lastReturnedPw = 1;
	private int lastReturnedPh = 1;
	
	protected int getPreferredWidthImpl(int availWidth, int availHeight) {
		HtmlLength width = this.renderableContext.getWidthLength();
		if(width != null) {
			return width.getLength(availWidth);
		}
		synchronized(this) {}
		Image img = this.image;
		int w = img == null ? this.lastReturnedPw : img.getWidth(this);
		int retValue = w == -1 ? this.lastReturnedPw : w;
		this.lastReturnedPw = retValue;
		return retValue;
	}
	
	protected int getPreferredHeightImpl(int availWidth, int availHeight) {
		HtmlLength height = this.renderableContext.getHeightLength();
		if(height != null) {
			return height.getLength(availHeight);
		}
		synchronized(this) {}
		Image img = this.image;
		int h = img == null ? this.lastReturnedPh : img.getHeight(this);
		int retValue = h == -1 ? this.lastReturnedPh : h;
		this.lastReturnedPh = retValue;
		return retValue;
	}
	
	private boolean isWidthAndHeightSet() {
		RenderableContext rc = this.renderableContext;
		return rc.getWidthLength() != null && rc.getHeightLength() != null;
	}
	
	private void checkImgSrc() {
		RenderableContext rc = this.renderableContext;
		if(rc instanceof HTMLImageElement) {
			HTMLImageElement ie = (HTMLImageElement) rc;
			String newSrc = ie.getSrc();
			newSrc=newSrc.toLowerCase();
			if(newSrc == null) {
				this.lastSrc = null;
				this.image = null;
			}
			else if(!newSrc.equals(this.lastSrc)) {
				this.lastSrc = newSrc;
				this.image = null;
				this.loadImage(newSrc);
			}
		}		
	}
	
	public void invalidate() {
		super.invalidate();
		this.checkImgSrc();
	}
		
	/* (non-Javadoc)
	 * @see java.awt.Component#imageUpdate(java.awt.Image, int, int, int, int, int)
	 */
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
		if((infoflags & ImageObserver.ALLBITS) != 0 || (infoflags & ImageObserver.FRAMEBITS) != 0) {
			if(this.isWidthAndHeightSet()) {
				this.repaint();
			}
			else {
				Insets insets = this.getInsets();
				Dimension currentBounds = this.getSize();
				if(w == currentBounds.width - insets.left - insets.right && h == currentBounds.height - insets.top - insets.bottom) {
					this.repaint();
				}
				else {
					this.invalidateAndRepaint();
				}
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#imageUpdate(java.awt.Image, int, int, int, int, int)
	 */
	public void imageUpdate(Image img, int w, int h) {
		if(this.isWidthAndHeightSet()) {
			this.repaint();
		}
		else {
			Insets insets = this.getInsets();
			Dimension currentBounds = this.getSize();
			if(w == currentBounds.width - insets.left - insets.right && h == currentBounds.height - insets.top - insets.bottom) {
				this.repaint();
			}
			else {
				this.invalidateAndRepaint();
			}
		}
	}

	private final void loadImage(String newSrc) {
		newSrc=newSrc.toLowerCase();

		
		RenderableContext rc = this.renderableContext;
		HtmlParserContext ctx = this.htmlParserContext;
		if(ctx != null) {
			final HttpRequest request = ctx.createHttpRequest();
			request.addReadyStateChangeListener(new ReadyStateChangeListener() {
				public void readyStateChanged() {
					int readyState = request.getReadyState();
					if(readyState == HttpRequest.STATE_COMPLETE) {
						int status = request.getStatus();
						if(status == 200 || status == 0) {
							Image img = request.getResponseImage();
							ImgControl.this.image = img;
							synchronized(this) {}
							// Cause observer to be called...
							int w = img.getWidth(ImgControl.this);
							int h = img.getHeight(ImgControl.this);
							// Maybe image already done...
							if(w != -1 && h != -1) {
								ImgControl.this.imageUpdate(img, w, h);
							}
						}							
					}
				}
			});
			try {
				URL fullUrl = rc.getFullURL(newSrc);
				File check=new File(fullUrl.getPath());
				if(!check.exists()){
					fullUrl=new File("data/error.gif").toURL();
				}
				request.open("GET", fullUrl.toExternalForm(), true);
			} catch(MalformedURLException mfu) {
				rc.warn("Bad image url: " + newSrc, mfu);
			}
		}
	}
	
	public boolean paintSelection(Graphics g, boolean inSelection, RenderablePoint startPoint, RenderablePoint endPoint) {
		return inSelection;
	}		
}
