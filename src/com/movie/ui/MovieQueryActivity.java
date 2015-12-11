package com.movie.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.fragment.MoiveCurrentFragment;
import com.movie.fragment.MoiveUpComingFragment;
import com.movie.view.PagerSlidingTabStrip;

public class MovieQueryActivity extends BaseActivity implements OnClickListener {

	TextView title;
	MoiveCurrentFragment currentFragment;
	MoiveUpComingFragment upComingFragment;
	String[] titles;
	PagerSlidingTabStrip tabs;
	ViewPager pager;
	TabAdapter tabAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_query);
		initViews();
		initData();
		initEvents();
	}
	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		pager = (ViewPager)findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
		titles = new String[]{ getResources().getString(R.string.current_movie), getResources().getString(R.string.upcoming_movie)};
		tabAdapter = new TabAdapter(getSupportFragmentManager(),titles);	
		getSupportFragmentManager();
		pager.setAdapter(tabAdapter);
		tabs.setViewPager(pager);
	}

	@Override
	protected void initEvents() {}

	@Override
	protected void initData() {
		title.setText("选择一个电影");
	}
	@Override
	public void onClick(View v) {}
	public class TabAdapter extends FragmentPagerAdapter{
		String[] _titles;
		public TabAdapter(FragmentManager fm,String[] titles) {
			super(fm);
			_titles=titles;
		}
		@Override
		public CharSequence getPageTitle(int position) {
			return _titles[position];
		}
		
		@Override
		public int getCount() {
			return _titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (currentFragment == null) {
					currentFragment = new MoiveCurrentFragment();
				}
				return currentFragment;
			case 1:
				if (upComingFragment == null) {
					upComingFragment = new MoiveUpComingFragment();
				}
				return upComingFragment;
			default:
				return null;
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		this.finish();
	}




	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	

}
