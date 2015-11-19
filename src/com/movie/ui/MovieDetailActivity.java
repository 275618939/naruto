package com.movie.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.WantSeeMovieAdapter;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.client.bean.Movie;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.client.service.FilmTypeService;
import com.movie.fragment.SelfFragment;
import com.movie.network.HttpFilmLoveService;
import com.movie.network.HttpFilmLoveUpdateService;
import com.movie.network.HttpMovieDetailService;
import com.movie.util.ImageLoaderCache;
import com.movie.util.MovieScore;
import com.movie.util.StringUtil;
import com.movie.view.HorizontalListView;
import com.movie.view.MovieCommentsDialog;


public class MovieDetailActivity extends BaseActivity implements OnClickListener, CallBackService {


	protected static int LOADUSERCOMPLETE=1;
	Movie movie;
	TextView title;
	TextView movieBreif;
	TextView loveFilm;
	TextView movieInterval;
	TextView movieScore;
	RatingBar startBar;
	TextView movieStart;
	TextView movieStype;
	TextView createMiss;
	TextView filmLoveMore;
	TextView movieScenarists;
	TextView movieStars;
	TextView movieMissInfo;
	TextView movieComment;
	TextView movieCommentInfo;
	ImageView imagePoster;
	LinearLayout movieDetailPanel;
	LinearLayout movieBriefPanel;
	LinearLayout movieNoneScoreLayout;
	LinearLayout movieHaveScoreLayout;
	RelativeLayout filmLoveLayout;
	RadioGroup movieTab;

	
	List<User> users=new ArrayList<User>();
	WantSeeMovieAdapter wantSeeMovieAdapter;
	HorizontalListView horizontalListView;
	BaseService httpFileLoveService;
	BaseService httpMovieDetailService;
	BaseService httpFilmLoveUpdateService;
	FilmTypeService filmTypeService;
	ImageLoaderCache imageLoaderCache;
	Map<Integer,String> filemTypes;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_detail);
		httpFileLoveService =new HttpFilmLoveService(this);
		httpMovieDetailService =new HttpMovieDetailService(this);
		httpFilmLoveUpdateService = new HttpFilmLoveUpdateService(this);
		filmTypeService = new FilmTypeService();
		imageLoaderCache=new ImageLoaderCache(this);
		initViews();
		initData();
	}

	private void initViews() {
		title = (TextView) findViewById(R.id.title);
		movieScore= (TextView) findViewById(R.id.movie_score);
		startBar= (RatingBar) findViewById(R.id.movie_star);
		movieInterval = (TextView) findViewById(R.id.movie_interval);
		movieStart = (TextView) findViewById(R.id.movie_start);
		movieStype = (TextView) findViewById(R.id.movie_stype);
		movieBreif = (TextView) findViewById(R.id.movie_breif);
		loveFilm = (TextView) findViewById(R.id.love_film);
		createMiss = (TextView) findViewById(R.id.create_miss);
		filmLoveMore = (TextView) findViewById(R.id.filmLove_more);
		movieScenarists = (TextView) findViewById(R.id.movie_scenarists);
		movieStars = (TextView) findViewById(R.id.movie_stars);
		movieMissInfo = (TextView) findViewById(R.id.movie_miss_info);
		movieComment = (TextView) findViewById(R.id.movie_comment);
		movieCommentInfo = (TextView) findViewById(R.id.movie_comment_info);
		imagePoster= (ImageView) findViewById(R.id.movie_poster);
		movieDetailPanel = (LinearLayout)  findViewById(R.id.movie_detail_panel);
		movieBriefPanel = (LinearLayout)  findViewById(R.id.movie_brief_panel);
		movieNoneScoreLayout = (LinearLayout)  findViewById(R.id.movie_none_score_layout);
		movieHaveScoreLayout = (LinearLayout)  findViewById(R.id.movie_have_score_layout);
		filmLoveLayout = (RelativeLayout)  findViewById(R.id.film_love_layout);
		movieTab = (RadioGroup) findViewById(R.id.movie_tab);
		horizontalListView = (HorizontalListView) findViewById(R.id.layout_want_see);
		wantSeeMovieAdapter = new WantSeeMovieAdapter(this, users);
		horizontalListView.setAdapter(wantSeeMovieAdapter);
		loveFilm.setOnClickListener(this);
		filmLoveMore.setOnClickListener(this);
		createMiss.setOnClickListener(this);
		movieComment.setOnClickListener(this);
		movieTab.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group,int checkedId) {
				if (checkedId == R.id.movie_detail_btn) {
					movieDetailPanel.setVisibility(View.VISIBLE);
					movieBriefPanel.setVisibility(View.GONE);
				} else if (checkedId == R.id.movie_intro_btn) {
					movieBriefPanel.setVisibility(View.VISIBLE);
					movieDetailPanel.setVisibility(View.GONE);
				}

			}
		});
	}

	private void initData() {
		filemTypes=filmTypeService.getFilmTypeMap();
		movie = (Movie) getIntent().getSerializableExtra("movie");
		title.setText(movie.getName());
		if(null!=movie){
			imageLoaderCache.DisplayImage(movie.getIcon(), imagePoster);
			String score=MovieScore.GetScore(movie.getScore(), movie.getScoreCnt());
			startBar.setRating(Float.valueOf(score)/2f);
			movieScore.setText(score);
			if(score.equals("NaN")){
				movieNoneScoreLayout.setVisibility(View.VISIBLE);
				movieHaveScoreLayout.setVisibility(View.GONE);
			}
			if(movie.getTryst()>0){
				movieMissInfo.setText(String.format(getResources().getString(R.string.miss_have),String.valueOf(movie.getTryst())));
				movieMissInfo.setOnClickListener(this);
			}else{
				movieMissInfo.setText(getResources().getString(R.string.miss_none));
			}
			loadMovieDetail();
		}

	}
	private void loadMovieDetail(){
		httpMovieDetailService.addParams("filmId", movie.getId());
		httpMovieDetailService.execute(this);
	}
	
	private void loadMovieLove(){
		httpFileLoveService.addParams("filmId", movie.getId());
		httpFileLoveService.addParams("page",Constant.Page.FIRST_PAGE);
		httpFileLoveService.addParams("size",Constant.Page.WANT_SEE_MOIVE_SIZE);
		httpFileLoveService.execute(this);
	}
	
	private void updateFilmLove(){
		
		httpFilmLoveUpdateService.addParams("filmId", movie.getId());
		httpFilmLoveUpdateService.execute(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.movie_comment:
				final MovieCommentsDialog.Builder builder = new MovieCommentsDialog.Builder(this);
				builder.setTitle(R.string.comment);
				builder.setPositiveButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("确定",
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								String comments=builder.getComments();
								
							}
						});

				builder.create().show();
				break;
			case R.id.love_film:
				updateFilmLove();
				break;
			case R.id.movie_miss_info:
				Intent movieIntent = new Intent(this, MissQueryActivity.class);
				movieIntent.putExtra(SelfFragment.MISS_KEY, SelfFragment.MOVIE_INVITATION);
				movieIntent.putExtra(SelfFragment.CONDITION_KEY, movie);
				startActivity(movieIntent);
				break;
			case R.id.create_miss:
				Intent missCreate = new Intent(this, MissCreateActivity.class);
				missCreate.putExtra("movie", movie);
				startActivity(missCreate);
				break;	
			case R.id.filmLove_more:
				
				break;
		}
		
	

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	@Override
	@SuppressWarnings("unchecked")
	public void SuccessCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String code=map.get(Constant.ReturnCode.RETURN_STATE).toString();
		String tag=map.get(Constant.ReturnCode.RETURN_TAG).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
		   try {
				if(tag.equals(httpMovieDetailService.TAG)){
					Map<String, Object> value =(Map<String, Object>) map.get(Constant.ReturnCode.RETURN_VALUE);
					if(null!=value){
						movieInterval.setText(String.format(getResources().getString(R.string.movie_interval),value.get("interval")));
						movieStart.setText(String.format(getResources().getString(R.string.movie_start),StringUtil.strChangeLocalDate(value.get("start").toString())));
						movieBreif.setText(value.get("briefing").toString());
						movieScenarists.setText(StringUtil.listToString(((List<String>)value.get("directors")), "/"));
						movieStype.setText(StringUtil.listToStringByMap(((List<Integer>)value.get("types")), filemTypes, "/"));
						movieStars.setText(StringUtil.listToString(((List<String>)value.get("stars")), "/"));
						if(value.containsKey("comment")){
							movieCommentInfo.setText(String.format(getResources().getString(R.string.movie_comment_have),value.get("comment")));
						}else{
							movieCommentInfo.setText(getResources().getString(R.string.movie_comment_none));
						}
					}
				}else if(tag.equals(httpFileLoveService.TAG)){ 
				    List<Map<String, Object>> values=(List<Map<String, Object>>)map.get(Constant.ReturnCode.RETURN_VALUE);
					User user = null;
					users.clear();
				    for(Map<String, Object> value:values){
				    	user = new User();
				    	user.setPortrait(value.get("portrait").toString());
				    	user.setNickname(value.get("nickname").toString());
				    	user.setMemberId(value.get("memberId").toString());
				    	user.setSignature(value.get("signature").toString());
				    	user.setHobbies((List<Integer>)value.get("hobbies"));
				    	users.add(user);
					}
				    if(values.size()>=Page.WANT_SEE_MOIVE_SIZE){
				    	filmLoveMore.setVisibility(View.VISIBLE);
				    }
				    if(values.size()>0){
				        wantSeeMovieAdapter.updateData(users);
				    	filmLoveLayout.setVisibility(View.VISIBLE);
				    }
				}else if(tag.equals(httpFilmLoveUpdateService.TAG)){ 
					loadMovieLove();
				}
		   } catch (Exception e) {
			   showToask(e.getMessage());
			}
			
		}else if (Constant.ReturnCode.STATE_3.equals(code)) {
			//提示用户登陆
			if(tag.equals(httpFilmLoveUpdateService.TAG)){
				Intent loginIntent = new Intent(this,LoginActivity.class);
				this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				startActivity(loginIntent);
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
		int size=0;
		if(size>0){
			movieMissInfo.setText(String.format(getResources().getString(R.string.miss_have),String.valueOf(size)));
			movieMissInfo.setOnClickListener(this);
		}else{
			movieMissInfo.setText(getResources().getString(R.string.miss_none));
		}
	}

	@Override
	public void OnRequest() {
		showProgressDialog("提示", "正在加载，请稍后....");		
	}
	
	

}
