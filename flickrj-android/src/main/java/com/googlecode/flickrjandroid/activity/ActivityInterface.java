package com.googlecode.flickrjandroid.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.Parameter;
import com.googlecode.flickrjandroid.Response;
import com.googlecode.flickrjandroid.Transport;
import com.googlecode.flickrjandroid.oauth.OAuthInterface;
import com.googlecode.flickrjandroid.oauth.OAuthUtils;

/**
 * Gather activity information belonging to the calling user.
 *
 * @author Martin Goebel
 * @version $Id: ActivityInterface.java,v 1.4 2008/01/28 23:01:45 x-mago Exp $
 */
public class ActivityInterface {

    public static final String METHOD_USER_COMMENTS = "flickr.activity.userComments";
    public static final String METHOD_USER_PHOTOS = "flickr.activity.userPhotos";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public ActivityInterface(
        String apiKey,
        String sharedSecret,
        Transport transport
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transport;
    }

    /**
     * Returns a list of recent activity on photos commented on by the calling user.<br>
     * Flickr says: Do not poll this method more than once an hour.
     *
     * @param perPage
     * @param page
     * @return ItemList
     * @throws IOException
     * @throws JSONException
     * @throws FlickrException
     */
    public ItemList userComments(int perPage, int page)
      throws IOException, FlickrException, JSONException {
        ItemList items = new ItemList();
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_USER_COMMENTS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        parameters.add(new Parameter("per_page", String.valueOf(perPage)));
        OAuthUtils.addOAuthToken(parameters);
        
        Response response = this.transportAPI.postJSON(this.sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONObject jObj = response.getData();
        JSONObject itemElements = jObj.getJSONObject("items");
        items.setPage(itemElements.getInt("page"));
        items.setPages(itemElements.getInt("pages"));
        items.setPerPage(itemElements.getInt("perpage"));
        items.setTotal(itemElements.getInt("total"));

        JSONArray children = itemElements.getJSONArray("item");
        
        for (int i = 0; i < children.length(); i++) {
            JSONObject itemElement = children.getJSONObject(i);
            items.add(createItem(itemElement));
        }

        return items;
    }

    /**
     * Returns a list of recent activity on photos belonging to the calling user.<br>
     * Flickr says: Do not poll this method more than once an hour.
     *
     * @param perPage
     * @param page
     * @param timeframe
     * @return ItemList
     * @throws IOException
     * @throws JSONException
     * @throws FlickrException
     */
    public ItemList userPhotos(int perPage, int page, String timeframe)
      throws IOException, JSONException, FlickrException {
        ItemList items = new ItemList();
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_USER_PHOTOS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", "" + perPage));
        }

        if (page > 0) {
            parameters.add(new Parameter("page", "" + page));
        }

        if (timeframe != null) {
            if (checkTimeframeArg(timeframe)) {
                parameters.add(new Parameter("timeframe", timeframe));
            } else {
                throw new FlickrException("0","Timeframe-argument to getUserPhotos() not valid");
            }
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.postJSON(this.sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONObject jObj = response.getData();
        JSONObject itemElements = jObj.getJSONObject("items");
        items.setPage(itemElements.getInt("page"));
        items.setPages(itemElements.getInt("pages"));
        items.setPerPage(itemElements.getInt("perpage"));
        items.setTotal(itemElements.getInt("total"));

        JSONArray children = itemElements.getJSONArray("item");
        
        for (int i = 0; i < children.length(); i++) {
            JSONObject itemElement = children.getJSONObject(i);
            items.add(createItem(itemElement));
        }

        return items;
    }

    private Item createItem(JSONObject itemElement) throws JSONException {
        Item item = new Item();
        item.setId(itemElement.getString("id"));
        item.setSecret(itemElement.getString("secret"));
        item.setType(itemElement.getString("type"));
        JSONObject title = itemElement.optJSONObject("title");
        if (title != null) {
            item.setTitle(title.getString("_content"));
        }
        item.setFarm(itemElement.getString("farm"));
        item.setServer(itemElement.getString("server"));
        // userComments
        item.setComments(itemElement.optInt("comments"));
        item.setComments(itemElement.optInt("notes"));
        // userPhotos
        item.setComments(itemElement.optInt("commentsold"));
        item.setComments(itemElement.optInt("commentsnew"));
        item.setComments(itemElement.optInt("notesold"));
        item.setComments(itemElement.optInt("notesnew"));

        item.setViews(itemElement.getInt("views"));
        item.setFaves(itemElement.getInt("faves"));
        item.setMore(itemElement.optInt("more"));

        try {
            JSONObject activityElement = itemElement.getJSONObject("activity");
            List<Event> events = new ArrayList<Event>();

            JSONArray eventNodes = activityElement.getJSONArray("event");
            for (int i = 0; i < eventNodes.length(); i++) {
                JSONObject eventElement = eventNodes.getJSONObject(i);
                Event event = new Event();
                event.setType(eventElement.getString("type"));
                if (event.getType().equals("comment")) {
                    event.setId(eventElement.getString("commentid"));
                } else if (event.getType().equals("note")) {
                    event.setId(eventElement.getString("noteid"));
                } else if (event.getType().equals("fave")) {
                    // has no id
                }
                event.setUser(eventElement.getString("user"));
                event.setUsername(eventElement.getString("username"));
                event.setDateadded(eventElement.getString("dateadded"));
                event.setValue(eventElement.optString("_content"));
                events.add(event);
            }
            item.setEvents(events);
        } catch (Exception e) {
            e.printStackTrace();
            // nop
        }
        return item;
    }

    /**
     * Checks for a valid timeframe-argument.<br>
     * Expects either days, or hours. Like: 2d or 4h.
     *
     * @param timeframe
     * @return boolean
     */
    public boolean checkTimeframeArg(String timeframe) {
        if (Pattern.compile("\\d*(d|h)", Pattern.CASE_INSENSITIVE)
          .matcher(timeframe).matches()) {
            return true;
        } else {
            return false;
        }
    }
}
