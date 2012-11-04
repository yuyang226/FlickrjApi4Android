/**
 * 
 */
package com.googlecode.flickrjandroid.collections;

import java.io.IOException;
import java.util.ArrayList;
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
import com.googlecode.flickrjandroid.photosets.Photoset;
import com.googlecode.flickrjandroid.util.JSONUtils;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class CollectionsInterface {
    private static final String METHOD_GET_INFO = "flickr.collections.getInfo";
    private static final String METHOD_GET_TREE = "flickr.collections.getTree";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public CollectionsInterface(String apiKey, String sharedSecret, Transport transport) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transport;
    }
    
    /**
     * Returns information for a single collection. Currently can only be called by the collection owner, this may change.
     * @param collectionId The ID of the collection to fetch information for.
     * @return an instance of <code>Collection</code> for the given collection ID, or null if can not be found.
     * @throws FlickrException 
     * @throws JSONException 
     * @throws IOException 
     */
    public Collection getInfo(String collectionId) throws IOException, JSONException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_INFO));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        parameters.add(new Parameter("collection_id", collectionId));
        OAuthUtils.addOAuthToken(parameters);
        
        Response response = this.transportAPI.postJSON(this.sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject collectionElement = response.getData().getJSONObject("collection");
        Collection collection = new Collection();
        collection.setId(collectionElement.getString("id"));
        collection.setTitle(JSONUtils.getChildValue(collectionElement, "title"));
        collection.setDescription(JSONUtils.getChildValue(collectionElement, "description"));
        collection.setChildCount(collectionElement.optInt("child_count"));
        collection.setIconLarge(collectionElement.optString("iconlarge"));
        collection.setIconSmall(collectionElement.optString("iconsmall"));
        collection.setServer(collectionElement.optString("server"));
        collection.setSecret(collectionElement.optString("secret"));
        //TODO process iconphotos
        /*"iconphotos": { 
              "photo": [
                { "id": "503410350", "owner": "8308954@N06", "secret": "4375e55284", "server": "200", "farm": 1, "title": "Taking Pictures", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
                { "id": "1576385789", "owner": "8308954@N06", "secret": "296cc2c17b", "server": "2142", "farm": 3, "title": "Fireworks", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
                { "id": "1576456481", "owner": "8308954@N06", "secret": "22690d008c", "server": "2081", "farm": 3, "title": "Fireworks", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
                { "id": "1576408333", "owner": "8308954@N06", "secret": "5fdfc4e936", "server": "2037", "farm": 3, "title": "Fireworks", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
                { "id": "498073478", "deleted": 1 },
                { "id": "498094539", "deleted": 1 },
                { "id": "498065404", "deleted": 1 },
                { "id": "1345799589", "deleted": 1 },
                { "id": "498079026", "owner": "8308954@N06", "secret": "f1d1e4ecea", "server": "224", "farm": 1, "title": "auto_sh_2007_cars006", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
                { "id": "498118829", "owner": "8308954@N06", "secret": "66855d6ece", "server": "202", "farm": 1, "title": "小猫熊", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
                { "id": "498120563", "owner": "8308954@N06", "secret": "cea33cd7de", "server": "194", "farm": 1, "title": "Ostrich", "ispublic": 1, "isfriend": 0, "isfamily": 0 },
                { "id": "1295631338", "owner": "8308954@N06", "secret": "db5d984122", "server": "1091", "farm": 2, "title": "Entering a Shop", "ispublic": 1, "isfriend": 0, "isfamily": 0 }
              ] }*/
        return collection;
    }
    
    /**
     * Returns a tree (or sub tree) of collections belonging to a given user.
     * @param collectionId The ID of the collection to fetch a tree for, or zero to fetch the root collection. Defaults to zero.
     * @param userId The ID of the account to fetch the collection tree for. Deafults to the calling user.
     * @return a list of <code>Collection</code> instances
     * @throws JSONException 
     * @throws IOException 
     * @throws FlickrException 
     */
    public List<Collection> getTree(String collectionId, String userId) throws IOException, JSONException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_TREE));
        boolean signed = OAuthUtils.hasSigned();
        if (signed) {
            parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        } else {
            parameters.add(new Parameter("api_key", apiKey));
        }
        if (collectionId != null) {
            parameters.add(new Parameter("collection_id", collectionId));
        }
        if (userId != null) {
            parameters.add(new Parameter("user_id", userId));
        }
        
        if (signed) {
            OAuthUtils.addOAuthToken(parameters);
        }

        Response response = signed ? transportAPI.postJSON(sharedSecret, parameters) : transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        List<Collection> result = new ArrayList<Collection>();
        JSONObject collectionsElement = response.getData().getJSONObject("collections");
        JSONArray collectionNode = collectionsElement.optJSONArray("collection");
        for (int i = 0; collectionNode != null && i < collectionNode.length(); i++) {
            JSONObject collectionElement = collectionNode.getJSONObject(i);
            result.add(createCollection(collectionElement));
        }
        return result;
    }
    
    private Collection createCollection(JSONObject jObject) throws JSONException {
        Collection col = new Collection();
        col.setId(jObject.getString("id"));
        col.setTitle(jObject.getString("title"));
        col.setDescription(jObject.getString("description"));
        
        JSONArray setElements = jObject.optJSONArray("set");
        for (int i = 0; setElements != null && i < setElements.length(); i++) {
            JSONObject setElement = setElements.getJSONObject(i);
            Photoset set = new Photoset();
            set.setId(setElement.getString("id"));
            set.setTitle(setElement.getString("title"));
            set.setDescription(setElement.getString("description"));
            col.getPhotoSets().add(set);
        }
        
        JSONArray colElements = jObject.optJSONArray("collection");
        for (int i = 0; colElements != null && i < colElements.length(); i++) {
            JSONObject colElement = colElements.getJSONObject(i);
            col.getCollections().add(createCollection(colElement));
        }
        return col;
    }

}
