package com.movie.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.movie.R;
import com.movie.adapter.PoiAdapter;
import com.movie.app.BasePopupWindow;

public class AddressPopupWindow extends BasePopupWindow implements OnClickListener {


	private ListView addressListView;
	
	public AddressPopupWindow(Context context) {
		super(LayoutInflater.from(context).inflate(R.layout.item_popup_cinema_windows, null));
		setAnimationStyle(R.style.photo_Popup_Animation_Alpha);
	}
	public AddressPopupWindow(Context context, int width, int height) {
		super(LayoutInflater.from(context).inflate(R.layout.item_popup_cinema_windows, null), width, height);
		setAnimationStyle(R.style.photo_Popup_Animation_Alpha);
		this.mContext=context;
	}

	@Override
	public void initViews() {
		addressListView = (ListView)findViewById(R.id.poi_listView);
	}

	@Override
	public void initEvents() {
		
	}
	@Override
	public void init() {
		this.setBackgroundDrawable(new ColorDrawable(0));
		this.setFocusable(true);
		this.setOutsideTouchable(true);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		}
		dismiss();
	}
	public void setAdapter(PoiAdapter adapter){
		addressListView.setAdapter(adapter);
	}
	
	

	
}
