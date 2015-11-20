package com.movie.ui;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.movie.R;
import com.movie.client.bean.Login;
import com.movie.client.service.LoginService;
import com.movie.listener.BackGestureListener;

public class BaseActivity extends Activity {
	
	protected SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	
	private ProgressDialog progressDialog;
	/** 手势监听 */
	GestureDetector mGestureDetector;
	/** 是否需要监听手势关闭功能 */
	private boolean mNeedBackGesture = false;
	
	protected LoginService loginService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		loginService=new LoginService();
		initGestureDetector();
	}

	private void initGestureDetector() {
		if (mGestureDetector == null) {
			mGestureDetector = new GestureDetector(getApplicationContext(),new BackGestureListener(this));
		}
	}
	protected Login getLogin(){
		Login login=loginService.getLogin();
		return login;
	}
	protected void goLogin(){
		Intent loginIntent = new Intent(this,LoginActivity.class);
		this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		startActivity(loginIntent);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (mNeedBackGesture) {
			return mGestureDetector.onTouchEvent(ev)
					|| super.dispatchTouchEvent(ev);
		}
		return super.dispatchTouchEvent(ev);
	}

	public void showToask(String hint) {
		Toast toast = Toast.makeText(this, hint, Toast.LENGTH_SHORT);
		toast.show();
	}

	public void showProgressDialog(String title, String message) {
		if (progressDialog == null) {

			progressDialog = ProgressDialog.show(this, title, message, true,
					true);
		} else if (progressDialog.isShowing()) {
			progressDialog.setTitle(title);
			progressDialog.setMessage(message);
		}

		progressDialog.show();

	}

	public void hideProgressDialog() {

		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

	}

	/*
	 * 设置是否进行手势监听
	 */
	public void setNeedBackGesture(boolean mNeedBackGesture) {
		this.mNeedBackGesture = mNeedBackGesture;
	}

	/*
	 * 返回
	 */
	public void doBack(View view) {
		onBackPressed();
	}

}
