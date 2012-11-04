/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickrjandroid.tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.Parameter;
import com.googlecode.flickrjandroid.Response;
import com.googlecode.flickrjandroid.Transport;
import com.googlecode.flickrjandroid.oauth.OAuthInterface;
import com.googlecode.flickrjandroid.oauth.OAuthUtils;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotoUtils;

/**
 * Interface for working with Flickr tags.
 *
 * @author Anthony Eden
 * @version $Id: TagsInterface.java,v 1.19 2009/07/02 21:52:35 x-mago Exp $
 */
public class TagsInterface {

    public static final String METHOD_GET_CLUSTERS = "flickr.tags.getClusters";
    public static final String METHOD_GET_HOT_LIST = "flickr.tags.getHotList";
    public static final String METHOD_GET_LIST_PHOTO = "flickr.tags.getListPhoto";
    public static final String METHOD_GET_LIST_USER = "flickr.tags.getListUser";
    public static final String METHOD_GET_LIST_USER_POPULAR = "flickr.tags.getListUserPopular";
    public static final String METHOD_GET_LIST_USER_RAW = "flickr.tags.getListUserRaw";
    public static final String METHOD_GET_RELATED = "flickr.tags.getRelated";
    public static final String METHOD_GET_CLUSTER_PHOTOS = "flickr.tags.getClusterPhotos";

    public static final String PERIOD_WEEK = "week";
    public static final String PERIOD_DAY = "day";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    /**
     * Construct a TagsInterface.
     *
     * @param apiKey The API key
     * @param transportAPI The Transport interface
     */
    public TagsInterface(
        String apiKey,
        String sharedSecret,
        Transport transportAPI
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Search for tag-clusters.<p/>
     *
     * <p>This method does not require authentication.</p>
     *
     * @since 1.2
     * @param searchTag
     * @return a list of clusters
     * @throws JSONException 
     */
    public ClusterList getClusters(String searchTag)
      throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_CLUSTERS));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("tag", searchTag));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        ClusterList clusters = new ClusterList();
        JSONObject clustersElement = response.getData().getJSONObject("clusters");
        JSONArray clusterElements = clustersElement.optJSONArray("cluster");
        for (int i = 0; clusterElements != null && i < clusterElements.length(); i++) {
            Cluster cluster = new Cluster();
            JSONObject clusterElement = clusterElements.getJSONObject(i);
            JSONArray tagElements = clusterElement.optJSONArray("tag");
            for (int j = 0; tagElements != null && j < tagElements.length(); j++) {
                Tag tag = new Tag();
                tag.setValue(tagElements.getJSONObject(j).getString("_content"));
                cluster.addTag(tag);
            }
            clusters.addCluster(cluster);
        }
        return clusters;
    }

    /**
     * Returns the first 24 photos for a given tag cluster.
     *
     * <p>This method does not require authentication.</p>
     *
     * @param tag
     * @param clusterId
     * @return PhotoList
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public PhotoList getClusterPhotos(String tag, String clusterId)
      throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_CLUSTER_PHOTOS));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("tag", tag));
        parameters.add(new Parameter("cluster_id", clusterId));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return PhotoUtils.createPhotoList(response.getData());
    }

    /**
     * Returns a list of hot tags for the given period.
     *
     * <p>This method does not require authentication.</p>
     *
     * @param period valid values are 'day' or 'week'
     * @param count maximum is 200
     * @return The collection of HotlistTag objects
     * @throws JSONException 
     */
    public Collection<HotlistTag> getHotList(String period, int count) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_HOT_LIST));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("period", period));
        parameters.add(new Parameter("count", "" + count));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONObject tagsElement = response.getData().getJSONObject("hottags");

        List<HotlistTag> tags = new ArrayList<HotlistTag>();
        JSONArray tagElements = tagsElement.optJSONArray("tag");
        for (int i = 0; tagElements != null && i < tagElements.length(); i++) {
            JSONObject tagElement = tagElements.getJSONObject(i);
            HotlistTag tag = new HotlistTag();
            tag.setScore(tagElement.getString("score"));
            tag.setValue(tagElement.getString("_content"));
            tags.add(tag);
        }
        return tags;
    }

    /**
     * Get a list of tags for the specified photo.
     *
     * <p>This method does not require authentication.</p>
     *
     * @param photoId The photo ID
     * @return The collection of Tag objects
     * @throws JSONException 
     */
    public Photo getListPhoto(String photoId) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_LIST_PHOTO));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONObject photoElement = response.getData().getJSONObject("photo");
        Photo photo = new Photo();
        photo.setId(photoElement.getString("id"));

        List<Tag> tags = new ArrayList<Tag>();
        JSONObject tagsElement = photoElement.getJSONObject("tags");
        JSONArray tagElements = tagsElement.optJSONArray("tag");
        for (int i = 0; tagElements != null && i < tagElements.length(); i++) {
            JSONObject tagElement = tagElements.getJSONObject(i);
            Tag tag = new Tag();
            tag.setId(tagElement.getString("id"));
            tag.setAuthor(tagElement.getString("author"));
            tag.setAuthorName(tagElement.getString("authorname"));
            tag.setRaw(tagElement.getString("raw"));
            tag.setValue(tagElement.getString("_content"));
            tags.add(tag);
        }
        photo.setTags(tags);
        return photo;
    }

    /**
     * Get a collection of tags used by the specified user.
     *
     * <p>This method does not require authentication.</p>
     *
     * @param userId The User ID
     * @return The User object
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Collection<Tag> getListUser(String userId) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_LIST_USER));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("user_id", userId));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONObject whoElement = response.getData().getJSONObject("who");

        List<Tag> tags = new ArrayList<Tag>();
        JSONObject tagsElement = whoElement.getJSONObject("tags");
        JSONArray tagElements = tagsElement.optJSONArray("tag");
        for (int i = 0; tagElements != null && i < tagElements.length(); i++) {
            JSONObject tagElement = tagElements.getJSONObject(i);
            Tag tag = new Tag();
            tag.setCount(tagElement.optInt("count"));
            tag.setValue(tagElement.getString("_content"));
            tags.add(tag);
        }
        return tags;
    }

    /**
     * Get a list of the user's popular tags.
     *
     * <p>This method does not require authentication.</p>
     *
     * @param userId The user ID
     * @return The collection of Tag objects
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Collection<Tag> getListUserPopular(String userId) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_LIST_USER_POPULAR));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("user_id", userId));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONObject whoElement = response.getData().getJSONObject("who");

        List<Tag> tags = new ArrayList<Tag>();
        JSONObject tagsElement = whoElement.getJSONObject("tags");
        JSONArray tagElements = tagsElement.optJSONArray("tag");
        for (int i = 0; tagElements != null && i < tagElements.length(); i++) {
            JSONObject tagElement = tagElements.getJSONObject(i);
            Tag tag = new Tag();
            tag.setCount(tagElement.getString("count"));
            tag.setValue(tagElement.getString("_content"));
            tags.add(tag);
        }
        return tags;
    }

    /**
     * Get a list of the user's (identified by token) popular tags.
     *
     * <p>This method does not require authentication.</p>
     *
     * @param tagVal a tag to search for, or null
     * @return The collection of Tag objects
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Collection<TagRaw> getListUserRaw(String tagVal) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_LIST_USER_RAW));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        if (tagVal != null) {
            parameters.add(new Parameter("tag", tagVal));
        }
        OAuthUtils.addOAuthToken(parameters);
        
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONObject whoElement = response.getData().getJSONObject("who");

        List<TagRaw> tags = new ArrayList<TagRaw>();
        JSONObject tagsElement = whoElement.getJSONObject("tags");
        JSONArray tagElements = tagsElement.optJSONArray("tag");
        for (int i = 0; tagElements != null && i < tagElements.length(); i++) {
            JSONObject tagElement = tagElements.getJSONObject(i);
            TagRaw tag = new TagRaw();
            tag.setClean(tagElement.getString("clean"));
            JSONArray rawElements = tagElement.optJSONArray("raw");
            for (int j = 0; rawElements != null && j < rawElements.length(); j++) {
                JSONObject rawElement = rawElements.getJSONObject(j);
                tag.addRaw(rawElement.getString("_content"));
            }
            tags.add(tag);
        }
        return tags;
    }

    /**
     * Get the related tags.
     *
     * <p>This method does not require authentication.</p>
     *
     * @param tag The source tag
     * @return A RelatedTagsList object
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public RelatedTagsList getRelated(String tag) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_RELATED));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("tag", tag));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONObject tagsElement = response.getData().getJSONObject("tags");
        RelatedTagsList tags = new RelatedTagsList();
        tags.setSource(tagsElement.getString("source"));
        JSONArray tagElements = tagsElement.optJSONArray("tag");
        for (int i = 0; tagElements != null && i < tagElements.length(); i++) {
            JSONObject tagElement = tagElements.getJSONObject(i);
            Tag t = new Tag();
            t.setValue(tagElement.getString("_content"));
            tags.add(t);
        }
        return tags;
    }

}
