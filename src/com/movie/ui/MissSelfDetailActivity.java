package com.movie.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.movie.R;
import com.movie.adapter.MissNarutoAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.Constant.MissBtnStatus;
import com.movie.app.Constant.ReturnCode;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.Miss;
import com.movie.client.bean.MissNaruto;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpMissCancelService;
import com.movie.network.HttpMissDetailService;
import com.movie.network.HttpMissQueryService;
import com.movie.network.HttpMissUpdateService;
import com.movie.state.MissState;
import com.movie.state.MissTimeBtnBackColor;
import com.movie.state.MissTimeState;
import com.movie.state.SexState;
import com.movie.util.Horoscope;
import com.movie.util.StringUtil;
import com.movie.util.UserCharm;
import com.movie.view.LoadView;


public class MissSelfDetailActivity extends BaseActivity implements OnClickListener, CallBackService,OnRefreshListener2<ListView>,OnRefreshListener<ScrollView>  {

	public static final int RELOAGIN = 0x1;
	TextView title;
	ImageView missIcon;
	TextView missCreateUser;
	TextView missDate;
	TextView missMovieName;
	TextView cinemaName;
	TextView cinemaAddress;
	TextView cinemaPhone;
	TextView missBtn;
	TextView userSex;
	TextView loveView;
	TextView charmView;
	TextView userLove;
	TextView userConstell;
	TextView hopeUserView;
	TextView attendUserView;
	LinearLayout layoutCinemaAddress;
	LinearLayout missBottomBar;
	RatingBar userCharmBar;
	BaseService httpMissDetailService;
	BaseService httpMissQueryService;
	BaseService httpMissCancelService;
	BaseService httpMissUpdateService;
	PullToRefreshListView refreshableListView;
	PullToRefreshScrollView refreshableScollView;
	MissNarutoAdapter missNarutoAdapter;
	List<MissNaruto> missNarutos = new ArrayList<MissNaruto>();
	//ExpandListViewForScrollView missPartList;
	//PartNarutoExpandableAdapter partNarutoAdapter;
	//List<Dictionary> parents=new ArrayList<Dictionary>();
    //List<List<User>> childs = new ArrayList<List<User>>();、
	int btnType;
    LoadView loadView;
    View rootView;
    Miss miss;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(rootView==null){  
	        rootView=getLayoutInflater().inflate(R.layout.activity_miss_self_detail,null);  
	    }  
		loadView=new LoadView();
		setContentView(rootView);
		httpMissDetailService = new HttpMissDetailService(this);
		httpMissQueryService = new HttpMissQueryService(this);
		httpMissCancelService =new HttpMissCancelService(this);
		httpMissUpdateService =new HttpMissUpdateService(this);
		initViews();
		initEvents();
		initData();
		initPartUser();
	}
	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		userLove = (TextView) findViewById(R.id.userLove);
		loveView = (TextView) findViewById(R.id.love);
		userSex = (TextView) findViewById(R.id.user_sex);
		charmView = (TextView) findViewById(R.id.charm);
		userConstell = (TextView) findViewById(R.id.user_constell);
		userCharmBar = (RatingBar) findViewById(R.id.user_charm_bar);
		missIcon = (ImageView) findViewById(R.id.miss_icon);
		missCreateUser = (TextView) findViewById(R.id.miss_create_user);
		missDate = (TextView) findViewById(R.id.miss_date);
		missMovieName = (TextView) findViewById(R.id.miss_movie_name);
		cinemaName = (TextView) findViewById(R.id.cinema_name);
		cinemaAddress = (TextView) findViewById(R.id.cinema_address);
		cinemaPhone = (TextView) findViewById(R.id.cinema_phone);
		missBtn  = (TextView) findViewById(R.id.miss_btn);
		hopeUserView  = (TextView) findViewById(R.id.hope_user);
		attendUserView  = (TextView) findViewById(R.id.attend_user);
		layoutCinemaAddress = (LinearLayout) findViewById(R.id.miss_cinema_detail_panel);
		missBottomBar = (LinearLayout) findViewById(R.id.miss_bottom_bar);
		refreshableListView  = (PullToRefreshListView) findViewById(R.id.attend_user_list);
		refreshableScollView=(PullToRefreshScrollView) findViewById(R.id.miss_detail_view);
		//refreshableListView.setEmptyView(rootView.findViewById(R.id.empty));
		missNarutoAdapter =new MissNarutoAdapter(this, mHandler, missNarutos);
		missNarutoAdapter.setShowKickedOut(true);
		refreshableListView.setAdapter(missNarutoAdapter);	
		refreshableListView.setMode(Mode.PULL_FROM_END);
		refreshableListView.setFocusable(false);
		refreshableScollView.setMode(Mode.PULL_FROM_START);
		//missPartList = (ExpandListViewForScrollView) findViewById(R.id.miss_part_list);
		//partNarutoAdapter = new PartNarutoExpandableAdapter(this,mHandler, null,null);
		//missPartList.setAdapter(partNarutoAdapter);
		loadView.initView(rootView);
		
	}

	@Override
	protected void initEvents() {
		refreshableScollView.setOnRefreshListener(this);
		refreshableListView.setOnRefreshListener(this);
		layoutCinemaAddress.setOnClickListener(this);	
		hopeUserView.setOnClickListener(this);
		missIcon.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		miss = (Miss) getIntent().getSerializableExtra("miss");
		if(null==miss){
			return;
		}
		title.setText(miss.getNickName());
		missCreateUser.setText(miss.getNickName());
		imageLoader.displayImage(miss.getIcon(), missIcon,NarutoApplication.imageOptions);
		missDate.setText(StringUtil.getShortStrBySym(miss.getRunTime(),":"));
		missMovieName.setText(miss.getFilmName());
		cinemaName.setText(miss.getCinameName());
		cinemaAddress.setText(miss.getCinameAddress());
		cinemaPhone.setText(miss.getCinemaPhone());
		hopeUserView.setText(getResources().getString(R.string.hope_have));
		//partNarutoAdapter.setMiss(miss);
		/*初始化操作按钮*/
		initMissBtn();
		/*初始化约会详情信息*/
		loadMissDetail();
		/*加载应约人信息*/
		loadAttendUser();
	}
	private void initMissBtn(){
		//验证是否可以撤销
		int result=StringUtil.dateCompareByCurrent(miss.getRunTime(),MissBtnStatus.MAX_MISS_CANCEL_HOUR);
		missBtn.setBackgroundResource(MissTimeBtnBackColor.getState(result).getSourceId());
		missNarutoAdapter.setLoginMemberId(userService.getUserItem().getMemberId());
		missNarutoAdapter.setMemberId(miss.getMemberId());
		missNarutoAdapter.setTimeResult(result);
		if(result==MissTimeState.HaveInHand.getState()){
			missBtn.setText(getResources().getString(R.string.miss_cancel));
			missBtn.setOnClickListener(this);
			btnType=Miss.CANCLE_MISS;
			return;
		}
		//验证是否可以派影币
		result=StringUtil.dateCompareByCurrent(miss.getRunTime());
		if(result<=MissTimeState.Expired.getState()&&miss.getCoin()>0){
			missBtn.setText(getResources().getString(R.string.branch_coin));
			missBtn.setOnClickListener(this);
			btnType=Miss.COIN_MISS;
		}else{
			missBtn.setText(MissState.Completed.getMessage());
		}
	
	}
	private void loadMissDetail() {
		httpMissDetailService.addParams("trystId",miss.getTrystId());
		httpMissDetailService.execute(this);
	}
	private void loadAttendUser(){
		missNarutos.clear();
		httpMissQueryService.addUrls(Constant.Miss_Attend_Query_API_URL);
		httpMissQueryService.addParams("id", miss.getTrystId());
		httpMissQueryService.execute(this);
	}
	private void kickNaruto(String memberId){
		httpMissUpdateService.addUrls(Constant.Miss_Kick_API_URL);
		httpMissUpdateService.addParams("trystId", miss.getTrystId());
		httpMissUpdateService.addParams("memberId", memberId);
		httpMissUpdateService.execute(this);
	}
	private void cancelMiss() {
		httpMissCancelService.addParams("trystId", miss.getTrystId());
		httpMissCancelService.execute(this);
	}
	private void initPartUser(){
//		parents.clear();
//		childs.clear();
//		List<User> users=User.getTempData();
//		parents.add(new Dictionary(0, getResources().getString(R.string.miss_part_list)));
//		parents.add(new Dictionary(1, getResources().getString(R.string.miss_take_list)));
//		childs.add(users);
//		childs.add(new ArrayList<User>());
//		partNarutoAdapter.updateData(parents, childs);
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RELOAGIN:
			loadAttendUser();
			break;
		}
	}
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			
			case Miss.KICKED_OUT:
				Bundle bundle = msg.getData();
				String memberid = bundle.getString("memberid");
				int postion = bundle.getInt("position");
				missNarutos.remove(postion);
				missNarutoAdapter.notifyDataSetChanged();
				//踢出用户
				kickNaruto(memberid);
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
				if(btnType==Miss.CANCLE_MISS){
					cancelMiss();
				}else if(btnType==Miss.COIN_MISS){
					Intent userIntent=new Intent(this,MissTreatCoinActivity.class);
					userIntent.putExtra("trystId", miss.getTrystId());
					userIntent.putExtra("coin", miss.getCoin());
					startActivity(userIntent);
				}
				break;
			case R.id.hope_user:
				Intent hopeIntent = new Intent(this, HopeNarutoQueryActivity.class);
				hopeIntent.putExtra("trystId", miss.getTrystId());
				hopeIntent.putExtra("memberId",miss.getMemberId());
				startActivityForResult(hopeIntent, RELOAGIN);
				break;
			case R.id.miss_icon:
				Intent userIntent=new Intent(this,UserDetailActivity.class);
				userIntent.putExtra("memberId", miss.getMemberId());
				startActivity(userIntent);
				break;
			
			default:
				break;
		
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		this.finish();
	}
	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		hideProgressDialog();
		loadView.showLoadAfter(this);
		refreshableListView.onRefreshComplete();
		refreshableScollView.onRefreshComplete();
		isLoad=true;
		String code=map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag=map.get(Constant.ReturnCode.RETURN_TAG).toString();
			int faceTtl=0,faceCnt=0;
			if (tag.endsWith(httpMissDetailService.TAG)) {
				Map<String, Object> values = (Map<String, Object>) map.get(ReturnCode.RETURN_VALUE);
				if (values.containsKey("sex")) {
					userSex.setText(SexState.getState(Integer.parseInt(values.get("sex").toString())).getMessage());
				}
				if (values.containsKey("birthday")){
					int [] ds=StringUtil.strConvertInts(values.get("birthday").toString());
					userConstell.setText(Horoscope.getHoroscope((byte)ds[1],(byte)ds[2]).getCnName());
				}
				if (values.containsKey("loveCnt")) {
					loveView.setText(String.format(getResources().getString(R.string.user_love_count), values.get("loveCnt").toString()));
				}else{
					loveView.setText(String.format(getResources().getString(R.string.user_love_none)));
				}
				if(values.containsKey("faceTtl")){
					faceTtl=Integer.parseInt(values.get("faceTtl").toString());
				}
				if(values.containsKey("faceCnt")){
					faceCnt=Integer.parseInt(values.get("faceCnt").toString());
				}
				String score=UserCharm.GetScore(faceTtl,faceCnt<=0?1:faceCnt);
				if(!score.equals("NaN")){
					userCharmBar.setRating(Float.valueOf(score)/2f);
					charmView.setText(score);
				}			
			}else if(tag.endsWith(httpMissQueryService.TAG)){
				List<HashMap<String, Object>> datas = (ArrayList<HashMap<String, Object>>) map.get(Constant.ReturnCode.RETURN_VALUE);
				MissNaruto missNaruto = null;
				int size = datas.size();
				int count=0;
				HashMap<String, Object> dataMap = null;
				attendUserView.setText(getResources().getString(R.string.attend_none));
				for (int i = 0; i < size; i++) {
					missNaruto = new MissNaruto();
					dataMap = datas.get(i);
					if (dataMap.containsKey("memberId")){
						count++;
						missNaruto.setMemberId(dataMap.get("memberId").toString());
					}else{
						continue;
					}
					if (dataMap.containsKey("portrait"))
						missNaruto.setPortrait(Constant.SERVER_ADRESS+dataMap.get("portrait").toString());
					if (dataMap.containsKey("nickname"))
						missNaruto.setNickname(dataMap.get("nickname").toString());
					if (dataMap.containsKey("birthday"))
						missNaruto.setBirthday(Integer.parseInt((dataMap.get("birthday").toString())));
					if (dataMap.containsKey("sex"))
						missNaruto.setSex(Integer.parseInt((dataMap.get("sex").toString())));
					if (dataMap.containsKey("loveCnt"))
						missNaruto.setLoveCnt(Integer.parseInt((dataMap.get("loveCnt").toString())));
					if (dataMap.containsKey("faceTtl"))
						missNaruto.setFaceTtl(Long.parseLong((dataMap.get("faceTtl").toString())));
					if (dataMap.containsKey("faceCnt"))
						missNaruto.setFaceCnt(Integer.parseInt((dataMap.get("faceCnt").toString())));
					if (dataMap.containsKey("trystCnt"))
						missNaruto.setTrystCnt(Integer.parseInt((dataMap.get("trystCnt").toString())));
					if (dataMap.containsKey("filmCnt"))
						missNaruto.setFilmCnt(Integer.parseInt((dataMap.get("filmCnt").toString())));
					if (dataMap.containsKey("stage"))
						missNaruto.setStage(Integer.parseInt((dataMap.get("stage").toString())));
					missNarutos.add(missNaruto);
				}
				if(count>0){
					attendUserView.setText(String.format(getResources().getString(R.string.attend_have), size));
				}
				setListViewHeight(missNarutoAdapter, refreshableListView);
		
			}else if(tag.endsWith(httpMissCancelService.TAG)){
				missBottomBar.setVisibility(View.GONE);
				showToask("撤销成功!");
			}
			
		}else{
			String message=map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		hideProgressDialog();
		refreshableListView.onRefreshComplete();
		refreshableScollView.onRefreshComplete();
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		int state=Integer.parseInt(map.get(Constant.ReturnCode.RETURN_STATE).toString());
		if(state==Integer.parseInt(ReturnCode.STATE_999)){
			loadView.showLoadLineFail(this);
		}else if(state>=Integer.parseInt(ReturnCode.STATE_97)){
			loadView.showLoadFail(this, this);
		}else{
			showToask(message);
		}
		
	}

	@Override
	public void OnRequest() {
		if(!isLoad){
			loadView.showLoading(this);
		}else{
			showProgressDialog();
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		httpMissDetailService = null;
		httpMissQueryService = null;
		httpMissCancelService =null;
		httpMissUpdateService =null;
		missNarutoAdapter=null;
		missNarutos.clear();
	}
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		
		
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadAttendUser();
	}
	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
		loadMissDetail();
		loadAttendUser();
	}
	
	
	

}
