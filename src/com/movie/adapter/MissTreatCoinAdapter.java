package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.movie.R;
import com.movie.app.BaseObjectListAdapter;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.BaseBean;
import com.movie.client.bean.Miss;

public class MissTreatCoinAdapter extends BaseObjectListAdapter {
	
	
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
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		// 获取position对应的数据
		final Miss miss =(Miss)getItem(position);
		imageLoader.displayImage(miss.getIcon(),mHolder.narutoPortrait,NarutoApplication.imageOptions);
		
		return view;
	}

	class ViewHolder {

		// 会员LOGO
		ImageView narutoPortrait;
		// 影币
		EditText coin;
	

	}


}
