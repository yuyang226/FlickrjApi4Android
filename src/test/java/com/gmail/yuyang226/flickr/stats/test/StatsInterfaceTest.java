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
import com.gmail.yuyang226.flickr.stats.AccountStats;
import com.gmail.yuyang226.flickr.stats.DomainList;
import com.gmail.yuyang226.flickr.stats.ReferrerList;
import com.gmail.yuyang226.flickr.stats.Stats;
import com.gmail.yuyang226.flickr.stats.StatsInterface.SORT;
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
		PhotoList photos = f.getStatsInterface().getPopularPhotos(null, SORT.VIEWS, 0, 0);
		Assert.assertNotNull(photos);
		Assert.assertFalse(photos.isEmpty());
	}
	
	@Test
	public void testGetPhotoStats() throws IOException, JSONException, FlickrException {
		Stats stats = f.getStatsInterface().getPhotoStats("2011-09-21", "6158374085");
		Assert.assertNotNull(stats);
		Assert.assertTrue(stats.getViews() > 0);
	}
	
	@Test
	public void testGetTotalViews() throws IOException, JSONException, FlickrException {
		AccountStats stats = f.getStatsInterface().getTotalViews((String)null);
		Assert.assertNotNull(stats);
		Assert.assertTrue(stats.getTotalViews() > 0);
		Assert.assertTrue(stats.getGalleriesViews() > 0);
		Assert.assertTrue(stats.getPhotostreamViews() > 0);
		Assert.assertTrue(stats.getPhotosViews() > 0);
		Assert.assertTrue(stats.getSetsViews() > 0);
		Assert.assertTrue(stats.getCollectionsViews() > 0);
	}
	
	@Test
	public void testGetPhotostreamStats() throws IOException, JSONException, FlickrException {
		Stats stats = f.getStatsInterface().getPhotostreamStats("2011-09-21");
		Assert.assertNotNull(stats);
		Assert.assertTrue(stats.getViews() > 0);
		Assert.assertTrue(stats.getComments() == 0);
		Assert.assertTrue(stats.getFavorites() == 0);
	}
	
	@Test
	public void testGetPhotosetStats() throws IOException, JSONException, FlickrException {
		Stats stats = f.getStatsInterface().getPhotosetStats("2011-09-21", "72157626738803062");
		Assert.assertNotNull(stats);
		Assert.assertTrue(stats.getViews() >= 0);
		Assert.assertTrue(stats.getComments() >= 0);
		Assert.assertTrue(stats.getFavorites() >= 0);
	}
	
	@Test
	public void testGetCollectionStats() throws IOException, JSONException, FlickrException {
		Stats stats = f.getStatsInterface().getCollectionStats("2011-09-21", "72157626776713094");
		Assert.assertNotNull(stats);
		Assert.assertTrue(stats.getViews() > 0);
		Assert.assertTrue(stats.getComments() >= 0);
		Assert.assertTrue(stats.getFavorites() >= 0);
	}
	
	@Test
	public void testGetPhotostreamReferrers() throws IOException, JSONException, FlickrException {
		ReferrerList referrers = f.getStatsInterface().getPhotostreamReferrers("2011-09-21", "flickr.com");
		Assert.assertNotNull(referrers);
		Assert.assertFalse(referrers.isEmpty());
	}
	
	@Test
	public void testGetPhotostreamDomains() throws IOException, JSONException, FlickrException {
		DomainList domains = f.getStatsInterface().getPhotostreamDomains("2011-09-21", 0, 0);
		Assert.assertNotNull(domains);
		Assert.assertFalse(domains.isEmpty());
	}
	
	@Test
	public void testGetPhotosetReferrers() throws IOException, JSONException, FlickrException {
		ReferrerList referrers = f.getStatsInterface().getPhotosetReferrers("2011-09-21", "flickr.com", null, 0, 0);
		Assert.assertNotNull(referrers);
	}
	
	@Test
	public void testGetPhotoReferrers() throws IOException, JSONException, FlickrException {
		ReferrerList referrers = f.getStatsInterface().getPhotoReferrers("2011-09-21", "flickr.com", null, 0, 0);
		Assert.assertNotNull(referrers);
		Assert.assertFalse(referrers.isEmpty());
	}
	
	@Test
	public void testGetPhotoDomains() throws IOException, JSONException, FlickrException {
		DomainList domains = f.getStatsInterface().getPhotoDomains("2011-09-21", null, 0, 0);
		Assert.assertNotNull(domains);
		Assert.assertFalse(domains.isEmpty());
	}
	
	@Test
	public void testGetCollectionDomains() throws IOException, JSONException, FlickrException {
		DomainList domains = f.getStatsInterface().getCollectionDomains("2011-09-21", null, 0, 0);
		Assert.assertNotNull(domains);
	}
	
	@Test
	public void testGetCollectionReferrers() throws IOException, JSONException, FlickrException {
		ReferrerList referrers = f.getStatsInterface().getCollectionReferrers("2011-09-21", "flickr.com", null, 0, 0);
		Assert.assertNotNull(referrers);
	}
	
	@Test
	public void testGetPhotosetDomains() throws IOException, JSONException, FlickrException {
		DomainList domains = f.getStatsInterface().getPhotosetDomains("2011-09-21", null, 0, 0);
		Assert.assertNotNull(domains);
	}

}
