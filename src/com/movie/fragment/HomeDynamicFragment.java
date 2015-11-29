package com.movie.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
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
import com.movie.adapter.DynamicAdapter;
import com.movie.app.BaseFragment;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.app.Constant.ReturnCode;
import com.movie.client.bean.Feed;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.common.service.LocationService;
import com.movie.network.HttpNearService;
import com.movie.ui.LoginActivity;

public class HomeDynamicFragment extends BaseFragment implements CallBackService,
		OnClickListener, OnRefreshListener2<ListView>  {
	
	DynamicAdapter dynamicAdapter;
	PullToRefreshListView refreshViewLayout;
	LocationService locationService;
	BaseService httpNearService;
	List<Feed> feeds = new ArrayList<Feed>();
	public HomeDynamicFragment() {
		super();		
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		locationService = new LocationService(getActivity());
		httpNearService = new HttpNearService(getActivity());
		locationService.initLocation();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if(rootView==null){  
	        rootView=inflater.inflate(R.layout.fragment_home_dynamic,container,false);  
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
		dynamicAdapter = new DynamicAdapter(getActivity(),mHandler, feeds);
		refreshViewLayout = (PullToRefreshListView) rootView.findViewById(R.id.dynamic_list);
		refreshViewLayout.setMode(Mode.PULL_FROM_START);
		refreshViewLayout.setAdapter(dynamicAdapter);	

	}
	@Override
	protected void initEvents() {
		refreshViewLayout.setOnRefreshListener(this);
		
	}
	@Override
	protected void lazyLoad() {
		
		if (!isVisible||!isPrepared) {
			return;
		}		
		loadFeeds();
	}
	protected void loadFeeds() {
		httpNearService.addParams("distance", Page.MAX_DISTANCE);
		locationService.start(httpNearService,this);
	}
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case User.USER_LOVE:
					Bundle bundle = msg.getData();
					String memberId =  bundle.getString("memberId");
					
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
			loadFeeds();
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
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if(tag.equals(httpNearService.TAG)){
				
			}
			
		} else if (Constant.ReturnCode.STATE_3.equals(code)) {
			//提示用户登陆
			Intent loginIntent = new Intent(getActivity(),LoginActivity.class);
			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			startActivity(loginIntent);
		}else {
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		refreshViewLayout.onRefreshComplete();
		String state= map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if(state.equals(ReturnCode.STATE_999)){
			loadView.showLoadLineFail(this);
		}else{
			loadView.showLoadFail(this, this);
		}
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
		

	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		dynamicAdapter = null;
		locationService.stop();
		feeds.clear();
	}
	@Override
	public void OnRequest() {
		loadView.showLoading(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		page = 0;
		feeds.clear();
		loadFeeds();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		page = 1;
		loadFeeds();
	}


}
