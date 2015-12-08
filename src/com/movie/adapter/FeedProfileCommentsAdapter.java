package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.movie.R;
import com.movie.app.BaseObjectListAdapter;
import com.movie.client.bean.BaseBean;
import com.movie.client.bean.FeedComment;
import com.movie.view.EmoticonsTextView;
import com.movie.view.HandyTextView;



public class FeedProfileCommentsAdapter extends BaseObjectListAdapter {

	public FeedProfileCommentsAdapter(Context context, Handler mHandler, List<? extends BaseBean> datas) {
		super(context, mHandler, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_feedcomment, null);
			holder = new ViewHolder();
			holder.mIvAvatar = (ImageView) convertView.findViewById(R.id.feedcomment_item_iv_avatar);
			holder.mEtvName = (EmoticonsTextView) convertView
					.findViewById(R.id.feedcomment_item_etv_name);
			holder.mEtvContent = (EmoticonsTextView) convertView
					.findViewById(R.id.feedcomment_item_etv_content);
			holder.mHtvTime = (HandyTextView) convertView
					.findViewById(R.id.feedcomment_item_htv_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FeedComment comment = (FeedComment) getItem(position);
		//holder.mIvAvatar.setImageBitmap(mApplication.getAvatar(comment.getAvatar()));
		holder.mEtvName.setText(comment.getName());
		holder.mEtvContent.setText(comment.getContent());
		holder.mHtvTime.setText(comment.getTime());
		return convertView;
	}

	class ViewHolder {
		ImageView mIvAvatar;
		EmoticonsTextView mEtvName;
		EmoticonsTextView mEtvContent;
		HandyTextView mHtvTime;
	}
}
