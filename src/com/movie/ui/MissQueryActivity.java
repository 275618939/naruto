package com.movie.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.MissQueryAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.client.bean.Miss;
import com.movie.client.bean.Movie;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpMissCancelService;
import com.movie.network.HttpMissQueryService;
import com.movie.util.Images;
import com.movie.view.RefreshableListView;
import com.movie.view.RefreshableListView.PullToRefreshListener;

public class MissQueryActivity extends BaseActivity implements OnClickListener,CallBackService, PullToRefreshListener {

	public static final int REFRESH_COMPLETE = 0X110;
	TextView title;
	ListView myMissList;
	MissQueryAdapter missQueryAdapter;
	BaseService missQueryService;
	BaseService httpMissCancelService;
	RefreshableListView refreshableListView;
	List<Miss> misses = new ArrayList<Miss>();
	int page;
	int missType;
	Object queryCondition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_miss_user_query);
		missQueryService = new HttpMissQueryService(this);
		httpMissCancelService = new HttpMissCancelService(this);
		initViews();
		initEvents();
		initData();
	}
	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		myMissList = (ListView) findViewById(R.id.miss_user_list);
		missQueryAdapter = new MissQueryAdapter(this, mHandler, null);
		myMissList.setAdapter(missQueryAdapter);
		refreshableListView = (RefreshableListView) findViewById(R.id.refresh_user);
	
	}

	@Override
	protected void initEvents() {
		refreshableListView.setOnRefreshListener(this, 0);
	}

	@Override
	protected void initData() {
		page=0;
		missType = getIntent().getIntExtra(Miss.MISS_KEY, Miss.MY_MISS);
		queryCondition=getIntent().getSerializableExtra(Miss.CONDITION_KEY);
		missQueryAdapter.setMissType(missType);
		loadMissData();
	}

	private void loadMissData() {
		switch (missType) {
		case Miss.MY_MISS:
			title.setText("发起的约会");
			missQueryService.addUrls(Constant.Miss_Query_API_URL);
			missQueryService.addParams("page", page);
			missQueryService.addParams("size", Page.DEFAULT_SIZE);
			missQueryService.execute(this);
			break;
		case Miss.MY_PART:
			title.setText("参与的约会");
			missQueryService.addUrls(Constant.Miss_Touch_Query_API_URL);
			missQueryService.addParams("page", page);
			missQueryService.addParams("size", Page.DEFAULT_SIZE);
			missQueryService.execute(this);
			break;
		case Miss.MY_INVITATION:
			title.setText("应邀的约会");
			missQueryService.addUrls(Constant.Miss_Attend_Query_API_URL);
			missQueryService.addParams("page", page);
			missQueryService.addParams("size", Page.DEFAULT_SIZE);
			missQueryService.execute(this);
			break;
		case Miss.USER_INVITATION:
			if(null!=queryCondition){
				User user=(User)queryCondition;
				title.setText(user.getNickname()+"正在进行的约会");
				missQueryService.addUrls(Constant.Miss_Touch_Query_API_URL);
				missQueryService.addParams("id", user.getMemberId());
				missQueryService.addParams("page", page);
				missQueryService.addParams("size", Page.DEFAULT_SIZE);
				missQueryService.execute(this);
			}
			break;
		case Miss.MOVIE_INVITATION:
			if(null!=queryCondition){
				Movie movie=(Movie)queryCondition;
				title.setText(movie.getName()+"正在进行的约会");
				missQueryService.addUrls(Constant.Miss_Film_Query_API_URL);
				missQueryService.addParams("id", movie.getId());
				missQueryService.addParams("page", page);
				missQueryService.addParams("size", Page.DEFAULT_SIZE);
				missQueryService.execute(this);
			}
			break;
		default:
			break;
		}

	}

	private void cancelMiss() {
		httpMissCancelService.execute(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;

		}

	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				loadMissData();
				break;
			case Miss.CANCLE_MISS:
				cancelMiss();
			default:
				break;

			}
		};
	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		this.finish();
	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		refreshableListView.finishRefreshing();
		hideProgressDialog();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if (tag.endsWith(missQueryService.TAG)) {
				List<HashMap<String, Object>> datas = (ArrayList<HashMap<String, Object>>) map.get(Constant.ReturnCode.RETURN_VALUE);
				Miss miss = null;
				int size = datas.size();
				HashMap<String, Object> missMap = null;
				for (int i = 0; i < size; i++) {
					miss = new Miss();
					missMap = datas.get(i);
					if (missMap.containsKey("trystId"))
						miss.setTrystId(missMap.get("trystId").toString());
					if (missMap.containsKey("memberId"))
						miss.setMemberId(missMap.get("memberId").toString());
					if (missMap.containsKey("filmId"))
						miss.setFilmId(Integer.parseInt(missMap.get("filmId").toString()));
					if (missMap.containsKey("runTime"))
						miss.setRunTime(missMap.get("runTime").toString());
					if (missMap.containsKey("coin"))
						miss.setCoin(Integer.parseInt(missMap.get("coin").toString()));
					if (missMap.containsKey("cinemaId")) {

					}
					if (missMap.containsKey("attend")) {

					}
					if (missMap.containsKey("status")) {
						miss.setStatus(Integer.parseInt(missMap.get("status").toString()));
					}
					misses.add(miss);
				}
				if (size >= Page.DEFAULT_SIZE) {
					page++;
				}

				missQueryAdapter.notifyDataSetChanged();
				if (size <= 0) {
					tempData();
				}

			}
		} else {
			tempData();
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}

	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {

		hideProgressDialog();
		tempData();
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
	}

	@Override
	public void OnRequest() {
		showProgressDialog("提示", "请稍后......");

	}

	private void tempData() {
		Random random=new Random();
		int stage=random.nextInt(4)+ 1;
		refreshableListView.finishRefreshing();
		Miss miss = new Miss();
		miss.setCinemaId("016fc79c22d5b3fc");
		miss.setStatus(1);
		miss.setCoin(0);
		miss.setTrystId("016fc7cd5300bb03");
		miss.setMemberId("016f9266086f4fab");
		miss.setRunTime("2015-10-30 10: 21: 00");
		miss.setFilmId(2015103001);
		miss.setStage(stage);
		miss.setCinameName("博纳国际影城通州店");
		miss.setCinameAddress("北京市通州区杨庄北里天时名苑14号楼F4-01");
		User user = null;
		List<User> users = new ArrayList<User>();
		List<Integer> hobbies = new ArrayList<Integer>();
		hobbies.add(1);
		hobbies.add(2);
		hobbies.add(3);
		for (int i = 0; i < 10; i++) {
			user = new User();
			user.setNickname("鸣人");
			user.setSignature("伙影，用了还想用!");
			user.setPortrait(Images.imageUrls[i]);
			user.setSex(1);
			user.setCharm(200);
			user.setLove(6000);
			user.setPortrait("http://101.200.176.217/portrait-img/abd8f54409da48512735580950f70355.jpg");
			user.setMemberId("016f8b8161a12f2e");
			user.setHobbies(hobbies);
			users.add(user);
		}
		miss.setAttend(users);
		misses.add(miss);
		missQueryAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		missQueryAdapter=null;
	}

	
}
