/**
 * 
 */
package com.googlecode.flickrjandroid.stats;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class Stats {
    private int comments;
    private int views;
    private int favorites;
    
    /**
     * 
     */
    public Stats() {
        super();
    }

    /**
     * @return the comments
     */
    public int getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(int comments) {
        this.comments = comments;
    }

    /**
     * @return the views
     */
    public int getViews() {
        return views;
    }

    /**
     * @param views the views to set
     */
    public void setViews(int views) {
        this.views = views;
    }

    /**
     * @return the favorites
     */
    public int getFavorites() {
        return favorites;
    }

    /**
     * @param favorites the favorites to set
     */
    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }
    
    

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + comments;
        result = prime * result + favorites;
        result = prime * result + views;
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
        if (!(obj instanceof Stats))
            return false;
        Stats other = (Stats) obj;
        if (comments != other.comments)
            return false;
        if (favorites != other.favorites)
            return false;
        if (views != other.views)
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Stats [comments=" + comments + ", views=" + views
                + ", favorites=" + favorites + "]";
    }
    
}
