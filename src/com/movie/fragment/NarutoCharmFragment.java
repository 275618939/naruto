package com.movie.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.movie.R;
import com.movie.adapter.NarutoAdapter;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.app.Constant.ReturnCode;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpNarutoQueryService;
import com.movie.network.HttpRegionService;
import com.movie.view.LoadView;

public class NarutoCharmFragment extends Fragment implements CallBackService,
		OnClickListener, OnRefreshListener2<ListView>  {


	NarutoAdapter natutoAdapter;
	PullToRefreshListView refreshView;
	BaseService httpNarutoBaseService;
	BaseService httpRegionService;
	List<User> users = new ArrayList<User>();
	View view;
	LoadView loadView;
	int page;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View titleView = getActivity().findViewById(R.id.main_head);
		if (null != titleView) {
			titleView.setVisibility(View.GONE);
		}
		httpNarutoBaseService = new HttpNarutoQueryService(getActivity());
		httpRegionService = new HttpRegionService(getActivity());
		view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_naruto_charm, null);
		loadView = new LoadView(view);
		initView(view);
		loadUser();
		httpRegionService.execute(this);
		return view;
	}

	protected void initView(View view) {
		natutoAdapter = new NarutoAdapter(getActivity(), mHandler,users);
		refreshView = (PullToRefreshListView) view.findViewById(R.id.naruto_list);
		refreshView.setMode(Mode.BOTH);
		refreshView.setOnRefreshListener(this);
		refreshView.setAdapter(natutoAdapter);
		
	}

	private void loadUser() {
		httpNarutoBaseService.addUrls(Constant.Member_ByFace_Query_API_URL);
		httpNarutoBaseService.addParams("regionId", "");
		httpNarutoBaseService.addParams("page", page);
		httpNarutoBaseService.addParams("size", Page.DEFAULT_SIZE);
		httpNarutoBaseService.execute(this);
	}
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			
			}
		};
	};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loading_error:
			users.clear();
			loadUser();
			break;
		default:
			break;
		}

	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		loadView.showLoadAfter(this);
		refreshView.onRefreshComplete();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if (tag.equals(httpNarutoBaseService.TAG)) {
				List<HashMap<String, Object>> datas = (ArrayList<HashMap<String, Object>>) map.get(Constant.ReturnCode.RETURN_VALUE);
				User user = null;
				int size = datas.size();
				HashMap<String, Object> userMap = null;
				for (int i = 0; i < size; i++) {
					user = new User();
					userMap = datas.get(i);
					if (userMap.containsKey("portrait")) {
						user.setPortrait(userMap.get("portrait").toString());
					}
					if (userMap.containsKey("memberId")) {
						user.setMemberId(userMap.get("memberId").toString());
					}
					if (userMap.containsKey("nickname")) {
						user.setNickname(userMap.get("nickname").toString());
					}
					if (userMap.containsKey("signature")) {
						user.setSignature(userMap.get("signature").toString());
					}
					if (userMap.containsKey("love")) {
						user.setLove(Integer.valueOf(userMap.get("love")
								.toString()));
					}
					if (userMap.containsKey("sex")) {
						user.setSex(Integer.valueOf(userMap.get("sex")
								.toString()));
					}
					if (userMap.containsKey("charm")) {
						user.setCharm(Integer.valueOf(userMap.get("charm")
								.toString()));
					}
					if (userMap.containsKey("stage")) {
						user.setStage(Integer.valueOf(userMap.get("stage")
								.toString()));
					}
					if (userMap.containsKey("hobbies")) {

					}
					users.add(user);
				}
				natutoAdapter.updateData(users);

			}
		} else {
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		refreshView.onRefreshComplete();
		String state= map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if(state.equals(ReturnCode.STATE_999)){
			loadView.showLoadLineFail(this);
		}else{
			loadView.showLoadFail(this, this);
		}
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
		

	}

	@Override
	public void OnRequest() {
		loadView.showLoading(this);
	}

	protected void showToask(String hint) {
		Toast toast = Toast.makeText(getActivity(), hint, Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		page = 0;
		users.clear();
		loadUser();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		page = 1;
		loadUser();

	}


}
