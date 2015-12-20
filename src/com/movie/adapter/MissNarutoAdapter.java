package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseObjectListAdapter;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.BaseBean;
import com.movie.client.bean.Miss;
import com.movie.client.bean.MissNaruto;
import com.movie.state.MissStage;
import com.movie.state.SexState;
import com.movie.ui.UserDetailActivity;
import com.movie.util.Horoscope;
import com.movie.util.StringUtil;
import com.movie.util.UserCharm;

public class MissNarutoAdapter extends BaseObjectListAdapter {

	
	protected String memberId;
	protected String loginMemberId;
	protected int timeResult;
	
	public MissNarutoAdapter(Context context, Handler mHandler,List<? extends BaseBean> datas) {
		super(context, mHandler, datas);
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = mInflater.inflate(R.layout.miss_attend_naruto_item, null);
			mHolder = new ViewHolder();
			mHolder.missBtnLayout= (LinearLayout) view.findViewById(R.id.miss_btn_layout);
			mHolder.userBreifLayout= (LinearLayout) view.findViewById(R.id.user_breif_layout);
			mHolder.userItemView = (LinearLayout) view.findViewById(R.id.user_item_view);
			mHolder.userIcon = (ImageView) view.findViewById(R.id.user_icon);
			mHolder.userName = (TextView) view.findViewById(R.id.user_name);
			mHolder.userSex = (TextView) view.findViewById(R.id.user_sex);
			mHolder.userConstell = (TextView) view.findViewById(R.id.user_constell);
			mHolder.userCharm = (TextView) view.findViewById(R.id.user_charm);
			mHolder.userLove = (TextView) view.findViewById(R.id.user_love);
			mHolder.userMovieLove = (TextView) view.findViewById(R.id.user_movie_love);
			mHolder.userCharmBar = (RatingBar) view.findViewById(R.id.user_charm_bar);
			mHolder.missBtn = (TextView)view.findViewById(R.id.miss_btn);
		
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		final MissNaruto nearNaruto = (MissNaruto)getItem(position);
		imageLoader.displayImage(nearNaruto.getPortrait(), mHolder.userIcon,NarutoApplication.imageOptions);
		mHolder.userName.setText(nearNaruto.getNickname());
		mHolder.userSex.setText(SexState.getState(nearNaruto.getSex()).getMessage());
		mHolder.userLove.setText(String.format(mContext.getResources().getString(R.string.user_love_count), nearNaruto.getLoveCnt()));
		if(nearNaruto.getBirthday()>0){
			int [] date=StringUtil.strConvertInts(String.valueOf(nearNaruto.getBirthday()));
			mHolder.userConstell.setText(Horoscope.getHoroscope((byte)date[1],(byte)date[2]).getCnName());
		}
		String score=UserCharm.GetScore(nearNaruto.getFaceTtl(), nearNaruto.getFaceCnt()<=0?1:nearNaruto.getFaceCnt());
		if(score.equals("NaN")){
			
		}else{
			mHolder.userCharmBar.setRating(Float.valueOf(score)/2f);
			mHolder.userCharm.setText(score);
		}
		mHolder.missBtnLayout.setVisibility(View.GONE);
		if(nearNaruto.getStage()>=0){
			if(memberId.equals(loginMemberId)){
				mHolder.missBtnLayout.setVisibility(View.VISIBLE);
				mHolder.missBtn.setText(MissStage.getState(nearNaruto.getStage()).getMessage());
				//是否可同意
				if(nearNaruto.getStage()==MissStage.Apply.getState()){
					mHolder.missBtn.setText(mContext.getResources().getString(R.string.miss_agree));
					mHolder.missBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							mHolder.missBtn.setText(mContext.getResources().getString(R.string.miss_agree_after));
							Message message=new Message();
							Bundle bundle=new Bundle();
							bundle.putString("memberid",nearNaruto.getMemberId());
							message.setData(bundle);
							message.what=Miss.AGREE_MISS;
							mHandler.sendMessage(message);
						}
					});
				//是否可踢出
				}else if(nearNaruto.getStage()==MissStage.At.getState()&&timeResult>0){ 
					mHolder.missBtn.setText(mContext.getResources().getString(R.string.kicked_out));
					mHolder.missBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Message message=new Message();
								Bundle bundle=new Bundle();
								bundle.putString("memberid",nearNaruto.getMemberId());
								bundle.putInt("position", position);
								message.setData(bundle);
								message.what=Miss.KICKED_OUT;
								mHandler.sendMessage(message);
							}
						});
				}
			}
		}
		
		mHolder.userBreifLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent userDetailIntent = new Intent(mContext, UserDetailActivity.class);
				userDetailIntent.putExtra("memberId", nearNaruto.getMemberId());
				mContext.startActivity(userDetailIntent);
				
			}
		});
		mHolder.userItemView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent userDetailIntent = new Intent(mContext, UserDetailActivity.class);
				userDetailIntent.putExtra("memberId", nearNaruto.getMemberId());
				mContext.startActivity(userDetailIntent);
				
			}
		});

		return view;
	}

	class ViewHolder {
	

		LinearLayout userBtnView;
        LinearLayout userBreifLayout;
		LinearLayout userItemView;
		LinearLayout missBtnLayout;
		//用户颜值
		RatingBar userCharmBar;
		// 用户LOGO
		ImageView userIcon;
		// 用户
		TextView userName;
		// 用户性别
		TextView userSex;
		// 用户星座
		TextView userConstell;
		// 用户颜值
		TextView userCharm;
		// 用户心动数
		TextView userLove;
		// 用户想看的电影
		TextView userMovieLove;
		// 操作按钮
		TextView missBtn;

	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public void setLoginMemberId(String loginMemberId) {
		this.loginMemberId = loginMemberId;
	}
	public void setTimeResult(int timeResult) {
		this.timeResult = timeResult;
	}
	
	
	

}
