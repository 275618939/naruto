package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
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
import com.movie.pop.OtherFeedListPopupWindow;
import com.movie.pop.OtherFeedListPopupWindow.onOtherFeedListPopupItemClickListner;
import com.movie.view.HandyTextView;

public class DynamicAdapter extends BaseObjectListAdapter implements
		OnItemClickListener , onOtherFeedListPopupItemClickListner{
	
	private OtherFeedListPopupWindow mPopupWindow;
	private int mWidthAndHeight;
	private int mPosition;
	

	public DynamicAdapter(Context context, Handler mHandler,
			List<? extends BaseBean> datas) {
		super(context, mHandler, datas);
		mWidthAndHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, context.getResources().getDisplayMetrics());
		mPopupWindow = new OtherFeedListPopupWindow(context, mWidthAndHeight,mWidthAndHeight);
		mPopupWindow.setOnOtherFeedListPopupItemClickListner(this);

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
			//holder.contentImage = (ImageView) convertView.findViewById(R.id.feed_item_iv_content);
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
			holder.contentImages.removeAllViews();
			LinearLayout dynamicLayout=null;
			ImageView dynamicImageView=null;
			for(String image:feed.getContentImage()){
				dynamicLayout=(LinearLayout)mInflater.inflate(R.layout.dynamic_content_image, null);
				dynamicImageView= (ImageView)dynamicLayout.getChildAt(0);
				imageLoader.displayImage(image, dynamicImageView);
				holder.contentImages.addView(dynamicLayout);
			}
			dynamicLayout=null;
			dynamicImageView=null;
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
		//ImageView contentImage;
		ImageButton more;
		LinearLayout comment;
		HandyTextView commentCount;
		HandyTextView site;

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		switch (view.getId()) {
		    case R.id.feed_item_ib_more:
		    	mPosition = position;
				int[] location = new int[2];
				view.getLocationOnScreen(location);
				mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY,location[0], location[1] - mWidthAndHeight + 30);
		    	break;

		default:
			break;
		}
		
	}

	@Override
	public void onCopy(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReport(View v) {
		// TODO Auto-generated method stub
		
	}


}
