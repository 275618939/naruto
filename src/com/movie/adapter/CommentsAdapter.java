package com.movie.adapter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.movie.app.Constant;
import com.movie.client.bean.Dictionary;
import com.movie.client.bean.Miss;
import com.movie.client.bean.User;
import com.movie.client.service.CommentService;
import com.movie.state.BackGroundColor;

public class CommentsAdapter extends BaseAdapter {
	

	List<Map<Integer,Integer>> dictionarys;
	Map<Integer,String> dictionarysAll;
	int sex;
	User user;
	boolean showCount;
	Context context;
	LayoutInflater inflater;
	CommentService commentService;
	Handler handler;

	
	public CommentsAdapter(Context context,List<Map<Integer,Integer>> dictionarys) {
		this.context = context;
		this.dictionarys = dictionarys;
		inflater = LayoutInflater.from(context);
		initData();
	}
	public CommentsAdapter(Context context,Handler handler,List<Map<Integer,Integer>> dictionarys) {
		this.context = context;
		this.dictionarys = dictionarys;
		this.handler=handler;
		inflater = LayoutInflater.from(context);
		initData();
	}
	public CommentsAdapter(Context context, int sex,List<Map<Integer,Integer>> dictionarys) {
		this.context = context;
		this.dictionarys = dictionarys;
		this.sex=sex;
		inflater = LayoutInflater.from(context);
		initData();
	}
	protected void initData(){
		commentService =new CommentService();
		Map<Integer, Map<Integer,String>> commentsMap = commentService.getCommentsMap();
		dictionarysAll=commentsMap.get(sex);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dictionarys == null ? 0 : dictionarys.size();
	}
	@Override
	public Map<Integer,Integer> getItem(int position) {
		// TODO Auto-generated method stub
		if (dictionarys != null && dictionarys.size() != 0) {
			return dictionarys.get(position);
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
			view = inflater.inflate(R.layout.comment_item, null);
			mHolder = new ViewHolder();
			mHolder.linearLayout= (LinearLayout)view.findViewById(R.id.comment_view);
			mHolder.dictionary = (TextView)view.findViewById(R.id.comment);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		int index=position%Constant.Page.COMMENTS_MAX_SHOW;
		mHolder.dictionary.setBackgroundResource(BackGroundColor.getState(index).getSourceId());
		//获取position对应的数据
		Map<Integer,Integer> Dictionarys = getItem(position);
		if(null!=Dictionarys){
			for(Entry<Integer, Integer> entry:Dictionarys.entrySet()){    
				String text= dictionarysAll.get(entry.getKey());
				if(showCount){
					if(entry.getValue()!=null&&entry.getValue()>0){
						text+="("+entry.getValue().toString()+")";
					}
				}
				mHolder.dictionary.setTag(entry.getKey());
				mHolder.dictionary.setText(text);
			}
		}
		if(user!=null) {
			mHolder.dictionary.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Message message = new Message();
					message.what = Miss.EVLATOIN_USER;
					Bundle bundle = new Bundle();
					bundle.putSerializable("user", user);
					message.setData(bundle);
					handler.sendMessage(message);
				}
			});
		}
	
		return view;
	}
	
	class ViewHolder {
		
		LinearLayout linearLayout;
		//用户评价
		TextView dictionary;
		
	}
	
	public void updateData(boolean showCount ,int sex,List<Map<Integer,Integer>> dictionarys) {
		this.sex=sex;
		this.showCount=showCount;
		this.dictionarys=dictionarys;
		this.notifyDataSetChanged();
		
	}
	public void updateData(List<Map<Integer,Integer>> dictionarys) {
		this.dictionarys=dictionarys;
		this.notifyDataSetChanged();
		
	}
	public void updateData(int sex,List<Map<Integer,Integer>> dictionarys) {
		this.sex=sex;
		dictionarysAll=Dictionary.commentsMap.get(sex);
		this.dictionarys=dictionarys;
		this.notifyDataSetChanged();
		
	}

	public void setUser(User user) {
		this.user = user;
	}
	

	
	

	
	

	




	



	
}
