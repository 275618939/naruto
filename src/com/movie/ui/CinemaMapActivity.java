package com.movie.ui;

import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.movie.R;
import com.movie.app.Constant;
import com.movie.client.bean.Miss;
import com.movie.client.service.CallBackService;

public class CinemaMapActivity extends BaseActivity implements OnClickListener,
		CallBackService, OnGetGeoCoderResultListener {

	static final int REFRESH_COMPLETE = 0X110;
	Miss miss;
	TextView title;
	TextView right;
	BaiduMap mBaiduMap;
	MapView mMapView;
	GeoCoder mSearch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_cinema_map);
		initViews();

	}

	private void initViews() {

		title = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right_text);
		right.setVisibility(View.VISIBLE);
		right.setText("查询");
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		right.setOnClickListener(this);
		miss = (Miss) getIntent().getSerializableExtra("miss");
	}

	private void initData() {
		if (miss != null) {
			title.setText(miss.getCinameName());
			if (miss.getCinameAddress() != null && !miss.getCinameAddress().isEmpty()) {
				mSearch.geocode(new GeoCodeOption().city(miss.getCinameCity())
						.address(miss.getCinameAddress()));
			}
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				initData();
				break;
			default:
				break;

			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_text:
			initData();
			break;
		default:
			break;

		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);
	}

	@Override
	protected void onPause() {

		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();

	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		mSearch.destroy();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		hideProgressDialog();
		onBackPressed();
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
	}

	@Override
	public void OnRequest() {
		showProgressDialog("提示", "正在提交，请稍后......");
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_marka)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(this, strInfo, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_marka)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		Toast.makeText(this, result.getAddress(), Toast.LENGTH_LONG).show();

	}

}
