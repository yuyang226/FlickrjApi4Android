/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickrjandroid.photos;


/**
 * @author Anthony Eden
 */
public class PhotoContext {
    public static final long serialVersionUID = 12L;

    private int count;
    private Photo previousPhoto;
    private Photo nextPhoto;

    public PhotoContext() {
        super();
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }


    public Photo getPreviousPhoto() {
        return previousPhoto;
    }

    public void setPreviousPhoto(Photo previousPhoto) {
        this.previousPhoto = previousPhoto;
    }

    public Photo getNextPhoto() {
        return nextPhoto;
    }

    public void setNextPhoto(Photo nextPhoto) {
        this.nextPhoto = nextPhoto;
    }

}
