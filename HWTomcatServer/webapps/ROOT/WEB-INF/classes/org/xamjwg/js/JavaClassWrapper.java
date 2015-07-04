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
package org.xamjwg.js;

import java.util.*;
import java.lang.reflect.*;
import org.mozilla.javascript.*;

public class JavaClassWrapper {
	private final Class javaClass;
	private final Map functions = new HashMap();
	private final Map properties = new HashMap();
	private PropertyInfo nameIndexer;
	private PropertyInfo integerIndexer;
	
	
	public JavaClassWrapper(Class class1) {
		super();
		this.javaClass = class1;
		this.scanMethods();
	}

	public Object newInstance() throws InstantiationException, IllegalAccessException {
		return this.javaClass.newInstance();
	}
	
	public String getClassName() {
		String className = this.javaClass.getName();
		int lastDotIdx = className.lastIndexOf('.');
		return lastDotIdx == -1 ? className : className.substring(lastDotIdx+1);
	}
	
	public Function getFunction(String name) {
		return (Function) this.functions.get(name);
	}
	
	public PropertyInfo getProperty(String name) {
		return (PropertyInfo) this.properties.get(name);
	}
	
	private void scanMethods() {
		Method[] methods = this.javaClass.getMethods();
		int len = methods.length;
		for(int i = 0; i < len; i++) {
			Method method = methods[i];
			String name = method.getName();
			if(isPropertyMethod(name, method)) {
				this.ensurePropertyKnown(name, method);
			}
			if(isNameIndexer(name, method)) {
				this.updateNameIndexer(name, method);
			}
			else if(this.isIntegerIndexer(name, method)) {
				this.updateIntegerIndexer(name, method);
			}
			else {
				Function f = new JavaFunctionObject(name, method);
				this.functions.put(name, f);
			}
		}
	}

	private boolean isNameIndexer(String name, Method method) {
		return ("namedItem".equals(name) && method.getParameterTypes().length == 1)  || 
			("setNamedItem".equals(name) && method.getParameterTypes().length == 2);
	}
	
	private boolean isIntegerIndexer(String name, Method method) {
		return ("item".equals(name) && method.getParameterTypes().length == 1)  || 
			("setItem".equals(name) && method.getParameterTypes().length == 2);
	}
 	
	private void updateNameIndexer(String methodName, Method method) {
		boolean getter = true;
		if(methodName.startsWith("set")) {
			getter = false;
		}
		PropertyInfo indexer = this.nameIndexer;
		if(indexer == null) {
			indexer = new PropertyInfo("$item");
			this.nameIndexer = indexer;
		}
		if(getter) {
			indexer.setGetter(method);
		}
		else {
			indexer.setSetter(method);
		}
	}
	
	private void updateIntegerIndexer(String methodName, Method method) {
		boolean getter;
		if(methodName.startsWith("get")) {
		    getter = true;
		}
		else if(methodName.startsWith("set")) {
			getter = false;
		}
		else {
			throw new IllegalArgumentException("methodName=" + methodName);
		}
		PropertyInfo indexer = this.integerIndexer;
		if(indexer == null) {
			indexer = new PropertyInfo("$item");
			this.integerIndexer = indexer;
		}
		if(getter) {
			indexer.setGetter(method);
		}
		else {
			indexer.setSetter(method);
		}
	}
	
	public PropertyInfo getIntegerIndexer() {
		return this.integerIndexer;
	}
	
	public PropertyInfo getNameIndexer() {
		return this.nameIndexer;
	}

	private boolean isPropertyMethod(String name, Method method) {
		if(name.startsWith("get") || name.startsWith("is")) {
			return method.getParameterTypes().length == 0;
		}
		else if(name.startsWith("set")) {
			return method.getParameterTypes().length == 1;
		}
		else {
			return false;
		}
	}

	private String uncapitalize(String text) {
		try {
			return Character.toLowerCase(text.charAt(0)) + text.substring(1);
		} catch(IndexOutOfBoundsException iob) {
			return text;
		}
	}
	
	private void ensurePropertyKnown(String methodName, Method method) {
		String capPropertyName;
		String propertyName;
		boolean getter = false;
		if(methodName.startsWith("get")) {
			capPropertyName = methodName.substring(3);
			propertyName = uncapitalize(capPropertyName);
		    getter = true;
		}
		else if(methodName.startsWith("set")) {
			capPropertyName = methodName.substring(3);
			propertyName = uncapitalize(capPropertyName);
		}
		else if(methodName.startsWith("is")) {
			capPropertyName = methodName.substring(2);
			propertyName = uncapitalize(capPropertyName);
			getter = true;
		}
		else {
			throw new IllegalArgumentException("methodName=" + methodName);
		}
		PropertyInfo pinfo = (PropertyInfo) this.properties.get(propertyName);
		if(pinfo == null) {
			pinfo = new PropertyInfo(propertyName);
			this.properties.put(propertyName, pinfo);
		}
		if(getter) {
			pinfo.setGetter(method);
		}
		else {
			pinfo.setSetter(method);
		}
	}
	
}
