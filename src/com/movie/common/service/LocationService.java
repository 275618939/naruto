package com.movie.common.service;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;

public class LocationService implements BDLocationListener {

	LocationClient mLocClient;
	LocationClientOption option;
	Context mContext;
	BaseService mHttpService;
	CallBackService mCallBackService;
	public LocationService(){}
	public LocationService(Context context){
		mContext=context;
		
	}
	public void initLocation() {
		mLocClient=new LocationClient(mContext);
		option = new LocationClientOption();
		//option.setOpenGps(true);// 打开gps
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置高精度定位定位模式
		option.setCoorType("bd09ll"); //设置百度经纬度坐标系格式
		option.setScanSpan(1000);//设置发起定位请求的间隔时间为1000ms
		option.setIsNeedAddress(true);//反编译获得具体位置，只有网络定位才可以
		mLocClient.setLocOption(option);
		mLocClient.registerLocationListener(this);
	}
	public void start(BaseService httpService,CallBackService callBackService ){
		mHttpService=httpService;
		mCallBackService=callBackService;
		mLocClient.start(); 
	}
	public void stop(){
		mLocClient.stop();
	}
	
	@Override
	public void onReceiveLocation(BDLocation location) {
		
		double longitude=location.getLongitude();  //纬度
		double latitude=location.getLatitude();    //经度
		if(null!=mHttpService){
			mHttpService.addParams("longitude",(int)(longitude*100000));
			mHttpService.addParams("latitude",(int) (latitude*100000));
			mHttpService.execute(mCallBackService);
		}
	}
	

}
