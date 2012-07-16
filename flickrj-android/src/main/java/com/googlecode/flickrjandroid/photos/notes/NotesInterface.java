/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.googlecode.flickrjandroid.photos.notes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.Parameter;
import com.googlecode.flickrjandroid.Response;
import com.googlecode.flickrjandroid.Transport;
import com.googlecode.flickrjandroid.oauth.OAuthInterface;
import com.googlecode.flickrjandroid.oauth.OAuthUtils;
import com.googlecode.flickrjandroid.photos.Note;
import com.googlecode.flickrjandroid.photos.Rectangle;

/**
 * @author Anthony Eden
 */
public class NotesInterface {

    public static final String METHOD_ADD = "flickr.photos.notes.add";
    public static final String METHOD_DELETE = "flickr.photos.notes.delete";
    public static final String METHOD_EDIT = "flickr.photos.notes.edit";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public NotesInterface(
        String apiKey,
        String sharedSecret,
        Transport transportAPI
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Add a note to a photo.  The Note object bounds and text must be specified.
     *
     * @param photoId The photo ID
     * @param note The Note object
     * @return The updated Note object
     * @throws JSONException 
     */
    public Note add(String photoId, Note note) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_ADD));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        Rectangle bounds = note.getBounds();
        if (bounds != null) {
            parameters.add(new Parameter("note_x", String.valueOf(bounds.x)));
            parameters.add(new Parameter("note_y", String.valueOf(bounds.y)));
            parameters.add(new Parameter("note_w", String.valueOf(bounds.width)));
            parameters.add(new Parameter("note_h", String.valueOf(bounds.height)));
        }
        String text = note.getText();
        if (text != null) {
            parameters.add(new Parameter("note_text", text));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONObject noteElement = response.getData().getJSONObject("note");
        note.setId(noteElement.getString("id"));
        return note;
    }

    /**
     * Delete the specified note.
     *
     * @param noteId The node ID
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void delete(String noteId) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_DELETE));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("note_id", noteId));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Update a note.
     *
     * @param note The Note to update
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void edit(Note note) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_EDIT));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("note_id", note.getId()));
        Rectangle bounds = note.getBounds();
        if (bounds != null) {
            parameters.add(new Parameter("note_x", String.valueOf(bounds.x)));
            parameters.add(new Parameter("note_y", String.valueOf(bounds.y)));
            parameters.add(new Parameter("note_w", String.valueOf(bounds.width)));
            parameters.add(new Parameter("note_h", String.valueOf(bounds.height)));
        }
        String text = note.getText();
        if (text != null) {
            parameters.add(new Parameter("note_text", text));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

}
