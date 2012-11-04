/**
 * 
 */
package com.googlecode.flickrjandroid.stats;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class AccountStats {
    
    private int totalViews;
    private int photosViews;
    private int photostreamViews;
    private int setsViews;
    private int collectionsViews;
    private int galleriesViews;

    /**
     * 
     */
    public AccountStats() {
        super();
    }

    /**
     * @return the totalViews
     */
    public int getTotalViews() {
        return totalViews;
    }

    /**
     * @param totalViews the totalViews to set
     */
    public void setTotalViews(int totalViews) {
        this.totalViews = totalViews;
    }

    /**
     * @return the photosViews
     */
    public int getPhotosViews() {
        return photosViews;
    }

    /**
     * @param photosViews the photosViews to set
     */
    public void setPhotosViews(int photosViews) {
        this.photosViews = photosViews;
    }

    /**
     * @return the photostreamViews
     */
    public int getPhotostreamViews() {
        return photostreamViews;
    }

    /**
     * @param photostreamViews the photostreamViews to set
     */
    public void setPhotostreamViews(int photostreamViews) {
        this.photostreamViews = photostreamViews;
    }

    /**
     * @return the setsViews
     */
    public int getSetsViews() {
        return setsViews;
    }

    /**
     * @param setsViews the setsViews to set
     */
    public void setSetsViews(int setsViews) {
        this.setsViews = setsViews;
    }

    /**
     * @return the collectionsViews
     */
    public int getCollectionsViews() {
        return collectionsViews;
    }

    /**
     * @param collectionsViews the collectionsViews to set
     */
    public void setCollectionsViews(int collectionsViews) {
        this.collectionsViews = collectionsViews;
    }

    /**
     * @return the galleriesViews
     */
    public int getGalleriesViews() {
        return galleriesViews;
    }

    /**
     * @param galleriesViews the galleriesViews to set
     */
    public void setGalleriesViews(int galleriesViews) {
        this.galleriesViews = galleriesViews;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + collectionsViews;
        result = prime * result + galleriesViews;
        result = prime * result + photosViews;
        result = prime * result + photostreamViews;
        result = prime * result + setsViews;
        result = prime * result + totalViews;
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
        if (!(obj instanceof AccountStats))
            return false;
        AccountStats other = (AccountStats) obj;
        if (collectionsViews != other.collectionsViews)
            return false;
        if (galleriesViews != other.galleriesViews)
            return false;
        if (photosViews != other.photosViews)
            return false;
        if (photostreamViews != other.photostreamViews)
            return false;
        if (setsViews != other.setsViews)
            return false;
        if (totalViews != other.totalViews)
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AccountStats [totalViews=" + totalViews + ", photosViews="
                + photosViews + ", photostreamViews=" + photostreamViews
                + ", setsViews=" + setsViews + ", collectionsViews="
                + collectionsViews + ", galleriesViews=" + galleriesViews + "]";
    }
    
    

}
