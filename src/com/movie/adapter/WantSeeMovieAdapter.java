package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseObjectListAdapter;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.BaseBean;
import com.movie.client.bean.User;
import com.movie.ui.UserDetailActivity;
import com.movie.util.StringUtil;
import com.movie.view.RoundImageView;

public class WantSeeMovieAdapter extends BaseObjectListAdapter {
	

	public WantSeeMovieAdapter(Context context,List<? extends BaseBean> datas) {
		super(context, datas);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = mInflater.inflate(R.layout.want_see_user_item, null);
			mHolder = new ViewHolder();
			mHolder.wantSeeView=(LinearLayout)view.findViewById(R.id.want_see_view);
			mHolder.userImage= (RoundImageView)view.findViewById(R.id.user_image);
			mHolder.userNick = (TextView)view.findViewById(R.id.user_nick);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		//获取position对应的数据
		final User user = (User)getItem(position);
		imageLoader.displayImage(user.getPortrait(), mHolder.userImage,NarutoApplication.imageOptions);
		mHolder.userNick.setText(StringUtil.getShortStr(user.getNickname(), 3));
		mHolder.userImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext,UserDetailActivity.class);
				intent.putExtra("memberId", user.getMemberId());
				mContext.startActivity(intent);
			}
		});
		return view;
	}
	class ViewHolder {
		
		LinearLayout wantSeeView;
		RoundImageView userImage;
		TextView userNick;
		
		
	}
	
}
