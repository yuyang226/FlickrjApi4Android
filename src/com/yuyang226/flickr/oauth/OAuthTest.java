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
			/*OAuthToken oauthToken = f.getOAuthInterface().getRequestToken("http://localhost");
			System.out.println(oauthToken);
			System.out.println(f.getOAuthInterface().buildAuthenticationUrl(Permission.READ, oauthToken));*/
			
			//72157627367793965-bdbc5590de3624c8&oauth_verifier=8647239bff0ea1a2
			//f.getOAuthInterface().testLogin("72157627473105782-46093e232883d652");
			
			f.getOAuthInterface().getAccessToken("72157627367793965-bdbc5590de3624c8", "8647239bff0ea1a2");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
