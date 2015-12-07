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
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.client.bean.Movie;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpUserFilmTypeService;
import com.movie.view.RefreshableView;
import com.movie.view.RefreshableView.PullToRefreshListener;

public class UserLoveMovieActivity extends BaseActivity implements
		OnClickListener, CallBackService, PullToRefreshListener {

	public static final int REFRESH_COMPLETE = 0X110;
	TextView title;
	GridView moviesView;
	RefreshableView refreshLayout;
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
		initViews();
		initEvents();
		initData();
	}

	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		moviesView = (GridView)findViewById(R.id.movies);
		refreshLayout = (RefreshableView) findViewById(R.id.refresh_movies);
		moviesAdapter = new MoviesAdapter(this,null, movies);
		moviesView.setAdapter(moviesAdapter);
		moviesView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	protected void initEvents() {
		refreshLayout.setOnRefreshListener(this, 0);
		
	}

	@Override
	protected void initData() {
		page = 0;
		title.setText("想看电影");
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
		this.finish();
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
					movie.setId(Integer.parseInt(movieMap.get("filmId").toString()));
					movie.setName(movieMap.get("filmName").toString());
					movie.setIcon(Constant.SERVER_ADRESS+movieMap.get("filmIcon").toString());
					if(movieMap.containsKey("score"))
						movie.setScore(Long.parseLong(movieMap.get("score").toString())/10);
					if(movieMap.containsKey("scoreCnt"))
						movie.setScoreCnt(Integer.parseInt(movieMap.get("scoreCnt").toString()));
					if(movieMap.containsKey("tryst"))
						movie.setTryst(Integer.parseInt(movieMap.get("tryst").toString()));
					movies.add(movie);
				}
				moviesAdapter.notifyDataSetChanged();
				if (size >= Page.DEFAULT_SIZE) {
					page++;
				}

			} 
		}else{
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
		map=null;
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		refreshLayout.finishRefreshing();
		hideProgressDialog();
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
		map=null;
	}

	@Override
	public void OnRequest() {
		showProgressDialog("提示", "请稍后......");

	}

	@Override
	public void onRefresh() {
		mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		moviesView=null;
		refreshLayout=null;
		httpUserFilmService=null;
		moviesAdapter=null;	
		movies.clear();
	}



}
