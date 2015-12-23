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
import com.movie.client.bean.Miss;
import com.movie.client.service.CommentService;
import com.movie.state.BackGroundColor;

public class CommentsShowAdapter extends BaseAdapter {
	

	List<Map<Integer,String>> comments;
	Map<Integer,String> dictionarysAll;
	Context context;
	LayoutInflater inflater;
	CommentService commentService;
	Handler handler;

	public CommentsShowAdapter(Context context,Handler handler,List<Map<Integer,String>> comments) {
		this.context = context;
		this.comments = comments;
		this.handler=handler;
		inflater = LayoutInflater.from(context);	
	}
	@Override
	public int getCount() {
		return comments == null ? 0 : comments.size();
	}
	@Override
	public Map<Integer,String> getItem(int position) {
		if (comments != null && comments.size() != 0) {
			return comments.get(position);
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
			mHolder.comment = (TextView)view.findViewById(R.id.comment);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		int index=position%Constant.Page.COMMENTS_MAX_SHOW;
		mHolder.comment.setBackgroundResource(BackGroundColor.getState(index).getSourceId());
		Map<Integer,String> commentMap = getItem(position);
		for(Entry<Integer, String> entry:commentMap.entrySet()){    
			String text= commentMap.get(entry.getKey());
			mHolder.comment.setTag(entry.getKey());
			mHolder.comment.setText(text);
		}
		mHolder.comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message message = new Message();
				message.what = Miss.EVLATOIN_USER;
				Bundle bundle = new Bundle();
				message.setData(bundle);
				handler.sendMessage(message);
			}
		});
		return view;
	}
	
	class ViewHolder {
		
		LinearLayout linearLayout;
		//用户评价
		TextView comment;

	}
	
	

	
}
