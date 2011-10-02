/**
 * 
 */
package com.gmail.yuyang226.flickr.galleries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.Parameter;
import com.gmail.yuyang226.flickr.Response;
import com.gmail.yuyang226.flickr.Transport;
import com.gmail.yuyang226.flickr.oauth.OAuthInterface;
import com.gmail.yuyang226.flickr.oauth.OAuthUtils;
import com.gmail.yuyang226.flickr.org.json.JSONArray;
import com.gmail.yuyang226.flickr.org.json.JSONException;
import com.gmail.yuyang226.flickr.org.json.JSONObject;
import com.gmail.yuyang226.flickr.photos.Extras;
import com.gmail.yuyang226.flickr.photos.PhotoList;
import com.gmail.yuyang226.flickr.photos.PhotoUtils;
import com.gmail.yuyang226.flickr.util.JSONUtils;
import com.gmail.yuyang226.flickr.util.StringUtilities;

/**
 * Represents the interface to get the gallery information from flickr. To get
 * an instance of this class, use the following way: <br/>
 * GalleriesInterface gi = FlickrHelper.getInstance().getGalleryInterface();
 * 
 * @author charles
 * 
 */
public class GalleriesInterface {

	private static final String KEY_METHOD = "method"; //$NON-NLS-1$
	private static final String KEY_API_KEY = "api_key"; //$NON-NLS-1$
	private static final String KEY_PER_PAGE = "per_page"; //$NON-NLS-1$
	private static final String KEY_PAGE = "page"; //$NON-NLS-1$
	private static final String KEY_USER_ID = "user_id"; //$NON-NLS-1$
	private static final String KEY_GALLERY_ID = "gallery_id"; //$NON-NLS-1$
	private static final String KEY_PHOTO_ID = "photo_id"; //$NON-NLS-1$

	private static final String METHOD_GET_LIST = "flickr.galleries.getList"; //$NON-NLS-1$
	private static final String METHOD_GET_PHOTOS = "flickr.galleries.getPhotos"; //$NON-NLS-1$
	private static final String METHOD_CREATE = "flickr.galleries.create"; //$NON-NLS-1$
	private static final String METHOD_GET_LIST_FOR_PHOTO = "flickr.galleries.getListForPhoto"; //$NON-NLS-1$
	private static final String METHOD_ADD_PHOTO = "flickr.galleries.addPhoto"; //$NON-NLS-1$
	private static final String METHOD_EDIT_METADATA = "flickr.galleries.editMeta"; //$NON-NLS-1$
	private static final String METHOD_EDIT_PHOTO = "flickr.galleries.editPhoto"; //$NON-NLS-1$
	private static final String METHOD_EDIT_PHOTOS = "flickr.galleries.editPhotos"; //$NON-NLS-1$
	
	/**
	 * The api key.
	 */
	private String mApiKey;

	/**
	 * Only support REST for now.
	 */
	private Transport mTransport;

	private String mSharedSecret;

	/**
	 * Constructor.
	 * 
	 * @param apiKey
	 * @param transport
	 */
	public GalleriesInterface(String apiKey, String sharedSecret,
			Transport transport) {
		this.mApiKey = apiKey;
		this.mTransport = transport;
		this.mSharedSecret = sharedSecret;
	}
	
	/**
	 * @param galleryId The ID of the gallery to add a photo to. Note: this is the compound ID returned in methods like flickr.galleries.getList, and flickr.galleries.getListForPhoto.
	 * @param photoId photo ID to add to the gallery
	 * @param comment An optional short comment or story to accompany the photo.
	 * @throws FlickrException 
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public void addPhoto(String galleryId, String photoId, String comment) throws IOException, JSONException, FlickrException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter(KEY_METHOD, METHOD_ADD_PHOTO));
		parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, this.mApiKey));

		parameters.add(new Parameter("gallery_id", galleryId)); //$NON-NLS-1$
		parameters.add(new Parameter("photo_id", photoId)); //$NON-NLS-1$
		if (comment != null) {
		parameters.add(new Parameter("comment", comment)); //$NON-NLS-1$
		}
		OAuthUtils.addOAuthToken(parameters);
		Response response = mTransport.postJSON(mSharedSecret, parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
	}

	/**
	 * Returns the gallery list of a given user.
	 * 
	 * @param userId
	 * @param pageSize
	 * @param pageNo
	 * @return
	 * @throws IOException
	 * @throws FlickrException
	 * @throws JSONException 
	 */
	public List<Gallery> getList(String userId, int perPage, int page)
			throws IOException, FlickrException, JSONException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter(KEY_METHOD, METHOD_GET_LIST));
		parameters.add(new Parameter(KEY_API_KEY, mApiKey));
		parameters.add(new Parameter(KEY_USER_ID, userId));
		if (perPage > 0) {
			parameters
					.add(new Parameter(KEY_PER_PAGE, String.valueOf(perPage)));
		}
		if (page > 0) {
			parameters.add(new Parameter(KEY_PAGE, String.valueOf(page)));
		}

		List<Gallery> galleries = new ArrayList<Gallery>();
		Response response = mTransport.get(mTransport.getPath(), parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
		JSONObject galleriesElement = response.getData().getJSONObject("galleries");
		JSONArray galleryNodes = galleriesElement
				.optJSONArray("gallery"); //$NON-NLS-1$
		for (int i = 0; galleryNodes != null && i < galleryNodes.length(); i++) {
			JSONObject galleryElement = galleryNodes.getJSONObject(i);
			Gallery gallery = new Gallery();
			gallery.setGalleryId(galleryElement.getString("id")); //$NON-NLS-1$
			gallery.setGalleryUrl(galleryElement.getString("url")); //$NON-NLS-1$
			gallery.setOwnerId(galleryElement.getString("owner")); //$NON-NLS-1$
			gallery.setPrimaryPhotoId(galleryElement
					.getString("primary_photo_id")); //$NON-NLS-1$
			gallery.setPhotoCount(Integer.parseInt(galleryElement
					.getString("count_photos"))); //$NON-NLS-1$
			gallery.setVideoCount(Integer.parseInt(galleryElement
					.getString("count_videos"))); //$NON-NLS-1$
			String title = JSONUtils.getChildValue(galleryElement, "title"); //$NON-NLS-1$
			gallery.setTitle(title == null ? "" : title); //$NON-NLS-1$

			String desc = JSONUtils.getChildValue(galleryElement,
					"description"); //$NON-NLS-1$
			gallery.setDescription(desc == null ? "" : desc); //$NON-NLS-1$
			galleries.add(gallery);
		}
		return galleries;
	}

	public PhotoList getPhotos(String galleryId, Set<String> extras,
			int perPage, int page) throws IOException, 
			FlickrException, JSONException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter(KEY_METHOD, METHOD_GET_PHOTOS));
		parameters.add(new Parameter(KEY_API_KEY, mApiKey));
		parameters.add(new Parameter(KEY_GALLERY_ID, galleryId));
		if (perPage > 0) {
			parameters
					.add(new Parameter(KEY_PER_PAGE, String.valueOf(perPage)));
		}
		if (page > 0) {
			parameters.add(new Parameter(KEY_PAGE, String.valueOf(page)));
		}

		if (extras != null && !extras.isEmpty()) {
			parameters.add(new Parameter(Extras.KEY_EXTRAS, StringUtilities
					.join(extras, ","))); //$NON-NLS-1$
		}

		Response response = mTransport.get(mTransport.getPath(), parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
		return PhotoUtils.createPhotoList(response.getData());
	}
	
	public List<Gallery> getListForPhoto(String photoId, int perPage, int page)
	throws IOException, FlickrException, JSONException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter(KEY_METHOD, METHOD_GET_LIST_FOR_PHOTO));
		parameters.add(new Parameter(KEY_API_KEY, mApiKey));
		parameters.add(new Parameter(KEY_PHOTO_ID, photoId));
		if (perPage > 0) {
			parameters
			.add(new Parameter(KEY_PER_PAGE, String.valueOf(perPage)));
		}
		if (page > 0) {
			parameters.add(new Parameter(KEY_PAGE, String.valueOf(page)));
		}

		List<Gallery> galleries = new ArrayList<Gallery>();
		Response response = mTransport.get(mTransport.getPath(), parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
		JSONObject galleriesElement = response.getData().getJSONObject("galleries");
		JSONArray galleryNodes = galleriesElement
		.optJSONArray("gallery"); //$NON-NLS-1$
		for (int i = 0; galleryNodes != null && i < galleryNodes.length(); i++) {
			JSONObject galleryElement = galleryNodes.getJSONObject(i);
			Gallery gallery = new Gallery();
			gallery.setGalleryId(galleryElement.getString("id")); //$NON-NLS-1$
			gallery.setGalleryUrl(galleryElement.getString("url")); //$NON-NLS-1$
			gallery.setOwnerId(galleryElement.getString("owner")); //$NON-NLS-1$
			gallery.setPrimaryPhotoId(galleryElement
					.getString("primary_photo_id")); //$NON-NLS-1$
			gallery.setPhotoCount(Integer.parseInt(galleryElement
					.getString("count_photos"))); //$NON-NLS-1$
			gallery.setVideoCount(Integer.parseInt(galleryElement
					.getString("count_videos"))); //$NON-NLS-1$
			String title = JSONUtils.getChildValue(galleryElement, "title"); //$NON-NLS-1$
			gallery.setTitle(title == null ? "" : title); //$NON-NLS-1$

			String desc = JSONUtils.getChildValue(galleryElement,
			"description"); //$NON-NLS-1$
			gallery.setDescription(desc == null ? "" : desc); //$NON-NLS-1$
			galleries.add(gallery);
		}
		return galleries;
	}

	/**
	 * Creates a gallery, return -1 says there is error, returns the gallery id
	 * if success.
	 * 
	 * @param title The name of the gallery
	 * @param description A short description for the gallery
	 * @param primaryPhotoId The optional first photo to add to your gallery
	 * @return the gallery ID for successful creation
	 * @throws IOException
	 * @throws FlickrException
	 * @throws JSONException 
	 */
	public String create(String title, String description,
			String primaryPhotoId) throws IOException, 
			FlickrException, JSONException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter(KEY_METHOD, METHOD_CREATE));
		parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, this.mApiKey));

		parameters.add(new Parameter("title", title)); //$NON-NLS-1$
		parameters.add(new Parameter(
				"description", description == null ? title : description)); //$NON-NLS-1$
		if (primaryPhotoId != null) {
			parameters.add(new Parameter("primary_photo_id", primaryPhotoId)); //$NON-NLS-1$
		}
		OAuthUtils.addOAuthToken(parameters);
		Response response = mTransport.postJSON(mSharedSecret, parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
		JSONObject gallery = response.getData().getJSONObject("gallery");
		return gallery.getString("id"); //$NON-NLS-1$
	}
	
	public void editMetadata(String galleryId, String title, String description) throws IOException, JSONException, FlickrException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter(KEY_METHOD, METHOD_EDIT_METADATA));
		parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, this.mApiKey));
		parameters.add(new Parameter("gallery_id", galleryId)); //$NON-NLS-1$
		parameters.add(new Parameter("title", title)); //$NON-NLS-1$
		if (description != null) {
			parameters.add(new Parameter("description", description)); //$NON-NLS-1$
		}
		OAuthUtils.addOAuthToken(parameters);
		Response response = mTransport.postJSON(mSharedSecret, parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
	}
	
	/**
	 * Edit the comment for a gallery photo.
	 * 
	 * @param galleryId The ID of the gallery to add a photo to
	 * @param photoId The photo ID to add to the gallery
	 * @param comment The updated comment the photo
	 * @throws IOException
	 * @throws JSONException
	 * @throws FlickrException
	 */
	public void editPhoto(String galleryId, String photoId, String comment) throws IOException, JSONException, FlickrException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter(KEY_METHOD, METHOD_EDIT_PHOTO));
		parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, this.mApiKey));

		parameters.add(new Parameter("gallery_id", galleryId)); //$NON-NLS-1$
		parameters.add(new Parameter("photo_id", photoId)); //$NON-NLS-1$
		parameters.add(new Parameter("comment", comment)); //$NON-NLS-1$
		OAuthUtils.addOAuthToken(parameters);
		Response response = mTransport.postJSON(mSharedSecret, parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
	}
	
	/**
	 * Modify the photos in a gallery. Use this method to add, remove and re-order photos.
	 * 
	 * @param galleryId The id of the gallery to modify. The gallery must belong to the calling user.
	 * @param primaryPhotoId The id of the photo to use as the 'primary' photo for the gallery. This id must also be passed along in photo_ids list argument.
	 * @param photoIds A comma-delimited list of photo ids to include in the gallery. They will appear in the set in the order sent. This list must contain the primary photo id. This list of photos replaces the existing list.
	 * @throws IOException
	 * @throws JSONException
	 * @throws FlickrException
	 */
	public void editPhotos(String galleryId, String primaryPhotoId, List<String> photoIds) throws IOException, JSONException, FlickrException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter(KEY_METHOD, METHOD_EDIT_PHOTOS));
		parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, this.mApiKey));

		parameters.add(new Parameter("gallery_id", galleryId)); //$NON-NLS-1$
		parameters.add(new Parameter("primary_photo_id", primaryPhotoId)); //$NON-NLS-1$
		if (photoIds.contains(primaryPhotoId) == false) {
			photoIds.add(primaryPhotoId);
		}
		parameters.add(new Parameter("photo_ids", StringUtilities.join(photoIds, ","))); //$NON-NLS-1$
		OAuthUtils.addOAuthToken(parameters);
		Response response = mTransport.postJSON(mSharedSecret, parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
	}
	
}
