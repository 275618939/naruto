package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.SexState;
import com.movie.client.bean.User;
import com.movie.ui.UserDetailActivity;
import com.movie.util.ImageLoaderCache;

public class PartNarutoAdapter extends BaseAdapter {

	List<User> userList;
	Context context;
	LayoutInflater inflater;
	ImageLoaderCache imageLoaderCache;
	Handler handler;

	public PartNarutoAdapter(Context context, List<User> users) {
		this.context = context;
		this.userList = users;
		inflater = LayoutInflater.from(context);
		imageLoaderCache=ImageLoaderCache.getInstance(context);

	}

	public PartNarutoAdapter(Context context, Handler handler,List<User> users) {
		this.context = context;
		this.userList = users;
		inflater = LayoutInflater.from(context);
		imageLoaderCache = new ImageLoaderCache(context);
		this.handler = handler;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userList == null ? 0 : userList.size();
	}

	@Override
	public User getItem(int position) {
		// TODO Auto-generated method stub
		if (userList != null && userList.size() != 0) {
			return userList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.part_naruto_item, null);
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
		User user = getItem(position);
		imageLoaderCache.DisplayImage(user.getPortrait(), mHolder.userIcon);
		mHolder.missUserName.setText(user.getNickname());
		mHolder.missUserSex.setText(SexState.getState(user.getSex()).getMessage());
		mHolder.missUserCharm.setText(user.getCharm().toString());
		mHolder.userConstell.setText(user.getConstell());
		mHolder.missUserLove.setText(String.format(context.getResources().getString(R.string.user_love_count), user.getLove().toString()));
		mHolder.userItemView.setOnClickListener(new UserSelectAction(position));

		return view;
	}

	static class ViewHolder {

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

	public void updateData(List<User> userList) {
		this.userList = userList;
		this.notifyDataSetChanged();

	}

	protected class UserSelectAction implements OnClickListener {

		int position;

		public UserSelectAction(int position) {
			this.position = position;
		}

		@Override
		public void onClick(final View v) {
			final User user = userList.get(position);
			switch (v.getId()) {
			case R.id.part_user_item_view:
				Intent intent = new Intent(context, UserDetailActivity.class);
				intent.putExtra("user", user);
				context.startActivity(intent);
				break;

			default:
				break;
			}
		}

	}

}
