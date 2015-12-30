package com.movie.ui.message;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.ChatAdapter;
import com.movie.client.bean.Message;
import com.movie.client.bean.Message.CONTENT_TYPE;
import com.movie.client.bean.Message.MESSAGE_TYPE;
import com.movie.client.bean.User;
import com.movie.pop.SimpleListDialog;
import com.movie.util.FileUtils;
import com.movie.util.PhotoUtils;
import com.movie.view.ChatListView;
import com.movie.view.EmoteInputView;
import com.movie.view.EmoticonsEditText;

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
		String[] modes = getResources().getStringArray(R.array.chat_audio_type);
		mDialog = new SimpleListDialog(this);
		mDialog.setTitle("语音收听方式");
		mDialog.setTitleLineVisibility(View.GONE);
		//mDialog.setAdapter(new CheckListDialogAdapter(mCheckId, this, modes));
		mDialog.setOnSimpleListItemClickListener(new OnVoiceModeDialogItemClickListener());
		mDialog.show();
	}

	@Override
	public void onCreateClick() {

	}

	@Override
	public void onSynchronousClick() {
		mSynchronousDialog.show();
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
}
