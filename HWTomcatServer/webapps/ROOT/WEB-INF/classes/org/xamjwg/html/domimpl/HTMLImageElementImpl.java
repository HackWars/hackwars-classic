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
package org.xamjwg.html.domimpl;

import org.w3c.dom.html2.HTMLImageElement;
import org.xamjwg.html.renderer.*;

public class HTMLImageElementImpl extends HTMLElementImpl implements
		HTMLImageElement {
	public HTMLImageElementImpl() {
		super("IMG");
	}

	public String getName() {
		return this.getAttribute("name");
	}

	public void setName(String name) {
		this.setAttribute("name", name);
	}

	private String src;
	
	public String getAlign() {
		return this.getAttribute("align");
	}

	public void setAlign(String align) {
		this.setAttribute("align", align);
	}

	public String getAlt() {
		return this.getAttribute("alt");
	}

	public void setAlt(String alt) {
		this.setAttribute("alt", alt);
	}

	public String getBorder() {
		return this.getAttribute("border");
	}

	public void setBorder(String border) {
		this.setAttribute("border", border);
	}

	public int getHeight() {
		ContainingBlockContext r = this.containingBlockContext;
		return r == null ? 0 : r.getBounds().height;
	}

	public void setHeight(int height) {
		this.setAttribute("height", String.valueOf(height));
	}

	public int getHspace() {
		return this.getAttributeAsInt("hspace", 0); 
	}

	public void setHspace(int hspace) {
		this.setAttribute("hspace", String.valueOf("hspace"));
	}

	public boolean getIsMap() {
		return this.getAttributeAsBoolean("isMap", false);
	}

	public void setIsMap(boolean isMap) {
		this.setAttribute("isMap", isMap ? "isMap" : null);
	}

	public String getLongDesc() {
		return this.getAttribute("longDesc");
	}

	public void setLongDesc(String longDesc) {
		this.setAttribute("longDesc", longDesc);
	}

	public String getSrc() {
		return this.src;
	}

	public void setSrc(String src) {
		this.setAttribute("src", src);
	}

	public String getUseMap() {
		return this.getAttribute("useMap");
	}

	public void setUseMap(String useMap) {
		this.setAttribute("useMap", useMap);
	}

	public int getVspace() {
		return this.getAttributeAsInt("vspace", 0);
	}

	public void setVspace(int vspace) {
		this.setAttribute("vspace", String.valueOf(vspace));
	}

	public int getWidth() {
		ContainingBlockContext r = this.containingBlockContext;
		return r == null ? 0 : r.getBounds().width;
	}

	public void setWidth(int width) {
		this.setAttribute("width", String.valueOf(width));
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContext#getHeightLength()
	 */
	public HtmlLength getHeightLength() {
		return this.heightLength;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContext#getWidthLength()
	 */
	public HtmlLength getWidthLength() {
		return this.widthLength;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContext#getAlignmentX()
	 */
	public float getAlignmentX() {
		return 0.5f;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContext#getAlignmentY()
	 */
	public float getAlignmentY() {
		return this.alignmentY;
	}

	private HtmlLength widthLength;
	private HtmlLength heightLength;
	private float alignmentY = 1.0f;

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.ElementImpl#assignAttributeField(java.lang.String, java.lang.String)
	 */
	protected void assignAttributeField(String normalName, String value) {
		super.assignAttributeField(normalName, value);
		if("SRC".equals(normalName)) {
			this.src = value;
		}
		else if("WIDTH".equals(normalName)) {
			try {
				this.widthLength = new HtmlLength(value);
			} catch(Exception err) {
				this.warn("Bad width spec: " + value, err);
				this.widthLength = null;
			}
		}
		else if("HEIGHT".equals(normalName)) {
			try {
				this.heightLength = new HtmlLength(value);
			} catch(Exception err) {
				this.warn("Bad height spec: " + value, err);
				this.heightLength = null;
			}
		}
		else if("ALIGN".equals(normalName)) {
			this.assignAlignment(value);
		}
	}
	
	private final void assignAlignment(String value) {
		if(value.equalsIgnoreCase("middle")) {
			this.alignmentY = 0.5f;
		}
		else if(value.equalsIgnoreCase("top")) {
			this.alignmentY = 0.0f;
		}
		else if(value.equalsIgnoreCase("bottom")) {
			this.alignmentY = 1.0f;
		}
		else {
			this.alignmentY = 1.0f;
		}
	}
}
