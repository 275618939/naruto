package com.movie.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ScrollView;

import com.movie.R;
import com.movie.adapter.RankingAdapter;
import com.movie.app.BaseFragment;
import com.movie.client.bean.User;

public class HomeFragment extends BaseFragment {

	CycleFragment cycleFragment;
	RankingAdapter rankingAdapter;
	ScrollView mainScrollView;
	GridView gridView;
	List<User> users = new ArrayList<User>();
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

		View titleView = getActivity().findViewById(R.id.main_head);
		if (null != titleView) {
			titleView.setVisibility(View.VISIBLE);
		}
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_main, container,false);
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
		ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
		/*pager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
			public int getCount() {
				return 1;
			}
			public Fragment getItem(int arg0) {
				cycleFragment = new CycleFragment();
				return cycleFragment;
			}
		});*/
		mainScrollView = (ScrollView) rootView.findViewById(R.id.main_scroll_view);
		mainScrollView.smoothScrollTo(0, 0);
		gridView = (GridView) rootView.findViewById(R.id.miss_grid);
		rankingAdapter = new RankingAdapter(rootView.getContext(), users);
		gridView.setAdapter(rankingAdapter);

	}
	@Override
	protected void initEvents() {
		
	}
	@Override
	protected void lazyLoad() {
		if (!isVisible||!isPrepared ) {
			return;
		}
		users = User.getTempData();
		//rankingAdapter.updateData(users);
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		rankingAdapter = null;
		users.clear();
	}
	

}
