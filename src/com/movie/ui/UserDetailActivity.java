package com.movie.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.movie.R;
import com.movie.adapter.EvaluationAdapter;
import com.movie.adapter.UserPhotoMangeGridAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.app.Constant.ReturnCode;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.Miss;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.client.service.FilmTypeService;
import com.movie.client.service.HobbyService;
import com.movie.client.service.UserService;
import com.movie.network.HttpUserCommentService;
import com.movie.network.HttpUserFilmTypeService;
import com.movie.network.HttpUserImageQueryService;
import com.movie.network.HttpUserLoveService;
import com.movie.network.HttpUserService;
import com.movie.state.BackGroundColor;
import com.movie.state.SexState;
import com.movie.util.Bimp;
import com.movie.util.Horoscope;
import com.movie.util.ImageItem;
import com.movie.util.StringUtil;
import com.movie.util.UserCharm;
import com.movie.view.LoadView;

public class UserDetailActivity extends BaseActivity implements
		OnClickListener, CallBackService,OnRefreshListener<ScrollView> {
	
	User user;
	ImageItem imageItem;
	String memberId;
	TextView title;
	TextView right_text;
	ImageView headView;
	RelativeLayout hobbyMore;
	BaseService httpUsersService;
	BaseService httpUserCommentService;
	BaseService httpUserFilmTyService;
	BaseService httpUserLoveService;
	BaseService httpUserQueryImageService;
	UserService userService;
	HobbyService hobbyService;
	FilmTypeService filmTypeService;
	EvaluationAdapter evaluationAdapter;
	GridView commentsView;
	GridView photoGridview;
	UserPhotoMangeGridAdapter photoGridAdapter;
	PullToRefreshScrollView refreshableScollView;
	TextView userSex;
	TextView loveView;
	TextView charmView;
	TextView signView;
	TextView commnetsMore;
	TextView userMoviesPre;
	TextView userMoviesWant;
	TextView userMiss;
	TextView userLove;
	TextView userConstell;
	TextView hobbyArrowMore;
	TextView userInviteView;
	RatingBar userCharmBar;
	LinearLayout hobbiesLayout;
	LinearLayout commentsLayout;
	LinearLayout layoutMoviesPre;
	LinearLayout userDetailTool;
	LinearLayout layoutShowPre;
	RelativeLayout userDetailParent;
	RelativeLayout layoutDynamic;
	List<Map<Integer, Integer>> comments = new ArrayList<Map<Integer, Integer>>();
	Map<Integer,String> hobbiesMap;
	Map<Integer,String> filmTypeMap;
	boolean isLove;
	LoadView loadView;
	User loginUser;
	String requestTag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_detail);
		httpUsersService = new HttpUserService(this);
		httpUserCommentService = new HttpUserCommentService(this);
		httpUserFilmTyService = new HttpUserFilmTypeService(this);
		httpUserLoveService = new HttpUserLoveService(this);
		httpUserQueryImageService = new HttpUserImageQueryService(this);
		hobbyService = new HobbyService();
		userService = new UserService();
		filmTypeService = new FilmTypeService();
		initViews();
		initEvents();
		initData();
	}
	@Override
	protected void initViews() {
		userDetailParent = (RelativeLayout) findViewById(R.id.user_detail_parent);
		layoutDynamic = (RelativeLayout) findViewById(R.id.layout_dynamic);
		loadView = new LoadView(userDetailParent);
		evaluationAdapter = new EvaluationAdapter(this,comments);
		title = (TextView) findViewById(R.id.title);
		headView = (ImageView) findViewById(R.id.user_poster);
		commentsView = (GridView) findViewById(R.id.comments);
		userSex = (TextView) findViewById(R.id.user_sex);
		loveView = (TextView) findViewById(R.id.love);
		charmView = (TextView) findViewById(R.id.charm);
		signView = (TextView) findViewById(R.id.sign);
		userMoviesWant = (TextView) findViewById(R.id.user_movies_want);
		userMiss = (TextView) findViewById(R.id.user_miss);
		userMoviesPre = (TextView) findViewById(R.id.user_movies_pre);
		userConstell = (TextView) findViewById(R.id.user_constell);
		hobbiesLayout = (LinearLayout) findViewById(R.id.hobbies);
		commentsLayout = (LinearLayout) findViewById(R.id.comments_layout);
		layoutMoviesPre = (LinearLayout) findViewById(R.id.layout_movies_pre);
		userDetailTool  = (LinearLayout) findViewById(R.id.user_detail_tool);
		layoutShowPre  = (LinearLayout) findViewById(R.id.layout_show_pre);
		hobbyMore = (RelativeLayout) findViewById(R.id.hobby_arrow);
		commnetsMore = (TextView) findViewById(R.id.comments_more);
		hobbyArrowMore = (TextView) findViewById(R.id.hobby_arrow_more);
		userLove = (TextView) findViewById(R.id.userLove);
		userInviteView=(TextView)findViewById(R.id.user_invite);
		userCharmBar = (RatingBar) findViewById(R.id.user_charm_bar);
		refreshableScollView=(PullToRefreshScrollView) findViewById(R.id.user_detail_view);
		refreshableScollView.setMode(Mode.PULL_FROM_START);
		commentsView.setAdapter(evaluationAdapter);
		commentsView.setSelector(new ColorDrawable(Color.TRANSPARENT));	
		refreshableScollView.setMode(Mode.PULL_FROM_START);
		photoGridview = (GridView)findViewById(R.id.userPhotoGridview);
		photoGridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		photoGridAdapter =new UserPhotoMangeGridAdapter(this,mHandler,Bimp.tempSelectBitmap);
		photoGridview.setAdapter(photoGridAdapter);
		photoGridAdapter.setUnEnableDelete(true);
		
	}

	@Override
	protected void initEvents() {
		refreshableScollView.setOnRefreshListener(this);
		userLove.setOnClickListener(this);
		userInviteView.setOnClickListener(this);
		layoutDynamic.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		memberId = getIntent().getStringExtra("memberId");
		hobbiesMap= hobbyService.getHobbyMap();
		filmTypeMap=filmTypeService.getFilmTypeMap();
		refreshableScollView.setRefreshing();
		loginUser = userService.getUserItem();
		loadUser();
		loadUserImage();
		loadUserFilmType();
		
	}
	private void loadUserImage(){
		httpUserQueryImageService.addParams("memberId", memberId);
		httpUserQueryImageService.execute(this);
	}

	@Override
	public void onStart(){
		super.onStart();
		if(null!=loginUser){
			if(memberId.equals(loginUser.getMemberId())){
				userDetailTool.setVisibility(View.GONE);
			}
		}
	}

	/* 获取传递过来的数据 */
	private void loadUser() {
		requestTag=httpUsersService.TAG;
		httpUsersService.addParams("userId", memberId);
		httpUsersService.addParams(httpUsersService.URL_KEY,Constant.User_Query_API_URL);
		httpUsersService.execute(this);
	}
	private void loadUserComments(){
		httpUserCommentService.addParams("memberId", memberId);
		httpUserCommentService.execute(this);
	}
	private void loadUserFilmType(){
		
		httpUserFilmTyService.addParams("page",0);
		httpUserFilmTyService.addParams("memberId", memberId);
		httpUserFilmTyService.addParams("size",Constant.Page.WANT_SEE_MOIVE_SIZE);
		httpUserFilmTyService.execute(this);
	}
	private void putLove(){
		isLove=true;
		requestTag=httpUserLoveService.TAG;
		httpUserLoveService.addParams("memberId", memberId);
		httpUserLoveService.execute(this);
	}
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {}
		};
	};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hobby_arrow:
			Intent intent = new Intent(this, UserShowHobbyActivity.class);
			intent.putExtra("user", user);
			startActivity(intent);
			break;
		case R.id.comments_more:
			Intent commentsIntent = new Intent(this, CommentsActivity.class);
			startActivity(commentsIntent);
			break;
		case R.id.user_movies_want:
			Intent movieIntent = new Intent(this, UserLoveMovieActivity.class);
			movieIntent.putExtra("user", user);
			startActivity(movieIntent);
			break;
		case R.id.user_miss:
			Intent missIntent = new Intent(this, MissQueryActivity.class);
			missIntent.putExtra(Miss.MISS_KEY, Miss.USER_INVITATION);
			missIntent.putExtra(Miss.CONDITION_KEY, user);
			startActivity(missIntent);
			break;
		case R.id.layout_dynamic:
			Intent dynamicIntent = new Intent(this,DynamicQueryActivity.class);
			dynamicIntent.putExtra("user", user);
			dynamicIntent.putExtra("memberId", user.getMemberId());
			startActivity(dynamicIntent);
			break;
		case R.id.loading_error:
			loadUser();
			loadUserFilmType();
			break;
		case R.id.userLove:
			if(isLove) {
				showToask("您已经心动过了哟!");
				return;
			}
			putLove();
			break;
		case R.id.user_invite:
			Intent inviteIntent = new Intent(this, MissSelfQueryActivity.class);
			inviteIntent.putExtra("memberId", user.getMemberId());
			inviteIntent.putExtra(Miss.MISS_KEY, Miss.INVITE_MISS);
			startActivity(inviteIntent);
			finish();
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
		refreshableScollView.onRefreshComplete();
		hideProgressDialog();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if (tag.endsWith(httpUsersService.TAG)) {
				user = new User();
				Map<String, Object> values = (Map<String, Object>) map.get(ReturnCode.RETURN_VALUE);
				if (values.containsKey("memberId")) {
					user.setMemberId(values.get("memberId").toString());
				}
				if (values.containsKey("portrait")) {
					user.setPortrait(Constant.SERVER_ADRESS+values.get("portrait").toString());
					imageLoader.displayImage(user.getPortrait(),headView,NarutoApplication.imageOptions);
				}
				if (values.containsKey("sex")) {
					user.setSex(Integer.parseInt(values.get("sex").toString()));
					userSex.setText(SexState.getState(Integer.parseInt(values.get("sex").toString())).getMessage());
				}
				if (values.containsKey("birthday")) {
					user.setBirthday(values.get("birthday").toString());
					int [] date=StringUtil.strConvertInts(user.getBirthday());
					userConstell.setText(Horoscope.getHoroscope((byte)date[1],(byte)date[2]).getCnName());
				}
				if (values.containsKey("nickname")) {
					user.setNickname(values.get("nickname").toString());
					title.setText(values.get("nickname").toString());
				}
				if (values.containsKey("signature")) {
					user.setSignature(values.get("signature").toString());
					signView.setText(values.get("signature").toString());
				}
				if (values.containsKey("loveCnt")) {
					user.setLove(Integer.parseInt(values.get("loveCnt").toString()));
					loveView.setText(String.format(getResources().getString(R.string.user_love_count), user.getLove()));
				}
				if(values.containsKey("faceTtl")){
					user.setFace(Integer.parseInt(values.get("faceTtl").toString()));
				}
				if(values.containsKey("faceCnt")){
					user.setFaceCnt(Integer.parseInt(values.get("faceCnt").toString()));
				}
				String score=UserCharm.GetScore(user.getFace(), user.getFaceCnt()<=0?1:user.getFaceCnt());
				if(!score.equals("NaN")){
					userCharmBar.setRating(Float.valueOf(score)/2f);
					charmView.setText(score);
				}				
				if (values.containsKey("hobbies")) {
					List<Integer> hobbies = (List<Integer>) values.get("hobbies");
					user.setHobbies(hobbies);
					TextView textView = null;
					if (null != hobbies) {
						int size = hobbies.size();
						for (int i = 0; i < size; i++) {
							if(i>=Page.MAXHOBBIES){
								break;
							}
							textView = new TextView(new ContextThemeWrapper(this, R.style.common_tag), null, 0);
							int key = hobbies.get(i);
							String value = hobbiesMap.get(key);
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
							params.setMargins(0, 0, 10, 0);
							textView.setLayoutParams(params);
							textView.setText(value);
							textView.setBackgroundResource(BackGroundColor.getState(i).getSourceId());
							hobbiesLayout.addView(textView);
						}
						if (size > Page.MAXHOBBIES) {
							hobbyArrowMore.setVisibility(View.VISIBLE);
							hobbyMore.setOnClickListener(this);
						}
					}
					if(user.getFilmType()!=null&&user.getFilmType().size()>0){
						layoutMoviesPre.setVisibility(View.VISIBLE);
						StringUtil.listToStringByMap(user.getFilmType(), filmTypeMap, "/");
					}
					
				}
				if(values.containsKey("trystCnt")){
					int tryst=Integer.parseInt(values.get("trystCnt").toString());
					userMiss.setText(String.format(getResources().getString(R.string.miss_have),tryst));
					user.setTryst(tryst);
					userMiss.setOnClickListener(this);
				}else{
					userMiss.setText(String.format(getResources().getString(R.string.miss_none)));
				}
				if(values.containsKey("filmCnt")){
					int filmCnt=Integer.parseInt(values.get("filmCnt").toString());
					user.setFilmCnt(filmCnt);
					userMoviesWant.setText(String.format(getResources().getString(R.string.movie_have),filmCnt));
					userMiss.setOnClickListener(this);
				}else{
					userMoviesWant.setText(String.format(getResources().getString(R.string.movie_none)));
				}
				
				
				initTempComments();

			}else if(tag.endsWith(httpUserCommentService.TAG)){
				List<Map<Integer,Integer>> values = (List<Map<Integer,Integer>>) map.get(ReturnCode.RETURN_VALUE);
				if (null != values) {
					comments = new ArrayList<Map<Integer, Integer>>();
					int size = 0;
					if (size > 0) {
						commentsLayout.setVisibility(View.VISIBLE);
						for (int i = 0; i < size; i++) {
							 comments.add(values.get(i));
						}
						evaluationAdapter.updateData(comments);
					} 
					if(size>Page.COMMENTS_MAX_SHOW){
						commnetsMore.setVisibility(View.VISIBLE);
						commnetsMore.setOnClickListener(this);
					}
					
				}
			}else if(tag.endsWith(httpUserFilmTyService.TAG)){
				List<Map<String,Object>> values = (List<Map<String,Object>>) map.get(ReturnCode.RETURN_VALUE);
				if (null != values&&values.size()>0) {
					userMoviesWant.setText(String.format(getResources().getString(R.string.movie_have),String.valueOf(values.size())));
					userMoviesWant.setOnClickListener(this);
				}
				
			}else if(tag.endsWith(httpUserLoveService.TAG)){
				loadUser();
			}else if (tag.equals(httpUserQueryImageService.TAG)) {
				Bimp.tempSelectBitmap.clear();
				List<HashMap<String, Object>> datas = (ArrayList<HashMap<String, Object>>) map.get(Constant.ReturnCode.RETURN_VALUE);
				int size = datas.size();
				if(size>0){
					layoutShowPre.setVisibility(View.VISIBLE);
				}else{
					layoutShowPre.setVisibility(View.GONE);
				}
				HashMap<String, Object> missMap = null;
				for (int i = 0; i < size; i++) {
					imageItem = new ImageItem();
					imageItem.setSelected(false);
					missMap = datas.get(i);
					if (missMap.containsKey("id"))
						imageItem.setImageId(missMap.get("id").toString());
					if (missMap.containsKey("url"))
						imageItem.setImagePath(Constant.SERVER_ADRESS+missMap.get("url").toString());
		
					Bimp.tempSelectBitmap.add(imageItem);
				}
				photoGridAdapter.notifyDataSetChanged();
			}
		}else if (Constant.ReturnCode.STATE_3.equals(code)) {
			Intent loginIntent = new Intent(this,LoginActivity.class);
			this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			startActivity(loginIntent);
			this.finish();
		} else {
			
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
		map=null;
	}
	private void initTempComments(){
		commentsLayout.setVisibility(View.VISIBLE);
		commnetsMore.setVisibility(View.VISIBLE);
		commnetsMore.setOnClickListener(this);
		Map<Integer, Integer> maps = null;
		Random random = new Random();
		for (int i = 0; i < Constant.Page.COMMENTS_MAX_SHOW; i++) {
			maps = new HashMap<Integer, Integer>();
			maps.put(i+1, random.nextInt(100));
			comments.add(maps);
		}
		evaluationAdapter.updateData(true,user.getSex()==null?1:user.getSex(),comments);
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		refreshableScollView.onRefreshComplete();
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		map=null;
		if(code.equals(ReturnCode.STATE_999)){
			loadView.hideAllHit(this);
			return;
		}
		if(tag.endsWith(httpUsersService.TAG)){
			loadView.showLoadFail(this,this);
			showToask(message);
		}
		
	}

	@Override
	public void OnRequest() {
		if(requestTag.equals(httpUserLoveService.TAG)){
			showProgressDialog();
		}else{
			loadView.showLoading(this);
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
		loadUser();
		loadUserFilmType();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		evaluationAdapter=null;	
		httpUsersService=null;
		httpUserCommentService=null;
		httpUserFilmTyService=null;
		httpUserLoveService=null;
		userService=null;
		hobbyService=null;
	    filmTypeService=null;
	    hobbiesMap=null;
		filmTypeMap=null;
		comments.clear();
		Bimp.tempSelectBitmap.clear();
	}

	
}
