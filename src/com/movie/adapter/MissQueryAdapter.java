package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
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
import com.movie.client.bean.BaseBean;
import com.movie.client.bean.Miss;
import com.movie.client.bean.User;
import com.movie.fragment.SelfFragment;
import com.movie.state.MissState;
import com.movie.ui.MissUserQueryActivity;
import com.movie.view.MessageDialog;

public class MissQueryAdapter extends BaseObjectListAdapter {
	
	
	public MissQueryAdapter(Context context, Handler mHandler,List<? extends BaseBean> datas) {
		super(context, mHandler, datas);
	}
	int missType;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
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
			//mHolder.missAddress = (TextView) view.findViewById(R.id.miss_address);
			mHolder.missState = (TextView) view.findViewById(R.id.miss_state);
			mHolder.missStage = (TextView) view.findViewById(R.id.miss_stage);
			mHolder.missPart = (TextView) view.findViewById(R.id.miss_part_list);
			mHolder.missBtn = (TextView) view.findViewById(R.id.miss_btn);
			mHolder.missBtnLayout = (LinearLayout) view.findViewById(R.id.miss_btn_layout);
			mHolder.missStageLayout = (LinearLayout) view.findViewById(R.id.miss_stage_layout);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		// 获取position对应的数据
		final Miss miss =(Miss)getItem(position);
		List<User> users = miss.getAttend();
		mHolder.missUser.setText(miss.getMemberId());
		mHolder.missDate.setText(miss.getRunTime());
		mHolder.missName.setText(miss.getCinameName());
		if (null != users) {
			mHolder.missPart.setText(String.valueOf(users.size()));
		}
		if (miss.getStatus().equals(MissState.HaveInHand.getMessage())) {
			mHolder.missBtnLayout.setVisibility(View.VISIBLE);
			mHolder.missBtn.setText("撤销");
			mHolder.missBtnLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					final MessageDialog.Builder builder = new MessageDialog.Builder(v.getContext());
					builder.setTitle(R.string.cancel_miss);
					builder.setMessage("您确定要取消约会么?");
					builder.setPositiveButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
					builder.setNegativeButton("确定",
							new android.content.DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									v.setVisibility(View.GONE);
									mHandler.sendEmptyMessage(Miss.CANCLE_MISS);
								}
							});

					builder.create().show();
					
				}
			});
		}
		if(missType>SelfFragment.MY_MISS){
			if(null!=miss.getStage()){
				mHolder.missStageLayout.setVisibility(View.VISIBLE);
				//mHolder.missStage.setText(MissStage.getState(miss.getStage()).getMessage());
			}
		}
		mHolder.missItemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<User> users = miss.getAttend();
				if(null==users||users.size()<=0) {
					return;
				}
				Intent intent = new Intent(mContext, MissUserQueryActivity.class);
				intent.putExtra("miss", miss);
				mContext.startActivity(intent);
				
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
		// 约会地址
		TextView missAddress;
		// 约会状态
		TextView missState;
		// 约会人状态
		TextView missStage;
		// 约会应约人
		TextView missPart;
		// 约会操作按钮
		TextView missBtn;

	}
	public void setMissType(int missType) {
		this.missType = missType;
	}
	

}
