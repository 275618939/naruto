package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.SexState;
import com.movie.client.bean.Miss;
import com.movie.client.bean.User;
import com.movie.ui.UserDetailActivity;
import com.movie.util.ImageLoaderCache;
import com.movie.view.MessageDialog;

public class MissUserAdapter extends BaseAdapter {

	List<User> userList;
	Context context;
	LayoutInflater inflater;
	ImageLoaderCache imageLoaderCache;
	Handler handler;

	public MissUserAdapter(Context context, List<User> users) {
		this.context = context;
		this.userList = users;
		inflater = LayoutInflater.from(context);
		imageLoaderCache=ImageLoaderCache.getInstance(context);

	}

	public MissUserAdapter(Context context, Handler handler,List<User> users) {
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
			view = inflater.inflate(R.layout.miss_user_item, null);
			mHolder = new ViewHolder();
			mHolder.missItemView = (RelativeLayout) view.findViewById(R.id.miss_user_item_view);
			mHolder.missIcon = (ImageView) view.findViewById(R.id.miss_user_icon);
			mHolder.missUserName = (TextView) view.findViewById(R.id.user_name);
			mHolder.missUserSex = (TextView) view.findViewById(R.id.miss_sex);
			mHolder.missUserCharm = (TextView) view.findViewById(R.id.user_charm);
			mHolder.missUserLove = (TextView) view.findViewById(R.id.user_love);
			mHolder.missBtn = (TextView) view.findViewById(R.id.miss_btn);
			mHolder.missBtnLayout = (LinearLayout) view.findViewById(R.id.miss_btn_layout);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		// 获取position对应的数据
		User user = getItem(position);

		imageLoaderCache.DisplayImage(user.getPortrait(), mHolder.missIcon);
		mHolder.missUserName.setText(user.getNickname());
		mHolder.missUserSex.setText(SexState.getState(user.getSex()).getMessage());
		mHolder.missUserCharm.setText(user.getCharm().toString());
		mHolder.missUserLove.setText(user.getLove().toString());
		mHolder.missBtnLayout.setVisibility(View.VISIBLE);
		mHolder.missBtn.setText("同意约会");
		mHolder.missBtnLayout.setOnClickListener(new UserSelectAction(position));
		mHolder.missItemView.setOnClickListener(new UserSelectAction(position));

		return view;
	}

	static class ViewHolder {

		RelativeLayout missItemView;
		LinearLayout missBtnLayout;
		// 约会LOGO
		ImageView missIcon;
		// 约会人
		TextView missUserName;
		// 约会人性别
		TextView missUserSex;
		// 约会人颜值
		TextView missUserCharm;
		// 约会人心动数
		TextView missUserLove;
		// 约会操作按钮
		TextView missBtn;

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
				final MessageDialog.Builder builder = new MessageDialog.Builder(v.getContext());
				builder.setTitle(R.string.cancel_miss);
				builder.setMessage("您确定要同意此申请么?");
				builder.setPositiveButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("确定",
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								v.setVisibility(View.GONE);
								Message message=new Message();
								message.what=Miss.AGREE_MISS;
								Bundle bundle=new Bundle();
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
