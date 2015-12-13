package com.movie.system.service;

import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;

import com.movie.ui.MainActivity;

public class DoubleClickExitHelper {
	private final MainActivity mActivity;
	private boolean isOnKeyBacking;
	private Handler mHandler;
	private Toast mBackToast;

	public DoubleClickExitHelper(MainActivity activity) {
		mActivity = activity;
		mHandler = new Handler(Looper.getMainLooper());
	}


	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode != KeyEvent.KEYCODE_BACK) {
			return false;
		}
		if (isOnKeyBacking) {
			mHandler.removeCallbacks(onBackTimeRunnable);
			if (mBackToast != null) {
				mBackToast.cancel();
			}
			mActivity.getLocationService().stop();
			mActivity.setLocationService(null);
			mActivity.finish();
			System.gc();
			return true;
		} else {
			isOnKeyBacking = true;
			if (mBackToast == null) {
				mBackToast = Toast.makeText(mActivity, "再按一次退出程序", 2000);
			}
			mBackToast.show();
			// 延迟两秒的时间，把Runable发出去
			mHandler.postDelayed(onBackTimeRunnable, 2000);
			return true;
		}
	}

	private Runnable onBackTimeRunnable = new Runnable() {

		@Override
		public void run() {
			isOnKeyBacking = false;
			if (mBackToast != null) {
				mBackToast.cancel();
			}
		}
	};
}
