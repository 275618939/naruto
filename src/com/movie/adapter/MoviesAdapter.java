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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseObjectListAdapter;
import com.movie.app.Constant.NameShow;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.BaseBean;
import com.movie.client.bean.Miss;
import com.movie.client.bean.Movie;
import com.movie.ui.MissCreateActivity;
import com.movie.ui.MovieDetailActivity;
import com.movie.util.MovieScore;
import com.movie.util.SharedPreferencesUtils;

public class MoviesAdapter extends BaseObjectListAdapter {

	protected int missType;

	public MoviesAdapter(Context context, Handler mHandler,
			List<? extends BaseBean> datas) {
		super(context, mHandler, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = mInflater.inflate(R.layout.movie_item, null);
			mHolder = new ViewHolder();
			mHolder.movieView = (RelativeLayout) view
					.findViewById(R.id.movie_view);
			mHolder.movieNoneScoreLayout = (RelativeLayout) view
					.findViewById(R.id.movie_none_score_layout);
			mHolder.movieHaveScoreLayout = (RelativeLayout) view
					.findViewById(R.id.movie_have_score_layout);
			mHolder.movieImage = (ImageView) view
					.findViewById(R.id.movie_image);
			mHolder.titleText = (TextView) view.findViewById(R.id.movie_title);
			mHolder.startBar = (RatingBar) view.findViewById(R.id.movie_star);
			mHolder.scoreText = (TextView) view.findViewById(R.id.movie_score);
			mHolder.movieMent = (TextView) view.findViewById(R.id.movie_ment);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		// 获取position对应的数据
		final Movie movie = (Movie) getItem(position);
		if (movie != null) {
			String score = MovieScore.GetScore(movie.getScore(),movie.getScoreCnt());
			if (score.equals("NaN")) {
				mHolder.movieNoneScoreLayout.setVisibility(View.VISIBLE);
				mHolder.movieHaveScoreLayout.setVisibility(View.GONE);
			}
			imageLoader.displayImage(movie.getIcon(), mHolder.movieImage,NarutoApplication.imageOptions);
			if (movie.getName().length() > NameShow.MOVIENAME_MAX) {
				mHolder.titleText.setText(movie.getName().substring(0,
						NameShow.MOVIENAME_MAX));
			} else {
				mHolder.titleText.setText(movie.getName());
			}
			mHolder.startBar.setRating(Float.valueOf(score) / 2f);
			mHolder.scoreText.setText(score);
			mHolder.movieMent.setText(Html.fromHtml(String.format(mContext
					.getResources().getString(R.string.movie_miss_tryst), movie
					.getTryst())));

		}
		mHolder.movieView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(missType==Miss.CREATE_MISS){
					SharedPreferencesUtils.setParam(mContext, "filmId", movie.getId());
					SharedPreferencesUtils.setParam(mContext, "filmName", movie.getName());
					Intent missCreate = new Intent(mContext, MissCreateActivity.class);
					mContext.startActivity(missCreate);
				}else{
					Intent intent = new Intent(mContext, MovieDetailActivity.class);
					intent.putExtra("filmId", movie.getId());
					mContext.startActivity(intent);
				}

			}
		});
		return view;
	}

	class ViewHolder {

		RelativeLayout movieView;
		RelativeLayout movieNoneScoreLayout;
		RelativeLayout movieHaveScoreLayout;
		ImageView movieImage;
		TextView movieMent;
		TextView titleText;
		RatingBar startBar;
		TextView scoreText;

	}

	public void setMissType(int missType) {
		this.missType = missType;
	}
	

}
