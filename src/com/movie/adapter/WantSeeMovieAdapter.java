package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.client.bean.User;
import com.movie.ui.UserDetailActivity;
import com.movie.util.ImageLoaderCache;
import com.movie.util.StringUtil;
import com.movie.view.RoundImageView;

public class WantSeeMovieAdapter extends BaseAdapter {
	

	List<User> users;
	Context context;
	LayoutInflater inflater;
	ImageLoaderCache imageLoader;
	
	
	public WantSeeMovieAdapter(Context context,List<User> users) {
		this.context = context;
		this.users = users;
		imageLoader=new ImageLoaderCache(context);
		inflater = LayoutInflater.from(context);
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return users == null ? 0 : users.size();
	}
	@Override
	public User getItem(int position) {
		// TODO Auto-generated method stub
		if (users != null && users.size() != 0) {
			return users.get(position);
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
			view = inflater.inflate(R.layout.want_see_user_item, null);
			mHolder = new ViewHolder();
			mHolder.wantSeeView=(LinearLayout)view.findViewById(R.id.want_see_view);
			mHolder.userImage= (RoundImageView)view.findViewById(R.id.user_image);
			mHolder.userNick = (TextView)view.findViewById(R.id.user_nick);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		//获取position对应的数据
		User user = getItem(position);
		imageLoader.DisplayImage(user.getPortrait(), mHolder.userImage);
		mHolder.userNick.setText(StringUtil.getShortStr(user.getNickname(), 3));
		mHolder.userImage.setOnClickListener(new UserSelectAction(position));
		return view;
	}
	static class ViewHolder {
		
		LinearLayout wantSeeView;
		RoundImageView userImage;
		TextView userNick;
		
		
	}
	public void updateData(List<User> users) {
		this.users=users;
		this.notifyDataSetChanged();
		
	}
	protected class UserSelectAction implements OnClickListener{

		int position;
		
		public UserSelectAction(int position){
			this.position=position;
		}
		@Override
		public void onClick(View v) {
			
			User user=users.get(position);
			Intent intent=new Intent(context,UserDetailActivity.class);
			intent.putExtra("memberId", user.getMemberId());
			context.startActivity(intent);
			
		}
		
	}
	
	
	

	
	

	




	



	
}
