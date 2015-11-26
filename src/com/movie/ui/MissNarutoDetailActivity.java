package com.movie.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.PartNarutoExpandableAdapter;
import com.movie.app.Constant;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.Dictionary;
import com.movie.client.bean.Miss;
import com.movie.client.bean.User;
import com.movie.client.service.CallBackService;
import com.movie.view.ExpandListViewForScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;


public class MissNarutoDetailActivity extends BaseActivity implements OnClickListener, CallBackService {


	protected static int LOADUSERCOMPLETE=1;
	Miss miss;
	TextView title;
	ImageView missIcon;
	TextView missCreateUser;
	TextView missDate;
	TextView missMovieName;
	TextView cinemaName;
	TextView cinemaAddress;
	TextView cinemaPhone;
	ScrollView missDetailView;
	ExpandListViewForScrollView missPartList;
	//PartNarutoAdapter partNarutoAdapter;
	PartNarutoExpandableAdapter partNarutoAdapter;
	ImageLoader imageLoaderCache;
	LinearLayout layoutCinemaAddress;
	List<Dictionary> parents=new ArrayList<Dictionary>();
    List<List<User>> childs = new ArrayList<List<User>>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_miss_naruto_detail);
	
		imageLoaderCache=ImageLoader.getInstance();
		initViews();
		initData();
		initPartUser();
	}

	private void initViews() {
		
		missDetailView = (ScrollView)findViewById(R.id.miss_detail_view);
		missDetailView.smoothScrollTo(0, 0);
		title = (TextView) findViewById(R.id.title);
		missIcon = (ImageView) findViewById(R.id.miss_icon);
		missCreateUser = (TextView) findViewById(R.id.miss_create_user);
		missDate = (TextView) findViewById(R.id.miss_date);
		missMovieName = (TextView) findViewById(R.id.miss_movie_name);
		cinemaName = (TextView) findViewById(R.id.cinema_name);
		cinemaAddress = (TextView) findViewById(R.id.cinema_address);
		cinemaPhone = (TextView) findViewById(R.id.cinema_phone);
		layoutCinemaAddress = (LinearLayout) findViewById(R.id.miss_cinema_detail_panel);
		missPartList = (ExpandListViewForScrollView) findViewById(R.id.miss_part_list);
		partNarutoAdapter = new PartNarutoExpandableAdapter(this, null,null);
		missPartList.setAdapter(partNarutoAdapter);
		layoutCinemaAddress.setOnClickListener(this);
	}

	private void initData() {

		miss = (Miss) getIntent().getSerializableExtra("miss");
		if(null==miss){
			return;
		}
		title.setText(miss.getMovieName());
		imageLoaderCache.displayImage(miss.getIcon(), missIcon,NarutoApplication.imageOptions);
		missCreateUser.setText(miss.getCreateUserName());
		missDate.setText(miss.getRunTime());
		missMovieName.setText(miss.getMovieName());
		cinemaName.setText(miss.getCinameName());
		cinemaAddress.setText(miss.getCinameAddress());
		cinemaPhone.setText(miss.getCinemaPhone());
	}
	private void initPartUser(){
		parents.clear();
		childs.clear();
		List<User> users=User.getTempData();
		parents.add(new Dictionary(0, getResources().getString(R.string.miss_part_list)));
		parents.add(new Dictionary(1, getResources().getString(R.string.miss_take_list)));
		childs.add(users);
		childs.add(new ArrayList<User>());
		partNarutoAdapter.updateData(parents, childs);
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.miss_cinema_detail_panel:
				Intent intent = new Intent(this, CinemaMapActivity.class);
				intent.putExtra("miss", miss);
				startActivity(intent);
				break;
			default:
				break;
		}
		
	

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String code=map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag=map.get(Constant.ReturnCode.RETURN_TAG).toString();
		
			
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
		
	}

	@Override
	public void OnRequest() {
		showProgressDialog("提示", "正在加载，请稍后....");		
	}
	
	

}
