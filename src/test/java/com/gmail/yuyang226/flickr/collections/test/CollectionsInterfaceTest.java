/**
 * 
 */
package com.gmail.yuyang226.flickr.collections.test;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.RequestContext;
import com.gmail.yuyang226.flickr.oauth.OAuth;
import com.gmail.yuyang226.flickr.oauth.OAuthToken;
import com.gmail.yuyang226.flickr.org.json.JSONException;
import com.gmail.yuyang226.flickr.people.User;
import com.gmail.yuyang226.flickr.test.AbstractFlickrTest;

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
	 * Test method for {@link com.gmail.yuyang226.flickr.collections.CollectionsInterface#getInfo(java.lang.String)}.
	 * @throws FlickrException 
	 * @throws JSONException 
	 * @throws IOException 
	 */
	@Test
	public void testGetInfo() throws IOException, JSONException, FlickrException {
		Assert.assertNotNull(f.getCollectionsInterface().getInfo("8263632-72157602429085472"));
	}

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.collections.CollectionsInterface#getTree(java.lang.String, java.lang.String)}.
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
