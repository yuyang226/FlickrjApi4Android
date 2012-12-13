/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.googlecode.flickrjandroid.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import com.googlecode.flickrjandroid.Parameter;

/** @author Anthony Eden */
public class UrlUtilities {

    public static final String UTF8 = "UTF-8";

    /**
     * Build a request URL.
     * @param host The host
     * @param port The port
     * @param path The path
     * @param parameters The parameters
     * @return The URL
     * @throws MalformedURLException
     */
    public static URL buildUrl(
        String host,
        int port,
        String path,
        List<Parameter> parameters
    ) throws MalformedURLException {
        // see: AuthUtilities.getSignature()
        // AuthUtilities.addAuthToken(parameters);

        StringBuffer buffer = new StringBuffer();
        buffer.append("http://");
        buffer.append(host);
        if (port > 0 & port != 80) {
            buffer.append(":");
            buffer.append(port);
        }
        if (path == null) {
            path = "/";
        }
        buffer.append(path);

        Iterator<Parameter> iter = parameters.iterator();
        if (iter.hasNext()) {
            buffer.append("?");
        }
        while (iter.hasNext()) {
            Parameter p = (Parameter) iter.next();
            buffer.append(p.getName());
            buffer.append("=");
        Object value = p.getValue();
        if (value != null) {
        String string = UrlUtilities.encode(value.toString());
        buffer.append(string);
        }
            if (iter.hasNext()) buffer.append("&");
        }

/*        RequestContext requestContext = RequestContext.getRequestContext();
        Auth auth = requestContext.getAuth();
        if (auth != null && !ignoreMethod(getMethod(parameters))) {
            buffer.append("&api_sig=");
            buffer.append(AuthUtilities.getSignature(sharedSecret, parameters));
        } */

        return new URL(buffer.toString());
    }

    public static URL buildPostUrl(String host, int port, String path) throws MalformedURLException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("http://");
        buffer.append(host);
        if (port > 0) {
            buffer.append(":");
            buffer.append(port);
        }
        if (path == null) {
            path = "/";
        }
        buffer.append(path);
        return new URL(buffer.toString());
    }

    private static String getMethod(List<Parameter> parameters) {
        Iterator<Parameter> iter = parameters.iterator();
        while (iter.hasNext()) {
            Parameter parameter = (Parameter) iter.next();
            if ("method".equals(parameter.getName())) {
                return String.valueOf(parameter.getValue());
            }
        }
        return null;
    }

    private static boolean ignoreMethod(String method) {
        if (method != null) {
            if ("flickr.auth.checkToken".equals(method)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Construct the BuddyIconUrl.<p>
     * If none available, return the 
     * <a href="http://www.flickr.com/images/buddyicon.jpg">default</a>,
     * or an URL assembled from farm, iconserver and nsid.
     *
     * @see <a href="http://flickr.com/services/api/misc.buddyicons.html">Flickr Documentation</a>
     * @param iconFarm
     * @param iconServer
     * @param id
     * @return The BuddyIconUrl
     */
    public static String createBuddyIconUrl(
        int iconFarm,
        int iconServer,
        String id
    ) {
        /**
         * The default-URL, if the iconServer equals 0.
         */
        String iconUrl = "http://www.flickr.com/images/buddyicon.jpg";
        if (iconServer > 0) {
            iconUrl = "http://farm"
            + iconFarm + ".static.flickr.com/"
            + iconServer + "/buddyicons/"
            + id + ".jpg";
        }
        return iconUrl;
    }

    /**
     * @param value string to be encoded
     * @return encoded string
     * @see <a href="http://wiki.oauth.net/TestCases">OAuth / TestCases</a>
     * @see <a href="http://groups.google.com/group/oauth/browse_thread/thread/a8398d0521f4ae3d/9d79b698ab217df2?hl=en&lnk=gst&q=space+encoding#9d79b698ab217df2">Space encoding - OAuth | Google Groups</a>
     * @see <a href="http://tools.ietf.org/html/rfc3986#section-2.1">RFC 3986 - Uniform Resource Identifier (URI): Generic Syntax - 2.1. Percent-Encoding</a>
     */
    public static String encode(String value) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }
        StringBuffer buf = new StringBuffer(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%' && (i + 1) < encoded.length()
                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }
    
}
