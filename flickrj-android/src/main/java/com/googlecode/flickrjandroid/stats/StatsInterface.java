/**
 * 
 */
package com.googlecode.flickrjandroid.stats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class StatsInterface {
    public static final String METHOD_GET_POPULAR_PHOTOS = "flickr.stats.getPopularPhotos";
    public static final String METHOD_GET_PHOTO_STATS = "flickr.stats.getPhotoStats";
    public static final String METHOD_GET_COLLECTION_DOMAINS = "flickr.stats.getCollectionDomains";
    public static final String METHOD_GET_COLLECTION_REFERRERS = "flickr.stats.getCollectionReferrers";
    public static final String METHOD_GET_COLLECTION_STATS = "flickr.stats.getCollectionStats";
    public static final String METHOD_GET_CSV_FILES = "flickr.stats.getCSVFiles";
    public static final String METHOD_GET_PHOTO_DOMAINS = "flickr.stats.getPhotoDomains";
    public static final String METHOD_GET_PHOTO_REFERRERS = "flickr.stats.getPhotoReferrers";
    public static final String METHOD_GET_PHOTOSET_DOMAINS = "flickr.stats.getPhotosetDomains";
    public static final String METHOD_GET_PHOTOSET_REFERRERS = "flickr.stats.getPhotosetReferrers";
    public static final String METHOD_GET_PHOTOSET_STATS = "flickr.stats.getPhotosetStats";
    public static final String METHOD_GET_PHOTOSTREAM_DOMAINS = "flickr.stats.getPhotostreamDomains";
    public static final String METHOD_GET_PHOTOSTREAM_REFERRERS = "flickr.stats.getPhotostreamReferrers";
    public static final String METHOD_GET_PHOTOSTREAM_STATS = "flickr.stats.getPhotostreamStats";
    public static final String METHOD_GET_TOTAL_VIEWS = "flickr.stats.getTotalViews";
    
    public static enum SORT {
        VIEWS, COMMENTS, FAVORITES
    }
    
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
    
    /**
     * 
     * <a href="http://www.flickr.com/services/api/flickr.stats.getPopularPhotos.html">getPopularPhotos</a>
     * @param date Stats will be returned for this date. This should be in either be in YYYY-MM-DD or unix timestamp format. A day according to Flickr Stats starts at midnight GMT for all users, and timestamps will automatically be rounded down to the start of the day. If no date is provided, all time view counts will be returned.
     * @param sort The order in which to sort returned photos. Defaults to views. The possible values are views, comments and favorites. 
     * @param perPage Number of referrers to return per page. If this argument is omitted, it defaults to 25. The maximum allowed value is 100.
     * @param page The page of results to return. If this argument is omitted, it defaults to 1.
     * @return
     * @throws IOException
     * @throws JSONException
     * @throws FlickrException
     */
    public PhotoList getPopularPhotos(Date date, SORT sort, int perPage, int page) throws IOException, JSONException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_POPULAR_PHOTOS));
        if (date != null) {
            parameters.add(new Parameter("date", date.getTime() / 1000L));
        }
        if (sort != null) {
            parameters.add(new Parameter("sort", sort.name().toLowerCase(Locale.US)));
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
    
    /**
     * <a href="http://www.flickr.com/services/api/flickr.stats.getPhotoStats.html">getPhotoStats</a>
     * @param date Stats will be returned for this date. This should be in either be in YYYY-MM-DD or unix timestamp format. A day according to Flickr Stats starts at midnight GMT for all users, and timestamps will automatically be rounded down to the start of the day.
     * @param photoId The id of the photo to get stats for.
     * @return
     * @throws IOException
     * @throws JSONException
     * @throws FlickrException
     */
    public Stats getPhotoStats(String date, String photoId) throws IOException, JSONException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PHOTO_STATS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (date != null) {
            parameters.add(new Parameter("date", date));
        }
        parameters.add(new Parameter("photo_id", photoId));
        OAuthUtils.addOAuthToken(parameters);
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return StatsUtils.createStats(response.getData());
    }
    
    /**
     * <a href="http://www.flickr.com/services/api/flickr.stats.getPhotoStats.html">getPhotoStats</a>
     * @param date
     * @param photoId
     * @return
     * @throws IOException
     * @throws JSONException
     * @throws FlickrException
     */
    public Stats getPhotoStats(Date date, String photoId) throws IOException, JSONException, FlickrException {
        return getPhotoStats(date != null ? String.valueOf(date.getTime() / 1000L) : null, photoId);
    }
    
    public DomainList getCollectionDomains(String date, String collectionId, int perPage, int page) throws IOException, JSONException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_COLLECTION_DOMAINS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (date != null) {
            parameters.add(new Parameter("date", date));
        }
        if (collectionId != null) {
            parameters.add(new Parameter("collection_id", collectionId));
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
        return StatsUtils.createDomainList(response.getData().getJSONObject("domains"));
    }
    
    public DomainList getCollectionDomains(Date date, String collectionId, int perPage, int page) throws IOException, JSONException, FlickrException {
        return getCollectionDomains(date != null ? String.valueOf(date.getTime() / 1000L) : null, collectionId, perPage, page);
    }
    
    public ReferrerList getCollectionReferrers(String date, String domain, String collectionId, int perPage, int page) throws IOException, JSONException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_COLLECTION_REFERRERS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (date != null) {
            parameters.add(new Parameter("date", date));
        }
        parameters.add(new Parameter("domain", domain));
        if (collectionId != null) {
            parameters.add(new Parameter("collection_id", collectionId));
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
        return StatsUtils.createReferrerList(response.getData().getJSONObject("domain"));
    }
    
    public ReferrerList getCollectionReferrers(Date date, String domain, String collectionId, int perPage, int page) throws IOException, JSONException, FlickrException {
        return getCollectionReferrers(date != null ? String.valueOf(date.getTime() / 1000L) : null, domain, collectionId, perPage, page);
    }
    
    public Stats getCollectionStats(String date, String collectionId) throws IOException, JSONException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_COLLECTION_STATS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (date != null) {
            parameters.add(new Parameter("date", date));
        }
        parameters.add(new Parameter("collection_id", collectionId));
        OAuthUtils.addOAuthToken(parameters);
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        
        return StatsUtils.createStats(response.getData());
    }
    
    public Stats getCollectionStats(Date date, String photosetId) throws IOException, JSONException, FlickrException {
        return getCollectionStats(date != null ? String.valueOf(date.getTime() / 1000L) : null, photosetId);
    }
    
    public void getCSVFiles() {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_CSV_FILES));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        throw new UnsupportedOperationException();
    }
    
    public DomainList getPhotoDomains(String date, String photoId, int perPage, int page) throws IOException, JSONException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PHOTO_DOMAINS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (date != null) {
            parameters.add(new Parameter("date", date));
        }
        if (photoId != null) {
            parameters.add(new Parameter("photo_id", photoId));
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
        return StatsUtils.createDomainList(response.getData().getJSONObject("domains"));
    }
    
    public DomainList getPhotoDomains(Date date, String photoId, int perPage, int page) throws IOException, JSONException, FlickrException {
        return getPhotoDomains(date != null ? String.valueOf(date.getTime() / 1000L) : null, photoId, perPage, page);
    }
    
    public ReferrerList getPhotoReferrers(String date, String domain, String photoId, int perPage, int page) throws IOException, JSONException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PHOTO_REFERRERS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (date != null) {
            parameters.add(new Parameter("date", date));
        }
        parameters.add(new Parameter("domain", domain));
        if (photoId != null) {
            parameters.add(new Parameter("photo_id", photoId));
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
        return StatsUtils.createReferrerList(response.getData().getJSONObject("domain"));
    }
    
    public ReferrerList getPhotoReferrers(Date date, String domain, String photoId, int perPage, int page) throws IOException, JSONException, FlickrException {
        return getPhotoReferrers(date != null ? String.valueOf(date.getTime() / 1000L) : null, domain, photoId, perPage, page);
    }
    
    public DomainList getPhotosetDomains(String date, String photosetId, int perPage, int page) throws IOException, JSONException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PHOTOSET_DOMAINS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (date != null) {
            parameters.add(new Parameter("date", date));
        }
        if (photosetId != null) {
            parameters.add(new Parameter("photoset_id", photosetId));
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
        return StatsUtils.createDomainList(response.getData().getJSONObject("domains"));
    }
    
    public DomainList getPhotosetDomains(Date date, String photosetId, int perPage, int page) throws IOException, JSONException, FlickrException {
        return getPhotosetDomains(date != null ? String.valueOf(date.getTime() / 1000L) : null, photosetId, perPage, page);
    }
    
    public ReferrerList getPhotosetReferrers(String date, String domain, String photosetId, int perPage, int page) throws IOException, JSONException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PHOTOSET_REFERRERS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (date != null) {
            parameters.add(new Parameter("date", date));
        }
        parameters.add(new Parameter("domain", domain));
        if (photosetId != null) {
            parameters.add(new Parameter("photoset_id", photosetId));
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
        return StatsUtils.createReferrerList(response.getData().getJSONObject("domain"));
    }
    
    public ReferrerList getPhotosetReferrers(Date date, String domain, String photosetId, int perPage, int page) throws IOException, JSONException, FlickrException {
        return getPhotosetReferrers(date != null ? String.valueOf(date.getTime() / 1000L) : null, domain, photosetId, perPage, page);
    }

    public Stats getPhotosetStats(String date, String photosetId) throws IOException, JSONException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PHOTOSET_STATS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (date != null) {
            parameters.add(new Parameter("date", date));
        }
        parameters.add(new Parameter("photoset_id", photosetId));
        OAuthUtils.addOAuthToken(parameters);
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        
        return StatsUtils.createStats(response.getData());
    }
    
    public Stats getPhotosetStats(Date date, String photosetId) throws IOException, JSONException, FlickrException {
        return getPhotosetStats(date != null ? String.valueOf(date.getTime() / 1000L) : null, photosetId);
    }
    
    public DomainList getPhotostreamDomains(String date, int perPage, int page) throws IOException, JSONException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PHOTOSTREAM_DOMAINS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (date != null) {
            parameters.add(new Parameter("date", date));
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
        return StatsUtils.createDomainList(response.getData().getJSONObject("domains"));
    }
    
    public DomainList getPhotostreamDomains(Date date, int perPage, int page) throws IOException, JSONException, FlickrException {
        return getPhotostreamDomains(date != null ? String.valueOf(date.getTime() / 1000L) : null, perPage, page);
    }
    
    public ReferrerList getPhotostreamReferrers(String date, String domain) throws IOException, JSONException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PHOTOSTREAM_REFERRERS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (date != null) {
            parameters.add(new Parameter("date", date));
        }
        parameters.add(new Parameter("domain", domain));
        OAuthUtils.addOAuthToken(parameters);
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return StatsUtils.createReferrerList(response.getData().getJSONObject("domain"));
    }
    
    public ReferrerList getPhotostreamReferrers(Date date, String domain) throws IOException, JSONException, FlickrException {
        return getPhotostreamReferrers(date != null ? String.valueOf(date.getTime() / 1000L) : null, domain);
    }
    
    public Stats getPhotostreamStats(String date) throws IOException, JSONException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PHOTOSTREAM_STATS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (date != null) {
            parameters.add(new Parameter("date", date));
        }
        OAuthUtils.addOAuthToken(parameters);
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        
        return StatsUtils.createStats(response.getData());
    }
    
    public Stats getPhotostreamStats(Date date) throws IOException, JSONException, FlickrException {
        return getPhotostreamStats(date != null ? String.valueOf(date.getTime() / 1000L) : null);
    }
    
    public AccountStats getTotalViews(String date) throws IOException, JSONException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_TOTAL_VIEWS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (date != null) {
            parameters.add(new Parameter("date", date));
        }
        OAuthUtils.addOAuthToken(parameters);
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        
        JSONObject statsObject = response.getData().getJSONObject("stats");
        AccountStats stats = new AccountStats();
        stats.setTotalViews(statsObject.getJSONObject("total").getInt("views"));
        stats.setPhotosViews(statsObject.getJSONObject("photos").getInt("views"));
        stats.setPhotostreamViews(statsObject.getJSONObject("photostream").getInt("views"));
        stats.setSetsViews(statsObject.getJSONObject("sets").getInt("views"));
        stats.setCollectionsViews(statsObject.getJSONObject("collections").getInt("views"));
        stats.setGalleriesViews(statsObject.getJSONObject("galleries").getInt("views"));
        return stats;
    }
    
    public AccountStats getTotalViews(Date date) throws IOException, JSONException, FlickrException {
        return getTotalViews(date != null ? String.valueOf(date.getTime() / 1000L) : null);
    }
    
}
