package com.googlecode.flickrjandroid.photos.comments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.Parameter;
import com.googlecode.flickrjandroid.Response;
import com.googlecode.flickrjandroid.Transport;
import com.googlecode.flickrjandroid.oauth.OAuthInterface;
import com.googlecode.flickrjandroid.oauth.OAuthUtils;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotoUtils;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.util.StringUtilities;

/**
 * Work on Comments.
 *
 * @author till (Till Krech) flickr:extranoise
 * @version $Id: CommentsInterface.java,v 1.4 2009/07/11 20:30:27 x-mago Exp $
 */
public class CommentsInterface {
    public static final String METHOD_ADD_COMMENT    = "flickr.photos.comments.addComment";
    public static final String METHOD_DELETE_COMMENT = "flickr.photos.comments.deleteComment";
    public static final String METHOD_EDIT_COMMENT   = "flickr.photos.comments.editComment";
    public static final String METHOD_GET_LIST       = "flickr.photos.comments.getList";
    public static final String METHOD_GET_RECENT     = "flickr.photos.comments.getRecentForContacts";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public CommentsInterface(
        String apiKey,
        String sharedSecret,
        Transport transport
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transport;
    }

    /**
     * Add comment to a photo as the currently authenticated user.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param photoId The id of the photo to add a comment to.
     * @param commentText Text of the comment.
     * @return a unique comment id.
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public String addComment(String photoId, String commentText) 
    throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_ADD_COMMENT));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("comment_text", commentText));
        OAuthUtils.addOAuthToken(parameters);

        //Note: This method requires an HTTP POST request.
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject commentElement = response.getData().getJSONObject("comment");
        return commentElement.getString("id");
    }

    /**
     * Delete a comment as the currently authenticated user.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param commentId The id of the comment to delete.
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void deleteComment(String commentId) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_DELETE_COMMENT));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        parameters.add(new Parameter("comment_id", commentId));
        OAuthUtils.addOAuthToken(parameters);

        //Note: This method requires an HTTP POST request.
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        // This method has no specific response - It returns an empty 
        // sucess response if it completes without error.
    }

    /**
     * Edit the text of a comment as the currently authenticated user.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param commentId The id of the comment to edit.
     * @param commentText Update the comment to this text.
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void editComment(String commentId, String commentText) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_EDIT_COMMENT));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        parameters.add(new Parameter("comment_id", commentId));
        parameters.add(new Parameter("comment_text", commentText));
        OAuthUtils.addOAuthToken(parameters);

        //Note: This method requires an HTTP POST request.
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        // This method has no specific response - It returns an empty 
        // sucess response if it completes without error.
    }

    /**
     * Returns the comments for a photo.
     *
     * This method does not require authentication.
     *
     * @param photoId The id of the photo to fetch comments for.
     * @param minCommentDate Minimum date that a a comment was added. Optional,
     * can be null.
     * @param maxCommentDate Maximum date that a comment was added. Optional,
     * can be null.
     * @return a List of {@link Comment} objects.
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public List<Comment> getList(String photoId, Date minCommentDate, Date maxCommentDate)
      throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_LIST));
        boolean signed = OAuthUtils.hasSigned();
        if (signed) {
            parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        } else {
            parameters.add(new Parameter("api_key", apiKey));
        }
        parameters.add(new Parameter("photo_id", photoId));
        if (minCommentDate != null) {
            parameters.add(new Parameter("min_comment_date", minCommentDate.getTime() / 1000L));
        }
        if (maxCommentDate != null) {
            parameters.add(new Parameter("max_comment_date", maxCommentDate.getTime() / 1000L));
        }
        
        if (signed) {
            OAuthUtils.addOAuthToken(parameters);
        }

        Response response = signed ? transportAPI.postJSON(sharedSecret, parameters) :
            transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        List<Comment> comments = new ArrayList<Comment>();
        JSONObject commentsElement = response.getData().getJSONObject("comments");
        JSONArray commentNodes = commentsElement.optJSONArray("comment");
        for (int i = 0; commentNodes != null && i < commentNodes.length(); i++) {
            Comment comment = new Comment();
            JSONObject commentElement = commentNodes.getJSONObject(i);
            comment.setId(commentElement.getString("id"));
            comment.setAuthor(commentElement.getString("author"));
            comment.setAuthorName(commentElement.getString("authorname"));
            comment.setIconServer(commentElement.getInt("iconserver"));
            comment.setIconFarm(commentElement.getInt("iconfarm"));
            comment.setPermaLink(commentElement.getString("permalink"));
            long unixTime = commentElement.optLong("datecreate");
            comment.setDateCreate(new Date(unixTime * 1000L));
            comment.setText(commentElement.getString("_content"));
            comments.add(comment);
        }
        return comments;
    }

    /**
     * <p>Returns the list of photos belonging to your contacts that have been commented on recently.</p>
     *
     * <p>There is an emphasis on the recent part with this method, which is 
     * fancy-talk for "in the last hour".</p>
     *
     * <p>It is not meant to be a general purpose, get all the comments ever, 
     * but rather a quick and easy way to bubble up photos that people are 
     * talking about ("about") now.</p>
     *
     * <p>It has the added bonus / side-effect of bubbling up photos a person 
     * may have missed because they were uploaded before the photo owner was 
     * made a contact or the business of life got in the way.</p>
     *
     * This method requires authentication with 'read' permission.
     *
     * @param lastComment Limits the resultset to photos that have been commented on since this date. The default, and maximum, offset is (1) hour. Optional, can be null.
     * @param contactsFilter A list of contact NSIDs to limit the scope of the query to. Optional, can be null.
     * @param extras A list of extra information to fetch for each returned record. Optional, can be null.
     * @param perPage The number of photos per page.
     * @param page The page offset.
     * @return List of photos
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public PhotoList getRecentForContacts(Date lastComment, List<String> contactsFilter, Set<String> extras, 
            int perPage, int page) throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", PhotosInterface.METHOD_GET_NOT_IN_SET));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (lastComment != null) {
            parameters.add(new Parameter("last_comment", String.valueOf(lastComment.getTime() / 1000L)));
        }

        if (extras != null && !extras.isEmpty()) {
            parameters.add(new Parameter("extras", StringUtilities.join(extras, ",")));
        }

        if (contactsFilter != null && !contactsFilter.isEmpty()) {
            parameters.add(new Parameter("contacts_filter", StringUtilities.join(contactsFilter, ",")));
        }

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return PhotoUtils.createPhotoList(response.getData());
    }
}
