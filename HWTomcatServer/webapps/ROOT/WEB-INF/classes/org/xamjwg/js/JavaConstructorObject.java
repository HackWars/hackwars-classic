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

public class JavaConstructorObject extends ScriptableObject implements Function {
	private final JavaClassWrapper classWrapper;
	private final String name;
	
	public JavaConstructorObject(String name, JavaClassWrapper classWrapper) {
		this.name = name;
		this.classWrapper = classWrapper;
	}

	public String getClassName() {
		return this.name;
	}

	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		throw new UnsupportedOperationException();
	}

	public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
		try {
			Scriptable newObject = new JavaObjectWrapper(this.classWrapper);
			newObject.setParentScope(scope);
			return newObject;
		} catch(Exception err) {
			throw new IllegalStateException(err.getMessage());
		}
	}
	
	public java.lang.Object getDefaultValue(java.lang.Class hint) {
		if(String.class.equals(hint)) {
			return "function " + this.name;
		}
		else {
			return super.getDefaultValue(hint);
		}
	}
}
