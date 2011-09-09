/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.gmail.yuyang226.flickr.photos.licenses;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.Parameter;
import com.gmail.yuyang226.flickr.Response;
import com.gmail.yuyang226.flickr.Transport;
import com.gmail.yuyang226.flickr.oauth.OAuthUtils;
import com.gmail.yuyang226.flickr.org.json.JSONArray;
import com.gmail.yuyang226.flickr.org.json.JSONException;
import com.gmail.yuyang226.flickr.org.json.JSONObject;

/**
 * Interface for working with copyright licenses.
 *
 * @author Anthony Eden
 */
public class LicensesInterface {

    public static final String METHOD_GET_INFO    = "flickr.photos.licenses.getInfo";
    public static final String METHOD_SET_LICENSE = "flickr.photos.licenses.setLicense";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public LicensesInterface(
        String apiKey,
        String sharedSecret,
        Transport transportAPI
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Fetches a list of available photo licenses for Flickr.
     *
     * This method does not require authentication.
     *
     * @return A collection of License objects
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Collection<License> getInfo() throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_INFO));
        parameters.add(new Parameter("api_key", apiKey));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        List<License> licenses = new ArrayList<License>();
        JSONObject licensesElement = response.getData().getJSONObject("licenses");
        JSONArray licenseElements = licensesElement.optJSONArray("license");
        for (int i = 0; licenseElements != null && i < licenseElements.length(); i++) {
        	JSONObject licenseElement = licenseElements.getJSONObject(i);
            License license = new License();
            license.setId(licenseElement.getString("id"));
            license.setName(licenseElement.getString("name"));
            license.setUrl(licenseElement.getString("url"));
            licenses.add(license);
        }
        return licenses;
    }

    /**
     * Sets the license for a photo.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param photoId The photo to update the license for.
     * @param licenseId The license to apply, or 0 (zero) to remove the current license.
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    public void setLicense(String photoId, int licenseId) throws IOException, FlickrException, InvalidKeyException, NoSuchAlgorithmException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_SET_LICENSE));
        //parameters.add(new Parameter("api_key", apiKey));
        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("license_id", licenseId));
        OAuthUtils.addOAuthToken(parameters);

        // Note: This method requires an HTTP POST request.
        Response response = transportAPI.postJSON(apiKey, sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        // This method has no specific response - It returns an empty sucess response if it completes without error.

    }

}
