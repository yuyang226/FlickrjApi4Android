package com.yuyang226.flickr.oauth;



import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.aetrion.flickr.Parameter;

/**
 * a simple program to get flickr token and token secret.
 * 
 * @author Mark Zang, Toby Yu
 * 
 */
public class OAuthUtils {
	private static final String PARAMETER_SEPARATOR = "&";
	private static final String NAME_VALUE_SEPARATOR = "=";
	/** Default charsets */
	public static final String DEFAULT_CONTENT_CHARSET = "ISO-8859-1";

	private static final String HMAC_SHA1 = "HmacSHA1";

	public static final String ENC = "UTF-8";
	
	public static final String REQUEST_METHOD_GET = "GET";
	
	
	public static String getSignature(String url, List<Parameter> parameters, String apiSecret)
	throws UnsupportedEncodingException, NoSuchAlgorithmException,
	InvalidKeyException {
		String baseString = getRequestBaseString(REQUEST_METHOD_GET, url, parameters);
		System.out.println("Signature Base String: " + baseString);
		return hmacsha1(baseString, apiSecret);
	}
	
	public static String getRequestBaseString(String oauth_request_method, String url, List<Parameter> parameters) throws UnsupportedEncodingException {
		StringBuffer result = new StringBuffer();
		result.append(oauth_request_method);
		result.append(PARAMETER_SEPARATOR);
		result.append(URLEncoder.encode(url, ENC));
		result.append(PARAMETER_SEPARATOR);
		
		return result.toString() + URLEncoder.encode(format(parameters, ENC), ENC);
	}
	
	public static String hmacsha1(String data, String key) throws IllegalStateException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		byte[] byteHMAC = null;
		Mac mac = Mac.getInstance(HMAC_SHA1);
		SecretKeySpec spec = new SecretKeySpec((key + PARAMETER_SEPARATOR).getBytes(), HMAC_SHA1);
		mac.init(spec);
		byteHMAC = mac.doFinal(data.getBytes(ENC));

		return new String(Base64.encodeBase64(byteHMAC));
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

	public static String decode (final String content, final String encoding) {
		try {
			return URLDecoder.decode(content,
					encoding != null ? encoding : DEFAULT_CONTENT_CHARSET);
		} catch (UnsupportedEncodingException problem) {
			throw new IllegalArgumentException(problem);
		}
	}

	public static String encode (final String content, final String encoding) {
		try {
			return URLEncoder.encode(content,
					encoding != null ? encoding : DEFAULT_CONTENT_CHARSET);
		} catch (UnsupportedEncodingException problem) {
			throw new IllegalArgumentException(problem);
		}
	}
	
	public static void addOAuthNonce(final List<Parameter> parameters) {
		parameters.add(new Parameter("oauth_nonce", Long.toString(System.nanoTime())));
	}
	
	public static void addOAuthSignatureMethod(final List<Parameter> parameters) {
		parameters.add(new Parameter("oauth_signature_method", "HMAC-SHA1"));
	}
	
	public static void addOAuthTimestamp(final List<Parameter> parameters) {
		parameters.add(new Parameter("oauth_timestamp", String.valueOf((System.currentTimeMillis() / 1000))));
	}
	
	public static void addOAuthVersion(final List<Parameter> parameters) {
		parameters.add(new Parameter("oauth_version", "1.0"));
	}

}
