package com.movie.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class CommentsGridView extends GridView {

	public CommentsGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CommentsGridView(Context context, AttributeSet attrs) {

		super(context, attrs);

	}

	public CommentsGridView(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		if (ev.getAction() == MotionEvent.ACTION_MOVE) {

			return true; // 禁止GridView滑动

		}

		return super.dispatchTouchEvent(ev);

	}

}
