package com.movie.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.Constant;
import com.movie.client.bean.Dictionary;
import com.movie.client.dao.BaseDao;
import com.movie.client.dao.CommentDaoImple;
import com.movie.client.db.SQLHelper;

public class EvaluationAdapter extends BaseAdapter {
	

	List<Map<Integer,Integer>> dictionarys;
	List<Dictionary> dictionarysAll;
	Context context;
	LayoutInflater inflater;
	BaseDao commentDao;
	int[] colors=new int[]{R.color.tag1,R.color.tag2,R.color.tag3,R.color.tag4,R.color.tag5,R.color.tag6,R.color.tag7,R.color.tag8};
	
	public EvaluationAdapter(Context context, List<Map<Integer,Integer>> dictionarys) {
		this.context = context;
		this.dictionarys = dictionarys;
		inflater = LayoutInflater.from(context);
		initData();
	}
	protected void initData(){
		commentDao =new CommentDaoImple();
		List<Map<String, String>> DictionaryList = commentDao.listData(null, null);
		if(null!=DictionaryList&&DictionaryList.size()>0){
			dictionarysAll =new ArrayList<Dictionary>();
			Dictionary Dictionary = null;
			for (Map<String, String> map : DictionaryList) {
				Dictionary = new Dictionary(Integer.parseInt(map.get(SQLHelper.ID)),map.get(SQLHelper.NAME));
				dictionarysAll.add(Dictionary);
			}
		}else{
			dictionarysAll=Dictionary.dictionaries;
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
			mHolder.Dictionary = (TextView)view.findViewById(R.id.comment);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		int index=position%Constant.Page.COMMENTS_MAX_SHOW;
		mHolder.Dictionary.setBackgroundResource(colors[index]);
		//获取position对应的数据
		Map<Integer,Integer> Dictionarys = getItem(position);
		if(null!=Dictionarys){
			for(Entry<Integer, Integer> entry:Dictionarys.entrySet()){    
				Dictionary Dictionary= dictionarysAll.get(entry.getKey());
				String text=Dictionary.getName();
				if(entry.getValue()!=null&&entry.getValue()>0){
					text+="("+entry.getValue().toString()+")";
				}
			
				mHolder.Dictionary.setText(text);
			}
		}
		return view;
	}
	static class ViewHolder {
		
		LinearLayout linearLayout;
		//用户评价
		TextView Dictionary;
		
	}
	public void updateData(List<Map<Integer,Integer>> dictionarys) {
		this.dictionarys=dictionarys;
		this.notifyDataSetChanged();
		
	}

	
	

	
	

	




	



	
}
