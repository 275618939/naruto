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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.EvaluationAdapter;
import com.movie.app.Constant;
import com.movie.app.SexState;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpSessionService;
import com.movie.view.CommentsGridView;

public class CommentsActivity extends BaseActivity implements OnClickListener,CallBackService {

	public HashMap<String, Object> apiParams = new HashMap<String, Object>();
	TextView title;
	CommentsGridView gridView;
	EvaluationAdapter evaluationAdapter;
	List<Map<Integer, Integer>> comments;
	BaseService sessionService;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		sessionService = new HttpSessionService(this);
		initViews();
		initData();
	}
	private void initViews() {
		evaluationAdapter = new EvaluationAdapter(this,SexState.WOMAN.getState(), comments);
		title = (TextView) findViewById(R.id.title);
		gridView = (CommentsGridView) findViewById(R.id.comments);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setAdapter(evaluationAdapter);
	}
	private void initData() {
		title.setText("伙影评价");
		loadComments();
	}
	private void loadComments(){
		Map<Integer, Integer> maps=null;
		Random random=new Random();
		comments=new ArrayList<Map<Integer,Integer>>();
		for(int i=0;i<20;i++){
    		maps=new HashMap<Integer, Integer>();
    		maps.put(i%8+1,random.nextInt(100));
		    comments.add(maps);
		}
		evaluationAdapter.updateData(comments);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		}

	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, UserDetailActivity.class);
		this.startActivity(intent);
		this.finish();
	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		hideProgressDialog();
	    String code=map.get(Constant.ReturnCode.RETURN_STATE).toString();
	    if (Constant.ReturnCode.STATE_1.equals(code)) {
			
		}else {
			String desc = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(desc);
		}
	}
	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		String message=map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
	}
	@Override
	public void OnRequest() {
		showProgressDialog("提示", "加载数据....");
	}

	

}
