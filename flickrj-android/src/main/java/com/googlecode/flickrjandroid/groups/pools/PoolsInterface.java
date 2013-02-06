/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickrjandroid.groups.pools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
import com.googlecode.flickrjandroid.photos.PhotoContext;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotoUtils;
import com.googlecode.flickrjandroid.util.StringUtilities;

/**
 * @author Anthony Eden
 * @version $Id: PoolsInterface.java,v 1.15 2009/07/11 20:30:27 x-mago Exp $
 */
public class PoolsInterface {

	public static final String METHOD_ADD = "flickr.groups.pools.add";
	public static final String METHOD_GET_CONTEXT = "flickr.groups.pools.getContext";
	public static final String METHOD_GET_GROUPS = "flickr.groups.pools.getGroups";
	public static final String METHOD_GET_PHOTOS = "flickr.groups.pools.getPhotos";
	public static final String METHOD_REMOVE = "flickr.groups.pools.remove";

	private String apiKey;
	private String sharedSecret;
	private Transport transport;

	public PoolsInterface(String apiKey, String sharedSecret,
			Transport transport) {
		this.apiKey = apiKey;
		this.sharedSecret = sharedSecret;
		this.transport = transport;
	}

	/**
	 * Add a photo to a group's pool.
	 * 
	 * @param photoId
	 *            The photo ID
	 * @param groupId
	 *            The group ID
	 * @throws JSONException
	 */
	public void add(String photoId, String groupId) throws IOException,
			FlickrException, JSONException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("method", METHOD_ADD));
		parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY,
				apiKey));
		parameters.add(new Parameter("photo_id", photoId));
		parameters.add(new Parameter("group_id", groupId));
		OAuthUtils.addOAuthToken(parameters);
		Response response = transport.postJSON(sharedSecret, parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
	}

	/**
	 * Get the context for a photo in the group pool.
	 * 
	 * This method does not require authentication.
	 * 
	 * @param photoId
	 *            The photo ID
	 * @param groupId
	 *            The group ID
	 * @return The PhotoContext
	 * @throws IOException
	 * @throws FlickrException
	 * @throws JSONException
	 */
	public PhotoContext getContext(String photoId, String groupId)
			throws IOException, FlickrException, JSONException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("method", METHOD_GET_GROUPS));
		parameters.add(new Parameter("api_key", apiKey));

		parameters.add(new Parameter("photo_id", photoId));
		parameters.add(new Parameter("group_id", groupId));

		Response response = transport.get(transport.getPath(), parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
		/*
		 * JSONArray payload = response.getData(); Iterator<Element> iter =
		 * payload.iterator();
		 */
		PhotoContext photoContext = new PhotoContext();
		/*
		 * while (iter.hasNext()) { Element element = (Element) iter.next();
		 * String elementName = element.getTagName(); if
		 * (elementName.equals("prevphoto")) { Photo photo = new Photo();
		 * photo.setId(element.getAttribute("id"));
		 * photoContext.setPreviousPhoto(photo); } else if
		 * (elementName.equals("nextphoto")) { Photo photo = new Photo();
		 * photo.setId(element.getAttribute("id"));
		 * photoContext.setNextPhoto(photo); } else {
		 * System.err.println("unsupported element name: " + elementName); } }
		 */
		return photoContext;
	}

	/**
	 * Get a collection of all of the user's groups.
	 * 
	 * @return A Collection of Group objects
	 * @throws IOException
	 * @throws FlickrException
	 * @throws JSONException
	 */
	public Collection<Group> getGroups() throws IOException, FlickrException,
			JSONException {
		return getGroups(-1, -1);
	}

	public Collection<Group> getGroups(int perPage, int page)
			throws IOException, FlickrException, JSONException {
		List<Group> groups = new ArrayList<Group>();

		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("method", METHOD_GET_GROUPS));
		parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY,
				apiKey));
		if (perPage > 0) {
			parameters.add(new Parameter("per_page", new Integer(perPage)));
		}

		if (page > 0) {
			parameters.add(new Parameter("page", new Integer(page)));
		}
		OAuthUtils.addOAuthToken(parameters);

		Response response = transport.postJSON(sharedSecret, parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
		JSONObject groupsElement = response.getData().getJSONObject("groups");
		JSONArray groupNodes = groupsElement.optJSONArray("group");
		for (int i = 0; groupNodes != null && i < groupNodes.length(); i++) {
			JSONObject groupElement = groupNodes.getJSONObject(i);
			Group group = new Group();
			group.setId(groupElement.getString("id"));
			group.setName(groupElement.getString("name"));
			group.setAdmin("1".equals(groupElement.getString("admin")));
			group.setPrivacy(groupElement.getString("privacy"));
			group.setPhotoCount(groupElement.getString("photos"));
			group.setIconServer(groupElement.getString("iconserver"));
			group.setIconFarm(groupElement.getString("iconfarm"));
			groups.add(group);
		}
		return groups;
	}

	/**
	 * Get the photos for the specified group pool, optionally filtering by taf.
	 * 
	 * This method does not require authentication.
	 * 
	 * @see com.googlecode.flickrjandroid.photos.Extras
	 * @param groupId
	 *            The group ID
	 * @param tags
	 *            The optional tags (may be null)
	 * @param extras
	 *            Set of extra-attributes to include (may be null)
	 * @param perPage
	 *            The number of photos per page (0 to ignore)
	 * @param page
	 *            The page offset (0 to ignore)
	 * @return A Collection of Photo objects
	 * @throws IOException
	 * @throws FlickrException
	 * @throws JSONException
	 */
	public PhotoList getPhotos(String groupId, String[] tags,
			Set<String> extras, int perPage, int page) throws IOException,
			FlickrException, JSONException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("method", METHOD_GET_PHOTOS));
		parameters.add(new Parameter("group_id", groupId));
		if (tags != null) {
			parameters.add(new Parameter("tags", StringUtilities
					.join(tags, " ")));
		}
		if (perPage > 0) {
			parameters.add(new Parameter("per_page", new Integer(perPage)));
		}
		if (page > 0) {
			parameters.add(new Parameter("page", new Integer(page)));
		}

		if (extras != null) {
			StringBuffer sb = new StringBuffer();
			Iterator<String> it = extras.iterator();
			for (int i = 0; it.hasNext(); i++) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append(it.next());
			}
			parameters.add(new Parameter(Extras.KEY_EXTRAS, sb.toString()));
		}

		boolean signed = OAuthUtils.hasSigned();
		if (signed) {
			parameters.add(new Parameter(
					OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
			OAuthUtils.addOAuthToken(parameters);
		} else {
			parameters.add(new Parameter("api_key", apiKey));
		}

		Response response = signed ? transport.postJSON(sharedSecret,
				parameters) : transport.get(transport.getPath(), parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
		return PhotoUtils.createPhotoList(response.getData());
	}

	/**
	 * Convenience/Compatibility method.
	 * 
	 * This method does not require authentication.
	 * 
	 * @see com.googlecode.flickrjandroid.photos.Extras
	 * @param groupId
	 *            The group ID
	 * @param tags
	 *            The optional tags (may be null)
	 * @param perPage
	 *            The number of photos per page (0 to ignore)
	 * @param page
	 *            The page offset (0 to ignore)
	 * @return A Collection of Photo objects
	 * @throws JSONException
	 */
	public PhotoList getPhotos(String groupId, String[] tags, int perPage,
			int page) throws IOException, FlickrException, JSONException {
		return getPhotos(groupId, tags, Extras.MIN_EXTRAS, perPage, page);
	}

	/**
	 * Remove the specified photo from the group.
	 * 
	 * @param photoId
	 *            The photo ID
	 * @param groupId
	 *            The group ID
	 * @throws IOException
	 * @throws FlickrException
	 * @throws JSONException
	 */
	public void remove(String photoId, String groupId) throws IOException,
			FlickrException, JSONException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("method", METHOD_REMOVE));
		parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY,
				apiKey));
		parameters.add(new Parameter("photo_id", photoId));
		parameters.add(new Parameter("group_id", groupId));
		OAuthUtils.addOAuthToken(parameters);

		Response response = transport.postJSON(sharedSecret, parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
	}

}
