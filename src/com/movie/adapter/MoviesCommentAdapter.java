package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.client.bean.MovieComment;
import com.movie.ui.MovieDetailActivity;
import com.movie.util.ImageLoaderCache;
import com.movie.view.RoundImageView;

public class MoviesCommentAdapter extends BaseAdapter {
	

	List<MovieComment> movieComments;
	Context context;
	LayoutInflater inflater;
	ImageLoaderCache imageLoader;
	
	
	public MoviesCommentAdapter(Context context,List<MovieComment> movieComments) {
		this.context = context;
		this.movieComments = movieComments;
		imageLoader=new ImageLoaderCache(context);
		inflater = LayoutInflater.from(context);
		initData();
	}
	protected void initData(){
		
	}
	@Override
	public int getCount() {
		return movieComments == null ? 0 : movieComments.size();
	}
	@Override
	public MovieComment getItem(int position) {
		if (movieComments != null && movieComments.size() != 0) {
			return movieComments.get(position);
		}
		return null;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.movie_comment_item, null);
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
		MovieComment movieComment = getItem(position);
		if(movieComment!=null){
			imageLoader.DisplayImage(movieComment.getPortrait(),mHolder.imageView);
			mHolder.nickname.setText(movieComment.getNickname());
			mHolder.starBar.setRating(Float.valueOf(movieComment.getScore())/10/2f);
			mHolder.score.setText(String.valueOf((Float.valueOf(movieComment.getScore())/10)));
			mHolder.content.setText(movieComment.getContent());
			mHolder.time.setText(movieComment.getTime());
		}
		mHolder.movieCommentItemView.setOnClickListener(new UserSelectAction(position));
		return view;
	}
	static class ViewHolder {
		
		RelativeLayout movieCommentItemView;
		RoundImageView imageView;
		TextView nickname;
		RatingBar starBar;
		TextView score;
		TextView content;
		TextView time;
		
		
	}
	public void updateData(List<MovieComment> movieComments) {
		this.movieComments=movieComments;
		this.notifyDataSetChanged();
		
	}
	protected class UserSelectAction implements OnClickListener{

		int position;
		
		public UserSelectAction(int position){
			this.position=position;
		}
		@Override
		public void onClick(View v) {
			
			MovieComment movie=movieComments.get(position);
			Intent intent=new Intent(context,MovieDetailActivity.class);
			intent.putExtra("movie", movie);
			context.startActivity(intent);
			
		}
		
	}
	
	
	

	
	

	




	



	
}