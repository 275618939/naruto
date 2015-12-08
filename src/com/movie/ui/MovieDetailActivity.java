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
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.movie.R;
import com.movie.adapter.MoviesCommentAdapter;
import com.movie.adapter.WantSeeMovieAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.app.Constant.ReturnCode;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.Login;
import com.movie.client.bean.Movie;
import com.movie.client.bean.MovieComment;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.client.service.FilmTypeService;
import com.movie.fragment.SelfFragment;
import com.movie.network.HttpFilmLoveService;
import com.movie.network.HttpFilmLoveUpdateService;
import com.movie.network.HttpMovieCommentCreateService;
import com.movie.network.HttpMovieCommentQueryService;
import com.movie.network.HttpMovieDetailService;
import com.movie.util.MovieScore;
import com.movie.util.SharedPreferencesUtils;
import com.movie.util.StringUtil;
import com.movie.view.HorizontalListView;
import com.movie.view.LoadView;
import com.movie.view.MovieCommentsDialog;
import com.nostra13.universalimageloader.core.ImageLoader;


public class MovieDetailActivity extends BaseActivity implements OnClickListener, CallBackService , OnRefreshListener2<ListView>,OnRefreshListener<ScrollView>{

	protected static int LOADUSERCOMPLETE=1;
	Movie movie;
	int filmId;
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
	RelativeLayout movieCommentsLayout;
	RadioGroup movieTab;
	PullToRefreshListView refreshableView;
	MoviesCommentAdapter commentAdapter;
	WantSeeMovieAdapter wantSeeMovieAdapter;
	HorizontalListView horizontalListView;
	PullToRefreshScrollView refreshableScollView;
	BaseService httpFileLoveService;
	BaseService httpMovieDetailService;
	BaseService httpFilmLoveUpdateService;
	BaseService httpCommentCreateService;
	BaseService httpCommentQueryService;
	FilmTypeService filmTypeService;
	ImageLoader imageLoaderCache;
	Map<Integer,String> filemTypes;
	List<User> users=new ArrayList<User>();
	List<MovieComment> comments = new ArrayList<MovieComment>(); 
	int page;
	LoadView loadView;
	RelativeLayout movieDetailParent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_detail);
		httpFileLoveService =new HttpFilmLoveService(this);
		httpMovieDetailService =new HttpMovieDetailService(this);
		httpFilmLoveUpdateService = new HttpFilmLoveUpdateService(this);
		httpCommentCreateService = new HttpMovieCommentCreateService(this);
		httpCommentQueryService = new HttpMovieCommentQueryService(this);
		filmTypeService = new FilmTypeService();
		imageLoaderCache=ImageLoader.getInstance();
		initViews();
		initEvents();
		initData();
	}
	@Override
	protected void initViews() {
		movieDetailParent = (RelativeLayout) findViewById(R.id.movie_detail_parent);
		loadView = new LoadView(movieDetailParent);
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
		movieCommentsLayout = (RelativeLayout)  findViewById(R.id.movie_comments_layout);
		movieTab = (RadioGroup) findViewById(R.id.movie_tab);
		refreshableView =  (PullToRefreshListView) findViewById(R.id.movie_comment_list);
		horizontalListView = (HorizontalListView) findViewById(R.id.layout_want_see);
		refreshableScollView=(PullToRefreshScrollView) findViewById(R.id.movie_detail_view);
		wantSeeMovieAdapter = new WantSeeMovieAdapter(this, users);
		commentAdapter = new MoviesCommentAdapter(this, comments);
		horizontalListView.setAdapter(wantSeeMovieAdapter);
		refreshableView.setAdapter(commentAdapter);
		refreshableView.setMode(Mode.PULL_FROM_END);
		refreshableView.setFocusable(false);
		refreshableScollView.setMode(Mode.PULL_FROM_START);
	}

	@Override
	protected void initEvents() {
		refreshableScollView.setOnRefreshListener(this);
		refreshableView.setOnRefreshListener(this);
		loveFilm.setOnClickListener(this);
		filmLoveMore.setOnClickListener(this);
		createMiss.setOnClickListener(this);
		movieComment.setOnClickListener(this);
		movieCommentsLayout.setOnClickListener(this);
		
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
	@Override
	protected void initData() {
		filemTypes=filmTypeService.getFilmTypeMap();
		filmId = getIntent().getIntExtra("filmId",0);
		loadMovieDetail();
		loadMovieLove();
		loadMovieComment();
	}
	private void loadMovieDetail(){
		httpMovieDetailService.addParams("filmId", filmId);
		httpMovieDetailService.execute(this);
	}
	
	private void loadMovieLove(){
		users.clear();
		httpFileLoveService.addParams("filmId",filmId);
		httpFileLoveService.addParams("page",Constant.Page.FIRST_PAGE);
		httpFileLoveService.addParams("size",Constant.Page.WANT_SEE_MOIVE_SIZE);
		httpFileLoveService.execute(this);
	}
	
	private void updateFilmLove(){
		
		httpFilmLoveUpdateService.addParams("filmId", filmId);
		httpFilmLoveUpdateService.execute(this);
	}
	
	private void createComment(String content,int score){
		httpCommentCreateService.addParams("filmId",filmId);
		httpCommentCreateService.addParams("content", content);
		httpCommentCreateService.addParams("score", score);
		httpCommentCreateService.execute(this);
	}
	private void loadMovieComment(){
		comments.clear();
		httpCommentQueryService.addParams("filmId", filmId);
		httpCommentQueryService.addParams("page", page);
		httpCommentQueryService.addParams("size", Page.DEFAULT_SIZE);
		httpCommentQueryService.execute(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.movie_comments_layout:
				Intent commentIntent = new Intent(this, MovieCommentQueryActivity.class);
				commentIntent.putExtra("filmId", filmId);
				startActivity(commentIntent);
				break;
			case R.id.movie_comment:
				Login login=getLogin();
				if(null==login){
					goLogin();
					return;
				}
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
								if(comments==null||comments.isEmpty())
									return;
								int score=builder.getRatingBarValue()*2;
								createComment(comments,score);
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
				SharedPreferencesUtils.setParam(this, "filmId", filmId);
				SharedPreferencesUtils.setParam(this, "filmName", movie.getName());
				Intent missCreate = new Intent(this, MissCreateActivity.class);
				startActivity(missCreate);
				this.finish();
				break;	
			case R.id.filmLove_more:
				
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
	@SuppressWarnings("unchecked")
	public void SuccessCallBack(Map<String, Object> map) {
		loadView.showLoadAfter(this);
		refreshableView.onRefreshComplete();
		refreshableScollView.onRefreshComplete();
		hideProgressDialog();
		String code=map.get(Constant.ReturnCode.RETURN_STATE).toString();
		String tag=map.get(Constant.ReturnCode.RETURN_TAG).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
		   try {
				if(tag.equals(httpMovieDetailService.TAG)){
					Map<String, Object> value =(Map<String, Object>) map.get(Constant.ReturnCode.RETURN_VALUE);
					if(null!=value){
						movie =new Movie();
						movie.setId(Integer.parseInt(value.get("filmId").toString()));
						movie.setName(value.get("filmName").toString());
						movie.setIcon(Constant.SERVER_ADRESS+value.get("filmIcon").toString());
						if(value.containsKey("scoreTtl")){
							movie.setScore(Long.valueOf(value.get("scoreTtl").toString()));
						}
						if(value.containsKey("scoreCnt")){
							movie.setScoreCnt(Integer.parseInt(value.get("scoreCnt").toString()));
						}
						if(value.containsKey("trystCnt")){
							movie.setTryst(Integer.parseInt(value.get("trystCnt").toString()));
						}
						if(value.containsKey("loveCnt")){
							movie.setLoveCnt(Integer.parseInt(value.get("loveCnt").toString()));
						}
						title.setText(movie.getName());
						imageLoaderCache.displayImage(movie.getIcon(), imagePoster,NarutoApplication.imageOptions);
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
						movieInterval.setText(String.format(getResources().getString(R.string.movie_interval),value.get("interval")));
						movieStart.setText(String.format(getResources().getString(R.string.movie_start),StringUtil.strChangeLocalDate(value.get("start").toString())));
						movieBreif.setText(value.get("briefing").toString());
						movieScenarists.setText(StringUtil.listToString(((List<String>)value.get("directors")), "/"));
						movieStype.setText(StringUtil.listToStringByMap(((List<Integer>)value.get("types")), filemTypes, "/"));
						movieStars.setText(StringUtil.listToString(((List<String>)value.get("stars")), "/"));
						if(value.containsKey("commentCnt")){
							movieCommentInfo.setText(String.format(getResources().getString(R.string.movie_comment_have),value.get("commentCnt")));
						}else{
							movieCommentInfo.setText(getResources().getString(R.string.movie_comment_none));
						}
					}
				}else if(tag.equals(httpFileLoveService.TAG)){ 
				    List<Map<String, Object>> values=(List<Map<String, Object>>)map.get(Constant.ReturnCode.RETURN_VALUE);
					User user = null;
				    for(Map<String, Object> value:values){
				    	user = new User();
				    	user.setPortrait(Constant.SERVER_ADRESS+value.get("portrait").toString());
				    	user.setNickname(value.get("nickname").toString());
				    	user.setMemberId(value.get("memberId").toString());
				    	users.add(user);
					}
				    if(values.size()>=Page.WANT_SEE_MOIVE_SIZE){
				    	filmLoveMore.setVisibility(View.VISIBLE);
				    }
				    if(values.size()>0){
				        wantSeeMovieAdapter.notifyDataSetChanged();
				    	filmLoveLayout.setVisibility(View.VISIBLE);
				    }
				}else if(tag.equals(httpFilmLoveUpdateService.TAG)) { 
					loadMovieLove();
				}else if(tag.equals(httpCommentCreateService.TAG)) { 
					loadMovieComment();
				}else if(tag.equals(httpCommentQueryService.TAG)) { 
					List<Map<String, Object>> values=(List<Map<String, Object>>)map.get(Constant.ReturnCode.RETURN_VALUE);
					MovieComment movieComment=null;
					int size=0;
					for(Map<String, Object> value:values){
					   if(size>=Page.MOVIES_COMMENTS_MAX_SHOW)
						  break;
						movieComment = new MovieComment();
						movieComment.setPortrait(Constant.SERVER_ADRESS+value.get("portrait").toString());
						movieComment.setNickname(value.get("nickname").toString());
						movieComment.setMemberId(value.get("memberId").toString());
						movieComment.setContent(value.get("content").toString());
						movieComment.setTime(value.get("time").toString());
						movieComment.setScore(Integer.parseInt(value.get("score").toString()));
					    comments.add(movieComment);
					    size++;
					}
					commentAdapter.notifyDataSetChanged();
				
				
				}
		   } catch (Exception e) {
			   showToask(e.getMessage());
			}
			
		}else if (Constant.ReturnCode.STATE_3.equals(code)) {
			//提示用户登陆
			//if(tag.equals(httpFilmLoveUpdateService.TAG)){
				Intent loginIntent = new Intent(this,LoginActivity.class);
				this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				startActivity(loginIntent);
				this.finish();
			//}
			
		}else{
			String message=map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
		map=null;
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		String message=map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		refreshableScollView.onRefreshComplete();
		String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		showToask(message);
		if(code.equals(ReturnCode.STATE_999)){
			loadView.hideAllHit(this);
			return;
		}
		if(tag.endsWith(httpMovieDetailService.TAG)){
			loadView.showLoadFail(this,this);
		}
		
	}

	@Override
	public void OnRequest() {
		loadView.showLoading(this);	
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		
		
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		page++;
		loadMovieComment();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
		loadMovieDetail();
		loadMovieLove();
		loadMovieComment();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		commentAdapter=null;
		wantSeeMovieAdapter=null;
		users.clear();
		comments.clear();
		httpFileLoveService=null;
		httpMovieDetailService=null;
		httpFilmLoveUpdateService=null;
		httpCommentCreateService=null;
		httpCommentQueryService=null;
		filmTypeService=null;
	}

	
	
	

}
