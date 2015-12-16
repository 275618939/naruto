package com.movie.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.ViewGroup;

import com.movie.app.NarutoApplication;
import com.movie.ui.ImageBrowserActivity;
import com.movie.util.ImageItem;
import com.movie.view.photo.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageBrowserAdapter extends PagerAdapter {

	private List<ImageItem> mPhotos = new ArrayList<ImageItem>();
	private ImageLoader imageLoader=ImageLoader.getInstance();
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
		ImageItem imageItem=null;
		if (ImageBrowserActivity.TYPE_ALBUM.equals(mType)) {
			imageItem =mPhotos.get(position% mPhotos.size());
		} else if (ImageBrowserActivity.TYPE_PHOTO.equals(mType)) {
			imageItem = mPhotos.get(position);
		}
		String imagePath=null;
		if(imageItem.getImageId()!=null&&!imageItem.getImageId().isEmpty()){	
			imagePath=imageItem.getImagePath();
		}else{
			imagePath="file://"+imageItem.getImagePath();
		}
		imageLoader.displayImage(imagePath,photoView,NarutoApplication.getApp().imageOptions);
		container.addView(photoView, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		return photoView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
