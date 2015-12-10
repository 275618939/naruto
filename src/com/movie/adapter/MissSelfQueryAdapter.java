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
import com.movie.ui.MissSelfDetailActivity;
import com.movie.util.StringUtil;

public class MissSelfQueryAdapter extends BaseObjectListAdapter {

	
	int missType;
	public MissSelfQueryAdapter(Context context, Handler mHandler,List<? extends BaseBean> datas) {
		super(context, mHandler, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = mInflater.inflate(R.layout.miss_slef_item, null);
			mHolder = new ViewHolder();
			mHolder.missItemView = (RelativeLayout) view.findViewById(R.id.miss_item_view);
			mHolder.missIcon = (ImageView) view.findViewById(R.id.miss_icon);
			mHolder.missDate = (TextView) view.findViewById(R.id.miss_date);
			mHolder.missName = (TextView) view.findViewById(R.id.miss_name);
			mHolder.missBtn = (TextView) view.findViewById(R.id.miss_btn);
			mHolder.missCoin = (TextView) view.findViewById(R.id.miss_coin);
			mHolder.missBtnLayout = (LinearLayout) view.findViewById(R.id.miss_btn_layout);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		// 获取position对应的数据
		final Miss miss = (Miss)getItem(position);
		imageLoader.displayImage(miss.getIcon(),mHolder.missIcon,NarutoApplication.imageOptions);
		mHolder.missDate.setText(StringUtil.getShortStrBySym(miss.getRunTime(),":"));
		mHolder.missName.setText(miss.getFilmName());
		mHolder.missCoin.setText(miss.getCoin()==null?"0":miss.getCoin().toString());
		int sourceId = MissStateBackColor.getState(miss.getStatus()).getSourceId();
		mHolder.missItemView.setBackgroundResource(sourceId);
		mHolder.missItemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, MissSelfDetailActivity.class);
				intent.putExtra("miss", miss);
				mContext.startActivity(intent);
			}
		});
		if(miss.getStatus().intValue()==MissState.Expired.getState()){
			mHolder.missBtnLayout.setVisibility(View.VISIBLE);
			mHolder.missBtn.setText(mContext.getResources().getString(R.string.branch_coin));
		}
		int result=StringUtil.dateCompareByCurrent(miss.getRunTime());
		if(result<0){
			mHolder.missItemView.setBackgroundResource(MissStateBackColor.Expired.getSourceId());
			mHolder.missBtnLayout.setVisibility(View.VISIBLE);
			mHolder.missBtn.setText(mContext.getResources().getString(R.string.branch_coin));
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
		// 影片名称
		TextView missName;
		// 约会地址
		TextView missAddress;
		// 约会操作
		TextView missBtn;
		// 悬赏影币
		TextView missCoin;

	}


	public void setMissType(int missType) {
		this.missType = missType;
	}
	

}
