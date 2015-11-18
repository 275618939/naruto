package com.movie.client.service;

import java.util.Map;

public interface CallBackService {
	public void SuccessCallBack(Map<String, Object> map);
	public void ErrorCallBack(Map<String, Object> map);
	public void OnRequest();
	
}
