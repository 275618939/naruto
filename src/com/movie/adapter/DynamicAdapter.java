package com.movie.adapter;

import java.util.List;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseObjectListAdapter;
import com.movie.app.Constant;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.BaseBean;
import com.movie.client.bean.Feed;
import com.movie.client.bean.User;
import com.movie.pop.OtherFeedListPopupWindow;
import com.movie.pop.OtherFeedListPopupWindow.onOtherFeedListPopupItemClickListner;
import com.movie.pop.SimpleListDialog;
import com.movie.pop.SimpleListDialog.onSimpleListItemClickListener;
import com.movie.ui.FeedProfileActivity;
import com.movie.view.HandyTextView;

public class DynamicAdapter extends BaseObjectListAdapter implements onSimpleListItemClickListener, onOtherFeedListPopupItemClickListner{
	
	private OtherFeedListPopupWindow mPopupWindow;
	private int mWidthAndHeight;
	private int mPosition;
	private SimpleListDialog mDialog;
	private User user;
	

	public DynamicAdapter(Context context, Handler mHandler,
			List<? extends BaseBean> datas) {
		super(context, mHandler, datas);
		mWidthAndHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, context.getResources().getDisplayMetrics());
		mPopupWindow = new OtherFeedListPopupWindow(context, mWidthAndHeight,mWidthAndHeight);
		mPopupWindow.setOnOtherFeedListPopupItemClickListner(this);

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_dynamic, null);
			holder = new ViewHolder();
			holder.root = (RelativeLayout) convertView.findViewById(R.id.feed_item_layout_root);
			holder.imagesView = (HorizontalScrollView)convertView.findViewById(R.id.feed_item_image_views);
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
		final Feed feed = (Feed) getItem(position);
		if(user!=null){
			imageLoader.displayImage(user.getPortrait(), holder.avatar,NarutoApplication.imageOptions);
			holder.name.setText(user.getNickname());
		} 
		if(feed.getPortrait()!=null&&!feed.getPortrait().isEmpty()){
			imageLoader.displayImage(feed.getPortrait(), holder.avatar,NarutoApplication.imageOptions);
		}
		if(feed.getPortrait()!=null&&!feed.getPortrait().isEmpty()){
			holder.name.setText(feed.getName());
		}
		holder.time.setText(feed.getTime());
		holder.content.setText(feed.getContent());
		if (feed.getContentImage() == null||feed.getContentImage().size()<=0) {
			holder.imagesView.setVisibility(View.GONE);
		} else {
			holder.imagesView.setVisibility(View.VISIBLE);
			holder.contentImages.removeAllViews();
			LinearLayout dynamicLayout=null;
			ImageView dynamicImageView=null;
			for(String image:feed.getContentImage()){
				dynamicLayout=(LinearLayout)mInflater.inflate(R.layout.dynamic_content_image, null);
				dynamicImageView= (ImageView)dynamicLayout.getChildAt(0);
				imageLoader.displayImage(Constant.SERVER_ADRESS+image, dynamicImageView,NarutoApplication.imageOptions);
				holder.contentImages.addView(dynamicLayout);
			}
			dynamicLayout=null;
			dynamicImageView=null;
		}
		holder.site.setText(feed.getSite()+"km");
		holder.commentCount.setText(feed.getCommentCount() + "");
		holder.more.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				mPosition = position;
				int[] location = new int[2];
				v.getLocationOnScreen(location);
				mPopupWindow.showAtLocation(v, Gravity.NO_GRAVITY,location[0], location[1] - mWidthAndHeight + 30);
				
			}
		});
		holder.root.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPosition = position;
				Intent intent = new Intent(mContext, FeedProfileActivity.class);
				intent.putExtra("entity_feed", feed);
				intent.putExtra("user", user);
				mContext.startActivity(intent);
			}
		});
		holder.contentImages.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPosition = position;
				Intent intent = new Intent(mContext, FeedProfileActivity.class);
				intent.putExtra("entity_feed", feed);
				intent.putExtra("user", user);
				mContext.startActivity(intent);
			}
		});

		return convertView;
	}

	class ViewHolder {
		HorizontalScrollView imagesView;
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
	public void onCopy(View v) {
		Feed feed = (Feed) getItem(mPosition);
		String text = feed.getContent();
		ClipboardManager  m = (ClipboardManager ) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
		m.setPrimaryClip(ClipData.newPlainText(null, text));
		showCustomToast("已复制到剪切板");
	}

	@Override
	public void onReport(View v) {
		String[] codes = mContext.getResources().getStringArray(R.array.reportfeed_items);
		mDialog = new SimpleListDialog(mContext);
		mDialog.setTitle("举报留言");
		mDialog.setTitleLineVisibility(View.GONE);
		mDialog.setAdapter(new SimpleListDialogAdapter(mContext, codes));
		mDialog.setOnSimpleListItemClickListener(this);
		mDialog.show();
		
	}

	@Override
	public void onItemClick(int position) {

		showProgressDialog("提示", "正在提交,请稍后...");
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				hideProgressDialog();
				showCustomToast("举报的信息已提交");
			}
		}, 1500);
		
	}

	public void setUser(User user) {
		this.user = user;
	}
	


}
