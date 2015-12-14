package com.movie.pop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.movie.R;
import com.movie.app.BasePopupWindow;
import com.movie.view.HandyTextView;

public class PhotoDeletePopupWindow extends BasePopupWindow implements
		OnClickListener {

	private HandyTextView dialogDelete;
	private onPhotoDeleteItemClickListner mOnPhotoDeleteItemClickListner;
	public PhotoDeletePopupWindow(Context context, int width, int height) {
		super(LayoutInflater.from(context).inflate(R.layout.include_dialog_delete_photo, null), width, height);
		setAnimationStyle(R.style.Popup_Animation_Alpha);
	}

	@Override
	public void initViews() {
		dialogDelete = (HandyTextView) findViewById(R.id.dialog_delete);
	}

	@Override
	public void initEvents() {
		dialogDelete.setOnClickListener(this);
	}

	@Override
	public void init() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_delete:
			if (mOnPhotoDeleteItemClickListner != null) {
				mOnPhotoDeleteItemClickListner.onDelete(v);
			}
			break;
		}
		dismiss();
	}

	public void setOnPhotoItemClickListner(onPhotoDeleteItemClickListner l) {
		mOnPhotoDeleteItemClickListner = l;
	}
	public interface onPhotoDeleteItemClickListner {
		void onDelete(View v);
	}
}
