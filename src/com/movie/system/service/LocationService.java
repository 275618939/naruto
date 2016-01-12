package com.movie.system.service;

import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.movie.app.Constant;
import com.movie.app.NarutoApplication;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.network.HttpLocationService;

public class LocationService implements BDLocationListener,CallBackService {

	LocationClient mLocClient;
	LocationClientOption option;
	CallBackService callBackService;
	BaseService httpLocationService;
	Context mContext;
	boolean upload;

	
	public LocationService(Context context){
		httpLocationService=new HttpLocationService(context);
		mContext=context;
	}
	public void initLocation(int scanSpan) {
		mLocClient=new LocationClient(mContext);
		option = new LocationClientOption();
		//option.setOpenGps(true);// 打开gps
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置高精度定位定位模式
		option.setCoorType("bd09ll"); //设置百度经纬度坐标系格式
		option.setScanSpan(scanSpan);//设置发起定位请求的间隔时间
		option.setIsNeedAddress(true);//反编译获得具体位置，只有网络定位才可以
		mLocClient.setLocOption(option);
		mLocClient.registerLocationListener(this);
	}
	public void start(){
		mLocClient.start(); 
	}
	public void start(boolean upload){
		mLocClient.start(); 
		this.upload=upload;
	}
	public void stop(){
		mLocClient.stop();
	}
	
	@Override
	public void onReceiveLocation(BDLocation location) {
		
		double longitude=location.getLongitude();  //纬度
		double latitude=location.getLatitude();    //经度
		NarutoApplication.city=location.getCity();
		NarutoApplication.address=location.getAddrStr();
		NarutoApplication.longitude=(int)(longitude*100000);
		NarutoApplication.latitude=(int)(latitude*100000);
		Log.i("-location-", "定位:"+"["+NarutoApplication.longitude+","+NarutoApplication.latitude+"]");
		if(upload){
			upload=false;
			httpLocationService.addParams("longitude", NarutoApplication.longitude);
			httpLocationService.addParams("latitude", NarutoApplication.latitude);
			httpLocationService.execute(this);
		}
	}
	@Override
	public void SuccessCallBack(Map<String, Object> map) {
		
		String code=map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			Log.i("-location-", "success:定位上传成功!");
		}else{
			String message=map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			Log.i("-location-", "error:"+message);
		}
		map=null;
	}
	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		
		
	}
	@Override
	public void OnRequest() {
		
		
	}
	

}
