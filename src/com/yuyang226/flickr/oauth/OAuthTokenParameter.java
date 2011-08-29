/**
 * 
 */
package com.yuyang226.flickr.oauth;

import com.aetrion.flickr.Parameter;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class OAuthTokenParameter extends Parameter {

	/**
	 * @param name
	 * @param value
	 */
	public OAuthTokenParameter(Object value) {
		super(OAuthInterface.KEY_OAUTH_TOKEN, value);
	}

}
