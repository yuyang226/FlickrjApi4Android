/**
 * 
 */
package com.googlecode.flickrandroid.tags.test;

import java.io.IOException;
import java.util.Collection;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;

import com.googlecode.flickrandroid.test.AbstractFlickrTest;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.tags.HotlistTag;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class TagsInterfaceTest extends AbstractFlickrTest {

    /**
     * Test method for {@link com.googlecode.flickrjandroid.tags.TagsInterface#getHotList(java.lang.String, int)}.
     * @throws JSONException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testGetHotList() throws IOException, FlickrException, JSONException {
        Collection<HotlistTag> tags = f.getTagsInterface().getHotList(null, 0);
        Assert.assertNotNull(tags);
        Assert.assertFalse(tags.isEmpty());
    }

}
