/**
 * 
 */
package com.gmail.yuyang226.flickr.stats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.Parameter;
import com.gmail.yuyang226.flickr.Response;
import com.gmail.yuyang226.flickr.Transport;
import com.gmail.yuyang226.flickr.oauth.OAuthInterface;
import com.gmail.yuyang226.flickr.oauth.OAuthUtils;
import com.gmail.yuyang226.flickr.org.json.JSONException;
import com.gmail.yuyang226.flickr.photos.PhotoList;
import com.gmail.yuyang226.flickr.photos.PhotoUtils;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class StatsInterface {
	public static final String METHOD_GET_POPULAR_PHOTOS = "flickr.stats.getPopularPhotos";
	
	public static final String SORT_VIEWS = "views";
	public static final String SORT_COMMENTS = "comments";
	public static final String SORT_FAVORITES = "favorites";

	private String apiKey;
	private String sharedSecret;
	private Transport transportAPI;

	/**
	 * 
	 */
	public StatsInterface(String apiKey, String sharedSecret, 
			Transport transportAPI) {
		this.apiKey = apiKey;
		this.sharedSecret = sharedSecret;
		this.transportAPI = transportAPI;
	}
	
	public PhotoList getPopularPhotos(Date date, String sort, int perPage, int page) throws IOException, JSONException, FlickrException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("method", METHOD_GET_POPULAR_PHOTOS));
		if (date != null) {
        	parameters.add(new Parameter("date", date.getTime() / 1000L));
        }
		if (sort != null) {
			parameters.add(new Parameter("sort", sort));
		}
		if (perPage > 0) {
            parameters.add(new Parameter("per_page", Integer.valueOf(perPage)));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", Integer.valueOf(page)));
        }
		parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        OAuthUtils.addOAuthToken(parameters);
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return PhotoUtils.createPhotoList(response.getData());
	}


}
