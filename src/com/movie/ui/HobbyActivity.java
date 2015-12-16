package com.movie.ui;

import java.util.ArrayList;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.client.bean.User;
import com.movie.client.service.CallBackService;
import com.movie.client.service.HobbyService;
import com.movie.network.HttpHobbyUpdateService;
import com.movie.view.HobbyView;

public class HobbyActivity extends BaseActivity implements OnClickListener,CallBackService {

	User user;
	TextView title;
	TextView right;
	HobbyView hobbyView;
	HttpHobbyUpdateService hobbyUpdateService;
	HobbyService hobbyService;
	Map<Integer,String> hobbies;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hobby);
		hobbyUpdateService = new HttpHobbyUpdateService(this);
		hobbyService = new HobbyService();
		initViews();
		initEvents();
		initData();
	}
	@Override
	protected void initViews() {
		hobbyView = (HobbyView) findViewById(R.id.hobby_view);
		title = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right_text);
		right.setVisibility(View.VISIBLE);
	
	}
	@Override
	protected void initEvents() {
		right.setOnClickListener(this);
	}
	@Override
	protected void initData() {
		user = (User) getIntent().getSerializableExtra("user");
		if(user.getHobbies()==null){
			user.setHobbies(new ArrayList<Integer>());
		}
		title.setText("修改喜好");
		hobbies= hobbyService.getHobbyMap();
		hobbyView.setHobbies(hobbies);
		hobbyView.setUserHobbies(user.getHobbies());
		hobbyView.init();
		right.setText("保存");
		hobbyView.loadMoreHobby();
		
	}
	private void modifyUser() {
		hobbyUpdateService.addParams("hobbies", hobbyView.getUserHobbies());
		hobbyUpdateService.execute(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_text:
			modifyUser();
			break;

		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		setResult(UserActivity.RELOAGIN);
		this.finish();
	}



	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		hideProgressDialog();
		onBackPressed();
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String message=map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
	}

	@Override
	public void OnRequest() {
		showProgressDialog();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		hobbyUpdateService=null;
	    hobbyService=null;
		hobbies=null;
	}

	

	

}
