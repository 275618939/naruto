package com.movie.ui;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.client.bean.Movie;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpMissCreateService;
import com.movie.util.SharedPreferencesUtils;

public class MissConfirmActivity extends BaseActivity implements
		OnClickListener, CallBackService {

	TextView title;
	TextView right;
	TextView movieName;
	TextView runTime;
	TextView coin;
	TextView cinemaNameView;
	TextView cinemaAddressView;
	TextView cinemaPhoneView;
	String dateTime;
	String cointInfo;
	String cinemaName;
	String cinemaAddress;
	String cinemaPhoneNum;
	String cinameUid;
	double latitude;
	double longitude;
	Integer filmId;
	BaseService httpMissCreateService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_miss_confirm);
		httpMissCreateService = new HttpMissCreateService(this);
		initViews();
		initEvents();
		initData();
	}
	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right_text);
		runTime = (TextView) findViewById(R.id.runTime);
		coin = (TextView) findViewById(R.id.coin);
		movieName = (TextView) findViewById(R.id.movie_name);
		cinemaNameView = (TextView) findViewById(R.id.cinema_name);
		cinemaAddressView = (TextView) findViewById(R.id.cinema_address);
		cinemaPhoneView = (TextView) findViewById(R.id.cinema_phone);
		right.setVisibility(View.VISIBLE);
	}

	@Override
	protected void initEvents() {
		right.setOnClickListener(this);
	}
	@Override
	protected void initData() {
		title.setText("约会确认");
		right.setText("提交");
		dateTime = getIntent().getStringExtra("dateTime");
		cointInfo = getIntent().getStringExtra("cointInfo");
		cinemaName = getIntent().getStringExtra("cinemaName");
		cinemaAddress = getIntent().getStringExtra("cinameAddress");
		cinemaPhoneNum = getIntent().getStringExtra("cinamePhoneNum");
		cinameUid = getIntent().getStringExtra("cinameUid");
		latitude = getIntent().getDoubleExtra("latitude", 0);
		longitude = getIntent().getDoubleExtra("longitude", 0);
		runTime.setText(dateTime);
		coin.setText(String.valueOf(cointInfo));
		cinemaNameView.setText(cinemaName);
		cinemaAddressView.setText(cinemaAddress);
		cinemaPhoneView.setText(cinemaPhoneNum);
		filmId=(Integer)SharedPreferencesUtils.getParam(this,"filmId", 0);
		movieName.setText((String)SharedPreferencesUtils.getParam(this,"filmName", ""));
		
	}
	private void saveMiss() {

		httpMissCreateService.addParams("filmId", filmId.intValue());
		httpMissCreateService.addParams("runTime", dateTime);
		httpMissCreateService.addParams("coin", cointInfo);
		httpMissCreateService.addParams("cinemaId", "016fc79c22d5b3fc");
		httpMissCreateService.execute(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.runTime:

			break;
		case R.id.right_text:
			saveMiss();
			break;

		default:
			break;

		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		this.finish();
	}
	@Override
	public void SuccessCallBack(Map<String, Object> map) {

		hideProgressDialog();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		//String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		} else {
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
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

	

}
