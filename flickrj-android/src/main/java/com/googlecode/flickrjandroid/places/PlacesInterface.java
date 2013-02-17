package com.googlecode.flickrjandroid.places;

import java.io.IOException;
import java.text.DateFormat;
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
import com.googlecode.flickrjandroid.photos.SearchParameters;
import com.googlecode.flickrjandroid.tags.Tag;
import com.googlecode.flickrjandroid.util.StringUtilities;

/**
 * Lookup Flickr Places.<p>
 *
 * Announcement on places from yahoo:<p>
<PRE>
From: kellan <kellan@yahoo-inc.com>
Date: Fri, 11 Jan 2008 15:57:59 -0800
Subject: [yws-flickr] Flickr and "Place IDs"

At Flickr we've got a really big database that lists a significant 
percentage of the places that exist in the world, and a few that don't. 
When you geotag a photo we try to identify the "place" (neighborhood, 
village, city, county, state, or country) where the photo was taken. And 
we assign that photo a "place ID".

A place ID is a globally unique identifier for a place on Earth.  A city 
has a place ID, so do counties, states, and countries.  Even some 
neighborhoods and landmarks have them, though Flickr isn't currently 
tracking those. And we're starting to expose these place IDs around Flickr.

### Place IDs and flickr.photos.search()

The Flickr API method flickr.photos.search() now accepts place_id as an 
argument.  Along with all of the other parameters you can
search on you can now scope your search to a given place.   Historically 
you've been able to pass bounding boxes to the API, but calculating the 
right bounding box for a city is tricky, and you can get noise and bad 
results around the edge.  Now you can pass a single non-ambiguous string 
and get photos geotagged in San Francisco, CA, or Ohio, or Beijing. 
(kH8dLOubBZRvX_YZ, LtkqzVqbApjAbJxv, and wpK7URqbAJnWB90W respectively)

The documentation has been updated at:
http://www.flickr.com/services/api/flickr.photos.search.html

### Sources of Place IDs

Place IDs are now returned from a number of source:
   * flickr.photos.getInfo will return place IDs for geotagged photos
   * available as a microformat on the appropriate Places page
   * flickr.places.resolvePlaceURL, and flickr.places.resolvePlaceId are
available for round tripping Flickr Places URLs.

http://www.flickr.com/services/api/flickr.photos.getInfo.html
http://www.flickr.com/services/api/flickr.places.resolvePlaceURL.html
http://www.flickr.com/services/api/flickr.places.resolvePlaceId.html

### More Place IDs

Right now you can also place IDs in the places URL, and pass them to the 
map like so:

   * http://flickr.com/places/wpK7URqbAJnWB90W
   * http://flickr.com/map?place_id=kH8dLOubBZRvX_YZ

### Place IDs elsewhere

The especially eagle-eyed among you might recognize Place IDs.  Upcoming 
has been quietly using them for months to uniquely identify their metros.

See events from San Francisco at:
http://upcoming.yahoo.com/place/kH8dLOubBZRvX_YZ

See photos from San Francisco at: http://flickr.com/places/kH8dLOubBZRvX_YZ

Additionally Yahoo's skunkworks project FireEagle will also support 
place IDs.

And yes, there is more work to do, but we're exciting about this as a start.

Thanks,
-kellan
</PRE>

 * @author mago
 * @version $Id: PlacesInterface.java,v 1.9 2009/07/03 22:31:40 x-mago Exp $
 */
public class PlacesInterface {
    private static final String METHOD_FIND = "flickr.places.find";
    private static final String METHOD_FIND_BY_LATLON = "flickr.places.findByLatLon";
    private static final String METHOD_RESOLVE_PLACE_ID = "flickr.places.resolvePlaceId";
    private static final String METHOD_RESOLVE_PLACE_URL = "flickr.places.resolvePlaceURL";
    private static final String METHOD_GET_CHILDREN_WITH_PHOTOS_PUBLIC = "flickr.places.getChildrenWithPhotosPublic";
    private static final String METHOD_GET_INFO = "flickr.places.getInfo";
    private static final String METHOD_GET_INFO_BY_URL = "flickr.places.getInfoByUrl";
    private static final String METHOD_GET_PLACETYPES = "flickr.places.getPlaceTypes";
    private static final String METHOD_GET_SHAPEHISTORY = "flickr.places.getShapeHistory";
    private static final String METHOD_GET_TOP_PLACES_LIST = "flickr.places.getTopPlacesList";
    private static final String METHOD_PLACES_FOR_BOUNDINGBOX = "flickr.places.placesForBoundingBox";
    private static final String METHOD_PLACES_FOR_CONTACTS = "flickr.places.placesForContacts";
    private static final String METHOD_PLACES_FOR_TAGS = "flickr.places.placesForTags";
    private static final String METHOD_PLACES_FOR_USER = "flickr.places.placesForUser";
    private static final String METHOD_TAGS_FOR_PLACE = "flickr.places.tagsForPlace";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public PlacesInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Return a list of place IDs for a query string.
     *
     * The flickr.places.find method is not a geocoder.
     * It will round "up" to the nearest place type to 
     * which place IDs apply. For example, if you pass
     * it a street level address it will return the city
     * that contains the address rather than the street,
     * or building, itself.
     *
     * <p>This method does not require authentication.</p>
     *
     * @param query
     * @return PlacesList
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public PlacesList find(String query)
      throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_FIND));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("query", query));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return parsePlacesList(response.getData());
    }

    /**
     * Return a place ID for a latitude, longitude and accuracy triple.<p>
     *
     * The flickr.places.findByLatLon method is not meant to be a (reverse)
     * geocoder in the traditional sense. It is designed to allow users to find
     * photos for "places" and will round up to the nearest place type to which
     * corresponding place IDs apply.<p>
     *
     * For example, if you pass it a street level coordinate it will return the
     * city that contains the point rather than the street, or building, itself.<p>
     *
     * It will also truncate latitudes and longitudes to three decimal points.<p>
     *
     * The gory details :
     *
     * This is (most of) the same magic that is performed when you geotag one
     * of your photos on the site itself. We know that at the neighbourhood
     * level this can get messy and not always return the correct location.<p>
     *
     * At the city level things are much better but there may still be some
     * gotchas floating around. Sometimes it's as simple as a bug and other
     * times it is an issue of two competing ideas of where a place "is".<p>
     *
     * This comes with the territory and we are eager to identify and wherever
     * possible fix the problems so when you see something that looks wrong
     * please be gentle :-)<p>
     *
     * (Reports of incorrect places sent to mailing list will not be
     * ignored but it would be better if you could use the forums for that sort
     * of thing.)<p>
     *
     * Also, as we do on the site if we can not identify a location for a point 
     * as a specific accuracy we pop up the stack and try again. For example, 
     * if we can't find a city for a given set of coordinates we try instead to 
     * locate the state.<p>
     *
     * As mentioned above, this method is not designed to serve as a general
     * purpose (reverse) geocoder which is partly reflected by the truncated
     * lat/long coordinates.<p>
     *
     * If you think that three decimal points are the cause of wonky results
     * locating photos for places, we are happy to investigate but until then
     * it should be All Good (tm).
     *
     * <p>This method does not require authentication.</p>
     *
     * @param latitude The latitude whose valid range is -90 to 90.
     *        Anything more than 4 decimal places will be truncated.
     * @param longitude The longitude whose valid range is -180 to 180.
     *        Anything more than 4 decimal places will be truncated.
     * @param accuracy
     * @return A PlacesList
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public PlacesList findByLatLon(
        double latitude,
        double longitude,
        int accuracy
    ) throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_FIND_BY_LATLON));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("lat", "" + latitude));
        parameters.add(new Parameter("lon", "" + longitude));
        parameters.add(new Parameter("accuracy", "" + accuracy));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return parsePlacesList(response.getData());
    }

    /**
     * <p>Return a list of locations with public photos that are parented by a 
     * Where on Earth (WOE) or Places ID.</p>
     *
     * <p>This method does not require authentication.</p>
     *
     * @param placeId A Flickr Places ID. Can be null. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @param woeId A Where On Earth (WOE) ID. Can be null. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @return List of Places
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public PlacesList getChildrenWithPhotosPublic(String placeId, String woeId)
      throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_CHILDREN_WITH_PHOTOS_PUBLIC));
        parameters.add(new Parameter("api_key", apiKey));

        if (placeId != null) {
            parameters.add(new Parameter("place_id", placeId));
        }
        if (woeId != null) {
            parameters.add(new Parameter("woe_id", woeId));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return parsePlacesList(response.getData());
    }

    /**
     * Get informations about a place.
     *
     * <p>This method does not require authentication.</p>
     *
     * @param placeId A Flickr Places ID. Optioal, can be null. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @param woeId A Where On Earth (WOE) ID. Optional, can be null. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @return A Location
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public Location getInfo(String placeId, String woeId)
      throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        Location loc = new Location();
        parameters.add(new Parameter("method", METHOD_GET_INFO));
        parameters.add(new Parameter("api_key", apiKey));

        if (placeId != null) {
            parameters.add(new Parameter("place_id", placeId));
        }
        if (woeId != null) {
            parameters.add(new Parameter("woe_id", woeId));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject locationElement = response.getData().getJSONObject("place");
        return parseLocation(locationElement);
    }

    /**
     * Lookup information about a place, by its flickr.com/places URL.
     *
     * <p>This method does not require authentication.</p>
     *
     * @param url
     * @return A Location
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public Location getInfoByUrl(String url)
      throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_INFO_BY_URL));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("url", url));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject locationElement = response.getData().getJSONObject("place");
        return parseLocation(locationElement);
    }

    /**
     * Fetches a list of available place types for Flickr.
     *
     * <p>This method does not require authentication.</p>
     *
     * @return A list of placetypes
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public List<PlaceType> getPlaceTypes()
      throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PLACETYPES));
        parameters.add(new Parameter("api_key", apiKey));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        List<PlaceType> placeTypeList = new ArrayList<PlaceType>();
        JSONObject placeTypeElement = response.getData().getJSONObject("place_types");
        JSONArray placeTypeNodes = placeTypeElement.optJSONArray("place_type");
        for (int i = 0; placeTypeNodes != null && i < placeTypeNodes.length(); i++) {
            placeTypeElement = placeTypeNodes.getJSONObject(i);
            PlaceType placeType = new PlaceType();
            placeType.setPlaceTypeId(placeTypeElement.getString("id"));
            placeType.setPlaceTypeName(placeTypeElement.getString("_content"));
            placeTypeList.add(placeType);
        }
        return placeTypeList;
    }

    /**
     * Return an historical list of all the shape data generated for a
     * Places or Where on Earth (WOE) ID.<p>
     *
     * <p>This method does not require authentication.</p>
     *
     * <p>Not working. As it was not possible to find any results.
     * Not even the ones, that have been described in the announcement of this feature.</p>
     *
     * @param placeId A Flickr Places ID. Optional, can be null.
     * @param woeId A Where On Earth (WOE) ID. Optional, can be null.
     * @return A list of shapes
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public List<Object> getShapeHistory(String placeId, String woeId) 
    throws FlickrException, IOException, JSONException {
        List<Object> shapeList = new ArrayList<Object>();
        List<Parameter> parameters = new ArrayList<Parameter>();
        Location loc = new Location();
        parameters.add(new Parameter("method", METHOD_GET_SHAPEHISTORY));
        parameters.add(new Parameter("api_key", apiKey));

        if (placeId != null) {
            parameters.add(new Parameter("place_id", placeId));
        }
        if (woeId != null) {
            parameters.add(new Parameter("woe_id", woeId));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject shapeElement = response.getData();
        return shapeList;
    }

    /**
     * Return the top 100 most geotagged places for a day.
     *
     * <p>This method does not require authentication.</p>
     *
     * @param placeType
     * @param date Optional, can be null. The default is yesterday.
     * @param placeId A Flickr Places ID. Optional, can be null.
     * @param woeId A Where On Earth (WOE) ID. Optional, can be null.
     * @return PlacesList
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public PlacesList getTopPlacesList(
        int placeType,
        Date date,
        String placeId,
        String woeId
    ) throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_TOP_PLACES_LIST));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("place_type", intPlaceTypeToString(placeType)));
        if (placeId != null) {
            parameters.add(new Parameter("place_id", placeId));
        }
        if (woeId != null) {
            parameters.add(new Parameter("woe_id", woeId));
        }
        if (date != null) {
            parameters.add(new Parameter("date", ((DateFormat) SearchParameters.DATE_FORMATS.get()).format(date)));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return parsePlacesList(response.getData());
    }

    /**
     * Return all the locations of a matching place type for a bounding box.<p>
     *
     * The maximum allowable size of a bounding box (the distance between the SW and NE corners) is governed by the place type you are requesting. Allowable sizes are as follows:
     * <ul>
     * <li>neighbourhood: 3km (1.8mi)</li>
     * <li>locality: 7km (4.3mi)</li>
     * <li>county: 50km (31mi)</li>
     * <li>region: 200km (124mi)</li>
     * <li>country: 500km (310mi)</li>
     * <li>continent: 1500km (932mi)</li>
     * </ul>
     *
     * <p>This method does not require authentication.</p>
     *
     * @param bbox
     * @param placeType
     * @return A PlacesList
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public PlacesList placesForBoundingBox(
        int placeType,
        String bbox
    ) throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        PlacesList placesList = new PlacesList();
        parameters.add(new Parameter("method", METHOD_PLACES_FOR_BOUNDINGBOX));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("place_type", intPlaceTypeToString(placeType)));
        parameters.add(new Parameter("bbox", bbox));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return parsePlacesList(response.getData());
    }

    /**
     * Return a list of the top 100 unique places clustered by a given placetype for a user's contacts. 
     *
     * @param placeType Use Type-constants at {@link Place}
     * @param placeId A Flickr Places ID. Optional, can be null.
     * @param woeId A Where On Earth (WOE) ID. Optional, can be null.
     * @param threshold The minimum number of photos that a place type must have to be included. If the number of photos is lowered then the parent place type for that place will be used. Optional, can be null.
     * @param contacts Search your contacts. Either 'all' or 'ff' for just friends and family. (Optional, default is all)
     * @return A PlacesList
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public PlacesList placesForContacts(
        int placeType,
        String placeId,
        String woeId,
        String threshold,
        String contacts
    ) throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_PLACES_FOR_CONTACTS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("place_type", intPlaceTypeToString(placeType)));
        if (placeId != null) {
            parameters.add(new Parameter("place_id", placeId));
        }
        if (woeId != null) {
            parameters.add(new Parameter("woe_id", woeId));
        }
        if (threshold != null) {
            parameters.add(new Parameter("threshold", threshold));
        }
        if (contacts != null) {
            parameters.add(new Parameter("contacts", contacts));
        }

        OAuthUtils.addOAuthToken(parameters);
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return parsePlacesList(response.getData());
    }

    /**
     * Return a list of the top 100 unique places clustered by a given
     * placetype for set of tags or machine tags.
     *
     * <p>This method does not require authentication.</p>
     *
     * @param placeTypeId
     * @param woeId A Where On Earth (WOE) ID. Optional, can be null. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @param placeId A Flickr Places ID. Optional, can be null. (While optional, you must pass either a valid Places ID or a WOE ID.)
     * @param threshold The minimum number of photos that a place type must have to be included. If the number of photos is lowered then the parent place type for that place will be used. Optional, can be null.
     * @param tags A String-array of Tags. Photos with one or more of the tags listed will be returned. Optional, can be null.
     * @param tagMode Either 'any' for an OR combination of tags, or 'all' for an AND combination. Defaults to 'any' if not specified. Optional, can be null.
     * @param machineTags
     * @param machineTagMode Either 'any' for an OR combination of tags, or 'all' for an AND combination. Defaults to 'any' if not specified. Optional, can be null.
     * @param minUploadDate Optional, can be null.
     * @param maxUploadDate Optional, can be null.
     * @param minTakenDate Optional, can be null.
     * @param maxTakenDate Optional, can be null.
     * @return A PlacesList
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public PlacesList placesForTags(
        int placeTypeId,
        String woeId,
        String placeId,
        String threshold,
        String[] tags,
        String tagMode,
        String machineTags,
        String machineTagMode,
        Date minUploadDate, Date maxUploadDate,
        Date minTakenDate, Date maxTakenDate
    ) throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_PLACES_FOR_TAGS));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("place_type_id", placeTypeId));
        if (woeId != null) {
            parameters.add(new Parameter("woe_id", woeId));
        }
        if (placeId != null) {
            parameters.add(new Parameter("place_id", placeId));
        }
        if (threshold != null) {
            parameters.add(new Parameter("threshold", threshold));
        }
        if (tags != null) {
            parameters.add(new Parameter("tags", StringUtilities.join(tags, ",")));
        }
        if (tagMode != null) {
            parameters.add(new Parameter("tag_mode", tagMode));
        }
        if (machineTags != null) {
            parameters.add(new Parameter("machine_tags", machineTags));
        }
        if (machineTagMode != null) {
            parameters.add(new Parameter("machine_tag_mode", machineTagMode));
        }
        if (minUploadDate != null) {
            parameters.add(new Parameter("min_upload_date", new Long(minUploadDate.getTime() / 1000L)));
        }
        if (maxUploadDate != null) {
            parameters.add(new Parameter("max_upload_date", new Long(maxUploadDate.getTime() / 1000L)));
        }
        if (minTakenDate != null) {
            parameters.add(new Parameter("min_taken_date", ((DateFormat) SearchParameters.MYSQL_DATE_FORMATS.get()).format(minTakenDate)));
        }
        if (maxTakenDate != null) {
            parameters.add(new Parameter("max_taken_date", ((DateFormat) SearchParameters.MYSQL_DATE_FORMATS.get()).format(maxTakenDate)));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return parsePlacesList(response.getData());
    }

    /**
     * Return a list of the top 100 unique places clustered by a given placetype for a user.
     *
     * @param placeType Use Type-constants at {@link Place}
     * @param woeId A Where On Earth (WOE) ID. Optional, can be null.
     * @param placeId A Flickr Places ID. Optional, can be null.
     * @param threshold The minimum number of photos that a place type must have to be included. If the number of photos is lowered then the parent place type for that place will be used. Optional, can be null.
     * @param minUploadDate Optional, can be null.
     * @param maxUploadDate Optional, can be null.
     * @param minTakenDate Optional, can be null.
     * @param maxTakenDate Optional, can be null.
     * @return A PlacesList
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public PlacesList placesForUser(
        int placeType,
        String woeId,
        String placeId,
        String threshold,
        Date minUploadDate, Date maxUploadDate,
        Date minTakenDate, Date maxTakenDate
    ) throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        
        parameters.add(new Parameter("method", METHOD_PLACES_FOR_USER));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        parameters.add(new Parameter("place_type", intPlaceTypeToString(placeType)));
        if (placeId != null) {
            parameters.add(new Parameter("place_id", placeId));
        }
        if (woeId != null) {
            parameters.add(new Parameter("woe_id", woeId));
        }
        if (threshold != null) {
            parameters.add(new Parameter("threshold", threshold));
        }
        if (minUploadDate != null) {
            parameters.add(new Parameter("min_upload_date", new Long(minUploadDate.getTime() / 1000L)));
        }
        if (maxUploadDate != null) {
            parameters.add(new Parameter("max_upload_date", new Long(maxUploadDate.getTime() / 1000L)));
        }
        if (minTakenDate != null) {
            parameters.add(new Parameter("min_taken_date", ((DateFormat) SearchParameters.MYSQL_DATE_FORMATS.get()).format(minTakenDate)));
        }
        if (maxTakenDate != null) {
            parameters.add(new Parameter("max_taken_date", ((DateFormat) SearchParameters.MYSQL_DATE_FORMATS.get()).format(maxTakenDate)));
        }

        OAuthUtils.addOAuthToken(parameters);
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        return parsePlacesList(response.getData());
    }
    
    private PlacesList parsePlacesList(JSONObject data) throws JSONException {
        PlacesList placesList = new PlacesList();
        JSONObject placesElement = data.getJSONObject("places");
        JSONArray placesNodes = placesElement.optJSONArray("place");
        placesList.setPage("1");
        placesList.setPages("1");
        placesList.setLatitude(placesElement.optDouble("latitude", -999.0));
        placesList.setLongitude(placesElement.optDouble("longitude", -999.0));
        placesList.setPerPage(placesNodes != null ? placesNodes.length() : 0);
        placesList.setTotal(placesList.getPerPage());
        for (int i = 0; placesNodes != null && i < placesNodes.length(); i++) {
            JSONObject placeElement = placesNodes.getJSONObject(i);
            placesList.add(parsePlace(placeElement));
        }
        return placesList;
    }

    /**
     * Return a list of the top 100 unique tags for a Flickr
     * Places or Where on Earth (WOE) ID.
     *
     * <p>This method does not require authentication.</p>
     *
     * @param woeId A Where On Earth (WOE) ID. Optional, can be null.
     * @param placeId A Flickr Places ID. Optional, can be null.
     * @param minUploadDate Optional, can be null.
     * @param maxUploadDate Optional, can be null.
     * @param minTakenDate Optional, can be null.
     * @param maxTakenDate Optional, can be null.
     * @return A list of Tags
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public List<Tag> tagsForPlace(
        String woeId,
        String placeId,
        Date minUploadDate, Date maxUploadDate,
        Date minTakenDate, Date maxTakenDate
    ) throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        List<Tag> tagsList = new ArrayList<Tag>();
        parameters.add(new Parameter("method", METHOD_TAGS_FOR_PLACE));
        parameters.add(new Parameter("api_key", apiKey));

        if (woeId != null) {
            parameters.add(new Parameter("woe_id", woeId));
        }
        if (placeId != null) {
            parameters.add(new Parameter("place_id", placeId));
        }
        if (minUploadDate != null) {
            parameters.add(new Parameter("min_upload_date", new Long(minUploadDate.getTime() / 1000L)));
        }
        if (maxUploadDate != null) {
            parameters.add(new Parameter("max_upload_date", new Long(maxUploadDate.getTime() / 1000L)));
        }
        if (minTakenDate != null) {
            parameters.add(new Parameter("min_taken_date", ((DateFormat) SearchParameters.MYSQL_DATE_FORMATS.get()).format(minTakenDate)));
        }
        if (maxTakenDate != null) {
            parameters.add(new Parameter("max_taken_date", ((DateFormat) SearchParameters.MYSQL_DATE_FORMATS.get()).format(maxTakenDate)));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject tagsElement = response.getData().getJSONObject("tags");
        JSONArray tagsNodes = tagsElement.optJSONArray("tag");
        for (int i = 0; tagsNodes != null && i < tagsNodes.length(); i++) {
            JSONObject tagElement = tagsNodes.getJSONObject(i);
            Tag tag = new Tag();
            tag.setCount(tagElement.getString("count"));
            tag.setValue(tagElement.getString("_content"));
            tagsList.add(tag);
        }
        return tagsList;
    }

    private Location parseLocation(JSONObject locationElement) throws JSONException {
        Location location = new Location();
        JSONObject localityElement = locationElement.getJSONObject("locality");
        JSONObject countyElement = locationElement.getJSONObject("county");
        JSONObject regionElement = locationElement.getJSONObject("region");
        JSONObject countryElement = locationElement.getJSONObject("country");

        location.setPlaceId(locationElement.getString("place_id"));
        //location.setName(locationElement.getString("name"));
        location.setPlaceUrl(locationElement.getString("place_url"));
        location.setWoeId(locationElement.getString("woeid"));
        location.setLatitude(locationElement.getString("latitude"));
        location.setLongitude(locationElement.getString("longitude"));
        location.setPlaceType(stringPlaceTypeToInt(locationElement.getString("place_type")));

        location.setLocality(
            parseLocationPlace(localityElement, Place.TYPE_LOCALITY)
        );
        location.setCounty(
            parseLocationPlace(countyElement, Place.TYPE_COUNTY)
        );
        location.setRegion(
            parseLocationPlace(regionElement, Place.TYPE_REGION)
        );
        location.setCountry(
            parseLocationPlace(countryElement, Place.TYPE_COUNTRY)
        );
        return location;
    }

    private Place parseLocationPlace(JSONObject element, int type) throws JSONException {
        Place place = new Place();
        if (element.has("name")) {
        	place.setName(element.getString("name"));
        } else {
        	place.setName(element.getString("_content"));
        }
        place.setPlaceId(element.getString("place_id"));
        place.setPlaceUrl(element.getString("place_url"));
        place.setWoeId(element.getString("woeid"));
        place.setLatitude(element.getString("latitude"));
        place.setLongitude(element.getString("longitude"));
        place.setPlaceType(type);
        return place;
    }

    private Place parsePlace(JSONObject placeElement) throws JSONException {
        Place place = new Place();
        place.setPlaceId(placeElement.getString("place_id"));
        place.setPlaceUrl(placeElement.getString("place_url"));
        place.setWoeId(placeElement.getString("woeid"));
        place.setLatitude(placeElement.getString("latitude"));
        place.setLongitude(placeElement.getString("longitude"));
        place.setPhotoCount(placeElement.optString("photo_count"));
        if (placeElement.has("place_type")) {
        	place.setPlaceType(placeElement.getString("place_type"));
        } else {
        	// Now the place-Id is directly available
        	String placeId = placeElement.optString("place_type_id");
            try {
				place.setPlaceType(intPlaceTypeToString(Integer.parseInt(placeId)));
			} catch (Exception e) {
				//Ignore
			}
        }
        
        if (placeElement.has("name")) {
        	place.setName(placeElement.getString("name"));
        } else {
        	place.setName(placeElement.getString("_content"));
        }
        
        return place;
    }

    private int stringPlaceTypeToInt(String typeString) {
        int placeType = 0;
        if (typeString.equals("locality")) {
            placeType = Place.TYPE_LOCALITY;
        } else if (typeString.equals("county")) {
            placeType = Place.TYPE_COUNTY;
        } else if (typeString.equals("region")) {
            placeType = Place.TYPE_REGION;
        } else if (typeString.equals("country")) {
            placeType = Place.TYPE_COUNTRY;
        } else if (typeString.equals("continent")) {
            placeType = Place.TYPE_CONTINENT;
        } else if (typeString.equals("neighbourhood")) {
            placeType = Place.TYPE_NEIGHBOURHOOD;
        }
        return placeType;
    }

    public String intPlaceTypeToString(int placeType) throws FlickrException {
        String placeTypeStr = "";
        if (placeType == Place.TYPE_COUNTRY) {
            placeTypeStr = "country";
        } else if (placeType == Place.TYPE_REGION) {
            placeTypeStr = "region";
        } else if (placeType == Place.TYPE_LOCALITY) {
            placeTypeStr = "locality";
        } else if (placeType == Place.TYPE_CONTINENT) {
            placeTypeStr = "continent";
        } else if (placeType == Place.TYPE_NEIGHBOURHOOD) {
            placeTypeStr = "neighbourhood";
        } else {
            throw new FlickrException("33", "Not a valid place type");
        }
        return placeTypeStr;
    }
}
