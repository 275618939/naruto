package com.movie.network;

import java.io.File;
import java.util.HashMap;

import android.content.Context;
import android.os.Message;

import com.movie.app.Constant;
import com.movie.app.InvokeException;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.state.DynamicContentType;
import com.movie.state.ErrorState;
import com.movie.util.HttpUtils;

public class HttpDynamicCreateService extends BaseService {


	public HttpDynamicCreateService() {
		TAG="HttpDynamicCreateService";
	
	}
	public HttpDynamicCreateService(Context context) {
		this.context=context;
		TAG="HttpDynamicCreateService";
		
	}

	@Override
	public void requestServer(CallBackService callbackService) {
		HashMap<String, Object> map=new HashMap<String, Object>();
		Message message = handler.obtainMessage();
		try {
			requestCount++;
			String sid = getSid();
			headers.put(SESSION_KEY, sid);
			Object typeObject=params.get("type");
			int type=Integer.valueOf(String.valueOf(typeObject));
			String result=null;
			StringBuilder builder=new StringBuilder(Constant.Dynamic_Create_API_URL);
			builder.append("/").append(type);
			if(type==DynamicContentType.Text.getType()){
				result=HttpUtils.requestPost(builder.toString(), headers, params);
			}else if (type==DynamicContentType.Photo.getType()) {
				Object files=params.get("files");
				params.remove("files");
				result = HttpUtils.requestPostImages(builder.toString(), (File[])files, params,headers);
				files=null;
			}
			if (result != null) {
				try {
					map = objectMapper.readValue(result, typeReference);
				} catch (Exception e) {
					throw new InvokeException(ErrorState.ConvertJsonFasle.getState(),ErrorState.ConvertJsonFasle.getMessage());
				}
				Integer state = (Integer) map.get(Constant.ReturnCode.RETURN_STATE);
				if (state == ErrorState.Success.getState()) {
					message.what = SUCCESS_STATE;
				}else if(state == ErrorState.Failure.getState()){
					message.what = SUCCESS_STATE;
				} else if (state == ErrorState.SessionInvalid.getState()) {
					if (requestCount < MAXREQUEST) {
						updateSid();
						requestServer(callbackService);
					}
				} else {
					message.what = FAILE_STATE;
				}
			}else{
				throw new InvokeException(ErrorState.InvalidResource.getState(),ErrorState.InvalidResource.getMessage());
			}
	
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
