package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.client.bean.Movie;
import com.movie.ui.MovieDetailActivity;
import com.movie.util.ImageLoaderCache;

public class MoviesAdapter extends BaseAdapter {
	

	List<Movie> movies;
	Context context;
	LayoutInflater inflater;
	ImageLoaderCache imageLoader;
	
	
	public MoviesAdapter(Context context,List<Movie> movies) {
		this.context = context;
		this.movies = movies;
		imageLoader=new ImageLoaderCache(context);
		inflater = LayoutInflater.from(context);
		initData();
	}
	protected void initData(){
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return movies == null ? 0 : movies.size();
	}
	@Override
	public Movie getItem(int position) {
		// TODO Auto-generated method stub
		if (movies != null && movies.size() != 0) {
			return movies.get(position);
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
			view = inflater.inflate(R.layout.movie_item, null);
			mHolder = new ViewHolder();
			mHolder.movieView=(RelativeLayout)view.findViewById(R.id.movie_view);
			mHolder.movieImage= (ImageView)view.findViewById(R.id.movie_image);
			mHolder.mentText = (TextView)view.findViewById(R.id.movie_ment);
			mHolder.titleText = (TextView)view.findViewById(R.id.movie_title);
			mHolder.startBar = (RatingBar)view.findViewById(R.id.movie_star);
			mHolder.scoreText = (TextView)view.findViewById(R.id.movie_score);
			mHolder.movieMent = (TextView)view.findViewById(R.id.movie_ment);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		//获取position对应的数据
		Movie movie = getItem(position);
		if(movie!=null){
			imageLoader.DisplayImage(movie.getIcon(), mHolder.movieImage);
			mHolder.mentText.setText(String.valueOf(movie.getMiss()));
			mHolder.titleText.setText(movie.getName());
			mHolder.startBar.setRating(movie.getStart());
			mHolder.scoreText.setText(String.valueOf(movie.getScore()));
			mHolder.movieMent.setText(String.valueOf(movie.getTryst()));
			
		}
		mHolder.movieView.setOnClickListener(new UserSelectAction(position));
		return view;
	}
	static class ViewHolder {
		
		RelativeLayout movieView;
		ImageView movieImage;
		TextView mentText;
		TextView movieMent;
		TextView titleText;
		RatingBar startBar;
		TextView scoreText;
		
		
	}
	public void updateData(List<Movie> movies) {
		this.movies=movies;
		this.notifyDataSetChanged();
		
	}
	protected class UserSelectAction implements OnClickListener{

		int position;
		
		public UserSelectAction(int position){
			this.position=position;
		}
		@Override
		public void onClick(View v) {
			
			Movie movie=movies.get(position);
			Intent intent=new Intent(context,MovieDetailActivity.class);
			intent.putExtra("movie", movie);
			context.startActivity(intent);
			
		}
		
	}
	
	
	

	
	

	




	



	
}
