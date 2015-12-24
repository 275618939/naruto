package com.movie.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Message;

import com.movie.app.Constant;
import com.movie.app.InvokeException;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.client.service.CommentService;
import com.movie.state.ErrorState;
import com.movie.util.HttpUtils;

public class HttpCommentService  extends  BaseService{

	CommentService commentService;
	public HttpCommentService() {
		TAG="HttpCommentService";
		commentService = new CommentService();
	}
	public HttpCommentService(Context context) {
		TAG="HttpCommentService";
		this.context=context;
		commentService = new CommentService();
	}
	
	@Override
	public void requestServer(CallBackService callbackService) {
		HashMap<String, Object> map=new HashMap<String, Object>();
		Message message = handler.obtainMessage();
		try {
			requestCount++;
			
			String sid= getSid();
			Object type=params.get("type");
			commentService.deleteCommnetByType(String.valueOf(type));
			headers.put(SESSION_KEY, sid);
			StringBuilder builder=new StringBuilder(Constant.Dic_Comment_API_URL);
			builder.append("/").append(type);
			String result  = HttpUtils.requestGet(builder.toString(),headers);
			if (result != null) {		
				try {
					map = objectMapper.readValue(result, typeReference);
				} catch (Exception e) {
					throw new InvokeException(ErrorState.ConvertJsonFasle.getState(),ErrorState.ConvertJsonFasle.getMessage());
			    }
				Integer state = (Integer) map.get(Constant.ReturnCode.RETURN_STATE);
				if (state==ErrorState.Success.getState()) {
					List<HashMap<String, String>> value = (ArrayList<HashMap<String, String>>) map.get(Constant.ReturnCode.RETURN_VALUE);
					Integer key=null;
					String data= null;
					int size=value.size();
					Map<String, String> commentMap=null;
					for(int i=0;i<size;i++){
						 commentMap=value.get(i);
						 key=Integer.valueOf(String.valueOf(commentMap.get("id")));
					     data=commentMap.get("name");
						 commentService.saveCommnet(key.intValue(),Integer.valueOf(String.valueOf(type)),data);
				    }
					value=null;
					message.what = SUCCESS_STATE;
				}else if(state==ErrorState.SessionInvalid.getState()){
					if(requestCount<MAXREQUEST){
						updateSid();
						requestServer(callbackService);
					}
				}else{
					message.what = FAILE_STATE;
				}
			}else{
				throw new InvokeException(ErrorState.InvalidResource.getState(),ErrorState.InvalidResource.getMessage());
			}
		} catch (InvokeException e) {
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
