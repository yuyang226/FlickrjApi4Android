/**
 * 
 */
package com.googlecode.flickrjandroid.oauth;

import com.googlecode.flickrjandroid.FlickrException;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class OAuthException extends FlickrException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     */
    public OAuthException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public OAuthException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public OAuthException(String message, Throwable cause) {
        super(message, cause);
    }

}
