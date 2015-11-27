package com.movie.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.movie.R;
import com.movie.adapter.ViewPagerAdapter;
import com.movie.app.BaseActivity;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpCommentService;
import com.movie.network.HttpFilmTypeService;
import com.movie.network.HttpHobbyService;
import com.movie.network.HttpRegionService;
import com.movie.state.SexState;

/**
 * 引导界面
 * @author liu
 *
 */
public class GuideActivity extends BaseActivity implements OnPageChangeListener ,CallBackService{

	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	BaseService httpHobbyService;
	BaseService httpCommentService;
	BaseService httpFilmTypeService;
	BaseService httpRegionService;
	// 底部小点图片
	private ImageView[] dots;

	// 记录当前选中位置
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
		httpHobbyService = new HttpHobbyService(this);
		httpCommentService = new HttpCommentService(this);
		httpFilmTypeService = new HttpFilmTypeService(this);
		httpRegionService = new HttpRegionService(this);
	
		initViews();
		initEvents();
		initDots();
		initData();
	}
	@Override
	protected void initViews() {
		
		LayoutInflater inflater = LayoutInflater.from(this);
		views = new ArrayList<View>();
		// 初始化引导图片列表
		views.add(inflater.inflate(R.layout.what_new_one, null));
		views.add(inflater.inflate(R.layout.what_new_two, null));
		views.add(inflater.inflate(R.layout.what_new_three, null));
		views.add(inflater.inflate(R.layout.what_new_four, null));
		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter(views, this);		
		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setAdapter(vpAdapter);

	}
	@Override
	protected void initEvents() {
		vp.addOnPageChangeListener(this);		
	}
	@Override
	protected void initData() {
		httpHobbyService.execute(this);
		//加载男性评价
		httpCommentService.addParams("type", SexState.MAN.getState());
		httpCommentService.execute(this);
		httpFilmTypeService.execute(this);
		//加载女性评价
		httpCommentService.addParams("type", SexState.WOMAN.getState());
		httpCommentService.execute(this);
		//初始化当前用户位置ID
		httpRegionService.execute(this);
		
	}
	

	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[views.size()];

		// 循环取得小点图片
		for (int i = 0; i < views.size(); i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);// 都设为灰色
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > views.size() - 1
				|| currentIndex == position) {
			return;
		}

		dots[position].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = position;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		// 设置底部小点选中状态
		setCurrentDot(arg0);
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
