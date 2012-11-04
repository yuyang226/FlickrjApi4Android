/**
 * 
 */
package com.googlecode.flickrandroid.galleries.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Ignore;
import org.junit.Test;

import com.googlecode.flickrandroid.test.AbstractFlickrTest;
import com.googlecode.flickrandroid.test.TestConstants;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.galleries.Gallery;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class GalleriesInterfaceTest extends AbstractFlickrTest {

    /**
     * Test method for {@link com.googlecode.flickrjandroid.galleries.GalleriesInterface#getList(java.lang.String, int, int)}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetList() throws IOException, FlickrException, JSONException {
        List<Gallery> galleries = f.getGalleriesInterface().getList(TestConstants.USER_ID, 0, 0);
        Assert.assertNotNull(galleries);
    }
    
    @Test
    public void testGetInfo() throws IOException, JSONException, FlickrException {
        Assert.assertNotNull(f.getGalleriesInterface().getInfo("54369659-72157627796895282"));
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.galleries.GalleriesInterface#getPhotos(java.lang.String, java.util.Set, int, int)}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetPhotos() throws IOException, FlickrException, JSONException {
        Assert.assertNotNull(f.getGalleriesInterface().getPhotos("54369659-72157627796895282", null, 0, 0));
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.galleries.GalleriesInterface#getListForPhoto(java.lang.String, int, int)}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetListForPhoto() throws IOException, FlickrException, JSONException {
        Assert.assertNotNull(f.getGalleriesInterface().getListForPhoto("6176571289", 0, 0));
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.galleries.GalleriesInterface#create(java.lang.String, java.lang.String, java.lang.String)}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Ignore("we only need to run it once")
    @Test
    public void testCreate() throws IOException, FlickrException, JSONException {
        String galleryId = f.getGalleriesInterface().create("My Test Gallery - " + new Date(System.currentTimeMillis()), "test gallery", "6176571289");
        System.out.println(galleryId);
        Assert.assertNotNull(galleryId);
    }
    
    @Test
    public void testEditMetadata() throws IOException, JSONException, FlickrException {
        f.getGalleriesInterface().editMetadata("54369659-72157627796895282", "Modified My Test Gallery",
                "Modified at " + new Date(System.currentTimeMillis()));
    }
    
    @Test
    public void testEditPhoto() throws IOException, JSONException, FlickrException {
        f.getGalleriesInterface().editPhoto("54369659-72157627796895282", "6176571289", "Modified at " + new Date(System.currentTimeMillis()));
    }
    
    @Test
    public void testAddPhoto() throws IOException, JSONException, FlickrException {
        f.getGalleriesInterface().addPhoto("54369659-72157627796895282", "6176568709", "testing");
    }
    
    @Test
    public void testEditPhotos() throws IOException, JSONException, FlickrException {
        f.getGalleriesInterface().editPhotos("54369659-72157627796895282", "6176571289", new ArrayList<String>(1));
    }

}
