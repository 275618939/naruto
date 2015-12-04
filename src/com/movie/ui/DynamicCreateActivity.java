package com.movie.ui;

import java.util.Map;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.UserPhotoGridAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.client.service.CallBackService;

public class DynamicCreateActivity extends BaseActivity implements
		OnClickListener, CallBackService {

	TextView title;
	TextView right;
	TextView content;
	GridView photoGridview;
	UserPhotoGridAdapter photoGridAdapter;
	View rootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dynamic_create);
		if(rootView==null){  
	        rootView=getLayoutInflater().inflate(R.layout.activity_dynamic_create,null);  
	    }  
		setContentView(rootView);
		initViews();
		initEvents();
		initData();
	}

	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right_text);
		content = (TextView) findViewById(R.id.dynamic_content);
		photoGridview = (GridView) findViewById(R.id.userPhotoGridview);
		photoGridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		photoGridAdapter = new UserPhotoGridAdapter(this, mHandler, rootView);
		photoGridAdapter.update();
		photoGridview.setAdapter(photoGridAdapter);
		photoGridview.setOnItemClickListener(photoGridAdapter);
		right.setVisibility(View.VISIBLE);
	}

	@Override
	protected void initEvents() {
		right.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		title.setText("创建动态");
		right.setText("发送");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		}

	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			default:
				break;

			}
		};
	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {

		hideProgressDialog();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {

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
		showProgressDialog("提示", "请稍后......");

	}

}
