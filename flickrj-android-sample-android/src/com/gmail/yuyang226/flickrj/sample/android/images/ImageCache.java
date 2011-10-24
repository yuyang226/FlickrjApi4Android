package com.gmail.yuyang226.flickrj.sample.android.images;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;

public final class ImageCache {

	private static final HashMap<String, SoftReference<Bitmap>> cache = new HashMap<String, SoftReference<Bitmap>>();
	
    /**
	 * 
	 */
	private ImageCache() {
		super();
	}

	public static Bitmap getFromCache(String id){
        if(!cache.containsKey(id))
            return null;
        SoftReference<Bitmap> ref=cache.get(id);
        return ref.get();
    }
    
    public static void saveToCache(String id, Bitmap bitmap){
        cache.put(id, new SoftReference<Bitmap>(bitmap));
    }

    public static void clear() {
        cache.clear();
    }

}
