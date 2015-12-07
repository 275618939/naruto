package com.movie.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.DynamicPhotoGridAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.NarutoApplication;
import com.movie.app.Constant.Page;
import com.movie.client.service.CallBackService;
import com.movie.pop.UserPhotoPopupWindow;
import com.movie.util.Bimp;
import com.movie.util.FileUtils;
import com.movie.util.ImageItem;
import com.movie.util.PhotoUtils;

public class DynamicCreateActivity extends BaseActivity implements
		OnClickListener, CallBackService {

	TextView title;
	TextView right;
	TextView content;
	GridView photoGridview;
	View rootView;
	ImageItem imageItem;
	ImageView addPicView;
	ImageView addEmoteView;
	DynamicPhotoGridAdapter photoGridAdapter;
	UserPhotoPopupWindow userPhotoPopupWindow;
	List<ImageItem> imageItems=new ArrayList<ImageItem>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dynamic_create);
		if(rootView==null){  
	        rootView=getLayoutInflater().inflate(R.layout.activity_dynamic_create,null);  
	    }  
		userPhotoPopupWindow = new UserPhotoPopupWindow(this,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		setContentView(rootView);
		initViews();
		initEvents();
		initData();
	}

	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right_text);
		content = (TextView) findViewById(R.id.dynamic_content);
		addPicView = (ImageView) findViewById(R.id.addPic);
		addEmoteView = (ImageView) findViewById(R.id.addEmote);
		photoGridview = (GridView) findViewById(R.id.userPhotoGridview);
		photoGridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		photoGridAdapter = new DynamicPhotoGridAdapter(this, mHandler, imageItems);
		photoGridview.setAdapter(photoGridAdapter);
		right.setVisibility(View.VISIBLE);
		
	}

	@Override
	protected void initEvents() {
		right.setOnClickListener(this);
		addPicView.setOnClickListener(this);
		addEmoteView.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		title.setText("创建动态");
		right.setText("发送");
		Bimp.tempSelectBitmap.clear();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.addPic:
				userPhotoPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
				break;
			default:
				break;
		}

	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
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
				Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
				if (cursor != null) {
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					if (cursor.getCount() > 0 && cursor.moveToFirst()) {
						String path = cursor.getString(column_index);
						boolean isImage=PhotoUtils.IsImage(path);
						if(!isImage){
							showToask("请选择正确的图片文件");
							return;
						}
						//压缩图片
						Bitmap bitmap = PhotoUtils.createBitmap(path,R.dimen.dynamic_image_content_width,R.dimen.dynamic_image_content_height);
						imageItem= new ImageItem();
						imageItem.setImagePath(path);
						imageItem.setBitmap(bitmap);
						imageItems.add(imageItem);
						bitmap=null;
						imageItem=null;
						photoGridAdapter.notifyDataSetChanged();
					}
				}
				cursor.close();
				uri=null;
				proj=null;
				data=null;
			}
			break;
		case PhotoUtils.INTENT_REQUEST_CODE_CAMERA:
			if (resultCode == RESULT_OK) {
				String path=userPhotoPopupWindow.getTakeImagePath();
				if (path != null) {
				   //按比例缩放图片
				   path = PhotoUtils.savePhotoToSDCard(PhotoUtils.CompressionPhoto(NarutoApplication.getApp().mScreenWidth, path, 2));					
				   Bitmap bitmap = PhotoUtils.getBitmapFromFile(path);
				   imageItem= new ImageItem();
				   imageItem.setImagePath(path);
				   imageItem.setBitmap(bitmap);
				   imageItems.add(imageItem);
				   bitmap=null;
				   imageItem=null;
				   photoGridAdapter.notifyDataSetChanged();
				}
			}
			break;
		default:
			break;
			
		}
		if(imageItems.size()==Page.MAX_SHOW_USER_PHOTO){
			addPicView.setVisibility(View.GONE);
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			default:
				break;

			}
		};
	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		this.finish();
	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {

		hideProgressDialog();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {

		} else {
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}

	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
	}

	@Override
	public void OnRequest() {
		showProgressDialog("提示", "请稍后......");

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		imageItems.clear();
		photoGridAdapter=null;
		photoGridview=null;
	}

}
