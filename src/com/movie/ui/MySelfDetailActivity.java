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
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.EvaluationAdapter;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.app.Constant.ReturnCode;
import com.movie.app.BaseActivity;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.client.service.FilmTypeService;
import com.movie.client.service.HobbyService;
import com.movie.fragment.SelfFragment;
import com.movie.network.HttpUserCommentService;
import com.movie.network.HttpUserFilmTypeService;
import com.movie.network.HttpUserService;
import com.movie.state.SexState;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MySelfDetailActivity extends BaseActivity implements
		OnClickListener, CallBackService {

	User user;
	TextView title;
	TextView right_text;
	ImageView headView;
	RelativeLayout hobbyMore;
	BaseService httpUsersService;
	BaseService httpUserCommentService;
	BaseService httpUserFilmTyService;
	HobbyService hobbyService;
	FilmTypeService filmTypeService;
	EvaluationAdapter evaluationAdapter;
	GridView commentsView;
	TextView userSex;
	TextView loveView;
	TextView charmView;
	TextView signView;
	TextView commnetsMore;
	TextView userMoviesPre;
	TextView userMoviesWant;
	TextView userMiss;
	LinearLayout hobbiesLayout;
	LinearLayout commentsLayout;
	List<Map<Integer, Integer>> comments = new ArrayList<Map<Integer, Integer>>();
	ImageLoader imageLoaderCache;
	Map<Integer,String> hobbiesMap;
	Map<Integer,String> filmTypeMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_detail);
		httpUsersService = new HttpUserService(this);
		httpUserCommentService = new HttpUserCommentService(this);
		httpUserFilmTyService = new HttpUserFilmTypeService(this);
		hobbyService = new HobbyService();
		filmTypeService = new FilmTypeService();
		imageLoaderCache=ImageLoader.getInstance();
		initViews();
		initEvents();
		initData();
	}

	@Override
	protected void initViews() {
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
		hobbiesLayout = (LinearLayout) findViewById(R.id.hobbies);
		commentsLayout = (LinearLayout) findViewById(R.id.comments_layout);
		hobbyMore = (RelativeLayout) findViewById(R.id.hobby_arrow);
		commnetsMore = (TextView) findViewById(R.id.comments_more);
		commentsView.setAdapter(evaluationAdapter);
		commentsView.setSelector(new ColorDrawable(Color.TRANSPARENT));	
		commentsLayout.setVisibility(View.GONE);		
	}

	@Override
	protected void initEvents() {
		commnetsMore.setOnClickListener(this);		
	}

	@Override
	protected void initData() {
		hobbiesMap= hobbyService.getHobbyMap();
		filmTypeMap=filmTypeService.getFilmTypeMap();
		userMoviesWant.setText(String.format(getResources().getString(R.string.movie_none)));
		userMiss.setText(String.format(getResources().getString(R.string.miss_none)));
		loadUser();		
	}
	private void loadUser() {
		httpUsersService.addParams(httpUsersService.URL_KEY,Constant.User_API_URL);
		httpUsersService.execute(this);
	}
	private void loadUserComments(){
		httpUserCommentService.addParams("memberId", user.getMemberId());
		httpUserCommentService.execute(this);
	}
	private void loadUserFilmType(){
		httpUserFilmTyService.addParams("page",0);
		httpUserFilmTyService.addParams("size",Constant.Page.WANT_SEE_MOIVE_SIZE);
		httpUserFilmTyService.execute(this);
	}

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hobby_arrow:
			Intent intent = new Intent(this, HobbyActivity.class);
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
			missIntent.putExtra(SelfFragment.MISS_KEY, SelfFragment.USER_INVITATION);
			missIntent.putExtra(SelfFragment.CONDITION_KEY, user);
			startActivity(missIntent);
			break;
		default:
			break;

		}

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		this.startActivity(intent);
		this.finish();
	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
			
			if (tag.endsWith(httpUsersService.TAG)) {
				
				user = new User();
			
				Map<String, Object> values = (Map<String, Object>) map.get(ReturnCode.RETURN_VALUE);
				user.setMemberId(values.get("memberId").toString());
				if (values.containsKey("portrait")) {
					imageLoaderCache.displayImage(Constant.SERVER_ADRESS+values.get("portrait").toString(),headView,NarutoApplication.imageOptions);
					user.setPortrait(values.get("portrait").toString());
				}
				if (values.containsKey("sex")) {
					user.setSex(Integer.parseInt(values.get("sex").toString()));
					userSex.setText(SexState.getState(Integer.parseInt(values.get("sex").toString())).getMessage());
				}
				if (values.containsKey("nickname")) {
					user.setNickname(values.get("nickname").toString());
					title.setText(values.get("nickname").toString());
				}
				if (values.containsKey("signature")) {
					user.setSignature(values.get("signature").toString());
					signView.setText(values.get("signature").toString());
				}
				if (values.containsKey("love")) {
					user.setLove(Integer.parseInt(values.get("love").toString()));
					loveView.setText(String.format(getResources().getString(R.string.user_love_count), values.get("love")));
				}
				if (values.containsKey("charm")) {
					user.setLove(Integer.parseInt(values.get("charm").toString()));
					charmView.setText(values.get("charm").toString());
				}
				if (values.containsKey("hobbies")) {
					List<Integer> hobbies = (List<Integer>) values.get("hobbies");
					user.setHobbies(hobbies);
					TextView textView = null;
					if (null != hobbies) {
						for (int i = 0; i < Page.MAXHOBBIES; i++) {
							textView = new TextView(new ContextThemeWrapper(this, R.style.common_tag), null, 0);
							int key = hobbies.get(i);
							String value = hobbiesMap.get(key);
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
							params.setMargins(0, 0, 10, 0);
							textView.setLayoutParams(params);
							textView.setText(value);
							hobbiesLayout.addView(textView);
						}
						hobbyMore.setOnClickListener(this);
					}
				}
				if(values.containsKey("tryst")){
					int tryst=Integer.parseInt(values.get("tryst").toString());
					user.setTryst(tryst);
					if(tryst>0){
						userMiss.setText(String.format(getResources().getString(R.string.miss_have),String.valueOf(values.size())));
						userMiss.setOnClickListener(this);
					}
				}
				
				initTempComments();
				/*if(user.getFilmType()==null||user.getFilmType().size()<=0){
					userMoviesPre.setText(getResources().getString(R.string.movie_none));
				}else{
					StringUtil.listToStringByMap(user.getFilmType(), filmTypeMap, "/");
				}*/
			
			}else if(tag.endsWith(httpUserCommentService.TAG)){
				List<Map<Integer,Integer>> values = (List<Map<Integer,Integer>>) map.get(ReturnCode.RETURN_VALUE);
				if (null != values) {
					comments = new ArrayList<Map<Integer, Integer>>();
					int size = 0;
					if (size > 0) {
						for (int i = 0; i < size; i++) {
							 comments.add(values.get(i));
						}
						evaluationAdapter.updateData(user.getSex(),comments);
					} 
					if(size>0){
					    commentsLayout.setVisibility(View.VISIBLE);
					}
					
				}
			}else if(tag.endsWith(httpUserFilmTyService.TAG)){
				List<Map<String,Object>> values = (List<Map<String,Object>>) map.get(ReturnCode.RETURN_VALUE);
				if (null != values&&values.size()>0) {
					userMoviesWant.setText(String.format(getResources().getString(R.string.movie_have),String.valueOf(values.size())));
					userMoviesWant.setOnClickListener(this);
				}
				
			}
		} else {
			
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}

	}
	private void initTempComments(){
		commentsLayout.setVisibility(View.VISIBLE);
		Map<Integer, Integer> maps = null;
		Random random = new Random();
		for (int i = 0; i < Constant.Page.COMMENTS_MAX_SHOW; i++) {
			maps = new HashMap<Integer, Integer>();
			maps.put(i, random.nextInt(100));
			comments.add(maps);
		}
		if(user.getSex()!=null){
			evaluationAdapter.updateData(user.getSex(),comments);
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
	protected void onDestroy() {
		super.onDestroy();
		evaluationAdapter=null;
		comments.clear();
	}


}
