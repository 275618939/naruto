package com.movie.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.movie.R;
import com.movie.client.bean.User;
import com.movie.util.PhotoUtils;

public class ChatMessageActivity extends BaseMessageActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	public void onBackPressed() {

		finish();

	}

	@Override
	protected void onDestroy() {
		PhotoUtils.deleteImageFile();
		super.onDestroy();
	}

	@Override
	protected void initViews() {

		titleView = (TextView) findViewById(R.id.title);
	

	}

	@Override
	protected void initEvents() {

	}

	private void init() {
		mUser = (User) getIntent().getSerializableExtra("user");
		titleView.setText("与" + mUser.getNickname() + "对话");

	}

	@Override
	public void doAction(int whichScreen) {
		switch (whichScreen) {
		case 0:

			break;

		case 1:

			hideKeyBoard();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		return false;
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (TextUtils.isEmpty(s)) {

		} else {

		}
	}

	@Override
	public void onVoiceModeClick() {

	}

	@Override
	public void onCreateClick() {

	}

	@Override
	public void onSynchronousClick() {
		mSynchronousDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	}

	public void refreshAdapter() {
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}
}
