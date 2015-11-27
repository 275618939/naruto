package com.movie.pop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.movie.R;
import com.movie.adapter.SignInAdapter;
import com.movie.app.BasePopupWindow;
import com.movie.view.CommentsGridView;


public class SignInPopupWindow extends BasePopupWindow {

	List<Map<Integer,Integer>> signInList=new ArrayList<Map<Integer,Integer>>();
	CommentsGridView signInGridView;
	SignInAdapter signInAdapter;
	ImageView btnPopClose;
	Context context;
	Handler mHandler;

	public SignInPopupWindow(Context context,Handler handler) {
		super(LayoutInflater.from(context).inflate(R.layout.sign_in_pop, null),LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setAnimationStyle(R.style.Popup_Animation_PushDownUp);
		this.context=context;
		this.mHandler=handler;
		signInAdapter = new SignInAdapter(this.context,this.mHandler,signInList);
		signInGridView.setAdapter(signInAdapter);
	}
	
	@Override
	public void initViews() {
		signInGridView = (CommentsGridView)findViewById(R.id.comments);
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
		
	}
	public void updateData(List<Map<Integer,Integer>> signInList){
		signInAdapter.updateData(signInList);
	}

}
