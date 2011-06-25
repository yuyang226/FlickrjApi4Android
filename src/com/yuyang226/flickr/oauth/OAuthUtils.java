package com.yuyang226.flickr.oauth;



import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.aetrion.flickr.Parameter;

/**
 * a simple program to get flickr token and token secret.
 * 
 * @author Mark Zang
 * 
 */
public class OAuthUtils {
	private static final String PARAMETER_SEPARATOR = "&";
	private static final String NAME_VALUE_SEPARATOR = "=";
	/** Default charsets */
	public static final String DEFAULT_CONTENT_CHARSET = "ISO-8859-1";

	private static final String HMAC_SHA1 = "HmacSHA1";

	public static final String ENC = "UTF-8";
	
	

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
	public static String getSignature(String url, String params, String apiSecret)
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
		byte[] keyBytes = (apiSecret + "&").getBytes(ENC);

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


}
