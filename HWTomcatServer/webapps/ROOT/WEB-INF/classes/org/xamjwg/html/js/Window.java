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
 * Created on Nov 12, 2005
 */
package org.xamjwg.html.js;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.w3c.dom.Document;
import org.xamjwg.html.*;
import org.xamjwg.html.domimpl.HTMLDocumentImpl;
import org.xamjwg.html.parser.HtmlParser;
import org.xamjwg.util.ID;

public class Window {
	private final Document document;
	private final HtmlParserContext context;
	private final Navigator navigator;
	private volatile Timer timer;
	private volatile Map taskMap; 
	
	public Window(HtmlParserContext context, Document document) {
		this.context = context;
		this.document = document;
		this.navigator = new Navigator(context, document);
	}
	
	private Timer getTimer() {
		synchronized(this) {
			if(this.timer == null) {
				this.timer = new Timer();
			}
			return this.timer;
		}
	}
	
	private Map getTaskMap() {
		synchronized(this) {
			if(this.taskMap == null) {
				this.taskMap = new HashMap();
			}
			return this.taskMap;
		}		
	}
	
	private HtmlRendererContext getRendererContext() {
		Object doc = this.document;
		if(doc instanceof HTMLDocumentImpl) {
			HTMLDocumentImpl di = (HTMLDocumentImpl) doc;
			return di.getHtmlRendererContext();
		}
		else {
			return null;
		}
	}
	
	public void alert(String message) {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			rcontext.alert(message);
		}
	}
	
	public void back() {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			rcontext.back();
		}
	}
	
	public void blur() {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			rcontext.blur();
		}
	}
	
	public void clearTimeout(int timeoutId) {
		Integer key = new Integer(timeoutId);
		TimerTask task = (TimerTask) this.getTaskMap().get(key);
		if(task != null) {
			task.cancel();
			this.getTaskMap().remove(key);
		}
	}
	
	public void close() {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			rcontext.close();
		}
	}
	
	public boolean confirm(String message) {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			return rcontext.confirm(message);
		}
		else {
			return false;
		}
	}
	
	public Object eval(String javascript) {
		Context ctx = (Context) Context.enter();
		try {
			Document doc = this.document;
			Scriptable scope;
			if(doc == null) {
				scope = ctx.initStandardObjects();
			}
			else {
				scope = (Scriptable) doc.getUserData(HtmlParser.SCOPE_KEY);
			}
			if(ctx == null || scope == null) {
				throw new IllegalStateException("Scriptable (scope) instance was expected to be keyed as UserData to document using " + HtmlParser.SCOPE_KEY);
			}
			String scriptURI = doc == null ? "" : doc.getDocumentURI();
			return ctx.evaluateString(scope, javascript, scriptURI, 1, null);
		} finally {
			Context.exit();
		}
	}
	
	public void focus() {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			rcontext.focus();
		}
	}
	
	public Window open(String url, String windowName, String windowFeatures, boolean replace) {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			WindowInfo wi = rcontext.open(url, windowName, windowFeatures, replace);
			return new Window(wi.context, wi.document);
		}
		else {
			return null;
		}
	}

	public Window open(String url, String windowName) {
		return this.open(url, windowName, "", false);
	}

	public Window open(String url, String windowName, String windowFeatures) {
		return this.open(url, windowName, windowFeatures, false);
	}

	public String prompt(String message) {
		return this.prompt(message, "");
	}
	
	public String prompt(String message, int inputDefault) {
		return this.prompt(message, String.valueOf(inputDefault));
	}

	public String prompt(String message, String inputDefault) {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			return rcontext.prompt(message, inputDefault);
		}
		else {
			return null;
		}
	}

	public void scroll(int x, int y) {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			rcontext.scroll(x, y);
		}
	}
	
	public int setTimeout(final String expr, double millis) {
		final int timeID = ID.generateInt();
		TimerTask task = new TimerTask() {
			public void run() {
				try {
					eval(expr);
				} catch(Exception err) {
					context.error("setTimeout()", err);
				} finally {
					getTaskMap().remove(new Integer(timeID));
				}
			}
		};
		this.getTaskMap().put(new Integer(timeID), task);
		this.getTimer().schedule(task, (long) Math.round(millis));
		return timeID;
	}
	
	public boolean isClosed() {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			return rcontext.isClosed();
		}
		else {
			return false;
		}
	}
	
	public String getDefaultStatus() {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			return rcontext.getDefaultStatus();
		}
		else {
			return null;
		}
	}
	
	public FilteredObjectList getFrames() {
		//TODO
		return null;
	}
	
	/**
	 * Gets the number of frames.
	 */
	public int getLength() {
		//TODO: Number
		return 0;
	}
	
	public String getName() {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			return rcontext.getName();
		}
		else {
			return null;
		}
	}
	
	public Window getParent() {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			WindowInfo wi = rcontext.getParent();
			return new Window(wi.context, wi.document);
		}
		else {
			return null;
		}
	}
	
	public Window getOpener() {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			WindowInfo wi = rcontext.getOpener();
			return new Window(wi.context, wi.document);
		}
		else {
			return null;
		}
	}
	
	public void setOpener(Window opener) {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			rcontext.setOpener(opener.getRendererContext());
		}
	}
	
	public Window getSelf() {
		return this;
	}
	
	public String getStatus() {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			return rcontext.getStatus();
		}
		else {
			return null;
		}
	}
	
	public void setStatus(String message) {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			rcontext.setStatus(message);
		}
	}
		
	public Window getTop() {
		HtmlRendererContext rcontext = this.getRendererContext();
		if(rcontext != null) {
			WindowInfo wi = rcontext.getTop();
			return new Window(wi.context, wi.document);
		}
		else {
			return null;
		}
	}
	
	public Window getWindow() {
		return this;
	}
	
	public Document getDocument() {
		return this.document;
	}
	
	public Navigator getNavigator() {
		return this.navigator;
	}
}

