package com.movie.client.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.movie.client.bean.Dictionary;
import com.movie.client.dao.BaseDao;
import com.movie.client.dao.HobbyDaoImple;
import com.movie.client.db.SQLHelper;


public class HobbyService   {

	protected BaseDao hobbyDao;
	protected Context context;

	public HobbyService() {
		hobbyDao = new HobbyDaoImple();
	}

	public void saveHobby(int id, String name) {
		Dictionary hobby = new Dictionary(id, name);
		hobbyDao.setContentValues(hobby);
		hobbyDao.addData();
	}

	public Map<Integer,String> getHobbyMap() {

		List<Map<String, String>> list = hobbyDao.listData(null, null);
		Map<Integer,String> hobbies = new HashMap<Integer, String>();
		for (Map<String, String> map : list) {
			hobbies.put(Integer.parseInt(map.get(SQLHelper.ID)),map.get(SQLHelper.NAME));
		}
		return hobbies;
	}
	public int countHobby() {
		return hobbyDao.countData(null);
	}

}
