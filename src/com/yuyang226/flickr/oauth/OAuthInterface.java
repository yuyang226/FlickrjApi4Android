/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.yuyang226.flickr.oauth;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.Parameter;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.Response;
import com.aetrion.flickr.Transport;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthUtilities;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.people.User;
import com.aetrion.flickr.util.UrlUtilities;
import com.aetrion.flickr.util.XMLUtilities;

/**
 * Authentication interface for the new Flickr OAuth 1a support: 
 * http://www.flickr.com/services/api/auth.oauth.html
 *
 * @author Toby Yu
 */
public class OAuthInterface {

    public static final String METHOD_CHECK_TOKEN = "flickr.auth.checkToken";
    public static final String METHOD_GET_ACCESS_TOKEN = "flickr.auth.oauth.getAccessToken";
    
    public static final String KEY_OAUTH_CALLBACK_CONFIRMED = "oauth_callback_confirmed";
	public static final String KEY_OAUTH_TOKEN = "oauth_token";
	public static final String KEY_OAUTH_TOKEN_SECRET = "oauth_token_secret";
    
    public static final String HOST_OAUTH = "www.flickr.com";
    public static final String PATH_OAUTH_REQUEST_TOKEN = "/services/oauth/request_token";
    public static final String PATH_OAUTH_ACCESS_TOKEN = "/services/oauth/access_token";
    public static final String URL_REQUEST_TOKEN = "http://" + HOST_OAUTH + PATH_OAUTH_REQUEST_TOKEN;
    public static final String URL_ACCESS_TOKEN = "http://" + HOST_OAUTH + PATH_OAUTH_ACCESS_TOKEN;
    

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;
    private REST oauthTransport;
    private static final Logger logger = Logger.getLogger(OAuthInterface.class);

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
        this.transportAPI = transport;
        try {
			this.oauthTransport = new REST(HOST_OAUTH);
		} catch (ParserConfigurationException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
    }

    /**
     * Check the authentication token for validity.
     *
     * @param authToken The authentication token
     * @return The Auth object
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public Auth checkToken(String authToken) throws IOException, SAXException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_CHECK_TOKEN));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("auth_token", authToken));

        // This method call must be signed.
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Auth auth = new Auth();

        Element authElement = response.getPayload();
        auth.setToken(XMLUtilities.getChildValue(authElement, "token"));
        auth.setPermission(Permission.fromString(XMLUtilities.getChildValue(authElement, "perms")));

        Element userElement = XMLUtilities.getChild(authElement, "user");
        User user = new User();
        user.setId(userElement.getAttribute("nsid"));
        user.setUsername(userElement.getAttribute("username"));
        user.setRealName(userElement.getAttribute("fullname"));
        auth.setUser(user);

        return auth;
    }

    /**
     * Exchange an auth token from the old Authentication API, to an OAuth access token. 
     * Calling this method will delete the auth token used to make the request.
     *
     * @return the new oauth token
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public OAuthToken exchangeOAuthRequestToken(String userOAuthToken) throws IOException, SAXException, FlickrException {
    	 List<Parameter> parameters = new ArrayList<Parameter>();
         parameters.add(new Parameter("method", METHOD_GET_ACCESS_TOKEN));
         parameters.add(new Parameter("api_key", apiKey));
         parameters.add(new Parameter("auth_token", userOAuthToken));

         // This method call must be signed.
         parameters.add(new Parameter("api_sig", AuthUtilities.getSignature(sharedSecret, parameters)));

         Response response = transportAPI.get(transportAPI.getPath(), parameters);
         if (response.isError()) {
             throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
         }
         Element authElement = response.getPayload();
         Element tokenElement = XMLUtilities.getChild(authElement, "access_token");
         String oauthToken = tokenElement.getAttribute("oauth_token");
         String tokenSecret = tokenElement.getAttribute("oauth_token_secret");
         return new OAuthToken(oauthToken, tokenSecret);
    }
    
    public void testLogin(String oauthToken) 
    throws InvalidKeyException, NoSuchAlgorithmException, FlickrException, IOException, JSONException {
    	/*http://api.flickr.com/services/rest
    		?nojsoncallback=1 &oauth_nonce=84354935
    		&format=json
    		&oauth_consumer_key=653e7a6ecc1d528c516cc8f92cf98611
    		&oauth_timestamp=1305583871
    		&oauth_signature_method=HMAC-SHA1
    		&oauth_version=1.0
    		&oauth_token=72157626318069415-087bfc7b5816092c
    		&oauth_signature=dh3pEH0Xk1qILr82HyhOsxRv1XA%3D
    		&method=flickr.test.login*/
    	List<Parameter> parameters = new ArrayList<Parameter>();
    	parameters.add(new Parameter("nojsoncallback", "1"));
    	OAuthUtils.addOAuthNonce(parameters);
    	parameters.add(new Parameter("format", "json"));
    	parameters.add(new Parameter("oauth_consumer_key", apiKey));
    	OAuthUtils.addOAuthTimestamp(parameters);
    	OAuthUtils.addOAuthSignatureMethod(parameters);
    	OAuthUtils.addOAuthVersion(parameters);
    	parameters.add(new Parameter("oauth_token", oauthToken));
    	parameters.add(new Parameter("method", "flickr.test.login"));
    	// generate the oauth_signature
		String signature = OAuthUtils.getSignature(URL_REQUEST_TOKEN, 
				parameters,
				this.sharedSecret);
		logger.info("Signature: " + signature);
		// This method call must be signed.
		parameters.add(new Parameter("oauth_signature", signature));
		
    	String response = ((REST)this.transportAPI).getLine(REST.PATH, parameters);
		if (response == null || response.length() == 0) {
			throw new FlickrException("Empty Response", "Empty Response");
		}
		
    	JSONObject jObj = new JSONObject(response);
		String id = jObj.getString("id");
		String stat = jObj.getString("stat");
		String name = jObj.getJSONObject("username").getString("_content");
		User user = new User();
		user.setId(id);
		user.setUsername(name);
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
		parameters.add(new Parameter("oauth_consumer_key", apiKey));
		OAuthUtils.addOAuthNonce(parameters);
		OAuthUtils.addOAuthSignatureMethod(parameters);
		OAuthUtils.addOAuthTimestamp(parameters);
		OAuthUtils.addOAuthVersion(parameters);

		// generate the oauth_signature
		String signature = OAuthUtils.getSignature(
				URLEncoder.encode(URL_REQUEST_TOKEN, OAuthUtils.ENC),
				URLEncoder.encode(OAuthUtils.format(parameters, OAuthUtils.ENC), OAuthUtils.ENC),
				this.sharedSecret);
		logger.info("Signature: " + signature);
		// This method call must be signed.
		parameters.add(new Parameter("oauth_signature", signature));

		Map<String, String> response = this.oauthTransport.getData(PATH_OAUTH_REQUEST_TOKEN, parameters);
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
		logger.info("oauth URL: http://www.flickr.com/services/oauth/authorize?oauth_token=" + token);
		return new OAuthToken(token, token_secret);
    }
    
    public void getAccessToken(String requestToken, String oauthVerifier) 
    throws InvalidKeyException, NoSuchAlgorithmException, IOException, FlickrException {
    	List<Parameter> parameters = new ArrayList<Parameter>();
    	OAuthUtils.addOAuthNonce(parameters);
    	OAuthUtils.addOAuthTimestamp(parameters);
    	parameters.add(new Parameter("oauth_verifier", oauthVerifier));
    	parameters.add(new Parameter("oauth_consumer_key", apiKey));
		OAuthUtils.addOAuthSignatureMethod(parameters);
		OAuthUtils.addOAuthVersion(parameters);
		parameters.add(new Parameter("oauth_token", requestToken));
		
		String signature = OAuthUtils.getSignature(
				URLEncoder.encode(URL_ACCESS_TOKEN, OAuthUtils.ENC),
				URLEncoder.encode(OAuthUtils.format(parameters, OAuthUtils.ENC), OAuthUtils.ENC),
				this.sharedSecret);
		logger.info("Signature: " + signature);
		// This method call must be signed.
		parameters.add(new Parameter("oauth_signature", signature));
		
		Map<String, String> response = this.oauthTransport.getData(PATH_OAUTH_ACCESS_TOKEN, parameters);
		if (response.isEmpty()) {
			throw new FlickrException("Empty Response", "Empty Response");
		}
		logger.info("Response: " + response);
		
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
    public URL buildAuthenticationUrl(Permission permission, String frob) throws MalformedURLException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("api_key", apiKey));
        parameters.add(new Parameter("perms", permission.toString()));
        parameters.add(new Parameter("frob", frob));

        // The parameters in the url must be signed
        parameters.add(new Parameter("api_sig", AuthUtilities.getSignature(sharedSecret, parameters)));

        String host = "www.flickr.com";
        int port = transportAPI.getPort();
        String path = "/services/oauth/";

        return UrlUtilities.buildUrl(host, port, path, parameters);
    }

}
