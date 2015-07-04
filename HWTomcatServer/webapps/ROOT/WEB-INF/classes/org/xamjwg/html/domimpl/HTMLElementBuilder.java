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
 * Created on Oct 8, 2005
 */
package org.xamjwg.html.domimpl;

import org.w3c.dom.html2.*;

public abstract class HTMLElementBuilder {
	public final HTMLElement create(HTMLDocument document) {
		HTMLElementImpl element = this.build();
		element.setOwnerDocument(document);
		return element;
	}
	
	protected abstract HTMLElementImpl build();
	
	public static class Html extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLHtmlElementImpl();
		}
	}

	public static class Body extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLBodyElementImpl();
		}
	}

	public static class Span extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLSpanElementImpl();
		}
	}

	public static class Script extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLScriptElementImpl();
		}
	}

	public static class Img extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLImageElementImpl();
		}
	}

	public static class Style extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLStyleElementImpl();
		}
	}

	public static class Table extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLTableElementImpl();
		}
	}

	public static class Td extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLTableCellElementImpl("TD");
		}
	}

	public static class Th extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLTableCellElementImpl("TH");
		}
	}

	public static class Tr extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLTableRowElementImpl();
		}
	}
	public static class Link extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLLinkElementImpl("LINK");
		}
	}
	
	public static class Anchor extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLLinkElementImpl("ANCHOR");
		}
	}

	public static class A extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLLinkElementImpl("A");
		}
	}
	
	public static class Form extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLFormElementImpl("FORM");
		}
	}

	public static class Input extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLInputElementImpl("INPUT");
		}
	}

	public static class Textarea extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLTextAreaElementImpl("TEXTAREA");
		}
	}

	public static class Frameset extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLTextAreaElementImpl("FRAMESET");
		}
	}

	public static class Frame extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLTextAreaElementImpl("FRAME");
		}
	}

	public static class Ul extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLUListElementImpl("UL");
		}
	}

	public static class Ol extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLOListElementImpl("OL");
		}
	}

	public static class Li extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLLIElementImpl("LI");
		}
	}

	public static class Pre extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLPreElementImpl("PRE");
		}
	}

	public static class Div extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLDivElementImpl("DIV");
		}
	}

	public static class Hr extends HTMLElementBuilder {
		public HTMLElementImpl build() {
			return new HTMLDivElementImpl("HR");
		}
	}
}
