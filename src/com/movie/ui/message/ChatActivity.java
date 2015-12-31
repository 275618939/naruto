package com.movie.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;

import com.movie.R;
import com.movie.util.PhotoUtils;

public class ChatActivity extends BaseMessageActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}
	@Override
	public void onBackPressed() {}

	@Override
	protected void onDestroy() {
		PhotoUtils.deleteImageFile();
		super.onDestroy();
	}

	@Override
	protected void initViews() {}

	@Override
	protected void initEvents() {}

	private void init() {}

	@Override
	public void doAction(int whichScreen) {}

	@Override
	public void onClick(View v) {}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.chat_textditor_eet_editer) {
			
			showKeyBoard();
		}
		if (v.getId() == R.id.fullscreen_mask) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				hidePlusBar();
			}
		}
		return false;
	}


	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {}

	public void refreshAdapter() {
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onVoiceModeClick() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCreateClick() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSynchronousClick() {
		// TODO Auto-generated method stub
		
	}
}
