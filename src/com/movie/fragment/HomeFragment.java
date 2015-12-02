package com.movie.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.movie.R;
import com.movie.app.BaseFragment;
import com.movie.view.PagerSlidingTabStrip;

public class HomeFragment extends BaseFragment {

	HomeNearFragment nearFragment;
	HomeDynamicFragment dynamicFragment;
	String[] titles;
	PagerSlidingTabStrip tabs;
	ViewPager pager;
	TabAdapter tabAdapter;
    public HomeFragment() {
		super();
	}
	public HomeFragment(Activity activity,Context context) {
		super(activity, context);
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_home, container,false);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		isVisible=true;
		isPrepared=true;
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	@Override
	protected void initViews() {
		pager = (ViewPager)rootView.findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
		titles = new String[]{ getResources().getString(R.string.home_dynamic), getResources().getString(R.string.home_near)};
		tabAdapter = new TabAdapter(getChildFragmentManager(),titles);		
		pager.setAdapter(tabAdapter);
		tabs.setViewPager(pager);

	}
	@Override
	protected void initEvents() {
		
	}
	@Override
	protected void lazyLoad() {
		
		View titleView = getActivity().findViewById(R.id.main_head);
		if (null != titleView) {
			titleView.setVisibility(View.VISIBLE);
		}
	}
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
				if (dynamicFragment == null) {
					dynamicFragment = new HomeDynamicFragment();
				}
				return dynamicFragment;
			case 1:
				if (nearFragment == null) {
					nearFragment = new HomeNearFragment();
				}
				return nearFragment;
			default:
				return null;
			}
		}
	}
	

}
