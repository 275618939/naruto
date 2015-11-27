package com.movie.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Message;

import com.movie.app.Constant;
import com.movie.app.InvokeException;
import com.movie.client.bean.Dictionary;
import com.movie.client.dao.BaseDao;
import com.movie.client.dao.FilmTypeDaoImple;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.state.ErrorState;
import com.movie.util.HttpUtils;

public class HttpFilmTypeService  extends  BaseService{

	BaseDao filmTypeDao;
	public HttpFilmTypeService() {
		TAG="HttpFilmTypeService";
		filmTypeDao = new FilmTypeDaoImple();
	}
	public HttpFilmTypeService(Context context) {
		TAG="HttpFilmTypeService";
		this.context=context;
		filmTypeDao = new FilmTypeDaoImple();
	}
	
	@Override
	public void requestServer(CallBackService callbackService) {
		HashMap<String, Object> map=new HashMap<String, Object>();
		Message message = handler.obtainMessage();
		try {
			requestCount++;
			String sid= getSid();
			headers.put(SESSION_KEY, sid);
			int count = filmTypeDao.countData(null);
			if(count>0){
				throw new InvokeException(ErrorState.Success.getState(),ErrorState.Success.getMessage());
			}
			String result  = HttpUtils.requestGet(Constant.Dic_FilmType_API_URL,headers);
			if (result != null) {		
				
				try {
					map = objectMapper.readValue(result, typeReference);
				} catch (Exception e) {
					throw new InvokeException(ErrorState.ConvertJsonFasle.getState(),ErrorState.ConvertJsonFasle.getMessage());
			    }
				Integer state = (Integer) map.get(Constant.ReturnCode.RETURN_STATE);
				if (state==ErrorState.Success.getState()) {
					List<HashMap<String, String>> value = (ArrayList<HashMap<String, String>>) map.get(Constant.ReturnCode.RETURN_VALUE);
					String key=null;
					String data= null;
					Dictionary dictionary = null;
					int size=value.size();
					Map<String, String> filmTypeMap=null;
					for(int i=0;i<size;i++){
						 filmTypeMap=value.get(i);
						 data=filmTypeMap.get("name");
						 dictionary = new Dictionary();
						 dictionary.setId(Integer.parseInt(String.valueOf(filmTypeMap.get("id"))));
						 dictionary.setName(data);
						 filmTypeDao.setContentValues(dictionary);
						 filmTypeDao.addData();
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
