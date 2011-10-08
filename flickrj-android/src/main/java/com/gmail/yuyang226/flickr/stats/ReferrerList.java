/**
 * 
 */
package com.gmail.yuyang226.flickr.stats;

import com.gmail.yuyang226.flickr.SearchResultList;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class ReferrerList extends SearchResultList<Referrer> {
	private String name;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ReferrerList() {
		super();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	

}
