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
	public void onBackPressed() {
		if (mLayoutMessagePlusBar.isShown()) {
			hidePlusBar();
		} else if (mInputView.isShown()) {
			mInputView.setVisibility(View.GONE);
		} else if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
			
			hideKeyBoard();
		}  else {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		PhotoUtils.deleteImageFile();
		super.onDestroy();
	}

	@Override
	protected void initViews() {
		
		titleView = (TextView) findViewById(R.id.title);
		mClvList = (ChatListView) findViewById(R.id.chat_clv_list);
		mInputView = (EmoteInputView) findViewById(R.id.chat_eiv_inputview);
		mEetTextDitorEditer = (EmoticonsEditText) findViewById(R.id.et_sendmessage);

	

		mLayoutFullScreenMask = (LinearLayout) findViewById(R.id.fullscreen_mask);
		mLayoutMessagePlusBar = (LinearLayout) findViewById(R.id.message_plus_layout_bar);
		mLayoutMessagePlusPicture = (LinearLayout) findViewById(R.id.message_plus_layout_picture);
		mLayoutMessagePlusCamera = (LinearLayout) findViewById(R.id.message_plus_layout_camera);
		mLayoutMessagePlusLocation = (LinearLayout) findViewById(R.id.message_plus_layout_location);
		mLayoutMessagePlusGift = (LinearLayout) findViewById(R.id.message_plus_layout_gift);

	}

	@Override
	protected void initEvents() {
		
		
		mLayoutFullScreenMask.setOnTouchListener(this);
		mLayoutMessagePlusPicture.setOnClickListener(this);
		mLayoutMessagePlusCamera.setOnClickListener(this);
		mLayoutMessagePlusLocation.setOnClickListener(this);
		mLayoutMessagePlusGift.setOnClickListener(this);

	}

	private void init() {
		mUser =(User) getIntent().getSerializableExtra("user");
		titleView.setText("与" + mUser.getNickname()+ "对话");
		mInputView.setEditText(mEetTextDitorEditer);
		initPopupWindow();
		initSynchronousDialog();
		mAdapter = new ChatAdapter(ChatActivity.this, mMessages);
		mClvList.setAdapter(mAdapter);
	}

	@Override
	public void doAction(int whichScreen) {
		switch (whichScreen) {
		case 0:
			
			break;

		case 1:
		
			if (mInputView.isShown()) {
				mInputView.setVisibility(View.GONE);
			}
			hideKeyBoard();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_textditor_ib_plus:
			if (!mLayoutMessagePlusBar.isShown()) {
				showPlusBar();
			}
			break;

		case R.id.chat_textditor_ib_emote:
			mEetTextDitorEditer.requestFocus();
			if (mInputView.isShown()) {
				hideKeyBoard();
			} else {
				hideKeyBoard();
				mInputView.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.chat_textditor_ib_keyboard:
			
			showKeyBoard();
			break;

		case R.id.chat_textditor_btn_send:
			String content = mEetTextDitorEditer.getText().toString().trim();
			if (!TextUtils.isEmpty(content)) {
				mEetTextDitorEditer.setText(null);
				mMessages.add(new Message("nearby_people_other", System.currentTimeMillis(), "0.12km", content,CONTENT_TYPE.TEXT, MESSAGE_TYPE.SEND));
				mAdapter.notifyDataSetChanged();
				mClvList.setSelection(mMessages.size());
			}
			break;

		case R.id.chat_textditor_iv_audio:
		
			break;

		case R.id.chat_audioditor_ib_plus:
			if (!mLayoutMessagePlusBar.isShown()) {
				showPlusBar();
			}
			break;

		case R.id.chat_audioditor_ib_keyboard:
		
			break;

		case R.id.message_plus_layout_picture:
			PhotoUtils.selectPhoto(ChatActivity.this);
			hidePlusBar();
			break;

		case R.id.message_plus_layout_camera:
			mCameraImagePath = PhotoUtils.takePicture(ChatActivity.this);
			hidePlusBar();
			break;

		case R.id.message_plus_layout_location:
			mMessages.add(new Message("nearby_people_other", System
					.currentTimeMillis(), "0.12km", null, CONTENT_TYPE.MAP,
					MESSAGE_TYPE.SEND));
			mAdapter.notifyDataSetChanged();
			mClvList.setSelection(mMessages.size());
			hidePlusBar();
			break;

		case R.id.message_plus_layout_gift:
			hidePlusBar();
			break;
		}
	}

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
				Cursor cursor = managedQuery(uri, proj, null, null, null);
				if (cursor != null) {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					if (cursor.getCount() > 0 && cursor.moveToFirst()) {
						String path = cursor.getString(column_index);
						Bitmap bitmap = PhotoUtils.getBitmapFromFile(path);
						if (PhotoUtils.bitmapIsLarge(bitmap)) {
							//PhotoUtils.cropPhoto(this, this, path);
						} else {
							if (path != null) {
								mMessages.add(new Message(
										"nearby_people_other", System
												.currentTimeMillis(), "0.12km",
										path, CONTENT_TYPE.IMAGE,
										MESSAGE_TYPE.SEND));
								mAdapter.notifyDataSetChanged();
								mClvList.setSelection(mMessages.size());
							}
						}
					}
				}
			}
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_CAMERA:
			if (resultCode == RESULT_OK) {
				if (mCameraImagePath != null) {
//					mCameraImagePath = PhotoUtils
//							.savePhotoToSDCard(PhotoUtils.CompressionPhoto(
//									mScreenWidth, mCameraImagePath, 2));
//					PhotoUtils.fliterPhoto(this, this, mCameraImagePath);
				}
			}
			mCameraImagePath = null;
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_CROP:
			if (resultCode == RESULT_OK) {
				String path = data.getStringExtra("path");
				if (path != null) {
					mMessages.add(new Message("nearby_people_other", System
							.currentTimeMillis(), "0.12km", path,
							CONTENT_TYPE.IMAGE, MESSAGE_TYPE.SEND));
					mAdapter.notifyDataSetChanged();
					mClvList.setSelection(mMessages.size());
				}
			}
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_FLITER:
			if (resultCode == RESULT_OK) {
				String path = data.getStringExtra("path");
				if (path != null) {
					mMessages.add(new Message("nearby_people_other", System
							.currentTimeMillis(), "0.12km", path,
							CONTENT_TYPE.IMAGE, MESSAGE_TYPE.SEND));
					mAdapter.notifyDataSetChanged();
					mClvList.setSelection(mMessages.size());
				}
			}
			break;
		}
	}

	public void refreshAdapter() {
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}
}
