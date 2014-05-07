package com.googlecode.flickrjandroid.photos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.tags.Tag;
import com.googlecode.flickrjandroid.util.JSONUtils;

/**
 * Utilitiy-methods to transfer requested XML to Photo-objects.
 * 
 * @author till, x-mago
 * @version $Id: PhotoUtils.java,v 1.20 2009/07/23 21:49:35 x-mago Exp $
 */
public final class PhotoUtils {
    public static final long serialVersionUID = 12L;

    private static final String PHOTO_SUFFIX_SQUARE = "_sq";
    private static final String PHOTO_SUFFIX_LARGE_SQUARE = "_q";
    private static final String PHOTO_SUFFIX_THUMB = "_t";
    private static final String PHOTO_SUFFIX_SMALL = "_s";
    private static final String PHOTO_SUFFIX_SMALL_320 = "_n";
    private static final String PHOTO_SUFFIX_MEDIUM = "_m";
    private static final String PHOTO_SUFFIX_MEDIUM_640 = "_z";
    private static final String PHOTO_SUFFIX_MEDIUM_800 = "_c";
    private static final String PHOTO_SUFFIX_LARGE = "_l";
    private static final String PHOTO_SUFFIX_LARGE_1600 = "_h";
    private static final String PHOTO_SUFFIX_LARGE_2048 = "_k";
    private static final String PHOTO_SUFFIX_ORIGINAL = "_o";

    private PhotoUtils() {
        super();
    }
    
    /**
     * Transfer the Information of a photo context from a JSONObject to a PhotoContext-object.
     * 
     * @param data
     * @return
     * @throws JSONException
     */
    public static final PhotoContext createPhotoContext(JSONObject data) throws JSONException {
        String count = JSONUtils.getChildValue(data, "count");
        PhotoContext photoContext = new PhotoContext();
        photoContext.setCount(Integer.parseInt(count));
        JSONObject payload = data;
        Iterator<?> iter = payload.keys();
        while (iter.hasNext()) {
            String tagName = String.valueOf(iter.next());
            JSONObject payloadElement = payload.optJSONObject(tagName);
            if (payloadElement == null)
                continue;
            if (tagName.equals("prevphoto")) {
                Photo photo = new Photo();
                photo.setId(payloadElement.getString("id"));
                photo.setSecret(payloadElement.getString("secret"));
                photo.setTitle(payloadElement.getString("title"));
                photo.setFarm(payloadElement.getString("farm"));
                photo.setUrl(payloadElement.getString("url"));
                photoContext.setPreviousPhoto(photo);
            } else if (tagName.equals("nextphoto")) {
                Photo photo = new Photo();
                photo.setId(payloadElement.getString("id"));
                photo.setSecret(payloadElement.getString("secret"));
                photo.setTitle(payloadElement.getString("title"));
                photo.setFarm(payloadElement.getString("farm"));
                photo.setUrl(payloadElement.getString("url"));
                photoContext.setNextPhoto(photo);
            }
        }
        return photoContext;
    }

    /**
     * Transfer the Information of a photo from a JSONObject to a Photo-object.
     * 
     * @param photoElement
     * @return Photo
     * @throws JSONException
     */
    public static final Photo createPhoto(JSONObject photoElement)
            throws JSONException {
        Photo photo = new Photo();
        photo.setId(photoElement.getString("id"));
        photo.setPlaceId(photoElement.optString("place_id", null));
        photo.setSecret(photoElement.getString("secret"));
        photo.setServer(photoElement.getString("server"));
        photo.setFarm(photoElement.getString("farm"));
        photo.setRotation(photoElement.optString("rotation", null));
        photo.setFavorite("1".equals(photoElement.optString("isfavorite")));
        photo.setPrimary(photoElement.optString("isprimary", null));
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
        
        JSONObject dates = photoElement.optJSONObject("dates");
        if (dates != null) {
            photo.setDateTaken(dates.optString("taken"));
            photo.setDatePosted(dates.optString("posted"));
            photo.setLastUpdate(dates.optString("lastupdate"));
        }

        // If the attributes active that contain the image-urls,
        // Size-objects created from them, which are used to override
        // the Url-generation.
        List<Size> sizes = new ArrayList<Size>();
        addSize(sizes, photoElement, PHOTO_SUFFIX_SQUARE, Size.SQUARE);
        addSize(sizes, photoElement, PHOTO_SUFFIX_LARGE_SQUARE, Size.LARGE_SQUARE);
        addSize(sizes, photoElement, PHOTO_SUFFIX_THUMB, Size.THUMB);
        addSize(sizes, photoElement, PHOTO_SUFFIX_SMALL, Size.SMALL);
        addSize(sizes, photoElement, PHOTO_SUFFIX_SMALL_320, Size.SMALL_320);
        addSize(sizes, photoElement, PHOTO_SUFFIX_MEDIUM, Size.MEDIUM);
        addSize(sizes, photoElement, PHOTO_SUFFIX_MEDIUM_640, Size.MEDIUM_640);
        addSize(sizes, photoElement, PHOTO_SUFFIX_MEDIUM_800, Size.MEDIUM_800);
        addSize(sizes, photoElement, PHOTO_SUFFIX_LARGE, Size.LARGE);
        addSize(sizes, photoElement, PHOTO_SUFFIX_LARGE_1600, Size.LARGE_1600);
        addSize(sizes, photoElement, PHOTO_SUFFIX_LARGE_2048, Size.LARGE_2048);
        addSize(sizes, photoElement, PHOTO_SUFFIX_ORIGINAL, Size.ORIGINAL);
        
        if (sizes.size() > 0) {
            photo.setSizes(sizes);
        }

        if (photo.getOriginalFormat() == null
                || photo.getOriginalFormat().equals("")) {
            photo.setOriginalFormat("jpg");
        }

        User owner = new User();
        if (photoElement.has("owner")) {
            Object obj = photoElement.get("owner");
            if (obj instanceof JSONObject) {
                JSONObject ownerObj = (JSONObject) obj;
                owner.setId(ownerObj.getString("nsid"));
                owner.setUsername(ownerObj.optString("username", null));
                owner.setRealName(ownerObj.optString("realname", null));
                owner.setLocation(ownerObj.optString("location", null));
                photo.setOwner(owner);
            } else {
                owner.setId(photoElement.getString("owner"));
                owner.setUsername(photoElement.optString("ownername", null));
                photo.setOwner(owner);
            }
            
            photo.setUrl("https://flickr.com/photos/" + owner.getId() + "/"
                    + photo.getId());
        }
        
        JSONObject titleObj = photoElement.optJSONObject("title");
        if (titleObj != null) {
            photo.setTitle(titleObj.getString("_content"));
        } else {
            photo.setTitle(photoElement.getString("title"));
        }

        photo.setDescription(JSONUtils.getChildValue(photoElement,
                "description"));

        // here the flags are set, if the photo is read by getInfo().
        photo.setPublicFlag("1".equals(photoElement.optString("ispublic")));
        photo.setFriendFlag("1".equals(photoElement.optString("isfriend")));
        photo.setFamilyFlag("1".equals(photoElement.optString("isfamily")));

        // Parse either photo by getInfo, or from list
        /*
         * try { JSONObject datesElement = photoElement.optJSONObject("dates");
         * photo.setDatePosted(datesElement.getAttribute("posted"));
         * photo.setDateTaken(datesElement.getAttribute("taken"));
         * photo.setTakenGranularity
         * (datesElement.getAttribute("takengranularity"));
         * photo.setLastUpdate(datesElement.getAttribute("lastupdate")); } catch
         * (IndexOutOfBoundsException e) {
         * photo.setDateTaken(photoElement.getAttribute("datetaken")); } catch
         * (NullPointerException e) {
         * photo.setDateTaken(photoElement.getAttribute("datetaken")); }
         */

        /*
         * NodeList permissionsNodes =
         * photoElement.getElementsByTagName("permissions"); if
         * (permissionsNodes.getLength() > 0) { Element permissionsElement =
         * (Element) permissionsNodes.item(0); Permissions permissions = new
         * Permissions();
         * permissions.setComment(permissionsElement.getAttribute(
         * "permcomment"));
         * permissions.setAddmeta(permissionsElement.getAttribute
         * ("permaddmeta")); }
         */

        /*
         * try { Element editabilityElement = (Element)
         * photoElement.getElementsByTagName("editability").item(0); Editability
         * editability = new Editability();
         * editability.setComment("1".equals(editabilityElement
         * .getAttribute("cancomment")));
         * editability.setAddmeta("1".equals(editabilityElement
         * .getAttribute("canaddmeta"))); photo.setEditability(editability); }
         * catch (IndexOutOfBoundsException e) { } catch (NullPointerException
         * e) { // nop }
         */

        if (photoElement.has("comments")) {
            photo.setComments(JSONUtils.getChildValue(photoElement, "comments"));
        }

        JSONObject notesElement = photoElement.optJSONObject("notes");
        List<Note> notes = new ArrayList<Note>();
        if (notesElement != null) {
            JSONArray noteNodes = notesElement.optJSONArray("note");
            for (int i = 0; noteNodes != null && i < noteNodes.length(); i++) {
                JSONObject noteElement = noteNodes.getJSONObject(i);
                Note note = new Note();
                note.setId(noteElement.getString("id"));
                note.setAuthor(noteElement.getString("author"));
                note.setAuthorName(noteElement.getString("authorname"));
                note.setBounds(noteElement.getString("x"), noteElement
                        .getString("y"), noteElement.getString("w"),
                        noteElement.getString("h"));
                note.setText(noteElement.getString("_content"));
                notes.add(note);
            }
        }
        photo.setNotes(notes);

        // Tags coming as space-seperated attribute calling
        // InterestingnessInterface#getList().
        // Through PhotoInterface#getInfo() the Photo has a list of
        // Elements.
        try {
            List<Tag> tags = new ArrayList<Tag>();
            Object obj = photoElement.opt("tags");
            if (obj instanceof JSONObject) {
                JSONObject tagsObject = (JSONObject) obj;
                JSONArray tagNodes = tagsObject.optJSONArray("tag");
                for (int i = 0; tagNodes != null && i < tagNodes.length(); i++) {
                    JSONObject tagElement = tagNodes.getJSONObject(i);
                    Tag tag = new Tag();
                    tag.setId(tagElement.getString("id"));
                    tag.setAuthor(tagElement.getString("author"));
                    tag.setRaw(tagElement.getString("raw"));
                    tag.setValue(tagElement.optString("_content"));
                    tags.add(tag);
                }
            } else if (obj instanceof String) {
                String tagsAttr = obj.toString();
                String[] values = tagsAttr.split(" ");
                for (int i = 0; i < values.length; i++) {
                    Tag tag = new Tag();
                    tag.setValue(values[i]);
                    tags.add(tag);
                }
            }
            photo.setTags(tags);
        } catch (NullPointerException e) {
            photo.setTags(new ArrayList<Tag>());
        }

        /*
         * try { Element urlsElement = (Element)
         * photoElement.getElementsByTagName("urls").item(0); List<PhotoUrl>
         * urls = new ArrayList<PhotoUrl>(); NodeList urlNodes =
         * urlsElement.getElementsByTagName("url"); for (int i = 0; i <
         * urlNodes.getLength(); i++) { Element urlElement = (Element)
         * urlNodes.item(i); PhotoUrl photoUrl = new PhotoUrl();
         * photoUrl.setType(urlElement.getAttribute("type"));
         * photoUrl.setUrl(XMLUtilities.getValue(urlElement)); if
         * (photoUrl.getType().equals("photopage")) {
         * photo.setUrl(photoUrl.getUrl()); } } photo.setUrls(urls); } catch
         * (IndexOutOfBoundsException e) { } catch (NullPointerException e) {
         * photo.setUrls(new ArrayList<PhotoUrl>()); }
         */

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
        
        JSONObject statsElement = photoElement.optJSONObject("stats");
        if (statsElement != null) {
            photo.setViews(statsElement.optInt("views", -1));
            photo.setComments(statsElement.optInt("comments", -1));
            photo.setFavorites(statsElement.optInt("favorites", -1));
        } else {
            photo.setViews(photoElement.optInt("views", -1));
        }

        return photo;
    }

    private static void addSize(final List<Size> sizes, final JSONObject photoElement, final String keySuffix, final int sizeLabel) {
        String urlTmp = photoElement.optString("url" + keySuffix);
        if (urlTmp.startsWith("http")) {
            Size sizeT = new Size();
            sizeT.setLabel(sizeLabel);
            sizeT.setSource(urlTmp);

            if (photoElement.has("width" + keySuffix)) {
                sizeT.setWidth(photoElement.optInt("width" + keySuffix, 0));
            }

            if (photoElement.has("height" + keySuffix)) {
                sizeT.setHeight(photoElement.optInt("height" + keySuffix, 0));
            }
            sizes.add(sizeT);
        }
    }

    
    /**
     * Parse a list of Photos from given Element.
     * 
     * @param responseData
     * @return PhotoList
     * @throws JSONException
     */
    public static final PhotoList createPhotoList(JSONObject responseData)
            throws JSONException {
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
