/**
 * 
 */
package com.googlecode.flickrjandroid.collections;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.flickrjandroid.photosets.Photoset;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class Collection {
    private String id;
    private String title;
    private String description;
    private String iconLarge;
    private String iconSmall;
    private int childCount;
    private Date dateCreate;
    private String server;
    private String secret;
    
    private final List<Photoset> photoSets = new ArrayList<Photoset>();
    private final List<Collection> collections = new ArrayList<Collection>();

    /**
     * 
     */
    public Collection() {
        super();
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the iconLarge
     */
    public String getIconLarge() {
        return iconLarge;
    }

    /**
     * @param iconLarge the iconLarge to set
     */
    public void setIconLarge(String iconLarge) {
        this.iconLarge = iconLarge;
    }

    /**
     * @return the iconSmall
     */
    public String getIconSmall() {
        return iconSmall;
    }

    /**
     * @param iconSmall the iconSmall to set
     */
    public void setIconSmall(String iconSmall) {
        this.iconSmall = iconSmall;
    }

    /**
     * @return the photoSets
     */
    public List<Photoset> getPhotoSets() {
        return photoSets;
    }

    /**
     * @return the collections
     */
    public List<Collection> getCollections() {
        return collections;
    }

    /**
     * @return the childCount
     */
    public int getChildCount() {
        return childCount;
    }

    /**
     * @param childCount the childCount to set
     */
    public void setChildCount(int childCount) {
        this.childCount = childCount;
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
     * @return the server
     */
    public String getServer() {
        return server;
    }

    /**
     * @param server the server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @return the secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * @param secret the secret to set
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }
    
    

}
