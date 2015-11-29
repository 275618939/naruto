package com.movie.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.movie.client.bean.BaseBean;
import com.nostra13.universalimageloader.core.ImageLoader;


public class BaseObjectListAdapter extends BaseAdapter {

	protected NarutoApplication mApplication;
	protected Context mContext;
	protected Handler mHandler;
	protected LayoutInflater mInflater;
	protected ImageLoader imageLoader=ImageLoader.getInstance();
	protected List<? extends BaseBean> mDatas = new ArrayList<BaseBean>();

	public BaseObjectListAdapter(Context context,
			List<? extends BaseBean> datas) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		if (datas != null) {
			mDatas = datas;
		}
	}
	
	public BaseObjectListAdapter(Context context,Handler handler,
			List<? extends BaseBean> datas) {
		mHandler = handler;
		mContext = context;
		mInflater = LayoutInflater.from(context);
		if (datas != null) {
			mDatas = datas;
		}
	}
	
	public BaseObjectListAdapter(NarutoApplication application,Context context,
			List<? extends BaseBean> datas) {
		mApplication = application;
		mContext = context;
		mInflater = LayoutInflater.from(context);
		if (datas != null) {
			mDatas = datas;
		}
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	public List<? extends BaseBean> getDatas() {
		return mDatas;
	}

	protected void showCustomToast(String text) {
		Toast toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		toast.show();
		
//		View toastRoot = LayoutInflater.from(mContext).inflate(R.layout.common_toast, null);
//		((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
//		Toast toast = new Toast(mContext);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		toast.setDuration(Toast.LENGTH_SHORT);
//		toast.setView(toastRoot);
//		toast.show();
	}
}
