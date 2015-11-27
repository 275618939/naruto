package com.movie.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.movie.util.NetWorkUtils;
import com.movie.view.LoadView;

public abstract class BaseFragment extends Fragment {
	
	protected ProgressDialog progressDialog;
	protected Activity mActivity;
	protected Context mContext;
	protected View rootView;
	protected NetWorkUtils mNetWorkUtils;
	/**
	 * 屏幕的宽度、高度、密度
	 */
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected float mDensity;
	// 标志位，标志已经初始化完成。
	protected boolean isPrepared;
	protected boolean isVisible;
	protected LoadView loadView;
	public BaseFragment() {
		super();
	}
	public BaseFragment( Activity activity,Context context) {
		mActivity = activity;
		mContext = context;
		mNetWorkUtils = new NetWorkUtils(context);
		/**
		 * 获取屏幕宽度、高度、密度
		 */
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		loadView=new LoadView();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		initViews();
		initEvents();
		lazyLoad();	
		return rootView;
	}
	protected abstract void initViews();
	protected abstract void initEvents();
	/**在fragment可见时加载数据*/
	protected abstract void lazyLoad();
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){
			isVisible = false;
			onInvisible();		
		} else {
			isVisible = true;
			onVisible();
		}
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}
	public void showToask(String hint) {
		Toast toast = Toast.makeText(mContext, hint, Toast.LENGTH_SHORT);
		toast.show();
	}
	/** 通过Class跳转界面 **/
	protected void startActivity(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(mContext, cls);
		startActivity(intent);
	}
	public void showProgressDialog(String title, String message) {
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(mContext, title, message, true,true);
		} else if (progressDialog.isShowing()) {
			progressDialog.setTitle(title);
			progressDialog.setMessage(message);
		}
		progressDialog.show();
	}
	public void hideProgressDialog() {

		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
	protected void onVisible() {
		lazyLoad();
	}
	protected void onInvisible() {
		
	}
}
