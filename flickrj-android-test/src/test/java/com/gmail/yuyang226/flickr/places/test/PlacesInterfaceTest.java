/**
 * 
 */
package com.gmail.yuyang226.flickr.places.test;

import java.io.IOException;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;

import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.places.PlacesList;
import com.gmail.yuyang226.flickr.test.AbstractFlickrTest;

/**
 * @author yayu
 *
 */
public class PlacesInterfaceTest extends AbstractFlickrTest {

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.places.PlacesInterface#findByLatLon(double, double, int)}.
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws FlickrException 
	 */
	@Test
	public void testFindByLatLon() throws FlickrException, IOException, JSONException {
		PlacesList pList = f.getPlacesInterface().findByLatLon(37.76513627957266, -122.42020770907402);
		Assert.assertNotNull(pList);
		Assert.assertFalse(pList.isEmpty());
	}

}
