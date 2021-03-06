package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.app.Constant.Page;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.BaseBean;
import com.movie.pop.PhotoDeletePopupWindow;
import com.movie.pop.PhotoDeletePopupWindow.onPhotoDeleteItemClickListner;
import com.movie.pop.UserPhotoPopupWindow;
import com.movie.ui.ImageBrowserActivity;
import com.movie.ui.UserActivity;
import com.movie.util.Bimp;
import com.movie.util.ImageItem;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserPhotoGridAdapter extends BaseAdapter implements onPhotoDeleteItemClickListner  {
	private LayoutInflater mInflater;
	private UserPhotoPopupWindow userPhotoPopupWindow;
	private PhotoDeletePopupWindow deletePopupWindow;
	private int selectPosition;
	private int mWidthAndHeight;
	private ImageLoader imageLoader=ImageLoader.getInstance();
	private Handler mHandler;
	private Context mContext;
	public UserPhotoGridAdapter(Context context, Handler mHandler,List<? extends BaseBean> datas) {
		this.mHandler=mHandler;
		this.mContext=context;
		mInflater = LayoutInflater.from(context);
		mWidthAndHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics());
		userPhotoPopupWindow = new UserPhotoPopupWindow(context,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		deletePopupWindow = new PhotoDeletePopupWindow(context, mWidthAndHeight*2, mWidthAndHeight);
		deletePopupWindow.setOnPhotoItemClickListner(this);
	}
	public int getCount() {
		if (Bimp.tempSelectBitmap.size()== Page.MAX_SHOW_USER_PHOTO) {
			return Page.MAX_SHOW_USER_PHOTO;
		}
		return (Bimp.tempSelectBitmap.size() + 1);
	}
	public Object getItem(int arg0) {
		return null;
	}
	public long getItemId(int arg0) {
		return 0;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_user_photo_grid,parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.item_grid_image);		
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position ==Bimp.tempSelectBitmap.size()) {
			holder.image.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_addpic_unfocused));
			if (position == Page.MAX_SHOW_USER_PHOTO) {
				holder.image.setVisibility(View.GONE);
				
			}else{
				holder.image.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						userPhotoPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
					}
				});
			}
		} else {
			ImageItem imageItem=(ImageItem)Bimp.tempSelectBitmap.get(position);
		
			String imagePath=null;
			if(imageItem.getImageId()!=null&&!imageItem.getImageId().isEmpty()){	
				imagePath=imageItem.getImagePath();
			}else{
				imagePath="file://"+imageItem.getImagePath();
			}
			imageLoader.displayImage(imagePath, holder.image,NarutoApplication.imageOptions);
			holder.image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, ImageBrowserActivity.class);
					mContext.startActivity(intent);
					((BaseActivity) mContext).overridePendingTransition(R.anim.zoom_enter,0);
				}
			});
			holder.image.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					selectPosition=position;
					int[] location = new int[2];
					v.getLocationOnScreen(location);
					deletePopupWindow.showAtLocation(v, Gravity.NO_GRAVITY,location[0], location[1]+mWidthAndHeight+10);
					return false;
				}
			});
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView image;	
	}
	public UserPhotoPopupWindow getUserPhotoPopupWindow() {
		return userPhotoPopupWindow;
	}
	@Override
	public void onDelete(View v) {
		Message message=new Message();
		message.what=UserActivity.DELETE_IMAGE;
		Bundle bundle=new Bundle();
		bundle.putInt("position", selectPosition);
		message.setData(bundle);
        mHandler.sendMessage(message);		
	}
	


	

	
}
