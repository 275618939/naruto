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
import com.movie.adapter.MissNarutoAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.app.Constant.ReturnCode;
import com.movie.client.bean.Miss;
import com.movie.client.bean.MissNaruto;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpMissAgreeService;
import com.movie.network.HttpMissCancelService;
import com.movie.network.HttpMissQueryService;
import com.movie.view.LoadView;

public class HopeNarutoQueryActivity extends BaseActivity implements
		OnClickListener, CallBackService, OnRefreshListener2<ListView> {

	LoadView loadView;
	TextView title;
	MissNarutoAdapter missNarutoAdapter;
	BaseService httpMissCancelService;
	BaseService httpMissQueryService;
	BaseService httpMissAgreeService;
	PullToRefreshListView refreshableListView;
	//List<String> members=new ArrayList<String>();
	List<MissNaruto> missNarutos = new ArrayList<MissNaruto>();
	int page;
	String trystId;
	String memberId;
	String loginMemberId;
	View rootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (rootView == null) {
			rootView = getLayoutInflater().inflate(R.layout.activity_hope_naruto_query, null);
		}
		loadView = new LoadView();
		setContentView(rootView);
		httpMissQueryService = new HttpMissQueryService(this);
		httpMissCancelService = new HttpMissCancelService(this);
		httpMissAgreeService = new HttpMissAgreeService(this);
		initViews();
		initData();
		initEvents();

	}

	@Override
	protected void initViews() {
		loadView.initView(rootView);
		title = (TextView) findViewById(R.id.title);
		missNarutoAdapter = new MissNarutoAdapter(this, mHandler, missNarutos);
		refreshableListView = (PullToRefreshListView) findViewById(R.id.hope_attend_list);
		refreshableListView.setMode(Mode.BOTH);
		refreshableListView.setAdapter(missNarutoAdapter);
		refreshableListView.setEmptyView(rootView.findViewById(R.id.empty));
	}

	@Override
	protected void initEvents() {
		refreshableListView.setOnRefreshListener(this);
		refreshableListView.setRefreshing(true);
	}

	@Override
	protected void initData() {
		trystId = getIntent().getStringExtra("trystId");
		memberId = getIntent().getStringExtra("memberId");
		title.setText("参与人");
		missNarutoAdapter.setMemberId(memberId);
		loginMemberId=userService.getUserItem().getMemberId();
		missNarutoAdapter.setLoginMemberId(loginMemberId);
		

	}

	private void loadMissData() {
		httpMissQueryService.addUrls(Constant.Miss_Hope_Query_API_URL);
		httpMissQueryService.addParams("id", trystId);
		httpMissQueryService.addParams("page", page);
		httpMissQueryService.addParams("size", Page.DEFAULT_SIZE);
		httpMissQueryService.execute(this);
	}
	private void agreeMiss(String memberId){
		//members.clear();
		//members.add(memberId);
		httpMissAgreeService.addParams("trystId", trystId);
		httpMissAgreeService.addParams("memberId", memberId);
		httpMissAgreeService.execute(this);
	}
	private void cancelMiss() {
		httpMissCancelService.execute(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;

		}

	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Miss.CANCLE_MISS:
				cancelMiss();
				break;
			case Miss.AGREE_MISS:
			    Bundle data =  msg.getData();
			    String memberid=data.getString("memberid");
				agreeMiss(memberid);
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
		hideProgressDialog();
		refreshableListView.onRefreshComplete();
		isLoad=true;
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if(tag.endsWith(httpMissQueryService.TAG)){
				List<HashMap<String, Object>> datas = (ArrayList<HashMap<String, Object>>) map.get(Constant.ReturnCode.RETURN_VALUE);
				MissNaruto missNaruto = null;
				int size = datas.size();
				HashMap<String, Object> dataMap = null;
				for (int i = 0; i < size; i++) {
					missNaruto = new MissNaruto();
					dataMap = datas.get(i);
					if (dataMap.containsKey("memberId")){
						missNaruto.setMemberId(dataMap.get("memberId").toString());
					}
					if (dataMap.containsKey("portrait"))
						missNaruto.setPortrait(Constant.SERVER_ADRESS+dataMap.get("portrait").toString());
					if (dataMap.containsKey("nickname"))
						missNaruto.setNickname(dataMap.get("nickname").toString());
					if (dataMap.containsKey("birthday"))
						missNaruto.setBirthday(Integer.parseInt((dataMap.get("birthday").toString())));
					if (dataMap.containsKey("sex"))
						missNaruto.setSex(Integer.parseInt((dataMap.get("sex").toString())));
					if (dataMap.containsKey("loveCnt"))
						missNaruto.setLoveCnt(Integer.parseInt((dataMap.get("loveCnt").toString())));
					if (dataMap.containsKey("faceTtl"))
						missNaruto.setFaceTtl(Long.parseLong((dataMap.get("faceTtl").toString())));
					if (dataMap.containsKey("faceCnt"))
						missNaruto.setFaceCnt(Integer.parseInt((dataMap.get("faceCnt").toString())));
					if (dataMap.containsKey("trystCnt"))
						missNaruto.setTrystCnt(Integer.parseInt((dataMap.get("trystCnt").toString())));
					if (dataMap.containsKey("filmCnt"))
						missNaruto.setFilmCnt(Integer.parseInt((dataMap.get("filmCnt").toString())));
					if (dataMap.containsKey("stage"))
						missNaruto.setStage(Integer.parseInt((dataMap.get("stage").toString())));
					missNarutos.add(missNaruto);
				}
				missNarutoAdapter.notifyDataSetChanged();
		
			}else if(tag.endsWith(httpMissAgreeService.TAG)){
				showToask("操作成功,可在应约人中查看详情!");
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
		missNarutos.clear();
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
		missNarutoAdapter = null;
		missNarutos.clear();
		System.gc();
	}

}
