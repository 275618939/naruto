package com.movie.network;

import java.util.List;
import java.util.Map;

public interface HttpAPIResponser {
	
	public void onSuccess(Object data, List<?> list, Page page, String code,String url);

	public void onFailed(Exception e);

	public Map<String, Object> getRequestParams();
	
	public String getSid();

	public void onRequest();
}
