/**
 * 
 */
package com.googlecode.flickrandroid.prefs.test;

import java.io.IOException;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import com.googlecode.flickrandroid.test.AbstractFlickrTest;
import com.googlecode.flickrjandroid.FlickrException;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class PrefsInterfaceTest extends AbstractFlickrTest{

    /**
     * Test method for {@link com.googlecode.flickrjandroid.prefs.PrefsInterface#getContentType()}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetContentType() throws IOException, FlickrException, JSONException {
        Assert.assertNotNull(f.getPrefsInterface().getContentType());
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.prefs.PrefsInterface#getGeoPerms()}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetGeoPerms() throws IOException, FlickrException, JSONException {
        Assert.assertTrue("Invalid Geo Perms", f.getPrefsInterface().getGeoPerms() >= 0);
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.prefs.PrefsInterface#getHidden()}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetHidden() throws IOException, FlickrException, JSONException {
        f.getPrefsInterface().getHidden();
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.prefs.PrefsInterface#getSafetyLevel()}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetSafetyLevel() throws IOException, FlickrException, JSONException {
        Assert.assertNotNull("Invalid safety level", f.getPrefsInterface().getSafetyLevel());
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.prefs.PrefsInterface#getPrivacy()}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetPrivacy() throws IOException, FlickrException, JSONException {
        Assert.assertTrue("Invalid privacy", f.getPrefsInterface().getPrivacy() > 0);
    }

}
