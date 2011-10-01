/**
 * 
 */
package com.gmail.yuyang226.flickr.photos.test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.org.json.JSONException;
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

}
