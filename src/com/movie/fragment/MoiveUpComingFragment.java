package com.movie.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.movie.R;
import com.movie.adapter.MoviesAdapter;
import com.movie.app.BaseFragment;
import com.movie.app.Constant;
import com.movie.app.Constant.ReturnCode;
import com.movie.client.bean.Miss;
import com.movie.client.bean.Movie;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpUPComingMovieService;

public class MoiveUpComingFragment extends BaseFragment implements CallBackService,
		OnClickListener, OnRefreshListener<GridView>  {

	MoviesAdapter moviesAdapter;
	BaseService upComingMovieService;
	PullToRefreshGridView refreshViewLayout;
	List<Movie> movies = new ArrayList<Movie>();
	public MoiveUpComingFragment() {
		super();
		
	}
	public MoiveUpComingFragment(Activity activity,Context context) {
		super(activity, context);
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		upComingMovieService = new HttpUPComingMovieService(getActivity());
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		if(rootView==null){  
	         rootView=inflater.inflate(R.layout.fragment_movie_upcoming,container,false);  
	    }  
		ViewGroup parent = (ViewGroup) rootView.getParent();  
	    if (parent != null) {  
	        parent.removeView(rootView);  
	    }   
	    loadView.initView(rootView);
		isPrepared=true;
		return super.onCreateView(inflater, container, savedInstanceState);
		
	}
	@Override
	protected void initViews() {
	
		moviesAdapter = new MoviesAdapter(getActivity(), null,movies);
		refreshViewLayout = (PullToRefreshGridView) rootView.findViewById(R.id.moive_list);
		refreshViewLayout.setAdapter(moviesAdapter);	
		refreshViewLayout.setEmptyView(rootView.findViewById(R.id.empty));
		int missType=getActivity().getIntent().getIntExtra(Miss.MISS_KEY, 0);
		moviesAdapter.setMissType(missType);
	}
	@Override
	protected void initEvents() {
		refreshViewLayout.setOnRefreshListener(this);
	}
	@Override
	protected void lazyLoad() {
		if (!isVisible||!isPrepared||isLoad) {
			return;
		}
		loadUpcomingMovie();
	}

	protected void loadUpcomingMovie() {
		movies.clear();
		upComingMovieService.execute(this);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			
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
		refreshViewLayout.onRefreshComplete();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
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
			}
		} else {
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
		map=null;
	
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		refreshViewLayout.onRefreshComplete();
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		String state= map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if(state.equals(ReturnCode.STATE_999)){
			loadView.showLoadLineFail(this);
		}else{
			loadView.showLoadFail(this, this);
			showToask(message);
		}
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		moviesAdapter = null;
		movies.clear();
	}
	@Override
	public void OnRequest() {
		if(!isLoad){
			loadView.showLoading(this);
			isLoad=true;
		}
	}
	@Override
	public void onRefresh(PullToRefreshBase<GridView> refreshView) {
		loadUpcomingMovie();
		
	}
	@Override
	protected void destroyData() {
		// TODO Auto-generated method stub
		
	}

}
