/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.aetrion.flickr.auth;

import java.io.Serializable;

import com.aetrion.flickr.people.User;

/**
 * @author Anthony Eden
 */
public class Auth implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2254618470673679663L;
	private String token;
    private Permission permission;
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
