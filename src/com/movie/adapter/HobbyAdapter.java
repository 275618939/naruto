package com.movie.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.Constant;
import com.movie.client.dao.BaseDao;
import com.movie.state.BackGroundColor;

public class HobbyAdapter extends BaseAdapter {
	

	Map<Integer,String> hobbies;
	List<Integer> userHobbies;
	Context context;
	LayoutInflater inflater;
	BaseDao commentDao;
	
	public HobbyAdapter(Context context, Map<Integer,String> hobbies,List<Integer> userHobbies) {
		this.context = context;
		this.hobbies = hobbies;
		inflater = LayoutInflater.from(context);		
	}
	@Override
	public int getCount() {
		return userHobbies == null ? 0 : userHobbies.size();
	}
	@Override
	public Integer getItem(int position) {
		if (userHobbies != null && userHobbies.size() != 0) {
			return userHobbies.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.comment_item, null);
			mHolder = new ViewHolder();
			mHolder.linearLayout= (LinearLayout)view.findViewById(R.id.comment_view);
			mHolder.Dictionary = (TextView)view.findViewById(R.id.comment);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		int index=position%Constant.Page.COMMENTS_MAX_SHOW;
		mHolder.Dictionary.setBackgroundResource(BackGroundColor.getState(index).getSourceId());
		Integer hobby = getItem(position);
		if(null!=hobby){
			mHolder.Dictionary.setText(hobbies.get(hobby));
		}
		return view;
	}
	static class ViewHolder {
		
		LinearLayout linearLayout;
		//用户评价
		TextView Dictionary;
		
	}
	public void updateData(Map<Integer,String> hobbies,List<Integer> userHobbies) {
		this.hobbies=hobbies;
		this.userHobbies=userHobbies;
		this.notifyDataSetChanged();
		
	}

	
	

	
	

	




	



	
}
