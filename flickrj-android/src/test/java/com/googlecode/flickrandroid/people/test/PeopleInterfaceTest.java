/**
 * 
 */
package com.googlecode.flickrandroid.people.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;

import com.googlecode.flickrandroid.test.AbstractFlickrTest;
import com.googlecode.flickrandroid.test.TestConstants;
import com.googlecode.flickrjandroid.FlickrException;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class PeopleInterfaceTest extends AbstractFlickrTest{

    /**
     * Test method for {@link com.googlecode.flickrjandroid.people.PeopleInterface#PeopleInterface(java.lang.String, java.lang.String, com.googlecode.flickrjandroid.Transport)}.
     */
    @Test
    public void testPeopleInterface() {
        Assert.assertNotNull(f.getPeopleInterface());
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.people.PeopleInterface#findByEmail(java.lang.String)}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testFindByEmail() throws IOException, FlickrException, JSONException {
        Assert.assertNotNull(f.getPeopleInterface().findByEmail("wanyun892@yahoo.cn"));
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.people.PeopleInterface#findByUsername(java.lang.String)}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testFindByUsername() throws IOException, FlickrException, JSONException {
        Assert.assertNotNull(f.getPeopleInterface().findByUsername("wanyun(Wandy)"));
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.people.PeopleInterface#getInfo(java.lang.String)}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetInfo() throws IOException, FlickrException, JSONException {
        Assert.assertNotNull("Request returned empty photo list", 
                f.getPeopleInterface().getInfo(TestConstants.USER_ID));
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.people.PeopleInterface#getPublicGroups(java.lang.String)}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetPublicGroups() throws IOException, FlickrException, JSONException {
        Assert.assertNotNull("Request returned empty photo list", 
                f.getPeopleInterface().getPublicGroups(TestConstants.USER_ID));
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.people.PeopleInterface#getPublicPhotos(java.lang.String, int, int)}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetPublicPhotosStringIntInt() throws IOException, FlickrException, JSONException {
        Assert.assertFalse("Request returned empty photo list", 
                f.getPeopleInterface().getPublicPhotos(TestConstants.USER_ID, 0, 0).isEmpty());
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.people.PeopleInterface#getPublicPhotos(java.lang.String, java.util.Set, int, int)}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetPublicPhotosStringSetOfStringIntInt() throws IOException, FlickrException, JSONException {
        Set<String> extras = new HashSet<String>();
        extras.add("geo");
        Assert.assertFalse("Request returned empty photo list", 
                f.getPeopleInterface().getPublicPhotos(TestConstants.USER_ID, extras, 0, 0).isEmpty());
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.people.PeopleInterface#getUploadStatus()}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetUploadStatus() throws IOException, FlickrException, JSONException {
        Assert.assertNotNull(f.getPeopleInterface().getUploadStatus());
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.people.PeopleInterface#getPhotos(java.lang.String, java.util.Set, int, int)}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetPhotos() throws IOException, FlickrException, JSONException {
        Assert.assertFalse("Request returned empty photo list", f.getPeopleInterface().getPhotos(TestConstants.USER_ID, 
                new HashSet<String>(Arrays.asList(new String[]{"owner_name","tags","geo"})), 18, 1).isEmpty());
    }

}
