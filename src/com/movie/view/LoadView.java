package com.movie.view;

import com.movie.R;
import com.movie.client.service.CallBackService;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
public class LoadView {

	
	public static final int LOAD_SUCESS=0x001;
	public static final int LOAD_FAIL=0x002;
	public static final int LOAD_LINEFAIL=0x003;
	public static final int LOAD_AFTER=0x004;
	Context context;
	View rootView;
	LinearLayout loadingLayout;
	LinearLayout loadingErrorLayout; 
	LinearLayout loadAfterLayout; 
	public LoadView(Context context) {
		this.context = context;
	}

	public LoadView(Context context, View rootView) {
		this.context = context;
		this.rootView = rootView;
	}
	public void initView(){
		loadingLayout=(LinearLayout)rootView.findViewById(R.id.loading);
		loadingErrorLayout=(LinearLayout)rootView.findViewById(R.id.loading_error);
		loadAfterLayout=(LinearLayout)rootView.findViewById(R.id.load_after);
	}
	public void showLoading(CallBackService service) {
		loadingLayout.setVisibility(View.VISIBLE);
		loadingErrorLayout.setVisibility(View.GONE);
	}
	public void showLoadFail(CallBackService service) {
		loadingErrorLayout.setVisibility(View.VISIBLE);
		loadingLayout.setVisibility(View.GONE);
	}
	public void showLoadAfter(CallBackService service) {
		loadAfterLayout.setVisibility(View.VISIBLE);
		loadingErrorLayout.setVisibility(View.GONE);
		loadingLayout.setVisibility(View.GONE);
	}
	public void showLoadLineFail(CallBackService service) {

	}
	
	

}
