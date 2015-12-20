package com.movie.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.movie.R;
import com.movie.adapter.MissSelfQueryAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.app.Constant.ReturnCode;
import com.movie.client.bean.Miss;
import com.movie.client.bean.Movie;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpMissInviteService;
import com.movie.network.HttpMissQueryService;
import com.movie.state.MissState;
import com.movie.view.LoadView;

public class MissSelfQueryActivity extends BaseActivity implements OnClickListener,CallBackService, OnRefreshListener2<ListView> {

	LoadView loadView;
	TextView title;
	ListView myMissList;
	TextView missBtn;
	LinearLayout missBarLayout;
	RelativeLayout userMissParentLayout;
	MissSelfQueryAdapter selfQueryAdapter;
	BaseService missQueryService;
	BaseService missInviteService;
	PullToRefreshListView refreshableListView;
	List<Miss> misses = new ArrayList<Miss>();
	int page;
	int missType;
	String memberId;
	Object queryCondition;
	View rootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(rootView==null){  
	        rootView=getLayoutInflater().inflate(R.layout.activity_miss_self_query,null);  
	    }  
		loadView=new LoadView();
		setContentView(rootView);
		missQueryService = new HttpMissQueryService(this);
		missInviteService =new HttpMissInviteService(this);
		initData();
		initViews();
		initEvents();
		
	}
	@Override
	protected void initViews() {
		loadView.initView(rootView);
		title = (TextView) findViewById(R.id.title);
		missBtn = (TextView) findViewById(R.id.miss_btn);
		missBarLayout = (LinearLayout) findViewById(R.id.miss_bottom_bar);
		userMissParentLayout= (RelativeLayout)findViewById(R.id.user_miss_parent_layout);
		selfQueryAdapter = new MissSelfQueryAdapter(this, mHandler, misses);
		refreshableListView = (PullToRefreshListView) findViewById(R.id.slef_miss_list);
		refreshableListView.setMode(Mode.BOTH);
		refreshableListView.setAdapter(selfQueryAdapter);
		refreshableListView.setEmptyView(rootView.findViewById(R.id.empty));
	}

	@Override
	protected void initEvents() {
		refreshableListView.setOnRefreshListener(this);
		refreshableListView.setRefreshing(true);
	}

	@Override
	protected void initData() {
		missType=getIntent().getIntExtra(Miss.MISS_KEY, 0);
		memberId=getIntent().getStringExtra("memberId");
	
	}
	private void loadMissData() {
		switch (missType) {
		case Miss.INVITE_MISS:
			title.setText("选择一个约会");
			selfQueryAdapter.setMissType(missType);
			missQueryService.addUrls(Constant.Miss_Query_API_URL);
			missQueryService.addParams("page", page);
			missQueryService.addParams("size", Page.DEFAULT_SIZE);
			missQueryService.execute(this);
			missBtn.setText("创建一个约会");
			missBarLayout.setVisibility(View.VISIBLE);
			missBtn.setOnClickListener(this);
			break;
		case Miss.MY_MISS:
			title.setText(getResources().getString(R.string.my_miss));
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.miss_btn:
			Intent intent = new Intent(this, MovieQueryActivity.class);
			intent.putExtra(Miss.MISS_KEY, Miss.CREATE_MISS);
			startActivity(intent);
			finish();
			break;
		default:
			break;

		}

	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Miss.INVITE_MISS:
				Bundle bundle=msg.getData();
				String trystId=bundle.getString("trystId");
				int status=bundle.getInt("status");
				if(status!=MissState.HaveInHand.getState()){
					showToask("当前约会已经"+MissState.getState(status).getMessage()+",请选择其他的约会!");
					return;
				}
				inviteMiss(trystId);
				break;
			default:
				break;

			}
		};
	};
	private void inviteMiss(String trystId){
		missInviteService.addParams("trystId", trystId);
		missInviteService.addParams("memberId", memberId);
		missInviteService.execute(this);
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
		hideProgressDialog();
		refreshableListView.onRefreshComplete();
		isLoad=true;
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
					if (missMap.containsKey("cinemaId")) {
						miss.setCinemaId(missMap.get("cinemaId").toString());
					}
					if (missMap.containsKey("cinemaName")) {
						miss.setCinameName(missMap.get("cinemaName")==null?"":missMap.get("cinemaName").toString());
					}
					if (missMap.containsKey("status")) {
						miss.setStatus(Integer.parseInt(missMap.get("status").toString()));
					}
					misses.add(miss);
				}
				
				selfQueryAdapter.notifyDataSetChanged();
			}else if(tag.endsWith(missInviteService.TAG)){
				showToask("邀请成功,等待对方同意!");
			}
		} else {
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}

	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		refreshableListView.onRefreshComplete();
		hideProgressDialog();
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
		}else{
			showProgressDialog();
		}
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
	@Override
	protected void onDestroy() {
		super.onDestroy();
		selfQueryAdapter=null;
		misses.clear();
		System.gc();
	}

	

}
