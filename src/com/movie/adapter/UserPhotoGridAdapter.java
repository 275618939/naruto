package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.movie.R;
import com.movie.app.BaseObjectListAdapter;
import com.movie.app.Constant.Page;
import com.movie.client.bean.BaseBean;
import com.movie.pop.PhotoDeletePopupWindow;
import com.movie.pop.PhotoDeletePopupWindow.onPhotoDeleteItemClickListner;
import com.movie.pop.UserPhotoUploadPopupWindow;
import com.movie.ui.ImageBrowserActivity;
import com.movie.ui.MainActivity;
import com.movie.util.Bimp;

public class UserPhotoGridAdapter extends BaseObjectListAdapter implements onPhotoDeleteItemClickListner  {
	private UserPhotoUploadPopupWindow userPhotoPopupWindow;
	private PhotoDeletePopupWindow deletePopupWindow;
	private int mWidthAndHeight;
	public UserPhotoGridAdapter(Context context, Handler mHandler,List<? extends BaseBean> datas) {
		super(context, mHandler, datas);
		mWidthAndHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 120, context.getResources()
						.getDisplayMetrics());
		userPhotoPopupWindow = new UserPhotoUploadPopupWindow(context,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		deletePopupWindow = new PhotoDeletePopupWindow(context, mWidthAndHeight, mWidthAndHeight);
	}

	public int getCount() {
		if (getDatas().size()== Page.MAX_SHOW_USER_PHOTO) {
			return Page.MAX_SHOW_USER_PHOTO;
		}
		return (getDatas().size() + 1);
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
		if (position == Bimp.tempSelectBitmap.size()) {
			holder.image.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_addpic_unfocused));
			if (position == Page.MAX_SHOW_USER_PHOTO) {
				holder.image.setVisibility(View.GONE);
			}
			holder.image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					userPhotoPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
				}
			});
		} else {
			holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
			holder.image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, ImageBrowserActivity.class);
					mContext.startActivity(intent);
					((MainActivity) mContext).overridePendingTransition(R.anim.zoom_enter,0);
				}
			});
			holder.image.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					
					int[] location = new int[2];
					v.getLocationOnScreen(location);
					deletePopupWindow.showAtLocation(v, Gravity.NO_GRAVITY,location[0], location[1] - mWidthAndHeight + 30);
					return false;
				}
			});
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView image;	
	}
	

	public UserPhotoUploadPopupWindow getUserPhotoPopupWindow() {
		return userPhotoPopupWindow;
	}

	@Override
	public void onDelete(View v) {
	
		showCustomToast("删除成功！");
		
	}
	


	

	
}
