package com.movie.client.dao;

import android.content.ContentValues;

import com.movie.client.bean.BaseBean;
import com.movie.client.bean.User;
import com.movie.client.db.SQLHelper;

public class UserDaoImple extends BaseDao {

	public UserDaoImple() {
		table = SQLHelper.TABLE_USER;
	}

	@Override
	public void setContentValues(BaseBean baseBean) {

		if (null != baseBean) {
			User user = (User) baseBean;
			contentValues = new ContentValues();
			contentValues.put(SQLHelper.MEMBERID, user.getMemberId());
		}

	}

}
