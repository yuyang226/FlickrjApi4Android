/**
 * 
 */
package com.googlecode.flickrandroid.photos.test;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.googlecode.flickrandroid.test.AbstractFlickrTest;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.interestingness.InterestingnessInterface;
import com.googlecode.flickrjandroid.photos.Extras;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoFavouriteUserList;
import com.googlecode.flickrjandroid.photos.PhotoList;



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
    
    @Test
    public void testGetInfo() throws Exception {
        Photo photo = f.getPhotosInterface().getInfo("5457780455", null);
        Assert.assertNotNull(photo);
        Assert.assertNotNull(photo.getOwner());
    }
    
    @Test
    public void testGetFavourites() throws IOException, FlickrException, JSONException {
    	PhotoFavouriteUserList favUsers = f.getPhotosInterface().getFavorites("820005888", -1, -1);
    	Assert.assertNotNull(favUsers);
    	Assert.assertTrue(favUsers.getPages() > 0);
    	Assert.assertFalse(favUsers.isEmpty());
    }
    
    @Test(expected=FlickrException.class)
    public void testGetFavouritesNotFound() throws IOException, FlickrException, JSONException {
    	PhotoFavouriteUserList favUsers = f.getPhotosInterface().getFavorites("820005888111", -1, -1);
    	Assert.assertNotNull(favUsers);
    }

}
