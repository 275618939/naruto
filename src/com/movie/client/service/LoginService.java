package com.movie.client.service;

import java.util.Map;

import com.movie.client.bean.Login;
import com.movie.client.dao.BaseDao;
import com.movie.client.dao.LoginDaoImple;
import com.movie.client.db.SQLHelper;


public class LoginService  {

	protected BaseDao loginDao;

	public LoginService() {
		loginDao = new LoginDaoImple();
	}
	public void addLogin(Login login) {

		loginDao.deleteData(null, null);
		loginDao.setContentValues(login);
		loginDao.addData();
		
	}
	public Login getLogin() {

		Map<String, String> valuesMap = loginDao.viewData(null, null);
		Login login = null;
		if (valuesMap != null && !((Map<String, String>) valuesMap).isEmpty()) {
			login = new Login(valuesMap.get(SQLHelper.ACCOUNT),valuesMap.get(SQLHelper.PASS));
		}
		return login;
	}

	public void deleteLogin() {
		loginDao.deleteData(null, null);
	}

}
