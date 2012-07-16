/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickrjandroid.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * String utility methods.
 *
 * @author Anthony Eden
 * @version $Id: StringUtilities.java,v 1.5 2009/07/23 20:41:03 x-mago Exp $
 */
public class StringUtilities {
    public static final Pattern getterPattern = Pattern.compile("^is|^get");

    private StringUtilities() {

    }
    
    public static String[] split(String str, String separator) {
        String[] returnValue;
        int index = str.indexOf(separator);
        if (index == -1) {
            returnValue = new String[]{str};
        } else {
            List<String> strList = new ArrayList<String>();
            int oldIndex = 0;
            while (index != -1) {
                String subStr = str.substring(oldIndex, index);
                strList.add(subStr);
                oldIndex = index + separator.length();
                index = str.indexOf(separator, oldIndex);
            }
            if (oldIndex != str.length()) {
                strList.add(str.substring(oldIndex));
            }
            returnValue = strList.toArray(new String[strList.size()]);
        }

        return returnValue;
    }

    /**
     * Join the array of Strings using the specified delimiter.
     *
     * @param s The String array
     * @param delimiter The delimiter String
     * @return The joined String
     */
    public static String join(String[] s, String delimiter) {
        return join(s, delimiter, false);
    }

    public static String join(String[] s, String delimiter, boolean doQuote) {
        return join(Arrays.asList(s), delimiter, doQuote);
    }

    /**
     * Join the Collection of Strings using the specified delimter and
     * optionally quoting each
     * @param s The String collection
     * @param delimiter the delimiter String
     * @param doQuote whether or not to quote the Strings
     * @return The joined String
     */
    public static String join( Collection<String> s, String delimiter, boolean doQuote ) {
        StringBuffer buffer = new StringBuffer();
        Iterator<String> iter = s.iterator();
        while (iter.hasNext()) {
            if( doQuote ) {
              buffer.append( "\"" + iter.next() + "\"" );
            }
            else {
                buffer.append(iter.next());
            }
            if (iter.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }

    /**
     * Join the Collection of Strings using the specified delimiter.
     *
     * @param s The String collection
     * @param delimiter The delimiter String
     * @return The joined String
     */
    public static String join(Collection<String> s, String delimiter) {
      return join( s, delimiter, false );
    }

}
