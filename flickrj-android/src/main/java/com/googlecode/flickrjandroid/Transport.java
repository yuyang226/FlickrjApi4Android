/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickrjandroid;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.xml.sax.SAXException;

import com.googlecode.flickrjandroid.oauth.OAuthInterface;
import com.googlecode.flickrjandroid.oauth.OAuthTokenParameter;
import com.googlecode.flickrjandroid.oauth.OAuthUtils;
import com.googlecode.flickrjandroid.uploader.Uploader;

/**
 * The abstract Transport class provides a common interface for transporting requests to the Flickr servers. Flickr
 * offers several transport methods including REST, SOAP and XML-RPC. FlickrJ currently implements the REST transport
 * and work is being done to include the SOAP transport.
 *
 * @author Matt Ray
 * @author Anthony Eden
 */
public abstract class Transport {

    public static final String REST = "REST";

    private String transportType;
    protected Class<?> responseClass;
    private String path;
    private String host;
    private int port = 80;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transport) {
        this.transportType = transport;
    }

    /**
     * Invoke an HTTP GET request on a remote host.  You must close the InputStream after you are done with.
     *
     * @param path The request path
     * @param parameters The parameters (collection of Parameter objects)
     * @return The Response
     * @throws IOException
     * @throws JSONException
     */
    public abstract Response get(String path, List<Parameter> parameters) throws IOException, JSONException;

    /**
     * Invoke an HTTP POST request on a remote host.
     *
     * @param path The request path
     * @param parameters The parameters (collection of Parameter objects)
     * @return The Response object
     * @throws IOException
     * @throws JSONException
     */
    protected abstract Response post(String path, List<Parameter> parameters) throws IOException, JSONException;
    
    public Response upload(String apiSharedSecret, 
            List<Parameter> parameters) throws IOException, FlickrException, SAXException {
//    	parameters.add(new Parameter("nojsoncallback", "1"));
//		parameters.add(new Parameter("format", "json"));
        OAuthUtils.addOAuthParams(apiSharedSecret, Uploader.URL_UPLOAD, parameters);
        return sendUpload(Uploader.UPLOAD_PATH, parameters);
    }
    
    public Response replace(String apiSharedSecret, 
            List<Parameter> parameters) throws IOException, FlickrException, SAXException {
//    	parameters.add(new Parameter("nojsoncallback", "1"));
//		parameters.add(new Parameter("format", "json"));
        OAuthUtils.addOAuthParams(apiSharedSecret, Uploader.URL_REPLACE, parameters);
        return sendUpload(Uploader.REPLACE_PATH, parameters);
    }
    
    protected abstract Response sendUpload(String path, List<Parameter> parameters) throws IOException, FlickrException, SAXException;
    
    public Response postJSON(String apiSharedSecret, 
            List<Parameter> parameters) throws IOException, JSONException, FlickrException {
        boolean isOAuth = false;
        for (int i = parameters.size() - 1; i >= 0; i--) {
            if (parameters.get(i) instanceof OAuthTokenParameter) {
                isOAuth = true;
                break;
            }
        }
        parameters.add(new Parameter("nojsoncallback", "1"));
        parameters.add(new Parameter("format", "json"));
        if (isOAuth) {
            OAuthUtils.addOAuthParams(apiSharedSecret, OAuthInterface.URL_REST, parameters);
        }
        return post(OAuthInterface.PATH_REST, parameters);
    }

    /**
     * @return Returns the path.
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path The path to set.
     */
    public void setPath(String path) {
        this.path = path;
    }

    public Class<?> getResponseClass() {
        return responseClass;
    }

    public void setResponseClass(Class<?> responseClass) {
        if (responseClass == null) {
            throw new IllegalArgumentException("The response Class cannot be null");
        }
        this.responseClass = responseClass;
    }

}
