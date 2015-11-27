package com.movie.network;

import java.util.HashMap;

import android.content.Context;
import android.os.Message;

import com.movie.app.Constant;
import com.movie.app.InvokeException;
import com.movie.client.service.BaseService;
import com.movie.client.service.CallBackService;
import com.movie.state.ErrorState;
import com.movie.util.HttpUtils;

public class HttpFilmLoveService extends BaseService {

	public HttpFilmLoveService() {
		TAG="HttpFilmLoveService";
	}
	public HttpFilmLoveService(Context context) {
		TAG="HttpFilmLoveService";
		this.context=context;
	}
	@Override
	public void requestServer(CallBackService callbackService) {
		HashMap<String, Object> map=new HashMap<String, Object>();
		Message message = handler.obtainMessage();
		try {
			requestCount++;
			String sid = getSid();
			headers.put(SESSION_KEY, sid);
			Object filmId=params.get("filmId");
			Object page=params.get("page");
			Object size=params.get("size");
			StringBuilder path=new StringBuilder(Constant.FilmLove_API_URL);
			if(null!=filmId){
				path.append("/").append(filmId);
			}
			if(null!=page){
				path.append("/").append(page);
			}
			if(null!=size){
				path.append("/").append(size);
			}
			String result = HttpUtils.requestGet(path.toString(), headers);
			if (result != null) {

				try {
					map = objectMapper.readValue(result, typeReference);
				} catch (Exception e) {
					throw new InvokeException(ErrorState.ConvertJsonFasle.getState(),ErrorState.ConvertJsonFasle.getMessage());
				}
				Integer state = (Integer) map.get(Constant.ReturnCode.RETURN_STATE);
				if (state == ErrorState.Success.getState()) {
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
			map.put(Constant.ReturnCode.RETURN_STATE, e.getState());
			map.put(Constant.ReturnCode.RETURN_MESSAGE, e.getMessage());
			callbackService.ErrorCallBack(map);
		}finally{
			map.put(Constant.ReturnCode.RETURN_TAG, TAG);
		    message.getData().putSerializable(Constant.ReturnCode.RETURN_DATA, map);
			handler.sendMessage(message);
		}

	}

}
