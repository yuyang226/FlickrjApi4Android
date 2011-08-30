package com.aetrion.flickr.photos;

import java.util.ArrayList;
import java.util.List;

import com.aetrion.flickr.people.User;
import com.aetrion.flickr.tags.Tag;
import com.yuyang226.flickr.org.json.JSONArray;
import com.yuyang226.flickr.org.json.JSONException;
import com.yuyang226.flickr.org.json.JSONObject;

/**
 * Utilitiy-methods to transfer requested XML to Photo-objects.
 *
 * @author till, x-mago
 * @version $Id: PhotoUtils.java,v 1.20 2009/07/23 21:49:35 x-mago Exp $
 */
public final class PhotoUtils {
	private static final long serialVersionUID = 12L;

	private PhotoUtils() {
	}

	/**
	 * Transfer the Information of a photo from a JSONObject
	 * to a Photo-object.
	 *
	 * @param photoElement
	 * @return Photo
	 * @throws JSONException 
	 */
	public static final Photo createPhoto(JSONObject photoElement) throws JSONException {
		Photo photo = new Photo();
		photo.setId(photoElement.getString("id"));
		photo.setPlaceId(photoElement.optString("place_id", null));
		photo.setSecret(photoElement.getString("secret"));
		photo.setServer(photoElement.getString("server"));
		photo.setFarm(photoElement.getString("farm"));
		photo.setRotation(photoElement.optString("rotation", null));
		photo.setFavorite("1".equals(photoElement.optString("isfavorite")));
		photo.setLicense(photoElement.optString("license"));
		photo.setOriginalFormat(photoElement.optString("originalformat"));
		photo.setOriginalSecret(photoElement.optString("originalsecret"));
		photo.setIconServer(photoElement.optString("iconserver", null));
		photo.setIconFarm(photoElement.optString("iconfarm", null));
		photo.setDateTaken(photoElement.optString("datetaken"));
		photo.setDatePosted(photoElement.optString("dateupload"));
		photo.setLastUpdate(photoElement.optString("lastupdate"));
		// flickr.groups.pools.getPhotos provides this value!
		photo.setDateAdded(photoElement.optString("dateadded"));
		photo.setOriginalWidth(photoElement.optString("o_width"));
		photo.setOriginalHeight(photoElement.optString("o_height"));
		photo.setMedia(photoElement.optString("media"));
		photo.setMediaStatus(photoElement.optString("media_status"));
		photo.setPathAlias(photoElement.optString("pathalias"));

		// If the attributes active that contain the image-urls,
		// Size-objects created from them, which are used to override
		// the Url-generation.
		List<Size> sizes = new ArrayList<Size>();
		String urlTmp = photoElement.optString("url_t");
		if (urlTmp.startsWith("http")) {
			Size sizeT = new Size();
			sizeT.setLabel(Size.THUMB);
			sizeT.setSource(urlTmp);
			sizes.add(sizeT);
		}
		urlTmp = photoElement.optString("url_s");
		if (urlTmp.startsWith("http")) {
			Size sizeT = new Size();
			sizeT.setLabel(Size.SMALL);
			sizeT.setSource(urlTmp);
			sizes.add(sizeT);
		}
		urlTmp = photoElement.optString("url_sq");
		if (urlTmp.startsWith("http")) {
			Size sizeT = new Size();
			sizeT.setLabel(Size.SQUARE);
			sizeT.setSource(urlTmp);
			sizes.add(sizeT);
		}
		urlTmp = photoElement.optString("url_m");
		if (urlTmp.startsWith("http")) {
			Size sizeT = new Size();
			sizeT.setLabel(Size.MEDIUM);
			sizeT.setSource(urlTmp);
			sizes.add(sizeT);
		}
		urlTmp = photoElement.optString("url_l");
		if (urlTmp.startsWith("http")) {
			Size sizeT = new Size();
			sizeT.setLabel(Size.LARGE);
			sizeT.setSource(urlTmp);
			sizes.add(sizeT);
		}
		urlTmp = photoElement.optString("url_o");
		if (urlTmp.startsWith("http")) {
			Size sizeT = new Size();
			sizeT.setLabel(Size.ORIGINAL);
			sizeT.setSource(urlTmp);
			sizes.add(sizeT);
		}
		if (sizes.size() > 0) {
			photo.setSizes(sizes);
		}

		if (photo.getOriginalFormat() == null 
				|| photo.getOriginalFormat().equals("")) {
			photo.setOriginalFormat("jpg");
		}

		User owner = new User();
		owner.setId(photoElement.getString("owner"));
		owner.setUsername(photoElement.optString("ownername", null));
		photo.setOwner(owner);
		photo.setUrl("http://flickr.com/photos/" + owner.getId() + "/" + photo.getId());
		photo.setTitle(photoElement.getString("title"));
		photo.setDescription(photoElement.optString("description", null));

		// here the flags are set, if the photo is read by getInfo().
		photo.setPublicFlag("1".equals(photoElement.getString("ispublic")));
		photo.setFriendFlag("1".equals(photoElement.getString("isfriend")));
		photo.setFamilyFlag("1".equals(photoElement.getString("isfamily")));

		// Parse either photo by getInfo, or from list
		/*try {
			JSONObject datesElement = photoElement.optJSONObject("dates");
			photo.setDatePosted(datesElement.getAttribute("posted"));
			photo.setDateTaken(datesElement.getAttribute("taken"));
			photo.setTakenGranularity(datesElement.getAttribute("takengranularity"));
			photo.setLastUpdate(datesElement.getAttribute("lastupdate"));
		} catch (IndexOutOfBoundsException e) {
			photo.setDateTaken(photoElement.getAttribute("datetaken"));
		} catch (NullPointerException e) {
			photo.setDateTaken(photoElement.getAttribute("datetaken"));
		}*/

		/*NodeList permissionsNodes = photoElement.getElementsByTagName("permissions");
		if (permissionsNodes.getLength() > 0) {
			Element permissionsElement = (Element) permissionsNodes.item(0);
			Permissions permissions = new Permissions();
			permissions.setComment(permissionsElement.getAttribute("permcomment"));
			permissions.setAddmeta(permissionsElement.getAttribute("permaddmeta"));
		}*/

		/*try {
			Element editabilityElement = (Element) photoElement.getElementsByTagName("editability").item(0);
			Editability editability = new Editability();
			editability.setComment("1".equals(editabilityElement.getAttribute("cancomment")));
			editability.setAddmeta("1".equals(editabilityElement.getAttribute("canaddmeta")));
			photo.setEditability(editability);
		} catch (IndexOutOfBoundsException e) {
		} catch (NullPointerException e) {
			// nop
		}*/

		/*try {
			Element commentsElement = (Element) photoElement.getElementsByTagName("comments").item(0);
			photo.setComments(((Text) commentsElement.getFirstChild()).getData());
		} catch (IndexOutOfBoundsException e) {
		} catch (NullPointerException e) {
			// nop
		}*/

		/*try {
			Element notesElement = (Element) photoElement.getElementsByTagName("notes").item(0);
			List<Note> notes = new ArrayList<Note>();
			NodeList noteNodes = notesElement.getElementsByTagName("note");
			for (int i = 0; i < noteNodes.getLength(); i++) {
				Element noteElement = (Element) noteNodes.item(i);
				Note note = new Note();
				note.setId(noteElement.getAttribute("id"));
				note.setAuthor(noteElement.getAttribute("author"));
				note.setAuthorName(noteElement.getAttribute("authorname"));
				note.setBounds(noteElement.getAttribute("x"), noteElement.getAttribute("y"),
						noteElement.getAttribute("w"), noteElement.getAttribute("h"));
				note.setText(noteElement.getTextContent());
				notes.add(note);
			}
			photo.setNotes(notes);
		} catch (IndexOutOfBoundsException e) {
			photo.setNotes(new ArrayList<Note>());
		} catch (NullPointerException e) {
			photo.setNotes(new ArrayList<Note>());
		}*/

		// Tags coming as space-seperated attribute calling
		// InterestingnessInterface#getList().
		// Through PhotoInterface#getInfo() the Photo has a list of
		// Elements.
		try {
			List<Tag> tags = new ArrayList<Tag>();
			String tagsAttr = photoElement.optString("tags", null);
			if (tagsAttr != null) {
				String[] values = tagsAttr.split(" ");
				for (int i = 0; i < values.length; i++) {
					Tag tag = new Tag();
					tag.setValue(values[i]);
					tags.add(tag);
				}
			} else {
				/*try {
					Element tagsElement = (Element) photoElement.getElementsByTagName("tags").item(0);
					NodeList tagNodes = tagsElement.getElementsByTagName("tag");
					for (int i = 0; i < tagNodes.getLength(); i++) {
						Element tagElement = (Element) tagNodes.item(i);
						Tag tag = new Tag();
						tag.setId(tagElement.getAttribute("id"));
						tag.setAuthor(tagElement.getAttribute("author"));
						tag.setRaw(tagElement.getAttribute("raw"));
						tag.setValue(((Text) tagElement.getFirstChild()).getData());
						tags.add(tag);
					}
				} catch (IndexOutOfBoundsException e) {
				}*/
			}
			photo.setTags(tags);
		} catch (NullPointerException e) {
			photo.setTags(new ArrayList<Tag>());
		}

		/*try {
			Element urlsElement = (Element) photoElement.getElementsByTagName("urls").item(0);
			List<PhotoUrl> urls = new ArrayList<PhotoUrl>();
			NodeList urlNodes = urlsElement.getElementsByTagName("url");
			for (int i = 0; i < urlNodes.getLength(); i++) {
				Element urlElement = (Element) urlNodes.item(i);
				PhotoUrl photoUrl = new PhotoUrl();
				photoUrl.setType(urlElement.getAttribute("type"));
				photoUrl.setUrl(XMLUtilities.getValue(urlElement));
				if (photoUrl.getType().equals("photopage")) {
					photo.setUrl(photoUrl.getUrl());
				}
			}
			photo.setUrls(urls);
		} catch (IndexOutOfBoundsException e) {
		} catch (NullPointerException e) {
			photo.setUrls(new ArrayList<PhotoUrl>());
		}*/

		//"latitude": 31.344969, "longitude": "121.371238", "accuracy": 16, 
		//"place_id": "JAJiM7JTU78IjzqC", "woeid": "2151849", "geo_is_family": 0, "geo_is_friend": 0, "geo_is_contact": 0, "geo_is_public": 1 },

		String longitude = photoElement.optString("longitude", null);
		String latitude = photoElement.optString("latitude", null);
		String accuracy = photoElement.optString("accuracy", null);
		String woeId = photoElement.optString("woeid", null);
		if (longitude != null && latitude != null) {
			if (longitude.length() > 0 && latitude.length() > 0
					&& !("0".equals(longitude) && "0".equals(latitude))) {
				photo.setGeoData(new GeoData(longitude, latitude, accuracy));
			}
		}

		return photo;
	}

	/**
	 * Parse a list of Photos from given Element.
	 *
	 * @param responseData
	 * @return PhotoList
	 * @throws JSONException 
	 */
	public static final PhotoList createPhotoList(JSONObject responseData) throws JSONException {
		JSONObject photosElement = responseData.getJSONObject("photos");
		PhotoList photos = new PhotoList();
		photos.setPage(photosElement.optInt("page"));
		photos.setPages(photosElement.optInt("pages"));
		photos.setPerPage(photosElement.optInt("perpage"));
		photos.setTotal(photosElement.optInt("total"));

		JSONArray photoNodes = photosElement.optJSONArray("photo");
		for (int i = 0; photoNodes != null && i < photoNodes.length(); i++) {
			JSONObject photoElement = photoNodes.getJSONObject(i);
			photos.add(PhotoUtils.createPhoto(photoElement));
		}
		return photos;
	}

}
