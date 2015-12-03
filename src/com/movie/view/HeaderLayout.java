package com.movie.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

public class HeaderLayout extends LinearLayout {

	
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
}
