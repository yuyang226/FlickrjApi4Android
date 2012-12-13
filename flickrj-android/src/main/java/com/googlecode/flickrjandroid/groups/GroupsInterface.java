/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickrjandroid.groups;

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
import com.googlecode.flickrjandroid.util.JSONUtils;

/**
 * Interface for working with Flickr Groups.
 *
 * @author Anthony Eden
 * @version $Id: GroupsInterface.java,v 1.19 2009/07/11 20:30:27 x-mago Exp $
 */
public class GroupsInterface {

    public static final String METHOD_BROWSE = "flickr.groups.browse";
    public static final String METHOD_GET_ACTIVE_LIST = "flickr.groups.getActiveList";
    public static final String METHOD_GET_INFO = "flickr.groups.getInfo";
    public static final String METHOD_SEARCH = "flickr.groups.search";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public GroupsInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Browse groups for the given category ID.  If a null value is passed for the category then the root category is
     * used.
     *
     * @param catId The optional category id.  Null value will be ignored.
     * @return The Collection of Photo objects
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Category browse(String catId) throws IOException, FlickrException, JSONException {
        List<Subcategory> subcategories = new ArrayList<Subcategory>();
        List<Group> groups = new ArrayList<Group>();

        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_BROWSE));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (catId != null) {
            parameters.add(new Parameter("cat_id", catId));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject categoryElement = response.getData().getJSONObject("category");

        Category category = new Category();
        category.setName(categoryElement.getString("name"));
        category.setPath(categoryElement.getString("path"));
        category.setPathIds(categoryElement.getString("pathids"));

        JSONArray subcatNodes = categoryElement.optJSONArray("subcat");
        for (int i = 0; subcatNodes != null && i < subcatNodes.length(); i++) {
            JSONObject node = subcatNodes.getJSONObject(i);
            Subcategory subcategory = new Subcategory();
            subcategory.setId(node.getInt("id"));
            subcategory.setName(node.getString("name"));
            subcategory.setCount(node.getInt("count"));
            subcategories.add(subcategory);
        }

        JSONArray groupNodes = categoryElement.optJSONArray("group");
        for (int i = 0; groupNodes != null && i < groupNodes.length(); i++) {
            JSONObject node = groupNodes.getJSONObject(i);
            Group group = new Group();
            group.setId(node.getString("nsid"));
            group.setName(node.getString("name"));
            group.setMembers(node.getString("members"));

            groups.add(group);
        }

        category.setGroups(groups);
        category.setSubcategories(subcategories);

        return category;
    }

    /**
     * Get the info for a specified group.
     *
     * This method does not require authentication.
     *
     * @param groupId The group id
     * @return The Group object
     * @throws JSONException 
     */
    public Group getInfo(String groupId) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_INFO));
        parameters.add(new Parameter("api_key", apiKey));
        parameters.add(new Parameter("group_id", groupId));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject groupElement = response.getData().getJSONObject("group");
        Group group = new Group();
        group.setId(groupElement.getString("id"));
        group.setIconFarm(groupElement.getString("iconfarm"));
        group.setIconServer(groupElement.getString("iconserver"));
        group.setLang(groupElement.getString("lang"));
        group.setPoolModerated("1".equals(groupElement.getString("ispoolmoderated")));

        group.setName(JSONUtils.getChildValue(groupElement, "name"));
        group.setDescription(JSONUtils.getChildValue(groupElement, "description"));
        group.setMembers(JSONUtils.getChildValue(groupElement, "members"));
        group.setPrivacy(JSONUtils.getChildValue(groupElement, "privacy"));

        JSONArray throttleNodes = groupElement.optJSONArray("throttle");
        int n = throttleNodes == null ? 0 : throttleNodes.length();
        if (n == 1) {
            JSONObject throttleElement = throttleNodes.getJSONObject(n);
            Throttle throttle = new Throttle();
            group.setThrottle(throttle);
            throttle.setMode(throttleElement.getString("mode"));
            throttle.setCount(throttleElement.optInt("count"));
            throttle.setRemaining(throttleElement.optInt("remaining"));
        } else if (n > 1) {
            System.err.println("WARNING: more than one throttle element in group");
        }

        return group;
    }

    /**
     * Search for groups. 18+ groups will only be returned for authenticated calls where the authenticated user is over 18.
     * This method does not require authentication.
     * @param text The text to search for.
     * @param perPage Number of groups to return per page. If this argument is 0, it defaults to 100. The maximum allowed value is 500.
     * @param page The page of results to return. If this argument is 0, it defaults to 1.
     * @return A GroupList Object. Only the fields <em>id</em>, <em>name</em> and <em>eighteenplus</em> in the Groups will be set.
     * @throws IOException 
     * @throws FlickrException
     * @throws JSONException 
     */
    public Collection<Group> search(String text, int perPage, int page) throws FlickrException, IOException, JSONException {
        GroupList groupList = new GroupList();
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_SEARCH));
        parameters.add(new Parameter("api_key", this.apiKey));
        parameters.add(new Parameter("text", text));

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", new Integer(perPage)));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", new Integer(page)));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject groupsElement = response.getData().getJSONObject("groups");
        JSONArray groupNodes = groupsElement.optJSONArray("group");
        groupList.setPage(groupsElement.getInt("page"));
        groupList.setPages(groupsElement.getInt("pages"));
        groupList.setPerPage(groupsElement.getInt("perpage"));
        groupList.setTotal(groupsElement.getInt("total"));
        for (int i = 0; groupNodes != null && i < groupNodes.length(); i++) {
            JSONObject groupElement = groupNodes.getJSONObject(i);
            Group group = new Group();
            group.setId(groupElement.getString("nsid"));
            group.setName(groupElement.getString("name"));
            groupList.add(group);
        }
        return groupList;
    }
}
