package com.movie.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseObjectListAdapter;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.BaseBean;
import com.movie.client.bean.Miss;

public class MissTreatCoinAdapter extends BaseObjectListAdapter {
	
	Map<String, String> coins = new HashMap<String,String>();
	public MissTreatCoinAdapter(Context context, Handler mHandler,List<? extends BaseBean> datas) {
		super(context, mHandler, datas);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = mInflater.inflate(R.layout.miss_treat_coin_item, null);
			mHolder = new ViewHolder();
			mHolder.narutoPortrait = (ImageView) view.findViewById(R.id.naruto_portrait);
			mHolder.nickName = (TextView)view.findViewById(R.id.nickname);
			mHolder.coin =(EditText)view.findViewById(R.id.coin);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		// 获取position对应的数据
		final Miss miss =(Miss)getItem(position);
		imageLoader.displayImage(miss.getIcon(),mHolder.narutoPortrait,NarutoApplication.imageOptions);
		mHolder.nickName.setText(miss.getNickName());
		mHolder.coin.setTag(miss.getMemberId());
		mHolder.coin.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}		
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}			
			@Override
			public void afterTextChanged(Editable s) {
				coins.put(miss.getMemberId(), s.toString());
			}
		});
		
		
		return view;
	}

	class ViewHolder {

		// 会员LOGO
		ImageView narutoPortrait;
		//会员昵称
		TextView nickName;
		// 影币
		EditText coin;

	}

	public Map<String, String> getCoins() {
		return coins;
	}
	


}
