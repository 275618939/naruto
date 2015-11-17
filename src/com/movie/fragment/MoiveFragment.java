package com.movie.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.movie.R;
import com.movie.adapter.MoviesAdapter;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.client.bean.Movie;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpPlayMovieService;
import com.movie.network.HttpUPComingMovieService;
import com.movie.view.RefreshableView;
import com.movie.view.RefreshableView.PullToRefreshListener;

public class MoiveFragment extends Fragment implements CallBackService,
		OnClickListener, PullToRefreshListener {

	static final int REFRESH_COMPLETE = 0X110;
	protected BaseService sessionService;
	RadioButton playButton;
	RadioButton upComingButton;
	RadioGroup movieClassGroup;
	MoviesAdapter moviesAdapter;
	BaseService playMovieService;
	BaseService upComingMovieService;
	GridView moviesView;
	// SwipeRefreshLayout refreshLayout;
	RefreshableView refreshLayout;
	int playPage;
	int upPage;
	List<Movie> movies = new ArrayList<Movie>();
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		View titleView = getActivity().findViewById(R.id.main_head);
		if(null!=titleView){
			titleView.setVisibility(View.VISIBLE);
		}
		view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_movie, null);
		movies.clear();
		playMovieService = new HttpPlayMovieService(getActivity());
		upComingMovieService = new HttpUPComingMovieService(getActivity());
		initView(view);
		loadPlayingMovie();
		return view;
	}

	protected void initView(View view) {
		playPage = 0;
		upPage = 0;
		movieClassGroup = (RadioGroup) view.findViewById(R.id.movie_class);
		playButton = (RadioButton) view.findViewById(R.id.play_movie);
		upComingButton = (RadioButton) view.findViewById(R.id.upComing_movie);
		moviesView = (GridView) view.findViewById(R.id.movies);
		refreshLayout = (RefreshableView) view.findViewById(R.id.refresh_movies);
		moviesAdapter = new MoviesAdapter(getActivity(), movies);
		refreshLayout.setOnRefreshListener(this, 0);
		moviesView.setAdapter(moviesAdapter);
		moviesView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		playButton.setOnClickListener(this);
		upComingButton.setOnClickListener(this);
	}

	protected void loadPlayingMovie() {
		playMovieService.addParams("page", playPage);
		playMovieService.addParams("size", Page.DEFAULT_SIZE);
		playMovieService.execute(this);
	}

	protected void loadUpcomingMovie() {
		playMovieService.addParams("page", upPage);
		playMovieService.addParams("size", Page.DEFAULT_SIZE);
		upComingMovieService.execute(this);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				int id = movieClassGroup.getCheckedRadioButtonId();
				switch (id) {
				case R.id.play_movie:
					upPage = 0;
					Log.i("MoiveFragment", "play_movie");
					movies.clear();
					loadPlayingMovie();
					break;
				case R.id.upComing_movie:
					playPage = 0;
					movies.clear();
					Log.i("MoiveFragment", "upComing_movie");
					loadUpcomingMovie();
					break;

				}

			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play_movie:
			Log.i("MoiveFragment", "play_movie");
			loadPlayingMovie();
			break;
		case R.id.upComing_movie:
			Log.i("MoiveFragment", "upComing_movie");
			loadUpcomingMovie();
			break;
		}

	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		refreshLayout.finishRefreshing();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		movies.clear();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
			List<HashMap<String, Object>> datas = (ArrayList<HashMap<String, Object>>) map.get(Constant.ReturnCode.RETURN_VALUE);
			if (datas != null && datas.size() > 0) {
				Movie movie = null;
				int size = datas.size();
				HashMap<String, Object> movieMap = null;
				for (int i = 0; i < size; i++) {
					movie = new Movie();
					movieMap = datas.get(i);
					if (movieMap.containsKey("filmId"))
						movie.setId(Integer.parseInt(movieMap.get("filmId").toString()));
					if (movieMap.containsKey("name"))
						movie.setName(movieMap.get("name").toString());
					if (movieMap.containsKey("briefing"))
						movie.setBriefing(movieMap.get("briefing").toString());
					if (movieMap.containsKey("icon"))
						movie.setIcon(movieMap.get("icon").toString());
					if (movieMap.containsKey("start"))
						movie.setPlayTime(movieMap.get("start").toString());
					if (movieMap.containsKey("interval"))
						movie.setInterval(Integer.parseInt(movieMap.get("interval").toString()));
					if (movieMap.containsKey("directors")) {
						movie.setDirectors((List<String>)movieMap.get("directors"));
					}
					if (movieMap.containsKey("stars")) {
						movie.setStars((List<String>)movieMap.get("stars"));
					}
					if (movieMap.containsKey("tryst")) {
						movie.setTryst(Integer.parseInt(movieMap.get("tryst").toString()));
					}
					if (movieMap.containsKey("types")) {
						movie.setTypes((List<Integer>)movieMap.get("types"));
					}
					if (movieMap.containsKey("scenarists")) {
						movie.setScenarists((List<String>)movieMap.get("scenarists"));
					}
					movies.add(movie);
				}
			
				if (size >= Page.DEFAULT_SIZE) {
					if(tag.equals(playMovieService.TAG)){
						playPage++;
					}else if(tag.equals(upComingMovieService.TAG)){
						upPage++;
					}
				}

			} else {
				int len = Movie.movies.length;
				for (int i = 0; i < len; i++) {
					movies.add(Movie.movies[i]);
				}
			}
		}else{
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
		moviesAdapter.updateData(movies);
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		refreshLayout.finishRefreshing();
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
	}

	@Override
	public void OnRequest() {
		showToask("加载影片信息");
	}

	protected void showToask(String hint) {
		Toast toast = Toast.makeText(getActivity(), hint, Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public void onRefresh() {
		mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);
	}

	/*
	 * @Override public void onRefresh() {
	 * mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000); }
	 */
}
