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
 * Created on Jan 15, 2006
 */
package org.xamjwg.html;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * The <code>FormInput</code> class contains the state
 * of an HTML form input item.
 * @author J. H. S.
 */
public class FormInput {
	public static final FormInput[] EMPTY_ARRAY = new FormInput[0];
	private final String name;
	private final InputStream inputStream;
	private final String charset;
	
	/**
	 * Constructs a <code>FormInput</code>.
	 * @param name The name of the input.
	 * @param stream The stream containing the input data.
	 */
	public FormInput(String name, InputStream stream) {
		super();
		this.name = name;
		this.inputStream = stream;
		this.charset = "UTF-8";
	}
	
	/**
	 * Constructs a <code>FormInput</code>.
	 * @param name The name of the input.
	 * @param value The value of the input.
	 * @param charset The character set of the value.
	 * @throws UnsupportedEncodingException
	 */
	public FormInput(String name, String value, String charset) throws UnsupportedEncodingException {
		super();
		this.name = name;
		this.inputStream = value == null ? null : new ByteArrayInputStream(value.getBytes(charset));
		this.charset = charset;
	}

	/**
	 * Constructs a <code>FormInput</code> assuming
	 * the value charset is UTF-8.
	 * @param name The name of the input.
	 * @param value The value of the input.
	 * @throws UnsupportedEncodingException
	 */
	public FormInput(String name, String value) throws UnsupportedEncodingException {
		this(name, value, "UTF-8");
	}
	
	/**
	 * Gets the charset of the input value.
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * Gets a stream containing the input value data.
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Gets the name of the input.
	 */
	public String getName() {
		return name;
	}	
}
