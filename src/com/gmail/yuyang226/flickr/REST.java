/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.gmail.yuyang226.flickr;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import com.gmail.yuyang226.flickr.oauth.OAuthUtils;
import com.gmail.yuyang226.flickr.org.json.JSONException;
import com.gmail.yuyang226.flickr.util.Base64;
import com.gmail.yuyang226.flickr.util.DebugInputStream;
import com.gmail.yuyang226.flickr.util.IOUtilities;
import com.gmail.yuyang226.flickr.util.StringUtilities;
import com.gmail.yuyang226.flickr.util.UrlUtilities;

/**
 * Transport implementation using the REST interface.
 *
 * @author Anthony Eden
 * @version $Id: REST.java,v 1.26 2009/07/01 22:07:08 x-mago Exp $
 */
public class REST extends Transport {

	private static final String UTF8 = "UTF-8";
	public static final String PATH = "/services/rest/";
	private boolean proxyAuth = false;
	private String proxyUser = "";
	private String proxyPassword = "";

	/**
	 * Construct a new REST transport instance.
	 *
	 * @throws ParserConfigurationException
	 */
	public REST() throws ParserConfigurationException {
		setTransportType(REST);
		setHost(Flickr.DEFAULT_HOST);
		setPath(PATH);
		setResponseClass(RESTResponse.class);
	}

	/**
	 * Construct a new REST transport instance using the specified host endpoint.
	 *
	 * @param host The host endpoint
	 * @throws ParserConfigurationException
	 */
	public REST(String host) throws ParserConfigurationException {
		this();
		setHost(host);
	}

	/**
	 * Construct a new REST transport instance using the specified host and port endpoint.
	 *
	 * @param host The host endpoint
	 * @param port The port
	 * @throws ParserConfigurationException
	 */
	public REST(String host, int port) throws ParserConfigurationException {
		this();
		setHost(host);
		setPort(port);
	}

	/**
	 * Set a proxy for REST-requests.
	 *
	 * @param proxyHost
	 * @param proxyPort
	 */
	public void setProxy(String proxyHost, int proxyPort) {
		System.setProperty("http.proxySet", "true");
		System.setProperty("http.proxyHost", proxyHost);
		System.setProperty("http.proxyPort", "" + proxyPort);
	}

	/**
	 * Set a proxy with authentication for REST-requests.
	 *
	 * @param proxyHost
	 * @param proxyPort
	 * @param username
	 * @param password
	 */
	public void setProxy(
			String proxyHost, int proxyPort,
			String username, String password
	) {
		setProxy (proxyHost, proxyPort);
		proxyAuth = true;
		proxyUser = username;
		proxyPassword = password;
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
	public Response get(String path, List<Parameter> parameters) throws IOException, JSONException {
		parameters.add(new Parameter("nojsoncallback", "1"));
		parameters.add(new Parameter("format", "json"));
		String data = getLine(path, parameters);
		return new RESTResponse(data);
	}

	private InputStream getInputStream(String path, List<Parameter> parameters) throws IOException {
		URL url = UrlUtilities.buildUrl(getHost(), getPort(), path, parameters);
		if (Flickr.debugRequest) System.out.println("GET: " + url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.addRequestProperty("Cache-Control", "no-cache,max-age=0"); 
		conn.addRequestProperty("Pragma", "no-cache"); 
		conn.setRequestMethod("GET");
		if (proxyAuth) {
			conn.setRequestProperty(
					"Proxy-Authorization",
					"Basic " + getProxyCredentials()
			);
		}
		conn.connect();

		InputStream in = null;
		if (Flickr.debugStream) {
			in = new DebugInputStream(conn.getInputStream(), System.out);
		} else {
			in = conn.getInputStream();
		}
		return in;
	}

	/**
	 * Send a GET request to the provided URL with the given parameters, 
	 * then return the response as a String.
	 * @param path
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public String getLine(String path, List<Parameter> parameters) throws IOException {
		InputStream in = null;
		BufferedReader rd = null;
		try {
			in = getInputStream(path, parameters);
			rd = new BufferedReader(new InputStreamReader(in, OAuthUtils.ENC));
			final StringBuffer buf = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				buf.append(line);
			}

			return buf.toString();
		} finally {
			IOUtilities.close(in);
			IOUtilities.close(rd);
		}
	}

	/**
	 * <p>A helper method for sending a GET request to the provided URL with the given parameters, 
	 * then return the response as a Map.</p>
	 * 
	 * <p>Please make sure the response data is a Map before calling this method.</p> 
	 * @param path
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public Map<String, String> getMapData(boolean getRequestMethod, String path, List<Parameter> parameters) throws IOException {
		String data = getRequestMethod ? getLine(path, parameters) : sendPost(path, parameters);
		return getDataAsMap(URLDecoder.decode(data, OAuthUtils.ENC));
	}

	public Map<String, String> getDataAsMap(String data) {
		Map<String, String> result = new HashMap<String, String>();
		if (data != null) {
			for (String string : StringUtilities.split(data, "&")) {
				String[] values = StringUtilities.split(string, "=");
				if (values.length == 2) {
					result.put(values[0], values[1]);
				}
			}
		}
		return result;
	}

	public String sendPost(String path, List<Parameter> parameters) throws IOException{
		HttpURLConnection conn = null;
		DataOutputStream out = null;
		try {
			URL url = UrlUtilities.buildPostUrl(getHost(), getPort(), path);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Cache-Control", "no-cache,max-age=0"); 
			conn.setRequestProperty("Pragma", "no-cache");
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new DataOutputStream(conn.getOutputStream());

			Iterator<?> iter = parameters.iterator();
			while (iter.hasNext()) {
				Parameter p = (Parameter) iter.next();
				out.writeBytes(p.getName());
				out.writeBytes("=");
				try {
					out.writeBytes(
							URLEncoder.encode(
									String.valueOf(p.getValue()),
									UTF8
							)
					);
				} catch (UnsupportedEncodingException e) {
					// Should never happen, but just in case
				}
				if (iter.hasNext()) {
					out.writeBytes("&");
				}
			}
			out.flush();
			out.close();

			if ((conn.getResponseCode() != HttpURLConnection.HTTP_OK)) {
				String errorMessage = readFromStream(conn.getErrorStream());
				throw new IOException("Connection Failed. Response Code: "
						+ conn.getResponseCode() + ", Response Message: " + conn.getResponseMessage()
						+ ", Error: " + errorMessage);
			}

			String result = readFromStream(conn.getInputStream());
			return URLDecoder.decode(result.trim(), OAuthUtils.ENC);
		} finally {
			IOUtilities.close(out);

			if (conn != null)
				conn.disconnect() ;
		}
	}

	private String readFromStream(InputStream input) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(input));
			StringBuffer buffer = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			return buffer.toString();
		}finally {
			IOUtilities.close(input);
			IOUtilities.close(reader);
		}
	}

	/* (non-Javadoc)
	 * @see com.gmail.yuyang226.flickr.Transport#post(java.lang.String, java.util.List, boolean)
	 */
	@Override
	public Response post(String path, List<Parameter> parameters) throws IOException, JSONException {
		String data = sendPost(path, parameters);
		return new RESTResponse(data);
	}

	public boolean isProxyAuth() {
		return proxyAuth;
	}

	/**
	 * Generates Base64-encoded credentials from locally stored
	 * username and password.
	 *
	 * @return credentials
	 */
	public String getProxyCredentials() {
		return new String(
				Base64.encode((proxyUser + ":" + proxyPassword).getBytes())
		);
	}
}
