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
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpMissUserQueryService;

public class NarutoFragment extends Fragment implements OnClickListener,
		CallBackService, OnRefreshListener2<ListView>{

	public static final int REFRESH_COMPLETE = 0X110;
	ListView userViewList;
	NarutoAdapter natutoAdapter;
	List<User> users = new ArrayList<User>();
	BaseService httpMissAgreeService;
	BaseService httpMissUserQueryService;
	PullToRefreshListView refreshableListView;
	int page;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View titleView = getActivity().findViewById(R.id.main_head);
		if(null!=titleView){
			titleView.setVisibility(View.VISIBLE);
		}
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_naruto, null);
		httpMissUserQueryService = new HttpMissUserQueryService(getActivity());
		initView(view);
		loadUser();
		return view;
	}

	private void initView(View view) {

		natutoAdapter = new NarutoAdapter(getActivity(), mHandler, null);
		refreshableListView = (PullToRefreshListView) view.findViewById(R.id.naruto_user_list);
		refreshableListView.setMode(Mode.BOTH);
		refreshableListView.setOnRefreshListener(this);
		refreshableListView.setAdapter(natutoAdapter);

	}

	private void loadUser() {

		httpMissUserQueryService.addParams("page", page);
		httpMissUserQueryService.addParams("size", Page.DEFAULT_SIZE);
		httpMissUserQueryService.execute(this);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				loadUser();
				break;

			default:
				break;

			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;

		}

	}

	

	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		
		refreshableListView.onRefreshComplete();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if (tag.equals(httpMissAgreeService.TAG)) {
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
				}
				if (size >= Page.DEFAULT_SIZE) {
					page++;
				}
				if (size <= 0) {
					users=User.getTempData();
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
	
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
		users=User.getTempData();
		natutoAdapter.updateData(users);
	}

	@Override
	public void OnRequest() {
		showToask("加载伙影信息");
	}

	protected void showToask(String hint) {
		Toast toast = Toast.makeText(getActivity(), hint, Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		page = 0;
		loadUser();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		page = 1;
		loadUser();
	}

}
