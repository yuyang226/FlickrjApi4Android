package com.gmail.yuyang226.flickrj.sample.android.images;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.gmail.yuyang226.flickrj.sample.android.tasks.ImageDownloadTask;


public final class ImageUtils {
	private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

	private static Map<String, SoftReference<Bitmap>> imageCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			20);

	/**
	 * This method must be called in a thread other than UI.
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap downloadImage(String url) {
		// final int IO_BUFFER_SIZE = 4 * 1024;

		// AndroidHttpClient is not allowed to be used from the main thread
		final HttpClient client = AndroidHttpClient.newInstance("Android"); //$NON-NLS-1$
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode //$NON-NLS-1$//$NON-NLS-2$
						+ " while retrieving bitmap from " + url); //$NON-NLS-1$
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					// return BitmapFactory.decodeStream(inputStream);
					// Bug on slow connections, fixed in future release.
					return BitmapFactory.decodeStream(new FlushedInputStream(
							inputStream));
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (IOException e) {
			getRequest.abort();
			logger.warn("I/O error while retrieving bitmap from " + url, e); //$NON-NLS-1$
		} catch (IllegalStateException e) {
			getRequest.abort();
			logger.warn("Incorrect URL:" + url, e); //$NON-NLS-1$
		} catch (Exception e) {
			getRequest.abort();
			logger.warn("Error while retrieving bitmap from " + url, e); //$NON-NLS-1$
		} finally {
			if ((client instanceof AndroidHttpClient)) {
				((AndroidHttpClient) client).close();
			}
		}
		return null;
	}

	public static class DownloadedDrawable extends ColorDrawable {

		private WeakReference<ImageDownloadTask> taskRef;

		public DownloadedDrawable(ImageDownloadTask task) {
			taskRef = new WeakReference<ImageDownloadTask>(task);
		}

		public ImageDownloadTask getBitmapDownloaderTask() {
			if (taskRef != null) {
				return taskRef.get();
			} else {
				return null;
			}
		}
	}

	public static void putToCache(String url, Bitmap bitmap) {
		imageCache.put(url, new SoftReference<Bitmap>(bitmap));
	}

	public static Bitmap getFromCache(String url) {
		if (imageCache.containsKey(url)) {
			return imageCache.get(url).get();
		} else {
			return null;
		}
	}

}
