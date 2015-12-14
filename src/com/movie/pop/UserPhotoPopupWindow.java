package com.movie.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.movie.R;
import com.movie.app.BasePopupWindow;
import com.movie.ui.DynamicCreateActivity;
import com.movie.util.PhotoUtils;

public class UserPhotoPopupWindow extends BasePopupWindow implements OnClickListener {


	private Button btnCamera;
	private Button btnPhoto;
	private Button btnCancel;
	private String takeImagePath;
	

	public UserPhotoPopupWindow(Context context) {
		super(LayoutInflater.from(context).inflate(R.layout.item_user_photo_popupwindows, null));
		setAnimationStyle(R.style.photo_Popup_Animation_Alpha);
	}
	public UserPhotoPopupWindow(Context context, int width, int height) {
		super(LayoutInflater.from(context).inflate(R.layout.item_user_photo_popupwindows, null), width, height);
		setAnimationStyle(R.style.photo_Popup_Animation_Alpha);
		this.mContext=context;
	}

	@Override
	public void initViews() {
		btnCamera = (Button)findViewById(R.id.item_popupwindows_camera);
		btnPhoto = (Button)findViewById(R.id.item_popupwindows_Photo);
		btnCancel = (Button)findViewById(R.id.item_popupwindows_cancel);
	}

	@Override
	public void initEvents() {
		btnCamera.setOnClickListener(this);
		btnPhoto.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	@Override
	public void init() {
		this.setBackgroundDrawable(new ColorDrawable(0));
		this.setFocusable(true);
		this.setOutsideTouchable(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item_popupwindows_camera:
			takeImagePath=PhotoUtils.takePicture((Activity)mContext);
			break;

		case R.id.item_popupwindows_Photo:
			PhotoUtils.selectPhoto((Activity)mContext);
			break;
		case R.id.item_popupwindows_cancel:
			
			break;
		}
		dismiss();
	}
	public String getTakeImagePath() {
		return takeImagePath;
	}
	

	
}
