/**
 * 
 */
package com.googlecode.flickrjandroid.oauth;

import java.io.Serializable;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class OAuthToken implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String oauthToken;
    private String oauthTokenSecret;

    /**
     * 
     */
    public OAuthToken() {
        super();
    }

    /**
     * @param oauthToken
     * @param oauthTokenSecret
     */
    public OAuthToken(String oauthToken, String oauthTokenSecret) {
        super();
        this.oauthToken = oauthToken;
        this.oauthTokenSecret = oauthTokenSecret;
    }

    /**
     * @return the oauthToken
     */
    public String getOauthToken() {
        return oauthToken;
    }

    /**
     * @param oauthToken the oauthToken to set
     */
    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    /**
     * @return the oauthTokenSecret
     */
    public String getOauthTokenSecret() {
        return oauthTokenSecret;
    }

    /**
     * @param oauthTokenSecret the oauthTokenSecret to set
     */
    public void setOauthTokenSecret(String oauthTokenSecret) {
        this.oauthTokenSecret = oauthTokenSecret;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((oauthToken == null) ? 0 : oauthToken.hashCode());
        result = prime
                * result
                + ((oauthTokenSecret == null) ? 0 : oauthTokenSecret.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof OAuthToken))
            return false;
        OAuthToken other = (OAuthToken) obj;
        if (oauthToken == null) {
            if (other.oauthToken != null)
                return false;
        } else if (!oauthToken.equals(other.oauthToken))
            return false;
        if (oauthTokenSecret == null) {
            if (other.oauthTokenSecret != null)
                return false;
        } else if (!oauthTokenSecret.equals(other.oauthTokenSecret))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OAuthToken [oauthToken=" + oauthToken + ", oauthTokenSecret="
                + oauthTokenSecret + "]";
    }

}
