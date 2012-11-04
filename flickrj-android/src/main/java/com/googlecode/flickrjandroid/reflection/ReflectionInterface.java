/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickrjandroid.reflection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.Parameter;
import com.googlecode.flickrjandroid.Response;
import com.googlecode.flickrjandroid.Transport;
import com.googlecode.flickrjandroid.util.JSONUtils;

/**
 * Interface for testing the complete implementation of all Flickr-methods.<p>
 *
 * @author Anthony Eden
 * @version $Id: ReflectionInterface.java,v 1.10 2008/01/28 23:01:45 x-mago Exp $
 */
public class ReflectionInterface {

    public static final String METHOD_GET_METHOD_INFO = "flickr.reflection.getMethodInfo";
    public static final String METHOD_GET_METHODS     = "flickr.reflection.getMethods";

    private String apiKey;
    private String sharedSecret;
    private Transport transport;

    /**
     * Construct a ReflectionInterface.
     *
     * @param apiKey The API key
     * @param sharedSecret The Shared Secret
     * @param transport The Transport interface
     */
    public ReflectionInterface(
        String apiKey,
        String sharedSecret,
        Transport transport
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transport = transport;
    }

    /**
     * Get the info for the specified method.
     *
     * @param methodName The method name
     * @return The Method object
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Method getMethodInfo(String methodName) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_METHOD_INFO));
        parameters.add(new Parameter("api_key", apiKey));
        parameters.add(new Parameter("method_name", methodName));
        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONObject methodElement = response.getData().getJSONObject("method");
        Method method = new Method();
        method.setName(methodElement.getString("name"));
        method.setNeedsLogin("1".equals(methodElement.getString("needslogin")));
        method.setNeedsSigning("1".equals(methodElement.getString("needssigning")));
        method.setRequiredPerms(methodElement.optInt("requiredperms"));
        method.setDescription(JSONUtils.getChildValue(methodElement, "description"));
        method.setResponse(JSONUtils.getChildValue(methodElement, "response"));
        method.setExplanation(JSONUtils.getChildValue(methodElement, "explanation"));

        List<Argument> arguments = new ArrayList<Argument>();
        JSONObject argumentsElement = response.getData().getJSONObject("arguments");
        JSONArray argumentElements = argumentsElement.optJSONArray("argument");
        for (int i = 0; argumentElements != null && i < argumentElements.length(); i++) {
            Argument argument = new Argument();
            JSONObject argumentElement = argumentElements.getJSONObject(i);
            argument.setName(argumentElement.getString("name"));
            argument.setOptional("1".equals(argumentElement.getString("optional")));
            argument.setDescription(argumentElement.getString("_content"));
            arguments.add(argument);
        }
        method.setArguments(arguments);

        JSONObject errorsElement = response.getData().getJSONObject("errors");
        List<Error> errors = new ArrayList<Error>();
        JSONArray errorElements = errorsElement.optJSONArray("error");
        for (int i = 0; errorElements != null && i < errorElements.length(); i++) {
            Error error = new Error();
            JSONObject errorElement = errorElements.getJSONObject(i);
            error.setCode(errorElement.getString("code"));
            error.setMessage(errorElement.getString("message"));
            error.setExplaination(errorElement.getString("_content"));
            errors.add(error);
        }
        method.setErrors(errors);

        return method;
    }

    /**
     * Get a list of all methods.
     *
     * @return The method names
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public Collection<String> getMethods() throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_METHODS));
        parameters.add(new Parameter("api_key", apiKey));
        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        JSONObject methodsElement = response.getData().getJSONObject("methods");

        List<String> methods = new ArrayList<String>();
        JSONArray methodElements = methodsElement.optJSONArray("method");
        for (int i = 0; methodElements != null && i < methodElements.length(); i++) {
            JSONObject methodElement = methodElements.getJSONObject(i);
            methods.add(methodElement.getString("_content"));
        }
        return methods;
    }

}
