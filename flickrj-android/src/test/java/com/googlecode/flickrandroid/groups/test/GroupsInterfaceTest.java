/**
 * 
 */
package com.googlecode.flickrandroid.groups.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;

import org.json.JSONException;
import org.junit.Test;

import com.googlecode.flickrandroid.test.AbstractFlickrTest;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.groups.Category;
import com.googlecode.flickrjandroid.groups.Group;

/**
 * @author yayu
 *
 */
public class GroupsInterfaceTest extends AbstractFlickrTest {

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.groups.GroupsInterface#browse(java.lang.String)}.
	 * @throws JSONException 
	 * @throws FlickrException 
	 * @throws IOException 
	 */
	@Test
	public void testBrowse() throws IOException, FlickrException, JSONException {
		Category cat = f.getGroupsInterface().browse(null);
		assertNotNull(cat);
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.groups.GroupsInterface#getInfo(java.lang.String)}.
	 * @throws JSONException 
	 * @throws FlickrException 
	 * @throws IOException 
	 */
	@Test
	public void testGetInfo() throws IOException, FlickrException, JSONException {
		Group group = f.getGroupsInterface().getInfo("1529302@N24");
		assertNotNull(group);
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.groups.GroupsInterface#search(java.lang.String, int, int)}.
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws FlickrException 
	 */
	@Test
	public void testSearch() throws FlickrException, IOException, JSONException {
		Collection<Group> groups = f.getGroupsInterface().search("lego", -1, -1);
		assertNotNull(groups);
		assertFalse(groups.isEmpty());
	}

	/**
	 * Test method for {@link com.googlecode.flickrjandroid.groups.GroupsInterface#joinPublicGroup(java.lang.String)}.
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws FlickrException 
	 */
	@Test
	public void testJoinPublicGroupAndLeave() throws FlickrException, IOException, JSONException {
		try {
			f.getGroupsInterface().leave("1529302@N24");
		} catch (Exception e) {
			//ensure it is now in the group
		}
		f.getGroupsInterface().joinPublicGroup("1529302@N24");
		f.getGroupsInterface().leave("1529302@N24");
	}

}
