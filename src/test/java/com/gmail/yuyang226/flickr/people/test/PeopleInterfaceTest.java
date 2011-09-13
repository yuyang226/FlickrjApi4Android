/**
 * 
 */
package com.gmail.yuyang226.flickr.people.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.org.json.JSONException;
import com.gmail.yuyang226.flickr.test.AbstractFlickrTest;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class PeopleInterfaceTest extends AbstractFlickrTest{

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.people.PeopleInterface#PeopleInterface(java.lang.String, java.lang.String, com.gmail.yuyang226.flickr.Transport)}.
	 */
	@Test
	public void testPeopleInterface() {
		Assert.assertNotNull(f.getPeopleInterface());
	}

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.people.PeopleInterface#findByEmail(java.lang.String)}.
	 * @throws JSONException 
	 * @throws FlickrException 
	 * @throws IOException 
	 */
	@Test
	public void testFindByEmail() throws IOException, FlickrException, JSONException {
		Assert.assertNotNull(f.getPeopleInterface().findByEmail("wanyun892@yahoo.cn"));
	}

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.people.PeopleInterface#findByUsername(java.lang.String)}.
	 * @throws JSONException 
	 * @throws FlickrException 
	 * @throws IOException 
	 */
	@Test
	public void testFindByUsername() throws IOException, FlickrException, JSONException {
		Assert.assertNotNull(f.getPeopleInterface().findByUsername("wanyun(Wandy)"));
	}

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.people.PeopleInterface#getInfo(java.lang.String)}.
	 * @throws JSONException 
	 * @throws FlickrException 
	 * @throws IOException 
	 */
	@Test
	public void testGetInfo() throws IOException, FlickrException, JSONException {
		Assert.assertNotNull("Request returned empty photo list", 
				f.getPeopleInterface().getInfo("8308954@N06"));
	}

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.people.PeopleInterface#getPublicGroups(java.lang.String)}.
	 * @throws JSONException 
	 * @throws FlickrException 
	 * @throws IOException 
	 */
	@Test
	public void testGetPublicGroups() throws IOException, FlickrException, JSONException {
		Assert.assertNotNull("Request returned empty photo list", 
				f.getPeopleInterface().getPublicGroups("8308954@N06"));
	}

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.people.PeopleInterface#getPublicPhotos(java.lang.String, int, int)}.
	 * @throws JSONException 
	 * @throws FlickrException 
	 * @throws IOException 
	 */
	@Test
	public void testGetPublicPhotosStringIntInt() throws IOException, FlickrException, JSONException {
		Assert.assertFalse("Request returned empty photo list", 
				f.getPeopleInterface().getPublicPhotos("8308954@N06", 0, 0).isEmpty());
	}

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.people.PeopleInterface#getPublicPhotos(java.lang.String, java.util.Set, int, int)}.
	 * @throws JSONException 
	 * @throws FlickrException 
	 * @throws IOException 
	 */
	@Test
	public void testGetPublicPhotosStringSetOfStringIntInt() throws IOException, FlickrException, JSONException {
		Set<String> extras = new HashSet<String>();
		extras.add("geo");
		Assert.assertFalse("Request returned empty photo list", 
				f.getPeopleInterface().getPublicPhotos("8308954@N06", extras, 0, 0).isEmpty());
	}

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.people.PeopleInterface#getUploadStatus()}.
	 * @throws JSONException 
	 * @throws FlickrException 
	 * @throws IOException 
	 */
	@Test
	public void testGetUploadStatus() throws IOException, FlickrException, JSONException {
		Assert.assertNotNull(f.getPeopleInterface().getUploadStatus());
	}

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.people.PeopleInterface#getPhotos(java.lang.String, java.util.Set, int, int)}.
	 * @throws JSONException 
	 * @throws FlickrException 
	 * @throws IOException 
	 */
	@Test
	public void testGetPhotos() throws IOException, FlickrException, JSONException {
		Assert.assertFalse("Request returned empty photo list", f.getPeopleInterface().getPhotos("8308954@N06", 
				new HashSet<String>(Arrays.asList(new String[]{"owner_name","tags","geo"})), 18, 1).isEmpty());
	}

}
