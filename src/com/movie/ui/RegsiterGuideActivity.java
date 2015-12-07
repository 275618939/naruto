package com.movie.ui;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpUserUpdateService;

public class RegsiterGuideActivity extends BaseActivity implements OnClickListener,
		CallBackService {

	TextView title;
	TextView right;
	EditText nickName;
	RadioGroup sexRadioGroup;
	ImageView clear;
	BaseService httpUserDateService;
	int sex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regsiter_guide);
		httpUserDateService = new HttpUserUpdateService(this);
		initViews();
		initEvents();
		initData();
	}
	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right_text);
		nickName = (EditText) findViewById(R.id.nickName);
		sexRadioGroup = (RadioGroup) findViewById(R.id.sexRadioGroup);
		clear = (ImageView) findViewById(R.id.clear);
		right.setVisibility(View.VISIBLE);
	}

	@Override
	protected void initEvents() {
		right.setOnClickListener(this);
		clear.setOnClickListener(this);
		sexRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group,
					int checkedId) {
				if (checkedId == R.id.men) {
					sex = Constant.Sex.MEN;
				} else if (checkedId == R.id.women) {
					sex = Constant.Sex.WOMEN;
				}

			}
		});
		
	}

	@Override
	protected void initData() {
		title.setText("设置基本信息");
		right.setText("保存");
	}

	private void modifyUser() {

		String value = nickName.getText().toString();
		if (value == null || value.isEmpty()) {
			showToask("昵称不能为空");
			return;
		}	
		httpUserDateService.addParams("sex", sex);
		httpUserDateService.addParams("nickname", value);
		httpUserDateService.execute(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_text:
			modifyUser();
			break;
		case R.id.clear:
			nickName.setText("");
			break;

		}

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
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
	@Override
	protected void onDestroy() {
		super.onDestroy();
		httpUserDateService=null;
	}

	

}
