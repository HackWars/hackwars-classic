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
 * Created on Jun 2, 2005
 */
package org.xamjwg.util;

import java.util.*;

/**
 * @author J. H. S.
 */
public class Domains {
	private static final Collection gTLDs;
	
	static {
		gTLDs = new HashSet();
		gTLDs.add(".com");
		gTLDs.add(".edu");
		gTLDs.add(".gov");
		gTLDs.add(".int");
		gTLDs.add(".mil");
		gTLDs.add(".net");
		gTLDs.add(".org");
		gTLDs.add(".biz");
		gTLDs.add(".info");
		gTLDs.add(".name");
		gTLDs.add(".pro");
		gTLDs.add(".aero");
		gTLDs.add(".coop");
		gTLDs.add(".museum");
		//TODO: New gTLDs?
	}
	
	/**
	 * 
	 */
	private Domains() {
		super();
	}
	
	public static boolean isValidCookieDomain(String domain, String hostName) {
		if(!hostName.endsWith(domain)) {
			return false;
		}
		int lastDotIdx = domain.lastIndexOf('.');
		if(lastDotIdx == -1) {
			return false;
		}
		String suffix = domain.substring(lastDotIdx);
		if(gTLDs.contains(suffix.toLowerCase())) {
			return Strings.countChars(domain, '.') >= 2;
		}
		else {
			return Strings.countChars(domain, '.') >= 3;
		}
	}	
	
	public static boolean endsWithGTLD(String host) {
		Iterator i = gTLDs.iterator();
		while(i.hasNext()) {
			String ending = (String) i.next();
			if(host.endsWith(ending)) {
				return true;
			}
		}
		return false;
	}
}
