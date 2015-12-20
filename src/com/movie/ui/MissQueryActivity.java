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
import com.movie.adapter.MissQueryAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.app.Constant.ReturnCode;
import com.movie.client.bean.Miss;
import com.movie.client.bean.Movie;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpMissCancelService;
import com.movie.network.HttpMissQueryService;
import com.movie.view.LoadView;

public class MissQueryActivity extends BaseActivity implements OnClickListener,CallBackService, OnRefreshListener2<ListView> {

	LoadView loadView;
	TextView title;
	ListView myMissList;
	View rootView;
	MissQueryAdapter missQueryAdapter;
	BaseService missQueryService;
	BaseService httpMissCancelService;
	PullToRefreshListView refreshableListView;
	List<Miss> misses = new ArrayList<Miss>();
	int page;
	int missType;
	Object queryCondition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (rootView == null) {
			rootView = getLayoutInflater().inflate(R.layout.activity_miss_query, null);
		}
		loadView = new LoadView();
		setContentView(rootView);
		missQueryService = new HttpMissQueryService(this);
		httpMissCancelService = new HttpMissCancelService(this);
		initViews();
		initData();
		initEvents();
		
	}
	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		missQueryAdapter = new MissQueryAdapter(this, mHandler, misses);
		refreshableListView = (PullToRefreshListView) findViewById(R.id.miss_list);
		refreshableListView.setMode(Mode.BOTH);
		refreshableListView.setAdapter(missQueryAdapter);
		refreshableListView.setEmptyView(findViewById(R.id.empty));
		loadView.initView(rootView);
	}

	@Override
	protected void initEvents() {
		refreshableListView.setOnRefreshListener(this);		
		refreshableListView.setRefreshing(true);
	}
	@Override
	protected void initData() {
		missType = getIntent().getIntExtra(Miss.MISS_KEY, Miss.MY_MISS);
		queryCondition=getIntent().getSerializableExtra(Miss.CONDITION_KEY);
	}

	private void loadMissData() {
		switch (missType) {
		case Miss.MY_MISS:
			title.setText("发起的约会");
			missQueryService.addUrls(Constant.Miss_Query_API_URL);
			missQueryService.addParams("page", page);
			missQueryService.addParams("size", Page.DEFAULT_SIZE);
			missQueryService.execute(this);
			break;
		case Miss.MY_TOUCH:
			title.setText("参与的约会");
			missQueryService.addUrls(Constant.Miss_Touch_Query_API_URL);
			missQueryService.addParams("page", page);
			missQueryService.addParams("size", Page.DEFAULT_SIZE);
			missQueryService.execute(this);
			break;
		case Miss.USER_INVITATION:
			if(null!=queryCondition){
				User user=(User)queryCondition;
				title.setText(user.getNickname()+"正在进行的约会");
				missQueryService.addUrls(Constant.Miss_Query_API_URL);
				missQueryService.addParams("id", user.getMemberId());
				missQueryService.addParams("page", page);
				missQueryService.addParams("size", Page.DEFAULT_SIZE);
				missQueryService.execute(this);
			}
			break;
		case Miss.MOVIE_INVITATION:
			if(null!=queryCondition){
				Movie movie=(Movie)queryCondition;
				title.setText(movie.getName()+"正在进行的约会");
				missQueryService.addUrls(Constant.Miss_Film_Query_API_URL);
				missQueryService.addParams("id", movie.getId());
				missQueryService.addParams("page", page);
				missQueryService.addParams("size", Page.DEFAULT_SIZE);
				missQueryService.execute(this);
			}
			break;
		default:
			break;
		}

	}

	private void cancelMiss() {
		httpMissCancelService.execute(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loading_error:
			misses.clear();
			loadMissData();
			break;
		default:
			break;

		}

	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
		
			case Miss.CANCLE_MISS:
				cancelMiss();
			default:
				break;

			}
		};
	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		this.finish();
	}
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
					if (missMap.containsKey("portrait"))
						miss.setIcon(Constant.SERVER_ADRESS+missMap.get("portrait").toString());
					if (missMap.containsKey("nickname"))
						miss.setNickName(missMap.get("nickname").toString());
					if (missMap.containsKey("filmId"))
						miss.setFilmId(Integer.parseInt(missMap.get("filmId").toString()));
					if (missMap.containsKey("filmName"))
						miss.setFilmName(missMap.get("filmName")==null?"":missMap.get("filmName").toString());
					if (missMap.containsKey("runTime"))
						miss.setRunTime(missMap.get("runTime").toString());
					if (missMap.containsKey("coin"))
						miss.setCoin(Integer.parseInt(missMap.get("coin").toString()));
					if (missMap.containsKey("cinemaId"))
						miss.setCinemaId(missMap.get("cinemaId").toString());
					if (missMap.containsKey("cinemaName"))
						miss.setCinameName(missMap.get("cinemaName")==null?"":missMap.get("cinemaName").toString());
					if (missMap.containsKey("status"))
						miss.setStatus(Integer.parseInt(missMap.get("status").toString()));
					misses.add(miss);
				}
			
				missQueryAdapter.notifyDataSetChanged();

			}
		} else {
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}

	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {

		refreshableListView.onRefreshComplete();
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
		//showProgressDialog("提示", "请稍后......");
		loadView.showLoading(this);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		missQueryAdapter=null;
	}
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		page = 0;
		misses.clear();
		loadMissData();
		
	}
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		page = 1;
		loadMissData();
		
	}

	
}
