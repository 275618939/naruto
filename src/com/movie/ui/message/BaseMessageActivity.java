package com.movie.ui.message;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.ChatAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.BaseDialog;
import com.movie.client.bean.Message;
import com.movie.client.bean.User;
import com.movie.pop.ChatPopupWindow.onChatPopupItemClickListener;
import com.movie.pop.SimpleListDialog;
import com.movie.ui.UserDetailActivity;
import com.movie.view.EmoticonsEditText;
import com.movie.view.HeaderLayout.onMiddleImageButtonClickListener;
import com.movie.view.HeaderLayout.onRightImageButtonClickListener;
import com.movie.view.ScrollLayout.OnScrollToScreenListener;

public abstract class BaseMessageActivity extends BaseActivity implements
		OnScrollToScreenListener, OnClickListener, OnTouchListener,
		TextWatcher, onChatPopupItemClickListener {

	public static final String COPY_IMAGE = "EASEMOBIMG";
	public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
	
	protected TextView titleView;
	protected EmoticonsEditText mEetTextDitorEditer;
	protected List<Message> mMessages = new ArrayList<Message>();
	protected ChatAdapter mAdapter;
	protected User mUser;
	protected SimpleListDialog mDialog;
	protected int mCheckId = 0;
	protected BaseDialog mSynchronousDialog;
	protected String mCameraImagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_message);
		initViews();
		initEvents();
	}

	protected class OnMiddleImageButtonClickListener implements
			onMiddleImageButtonClickListener {

		@Override
		public void onClick() {
			Intent intent = new Intent(BaseMessageActivity.this,UserDetailActivity.class);
			intent.putExtra("uid", mUser.getMemberId());
			intent.putExtra("name",mUser.getNickname());
			intent.putExtra("avatar",mUser.getPortrait());
			startActivity(intent);
			finish();
		}
	}

	protected class OnRightImageButtonClickListener implements
			onRightImageButtonClickListener {

		@Override
		public void onClick() {
			
		}
	}

	protected void showKeyBoard() {
		
	}

	protected void hideKeyBoard() {}

	protected void showPlusBar() {
		
	}

	protected void hidePlusBar() {
		
	}

	

	protected void initPopupWindow() {}

	protected void initSynchronousDialog() {}

	
}
