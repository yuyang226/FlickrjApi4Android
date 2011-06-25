/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.aetrion.flickr.auth;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.Parameter;
import com.aetrion.flickr.Response;
import com.aetrion.flickr.Transport;
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
    public static final String METHOD_GET_FROB = "flickr.auth.getFrob";
    public static final String METHOD_GET_TOKEN = "flickr.auth.getToken";
    public static final String METHOD_GET_FULL_TOKEN = "flickr.auth.getFullToken";
    public static final String METHOD_GET_ACCESS_TOKEN = "flickr.auth.oauth.getAccessToken";
    

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

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
     * Get the full authentication token for a mini-token.
     * @param miniToken The mini-token typed in by a user. 
     * It should be 9 digits long. It may optionally contain dashes.
     * @return an Auth object containing the full token, permissions and user info. 
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public Auth getFullToken(String miniToken) throws IOException, SAXException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_FULL_TOKEN));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("mini_token", miniToken));

        // This method call must be signed.
        parameters.add(new Parameter("api_sig", AuthUtilities.getSignature(sharedSecret, parameters)));

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
     * Get a request token.
     *
     * @return the frob
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public String getRequestToken(String userOAuthToken) throws IOException, SAXException, FlickrException {
    	 List<Parameter> parameters = new ArrayList<Parameter>();
         parameters.add(new Parameter("method", METHOD_GET_ACCESS_TOKEN));
         parameters.add(new Parameter("api_key", apiKey));
         parameters.add(new Parameter("format", "rest"));
         parameters.add(new Parameter("auth_token", userOAuthToken));

         // This method call must be signed.
         parameters.add(new Parameter("api_sig", AuthUtilities.getSignature(sharedSecret, parameters)));

         Response response = transportAPI.get(transportAPI.getPath(), parameters);
         if (response.isError()) {
             throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
         }
         return XMLUtilities.getValue(response.getPayload());
    }

    /**
     * Get an authentication token for the specific frob.
     *
     * @param frob The frob
     * @return The Auth object
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public Auth getToken(String frob) throws IOException, SAXException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_TOKEN));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("frob", frob));

        // This method call must be signed.
        parameters.add(new Parameter("api_sig", AuthUtilities.getSignature(sharedSecret, parameters)));

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
