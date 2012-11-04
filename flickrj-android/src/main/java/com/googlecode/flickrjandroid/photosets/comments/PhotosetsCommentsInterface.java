package com.googlecode.flickrjandroid.photosets.comments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import com.googlecode.flickrjandroid.photos.comments.Comment;

/**
 * Access to the <b>flickr.photosets.comments</b> methods.
 *
 * @author till (Till Krech) flickr:extranoise
 * @version $Id: PhotosetsCommentsInterface.java,v 1.3 2009/07/11 20:30:27 x-mago Exp $
 */
public class PhotosetsCommentsInterface {
    public static final String METHOD_ADD_COMMENT = "flickr.photosets.comments.addComment";
    public static final String METHOD_DELETE_COMMENT = "flickr.photosets.comments.deleteComment";
    public static final String METHOD_EDIT_COMMENT = "flickr.photosets.comments.editComment";
    public static final String METHOD_GET_LIST = "flickr.photosets.comments.getList";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public PhotosetsCommentsInterface(
        String apiKey,
        String sharedSecret,
        Transport transport
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transport;
    }

    /**
     * Add a comment to a photoset.
     * This method requires authentication with 'write' permission.
     * @param photosetId The id of the photoset to add a comment to.
     * @param commentText Text of the comment
     * @return the comment id
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public String addComment(String photosetId, String commentText) throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_ADD_COMMENT));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photoset_id", photosetId));
        parameters.add(new Parameter("comment_text", commentText));
        OAuthUtils.addOAuthToken(parameters);

        // Note: This method requires an HTTP POST request.
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return response.getData().getJSONObject("comment").getString("id");
    }

    /**
     * Delete a photoset comment as the currently authenticated user.
     * @param commentId The id of the comment to delete from a photoset.
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public void deleteComment(String commentId) throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_DELETE_COMMENT));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("comment_id", commentId));
        OAuthUtils.addOAuthToken(parameters);

        // Note: This method requires an HTTP POST request.
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        // This method has no specific response - It returns an empty sucess response if it completes without error.
    }

    /**
     * Edit the text of a comment as the currently authenticated user.
     * This method requires authentication with 'write' permission.
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

        // Note: This method requires an HTTP POST request.
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        // This method has no specific response - It returns an empty sucess response if it completes without error.
    }

    /**
     * Returns the comments for a photoset.
     *
     * This method does not require authentication.
     *
     * @param photosetId The id of the photoset to fetch comments for.
     * @return a list of {@link Comment} objects
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public List<Comment> getList(String photosetId) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_LIST));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photoset_id", photosetId));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        // response:
        //      <comments photoset_id="72157594152539675">
        //      <comment 
        //       id="2534725-72157594152539675-72157594172966066" 
        //       author="99589152@N00" 
        //       authorname="ortrun" 
        //       datecreate="1150905040" 
        //       permalink="http://www.flickr.com/photos/extranoise/sets/72157594152539675/comments#comment72157594172966066">
        //         really unreal!
        //      </comment>
        //      <comment 
        //       id="2534725-72157594152539675-72157594176993150" 
        //       author="47093120@N00" 
        //       authorname="nis.jensen" 
        //       datecreate="1151259227" 
        //       permalink="http://www.flickr.com/photos/extranoise/sets/72157594152539675/comments#comment72157594176993150">
        //         Wow - you're better than most - this was a great view - i'll just take it again!
        //      </comment>
        //      <comment 
        //       id="2534725-72157594152539675-72157594176996639" 
        //       author="47093120@N00" 
        //       authorname="nis.jensen" 
        //       datecreate="1151259453" 
        //       permalink="http://www.flickr.com/photos/extranoise/sets/72157594152539675/comments#comment72157594176996639">
        //        Second it's even better!!!!
        //      </comment>
        //     </comments>
        List<Comment> comments = new ArrayList<Comment>();
        JSONObject commentsElement = response.getData().getJSONObject("comments");
        JSONArray commentNodes = commentsElement.optJSONArray("comment");
        for (int i = 0; commentNodes != null && i < commentNodes.length(); i++) {
            Comment comment = new Comment();
            JSONObject commentElement = commentNodes.getJSONObject(i);
            comment.setId(commentElement.getString("id"));
            comment.setAuthor(commentElement.getString("author"));
            comment.setAuthorName(commentElement.getString("authorname"));
            comment.setPermaLink(commentElement.getString("permalink"));
            long unixTime = 0;
            try {
                unixTime = Long.parseLong(commentElement.getString("datecreate"));
            } catch (NumberFormatException e) {
                // what shall we do?
                e.printStackTrace();
            }
            comment.setDateCreate(new Date(unixTime * 1000L));
            comment.setText(commentElement.getString("_content"));
            comments.add(comment);
        }
        return comments;
    }

}
