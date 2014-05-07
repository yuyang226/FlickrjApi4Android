/**
 * 
 */
package com.googlecode.flickrjandroid.photosets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.json.JSONException;
import org.junit.Test;

import com.googlecode.flickrandroid.test.AbstractFlickrTest;
import com.googlecode.flickrandroid.test.TestConstants;
import com.googlecode.flickrjandroid.FlickrException;

/**
 * @author yayu
 *
 */
public class PhotosetsInterfaceTest extends AbstractFlickrTest {
	private static final String PHOTOSET_ID = "72157602790288345";
	private static final String CHARLES_PHOTO_SET_ID = "72157626046645016"; 

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.photosets.PhotosetsInterface#getInfo(java.lang.String)}.
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws FlickrException 
	 */
	@Test
	public void testGetInfo() throws FlickrException, IOException, JSONException {
		Photoset photoset = f.getPhotosetsInterface().getInfo(PHOTOSET_ID);
		assertNotNull(photoset);
		assertEquals(PHOTOSET_ID, photoset.getId());
	}
	
	@Test
	public void testGetInfoAuth() throws FlickrException, IOException, JSONException {
		Photoset photoset = f.getPhotosetsInterface().getInfo(CHARLES_PHOTO_SET_ID);
		assertNotNull(photoset);
		assertEquals(CHARLES_PHOTO_SET_ID, photoset.getId());
		assertTrue( photoset.getPhotoCount() > 50 );
	}
	
	@Test
	public void testGetList() throws IOException, FlickrException, JSONException {
		Photosets photosets = f.getPhotosetsInterface().getList(TestConstants.USER_ID);
		assertNotNull(photosets);
		assertFalse(photosets.getPhotosets().isEmpty());
	}
	
	@Test
	public void  testGetList2() throws IOException, FlickrException, JSONException {
		Photosets photosets = f.getPhotosetsInterface().getList(TestConstants.USER_ID, 2, 1);
		assertNotNull(photosets);
		assertFalse(photosets.getPhotosets().isEmpty());
		assertTrue( photosets.getPhotosets().size() <= 2);
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.photosets.PhotosetsInterface#getPhotos(java.lang.String, java.util.Set, int, int, int)}.
	 * @throws JSONException 
	 * @throws FlickrException 
	 * @throws IOException 
	 */
	@Test
	public void testGetPhotosStringSetOfStringIntIntInt() throws IOException, FlickrException, JSONException {
		Photoset photoset = f.getPhotosetsInterface().getPhotos(PHOTOSET_ID, 
				null, -1, 10, 1);
		assertNotNull(photoset);
		assertFalse(photoset.getPhotoList().isEmpty());
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.photosets.PhotosetsInterface#getPhotos(java.lang.String, int, int)}.
	 * @throws JSONException 
	 * @throws FlickrException 
	 * @throws IOException 
	 */
	@Test
	public void testGetPhotosStringIntInt() throws IOException, FlickrException, JSONException {
		Photoset photoset = f.getPhotosetsInterface().getPhotos(PHOTOSET_ID, 10, 1);
		assertNotNull(photoset);
		assertFalse(photoset.getPhotoList().isEmpty());
	}

}
