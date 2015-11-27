package com.movie.view;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.movie.R;
import com.movie.client.service.CallBackService;
public class LoadView {

	
	public static final int LOAD_SUCESS=0x001;
	public static final int LOAD_FAIL=0x002;
	public static final int LOAD_LINEFAIL=0x003;
	public static final int LOAD_AFTER=0x004;
	View rootView;
	LinearLayout loadingLayout;
	LinearLayout loadingErrorLayout; 
	LinearLayout loadAfterLayout; 
	LinearLayout loadingLineFail;

	public LoadView() {}
	
	public LoadView(View rootView) {
		this.rootView = rootView;
		initView();
	}
	public void initView(View rootView){
		this.rootView = rootView;
		initView();
	}
	public void initView(){

		loadingLayout=(LinearLayout)rootView.findViewById(R.id.loading);
		loadingErrorLayout=(LinearLayout)rootView.findViewById(R.id.loading_error);
		loadAfterLayout=(LinearLayout)rootView.findViewById(R.id.load_after);
		loadingLineFail=(LinearLayout)rootView.findViewById(R.id.loading_line_fail);
	}
	public void showLoading(CallBackService service) {
		loadingLayout.setVisibility(View.VISIBLE);
		loadingErrorLayout.setVisibility(View.GONE);
		loadingLineFail.setVisibility(View.GONE);
	}
	public void showLoadFail(CallBackService service,OnClickListener listener) {
		loadingErrorLayout.setVisibility(View.VISIBLE);
		loadingErrorLayout.setOnClickListener(listener);
		loadingLayout.setVisibility(View.GONE);
		loadingLineFail.setVisibility(View.GONE);
	}
	public void showLoadAfter(CallBackService service) {
		loadAfterLayout.setVisibility(View.VISIBLE);
		loadingErrorLayout.setVisibility(View.GONE);
		loadingLayout.setVisibility(View.GONE);
		loadingLineFail.setVisibility(View.GONE);
	}
	public void showLoadLineFail(CallBackService service) {
		loadingLineFail.setVisibility(View.VISIBLE);
		loadAfterLayout.setVisibility(View.GONE);
		loadingErrorLayout.setVisibility(View.GONE);
		loadingLayout.setVisibility(View.GONE);
	}
	public void hideAllHit(CallBackService service) {
		loadingLineFail.setVisibility(View.GONE);
		loadingErrorLayout.setVisibility(View.GONE);
		loadingLayout.setVisibility(View.GONE);
	}
	
	

}
