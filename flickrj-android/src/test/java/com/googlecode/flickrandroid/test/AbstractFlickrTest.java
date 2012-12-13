/**
 * 
 */
package com.googlecode.flickrandroid.test;

import org.junit.After;
import org.junit.Before;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public abstract class AbstractFlickrTest {
    protected Flickr f;

    /**
     * 
     */
    public AbstractFlickrTest() {
        super();
    }
    
    @Before
    public void setup() throws Exception {
        f = new Flickr(TestConstants.FLICKR_API_KEY, TestConstants.FLICKR_API_SECRET);
        OAuth auth = new OAuth();
        User user = new User();
        user.setId(TestConstants.USER_ID);
        auth.setToken(new OAuthToken(TestConstants.OAUTH_TOKEN, TestConstants.OAUTH_TOKEN_SECRET));
        RequestContext.getRequestContext().setOAuth(auth);
    }
    
    @After
    public void tearDown() throws Exception {
        RequestContext.getRequestContext().setOAuth(null);
        RequestContext.resetThreadLocals();
        f = null;
    }

}
