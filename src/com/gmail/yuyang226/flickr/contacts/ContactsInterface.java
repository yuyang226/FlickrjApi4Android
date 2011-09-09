/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.gmail.yuyang226.flickr.contacts;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
 * Interface for working with Flickr contacts.
 *
 * @author Anthony Eden
 * @version $Id: ContactsInterface.java,v 1.18 2009/07/11 20:30:27 x-mago Exp $
 */
public class ContactsInterface {

    private static final String METHOD_GET_LIST = "flickr.contacts.getList";
    private static final String METHOD_GET_LIST_RECENTLY_UPLOADED = "flickr.contacts.getListRecentlyUploaded";
    private static final String METHOD_GET_PUBLIC_LIST = "flickr.contacts.getPublicList";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public ContactsInterface(
        String apiKey,
        String sharedSecret,
        Transport transportAPI
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Get the collection of contacts for the calling user.
     *
     * @return The Collection of Contact objects
     * @throws IOException
     * @throws JSONException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    public Collection<Contact> getList() throws IOException, FlickrException, InvalidKeyException, NoSuchAlgorithmException, JSONException {
        List<Contact> contacts = new ArrayList<Contact>();

        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_LIST));
        OAuthUtils.addOAuthToken(parameters);
        Response response = transportAPI.postJSON(apiKey, sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONObject contactsElement = response.getData().getJSONObject("contacts");
        JSONArray contactNodes = contactsElement.getJSONArray("contact");
        for (int i = 0; i < contactNodes.length(); i++) {
        	JSONObject contactElement = contactNodes.getJSONObject(i);
            Contact contact = new Contact();
            contact.setId(contactElement.getString("nsid"));
            contact.setUsername(contactElement.getString("username"));
            contact.setRealName(contactElement.getString("realname"));
            contact.setFriend("1".equals(contactElement.getString("friend")));
            contact.setFamily("1".equals(contactElement.getString("family")));
            contact.setIgnored("1".equals(contactElement.getString("ignored")));
            contact.setPathAlias(contactElement.getString("path_alias"));
            contact.setLocation(contactElement.getString("location"));
            contact.setIconFarm(contactElement.getString("iconfarm"));
            contact.setIconServer(contactElement.getString("iconserver"));
            contacts.add(contact);
        }
        return contacts;
    }

    /**
     * Return a list of contacts for a user who have recently uploaded photos
     * along with the total count of photos uploaded.
     *
     * @param lastUpload Limits the resultset to contacts that have uploaded photos since this date. The date should be in the form of a Unix timestamp. The default, and maximum, offset is (1) hour. (Optional, can be null)
     * @param filter Limit the result set to all contacts or only those who are friends or family.<br/>Valid options are: <b>ff</b> -&gt; friends and family, <b>all</b> -&gt; all your contacts. (Optional, can be null)
     *
     * @return List of Contacts
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    public Collection<Contact> getListRecentlyUploaded(Date lastUpload, String filter)
      throws IOException, FlickrException, InvalidKeyException, NoSuchAlgorithmException, JSONException {
        List<Contact> contacts = new ArrayList<Contact>();

        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_LIST_RECENTLY_UPLOADED));

        if (lastUpload != null) {
            parameters.add(new Parameter("date_lastupload", lastUpload.getTime() / 1000L));
        }
        if (filter != null) {
            parameters.add(new Parameter("filter", filter));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.postJSON(apiKey, sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONObject contactsElement = response.getData().getJSONObject("contacts");
        JSONArray contactNodes = contactsElement.getJSONArray("contact");
        for (int i = 0; i < contactNodes.length(); i++) {
        	JSONObject contactElement = contactNodes.getJSONObject(i);
            Contact contact = new Contact();
            contact.setId(contactElement.getString("nsid"));
            contact.setUsername(contactElement.getString("username"));
            contact.setRealName(contactElement.getString("realname"));
            contact.setFriend("1".equals(contactElement.getString("friend")));
            contact.setFamily("1".equals(contactElement.getString("family")));
            if (contactElement.has("ignored")) {
            	contact.setIgnored("1".equals(contactElement.getString("ignored")));
            }
            contact.setIconFarm(contactElement.getString("iconfarm"));
            contact.setIconServer(contactElement.getString("iconserver"));
            contacts.add(contact);
        }
        return contacts;
    }

    /**
     * Get the collection of public contacts for the specified user ID.
     *
     * This method does not require authentication.
     *
     * @param userId The user ID
     * @return The Collection of Contact objects
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    public Collection<Contact> getPublicList(String userId) throws IOException, FlickrException, InvalidKeyException, NoSuchAlgorithmException, JSONException {
        List<Contact> contacts = new ArrayList<Contact>();

        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PUBLIC_LIST));
        parameters.add(new Parameter("api_key", apiKey));
        parameters.add(new Parameter("user_id", userId));

        Response response = transportAPI.postJSON(apiKey, sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONObject contactsElement = response.getData().getJSONObject("contacts");
        JSONArray contactNodes = contactsElement.getJSONArray("contact");
        for (int i = 0; i < contactNodes.length(); i++) {
        	JSONObject contactElement = contactNodes.getJSONObject(i);
            Contact contact = new Contact();
            contact.setId(contactElement.getString("nsid"));
            contact.setUsername(contactElement.getString("username"));
            contact.setIgnored("1".equals(contactElement.getString("ignored")));
            contact.setIconFarm(contactElement.getString("iconfarm"));
            contact.setIconServer(contactElement.getString("iconserver"));
            contacts.add(contact);
        }
        return contacts;
    }

}
