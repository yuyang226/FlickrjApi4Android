/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickrjandroid.photos;

import com.googlecode.flickrjandroid.util.StringUtilities;

import java.io.Serializable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.regex.Matcher;

/**
 * This class descibes a Size of a Photo.<p>
 *
 * @author Anthony Eden
 * @version $Id: Size.java,v 1.7 2009/07/23 20:41:03 x-mago Exp $
 */
public class Size implements Serializable, Comparable<Size> {
    public static final long serialVersionUID = 12L;

    /**
     * Thumbnail, 100 on longest side.
     *
     * @see com.googlecode.flickrjandroid.photos.Size#getLabel()
     * @see com.googlecode.flickrjandroid.photos.Size#setLabel(int)
     * @see com.googlecode.flickrjandroid.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int THUMB = 0;

    /**
     * Small square 75x75.
     *
     * @see com.googlecode.flickrjandroid.photos.Size#getLabel()
     * @see com.googlecode.flickrjandroid.photos.Size#setLabel(int)
     * @see com.googlecode.flickrjandroid.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int SQUARE = 1;

    /**
     * Small, 240 on longest side.
     *
     * @see com.googlecode.flickrjandroid.photos.Size#getLabel()
     * @see com.googlecode.flickrjandroid.photos.Size#setLabel(int)
     * @see com.googlecode.flickrjandroid.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int SMALL = 2;

    /**
     * Medium, 500 on longest side.
     *
     * @see com.googlecode.flickrjandroid.photos.Size#getLabel()
     * @see com.googlecode.flickrjandroid.photos.Size#setLabel(int)
     * @see com.googlecode.flickrjandroid.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int MEDIUM = 3;

    /**
     * Large, 1024 on longest side (before May 25th 2010, large photos only exist for very large original images).
     *
     * @see com.googlecode.flickrjandroid.photos.Size#getLabel()
     * @see com.googlecode.flickrjandroid.photos.Size#setLabel(int)
     * @see com.googlecode.flickrjandroid.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int LARGE = 4;

    /**
     * Original image, either a jpg, gif or png, depending on source format.<br>
     * Only from pro-users original images are available!
     *
     * @see com.googlecode.flickrjandroid.photos.Size#getLabel()
     * @see com.googlecode.flickrjandroid.photos.Size#setLabel(int)
     * @see com.googlecode.flickrjandroid.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int ORIGINAL = 5;
    
    /**
     * Large square image, 150 * 150
     *
     * @see com.googlecode.flickrjandroid.photos.Size#getLabel()
     * @see com.googlecode.flickrjandroid.photos.Size#setLabel(int)
     * @see com.googlecode.flickrjandroid.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int LARGE_SQUARE = 6;
    
    /**
     * Link to an embeddable Flash player to play the video
     */
    public static final int VIDEO_PLAYER = 7;

    /**
     * Streamable MP4 url in the resolution as offered by flickr.com
     */
    public static final int SITE_MP4 = 8;

    /**
     * Streamable MP4 url in 480*360
     */
    public static final int MOBILE_MP4 = 9;
    
    /**
     * Small, 320 on longest side
     *
     * @see com.googlecode.flickrjandroid.photos.Size#getLabel()
     * @see com.googlecode.flickrjandroid.photos.Size#setLabel(int)
     * @see com.googlecode.flickrjandroid.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int SMALL_320 = 10;
    
    /**
     * Medium 640, 640 on longest side
     *
     * @see com.googlecode.flickrjandroid.photos.Size#getLabel()
     * @see com.googlecode.flickrjandroid.photos.Size#setLabel(int)
     * @see com.googlecode.flickrjandroid.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int MEDIUM_640 = 11;
    
    /**
     * Medium 800, 800 on longest side (only exists after March 1st 2012)
     *
     * @see com.googlecode.flickrjandroid.photos.Size#getLabel()
     * @see com.googlecode.flickrjandroid.photos.Size#setLabel(int)
     * @see com.googlecode.flickrjandroid.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int MEDIUM_800 = 12;
    
    /**
     * Large 1600, 1600 on longest side (undocumented in official api)
     *
     * @see com.googlecode.flickrjandroid.photos.Size#getLabel()
     * @see com.googlecode.flickrjandroid.photos.Size#setLabel(int)
     * @see com.googlecode.flickrjandroid.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int LARGE_1600 = 13;
    
    /**
     * Large 2048, 2048 on longest side (undocumented in official api)
     *
     * @see com.googlecode.flickrjandroid.photos.Size#getLabel()
     * @see com.googlecode.flickrjandroid.photos.Size#setLabel(int)
     * @see com.googlecode.flickrjandroid.photos.PhotosInterface#getImageAsStream(Photo, int)
     */
    public static final int LARGE_2048 = 14;

    private int label;
    private int width;
    private int height;
    private String source;
    private String url;

    public Size() {

    }

    /**
     * Size of the Photo.
     *
     * @return label
     * @see com.googlecode.flickrjandroid.photos.Size#SQUARE
     * @see com.googlecode.flickrjandroid.photos.Size#LARGE_SQUARE
     * @see com.googlecode.flickrjandroid.photos.Size#THUMB
     * @see com.googlecode.flickrjandroid.photos.Size#SMALL
     * @see com.googlecode.flickrjandroid.photos.Size#SMALL_320
     * @see com.googlecode.flickrjandroid.photos.Size#MEDIUM
     * @see com.googlecode.flickrjandroid.photos.Size#MEDIUM_640
     * @see com.googlecode.flickrjandroid.photos.Size#MEDIUM_800
     * @see com.googlecode.flickrjandroid.photos.Size#LARGE
     * @see com.googlecode.flickrjandroid.photos.Size#LARGE_1600
     * @see com.googlecode.flickrjandroid.photos.Size#LARGE_2048
     * @see com.googlecode.flickrjandroid.photos.Size#ORIGINAL
     */
    public int getLabel() {
        return label;
    }

    /**
     * Set the String-representation of size.
     *
     * Like: Square, Thumbnail, Small, Medium, Large, Original.
     * @param label
     */
    public void setLabel(String label) {
        if (label.equals("Square")) {
            setLabel(SQUARE);
        } else if( label.equals("Large Square")) {
            setLabel(LARGE_SQUARE);
        } else if (label.equals("Thumbnail")) {
            setLabel(THUMB);
        } else if (label.equals("Small")) {
            setLabel(SMALL);
        } else if (label.equals("Small 320")) {
            setLabel(SMALL_320);
        } else if (label.equals("Medium")) {
            setLabel(MEDIUM);
        } else if (label.equals("Medium 640")) {
            setLabel(MEDIUM_640);
        } else if (label.equals("Medium 800")) {
            setLabel(MEDIUM_800);
        } else if (label.equals("Large")) {
            setLabel(LARGE);
        } else if (label.equals("Large 1600")) {
            setLabel(LARGE_1600);
        } else if (label.equals("Large 2048")) {
            setLabel(LARGE_2048);
        } else if (label.equals("Original")) {
            setLabel(ORIGINAL);
        } else if( label.equals("Video Player")) {
            setLabel(VIDEO_PLAYER);
        } else if( label.equals("Site MP4")) {
            setLabel(SITE_MP4);
        } else if( label.equals("Mobile MP4")) {
            setLabel(MOBILE_MP4);
        }
    }

    public int getLongestDimension() {
        if (label == SQUARE) {
            return 75;
        } else if (label == THUMB) {
        	return 100;
        } else if(label == LARGE_SQUARE) {
            return 150;
        } else if (label == SMALL) {
            return 240;
        } else if (label == SMALL_320) {
            return 320;
        } else if (label == MEDIUM) {
            return 500;
        } else if (label == MEDIUM_640) {
            return 640;
        } else if (label == MEDIUM_800) {
            return 800;
        } else if (label == LARGE) {
            return 1024;
        } else if (label == LARGE_1600) {
            return 1600;
        } else if (label == LARGE_2048) {
            return 2048;
        } else {
            return Math.max(getWidth(), getHeight());
        }
    }
    
    /**
     * Size of the Photo.
     *
     * @param label The integer-representation of a size
     * @see com.googlecode.flickrjandroid.photos.Size#SQUARE
     * @see com.googlecode.flickrjandroid.photos.Size#LARGE_SQUARE
     * @see com.googlecode.flickrjandroid.photos.Size#THUMB
     * @see com.googlecode.flickrjandroid.photos.Size#SMALL
     * @see com.googlecode.flickrjandroid.photos.Size#SMALL_320
     * @see com.googlecode.flickrjandroid.photos.Size#MEDIUM
     * @see com.googlecode.flickrjandroid.photos.Size#MEDIUM_640
     * @see com.googlecode.flickrjandroid.photos.Size#MEDIUM_800
     * @see com.googlecode.flickrjandroid.photos.Size#LARGE
     * @see com.googlecode.flickrjandroid.photos.Size#LARGE_1600
     * @see com.googlecode.flickrjandroid.photos.Size#LARGE_2048
     * @see com.googlecode.flickrjandroid.photos.Size#ORIGINAL
     */
    public void setLabel(int label) {
        this.label = label;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setWidth(String width) {
        if (width != null) {
            setWidth(Integer.parseInt(width));
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setHeight(String height) {
        if (height != null) {
            setHeight(Integer.parseInt(height));
        }
    }

    /**
     * URL of the image.
     *
     * @return Image-URL
     */
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * URL of the photopage.
     *
     * @return Page-URL
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
        // object must be GeoData at this point
        Size test = (Size) obj;
        Class<?> cl = this.getClass();
        Method[] method = cl.getMethods();
        for (int i = 0; i < method.length; i++) {
            Matcher m = StringUtilities.getterPattern.matcher(method[i].getName());
            if (m.find() && !method[i].getName().equals("getClass")) {
                try {
                    Object res = method[i].invoke(this, (Object[])null);
                    Object resTest = method[i].invoke(test, (Object[])null);
                    String retType = method[i].getReturnType().toString();
                    if (retType.indexOf("class") == 0) {
                        if (res != null && resTest != null) {
                            if (!res.equals(resTest)) return false;
                        } else {
                            //return false;
                        }
                    } else if (retType.equals("int")) {
                        if (!((Integer) res).equals(((Integer)resTest))) return false;
                    } else {
                        System.out.println(method[i].getName() + "|" +
                            method[i].getReturnType().toString());
                    }
                } catch (IllegalAccessException ex) {
                    System.out.println("Size equals " + method[i].getName() + " " + ex);
                } catch (InvocationTargetException ex) {
                    //System.out.println("equals " + method[i].getName() + " " + ex);
                } catch (Exception ex) {
                    System.out.println("Size equals " + method[i].getName() + " " + ex);
                }
            }
        }
        return true;
    }

    @Override
    public int compareTo(Size other) {
    	return (this.getLongestDimension() - other.getLongestDimension());
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash += new Integer(label).hashCode();
        hash += new Integer(width).hashCode();
        hash += new Integer(height).hashCode();
        if (source != null) hash += source.hashCode();
        if (url != null) hash += url.hashCode();
        return hash;
    }
}
