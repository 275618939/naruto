package com.movie.ui;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.Constant;
import com.movie.app.Constant.ReturnCode;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpMobileCaptchaService;
import com.movie.network.HttpVerifyService;
import com.movie.view.VerifyCodeCountTimer;

public class VerifyActivity extends BaseActivity implements OnClickListener,CallBackService {


	TextView title;
	TextView right;
	EditText verifyCode;
	Button codeBtn;
	BaseService httpMoblieCaptchaService;
	BaseService httpVerifyService;
	VerifyCodeCountTimer  countTimer;
	String login;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify);
		httpVerifyService = new HttpVerifyService(this);
		httpMoblieCaptchaService = new HttpMobileCaptchaService(this);
		initViews();
		initData();
	}

	private void initViews() {

		title = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right_text);
		verifyCode = (EditText) findViewById(R.id.captcha);
		codeBtn = (Button) findViewById(R.id.send_captcha);
	    countTimer= new VerifyCodeCountTimer(codeBtn, 0xfff30008, 0xff969696);
	    
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);
		codeBtn.setOnClickListener(this);
	
	}

	private void initData() {
		login = getIntent().getStringExtra("login");
		title.setText("认证");
		right.setText("下一步");
		countTimer.start();
		
	}

	private void sendVerify(){
		String value = verifyCode.getText().toString();
		if (value == null || value.isEmpty()) {
			showToask("验证码不能为空");
			return;
		}
		httpVerifyService.addParams("login", login);
		httpVerifyService.addParams("verifyCode", value);
		httpVerifyService.execute(this);
		
	}
	
	private void sendCaptcha() {

		httpMoblieCaptchaService.addParams("login", login);
		httpMoblieCaptchaService.execute(this);
		countTimer.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_text:
			sendVerify();
			break;
		case R.id.clear:
			verifyCode.setText("");
			break;
		case R.id.send_captcha:
			sendCaptcha();
			break;
		}
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, ForgetActivity.class);
		this.startActivity(intent);
		//this.finish();
	}
	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String code=map.get(ReturnCode.RETURN_STATE).toString();
	    if (Constant.ReturnCode.STATE_1.equals(code)) {
	    	String tag=map.get(ReturnCode.RETURN_TAG).toString();
	    	if(tag.endsWith(httpVerifyService.TAG)){
	    		String token  = map.get(ReturnCode.RETURN_VALUE).toString();
	    		Intent intent = new Intent(this, SetpwdActivity.class);
	    		intent.putExtra("login", login);
	    		intent.putExtra("token", token);
	    		startActivity(intent);
	    		//this.finish();
	    	}
	    	
		}else {
			String desc = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(desc);
		}
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String message=map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
	}

	@Override
	public void OnRequest() {
	
		showProgressDialog("提示", "正在提交，请稍后......");
	}

}
