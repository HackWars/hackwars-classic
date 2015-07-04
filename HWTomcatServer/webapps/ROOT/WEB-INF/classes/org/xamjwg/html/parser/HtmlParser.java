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
 * Created on Aug 28, 2005
 */
package org.xamjwg.html.parser;

import java.io.*;
import java.util.*;
import org.w3c.dom.html2.*;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.mozilla.javascript.*;
import org.xamjwg.html.js.*;
import org.xamjwg.html.*;
import org.xamjwg.html.io.*;
import org.xamjwg.js.*;

//Context ctx = Context.enter();
//try {
//	Scriptable scope = ctx.initStandardObjects();
//	JavaClassWrapper windowCW = JavaClassWrapperFactory.getInstance().getClassWrapper(Window.class);
//	Function f = JavaObjectWrapper.getConstructor("Window", windowCW, scope);
//	ScriptableObject.defineProperty(scope, "Window", f, ScriptableObject.READONLY);
//	Window window = new Window();
//	window.setDocument(new Document());
//	Scriptable windowScope = (Scriptable) JavaScript.getJavascriptObject(window, scope);
//	windowScope.setParentScope(scope);
//	ctx.evaluateString(windowScope, src, "test.js", 0, null);
//} finally {
//	Context.exit();
//}

/**
 * The <code>HtmlParser</code> class is an HTML DOM parser.
 * This parser provides the functionality for
 * the standard DOM parser, {@link org.xamjwg.html.parser.DocumentBuilderImpl}.
 * This parser should only be used directly when a different DOM
 * implementation is preferred.
 */
public class HtmlParser {
	private final HTMLDocument document;
	private final ErrorHandler errorHandler;
	private final String publicId;
	private final String systemId;
	private final HtmlParserContext context;
	
	private static final Map ENTITIES = new HashMap();
	private static final Map ELEMENT_INFOS = new HashMap();

	/**
	 * A document <code>UserData</code> key used
	 * to map Javascript scope in the HTML document.
	 */
	public static final String SCOPE_KEY = "js.scope";
	
	static {
		Map entities = ENTITIES;
		entities.put("amp", new Character('&'));
		entities.put("lt", new Character('<'));
		entities.put("gt", new Character('>'));
		entities.put("quot", new Character('"'));
		entities.put("nbsp", new Character((char) 160));

		entities.put("lsquo", new Character('`'));
		entities.put("rsquo", new Character('´'));

		entities.put("frasl", new Character((char) 47));
		entities.put("ndash", new Character((char) 8211));
		entities.put("mdash", new Character((char) 8212));
		entities.put("iexcl", new Character((char) 161));
		entities.put("cent", new Character((char) 162));
		entities.put("pound", new Character((char) 163));
		entities.put("curren", new Character((char) 164));
		entities.put("yen", new Character((char) 165));
		entities.put("brvbar", new Character((char) 166));
		entities.put("brkbar", new Character((char) 166));
		entities.put("sect", new Character((char) 167));
		entities.put("uml", new Character((char) 168));
		entities.put("die", new Character((char) 168));
		entities.put("copy", new Character((char) 169));
		entities.put("ordf", new Character((char) 170));
		entities.put("laquo", new Character((char) 171));
		entities.put("not", new Character((char) 172));
		entities.put("shy", new Character((char) 173));
		entities.put("reg", new Character((char) 174));
		entities.put("macr", new Character((char) 175));
		entities.put("hibar", new Character((char) 175));
		entities.put("deg", new Character((char) 176));
		entities.put("plusmn", new Character((char) 177));
		entities.put("sup2", new Character((char) 178));
		entities.put("sup3", new Character((char) 179));
		entities.put("acute", new Character((char) 180));
		entities.put("micro", new Character((char) 181));
		entities.put("para", new Character((char) 182));
		entities.put("middot", new Character((char) 183));
		entities.put("cedil", new Character((char) 184));
		entities.put("sup1", new Character((char) 185));
		entities.put("ordm", new Character((char) 186));
		entities.put("raquo", new Character((char) 187));
		entities.put("frac14", new Character((char) 188));
		entities.put("frac12", new Character((char) 189));
		entities.put("frac34", new Character((char) 190));
		entities.put("iquest", new Character((char) 191));
		entities.put("Agrave", new Character((char) 192));
		entities.put("Aacute", new Character((char) 193));
		entities.put("Acirc", new Character((char) 194));
		entities.put("Atilde", new Character((char) 195));
		entities.put("Auml", new Character((char) 196));
		entities.put("Aring", new Character((char) 197));
		entities.put("AElig", new Character((char) 198));
		entities.put("Ccedil", new Character((char) 199));
		entities.put("Egrave", new Character((char) 200));
		entities.put("Eacute", new Character((char) 201));
		entities.put("Ecirc", new Character((char) 202));
		entities.put("Euml", new Character((char) 203));
		entities.put("Igrave", new Character((char) 204));
		entities.put("Iacute", new Character((char) 205));
		entities.put("Icirc", new Character((char) 206));
		entities.put("Iuml", new Character((char) 207));
		entities.put("ETH", new Character((char) 208));
		entities.put("Ntilde", new Character((char) 209));
		entities.put("Ograve", new Character((char) 210));
		entities.put("Oacute", new Character((char) 211));
		entities.put("Ocirc", new Character((char) 212));
		entities.put("Otilde", new Character((char) 213));
		entities.put("Ouml", new Character((char) 214));
		entities.put("times", new Character((char) 215));
		entities.put("Oslash", new Character((char) 216));
		entities.put("Ugrave", new Character((char) 217));
		entities.put("Uacute", new Character((char) 218));
		entities.put("Ucirc", new Character((char) 219));
		entities.put("Uuml", new Character((char) 220));
		entities.put("Yacute", new Character((char) 221));
		entities.put("THORN", new Character((char) 222));
		entities.put("szlig", new Character((char) 223));
		entities.put("agrave", new Character((char) 224));
		entities.put("aacute", new Character((char) 225));
		entities.put("acirc", new Character((char) 226));
		entities.put("atilde", new Character((char) 227));
		entities.put("auml", new Character((char) 228));
		entities.put("aring", new Character((char) 229));
		entities.put("aelig", new Character((char) 230));
		entities.put("ccedil", new Character((char) 231));
		entities.put("egrave", new Character((char) 232));
		entities.put("eacute", new Character((char) 233));
		entities.put("ecirc", new Character((char) 234));
		entities.put("euml", new Character((char) 235));
		entities.put("igrave", new Character((char) 236));
		entities.put("iacute", new Character((char) 237));
		entities.put("icirc", new Character((char) 238));
		entities.put("iuml", new Character((char) 239));
		entities.put("eth", new Character((char) 240));
		entities.put("ntilde", new Character((char) 241));
		entities.put("ograve", new Character((char) 242));
		entities.put("oacute", new Character((char) 243));
		entities.put("ocirc", new Character((char) 244));
		entities.put("otilde", new Character((char) 245));
		entities.put("ouml", new Character((char) 246));
		entities.put("divide", new Character((char) 247));
		entities.put("oslash", new Character((char) 248));
		entities.put("ugrave", new Character((char) 249));
		entities.put("uacute", new Character((char) 250));
		entities.put("ucirc", new Character((char) 251));
		entities.put("uuml", new Character((char) 252));
		entities.put("yacute", new Character((char) 253));
		entities.put("thorn", new Character((char) 254));
		entities.put("yuml", new Character((char) 255));

		//TODO: See http://elcursillo0.tripod.com/muestras/caracters.html
		//TODO: See http://www.koders.com/java/fid0B623B057D114F8A68B54C6060C2B1AEE2D04750.aspx
		
		Map elementInfos = ELEMENT_INFOS;
		ElementInfo optionalEndElement = new ElementInfo(true, ElementInfo.END_ELEMENT_OPTIONAL);
		ElementInfo forbiddenEndElement = new ElementInfo(false, ElementInfo.END_ELEMENT_FORBIDDEN);
		ElementInfo onlyText = new ElementInfo(false, ElementInfo.END_ELEMENT_REQUIRED);
		Set tableCellStopElements = new HashSet();
		tableCellStopElements.add("TH");
		tableCellStopElements.add("TD");
		tableCellStopElements.add("TR");
		ElementInfo tableCellElement = new ElementInfo(true, ElementInfo.END_ELEMENT_OPTIONAL, tableCellStopElements);
		Set headStopElements = new HashSet();
		headStopElements.add("BODY");
		headStopElements.add("DIV");
		headStopElements.add("SPAN");
		headStopElements.add("TABLE");
		ElementInfo headElement = new ElementInfo(true, ElementInfo.END_ELEMENT_OPTIONAL, headStopElements);
		
		elementInfos.put("SCRIPT", onlyText);
		elementInfos.put("STYLE", onlyText);
		elementInfos.put("TEXTAREA", onlyText);
		elementInfos.put("IMG", forbiddenEndElement);
		elementInfos.put("META", forbiddenEndElement);
		elementInfos.put("LINK", forbiddenEndElement);
		elementInfos.put("BASE", forbiddenEndElement);
		elementInfos.put("INPUT", forbiddenEndElement);
		elementInfos.put("FRAME", forbiddenEndElement);
		elementInfos.put("BR", forbiddenEndElement);
		elementInfos.put("HR", forbiddenEndElement);
		elementInfos.put("P", optionalEndElement);
		elementInfos.put("LI", optionalEndElement);
		elementInfos.put("DT", optionalEndElement);
		elementInfos.put("DD", optionalEndElement);
		elementInfos.put("TR", optionalEndElement);
		elementInfos.put("TH", tableCellElement);
		elementInfos.put("TD", tableCellElement);
		elementInfos.put("HEAD", headElement);
		//TODO: Keep adding tags here		
	}

	/**
	 * Constructs a <code>HtmlParser</code>.
	 * @param context An instance of {@link org.xamjwg.html.HtmlRendererContext},
	 * which may be an instance of {@link org.xamjwg.html.test.SimpleHtmlRendererContext}.
	 * @param document An instanceof of <code>HTMLDocument</code>.
	 * @param errorHandler The error handler. 
	 * @param publicId The public ID of the document.
	 * @param systemId The system ID of the document.
	 */
	public HtmlParser(HtmlParserContext context, HTMLDocument document, ErrorHandler errorHandler, String publicId, String systemId) {
		this.context = context;
		this.document = document;
		this.errorHandler = errorHandler;
		this.publicId = publicId;
		this.systemId = systemId;
	}
	
	/**
	 * Parses HTML from an input stream, assuming
	 * the character set is ISO-8859-1.
	 * @param in The input stream.
	 * @throws IOException Thrown when there are errors reading the stream.
	 * @throws SAXException Thrown when there are parse errors.
	 */
	public void parse(InputStream in) throws IOException,SAXException,UnsupportedEncodingException {
		this.parse(in, "ISO-8859-1");
	}
	
/**
 * Parses HTML from an input stream, using the given character set. 
 * @param in The input stream.
 * @param charset The character set.
 * @throws IOException Thrown when there's an error reading from the stream.
 * @throws SAXException Thrown when there is a parser error.
 * @throws UnsupportedEncodingException Thrown if the character set is not supported.
 */
	public void parse(InputStream in, String charset) throws IOException,SAXException,UnsupportedEncodingException {
		WritableLineReader reader = new WritableLineReader(in, charset);
		this.parse(reader);
	}

	/**
	 * Parses HTML given by a <code>WritableLineReader</code>. 
	 * @param reader An instance of <code>WritableLineReader</code>.
	 * @throws IOException Thrown if there are errors reading the input stream.
	 * @throws SAXException Thrown if there are parse errors.
	 */
	public void parse(WritableLineReader reader) throws IOException, SAXException {
		Document doc = this.document;
		Context ctx = Context.enter();
		try {
			ctx.setOptimizationLevel(-1);
			Scriptable scope = ctx.initStandardObjects();
			Window window = new Window(this.context, doc);
			Scriptable windowScope = (Scriptable) JavaScript.getInstance().getJavascriptObject(window, scope);
			windowScope.setParentScope(scope);
			doc.setUserData(SCOPE_KEY, windowScope, null);
			
			// Special classes
			
			JavaClassWrapper imageCW = JavaClassWrapperFactory.getInstance().getClassWrapper(Image.class);
			Function imageC = JavaObjectWrapper.getConstructor("Image", imageCW, windowScope);
			ScriptableObject.defineProperty(windowScope, "Image", imageC, ScriptableObject.READONLY);

			try {
				while(this.parseToken(doc, reader, null, null) != TOKEN_EOD) {;}
			} catch(StopException se) {
				throw new SAXException("Unexpected flow exception", se);
			}
		} finally {
			Context.exit();
		}
	}
	
	
	private static final int TOKEN_EOD = 0;
	private static final int TOKEN_COMMENT = 1;
	private static final int TOKEN_TEXT = 2;
	private static final int TOKEN_BEGIN_ELEMENT = 3;
	private static final int TOKEN_END_ELEMENT = 4;
	private static final int TOKEN_FULL_ELEMENT = 5;
	private static final int TOKEN_BAD = 6;
	
	private String lastTag = null;
	private boolean justReadTagBegin = false;
	private boolean justReadTagEnd = false;
	
	/**
	 * Only set when readAttribute returns false. 
	 */
	private boolean justReadEmptyElement = false;
	
	private final int parseToken(Node parent, WritableLineReader reader, String stopAtTagUC, Set stopTags) throws IOException, StopException, SAXException {
		Document doc = this.document;
		String text = this.readUpToTagBegin(reader);
		if(text == null) {
			return TOKEN_EOD;
		}
		if(!"".equals(text)) {
			int textLine = reader.getLineNumber();
			String decText = this.entityDecode(text, textLine);
			Node textNode = doc.createTextNode(decText);
			parent.appendChild(textNode);
		}
		if(this.justReadTagBegin) {
			String tag = this.readTag(reader);
			if(tag == null) {
				return TOKEN_EOD;
			}
			try {
				if(tag.startsWith("!")) {
					if("!--".equals(tag)) {
						int commentLine = reader.getLineNumber();
						String comment = this.passEndOfComment(reader);
						String decText = this.entityDecode(comment, commentLine);
						parent.appendChild(doc.createComment(decText));
						return TOKEN_COMMENT;
					}
					else {
						//TODO: DOCTYPE node
						this.passEndOfTag(reader);
						return TOKEN_BAD;
					}
				}
				else if(tag.startsWith("/")) {
					tag = tag.substring(1);
					this.passEndOfTag(reader);
					return TOKEN_END_ELEMENT;
				}
				else {
					Element element = doc.createElement(tag);
					if(!this.justReadTagEnd) {
						while(this.readAttribute(reader, element)) {;}
					}
					String normalTag = tag.toUpperCase();
					if(stopAtTagUC != null && stopAtTagUC.equals(normalTag)) {
						// Throw before appending to parent
						// After attributes are set
						throw new StopException(element);
					}
					else if(stopTags != null && stopTags.contains(normalTag)) {
						throw new StopException(element);
					}
					try {
						if(!this.justReadEmptyElement) {
							ElementInfo einfo = (ElementInfo) ELEMENT_INFOS.get(normalTag);
							int endTagType = einfo == null ? ElementInfo.END_ELEMENT_REQUIRED : einfo.endElementType;
							if(endTagType != ElementInfo.END_ELEMENT_FORBIDDEN) {
								boolean childrenOk = einfo == null ? true : einfo.childElementOk;
								String newStopTag = null; 
								Set newStopSet = einfo == null ? null : einfo.stopTags;
								if(newStopSet == null) {
									if(endTagType == ElementInfo.END_ELEMENT_OPTIONAL) {
										newStopTag = normalTag;	
									}
								}
								for(;;) {
									try {
										int token = childrenOk ? this.parseToken(element, reader, newStopTag, newStopSet) : this.parseForEndTag(element, reader, tag);
										if(token == TOKEN_END_ELEMENT) {
											String lastTag = this.lastTag;
											if(tag.equalsIgnoreCase(lastTag)) {
												return TOKEN_FULL_ELEMENT;
											}
											else if(newStopTag != null || newStopSet != null) {
												tag = lastTag;
												return TOKEN_END_ELEMENT;
											}
										}
										else if(token == TOKEN_EOD) {
											return TOKEN_EOD;
										}
									} catch(StopException se) {
										parent.appendChild(element);
										Element newElement = se.getElement();
										tag = newElement.getTagName();
										normalTag = tag.toUpperCase();
										if(stopAtTagUC != null && stopAtTagUC.equals(normalTag)) {
											element = null;
											throw se;
										}
										else if(stopTags != null && stopTags.contains(normalTag)) {
											element = null;                                                             
											throw se;
										}
										einfo = (ElementInfo) ELEMENT_INFOS.get(normalTag);
										endTagType = einfo == null ? ElementInfo.END_ELEMENT_REQUIRED : einfo.endElementType;
										childrenOk = einfo == null ? true : einfo.childElementOk;
										newStopTag = null; 
										newStopSet = einfo == null ? null : einfo.stopTags;
										if(newStopSet == null) {
											if(endTagType == ElementInfo.END_ELEMENT_OPTIONAL) {
												newStopTag = normalTag;	
											}
										}
										element = newElement;
										if(this.justReadEmptyElement) {
											return TOKEN_BEGIN_ELEMENT;
										}
									}
								}
							}
						}
					} finally {
						// Append child at the end so that it's complete for addNotify
						if(element != null) {
							parent.appendChild(element);
						}
					}
					return TOKEN_BEGIN_ELEMENT;
				}
			} finally  {
				this.lastTag = tag;				
			}
		}
		else {
			this.lastTag = null;
			return TOKEN_TEXT;
		}
	}

//	private final int parseForEndTag(Node parent, WritableLineReader reader, String tagName) throws IOException, SAXException{
//		Document doc = this.document;
//		String text = this.readUpToTagBegin(reader);
//		if(text == null) {
//			return TOKEN_EOD;
//		}
//		if(!"".equals(text)) {
//			int textLine = reader.getLineNumber();
//			String decText = this.entityDecode(text, textLine);
//			Node textNode = doc.createTextNode(decText);
//			parent.appendChild(textNode);
//		}
//		if(this.justReadTagBegin) {
//			String tag = this.readTag(reader);
//			if(tag.startsWith("/")) {
//				tag = tag.substring(1);
//				if(tag.equalsIgnoreCase(tagName)) {
//					this.passEndOfTag(reader);
//					this.lastTag = tag;
//					return TOKEN_END_ELEMENT;
//				}
//			}
//			int textLine = reader.getLineNumber();
//			text = "<" + tag;
//			if(this.justReadTagEnd) {
//				text += ">";
//			}
//			String decText = this.entityDecode(text, textLine);
//			Node textNode = doc.createTextNode(decText);
//			parent.appendChild(textNode);
//			return TOKEN_TEXT;					
//		}
//		else {
//			this.lastTag = null;
//			return TOKEN_TEXT;
//		}
//	}

	/**
	 * Reads text until the beginning of the next tag.
	 * Leaves the reader offset past the opening angle bracket.
	 * Returns null only on EOF.
	 */
	private final String readUpToTagBegin(WritableLineReader reader) throws IOException, SAXException{
		StringBuffer sb = null;
		int intCh;
		while((intCh = reader.read()) != -1) {
			char ch = (char) intCh;
			if(ch == '<') {
				this.justReadTagBegin = true;
				this.justReadTagEnd = false;
				this.justReadEmptyElement = false;
				return sb == null ? "" : sb.toString();
			}
			if(sb == null) {
				sb = new StringBuffer();
			}
			sb.append(ch);
		}
		this.justReadTagBegin = false;
		this.justReadTagEnd = false;
		this.justReadEmptyElement = false;
		return sb == null ? null : sb.toString();
	}

	private final int parseForEndTag(Node parent, WritableLineReader reader, String tagName) throws IOException {		
	    Document doc = this.document;
	    int intCh;
	    StringBuffer sb = new StringBuffer();
	    OUTER:
	    while((intCh = reader.read()) != -1) {
	    	char ch = (char) intCh;
	    	if(ch == '<') {
	    		intCh = reader.read();
	    		if(intCh != -1) {
	    			ch = (char) intCh;
	    			if(ch == '/') {
	    				StringBuffer tempBuffer = new StringBuffer();
	    				INNER:
	    				while((intCh = reader.read()) != -1) {
	    					ch = (char) intCh;
	    					if(ch == '>') {
	    						String thisTag = tempBuffer.toString().trim();
	    						if(thisTag.equalsIgnoreCase(tagName)) {
	    							this.justReadTagBegin = false;
	    							this.justReadTagEnd = true;
	    							this.justReadEmptyElement = false;
	    							this.lastTag = thisTag;
	    							String text = sb.toString();
	    							Node textNode = doc.createTextNode(text);
	    							parent.appendChild(textNode);
	    							return HtmlParser.TOKEN_END_ELEMENT;
	    						}
	    						else {
	    							break INNER;
	    						}
	    					}
	    					else {
	    						tempBuffer.append(ch);
	    					}
	    				}
	    				sb.append("</");
	    				sb.append(tempBuffer);
	    			}
	    			else {
	    				sb.append('<');
	    			}
	    		}
	    	}
	    	sb.append(ch);
	    }
	    this.justReadTagBegin = false;
	    this.justReadTagEnd = false;
	    this.justReadEmptyElement = false;
	    String text = sb.toString();
	    Node textNode = doc.createTextNode(text);
	    parent.appendChild(textNode);
	    return HtmlParser.TOKEN_EOD;
	}
	
	    /**
	 * The reader offset should be 
	 * @param reader
	 * @return
	 */
	private final String readTag(WritableLineReader reader) throws IOException {
		StringBuffer sb = new StringBuffer();
		int chInt;
		chInt = reader.read();
		if(chInt != -1) {
			boolean cont = true;
			char ch = (char) chInt;
			if(ch == '!') {
				sb.append('!');
				chInt = reader.read();
				if(chInt != -1) {
					ch = (char) chInt;
					if(ch == '-') {
						sb.append('-');
						chInt = reader.read();
						if(chInt != -1) {
							ch = (char) chInt;
							if(ch == '-') {
								sb.append('-');
								cont = false;
							}
						}
						else {
							cont = false;
						}
					}
				}
				else {
					cont = false;
				}
			}
			else if(ch == '/') {
				sb.append(ch);
				chInt = reader.read();
				if(chInt != -1) {
					ch = (char) chInt;
				}
				else {
					cont = false;
				}
			}
			if(cont) {
				boolean lastCharSlash = false;
				for(;;) {
					if(Character.isWhitespace(ch)) {
						break;
					}
					else if(ch == '>') {
						this.justReadTagEnd = true;
						this.justReadTagBegin = false;
						this.justReadEmptyElement = lastCharSlash;
						String tag = sb.toString();
						return tag;
					}
					else if (ch == '/') {
						lastCharSlash = true;
					}
					else {
						if(lastCharSlash) {
							sb.append('/');
						}
						lastCharSlash = false;
						sb.append(ch);
					}
					chInt = reader.read();
					if(chInt == -1) {
						break;
					}
					ch = (char) chInt;
				} 
			}
		}
		if(sb.length() > 0) {
			this.justReadTagEnd = false;
			this.justReadTagBegin = false;
			this.justReadEmptyElement = false;
		}
		String tag = sb.toString();
		return tag;
	}

	private final String passEndOfComment(WritableLineReader reader) throws IOException {
		if(this.justReadTagEnd) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		OUTER:
		for(;;) {
			int chInt = reader.read();
			if(chInt == -1) {
				break OUTER;
			}
			char ch = (char) chInt;
			if(ch == '-') {
				chInt = reader.read();
				if(chInt == -1) {
					sb.append(ch);
					break OUTER;
				}
				ch = (char) chInt;
				if(ch == '-') {
					StringBuffer extra = null;
					INNER:
					for(;;) {
						chInt = reader.read();
						if(chInt == -1) {
							if(extra != null) {
								sb.append(extra.toString());
							}
							break OUTER; 
						}
						ch = (char) chInt;
						if(ch == '>') {
							this.justReadTagBegin = false;
							this.justReadTagEnd = true;
							return sb.toString();
						}
						else if(Character.isWhitespace(ch)) {
							if(extra == null) {
								extra = new StringBuffer();
								extra.append("--");
							}
							extra.append(ch);
						}
						else {
							if(extra != null) {
								sb.append(extra.toString());
							}
							sb.append(ch);
							break INNER;
						}
					}
				}
				else {
					sb.append('-');
					sb.append(ch);
				}
			}
			else {
				sb.append(ch);
			}
		}
		if(sb.length() > 0) {
			this.justReadTagBegin = false;
			this.justReadTagEnd = false;
		}
		return sb.toString(); 
	}

	private final void passEndOfTag(Reader reader) throws IOException {
		if(this.justReadTagEnd) {
			return;
		}
		boolean readSomething = false; 
		for(;;) {
			int chInt = reader.read();
			if(chInt == -1) {
				break;
			}
			readSomething = true;
			char ch = (char) chInt;
			if(ch == '>') {
				this.justReadTagEnd = true;
				this.justReadTagBegin = false;
				return;
			}
		}
		if(readSomething) {
			this.justReadTagBegin = false;
			this.justReadTagEnd = false;
		}
	}
	
	private final boolean readAttribute(WritableLineReader reader, Element element) throws IOException, SAXException {
		if(this.justReadTagEnd) {
			return false;
		}

		// Read attribute name up to equals 
		
		StringBuffer attributeName = null;
		boolean blankFound = false;
		boolean lastCharSlash = false;
		for(;;) {
			int chInt = reader.read();
			if(chInt == -1) {
				if(attributeName != null) {
					String attributeNameStr = attributeName.toString();
					element.setAttribute(attributeNameStr, attributeNameStr);
					attributeName.setLength(0);
				}
				this.justReadTagBegin = false;
				this.justReadTagEnd = false;
				this.justReadEmptyElement = false;
				return false;
			}
			char ch = (char) chInt;
			if(ch == '=') {
				lastCharSlash = false;
				blankFound = false;
				break;
			}
			else if(ch == '>') {
				this.justReadTagBegin = false;
				this.justReadTagEnd = true;
				this.justReadEmptyElement = lastCharSlash;
				return false;
			}
			else if(ch == '/') {
                blankFound = true;
                lastCharSlash = true;
			}
			else if(Character.isWhitespace(ch)) {
				lastCharSlash = false;
				blankFound = true;
			}
			else {
				lastCharSlash = false;
				if(blankFound) {
					blankFound = false;
					if(attributeName != null) {
						String attributeNameStr = attributeName.toString();
						element.setAttribute(attributeNameStr, attributeNameStr);
						attributeName.setLength(0);
					}
				}
				if(attributeName == null) {
					attributeName = new StringBuffer(6);
				}
				attributeName.append(ch);
			}
		}
		// Read blanks up to open quote or first non-blank.
		StringBuffer attributeValue = null;
		int openQuote = -1;
		for(;;) {
			int chInt = reader.read();
			if(chInt == -1) {
				break;
			}
			char ch = (char) chInt;
			if(ch == '>') {
				if(attributeName != null) {
					String attributeNameStr = attributeName.toString();
					element.setAttribute(attributeNameStr, attributeNameStr);
				}
				this.justReadTagBegin = false;
				this.justReadTagEnd = true;
				this.justReadEmptyElement = lastCharSlash;
				return false;
			}
			else if(ch == '/') {
                lastCharSlash = true;
			}
			else if(Character.isWhitespace(ch)) {
				lastCharSlash = false;
			}
			else {
				lastCharSlash = false;
				if(ch == '"') {
					openQuote = '"';
				}
				else if(ch == '\'') {
					openQuote = '\'';
				}
				else {
					openQuote = -1;
					if(attributeValue == null) {
						attributeValue = new StringBuffer(6);
					}
					attributeValue.append(ch);
				}
				break;
			}
		}

		// Read attribute value
		
		for(;;) {
			int chInt = reader.read();
			if(chInt == -1) {
				break;
			}
			char ch = (char) chInt;
			if(openQuote != -1 && ch == openQuote) {
				lastCharSlash = false;
				if(attributeName != null) {
					String attributeNameStr = attributeName.toString();
					String attributeValueStr = attributeValue == null ? "" : attributeValue.toString();
					attributeValueStr = this.entityDecode(attributeValueStr, reader.getLineNumber());
					element.setAttribute(attributeNameStr, attributeValueStr);
				}
				this.justReadTagBegin = false;
				this.justReadTagEnd = false;
				return true;
			}
			else if(openQuote == -1 && ch == '>') {
				if(attributeName != null) {
					String attributeNameStr = attributeName.toString();
					String attributeValueStr = attributeValue == null ? "" : attributeValue.toString();
					attributeValueStr = this.entityDecode(attributeValueStr, reader.getLineNumber());
					element.setAttribute(attributeNameStr, attributeValueStr);
				}
				this.justReadTagBegin = false;
				this.justReadTagEnd = true;
				this.justReadEmptyElement = lastCharSlash;
				return false;
			}
			else if(openQuote == -1 && Character.isWhitespace(ch)) {
				lastCharSlash = false;
				if(attributeName != null) {
					String attributeNameStr = attributeName.toString();
					String attributeValueStr = attributeValue == null ? "" : attributeValue.toString();
					attributeValueStr = this.entityDecode(attributeValueStr, reader.getLineNumber());
					element.setAttribute(attributeNameStr, attributeValueStr);
				}
				this.justReadTagBegin = false;
				this.justReadTagEnd = false;
				return true;				
			}
			else {
				if(attributeValue == null) {
					attributeValue = new StringBuffer(6);
				}
				if(lastCharSlash) {
					attributeValue.append('/');
				}
				lastCharSlash = false;
				attributeValue.append(ch);
			}
		}
		this.justReadTagBegin = false;
		this.justReadTagEnd = false;
		if(attributeName != null) {
			String attributeNameStr = attributeName.toString();
			String attributeValueStr = attributeValue == null ? "" : attributeValue.toString();
			attributeValueStr = this.entityDecode(attributeValueStr, reader.getLineNumber());
			element.setAttribute(attributeNameStr, attributeValueStr);
		}
		return false;
	}
		
	private final String entityDecode(String rawText, int lineNumber) throws org.xml.sax.SAXException {
		int startIdx = 0;
		StringBuffer sb = null;
		for(;;) {
			int ampIdx = rawText.indexOf('&', startIdx);
			if(ampIdx == -1) {
				if(sb == null) {
					return rawText;
				}
				else {
					sb.append(rawText.substring(startIdx));
					return sb.toString();
				}
			}
			if(sb == null) {
				sb = new StringBuffer();
			}
			sb.append(rawText.substring(startIdx, ampIdx));
			int colonIdx = rawText.indexOf(';', ampIdx);
			if(colonIdx == -1) {
				sb.append('&');
				startIdx = ampIdx+1;
				continue;
			}
			String spec = rawText.substring(ampIdx+1, colonIdx);
			if(spec.startsWith("#")) {
				String number = spec.substring(1).toLowerCase();
				int decimal;
				try {
					if(number.startsWith("x")) {
						decimal = Integer.parseInt(number.substring(1), 16);
					}
					else {
						decimal = Integer.parseInt(number);
					}
				} catch(NumberFormatException nfe) {
					this.errorHandler.error(new SAXParseException("Bad entity: " + spec, this.getLocator(lineNumber, 0)));
					decimal = 0;
				}
				sb.append((char) decimal);
			}
			else {
				int chInt = this.getEntityChar(spec);
				if(chInt == -1) {
					sb.append('&');
					sb.append(spec);
					sb.append(';');
				}
				else {
					sb.append((char) chInt);
				}
			}
			startIdx = colonIdx+1;
		}
	}
		
	private final Locator getLocator(int lineNumber, int columnNumber) {
		return new LocatorImpl(this.publicId, this.systemId, lineNumber, columnNumber);
	}
	
	private final int getEntityChar(String spec) {
		//TODO: Declared entities
		Character c = (Character) ENTITIES.get(spec);
		if(c == null) {
			String specTL = spec.toLowerCase();
			c = (Character) ENTITIES.get(specTL);
			if(c == null) {
				return -1;
			}
		}
		return (int) c.charValue();
	}
}
