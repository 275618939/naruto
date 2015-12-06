package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseObjectListAdapter;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.BaseBean;
import com.movie.client.bean.User;
import com.movie.state.SexState;
import com.movie.ui.UserDetailActivity;

public class PartNarutoAdapter extends BaseObjectListAdapter {

	public PartNarutoAdapter(Context context,List<? extends BaseBean> datas) {
		super(context, datas);
	}
	
	public PartNarutoAdapter(Context context, Handler mHandler,List<? extends BaseBean> datas) {
		super(context, mHandler, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = mInflater.inflate(R.layout.part_naruto_item, null);
			mHolder = new ViewHolder();
			mHolder.userItemView = (LinearLayout) view.findViewById(R.id.part_user_item_view);
			mHolder.userIcon = (ImageView) view.findViewById(R.id.part_user_icon);
			mHolder.missUserName = (TextView) view.findViewById(R.id.user_name);
			mHolder.missUserSex = (TextView) view.findViewById(R.id.user_sex);
			mHolder.userConstell = (TextView) view.findViewById(R.id.user_constell);
			mHolder.missUserCharm = (TextView) view.findViewById(R.id.user_charm);
			mHolder.missUserLove = (TextView) view.findViewById(R.id.user_love);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		// 获取position对应的数据
		final User user = (User)getItem(position);
		imageLoader.displayImage(user.getPortrait(), mHolder.userIcon,NarutoApplication.imageOptions);
		mHolder.missUserName.setText(user.getNickname());
		mHolder.missUserSex.setText(SexState.getState(user.getSex()).getMessage());
		mHolder.missUserCharm.setText(user.getCharm().toString());
		mHolder.userConstell.setText(user.getConstell());
		mHolder.missUserLove.setText(String.format(mContext.getResources().getString(R.string.user_love_count), user.getLove().toString()));
		mHolder.userItemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, UserDetailActivity.class);
				intent.putExtra("user", user);
				mContext.startActivity(intent);
				
			}
		});

		return view;
	}

	class ViewHolder {

		LinearLayout userItemView;
		// 用户LOGO
		ImageView userIcon;
		// 用户
		TextView missUserName;
		// 用户性别
		TextView missUserSex;
		// 用户星座
		TextView userConstell;
		// 用户颜值
		TextView missUserCharm;
		// 用户心动数
		TextView missUserLove;


	}

}
