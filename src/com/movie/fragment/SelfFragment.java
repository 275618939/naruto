package com.movie.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.UserPhotoGridAdapter;
import com.movie.app.BaseFragment;
import com.movie.app.Constant;
import com.movie.app.Constant.ReturnCode;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.Miss;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpLogoutService;
import com.movie.network.HttpUserService;
import com.movie.pop.SignInPopupWindow;
import com.movie.ui.LoginActivity;
import com.movie.ui.MissQueryActivity;
import com.movie.ui.MissSelfQueryActivity;
import com.movie.ui.UserActivity;
import com.movie.ui.UserDetailActivity;
import com.movie.ui.UserLoveMovieActivity;
import com.movie.util.Bimp;
import com.movie.util.ImageItem;
import com.movie.util.UserCharm;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SelfFragment extends BaseFragment implements OnClickListener , CallBackService{
	
	public static final int RELOAGIN = 0X101;
	public static final int PTHOTO_UPDATE = 0X001;
	public static final int LOGOUT = 0X110;
	User user;
	BaseService httpUsersService;
	BaseService httpLogotService;
	RelativeLayout loginLayout;
	RelativeLayout logoutLayout;
	LinearLayout myMissLayout;
	LinearLayout loginAfterLayout;
	LinearLayout seeMoiveLayout;
	ImageLoader loaderCache;
	ImageView loginLogo;
	ImageView userInfoLogo;
	ImageView userEdit;
	TextView userNickView;
	TextView userSignView;
	TextView userLogoutBtn;
	TextView selfCharmView;
	TextView selfLoveView;
	TextView trystCntView;
	TextView applyCntView;
	TextView attendCntView;
	TextView filmCntView;
	ImageView userSignIn;
	ImageItem imageItem;
	SignInPopupWindow signInPopupWindow;
	UserPhotoGridAdapter photoGridAdapter;
	GridView photoGridview;
	LinearLayout userPhotoMangerLayout;
	public SelfFragment() {
		super();		
	}
	public SelfFragment(Activity activity,Context context) {
		super(activity, context);
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		loaderCache = ImageLoader.getInstance();
		httpUsersService = new HttpUserService(getActivity());
		httpLogotService = new HttpLogoutService(getActivity());
		initPopWindowData();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		
		if(rootView==null){  
	        rootView=inflater.inflate(R.layout.fragment_self,container,false);  
	    }  
		ViewGroup parent = (ViewGroup) rootView.getParent();  
	    if (parent != null) {  
	        parent.removeView(rootView);  
	    }   
		isVisible=true;
		isPrepared=true;
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void initViews() {
		loginLayout=(RelativeLayout)rootView.findViewById(R.id.login_layout);
		logoutLayout=(RelativeLayout)rootView.findViewById(R.id.logout_layout);
		myMissLayout=(LinearLayout)rootView.findViewById(R.id.my_miss_layout);
		loginAfterLayout=(LinearLayout)rootView.findViewById(R.id.loginAfter);
		seeMoiveLayout=(LinearLayout)rootView.findViewById(R.id.see_moive_layout);
		loginLogo = (ImageView)rootView.findViewById(R.id.login_logo);
		userInfoLogo = (ImageView)rootView.findViewById(R.id.user_info_logo);
		userEdit = (ImageView)rootView.findViewById(R.id.user_edit);
		userNickView = (TextView)rootView.findViewById(R.id.usernick);
		userSignView = (TextView)rootView.findViewById(R.id.usersign);
		userLogoutBtn = (TextView)rootView.findViewById(R.id.user_logout_btn);
		selfCharmView = (TextView)rootView.findViewById(R.id.self_charm);
		selfLoveView = (TextView)rootView.findViewById(R.id.self_love);
		trystCntView = (TextView)rootView.findViewById(R.id.trystCnt);
		applyCntView= (TextView)rootView.findViewById(R.id.applyCnt);
		attendCntView= (TextView)rootView.findViewById(R.id.attendCnt);
		filmCntView = (TextView) rootView.findViewById(R.id.filmCnt);
		userSignIn = (ImageView)rootView.findViewById(R.id.user_sign_in);
		userPhotoMangerLayout = (LinearLayout)rootView.findViewById(R.id.user_photo_manger);
		photoGridview = (GridView)rootView.findViewById(R.id.userPhotoGridview);
		photoGridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		photoGridAdapter =new UserPhotoGridAdapter(getActivity(), mHandler,Bimp.tempSelectBitmap);
		photoGridview.setAdapter(photoGridAdapter);
		Bimp.photoGridAdapter=photoGridAdapter;
		
	}
	@Override
	protected void initEvents() {
		loginLogo.setOnClickListener(this);
		userInfoLogo.setOnClickListener(this);
		userEdit.setOnClickListener(this);
		userLogoutBtn.setOnClickListener(this);
		myMissLayout.setOnClickListener(this);
		userSignIn.setOnClickListener(this);
	}
	@Override
	protected void lazyLoad() {
		View titleView = getActivity().findViewById(R.id.main_head);
		if(null!=titleView){
			titleView.setVisibility(View.GONE);
		}
		if (!isVisible||!isPrepared) {
			return;
		}		
		loadUser();
		
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
	private void initPopWindowData() {
		signInPopupWindow = new SignInPopupWindow(getActivity(), mHandler);
		/*临时签到数据，正式环境需去除*/
		List<Map<Integer,Integer>> signInList = new ArrayList<Map<Integer,Integer>>();
		signInList.add(new HashMap<Integer, Integer>(){{put(100,1);}});
		signInList.add(new HashMap<Integer, Integer>(){{put(200,1);}});
		signInList.add(new HashMap<Integer, Integer>(){{put(300,1);}});
		signInList.add(new HashMap<Integer, Integer>(){{put(400,1);}});
		signInList.add(new HashMap<Integer, Integer>(){{put(500,1);}});
		signInList.add(new HashMap<Integer, Integer>(){{put(600,1);}});
		signInList.add(new HashMap<Integer, Integer>(){{put(700,1);}});
		signInPopupWindow.updateData(signInList);
		signInPopupWindow.setOutsideTouchable(true);
		
	}
	public void showPop(View parent,int x,int y) {	
		signInPopupWindow.showAtLocation(parent,Gravity.CENTER, 0,0);
		signInPopupWindow.setFocusable(true);
	}
	private void loadUser() {
		httpUsersService.addParams(httpUsersService.URL_KEY,Constant.User_API_URL);
		httpUsersService.execute(this);
	}
	
	private void logoutUser(){
		loginLayout.setVisibility(View.VISIBLE);
		logoutLayout.setVisibility(View.GONE);
		loginAfterLayout.setVisibility(View.GONE);
		userPhotoMangerLayout.setVisibility(View.GONE);
		httpLogotService.execute(this);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { 
		   case RELOAGIN:
			    loadUser();
			    break;	
			default:
				break;
				
			
		    }
		}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			
		    case R.id.user_sign_in:
		    	int[] arrayOfInt = new int[2];
				//获取点击按钮的坐标
				v.getLocationOnScreen(arrayOfInt);
		    	int popupWidth = signInPopupWindow.getContentView().getMeasuredWidth();
				int popupHeight =  signInPopupWindow.getContentView().getMeasuredHeight();
				int x = (arrayOfInt[0]+v.getWidth()/2)-popupWidth/2;
				int y =  arrayOfInt[1]-popupHeight;
				showPop(v,x,y);
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
				startActivityForResult(loginIntent, RELOAGIN);
				break;
			case R.id.user_info_logo:
				Intent userIntent = new Intent(getActivity(),UserDetailActivity.class);
				userIntent.putExtra("memberId", user.getMemberId());
				startActivity(userIntent);
				break;
			case R.id.user_edit:
				Intent userEditIntent = new Intent(getActivity(),UserActivity.class);			
				startActivity(userEditIntent);
				break;
			case R.id.filmCnt:
				Intent movieIntent = new Intent(getActivity(),UserLoveMovieActivity.class);
				movieIntent.putExtra("user", user);
				startActivity(movieIntent);
				break;
			case R.id.my_miss_layout:
				Intent myMissIntent = new Intent(getActivity(),MissSelfQueryActivity.class);
				myMissIntent.putExtra(Miss.MISS_KEY, Miss.MY_MISS);
				startActivity(myMissIntent);
				break;
			case R.id.attendCnt:
				Intent attendIntent = new Intent(getActivity(),MissQueryActivity.class);
				attendIntent.putExtra(Miss.MISS_KEY, Miss.ATTEDD_MISS);
				startActivity(attendIntent);
				break;
			case R.id.applyCnt:
				Intent applyIntent = new Intent(getActivity(),MissQueryActivity.class);
				applyIntent.putExtra(Miss.MISS_KEY, Miss.MY_APPLY);
				startActivity(applyIntent);
				break;
	
			default:
				break;
	
			}

	}
	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		//loadView.showLoadAfter(this);
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if (tag.equals(httpUsersService.TAG)) {
				isLoad=true;				
				user = new User();			
				Map<String, Object> values = (Map<String, Object>) map.get(ReturnCode.RETURN_VALUE);
				user.setMemberId(values.get("memberId").toString());
				if (values.containsKey("portrait")) {
					user.setPortrait(Constant.SERVER_ADRESS+values.get("portrait").toString());
					loaderCache.displayImage(user.getPortrait(), userInfoLogo,NarutoApplication.imageOptions);
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
				if (values.containsKey("loveCnt")) {
					user.setLove(Integer.parseInt(values.get("loveCnt").toString()));
					selfLoveView.setText(values.get("loveCnt").toString());
				}
				if(values.containsKey("faceTtl")){
					user.setFace(Integer.parseInt(values.get("faceTtl").toString()));
				}
				if(values.containsKey("faceCnt")){
					user.setFaceCnt(Integer.parseInt(values.get("faceCnt").toString()));
				}
				if(values.containsKey("trystCnt")){
					int tryst=Integer.parseInt(values.get("trystCnt").toString());
					user.setTryst(tryst);
					trystCntView.setText(String.format(getResources().getString(R.string.miss_have),tryst));
				}
				if(values.containsKey("filmCnt")){
					int filmCnt=Integer.parseInt(values.get("filmCnt").toString());
					user.setFilmCnt(filmCnt);
					filmCntView.setText(String.format(getResources().getString(R.string.movie_have),filmCnt));
					filmCntView.setOnClickListener(this);
				}
				if(values.containsKey("applyCnt")){
					user.setApplyCnt(Integer.parseInt(values.get("applyCnt").toString()));
					applyCntView.setText(String.format(getResources().getString(R.string.apply_miss_have),user.getApplyCnt()));
					applyCntView.setOnClickListener(this);
				}
				if(values.containsKey("attendCnt")){
					user.setInviteCnt(Integer.parseInt(values.get("attendCnt").toString()));
					attendCntView.setText(String.format(getResources().getString(R.string.attend_miss_have),user.getInviteCnt()));
					attendCntView.setOnClickListener(this);
				}
				
				String score=UserCharm.GetScore(user.getFace(), user.getFaceCnt()<=0?1:user.getFaceCnt());
				if(!score.equals("NaN")){
					selfCharmView.setText(score);
				}
				loginLayout.setVisibility(View.GONE);
				logoutLayout.setVisibility(View.VISIBLE);
				loginAfterLayout.setVisibility(View.VISIBLE);
				userPhotoMangerLayout.setVisibility(View.VISIBLE);
				
			}else if(tag.equals(httpLogotService.TAG)){
				 user=null;
			}
		}else if (Constant.ReturnCode.STATE_3.equals(code)) {
			//提示用户登入
//			Intent loginIntent = new Intent(getActivity(),LoginActivity.class);
//			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//			startActivityForResult(loginIntent, RELOAGIN);
		}else {
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		int state=Integer.parseInt(map.get(Constant.ReturnCode.RETURN_STATE).toString());
		if(state==Integer.parseInt(ReturnCode.STATE_999)){
			loadView.showLoadLineFail(this);
		}else if(state>=Integer.parseInt(ReturnCode.STATE_97)){
			loadView.showLoadFail(this, this);
		}else{
			showToask(message);
		}
	}
	@Override
	public void OnRequest() {
		//loadView.showLoading(this);
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		photoGridAdapter=null;
	}
	@Override
	protected void destroyData() {
		Bimp.tempSelectBitmap.clear();
		Bimp.photoGridAdapter=null;
	}
	

	

}
