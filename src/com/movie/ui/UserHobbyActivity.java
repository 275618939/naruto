package com.movie.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.Constant;
import com.movie.client.bean.User;
import com.movie.client.service.CallBackService;
import com.movie.client.service.HobbyService;
import com.movie.network.HttpHobbyUpdateService;
import com.movie.view.HobbyView;

public class UserHobbyActivity extends BaseActivity implements OnClickListener,CallBackService {

	public HashMap<String, Object> apiParams = new HashMap<String, Object>();
	protected final static int GO_USER=1;
	protected final static int GO_DETAIL=2;
	int go;
	User user;
	TextView title;
	TextView right;
	HobbyView hobbyView;
	HttpHobbyUpdateService hobbyUpdateService;
	HobbyService hobbyService;
	Map<Integer,String> hobbies;
	List<Integer> userHobbys=new ArrayList<Integer>();;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hobby);
		hobbyUpdateService = new HttpHobbyUpdateService(this);
		hobbyService = new HobbyService();
		initViews();
		initData();
	}

	private void initViews() {

		hobbyView = (HobbyView) findViewById(R.id.hobby_view);
		title = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right_text);
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);

	}

	private void initData() {
		go=getIntent().getIntExtra("go",GO_USER);
		if(go==GO_DETAIL){
			user = (User) getIntent().getSerializableExtra("user");
			if(null!=user.getNickname()){
				title.setText(user.getNickname()+"的个人喜好");
			}else{
				title.setText("个人喜好");
			}
			right.setVisibility(View.GONE);
			userHobbys= user.getHobbies();
		}else{
			title.setText("修改喜好");
			userHobbys= getIntent().getIntegerArrayListExtra("hobbis");
		}
		
		hobbies= hobbyService.getHobbyMap();
		hobbyView.setHobbies(hobbies);
		hobbyView.setUserHobbies(userHobbys);
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
		if(go==GO_USER){
			Intent intent = new Intent(this, UserActivity.class);
			this.startActivity(intent);
			this.finish();
		}else if(go==GO_DETAIL){
			Intent intent = new Intent(this, UserDetailActivity.class);
			intent.putExtra("user", user);
			startActivity(intent);
			//this.finish();
		}
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
		showProgressDialog("提示", "正在提交，请稍后......");
	}

	

}
