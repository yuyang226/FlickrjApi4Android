/**
 * 
 */
package com.yuyang226.flickr.oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.people.User;



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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Flickr f = new Flickr("cf133e9bab9b574fa5f8166c9ecf6455", "d9b66ded5812c3a8");
			/*OAuthToken oauthToken = f.getOAuthInterface().getRequestToken("http://localhost");
			System.out.println(oauthToken);
			System.out.println(f.getOAuthInterface().buildAuthenticationUrl(Permission.READ, oauthToken));
			String tokenVerifier = readParamFromCommand("Enter Token Verifier: ");
			f.getOAuthInterface().getAccessToken(oauthToken, tokenVerifier);
			f.getOAuthInterface().testLogin();*/
			
			//oauth_token=72157626911878883-7288bed42b42e288, oauth_token_secret=9b5e1fc9a8d33997
			OAuth auth = new OAuth();
			User user = new User();
			user.setId("id=8308954@N06");
			user.setUsername("Yang and Yun's Album");
			auth.setToken(new OAuthToken("72157626911878883-7288bed42b42e288", "9b5e1fc9a8d33997"));
			RequestContext.getRequestContext().setOAuth(auth);
			
			System.out.println(f.getOAuthInterface().testLogin());
//			System.out.println(f.getActivityInterface().userComments(0, 0));
//			System.out.println(f.getActivityInterface().userPhotos(1, 1, null));
//			System.out.println(f.getBlogsInterface().getServices());
//			System.out.println(f.getBlogsInterface().getList());
//			System.out.println(f.getCommonsInterface().getInstitutions());
//			System.out.println(f.getContactsInterface().getList());
//			System.out.println(f.getContactsInterface().getPublicList("8308954@N06"));
//			System.out.println(f.getContactsInterface().getListRecentlyUploaded(
//					new Date(System.currentTimeMillis() - 24L * 60L * 60L * 1000L), null));
			System.out.println(f.getPeopleInterface().findByEmail("wanyun892@yahoo.cn"));
			System.out.println(f.getPeopleInterface().findByUsername("Yang and Yun's Album"));
			System.out.println(f.getPeopleInterface().getInfo("8308954@N06"));
			System.out.println(f.getPeopleInterface().getPublicGroups("8308954@N06"));
			System.out.println(f.getPeopleInterface().getUploadStatus());
//			System.out.println(f.getPeopleInterface().getPublicPhotos("8308954@N06", 0, 0));
			System.out.println(f.getPeopleInterface().getPhotos("8308954@N06", null, 0, 0));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
