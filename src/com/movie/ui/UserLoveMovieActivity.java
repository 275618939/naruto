package com.movie.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.MoviesAdapter;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.client.bean.Movie;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpUserFilmTypeService;
import com.movie.util.ImageLoaderCache;
import com.movie.view.RefreshableView;
import com.movie.view.RefreshableView.PullToRefreshListener;

public class UserLoveMovieActivity extends BaseActivity implements
		OnClickListener, CallBackService, PullToRefreshListener {

	public static final int REFRESH_COMPLETE = 0X110;
	TextView title;
	GridView moviesView;
	RefreshableView refreshLayout;
	ImageLoaderCache loaderCache;
	BaseService httpUserFilmService;
	MoviesAdapter moviesAdapter;
	List<Movie> movies = new ArrayList<Movie>();
	User user;
	int page;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_love_movie);
		httpUserFilmService = new HttpUserFilmTypeService(this);
		loaderCache = new ImageLoaderCache(this);
		initViews();
		initData();
	}

	private void initViews() {

		title = (TextView) findViewById(R.id.title);
		moviesView = (GridView)findViewById(R.id.movies);
		refreshLayout = (RefreshableView) findViewById(R.id.refresh_movies);
		moviesAdapter = new MoviesAdapter(this, movies);
		refreshLayout.setOnRefreshListener(this, 0);
		moviesView.setAdapter(moviesAdapter);
		moviesView.setSelector(new ColorDrawable(Color.TRANSPARENT));

	}

	private void initData() {
		page = 0;
		title.setText("参与会员");
		user = (User) getIntent().getSerializableExtra("user");
		if (null != user) {
			loadMovies();
		}
		

	}

	private void loadMovies() {
		httpUserFilmService.addParams("memberId", user.getMemberId());
		httpUserFilmService.addParams("page", page);
		httpUserFilmService.addParams("size", Page.DEFAULT_SIZE);
		httpUserFilmService.execute(this);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				loadMovies();
				break;
			
			default:
				break;

			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;

		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		hideProgressDialog();
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
					if (movieMap.containsKey("types")) {
						movie.setTypes((List<Integer>)movieMap.get("types"));
					}
					if (movieMap.containsKey("scenarists")) {
						movie.setScenarists((List<String>)movieMap.get("scenarists"));
					}
					movies.add(movie);
				}
			
				if (size >= Page.DEFAULT_SIZE) {
					page++;
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
		hideProgressDialog();
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
	}

	@Override
	public void OnRequest() {
		showProgressDialog("提示", "请稍后......");

	}

	@Override
	public void onRefresh() {
		mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);

	}

}
