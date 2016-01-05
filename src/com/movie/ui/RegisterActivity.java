package com.movie.ui;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.Login;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.client.service.LoginService;
import com.movie.client.service.UserService;
import com.movie.network.HttpMobileCaptchaService;
import com.movie.network.HttpRegisterService;
import com.movie.util.BytesUtils;
import com.movie.view.VerifyCodeCountTimer;


public class RegisterActivity extends BaseActivity implements OnClickListener, CallBackService {

	
	EditText login;
	EditText password;
	EditText passwordConfirm;
	EditText captcha;
	Button registerButton;
	TextView title;
	ImageView clearLogin;
	ImageView clearPass;
	ImageView clearPassConfirm;
	Button codeBtn;
	UserService userService;
	LoginService loginService;
	VerifyCodeCountTimer  countTimer;
	BaseService  httpMobileCaptchaService;
	BaseService  httpRegisterService;
	String tag;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		httpMobileCaptchaService =new HttpMobileCaptchaService(this);
		httpRegisterService = new HttpRegisterService(this);
		userService = new UserService();
		loginService =new LoginService();
		initViews();
		initEvents();
		initData();
	}
	@Override
	protected void initViews() {
		login = (EditText) this.findViewById(R.id.login);
		clearLogin=(ImageView)this.findViewById(R.id.clear_login);
		password = (EditText) this.findViewById(R.id.password);
		clearPass=(ImageView)this.findViewById(R.id.clear_password);
		passwordConfirm= (EditText) this.findViewById(R.id.password_confirm);
		clearPassConfirm=(ImageView)this.findViewById(R.id.clear_password_confirm);
		captcha = (EditText) this.findViewById(R.id.captcha);
		registerButton = (Button) this.findViewById(R.id.register);
		codeBtn=(Button) this.findViewById(R.id.send_captcha);
		login.setInputType(EditorInfo.TYPE_CLASS_NUMBER);		
		title = (TextView) findViewById(R.id.title);
		countTimer= new VerifyCodeCountTimer(codeBtn, 0xfff30008, 0xff969696);

	}

	@Override
	protected void initEvents() {
		registerButton.setOnClickListener(this);
		clearLogin.setOnClickListener(this);
		clearPass.setOnClickListener(this);
		clearPassConfirm.setOnClickListener(this);
		codeBtn.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		title.setText("注册");
	}
	private void doCaptcha(){
		String account=login.getText().toString();
		if(null!=account&&!account.isEmpty()){		
			tag=httpMobileCaptchaService.TAG;
			httpMobileCaptchaService.addParams("login", account);
			httpMobileCaptchaService.execute(this);
			countTimer.start();
		}else{
			new AlertDialog.Builder(RegisterActivity.this).setIcon(getResources().getDrawable(R.drawable.login_error_icon)).setTitle("注册错误").setMessage("验证码不能为空!").create().show();
			return;
		}
		
	}
	
	private void doRegister() {
		
		
		String account=login.getText().toString();
		String pass=password.getText().toString().trim();
		String passConfirm=passwordConfirm.getText().toString().trim();
		String code=captcha.getText().toString().trim();
		
		if (account==null||account.isEmpty()) {
			new AlertDialog.Builder(RegisterActivity.this).setIcon(getResources().getDrawable(R.drawable.login_error_icon)).setTitle("注册错误").setMessage("账号不能为空!").create().show();
			return;
		}
		if (pass==null||pass.isEmpty()) {
			new AlertDialog.Builder(RegisterActivity.this).setIcon(getResources().getDrawable(R.drawable.login_error_icon)).setTitle("注册错误").setMessage("密码不能为空!").create().show();
			return;
		}
		if(!pass.equals(passConfirm)){
			new AlertDialog.Builder(RegisterActivity.this).setIcon(getResources().getDrawable(R.drawable.login_error_icon)).setTitle("注册错误").setMessage("两次密码输入不一致!").create().show();
			return;
		}
		if (code==null||code.isEmpty()) {
			new AlertDialog.Builder(RegisterActivity.this).setIcon(getResources().getDrawable(R.drawable.login_error_icon)).setTitle("注册错误").setMessage("验证码不能为空!").create().show();
			return;
		}
		try {
			String pwd = BytesUtils.toHexString(MessageDigest.getInstance("MD5").digest((account +":"+ pass).getBytes()), false);
			tag=httpRegisterService.TAG;
			httpRegisterService.addParams("login", account);
			httpRegisterService.addParams("password", pwd);
			httpRegisterService.addParams("captcha", code);
			httpRegisterService.execute(this);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, LoginActivity.class);
		this.startActivity(intent);
		this.finish();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.clear_login:
				login.setText("");
				break;
			case R.id.clear_password:
				password.setText("");
				break;
			case R.id.clear_password_confirm:
				passwordConfirm.setText("");
				break;
			case R.id.register:
				doRegister();
				break;
			case R.id.send_captcha:
				doCaptcha();
				break;
			}

	}
	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String code=map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag=map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if(tag.equals(httpRegisterService.TAG)){
				final String account=login.getText().toString();
				final String pass=password.getText().toString().trim();
				new Thread(new Runnable() {
					public void run() {
						// 调用sdk注册方法
						NarutoApplication.getApp().setUserName(account);
						try {
							EMChatManager.getInstance().createAccountOnServer(account, pass);
						} catch (EaseMobException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				}});
				String pwd=null;
				try {
					pwd = BytesUtils.toHexString(MessageDigest.getInstance("MD5").digest((account +":"+ pass).getBytes()), false);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				String memberId=map.get("value").toString();
				Login login=new Login();
				login.setAccount(account);
				login.setPass(pwd);
				loginService.addLogin(login);
				User user=new User();
				user.setMemberId(memberId);
				userService.addUser(user);
				Intent intent = new Intent(this, RegsiterGuideActivity.class);
				startActivity(intent);
				this.finish();
			}
			
		}else{
			String message=map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
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
		if(tag.equals(httpRegisterService.TAG)){
			showProgressDialog();
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		httpMobileCaptchaService=null;
		httpRegisterService=null;
	}


	
}
