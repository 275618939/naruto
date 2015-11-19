package com.movie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.movie.R;
import com.movie.view.PagerSlidingTabStrip;

public class MoiveFragment extends Fragment implements OnClickListener {

	int page;
	MoiveCurrentFragment currentFragment;
	MoiveUpComingFragment upComingFragment;
	String[] titles;
	PagerSlidingTabStrip tabs;
	ViewPager pager;
	DisplayMetrics dm;
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		View titleView = getActivity().findViewById(R.id.main_head);
		if(null!=titleView){
			titleView.setVisibility(View.GONE);
		}
		view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_movie, null);
		initView(view);
		return view;
	}

	protected void initView(View view) {
		titles = new String[]{ getResources().getString(R.string.current_movie), getResources().getString(R.string.upcoming_movie)};
		dm = getResources().getDisplayMetrics();
		pager = (ViewPager)view.findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		pager.setAdapter(new MissAdapter(getChildFragmentManager(),titles));
		tabs.setViewPager(pager);
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {}

	}
	public class MissAdapter extends FragmentPagerAdapter{
		String[] _titles;
		public MissAdapter(FragmentManager fm,String[] titles) {
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




	


}
