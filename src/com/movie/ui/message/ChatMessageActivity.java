package com.movie.ui.message;

import java.io.File;
import java.util.List;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMError;
import com.easemob.chatuidemo.activity.ChatActivity;
import com.easemob.chatuidemo.adapter.VoicePlayClickListener;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.client.bean.User;
import com.movie.util.FileUtils;
import com.movie.util.PhotoUtils;
import com.movie.view.EmoteInputView;
import com.movie.view.EmoticonsEditText;

public class ChatMessageActivity extends BaseActivity implements OnClickListener,OnTouchListener {

	private User mUser;
	private TextView titleView;
	private View recordingContainer;
	private ImageView micImage;
	private TextView recordingHint;
	private ListView listView;
	private EmoteInputView mInputView;
	private EmoticonsEditText mEditTextContent;
	private View buttonSetModeKeyboard;
	private View buttonSetModeVoice;
	private View buttonSend;
	private View buttonPressToSpeak;
	private LinearLayout btnContainer;
	private ImageView locationImgview;
	private View more;
	private int position;
	private ClipboardManager clipboard;
	private PowerManager.WakeLock wakeLock;
	private List<String> reslist;
	private Drawable[] micImages;
	private int chatType;
	public static ChatActivity activityInstance = null;
	// 给谁发送消息
	private String toChatUsername;
	private File cameraFile;
	static int resendPos;
	private ImageView iv_emoticons_normal;
	private ImageView iv_emoticons_checked;
	private RelativeLayout edittext_layout;
	private ImageView voiceCallBtn;
    private ImageView videoCallBtn;
	private ProgressBar loadmorePB;
	private boolean isloading;
	private final int pagesize = 20;
	private boolean haveMoreData = true;
	private Button btnMore;
	public String playMsgId;
	private SwipeRefreshLayout swipeRefreshLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_message);
		initViews();
		initEvents();
		initData();
		setUpView();
	}
	@Override
	protected void initViews() {

		titleView = (TextView) findViewById(R.id.title);
		recordingContainer = findViewById(R.id.recording_container);
		micImage = (ImageView) findViewById(R.id.mic_image);
		recordingHint = (TextView) findViewById(R.id.recording_hint);
		listView = (ListView) findViewById(R.id.list);
		mEditTextContent = (EmoticonsEditText) findViewById(R.id.et_sendmessage);
		mInputView = (EmoteInputView) findViewById(R.id.feedprofile_eiv_input);
		buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
		buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
		buttonSend = findViewById(R.id.btn_send);
		buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
		btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
		locationImgview = (ImageView) findViewById(R.id.btn_location);
		iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
		iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
		loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
		btnMore = (Button) findViewById(R.id.btn_more);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		more = findViewById(R.id.more);
		edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
		voiceCallBtn = (ImageView) findViewById(R.id.btn_voice_call);
		videoCallBtn = (ImageView) findViewById(R.id.btn_video_call);

	}

	@Override
	protected void initEvents() {
		buttonSetModeVoice.setOnClickListener(this);
		buttonSetModeKeyboard.setOnClickListener(this);
		mEditTextContent.setOnClickListener(this);
		btnMore.setOnClickListener(this);
		buttonSend.setOnClickListener(this);
		buttonPressToSpeak.setOnTouchListener(this);
		mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
				} else {
					edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
				}
			}
		});
		mEditTextContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
				more.setVisibility(View.GONE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				btnContainer.setVisibility(View.GONE);
			}
		});
	}
	@Override
	protected void initData() {
		mUser = (User) getIntent().getSerializableExtra("user");
		titleView.setText("与" + mUser.getNickname() + "对话");
		mInputView.setEditText(mEditTextContent);
	}
	private void setUpView() {
		iv_emoticons_normal.setOnClickListener(this);
		iv_emoticons_checked.setOnClickListener(this);
		// position = getIntent().getIntExtra("position", -1);
		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
	} 


	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case  R.id.iv_emoticons_normal:
			 // 点击显示表情框
			more.setVisibility(View.VISIBLE);
			iv_emoticons_normal.setVisibility(View.INVISIBLE);
			iv_emoticons_checked.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.GONE);
			mEditTextContent.requestFocus();
			hideKeyboard();
			mInputView.setVisibility(View.VISIBLE);
			break;
		case  R.id.iv_emoticons_checked:
			 // 点击隐藏表情框
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			more.setVisibility(View.GONE);
			mEditTextContent.requestFocus();
			showKeyBoard();
			break;
		case R.id.btn_set_mode_voice:
			setModeVoice(v);
			break;
		case R.id.btn_set_mode_keyboard:
			setModeKeyboard(v);
			break;
		case R.id.et_sendmessage:
			editClick(v);
			break;
		case R.id.btn_more:
			toggleMore(v);
			break;
		case R.id.btn_send:
			String s = mEditTextContent.getText().toString();
			//发送消息
			break;
	
		default:
			break;
		}
	}
	private void showKeyBoard() {
		if (mInputView.isShown()) {
			mInputView.setVisibility(View.GONE);
		}
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.showSoftInput(mEditTextContent, 0);
	}

	private void hideKeyBoard() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(ChatMessageActivity.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
	}
	/**
	 * 显示语音图标按钮
	 * 
	 * @param view
	 */
	public void setModeVoice(View view) {
		hideKeyboard();
		edittext_layout.setVisibility(View.GONE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeKeyboard.setVisibility(View.VISIBLE);
		buttonSend.setVisibility(View.GONE);
		btnMore.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.VISIBLE);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		btnContainer.setVisibility(View.VISIBLE);

	}
	/**
	 * 显示键盘图标
	 * 
	 * @param view
	 */
	public void setModeKeyboard(View view) {
		edittext_layout.setVisibility(View.VISIBLE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeVoice.setVisibility(View.VISIBLE);
		mEditTextContent.requestFocus();
		buttonPressToSpeak.setVisibility(View.GONE);
		if (TextUtils.isEmpty(mEditTextContent.getText())) {
			btnMore.setVisibility(View.VISIBLE);
			buttonSend.setVisibility(View.GONE);
		} else {
			btnMore.setVisibility(View.GONE);
			buttonSend.setVisibility(View.VISIBLE);
		}

	}
	/**
	 * 点击文字输入框
	 * 
	 * @param v
	 */
	public void editClick(View v) {
		listView.setSelection(listView.getCount() - 1);
		if (more.getVisibility() == View.VISIBLE) {
			more.setVisibility(View.GONE);
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
		}

	}
	/**
	 * 显示或隐藏图标按钮页
	 * 
	 * @param view
	 */
	public void toggleMore(View view) {
		if (more.getVisibility() == View.GONE) {
			hideKeyboard();
			more.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
		
		} else {			
			more.setVisibility(View.GONE);

		}

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
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!FileUtils.isSdcardExist()) {
				showToask("SD卡不可用,请检查");
				return false;			
			}
			try {
				v.setPressed(true);
				wakeLock.acquire();
				if (VoicePlayClickListener.isPlaying)
					VoicePlayClickListener.currentPlayListener.stopPlayVoice();
				recordingContainer.setVisibility(View.VISIBLE);
				recordingHint.setText(getString(R.string.move_up_to_cancel));
				recordingHint.setBackgroundColor(Color.TRANSPARENT);
				voiceRecorder.startRecording(null, toChatUsername, getApplicationContext());
			} catch (Exception e) {
				e.printStackTrace();
				v.setPressed(false);
				if (wakeLock.isHeld())
					wakeLock.release();
				if (voiceRecorder != null)
					voiceRecorder.discardRecording();
				recordingContainer.setVisibility(View.INVISIBLE);
				Toast.makeText(ChatActivity.this, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
				return false;
			}

			return true;
		case MotionEvent.ACTION_MOVE: {
			if (event.getY() < 0) {
				recordingHint.setText(getString(R.string.release_to_cancel));
				recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
			} else {
				recordingHint.setText(getString(R.string.move_up_to_cancel));
				recordingHint.setBackgroundColor(Color.TRANSPARENT);
			}
			return true;
		}
		case MotionEvent.ACTION_UP:
			v.setPressed(false);
			recordingContainer.setVisibility(View.INVISIBLE);
			if (wakeLock.isHeld())
				wakeLock.release();
			if (event.getY() < 0) {
				// discard the recorded audio.
				voiceRecorder.discardRecording();

			} else {
				// stop recording and send voice file
				String st1 = getResources().getString(R.string.Recording_without_permission);
				String st2 = getResources().getString(R.string.The_recording_time_is_too_short);
				String st3 = getResources().getString(R.string.send_failure_please);
				try {
					int length = voiceRecorder.stopRecoding();
					if (length > 0) {
						sendVoice(voiceRecorder.getVoiceFilePath(), voiceRecorder.getVoiceFileName(toChatUsername),
								Integer.toString(length), false);
					} else if (length == EMError.INVALID_FILE) {
						Toast.makeText(getApplicationContext(), st1, Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), st2, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(ChatActivity.this, st3, Toast.LENGTH_SHORT).show();
				}

			}
			return true;
		default:
			recordingContainer.setVisibility(View.INVISIBLE);
			if (voiceRecorder != null)
				voiceRecorder.discardRecording();
			return false;
		}
	
	}



	

	





	




	
}
