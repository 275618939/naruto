package com.movie.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.movie.R;
import com.movie.adapter.MoviesCommentAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.client.bean.MovieComment;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpMovieCommentQueryService;
import com.movie.view.LoadView;

public class MovieCommentQueryActivity extends BaseActivity implements OnClickListener,CallBackService, OnRefreshListener2<ListView> {


	TextView title;
	PullToRefreshListView refreshableView;
	MoviesCommentAdapter commentAdapter;
	BaseService httpCommentQueryService;
	LoadView loadView;
	RelativeLayout movieCommentParentLayout;
	List<MovieComment> comments = new ArrayList<MovieComment>(); 
	int page;
	int filmId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_comment_query);
		httpCommentQueryService = new HttpMovieCommentQueryService(this);
		initViews();
		initEvents();
		initData();
	}
	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		movieCommentParentLayout= (RelativeLayout)findViewById(R.id.movie_comment_parent_layout);
		loadView = new LoadView(movieCommentParentLayout);
		refreshableView =  (PullToRefreshListView) findViewById(R.id.movie_comment_list);
		commentAdapter = new MoviesCommentAdapter(this, comments);
		refreshableView.setAdapter(commentAdapter);
		refreshableView.setMode(Mode.BOTH);
	}

	@Override
	protected void initEvents() {
		refreshableView.setOnRefreshListener(this);	
	}

	@Override
	protected void initData() {
		page=0;
		title.setText("评论信息");
		loadView.showLoading(this);
		filmId = getIntent().getExtras().getInt("filmId");
		loadMovieComment();	
	}
	private void loadMovieComment(){
		httpCommentQueryService.addParams("filmId", filmId);
		httpCommentQueryService.addParams("page", page);
		httpCommentQueryService.addParams("size", Page.DEFAULT_SIZE);
		httpCommentQueryService.execute(this);
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
		this.finish();
	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		loadView.showLoadAfter(this);
		refreshableView.onRefreshComplete();
		String code=map.get(Constant.ReturnCode.RETURN_STATE).toString();
		String tag=map.get(Constant.ReturnCode.RETURN_TAG).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
		   try {
				if(tag.equals(httpCommentQueryService.TAG)) { 
					List<Map<String, Object>> values=(List<Map<String, Object>>)map.get(Constant.ReturnCode.RETURN_VALUE);
					MovieComment movieComment=null;
					for(Map<String, Object> value:values){
						movieComment = new MovieComment();
						movieComment.setPortrait(Constant.SERVER_ADRESS+value.get("portrait").toString());
						movieComment.setNickname(value.get("nickname").toString());
						movieComment.setMemberId(value.get("memberId").toString());
						movieComment.setContent(value.get("content").toString());
						movieComment.setTime(value.get("time").toString());
						movieComment.setScore(Integer.parseInt(value.get("score").toString()));
					    comments.add(movieComment);
					}
					commentAdapter.notifyDataSetChanged();
				}
		   } catch (Exception e) {
			   showToask(e.getMessage());
			}
			
		}else{
			String message=map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		refreshableView.onRefreshComplete();
		loadView.showLoadFail(this,this);
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
	}

	@Override
	public void OnRequest() {		
		loadView.showLoading(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		page=0;
		comments.clear();
		loadMovieComment();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		page=1;
		loadMovieComment();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		commentAdapter=null;
		comments.clear();
	}

	

	

}
