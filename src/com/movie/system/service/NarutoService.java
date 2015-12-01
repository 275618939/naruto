package com.movie.system.service;

import com.movie.app.NarutoManager;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class NarutoService extends Service {

	final String TAG = "NarutoService";
	LocationService locationService;
	
	IBinder binder = new NarutoService.LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		locationService = new LocationService(this.getApplicationContext());
		locationService.initLocation();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String action = intent.getStringExtra(NarutoManager.SERVICE_ACTION);
		if(NarutoManager.ACTION_LOCATION.equals(action)){
			//上传位置
			locationService.start();
		}
		if(NarutoManager.ACTION_DESTORY.equals(action)){
			locationService.stop();
    		this.stopSelf();
    		android.os.Process.killProcess(android.os.Process.myPid());
    	}
		return Service.START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy() executed");
	}

	public class LocalBinder extends Binder {

		public NarutoService getService() {
			return NarutoService.this;
		}
	}

}
