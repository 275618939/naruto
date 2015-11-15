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
import com.movie.client.service.CallBackService;

public class EmailActivity extends BaseActivity implements OnClickListener,CallBackService {


	TextView title;
	TextView right;
	EditText email;
	ImageView clear;

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email);
		initViews();
		initData();
	}

	private void initViews() {

		title = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right_text);
		email = (EditText) findViewById(R.id.email);
		clear = (ImageView) findViewById(R.id.clear);
		
		//clear.setClickable(true);
		//right.setClickable(true);
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);
		clear.setOnClickListener(this);
	}

	private void initData() {
		title.setText("修改邮箱");
		right.setText("保存");
	}

	private void modifyUser() {

		String value = email.getText().toString();
		if (value == null || value.isEmpty()) {
			showToask("邮箱不能为空");
			return;
		}
		if (!value.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+") || value.length() < 0)
        {
			showToask("邮箱不合法");
			return;
        }

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_text:
			modifyUser();
			break;
		case R.id.clear:
			email.setText("");
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnRequest() {
		showProgressDialog("提示", "正在提交，请稍后......");
		
	}



}
