package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Html;
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
import com.movie.client.bean.NearNaruto;
import com.movie.state.SexState;
import com.movie.ui.MovieDetailActivity;
import com.movie.ui.UserDetailActivity;
import com.movie.util.Horoscope;
import com.movie.util.StringUtil;
import com.movie.util.UserCharm;

public class NarutoAdapter extends BaseObjectListAdapter {

	
	public NarutoAdapter(Context context, Handler mHandler,List<? extends BaseBean> datas) {
		super(context, mHandler, datas);

	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = mInflater.inflate(R.layout.naruto_item, null);
			mHolder = new ViewHolder();
			mHolder.userMovieTag = (LinearLayout) view.findViewById(R.id.user_movie_tag);
			mHolder.movieBreifLayout= (LinearLayout) view.findViewById(R.id.movie_breif_layout);
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
			//mHolder.userBtnView = (LinearLayout) view.findViewById(R.id.user_btn_view);
			//mHolder.userIngMiss = (TextView) view.findViewById(R.id.user_ing_miss);
			//mHolder.userBtnMessage = (TextView) view.findViewById(R.id.user_btn_message);
			//mHolder.userBtnLove = (TextView) view.findViewById(R.id.user_btn_love);
			//mHolder.missInvite = (TextView) view.findViewById(R.id.miss_invite);

			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		final NearNaruto nearNaruto = (NearNaruto)getItem(position);
		imageLoader.displayImage(nearNaruto.getPortrait(), mHolder.userIcon,NarutoApplication.imageOptions);
		mHolder.userName.setText(nearNaruto.getNickname());
		mHolder.userSex.setText(SexState.getState(nearNaruto.getSex()).getMessage());
		mHolder.userLove.setText(String.format(mContext.getResources().getString(R.string.user_love_count), nearNaruto.getLoveCnt()));
		if(nearNaruto.getBirthday()>0){
			int [] date=StringUtil.strConvertInts(String.valueOf(nearNaruto.getBirthday()));
			mHolder.userConstell.setText(Horoscope.getHoroscope((byte)date[1],(byte)date[2]).getCnName());
		}
	
		if(nearNaruto.getFilmName()!=null&&!nearNaruto.getFilmName().isEmpty()){
			mHolder.userMovieTag.setVisibility(View.VISIBLE);
		   mHolder.userMovieLove.setText(Html.fromHtml(String.format(mContext.getResources().getString(R.string.user_love_movie), nearNaruto.getFilmName(),nearNaruto.getFilmCnt())));
		}else{
			mHolder.userMovieTag.setVisibility(View.GONE);
		}
		//mHolder.userIngMiss.setText(String.format(context.getResources().getString(R.string.user_ing_miss), user.getTryst()));
		String score=UserCharm.GetScore(nearNaruto.getFaceTtl(), nearNaruto.getFaceCnt()<=0?1:nearNaruto.getFaceCnt());
		if(score.equals("NaN")){
			
		}else{
			mHolder.userCharmBar.setRating(Float.valueOf(score)/2f);
			mHolder.userCharm.setText(score);
		}
		
		mHolder.movieBreifLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent movieDetailIntent = new Intent(mContext, MovieDetailActivity.class);
				movieDetailIntent.putExtra("filmId", nearNaruto.getFilmId());
				mContext.startActivity(movieDetailIntent);
				
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
		LinearLayout userMovieTag;
		LinearLayout movieBreifLayout;
		LinearLayout userBtnView;
        LinearLayout userBreifLayout;
		LinearLayout userItemView;
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
		// 用户留言
		//TextView userBtnMessage;
		// 用户心动
	    //TextView userBtnLove;
	    // 用户邀请
	    //TextView missInvite;

	}

}
