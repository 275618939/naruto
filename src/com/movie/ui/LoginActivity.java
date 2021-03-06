package com.movie.ui;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.NarutoApplication;
import com.movie.app.Constant.ReturnCode;
import com.movie.client.bean.Login;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.client.service.LoginService;
import com.movie.client.service.UserService;
import com.movie.dialog.CaptchaDialog;
import com.movie.fragment.SelfFragment;
import com.movie.network.HttpCaptchaService;
import com.movie.network.HttpLoginService;
import com.movie.util.BytesUtils;


public class LoginActivity extends BaseActivity implements OnClickListener, CallBackService {

	EditText accountEdit;
	EditText passwordEdit;
	ImageView loginClearView;
	ImageView passClearView;
	Button loginButton;
	Button forgetButton;
	TextView title;
	TextView right_text;
	ImageView captchaView;
	LoginService loginService;
	UserService userService;
	BaseService httpLoginService;
	BaseService httpCaptchaService;
	BaseService httpUsersService;
	CaptchaDialog captchaDialog;
	String requestTag;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loginService = new LoginService();
		httpLoginService=new HttpLoginService(this);
		httpCaptchaService=new HttpCaptchaService(this);
		userService = new UserService();
		captchaDialog = new CaptchaDialog(this);
		initViews();
		initEvents();
		initData();
	}
	@Override
	protected void initViews() {		
		accountEdit = (EditText) findViewById(R.id.account);
		passwordEdit= (EditText) findViewById(R.id.password);
		loginButton = (Button) findViewById(R.id.login);
		forgetButton = (Button) findViewById(R.id.forget);
		loginClearView = (ImageView)findViewById(R.id.clear_login);
		passClearView = (ImageView)findViewById(R.id.clear_password);
		accountEdit.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
		title = (TextView) findViewById(R.id.title);
		right_text = (TextView) findViewById(R.id.right_text);
		right_text.setVisibility(View.VISIBLE);	
	}

	@Override
	protected void initEvents() {
		loginButton.setOnClickListener(this);
		forgetButton.setOnClickListener(this);
		right_text.setOnClickListener(this);	
		right_text.setClickable(true);
		loginClearView.setOnClickListener(this);
		passClearView.setOnClickListener(this);
		captchaDialog.getChange().setOnClickListener(this);
	}

	@Override
	protected void initData() {
		title.setText("登陆");
		right_text.setText("注册");
		captchaDialog.setTitle(R.string.captcha_hint);
	}


	private void doLogin(String captcha) {

		String account=accountEdit.getText().toString();
		String password=passwordEdit.getText().toString().trim();
		if (account==null||account.isEmpty()) {
		   showToask("账号不能为空");
		   return;
		}
		if (password==null||password.isEmpty()) {
		   showToask("密码不能为空");
		   return;
	    }
		try {
			String pwd = BytesUtils.toHexString(MessageDigest.getInstance("MD5").digest((account +":"+ password).getBytes()), false);
			httpLoginService.addParams("login", account);
			httpLoginService.addParams("password", pwd);
			if(null!=captcha){
				httpLoginService.addParams("captcha", captcha);
			}
			httpLoginService.execute(this);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	
	}
	private void doCaptcha(String account){
		if(null!=account&&!account.isEmpty()){
			requestTag=httpCaptchaService.TAG;
			httpCaptchaService.addParams("login", account);
			httpCaptchaService.execute(this);
		}
		
	}
	private void loadUser(String memberId) {
		httpUsersService.addParams("userId", memberId);
		httpUsersService.addParams(httpUsersService.URL_KEY,Constant.User_Query_API_URL);
		httpUsersService.execute(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.change:
			doCaptcha(accountEdit.getText().toString());
			break;
		case R.id.clear_login:
			accountEdit.setText("");
			break;
		case R.id.clear_password:
			passwordEdit.setText("");
			break;
		case R.id.right_text:
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.login:
			doLogin(null);
			break;
		case R.id.forget:
			Intent forgetIntent = new Intent(this, ForgetActivity.class);
			startActivity(forgetIntent);
			this.finish();
			break;
		}
	}
	@Override
	public void onBackPressed() {
		 //super.onBackPressed();
		 //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		 Intent intent = new Intent(this, MainActivity.class);
		 //startActivity(intent);
		 setResult(SelfFragment.RELOAGIN,intent);
		 this.finish();
	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String code=map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag=map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if(tag.equals(httpLoginService.TAG)){
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				Login login=new Login();
				/*记录登入信息，下次自动登陆*/
				final String account=accountEdit.getText().toString();
				final String password=passwordEdit.getText().toString().trim();
				EMChatManager.getInstance().login(account, password,
						new EMCallBack() {
							@Override
							public void onSuccess() {
								try {
									// 登陆成功，保存用户名密码
									NarutoApplication.getApp().setUserName(account);
									NarutoApplication.getApp().setPassword(password);
									EMGroupManager.getInstance().loadAllGroups();
									EMChatManager.getInstance().loadAllConversations();
								} catch (Exception e) {
								}
								// 更新当前用户的nickname
								// 此方法的作用是在ios离线推送时能够显示用户nick
								boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(NarutoApplication.currentUserNick.trim());
								if (!updatenick) {
									Log.e("LoginActivity","update current user nick fail");
								}
							}
							@Override
							public void onProgress(int progress,
									String status) {
							}
							@Override
							public void onError(final int code,final String message) {
								Log.e("hxlogin", message);
							}

				});
				
	
				String pwd=null;
				try {
					pwd = BytesUtils.toHexString(MessageDigest.getInstance("MD5").digest((account +":"+ password).getBytes()), false);
				} catch (NoSuchAlgorithmException e) {
					
					e.printStackTrace();
				}	
				String memberId=map.get("value").toString();
				login.setAccount(account);
				login.setPass(pwd);
				loginService.addLogin(login);
				User user=new User();
				user.setMemberId(memberId);
				userService.addUser(user);
				loadUser(memberId);
				onBackPressed();
			}else if(tag.equals(httpCaptchaService.TAG)){
				Object object= map.get(Constant.ReturnCode.RETURN_VALUE);
				if(null!=object){
					byte[] content=(byte[])object;
					captchaDialog.getCodeView().setImageBitmap(BitmapFactory.decodeByteArray(content, 0, content.length));
					captchaDialog.setButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									captchaDialog.cancel();
								}
							}, "确认", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									captchaDialog.dismiss();
									 String realCode=captchaDialog.getRealCode().getText().toString();
				                     doLogin(realCode);				                       
								}
							});
					captchaDialog.show();
					content=null;
				}
			}else if(tag.equals(httpUsersService.TAG)){
				User user=new User();
				Map<String, Object> values = (Map<String, Object>) map.get(ReturnCode.RETURN_VALUE);
				if (values.containsKey("memberId")) {
					user.setMemberId(values.get("memberId").toString());
				}
				if (values.containsKey("portrait")) {
					user.setPortrait(Constant.SERVER_ADRESS+values.get("portrait").toString());
				}
				if (values.containsKey("sex")) {
					user.setSex(Integer.parseInt(values.get("sex").toString()));
				}
				if (values.containsKey("birthday")) {
					user.setBirthday(values.get("birthday").toString());
				}
				if (values.containsKey("nickname")) {
					user.setNickname(values.get("nickname").toString());
				}
				if (values.containsKey("mobile")) {
					user.setMobile(values.get("mobile").toString());
				}
				if (values.containsKey("signature")) {
					user.setSignature(values.get("signature").toString());
				}
				if (values.containsKey("loveCnt")) {
					user.setLove(Integer.parseInt(values.get("loveCnt").toString()));
				}
				if(values.containsKey("faceTtl")){
					user.setFace(Integer.parseInt(values.get("faceTtl").toString()));
				}
				if(values.containsKey("faceCnt")){
					user.setFaceCnt(Integer.parseInt(values.get("faceCnt").toString()));
				}	
				if (values.containsKey("hobbies")) {
					List<Integer> hobbies = (List<Integer>) values.get("hobbies");
					user.setHobbies(hobbies);
				}
				if(values.containsKey("trystCnt")){
					int tryst=Integer.parseInt(values.get("trystCnt").toString());
					user.setTryst(tryst);
				}
				if(values.containsKey("filmCnt")){
					int filmCnt=Integer.parseInt(values.get("filmCnt").toString());
					user.setFilmCnt(filmCnt);
				}
				NarutoApplication.getApp().cuurentUser=user;
			}
			
		}else if(Constant.ReturnCode.STATE_2.equals(code)){	
			doCaptcha(accountEdit.getText().toString());
		}else{
			String message=map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
		map=null;
	}
	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String message=map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
		map=null;
	}

	@Override
	public void OnRequest() {
		if(requestTag!=httpCaptchaService.TAG){
			showProgressDialog();
		}
	}
	@Override
	protected void onDestroy() {
		loginService=null;
		userService=null;
		httpLoginService=null;
		httpCaptchaService=null;
		super.onDestroy();
	}

	

	

}
