package com.movie.ui.message;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
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

import com.easemob.EMChatRoomChangeListener;
import com.easemob.EMError;
import com.easemob.applib.controller.DemoHXSDKHelper;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;
import com.hxsdk.adpter.MessageAdapter;
import com.hxsdk.adpter.VoicePlayClickListener;
import com.hxsdk.bean.RobotUser;
import com.hxsdk.utils.CommonUtils;
import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.client.bean.User;
import com.movie.util.FileUtils;
import com.movie.util.PhotoUtils;
import com.movie.view.EmoteInputView;
import com.movie.view.EmoticonsEditText;

public class ChatMessageActivity extends BaseActivity implements OnClickListener,OnTouchListener {

	private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
	public static final int REQUEST_CODE_CONTEXT_MENU = 3;
	private static final int REQUEST_CODE_MAP = 4;
	public static final int REQUEST_CODE_TEXT = 5;
	public static final int REQUEST_CODE_VOICE = 6;
	public static final int REQUEST_CODE_PICTURE = 7;
	public static final int REQUEST_CODE_LOCATION = 8;
	public static final int REQUEST_CODE_NET_DISK = 9;
	public static final int REQUEST_CODE_FILE = 10;
	public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
	public static final int REQUEST_CODE_PICK_VIDEO = 12;
	public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
	public static final int REQUEST_CODE_VIDEO = 14;
	public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
	public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
	public static final int REQUEST_CODE_SEND_USER_CARD = 17;
	public static final int REQUEST_CODE_CAMERA = 18;
	public static final int REQUEST_CODE_LOCAL = 19;
	public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
	public static final int REQUEST_CODE_GROUP_DETAIL = 21;
	public static final int REQUEST_CODE_SELECT_VIDEO = 23;
	public static final int REQUEST_CODE_SELECT_FILE = 24;
	public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

	public static final int RESULT_CODE_COPY = 1;
	public static final int RESULT_CODE_DELETE = 2;
	public static final int RESULT_CODE_FORWARD = 3;
	public static final int RESULT_CODE_OPEN = 4;
	public static final int RESULT_CODE_DWONLOAD = 5;
	public static final int RESULT_CODE_TO_CLOUD = 6;
	public static final int RESULT_CODE_EXIT_GROUP = 7;

	public static final int CHATTYPE_SINGLE = 1;
	public static final int CHATTYPE_GROUP = 2;
	public static final int CHATTYPE_CHATROOM = 3;

	
	private User mUser;
	private TextView titleView;
	private View recordingContainer;
	private ImageView micImage;
	private TextView recordingHint;
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
	private EMConversation conversation;
	private int chatType;
	public static ChatActivity activityInstance = null;
	// 给谁发送消息
	private String toChatUsername;
	private VoiceRecorder voiceRecorder;

	private File cameraFile;
	static int resendPos;
	private ListView listView;
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
	public boolean isRobot;
	private MessageAdapter adapter;
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
		 // 单聊
		//toChatUsername = getIntent().getStringExtra("userId");
		Map<String,RobotUser> robotMap=((DemoHXSDKHelper)HXSDKHelper.getInstance()).getRobotList();
		if(robotMap!=null&&robotMap.containsKey(toChatUsername)){
			isRobot = true;
			/*String nick = robotMap.get(toChatUsername).getNick();
			if(!TextUtils.isEmpty(nick)){
				((TextView) findViewById(R.id.name)).setText(nick);
			}else{
				((TextView) findViewById(R.id.name)).setText(toChatUsername);
			}*/
		}else{
			//UserUtils.setUserNick(toChatUsername, (TextView) findViewById(R.id.name));
		}
		onConversationInit();    
	    onListViewCreation();
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
			//发送消息
			String s = mEditTextContent.getText().toString();
			sendText(s);
			break;
		case R.id.btn_take_picture:
			//拍照
			selectPicFromCamera();
			break;
		case R.id.btn_picture:
			//选择图片
			selectPicFromLocal();
			break;
	
		default:
			break;
		}
	}
	/**
	 * 从图库获取图片
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}
	/**
	 * 照相获取图片
	 */
	public void selectPicFromCamera() {
		if (!CommonUtils.isExitsSdcard()) {
			String st = getResources().getString(R.string.sd_card_does_not_exist);
			Toast.makeText(getApplicationContext(), st, 0).show();
			return;
		}
		cameraFile = new File(PathUtil.getInstance().getImagePath(), mUser.getNickname()+ System.currentTimeMillis() + ".jpg");
		cameraFile.getParentFile().mkdirs();
		startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),REQUEST_CODE_CAMERA);
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
				Toast.makeText(this, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
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
					Toast.makeText(this, st3, Toast.LENGTH_SHORT).show();
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
	protected void onConversationInit(){
	    if(chatType == CHATTYPE_SINGLE){
	        conversation = EMChatManager.getInstance().getConversationByType(toChatUsername,EMConversationType.Chat);
	    }else if(chatType == CHATTYPE_GROUP){
	        conversation = EMChatManager.getInstance().getConversationByType(toChatUsername,EMConversationType.GroupChat);
	    }else if(chatType == CHATTYPE_CHATROOM){
	        conversation = EMChatManager.getInstance().getConversationByType(toChatUsername,EMConversationType.ChatRoom);
	    }
        // 把此会话的未读数置为0
        conversation.markAllMessagesAsRead();

        // 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
        // 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            if (chatType == CHATTYPE_SINGLE) {
                conversation.loadMoreMsgFromDB(msgId, pagesize);
            } else {
                conversation.loadMoreGroupMsgFromDB(msgId, pagesize);
            }
        }
        
        EMChatManager.getInstance().addChatRoomChangeListener(new EMChatRoomChangeListener(){

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if(roomId.equals(toChatUsername)){
                    finish();
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {                
            }

            @Override
            public void onMemberExited(String roomId, String roomName,
                    String participant) {
                
            }

            @Override
            public void onMemberKicked(String roomId, String roomName,
                    String participant) {
                if(roomId.equals(toChatUsername)){
                    String curUser = EMChatManager.getInstance().getCurrentUser();
                    if(curUser.equals(participant)){
                        EMChatManager.getInstance().leaveChatRoom(toChatUsername);
                        finish();
                    }
                }
            }
            
        });
	}
	
	protected void onListViewCreation(){
        adapter = new MessageAdapter(this, toChatUsername, chatType);
        // 显示消息
        listView.setAdapter(adapter);
        adapter.refreshSelectLast();
        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                btnContainer.setVisibility(View.GONE);
                return false;
            }
        });
	}
	/**
	 * 发送文本消息
	 * 
	 * @param content
	 *            message content
	 * @param isResend
	 *            boolean resend
	 */
	public void sendText(String content) {

		if (content.length() > 0) {
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			// 如果是群聊，设置chattype,默认是单聊
			if (chatType == CHATTYPE_GROUP){
			    message.setChatType(ChatType.GroupChat);
			}else if(chatType == CHATTYPE_CHATROOM){
			    message.setChatType(ChatType.ChatRoom);
			}
			if(isRobot){
				message.setAttribute("em_robot_message", true);
			}
			TextMessageBody txtBody = new TextMessageBody(content);
			// 设置消息body
			message.addBody(txtBody);
			// 设置要发给谁,用户username或者群聊groupid
			message.setReceipt(toChatUsername);
			// 把messgage加到conversation中
			conversation.addMessage(message);
			// 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
			adapter.refreshSelectLast();
			mEditTextContent.setText("");

			setResult(RESULT_OK);

		}
	}

	/**
	 * 发送语音
	 * 
	 * @param filePath
	 * @param fileName
	 * @param length
	 * @param isResend
	 */
	private void sendVoice(String filePath, String fileName, String length, boolean isResend) {
		if (!(new File(filePath).exists())) {
			return;
		}
		try {
			final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
			// 如果是群聊，设置chattype,默认是单聊
			if (chatType == CHATTYPE_GROUP){
				message.setChatType(ChatType.GroupChat);
			}else if(chatType == CHATTYPE_CHATROOM){
				message.setChatType(ChatType.ChatRoom);
			}
			message.setReceipt(toChatUsername);
			int len = Integer.parseInt(length);
			VoiceMessageBody body = new VoiceMessageBody(new File(filePath), len);
			message.addBody(body);
			if(isRobot){
				message.setAttribute("em_robot_message", true);
			}
			conversation.addMessage(message);
			adapter.refreshSelectLast();
			setResult(RESULT_OK);
			// send file
			// sendVoiceSub(filePath, fileName, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getToChatUsername() {
		return toChatUsername;
	}

	public ListView getListView() {
		return listView;
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




	

	





	




	
}
