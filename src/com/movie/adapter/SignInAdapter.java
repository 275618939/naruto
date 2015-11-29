package com.movie.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.client.bean.Miss;
import com.movie.client.bean.User;
import com.movie.client.service.CommentService;

public class SignInAdapter extends BaseAdapter {
	

	List<Map<Integer,Integer>> signInList;
	int sex;
	User user;
	boolean showCount;
	Context context;
	LayoutInflater inflater;
	CommentService commentService;
	Handler handler;

	
	public SignInAdapter(Context context,List<Map<Integer,Integer>> signInList) {
		this.context = context;
		this.signInList = signInList;
		inflater = LayoutInflater.from(context);
	}
	public SignInAdapter(Context context,Handler handler,List<Map<Integer,Integer>> signInList) {
		this.context = context;
		this.signInList = signInList;
		this.handler=handler;
		inflater = LayoutInflater.from(context);
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return signInList == null ? 0 : signInList.size();
	}
	@Override
	public Map<Integer,Integer> getItem(int position) {
		// TODO Auto-generated method stub
		if (signInList != null && signInList.size() != 0) {
			return signInList.get(position);
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
			view = inflater.inflate(R.layout.signin_item, null);
			mHolder = new ViewHolder();
			mHolder.linearLayout= (LinearLayout)view.findViewById(R.id.comment_view);
			mHolder.reward = (TextView)view.findViewById(R.id.reward);
			mHolder.signIn = (TextView)view.findViewById(R.id.signInBtn);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		//获取position对应的数据
		Map<Integer,Integer> signs = getItem(position);
		for (Map.Entry<Integer, Integer> entry : signs.entrySet()) { 
			mHolder.reward.setText("奖励"+String.valueOf(entry.getKey())+"影币");
		}
		return view;
	}
	
	class ViewHolder {
		
		LinearLayout linearLayout;
		//签到奖励
		TextView reward;
		//签到
		TextView signIn;
		
	}
	protected class UserSelectAction implements OnClickListener {

		int position;

		public UserSelectAction(int position) {
			this.position = position;
		}

		@Override
		public void onClick(final View v) {

			switch (v.getId()) {
			case R.id.comment:
				Message message = new Message();
				message.what = Miss.EVLATOIN_USER;
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", user);
				message.setData(bundle);
				handler.sendMessage(message);
				break;
			
			default:
				break;
			}
			
		}

	}
	public void updateData(List<Map<Integer,Integer>> signInList) {
		this.signInList=signInList;
		this.notifyDataSetChanged();
		
	}
	

	public void setUser(User user) {
		this.user = user;
	}
	

	
	

	
	

	




	



	
}
