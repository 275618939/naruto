package com.movie.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.Constant.ReturnCode;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.fragment.HomeFragment;
import com.movie.fragment.MissLatelyFragment;
import com.movie.fragment.MoiveFragment;
import com.movie.fragment.SelfFragment;
import com.movie.network.HttpLoginAutoService;
import com.movie.network.HttpUserService;
import com.movie.system.service.DoubleClickExitHelper;
import com.movie.system.service.LocationService;
import com.movie.util.ImageItem;
import com.movie.view.FragmentTabHost;

public class MainActivity extends BaseActivity implements OnClickListener,
		CallBackService {

	/** 首页底部导航栏文本 */
	String tabTextviewArray[] = { "发现", "约会", "影片", "我的" };
	/** 首页底部导航栏内容 */
	Class fragmentArray[] = { HomeFragment.class, MissLatelyFragment.class,MoiveFragment.class, SelfFragment.class };
	ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	FragmentTabHost mTabHost;
	LayoutInflater layoutInflater;
	LocationService locationService;
	BaseService httpAutoLoginSercService;
	BaseService httpUsersService;
	DoubleClickExitHelper doubleClick;
	ImageView addDynamic;
	ImageItem imageItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		locationService = new LocationService(this);
		httpAutoLoginSercService = new HttpLoginAutoService(this);
		httpUsersService = new HttpUserService(this);
		doubleClick = new DoubleClickExitHelper(this);
		initViews();
		initEvents();
		initData();
	}

	/**
	 * 初始化界面
	 */
	@Override
	protected void initViews() {
		addDynamic = (ImageView) findViewById(R.id.add_dynamic);
		locationService.initLocation(Constant.UPLOAD_LOCATION_TIME);
		layoutInflater = LayoutInflater.from(this);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		int count = fragmentArray.length;
		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = mTabHost.newTabSpec(tabTextviewArray[i])
					.setIndicator(getTabItemView(i));
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
		}
		mTabHost.getTabWidget().setDividerDrawable(null);

		// textHeapView=(TextView)findViewById(R.id.text_heap);
		// textHeapView.setOnClickListener(this);
	}

	@Override
	protected void initEvents() {
		addDynamic.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		locationService.start(false);
		if (!isLoad) {
			autoLogin();
		}
	}
	private void loadUser(String memberId) {
		httpUsersService.addParams("userId", memberId);
		httpUsersService.addParams(httpUsersService.URL_KEY,Constant.User_Query_API_URL);
		httpUsersService.execute(this);
	}
	private void autoLogin() {
		httpAutoLoginSercService.execute(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mTabHost = null;
		httpAutoLoginSercService = null;
	}

	/**
	 * 给Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index) {
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);
		/*
		 * ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		 * imageView.setImageResource(mImageViewArray[index]);
		 */

		TextView textView = (TextView) view.findViewById(R.id.text_btn);
		textView.setText(tabTextviewArray[index]);

		return view;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.add_dynamic:
			Intent dynamicIntent = new Intent(this, DynamicCreateActivity.class);
			startActivity(dynamicIntent);
			break;
		/*
		 * case R.id.text_heap: try {
		 * Debug.dumpHprofData("/naruto/naruto.hprof"); } catch (IOException e)
		 * { // TODO Auto-generated catch block e.printStackTrace(); } break;
		 */
		default:
			break;

		}

	}

	// @Override
	// public void onBackPressed() {
	// AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// builder.setTitle("退出");
	// builder.setMessage("确定要退出伙影吗！");
	// builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface diaCustomDialoglog, int which) {
	// }
	// });
	// builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
	//
	// public void onClick(DialogInterface diaCustomDialoglog, int which) {
	// MainActivity.this.finish();
	// locationService.stop();
	// locationService = null;
	// }
	// });
	// AlertDialog dialog = builder.create();
	// dialog.show();
	// }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			return doubleClick.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		isLoad = true;
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if (tag.equals(httpAutoLoginSercService.TAG)) {
				String memberId=map.get("value").toString();
				loadUser(memberId);
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
		}
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {

	}

	@Override
	public void OnRequest() {

	}

	public LocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(LocationService locationService) {
		this.locationService = locationService;
	}

}