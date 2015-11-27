package com.movie.ui;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpUserUpdateService;

public class MobileActivity extends BaseActivity implements OnClickListener,CallBackService {

	
	TextView title;
	TextView right;
	EditText mobile;
	ImageView clear;
	BaseService httpUserDateService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mobile);
		httpUserDateService = new HttpUserUpdateService(this);
		initViews();
		initData();
	}

	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right_text);
		mobile = (EditText) findViewById(R.id.mobile);
		clear = (ImageView) findViewById(R.id.clear);
		right.setVisibility(View.VISIBLE);

	}

	@Override
	protected void initEvents() {
		right.setOnClickListener(this);
		clear.setOnClickListener(this);
		
	}

	@Override
	protected void initData() {
		title.setText("修改手机");
		right.setText("保存");
		
	}

	private void modifyUser() {

		String value = mobile.getText().toString();
		if (value == null || value.isEmpty()) {
			showToask("手机不能为空");
			return;
		}
		if (value.length() != 11) {
			showToask("手机号不合法");
			return;
		}
		httpUserDateService.addParams("mobile", value);
		httpUserDateService.execute(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_text:
			modifyUser();
			break;
		case R.id.clear:
			mobile.setText("");
			break;

		}

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, UserActivity.class);
		this.startActivity(intent);
		this.finish();
	}



	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String code=map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			onBackPressed();
		}else{
			String message=map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		String message=map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
	}

	@Override
	public void OnRequest() {
		showProgressDialog("提示", "正在提交，请稍后......");		
	}


}
