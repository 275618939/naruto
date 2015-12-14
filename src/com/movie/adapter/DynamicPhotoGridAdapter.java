package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseObjectListAdapter;
import com.movie.client.bean.BaseBean;
import com.movie.ui.DynamicCreateActivity;
import com.movie.ui.ImageBrowserActivity;
import com.movie.util.ImageItem;

public class DynamicPhotoGridAdapter extends BaseObjectListAdapter {
	
	
	public DynamicPhotoGridAdapter(Context context, Handler mHandler,List<? extends BaseBean> datas) {
		super(context, mHandler, datas);
	}
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_published_grida,parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
			holder.deleteImage=(TextView)convertView.findViewById(R.id.deleteImage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		ImageItem imageItem= (ImageItem)getItem(position);	
		if(null!=imageItem){
			holder.image.setImageBitmap(imageItem.getBitmap());
			imageItem=null;
		} 
		holder.deleteImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message message = new Message();
				message.what = DynamicCreateActivity.DELETE_IMAGE;
				Bundle bundle=new Bundle();
				bundle.putInt("position", position);
				message.setData(bundle);
				mHandler.sendMessage(message);
			}
		});
		holder.image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ImageBrowserActivity.class);
				mContext.startActivity(intent);
				((DynamicCreateActivity) mContext).overridePendingTransition(R.anim.zoom_enter,0);
			}
		});
	
		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
		public TextView  deleteImage;
	}

	
		
		
	

	
	


	

	
}
