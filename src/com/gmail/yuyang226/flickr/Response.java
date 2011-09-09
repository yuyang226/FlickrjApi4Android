/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.gmail.yuyang226.flickr;

import com.gmail.yuyang226.flickr.org.json.JSONException;
import com.gmail.yuyang226.flickr.org.json.JSONObject;

/**
 * @author Anthony Eden
 */
public interface Response {

    void parse(String rawMessage) throws JSONException;

    boolean isError();

    String getErrorCode();

    String getErrorMessage();
    
    JSONObject getData();
    
    String getRawResponse();
    
}
