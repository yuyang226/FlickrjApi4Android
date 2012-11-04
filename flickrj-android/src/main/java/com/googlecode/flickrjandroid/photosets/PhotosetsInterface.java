/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickrjandroid.photosets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.Parameter;
import com.googlecode.flickrjandroid.Response;
import com.googlecode.flickrjandroid.Transport;
import com.googlecode.flickrjandroid.oauth.OAuthInterface;
import com.googlecode.flickrjandroid.oauth.OAuthUtils;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.Extras;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoContext;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotoUtils;
import com.googlecode.flickrjandroid.util.JSONUtils;
import com.googlecode.flickrjandroid.util.StringUtilities;

/**
 * Interface for working with photosets.
 *
 * @author Anthony Eden
 * @version $Id: PhotosetsInterface.java,v 1.27 2009/11/08 21:58:00 x-mago Exp $
 */
public class PhotosetsInterface {

    public static final String METHOD_ADD_PHOTO = "flickr.photosets.addPhoto";
    public static final String METHOD_CREATE = "flickr.photosets.create";
    public static final String METHOD_DELETE = "flickr.photosets.delete";
    public static final String METHOD_EDIT_META = "flickr.photosets.editMeta";
    public static final String METHOD_EDIT_PHOTOS = "flickr.photosets.editPhotos";
    public static final String METHOD_GET_CONTEXT = "flickr.photosets.getContext";
    public static final String METHOD_GET_INFO = "flickr.photosets.getInfo";
    public static final String METHOD_GET_LIST = "flickr.photosets.getList";
    public static final String METHOD_GET_PHOTOS = "flickr.photosets.getPhotos";
    public static final String METHOD_ORDER_SETS = "flickr.photosets.orderSets";
    public static final String METHOD_REMOVE_PHOTO = "flickr.photosets.removePhoto";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public PhotosetsInterface(
        String apiKey,
        String sharedSecret,
        Transport transportAPI
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Add a photo to the end of the photoset.
     * <p/>
     * Note: requires authentication with the new authentication API with 'write' permission.
     *
     * @param photosetId The photoset ID
     * @param photoId The photo ID
     * @throws JSONException 
     */
    public void addPhoto(String photosetId, String photoId) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_ADD_PHOTO));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photoset_id", photosetId));
        parameters.add(new Parameter("photo_id", photoId));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Create a new photoset.
     *
     * @param title The photoset title
     * @param description The photoset description
     * @param primaryPhotoId The primary photo id
     * @return The new Photset
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Photoset create(String title, String description, String primaryPhotoId)
            throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_CREATE));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("title", title));
        parameters.add(new Parameter("description", description));
        parameters.add(new Parameter("primary_photo_id", primaryPhotoId));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject photosetElement = response.getData().getJSONObject("photoset");
        Photoset photoset = new Photoset();
        photoset.setId(photosetElement.getString("id"));
        photoset.setUrl(photosetElement.getString("url"));
        return photoset;
    }

    /**
     * Delete the specified photoset.
     *
     * @param photosetId The photoset ID
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void delete(String photosetId) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_DELETE));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photoset_id", photosetId));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Modify the meta-data for a photoset.
     *
     * @param photosetId The photoset ID
     * @param title A new title
     * @param description A new description (can be null)
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void editMeta(String photosetId, String title, String description)
            throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_EDIT_META));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photoset_id", photosetId));
        parameters.add(new Parameter("title", title));
        if (description != null) {
            parameters.add(new Parameter("description", description));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Edit which photos are in the photoset.
     *
     * @param photosetId The photoset ID
     * @param primaryPhotoId The primary photo Id
     * @param photoIds The photo IDs for the photos in the set
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void editPhotos(String photosetId, String primaryPhotoId, String[] photoIds)
            throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_EDIT_PHOTOS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photoset_id", photosetId));
        parameters.add(new Parameter("primary_photo_id", primaryPhotoId));
        parameters.add(new Parameter("photo_ids", StringUtilities.join(photoIds, ",")));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Get a photo's context in the specified photo set.
     *
     * This method does not require authentication.
     *
     * @param photoId The photo ID
     * @param photosetId The photoset ID
     * @return The PhotoContext
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public PhotoContext getContext(String photoId, String photosetId)
            throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_CONTEXT));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("photoset_id", photosetId));

        //the Flickr API page says this method does not require OAuth
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject payload = response.getData();
        Iterator<?> keys = payload.keys();
        PhotoContext photoContext = new PhotoContext();
        while (keys.hasNext()) {
            String key = String.valueOf(keys.next());
            JSONObject element = payload.optJSONObject(key);
            if (key.equals("prevphoto")) {
                String id = element.getString("id");
                if ("0".equals(id) == false) {
                    //this is the first photo
                    Photo photo = new Photo();
                    photo.setId(id);
                    photo.setTitle(element.optString("title"));
                    photo.setUrl(element.optString("url"));
                    photoContext.setPreviousPhoto(photo);
                }
            } else if (key.equals("nextphoto")) {
                String id = element.getString("id");
                if ("0".equals(id) == false) {
                    //this is the last photo
                    Photo photo = new Photo();
                    photo.setId(id);
                    photo.setTitle(element.optString("title"));
                    photo.setUrl(element.optString("url"));
                    photoContext.setNextPhoto(photo);
                }
            } else if (key.equals("count")) {
                // TODO: process this information
            } else {
                System.err.println("unsupported element name: " + key);
            }
        }
        return photoContext;
    }

    /**
     * Get the information for a specified photoset.
     *
     * This method does not require authentication.
     *
     * @param photosetId The photoset ID
     * @return The Photoset
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public Photoset getInfo(String photosetId) throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_INFO));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photoset_id", photosetId));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject photosetElement = response.getData().getJSONObject("photoset");
        Photoset photoset = new Photoset();
        photoset.setId(photosetElement.getString("id"));

        User owner = new User();
        owner.setId(photosetElement.getString("owner"));
        photoset.setOwner(owner);

        Photo primaryPhoto = new Photo();
        primaryPhoto.setId(photosetElement.getString("primary"));
        primaryPhoto.setSecret(photosetElement.getString("secret")); // TODO verify that this is the secret for the photo
        primaryPhoto.setServer(photosetElement.getString("server")); // TODO verify that this is the server for the photo
        primaryPhoto.setFarm(photosetElement.getString("farm"));
        photoset.setPrimaryPhoto(primaryPhoto);

        // TODO remove secret/server/farm from photoset?
        // It's rather related to the primaryPhoto, then to the photoset itself.
        photoset.setSecret(photosetElement.getString("secret"));
        photoset.setServer(photosetElement.getString("server"));
        photoset.setFarm(photosetElement.getString("farm"));
        photoset.setPhotoCount(photosetElement.getString("photos"));

        photoset.setTitle(JSONUtils.getChildValue(photosetElement, "title"));
        photoset.setDescription(JSONUtils.getChildValue(photosetElement, "description"));
        photoset.setPrimaryPhoto(primaryPhoto);

        return photoset;
    }

    /**
     * Get a list of all photosets for the specified user.
     *
     * This method does not require authentication.
     * But to get a Photoset into the list, that contains just private photos,
     * the call needs to be authenticated.
     *
     * @param userId The User id
     * @return The Photosets collection
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Photosets getList(String userId) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_LIST));
        
        boolean signed = OAuthUtils.hasSigned();
        if (signed) {
            parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        } else {
            parameters.add(new Parameter("api_key", apiKey));
        }

        if (userId != null) {
            parameters.add(new Parameter("user_id", userId));
        }
        
        if (signed) {
            OAuthUtils.addOAuthToken(parameters);
        }

        Response response = signed ? transportAPI.postJSON(sharedSecret, parameters) : transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Photosets photosetsObject = new Photosets();
        JSONObject photosetsElement = response.getData().getJSONObject("photosets");
        List<Photoset> photosets = new ArrayList<Photoset>();
        JSONArray photosetElements = photosetsElement.optJSONArray("photoset");
        for (int i = 0; photosetElements != null && i < photosetElements.length(); i++) {
            JSONObject photosetElement = photosetElements.getJSONObject(i);
            Photoset photoset = new Photoset();
            photoset.setId(photosetElement.getString("id"));

            if (photosetElement.has("owner")) {
                User owner = new User();
                owner.setId(photosetElement.getString("owner"));
                photoset.setOwner(owner);
            }

            Photo primaryPhoto = new Photo();
            primaryPhoto.setId(photosetElement.getString("primary"));
            primaryPhoto.setSecret(photosetElement.getString("secret")); // TODO verify that this is the secret for the photo
            primaryPhoto.setServer(photosetElement.getString("server")); // TODO verify that this is the server for the photo
            primaryPhoto.setFarm(photosetElement.getString("farm"));
            photoset.setPrimaryPhoto(primaryPhoto);

            photoset.setSecret(photosetElement.getString("secret"));
            photoset.setServer(photosetElement.getString("server"));
            photoset.setFarm(photosetElement.getString("farm"));
            photoset.setPhotoCount(photosetElement.getString("photos"));

            photoset.setTitle(JSONUtils.getChildValue(photosetElement, "title"));
            photoset.setDescription(JSONUtils.getChildValue(photosetElement, "description"));

            photosets.add(photoset);
        }

        photosetsObject.setPhotosets(photosets);
        return photosetsObject;
    }

    /**
     * Get a collection of Photo objects for the specified Photoset.
     * 
     * This method does not require authentication.
     *
     * @see com.googlecode.flickrjandroid.photos.Extras
     * @see com.googlecode.flickrjandroid.Flickr#PRIVACY_LEVEL_NO_FILTER
     * @see com.googlecode.flickrjandroid.Flickr#PRIVACY_LEVEL_PUBLIC
     * @see com.googlecode.flickrjandroid.Flickr#PRIVACY_LEVEL_FRIENDS
     * @see com.googlecode.flickrjandroid.Flickr#PRIVACY_LEVEL_FRIENDS_FAMILY
     * @see com.googlecode.flickrjandroid.Flickr#PRIVACY_LEVEL_FAMILY
     * @see com.googlecode.flickrjandroid.Flickr#PRIVACY_LEVEL_FRIENDS
     * @param photosetId The photoset ID
     * @param extras Set of extra-fields
     * @param privacy_filter filter value for authenticated calls
     * @param perPage The number of photos per page
     * @param page The page offset
     * @return PhotoList The Collection of Photo objects
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public PhotoList getPhotos(String photosetId, Set<String> extras,
      int privacy_filter, int perPage, int page)
      throws IOException, FlickrException, JSONException {
        PhotoList photos = new PhotoList();
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PHOTOS));
        boolean signed = OAuthUtils.hasSigned();
        if (signed) {
            parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        } else {
            parameters.add(new Parameter("api_key", apiKey));
        }

        parameters.add(new Parameter("photoset_id", photosetId));

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", new Integer(perPage)));
        }

        if (page > 0) {
            parameters.add(new Parameter("page", new Integer(page)));
        }

        if (privacy_filter > 0) {
            parameters.add(new Parameter("privacy_filter", "" + privacy_filter));
        }

        if (extras != null && !extras.isEmpty()) {
            parameters.add(new Parameter(Extras.KEY_EXTRAS, StringUtilities.join(extras, ",")));
        }
        if (signed) {
            OAuthUtils.addOAuthToken(parameters);
        }

        Response response = signed ? transportAPI.postJSON(sharedSecret, parameters) : transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONObject photoset = response.getData().getJSONObject("photoset");
        JSONArray photoElements = photoset.optJSONArray("photo");
        photos.setPage(photoset.getString("page"));
        photos.setPages(photoset.getString("pages"));
        photos.setPerPage(photoset.getString("per_page"));
        photos.setTotal(photoset.getString("total"));

        for (int i = 0; photoElements != null && i < photoElements.length(); i++) {
            JSONObject photoElement = photoElements.getJSONObject(i);
            photos.add(PhotoUtils.createPhoto(photoElement));
        }

        return photos;
    }

    /**
     * Convenience method.
     *
     * Calls getPhotos() with Extras.MIN_EXTRAS and Flickr.PRIVACY_LEVEL_NO_FILTER.
     *
     * This method does not require authentication.
     *
     * @see com.googlecode.flickrjandroid.photos.Extras
     * @see com.googlecode.flickrjandroid.Flickr#PRIVACY_LEVEL_NO_FILTER
     * @see com.googlecode.flickrjandroid.Flickr#PRIVACY_LEVEL_PUBLIC
     * @see com.googlecode.flickrjandroid.Flickr#PRIVACY_LEVEL_FRIENDS
     * @see com.googlecode.flickrjandroid.Flickr#PRIVACY_LEVEL_FRIENDS_FAMILY
     * @see com.googlecode.flickrjandroid.Flickr#PRIVACY_LEVEL_FAMILY
     * @see com.googlecode.flickrjandroid.Flickr#PRIVACY_LEVEL_FRIENDS
     * @param photosetId The photoset ID
     * @param perPage The number of photos per page
     * @param page The page offset
     * @return PhotoList The Collection of Photo objects
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public PhotoList getPhotos(String photosetId, int perPage, int page) 
      throws IOException, FlickrException, JSONException {
        return getPhotos(photosetId, Extras.MIN_EXTRAS, Flickr.PRIVACY_LEVEL_NO_FILTER, perPage, page);
    }

    /**
     * Set the order in which sets are returned for the user.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param photosetIds An array of Ids
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void orderSets(String[] photosetIds) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_ORDER_SETS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photoset_ids", StringUtilities.join(photosetIds, ",")));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Remove a photo from the set.
     *
     * @param photosetId The photoset ID
     * @param photoId The photo ID
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void removePhoto(String photosetId, String photoId) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_REMOVE_PHOTO));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photoset_id", photosetId));
        parameters.add(new Parameter("photo_id", photoId));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

}
