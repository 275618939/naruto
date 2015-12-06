package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseObjectListAdapter;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.BaseBean;
import com.movie.client.bean.MovieComment;
import com.movie.ui.UserDetailActivity;
import com.movie.view.RoundImageView;

public class MoviesCommentAdapter extends BaseObjectListAdapter {
	

	public MoviesCommentAdapter(Context context,List<? extends BaseBean> datas) {
		super(context, datas);
	}
	
	public MoviesCommentAdapter(Context context, Handler mHandler,List<? extends BaseBean> datas) {
		super(context, mHandler, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = mInflater.inflate(R.layout.movie_comment_item, null);
			mHolder = new ViewHolder();
			mHolder.movieCommentItemView=(RelativeLayout)view.findViewById(R.id.movie_comment_item_view);
			mHolder.imageView=(RoundImageView)view.findViewById(R.id.user_image);
			mHolder.nickname=(TextView)view.findViewById(R.id.nickname);
			mHolder.starBar=(RatingBar)view.findViewById(R.id.score_star);
			mHolder.score= (TextView)view.findViewById(R.id.score);
			mHolder.content = (TextView)view.findViewById(R.id.content);
			mHolder.time = (TextView)view.findViewById(R.id.time);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		//获取position对应的数据
		final MovieComment movieComment = (MovieComment)getItem(position);
		if(movieComment!=null){
			imageLoader.displayImage(movieComment.getPortrait(),mHolder.imageView,NarutoApplication.imageOptions);
			mHolder.nickname.setText(movieComment.getNickname());
			mHolder.starBar.setRating(Float.valueOf(movieComment.getScore())/10f/2f);
			mHolder.score.setText(String.valueOf(movieComment.getScore()/10f));
			mHolder.content.setText(movieComment.getContent());
			mHolder.time.setText(movieComment.getTime());
		}
		mHolder.movieCommentItemView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				Intent intent=new Intent(mContext,UserDetailActivity.class);
				intent.putExtra("memberId", movieComment.getMemberId());
				mContext.startActivity(intent);
				
			}
		});
		return view;
	}
	class ViewHolder {
		
		RelativeLayout movieCommentItemView;
		RoundImageView imageView;
		TextView nickname;
		RatingBar starBar;
		TextView score;
		TextView content;
		TextView time;
		
		
	}
	
}
