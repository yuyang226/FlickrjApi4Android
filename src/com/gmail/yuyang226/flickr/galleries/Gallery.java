/**
 * 
 */
package com.gmail.yuyang226.flickr.galleries;

/**
 * Represents the flickr gallery.
 * 
 * @author charles
 */
public class Gallery {
	private String galleryId;
	private String galleryUrl;
	private String ownerId;
	private String primaryPhotoId;
	private int photoCount;
	private int videoCount;
	private String title;
	private String description;

	public String getGalleryId() {
		return galleryId;
	}

	public void setGalleryId(String galleryId) {
		this.galleryId = galleryId;
	}

	public String getGalleryUrl() {
		return galleryUrl;
	}

	public void setGalleryUrl(String galleryUrl) {
		this.galleryUrl = galleryUrl;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getPrimaryPhotoId() {
		return primaryPhotoId;
	}

	public void setPrimaryPhotoId(String primaryPhotoId) {
		this.primaryPhotoId = primaryPhotoId;
	}

	public int getPhotoCount() {
		return photoCount;
	}

	public void setPhotoCount(int photoCount) {
		this.photoCount = photoCount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setVideoCount(int videoCount) {
		this.videoCount = videoCount;
	}

	public int getVideoCount() {
		return videoCount;
	}

	public int getTotalCount() {
		return getPhotoCount() + getVideoCount();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((galleryId == null) ? 0 : galleryId.hashCode());
		result = prime * result
				+ ((galleryUrl == null) ? 0 : galleryUrl.hashCode());
		result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
		result = prime * result + photoCount;
		result = prime * result
				+ ((primaryPhotoId == null) ? 0 : primaryPhotoId.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + videoCount;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Gallery))
			return false;
		Gallery other = (Gallery) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (galleryId == null) {
			if (other.galleryId != null)
				return false;
		} else if (!galleryId.equals(other.galleryId))
			return false;
		if (galleryUrl == null) {
			if (other.galleryUrl != null)
				return false;
		} else if (!galleryUrl.equals(other.galleryUrl))
			return false;
		if (ownerId == null) {
			if (other.ownerId != null)
				return false;
		} else if (!ownerId.equals(other.ownerId))
			return false;
		if (photoCount != other.photoCount)
			return false;
		if (primaryPhotoId == null) {
			if (other.primaryPhotoId != null)
				return false;
		} else if (!primaryPhotoId.equals(other.primaryPhotoId))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (videoCount != other.videoCount)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Gallery [galleryId=" + galleryId + ", galleryUrl=" + galleryUrl
				+ ", ownerId=" + ownerId + ", primaryPhotoId=" + primaryPhotoId
				+ ", photoCount=" + photoCount + ", videoCount=" + videoCount
				+ ", title=" + title + ", description=" + description + "]";
	}
	
}
