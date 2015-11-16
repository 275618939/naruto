package com.movie.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.PartNarutoExpandableAdapter.UserSelectAction;
import com.movie.app.Constant;
import com.movie.client.bean.Dictionary;
import com.movie.client.bean.Miss;
import com.movie.client.bean.User;
import com.movie.client.dao.BaseDao;
import com.movie.client.dao.CommentDaoImple;
import com.movie.client.db.SQLHelper;
import com.movie.ui.MissUserQueryActivity;
import com.movie.view.MessageDialog;

public class EvaluationAdapter extends BaseAdapter {
	

	List<Map<Integer,Integer>> dictionarys;
	Map<Integer,String> dictionarysAll;
	int sex;
	User user;
	boolean showCount;
	Context context;
	LayoutInflater inflater;
	BaseDao commentDao;
	Handler handler;
	int[] colors=new int[]{R.color.tag1,R.color.tag2,R.color.tag3,R.color.tag4,R.color.tag5,R.color.tag6,R.color.tag7,R.color.tag8};
	
	public EvaluationAdapter(Context context,List<Map<Integer,Integer>> dictionarys) {
		this.context = context;
		this.dictionarys = dictionarys;
		inflater = LayoutInflater.from(context);
		initData();
	}
	public EvaluationAdapter(Context context,Handler handler,List<Map<Integer,Integer>> dictionarys) {
		this.context = context;
		this.dictionarys = dictionarys;
		this.handler=handler;
		inflater = LayoutInflater.from(context);
		initData();
	}
	public EvaluationAdapter(Context context, int sex,List<Map<Integer,Integer>> dictionarys) {
		this.context = context;
		this.dictionarys = dictionarys;
		this.sex=sex;
		inflater = LayoutInflater.from(context);
		initData();
	}
	protected void initData(){
		commentDao =new CommentDaoImple();
		List<Map<String, String>> DictionaryList = commentDao.listData(null, null);
		if(null!=DictionaryList&&DictionaryList.size()>0){
			dictionarysAll =new HashMap<Integer, String>();
			for (Map<String, String> map : DictionaryList) {
				dictionarysAll.put(Integer.parseInt(map.get(SQLHelper.ID)),map.get(SQLHelper.NAME));
			}
		}else{
			dictionarysAll=Dictionary.commentsMap.get(sex);
		}
	
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
		mHolder.dictionary.setBackgroundResource(colors[index]);
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
			mHolder.dictionary.setOnClickListener(new UserSelectAction(position));
		}
		return view;
	}
	
	static class ViewHolder {
		
		LinearLayout linearLayout;
		//用户评价
		TextView dictionary;
		
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
