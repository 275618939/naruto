package com.movie.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class HeaderLayout extends LinearLayout {

	private LayoutInflater mInflater;
	private View mHeader;
	private ImageView mIvLogo;
	private LinearLayout mLayoutLeftContainer;
	private LinearLayout mLayoutMiddleContainer;
	private LinearLayout mLayoutRightContainer;

	// 标题
	private LinearLayout mLayoutTitle;
	private HandyTextView mHtvSubTitle;

	// 搜索
	private RelativeLayout mLayoutSearch;
	private EditText mEtSearch;
	private Button mBtnSearchClear;
	private ImageView mIvSearchLoading;
	private onSearchListener mOnSearchListener;

	// 右边文本
	private HandyTextView mHtvRightText;

	// 右边按钮
	private LinearLayout mLayoutRightImageButtonLayout;
	private ImageButton mIbRightImageButton;
	private onRightImageButtonClickListener mRightImageButtonClickListener;

	private LinearLayout mLayoutMiddleImageButtonLayout;
	private ImageButton mIbMiddleImageButton;
	private ImageView mIvMiddleLine;
	private onMiddleImageButtonClickListener mMiddleImageButtonClickListener;


	
	
	public HeaderLayout(Context context) {
		super(context);
	}

	public HeaderLayout(Context context, AttributeSet attrs) {
		super(context, attrs);		
	}
	public interface onMiddleImageButtonClickListener {
		void onClick();
	}

	public interface onRightImageButtonClickListener {
		void onClick();
	}

	public interface onSearchListener {
		void onSearch(EditText et);
	}
	public enum HeaderStyle {
		DEFAULT_TITLE, TITLE_RIGHT_TEXT, TITLE_RIGHT_IMAGEBUTTON, TITLE_NEARBY_PEOPLE, TITLE_NEARBY_GROUP, TITLE_CHAT;
	}

}
