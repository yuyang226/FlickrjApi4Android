package com.yuyang226.flickr.oauth;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.Parameter;
import com.aetrion.flickr.REST;

/**
 * a simple program to get flickr token and token secret.
 * 
 * @author Mark Zang
 * 
 */
public class OAuthForFlickr {
	private static final String PARAMETER_SEPARATOR = "&";
	private static final String NAME_VALUE_SEPARATOR = "=";
	/** Default charsets */
	public static final String DEFAULT_CONTENT_CHARSET = "ISO-8859-1";

	private static String key = "da4fadd0084ea1799ad33048f0d6a5c5";
	private static String secret = "186b04791439c326";

	private static final String HMAC_SHA1 = "HmacSHA1";

	private static final String ENC = "UTF-8";
	
	private static final String KEY_OAUTH_CALLBACK_CONFIRMED = "oauth_callback_confirmed";
	private static final String KEY_OAUTH_TOKEN = "oauth_token";
	private static final String KEY_OAUTH_TOKEN_SECRET = "oauth_token_secret";

	private static Base64 base64 = new Base64();

	/**
	 * 
	 * @param url
	 *            the url for "request_token" URLEncoded.
	 * @param params
	 *            parameters string, URLEncoded.
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	private static String getSignature(String url, String params)
	throws UnsupportedEncodingException, NoSuchAlgorithmException,
	InvalidKeyException {
		/**
		 * base has three parts, they are connected by "&": 1) protocol 2) URL
		 * (need to be URLEncoded) 3) Parameter List (need to be URLEncoded).
		 */
		StringBuilder base = new StringBuilder();
		base.append("GET&");
		base.append(url);
		base.append("&");
		base.append(params);
		System.out.println("String for oauth_signature generation:" + base);
		// yea, don't ask me why, it is needed to append a "&" to the end of
		// secret key.
		byte[] keyBytes = (secret + "&").getBytes(ENC);

		SecretKey key = new SecretKeySpec(keyBytes, HMAC_SHA1);

		Mac mac = Mac.getInstance(HMAC_SHA1);
		mac.init(key);

		// encode it, base64 it, change it to string and return.
		return new String(base64.encode(mac.doFinal(base.toString().getBytes(
				ENC))), ENC).trim();
	}

	/**
	 * Returns a String that is suitable for use as an <code>application/x-www-form-urlencoded</code>
	 * list of parameters in an HTTP PUT or HTTP POST.
	 *
	 * @param parameters  The parameters to include.
	 * @param encoding The encoding to use.
	 */
	public static String format (
			final List<Parameter> parameters,
			final String encoding) {
		final StringBuilder result = new StringBuilder();
		for (final Parameter parameter : parameters) {
			final String encodedName = encode(parameter.getName(), encoding);
			final String value = String.valueOf(parameter.getValue());
			final String encodedValue = value != null ? encode(value, encoding) : "";
			if (result.length() > 0)
				result.append(PARAMETER_SEPARATOR);
			result.append(encodedName);
			result.append(NAME_VALUE_SEPARATOR);
			result.append(encodedValue);
		}
		return result.toString();
	}

	private static String decode (final String content, final String encoding) {
		try {
			return URLDecoder.decode(content,
					encoding != null ? encoding : DEFAULT_CONTENT_CHARSET);
		} catch (UnsupportedEncodingException problem) {
			throw new IllegalArgumentException(problem);
		}
	}

	private static String encode (final String content, final String encoding) {
		try {
			return URLEncoder.encode(content,
					encoding != null ? encoding : DEFAULT_CONTENT_CHARSET);
		} catch (UnsupportedEncodingException problem) {
			throw new IllegalArgumentException(problem);
		}
	}

	/**
	 * Constructs a {@link URI} using all the parameters. This should be
	 * used instead of
	 * {@link URI#URI(String, String, String, int, String, String, String)}
	 * or any of the other URI multi-argument URI constructors.
	 *
	 * @param scheme
	 *            Scheme name
	 * @param host
	 *            Host name
	 * @param port
	 *            Port number
	 * @param path
	 *            Path
	 * @param query
	 *            Query
	 * @param fragment
	 *            Fragment
	 *
	 * @throws URISyntaxException
	 *             If both a scheme and a path are given but the path is
	 *             relative, if the URI string constructed from the given
	 *             components violates RFC&nbsp;2396, or if the authority
	 *             component of the string is present but cannot be parsed
	 *             as a server-based authority
	 */
	public static URI createURI(
			final String scheme,
			final String host,
			int port,
			final String path,
			final String query,
			final String fragment) throws URISyntaxException {

		StringBuilder buffer = new StringBuilder();
		if (host != null) {
			if (scheme != null) {
				buffer.append(scheme);
				buffer.append("://");
			}
			buffer.append(host);
			if (port > 0) {
				buffer.append(':');
				buffer.append(port);
			}
		}
		if (path == null || !path.startsWith("/")) {
			buffer.append('/');
		}
		if (path != null) {
			buffer.append(path);
		}
		if (query != null) {
			buffer.append('?');
			buffer.append(query);
		}
		if (fragment != null) {
			buffer.append('#');
			buffer.append(fragment);
		}
		return new URI(buffer.toString());
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static void main(String[] args) {
		try {
			REST transport = new REST();
			transport.setHost("www.flickr.com");
			transport.setPort(80);
			transport.setPath("/services/oauth/request_token");
			
			List<Parameter> parameters = new ArrayList<Parameter>();
			parameters.add(new Parameter("oauth_callback", "http://localhost:8888/flickrcallback.jsp"));
			parameters.add(new Parameter("oauth_consumer_key", key));
			parameters.add(new Parameter("oauth_nonce", String.valueOf((int)(Math.random() * 100000000))));
			parameters.add(new Parameter("oauth_signature_method", "HMAC-SHA1"));
			parameters.add(new Parameter("oauth_timestamp", String.valueOf((System.currentTimeMillis() / 1000))));
			parameters.add(new Parameter("oauth_version", "1.0"));

			// generate the oauth_signature
			String signature = getSignature(URLEncoder.encode(
					"http://www.flickr.com/services/oauth/request_token", ENC),
					URLEncoder.encode(format(parameters, ENC), ENC));
			System.out.println("Signature: " + signature);
			// This method call must be signed.
			parameters.add(new Parameter("oauth_signature", signature));

			// generate URI which lead to access_token and token_secret.
			URI uri = createURI("http", "www.flickr.com", -1,
					"/services/oauth/request_token",
					format(parameters, ENC), null);

			System.out.println("Get Token and Token Secrect from:"
					+ uri.toString());

			Map<String, String> response = transport.getData(transport.getPath(), parameters);
			if (response.isEmpty()) {
				throw new FlickrException("Empty Response", "Empty Response");
			}
			
			if (response.containsKey(KEY_OAUTH_CALLBACK_CONFIRMED) == false
					|| Boolean.valueOf(response.get(KEY_OAUTH_CALLBACK_CONFIRMED)) == false) {
				throw new FlickrException("Error", "Invalid response: " + response);
			}
			String token = response.get(KEY_OAUTH_TOKEN);
			String token_secret = response.get(KEY_OAUTH_TOKEN_SECRET);
			System.out.println(response);
			System.out.println("oauth URL: http://www.flickr.com/services/oauth/authorize?oauth_token=" + token);
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
