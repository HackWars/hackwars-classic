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
import java.util.*;
import org.w3c.dom.css.CSS2Properties;
import org.xamjwg.html.domimpl.CSS2PropertiesContext;
import org.xamjwg.html.domimpl.CSS2PropertiesImpl;
import org.xamjwg.util.*;
//TODO: Later optimize by creating an interface for this class, with special
//implementations for common elements, like B, I, U, A, etc.
/**
 * @author J. H. S.
 */
public class TextRenderState implements RenderState,CSS2PropertiesContext {
	private final CSS2PropertiesImpl css2properties;
	private final RenderState prevRenderState;
//	private String lineBreakAfter;
//	private String paragraphBreakAfter;
	
	private Font iFont;
	//private LineMetrics iLineMetrics;
	private FontMetrics iFontMetrics;
	private Color iColor;
	private Color iBackgroundColor;
	private Color iOverlayColor;
	private int iTextDecoration = -1;
	private int iBlankWidth = -1;
	//private Boolean iTextAntiAliasing;
	private boolean iHighlight;
//	private int iLineBreakAfter = -1;
//	private int iParagraphBreakAfter = -1;
	//		
//	public static final int BREAK_AUTO = 0;
//	public static final int BREAK_ALWAYS = 1;
	private static final Map SYSTEM_FONTS = new HashMap();
	
	static {
		FontInfo systemFont = new FontInfo();
		SYSTEM_FONTS.put("caption", systemFont);
		SYSTEM_FONTS.put("icon", systemFont);
		SYSTEM_FONTS.put("menu", systemFont);
		SYSTEM_FONTS.put("message-box", systemFont);
		SYSTEM_FONTS.put("small-caption", systemFont);
		SYSTEM_FONTS.put("status-bar", systemFont);
	}
	public TextRenderState(RenderState prevRenderState, CSS2PropertiesImpl props) {
		this.css2properties = props;
		this.prevRenderState = prevRenderState;
	}
	public TextRenderState(RenderState prevRenderState) {
		this.css2properties = new CSS2PropertiesImpl(this);
		this.prevRenderState = prevRenderState;
	}
	
	public void repaint() {
		// Dummy implementation
	}
	
	public CSS2Properties getCssProperties() {
		return this.css2properties;
	}
	public TextRenderState() {
		this.css2properties = new CSS2PropertiesImpl(this);
		this.prevRenderState = null;
	}
	
	public final void invalidate() {
		this.iFont = null;
		this.iFontMetrics = null;
		this.iColor = null;
		this.iBackgroundColor = null;
		this.iOverlayColor = null;
		this.iTextDecoration = -1;
		this.iBlankWidth = -1;
		RenderState prs = this.prevRenderState;
		if(prs != null) {
			prs.invalidate();
		}
	}
	
	private static final String DEFAULT_FONT_FAMILY = "Times New Roman";
	private static final Float DEFAULT_FONT_SIZE = new Float(12.0f);	
	
	private final String getLocalFont() {
		String value = this.css2properties.getFont();
		if("".equals(value)) {
			value = null;
		}
		return value;
	}
	private final String getLocalFontFamily() {
		String value = this.css2properties.getFontFamily();
		if("".equals(value)) {
			value = null;
		}
		return value;
	}
	private final String getLocalFontStyle() {
		String value = this.css2properties.getFontStyle();
		if("".equals(value)) {
			value = null;
		}
		return value;
	}
	private final String getLocalFontVariant() {
		String value = this.css2properties.getFontVariant();
		if("".equals(value)) {
			value = null;
		}		
		return value;
	}
	private final String getLocalFontWeight() {
		String value = this.css2properties.getFontWeight();
		if("".equals(value)) {
			value = null;
		}
		return value;
	}
	private final String getLocalFontSize() {
		String value = this.css2properties.getFontSize();
		if("".equals(value)) {
			value = null;
		}		
		return value;
	}
	private static final Color DUMMY_COLOR = new Color(0);
	
	public Font getFont() {
		Font f = this.iFont;
		if(f != null) {
			return f;
		}
		Float fontSize = null;
		String fontStyle = null;
		String fontVariant = null;
		String fontWeight = null;
		String fontFamily = null;
		
		String fontSpec = this.getLocalFont();
		String newFontSize = this.getLocalFontSize();
		String newFontFamily = this.getLocalFontFamily();
		String newFontStyle = this.getLocalFontStyle();
		String newFontVariant = this.getLocalFontVariant();
		String newFontWeight = this.getLocalFontWeight();
		RenderState prs = this.prevRenderState;
		if(newFontSize == null && newFontWeight == null && newFontStyle == null && newFontFamily == null && newFontVariant == null && fontSpec == null) {
			if(prs != null) {
				f = prs.getFont();
				this.iFont = f;
				return f;
			}
		}
		if(fontSpec != null) {
			FontInfo fontInfo = this.createFontInfo(fontSpec);
			fontSize = fontInfo.fontSize;
			fontStyle = fontInfo.fontStyle;
			fontVariant = fontInfo.fontVariant;
			fontWeight = fontInfo.fontWeight;
			fontFamily = fontInfo.fontFamily;
		}		
		if(newFontSize != null) {
			try {
				fontSize = new Float(HtmlValues.getFontSize(newFontSize, prs));
			} catch(Exception err) {
				fontSize = DEFAULT_FONT_SIZE;
			}
		}
		if(fontSize == null) {
			fontSize = DEFAULT_FONT_SIZE;
		}
		if(newFontFamily != null) {
			fontFamily = newFontFamily;
		}
		if(fontFamily == null) {
			fontFamily = DEFAULT_FONT_FAMILY;
		}
		if(newFontStyle != null) {
			fontStyle = newFontStyle;
		}
		if(newFontVariant != null) {
			fontVariant = newFontVariant;
		}
		if(newFontWeight != null) {
			fontWeight = newFontWeight;
		}
		f = FontFactory.getInstance().getFont(fontFamily, fontStyle, fontVariant, fontWeight, fontSize);
		this.iFont = f;
		return f;
	}
	public Color getColor() {
		Color c = this.iColor;
		if(c != null) {
			return c;
		}
		String colorValue = this.css2properties.getColor();
		if(colorValue == null || "".equals(colorValue)) {
			RenderState prs = this.prevRenderState;
			if(prs != null) {
				c = prs.getColor();
				this.iColor = c;
				return c;
			}
			else {
				colorValue = "black";
			}
		}
		c = ColorFactory.getInstance().getColor(colorValue);
		this.iColor = c;
		return c;
	}
	
	public Color getTextBackgroundColor() {
		Color c = this.iBackgroundColor;
		if(c != null) {
			if(c == DUMMY_COLOR) {
				return null;
			}
			return c;
		}
		CSS2Properties props = this.css2properties;
		String colorValue = props.getBackgroundColor();
		if(colorValue == null || "".equals(colorValue)) {
			String backgroundValue = props.getBackground();
			if(backgroundValue != null && !"".equals(backgroundValue)) {
				colorValue = HtmlValues.getColorFromBackground(backgroundValue);
			}
			if(colorValue == null || "".equals(colorValue)) {
				RenderState prs = this.prevRenderState;
				if(prs != null) {
					c = prs.getTextBackgroundColor();
					if(c == null) {
						this.iBackgroundColor = DUMMY_COLOR;
					}
					else {
						this.iBackgroundColor = c;
					}
					return c;
				}
				else {
					colorValue = null;
				}
			}
		}
		c = colorValue == null ? DUMMY_COLOR : ColorFactory.getInstance().getColor(colorValue);
		this.iBackgroundColor = c;
		if(colorValue == null) {
			return null;
		}
		return c;
	}
	
	public Color getOverlayColor() {
		Color c = this.iOverlayColor;
		if(c != null) {
			if(c == DUMMY_COLOR) {
				return null;
			}
			return c;
		}
		String colorValue = this.css2properties.getOverlayColor();
		if(colorValue == null || "".equals(colorValue)) {
			RenderState prs = this.prevRenderState;
			if(prs != null) {
				c = prs.getOverlayColor();
				if(c == null) {
					this.iOverlayColor = DUMMY_COLOR;
				}
				else {
					this.iOverlayColor = c;
				}
				return c;
			}
			else {
				colorValue = null;
			}
		}
		c = colorValue == null ? DUMMY_COLOR : ColorFactory.getInstance().getColor(colorValue);
		this.iOverlayColor = c;
		if(colorValue == null) {
			return null;
		}
		return c;
	}
	
	public int getTextDecorationMask() {
		int td = this.iTextDecoration;
		if(td != -1) {
			return td;
		}
		String tdText = this.css2properties.getTextDecoration();
		if(tdText == null || "".equals(tdText)) {
			RenderState prs = this.prevRenderState;
			if(prs != null) {
				td = prs.getTextDecorationMask();
				this.iTextDecoration = td;
				return td;
			}
			else {
				tdText = null;
			}
		}
		td = 0;
		if(tdText != null) {
			StringTokenizer tok = new StringTokenizer(tdText, ", \t\n\r");
			while(tok.hasMoreTokens()) {
				String token = tok.nextToken();
				if("none".equals(token)) {
					// continue
				}
				else if("underline".equals(token)) {
					td |= TextRenderState.MASK_TEXTDECORATION_UNDERLINE;
				}
				else if("line-through".equals(token)) {
					td |= TextRenderState.MASK_TEXTDECORATION_LINE_THROUGH;
				}
				else if("blink".equals(token)) {
					td |= TextRenderState.MASK_TEXTDECORATION_BLINK;
				}
				else if("overline".equals(token)) {
					td |= TextRenderState.MASK_TEXTDECORATION_OVERLINE;
				}
			}
		}
		this.iTextDecoration = td;
		return td;
	}
	
	public FontMetrics getFontMetrics() {
		FontMetrics fm = this.iFontMetrics;
		if(fm == null) {
			//TODO getFontMetrics deprecated. How to get text width?
			fm = Toolkit.getDefaultToolkit().getFontMetrics(this.getFont());
			this.iFontMetrics = fm;
		}
		return fm;
	}
	
	public int getBlankWidth() {
		int bw = this.iBlankWidth;
		if(bw == -1) {
			bw = this.getFontMetrics().charWidth(' ');
			this.iBlankWidth = bw;
		}
		return bw;
	}

	/**
	 * @return Returns the iHighlight.
	 */
	public boolean isHighlight() {
		return this.iHighlight;
	}
	
	/**
	 * @param highlight The iHighlight to set.
	 */
	public void setHighlight(boolean highlight) {
		this.iHighlight = highlight;
	}
	
	private boolean isFontStyle(String token) {
		return "italic".equals(token) || "normal".equals(token) || "oblique".equals(token);
	}
	private boolean isFontVariant(String token) {
		return "small-caps".equals(token) || "normal".equals(token);
	}
	private boolean isFontWeight(String token) {
		if("bold".equals(token) || "bolder".equals(token) || "lighter".equals(token)) {
			return true;
		}
		try {
			int value = Integer.parseInt(token);
			return (value % 100) == 0 && value >= 100 && value <= 900;
		} catch(NumberFormatException nfe) {
			return false;
		}
	}
	
	private FontInfo createFontInfo(String fontSpec) {
		FontInfo fontInfo = (FontInfo) SYSTEM_FONTS.get(fontSpec);
		if(fontInfo != null) {
			return fontInfo;
		}
		fontInfo = new FontInfo();
		StringTokenizer tok = new StringTokenizer(fontSpec, " \t\r\n");
		if(tok.hasMoreTokens()) {
			String token = tok.nextToken();
			boolean hasMoreTokens = tok.hasMoreTokens();
			if(this.isFontStyle(token)) {
				fontInfo.fontStyle = token;
				if(hasMoreTokens) {
					token = tok.nextToken();
					hasMoreTokens = tok.hasMoreTokens();
				}
				else {
					token = null;
				}
			}
			if(token != null && this.isFontVariant(token)) {
				fontInfo.fontVariant = token;
				if(hasMoreTokens) {
					token = tok.nextToken();
					hasMoreTokens = tok.hasMoreTokens();
				}
				else {
					token = null;
				}
			}
			if(token != null && this.isFontWeight(token)) {
				fontInfo.fontWeight = token;
				if(hasMoreTokens) {
					token = tok.nextToken();
					hasMoreTokens = tok.hasMoreTokens();
				}
				else {
					token = null;
				}
			}
			if(token != null) {
				int slashIdx = token.indexOf('/');
				String fontSizeText = slashIdx == -1 ? token : token.substring(0, slashIdx);
				try {
					fontSizeText = fontSizeText.toLowerCase();
					if(fontSizeText.endsWith("pt") || fontSizeText.endsWith("px")) {
						fontSizeText = fontSizeText.substring(0, fontSizeText.length() - 2);
					}
					fontInfo.fontSize = Float.valueOf(fontSizeText);
				} catch(Exception err) {
					// ignore
				}
				
				// The rest is the font family
				if(hasMoreTokens) {
					StringBuffer fontFamilyBuf = new StringBuffer();
					while(tok.hasMoreTokens()) {	
						fontFamilyBuf.append(tok.nextToken());
						fontFamilyBuf.append(' ');
					}
					fontInfo.fontFamily = fontFamilyBuf.toString();
				}
			}	
		}
		return fontInfo;
	}
	
	private static class FontInfo {
		public String fontFamily;
		public String fontStyle;
		public String fontVariant;
		public String fontWeight;
		public Float fontSize;
	}
}
