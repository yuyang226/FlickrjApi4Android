/**
 * 
 */
package com.googlecode.flickrjandroid.oauth;

import java.io.Serializable;

import com.googlecode.flickrjandroid.people.User;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class OAuth implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private OAuthToken token;
    private User user;

    /**
     * 
     */
    public OAuth() {
        super();
    }

    /**
     * @return the token
     */
    public OAuthToken getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(OAuthToken token) {
        this.token = token;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OAuth [token=" + token 
                + ", user=" + user + "]";
    }
    
}
