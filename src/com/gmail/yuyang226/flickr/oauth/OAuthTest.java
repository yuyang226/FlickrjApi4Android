/**
 * 
 */
package com.gmail.yuyang226.flickr.oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.RequestContext;
import com.gmail.yuyang226.flickr.people.User;
import com.gmail.yuyang226.flickr.photos.Exif;
import com.gmail.yuyang226.flickr.photos.Photo;



/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class OAuthTest {
	public static final String DATA = "\\u4e0a\\u6d77\\u98ce\\u534e\\u521d\\u4e2d";
	/**
	 * 
	 */
	public OAuthTest() {
		super();
		System.out.println(DATA);
		String v = "'"+DATA+"'";  
	    System.out.println(v);  
	}

	private static String readParamFromCommand(String message) throws IOException {
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
			System.out.println(f.getOAuthInterface().buildAuthenticationUrl(Permission.WRITE, oauthToken));
			String tokenVerifier = readParamFromCommand("Enter Token Verifier: ");
			OAuth oauth = f.getOAuthInterface().getAccessToken(oauthToken, tokenVerifier);
			System.out.println(oauth);
			RequestContext.getRequestContext().setOAuth(oauth);*/
			
			//oauthToken=72157627458295241-83050bfaaeffd445, oauthTokenSecret=07c38a749dc7d36e
			OAuth auth = new OAuth();
			User user = new User();
			user.setId("8308954@N06");
			user.setUsername("Yang and Yun's Album");
			auth.setToken(new OAuthToken("72157627458295241-83050bfaaeffd445", "07c38a749dc7d36e"));
			RequestContext.getRequestContext().setOAuth(auth);
			f.setOAuth(auth);
			
			System.out.println(f.getOAuthInterface().testLogin());
			f.getInterestingnessInterface().getList();
			//f.getPhotosInterface().addTags("5772049100", new String[]{"Hello", "World"});
			//f.getCommentsInterface().addComment("5772049100", "Hello World");
			Collection<Exif> exifs = f.getPhotosInterface().getExif("5772049100", null);
			f.getPhotosInterface().getInfo("6024664723", null);
			f.getCommentsInterface().getList("6024664723");
			System.out.println(exifs);
			System.out.println(f.getGalleriesInterface().getList("8308954@N06", 0, 0));
			System.out.println(f.getGalleriesInterface().getPhotos("8263632-72157623259986613", null, 0, 0));
//			System.out.println(f.getActivityInterface().userComments(0, 0));
//			System.out.println(f.getActivityInterface().userPhotos(1, 1, null));
//			System.out.println(f.getBlogsInterface().getServices());
//			System.out.println(f.getBlogsInterface().getList());
//			System.out.println(f.getCommonsInterface().getInstitutions());
//			System.out.println(f.getContactsInterface().getList());
//			System.out.println(f.getContactsInterface().getPublicList("8308954@N06"));
//			System.out.println(f.getContactsInterface().getListRecentlyUploaded(
//					new Date(System.currentTimeMillis() - 24L * 60L * 60L * 1000L), null));
//			System.out.println(f.getPeopleInterface().findByEmail("wanyun892@yahoo.cn"));
//			System.out.println(f.getPeopleInterface().findByUsername("wanyun(Wandy)"));
//			System.out.println(f.getPeopleInterface().getInfo("8308954@N06"));
//			System.out.println(f.getPeopleInterface().getPublicGroups("8308954@N06"));
//			System.out.println(f.getPeopleInterface().getUploadStatus());
			Photo photo = f.getPeopleInterface().getPublicPhotos("8308954@N06", 0, 0).get(0);
			//Photoset ID: 72157626738803062
//			Photoset set = f.getPhotosetsInterface().getList("8308954@N06").getPhotosets().iterator().next();
//			System.out.println(f.getPhotosetsInterface().getInfo(set.getId()));
//			System.out.println(f.getPhotosetsInterface().getPhotos("72157626738803062", 0, 0));
//			System.out.println(f.getPhotosetsInterface().getContext("5726077435", "72157626738803062"));
			//photo ID: 6024664723
			//System.out.println(f.getPeopleInterface().getPhotos("8308954@N06", null, 0, 0));
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
			
//			System.out.println(f.getPrefsInterface().getContentType());
//			System.out.println(f.getPrefsInterface().getGeoPerms());
//			System.out.println(f.getPrefsInterface().getHidden());
//			System.out.println(f.getPrefsInterface().getPrivacy());
//			System.out.println(f.getPrefsInterface().getSafetyLevel());
			
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
			e.printStackTrace();
		}
	}

}
