package com.movie.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.movie.R;
import com.movie.adapter.MissTreatCoinAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.Constant.ReturnCode;
import com.movie.client.bean.Miss;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpMissQueryService;
import com.movie.network.HttpMissTreatCoinService;
import com.movie.view.LoadView;

public class MissTreatCoinActivity extends BaseActivity implements OnClickListener,CallBackService, OnRefreshListener<ListView> {

	LoadView loadView;
	TextView title;
	TextView rightBtn;
	ListView myMissList;
	View rootView;
	MissTreatCoinAdapter treatCoinAdapter;
	BaseService missQueryService;
	BaseService missTreatCoinService;
	PullToRefreshListView refreshableListView;
	List<Miss> misses = new ArrayList<Miss>();
	int page;
	String trystId;
	int coin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (rootView == null) {
			rootView = getLayoutInflater().inflate(R.layout.activity_attend_coin_query, null);
		}
		loadView = new LoadView();
		setContentView(rootView);
		missQueryService = new HttpMissQueryService(this);
		missTreatCoinService = new HttpMissTreatCoinService(this);
		initViews();
		initData();
		initEvents();
		
		
		
	}
	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		treatCoinAdapter = new MissTreatCoinAdapter(this, mHandler, misses);
		refreshableListView = (PullToRefreshListView) findViewById(R.id.miss_list);
		refreshableListView.setMode(Mode.PULL_FROM_START);
		refreshableListView.setAdapter(treatCoinAdapter);
		refreshableListView.setEmptyView(findViewById(R.id.empty));
		rightBtn = (TextView) findViewById(R.id.right_text);
		rightBtn.setVisibility(View.VISIBLE);	
		loadView.initView(rootView);
	}

	@Override
	protected void initEvents() {
		refreshableListView.setOnRefreshListener(this);		
		refreshableListView.setRefreshing(true);
		rightBtn.setOnClickListener(this);
	}
	@Override
	protected void initData() {
		trystId=getIntent().getStringExtra("trystId");
		coin=getIntent().getIntExtra("coin", 0);
		title.setText("可派发影币("+coin+")");
		rightBtn.setText("确定");
	
	}

	private void loadMissData() {
		missQueryService.addUrls(Constant.Miss_Attend_Query_API_URL);
		missQueryService.addParams("id", trystId);
		missQueryService.execute(this);
	}
	private void treatCoin(){
		
		List<Map<String, Object>> treats = new ArrayList<Map<String,Object>>();
		int count = misses.size();
		EditText coinView = null;
		int sumCoin=0;
		LinearLayout linearLayout = null;
		Map<String, Object> coins=null;
		for(int i = 0; i < count; i++){  
	        linearLayout =(LinearLayout) treatCoinAdapter.getView(i, null, null);
	        coinView = (EditText) linearLayout.findViewById(R.id.coin);
	        String memberId = coinView.getTag().toString();
	        String coin = treatCoinAdapter.getCoins().get(memberId);
	        if(null!=coin&&!coin.isEmpty()){
	        	sumCoin+=Integer.parseInt(coin);
	        	coins=new HashMap<String, Object>();
	        	coins.put("memberId", memberId);
	        	coins.put("value", Integer.parseInt(coin));
	        	treats.add(coins);
	        }
	    }  
		if(sumCoin>coin){
			showToask("超出可派影币范围!");
			return;
		}else if(sumCoin<coin){
			showToask("请派出所有影币!");
			return;
		}
		missTreatCoinService.addParams("value", treats);
		missTreatCoinService.execute(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loading_error:
			misses.clear();
			loadMissData();
			break;
		case R.id.right_text:
			treatCoin();
			break;
		default:
			break;

		}

	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
		
		
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
					else
						continue;
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
			
				treatCoinAdapter.notifyDataSetChanged();

			}else if(tag.equals(missTreatCoinService.TAG)){
				onBackPressed();
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
		treatCoinAdapter=null;
	}
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		misses.clear();
		loadMissData();		
	}
	
	
}
