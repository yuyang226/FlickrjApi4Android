/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickrjandroid.contacts;

import com.googlecode.flickrjandroid.util.BuddyIconable;
import com.googlecode.flickrjandroid.util.UrlUtilities;

/**
 * Class representing a Flickr contact.
 *
 * @author Anthony Eden
 * @version $Id: Contact.java,v 1.5 2009/07/12 22:43:07 x-mago Exp $
 */
public class Contact implements BuddyIconable {
    public static final long serialVersionUID = 12L;

    private String id;
    private String username;
    private String realName;
    private boolean friend;
    private boolean family;
    private boolean ignored;
    private String pathAlias;
    private int iconFarm;
    private int iconServer;
    private String location;
    
    public Contact() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }

    public boolean isFamily() {
        return family;
    }

    public void setFamily(boolean family) {
        this.family = family;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    /**
     * Construct the BuddyIconUrl.<p>
     * If none available, return the
     * <a href="http://www.flickr.com/images/buddyicon.jpg">default</a>,
     * or an URL assembled from farm, iconserver and nsid.
     *
     * @see <a href="http://flickr.com/services/api/misc.buddyicons.html">Flickr Documentation</a>
     * @return The BuddyIconUrl
     */
    public String getBuddyIconUrl() {
        return UrlUtilities.createBuddyIconUrl(iconFarm, iconServer, id);
    }

    public int getIconFarm() {
        return iconFarm;
    }

    public void setIconFarm(int iconFarm) {
        this.iconFarm = iconFarm;
    }

    public void setIconFarm(String iconFarm) {
        setIconFarm(Integer.parseInt(iconFarm));
    }

    public int getIconServer() {
        return iconServer;
    }

    public void setIconServer(int iconServer) {
        this.iconServer = iconServer;
    }

    public void setIconServer(String iconServer) {
        setIconServer(Integer.parseInt(iconServer));
    }

    /**
     * @return the pathAlias
     */
    public String getPathAlias() {
        return pathAlias;
    }

    /**
     * @param pathAlias the pathAlias to set
     */
    public void setPathAlias(String pathAlias) {
        this.pathAlias = pathAlias;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Contact [id=" + id + ", username=" + username + ", realName="
                + realName + "]";
    }
    
    
}
