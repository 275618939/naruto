package com.movie.ui;

import java.util.ArrayList;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.movie.R;
import com.movie.app.Constant;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.fragment.HomeFragment;
import com.movie.fragment.MissFragment;
import com.movie.fragment.MoiveFragment;
import com.movie.fragment.NarutoFragment;
import com.movie.fragment.SelfFragment;
import com.movie.network.HttpLoginAutoService;
import com.movie.network.HttpLogoutService;
import com.movie.network.HttpUserService;
import com.movie.util.ImageLoaderCache;
import com.movie.view.DrawerView;

public class MainActivity extends NarutoMonitorActivity implements
		OnClickListener, CallBackService {

	/** 首页底部导航栏文本 */
	String tabTextviewArray[] = {"今日","约会","影片", "会员","我的" };
	/** 首页底部导航栏内容 */
	Class fragmentArray[] = {HomeFragment.class,MissFragment.class, MoiveFragment.class,NarutoFragment.class,SelfFragment.class };
	ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	FragmentTabHost mTabHost;
	LayoutInflater layoutInflater;
	// 左侧按钮
	SlidingMenu side_drawer;
	// head 头部 的左侧菜单 按钮
	//ImageView user_info;
	// head 头部 的用户头像
	TextView textView;
	Bitmap headImage;
	BaseService httpUsersService;
	BaseService httpLogotService;
	BaseService httpLoginAutoService;
	ImageLoaderCache imageLoaderCache;
	MainActivity mainActivity;
	String login;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainActivity = this;
		httpLogotService = new HttpLogoutService(this);
		httpUsersService = new HttpUserService(this);
		httpLoginAutoService = new HttpLoginAutoService(this);
		imageLoaderCache = new ImageLoaderCache(this);
		initSlidingMenu();
		initViews();
		initData();
	}
	/**
	 * 初始化界面
	 */
	public void initViews() {

		layoutInflater = LayoutInflater.from(this);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		// 得到fragment的个数
		int count = fragmentArray.length;
		for (int i = 0; i < count; i++) {
			// 为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(tabTextviewArray[i]).setIndicator(getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
		}
		mTabHost.getTabWidget().setDividerDrawable(null);


	}
	public void initData() {	
		httpLoginAutoService.execute(this);
	}
	/**
	 * 初始化左侧按钮
	 */
	protected void initSlidingMenu() {
		side_drawer = new DrawerView(this).initSlidingMenu();
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

		/*case R.id.user_info: {
			if (side_drawer.isMenuShowing()) {
				side_drawer.showContent();
			} else {
				side_drawer.showMenu();
			}
			break;
		}*/
		case R.id.user_image: {
			/*if (side_drawer.isMenuShowing()) {
				side_drawer.showContent();
			} else {
				side_drawer.showMenu();
			}*/
			break;
		}

		}
	}

	@Override
	public void onBackPressed() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("退出");
		builder.setMessage("确定要退出伙影吗！");
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface diaCustomDialoglog, int which) {
			}
		});
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface diaCustomDialoglog, int which) {
				MainActivity.this.finish();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();

	}
	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			if (tag.endsWith(httpUsersService.TAG)) {
				Map<String, Object> value = (Map<String, Object>) map.get(Constant.ReturnCode.RETURN_VALUE);
				if (value != null) {
					User user = new User();
					user.setMemberId(value.get("memberId").toString());
					if (value.containsKey("portrait"))
						user.setPortrait(value.get("portrait").toString());
					if (value.containsKey("nickname"))
						user.setNickname(value.get("nickname").toString());
					if (value.containsKey("signature"))
						user.setSignature(value.get("signature").toString());
					if (value.containsKey("charm"))
						user.setCharm(Integer.parseInt(value.get("signature").toString()));
					if (value.containsKey("love"))
						user.setCharm(Integer.parseInt(value.get("love").toString()));
					if (user.getPortrait() != null&& !user.getPortrait().isEmpty()) {
						
					}
					
				}
			} else if (tag.endsWith(httpLoginAutoService.TAG)) {
				login =  map.get("login").toString();
				httpUsersService.execute(this);
			}
		} else {
			String desc = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(desc);
		}
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		// showToask(message);
	}

	@Override
	public void OnRequest() {
		showProgressDialog("提示", "链接服务器...");
	}

}