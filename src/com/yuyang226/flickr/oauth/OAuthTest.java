/**
 * 
 */
package com.yuyang226.flickr.oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.Parameter;
import com.aetrion.flickr.auth.Permission;



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

	private static String readParamFromCommand(String message) throws IOException {
		//  prompt the user to enter their name
		System.out.print(message);
		//  open up standard input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		return String.valueOf(br.readLine()).trim();
	}
	
	private static void test() throws Exception {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("oauth_nonce", "kllo9940pd9333jh"));
		parameters.add(new Parameter("oauth_timestamp", "1191242096"));
		
		parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, "dpf43f3p2l4k3l03"));
		OAuthUtils.addOAuthSignatureMethod(parameters);
		OAuthUtils.addOAuthVersion(parameters);
		parameters.add(new Parameter("file", "vacation.jpg"));
		parameters.add(new Parameter("size", "original"));
		parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_TOKEN, "nnch734d00sl2jdk"));
		String baseString = OAuthUtils.getRequestBaseString(OAuthUtils.REQUEST_METHOD_GET, "http://photos.example.net/photos", parameters);
		
		String expected = "GET&http%3A%2F%2Fphotos.example.net%2Fphotos&file%3Dvacation.jpg%26oauth_consumer_key%3Ddpf43f3p2l4k3l03%26oauth_nonce%3Dkllo9940pd9333jh%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1191242096%26oauth_token%3Dnnch734d00sl2jdk%26oauth_version%3D1.0%26size%3Doriginal";
		if (baseString.equals(expected) == false) {
			System.out.println("Generated Base String: " + baseString);
			throw new RuntimeException("baseString not match");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Flickr f = new Flickr("cf133e9bab9b574fa5f8166c9ecf6455", "d9b66ded5812c3a8");
			OAuthToken oauthToken = f.getOAuthInterface().getRequestToken("http://localhost");
			System.out.println(oauthToken);
			System.out.println(f.getOAuthInterface().buildAuthenticationUrl(Permission.READ, oauthToken));
			String tokenVerifier = readParamFromCommand("Enter Token Verifier: ");
			OAuth oauth = f.getOAuthInterface().getAccessToken(oauthToken, tokenVerifier);
			f.getOAuthInterface().testLogin(oauth.getToken());
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
