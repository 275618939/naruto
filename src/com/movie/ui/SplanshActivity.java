package com.movie.ui;

import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.easemob.applib.controller.DemoHXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.client.service.CallBackService;
import com.movie.system.service.LocationService;

/**
 * 
 * 客户端首次登陆引导页
 * @author liu
 * 
 */
public class SplanshActivity extends BaseActivity implements CallBackService{

	private static final int sleepTime = 2000;
	private  final int GO_HOME = 1000;
	private  final int GO_GUIDE = 1001;
	boolean isFirstIn = false;
	// 延迟3秒
	private static final long SPLASH_DELAY_MILLIS = 3000;
	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	boolean initComplete = false;
	LocationService locationService;
	ImageView imageView;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		locationService = new LocationService(this);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		final View view = View.inflate(this, R.layout.activity_splansh, null);
		setContentView(view);
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);	
		initViews();
		initEvents();
		initData();
		
	
	}
	@Override
	protected void initViews() {
		imageView=(ImageView)findViewById(R.id.welcome);
		imageView.setImageResource(R.drawable.splash_bg);
		locationService.initLocation(Constant.UPLOAD_SCANSPAN);
	}
	@Override
	protected void initEvents() {
		
	}



	@Override
	protected void initData() {

		locationService.start(true);
		SharedPreferences preferences = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		isFirstIn = preferences.getBoolean("isFirstIn", true);
		if (!isFirstIn) {
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		}
		
	}
	protected void initHXLogin(){

		super.onStart();

		new Thread(new Runnable() {
			public void run() {
				if (DemoHXSDKHelper.getInstance().isLogined()) {
					// ** 免登陆情况 加载所有本地群和会话
					//不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
					//加上的话保证进了主页面会话和群组都已经load完毕
					long start = System.currentTimeMillis();
					EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					long costTime = System.currentTimeMillis() - start;
					//等待sleeptime时长
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					//进入主页面
					startActivity(new Intent(SplanshActivity.this, MainActivity.class));
					finish();
				}else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
					startActivity(new Intent(SplanshActivity.this, LoginActivity.class));
					finish();
				}
			}
		}).start();

	
	}

	@Override
	public void onBackPressed() {
		finish();
		
	}
	private void goHome() {
		Intent intent = new Intent(SplanshActivity.this, MainActivity.class);
		SplanshActivity.this.startActivity(intent);
		SplanshActivity.this.finish();
	}

	private void goGuide() {
		Intent intent = new Intent(SplanshActivity.this, GuideActivity.class);
		SplanshActivity.this.startActivity(intent);
		SplanshActivity.this.finish();
	}
    
	/**
	 * Handler:跳转到不同界面
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onDestroy() {
		super.onDestroy();
		locationService.stop();
		locationService=null;
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
		// TODO Auto-generated method stub
		
	}
    
	
}
