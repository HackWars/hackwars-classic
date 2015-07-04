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
 * Created on Nov 13, 2005
 */
package org.xamjwg.html.io;

import java.io.*;

public class WritableLineReader extends Reader {
	private final LineNumberReader delegate;
	
	public WritableLineReader(Reader reader) {
		this.delegate = new LineNumberReader(reader);
	}

	public WritableLineReader(LineNumberReader reader) {
		this.delegate = reader;
	}
	
	public WritableLineReader(InputStream stream, String charset) throws UnsupportedEncodingException {
		if(stream == null) {
			throw new IllegalArgumentException("stream is null");
		}
		this.delegate = new LineNumberReader(new InputStreamReader(stream, charset));
	}

	/*
	 * Note: Not implicitly thread safe.
	 */
	public int read() throws IOException {
		StringBuffer sb = this.writeBuffer;
		if(sb != null && sb.length() > 0) {
			char ch = sb.charAt(0);
			sb.deleteCharAt(0);
			if(sb.length() == 0) {
				this.writeBuffer = null;
			}
			return (int) ch;
		}
		return this.delegate.read();
	}
	
	/* (non-Javadoc)
	 *  Note: Not implicitly thread safe.
	 * @see java.io.Reader#read(byte[], int, int)
	 */
	public int read(char[] b, int off, int len) throws IOException {
		StringBuffer sb = this.writeBuffer;
		if(sb != null && sb.length() > 0) {
			int srcEnd = Math.min(sb.length(), len);
			sb.getChars(0, srcEnd, b, off);
			sb.delete(0, srcEnd);
			if(sb.length() == 0) {
				this.writeBuffer = null;
			}
			return srcEnd;
		}
		return this.delegate.read(b, off, len);
	}	
	
	public boolean ready() throws IOException {
		StringBuffer sb = this.writeBuffer;
		if(sb != null && sb.length() > 0) {
			return true;
		}
		return this.delegate.ready();
	}

	/* (non-Javadoc)
	 * Note: Not implicitly thread safe.
	 * @see java.io.Reader#close()
	 */
	public void close() throws IOException {
		this.writeBuffer = null;
		this.delegate.close();
	}
	
	private StringBuffer writeBuffer = null;
	
	/**
	 * Note: Not implicitly thread safe.
	 * @param text
	 * @throws IOException
	 */
	public void write(String text) throws IOException {
		StringBuffer sb = this.writeBuffer;
		if(sb == null) {
			sb = new StringBuffer();
			this.writeBuffer = sb;
		}
		sb.append(text);
	}
	
	public int getLineNumber() {
		return this.delegate.getLineNumber();
	}
}
