package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import com.movie.app.BaseObjectListAdapter;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.BaseBean;
import com.movie.ui.message.MessageItem;

public class ChatAdapter extends BaseObjectListAdapter {

	public ChatAdapter(NarutoApplication application, Context context,
			List<? extends BaseBean> datas) {
		super(application, context, datas);
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
