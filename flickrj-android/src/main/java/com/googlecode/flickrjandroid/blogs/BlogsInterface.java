/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickrjandroid.blogs;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

/**
 * Interface for working with Flickr blog configurations.
 *
 * @author Anthony Eden
 * @version $Id: BlogsInterface.java,v 1.14 2009/07/11 20:30:27 x-mago Exp $
 */
public class BlogsInterface {

    private static final String METHOD_GET_SERVICES = "flickr.blogs.getServices";
    private static final String METHOD_GET_LIST = "flickr.blogs.getList";
    private static final String METHOD_POST_PHOTO = "flickr.blogs.postPhoto";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public BlogsInterface(String apiKey, String sharedSecret, Transport transport) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transport;
    }

    /**
     * Return a list of Flickr supported blogging services.
     *
     * This method does not require authentication.
     *
     * @return List of Services
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Collection<Service> getServices()
      throws IOException, FlickrException, JSONException {
        List<Service> list = new ArrayList<Service>();
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_SERVICES));
        parameters.add(new Parameter("api_key", apiKey));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        
        JSONObject servicesElement = response.getData().getJSONObject("services");
        JSONArray serviceNodes = servicesElement.getJSONArray("service");
        for (int i = 0; i < serviceNodes.length(); i++) {
            JSONObject serviceElement = serviceNodes.getJSONObject(i);
            Service srv = new Service();
            srv.setId(serviceElement.getString("id"));
            srv.setName(serviceElement.getString("_content"));
            list.add(srv);
        }
        return list;
    }

    /**
     * Post the specified photo to a blog.  Note that the Photo.title and Photo.description are used for the blog entry
     * title and body respectively.
     *
     * @param photo The photo metadata
     * @param blogId The blog ID
     * @param blogPassword The blog password
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    public void postPhoto(Photo photo, String blogId, String blogPassword) throws IOException, FlickrException, InvalidKeyException, NoSuchAlgorithmException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_POST_PHOTO));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        parameters.add(new Parameter("blog_id", blogId));
        parameters.add(new Parameter("photo_id", photo.getId()));
        parameters.add(new Parameter("title", photo.getTitle()));
        parameters.add(new Parameter("description", photo.getDescription()));
        if (blogPassword != null) {
            parameters.add(new Parameter("blog_password", blogPassword));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.postJSON(this.sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Post the specified photo to a blog.
     *
     * @param photo The photo metadata
     * @param blogId The blog ID
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    public void postPhoto(Photo photo, String blogId) throws IOException, FlickrException, InvalidKeyException, NoSuchAlgorithmException, JSONException {
        postPhoto(photo, blogId, null);
    }

    /**
     * Get the collection of configured blogs for the calling user.
     *
     * @return The Collection of configured blogs
     * @throws IOException
     * @throws JSONException 
     */
    public Collection<Blog> getList() throws IOException, FlickrException, JSONException {
        List<Blog> blogs = new ArrayList<Blog>();
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_LIST));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        OAuthUtils.addOAuthToken(parameters);
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONObject blogsElement = response.getData().getJSONObject("blogs");
        JSONArray blogNodes = blogsElement.getJSONArray("blog");
        for (int i = 0; i < blogNodes.length(); i++) {
            JSONObject blogElement = blogNodes.getJSONObject(i);
            Blog blog = new Blog();
            blog.setId(blogElement.getString("id"));
            blog.setName(blogElement.getString("name"));
            blog.setNeedPassword("1".equals(blogElement.getString("needspassword")));
            blog.setUrl(blogElement.getString("url"));
            blogs.add(blog);
        }
        return blogs;
    }
}
