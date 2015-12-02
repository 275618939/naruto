package com.movie.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.movie.R;
import com.movie.adapter.NarutoAdapter;
import com.movie.app.BaseFragment;
import com.movie.app.Constant;
import com.movie.app.NarutoManager;
import com.movie.app.Constant.Page;
import com.movie.app.Constant.ReturnCode;
import com.movie.client.bean.NearNaruto;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpNearService;
import com.movie.network.HttpUserLoveService;
import com.movie.ui.LoginActivity;

public class HomeNearFragment extends BaseFragment implements CallBackService,
		OnClickListener, OnRefreshListener2<ListView> {
	
	NarutoAdapter natutoAdapter;
	PullToRefreshListView refreshViewLayout;
	List<NearNaruto> nearNarutos = new ArrayList<NearNaruto>();
	BaseService httpNearService;
	BaseService httpUserLoveService;
	public HomeNearFragment() {
		super();		
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		httpNearService = new HttpNearService(getActivity());
		httpUserLoveService =new HttpUserLoveService(getActivity());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if(rootView==null){  
	        rootView=inflater.inflate(R.layout.fragment_home_near,container,false);  
	    }  
		ViewGroup parent = (ViewGroup) rootView.getParent();  
	    if (parent != null) {  
	        parent.removeView(rootView);  
	    }   
		loadView.initView(rootView);
		isPrepared=true;
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	@Override
	protected void initViews() {
		natutoAdapter = new NarutoAdapter(getActivity(),mHandler, nearNarutos);
		refreshViewLayout = (PullToRefreshListView) rootView.findViewById(R.id.near_list);
		refreshViewLayout.setMode(Mode.BOTH);
		refreshViewLayout.setAdapter(natutoAdapter);	
		refreshViewLayout.setEmptyView(rootView.findViewById(R.id.empty));
	
	}
	@Override
	protected void initEvents() {
		refreshViewLayout.setOnRefreshListener(this);
		refreshViewLayout.setRefreshing(true);
		
		
	}
	@Override
	protected void lazyLoad() {
		if (!isVisible||!isPrepared||isLoad) {
			return;
		}		
		//loadNearNaruto();
	}
	protected void loadNearNaruto() {
		httpNearService.addParams("distance", Page.MAX_DISTANCE);
		httpNearService.addParams("longitude", NarutoManager.longitude);
		httpNearService.addParams("latitude", NarutoManager.latitude);
		httpNearService.execute(this);
	}
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case User.USER_LOVE:
					Bundle bundle = msg.getData();
					String memberId =  bundle.getString("memberId");
					
					break;
				default:
					break;
			}
		};
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loading_error:
			loadNearNaruto();
			break;
		default:
			break;
		}

	}
	

	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		loadView.showLoadAfter(this);
		refreshViewLayout.onRefreshComplete();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if (tag.equals(httpNearService.TAG)) {
				List<HashMap<String, Object>> datas = (ArrayList<HashMap<String, Object>>) map.get(Constant.ReturnCode.RETURN_VALUE);
				NearNaruto user = null;
				int size = datas.size();
				HashMap<String, Object> userMap = null;
				for (int i = 0; i < size; i++) {
					user = new NearNaruto();
					userMap = datas.get(i);
					if (userMap.containsKey("portrait")) {
						user.setPortrait(Constant.SERVER_ADRESS+userMap.get("portrait").toString());
					}
					if (userMap.containsKey("memberId")) {
						user.setMemberId(userMap.get("memberId").toString());
					}
					if (userMap.containsKey("nickname")) {
						user.setNickname(userMap.get("nickname").toString());
					}
					if (userMap.containsKey("birthday")) {
						user.setBirthday(Integer.parseInt(userMap.get("birthday").toString()));
					}
					if (userMap.containsKey("filmId")) {
						user.setFilmId(Integer.parseInt(userMap.get("filmId").toString()));
					}
					if (userMap.containsKey("filmName")) {
						user.setFilmName(userMap.get("filmName").toString());
					}	
					if (userMap.containsKey("filmCnt")) {
						user.setFilmCnt(Integer.parseInt(userMap.get("filmCnt").toString()));
					}	
					if (userMap.containsKey("loveCnt")) {
						user.setLoveCnt(Integer.valueOf(userMap.get("loveCnt").toString()));
					}
					if (userMap.containsKey("sex")) {
						user.setSex(Integer.valueOf(userMap.get("sex").toString()));
					}
					if (userMap.containsKey("tryst")) {
						user.setTrystCnt(Integer.valueOf(userMap.get("trystCnt").toString()));
					}
					if (userMap.containsKey("faceCnt")) {
						user.setFaceCnt(Integer.valueOf(userMap.get("faceCnt").toString()));
					}
					if (userMap.containsKey("face")) {
						user.setFaceTtl(Long.valueOf(userMap.get("faceTtl").toString()));
					}
					nearNarutos.add(user);
				}
				natutoAdapter.notifyDataSetChanged();

			}else if(tag.equals(httpUserLoveService.TAG)){
				showToask("感谢^_^您的关注!");
				
			}
		} else if (Constant.ReturnCode.STATE_3.equals(code)) {
			//提示用户登陆
			Intent loginIntent = new Intent(getActivity(),LoginActivity.class);
			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			startActivity(loginIntent);
		}else {
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
		map=null;
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		refreshViewLayout.onRefreshComplete();
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
	public void onDestroyView() {
		super.onDestroyView();
		natutoAdapter = null;
		nearNarutos.clear();
	}
	@Override
	public void OnRequest() {
		if(!isLoad){
			loadView.showLoading(this);
			isLoad=true;
		}
		
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		page = 0;
		nearNarutos.clear();
		loadNearNaruto();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		page = 1;
		loadNearNaruto();
	}


}
