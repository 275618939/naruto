package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.movie.app.BaseObjectListAdapter;
import com.movie.client.bean.BaseBean;
import com.movie.client.bean.Message;
import com.movie.ui.message.MessageItem;

public class ChatAdapter extends BaseObjectListAdapter {

	public ChatAdapter(Context context,
			List<? extends BaseBean> datas) {
		super(context, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message msg = (Message) getItem(position);
		MessageItem messageItem = MessageItem.getInstance(msg, mContext);
		messageItem.fillContent();
		View view = messageItem.getRootView();
		return view;
	}
}
