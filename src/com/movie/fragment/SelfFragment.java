package com.movie.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.movie.R;
import com.movie.adapter.SignInAdapter;
import com.movie.adapter.UserPhotoGridAdapter;
import com.movie.app.Constant;
import com.movie.app.Constant.ReturnCode;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpLoginAutoService;
import com.movie.network.HttpLogoutService;
import com.movie.network.HttpUserService;
import com.movie.ui.LoginActivity;
import com.movie.ui.MissSelfQueryActivity;
import com.movie.ui.UserActivity;
import com.movie.ui.UserDetailActivity;
import com.movie.util.ImageLoaderCache;
import com.movie.view.CommentsGridView;

public class SelfFragment extends Fragment implements OnClickListener , CallBackService{
	
	public static final int PTHOTO_UPDATE = 0X001;
	public static final int LOGOUT = 0X110;
	public static final int MAX_SHOW_USER_PHOTO=4;
	List<Map<Integer,Integer>> signInList;
	//我发起的约会
	public static final int MY_MISS = 0X210;
	//我参与的约会
	public static final int MY_PART = 0X211;
	//我应邀的约会
	public static final int MY_INVITATION = 0X212;
	//用户参与的约会
	public static final int USER_INVITATION = 0X213;
	//电影下的约会
	public static final int MOVIE_INVITATION = 0X214;
	//约会类型
	public static final String MISS_KEY = "miss_type";
	//电影下的约会
	public static final String CONDITION_KEY = "miss_query_key";
	User user;
	BaseService httpLoginAutoService;
	BaseService httpUsersService;
	BaseService httpLogotService;
	RelativeLayout loginLayout;
	RelativeLayout logoutLayout;
	LinearLayout myMissLayout;
	LinearLayout loginAfterLayout;
	ImageLoaderCache loaderCache;
	ImageView loginLogo;
	ImageView userInfoLogo;
	ImageView userEdit;
	TextView userNickView;
	TextView userSignView;
	TextView userLogoutBtn;
	ImageView userSignIn;
	PopupWindow popupWindow;
	ImageView btnPopClose;
	View popView;
	LayoutInflater layoutInflater;
	CommentsGridView signInGridView;
	SignInAdapter signInAdapter;
	UserPhotoGridAdapter photoGridAdapter;
	GridView photoGridview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View titleView = getActivity().findViewById(R.id.main_head);
		if(null!=titleView){
			titleView.setVisibility(View.GONE);
		}
		loaderCache = new ImageLoaderCache(getActivity());
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_self, null);
		httpUsersService = new HttpUserService(getActivity());
		httpLogotService = new HttpLogoutService(getActivity());
		httpLoginAutoService = new HttpLoginAutoService(getActivity());
		initView(view);
		initSignIn();
		return view;
	}
	@Override
	public void onStart(){
		initUser();
		super.onStart();
	}
	private void initView(View view){
		
		layoutInflater = LayoutInflater.from(getActivity());
		loginLayout=(RelativeLayout)view.findViewById(R.id.login_layout);
		logoutLayout=(RelativeLayout)view.findViewById(R.id.logout_layout);
		myMissLayout=(LinearLayout)view.findViewById(R.id.my_miss_layout);
		loginAfterLayout=(LinearLayout)view.findViewById(R.id.loginAfter);
		loginLogo = (ImageView)view.findViewById(R.id.login_logo);
		userInfoLogo = (ImageView)view.findViewById(R.id.user_info_logo);
		userEdit = (ImageView)view.findViewById(R.id.user_edit);
		userNickView = (TextView)view.findViewById(R.id.usernick);
		userSignView = (TextView)view.findViewById(R.id.usersign);
		userLogoutBtn = (TextView)view.findViewById(R.id.user_logout_btn);
		userSignIn = (ImageView)view.findViewById(R.id.user_sign_in);
		photoGridview = (GridView)view.findViewById(R.id.userPhotoGridview);
		photoGridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		photoGridAdapter =new UserPhotoGridAdapter(getActivity(), mHandler,view);
		photoGridAdapter.update();
		photoGridview.setAdapter(photoGridAdapter);
		photoGridview.setOnItemClickListener(photoGridAdapter);
		loginLogo.setOnClickListener(this);
		userInfoLogo.setOnClickListener(this);
		userEdit.setOnClickListener(this);
		userLogoutBtn.setOnClickListener(this);
		myMissLayout.setOnClickListener(this);
		userSignIn.setOnClickListener(this);
		
		initPopWindow();
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
				case PTHOTO_UPDATE:
					photoGridAdapter.notifyDataSetChanged();
					break;
			
			default:
				break;

			}
		};
	};
	private void initPopWindow() {
		popView = layoutInflater.inflate(R.layout.sign_in_pop, null);
		popView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		popupWindow = new PopupWindow(popView,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		//设置popwindow出现和消失动画
		popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
		signInGridView = (CommentsGridView) popView.findViewById(R.id.comments);
		signInAdapter = new SignInAdapter(popView.getContext(),mHandler,null);
		signInGridView.setAdapter(signInAdapter);
		btnPopClose = (ImageView) popView.findViewById(R.id.btn_pop_close);
	}
	public void showPop(View parent,int x,int y,User user) {
		
		signInAdapter.updateData(signInList);
		popupWindow.showAtLocation(parent,Gravity.CENTER, 0,0);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		if (popupWindow.isShowing()) {
		}
		btnPopClose.setOnClickListener(new OnClickListener() {
			public void onClick(View paramView) {
				popupWindow.dismiss();
			}
		});
		
	}
	private void initSignIn(){
		
		signInList = new ArrayList<Map<Integer,Integer>>();
		signInList.add(new HashMap<Integer, Integer>(){{put(100,1);}});
		signInList.add(new HashMap<Integer, Integer>(){{put(200,1);}});
		signInList.add(new HashMap<Integer, Integer>(){{put(300,1);}});
		signInList.add(new HashMap<Integer, Integer>(){{put(400,1);}});
		signInList.add(new HashMap<Integer, Integer>(){{put(500,1);}});
		signInList.add(new HashMap<Integer, Integer>(){{put(600,1);}});
		signInList.add(new HashMap<Integer, Integer>(){{put(700,1);}});
		
	}
	private void initUser() {
		httpUsersService.addParams(httpUsersService.URL_KEY,Constant.User_API_URL);
		httpUsersService.execute(this);
	}
	private void autoLogin() {
		httpLoginAutoService.execute(this);
	}
	private void logoutUser(){
		loginLayout.setVisibility(View.VISIBLE);
		logoutLayout.setVisibility(View.GONE);
		loginAfterLayout.setVisibility(View.GONE);
		httpLogotService.execute(this);
		
	}


	private void goToMyMissQuery(int type){
		Intent cinemaPoi = new Intent(getActivity(),MissSelfQueryActivity.class);
		cinemaPoi.putExtra(MISS_KEY, type);
		startActivity(cinemaPoi);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		    case R.id.user_sign_in:
		    	int[] arrayOfInt = new int[2];
				//获取点击按钮的坐标
				v.getLocationOnScreen(arrayOfInt);
		    	int popupWidth = popView.getMeasuredWidth();
				int popupHeight =  popView.getMeasuredHeight();
				int x = (arrayOfInt[0]+v.getWidth()/2)-popupWidth/2;
				int y =  arrayOfInt[1]-popupHeight;
				showPop(v,x,y, user);
		    	break;
			case R.id.user_logout_btn:
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setTitle("退出");
				builder.setMessage("确定要注销吗?");
				builder.setPositiveButton("取消",
						new DialogInterface.OnClickListener() {
							     public void onClick(DialogInterface diaCustomDialoglog,int which) {
							}
						});
				builder.setNegativeButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface diaCustomDialoglog,int which) {
								logoutUser();
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();
				break;
			case R.id.login_logo:
				Intent loginIntent = new Intent(getActivity(),LoginActivity.class);
				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				startActivity(loginIntent);
				getActivity().finish();
			
				break;
			case R.id.user_info_logo:
				Intent userIntent = new Intent(getActivity(),UserDetailActivity.class);
				userIntent.putExtra("memberId", user.getMemberId());
				//Intent userIntent = new Intent(getActivity(),MySelfDetailActivity.class);
				//getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				startActivity(userIntent);
				break;
			case R.id.user_edit:
				Intent userEditIntent = new Intent(getActivity(),UserActivity.class);
				//getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				startActivity(userEditIntent);
				break;
			case R.id.my_miss_layout:
				goToMyMissQuery(MY_MISS);
				break;
			case R.id.my_invitation_layout:
				goToMyMissQuery(MY_INVITATION);
				break;
	
			default:
				break;
	
			}

	}
	



	@Override
	public void SuccessCallBack(Map<String, Object> map) {
	
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if (tag.endsWith(httpUsersService.TAG)) {
				user = new User();			
				Map<String, Object> values = (Map<String, Object>) map.get(ReturnCode.RETURN_VALUE);
				user.setMemberId(values.get("memberId").toString());
				if (values.containsKey("portrait")) {
					user.setPortrait(Constant.SERVER_ADRESS+values.get("portrait").toString());
					loaderCache.DisplayImage(user.getPortrait(), userInfoLogo);
				}
				if (values.containsKey("sex")) {
					user.setSex(Integer.parseInt(values.get("sex").toString()));	
				}
				if (values.containsKey("nickname")) {
					user.setNickname(values.get("nickname").toString());
					userNickView.setText(user.getNickname());
				}
				if (values.containsKey("signature")) {
					user.setSignature(values.get("signature").toString());
					userSignView.setText(user.getSignature());
				}else{
					userSignView.setText(getResources().getString(R.string.user_none_sign));
				}
				if (values.containsKey("love")) {
					user.setLove(Integer.parseInt(values.get("love").toString()));
				}
				if (values.containsKey("charm")) {
					user.setLove(Integer.parseInt(values.get("charm").toString()));				
				}
				if(values.containsKey("tryst")){
					int tryst=Integer.parseInt(values.get("tryst").toString());
					user.setTryst(tryst);
				}
				loginLayout.setVisibility(View.GONE);
				logoutLayout.setVisibility(View.VISIBLE);
				loginAfterLayout.setVisibility(View.VISIBLE);
			}else if(tag.endsWith(httpLoginAutoService.TAG)){
				initUser();
			}
		}else if (Constant.ReturnCode.STATE_3.equals(code)) {
			//自动登陆
			autoLogin();
			
		}else {
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			//showToask(message);
		}
	}



	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		//showToask(message);
	}

	protected void showToask(String hint) {
		Toast toast = Toast.makeText(getActivity(), hint, Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public void OnRequest() {
		//showToask("加载个人信息");
	}

	

}
