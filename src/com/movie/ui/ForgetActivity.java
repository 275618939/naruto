package com.movie.ui;

import java.util.Map;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpAccountForgetService;
import com.movie.network.HttpCaptchaService;

public class ForgetActivity extends BaseActivity implements OnClickListener,CallBackService {

	TextView title;
	TextView right;
	TextView change;
	EditText login;
	EditText captcha;
	ImageView clear;
	ImageView captchaImage;
	BaseService httpCaptchaService;
	BaseService httpForgetService;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget);
		httpCaptchaService = new HttpCaptchaService(this);
		httpForgetService = new HttpAccountForgetService(this);
		initViews();
		initEvents();
		initData();
	}
	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right_text);
		change = (TextView) findViewById(R.id.change);
		login = (EditText) findViewById(R.id.login);
		captcha = (EditText) findViewById(R.id.captcha);
		clear = (ImageView) findViewById(R.id.clear_login);
		captchaImage = (ImageView) findViewById(R.id.captcha_image);
		right.setVisibility(View.VISIBLE);
	}
	@Override
	protected void initEvents() {
		right.setOnClickListener(this);
		clear.setOnClickListener(this);
		change.setOnClickListener(this);
		login.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					TextView account = (TextView) v;
					String content = account.getText().toString();
					if (null == content || content.isEmpty())
						return;
					doCaptcha(content);
				}

			}
		});
		
	}

	@Override
	protected void initData() {
		title.setText("忘记密码");
		right.setText("下一步");
		
	}
	private void doCaptcha(String account) {
		if (null != account && !account.isEmpty()) {
			httpCaptchaService.addParams("login", account);
			httpCaptchaService.execute(this);
		}

	}
	private void nextActivity() {

		String value = login.getText().toString();
		String captchaText = captcha.getText().toString();
		if (value == null || value.isEmpty()) {
			showToask("账户名不能为空");
			return;
		}
		if (captchaText == null || captchaText.isEmpty()) {
			showToask("验证码不能为空");
			return;
		}
		httpForgetService.addParams("login", value);
		httpForgetService.addParams("captcha", captchaText);
		httpForgetService.execute(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_text:
			nextActivity();
			break;
		case R.id.change:
			String value = login.getText().toString();
			if (value == null || value.isEmpty()) {
				showToask("登录名不能为空");
				return;
			}
			doCaptcha(value);
			break;
		case R.id.clear:
			login.setText("");
			break;

		}

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, LoginActivity.class);
		this.startActivity(intent);
		this.finish();
	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {

		hideProgressDialog();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag=map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if(tag.equals(httpCaptchaService.TAG)) {
				Object object= map.get(Constant.ReturnCode.RETURN_VALUE);
				if(null!=object) {
					byte[] content=(byte[])object;
					captchaImage.setImageBitmap(BitmapFactory.decodeByteArray(content, 0, content.length));
				}
			}else if(tag.equals(httpForgetService.TAG)){
				Intent forgetIntent = new Intent(this, VerifyActivity.class);
				forgetIntent.putExtra("login", login.getText().toString());
				startActivity(forgetIntent);
			}
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
