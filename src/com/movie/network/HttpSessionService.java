package com.movie.network;

import java.util.HashMap;

import android.content.Context;
import android.os.Message;

import com.movie.app.Constant;
import com.movie.app.InvokeException;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;

public class HttpSessionService extends BaseService {

	public HttpSessionService(){
		TAG="HttpSessionService";
	}
	public HttpSessionService(Context context) {
		this.context=context;
		TAG="HttpSessionService";
	}
	@Override
	public void requestServer(CallBackService callbackService) {
		HashMap<String, Object> map=new HashMap<String, Object>();
		Message message = handler.obtainMessage();
	    try {
	    	 String sid=updateSid();
			 map.put(SESSION_KEY, sid);
			 if(sid!=null){
				 message.what = SUCCESS_STATE;
			 }else{
				 message.what = FAILE_STATE;
			 }
			 map.put(Constant.ReturnCode.RETURN_STATE, SUCCESS_STATE);
			 map.put(Constant.ReturnCode.RETURN_VALUE, sid);
		}catch (InvokeException e) {
			 message.what = FAILE_STATE;
			 map.put(Constant.ReturnCode.RETURN_STATE, e.getState());
			 map.put(Constant.ReturnCode.RETURN_MESSAGE, e.getMessage());
			
		}finally{
			map.put(Constant.ReturnCode.RETURN_TAG, TAG);
		    message.getData().putSerializable(Constant.ReturnCode.RETURN_DATA, map);
			handler.sendMessage(message);
		}
		
		
		
	}



}
