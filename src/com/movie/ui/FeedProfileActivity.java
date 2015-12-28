package com.movie.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.adapter.FeedProfileCommentsAdapter;
import com.movie.adapter.SimpleListDialogAdapter;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.app.Constant.Page;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.Feed;
import com.movie.client.bean.FeedComment;
import com.movie.client.bean.NearPeople;
import com.movie.client.bean.User;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpDynamicCommentCreateService;
import com.movie.network.HttpDynamicCommentQueryService;
import com.movie.pop.OtherFeedListPopupWindow;
import com.movie.pop.OtherFeedListPopupWindow.onOtherFeedListPopupItemClickListner;
import com.movie.pop.SimpleListDialog;
import com.movie.pop.SimpleListDialog.onSimpleListItemClickListener;
import com.movie.util.JsonResolveUtils;
import com.movie.view.EmoteInputView;
import com.movie.view.EmoticonsEditText;
import com.movie.view.EmoticonsTextView;
import com.movie.view.HandyTextView;
import com.movie.view.HeaderLayout.onRightImageButtonClickListener;


public class FeedProfileActivity extends BaseActivity implements
		OnItemClickListener, onRightImageButtonClickListener,
		onOtherFeedListPopupItemClickListner, OnClickListener, OnTouchListener,CallBackService{


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
	private LinearLayout mLayoutComment;
	private TextView mTvCommentCount;
	private TextView mTvDistance;
	private RelativeLayout mLayoutLoading;
	private TextView mTvLoading;
	private ImageView mIvLoading;
	private EmoteInputView mInputView;
	private HorizontalScrollView imagesView;
    private LinearLayout contentImages;
	private FeedProfileCommentsAdapter mAdapter;
	private User user;
	private NearPeople people;
	private Feed mFeed;
	private OtherFeedListPopupWindow mPopupWindow;
	private int mWidthAndHeight;
	private int mHeaderHeight;
	private SimpleListDialog mDialog;
	private Animation mLoadingAnimation;
	private BaseService httpCommentCreateService;
	private BaseService httpCommentQueryService;
	private int page;
	private List<FeedComment> mComments = new ArrayList<FeedComment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedprofile);
		httpCommentCreateService = new HttpDynamicCommentCreateService(this);
		httpCommentQueryService = new HttpDynamicCommentQueryService(this);
		initViews();
		initEvents();
		initData();
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
		mHeaderView = LayoutInflater.from(FeedProfileActivity.this).inflate(R.layout.header_feed, null);
		mIvAvatar = (ImageView) mHeaderView.findViewById(R.id.header_feed_iv_avatar);
		mTvTime = (TextView) mHeaderView.findViewById(R.id.header_feed_tv_time);
		mEtvName = (EmoticonsTextView) mHeaderView.findViewById(R.id.header_feed_etv_name);
		mLayoutGender = (LinearLayout) mHeaderView.findViewById(R.id.header_feed_layout_gender);
		mIvGender = (ImageView) mHeaderView.findViewById(R.id.header_feed_iv_gender);
		mHtvAge = (HandyTextView) mHeaderView.findViewById(R.id.header_feed_htv_age);
		mEtvContent = (EmoticonsTextView) mHeaderView.findViewById(R.id.header_feed_etv_content);
		imagesView= (HorizontalScrollView) mHeaderView.findViewById(R.id.feed_item_image_views);
		contentImages = (LinearLayout) mHeaderView.findViewById(R.id.feed_item_content_images);
		mLayoutComment = (LinearLayout) mHeaderView.findViewById(R.id.header_feed_layout_comment);
		mTvCommentCount = (TextView) mHeaderView.findViewById(R.id.header_feed_tv_commentcount);
		mTvDistance = (TextView) mHeaderView.findViewById(R.id.header_feed_tv_distance);
		mLayoutLoading = (RelativeLayout) mHeaderView.findViewById(R.id.header_feed_layout_loading);
		mTvLoading = (TextView) mHeaderView.findViewById(R.id.header_feed_tv_loading);
		mIvLoading = (ImageView) mHeaderView.findViewById(R.id.header_feed_iv_loading);
	}

	@Override
	protected void initEvents() {
		mLvList.setOnItemClickListener(this);
		mLayoutComment.setOnClickListener(this);
		mEetEditer.setOnTouchListener(this);
		mBtnSend.setOnClickListener(this);
		mIvEmote.setOnClickListener(this);
	}
	@Override
	protected void initData() {
		user = (User) getIntent().getSerializableExtra("user");
		mFeed = getIntent().getParcelableExtra("entity_feed");
		initHeaderView();
		initPopupWindow();
		mInputView.setEditText(mEetEditer);
		mLvList.addHeaderView(mHeaderView);
		mAdapter = new FeedProfileCommentsAdapter(FeedProfileActivity.this,mHandler,mComments);
		mLvList.setAdapter(mAdapter);
		loadComment();
	}
	private void loadComment(){
		
		httpCommentQueryService.addParams("dynamicId", mFeed.getDynamicId());
		httpCommentQueryService.addParams("page", page);
		httpCommentQueryService.addParams("size", Page.DEFAULT_SIZE);
		httpCommentQueryService.execute(this);
		startLoading();
	}
	private void createComment(String content){
		
		httpCommentCreateService.addParams("dynamicId", mFeed.getDynamicId());
		httpCommentCreateService.addParams("content", content);
		httpCommentCreateService.execute(this);
	}

	private void initPopupWindow() {
		mWidthAndHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
		mHeaderHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
		mPopupWindow = new OtherFeedListPopupWindow(this, mWidthAndHeight,mWidthAndHeight);
		mPopupWindow.setOnOtherFeedListPopupItemClickListner(this);
	}

	private void initHeaderView() {
		people = new NearPeople();
		/*临时人员信息*/
		people.setGender(0);
		people.setAge(18);
		if(user!=null){
			imageLoader.displayImage(user.getPortrait(), mIvAvatar,NarutoApplication.imageOptions);
			mEtvName.setText(user.getNickname());
		}
		if(mFeed.getPortrait()!=null&&!mFeed.getPortrait().isEmpty()){
			imageLoader.displayImage(mFeed.getPortrait(), mIvAvatar,NarutoApplication.imageOptions);
		}
		if(mFeed.getPortrait()!=null&&!mFeed.getPortrait().isEmpty()){
			mEtvName.setText(mFeed.getName());
		}
		mTvTime.setText(mFeed.getTime());
		mLayoutGender.setBackgroundResource(people.getGenderBgId());
		mIvGender.setImageResource(people.getGenderId());
		mHtvAge.setText(people.getAge() + "");		
		mEtvContent.setText(mFeed.getContent());
		imagesView.setVisibility(View.GONE);
		if (mFeed.getContentImage() != null) {
			imagesView.setVisibility(View.VISIBLE);
			contentImages.removeAllViews();
			LinearLayout dynamicLayout=null;
			ImageView dynamicImageView=null;
			for(String image:mFeed.getContentImage()){
				dynamicLayout=(LinearLayout)getLayoutInflater().inflate(R.layout.dynamic_content_image, null);
				dynamicImageView= (ImageView)dynamicLayout.getChildAt(0);
				imageLoader.displayImage(Constant.SERVER_ADRESS+image, dynamicImageView);
				contentImages.addView(dynamicLayout);
			}
			dynamicLayout=null;
			dynamicImageView=null;
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
			String[] codes = new String[] { "复制文本", "举报" };
			mDialog = new SimpleListDialog(this);
			mDialog.setTitle("提示");
			mDialog.setTitleLineVisibility(View.GONE);
			mDialog.setAdapter(new SimpleListDialogAdapter(this, codes));
			mDialog.setOnSimpleListItemClickListener(new OnReplyDialogItemClickListener(comment));
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
				createComment(content);
				//FeedComment comment = new FeedComment("测试用户","nearby_people_other", content, DateUtils.formatDate(FeedProfileActivity.this,System.currentTimeMillis()));
				//mComments.add(0, comment);
				//mAdapter.notifyDataSetChanged();
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
		String[] codes = getResources().getStringArray(R.array.reportfeed_items);
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
//				mEtvEditerTitle.setVisibility(View.VISIBLE);
//				mEtvEditerTitle.setText("回复" + mComment.getName() + " :");
//				mEetEditer.requestFocus();
//				showKeyBoard();
//				break;
//				
				String text = mComment.getContent();
				copy(text);
				break;

			case 1:
				report();
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
	@Override
	public void SuccessCallBack(Map<String, Object> map) {
	
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag = map.get(Constant.ReturnCode.RETURN_TAG).toString();
			if (tag.equals(httpCommentQueryService.TAG)) {
				mComments.clear();
				List<HashMap<String, Object>> datas = (ArrayList<HashMap<String, Object>>) map.get(Constant.ReturnCode.RETURN_VALUE);
				HashMap<String, Object> maps = null;
				int size = datas.size();
				FeedComment comment=null;
				for (int i = 0; i < size; i++) {
					maps = datas.get(i);
					comment = new FeedComment();
					comment.setCommentId(maps.get("id").toString());
					comment.setMemberId(maps.get("memberId").toString());
					comment.setPortrait(Constant.SERVER_ADRESS+maps.get("portrait").toString());
					comment.setName(maps.get("nickname").toString());
					comment.setContent(maps.get("content").toString());
					comment.setTime(maps.get("time").toString());
					mComments.add(comment);
				}
				refreshCommentTitle();
				mAdapter.notifyDataSetChanged();
			}else if(tag.equals(httpCommentCreateService.TAG)){
				loadComment();
			}
			
		}else {
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
			mLayoutLoading.setVisibility(View.GONE);
			mIvLoading.clearAnimation();
		}
	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {

		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();		
		showToask(message);
		mLayoutLoading.setVisibility(View.GONE);
		mIvLoading.clearAnimation();
		
	}
	@Override
	public void onBackPressed() {
		if (mInputView.isShown()) {
			mInputView.setVisibility(View.GONE);
		} else {
			
		}
		finish();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mComments.clear();
		httpCommentCreateService=null;
		httpCommentQueryService=null;
		//mAdapter=null;
	}
	

	@Override
	public void OnRequest() {
		
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
