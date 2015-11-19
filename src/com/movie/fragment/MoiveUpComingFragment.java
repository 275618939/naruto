package com.movie.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.movie.R;
import com.movie.adapter.MoviesAdapter;
import com.movie.app.Constant;
import com.movie.client.bean.Movie;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpUPComingMovieService;
import com.movie.view.LoadView;

public class MoiveUpComingFragment extends Fragment implements CallBackService,
		OnClickListener, OnRefreshListener2<GridView>  {

	static final int REFRESH_COMPLETE = 0X110;
	MoviesAdapter moviesAdapter;
	BaseService upComingMovieService;
	PullToRefreshGridView refreshLayout;
	List<Movie> movies = new ArrayList<Movie>();
	View view;
	LoadView loadView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		View titleView = getActivity().findViewById(R.id.main_head);
		if(null!=titleView){
			titleView.setVisibility(View.GONE);
		}
		view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_movie_upcoming, null);
		loadView = new LoadView(view);
		upComingMovieService = new HttpUPComingMovieService(getActivity());
		initView(view);
		loadUpcomingMovie();
		return view;
	}

	protected void initView(View view) {
		moviesAdapter = new MoviesAdapter(getActivity(), movies);
		refreshLayout = (PullToRefreshGridView) view.findViewById(R.id.moive_list);
		refreshLayout.setMode(Mode.PULL_FROM_START);
		refreshLayout.setOnRefreshListener(this);
		refreshLayout.setAdapter(moviesAdapter);
	}

	protected void loadUpcomingMovie() {
		movies.clear();
		upComingMovieService.execute(this);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				loadUpcomingMovie();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.loading_error:
				loadUpcomingMovie();
				break;
			default:
				break;
		}

	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		loadView.showLoadAfter(this);
		refreshLayout.onRefreshComplete();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
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

			} else {
				int len = Movie.movies.length;
				for (int i = 0; i < len; i++) {
					movies.add(Movie.movies[i]);
				}
			}
		} else {
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE)
					.toString();
			showToask(message);
		}
		moviesAdapter.updateData(movies);
	
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		refreshLayout.onRefreshComplete();
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
		loadView.showLoadFail(this,this);
	}

	@Override
	public void OnRequest() {
		loadView.showLoading(this);
	}

	protected void showToask(String hint) {
		Toast toast = Toast.makeText(getActivity(), hint, Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		loadUpcomingMovie();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		
		
	}

}
