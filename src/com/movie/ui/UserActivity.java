package com.movie.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.InvokeException;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpUserService;
import com.movie.network.HttpUserUpdateService;
import com.movie.pop.UserPhotoPopupWindow;
import com.movie.util.Bimp;
import com.movie.util.FileUtils;
import com.movie.util.PathUtil;
import com.movie.util.PhotoUtils;
import com.movie.util.StringUtil;
import com.movie.util.UploadUtil;
import com.movie.view.BirthdayDialog;
import com.movie.view.RoundImageView;
import com.movie.view.SexDialog;

public class UserActivity extends BaseActivity implements OnClickListener,
		CallBackService {

    private  final int TAKE_PICTURE = 11;// 拍照
	private  final int SELECT_PICTURE = 22;// 选择照片
	public final static int loadUserImage = 1;
	public static final String KEY_PHOTO_PATH = "photo_path";

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
	BaseService httpUserUpdateService;
	BaseService httpUserService;
	Uri photoUri;
	List<Integer> userHobbis;
	String headUrl;
	String picPath;
	Intent lastIntent;
	User user;
	UserPhotoPopupWindow userPhotoPopupWindow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		httpUserUpdateService = new HttpUserUpdateService(this);
		httpUserService = new HttpUserService(this);
		userPhotoPopupWindow = new UserPhotoPopupWindow(this,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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
		lastIntent = getIntent();
	}

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
		getUser();
	}

	private void getUser() {
		httpUserService.execute(this);
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
			userPhotoPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			// final HeadDialog.Builder headBuilder=new
			// HeadDialog.Builder(this);
			// headBuilder.setTitle(R.string.head_hint);
			// headBuilder.setPhotograph(new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// dialog.dismiss();
			// photo();
			// }
			// });
			// headBuilder.setPictureLib(new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// dialog.dismiss();
			//
			// pickPhoto();
			// }
			// });
			// headBuilder.setPositiveButton("取消",
			// new DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog, int which) {
			// dialog.dismiss();
			// }
			// });
			// headBuilder.setNegativeButton("确定",
			// new android.content.DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog, int which) {
			// dialog.dismiss();
			//
			// }
			// });
			// headBuilder.create().show();
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

			final BirthdayDialog.Builder birthdayBuilder = new BirthdayDialog.Builder(
					this);
			/*
			 * String temp_date=birthday.getText().toString();
			 * if(null!=temp_date&&!temp_date.isEmpty()) { int []
			 * arry=StringUtil.strChangeInt(temp_date);
			 * birthdayBuilder.setBirthDay(arry[0],arry[1], arry[2]); }
			 */
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

	public void pickPhoto() {
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, SELECT_PICTURE);
		} else {
			Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
		}
	}

	public void photo() {
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(openCameraIntent, TAKE_PICTURE);
		} else {
			Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
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
						picPath = cursor.getString(column_index);
						boolean isImage = PhotoUtils.IsImage(picPath);
						if (!isImage) {
							showToask("请选择正确的图片文件");
							return;
						}
						// 压缩图片
						Bitmap bitmap = PhotoUtils.createBitmap(picPath,NarutoApplication.getApp().mScreenWidth / 2,NarutoApplication.getApp().mScreenHeight / 2);
					
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
				picPath = userPhotoPopupWindow.getTakeImagePath();
				if (picPath != null) {
					// 按比例缩放图片
					Bitmap bitmap = PhotoUtils.createBitmap(picPath,NarutoApplication.getApp().mScreenWidth / 2,NarutoApplication.getApp().mScreenHeight / 2);
					System.gc();
				}
			}
			break;

		
		case SELECT_PICTURE:
			doPhoto(SELECT_PICTURE, data);
			if (null != picPath && !picPath.isEmpty()) {
				// Bitmap image = BitmapFactory.decodeFile(picPath);
				Bitmap image = Bimp.getSmallBitmap(picPath,Constant.ImageSize.HEADWIDTH,Constant.ImageSize.HEADHEIGTH);
				headImage.setImageBitmap(image);
				photoUri = Uri.parse(MediaStore.Images.Media.insertImage(
						getContentResolver(), image, null, null));
				doPhoto(TAKE_PICTURE, data);
				upload();
				image = null;
			}
			break;
		case TAKE_PICTURE:
			try {
				Bitmap image = (Bitmap) data.getExtras().get("data");
				headImage.setImageBitmap(image);
				photoUri = Uri.parse(MediaStore.Images.Media.insertImage(
						getContentResolver(), image, null, null));
				doPhoto(TAKE_PICTURE, data);
				upload();
				image = null;
				break;
			} catch (NullPointerException e) {

			}
		default:
			break;
		}

	}

	private void upload() {
		String fileKey = "image";
		UploadUtil uploadUtil = UploadUtil.getInstance();
		;
		Map<String, String> heads = new HashMap<String, String>();
		try {
			heads.put("Session-Id", httpUserService.getSid());
		} catch (InvokeException e) {
			e.printStackTrace();
		}
		uploadUtil.uploadFile(picPath, fileKey,
				Constant.Portrait_Modify_API_URL, heads, null);
	}

	/**
	 * 选择图片后，获取图片的路径
	 * 
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode, Intent data) {
		if (requestCode == SELECT_PICTURE) {
			if (data == null) {
				// Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			photoUri = data.getData();
			if (photoUri == null) {
				// Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
		}
		picPath = PathUtil.getPath(this, photoUri);
		Log.i("User ui", "imagePath = " + picPath);
		if (picPath != null
				&& (picPath.endsWith(".png") || picPath.endsWith(".PNG")
						|| picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
		} else {
			Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
		}
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
				Map<String, Object> value = (Map<String, Object>) map
						.get(Constant.ReturnCode.RETURN_VALUE);
				if (null != value) {
					user = new User();
					user.setMemberId(value.get("memberId").toString());
					if (value.containsKey("nickname")) {
						nickName.setText(value.get("nickname").toString());
						user.setNickname(value.get("nickname").toString());
					}
					if (value.containsKey("sex")) {
						user.setSex(Integer.parseInt(value.get("sex")
								.toString()));
						sex.setText(sexChangeStr(Integer.parseInt(value.get(
								"sex").toString())));
					}
					if (value.containsKey("birthday"))
						birthday.setText(StringUtil.strChangeDate(value.get(
								"birthday").toString()));
					if (value.containsKey("mobile"))
						mobile.setText(value.get("mobile").toString());
					if (value.containsKey("email"))
						email.setText(value.get("email").toString());
					if (value.containsKey("signature")) {
						sign.setText(value.get("signature").toString());
						user.setSignature(value.get("signature").toString());
					}
					if (value.containsKey("portrait")) {
						headUrl = Constant.SERVER_ADRESS
								+ value.get("portrait").toString();
						user.setPortrait(headUrl);
						imageLoader.displayImage(headUrl, headImage,
								NarutoApplication.imageOptions);
					}
					if (value.containsKey("hobbies")) {
						user.setHobbies((List<Integer>) value.get("hobbies"));
					}
					if (value.containsKey("tryst")) {
						int tryst = Integer.parseInt(value.get("tryst")
								.toString());
						user.setTryst(tryst);
					}
				}

			}
		} else {
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE)
					.toString();
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
		httpUserUpdateService = null;
		httpUserService = null;
		photoUri = null;
		userHobbis = null;
	}

}
