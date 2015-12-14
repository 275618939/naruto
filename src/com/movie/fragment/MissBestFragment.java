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
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.movie.R;
import com.movie.adapter.MissNarutoQueryAdapter;
import com.movie.app.BaseFragment;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.app.Constant.ReturnCode;
import com.movie.client.bean.Miss;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpMissQueryService;

public class MissBestFragment extends BaseFragment implements OnClickListener,
		CallBackService, OnRefreshListener2<ListView> {

	BaseService missQueryService;
	PullToRefreshListView refreshableListView;
	MissNarutoQueryAdapter missQueryAdapter;
	List<Miss> misses = new ArrayList<Miss>();
	int page;
	public MissBestFragment() {
		super();		
	}
	public MissBestFragment(Activity activity,Context context) {
		super(activity, context);
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		missQueryService = new HttpMissQueryService(getActivity());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if(rootView==null){  
	        rootView=inflater.inflate(R.layout.fragment_miss_best_query,container,false);  
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
		missQueryAdapter = new MissNarutoQueryAdapter(getActivity(), mHandler, misses);
		refreshableListView = (PullToRefreshListView) rootView.findViewById(R.id.miss_list);
		refreshableListView.setMode(Mode.BOTH);
		refreshableListView.setAdapter(missQueryAdapter);
		refreshableListView.setEmptyView(rootView.findViewById(R.id.empty));
	}

	@Override
	protected void initEvents() {
		refreshableListView.setOnRefreshListener(this);		
		refreshableListView.setRefreshing(true);
	}

	@Override
	protected void lazyLoad() {
		if (!isVisible||!isPrepared) {
			return;
		}		
		//loadMiss();
	}
	
	private void loadMiss() {
		missQueryService.addUrls(Constant.Miss_Query_API_URL);
		missQueryService.addParams("page", page);
		missQueryService.addParams("size", Page.DEFAULT_SIZE);
		missQueryService.execute(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			
		   case R.id.loading_error:
			   misses.clear();
			   loadMiss();
			break;
	
		default:
			break;

		}
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
	public void SuccessCallBack(Map<String, Object> map) {
		loadView.showLoadAfter(this);
		refreshableListView.onRefreshComplete();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if (tag.endsWith(missQueryService.TAG)) {
				List<HashMap<String, Object>> datas = (ArrayList<HashMap<String, Object>>) map.get(Constant.ReturnCode.RETURN_VALUE);
				Miss miss = null;
				int size = datas.size();
				HashMap<String, Object> missMap = null;
				for (int i = 0; i < size; i++) {
					miss = new Miss();
					missMap = datas.get(i);
					if (missMap.containsKey("trystId"))
						miss.setTrystId(missMap.get("trystId").toString());
					if (missMap.containsKey("memberId"))
						miss.setMemberId(missMap.get("memberId").toString());
					if (missMap.containsKey("filmId"))
						miss.setFilmId(Integer.parseInt(missMap.get("filmId").toString()));
					if (missMap.containsKey("runTime"))
						miss.setRunTime(missMap.get("runTime").toString());
					if (missMap.containsKey("coin"))
						miss.setCoin(Integer.parseInt(missMap.get("coin").toString()));
					if (missMap.containsKey("cinemaId")) {

					}
					if (missMap.containsKey("attend")) {

					}
					if (missMap.containsKey("status")) {
						miss.setStatus(Integer.parseInt(missMap.get("status").toString()));
					}
					miss.setIcon("http://101.200.176.217/test.jpg");
					misses.add(miss);
				}
				if (size >= Page.DEFAULT_SIZE) {
					page++;
				}

				missQueryAdapter.notifyDataSetChanged();
				if (size <= 0) {
					tempData();
				}

			}
		} else {
			tempData();
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}

	}

	private void tempData() {
		misses=Miss.getTempData();
		missQueryAdapter.notifyDataSetChanged();
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		refreshableListView.onRefreshComplete();
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		String state= map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if(state.equals(ReturnCode.STATE_999)){
			loadView.showLoadLineFail(this);
		}else{
			loadView.showLoadFail(this, this);
			showToask(message);
		}

	}
	/* 摧毁视图 */
	@Override
	public void onDestroyView() {
	
		super.onDestroyView();
		missQueryAdapter = null;
		misses.clear();
	}
	@Override
	public void OnRequest() {
		if(!isLoad){
			loadView.showLoading(this);
			isLoad=true;
		}
		
	}
	// 下拉刷新
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		page = 0;
		misses.clear();
		loadMiss();
	}

	// 上拉Pulling Up
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		page = 1;
		loadMiss();
	}
	@Override
	protected void destroyData() {
		// TODO Auto-generated method stub
		
	}

	

}
