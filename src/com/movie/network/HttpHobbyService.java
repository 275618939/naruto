package com.movie.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Message;

import com.movie.app.Constant;
import com.movie.app.InvokeException;
import com.movie.client.bean.Dictionary;
import com.movie.client.dao.BaseDao;
import com.movie.client.dao.HobbyDaoImple;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.state.ErrorState;
import com.movie.util.HttpUtils;

public class HttpHobbyService extends BaseService {

	BaseDao hobbyDao;
	public HttpHobbyService() {
		TAG="HttpHobbyService";
		hobbyDao = new HobbyDaoImple();
	}
	public HttpHobbyService(Context context) {
		this.context=context;
		TAG="HttpHobbyService";
		hobbyDao = new HobbyDaoImple();
	}

	@Override
	public void requestServer(CallBackService callbackService) {
		HashMap<String, Object> map=new HashMap<String, Object>();
		Message message = handler.obtainMessage();
		try {
			requestCount++;
			int count = hobbyDao.countData(null);
			if(count>0){
				throw new InvokeException(ErrorState.Success.getState(),ErrorState.Success.getMessage());
			}
			String sid= getSid();
			headers.put(SESSION_KEY, sid);
			String result = HttpUtils.requestGet(Constant.Hobby_API_URL, headers);
			if (result != null) {

				try {
					map = objectMapper.readValue(result, typeReference);
				} catch (Exception e) {
					throw new InvokeException(ErrorState.ConvertJsonFasle.getState(),ErrorState.ConvertJsonFasle.getMessage());
				}
				Integer state = (Integer) map.get(Constant.ReturnCode.RETURN_STATE);
				if (state == ErrorState.Success.getState()) {
					List<HashMap<String, String>> value = (ArrayList<HashMap<String, String>>) map.get(Constant.ReturnCode.RETURN_VALUE);
					String data= null;
					Dictionary hobby = null;
					int size=value.size();
					Map<String, String> hobbyMap=null;
					for(int i=0;i<size;i++){
						 hobbyMap=value.get(i);
					     data=hobbyMap.get("name");
						 hobby = new Dictionary();
						 hobby.setId(Integer.parseInt(String.valueOf(hobbyMap.get("id"))));
						 hobby.setName(data);
						 hobbyDao.setContentValues(hobby);
						 hobbyDao.addData();
					  }
					
					/*for (Map.Entry<String, String> entry : value.entrySet()) {
						hobby = new Hobby();
						hobby.setId(Integer.parseInt(entry.getKey()));
						hobby.setName(entry.getValue());
						hobbyDao.setContentValues(hobby);
						hobbyDao.addData();
					}*/
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
