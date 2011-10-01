/**
 * 
 */
package com.gmail.yuyang226.flickr.galleries.test;

import static org.junit.Assert.fail;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.org.json.JSONException;
import com.gmail.yuyang226.flickr.test.AbstractFlickrTest;
import com.gmail.yuyang226.flickr.test.TestConstants;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class GalleriesInterfaceTest extends AbstractFlickrTest {

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.galleries.GalleriesInterface#getList(java.lang.String, int, int)}.
	 * @throws JSONException 
	 * @throws FlickrException 
	 * @throws IOException 
	 */
	@Test
	public void testGetList() throws IOException, FlickrException, JSONException {
		Assert.assertNotNull(f.getGalleriesInterface().getList(TestConstants.USER_ID, 0, 0));
	}

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.galleries.GalleriesInterface#getPhotos(java.lang.String, java.util.Set, int, int)}.
	 * @throws JSONException 
	 * @throws FlickrException 
	 * @throws IOException 
	 */
	@Test
	public void testGetPhotos() throws IOException, FlickrException, JSONException {
		Assert.assertNotNull(f.getGalleriesInterface().getPhotos("8263632-72157623259986613", null, 0, 0));
	}

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.galleries.GalleriesInterface#getListForPhoto(java.lang.String, int, int)}.
	 * @throws JSONException 
	 * @throws FlickrException 
	 * @throws IOException 
	 */
	@Test
	public void testGetListForPhoto() throws IOException, FlickrException, JSONException {
		Assert.assertNotNull(f.getGalleriesInterface().getListForPhoto("5116571240", 0, 0));
	}

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.galleries.GalleriesInterface#create(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Ignore
	@Test
	public void testCreate() {
		fail("Not yet implemented");
	}

}
