/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickrjandroid.photos;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.Parameter;
import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.Response;
import com.googlecode.flickrjandroid.Transport;
import com.googlecode.flickrjandroid.oauth.OAuthInterface;
import com.googlecode.flickrjandroid.oauth.OAuthUtils;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.geo.GeoInterface;
import com.googlecode.flickrjandroid.util.JSONUtils;
import com.googlecode.flickrjandroid.util.StringUtilities;

/**
 * Interface for working with Flickr Photos.
 *
 * @author Anthony Eden
 * @version $Id: PhotosInterface.java,v 1.51 2010/07/20 20:11:16 x-mago Exp $
 */
public class PhotosInterface {
    public static final long serialVersionUID = 12L;

    public static final String METHOD_ADD_TAGS = "flickr.photos.addTags";
    public static final String METHOD_DELETE = "flickr.photos.delete";
    public static final String METHOD_GET_ALL_CONTEXTS = "flickr.photos.getAllContexts";
    public static final String METHOD_GET_CONTACTS_PHOTOS = "flickr.photos.getContactsPhotos";
    public static final String METHOD_GET_CONTACTS_PUBLIC_PHOTOS = "flickr.photos.getContactsPublicPhotos";
    public static final String METHOD_GET_CONTEXT = "flickr.photos.getContext";
    public static final String METHOD_GET_COUNTS = "flickr.photos.getCounts";
    public static final String METHOD_GET_EXIF = "flickr.photos.getExif";
    public static final String METHOD_GET_FAVORITES = "flickr.photos.getFavorites";
    public static final String METHOD_GET_INFO = "flickr.photos.getInfo";
    public static final String METHOD_GET_NOT_IN_SET = "flickr.photos.getNotInSet";
    public static final String METHOD_GET_PERMS = "flickr.photos.getPerms";
    public static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
    public static final String METHOD_GET_SIZES = "flickr.photos.getSizes";
    public static final String METHOD_GET_UNTAGGED = "flickr.photos.getUntagged";
    public static final String METHOD_GET_WITH_GEO_DATA = "flickr.photos.getWithGeoData";
    public static final String METHOD_GET_WITHOUT_GEO_DATA = "flickr.photos.getWithoutGeoData";
    public static final String METHOD_RECENTLY_UPDATED ="flickr.photos.recentlyUpdated";
    public static final String METHOD_REMOVE_TAG = "flickr.photos.removeTag";
    public static final String METHOD_SEARCH = "flickr.photos.search";
    public static final String METHOD_SET_CONTENTTYPE = "flickr.photos.setContentType";
    public static final String METHOD_SET_DATES = "flickr.photos.setDates";
    public static final String METHOD_SET_META = "flickr.photos.setMeta";
    public static final String METHOD_SET_PERMS = "flickr.photos.setPerms";
    public static final String METHOD_SET_SAFETYLEVEL = "flickr.photos.setSafetyLevel";
    public static final String METHOD_SET_TAGS = "flickr.photos.setTags";
    public static final String METHOD_GET_INTERESTINGNESS = "flickr.interestingness.getList";

    private static final ThreadLocal<DateFormat> DATE_FORMATS = new ThreadLocal<DateFormat>() {
        protected synchronized DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private GeoInterface geoInterface = null;

    private String apiKey;
    private String sharedSecret;
    private Transport transport;

    public PhotosInterface(String apiKey, String sharedSecret, Transport transport) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transport = transport;
    }

    /**
     * Get the geo interface.
     * @return Access class to the flickr.photos.geo methods.
     */
    public synchronized GeoInterface getGeoInterface() {
        if (geoInterface == null) {
            geoInterface = new GeoInterface(apiKey, sharedSecret, transport);
        }
        return geoInterface;
    }

    /**
     * Add tags to a photo.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param photoId The photo ID
     * @param tags The tags
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void addTags(String photoId, String[] tags) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_ADD_TAGS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("tags", StringUtilities.join(tags, " ", true)));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Delete a photo from flickr.
     *
     * This method requires authentication with 'delete' permission.
     *
     * @param photoId
     * @throws IOException 
     * @throws FlickrException 
     * @throws JSONException 
     */
    public void delete(String photoId)
        throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_DELETE));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        // This method has no specific response - It returns an empty 
        // sucess response if it completes without error.
    }

    /**
     * Returns all visble sets and pools the photo belongs to.
     *
     * This method does not require authentication.
     *
     * @param photoId The photo to return information for.
     * @return a list of {@link PhotoPlace} objects
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public List<PhotoPlace> getAllContexts(String photoId) throws IOException, FlickrException, JSONException {
        List<PhotoPlace> list = new ArrayList<PhotoPlace>();
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_ALL_CONTEXTS));
        boolean signed = OAuthUtils.hasSigned();
        parameters.add(new Parameter("photo_id", photoId));
        if (signed) {
            parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
            OAuthUtils.addOAuthToken(parameters);
        } else {
            parameters.add(new Parameter("api_key", apiKey));
        }

        Response response = signed ? transport.postJSON(sharedSecret, parameters) : 
            transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONArray sets = response.getData().optJSONArray("set");
        for (int i = 0; sets != null && i < sets.length(); i++) {
            JSONObject el = sets.getJSONObject(i);
            String id = el.getString("id");
            String title = el.getString("title");
            list.add(new PhotoPlace(PhotoPlace.SET, id, title));
        }
        
        JSONArray pools = response.getData().optJSONArray("pool");
        for (int i = 0; pools != null && i < pools.length(); i++) {
            JSONObject el = pools.getJSONObject(i);
            String id = el.getString("id");
            String title = el.getString("title");
            list.add(new PhotoPlace(PhotoPlace.POOL, id, title));
        }
        return list;
    }

    /**
     * Get photos from the user's contacts.
     *
     * This method requires authentication with 'read' permission.
     *
     * @see com.googlecode.flickrjandroid.photos.Extras
     * @param count The number of photos to return
     * @param justFriends Set to true to only show friends photos
     * @param singlePhoto Set to true to get a single photo
     * @param includeSelf Set to true to include self
     * @return The Collection of photos
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public PhotoList getContactsPhotos(int count, boolean justFriends, boolean singlePhoto, boolean includeSelf)
            throws IOException, FlickrException, JSONException {
        return getContactsPhotos(count, Extras.MIN_EXTRAS, justFriends, singlePhoto, includeSelf, 0, 0);
    }

    /*
     * NOTE(bourke): Regarding pagination for flickr.photos.getContactsPhotos:
     *
     * The api docs don't mention that pagination for this method is supported.
     * However, looking at the json response from the api explorer it quite
     * clearly is.  I have tested and it works fine.
     *
     * It's likely Flickr just need to update their docs, though this may
     * change in the future.
     */
    public PhotoList getContactsPhotos(int count, Set<String> extras,
            boolean justFriends, boolean singlePhoto, boolean includeSelf,
            int page, int perPage)
                throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_CONTACTS_PHOTOS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", new Integer(perPage)));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", new Integer(page)));
        }
        if (count > 0) {
            parameters.add(new Parameter("count", count));
        }
        if (justFriends) {
            parameters.add(new Parameter("just_friends", "1"));
        }
        if (singlePhoto) {
            parameters.add(new Parameter("single_photo", "1"));
        }
        if (includeSelf) {
            parameters.add(new Parameter("include_self", "1"));
        }
        if (extras != null) {
            parameters.add(new Parameter(Extras.KEY_EXTRAS, StringUtilities.join(extras, ",")));
        }

        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return PhotoUtils.createPhotoList(response.getData());
    }

    /**
     * Get public photos from the user's contacts.
     *
     * This method does not require authentication.
     *
     * @see com.googlecode.flickrjandroid.photos.Extras
     * @param userId The user ID
     * @param count The number of photos to return
     * @param justFriends True to include friends
     * @param singlePhoto True to get a single photo
     * @param includeSelf True to include self
     * @return A collection of Photo objects
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public PhotoList getContactsPublicPhotos(String userId, int count, boolean justFriends, boolean singlePhoto, boolean includeSelf)
      throws IOException, FlickrException, JSONException {
        return getContactsPublicPhotos(userId, Extras.MIN_EXTRAS, count, justFriends, singlePhoto, includeSelf);
    }

    public PhotoList getContactsPublicPhotos(String userId, Set<String> extras, int count, boolean justFriends, boolean singlePhoto, boolean includeSelf)
      throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_CONTACTS_PUBLIC_PHOTOS));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("user_id", userId));

        if (count > 0) {
            parameters.add(new Parameter("count", count));
        }
        if (justFriends) {
            parameters.add(new Parameter("just_friends", "1"));
        }
        if (singlePhoto) {
            parameters.add(new Parameter("single_photo", "1"));
        }
        if (includeSelf) {
            parameters.add(new Parameter("include_self", "1"));
        }

        if (extras != null) {
            parameters.add(new Parameter(Extras.KEY_EXTRAS, StringUtilities.join(extras, ",")));
        }

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return PhotoUtils.createPhotoList(response.getData());
    }

    /**
     * Get the context for the specified photo.
     *
     * This method does not require authentication.
     *
     * @param photoId The photo ID
     * @return The PhotoContext
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public PhotoContext getContext(String photoId)
      throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_CONTEXT));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return PhotoUtils.createPhotoContext(response.getData());
    }

    /**
     * Gets a collection of photo counts for the given date ranges for the calling user.
     *
     * This method requires authentication with 'read' permission.
     *
     * @param dates An array of dates, denoting the periods to return counts for.
     * They should be specified smallest first.
     * @param takenDates An array of dates, denoting the periods to return 
     * counts for. They should be specified smallest first.
     * @return A Collection of Photocount objects
     * @throws JSONException 
     */
    public Collection<Photocount> getCounts(Date[] dates, Date[] takenDates)
        throws IOException, FlickrException, JSONException {
        List<Photocount> photocounts = new ArrayList<Photocount>();

        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_COUNTS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        if (dates == null && takenDates == null) {
            throw new IllegalArgumentException("You must provide a value for either dates or takenDates");
        }

        if (dates != null) {
            List<String> dateList = new ArrayList<String>();
            for (int i = 0; i < dates.length; i++) {
                dateList.add(String.valueOf(dates[i].getTime() / 1000L));
            }
            parameters.add(new Parameter("dates", StringUtilities.join(dateList, ",")));
        }

        if (takenDates != null) {
            List<String> takenDateList = new ArrayList<String>();
            for (int i = 0; i < takenDates.length; i++) {
                takenDateList.add(String.valueOf(takenDates[i].getTime() / 1000L));
            }
            parameters.add(new Parameter("taken_dates", StringUtilities.join(takenDateList, ",")));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject photocountsElement = response.getData().getJSONObject("photocounts");
        JSONArray photocountNodes = photocountsElement.optJSONArray("photocount");
        for (int i = 0; photocountNodes != null && i < photocountNodes.length(); i++) {
            JSONObject photocountElement = photocountNodes.getJSONObject(i);
            Photocount photocount = new Photocount();
            photocount.setCount(photocountElement.getInt("count"));
            photocount.setFromDate(photocountElement.getString("fromdate"));
            photocount.setToDate(photocountElement.getString("todate"));
            photocounts.add(photocount);
        }
        return photocounts;
    }

    /**
     * Get the Exif data for the photo.
     * 
     * The calling user must have permission to view the photo.
     *
     * This method does not require authentication.
     *
     * @param photoId The photo ID
     * @param secret The secret
     * @return A collection of Exif objects
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Collection<Exif> getExif(String photoId, String secret)
        throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_EXIF));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        if (secret != null) {
            parameters.add(new Parameter("secret", secret));
        }

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        List<Exif> exifs = new ArrayList<Exif>();
        JSONObject photoElement = response.getData().getJSONObject("photo");
        JSONArray exifElements = photoElement.optJSONArray("exif");
        for (int i = 0; exifElements != null && i < exifElements.length(); i++) {
            JSONObject exifElement = exifElements.getJSONObject(i);
            Exif exif = new Exif();
            exif.setTagspace(exifElement.getString("tagspace"));
            exif.setTagspaceId(exifElement.getString("tagspaceid"));
            exif.setTag(exifElement.getString("tag"));
            exif.setLabel(exifElement.getString("label"));
            exif.setRaw(JSONUtils.getChildValue(exifElement, "raw"));
            exif.setClean(JSONUtils.getChildValue(exifElement, "clean"));
            exifs.add(exif);
        }
        return exifs;
    }

    /**
     * Returns the list of people who have favorited a given photo.
     *
     * This method does not require authentication.
     *
     * @param photoId
     * @param perPage
     * @param page
     * @return List of {@link com.googlecode.flickrjandroid.people.User}
     * @throws JSONException 
     */
    public PhotoFavouriteUserList getFavorites(String photoId, int perPage, int page)
        throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();

        parameters.add(new Parameter("method", METHOD_GET_FAVORITES));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }

        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        PhotoFavouriteUserList favUsers = new PhotoFavouriteUserList();
        JSONObject photoRoot = response.getData().getJSONObject("photo");
        favUsers.setPage(photoRoot.getInt("page"));
        favUsers.setPerPage(photoRoot.getInt("perpage"));
        favUsers.setPages(photoRoot.getInt("pages"));
        favUsers.setTotal(photoRoot.getInt("total"));
        favUsers.setSecret(photoRoot.optString("secret"));
        favUsers.setServer(photoRoot.optString("server"));
        favUsers.setFarm(photoRoot.optInt("farm"));
        JSONArray userNodes = photoRoot.optJSONArray("person");
        for (int i = 0; userNodes != null && i < userNodes.length(); i++) {
            JSONObject userElement = userNodes.getJSONObject(i);
            User user = new User();
            user.setId(userElement.getString("nsid"));
            user.setUsername(userElement.getString("username"));
            user.setFaveDate(userElement.getString("favedate"));
            favUsers.add(user);
        }
        return favUsers;
    }

    /**
     * Get all info for the specified photo.
     *
     * The calling user must have permission to view the photo.
     *
     * This method does not require authentication.
     *
     * @param photoId The photo Id
     * @param secret The optional secret String
     * @return The Photo
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Photo getInfo(String photoId, String secret) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_INFO));
        boolean signed = OAuthUtils.hasSigned();
        parameters.add(new Parameter("photo_id", photoId));
        if (secret != null) {
            parameters.add(new Parameter("secret", secret));
        }
        if (signed) {
            parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
            OAuthUtils.addOAuthToken(parameters);
        } else {
            parameters.add(new Parameter("api_key", apiKey));
        }

        Response response = signed ? transport.postJSON(sharedSecret, parameters) : 
            transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject photoElement = response.getData().getJSONObject("photo");
        return PhotoUtils.createPhoto(photoElement);
    }

    /**
     * Return a collection of Photo objects not in part of any sets.
     *
     * This method requires authentication with 'read' permission.
     *
     * @param perPage The per page
     * @param page The page
     * @return The collection of Photo objects
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public PhotoList getNotInSet(int perPage, int page) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", PhotosInterface.METHOD_GET_NOT_IN_SET));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        RequestContext requestContext = RequestContext.getRequestContext();

        List<String> extras = requestContext.getExtras();
        if (extras.size() > 0) {
            parameters.add(new Parameter("extras", StringUtilities.join(extras, ",")));
        }

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return PhotoUtils.createPhotoList(response.getData());
    }


    /**
     * Get the permission information for the specified photo.
     *
     * This method requires authentication with 'read' permission.
     *
     * @param photoId The photo id
     * @return The Permissions object
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Permissions getPerms(String photoId) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PERMS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject permsElement = response.getData().getJSONObject("perms");
        Permissions permissions = new Permissions();
        permissions.setId(permsElement.getString("id"));
        permissions.setPublicFlag("1".equals(permsElement.getString("ispublic")));
        permissions.setFriendFlag("1".equals(permsElement.getString("isfriend")));
        permissions.setFamilyFlag("1".equals(permsElement.getString("isfamily")));
        permissions.setComment(permsElement.getString("permcomment"));
        permissions.setAddmeta(permsElement.getString("permaddmeta"));
        return permissions;
    }


    /**
     * Get a collection of recent photos.
     *
     * This method does not require authentication.
     *
     * @see com.googlecode.flickrjandroid.photos.Extras
     * @param extras Set of extra-fields
     * @param perPage The number of photos per page
     * @param page The page offset
     * @return A collection of Photo objects
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public PhotoList getRecent(Set<String> extras, int perPage, int page) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_RECENT));
        parameters.add(new Parameter("api_key", apiKey));

        if (extras != null && !extras.isEmpty()) {
            parameters.add(new Parameter(Extras.KEY_EXTRAS, StringUtilities.join(extras, ",")));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return PhotoUtils.createPhotoList(response.getData());
    }

    /**
     * Get the available sizes of a Photo.
     *
     * The calling user must have permission to view the photo.
     *
     * This method uses no authentication.
     *
     * @param photoId The photo ID
     * @return A collection of {@link Size}
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Collection<Size> getSizes(String photoId) throws IOException, FlickrException, JSONException {
        return getSizes(photoId, false);
    }

    /**
     * Get the available sizes of a Photo.
     * 
     * The boolean toggle allows to (api-)sign the call.
     * 
     * This way the calling user can retrieve sizes for <b>his own</b> private photos.
     *
     * @param photoId The photo ID
     * @param sign toggle to allow optionally signing the call (Authenticate)
     * @return A collection of {@link Size}
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Collection<Size> getSizes(String photoId, boolean sign) throws IOException, FlickrException, JSONException {
        List<Size> sizes = new ArrayList<Size>();

        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_SIZES));
        parameters.add(new Parameter("photo_id", photoId));

        if (sign) {
            OAuthUtils.addOAuthToken(parameters);
        } else {
            parameters.add(new Parameter("api_key", apiKey));
        }
        Response response = sign ? transport.postJSON(sharedSecret, parameters) : 
            transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject sizesElement = response.getData().getJSONObject("sizes");
        JSONArray sizeNodes = sizesElement.optJSONArray("size");
        for (int i = 0; sizeNodes != null && i < sizeNodes.length(); i++) {
            JSONObject sizeElement = sizeNodes.getJSONObject(i);
            Size size = new Size();
            size.setLabel(sizeElement.getString("label"));
            size.setWidth(sizeElement.getString("width"));
            size.setHeight(sizeElement.getString("height"));
            size.setSource(sizeElement.getString("source"));
            size.setUrl(sizeElement.getString("url"));
            sizes.add(size);
        }
        return sizes;
    }


    /**
     * Get the collection of untagged photos.
     *
     * This method requires authentication with 'read' permission.
     *
     * @param perPage
     * @param page
     * @return A Collection of Photos
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public PhotoList getUntagged(int perPage, int page)
        throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_UNTAGGED));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return PhotoUtils.createPhotoList(response.getData());
    }


    /**
     * Returns a list of your geo-tagged photos.
     *
     * This method requires authentication with 'read' permission.
     *
     * @param minUploadDate Minimum upload date. Photos with an upload date greater than or equal to this value will be returned. Set to null to not specify a date.
     * @param maxUploadDate Maximum upload date. Photos with an upload date less than or equal to this value will be returned. Set to null to not specify a date.
     * @param minTakenDate Minimum taken date. Photos with an taken date greater than or equal to this value will be returned. Set to null to not specify a date.
     * @param maxTakenDate Maximum taken date. Photos with an taken date less than or equal to this value will be returned. Set to null to not specify a date.
     * @param privacyFilter Return photos only matching a certain privacy level. Valid values are:
     * <ul>
     * <li>1 public photos</li>
     * <li>2 private photos visible to friends</li>
     * <li>3 private photos visible to family</li>
     * <li>4 private photos visible to friends & family</li>
     * <li>5 completely private photos</li>
     * </ul>
     * Set to 0 to not specify a privacy Filter.
     *
     * @see com.googlecode.flickrjandroid.photos.Extras
     * @param sort The order in which to sort returned photos. Deafults to date-posted-desc. The possible values are: date-posted-asc, date-posted-desc, date-taken-asc, date-taken-desc, interestingness-desc, and interestingness-asc.
     * @param extras A set of Strings controlling the extra information to fetch for each returned record. Currently supported fields are: license, date_upload, date_taken, owner_name, icon_server, original_format, last_update, geo. Set to null or an empty set to not specify any extras.
     * @param perPage Number of photos to return per page. If this argument is 0, it defaults to 100. The maximum allowed value is 500.
     * @param page The page of results to return. If this argument is 0, it defaults to 1.
     * @return photos
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public PhotoList getWithGeoData(
        Date minUploadDate, Date maxUploadDate,
        Date minTakenDate, Date maxTakenDate,
        int privacyFilter, String sort, Set<String> extras, int perPage, int page) 
        throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_WITH_GEO_DATA));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        if (minUploadDate != null) {
            parameters.add(new Parameter("min_upload_date", minUploadDate.getTime() / 1000L));
        }
        if (maxUploadDate != null) {
            parameters.add(new Parameter("max_upload_date", maxUploadDate.getTime() / 1000L));
        }
        if (minTakenDate != null) {
            parameters.add(new Parameter("min_taken_date", minTakenDate.getTime() / 1000L));
        }
        if (maxTakenDate != null) {
            parameters.add(new Parameter("max_taken_date", maxTakenDate.getTime() / 1000L));
        }
        if (privacyFilter > 0) {
            parameters.add(new Parameter("privacy_filter", privacyFilter));        	
        }
        if (sort != null) {
            parameters.add(new Parameter("sort", sort));
        }
        if (extras != null && !extras.isEmpty()) {
            parameters.add(new Parameter("extras", StringUtilities.join(extras, ",")));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return PhotoUtils.createPhotoList(response.getData());
    }


    /**
     * Returns a list of your photos which haven't been geo-tagged.
     *
     * This method requires authentication with 'read' permission.
     *
     * @param minUploadDate Minimum upload date. Photos with an upload date greater than or equal to this value will be returned. Set to null to not specify a date.
     * @param maxUploadDate Maximum upload date. Photos with an upload date less than or equal to this value will be returned. Set to null to not specify a date.
     * @param minTakenDate Minimum taken date. Photos with an taken date greater than or equal to this value will be returned. Set to null to not specify a date.
     * @param maxTakenDate Maximum taken date. Photos with an taken date less than or equal to this value will be returned. Set to null to not specify a date.
     * @param privacyFilter Return photos only matching a certain privacy level. Valid values are:
     * <ul>
     * <li>1 public photos</li>
     * <li>2 private photos visible to friends</li>
     * <li>3 private photos visible to family</li>
     * <li>4 private photos visible to friends & family</li>
     * <li>5 completely private photos</li>
     * </ul>
     * Set to 0 to not specify a privacy Filter.
     *
     * @see com.googlecode.flickrjandroid.photos.Extras
     * @param sort The order in which to sort returned photos. Deafults to date-posted-desc. The possible values are: date-posted-asc, date-posted-desc, date-taken-asc, date-taken-desc, interestingness-desc, and interestingness-asc.
     * @param extras A set of Strings controlling the extra information to fetch for each returned record. Currently supported fields are: license, date_upload, date_taken, owner_name, icon_server, original_format, last_update, geo. Set to null or an empty set to not specify any extras.
     * @param perPage Number of photos to return per page. If this argument is 0, it defaults to 100. The maximum allowed value is 500.
     * @param page The page of results to return. If this argument is 0, it defaults to 1.
     * @return a photo list
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public PhotoList getWithoutGeoData(Date minUploadDate, Date maxUploadDate, Date minTakenDate, Date maxTakenDate, int privacyFilter, String sort, Set<String> extras, int perPage, int page) 
    throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_WITHOUT_GEO_DATA));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        if (minUploadDate != null) {
            parameters.add(new Parameter("min_upload_date", minUploadDate.getTime() / 1000L));
        }
        if (maxUploadDate != null) {
            parameters.add(new Parameter("max_upload_date", maxUploadDate.getTime() / 1000L));
        }
        if (minTakenDate != null) {
            parameters.add(new Parameter("min_taken_date", minTakenDate.getTime() / 1000L));
        }
        if (maxTakenDate != null) {
            parameters.add(new Parameter("max_taken_date", maxTakenDate.getTime() / 1000L));
        }
        if (privacyFilter > 0) {
            parameters.add(new Parameter("privacy_filter", privacyFilter));        	
        }
        if (sort != null) {
            parameters.add(new Parameter("sort", sort));
        }
        if (extras != null && !extras.isEmpty()) {
            parameters.add(new Parameter("extras", StringUtilities.join(extras, ",")));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return PhotoUtils.createPhotoList(response.getData());
    }


    /**
     * Return a list of your photos that have been recently created or which have been recently modified. 
     * Recently modified may mean that the photo's metadata (title, description, tags) may have been changed or a comment has been added (or just modified somehow :-)
     *
     * This method requires authentication with 'read' permission.
     *
     * @see com.googlecode.flickrjandroid.photos.Extras
     * @param minDate Date indicating the date from which modifications should be compared. Must be given.
     * @param extras A set of Strings controlling the extra information to fetch for each returned record. Currently supported fields are: license, date_upload, date_taken, owner_name, icon_server, original_format, last_update, geo. Set to null or an empty set to not specify any extras.
     * @param perPage Number of photos to return per page. If this argument is 0, it defaults to 100. The maximum allowed value is 500.
     * @param page The page of results to return. If this argument is 0, it defaults to 1.
     * @return a list of photos
     * @throws IOException 
     * @throws FlickrException
     * @throws JSONException 
     */
    public PhotoList recentlyUpdated(Date minDate, Set<String> extras, int perPage, int page) 
    throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_RECENTLY_UPDATED));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        parameters.add(new Parameter("min_date", minDate.getTime() / 1000L));

        if (extras != null && !extras.isEmpty()) {
            parameters.add(new Parameter("extras", StringUtilities.join(extras, ",")));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return PhotoUtils.createPhotoList(response.getData());
    }

    /**
     * Remove a tag from a photo.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param tagId The tag ID
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void removeTag(String tagId) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_REMOVE_TAG));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("tag_id", tagId));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Search for photos which match the given search parameters.
     *
     * @param params The search parameters
     * @param perPage The number of photos to show per page
     * @param page The page offset
     * @return A PhotoList
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public PhotoList search(SearchParameters params, int perPage, int page)
        throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_SEARCH));
        boolean sign = OAuthUtils.hasSigned();
        if (sign) {
            parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
            OAuthUtils.addOAuthToken(parameters);
        } else { 
            parameters.add(new Parameter("api_key", apiKey));
        }

        parameters.addAll(params.getAsParameters());

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", "" + perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", "" + page));
        }

        Response response = sign ? transport.postJSON(sharedSecret, parameters) 
                : transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return PhotoUtils.createPhotoList(response.getData());
    }

    /**
     * Search for interesting photos using the Flickr Interestingness algorithm.
     *
     * @param params Any search parameters
     * @param perPage Number of items per page
     * @param page The page to start on
     * @return A PhotoList
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public PhotoList searchInterestingness(SearchParameters params, int perPage, int page)
        throws IOException, FlickrException, JSONException {
        PhotoList photos = new PhotoList();

        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_INTERESTINGNESS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.addAll(params.getAsParameters());

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response
                .getErrorMessage());
        }
        JSONObject photosElement = response.getData().getJSONObject("photos");
        photos.setPage(photosElement.getInt("page"));
        photos.setPages(photosElement.getInt("pages"));
        photos.setPerPage(photosElement.getInt("perpage"));
        photos.setTotal(photosElement.getInt("total"));

        JSONArray photoNodes = photosElement.optJSONArray("photo");
        for (int i = 0; photoNodes != null && i < photoNodes.length(); i++) {
            JSONObject photoElement = photoNodes.getJSONObject(i);
            Photo photo = new Photo();
            photo.setId(photoElement.getString("id"));

            User owner = new User();
            owner.setId(photoElement.getString("owner"));
            photo.setOwner(owner);

            photo.setSecret(photoElement.getString("secret"));
            photo.setServer(photoElement.getString("server"));
            photo.setFarm(photoElement.getString("farm"));
            photo.setTitle(photoElement.getString("title"));
            photo.setPublicFlag("1".equals(photoElement
                .getString("ispublic")));
            photo.setFriendFlag("1".equals(photoElement
                .getString("isfriend")));
            photo.setFamilyFlag("1".equals(photoElement
                .getString("isfamily")));
            photos.add(photo);
        }
        return photos;
    }

    /**
     * Set the content type of a photo.
     *
     * This method requires authentication with 'write' permission.
     *
     * @see com.googlecode.flickrjandroid.Flickr#CONTENTTYPE_PHOTO
     * @see com.googlecode.flickrjandroid.Flickr#CONTENTTYPE_SCREENSHOT
     * @see com.googlecode.flickrjandroid.Flickr#CONTENTTYPE_OTHER
     * @param photoId The photo ID
     * @param contentType The contentType to set
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void setContentType(String photoId, String contentType) throws IOException,
            FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_SET_CONTENTTYPE));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("content_type", contentType));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Set the dates for the specified photo.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param photoId The photo ID
     * @param datePosted The date the photo was posted or null
     * @param dateTaken The date the photo was taken or null
     * @param dateTakenGranularity The granularity of the taken date or null
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void setDates(String photoId, Date datePosted, Date dateTaken, String dateTakenGranularity)
            throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_SET_DATES));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photo_id", photoId));

        if (datePosted != null) {
            parameters.add(new Parameter("date_posted", new Long(datePosted.getTime() / 1000)));
        }

        if (dateTaken != null) {
            parameters.add(new Parameter("date_taken", ((DateFormat)DATE_FORMATS.get()).format(dateTaken)));
        }

        if (dateTakenGranularity != null) {
            parameters.add(new Parameter("date_taken_granularity", dateTakenGranularity));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Set the meta data for the photo.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param photoId The photo ID
     * @param title The new title
     * @param description The new description
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void setMeta(String photoId, String title, String description)
        throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_SET_META));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("title", title));
        parameters.add(new Parameter("description", description));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Set the permissions for the photo.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param photoId The photo ID
     * @param permissions The permissions object
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void setPerms(String photoId, Permissions permissions) throws IOException,
            FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_SET_PERMS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("is_public", permissions.isPublicFlag() ? "1" : "0"));
        parameters.add(new Parameter("is_friend", permissions.isFriendFlag() ? "1" : "0"));
        parameters.add(new Parameter("is_family", permissions.isFamilyFlag() ? "1" : "0"));
        parameters.add(new Parameter("perm_comment", permissions.getComment()));
        parameters.add(new Parameter("perm_addmeta", permissions.getAddmeta()));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Set the safety level (adultness) of a photo.<p>
     *
     * This method requires authentication with 'write' permission.
     *
     * @param photoId The photo ID
     * @param safetyLevel The safety level of the photo or null
     * @param hidden Hidden from public searches or not or null
     * @see com.googlecode.flickrjandroid.Flickr#SAFETYLEVEL_SAFE
     * @see com.googlecode.flickrjandroid.Flickr#SAFETYLEVEL_MODERATE
     * @see com.googlecode.flickrjandroid.Flickr#SAFETYLEVEL_RESTRICTED
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void setSafetyLevel(String photoId, String safetyLevel, Boolean hidden)
            throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_SET_SAFETYLEVEL));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photo_id", photoId));

        if (safetyLevel != null) {
            parameters.add(new Parameter("safety_level", safetyLevel));
        }

        if (hidden != null) {
            parameters.add(new Parameter("hidden", hidden.booleanValue() ? "1" : "0"));
        }
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    /**
     * Set the tags for a photo.
     *
     * This method requires authentication with 'write' permission.
     *
     * @param photoId The photo ID
     * @param tags The tag array
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public void setTags(String photoId, String[] tags)
        throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_SET_TAGS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("photo_id", photoId));
        parameters.add(new Parameter("tags", StringUtilities.join(tags, " ", true)));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transport.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(
                response.getErrorCode(),
                response.getErrorMessage()
            );
        }
    }

    /**
     * Get the photo for the specified ID.
     * Currently maps to the getInfo() method.
     *
     * @param id The ID
     * @return The Photo
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Photo getPhoto(String id) throws IOException, FlickrException, JSONException {
        return getPhoto(id, null);
    }

    /**
     * Get the photo for the specified ID with the given secret.
     * Currently maps to the getInfo() method.
     *
     * @param id The ID
     * @param secret The secret
     * @return The Photo
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Photo getPhoto(String id, String secret) throws IOException, FlickrException, JSONException {
        return getInfo(id, secret);
    }

    /**
     * Request an image from the Flickr-servers.<br>
     * Callers must close the stream upon completion.<p>
     *
     * At {@link Size} you can find constants for the available sizes.
     *
     * @param photo A photo-object
     * @param size The Size
     * @return InputStream The InputStream
     * @throws IOException
     * @throws FlickrException
     */
    public InputStream getImageAsStream(Photo photo, int size)
      throws IOException, FlickrException {
        String urlStr = "";
        if (size == Size.SQUARE) {
            urlStr = photo.getSmallSquareUrl();
        } if (size == Size.LARGE_SQUARE) {
            urlStr = photo.getLargeSquareUrl();
        } else if (size == Size.THUMB) {
            urlStr = photo.getThumbnailUrl();
        } else if (size == Size.SMALL) {
            urlStr = photo.getSmallUrl();
        } if (size == Size.SMALL_320) {
            urlStr = photo.getSmall320Url();
        } else if (size == Size.MEDIUM) {
            urlStr = photo.getMediumUrl();
        } else if (size == Size.MEDIUM_640) {
            urlStr = photo.getMedium640Url();
        } else if (size == Size.MEDIUM_800) {
            urlStr = photo.getMedium800Url();
        } else if (size == Size.LARGE) {
            urlStr = photo.getLargeUrl();
        } else if (size == Size.LARGE_1600) {
            urlStr = photo.getLarge1600Url();
        } else if (size == Size.LARGE_2048) {
            urlStr = photo.getLarge2048Url();
        } else if (size == Size.ORIGINAL) {
            urlStr = photo.getOriginalUrl();
        } else {
            throw new FlickrException("0", "Unknown Photo-size");
        }
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (transport instanceof REST) {
            if (((REST) transport).isProxyAuth()) {
                conn.setRequestProperty(
                    "Proxy-Authorization",
                    "Basic " + ((REST) transport).getProxyCredentials()
                );
            }
        }
        conn.connect();
        return conn.getInputStream();
    }
}
