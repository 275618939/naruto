package com.movie.adapter;

import java.util.List;

import android.content.Context;
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
import com.movie.state.MissStateBackColor;

public class MissNarutoQueryAdapter extends BaseObjectListAdapter {

	

	public MissNarutoQueryAdapter(Context context, Handler mHandler,List<? extends BaseBean> datas) {
		super(context, mHandler, datas);

	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = mInflater.inflate(R.layout.miss_naruto_item, null);
			mHolder = new ViewHolder();
			mHolder.missItemView = (RelativeLayout) view.findViewById(R.id.miss_item_view);
			mHolder.missIcon = (ImageView) view.findViewById(R.id.miss_icon);
			mHolder.missUser = (TextView) view.findViewById(R.id.miss_user);
			mHolder.missDate = (TextView) view.findViewById(R.id.miss_date);
			mHolder.missName = (TextView) view.findViewById(R.id.miss_name);
			mHolder.missCoin = (TextView) view.findViewById(R.id.miss_coin);
			mHolder.missBtnLayout = (LinearLayout) view.findViewById(R.id.miss_btn_layout);
			mHolder.missStageLayout = (LinearLayout) view.findViewById(R.id.miss_stage_layout);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		// 获取position对应的数据
		Miss miss = (Miss)getItem(position);
		imageLoader.displayImage(miss.getIcon(),mHolder.missIcon,NarutoApplication.imageOptions);
		mHolder.missUser.setText(miss.getMemberId());
		mHolder.missDate.setText(miss.getRunTime());
		mHolder.missName.setText(miss.getCinameName());
		mHolder.missCoin.setText(miss.getCoin()==null?"0":miss.getCoin().toString());
		int sourceId = MissStateBackColor.getState(miss.getStatus()).getSourceId();
		mHolder.missItemView.setBackgroundResource(sourceId);
		mHolder.missItemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Miss miss = misses.get(position);
				Intent intent = new Intent(context, MissNarutoDetailActivity.class);
				intent.putExtra("miss", miss);
				context.startActivity(intent);*/
			}
		});
		return view;
	}

	class ViewHolder {

		RelativeLayout missItemView;
		LinearLayout missBtnLayout;
		LinearLayout missStageLayout;
		// 约会LOGO
		ImageView missIcon;
		// 约会人
		TextView missUser;
		// 约会时间
		TextView missDate;
		// 影片名称
		TextView missName;
		//悬赏影币
		TextView missCoin;
		// 约会地址
		TextView missAddress;
	}


}
