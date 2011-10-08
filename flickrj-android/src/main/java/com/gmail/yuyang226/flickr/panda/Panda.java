package com.gmail.yuyang226.flickr.panda;

/**
 * Holds the name of a panda.
 *
 * @author mago
 * @version $Id: Panda.java,v 1.3 2009/07/12 22:43:07 x-mago Exp $
 * @see com.gmail.yuyang226.flickr.panda.PandaInterface#getPhotos(Panda, java.util.Set, int, int)
 */
public class Panda {
	public static final long serialVersionUID = 12L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
