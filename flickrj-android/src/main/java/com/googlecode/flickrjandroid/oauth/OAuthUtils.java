package com.googlecode.flickrjandroid.oauth;



import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.Parameter;
import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.uploader.ImageParameter;
import com.googlecode.flickrjandroid.util.Base64;
import com.googlecode.flickrjandroid.util.UrlUtilities;

/**
 * a simple program to get flickr token and token secret.
 * 
 * @author Mark Zang, Toby Yu
 * 
 */
public class OAuthUtils {
    private static final String PARAMETER_SEPARATOR = "&";
    private static final String NAME_VALUE_SEPARATOR = "=";
    
    public static final String ENC = "UTF-8";
    /** Default charsets */
    public static final String DEFAULT_CONTENT_CHARSET = "ISO-8859-1";

    private static final String HMAC_SHA1 = "HmacSHA1";
    
    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_POST = "POST";
    
    private static final Logger logger = LoggerFactory.getLogger(OAuthUtils.class);
    
    public static void addOAuthParams(String apiSharedSecret, String url, List<Parameter> parameters) 
    throws FlickrException {
        addBasicOAuthParams(parameters);
        signPost(apiSharedSecret, url, parameters);
    }
    
    public static void addOAuthToken(List<Parameter> parameters) throws OAuthException {
        OAuth oauth = RequestContext.getRequestContext().getOAuth();
        if (oauth == null || oauth.getToken() == null)
            throw new OAuthException("OAuth token not set");
        parameters.add(new OAuthTokenParameter(oauth.getToken().getOauthToken()));
    }
    
    public static void signGet(String apiSharedSecret, String url, List<Parameter>  parameters) throws FlickrException {
        sign(OAuthUtils.REQUEST_METHOD_GET, url, apiSharedSecret, parameters);
    }
    
    public static void signPost(String apiSharedSecret, String url, List<Parameter>  parameters) throws FlickrException {
        sign(OAuthUtils.REQUEST_METHOD_POST, url, apiSharedSecret, parameters);
    }
    
    public static void sign(String requestMethod, String url, String apiSharedSecret, List<Parameter>  parameters) throws FlickrException {
        OAuth oauth = RequestContext.getRequestContext().getOAuth();
        
        String tokenSecret = oauth != null && oauth.getToken() != null 
        ? oauth.getToken().getOauthTokenSecret() : "";
        // generate the oauth_signature
        String signature = OAuthUtils.getSignature(
                requestMethod, 
                url, 
                parameters,
                apiSharedSecret, tokenSecret);
        // This method call must be signed.
        parameters.add(new Parameter("oauth_signature", signature));
    }
    
    public static boolean hasSigned() {
        OAuth oauth = RequestContext.getRequestContext().getOAuth();
        if (oauth == null)
            return false;
        
        return oauth.getToken() != null;
    }
    
    public static void addBasicOAuthParams(List<Parameter> parameters) {
        OAuthUtils.addOAuthNonce(parameters);
        OAuthUtils.addOAuthTimestamp(parameters);
        OAuthUtils.addOAuthSignatureMethod(parameters);
        OAuthUtils.addOAuthVersion(parameters);
    }
    
    public static String getSignature(String requestMethod, String url, List<Parameter> parameters
            , String apiSecret, String tokenSecret)
    throws FlickrException {
        String baseString;
        try {
            baseString = getRequestBaseString(requestMethod, url.toLowerCase(Locale.US), parameters);
            logger.debug("Generated OAuth Base String: {}", baseString);
            return hmacsha1(baseString, apiSecret, tokenSecret);
        } catch (UnsupportedEncodingException e) {
            throw new FlickrException(e);
        } catch (InvalidKeyException e) {
            throw new FlickrException(e);
        } catch (IllegalStateException e) {
            throw new FlickrException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new FlickrException(e);
        }
    }
    
    public static String getSignature(String url, List<Parameter> parameters
            , String apiSecret, String tokenSecret)
    throws FlickrException {
        return getSignature(REQUEST_METHOD_GET, url, parameters, apiSecret, tokenSecret);
    }
    
    public static String getRequestBaseString(String oauth_request_method, String url, List<Parameter> parameters) throws UnsupportedEncodingException {
        StringBuffer result = new StringBuffer();
        result.append(oauth_request_method);
        result.append(PARAMETER_SEPARATOR);
        result.append(UrlUtilities.encode(url));
        result.append(PARAMETER_SEPARATOR);
        
        Collections.sort(parameters, new Comparator<Parameter>() {

            @Override
            public int compare(Parameter o1, Parameter o2) {
                if (o1 instanceof ImageParameter && (o2 instanceof ImageParameter) == false) {
                    return 1;
                }
                if (o2 instanceof ImageParameter && (o1 instanceof ImageParameter) == false) {
                    return -1;
                }
                
                int result = o1.getName().compareTo(o2.getName());
                if (result == 0) {
                    result = o1.getValue().toString().compareTo(o2.getValue().toString());
                }
                return result;
            }
            
        });
        
        return result.append(UrlUtilities.encode(format(parameters, ENC))).toString();
    }
    
    public static String hmacsha1(String data, String key, String tokenSecret) throws IllegalStateException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] byteHMAC = null;
        Mac mac = Mac.getInstance(HMAC_SHA1);
        if (tokenSecret == null) {
            tokenSecret = "";
        }
        SecretKeySpec spec = new SecretKeySpec((key + PARAMETER_SEPARATOR + tokenSecret).getBytes(), HMAC_SHA1);
        mac.init(spec);
        byteHMAC = mac.doFinal(data.getBytes(ENC));

        return new String(Base64.encode(byteHMAC));
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
            Object valueObj = parameter.getValue();
            if ((parameter instanceof ImageParameter) == false) {
                final String encodedName = UrlUtilities.encode(parameter.getName());
                final String value = String.valueOf(valueObj);
                final String encodedValue = value != null ? UrlUtilities.encode(value) : "";
                if (result.length() > 0)
                    result.append(PARAMETER_SEPARATOR);
                result.append(encodedName);
                result.append(NAME_VALUE_SEPARATOR);
                result.append(encodedValue);
            }
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
    
    private static void addOAuthNonce(final List<Parameter> parameters) {
        parameters.add(new Parameter("oauth_nonce", Long.toString(System.nanoTime())));
    }
    
    private static void addOAuthSignatureMethod(final List<Parameter> parameters) {
        parameters.add(new Parameter("oauth_signature_method", "HMAC-SHA1"));
    }
    
    private static void addOAuthTimestamp(final List<Parameter> parameters) {
        parameters.add(new Parameter("oauth_timestamp", String.valueOf((System.currentTimeMillis() / 1000))));
    }
    
    private static void addOAuthVersion(final List<Parameter> parameters) {
        parameters.add(new Parameter("oauth_version", "1.0"));
    }

}
