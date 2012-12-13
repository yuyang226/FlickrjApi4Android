/**
 * 
 */
package com.googlecode.flickrandroid.oauth.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.googlecode.flickrandroid.test.AbstractFlickrTest;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.auth.Permission;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;



/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class OAuthTest extends AbstractFlickrTest{
    /**
     * 
     */
    public OAuthTest() {
        super();
    }
    
    @Test
    public void testOAuthInterfaceTestLogin() throws FlickrException, IOException, JSONException {
        Assert.assertNotNull("Login failed", f.getOAuthInterface().testLogin()); //$NON-NLS-1$
        Assert.assertNotNull(f.getPhotosInterface().getAllContexts("5772049100")); //$NON-NLS-1$
    }
    
    @Test
    public void testGetRequestToken() throws IOException, FlickrException {
        OAuthToken token = f.getOAuthInterface().getRequestToken("http://localhost:8080"); //$NON-NLS-1$
        Assert.assertNotNull(token);
        URL url = f.getOAuthInterface().buildAuthenticationUrl(Permission.READ, token);
        System.out.println("OAuth URL: " + url); //$NON-NLS-1$
        Assert.assertNotNull(url);
    }
    
    public static String readParamFromCommand(String message) throws IOException {
        //  prompt the user to enter their name
        System.out.print(message);
        //  open up standard input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return String.valueOf(br.readLine()).trim();
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            Flickr f = new Flickr("da4fadd0084ea1799ad33048f0d6a5c5", "186b04791439c326");
            /*OAuthToken oauthToken = f.getOAuthInterface().getRequestToken("http://localhost");
            System.out.println(oauthToken);
            System.out.println(f.getOAuthInterface().buildAuthenticationUrl(Permission.DELETE, oauthToken));
            String tokenVerifier = readParamFromCommand("Enter Token Verifier: ");
            OAuth oauth = f.getOAuthInterface().getAccessToken(oauthToken.getOauthToken(), 
                    oauthToken.getOauthTokenSecret(), tokenVerifier);
            System.out.println(oauth);
            RequestContext.getRequestContext().setOAuth(oauth);*/
            
            //oauthToken=72157627458295241-83050bfaaeffd445, oauthTokenSecret=07c38a749dc7d36e
            OAuth auth = new OAuth();
            User user = new User();
            user.setId("8308954@N06");
            user.setUsername("Yang and Yun's Album");
            auth.setToken(new OAuthToken("72157627458295241-83050bfaaeffd445", "07c38a749dc7d36e"));
            //auth.setToken(new OAuthToken("72157627641862382-904dfd0d8fb0294e", "07c38a749dc7d36e"));
            RequestContext.getRequestContext().setOAuth(auth);
            //f.getGalleriesInterface().getListForPhoto("5772049100", 0, 0);
            System.out.println(f.getOAuthInterface().testLogin());
            //f.getPhotosInterface().addTags("5772049100", new String[]{"Hello", "World"});
            //f.getCommentsInterface().addComment("5772049100", "Hello World");
            //Collection<Exif> exifs = f.getPhotosInterface().getExif("5772049100", null);
            //f.getPhotosInterface().getInfo("6024664723", null);
            //f.getCommentsInterface().getList("6024664723");
//			System.out.println(f.getActivityInterface().userComments(0, 0));
//			System.out.println(f.getActivityInterface().userPhotos(1, 1, null));
//			System.out.println(f.getBlogsInterface().getServices());
//			System.out.println(f.getBlogsInterface().getList());
//			System.out.println(f.getCommonsInterface().getInstitutions());
//			System.out.println(f.getContactsInterface().getList());
//			System.out.println(f.getContactsInterface().getPublicList("8308954@N06"));
//			System.out.println(f.getContactsInterface().getListRecentlyUploaded(
//					new Date(System.currentTimeMillis() - 24L * 60L * 60L * 1000L), null));
            
            //Photoset ID: 72157626738803062
//			Photoset set = f.getPhotosetsInterface().getList("8308954@N06").getPhotosets().iterator().next();
//			System.out.println(f.getPhotosetsInterface().getInfo(set.getId()));
//			System.out.println(f.getPhotosetsInterface().getPhotos("72157626738803062", 0, 0));
//			System.out.println(f.getPhotosetsInterface().getContext("5726077435", "72157626738803062"));
            //photo ID: 6024664723
            //f.getFavoritesInterface().add(photo.getId());
//			System.out.println("Favourites List: \n" + f.getFavoritesInterface().getList("8308954@N06", 0, 0, null));
//			System.out.println("Favourites Public List: \n" + f.getFavoritesInterface().getPublicList("8308954@N06", 0, 0, null));
            //group ID: 95014477@N00
//			Group group = f.getGroupsInterface().search("Nikon", 0, 0).iterator().next();
//			System.out.println(f.getGroupsInterface().getInfo(group.getId()));
//			System.out.println(f.getMembersInterface().getList(group.getId(), null, 0, 0));
//			System.out.println(f.getGeoInterface().getLocation("6024664723"));
//			System.out.println(f.getGeoInterface().getPerms("6024664723"));
//			System.out.println(f.getLicensesInterface().getInfo());
//			Place place = f.getPlacesInterface().find("Shanghai").get(0);
//			System.out.println(place.getName() + ": " + place.getPlaceId());
            //shanghai place ID: JAJiM7JTU78IjzqC
//			System.out.println(f.getPlacesInterface().getInfoByUrl(place.getPlaceUrl()));
//			System.out.println(f.getPlacesInterface().getInfo(place.getPlaceId(), place.getWoeId()));
            
//			System.out.println(f.getUrlsInterface().lookupUser(f.getUrlsInterface().getUserProfile("8308954@N06")));
//			System.out.println(f.getUrlsInterface().getUserPhotos("8308954@N06"));
//			System.out.println(f.getUrlsInterface().lookupGroup(f.getUrlsInterface().getGroup("95014477@N00")));
            
//			System.out.println(f.getTagsInterface().getClusters("nikon"));
//			System.out.println(f.getTagsInterface().getListPhoto("6024664723"));
//			System.out.println(f.getTagsInterface().getListUser("8308954@N06"));
//			System.out.println(f.getTagsInterface().getListUserPopular("8308954@N06"));
//			System.out.println(f.getTagsInterface().getListUserRaw(null));
//			System.out.println(f.getTagsInterface().getRelated("nikon"));
            
//			System.out.println(f.getTestInterface().login());
//			f.getTestInterface().null_();
//			System.out.println(f.getTestInterface().echo(Arrays.asList(
//					new Parameter[]{new Parameter("hello", "nikon")})));
            
//			System.out.println(f.getReflectionInterface().getMethods());
//			System.out.println(f.getReflectionInterface().getMethodInfo("flickr.photos.delete"));
            
//			System.out.println(f.getPhotosInterface().getAllContexts("5772049100"));
//			System.out.println(f.getPhotosInterface().getContext("5772049100"));
//			System.out.println(f.getPhotosInterface().getFavorites("5772049100", 0, 0));
//			System.out.println(f.getPhotosInterface().getExif("5772049100", null));
//			System.out.println(f.getPhotosInterface().getUntagged(0, 0));
//			System.out.println(f.getPhotosInterface().getRecent(null, 0, 0));
            //System.out.println(f.getPhotosInterface().getContactsPhotos(10, false, false, false));
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            LoggerFactory.getLogger(OAuthTest.class).error(e.getLocalizedMessage(), e);
        }
    }

}
