package com.movie.ui;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import com.movie.client.bean.Login;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.client.service.LoginService;
import com.movie.network.HttpSetPassService;
import com.movie.util.BytesUtils;

public class SetpwdActivity extends BaseActivity implements OnClickListener,CallBackService {


	TextView title;
	TextView right;
	EditText pass;
	EditText passConfrim;
	ImageView passClear;
	ImageView passConfirmClear;
	BaseService httpSetPassService;
	LoginService loginService;
	String login;
	String token;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setpwd);
		httpSetPassService = new HttpSetPassService(this);
		loginService = new LoginService();
		initViews();
		initEvents();
		initData();
	}

	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right_text);
		pass = (EditText) findViewById(R.id.password);
		passConfrim = (EditText) findViewById(R.id.password_confirm);
		passClear = (ImageView) findViewById(R.id.pass_clear);
		passConfirmClear = (ImageView) findViewById(R.id.pass_confim_clear);
		right.setVisibility(View.VISIBLE);
	}

	@Override
	protected void initEvents() {
		right.setOnClickListener(this);
		passClear.setOnClickListener(this);
		passConfirmClear.setOnClickListener(this);
		
	}

	@Override
	protected void initData() {
		login = getIntent().getStringExtra("login");
		token = getIntent().getStringExtra("token");
		title.setText("设置密码");
		right.setText("保存");
		
	}
	private void modifyPwd() {

		String value = pass.getText().toString();
		String valueConfrim = passConfrim.getText().toString();
		if (value == null || value.isEmpty()) {
			showToask("密码不能为空");
			return;
		}
		if(!value.equals(valueConfrim)){
			showToask("两次密码输入不一致");
			return;
		}
		String pwd=null;
		try {
			pwd = BytesUtils.toHexString(MessageDigest.getInstance("MD5").digest((this.login +":"+ value).getBytes()), false);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpSetPassService.addParams("login", login);
		httpSetPassService.addParams("token", token);
		httpSetPassService.addParams("password", pwd);
		httpSetPassService.execute(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_text:
			modifyPwd();
			break;
		case R.id.pass_clear:
			pass.setText("");
			break;
		case R.id.pass_confim_clear:
			passConfrim.setText("");
			break;

		}

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, VerifyActivity.class);
		this.startActivity(intent);
		this.finish();
	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String code=map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			Login login=new Login();
			login.setAccount(this.login);
			String password=pass.getText().toString().trim();
			String pwd=null;
			try {
				pwd = BytesUtils.toHexString(MessageDigest.getInstance("MD5").digest((this.login +":"+ password).getBytes()), false);
			} catch (NoSuchAlgorithmException e) {
				showToask(e.getMessage());
			}	
			login.setPass(pwd);
			loginService.addLogin(login);
			Intent intent = new Intent(this, MainActivity.class);
			this.startActivity(intent);
			this.finish();
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
