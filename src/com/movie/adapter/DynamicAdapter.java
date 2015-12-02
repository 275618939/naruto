package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseObjectListAdapter;
import com.movie.client.bean.BaseBean;
import com.movie.client.bean.Feed;
import com.movie.view.HandyTextView;

public class DynamicAdapter extends BaseObjectListAdapter implements
		OnItemClickListener {

	public DynamicAdapter(Context context, Handler mHandler,
			List<? extends BaseBean> datas) {
		super(context, mHandler, datas);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_dynamic, null);
			holder = new ViewHolder();
			holder.root = (RelativeLayout) convertView.findViewById(R.id.feed_item_layout_root);
			holder.contentImages=(LinearLayout) convertView.findViewById(R.id.feed_item_content_images);
			holder.avatar = (ImageView) convertView.findViewById(R.id.feed_item_iv_avatar);
			holder.time = (HandyTextView) convertView.findViewById(R.id.feed_item_htv_time);
			holder.name = (HandyTextView) convertView.findViewById(R.id.feed_item_htv_name);
			holder.content = (TextView) convertView.findViewById(R.id.feed_item_etv_content);
			holder.contentImage = (ImageView) convertView.findViewById(R.id.feed_item_iv_content);
			holder.more = (ImageButton) convertView.findViewById(R.id.feed_item_ib_more);
			holder.comment = (LinearLayout) convertView.findViewById(R.id.feed_item_layout_comment);
			holder.commentCount = (HandyTextView) convertView.findViewById(R.id.feed_item_htv_commentcount);
			holder.site = (HandyTextView) convertView.findViewById(R.id.feed_item_htv_site);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Feed feed = (Feed) getItem(position);
		imageLoader.displayImage(feed.getPortrait(), holder.avatar);
		holder.name.setText(feed.getName());
		holder.time.setText(feed.getTime());
		holder.content.setText(feed.getContent());
		if (feed.getContentImage() == null) {
			holder.contentImages.setVisibility(View.GONE);
		} else {
			//holder.contentImages.removeView(view)
			for(String image:feed.getContentImage()){
				ImageView imageView=new ImageView(mContext);
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(R.dimen.dynamic_image_content_width, R.dimen.dynamic_image_content_height);
				params.setMargins(0, 3, 3, 0);
				imageView.setLayoutParams(params);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				imageLoader.displayImage(image, imageView);
				holder.contentImages.addView(imageView);
				//imageView=null;
				//params=null;
			}
		}
		holder.site.setText(feed.getSite());
		holder.commentCount.setText(feed.getCommentCount() + "");

		return convertView;
	}

	class ViewHolder {
		RelativeLayout root;
		LinearLayout contentImages;
		ImageView avatar;
		HandyTextView time;
		HandyTextView name;
		TextView content;
		ImageView contentImage;
		ImageButton more;
		LinearLayout comment;
		HandyTextView commentCount;
		HandyTextView site;

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		switch (view.getId()) {
		

		default:
			break;
		}
		
	}


}
