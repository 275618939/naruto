package com.movie.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Message;

import com.movie.app.Constant;
import com.movie.app.ErrorState;
import com.movie.app.InvokeException;
import com.movie.client.bean.Dictionary;
import com.movie.client.dao.BaseDao;
import com.movie.client.dao.CommentDaoImple;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.util.HttpUtils;

public class HttpCommentService  extends  BaseService{

	BaseDao commentDao;
	public HttpCommentService() {
		TAG="HttpCommentService";
		commentDao = new CommentDaoImple();
	}
	public HttpCommentService(Context context) {
		TAG="HttpCommentService";
		this.context=context;
		commentDao = new CommentDaoImple();
	}
	
	@Override
	public void requestServer(CallBackService callbackService) {
		HashMap<String, Object> map=new HashMap<String, Object>();
		Message message = handler.obtainMessage();
		try {
			requestCount++;
			int count = commentDao.countData(null);
			if(count>0){
				throw new InvokeException(ErrorState.Success.getState(),ErrorState.Success.getMessage());
			}
			String sid= getSid();
			Object type=params.get("type");
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
					Dictionary comment = null;
					int size=value.size();
					Map<String, String> commentMap=null;
					for(int i=0;i<size;i++){
						 commentMap=value.get(i);
						 key=Integer.valueOf(String.valueOf(commentMap.get("id")));
					     data=commentMap.get("name");
						 comment = new Dictionary();
						 comment.setId(key.intValue());
						 comment.setType(Integer.valueOf(String.valueOf(type)));
						 comment.setName(data);
						 commentDao.setContentValues(comment);
						 commentDao.addData();
				    }
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
