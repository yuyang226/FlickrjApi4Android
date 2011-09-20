/**
 * 
 */
package com.gmail.yuyang226.flickr.stats.test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.org.json.JSONException;
import com.gmail.yuyang226.flickr.photos.PhotoList;
import com.gmail.yuyang226.flickr.test.AbstractFlickrTest;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class StatsInterfaceTest extends AbstractFlickrTest {

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.stats.StatsInterface#getPopularPhotos(java.util.Date, java.lang.String, int, int)}.
	 * @throws FlickrException 
	 * @throws JSONException 
	 * @throws IOException 
	 */
	@Test
	public void testGetPopularPhotos() throws IOException, JSONException, FlickrException {
		PhotoList photos = f.getStatsInterface().getPopularPhotos(null, null, 0, 0);
		Assert.assertNotNull(photos);
		Assert.assertFalse(photos.isEmpty());
	}

}
