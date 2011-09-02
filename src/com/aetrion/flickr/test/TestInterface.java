/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.aetrion.flickr.test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.Parameter;
import com.aetrion.flickr.Response;
import com.aetrion.flickr.Transport;
import com.aetrion.flickr.people.User;
import com.aetrion.flickr.util.JSONUtils;
import com.yuyang226.flickr.oauth.OAuthUtils;
import com.yuyang226.flickr.org.json.JSONException;
import com.yuyang226.flickr.org.json.JSONObject;

/**
 * Interface for testing Flickr connectivity.
 *
 * @author Matt Ray
 */
public class TestInterface {

    public static final String METHOD_ECHO = "flickr.test.echo";
    public static final String METHOD_LOGIN = "flickr.test.login";
    public static final String METHOD_NULL = "flickr.test.null";

    private String apiKey;
    private String sharedSecret;
    private Transport transport;

    public TestInterface(
        String apiKey,
        String sharedSecret,
        Transport transportAPI
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transport = transportAPI;
    }

    /**
     * A testing method which echo's all paramaters back in the response.
     *
     * @param params The parameters
     * @return The Collection of echoed elements
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public JSONObject echo(Collection<Parameter> params) 
    throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_ECHO));
        parameters.add(new Parameter("api_key", apiKey));
        parameters.addAll(params);

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return response.getData();
    }

    /**
     * A testing method which checks if the caller is logged in then returns a User object.
     *
     * @return The User object
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    public User login() throws IOException, FlickrException, InvalidKeyException, NoSuchAlgorithmException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_LOGIN));
        //parameters.add(new Parameter("api_key", apiKey));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(apiKey, sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject userElement = response.getData().getJSONObject("user");
        User user = new User();
        user.setId(userElement.getString("id"));

        user.setUsername(JSONUtils.getChildValue(userElement, "username"));
        return user;
    }
    
    /**
     * Null test.
     * This method requires authentication with 'read' permission.
     * @throws IOException 
     * @throws FlickrException 
     * @throws JSONException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    public void null_() throws IOException, FlickrException, InvalidKeyException, NoSuchAlgorithmException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_NULL));
//        parameters.add(new Parameter("api_key", apiKey));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(apiKey, sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

}
