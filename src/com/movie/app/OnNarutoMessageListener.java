package com.movie.app;

import android.net.NetworkInfo;

/**
 * CIM 主要事件接口 类名称：OnCIMMessageListener 类描述： 修改备注：
 * 
 * @version 1.0.0
 * 
 */
public interface OnNarutoMessageListener {

	/**
	 * 当手机网络发生变化时调用
	 * 
	 * @param networkinfo
	 */
	public abstract void onNetworkChanged(NetworkInfo networkinfo);

	/**
	 * 获取到是否连接到服务端 通过调用CIMPushManager.detectIsConnected()来异步获取
	 * 
	 */
	public abstract void onConnectionStatus(boolean isConnected);

	/**
	 * 连接服务端成功
	 */
	public abstract void onConnectionSucceed();

	/**
	 * 连接断开
	 */
	public abstract void onConnectionClosed();
}
