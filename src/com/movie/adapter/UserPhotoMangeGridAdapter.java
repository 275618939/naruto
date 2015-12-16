package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.app.BaseObjectListAdapter;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.BaseBean;
import com.movie.pop.PhotoDeletePopupWindow;
import com.movie.pop.PhotoDeletePopupWindow.onPhotoDeleteItemClickListner;
import com.movie.ui.ImageBrowserActivity;
import com.movie.ui.UserActivity;
import com.movie.util.ImageItem;

public class UserPhotoMangeGridAdapter extends BaseObjectListAdapter implements onPhotoDeleteItemClickListner {
	
	private PhotoDeletePopupWindow deletePopupWindow;
	private int selectPosition;
	private int mWidthAndHeight;
	private boolean unEnableDelete;
	public UserPhotoMangeGridAdapter(Context context, Handler mHandler,List<? extends BaseBean> datas) {
		super(context, mHandler, datas);
		mWidthAndHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics());
		deletePopupWindow = new PhotoDeletePopupWindow(context, mWidthAndHeight*2, mWidthAndHeight);
		deletePopupWindow.setOnPhotoItemClickListner(this);
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
		
		ImageItem imageItem= (ImageItem)getItem(position);	
		String imagePath=null;
		if(imageItem.getImageId()!=null&&!imageItem.getImageId().isEmpty()){	
			imagePath=imageItem.getImagePath();
		}else{
			imagePath="file://"+imageItem.getImagePath();
		}
		imageLoader.displayImage(imagePath, holder.image,NarutoApplication.imageOptions);
//		if(imageItem.getImageId()!=null&&!imageItem.getImageId().isEmpty()){	
//			imageLoader.displayImage(imageItem.getImagePath(), holder.image,NarutoApplication.imageOptions);
//		}else{
//			holder.image.setImageBitmap(imageItem.getBitmap());
//		}
		holder.image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ImageBrowserActivity.class);
				mContext.startActivity(intent);			
				((BaseActivity) mContext).overridePendingTransition(R.anim.zoom_enter,0);
			}
		});
		if(!unEnableDelete){
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
	@Override
	public void onDelete(View v) {
		Message message=new Message();
		message.what=UserActivity.DELETE_IMAGE;
		Bundle bundle=new Bundle();
		bundle.putInt("position", selectPosition);
		message.setData(bundle);
        mHandler.sendMessage(message);		
	}
	public void setUnEnableDelete(boolean unEnableDelete) {
		this.unEnableDelete = unEnableDelete;
	}
	

	
		
		
	

	
	


	

	
}
