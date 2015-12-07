package com.movie.ui;

import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.HobbyAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.client.bean.User;
import com.movie.client.service.CallBackService;
import com.movie.client.service.HobbyService;
import com.movie.view.CommentsGridView;

public class UserShowHobbyActivity extends BaseActivity implements OnClickListener,CallBackService {

	TextView title;
	CommentsGridView gridView;
	HobbyAdapter hobbyAdapter;
	Map<Integer,String> hobbies;
	HobbyService hobbyService;
	User user;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_hobbies);
		hobbyService = new HobbyService();
		initViews();
		initEvents();
		initData();
	}
	@Override
	protected void initViews() {
		hobbyAdapter = new HobbyAdapter(this, null,null);
		title = (TextView) findViewById(R.id.title);
		gridView = (CommentsGridView) findViewById(R.id.hobbies);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setAdapter(hobbyAdapter);
	}
	@Override
	protected void initEvents() {
		
		
	}
	@Override
	protected void initData() {
		user = (User) getIntent().getSerializableExtra("user");
		title.setText(user.getNickname()+"的喜好");
		hobbies= hobbyService.getHobbyMap();
		hobbyAdapter.updateData(hobbies, user.getHobbies());
		
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
	    map=null;
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
	@Override
	protected void onDestroy() {
		super.onDestroy();
		hobbyAdapter=null;
		gridView=null;
		hobbies=null;
		hobbyService=null;
	}
	
	

}
