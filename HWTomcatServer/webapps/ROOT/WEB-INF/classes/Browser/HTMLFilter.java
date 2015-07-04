/*
* Copyright 2004 The Apache Software Foundation
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* Modified: Benjamin E. Coe 2007.
*/
package Browser;
//import DolphinBlog.element.Textile.*;

/**
 * HTML filter utility.
 *
 * @author Craig R. McClanahan
 * @author Tim Tye
 * @version $Revision: 267129 $ $Date: 2004-03-18 11:40:35 -0500 (Thu, 18 Mar 2004) $
 */

public class HTMLFilter {


    /**
     * Filter the specified message string for characters that are sensitive
     * in HTML.  This avoids potential attacks caused by including JavaScript
     * codes in the request URL that is often reported in error messages.
     *
     * @param message The message string to be filtered
     */
    public static String filter(String message) {

		if (message == null)
			return " ";

		if (message.length() < 1)
			return " ";

        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuffer result = new StringBuffer(content.length + 50);
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
            case '<':
                result.append("&lt;");
                break;
            case '>':
                result.append("&gt;");
                break;
            case '&':
                result.append("&amp;");
                break;
            case '"':
                result.append("&quot;");
                break;
            default:
				if(content[i]>126)
					result.append("&#"+(int)(content[i])+";");
				else
					result.append(content[i]);
            }
        }
        return (result.toString());

    }

	/**
	Convert any illegal characters into &char; representation.
	*/
	public static String convertString(String original){
		StringBuffer result = new StringBuffer(original.length());
		for(int i=0;i<original.length();i++){
			if(original.charAt(i)<32||original.charAt(i)>126){
				int temp=original.charAt(i);
				if(temp<0)
					temp=256-temp;
				result.append("&#"+temp+";");
			}else
				result.append(original.charAt(i));
		}
		return(result.toString());
	}
	
	/**
	re-convert string.
	*/
	public static String reconvertString(String original){
		String returnMe=original;
		for(int i=0;i<10000;i++)
			if(i>31||i==10)
				returnMe=returnMe.replaceAll("&#"+i+";","\\"+(char)i);
			else
				returnMe=returnMe.replaceAll("&#"+i+";","");
	
		return(returnMe);
	}
	
	//////////////////////////////////////
	// getSafe()
	// Description: Return the data in a form
	// that is safe for http get.
	//////////////////////////////////////
	private static final char unsafe[]={'$','&','+',',','/',':',';','=','?','@',' ','"','<','>','#','%','{','}','|','\\','^','~','[',']',((char)96)};
	private static final String safe[]={"%24","%26","%2B","%2C","%2F","%3A","%3B","%3D","%3F","%40","%20","%22","%3C","%3E","%23","%25","%7B","%7D","%7C","%5C","%5E","%7E","%5B","%5D","%60"};
	public static String getURLSafe(String temp){
		if(temp==null)
			return("");
	
		String temp2="";
		for(int ii=0;ii<temp.length();ii++){
			boolean isSafe=true;
			int i;
			for(i=0;i<unsafe.length;i++){
				if(unsafe[i]==temp.charAt(ii)){
					isSafe=false;
					break;
				}
			}
			if(!isSafe)
				temp2+=safe[i];
			else
				temp2+=temp.charAt(ii);
		}
		
		return(temp2);
	}
	
	/**
	Return an URL back to its unsafe form.
	*/
	public static String getURLUnsafe(String temp){
		if(temp==null)
			return("");
	
		for(int i=0;i<unsafe.length;i++){
			temp=temp.replaceAll(safe[i],""+unsafe[i]);
		}
		return(temp);
	}
	
	public static String getSafe(String temp){
		return(temp.replaceAll(" ","_"));
	}

}

