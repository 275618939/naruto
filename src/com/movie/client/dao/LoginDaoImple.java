package com.movie.client.dao;

import android.content.ContentValues;

import com.movie.client.bean.BaseBean;
import com.movie.client.bean.Login;
import com.movie.client.db.SQLHelper;

public class LoginDaoImple extends BaseDao {

	public LoginDaoImple() {
		table = SQLHelper.TABLE_LOGIN;
	}

	@Override
	public void setContentValues(BaseBean baseBean) {

		if (null != baseBean) {
			Login login = (Login) baseBean;
			contentValues = new ContentValues();
			contentValues.put(SQLHelper.ACCOUNT, login.getAccount());
			contentValues.put(SQLHelper.PASS, login.getPass());
		}
	}
	
}
