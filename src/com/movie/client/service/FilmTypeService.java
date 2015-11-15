package com.movie.client.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.movie.client.bean.Dictionary;
import com.movie.client.dao.BaseDao;
import com.movie.client.dao.FilmTypeDaoImple;
import com.movie.client.db.SQLHelper;


public class FilmTypeService   {

	protected BaseDao filmTypeDao;
	protected Context context;

	public FilmTypeService() {
		filmTypeDao = new FilmTypeDaoImple();
	}

	public void saveHobby(int id, String name) {
		Dictionary hobby = new Dictionary(id, name);
		filmTypeDao.setContentValues(hobby);
		filmTypeDao.addData();
	}

	public Map<Integer,String> getFilmTypeMap() {

		List<Map<String, String>> list = filmTypeDao.listData(null, null);
		Map<Integer,String> hobbies = new HashMap<Integer, String>();
		for (Map<String, String> map : list) {
			hobbies.put(Integer.parseInt(map.get(SQLHelper.ID)),map.get(SQLHelper.NAME));
		}
		return hobbies;
	}

	public int countFilmType() {
		return filmTypeDao.countData(null);
	}

}
