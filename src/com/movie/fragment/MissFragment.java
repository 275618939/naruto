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

public class MissFragment extends BaseFragment {
	
	MissBestFragment bestFragment;
	MissLatelyFragment latelyFragment;
	String[] titles;
	PagerSlidingTabStrip tabs;
	ViewPager pager;
	TabAdapter tabAdapter;
	public MissFragment() {
		super();
	}
	public MissFragment(Activity activity,Context context) {
		super(activity, context);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(rootView==null){  
	         rootView=inflater.inflate(R.layout.fragment_miss,container,false);  
	    }  
		ViewGroup parent = (ViewGroup) rootView.getParent();  
	    if (parent != null) {  
	        parent.removeView(rootView);  
	    }   
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	@Override
	protected void initViews() {

		pager = (ViewPager)rootView.findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
		titles = new String[]{ getResources().getString(R.string.miss_lately), getResources().getString(R.string.miss_best)};
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
		if(null!=titleView){
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
				if (latelyFragment == null) {
					latelyFragment = new MissLatelyFragment();
				}
				return latelyFragment;
			case 1:
				if (bestFragment == null) {
					bestFragment = new MissBestFragment();
				}
				return bestFragment;
			default:
				return null;
			}
		}
	}

	@Override
	protected void destroyData() {
		// TODO Auto-generated method stub
		
	}

}
