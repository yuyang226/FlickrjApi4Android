package com.googlecode.flickrjandroid.panda;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.Parameter;
import com.googlecode.flickrjandroid.Response;
import com.googlecode.flickrjandroid.Transport;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotoUtils;
import com.googlecode.flickrjandroid.util.StringUtilities;

/**
 * Flickr Panda.
 *
 * @author mago
 * @version $Id: PandaInterface.java,v 1.3 2009/07/11 20:30:27 x-mago Exp $
 * @see <a href="http://www.flickr.com/explore/panda">Flickr Panda</a>
 */
public class PandaInterface {
    private static final String METHOD_GET_PHOTOS = "flickr.panda.getPhotos";
    private static final String METHOD_GET_LIST = "flickr.panda.getList";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public PandaInterface(
        String apiKey,
        String sharedSecret,
        Transport transportAPI
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Return a list of Flickr pandas, from whom you can request photos using
     * the {@link com.googlecode.flickrjandroid.panda.PandaInterface#getPhotos(Panda, Set, int, int)}
     * API method.
     *
     * This method does not require authentication.
     *
     * @return A list of pandas
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public List<Panda> getList() throws FlickrException, IOException, JSONException {
        List<Panda> pandas = new ArrayList<Panda>();
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_LIST));
        parameters.add(new Parameter("api_key", apiKey));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject pandaElement = response.getData().getJSONObject("pandas");
        JSONArray pandaNodes = pandaElement.optJSONArray("panda");
        for (int i = 0; pandaNodes != null && i < pandaNodes.length(); i++) {
            JSONObject pElement = pandaNodes.getJSONObject(i);
            Panda panda = new Panda();
            panda.setName(pElement.getString("_content"));
            pandas.add(panda);
        }
        return pandas;
    }

    /**
     * Ask the Flickr Pandas for a list of recent public (and "safe") photos.
     *
     * This method does not require authentication.
     *
     * @param panda The panda to ask for photos from.
     * @param extras A set of Strings controlling the extra information to fetch for each returned record. {@link com.googlecode.flickrjandroid.photos.Extras#ALL_EXTRAS}
     * @param perPage The number of photos to show per page
     * @param page The page offset
     * @return A PhotoList
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     * @see com.googlecode.flickrjandroid.photos.Extras
     */
    public PhotoList getPhotos(Panda panda, Set<String> extras, int perPage, int page) 
    throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PHOTOS));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("panda_name", panda.getName()));

        if (extras != null && !extras.isEmpty()) {
            parameters.add(new Parameter("extras", StringUtilities.join(extras, ",")));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return PhotoUtils.createPhotoList(response.getData());
    }
}
