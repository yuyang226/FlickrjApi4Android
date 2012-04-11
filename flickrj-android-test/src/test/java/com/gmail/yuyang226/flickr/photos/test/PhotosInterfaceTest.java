/**
 * 
 */
package com.gmail.yuyang226.flickr.photos.test;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.interestingness.InterestingnessInterface;
import com.gmail.yuyang226.flickr.photos.Extras;
import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotoList;
import com.gmail.yuyang226.flickr.test.AbstractFlickrTest;



/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class PhotosInterfaceTest extends AbstractFlickrTest {
	
	@Test
	public void testPhotos() throws FlickrException, IOException, JSONException {
		Assert.assertFalse(f.getPhotosInterface().getAllContexts("5116571240").isEmpty());
	}
	
	@Test
	public void testLargeSquareSize() throws Exception {
		InterestingnessInterface iff = f.getInterestingnessInterface();
	    Set<String> extras = new HashSet<String>();
	    extras.add(Extras.URL_Q);
	    PhotoList pl = iff.getList((Date)null, extras, 10, 1);
	    Assert.assertNotNull(pl);
	    Assert.assertFalse(pl.isEmpty());
	    Photo photo = pl.get(0);
	    Assert.assertNotNull(photo);
	    Assert.assertNotNull(photo.getLargeSquareUrl());
	    LoggerFactory.getLogger("====" + PhotosInterfaceTest.class).info(photo.getLargeSquareUrl());
		System.out.println(photo.getLargeSquareUrl());
	}

}
