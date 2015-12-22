package com.movie.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.movie.R;
import com.movie.adapter.DynamicAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.app.Constant.ReturnCode;
import com.movie.client.bean.Feed;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpDynamicQueryService;
import com.movie.state.DynamicContentType;
import com.movie.util.SiteConvert;
import com.movie.view.LoadView;


public class DynamicQueryActivity extends BaseActivity implements
		OnClickListener, CallBackService, OnRefreshListener2<ListView> {

	
	DynamicAdapter dynamicAdapter;
	PullToRefreshListView refreshViewLayout;
	BaseService httpDynamicService;
	List<Feed> feeds = new ArrayList<Feed>();
	View rootView;
	LoadView loadView;
	TextView title;
	String memberId;
	int page;
	User user;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(rootView==null){  
	        rootView=getLayoutInflater().inflate(R.layout.activity_dynamic_query,null);  
	    }  
		loadView = new LoadView();
		setContentView(rootView);
		httpDynamicService = new HttpDynamicQueryService(this);
		initViews();
		initData();
		initEvents();
		
	}

	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		dynamicAdapter = new DynamicAdapter(this,mHandler, feeds);
		refreshViewLayout = (PullToRefreshListView)findViewById(R.id.dynamic_list);
		refreshViewLayout.setMode(Mode.BOTH);
		refreshViewLayout.setAdapter(dynamicAdapter);	
		refreshViewLayout.setEmptyView(rootView.findViewById(R.id.empty));
		loadView.initView(rootView);
	}

	@Override
	protected void initEvents() {
		refreshViewLayout.setOnRefreshListener(this);
		refreshViewLayout.setRefreshing(true);
	}

	@Override
	protected void initData() {
		title.setText("历史动态");
		user = (User) getIntent().getSerializableExtra("user");
		memberId = getIntent().getStringExtra("memberId");
		dynamicAdapter.setUser(user);
		
	}
	
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

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	protected void loadFeeds() {
		if(null!=memberId){
			httpDynamicService.addParams("memberId", memberId);
		}
		httpDynamicService.addParams("page", page);
		httpDynamicService.addParams("size", Page.DEFAULT_SIZE);
		httpDynamicService.execute(this);
		
		
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		this.finish();
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
			}
			
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
	public void OnRequest() {
		if(!isLoad){
			loadView.showLoading(this);
			isLoad=true;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dynamicAdapter = null;
		feeds.clear();
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
