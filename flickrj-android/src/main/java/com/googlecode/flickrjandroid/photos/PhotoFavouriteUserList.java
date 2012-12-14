/**
 * 
 */
package com.googlecode.flickrjandroid.photos;

import com.googlecode.flickrjandroid.SearchResultList;
import com.googlecode.flickrjandroid.people.User;

/**
 * Represents the photo favourites result
 * 
 * @author yayu
 */
public class PhotoFavouriteUserList extends SearchResultList<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8033993738089756037L;
	private String secret;
	private int farm = -1;
	private String server;
	

	/**
	 * 
	 */
	public PhotoFavouriteUserList() {
		super();
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

	/**
	 * @return the farm
	 */
	public int getFarm() {
		return farm;
	}

	/**
	 * @param farm the farm to set
	 */
	public void setFarm(int farm) {
		this.farm = farm;
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
	
}
