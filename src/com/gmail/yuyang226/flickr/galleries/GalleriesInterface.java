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

	private static final String METHOD_GET_LIST = "flickr.galleries.getList"; //$NON-NLS-1$
	private static final String METHOD_GET_PHOTOS = "flickr.galleries.getPhotos"; //$NON-NLS-1$
	private static final Object METHOD_CREATE = "flickr.galleries.create"; //$NON-NLS-1$

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

	/**
	 * Creates a gallery, return -1 says there is error, returns the gallery id
	 * if success.
	 * 
	 * @param title
	 * @param description
	 * @param primaryPhotoId
	 * @return
	 * @throws IOException
	 * @throws FlickrException
	 * @throws JSONException 
	 */
	public String create(String title, String description,
			String primaryPhotoId) throws IOException, 
			FlickrException, JSONException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter(KEY_METHOD, METHOD_CREATE));
		//parameters.add(new Parameter(KEY_API_KEY, mApiKey));

		parameters.add(new Parameter("title", title)); //$NON-NLS-1$
		parameters.add(new Parameter(
				"description", description == null ? title : description)); //$NON-NLS-1$
		parameters.add(new Parameter("primary_photo_id", primaryPhotoId)); //$NON-NLS-1$
		OAuthUtils.addOAuthToken(parameters);
		Response response = mTransport.postJSON(mApiKey, mSharedSecret, parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
		JSONObject gallery = response.getData().getJSONObject("gallery");
		return gallery.getString("id"); //$NON-NLS-1$
	}
}
