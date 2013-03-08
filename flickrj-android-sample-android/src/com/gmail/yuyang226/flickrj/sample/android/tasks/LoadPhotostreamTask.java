/**
 * 
 */
package com.googlecode.flickrjandroidj.sample.android.tasks;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroidj.sample.android.FlickrHelper;
import com.googlecode.flickrjandroidj.sample.android.images.LazyAdapter;

public class LoadPhotostreamTask extends AsyncTask<OAuth, Void, PhotoList> {

	/**
	 * 
	 */
	private ListView listView;
	private Activity activity;

	/**
	 * @param flickrjAndroidSampleActivity
	 */
	public LoadPhotostreamTask(Activity activity,
			ListView listView) {
		this.activity = activity;
		this.listView = listView;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected PhotoList doInBackground(OAuth... arg0) {
		OAuthToken token = arg0[0].getToken();
		Flickr f = FlickrHelper.getInstance().getFlickrAuthed(token.getOauthToken(), 
				token.getOauthTokenSecret());
		Set<String> extras = new HashSet<String>();
		extras.add("url_sq"); //$NON-NLS-1$
		extras.add("url_l"); //$NON-NLS-1$
		extras.add("views"); //$NON-NLS-1$
		User user = arg0[0].getUser();
		try {
			return f.getPeopleInterface().getPhotos(user.getId(), extras, 20, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(PhotoList result) {
		if (result != null) {
			LazyAdapter adapter = 
					new LazyAdapter(this.activity, result);
			this.listView.setAdapter(adapter);
		}
	}
	
}