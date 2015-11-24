package com.movie.client.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.movie.app.SexState;
import com.movie.client.bean.Dictionary;
import com.movie.client.dao.BaseDao;
import com.movie.client.dao.CommentDaoImple;
import com.movie.client.db.SQLHelper;

public class CommentService {

	protected BaseDao commentDao;
	protected Context context;

	public CommentService() {
		commentDao = new CommentDaoImple();
	}

	public void saveCommnet(int id, String name) {
		Dictionary comment = new Dictionary(id, name);
		commentDao.setContentValues(comment);
		commentDao.addData();
	}

	public Map<Integer, Map<Integer,String>> getCommentsMap() {

		List<Map<String, String>> commnetList = commentDao.listData(null, null);
		Map<Integer,String> womenDictionaries = new HashMap<Integer,String>();
	    Map<Integer,String> menDictionaries = new HashMap<Integer,String>();
	    Map<Integer, Map<Integer,String>> commentsMap = new HashMap<Integer, Map<Integer,String>>();
	    int type=0;
		for (Map<String, String> map : commnetList) {
			type=Integer.parseInt(map.get(SQLHelper.TYPE));
			if(type==SexState.MAN.getState()){
				menDictionaries.put(Integer.parseInt(map.get(SQLHelper.ID)),map.get(SQLHelper.NAME));
			}else{
				womenDictionaries.put(Integer.parseInt(map.get(SQLHelper.ID)),map.get(SQLHelper.NAME));	
			}
		}
		commentsMap.put(SexState.MAN.getState(), menDictionaries);
		commentsMap.put(SexState.WOMAN.getState(), womenDictionaries);
		return commentsMap;
	}

	public int countHobby() {
		return commentDao.countData(null);
	}

}