package com.movie.app;
import android.content.Context;
import android.content.Intent;

import com.movie.system.service.NarutoService;


public class NarutoManager  {

	public static final String ACTION_LOCATION = "ACTION_LOCATION"; // 上传位置
	public static final String ACTION_DESTORY = "ACTION_DESTORY";   // 销毁service
	public static final String SERVICE_ACTION ="SERVICE_ACTION";
	public static int longitude; 								     //经度
	public static int latitude; 								     //纬度
	

	public static  void init(Context context){
		
		Intent serviceIntent  = new Intent(context, NarutoService.class);
		serviceIntent.putExtra(SERVICE_ACTION, ACTION_LOCATION);
		context.startService(serviceIntent);
	}
	
    /**
	 * 完全销毁服务
	 * @param context
	 */
    public static  void destory(Context context){
    
    	Intent serviceIntent  = new Intent(context,NarutoService.class);
		serviceIntent.putExtra(SERVICE_ACTION, ACTION_DESTORY);
		context.startService(serviceIntent);
		
	}
    
    
   
 
}