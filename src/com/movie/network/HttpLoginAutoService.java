package com.movie.network;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Message;

import com.movie.app.Constant;
import com.movie.app.InvokeException;
import com.movie.client.dao.BaseDao;
import com.movie.client.dao.LoginDaoImple;
import com.movie.client.db.SQLHelper;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.state.ErrorState;
import com.movie.util.HttpUtils;

public class HttpLoginAutoService extends BaseService {

	BaseDao loginDao;

	public HttpLoginAutoService() {
		TAG="HttpLoginAutoService";
		loginDao = new LoginDaoImple();
	}
	public HttpLoginAutoService(Context context) {
		this.context=context;
		TAG="HttpLoginAutoService";
		loginDao = new LoginDaoImple();
	}

	@Override
	public void requestServer(CallBackService callbackService) {
		HashMap<String, Object> map=new HashMap<String, Object>();
		Message message = handler.obtainMessage();
		try {
			requestCount++;
			String sid = getSid();
			Map<String, String> loginMap = loginDao.viewData(null, null);
			if (loginMap == null||loginMap.size()<0) {
				throw new InvokeException(ErrorState.ObjectNotExist.getState(),ErrorState.ObjectNotExist.getMessage());
			}
			headers.put(SESSION_KEY, sid);
			// 重新登陆，更新回话信息
			params.put("login", loginMap.get(SQLHelper.ACCOUNT));
			params.put("password", loginMap.get(SQLHelper.PASS));
		
			String result = HttpUtils.requestPost(Constant.Login_API_URL, headers, params);
			if (result != null) {
				try {
					map = objectMapper.readValue(result, typeReference);
				} catch (Exception e) {
					throw new InvokeException(ErrorState.ConvertJsonFasle.getState(),ErrorState.ConvertJsonFasle.getMessage());
				}
				Integer state = (Integer) map.get(Constant.ReturnCode.RETURN_STATE);
				if (state == ErrorState.Success.getState()) {
					map.put("login", loginMap.get(SQLHelper.ACCOUNT));
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
