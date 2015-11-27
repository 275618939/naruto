package com.movie.ui;

import java.util.ArrayList;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.fragment.HomeFragment;
import com.movie.fragment.MissFragment;
import com.movie.fragment.MoiveFragment;
import com.movie.fragment.NarutoFragment;
import com.movie.fragment.SelfFragment;
import com.movie.network.HttpLoginAutoService;
import com.movie.view.FragmentTabHost;

public class MainActivity extends BaseActivity implements OnClickListener, CallBackService {

	/** 首页底部导航栏文本 */
	String tabTextviewArray[] = { "今日", "约会", "影片", "会员", "我的" };
	/** 首页底部导航栏内容 */
	Class fragmentArray[] = { HomeFragment.class, MissFragment.class,MoiveFragment.class, NarutoFragment.class, SelfFragment.class };
	ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	FragmentTabHost mTabHost;
	LayoutInflater layoutInflater;
	BaseService httpLoginAutoService;
	String login;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		httpLoginAutoService = new HttpLoginAutoService(this);
		initViews();
		initEvents();
		initData();
	}
	/**
	 * 初始化界面
	 */
	@Override
	protected void initViews() {
		layoutInflater = LayoutInflater.from(this);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		int count = fragmentArray.length;
		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = mTabHost.newTabSpec(tabTextviewArray[i]).setIndicator(getTabItemView(i));
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
		}
		mTabHost.getTabWidget().setDividerDrawable(null);
	}
	
	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void initData() {
		httpLoginAutoService.execute(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mTabHost = null;
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
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			
		}else {
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
		showProgressDialog("提示", "请求中...");
	}

	
	
}