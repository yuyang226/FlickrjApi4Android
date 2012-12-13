/**
 * 
 */
package com.googlecode.flickrjandroid.galleries;

import java.util.Date;

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
    private int viewsCount;
    private int commentsCount;
    private String title;
    private String description;
    private Date dateCreate;
    private Date dateUpdate;
    
    /**
     * 
     */
    public Gallery() {
        super();
    }

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
    
    /**
     * @return the dateCreate
     */
    public Date getDateCreate() {
        return dateCreate;
    }

    /**
     * @param dateCreate the dateCreate to set
     */
    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }
    
    public void setDateCreate(String dateCreate) {
        if (dateCreate == null || "".equals(dateCreate)) return;
        long unixTime = Long.parseLong(dateCreate);
        setDateCreate(new Date(unixTime * 1000L));
    }
    
    /**
     * @return the dateUpdate
     */
    public Date getDateUpdate() {
        return dateUpdate;
    }

    /**
     * @param dateUpdate the dateUpdate to set
     */
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
    
    public void setDateUpdate(String dateUpdate) {
        if (dateUpdate == null || "".equals(dateUpdate)) return;
        long unixTime = Long.parseLong(dateUpdate);
        setDateUpdate(new Date(unixTime * 1000L));
    }
    
    /**
     * @return the viewsCount
     */
    public int getViewsCount() {
        return viewsCount;
    }

    /**
     * @param viewsCount the viewsCount to set
     */
    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    /**
     * @return the commentsCount
     */
    public int getCommentsCount() {
        return commentsCount;
    }

    /**
     * @param commentsCount the commentsCount to set
     */
    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + commentsCount;
        result = prime * result
                + ((dateCreate == null) ? 0 : dateCreate.hashCode());
        result = prime * result
                + ((dateUpdate == null) ? 0 : dateUpdate.hashCode());
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
        result = prime * result + viewsCount;
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
        if (commentsCount != other.commentsCount)
            return false;
        if (dateCreate == null) {
            if (other.dateCreate != null)
                return false;
        } else if (!dateCreate.equals(other.dateCreate))
            return false;
        if (dateUpdate == null) {
            if (other.dateUpdate != null)
                return false;
        } else if (!dateUpdate.equals(other.dateUpdate))
            return false;
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
        if (viewsCount != other.viewsCount)
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
                + ", viewsCount=" + viewsCount + ", commentsCount="
                + commentsCount + ", title=" + title + ", description="
                + description + ", dateCreate=" + dateCreate + ", dateUpdate="
                + dateUpdate + "]";
    }

}
