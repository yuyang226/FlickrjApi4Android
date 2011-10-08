/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.gmail.yuyang226.flickr;

import org.json.JSONException;
import org.json.JSONObject;

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
