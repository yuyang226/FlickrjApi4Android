/**
 * 
 */
package com.yuyang226.flickr.oauth;

import java.util.Arrays;



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
			String query = "oauth_token=72157627473105782-46093e232883d652&oauth_verifier=85f63b4443cce876";
			String[] data = query.split("&");
			System.out.println(Arrays.asList(data));
			/*
			Flickr f = new Flickr("cf133e9bab9b574fa5f8166c9ecf6455", "d9b66ded5812c3a8");
			OAuthToken oauthToken = f.getOAuthInterface().getRequestToken("http://localhost");
			System.out.println(oauthToken);
			System.out.println(f.getOAuthInterface().buildAuthenticationUrl(Permission.READ, oauthToken));
			//oauth_token=72157627473105782-46093e232883d652&oauth_verifier=85f63b4443cce876
			f.getOAuthInterface().testLogin("72157627473166384-736060216548e632");*/
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
