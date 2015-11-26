package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.MissStateBackColor;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.Miss;
import com.movie.ui.MissNarutoDetailActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MissNarutoQueryAdapter extends BaseAdapter {

	List<Miss> misses;
	Context context;
	LayoutInflater inflater;
	ImageLoader imageLoaderCache;
	Handler handler;
	int missType;

	public MissNarutoQueryAdapter(Context context, List<Miss> misses) {
		this.context = context;
		this.misses = misses;
		inflater = LayoutInflater.from(context);
		imageLoaderCache=ImageLoader.getInstance();
	}
	public MissNarutoQueryAdapter(Context context,Handler handler, List<Miss> misses) {
		this.context = context;
		this.misses = misses;
		this.handler=handler;
		inflater = LayoutInflater.from(context);
		imageLoaderCache=ImageLoader.getInstance();

	}

	@Override
	public int getCount() {
		return misses == null ? 0 : misses.size();
	}

	@Override
	public Miss getItem(int position) {
		if (misses != null && misses.size() != 0) {
			return misses.get(position);
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
			view = inflater.inflate(R.layout.miss_naruto_item, null);
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
		Miss miss = getItem(position);
		imageLoaderCache.displayImage(miss.getIcon(),mHolder.missIcon,NarutoApplication.imageOptions);
		mHolder.missUser.setText(miss.getMemberId());
		mHolder.missDate.setText(miss.getRunTime());
		mHolder.missName.setText(miss.getCinameName());
		mHolder.missCoin.setText(miss.getCoin()==null?"0":miss.getCoin().toString());
		int sourceId = MissStateBackColor.getState(miss.getStatus()).getSourceId();
		mHolder.missItemView.setBackgroundResource(sourceId);
		mHolder.missItemView.setOnClickListener(new UserSelectAction(position));
		return view;
	}

	static class ViewHolder {

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

	public void updateData(List<Miss> misses) {
		this.misses = misses;
		this.notifyDataSetChanged();

	}
	

	protected class UserSelectAction implements OnClickListener {

		int position;

		public UserSelectAction(int position) {
			this.position = position;
		}

		@Override
		public void onClick(final View v) {

			switch (v.getId()) {
			case R.id.miss_item_view:
				Miss miss = misses.get(position);
				Intent intent = new Intent(context, MissNarutoDetailActivity.class);
				intent.putExtra("miss", miss);
				context.startActivity(intent);
				break;
			default:
				break;
			}
			
		}

	}

	public void setMissType(int missType) {
		this.missType = missType;
	}
	

}
