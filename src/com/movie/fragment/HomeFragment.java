package com.movie.fragment;

import java.util.ArrayList;
import java.util.List;

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
import com.movie.client.bean.User;
import com.movie.util.Images;

public class HomeFragment extends Fragment {

	CycleFragment cycleFragment;
	RankingAdapter rankingAdapter;
	ScrollView mainScrollView;
	List<User> users = new ArrayList<User>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		users.clear();
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main, null);

		// FragmentStatePagerAdapter
		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		pager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
			public int getCount() {
				return 1;
			}

			public Fragment getItem(int arg0) {
				cycleFragment = new CycleFragment();
				return cycleFragment;
			}
		});
		mainScrollView = (ScrollView) view.findViewById(R.id.main_scroll_view);
		mainScrollView.smoothScrollTo(0, 0);
		users=User.getTempData();
		rankingAdapter = new RankingAdapter(view.getContext(), users);
		GridView gridView = (GridView) view.findViewById(R.id.miss_grid);
		gridView.setAdapter(rankingAdapter);
		return view;
	}



	/*
	 * public void onDestroyView() {
	 * 
	 * Log.i(this.getTag(), "------------->>>>执行了操作onDestroyView"); //
	 * 内嵌的Fragment CycleFragment FragmentManager manager = getFragmentManager();
	 * CycleFragment cycleFragment = (CycleFragment) manager
	 * .findFragmentById(R.id.fragment_cycle_viewpager_content);
	 * Log.i(this.getTag(), "------------->>>>cycleFragment--->>>value:" +
	 * cycleFragment); if (cycleFragment != null) { FragmentTransaction
	 * transaction = manager.beginTransaction();
	 * transaction.addToBackStack(null); transaction.remove(cycleFragment);
	 * transaction.commitAllowingStateLoss(); } super.onDestroyView(); }
	 */

}
