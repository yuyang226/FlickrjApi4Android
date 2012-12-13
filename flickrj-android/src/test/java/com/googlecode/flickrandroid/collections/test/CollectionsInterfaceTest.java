/**
 * 
 */
package com.googlecode.flickrandroid.collections.test;

import java.io.IOException;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.flickrandroid.test.AbstractFlickrTest;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class CollectionsInterfaceTest extends AbstractFlickrTest{

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        super.setup();
        OAuth auth = new OAuth();
        User user = new User();
        user.setId("8308954@N06");
        user.setUsername("Yang and Yun's Album");
        auth.setToken(new OAuthToken("72157627458295241-83050bfaaeffd445", "07c38a749dc7d36e"));
        RequestContext.getRequestContext().setOAuth(auth);
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.collections.CollectionsInterface#getInfo(java.lang.String)}.
     * @throws FlickrException 
     * @throws JSONException 
     * @throws IOException 
     */
    @Test
    public void testGetInfo() throws IOException, JSONException, FlickrException {
        Assert.assertNotNull(f.getCollectionsInterface().getInfo("8263632-72157602429085472"));
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.collections.CollectionsInterface#getTree(java.lang.String, java.lang.String)}.
     * @throws FlickrException 
     * @throws JSONException 
     * @throws IOException 
     */
    @Test
    public void testGetTree() throws IOException, JSONException, FlickrException {
        Assert.assertNotNull(f.getCollectionsInterface().getTree(null, null));
        RequestContext.getRequestContext().setOAuth(null);
        Assert.assertNotNull(f.getCollectionsInterface().getTree(null, "8308954@N06"));
    }

}
