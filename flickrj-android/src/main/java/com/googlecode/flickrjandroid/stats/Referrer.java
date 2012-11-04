/**
 * 
 */
package com.googlecode.flickrjandroid.stats;

import java.net.URL;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class Referrer {
    private URL url;
    private int views;
    private String searchterm;

    /**
     * 
     */
    public Referrer() {
        super();
    }

    /**
     * @return the url
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(URL url) {
        this.url = url;
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
     * @return the searchterm
     */
    public String getSearchterm() {
        return searchterm;
    }

    /**
     * @param searchterm the searchterm to set
     */
    public void setSearchterm(String searchterm) {
        this.searchterm = searchterm;
    }

    
}
