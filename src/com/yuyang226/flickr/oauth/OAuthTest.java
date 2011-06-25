/**
 * 
 */
package com.yuyang226.flickr.oauth;

import com.aetrion.flickr.Flickr;



/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class OAuthTest {

	/**
	 * 
	 */
	public OAuthTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Flickr f = new Flickr("cf133e9bab9b574fa5f8166c9ecf6455", "d9b66ded5812c3a8");
			OAuthToken oauthToken = f.getOAuthInterface().getRequestToken(null);
			System.out.println(oauthToken);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
