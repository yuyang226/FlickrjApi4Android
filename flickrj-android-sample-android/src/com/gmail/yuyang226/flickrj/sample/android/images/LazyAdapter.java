/**
 * 
 */
package com.gmail.yuyang226.flickrj.sample.android.images;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotoList;
import com.gmail.yuyang226.flickrj.sample.android.R;
import com.gmail.yuyang226.flickrj.sample.android.images.ImageUtils.DownloadedDrawable;
import com.gmail.yuyang226.flickrj.sample.android.tasks.ImageDownloadTask;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private PhotoList photos;
    private static LayoutInflater inflater=null;
    
    public LazyAdapter(Activity a, PhotoList d) {
        activity = a;
        photos = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return photos.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView == null)
            vi = inflater.inflate(R.layout.row, null);

        TextView text=(TextView)vi.findViewById(R.id.imageTitle);;
        ImageView image=(ImageView)vi.findViewById(R.id.imageIcon);
        Photo photo = photos.get(position);
        text.setText(photo.getTitle());
        if (image != null) {
        	ImageDownloadTask task = new ImageDownloadTask(image);
            Drawable drawable = new DownloadedDrawable(task);
            image.setImageDrawable(drawable);
            task.execute(photo.getSmallSquareUrl());
        }
        
        ImageView viewIcon = (ImageView)vi.findViewById(R.id.viewIcon);
        if (photo.getViews() >= 0) {
        	viewIcon.setImageResource(R.drawable.views);
        	TextView viewsText = (TextView)vi.findViewById(R.id.viewsText);
        	viewsText.setText(String.valueOf(photo.getViews()));
        } else {
        	viewIcon.setImageBitmap(null);
        }
        
        return vi;
    }
}
