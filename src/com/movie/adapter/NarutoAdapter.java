package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
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
import com.movie.client.bean.Miss;
import com.movie.client.bean.User;
import com.movie.ui.UserDetailActivity;
import com.movie.util.ImageLoaderCache;
import com.movie.view.MessageDialog;

public class NarutoAdapter extends BaseAdapter {

	List<User> userList;
	Context context;
	LayoutInflater inflater;
	ImageLoaderCache imageLoaderCache;
	Handler handler;

	public NarutoAdapter(Context context, List<User> users) {
		this.context = context;
		this.userList = users;
		inflater = LayoutInflater.from(context);
		imageLoaderCache = new ImageLoaderCache(context);

	}

	public NarutoAdapter(Context context, Handler handler, List<User> users) {
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
			view = inflater.inflate(R.layout.naruto_item, null);
			mHolder = new ViewHolder();
			mHolder.userItemView = (LinearLayout) view.findViewById(R.id.user_item_view);
			mHolder.userIcon = (ImageView) view.findViewById(R.id.user_icon);
			mHolder.userName = (TextView) view.findViewById(R.id.user_name);
			mHolder.userSex = (TextView) view.findViewById(R.id.user_sex);
			mHolder.userConstell = (TextView) view.findViewById(R.id.user_constell);
			mHolder.userCharm = (TextView) view.findViewById(R.id.user_charm);
			mHolder.userLove = (TextView) view.findViewById(R.id.user_love);
			mHolder.userMovieLove = (TextView) view.findViewById(R.id.user_movie_love);
			mHolder.userIngMiss = (TextView) view.findViewById(R.id.user_ing_miss);

			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		// 获取position对应的数据
		User user = getItem(position);

		imageLoaderCache.DisplayImage(user.getPortrait(), mHolder.userIcon);
		mHolder.userName.setText(user.getNickname());
		mHolder.userSex.setText(SexState.getState(user.getSex()).getMessage());
		mHolder.userCharm.setText(user.getCharm().toString());
		mHolder.userLove.setText(String.format(context.getResources().getString(R.string.user_love_count), user.getLove()));
		mHolder.userConstell.setText(user.getConstell());
		mHolder.userItemView.setOnClickListener(new UserSelectAction(position));
		mHolder.userMovieLove.setText(Html.fromHtml(String.format(context.getResources().getString(R.string.user_love_movie), user.getLoveMovieName(),user.getLoveMovie())));
		mHolder.userIngMiss.setText(String.format(context.getResources().getString(R.string.user_ing_miss), user.getTryst()));

		return view;
	}

	static class ViewHolder {

		LinearLayout userItemView;
		// 用户LOGO
		ImageView userIcon;
		// 用户
		TextView userName;
		// 用户性别
		TextView userSex;
		// 用户星座
		TextView userConstell;
		// 用户颜值
		TextView userCharm;
		// 用户心动数
		TextView userLove;
		// 用户操作按钮
		TextView missBtn;
		// 用户想看的电影
		TextView userMovieLove;
		// 用户进行中的约会
		TextView userIngMiss;

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
			case R.id.miss_user_item_view:

				Intent intent = new Intent(context, UserDetailActivity.class);
				intent.putExtra("user", user);
				context.startActivity(intent);
				break;
			case R.id.miss_btn_layout:
				final MessageDialog.Builder builder = new MessageDialog.Builder(
						v.getContext());
				builder.setTitle(R.string.cancel_miss);
				builder.setMessage("您确定要同意此申请么?");
				builder.setPositiveButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("确定",
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								v.setVisibility(View.GONE);
								Message message = new Message();
								message.what = Miss.AGREE_MISS;
								Bundle bundle = new Bundle();
								bundle.putSerializable("user", user);
								message.setData(bundle);
								handler.sendMessage(message);
							}
						});

				builder.create().show();
				break;
			default:
				break;
			}
		}

	}

}
