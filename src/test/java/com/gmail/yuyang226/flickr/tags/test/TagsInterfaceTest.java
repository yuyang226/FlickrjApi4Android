/**
 * 
 */
package com.gmail.yuyang226.flickr.tags.test;

import java.io.IOException;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.org.json.JSONException;
import com.gmail.yuyang226.flickr.tags.HotlistTag;
import com.gmail.yuyang226.flickr.test.AbstractFlickrTest;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class TagsInterfaceTest extends AbstractFlickrTest {

	/**
	 * Test method for {@link com.gmail.yuyang226.flickr.tags.TagsInterface#getHotList(java.lang.String, int)}.
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
