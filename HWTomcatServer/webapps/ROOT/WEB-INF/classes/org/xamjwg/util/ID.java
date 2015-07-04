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
 * Created on Jun 7, 2005
 */
package org.xamjwg.util;

import java.util.*;

/**
 * @author J. H. S.
 */
public class ID {
	private static final Random RANDOM1;
	private static final Random RANDOM2;
	
	static {
		RANDOM1 = new Random((int) System.currentTimeMillis());
		RANDOM2 = new Random((int) ID.class.hashCode());
	}
	
	/**
	 * 
	 */
	private ID() {
		super();
	}
	
	public static long generateLong() {
		return Math.abs(RANDOM1.nextLong() ^ RANDOM2.nextLong());
	}
	
	public static int generateInt() {
		return Math.abs(RANDOM1.nextInt() ^ RANDOM2.nextInt());
	}
}
