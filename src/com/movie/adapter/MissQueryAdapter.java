package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseObjectListAdapter;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.BaseBean;
import com.movie.client.bean.Miss;
import com.movie.state.MissState;
import com.movie.state.MissStateBackColor;
import com.movie.state.MissTimeBackColor;
import com.movie.state.MissTimeState;
import com.movie.ui.MissNarutoDetailActivity;
import com.movie.util.StringUtil;

public class MissQueryAdapter extends BaseObjectListAdapter {
	
	
	public MissQueryAdapter(Context context, Handler mHandler,List<? extends BaseBean> datas) {
		super(context, mHandler, datas);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = mInflater.inflate(R.layout.miss_item, null);
			mHolder = new ViewHolder();
			mHolder.missItemView = (RelativeLayout) view.findViewById(R.id.miss_item_view);
			mHolder.missIcon = (ImageView) view.findViewById(R.id.miss_icon);
			mHolder.missUser = (TextView) view.findViewById(R.id.miss_user);
			mHolder.missDate = (TextView) view.findViewById(R.id.miss_date);
			mHolder.missName = (TextView) view.findViewById(R.id.miss_name);
			mHolder.missCoin = (TextView) view.findViewById(R.id.miss_coin);
			mHolder.missBtn = (TextView) view.findViewById(R.id.miss_btn);
			mHolder.missBtnLayout = (LinearLayout) view.findViewById(R.id.miss_btn_layout);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		// 获取position对应的数据
		final Miss miss =(Miss)getItem(position);
		imageLoader.displayImage(miss.getIcon(),mHolder.missIcon,NarutoApplication.imageOptions);
		mHolder.missUser.setText(miss.getNickName());
		mHolder.missDate.setText(StringUtil.getShortStrBySym(miss.getRunTime(),":"));
		mHolder.missName.setText(miss.getFilmName());
		mHolder.missCoin.setText(String.valueOf(miss.getCoin()));
		mHolder.missName.setText(miss.getFilmName());
		mHolder.missItemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, MissNarutoDetailActivity.class);
				intent.putExtra("miss", miss);
				mContext.startActivity(intent);
			}
		});
		//根据时间验证
		int result=StringUtil.dateCompareByCurrent(miss.getRunTime());
		mHolder.missItemView.setBackgroundResource(MissTimeBackColor.getState(result).getSourceId());
		mHolder.missBtn.setText(MissTimeState.getState(result).getMessage());
		if(result==MissTimeState.HaveInHand.getState()){
			//时间和状态不符
			if(miss.getStatus()>MissState.HaveInHand.getState()){
				mHolder.missItemView.setBackgroundResource(MissStateBackColor.getState(miss.getStatus()).getSourceId());
				mHolder.missBtn.setText(MissState.getState(miss.getStatus()).getMessage());
			}
		}
		
		
		return view;
	}

	class ViewHolder {

		RelativeLayout missItemView;
		LinearLayout missBtnLayout;
		// 约会LOGO
		ImageView missIcon;
		// 约会人
		TextView missUser;
		// 约会时间
		TextView missDate;
		// 约会悬赏影币
		TextView missCoin;
		// 影片名称
		TextView missName;
		// 约会操作按钮
		TextView missBtn;

	}


}
