package com.movie.fragment;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.movie.app.NarutoApplication;
import com.movie.client.bean.Feed;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpDynamicNearQueryService;
import com.movie.state.DynamicContentType;
import com.movie.ui.LoginActivity;
import com.movie.util.SiteConvert;

public class HomeDynamicFragment extends BaseFragment implements CallBackService,
		OnClickListener, OnRefreshListener2<ListView>  {
	
	DynamicAdapter dynamicAdapter;
	PullToRefreshListView refreshViewLayout;
	BaseService httpDynamicService;
	List<Feed> feeds = new ArrayList<Feed>();
	
	public HomeDynamicFragment() {
		super();		
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);		
		httpDynamicService = new HttpDynamicNearQueryService(getActivity());
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
		refreshViewLayout.setMode(Mode.BOTH);
		refreshViewLayout.setAdapter(dynamicAdapter);	
		refreshViewLayout.setEmptyView(rootView.findViewById(R.id.empty));
	}
	@Override
	protected void initEvents() {
		refreshViewLayout.setOnRefreshListener(this);
		refreshViewLayout.setRefreshing(true);
	}
	@Override
	protected void lazyLoad() {
	
		if (!isVisible||!isPrepared||isLoad) {
			return;
		}			
	
	}
	protected void loadFeeds() {
		httpDynamicService.addParams("distance", page);
		httpDynamicService.addParams("longitude", NarutoApplication.longitude);
		httpDynamicService.addParams("latitude", NarutoApplication.latitude);
		httpDynamicService.execute(this);
		
		
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
			if (tag.equals(httpDynamicService.TAG)) {

				List<HashMap<String, Object>> datas = (ArrayList<HashMap<String, Object>>) map.get(Constant.ReturnCode.RETURN_VALUE);
				HashMap<String, Object> maps = null;
				int size = datas.size();
				Feed feed=null;
				int longitude,latitude=0;
				for (int i = 0; i < size; i++) {
					maps = datas.get(i);
					feed = new Feed();
					feed.setType(Integer.parseInt(maps.get("type").toString()));
					feed.setDynamicId(maps.get("dynamicId").toString());
					feed.setMemberId(maps.get("memberId").toString());
					feed.setTime(maps.get("time").toString());
					feed.setContent(maps.get("title").toString());
					feed.setPortrait(Constant.SERVER_ADRESS+maps.get("portrait").toString());
					feed.setName(maps.get("nickname").toString());
					longitude=Integer.parseInt(maps.get("longitude").toString());
					latitude=Integer.parseInt(maps.get("latitude").toString());
					feed.setSite(SiteConvert.GetSite(longitude, latitude));
					if(feed.getType()==DynamicContentType.Photo.getType()){
						List<String> contents = (List<String>) maps.get("content");
						feed.setContentImage(contents);
					}
					feeds.add(feed);
				}
				dynamicAdapter.notifyDataSetChanged();
				//初始化临时数据
//				refreshViewLayout.onRefreshComplete();
//				JsonResolveUtils.resolveNearbyStatus(getActivity(), feeds,"momo_p_001");
//				dynamicAdapter.notifyDataSetChanged();
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
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		int state=Integer.parseInt(map.get(Constant.ReturnCode.RETURN_STATE).toString());
		if(state==Integer.parseInt(ReturnCode.STATE_999)){
			loadView.showLoadLineFail(this);
		}else if(state>=Integer.parseInt(ReturnCode.STATE_97)){
			loadView.showLoadFail(this, this);
		}else{
			showToask(message);
		}
		

	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		dynamicAdapter = null;
		feeds.clear();
	}
	@Override
	public void OnRequest() {
		if(!isLoad){
			loadView.showLoading(this);
			isLoad=true;
		}
		
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		page = Page.MIN_DISTANCE;
		feeds.clear();
		loadFeeds();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		page = 1;
		page+=Page.MIN_DISTANCE;
		loadFeeds();
	}
	@Override
	protected void destroyData() {
		// TODO Auto-generated method stub
		
	}


}
