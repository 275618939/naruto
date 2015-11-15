package com.movie.network;


public interface HttpResponse {
	
	public void onSuccess(Object data);

	public void onFailed(Object data);

	public void onRequest();
}
