package com.movie.client.dao;

import android.content.ContentValues;

import com.movie.client.bean.BaseBean;
import com.movie.client.bean.Session;
import com.movie.client.db.SQLHelper;

public class SessionDaoImple extends BaseDao {

	public SessionDaoImple() {
		table = SQLHelper.TABLE_SESSION;
	}

	@Override
	public void setContentValues(BaseBean baseBean) {
		if (null != baseBean) {
			Session session = (Session) baseBean;
			contentValues = new ContentValues();
			contentValues.put(SQLHelper.SID, session.getSid());
		}

	}

}
