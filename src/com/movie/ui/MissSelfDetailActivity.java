package com.movie.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.PartNarutoExpandableAdapter;
import com.movie.app.Constant;
import com.movie.app.Constant.MissBtnStatus;
import com.movie.app.Constant.ReturnCode;
import com.movie.client.bean.Dictionary;
import com.movie.client.bean.Miss;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpUserService;
import com.movie.util.ImageLoaderCache;
import com.movie.util.StringUtil;
import com.movie.view.ExpandListViewForScrollView;


public class MissSelfDetailActivity extends BaseActivity implements OnClickListener, CallBackService {


	protected static int LOADUSERCOMPLETE=1;
	Miss miss;
	TextView title;
	ImageView missIcon;
	TextView missCreateUser;
	TextView missDate;
	TextView missMovieName;
	TextView cinemaName;
	TextView cinemaAddress;
	TextView cinemaPhone;
	TextView missBtn;
	LinearLayout layoutCinemaAddress;
	ScrollView missDetailView;
	BaseService httpUsersService;
	ExpandListViewForScrollView missPartList;
	PartNarutoExpandableAdapter partNarutoAdapter;
	ImageLoaderCache imageLoaderCache;
	List<Dictionary> parents=new ArrayList<Dictionary>();
    List<List<User>> childs = new ArrayList<List<User>>();
    User user;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_miss_self_detail);
		httpUsersService = new HttpUserService(this);
		imageLoaderCache=new ImageLoaderCache(this);
		initViews();
		initData();
		initPartUser();
	}

	private void initViews() {
		
		missDetailView = (ScrollView)findViewById(R.id.miss_detail_view);
		missDetailView.smoothScrollTo(0, 0);
		title = (TextView) findViewById(R.id.title);
		missIcon = (ImageView) findViewById(R.id.miss_icon);
		missCreateUser = (TextView) findViewById(R.id.miss_create_user);
		missDate = (TextView) findViewById(R.id.miss_date);
		missMovieName = (TextView) findViewById(R.id.miss_movie_name);
		cinemaName = (TextView) findViewById(R.id.cinema_name);
		cinemaAddress = (TextView) findViewById(R.id.cinema_address);
		cinemaPhone = (TextView) findViewById(R.id.cinema_phone);
		missBtn  = (TextView) findViewById(R.id.miss_btn);
		layoutCinemaAddress = (LinearLayout) findViewById(R.id.miss_cinema_detail_panel);
		missPartList = (ExpandListViewForScrollView) findViewById(R.id.miss_part_list);
		partNarutoAdapter = new PartNarutoExpandableAdapter(this,mHandler, null,null);
		missPartList.setAdapter(partNarutoAdapter);
		layoutCinemaAddress.setOnClickListener(this);
	}

	private void initData() {

		miss = (Miss) getIntent().getSerializableExtra("miss");
		if(null==miss){
			return;
		}
		title.setText(miss.getMovieName());
		imageLoaderCache.DisplayImage(miss.getIcon(), missIcon);
		missCreateUser.setText(miss.getCreateUserName());
		missDate.setText(StringUtil.getShortStrBySym(miss.getRunTime(),":"));
		missMovieName.setText(miss.getMovieName());
		cinemaName.setText(miss.getCinameName());
		cinemaAddress.setText(miss.getCinameAddress());
		cinemaPhone.setText(miss.getCinemaPhone());
		partNarutoAdapter.setMiss(miss);
		/*初始化操作按钮*/
		initMissBtn();
		/*初始化用不信息*/
		loadUser();
	}
	private void initMissBtn(){
		//验证是否可以撤销
		int result=StringUtil.dateCompareByCurrent(miss.getRunTime(),MissBtnStatus.MAX_MISS_CANCEL_HOUR);
		if(result<0){
			missBtn.setVisibility(View.VISIBLE);
			missBtn.setText(getResources().getString(R.string.miss_cancel));
			missBtn.setOnClickListener(this);
		}
		//验证是否可以评价
		result=StringUtil.dateCompareByCurrent(miss.getRunTime());
		if(result>0){
			missBtn.setVisibility(View.VISIBLE);
			missBtn.setText(getResources().getString(R.string.branch_coin));
			missBtn.setOnClickListener(this);
		}
		
	}
	private void loadUser() {
		httpUsersService.addParams(httpUsersService.URL_KEY,Constant.User_API_URL);
		httpUsersService.execute(this);
	}
	private void initPartUser(){
		parents.clear();
		childs.clear();
		List<User> users=User.getTempData();
		parents.add(new Dictionary(0, getResources().getString(R.string.miss_part_list)));
		parents.add(new Dictionary(1, getResources().getString(R.string.miss_take_list)));
		childs.add(users);
		childs.add(new ArrayList<User>());
		partNarutoAdapter.updateData(parents, childs);
	}
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			
			case Miss.KICKED_OUT:
				Bundle bundle = msg.getData();
				User user = (User) bundle.getSerializable("user");
				//踢出用户
				break;
			case Miss.EVLATOIN_USER:
				//评价用户
				break;
		
			default:
				break;

			}
		};
	};

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
			case R.id.miss_cinema_detail_panel:
				Intent intent = new Intent(this, CinemaMapActivity.class);
				intent.putExtra("miss", miss);
				startActivity(intent);
				break;
			case R.id.miss_btn:
				
				break;
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
		hideProgressDialog();
		String code=map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag=map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if (tag.endsWith(httpUsersService.TAG)) {
				user = new User();
				Map<String, Object> values = (Map<String, Object>) map.get(ReturnCode.RETURN_VALUE);
				user.setMemberId(values.get("memberId").toString());
				if (values.containsKey("portrait"))
					user.setPortrait(values.get("portrait").toString());
				if (values.containsKey("sex"))
					user.setSex(Integer.parseInt(values.get("sex").toString()));
				if (values.containsKey("nickname"))
					user.setNickname(values.get("nickname").toString());
					title.setText(values.get("nickname").toString());
				if (values.containsKey("signature"))
					user.setSignature(values.get("signature").toString());
				if (values.containsKey("love"))
					user.setLove(Integer.parseInt(values.get("love").toString()));		
				if (values.containsKey("charm"))
					user.setLove(Integer.parseInt(values.get("charm").toString()));
				if(values.containsKey("tryst"))
					user.setTryst(Integer.parseInt(values.get("tryst").toString()));
			}
			
		}else{
			String message=map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String message=map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
		
	}

	@Override
	public void OnRequest() {
		showProgressDialog("提示", "正在加载，请稍后....");		
	}
	
	

}
