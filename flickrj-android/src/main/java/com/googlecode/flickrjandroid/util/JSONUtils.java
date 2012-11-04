/**
 * 
 */
package com.googlecode.flickrjandroid.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public final class JSONUtils {

    /**
     * 
     */
    private JSONUtils() {
        super();
    }
    
    public static String getChildValue(JSONObject jObject, String key) throws JSONException {
        if (jObject.has(key)) {
            return jObject.getJSONObject(key).optString("_content");
        }
        return null;
    }

}
