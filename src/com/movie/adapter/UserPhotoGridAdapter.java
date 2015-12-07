package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.movie.R;
import com.movie.app.BaseObjectListAdapter;
import com.movie.app.Constant.Page;
import com.movie.client.bean.BaseBean;
import com.movie.pop.UserPhotoPopupWindow;
import com.movie.util.Bimp;

public class UserPhotoGridAdapter extends BaseObjectListAdapter implements OnItemClickListener{
	private int selectedPosition = -1;
	private UserPhotoPopupWindow userPhotoPopupWindow;
	public UserPhotoGridAdapter(Context context, Handler mHandler,List<? extends BaseBean> datas) {
		super(context, mHandler, datas);
		userPhotoPopupWindow = new UserPhotoPopupWindow(context,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
	}

	public int getCount() {
		if (Bimp.tempSelectBitmap.size() == Page.MAX_SHOW_USER_PHOTO) {
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

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_published_grida,parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == Bimp.tempSelectBitmap.size()) {
			holder.image.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_addpic_unfocused));
			if (position == Page.MAX_SHOW_USER_PHOTO) {
				holder.image.setVisibility(View.GONE);
			}
		} else {
			holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		if (position == Bimp.tempSelectBitmap.size()) {
			userPhotoPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		} else {
			/*Intent intent = new Intent(this.context,GalleryActivity.class);
			intent.putExtra("position", "1");
			intent.putExtra("ID", position);
			startActivity(intent);*/
		}
		
	}

	public UserPhotoPopupWindow getUserPhotoPopupWindow() {
		return userPhotoPopupWindow;
	}
	


	

	
}
