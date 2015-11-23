package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.client.bean.User;
import com.movie.ui.UserDetailActivity;
import com.movie.util.ImageLoaderCache;

public class RankingAdapter extends BaseAdapter {
	
	List<User> userList;
	Context context;
	LayoutInflater inflater;
	ImageLoaderCache imageLoaderCache;

	public RankingAdapter(Context context, List<User> users) {
		this.context = context;
		this.userList = users;
		inflater = LayoutInflater.from(context);
		imageLoaderCache=ImageLoaderCache.getInstance(context);
	
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
			view = inflater.inflate(R.layout.home_miss_item, null);
			mHolder = new ViewHolder();
			mHolder.userMissLayout = (LinearLayout)view.findViewById(R.id.user_miss_layout);
			mHolder.userNickName=(TextView)view.findViewById(R.id.user_nickname);
			mHolder.userImage = (ImageView)view.findViewById(R.id.user_portrait);
			mHolder.userMiss = (TextView)view.findViewById(R.id.user_miss);
			mHolder.missTime = (TextView)view.findViewById(R.id.miss_time);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		//获取position对应的数据
		User user = getItem(position);
		imageLoaderCache.DisplayImage(user.getPortrait(), mHolder.userImage);
		mHolder.userNickName.setText(user.getNickname());
		mHolder.userMiss.setText(String.format(context.getResources().getString(R.string.miss_invite), user.getTryst()));
		mHolder.missTime.setText("2015-11-8 20:59");
		mHolder.userMissLayout.setOnClickListener(new UserSelectAction(position));
		return view;
	}

	static class ViewHolder {
		
		//布局
		LinearLayout userMissLayout;
		//昵称
		TextView userNickName;
		//用户头像
		ImageView userImage;
		//用户签名
		TextView userMiss;
		//颜值
		TextView missTime;
		//心动
	    TextView userLove;
	}


	public void updateData(List<User> userList) {
		this.userList=userList;
		this.notifyDataSetChanged();
		
	}
	protected class UserSelectAction implements OnClickListener{

		int position;
		
		public UserSelectAction(int position){
			this.position=position;
		}
		@Override
		public void onClick(View v) {
			
			User user=userList.get(position);
			Intent intent=new Intent(context,UserDetailActivity.class);
			intent.putExtra("user", user);
			context.startActivity(intent);
			
		}
		
	}
	
	

	
	

	




	



	
}
