package com.movie.adapter;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.movie.ui.ImageBrowserActivity;
import com.movie.util.ImageItem;
import com.movie.view.photo.PhotoView;

public class ImageBrowserAdapter extends PagerAdapter {

	private List<ImageItem> mPhotos = new ArrayList<ImageItem>();
	private String mType;
	public ImageBrowserAdapter(List<ImageItem> photos, String type) {
		if (photos != null) {
			mPhotos = photos;
		}
		mType = type;
	}

	@Override
	public int getCount() {
		if (mPhotos.size() > 1) {
			return Integer.MAX_VALUE;
		}
		return mPhotos.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		
		PhotoView photoView = new PhotoView(container.getContext());
		Bitmap bitmap=null;
		if (ImageBrowserActivity.TYPE_ALBUM.equals(mType)) {
			bitmap =mPhotos.get(position% mPhotos.size()).getBitmap();
		} else if (ImageBrowserActivity.TYPE_PHOTO.equals(mType)) {
			bitmap = mPhotos.get(position).getBitmap();
		}
		photoView.setImageBitmap(bitmap);
		container.addView(photoView, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		return photoView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
