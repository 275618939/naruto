package com.movie.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.MissUserAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.client.bean.Miss;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpMissAgreeService;
import com.movie.network.HttpMissUserQueryService;
import com.movie.view.RefreshableListView;
import com.movie.view.RefreshableListView.PullToRefreshListener;

public class MissUserQueryActivity extends BaseActivity implements
		OnClickListener, CallBackService, PullToRefreshListener {

	public static final int REFRESH_COMPLETE = 0X110;

	TextView title;
	ListView userViewList;
	MissUserAdapter userAdapter;
	List<User> users = new ArrayList<User>();
	BaseService httpMissAgreeService;
	BaseService httoMissUserQueryService;
	RefreshableListView refreshableListView;
	Miss miss;
	int page;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_miss_user_query);
		httpMissAgreeService = new HttpMissAgreeService(this);
		httoMissUserQueryService = new HttpMissUserQueryService(this);
		initViews();
		initEvents();
		initData();
	}
	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		userViewList = (ListView) findViewById(R.id.miss_user_list);
		userAdapter = new MissUserAdapter(this, mHandler, null);
		userViewList.setAdapter(userAdapter);
		refreshableListView = (RefreshableListView) findViewById(R.id.refresh_user);
	}

	@Override
	protected void initEvents() {
		refreshableListView.setOnRefreshListener(this, 0);		
	}
	@Override
	protected void initData() {
		page = 0;
		title.setText("参与会员");
		miss = (Miss) getIntent().getSerializableExtra("miss");
		if (null != miss) {
			//userAdapter.updateData(miss.getAttend());
		}
		loadUser();		
	}
	private void loadUser() {
		httoMissUserQueryService.addParams("trystId", miss.getTrystId());
		httoMissUserQueryService.addParams("page", page);
		httoMissUserQueryService.addParams("size", Page.DEFAULT_SIZE);
		httoMissUserQueryService.execute(this);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				loadUser();
				break;
			case Miss.AGREE_MISS:
				Bundle bundle = msg.getData();
				User user = (User) bundle.getSerializable("user");
				agreeMiss(user);
			default:
				break;

			}
		};
	};

	private void agreeMiss(User user) {

		if (null != user) {
			List<String> users = new ArrayList<String>();
			users.add(user.getMemberId());
			httpMissAgreeService.addParams("trystId", miss.getTrystId());
			httpMissAgreeService.addParams("members", users);
			httpMissAgreeService.execute(this);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;

		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		refreshableListView.finishRefreshing();
		hideProgressDialog();
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
				userAdapter.notifyDataSetChanged();
			}

		} else {

			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}

	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {

		hideProgressDialog();
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
	}

	@Override
	public void OnRequest() {
		showProgressDialog("提示", "请稍后......");

	}
	@Override
	public void onRefresh() {
		mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		userAdapter=null;
		users.clear();
	}

	
}
