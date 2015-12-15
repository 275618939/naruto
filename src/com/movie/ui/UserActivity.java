package com.movie.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.DynamicPhotoGridAdapter;
import com.movie.adapter.UserPhotoGridAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpUploadImageService;
import com.movie.network.HttpUserDeleteImageService;
import com.movie.network.HttpUserImageQueryService;
import com.movie.network.HttpUserService;
import com.movie.network.HttpUserUpdateService;
import com.movie.pop.UserPhotoPopupWindow;
import com.movie.util.Bimp;
import com.movie.util.FileUtils;
import com.movie.util.ImageItem;
import com.movie.util.PhotoUtils;
import com.movie.util.StringUtil;
import com.movie.util.UploadUtil;
import com.movie.view.BirthdayDialog;
import com.movie.view.RoundImageView;
import com.movie.view.SexDialog;

public class UserActivity extends BaseActivity implements OnClickListener,CallBackService {
	
	public static final int DELETE_IMAGE=0x1;
	UploadUtil uploadUtil = UploadUtil.getInstance();
	EditText accountEdit;
	EditText passwordEdit;
	Button loginButton;
	TextView title;
	TextView right_text;
	TextView nickName;
	TextView sex;
	TextView birthday;
	TextView mobile;
	TextView email;
	TextView sign;
	ImageView captchaView;
	RoundImageView headImage;
	LinearLayout layoutNick;
	LinearLayout layoutSex;
	LinearLayout layoutBirthday;
	LinearLayout layoutMobile;
	LinearLayout layoutEmail;
	LinearLayout layoutSign;
	LinearLayout layoutHobby;
	LinearLayout layoutHead;
	BaseService httpUserService;
	BaseService httpUserUpdateService;
	BaseService httpUserDeleteImageService;
	BaseService httpUserQueryImageService;
	BaseService httpUploadImageService;
	boolean isHead;
	User user;
	ImageItem imageItem;
	List<Integer> userHobbis;
	UserPhotoPopupWindow userPhotoPopupWindow;
	GridView photoGridview;
	DynamicPhotoGridAdapter photoGridAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		httpUserUpdateService = new HttpUserUpdateService(this);
		httpUserService = new HttpUserService(this);
		httpUserQueryImageService = new HttpUserImageQueryService(this);
		httpUserDeleteImageService = new HttpUserDeleteImageService(this);
		httpUploadImageService = new HttpUploadImageService(this);
		userPhotoPopupWindow = new UserPhotoPopupWindow(this,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		initViews();
		initEvents();
		initData();
	}

	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		nickName = (TextView) findViewById(R.id.nickName);
		sex = (TextView) findViewById(R.id.sex);
		birthday = (TextView) findViewById(R.id.birthday);
		mobile = (TextView) findViewById(R.id.mobile);
		email = (TextView) findViewById(R.id.email);
		sign = (TextView) findViewById(R.id.sign);
		headImage = (RoundImageView) findViewById(R.id.head_image);
		layoutNick = (LinearLayout) findViewById(R.id.layout_nick);
		layoutSex = (LinearLayout) findViewById(R.id.layout_sex);
		layoutBirthday = (LinearLayout) findViewById(R.id.layout_birthday);
		layoutMobile = (LinearLayout) findViewById(R.id.layout_mobile);
		layoutEmail = (LinearLayout) findViewById(R.id.layout_email);
		layoutSign = (LinearLayout) findViewById(R.id.layout_sign);
		layoutHobby = (LinearLayout) findViewById(R.id.layout_hobby);
		layoutHead = (LinearLayout) findViewById(R.id.layout_head);
		photoGridview = (GridView)findViewById(R.id.userPhotoGridview);
		photoGridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		Log.i("bimpsize1", Bimp.tempSelectBitmap.size()+"");
		Bimp.tempSelectBitmap.clear();
		Log.i("bimpsize2", Bimp.tempSelectBitmap.size()+"");
		photoGridAdapter =new DynamicPhotoGridAdapter(this, mHandler,Bimp.tempSelectBitmap);
		photoGridview.setAdapter(photoGridAdapter);
	}
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DELETE_IMAGE:
				int position= msg.getData().getInt("position");
				String imageId=Bimp.tempSelectBitmap.get(position).getImageId();
				//删除用户图片
				if(imageId!=null&&!imageId.isEmpty()){
					deleteImage(imageId);
				}else{
				   Bimp.tempSelectBitmap.get(position).getBitmap().recycle();
				}
				Bimp.tempSelectBitmap.remove(position);
				photoGridAdapter.notifyDataSetChanged();
				break;
			
			default:
				break;

			}
		};
	};
	@Override
	protected void initEvents() {
		layoutNick.setOnClickListener(this);
		layoutSex.setOnClickListener(this);
		layoutBirthday.setOnClickListener(this);
		layoutMobile.setOnClickListener(this);
		layoutEmail.setOnClickListener(this);
		layoutSign.setOnClickListener(this);
		layoutHobby.setOnClickListener(this);
		layoutHead.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		title.setText("编辑信息");
		loadUser();
		loadUserImage();
	}
	private void deleteImage(String imageId){
		httpUserDeleteImageService.addParams("imageId", imageId);
		httpUserDeleteImageService.execute(this);
	}
	private void loadUser() {
		httpUserService.execute(this);
	}
	private void loadUserImage(){
		
		httpUserQueryImageService.execute(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_nick:
			Intent intent = new Intent(this, NickActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.layout_mobile:
			Intent intentMobile = new Intent(this, MobileActivity.class);
			startActivity(intentMobile);
			this.finish();
			break;
		case R.id.layout_email:
			Intent intentEmail = new Intent(this, EmailActivity.class);
			startActivity(intentEmail);
			this.finish();
			break;
		case R.id.layout_sign:
			Intent intentSign = new Intent(this, SignActivity.class);
			startActivity(intentSign);
			this.finish();
			break;
		case R.id.layout_hobby:
			Intent intentHobby = new Intent(this, HobbyActivity.class);
			intentHobby.putExtra("user", user);
			startActivity(intentHobby);
			this.finish();
			break;
		case R.id.layout_head:
			isHead=true;
			userPhotoPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.layout_sex:
			final SexDialog.Builder builder = new SexDialog.Builder(this);
			builder.setTitle(R.string.sex_hint);
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
							int sex = builder.getSex();
							httpUserUpdateService.clearReqeustParams();
							httpUserUpdateService.addParams("sex", sex);
							UserActivity.this.sex.setText(sexChangeStr(sex));
							modifyUserInfo();
						}
					});

			builder.create().show();
			break;
		case R.id.layout_birthday:

			final BirthdayDialog.Builder birthdayBuilder = new BirthdayDialog.Builder(this);
			birthdayBuilder.setTitle(R.string.birthday_hint);
			birthdayBuilder.setPositiveButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			birthdayBuilder.setNegativeButton("确定",
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							DatePicker datePicker = birthdayBuilder
									.getBirthDay();
							int year = datePicker.getYear();
							int month = datePicker.getMonth();
							int data = datePicker.getDayOfMonth();
							String value = StringUtil.dateChangeStr(year,
									month, data);
							birthday.setText(StringUtil.strChangeDate(value));
							httpUserUpdateService.clearReqeustParams();
							httpUserUpdateService.addParams("birthday", value);
							modifyUserInfo();
						}
					});

			birthdayBuilder.create().show();
			break;

		}

	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		
		case PhotoUtils.INTENT_REQUEST_CODE_ALBUM:
			if (data == null) {
				return;
			}
			if (resultCode == RESULT_OK) {
				if (data.getData() == null) {
					return;
				}
				if (!FileUtils.isSdcardExist()) {
					showToask("SD卡不可用,请检查");
					return;
				}
				Uri uri = data.getData();
				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(uri, proj, null,null, null);
				if (cursor != null) {
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					if (cursor.getCount() > 0 && cursor.moveToFirst()) {
						String picPath = cursor.getString(column_index);
						boolean isImage = PhotoUtils.IsImage(picPath);
						if (!isImage) {
							showToask("请选择正确的图片文件");
							return;
						}
						// 压缩图片
						Bitmap bitmap = PhotoUtils.createBitmap(picPath,NarutoApplication.getApp().mScreenWidth / 2,NarutoApplication.getApp().mScreenHeight / 2);
						if(isHead){
							headImage.setImageBitmap(bitmap);
							//重新保存压缩后的图片
							FileUtils.saveBitmapByPath(bitmap, picPath);
							//上传图片
							upload(picPath,Constant.Portrait_Modify_API_URL);
							isHead=false;
						}else{
							imageItem= new ImageItem();
							imageItem.setImagePath(picPath);
							imageItem.setBitmap(bitmap);
							imageItem.setSelected(true);
							Bimp.tempSelectBitmap.add(imageItem);
							imageItem=null;
							photoGridAdapter.notifyDataSetChanged();
						}
						bitmap=null;
						System.gc();
					}
				}
				cursor.close();
				uri = null;
				proj = null;
				data = null;
			}
			break;
		case PhotoUtils.INTENT_REQUEST_CODE_CAMERA:
			if (resultCode == RESULT_OK) {
				String picPath=null;
				if(isHead){
					picPath = userPhotoPopupWindow.getTakeImagePath();
				}else{
					//picPath = photoGridAdapter.getUserPhotoPopupWindow().getTakeImagePath();
				}
				if (picPath != null) {
					// 按比例缩放图片
					Bitmap bitmap = PhotoUtils.createBitmap(picPath,NarutoApplication.getApp().mScreenWidth / 2,NarutoApplication.getApp().mScreenHeight / 2);
					if(isHead){
						headImage.setImageBitmap(bitmap);
					   //重新保存压缩后的图片
					   FileUtils.saveBitmapByPath(bitmap, picPath);
					   //上传图片
					   upload(picPath,Constant.Portrait_Modify_API_URL);
					   isHead=false;
					}else{
						imageItem= new ImageItem();
						imageItem.setImagePath(picPath);
						imageItem.setBitmap(bitmap);
						imageItem.setSelected(true);
						Bimp.tempSelectBitmap.add(imageItem);
						imageItem=null;
						photoGridAdapter.notifyDataSetChanged();
					}
					 bitmap=null;
				
					System.gc();
				}
			}
			break;
		default:
			break;
		}
	}
	private void synUserPhoto(){
		for(ImageItem imageItem:Bimp.tempSelectBitmap){
			if(imageItem.isSelected){
				//上传图片
				upload(imageItem.getImagePath(),Constant.User_Image_Upload_API_URL);
			}
		}
		httpUploadImageService=null;
		Bimp.tempSelectBitmap.clear();
		//PhotoUtils.deleteImageFile();
	}
	private void upload(String picPath,String url) {
		
		httpUploadImageService.addUrls(url);
		httpUploadImageService.addParams("file", new File(picPath));
		httpUploadImageService.execute(this);
		/*String fileKey = "image";
		Map<String, String> heads = new HashMap<String, String>();
		try {
			heads.put("Session-Id", httpUserService.getSid());
		} catch (InvokeException e) {
			e.printStackTrace();
		}
		uploadUtil.uploadFile(picPath, fileKey,url, heads, null);*/
	}



	// 修改用户信息
	protected void modifyUserInfo() {
		httpUserUpdateService.execute(this);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		this.startActivity(intent);
		this.finish();
	}

	public String sexChangeStr(int sex) {
		if (sex == Constant.Sex.MEN) {
			return "男";
		} else if (sex == Constant.Sex.WOMEN) {
			return "女";
		} else {
			return "保密";
		}

	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {

		hideProgressDialog();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if (tag.endsWith(httpUserService.TAG)) {
				Map<String, Object> value = (Map<String, Object>) map.get(Constant.ReturnCode.RETURN_VALUE);
				if (null != value) {
					user = new User();
					user.setMemberId(value.get("memberId").toString());
					if (value.containsKey("nickname")) {
						nickName.setText(value.get("nickname").toString());
						user.setNickname(value.get("nickname").toString());
					}
					if (value.containsKey("sex")) {
						user.setSex(Integer.parseInt(value.get("sex").toString()));
						sex.setText(sexChangeStr(Integer.parseInt(value.get("sex").toString())));
					}
					if (value.containsKey("birthday"))
						birthday.setText(StringUtil.strChangeDate(value.get("birthday").toString()));
					if (value.containsKey("mobile"))
						mobile.setText(value.get("mobile").toString());
					if (value.containsKey("email"))
						email.setText(value.get("email").toString());
					if (value.containsKey("signature")) {
						sign.setText(value.get("signature").toString());
						user.setSignature(value.get("signature").toString());
					}
					if (value.containsKey("portrait")) {
						user.setPortrait( Constant.SERVER_ADRESS+ value.get("portrait").toString());
						imageLoader.displayImage(user.getPortrait(), headImage,NarutoApplication.imageOptions);
					}
					if (value.containsKey("hobbies")) {
						user.setHobbies((List<Integer>) value.get("hobbies"));
					}
					if (value.containsKey("tryst")) {
						user.setTryst(Integer.parseInt(value.get("tryst").toString()));
					}
				}

			}else if (tag.endsWith(httpUserQueryImageService.TAG)) {
				Bimp.tempSelectBitmap.clear();
				List<HashMap<String, Object>> datas = (ArrayList<HashMap<String, Object>>) map.get(Constant.ReturnCode.RETURN_VALUE);
				int size = datas.size();
				HashMap<String, Object> missMap = null;
				ImageItem imageItem=null;
				for (int i = 0; i < size; i++) {
					imageItem = new ImageItem();
					imageItem.setSelected(false);
					missMap = datas.get(i);
					if (missMap.containsKey("id"))
						imageItem.setImageId(missMap.get("id").toString());
					if (missMap.containsKey("url"))
						imageItem.setImagePath(Constant.SERVER_ADRESS+missMap.get("url").toString());
					//Bimp.tempSelectBitmap.add(imageItem);
				}
				//photoGridAdapter.notifyDataSetChanged();
			}else if (tag.endsWith(httpUploadImageService.TAG)) {
				
			}
		} else {
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}
		map = null;
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
	}

	@Override
	public void OnRequest() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		httpUserDeleteImageService = null;
		httpUserUpdateService = null;
		//httpUserService = null;
		userHobbis = null;
		//退出时同步用户新增图片
		synUserPhoto();
		
	}

}
