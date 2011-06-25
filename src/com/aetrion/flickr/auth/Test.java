/**
 * 
 */
package com.aetrion.flickr.auth;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.OAuthProviderListener;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.http.HttpResponse;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.REST;



/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class Test {

	/**
	 * 
	 */
	public Test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// create a consumer object and configure it with the access
	        // token and token secret obtained from the service provider
	        OAuthConsumer consumer = new DefaultOAuthConsumer("da4fadd0084ea1799ad33048f0d6a5c5",
	                                             "186b04791439c326");
	        
//	        consumer.setSigningStrategy(SignatureMethod.HMAC_SHA1);
	        
			OAuthProvider provider = new DefaultOAuthProvider(
	                "http://www.flickr.com/services/oauth/request_token", 
	                "http://www.flickr.com/services/oauth/access_token",
	                "http://www.flickr.com/services/oauth/authorize");
			provider.setListener(new OAuthProviderListener() {

				@Override
				public boolean onResponseReceived(HttpRequest arg0,
						HttpResponse arg1) throws Exception {
					System.out.println(arg0);
					System.out.println(arg1);
					return false;
				}

				@Override
				public void prepareRequest(HttpRequest arg0) throws Exception {
					System.out.println(arg0);
				}

				@Override
				public void prepareSubmission(HttpRequest arg0)
						throws Exception {
					System.out.println(arg0);
				}
				
			});
	        // fetches a request token from the service provider and builds
	        // a url based on AUTHORIZE_WEBSITE_URL and CALLBACK_URL to
	        // which your app must now send the user
			provider.setOAuth10a(true);
			System.out.println(provider.getRequestTokenEndpointUrl());
			
	        String url2 = provider.retrieveRequestToken(consumer, "http://localhost");
	        System.out.println(url2);
	        System.out.println("Token: " + consumer.getToken());
	        System.out.println("Token Secret" + consumer.getTokenSecret());
			Flickr f = new Flickr("cf133e9bab9b574fa5f8166c9ecf6455", 
					"d9b66ded5812c3a8", new REST());
			String oauthToken = "72157626086709404-03e6dc2d330746d8";
			Auth auth = f.getAuthInterface().checkToken(oauthToken);
			System.out.println("72157626911878883-7288bed42b42e288");
			/*RequestContext requestContext = RequestContext.getRequestContext();
			Auth auth = new Auth();
			auth.setPermission(Permission.READ);
			auth.setToken(oauthToken);
			requestContext.setAuth(auth);
			PhotosInterface photosFace = f.getPhotosInterface();
			Set<String> extras = new HashSet<String>(2);
			extras.add(Extras.DATE_UPLOAD);
			extras.add(Extras.LAST_UPDATE);
			extras.add(Extras.GEO);
			Calendar cstTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			cstTime.setTimeInMillis(System.currentTimeMillis());
			System.out.println("Converted current time: " + cstTime.getTime());

			//		Calendar past = getFromTime(globalConfig, currentTime);
			Calendar past = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			long newTime = cstTime.getTime().getTime() - 999999;
			past.setTimeInMillis(newTime);
			PhotoList list = photosFace.recentlyUpdated(past.getTime(), extras, 100,
					1);
			
			System.out.println("Trying to find photos uploaded for user "
					+ " after " + past.getTime().toString() + " from "
					+ list.getTotal() + " new photos");*/
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
