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
import com.movie.network.HttpLoginAutoService;
import com.movie.network.HttpLogoutService;
import com.movie.network.HttpUserService;
import com.movie.pop.SignInPopupWindow;
import com.movie.ui.LoginActivity;
import com.movie.ui.MissSelfQueryActivity;
import com.movie.ui.UserActivity;
import com.movie.ui.UserDetailActivity;
import com.movie.util.UserCharm;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SelfFragment extends BaseFragment implements OnClickListener , CallBackService{
	
	public static final int RELOAGIN = 0X101;
	public static final int PTHOTO_UPDATE = 0X001;
	public static final int LOGOUT = 0X110;
	User user;
	BaseService httpLoginAutoService;
	BaseService httpUsersService;
	BaseService httpLogotService;
	RelativeLayout loginLayout;
	RelativeLayout logoutLayout;
	LinearLayout myMissLayout;
	LinearLayout loginAfterLayout;
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
	TextView inviteCntView;
	ImageView userSignIn;
	SignInPopupWindow signInPopupWindow;
	UserPhotoGridAdapter photoGridAdapter;
	GridView photoGridview;
	boolean isLoad;
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
		httpLoginAutoService = new HttpLoginAutoService(getActivity());
		initPopWindow();
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
		inviteCntView= (TextView)rootView.findViewById(R.id.inviteCnt);
		userSignIn = (ImageView)rootView.findViewById(R.id.user_sign_in);
		photoGridview = (GridView)rootView.findViewById(R.id.userPhotoGridview);
		photoGridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		photoGridAdapter =new UserPhotoGridAdapter(getActivity(), mHandler,null);
		photoGridview.setAdapter(photoGridAdapter);
		photoGridview.setOnItemClickListener(photoGridAdapter);	
		
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
		if(!isLoad){
			autoLogin();
		}
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
	private void autoLogin() {
		httpLoginAutoService.execute(this);
	}
	private void logoutUser(){
		loginLayout.setVisibility(View.VISIBLE);
		logoutLayout.setVisibility(View.GONE);
		loginAfterLayout.setVisibility(View.GONE);
		httpLogotService.execute(this);
	}
	private void missQuery(int type){
		Intent cinemaPoi = new Intent(getActivity(),MissSelfQueryActivity.class);
		cinemaPoi.putExtra(Miss.MISS_KEY, type);
		startActivity(cinemaPoi);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { 
		   case RELOAGIN:
		    //Bundle b=data.getExtras(); 
		    //String str=b.getString("str1");
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
				//startActivity(loginIntent);
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
			case R.id.my_miss_layout:
				missQuery(Miss.MY_MISS);
				break;
			case R.id.my_invitation_layout:
				missQuery(Miss.MY_INVITATION);
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
				}
				if(values.containsKey("applyCnt")){
					user.setApplyCnt(Integer.parseInt(values.get("applyCnt").toString()));
					applyCntView.setText(String.format(getResources().getString(R.string.apply_miss_have),user.getApplyCnt()));
					applyCntView.setOnClickListener(this);
				}
				if(values.containsKey("inviteCnt")){
					user.setInviteCnt(Integer.parseInt(values.get("inviteCnt").toString()));
					inviteCntView.setText(String.format(getResources().getString(R.string.invite_miss_have),user.getInviteCnt()));
					inviteCntView.setOnClickListener(this);
				}
				String score=UserCharm.GetScore(user.getFace(), user.getFaceCnt()<=0?1:user.getFaceCnt());
				if(!score.equals("NaN")){
					selfCharmView.setText(score);
				}
				loginLayout.setVisibility(View.GONE);
				logoutLayout.setVisibility(View.VISIBLE);
				loginAfterLayout.setVisibility(View.VISIBLE);
				
			}else if(tag.equals(httpLoginAutoService.TAG)){
				isLoad=true;
				loadUser();
			}else if(tag.equals(httpLogotService.TAG)){
				
			}
		}else if (Constant.ReturnCode.STATE_3.equals(code)) {
			//提示用户登入
			//autoLogin();
			Intent loginIntent = new Intent(getActivity(),LoginActivity.class);
			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			startActivityForResult(loginIntent, RELOAGIN);
		}else {
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		String state= map.get(Constant.ReturnCode.RETURN_STATE).toString();
//		if(state.equals(ReturnCode.STATE_999)){
//			loadView.showLoadLineFail(this);
//		}else{
//			loadView.showLoadFail(this, this);
//			showToask(message);
//		}
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
	

	

}
