/**
 * 
 */
package com.googlecode.flickrandroid.places.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.json.JSONException;
import org.junit.Ignore;
import org.junit.Test;

import com.googlecode.flickrandroid.test.AbstractFlickrTest;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.places.PlacesList;

/**
 * @author yayu
 *
 */
public class PlacesInterfaceTest extends AbstractFlickrTest {

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.places.PlacesInterface#find(java.lang.String)}.
	 */
	@Test
	@Ignore
	public void testFind() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.places.PlacesInterface#findByLatLon(double, double, int)}.
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws FlickrException 
	 */
	@Test
	public void testFindByLatLon() throws FlickrException, IOException, JSONException {
		PlacesList places = f.getPlacesInterface().findByLatLon(33.629, 73.114,4);
		assertNotNull(places);
		assertFalse(places.isEmpty());
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.places.PlacesInterface#getChildrenWithPhotosPublic(java.lang.String, java.lang.String)}.
	 */
	@Test
	@Ignore
	public void testGetChildrenWithPhotosPublic() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.places.PlacesInterface#getInfo(java.lang.String, java.lang.String)}.
	 */
	@Test
	@Ignore
	public void testGetInfo() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.places.PlacesInterface#getInfoByUrl(java.lang.String)}.
	 */
	@Test
	@Ignore
	public void testGetInfoByUrl() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.places.PlacesInterface#getPlaceTypes()}.
	 */
	@Test
	@Ignore
	public void testGetPlaceTypes() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.places.PlacesInterface#getShapeHistory(java.lang.String, java.lang.String)}.
	 */
	@Test
	@Ignore
	public void testGetShapeHistory() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.places.PlacesInterface#getTopPlacesList(int, java.util.Date, java.lang.String, java.lang.String)}.
	 */
	@Test
	@Ignore
	public void testGetTopPlacesList() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.places.PlacesInterface#placesForBoundingBox(int, java.lang.String)}.
	 */
	@Test
	@Ignore
	public void testPlacesForBoundingBox() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.places.PlacesInterface#placesForContacts(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	@Ignore
	public void testPlacesForContacts() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.places.PlacesInterface#placesForTags(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date, java.util.Date, java.util.Date)}.
	 */
	@Test
	@Ignore
	public void testPlacesForTags() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.places.PlacesInterface#placesForUser(int, java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date, java.util.Date, java.util.Date)}.
	 */
	@Test
	@Ignore
	public void testPlacesForUser() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.places.PlacesInterface#tagsForPlace(java.lang.String, java.lang.String, java.util.Date, java.util.Date, java.util.Date, java.util.Date)}.
	 */
	@Test
	@Ignore
	public void testTagsForPlace() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.places.PlacesInterface#intPlaceTypeToString(int)}.
	 */
	@Test
	@Ignore
	public void testIntPlaceTypeToString() {
		fail("Not yet implemented");
	}

}
