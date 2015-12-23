package com.movie.pop;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.movie.R;
import com.movie.adapter.CommentsShowAdapter;
import com.movie.app.BasePopupWindow;
import com.movie.view.CommentsGridView;


public class CommentPopupWindow extends BasePopupWindow {

	CommentsGridView commentsGridView;
	CommentsShowAdapter commentsAdapter;
	ImageView btnPopClose;
	Context context;
	Handler mHandler;
	public CommentPopupWindow(Context context,Handler handler) {
		super(LayoutInflater.from(context).inflate(R.layout.comments_pop, null),LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setAnimationStyle(R.style.Popup_Animation_PushDownUp);
		this.context=context;
		this.mHandler=handler;
		commentsAdapter = new CommentsShowAdapter(this.context,this.mHandler,null);
		commentsGridView.setAdapter(commentsAdapter);
	}
	
	@Override
	public void initViews() {
		commentsGridView = (CommentsGridView)findViewById(R.id.comments);
		btnPopClose = (ImageView)findViewById(R.id.btn_pop_close);
	}
    
	@Override
	public void initEvents() {
	
		btnPopClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	@Override
	public void init() {
		this.setBackgroundDrawable(new ColorDrawable(0));
		this.setFocusable(true);
		this.setOutsideTouchable(true);
	}
	public void setCommentsList(List<Map<Integer, String>> comments) {
		commentsAdapter.updateData(comments);
	}


}
