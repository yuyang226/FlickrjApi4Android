/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickrjandroid.people;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.Parameter;
import com.googlecode.flickrjandroid.Response;
import com.googlecode.flickrjandroid.Transport;
import com.googlecode.flickrjandroid.groups.Group;
import com.googlecode.flickrjandroid.oauth.OAuthInterface;
import com.googlecode.flickrjandroid.oauth.OAuthUtils;
import com.googlecode.flickrjandroid.photos.Extras;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotoUtils;
import com.googlecode.flickrjandroid.util.JSONUtils;
import com.googlecode.flickrjandroid.util.StringUtilities;

/**
 * Interface for finding Flickr users.
 * 
 * @author Anthony Eden
 * @version $Id: PeopleInterface.java,v 1.28 2010/09/12 20:13:57 x-mago Exp $
 */
public class PeopleInterface {

    public static final String METHOD_FIND_BY_EMAIL = "flickr.people.findByEmail";
    public static final String METHOD_FIND_BY_USERNAME = "flickr.people.findByUsername";
    public static final String METHOD_GET_INFO = "flickr.people.getInfo";
    public static final String METHOD_GET_ONLINE_LIST = "flickr.people.getOnlineList";
    public static final String METHOD_GET_PUBLIC_GROUPS = "flickr.people.getPublicGroups";
    public static final String METHOD_GET_PUBLIC_PHOTOS = "flickr.people.getPublicPhotos";
    public static final String METHOD_GET_UPLOAD_STATUS = "flickr.people.getUploadStatus";
    public static final String METHOD_GET_PHOTOS = "flickr.people.getPhotos";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public PeopleInterface(
        String apiKey,
        String sharedSecret,
        Transport transportAPI
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Find the user by their email address.
     *
     * This method does not require authentication.
     *
     * @param email The email address
     * @return The User
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public User findByEmail(String email) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_FIND_BY_EMAIL));
        parameters.add(new Parameter("api_key", apiKey));
        parameters.add(new Parameter("find_email", email));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return createUser(response.getData());
    }

    /**
     * Find a User by the username.
     *
     * This method does not require authentication.
     *
     * @param username The username
     * @return The User object
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public User findByUsername(String username) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_FIND_BY_USERNAME));
        parameters.add(new Parameter("api_key", apiKey));
        parameters.add(new Parameter("username", username));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return createUser(response.getData());
    }
    
    private User createUser(JSONObject rootObject) throws JSONException {
        JSONObject userElement = rootObject.getJSONObject("user");
        User user = new User();
        user.setId(userElement.getString("nsid"));
        user.setUsername(JSONUtils.getChildValue(userElement, "username"));
        return user;
    }

    /**
     * Get info about the specified user.
     *
     * This method does not require authentication.
     *
     * @param userId The user ID
     * @return The User object
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public User getInfo(String userId) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_INFO));
        parameters.add(new Parameter("user_id", userId));
        parameters.add(new Parameter("api_key", apiKey));
        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject userElement = response.getData().getJSONObject("person");
        User user = new User();
        user.setId(userElement.getString("nsid"));
        user.setPro("1".equals(userElement.getString("ispro")));
        user.setIconFarm(userElement.getString("iconfarm"));
        user.setIconServer(userElement.getString("iconserver"));
        user.setUsername(JSONUtils.getChildValue(userElement, "username"));
        user.setRealName(JSONUtils.getChildValue(userElement, "realname"));
        user.setLocation(JSONUtils.getChildValue(userElement, "location"));
        user.setPathAlias(userElement.getString("path_alias"));
        user.setMbox_sha1sum(JSONUtils.getChildValue(userElement, "mbox_sha1sum"));
        user.setPhotosurl(JSONUtils.getChildValue(userElement, "photosurl"));
        user.setProfileurl(JSONUtils.getChildValue(userElement, "profileurl"));
        user.setMobileurl(JSONUtils.getChildValue(userElement, "mobileurl"));

        JSONObject photosElement = userElement.getJSONObject("photos");
        user.setPhotosFirstDate(JSONUtils.getChildValue(photosElement, "firstdate"));
        user.setPhotosFirstDateTaken(JSONUtils.getChildValue(photosElement, "firstdatetaken"));
        user.setPhotosCount(JSONUtils.getChildValue(photosElement, "count"));

        return user;
    }

    /**
     * Get a collection of public groups for the user.
     *
     * The groups will contain only the members nsid, name, admin and eighteenplus.
     * If you want the whole group-information, you have to call 
     * {@link com.googlecode.flickrjandroid.groups.GroupsInterface#getInfo(String)}.
     *
     * This method does not require authentication.
     *
     * @param userId The user ID
     * @return The public groups
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Collection<Group> getPublicGroups(String userId)
      throws IOException, FlickrException, JSONException {
        List<Group> groups = new ArrayList<Group>();

        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PUBLIC_GROUPS));
        parameters.add(new Parameter("api_key", apiKey));
        parameters.add(new Parameter("user_id", userId));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject groupsElement = response.getData().getJSONObject("groups");
        JSONArray groupNodes = groupsElement.getJSONArray("group");
        for (int i = 0; i < groupNodes.length(); i++) {
            JSONObject groupElement = groupNodes.getJSONObject(i);
            Group group = new Group();
            group.setId(groupElement.getString("nsid"));
            group.setName(groupElement.getString("name"));
            group.setAdmin("1".equals(groupElement.getString("admin")));
            group.setEighteenPlus("1".equals(groupElement.getString("eighteenplus")));
            group.setInvitationOnly("1".equals(groupElement.getString("invitation_only")));
            groups.add(group);
        }
        return groups;
    }

    public PhotoList getPublicPhotos(String userId, int perPage, int page)
    throws IOException, FlickrException, JSONException {
        return getPublicPhotos(userId, Extras.MIN_EXTRAS, perPage, page);
    }

    /**
     * Get a collection of public photos for the specified user ID.
     *
     * This method does not require authentication.
     *
     * @see com.googlecode.flickrjandroid.photos.Extras
     * @param userId The User ID
     * @param extras Set of extra-attributes to include (may be null)
     * @param perPage The number of photos per page
     * @param page The page offset
     * @return The PhotoList collection
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public PhotoList getPublicPhotos(String userId, Set<String> extras, int perPage, int page) 
    throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PUBLIC_PHOTOS));
        parameters.add(new Parameter("api_key", apiKey));
        parameters.add(new Parameter("user_id", userId));

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", "" + perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", "" + page));
        }

        if (extras != null) {
            parameters.add(new Parameter(Extras.KEY_EXTRAS, StringUtilities.join(extras, ",")));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return PhotoUtils.createPhotoList(response.getData());
    }

    /**
     * Get upload status for the currently authenticated user.
     *
     * Requires authentication with 'read' permission using the new authentication API.
     *
     * @return A User object with upload status data fields filled
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public User getUploadStatus() throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_UPLOAD_STATUS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        OAuthUtils.addOAuthToken(parameters);
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject userElement = response.getData().getJSONObject("user");
        User user = new User();
        user.setId(userElement.getString("id"));
        user.setPro("1".equals(userElement.getString("ispro")));
        user.setUsername(JSONUtils.getChildValue(userElement, "username"));

        JSONObject bandwidthElement = userElement.getJSONObject("bandwidth");
        Bandwidth bandwidth = new Bandwidth();
        bandwidth.setMax(bandwidthElement.getLong("max"));
        bandwidth.setUsed(bandwidthElement.getLong("used"));
        bandwidth.setUsed(bandwidthElement.getLong("used"));
        bandwidth.setMaxBytes(bandwidthElement.getLong("maxbytes"));
        bandwidth.setUsedBytes(bandwidthElement.getLong("usedbytes"));
        bandwidth.setRemainingBytes(bandwidthElement.getLong("remainingbytes"));
        bandwidth.setMaxKb(bandwidthElement.getLong("maxkb"));
        bandwidth.setUsedKb(bandwidthElement.getLong("usedkb"));
        bandwidth.setRemainingKb(bandwidthElement.getLong("remainingkb"));
        bandwidth.setUnlimited("1".equals(bandwidthElement.getString("unlimited")));
        user.setBandwidth(bandwidth);
        
        JSONObject filesizeElement = userElement.getJSONObject("filesize");
        user.setFilesizeMax(filesizeElement.getString("max"));

        return user;
    }

    /**
     * Returns photos from the given user's photostream. Only photos visible the
     * calling user will be returned. this method must be authenticated.
     * 
     * @param userId
     * @param extras
     * @param perpage
     * @param page
     * @return
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public PhotoList getPhotos(String userId, Set<String> extras, int perPage,
            int page) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PHOTOS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        parameters.add(new Parameter("user_id", userId));

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", "" + perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", "" + page));
        }

        if (extras != null) {
            parameters.add(new Parameter(Extras.KEY_EXTRAS, StringUtilities
                    .join(extras, ",")));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI
                .postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response
                    .getErrorMessage());
        }
        return PhotoUtils.createPhotoList(response.getData());
    }
}
