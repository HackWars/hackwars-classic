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

import org.mozilla.javascript.*;
import java.lang.reflect.*;

public class JavaObjectWrapper extends ScriptableObject {
	private final Object delegate;
	private final JavaClassWrapper classWrapper;
	
	public JavaObjectWrapper(JavaClassWrapper classWrapper) throws InstantiationException, IllegalAccessException {
		this.classWrapper = classWrapper;
		this.delegate = this.classWrapper.newInstance();
	}

	public JavaObjectWrapper(JavaClassWrapper classWrapper, Object delegate) {
		this.classWrapper = classWrapper;
		this.delegate = delegate;
	}
	
	public Object getJavaObject() {
		return this.delegate;
	}
	
	public String getClassName() {
		return this.classWrapper.getClassName();
	}

	public Object get(int index, Scriptable start) {
		PropertyInfo pinfo = this.classWrapper.getIntegerIndexer();
		if(pinfo == null) {
			return super.get(index, start);
		}
		else {
			try {
				Method getter = pinfo.getGetter();
				if(getter == null) {
					throw new EvaluatorException("Indexer is write-only");
				}
				Object raw = getter.invoke(this.getJavaObject(), new Object[] { new Integer(index) });
				return JavaScript.getInstance().getJavascriptObject(raw, this.getParentScope());
			} catch(Exception err) {
				throw new WrappedException(err);
			}
		}
	}

	public Object get(String name, Scriptable start) {
		PropertyInfo pinfo = this.classWrapper.getProperty(name);
		if(pinfo != null) {
			Method getter = pinfo.getGetter();
			if(getter == null) {
				throw new EvaluatorException("Property '" + name + "' is not readable");
			}
			try {
				Object val = getter.invoke(this.getJavaObject(), (Object[]) null);
				return JavaScript.getInstance().getJavascriptObject(val, start.getParentScope());
			} catch(Exception err) {
				throw new WrappedException(err);
			}
		}
		else {
			Function f = this.classWrapper.getFunction(name);
			if(f != null) {
				return f;
			}
			else {
				PropertyInfo ni = this.classWrapper.getNameIndexer();
				if(ni != null) {
					Method getter = ni.getGetter();
					if(getter != null) {
						try {
							Object val = getter.invoke(this.getJavaObject(), new Object[] { name });
							return JavaScript.getInstance().getJavascriptObject(val, start.getParentScope());
						} catch(Exception err) {
							throw new WrappedException(err);
						}
					}
					else {
						return super.get(name, start);
					}
				}
				Object retValue = super.get(name, start);
				return retValue;
			}
		}
	}

	public void put(int index, Scriptable start, Object value) {
		PropertyInfo pinfo = this.classWrapper.getIntegerIndexer();
		if(pinfo == null) {
			super.put(index, start, value);
		}
		else {
			try {
				Method setter = pinfo.getSetter();
				if(setter == null) {
					throw new EvaluatorException("Indexer is read-only");
				}
				Object actualValue;
				if(value instanceof JavaObjectWrapper) {
					actualValue = ((JavaObjectWrapper) value).getJavaObject();
				}
				else {
					actualValue = value;
				}
				setter.invoke(this.getJavaObject(), new Object[] { new Integer(index), actualValue });
			} catch(Exception err) {
				throw new WrappedException(err);
			}
		}
	}

	public void put(String name, Scriptable start, Object value) {
		PropertyInfo pinfo = this.classWrapper.getProperty(name);
		if(pinfo != null) {
			Method setter = pinfo.getSetter();
			if(setter == null) {
				throw new EvaluatorException("Property '" + name + "' is not settable");
			}
			try {
				Object actualValue;
				if(value instanceof JavaObjectWrapper) {
					actualValue = ((JavaObjectWrapper) value).getJavaObject();
				}
				else {
					actualValue = value;
				}
				setter.invoke(this.getJavaObject(), new Object[] { actualValue });
			} catch(Exception err) {
				throw new WrappedException(err);
			}
		}
		else {
			PropertyInfo ni = this.classWrapper.getNameIndexer();
			if(ni != null) {
				Method setter = ni.getSetter();
				if(setter == null) {
					throw new EvaluatorException("Named indexes are read-only");
				}
				try {
					Object actualValue;
					if(value instanceof JavaObjectWrapper) {
						actualValue = ((JavaObjectWrapper) value).getJavaObject();
					}
					else {
						actualValue = value;
					}
					setter.invoke(this.getJavaObject(), new Object[] { name, actualValue });
				} catch(Exception err) {
					throw new WrappedException(err);
				}
			}
			else {
				super.put(name, start, value);
			}
		}
	}
	
	public static Function getConstructor(String className, JavaClassWrapper classWrapper, Scriptable scope) {
		return new JavaConstructorObject(className, classWrapper);
	}
	
	public java.lang.Object getDefaultValue(java.lang.Class hint) {
		if(hint == null || String.class.equals(hint)) {
			return this.delegate.toString(); 
		}
		else {
			return super.getDefaultValue(hint);
		}
	}
}
