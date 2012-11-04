/**
 * 
 */
package com.googlecode.flickrandroid.favorites.test;

import static org.junit.Assert.fail;

import java.io.IOException;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Ignore;
import org.junit.Test;

import com.googlecode.flickrandroid.test.AbstractFlickrTest;
import com.googlecode.flickrjandroid.FlickrException;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class FavoritesInterfaceTest extends AbstractFlickrTest {

    /**
     * Test method for {@link com.googlecode.flickrjandroid.favorites.FavoritesInterface#add(java.lang.String)}.
     */
    @Ignore
    @Test
    public void testAdd() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.favorites.FavoritesInterface#getContext(java.lang.String, java.lang.String)}.
     * @throws FlickrException 
     * @throws JSONException 
     * @throws IOException 
     */
    @Test
    public void testGetContext() throws IOException, JSONException, FlickrException {
        f.getFavoritesInterface().getContext("5284146252", "8308954@N06");
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.favorites.FavoritesInterface#getList(java.lang.String, int, int, java.util.Set)}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetList() throws IOException, FlickrException, JSONException {
        Assert.assertFalse(f.getFavoritesInterface().getList("8308954@N06", null, null, 0, 0, null).isEmpty());
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.favorites.FavoritesInterface#getPublicList(java.lang.String, int, int, java.util.Set)}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetPublicList() throws IOException, FlickrException, JSONException {
        Assert.assertFalse(f.getFavoritesInterface().getPublicList("8308954@N06", null, null, 0, 0, null).isEmpty());
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.favorites.FavoritesInterface#remove(java.lang.String)}.
     */
    @Ignore
    @Test
    public void testRemove() {
        fail("Not yet implemented");
    }

}
