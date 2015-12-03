package com.movie.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.FeedProfileCommentsAdapter;
import com.movie.adapter.SimpleListDialogAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.Feed;
import com.movie.client.bean.FeedComment;
import com.movie.client.bean.NearPeople;
import com.movie.pop.OtherFeedListPopupWindow;
import com.movie.pop.OtherFeedListPopupWindow.onOtherFeedListPopupItemClickListner;
import com.movie.pop.SimpleListDialog;
import com.movie.pop.SimpleListDialog.onSimpleListItemClickListener;
import com.movie.util.DateUtils;
import com.movie.util.JsonResolveUtils;
import com.movie.view.EmoteInputView;
import com.movie.view.EmoticonsEditText;
import com.movie.view.EmoticonsTextView;
import com.movie.view.HandyTextView;
import com.movie.view.HeaderLayout.onRightImageButtonClickListener;


public class FeedProfileActivity extends BaseActivity implements
		OnItemClickListener, onRightImageButtonClickListener,
		onOtherFeedListPopupItemClickListner, OnClickListener, OnTouchListener {


	private ListView mLvList;
	private EmoticonsTextView mEtvEditerTitle;
	private ImageView mIvEmote;
	private Button mBtnSend;
	private EmoticonsEditText mEetEditer;
	private TextView title;
	private View mHeaderView;
	private ImageView mIvAvatar;
	private TextView mTvTime;
	private EmoticonsTextView mEtvName;
	private LinearLayout mLayoutGender;
	private ImageView mIvGender;
	private HandyTextView mHtvAge;
	private EmoticonsTextView mEtvContent;
	private ImageView mIvContent;
	private LinearLayout mLayoutComment;
	private TextView mTvCommentCount;
	private TextView mTvDistance;
	private RelativeLayout mLayoutLoading;
	private TextView mTvLoading;
	private ImageView mIvLoading;
	private EmoteInputView mInputView;

	private FeedProfileCommentsAdapter mAdapter;

	private NearPeople people;
	private Feed mFeed;
	private OtherFeedListPopupWindow mPopupWindow;
	private int mWidthAndHeight;
	private int mHeaderHeight;
	private SimpleListDialog mDialog;
	private Animation mLoadingAnimation;

	private List<FeedComment> mComments = new ArrayList<FeedComment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedprofile);
		initViews();
		initEvents();
		init();
	}

	@Override
	public void onBackPressed() {
		if (mInputView.isShown()) {
			mInputView.setVisibility(View.GONE);
		} else {
			finish();
		}
	}
	

	@Override
	protected void initViews() {
		title = (TextView) findViewById(R.id.title);
		title.setText("留言内容");
		mLvList = (ListView) findViewById(R.id.feedprofile_lv_list);
		mEtvEditerTitle = (EmoticonsTextView) findViewById(R.id.feedprofile_etv_editertitle);
		mIvEmote = (ImageView) findViewById(R.id.feedprofile_iv_emote);
		mBtnSend = (Button) findViewById(R.id.feedprofile_btn_send);
		mEetEditer = (EmoticonsEditText) findViewById(R.id.feedprofile_eet_editer);
		mInputView = (EmoteInputView) findViewById(R.id.feedprofile_eiv_input);
	
	}

	@Override
	protected void initEvents() {
		mLvList.setOnItemClickListener(this);
		mLayoutComment.setOnClickListener(this);
		mEetEditer.setOnTouchListener(this);
		mBtnSend.setOnClickListener(this);
		mIvEmote.setOnClickListener(this);
	}

	private void init() {
		mFeed = getIntent().getParcelableExtra("entity_feed");
		initPopupWindow();
		initHeaderView();
		mInputView.setEditText(mEetEditer);
		mLvList.addHeaderView(mHeaderView);
		mAdapter = new FeedProfileCommentsAdapter(FeedProfileActivity.this,mHandler,mComments);
		mLvList.setAdapter(mAdapter);
		getComments();
	}
	@Override
	protected void initData() {
	
		
	}

	private void initPopupWindow() {
		mWidthAndHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
		mHeaderHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
		mPopupWindow = new OtherFeedListPopupWindow(this, mWidthAndHeight,mWidthAndHeight);
		mPopupWindow.setOnOtherFeedListPopupItemClickListner(this);
	}

	private void initHeaderView() {
		mHeaderView = LayoutInflater.from(FeedProfileActivity.this).inflate(R.layout.header_feed, null);
		imageLoader.displayImage(mFeed.getPortrait(), mIvAvatar,NarutoApplication.imageOptions);
		mEtvName.setText(mFeed.getName());
		mTvTime.setText(mFeed.getTime());
		mLayoutGender.setBackgroundResource(people.getGenderBgId());
		mIvGender.setImageResource(people.getGenderId());
		mHtvAge.setText(people.getAge() + "");		
		mEtvContent.setText(mFeed.getContent());
		if (mFeed.getContentImage() == null) {
			mIvContent.setVisibility(View.GONE);
		} else {
			mIvContent.setVisibility(View.VISIBLE);
			//mIvContent.setImageBitmap(mApplication.getStatusPhoto(mFeed.getContentImage()));
		}
		mTvDistance.setText(mFeed.getSite());
		mTvCommentCount.setText(mFeed.getCommentCount() + "");
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
	public void onClick() {
		mPopupWindow.showAtLocation(title, Gravity.RIGHT | Gravity.TOP,-10, mHeaderHeight + 10);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			FeedComment comment = mComments.get((int) arg3);
			String[] codes = new String[] { "回复", "复制文本", "举报" };
			mDialog = new SimpleListDialog(this);
			mDialog.setTitle("提示");
			mDialog.setTitleLineVisibility(View.GONE);
			mDialog.setAdapter(new SimpleListDialogAdapter(this, codes));
			mDialog.setOnSimpleListItemClickListener(new OnReplyDialogItemClickListener(
					comment));
			mDialog.show();
		}
	}

	@Override
	public void onCopy(View v) {
		String text = mFeed.getContent();
		copy(text);
	}

	@Override
	public void onReport(View v) {
		report();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_feed_layout_comment:
			mEtvEditerTitle.setText(null);
			mEtvEditerTitle.setVisibility(View.GONE);
			mEetEditer.requestFocus();
			showKeyBoard();
			break;

		case R.id.feedprofile_iv_emote:
			mEetEditer.requestFocus();
			if (mInputView.isShown()) {
				showKeyBoard();
			} else {
				hideKeyBoard();
				mInputView.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.feedprofile_btn_send:
			String content = mEetEditer.getText().toString().trim();
			if (TextUtils.isEmpty(content)) {
				showToask("请输入评论内容");
				mEetEditer.requestFocus();
			} else {
				String reply = null;
				if (mEtvEditerTitle.isShown()) {
					reply = mEtvEditerTitle.getText().toString().trim();
				}
				content = TextUtils.isEmpty(reply) ? content : reply + content;
				FeedComment comment = new FeedComment("测试用户","nearby_people_other", content, DateUtils.formatDate(FeedProfileActivity.this,System.currentTimeMillis()));
				mComments.add(0, comment);
				mAdapter.notifyDataSetChanged();
			}
			mEtvEditerTitle.setText(null);
			mEtvEditerTitle.setVisibility(View.GONE);
			mEetEditer.setText(null);
			mInputView.setVisibility(View.GONE);
			hideKeyBoard();
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (v.getId() == R.id.feedprofile_eet_editer) {
			showKeyBoard();
		}
		return false;
	}

	private void copy(String text) {
		ClipboardManager m = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		m.setPrimaryClip(ClipData.newPlainText(null, text));
		showToask("已成功复制文本");
		mEetEditer.requestFocus();
	}

	private void report() {
		String[] codes = getResources()
				.getStringArray(R.array.reportfeed_items);
		mDialog = new SimpleListDialog(this);
		mDialog.setTitle("举报留言");
		mDialog.setTitleLineVisibility(View.GONE);
		mDialog.setAdapter(new SimpleListDialogAdapter(this, codes));
		mDialog.setOnSimpleListItemClickListener(new OnReportDialogItemClickListener());
		mDialog.show();
	}

	private class OnReportDialogItemClickListener implements
			onSimpleListItemClickListener {

		@Override
		public void onItemClick(int position) {
			showProgressDialog("提示","正在提交,请稍后...");
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					hideProgressDialog();
					showToask("举报的信息已提交");
				}
			}, 1500);
		}
	}

	private class OnReplyDialogItemClickListener implements
			onSimpleListItemClickListener {

		private FeedComment mComment;

		public OnReplyDialogItemClickListener(FeedComment comment) {
			mComment = comment;
		}

		@Override
		public void onItemClick(int position) {
			switch (position) {
			case 0:
				mEtvEditerTitle.setVisibility(View.VISIBLE);
				mEtvEditerTitle.setText("回复" + mComment.getName() + " :");
				mEetEditer.requestFocus();
				showKeyBoard();
				break;

			case 1:
				String text = mComment.getContent();
				copy(text);
				break;

			case 2:
				report();
				break;
			}
		}

	}

	private void startLoading() {
		if (!mLayoutLoading.isShown()) {
			mLayoutLoading.setVisibility(View.VISIBLE);
		}
		if (mIvLoading != null) {
			mIvLoading.setVisibility(View.VISIBLE);
			mTvLoading.setText("评论加载中");
			mLoadingAnimation = AnimationUtils.loadAnimation(FeedProfileActivity.this, R.anim.loading);
			mIvLoading.startAnimation(mLoadingAnimation);
		}
	}

	private void refreshCommentTitle() {
		if (mComments != null) {
			if (mComments.size() > 0 && !mAdapter.isEmpty()) {
				mLayoutLoading.setVisibility(View.GONE);
			} else {
				mIvLoading.clearAnimation();
				mIvLoading.setVisibility(View.GONE);
				mTvLoading.setText("暂无评论");
				mLayoutLoading.setVisibility(View.VISIBLE);
			}
		}
	}

	private void showKeyBoard() {
		if (mInputView.isShown()) {
			mInputView.setVisibility(View.GONE);
		}
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.showSoftInput(mEetEditer, 0);
	}

	private void hideKeyBoard() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(FeedProfileActivity.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void getComments() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				startLoading();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return JsonResolveUtils.resoleFeedComment(FeedProfileActivity.this, mComments);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (!result) {
					showToask("数据加载失败...");
					mLayoutLoading.setVisibility(View.GONE);
					mIvLoading.clearAnimation();
				} else {
					refreshCommentTitle();
					mAdapter.notifyDataSetChanged();
				}
			}

		});
	}

	

}
