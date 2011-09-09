/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.gmail.yuyang226.flickr;

import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import com.gmail.yuyang226.flickr.activity.ActivityInterface;
import com.gmail.yuyang226.flickr.blogs.BlogsInterface;
import com.gmail.yuyang226.flickr.commons.CommonsInterface;
import com.gmail.yuyang226.flickr.contacts.ContactsInterface;
import com.gmail.yuyang226.flickr.favorites.FavoritesInterface;
import com.gmail.yuyang226.flickr.galleries.GalleriesInterface;
import com.gmail.yuyang226.flickr.groups.GroupsInterface;
import com.gmail.yuyang226.flickr.groups.members.MembersInterface;
import com.gmail.yuyang226.flickr.groups.pools.PoolsInterface;
import com.gmail.yuyang226.flickr.interestingness.InterestingnessInterface;
import com.gmail.yuyang226.flickr.machinetags.MachinetagsInterface;
import com.gmail.yuyang226.flickr.oauth.OAuth;
import com.gmail.yuyang226.flickr.oauth.OAuthInterface;
import com.gmail.yuyang226.flickr.panda.PandaInterface;
import com.gmail.yuyang226.flickr.people.PeopleInterface;
import com.gmail.yuyang226.flickr.photos.PhotosInterface;
import com.gmail.yuyang226.flickr.photos.comments.CommentsInterface;
import com.gmail.yuyang226.flickr.photos.geo.GeoInterface;
import com.gmail.yuyang226.flickr.photos.licenses.LicensesInterface;
import com.gmail.yuyang226.flickr.photos.notes.NotesInterface;
import com.gmail.yuyang226.flickr.photos.transform.TransformInterface;
import com.gmail.yuyang226.flickr.photosets.PhotosetsInterface;
import com.gmail.yuyang226.flickr.photosets.comments.PhotosetsCommentsInterface;
import com.gmail.yuyang226.flickr.places.PlacesInterface;
import com.gmail.yuyang226.flickr.prefs.PrefsInterface;
import com.gmail.yuyang226.flickr.reflection.ReflectionInterface;
import com.gmail.yuyang226.flickr.tags.TagsInterface;
import com.gmail.yuyang226.flickr.test.TestInterface;
import com.gmail.yuyang226.flickr.urls.UrlsInterface;

/**
 * Main entry point for the Flickrj API.
 * This class is used to acquire Interface classes which wrap the Flickr API.<p>
 *
 * If you registered API keys, you find them with the shared secret at your
 * <a href="http://www.flickr.com/services/api/registered_keys.gne">list of API keys</a><p>
 *
 * The user who authenticates himself, can manage this permissions at
 * <a href="http://www.flickr.com/services/auth/list.gne">his list of Third-party applications</a>
 * (You -> Your account -> Extending Flickr -> Account Links -> edit).
 *
 * @author Anthony Eden
 * @version $Id: Flickr.java,v 1.45 2009/06/23 21:51:25 x-mago Exp $
 */
public class Flickr {

    /**
     * The default endpoint host.
     */
    public static final String DEFAULT_HOST = "www.flickr.com";
    
    public static final String DEFAULT_API_HOST = "api.flickr.com";

    /**
     * Set to true to enable response debugging (print the response stream)
     */
    public static boolean debugStream = false;

    /**
     * Set to true to enable request debugging (print the request stream, used for "post")
     */
    public static boolean debugRequest = false;

    /**
     * If set to true, trace messages will be printed to STDOUT.
     */
    public static boolean tracing = false;

    private String apiKey;
    private String sharedSecret;
    private Transport transport;
    private OAuth oauth;

    private OAuthInterface oAuthInterface;
    private ActivityInterface activityInterface;
    private BlogsInterface blogsInterface;
    private CommentsInterface commentsInterface;
    private CommonsInterface commonsInterface;
    private ContactsInterface contactsInterface;
    private FavoritesInterface favoritesInterface;
    private GeoInterface geoInterface;
    private GroupsInterface groupsInterface;
    private InterestingnessInterface interestingnessInterface;
    private LicensesInterface licensesInterface;
    private MembersInterface membersInterface;
    private MachinetagsInterface machinetagsInterface;
    private NotesInterface notesInterface;
    private PandaInterface pandaInterface;
    private PoolsInterface poolsInterface;
    private PeopleInterface peopleInterface;
    private PhotosInterface photosInterface;
    private PhotosetsCommentsInterface photosetsCommentsInterface;
    private PhotosetsInterface photosetsInterface;
    private GalleriesInterface galleriesInterface;
    private PlacesInterface placesInterface;
    private PrefsInterface prefsInterface;
    private ReflectionInterface reflectionInterface;
    private TagsInterface tagsInterface;
    private TestInterface testInterface;
    private TransformInterface transformInterface;
    private UrlsInterface urlsInterface;

    /**
     * @see com.gmail.yuyang226.flickr.photos.PhotosInterface#setContentType(String, String)
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getContentType()
     * @see com.gmail.yuyang226.flickr.uploader.UploadMetaData#setContentType(String)
     */
    public static final String CONTENTTYPE_PHOTO = "1";

    /**
     * @see com.gmail.yuyang226.flickr.photos.PhotosInterface#setContentType(String, String)
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getContentType()
     * @see com.gmail.yuyang226.flickr.uploader.UploadMetaData#setContentType(String)
     */
    public static final String CONTENTTYPE_SCREENSHOT = "2";

    /**
     * @see com.gmail.yuyang226.flickr.photos.PhotosInterface#setContentType(String, String)
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getContentType()
     * @see com.gmail.yuyang226.flickr.uploader.UploadMetaData#setContentType(String)
     */
    public static final String CONTENTTYPE_OTHER = "3";

    /**
     * The lowest accuracy for bounding-box searches.
     *
     * @see com.gmail.yuyang226.flickr.photos.SearchParameters#setAccuracy(int)
     */
    public static final int ACCURACY_WORLD = 1;

    /**
     * @see com.gmail.yuyang226.flickr.photos.SearchParameters#setAccuracy(int)
     */
    public static final int ACCURACY_COUNTRY = 3;

    /**
     * @see com.gmail.yuyang226.flickr.photos.SearchParameters#setAccuracy(int)
     */
    public static final int ACCURACY_REGION = 6;

    /**
     * @see com.gmail.yuyang226.flickr.photos.SearchParameters#setAccuracy(int)
     */
    public static final int ACCURACY_CITY = 11;

    /**
     * The highest accuracy for bounding-box searches.
     *
     * @see com.gmail.yuyang226.flickr.photos.SearchParameters#setAccuracy(int)
     */
    public static final int ACCURACY_STREET = 16;

    /**
     * @see com.gmail.yuyang226.flickr.photos.PhotosInterface#setSafetyLevel(String, String, Boolean)
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getSafetyLevel()
     * @see com.gmail.yuyang226.flickr.uploader.UploadMetaData#setSafetyLevel(String)
	 * @see com.gmail.yuyang226.flickr.photos.SearchParameters#setSafeSearch(String)
     */
    public static final String SAFETYLEVEL_SAFE = "1";
    /**
     * @see com.gmail.yuyang226.flickr.photos.PhotosInterface#setSafetyLevel(String, String, Boolean)
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getSafetyLevel()
     * @see com.gmail.yuyang226.flickr.uploader.UploadMetaData#setSafetyLevel(String)
	 * @see com.gmail.yuyang226.flickr.photos.SearchParameters#setSafeSearch(String)
     */
    public static final String SAFETYLEVEL_MODERATE = "2";
    /**
     * @see com.gmail.yuyang226.flickr.photos.PhotosInterface#setSafetyLevel(String, String, Boolean)
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getSafetyLevel()
     * @see com.gmail.yuyang226.flickr.uploader.UploadMetaData#setSafetyLevel(String)
	 * @see com.gmail.yuyang226.flickr.photos.SearchParameters#setSafeSearch(String)
     */
    public static final String SAFETYLEVEL_RESTRICTED = "3";

    /**
     * @see com.gmail.yuyang226.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_NO_FILTER = 0;
    /**
     * @see com.gmail.yuyang226.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_PUBLIC = 1;
    /**
     * @see com.gmail.yuyang226.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_FRIENDS = 2;
    /**
     * @see com.gmail.yuyang226.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_FAMILY = 3;
    /**
     * @see com.gmail.yuyang226.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_FRIENDS_FAMILY = 4;
    /**
     * @see com.gmail.yuyang226.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.gmail.yuyang226.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_PRIVATE = 5;

    /**
     * Construct a new Flickr gateway instance.  Defaults to a REST transport.
     *
     * @param apiKey The API key, must be non-null
     */
    public Flickr(String apiKey) {
        setApiKey(apiKey);
        try {
            setTransport(new REST(DEFAULT_HOST));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Construct a new Flickr gateway instance.
     *
     * @param apiKey The API key, must be non-null
     * @param transport The transport (REST or SOAP), must be non-null
     */
    public Flickr(String apiKey, Transport transport) {
        setApiKey(apiKey);
        setTransport(transport);
    }
    
    /**
     * Construct a new Flickr gateway instance.
     *
     * @param apiKey The API key, must be non-null
     * @param sharedSecret
     * @param transport
     */
    public Flickr(String apiKey, String sharedSecret) {
        this(apiKey);
        setSharedSecret(sharedSecret);
    }

    /**
     * Construct a new Flickr gateway instance.
     *
     * @param apiKey The API key, must be non-null
     * @param sharedSecret
     * @param transport
     */
    public Flickr(String apiKey, String sharedSecret, Transport transport) {
        setApiKey(apiKey);
        setSharedSecret(sharedSecret);
        setTransport(transport);
    }

    /**
     * Get the API key.
     *
     * @return The API key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Set the API key to use which must not be null.
     *
     * @param apiKey The API key which cannot be null
     */
    public void setApiKey(String apiKey) {
        if (apiKey == null) {
            throw new IllegalArgumentException("API key must not be null");
        }
        this.apiKey = apiKey;
    }

    public void setOAuth(OAuth oauth) {
        this.oauth = oauth;
    }

    /**
     * Get the OAuth-object.
     *
     * @return The OAuth-object
     */
    public OAuth getOAuth() {
        return oauth;
    }

    /**
     * Get the Shared-Secret.
     *
     * @return The Shared-Secret
     */
    public String getSharedSecret() {
        return sharedSecret;
    }

    /**
     * Set the Shared-Secret to use which must not be null.
     *
     * @param sharedSecret The Shared-Secret which cannot be null
     */
    public void setSharedSecret(String sharedSecret) {
        if (sharedSecret == null) {
            throw new IllegalArgumentException("Shared-Secret must not be null");
        }
        this.sharedSecret = sharedSecret;
    }

    /**
     * Get the Transport interface.
     *
     * @return The Tranport interface
     */
    public Transport getTransport() {
        return transport;
    }

    /**
     * Set the Transport which must not be null.
     *
     * @param transport
     */
    public void setTransport(Transport transport) {
        if (transport == null) {
            throw new IllegalArgumentException("Transport must not be null");
        }
        this.transport = transport;
    }
    
    public OAuthInterface getOAuthInterface() {
    	if (oAuthInterface == null) {
    		oAuthInterface = new OAuthInterface(apiKey, sharedSecret, 
    				transport);
        }
        return oAuthInterface;
    }

    /**
     * Get the ActivityInterface.
     *
     * @return The ActivityInterface
     */
    public ActivityInterface getActivityInterface() {
        if (activityInterface == null) {
            activityInterface = new ActivityInterface(apiKey, sharedSecret, transport);
        }
        return activityInterface;
    }

    public synchronized BlogsInterface getBlogsInterface() {
        if (blogsInterface == null) {
            blogsInterface = new BlogsInterface(apiKey, sharedSecret, transport);
        }
        return blogsInterface;
    }

    public CommentsInterface getCommentsInterface() {
        if (commentsInterface == null) {
            commentsInterface = new CommentsInterface(apiKey, sharedSecret, transport);
        }
        return commentsInterface;
    }

    public CommonsInterface getCommonsInterface() {
        if (commonsInterface == null) {
            commonsInterface = new CommonsInterface(apiKey, sharedSecret, transport);
        }
        return commonsInterface;
    }

    public ContactsInterface getContactsInterface() {
        if (contactsInterface == null) {
            contactsInterface = new ContactsInterface(apiKey, sharedSecret, transport);
        }
        return contactsInterface;
    }

    public FavoritesInterface getFavoritesInterface() {
        if (favoritesInterface == null) {
            favoritesInterface = new FavoritesInterface(apiKey, sharedSecret, transport);
        }
        return favoritesInterface;
    }

    public GeoInterface getGeoInterface() {
        if (geoInterface == null) {
            geoInterface = new GeoInterface(apiKey, sharedSecret, transport);
        }
        return geoInterface;
    }

    public GroupsInterface getGroupsInterface() {
        if (groupsInterface == null) {
            groupsInterface = new GroupsInterface(apiKey, sharedSecret, transport);
        }
        return groupsInterface;
    }

    /**
     * @return the interface to the flickr.interestingness methods
     */
    public synchronized InterestingnessInterface getInterestingnessInterface() {
        if (interestingnessInterface == null) {
            interestingnessInterface = new InterestingnessInterface(apiKey, sharedSecret, transport);
        }
        return interestingnessInterface;
    }

    public LicensesInterface getLicensesInterface() {
        if (licensesInterface == null) {
            licensesInterface = new LicensesInterface(apiKey, sharedSecret, transport);
        }
        return licensesInterface;
    }

    public MachinetagsInterface getMachinetagsInterface() {
        if (machinetagsInterface == null) {
        	machinetagsInterface = new MachinetagsInterface(apiKey, sharedSecret, transport);
        }
        return machinetagsInterface;
    }

    public MembersInterface getMembersInterface() {
        if (membersInterface == null) {
        	membersInterface = new MembersInterface(apiKey, sharedSecret, transport);
        }
        return membersInterface;
    }

    public NotesInterface getNotesInterface() {
        if (notesInterface == null) {
            notesInterface = new NotesInterface(apiKey, sharedSecret, transport);
        }
        return notesInterface;
    }

    public PandaInterface getPandaInterface() {
        if (pandaInterface == null) {
            pandaInterface = new PandaInterface(apiKey, sharedSecret, transport);
        }
        return pandaInterface;
    }

    public PoolsInterface getPoolsInterface() {
        if (poolsInterface == null) {
            poolsInterface = new PoolsInterface(apiKey, sharedSecret, transport);
        }
        return poolsInterface;
    }

    public PeopleInterface getPeopleInterface() {
        if (peopleInterface == null) {
            peopleInterface = new PeopleInterface(apiKey, sharedSecret, transport);
        }
        return peopleInterface;
    }

    public PhotosInterface getPhotosInterface() {
        if (photosInterface == null) {
            photosInterface = new PhotosInterface(apiKey, sharedSecret, transport);
        }
        return photosInterface;
    }

    public PhotosetsCommentsInterface getPhotosetsCommentsInterface() {
        if (photosetsCommentsInterface == null) {
            photosetsCommentsInterface = new PhotosetsCommentsInterface(apiKey, sharedSecret, transport);
        }
        return photosetsCommentsInterface;
    }

    public PhotosetsInterface getPhotosetsInterface() {
        if (photosetsInterface == null) {
            photosetsInterface = new PhotosetsInterface(apiKey, sharedSecret, transport);
        }
        return photosetsInterface;
    }
    
    public GalleriesInterface getGalleriesInterface() {
    	if (galleriesInterface == null) {
    		galleriesInterface = new GalleriesInterface(apiKey, sharedSecret, transport);
    	}
    	return galleriesInterface;
    }

    public PlacesInterface getPlacesInterface() {
        if (placesInterface == null) {
            placesInterface = new PlacesInterface(apiKey, sharedSecret, transport);
        }
        return placesInterface;
    }

    public PrefsInterface getPrefsInterface() {
        if (prefsInterface == null) {
            prefsInterface = new PrefsInterface(apiKey, sharedSecret, transport);
        }
        return prefsInterface;
    }

    public ReflectionInterface getReflectionInterface() {
        if (reflectionInterface == null) {
            reflectionInterface = new ReflectionInterface(apiKey, sharedSecret, transport);
        }
        return reflectionInterface;
    }

    /**
     * Get the TagsInterface for working with Flickr Tags.
     *
     * @return The TagsInterface
     */
    public TagsInterface getTagsInterface() {
        if (tagsInterface == null) {
            tagsInterface = new TagsInterface(apiKey, sharedSecret, transport);
        }
        return tagsInterface;
    }

    public TestInterface getTestInterface() {
        if (testInterface == null) {
            testInterface = new TestInterface(apiKey, sharedSecret, transport);
        }
        return testInterface;
    }

    public TransformInterface getTransformInterface() {
        if (transformInterface == null) {
            transformInterface = new TransformInterface(apiKey, sharedSecret, transport);
        }
        return transformInterface;
    }

    public UrlsInterface getUrlsInterface() {
        if (urlsInterface == null) {
            urlsInterface = new UrlsInterface(apiKey, sharedSecret, transport);
        }
        return urlsInterface;
    }

}
