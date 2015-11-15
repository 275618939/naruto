package com.movie.client.service;

import java.util.Map;

import android.content.Context;

import com.movie.app.NarutoApplication;
import com.movie.client.bean.User;
import com.movie.client.dao.BaseDao;
import com.movie.client.dao.UserDaoImple;
import com.movie.client.db.SQLHelper;

public class UserService {

	protected BaseDao userDao;
	protected Context context;

	public UserService() {
		userDao = new UserDaoImple();
	}
	public void addUser(User user) {
		userDao.deleteData(null, null);
		userDao.setContentValues(user);
		userDao.addData();
	}
	public User getUserItem() {

		Object userMap = userDao.viewData(null, null);
		User userItem = null;
		if (userMap != null&&!((Map<String, String>) userMap).isEmpty()) {
			Map<String, String> maplist = (Map<String, String>) userMap;
			userItem = new User();
			userItem.setMemberId(maplist.get(SQLHelper.MEMBERID));
		}
		return userItem;
	}

	

}
