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
 * Created on Nov 20, 2005
 */
package org.xamjwg.html.domimpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSS2Properties;
import org.w3c.dom.css.CSSStyleDeclaration;

public class CSS2PropertiesImpl implements CSS2Properties {
	private final CSS2PropertiesContext context;
	private Object styleDeclarations = null;
	
	public CSS2PropertiesImpl(CSS2PropertiesContext context) {
		this.context = context;
	}
	
	public void addStyleDeclaration(CSSStyleDeclaration styleDeclaration) {
		Object sd = this.styleDeclarations;
		if(sd == null) {
			this.styleDeclarations = styleDeclaration;
		}
		else if(sd instanceof Collection) {
			synchronized(this) {
				((Collection) sd).add(styleDeclaration);
			}
		}
		else {
			//Note: Must be ArrayList
			Collection sdc = new ArrayList();
			sdc.add(sd);
			sdc.add(styleDeclaration);
			this.styleDeclarations = sdc;
		}
	}
	
	private final String getPropertyValue(String name) {
		Object sd = this.styleDeclarations;
		if(sd instanceof CSSStyleDeclaration) {
			return ((CSSStyleDeclaration) sd).getPropertyValue(name);
		}
		else if(sd instanceof ArrayList) {
			ArrayList sds = (ArrayList) sd;
			synchronized(sds) {
				int size = sds.size();
				for(int i = size; --i >= 0;) {
					CSSStyleDeclaration styleDeclaration = (CSSStyleDeclaration) sds.get(i);
					String pv = styleDeclaration.getPropertyValue(name);
					if(pv != null && !"".equals(pv)) {
						return pv;
					}
				}
			}
			return null;
		}
		else {
			return null;
		}
	}
	
	private String azimuth;
	
	public String getAzimuth() {
		String azimuth = this.azimuth;
		if(azimuth != null) {
			return azimuth;
		}
		return this.getPropertyValue("azimuth");
	}

	public void setAzimuth(String azimuth) throws DOMException {
		this.azimuth = azimuth;
	}

	private String background;
	
	public String getBackground() {
		String background = this.background;
		if(background != null) {
			return background;
		}
		return this.getPropertyValue("background");
	}

	public void setBackground(String background) throws DOMException {
		this.background = background;
	}

	private String backgroundAttachment;
	
	public String getBackgroundAttachment() {
		String value = this.backgroundAttachment;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("background-attachment");
	}

	public void setBackgroundAttachment(String backgroundAttachment)
			throws DOMException {
		this.backgroundAttachment = backgroundAttachment;
	}

	private String backgroundColor;
	
	public String getBackgroundColor() {
		String value = this.backgroundColor;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("background-color");
	}

	public void setBackgroundColor(String backgroundColor) throws DOMException {
		this.backgroundColor = backgroundColor;
		this.context.repaint();
	}
	private String backgroundImage;
	public String getBackgroundImage() {
		String value = this.backgroundImage;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("background-image");
	}

	public void setBackgroundImage(String backgroundImage) throws DOMException {
		this.backgroundImage = backgroundImage;
		this.context.repaint();
	}

	private String backgroundPosition;
	
	public String getBackgroundPosition() {
		String value = this.backgroundPosition;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("background-position");
	}

	public void setBackgroundPosition(String backgroundPosition)
			throws DOMException {
		this.backgroundPosition = backgroundPosition;
	}

	private String backgroundRepeat;
	
	public String getBackgroundRepeat() {
		String value = this.backgroundRepeat;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("background-repeat");
	}

	public void setBackgroundRepeat(String backgroundRepeat)
			throws DOMException {
		this.backgroundRepeat = backgroundRepeat;
	}

	private String border;
	
	public String getBorder() {
		String value = this.border;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border");
	}

	public void setBorder(String border) throws DOMException {
		this.border = border;
	}

	private String borderCollapse;
	
	public String getBorderCollapse() {
		String value = this.borderCollapse;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-collapse");
	}

	public void setBorderCollapse(String borderCollapse) throws DOMException {
		this.borderCollapse = borderCollapse;
	}

	private String borderColor;
	
	public String getBorderColor() {
		String value = this.borderColor;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-color");
	}

	public void setBorderColor(String borderColor) throws DOMException {
		this.borderColor = borderColor;
	}

	private String borderSpacing;
	
	public String getBorderSpacing() {
		String value = this.borderSpacing;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-spacing");		
	}

	public void setBorderSpacing(String borderSpacing) throws DOMException {
		this.borderSpacing = borderSpacing;
	}
	
	private String borderStyle;

	public String getBorderStyle() {
		String value = this.borderStyle;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-style");		
	}

	public void setBorderStyle(String borderStyle) throws DOMException {
		this.borderStyle = borderStyle;
	}

	private String borderTop;
	
	public String getBorderTop() {
		String value = this.borderTop;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-top");		
	}

	public void setBorderTop(String borderTop) throws DOMException {
		this.borderTop = borderTop;
	}

	private String borderRight;
	
	public String getBorderRight() {
		String value = this.borderRight;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-right");		
	}

	public void setBorderRight(String borderRight) throws DOMException {
		this.borderRight = borderRight;
	}

	private String borderBottom;
	
	public String getBorderBottom() {
		String value = this.borderBottom;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-bottom");		
	}

	public void setBorderBottom(String borderBottom) throws DOMException {
		this.borderBottom = borderBottom;
	}

	private String borderLeft;
	
	public String getBorderLeft() {
		String value = this.borderLeft;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-left");		
	}

	public void setBorderLeft(String borderLeft) throws DOMException {
		this.borderLeft = borderLeft;
	}
	
	private String borderTopColor;

	public String getBorderTopColor() {
		String value = this.borderTopColor;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-top-color");				
	}

	public void setBorderTopColor(String borderTopColor) throws DOMException {
		this.borderTopColor = borderTopColor;
	}

	private String borderRightColor;
	
	public String getBorderRightColor() {
		String value = this.borderRightColor;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-right-color");						
	}

	public void setBorderRightColor(String borderRightColor)
			throws DOMException {
		this.borderRightColor = borderRightColor;
	}

	private String borderBottomColor;
	
	public String getBorderBottomColor() {
		String value = this.borderBottomColor;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-bottom-color");						
	}

	public void setBorderBottomColor(String borderBottomColor)
			throws DOMException {
		this.borderBottomColor = borderBottomColor;
	}

	private String borderLeftColor;
	
	public String getBorderLeftColor() {
		String value = this.borderLeftColor;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-left-color");						
	}

	public void setBorderLeftColor(String borderLeftColor) throws DOMException {
		this.borderLeftColor = borderLeftColor;
	}

	private String borderTopStyle;
	
	public String getBorderTopStyle() {
		String value = this.borderTopStyle;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-top-style");						
	}

	public void setBorderTopStyle(String borderTopStyle) throws DOMException {
		this.borderTopStyle = borderTopStyle;
	}

	private String borderRightStyle;
	
	public String getBorderRightStyle() {
		String value = this.borderRightStyle;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-right-style");						
	}

	public void setBorderRightStyle(String borderRightStyle)
			throws DOMException {
		this.borderRightStyle = borderRightStyle;
	}

	private String borderBottomStyle;
	
	public String getBorderBottomStyle() {
		String value = this.borderBottomStyle;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-bottom-style");						
	}

	public void setBorderBottomStyle(String borderBottomStyle)
			throws DOMException {
		this.borderBottomStyle = borderBottomStyle;
	}

	private String borderLeftStyle;
	
	public String getBorderLeftStyle() {
		String value = this.borderLeftStyle;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-left-style");						
	}

	public void setBorderLeftStyle(String borderLeftStyle) throws DOMException {
		this.borderLeftStyle = borderLeftStyle;
	}

	private String borderTopWidth;
	
	public String getBorderTopWidth() {
		String value = this.borderTopWidth;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-top-width");						
	}

	public void setBorderTopWidth(String borderTopWidth) throws DOMException {
		this.borderTopWidth = borderTopWidth;
	}

	private String borderRightWidth;
	
	public String getBorderRightWidth() {
		String value = this.borderRightWidth;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-right-width");						
	}

	public void setBorderRightWidth(String borderRightWidth)
			throws DOMException {
		this.borderRightWidth = borderRightWidth;
	}

	private String borderBottomWidth;
	
	public String getBorderBottomWidth() {
		String value = this.borderTopWidth;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-bottom-width");						
	}

	public void setBorderBottomWidth(String borderBottomWidth)
			throws DOMException {
		this.borderBottomWidth = borderBottomWidth;
	}

	private String borderLeftWidth;
	
	public String getBorderLeftWidth() {
		String value = this.borderLeftWidth;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-left-width");						
	}

	public void setBorderLeftWidth(String borderLeftWidth) throws DOMException {
		this.borderLeftWidth = borderLeftWidth;
	}

	private String borderWidth;
	
	public String getBorderWidth() {
		String value = this.borderWidth;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("border-width");								
	}

	public void setBorderWidth(String borderWidth) throws DOMException {
		this.borderWidth = borderWidth;
	}

	private String bottom;
	
	public String getBottom() {
		String value = this.bottom;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("bottom");								
	}

	public void setBottom(String bottom) throws DOMException {
		this.bottom = bottom;
	}

	private String captionSide;
	
	public String getCaptionSide() {
		String value = this.captionSide;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("caption-side");								
	}

	public void setCaptionSide(String captionSide) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getClear() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setClear(String clear) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getClip() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setClip(String clip) throws DOMException {
		// TODO Auto-generated method stub

	}

	private volatile String color;
	
	public String getColor() {
		String value = this.color;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("color");						
	}

	public void setColor(String color) throws DOMException {
		this.color = color;
		this.context.repaint();
	}

	public String getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setContent(String content) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getCounterIncrement() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCounterIncrement(String counterIncrement)
			throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getCounterReset() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCounterReset(String counterReset) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getCue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCue(String cue) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getCueAfter() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCueAfter(String cueAfter) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getCueBefore() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCueBefore(String cueBefore) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getCursor() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCursor(String cursor) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDirection(String direction) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getDisplay() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDisplay(String display) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getElevation() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setElevation(String elevation) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getEmptyCells() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setEmptyCells(String emptyCells) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getCssFloat() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCssFloat(String cssFloat) throws DOMException {
		// TODO Auto-generated method stub

	}

	private String font;
	
	public String getFont() {
		String value = this.font;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("font");						
	}
	public void setFont(String font) throws DOMException {
		this.font = font;
	}
	private String fontFamily;
	public String getFontFamily() {
		String value = this.fontFamily;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("font-family");						
	}
	public void setFontFamily(String fontFamily) throws DOMException {
		this.fontFamily = fontFamily;
	}

	private String fontSize;
	
	public String getFontSize() {
		String value = this.fontSize;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("font-size");						
	}

	public void setFontSize(String fontSize) throws DOMException {
		this.fontSize = fontSize;
	}

	private String fontSizeAdjust;
	
	public String getFontSizeAdjust() {
		String value = this.fontSizeAdjust;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("font-size-adjust");						
	}

	public void setFontSizeAdjust(String fontSizeAdjust) throws DOMException {
		this.fontSizeAdjust = fontSizeAdjust;
	}

	private String fontStretch;
	
	public String getFontStretch() {
		String value = this.fontStretch;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("font-stretch");						
	}

	public void setFontStretch(String fontStretch) throws DOMException {
		this.fontStretch = fontStretch;
	}

	private String fontStyle;
	
	public String getFontStyle() {
		String value = this.fontStyle;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("font-style");						
	}

	public void setFontStyle(String fontStyle) throws DOMException {
		this.fontStyle = fontStyle;
	}

	private String fontVariant;
	
	public String getFontVariant() {
		String value = this.fontVariant;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("font-variant");						
	}

	public void setFontVariant(String fontVariant) throws DOMException {
		this.fontVariant = fontVariant;
	}

	private String fontWeight;
	
	public String getFontWeight() {
		String value = this.fontWeight;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("font-weight");						
	}

	public void setFontWeight(String fontWeight) throws DOMException {
		this.fontWeight = fontWeight;
	}

	private String height;
	
	public String getHeight() {
		String value = this.height;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("height");
	}

	public void setHeight(String height) throws DOMException {
		this.height = height;
	}

	public String getLeft() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLeft(String left) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getLetterSpacing() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLetterSpacing(String letterSpacing) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getLineHeight() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLineHeight(String lineHeight) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getListStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setListStyle(String listStyle) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getListStyleImage() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setListStyleImage(String listStyleImage) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getListStylePosition() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setListStylePosition(String listStylePosition)
			throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getListStyleType() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setListStyleType(String listStyleType) throws DOMException {
		// TODO Auto-generated method stub

	}

	private String margin;
	
	public String getMargin() {
		String value = this.margin;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("margin");								
	}

	public void setMargin(String margin) throws DOMException {
		this.margin = margin;
	}

	private String marginTop;
	
	public String getMarginTop() {
		String value = this.marginTop;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("margin-top");								
	}

	public void setMarginTop(String marginTop) throws DOMException {
		this.marginTop = marginTop;
	}

	private String marginRight;
	
	public String getMarginRight() {
		String value = this.marginRight;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("margin-right");								
	}

	public void setMarginRight(String marginRight) throws DOMException {
		this.marginRight = marginRight;
	}

	private String marginBottom;
	
	public String getMarginBottom() {
		String value = this.marginBottom;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("margin-bottom");								
	}

	public void setMarginBottom(String marginBottom) throws DOMException {
		this.marginBottom = marginBottom;
	}

	private String marginLeft;
	
	public String getMarginLeft() {
		String value = this.marginLeft;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("margin-left");								
	}

	public void setMarginLeft(String marginLeft) throws DOMException {
		this.marginLeft = marginLeft;
	}

	public String getMarkerOffset() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMarkerOffset(String markerOffset) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getMarks() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMarks(String marks) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getMaxHeight() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMaxHeight(String maxHeight) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getMaxWidth() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMaxWidth(String maxWidth) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getMinHeight() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMinHeight(String minHeight) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getMinWidth() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMinWidth(String minWidth) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getOrphans() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOrphans(String orphans) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getOutline() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOutline(String outline) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getOutlineColor() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOutlineColor(String outlineColor) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getOutlineStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOutlineStyle(String outlineStyle) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getOutlineWidth() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOutlineWidth(String outlineWidth) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getOverflow() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOverflow(String overflow) throws DOMException {
		// TODO Auto-generated method stub

	}

	private String padding;
	
	public String getPadding() {
		String value = this.padding;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("padding");								
	}

	public void setPadding(String padding) throws DOMException {
		this.padding = padding;
	}

	private String paddingTop;
	
	public String getPaddingTop() {
		String value = this.paddingTop;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("padding-top");								
	}

	public void setPaddingTop(String paddingTop) throws DOMException {
		this.paddingTop = paddingTop;
	}

	private String paddingRight;
	
	public String getPaddingRight() {
		String value = this.paddingRight;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("padding-right");								
	}

	public void setPaddingRight(String paddingRight) throws DOMException {
		this.paddingRight = paddingRight;
	}

	private String paddingBottom;
	
	public String getPaddingBottom() {
		String value = this.paddingBottom;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("padding-bottom");								
	}

	public void setPaddingBottom(String paddingBottom) throws DOMException {
		this.paddingBottom = paddingBottom;
	}

	private String paddingLeft;
	
	public String getPaddingLeft() {
		String value = this.paddingLeft;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("padding-left");								
	}

	public void setPaddingLeft(String paddingLeft) throws DOMException {
		this.paddingLeft = paddingLeft;
	}

	public String getPage() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPage(String page) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getPageBreakAfter() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPageBreakAfter(String pageBreakAfter) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getPageBreakBefore() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPageBreakBefore(String pageBreakBefore) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getPageBreakInside() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPageBreakInside(String pageBreakInside) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getPause() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPause(String pause) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getPauseAfter() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPauseAfter(String pauseAfter) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getPauseBefore() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPauseBefore(String pauseBefore) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getPitch() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPitch(String pitch) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getPitchRange() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPitchRange(String pitchRange) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getPlayDuring() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPlayDuring(String playDuring) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPosition(String position) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getQuotes() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setQuotes(String quotes) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getRichness() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setRichness(String richness) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getRight() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setRight(String right) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getSize() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSize(String size) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getSpeak() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSpeak(String speak) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getSpeakHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSpeakHeader(String speakHeader) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getSpeakNumeral() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSpeakNumeral(String speakNumeral) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getSpeakPunctuation() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSpeakPunctuation(String speakPunctuation)
			throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getSpeechRate() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSpeechRate(String speechRate) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getStress() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setStress(String stress) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getTableLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTableLayout(String tableLayout) throws DOMException {
		// TODO Auto-generated method stub

	}

	private String textAlign;
	
	public String getTextAlign() {
		String value = this.textAlign;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("text-align");
	}

	public void setTextAlign(String textAlign) throws DOMException {
		this.textAlign = textAlign;
	}

	private String textDecoration;
	
	public String getTextDecoration() {
		String value = this.textDecoration;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("text-decoration");
	}

	public void setTextDecoration(String textDecoration) throws DOMException {
		this.textDecoration = textDecoration;
		this.context.repaint();
	}
	public String getTextIndent() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTextIndent(String textIndent) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getTextShadow() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTextShadow(String textShadow) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getTextTransform() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTextTransform(String textTransform) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getTop() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTop(String top) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getUnicodeBidi() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setUnicodeBidi(String unicodeBidi) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getVerticalAlign() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setVerticalAlign(String verticalAlign) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getVisibility() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setVisibility(String visibility) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getVoiceFamily() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setVoiceFamily(String voiceFamily) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getVolume() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setVolume(String volume) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getWhiteSpace() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setWhiteSpace(String whiteSpace) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getWidows() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setWidows(String widows) throws DOMException {
		// TODO Auto-generated method stub

	}

	private String width;
	
	public String getWidth() {
		String value = this.width;
		if(value != null) {
			return value;
		}
		return this.getPropertyValue("width");
	}

	public void setWidth(String width) throws DOMException {
		this.width = width;
	}

	public String getWordSpacing() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setWordSpacing(String wordSpacing) throws DOMException {
		// TODO Auto-generated method stub

	}

	public String getZIndex() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setZIndex(String zIndex) throws DOMException {
		// TODO Auto-generated method stub

	}
	private String overlayColor;
	
	public String getOverlayColor() {
		return this.overlayColor;
	}
	public void setOverlayColor(String value) {
		this.overlayColor = value;
		this.context.repaint();
	}
}
