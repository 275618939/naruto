package com.movie.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.movie.R;
import com.movie.adapter.MissNarutoQueryAdapter;
import com.movie.client.bean.Miss;
import com.movie.client.service.BaseService;
import com.movie.network.HttpMissQueryService;
import com.movie.view.PagerSlidingTabStrip;

public class MissFragment extends Fragment {
	public static final int REFRESH_COMPLETE = 0X110;
	ListView missListView;
	BaseService missQueryService;
	PullToRefreshListView refreshableListView;
	MissNarutoQueryAdapter missQueryAdapter;
	List<Miss> misses = new ArrayList<Miss>();
	int page;
	boolean isRefreshing;
	MissBestFragment bestFragment;
	MissLatelyFragment latelyFragment;
	String[] titles;
	PagerSlidingTabStrip tabs;
	ViewPager pager;
	DisplayMetrics dm;
	//View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		View  rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_miss, null);
		View titleView = getActivity().findViewById(R.id.main_head);
		if(null!=titleView){
			titleView.setVisibility(View.GONE);
		}
		titles = new String[]{ getResources().getString(R.string.miss_lately), getResources().getString(R.string.miss_best)};
		missQueryService = new HttpMissQueryService(getActivity());
		misses.clear();
		initView(rootView);
		return rootView;
	}

	public void initView(View view) {

		dm = getResources().getDisplayMetrics();
		pager = (ViewPager)view.findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		pager.setAdapter(new MissAdapter(getChildFragmentManager(),titles));
		tabs.setViewPager(pager);

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

}
