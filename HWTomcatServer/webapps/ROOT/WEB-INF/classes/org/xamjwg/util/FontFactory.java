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
 * Created on Apr 17, 2005
 */
package org.xamjwg.util;

import java.util.*;
import java.awt.*;
import java.awt.font.*;
//import org.apache.log4j.*;

/**
 * @author J. H. S.
 */
public class FontFactory {
	//private static final Logger logger = Logger.getLogger(FontFactory.class);
	private static FontFactory instance;
	private final Set fontFamilies = new HashSet();
	private final Map fontMap = new HashMap();
	
	/**
	 * 
	 */
	private FontFactory() {
		String[] ffns = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		synchronized(this) {
			for(int i = 0; i < ffns.length; i++) {
				this.fontFamilies.add(ffns[i]);
			}
		}
	}
	
	public static final FontFactory getInstance() {
		if(instance == null) {
			synchronized(FontFactory.class) {
				if(instance == null) {
					instance = new FontFactory();
				}
			}
		}
		return instance;
	}
	
	public Font getFont(String fontFamily, String fontStyle, String fontVariant, String fontWeight, Float fontSize) {
		FontKey key = new FontKey(fontFamily, fontStyle, fontVariant, fontWeight, fontSize);
		synchronized(this) {
			Font font = (Font) this.fontMap.get(key);
			if(font == null) {
				//if(logger.isDebugEnabled()) logger.debug("getFont(): New font: family=" + fontFamily + ",style=" + fontStyle + ",weight=" + fontWeight + ",size=" + fontSize); 
				font = this.createFont(key);
				this.fontMap.put(key, font);
			}
			return font;
		}
	}
	private Font createFont(FontKey key) {
		String fontNames = key.fontFamily;
		String fontFam = null;
		if(fontNames != null) {
			StringTokenizer tok = new StringTokenizer(fontNames, ", \r\n\t");
			while(tok.hasMoreTokens()) {
				fontFam = tok.nextToken().trim();
				if(this.fontFamilies.contains(fontFam)) {
					break;
				}
			}
		}
		Map attribs = new HashMap();
		if(fontFam != null) {
			attribs.put(TextAttribute.FAMILY, fontFam);
		}
		if("italic".equals(key.fontStyle)) {
			attribs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
		}
		if("bold".equals(key.fontWeight) || "bolder".equals(key.fontWeight)) {
			attribs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		}
		attribs.put(TextAttribute.SIZE, key.fontSize);
		return new Font(attribs);
	}	
	private static class FontKey {
		public final String fontFamily;
		public final String fontStyle; 
		public final String fontVariant; 
		public final String fontWeight; 
		public final Float fontSize;
		
		
		/**
		 * @param fontFamily
		 * @param fontStyle
		 * @param fontVariant
		 * @param fontWeight
		 * @param fontSize
		 */
		public FontKey(final String fontFamily, final String fontStyle,
				final String fontVariant, final String fontWeight,
				final Float fontSize) {
			super();
			this.fontFamily = fontFamily;
			this.fontStyle = fontStyle;
			this.fontVariant = fontVariant;
			this.fontWeight = fontWeight;
			this.fontSize = fontSize;
		}
		
		public boolean equals(Object other) {
			if(!(other instanceof FontKey)) {
				return false;
			}
			FontKey ors = (FontKey) other;
			return Objects.equals(this.fontStyle,ors.fontStyle) &&
				   Objects.equals(this.fontWeight,ors.fontWeight) &&
				   Objects.equals(this.fontFamily,ors.fontFamily) &&
				   Objects.equals(this.fontVariant,ors.fontVariant) &&
				   this.fontSize == ors.fontSize;
		}
		
		public int hashCode() {
			String ff = this.fontFamily;
			if(ff == null) {
				ff = "";
			}
			String fw = this.fontWeight;
			if(fw == null) {
				fw = "";
			}
			return ff.hashCode() ^ 
				   fw.hashCode() ^ 
				   this.fontSize.hashCode();
		}
	}
}