package com.movie.ui;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.movie.R;
import com.movie.adapter.PoiAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.NarutoApplication;
import com.movie.pop.AddressPopupWindow;


public class CinemaSearchActivity extends BaseActivity implements
	OnGetPoiSearchResultListener, OnGetSuggestionResultListener,OnClickListener {

	final int InitSerach=0x01;
	final static String DefaultSearchKey="影院";
	PoiSearch mPoiSearch;
	SuggestionSearch mSuggestionSearch;
	BaiduMap mBaiduMap;
	AutoCompleteTextView keyWorldsView;
	ArrayAdapter<String> sugAdapter;
	boolean isFirstLoc = true;// 是否首次定位
	int load_Index = 0;
	String dateTime;
	String cointInfo;
    View parentView;
	TextView right;
	PoiAdapter poiAdapter;
	AddressPopupWindow addressPopupWindow;
	
	MyLocationListenner myListener = new MyLocationListenner();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		SDKInitializer.initialize(getApplicationContext());
		super.onCreate(savedInstanceState);
		parentView = getLayoutInflater().inflate(R.layout.activity_cinema_search, null);
		addressPopupWindow=new AddressPopupWindow(this,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		setContentView(parentView);
		initViews();
		initEvents();
		initData();

	}
	@Override
	protected void initViews() {
		right = (TextView) findViewById(R.id.right_text);
		keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		mBaiduMap = ((SupportMapFragment) (getSupportFragmentManager().findFragmentById(R.id.map))).getBaiduMap();
		sugAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line);
		keyWorldsView.setThreshold(1);
		keyWorldsView.setAdapter(sugAdapter);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		poiAdapter =new PoiAdapter(this, null);	
		addressPopupWindow.setAdapter(poiAdapter);
		right.setVisibility(View.VISIBLE);
	
		
	}
	@Override
	protected void initEvents() {
		right.setOnClickListener(this);
		/**
		 * 当输入关键字变化时，动态更新建议列表
		 */
		keyWorldsView.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.length() <= 0) {
					return;
				}
				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(cs.toString()).city(NarutoApplication.city));
			}
		});
	}
	@Override
	protected void initData() {
		dateTime = getIntent().getStringExtra("dateTime");
		cointInfo = getIntent().getStringExtra("cointInfo");
		right.setText("搜索");
		poiAdapter.setDateTime(dateTime);
		poiAdapter.setCointInfo(cointInfo);
		keyWorldsView.setText(DefaultSearchKey);
		mHandler.postDelayed(new Runnable(){    
		    public void run() {
		    	searchButtonProcess(null);
		    }},1000);
	}
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case InitSerach:
				searchButtonProcess(null);
				break;
			default:
				break;

			}
		};
	};
	private void searchButtonProcess(View v) {	
		mPoiSearch.searchInCity((new PoiCitySearchOption()).city(NarutoApplication.city).keyword(keyWorldsView.getText().toString()).pageNum(load_Index));
	}
	@Override
	protected void onDestroy() {
		// 关闭定位图层
		super.onDestroy();
		mBaiduMap.setMyLocationEnabled(false);
		mPoiSearch.destroy();
		mSuggestionSearch.destroy();
		sugAdapter=null;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		sugAdapter.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			if (info.key != null)
				sugAdapter.add(info.key);
		}
		sugAdapter.notifyDataSetChanged();
		
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			showToask("抱歉，未找到结果");
		} else {
			showToask(result.getName() + ": " + result.getAddress());
		}
		
	}

	@Override
	public void onGetPoiResult(PoiResult result) {

		if (result == null|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			showToask("未找到结果");
			return;
		}
		
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			mBaiduMap.clear();
			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();
			addressPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
			poiAdapter.updateData(result.getAllPoi());	
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
			String strInfo = "在";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "找到结果";
			showToask(strInfo);
		}
		
	
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.right_text:
				searchButtonProcess(v);
				break;		
			case R.id.parent:
				addressPopupWindow.dismiss();
				break;		
			default:
				break;
		}
		
	}
	
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
         
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}
	

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			// if (poi.hasCaterDetails) {
				mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poi.uid));
			// }
			return true;
		}
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		this.finish();
	}
	
	
	
}
