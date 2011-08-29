/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.yuyang226.flickr.oauth;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.Parameter;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.Response;
import com.aetrion.flickr.Transport;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.people.User;
import com.aetrion.flickr.util.UrlUtilities;
import com.yuyang226.flickr.org.json.JSONException;
import com.yuyang226.flickr.org.json.JSONObject;

/**
 * Authentication interface for the new Flickr OAuth 1a support: 
 * http://www.flickr.com/services/api/auth.oauth.html
 *
 * @author Toby Yu
 */
public class OAuthInterface {

	public static final String METHOD_TEST_LOGIN = "flickr.test.login";

	public static final String KEY_OAUTH_CALLBACK_CONFIRMED = "oauth_callback_confirmed";
	public static final String KEY_OAUTH_TOKEN = "oauth_token";
	public static final String KEY_OAUTH_TOKEN_SECRET = "oauth_token_secret";
	public static final String KEY_OAUTH_VERIFIER = "oauth_verifier";

	public static final String PATH_OAUTH_REQUEST_TOKEN = "/services/oauth/request_token";
	public static final String PATH_OAUTH_ACCESS_TOKEN = "/services/oauth/access_token";
	public static final String PATH_REST = "/services/rest";
	public static final String URL_REQUEST_TOKEN = "http://" + Flickr.DEFAULT_HOST + PATH_OAUTH_REQUEST_TOKEN;
	public static final String URL_ACCESS_TOKEN = "http://" + Flickr.DEFAULT_HOST + PATH_OAUTH_ACCESS_TOKEN;

	public static final String URL_REST = "http://" + Flickr.DEFAULT_HOST + PATH_REST;

	public static final String PARAM_OAUTH_CONSUMER_KEY = "oauth_consumer_key";
	public static final String PARAM_OAUTH_TOKEN = "oauth_token";

	private String apiKey;
	private String sharedSecret;
	private REST oauthTransport;
	private static final Logger logger = Logger.getLogger(OAuthInterface.class.getName());

	/**
	 * Construct the AuthInterface.
	 *
	 * @param apiKey The API key
	 * @param transport The Transport interface
	 */
	public OAuthInterface(
			String apiKey,
			String sharedSecret,
			Transport transport
	) {
		this.apiKey = apiKey;
		this.sharedSecret = sharedSecret;
		this.oauthTransport = (REST)transport;
	}

	public User testLogin() 
	throws InvalidKeyException, NoSuchAlgorithmException, FlickrException, IOException, JSONException, SAXException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("method", METHOD_TEST_LOGIN));

		Response response = this.oauthTransport.postOAuthJSON(this.apiKey, this.sharedSecret, parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
		}

		JSONObject jObj = response.getData();
		String stat = jObj.getString("stat");
		JSONObject userObj = jObj.getJSONObject("user");
		String id = userObj.getString("id");
		String name = userObj.getJSONObject("username").getString("_content");
		User user = new User();
		user.setId(id);
		user.setUsername(name);
		return user;
	}
	
	/**
	 * Get a request token.
	 *
	 * @return the frob
	 * @throws IOException
	 * @throws SAXException
	 * @throws FlickrException
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws URISyntaxException 
	 */
	public OAuthToken getRequestToken(String callbackUrl) throws IOException, FlickrException, 
	InvalidKeyException, NoSuchAlgorithmException {
		if (callbackUrl == null)
			callbackUrl = "oob";
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("oauth_callback", callbackUrl));
		OAuthUtils.addBasicOAuthParams(apiKey, this.sharedSecret, parameters);
		OAuthUtils.signGet(this.sharedSecret, URL_REQUEST_TOKEN, parameters);

		Map<String, String> response = this.oauthTransport.getMapData(true, PATH_OAUTH_REQUEST_TOKEN, parameters);
		if (response.isEmpty()) {
			throw new FlickrException("Empty Response", "Empty Response");
		}

		if (response.containsKey(KEY_OAUTH_CALLBACK_CONFIRMED) == false
				|| Boolean.valueOf(response.get(KEY_OAUTH_CALLBACK_CONFIRMED)) == false) {
			throw new FlickrException("Error", "Invalid response: " + response);
		}
		String token = response.get(KEY_OAUTH_TOKEN);
		String token_secret = response.get(KEY_OAUTH_TOKEN_SECRET);
		logger.info("Response: " + response);
		OAuth oauth = new OAuth();
		oauth.setToken(new OAuthToken(token, token_secret));
		RequestContext.getRequestContext().setOAuth(oauth);
		return oauth.getToken();
	}

	/**
	 * @param requestToken
	 * @param oauthVerifier
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws FlickrException
	 * @throws SAXException 
	 */
	public OAuth getAccessToken(OAuthToken oauthToken, String oauthVerifier) 
	throws InvalidKeyException, NoSuchAlgorithmException, IOException, FlickrException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter(KEY_OAUTH_VERIFIER, oauthVerifier));
		OAuthUtils.addOAuthParams(apiKey, this.sharedSecret, URL_ACCESS_TOKEN, parameters);

		Map<String, String> response = this.oauthTransport.getMapData(false, PATH_OAUTH_ACCESS_TOKEN, parameters);
		if (response.isEmpty()) {
			throw new FlickrException("Empty Response", "Empty Response");
		}
		logger.info("Response: " + response);
		OAuth result = new OAuth();
		User user = new User();
		user.setId(response.get("user_nsid"));
		user.setUsername(response.get("username"));
		user.setRealName(response.get("fullname"));
		result.setUser(user);
		result.setToken(new OAuthToken(
				response.get("oauth_token"), response.get("oauth_token_secret")));
		RequestContext.getRequestContext().setOAuth(result);
		return result;
	}


	/**
	 * Build the authentication URL using the given permission and frob.
	 *
	 * The hostname used here is www.flickr.com. It differs from the api-default
	 * api.flickr.com.
	 * 
	 * @param permission The Permission
	 * @param frob The frob returned from getFrob()
	 * @return The URL
	 * @throws MalformedURLException
	 */
	public URL buildAuthenticationUrl(Permission permission, OAuthToken oauthToken) throws MalformedURLException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter(PARAM_OAUTH_TOKEN, oauthToken.getOauthToken()));
		if (permission != null) {
			parameters.add(new Parameter("perms", permission.toString()));
		}

		int port = oauthTransport.getPort();
		String path = "/services/oauth/authorize";

		return UrlUtilities.buildUrl(Flickr.DEFAULT_HOST, port, path, parameters);
	}

}
