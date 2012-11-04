package com.googlecode.flickrjandroid.commons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.Parameter;
import com.googlecode.flickrjandroid.Response;
import com.googlecode.flickrjandroid.Transport;

/**
 *
 * @author mago
 * @version $Id: CommonsInterface.java,v 1.2 2009/07/11 20:30:27 x-mago Exp $
 */
public class CommonsInterface {
    public static final String METHOD_GET_INSTITUTIONS = "flickr.commons.getInstitutions";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public CommonsInterface(
        String apiKey,
        String sharedSecret,
        Transport transportAPI
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Retrieves a list of the current Commons institutions.
     *
     * This method does not require authentication.
     *
     * @return List of Institution
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public List<Institution> getInstitutions() throws FlickrException, IOException, JSONException {
        List<Institution> institutions = new ArrayList<Institution>();
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_INSTITUTIONS));
        parameters.add(new Parameter("api_key", apiKey));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject mElement = response.getData().getJSONObject("institutions");
        JSONArray mNodes = mElement.getJSONArray("institution");
        for (int i = 0; i < mNodes.length(); i++) {
            JSONObject element = mNodes.getJSONObject(i);
            institutions.add(parseInstitution(element));
        }
        return institutions;
    }

    private Institution parseInstitution(JSONObject mElement) throws JSONException {
        Institution inst = new Institution();
        inst.setId(mElement.getString("nsid"));
        inst.setDateLaunch(mElement.getLong("date_launch"));
        inst.setName(mElement.getJSONObject("name").getString("_content"));
        JSONObject urlsElement = mElement.getJSONObject("urls");
        JSONArray urlNodes = urlsElement.getJSONArray("url");
        for (int i = 0; i < urlNodes.length(); i++) {
            JSONObject urlElement = urlNodes.getJSONObject(i);
            if (urlElement.getString("type").equals("site")) {
                inst.setSiteUrl(urlElement.getString("_content"));
            } else if (urlElement.getString("type").equals("license")) {
                inst.setLicenseUrl(urlElement.getString("_content"));
            } else if (urlElement.getString("type").equals("flickr")) {
                inst.setFlickrUrl(urlElement.getString("_content"));
            }
        }
        return inst;
    }
}
