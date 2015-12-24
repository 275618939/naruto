package com.movie.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.Constant;
import com.movie.state.BackGroundColor;

public class CommentsShowAdapter extends BaseAdapter {
	

	List<Map<Integer,String>> comments;
	ArrayList<Integer> selectComments=new ArrayList<Integer>();
	Context context;
	LayoutInflater inflater;
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
		final ViewHolder mHolder;
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
		final int index=position%Constant.Page.COMMENTS_MAX_SHOW;
		mHolder.comment.setBackgroundResource(BackGroundColor.getState(index).getSourceId());
		Map<Integer,String> commentMap = getItem(position);
		for(Entry<Integer, String> entry:commentMap.entrySet()){ 
			mHolder.comment.setTag(R.id.tag_key,entry.getKey());
			mHolder.comment.setText(entry.getValue());
		}
		mHolder.comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Object backObject = mHolder.comment.getTag(R.id.tag_select);
				if(null!=backObject){
					mHolder.comment.setBackgroundResource(Integer.parseInt(String.valueOf(backObject)));
					mHolder.comment.setTag(R.id.tag_select, R.color.btn_add);
					selectComments.remove(mHolder.comment.getTag(R.id.tag_key));
				}else{
					selectComments.add(Integer.valueOf(String.valueOf(mHolder.comment.getTag(R.id.tag_key))));
					mHolder.comment.setBackgroundResource(R.color.btn_add);
					mHolder.comment.setTag(R.id.tag_select, BackGroundColor.getState(index).getSourceId());
				}
				
			}
		});
		return view;
	}
	
	class ViewHolder {
		
		LinearLayout linearLayout;
		//用户评价
		TextView comment;

	}
	public void updateData(List<Map<Integer,String>> comments) {
		this.comments=comments;
		this.notifyDataSetChanged();
		
	}
	public ArrayList<Integer> getSelectComments() {
		return selectComments;
	}
	
	
	
	
	

	
}
